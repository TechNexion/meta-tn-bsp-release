#!/bin/bash

#
# Power ON/OFF scripts
# utilises microchip pt2-usb57x4 controller for testing
# available commands:
# Format:
#     cmd VID PID read/write [DID] [[GPIO#] [REGADDR]] [data_len] [data_value]
#
# GPIO:
#    GPIO#: 2, 3, 10, 11
#     READ: gpio 0x0424 0x2734 [DID] 0x00 2
#    WRITE: gpio 0x0424 0x2734 [DID] 0x01 3 0x01/0x00 (ON/OFF)
#
# POWER:
#  REGADDR: 0x3c00, 0x3c04, 0x3c08, 0x3c0c (USB Port 1, 2, 3, 4)
#     READ: register_rw 0x0424 0x2734 [DID] 0x00 0x3c00 1
#    WRITE: register_rw 0x0424 0x2734 [DID] 0x01 0x3c08 1 0x93/0x90 (ON/OFF)
#

DID=$1
NUM=$2
REPEAT=$3
LOCK="/tmp/power-cycle.pid"
TODAY="$(date -u +%Y%m%d-%H%M%S)"
LOG=/var/log/power-cycle-$TODAY-$NUM.log
VID=0x0424
PID=0x2734
REGADDR=0x3c00
ONOFF_STAT=0x00
DET_PID=""
LOOP_COUNT=0
SUCC_COUNT=0
FAIL_COUNT=0

help_msg () {
	echo "Command Syntax:"
	echo "	power-cycle.sh [DID] [usb_num] [repeat_count]"
	echo "-DID = device id from lsusb"
	echo "-usb_num = 1, 2, 3, 4"
	echo "-repeat_count > 0"
}

time_lapsed () {
	end_time="$(date -u +%s)"
	elapsed="$(($end_time-$start_time))"
	total="$(($end_time-$init_time))"
}

dump_log () {
	echo "" >> $LOG
	echo "Loop Count: $LOOP_COUNT" >> $LOG
	echo "Success Count: $SUCC_COUNT" >> $LOG
	echo "Failure Count: $FAIL_COUNT" >> $LOG
	printf "Elapsed: %02d:%02d\n" $(($elapsed/60)) $(($elapsed%60)) >> $LOG
	printf "Total time: %02d:%02d\n" $(($total/60)) $(($total%60)) >> $LOG
}

set_gpio_led () {
	# ARG1: LED GPIO Number matching to USB Port Number
	case "$NUM" in
	1)
		GPIOLED=4
		;;
	2)
		GPIOLED=1
		;;
	3)
		GPIOLED=5
		;;
	4)
		GPIOLED=7
		;;
	*)
		echo "GPIO Led Pin Error"
		;;
	esac
}

led_onoff () {
	# ARG1: On/Off
	case "$1" in
	on)
		flock -x 999
		STAT=$(/usr/sbin/gpio $VID $PID $DID 0x01 $GPIOLED 0x01)
		flock -u 999
		;;
	off)
		flock -x 999
		STAT=$(/usr/sbin/gpio $VID $PID $DID 0x01 $GPIOLED 0x00)
		flock -u 999
		;;
	*)
		;;
	esac
}

set_addr () {
	# ARG1: Port Number
	case "$NUM" in
	1)
		REGADDR=0x3c00
		;;
	2)
		REGADDR=0x3c04
		;;
	3)
		REGADDR=0x3c08
		;;
	4)
		REGADDR=0x3c0c
		;;
	*)
		echo -e "Power On/Off Error: Port Number not specified"
		;;
	esac
}

power_stat () {
	flock -x 999
	STAT=$(register_rw $VID $PID $DID 0x00 $REGADDR 1 | grep "Register Read value is" | sed -E 's/(.*is\s(0x[0-9a-fA-F][0-9a-fA-F])\s.*)/\2/')
	flock -u 999
	ONOFF_STAT=$(( STAT & 0xff ))
}

power_onoff () {
	# ARG1: On/Off
	case "$1" in
	on)
		VAL=$(( ONOFF_STAT | 0x03 ))
		flock -x 999
		RESULT=$(register_rw $VID $PID $DID 0x01 $REGADDR 1 $VAL)
		flock -u 999
		;;
	off)
		VAL=$(( ONOFF_STAT & 0xF0 ))
		flock -x 999
		RESULT=$(register_rw $VID $PID $DID 0x01 $REGADDR 1 $VAL)
		flock -u 999
		;;
	*)
		;;
	esac
}

start_usb () {
	# ARG1: Port number
	# start the usb power cycle tests
	set_gpio_led
	led_onoff off
	set_addr
	power_stat
	power_onoff off
	sleep 3
	power_onoff on
	# takes about 20 seconds to go into rootfs, start det-gpio after 10s, as there
	# are some issue with emmc not found at boot and and gpio is pulled high (by default)
	sleep 10
	det-gpio.sh $DID $NUM $$ & DET_PID="$!"
}

#
# main entry
#
if [ -z "$1" ] || [ "$1" -lt 0 ] || [ "$2" -lt 1 ] || [ "$2" -gt 4 ] || [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
	help_msg
	exit 1
fi

if [ -n "$2" ] && [ -n "$3" ] && [ "$3" -lt 0 ]; then
	help_msg
	exit 1
fi

detected_gpio () {
	echo "detected gpio from USB$NUM (DID:$DID)"
}

trap detected_gpio USR1

init_time="$(date -u +%s)"
echo "===== DATE: $TODAY =====" >> $LOG
echo "power cycle test on USB$NUM (DID:$DID)" >> $LOG

exec 999>$LOCK
while true; do
	start_time="$(date -u +%s)"
	LOOP_COUNT=$(( $LOOP_COUNT + 1 ))
	start_usb

	#wait for 3 minute
	sleep 180 &
	wait $!
	# check if detected gpios every 3 minutes
	if ps -p $DET_PID 2>&1 > /dev/null; then
		kill -KILL $DET_PID
	fi
	if [ -f /tmp/usb$NUM ]; then
		SUCC_COUNT=$(( $SUCC_COUNT + 1 ))
	else
		FAIL_COUNT=$(( $FAIL_COUNT + 1 ))
		led_onoff on
		exit 1
	fi
	time_lapsed
	dump_log
	if [ -n "$REPEAT" ] && [ "$REPEAT" -gt 0 ] && [ "$REPEAT" -eq "$LOOP_COUNT" ]; then
		exit 0
	fi
done

