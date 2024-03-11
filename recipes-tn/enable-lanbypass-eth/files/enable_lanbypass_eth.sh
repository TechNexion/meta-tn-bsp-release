#!/bin/bash

LANBYPASS_PWM_BLOCK="30660000"
PERIOD="400000000"      # 0.4 sec
DUTY_CYCLE="200000000"  # 0.2 sec

PWMCHIP=$(find / -name "*pwm*" | grep ${LANBYPASS_PWM_BLOCK} | grep -Poi "pwmchip\d$")

if [[ -z ${PWMCHIP} ]];then
	echo "" > /dev/console
	echo "$0: Can't find the pwm block of ${LANBYPASS_PWM_BLOCK}.pwm" > /dev/console
	echo "$0: Ethernet might not work due to this error!!!" > /dev/console
	exit
fi

cd /sys/class/pwm/${PWMCHIP}

ls |grep pwm0 > /dev/null
if [[ $? -ne 0 ]];then
	echo 0 > export
fi

cd pwm0/
echo ${PERIOD} > period
echo ${DUTY_CYCLE} > duty_cycle

echo 1 > enable
