#!/bin/bash

generate_clock () {
	while true
	do
		gpioset --hold-period 200ms -t0 "LANBYPASS_gen_clock"=0
		gpioset --hold-period 200ms -t0 "LANBYPASS_gen_clock"=1
	done
}

generate_clock ${GPIO_NUM} &

sleep 5
