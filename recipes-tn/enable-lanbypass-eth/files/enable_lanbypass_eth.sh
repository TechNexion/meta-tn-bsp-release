#!/bin/bash

generate_clock () {
	while true
	do
		echo 1 > /sys/class/gpio/gpio${1}/value
		sleep 0.2
		echo 0 > /sys/class/gpio/gpio${1}/value
		sleep 0.2
	done
}

GPIO_NUM=$(cat /sys/kernel/debug/gpio | grep "LANBYPASS_gen_clock" | grep -Poi "gpio-\d+" | cut -d '-' -f 2)
echo ${GPIO_NUM} > /sys/class/gpio/export
echo out > /sys/class/gpio/gpio${GPIO_NUM}/direction

generate_clock ${GPIO_NUM} &

sleep 5
