#!/bin/bash

PID=$(ps -aux |grep "/usr/sbin/enable_lanbypass_eth.sh" | tr -s ' '| cut -d  ' ' -f 2)
kill -STOP ${PID}

sleep 5
