#!/bin/bash
#
# USB Utility Scripts
#
# utilises microchip pt2-usb57x4 controller for testing
# available commands from pt2-usb57x4 utilities:
# Format:
#     cmd VID PID DID read/write [[GPIO#] [REGADDR] [SLVADDR CLKRATE CMDREG]] [[data_len] data_value]
#
# GPIO:
#      cmd: gpio
#    GPIO#: 2, 3, 10, 11
#
#     READ: gpio 0x0424 0x2734 [DID] 0x00 2
#    WRITE: gpio 0x0424 0x2734 [DID] 0x01 3 0x01/0x00 (ON/OFF)
#
# POWER:
#      cmd: register_rw
#  REGADDR: 0x3c00, 0x3c04, 0x3c08, 0x3c0c (USB Port 1, 2, 3, 4)
#
#     READ: register_rw 0x0424 0x2734 [DID] 0x00 0x3c00 1
#    WRITE: register_rw 0x0424 0x2734 [DID] 0x01 0x3c08 1 0x93/0x90 (ON/OFF)
#
# BOOTMODE:
#      cmd: i2cBridging
#  SLVADDR: 0x20
#  CLKRATE: 1
#  CMDREG: 0x00 0x01 0x02 0x03 (0x00: Input Port, 0x01: Output Port, 0x02: Polarity Inversion, 0x03: I/O Configuration)
#
#     READ: i2cBridging 0x0424 0x2734 [DID] 0x00 0x20 1 0x00 1
#    WRITE: i2cBridging 0x0424 0x2734 [DID] 0x01 0x20 1 0x03 1 0x00
#

DID=$1
CMD=$2
NUM=$3
LOCK="/tmp/pt2usb-util.pid"
VID=0x0424
PID=0x2734
SLVADDR=0x20
CLKRATE=1
CMDREG=0x00
REGADDR=0x3c00
ONOFF_STAT=0x00

help_msg () {
	echo "Command Syntax:"
	echo "pt2usb-util.sh DID cmd pnum|-h|--help [on|off [on|off]]"
	echo "    -DID = device id from lsusb and must match VID:PID(0424:2734)"
	echo "           or hub path from lsusb or /dev/filename from udev rules"
	echo "    -cmd = gpio, power, btmode"
	echo "    -pnum = 1, 2, 3, 4 (also 5, 7, 10, 11 for gpio)"
	echo "    -on|off = on, off, (btmode: takes 2 sets of on|off values, on->left off->right)"
	echo "More info on commands:"
	echo "    pt2usb-util.sh DID cmd -h"
	echo "or  pt2usb-util.sh DID cmd --help"
}

set_gpio_port () {
	case $NUM in
	1) return 0 ;;
	2) return 0 ;;
	3) return 0 ;;
	4) return 0 ;;
	5) return 0 ;;
	7) return 0 ;;
	10) return 0 ;;
	11) return 0 ;;
	-h | --help)
		echo "gpio command:"
		echo "pt2usb-util.sh DID gpio pnum [on|off]"
		echo "    -pnum = pin numbers are 1, 2, 3, 4, 5, 7, 10, 11"
		echo "    -on|off = on, off"
		;;
	*) ;;
	esac
	return 1
}

gpio_stat () {
	flock -x 999
	$(gpio $VID $PID $DEVID 0x00 $NUM | grep -q "Pin State is 0") && STAT="off" || STAT="on"
	flock -u 999
}

gpio_onoff () {
	case "$VALUE1" in
	on)
		flock -x 999
		STAT=$(gpio $VID $PID $DEVID 0x01 $NUM 0x01)
		flock -u 999
		;;
	off)
		flock -x 999
		STAT=$(gpio $VID $PID $DEVID 0x01 $NUM 0x00)
		flock -u 999
		;;
	"")
		;;
	*)
		echo -e "GPIO On/Off Error: Please specify on or off"
		;;
	esac
}

