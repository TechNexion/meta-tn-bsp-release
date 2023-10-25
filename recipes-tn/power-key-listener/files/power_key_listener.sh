#!/bin/bash

DEVICE=/dev/input/event0

INFO="POWER KEY Listener:"

KEY_DOWN='*type 1 (EV_KEY), code 116 (KEY_POWER), value 1*'
KEY_UP='*type 1 (EV_KEY), code 116 (KEY_POWER), value 0*'
PRESSED_TIME=0
TRI_TIME=5

toggle_led(){
        echo "${FUNCNAME[ 0 ]}: toggle_led !!!" > /dev/console
        if [[ $(cat /sys/class/leds/ALERT1-LED/brightness) -eq 1 ]];then
                echo 0 > /sys/class/leds/ALERT1-LED/brightness
        else
                echo 1 > /sys/class/leds/ALERT1-LED/brightness
        fi
}

do_long_pressed_job(){
        # reset device
        gpioset 1 5=0
}

do_job(){
        toggle_led
}

evtest "$DEVICE" | while read EVENT ; do
	case $EVENT in
	$KEY_DOWN)
		PRESSED_TIME=$(date '+%-S')
		;;
	$KEY_UP)
		NOW_TIME=$(date '+%-S')
		if [[ ${NOW_TIME} -lt ${PRESSED_TIME} ]];then
			NOW_TIME=$(( ${NOW_TIME} + 60 ))
		fi
		if [[ $(( ${NOW_TIME} - ${PRESSED_TIME} )) -gt ${TRI_TIME} ]];then
			echo "$INFO: trigger reset !!!" > /dev/console
			sleep 1
			do_long_pressed_job
			continue
		fi
		echo "$INFO: detect POWER key, do job !!!" > /dev/console
		do_job
		;;
	esac
done
