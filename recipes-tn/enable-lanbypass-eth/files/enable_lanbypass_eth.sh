#!/bin/bash

while true
do
	gpioset 0 8=0
	sleep 0.2
	gpioset 0 8=1
	sleep 0.2
done
