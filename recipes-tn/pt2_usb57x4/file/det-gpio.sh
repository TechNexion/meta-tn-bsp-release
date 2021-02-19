#!/bin/bash

# detect the gpio input in a loop
# exit when gpio input becomes true

LOCK="/tmp/power-cycle.pid"
VID=0x0424
PID=0x2734
DID=$1
PORT=$2
PROCID=$3
DET_STATE=0
DET_COUNT=0

set_gpio_num () {
	# ARG1: Number matching to USB Port Number
	case "$PORT" in
	1)
		GPIONUM=11
		;;
	2)
		GPIONUM=10
		;;
	3)
		GPIONUM=3
		;;
	4)
		GPIONUM=2
		;;
	*)
		echo "GPIO detect Error"
		;;
	esac
}

read_gpio () {
	# reading GPIONUM only
	flock -x 999
	RESULT=$(/usr/sbin/gpio $VID $PID 0x00 $GPIONUM)
	flock -u 999
}

detect_gpio_low () {
	read_gpio
	if grep -q "Pin State is 0" <<< $RESULT; then
		return 0
	fi
	return 1
}

detect_gpio_high () {
	read_gpio
	if grep -q "Pin State is 1" <<< $RESULT; then
		return 0
	fi
	return 1
}

state_change () {
	# figure out PWM's regular pattern here
	# e.g. must detect state change L->H 30 times (during PWM)
	if [ "$DET_STATE" -eq 1 ]; then
		DET_COUNT=$(( $DET_COUNT+1 ))
	fi
	if [ "$DET_COUNT" -eq 30 ]; then
		echo "detected" > /tmp/usb$PORT
		if [ -n "$PROCID" ]; then
			kill -USR1 $PROCID
		fi
		exit 0
	fi
}

#
# main
#
# ARG1: USB Port Number
# ARG2: Parents PID for kill -USR1
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
	echo "Command Syntax:"
	echo "	det_gpio [DID] [USB_PORT] [PROC_PID]"
	echo "DID = Device ID from lsusb"
	echo "USB_PORT = 1, 2, 3, 4"
	echo "PROC_PID = Process ID to send signal USR1 (i.e. kill -10)"
	exit 1
fi

trap state_change USR2
exec 999>$LOCK
rm -f /tmp/usb$PORT
set_gpio_num
while true
do
	usleep 500
	if [ "$DET_STATE" -eq 0 ]; then
		if detect_gpio_high; then
			DET_STATE=1
			kill -USR2 $$
		fi
	elif [ "$DET_STATE" -eq 1 ]; then
		if detect_gpio_low; then
			DET_STATE=0
			kill -USR2 $$
		fi
	fi
done