set_power_addr () {
	case "$NUM" in
	1)
		REGADDR=0x3c00
		return 0
		;;
	2)
		REGADDR=0x3c04
		return 0
		;;
	3)
		REGADDR=0x3c08
		return 0
		;;
	4)
		REGADDR=0x3c0c
		return 0
		;;
	-h | --help)
		echo "power command:"
		echo "pt2usb-util.sh DID power pnum [on|off]"
		echo "    -pnum = port numbers are 1, 2, 3, 4"
		echo "    -on|off = on, off"
		;;
	*)
		;;
	esac
	return 1
}

power_stat () {
	flock -x 999
	STAT=$(register_rw $VID $PID $DEVID 0x00 $REGADDR 1 | grep "Register Read value is" | sed -E 's/(.*is\s(0x[0-9a-fA-F][0-9a-fA-F])\s.*)/\2/')
	flock -u 999
	ONOFF_STAT=$(( STAT & 0xff ))
}

power_onoff () {
	power_stat
	case "$VALUE1" in
	on)
		VAL=$(( ONOFF_STAT | 0x03 ))
		flock -x 999
		RESULT=$(register_rw $VID $PID $DEVID 0x01 $REGADDR 1 $VAL)
		flock -u 999
		;;
	off)
		VAL=$(( ONOFF_STAT & 0xF0 ))
		flock -x 999
		RESULT=$(register_rw $VID $PID $DEVID 0x01 $REGADDR 1 $VAL)
		flock -u 999
		;;
	*)
		;;
	esac
}

init_btmode_out () {
	# initialize via i2c bridge to set PCA9554BS as outputs
	CMDREG=0x03
	flock -x 999
	# sends 2 bytes of the same value to overcome i2c communication error
	STAT=$(i2cBridging $VID $PID $DEVID 0x01 $SLVADDR $CLKRATE $CMDREG 2 0x00 0x00)
	flock -u 999
}

set_btmode_conf () {
	#
	# USB Port 1 => BT1 (J2) => CFG0 L/H, CFG1 L/H
	# USB Port 2 => BT2 (J3) => CFG0 L/H, CFG1 L/H
	# USB Port 3 => BT3 (J4) => CFG0 L/H, CFG1 L/H
	# USB Port 4 => BT4 (J5) => CFG0 L/H, CFG1 L/H
	#
	# (Data to write to slave:0x20 cmdreg: 0x01)
	# Bits: 7        6       | 5        4        | 3        2       | 1        0
	#       BT4CFG1  BT4CFG0 | BT3CFG1  BT3CFG0  | BT2CFG1  BT2CFG0 | BT1CFG1  BT1CFG0
	#
	if [ $NUM -ge 0 -a $NUM -le 4 ]; then
		OFFSET=$(( NUM - 1 ))
		LSHIFT=$(( OFFSET * 2))
		return 0
	elif [ "$NUM" = "-h" -o "$NUM" = "--help" ]; then
		echo "btmode command:"
		echo "pt2usb-util.sh DID btmode pnum [on|off on|off]"
		echo "    -pnum = port numbers are 1, 2, 3, 4"
		echo "    -on|off = on, off (must specify 2 sets of on|off for CFG0 and CFG1)"
		echo "Use following table for technexion target board settings"
		echo "NOTE: jumpers must be pin to pin, i.e. p1-p1..p5-p5 on USB Control and Target boards"
		echo ""
		echo "EMMC (EMMC Boot Mode):"
		echo "+-------------------------------------------------------------------+"
		echo "| PICO-PI: (pnum1 -> J1, pnum2 -> J2)                               |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 on on   | pnum:1 off on  | pnum:1 on on   | pnum:1 off on  |"
		echo "| pnum:2 off on  | pnum:2 on off  | pnum:2 off on  | pnum:2 on off  |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-HOBBIT: (pnum1 -> J1J4, pnum2 -> J2J5)                       |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 on on   | pnum:1 off on  | pnum:1 on on   |                |"
		echo "| pnum:2 off on  | pnum:2 on off  | pnum:2 off on  |                |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-DWARF: (pnum1 -> J17J19, pnum2 -> J18J16)                    |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 on on   | pnum:1 off on  | pnum:1 on on   |                |"
		echo "| pnum:2 off on  | pnum:2 on off  | pnum:2 off on  |                |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-NYMPH: (pnum1 -> J1J2, pnum2 -> J3J4)                        |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 on on   | pnum:1 off on  | pnum:1 on on   |                |"
		echo "| pnum:2 off on  | pnum:2 on off  | pnum:2 off on  |                |"
		echo "+-------------------------------------------------------------------+"
		echo ""
		echo "SDL (Serial Download Mode):"
		echo "+-------------------------------------------------------------------+"
		echo "| PICO-PI: (pnum1 -> J1, pnum2 -> J2)                               |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 off off | pnum:1 on off  | pnum:1 off off | pnum:1 off off |"
		echo "| pnum:2 on on   | pnum:2 on off  | pnum:2 on on   | pnum:2 on  on  |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-HOBBIT: (pnum1 -> J1J4, pnum2 -> J2J5)                       |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 off off | pnum:1 on off  | pnum:1 off off |                |"
		echo "| pnum:2 on on   | pnum:2 on off  | pnum:2 on on   |                |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-DWARF: (pnum1 -> J17J19, pnum2 -> J18J16)                    |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 off off | pnum:1 on off  | pnum:1 off off |                |"
		echo "| pnum:2 on on   | pnum:2 on off  | pnum:2 on  on  |                |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| PICO-NYMPH: (pnum1 -> J1J2, pnum2 -> J3J4)                        |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pico-imx6      | pico-imx6ul    | pico-imx7      | pico-imx8      |"
		echo "+----------------+----------------+----------------+----------------+"
		echo "| pnum:1 off off |                | pnum:1 off off |                |"
		echo "| pnum:2 on on   |                | pnum:2 on  on  |                |"
		echo "+-------------------------------------------------------------------+"
	fi
	return 1
}

