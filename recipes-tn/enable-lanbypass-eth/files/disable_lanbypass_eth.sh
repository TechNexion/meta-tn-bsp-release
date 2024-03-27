#!/bin/bash

PID=$(ps -aux |grep "/usr/sbin/enable_lanbypass_eth.sh" | tr -s ' '| cut -d  ' ' -f 2)
kill -STOP ${PID}

GPIO_NUM=$(cat /sys/kernel/debug/gpio | grep "LANBYPASS_gen_clock" | grep -Eoi "[[:digit:]]{1,}" | cut -d '-' -f 2)
echo ${GPIO_NUM} > /sys/class/gpio/unexport

sleep 5