btmode_stat () {
	CMDREG=0x01
	flock -x 999
	STAT=$(i2cBridging $VID $PID $DEVID 0x00 $SLVADDR $CLKRATE $CMDREG 1 | grep "^0x" | sed -E 's/(0x[0-9a-fA-F][0-9a-fA-F])/\1/')
	flock -u 999
	ONOFF_STAT=$(( STAT & 0xff ))
	BT1CFG0=$(( ONOFF_STAT & 0x1 ))
	BT1CFG1=$(( ONOFF_STAT >> 1 & 0x1 ))
	BT2CFG0=$(( ONOFF_STAT >> 2 & 0x1 ))
	BT2CFG1=$(( ONOFF_STAT >> 3 & 0x1 ))
	BT3CFG0=$(( ONOFF_STAT >> 4 & 0x1 ))
	BT3CFG1=$(( ONOFF_STAT >> 5 & 0x1 ))
	BT4CFG0=$(( ONOFF_STAT >> 6 & 0x1 ))
	BT4CFG1=$(( ONOFF_STAT >> 7 & 0x1 ))
}

btmode_onoff () {
	btmode_stat
	CMDREG=0x01
	VAL=$ONOFF_STAT
	case "$VALUE1" in
	on)
		# CFG0 HIGH (Jumper: Left Postition)
		BITMASK=$(( 0x01 << LSHIFT ))
		VAL=$(( VAL | BITMASK ))
		;;
	off)
		# CFG0 LOW (Jumper: Right Postition)
		BITMASK=$(( 0x01 << LSHIFT ))
		VAL=$(( VAL & ~BITMASK ))
		;;
	*)
		;;
	esac
	case "$VALUE2" in
	on)
		# CFG1 HIGH (Jumper: Left Postition)
		BITMASK=$(( 0x02 << LSHIFT ))
		VAL=$(( VAL | BITMASK ))
		;;
	off)
		# CFG1 LOW (Jumper: Right Postition)
		BITMASK=$(( 0x02 << LSHIFT ))
		VAL=$(( VAL & ~BITMASK ))
		;;
	*)
		;;
	esac

	flock -x 999
	# sends 2 bytes of the same value to overcome i2c communication error
	STAT=$(i2cBridging $VID $PID $DEVID 0x01 $SLVADDR $CLKRATE $CMDREG 2 $VAL $VAL)
	flock -u 999
}

parse_device_id () {
	if grep -qF "pt2usb" <<< "$DID"; then
		# /dev/filename
		if [ -e "$DID" ]; then
			DEVID=${DID##*_}
		else
			echo "/dev/filename must be a pt2usb"
			exit 1
		fi
	elif find /dev -name "pt2usb_$DID*" | grep -qF "$DID" ; then
		# HUBPATH
		DEVPATH=$(find /dev -name "pt2usb_$DID*")
		DEVID=${DEVPATH##*_}
	elif find /dev -name "pt2usb_*$DID" | grep -qF "$DID" ; then
		# DID
		DEVPATH=$(find /dev -name "pt2usb_*$DID")
		DEVID=${DEVPATH##*_}
	else
		echo "invalid DID parameter"
		exit 1
	fi
	echo "DEVID: $DEVID"
}

#
# main entry point
#
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
	help_msg
	exit 1
fi

if [ -n "$4" ]; then
	# has VALUE1 and VALUE2 means WRITE Operation
	VALUE1=$4
	if [ -n "$2" ] && [ "$2" = "btmode" ] && [ -n "$5" ]; then
		VALUE2=$5
	elif [ -n "$2" ] && [ "$2" = "btmode" ] && [ -z "$5" ]; then
		help_msg
		exit 1
	fi
fi

# issuing actual command needs to be mutexed (so use a lock file)
exec 999>$LOCK

# need to allow 1st param to be DID or HUB_PATH or the full /dev/filename (according to UDEV rule)
parse_device_id

sleep $((RANDOM % 5))

case "$CMD" in
gpio)
	if set_gpio_port; then
		if [ -n "$VALUE1" ]; then
			gpio_onoff
		elif [ -z "$VALUE1" ]; then
			gpio_stat
			echo "GPIO:${NUM} (${STAT})"
		fi
	else
		echo "GPIO Pin Number Error: Must specify available pin numbers: 1,2,3,4,5,7,10,11"
	fi
	;;
power)
	if set_power_addr; then
		if [ -n "$VALUE1" ]; then
			power_onoff
		elif [ -z "$VALUE1" ]; then
			power_stat
			VAL=$(( ONOFF_STAT & 0x03 ))
			test $VAL -eq 3 && STAT="on" || STAT="off"
			printf "USB Port:%d Power:%s(0x%x)\n" ${NUM} ${STAT} ${ONOFF_STAT}
		fi
	else
		echo "Power Port Error: Please specify 1~4 for usb port number"
	fi
	;;
btmode)
	init_btmode_out
	if set_btmode_conf; then
		if [ -n "$VALUE1" -a -n "$VALUE2" ]; then
			btmode_onoff
			btmode_stat
		elif [ -z "$VALUE1" -a -z "$VALUE2" ]; then
			btmode_stat
		fi
		printf "1:\t"
		test $BT1CFG0 -eq 0 && printf "CFG0:Right " || printf "CFG0:Left "
		test $BT1CFG1 -eq 0 && printf "CFG1:Right\n" || printf "CFG1:Left\n"
		printf "2:\t"
		test $BT2CFG0 -eq 0 && printf "CFG0:Right " || printf "CFG0:Left "
		test $BT2CFG1 -eq 0 && printf "CFG1:Right\n" || printf "CFG1:Left\n"
		printf "3:\t"
		test $BT3CFG0 -eq 0 && printf "CFG0:Right " || printf "CFG0:Left "
		test $BT3CFG1 -eq 0 && printf "CFG1:Right\n" || printf "CFG1:Left\n"
		printf "4:\t"
		test $BT4CFG0 -eq 0 && printf "CFG0:Right " || printf "CFG0:Left "
		test $BT4CFG1 -eq 0 && printf "CFG1:Right\n" || printf "CFG1:Left\n"
		printf "(btmode:0x%x)\n" $ONOFF_STAT
	else
		echo "BootMode Port Error: Please specify 1~4 for usb port number"
	fi
	;;
*)
	;;
esac

exit 0
