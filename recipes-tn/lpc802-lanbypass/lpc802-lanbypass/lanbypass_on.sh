#!/bin/bash

if [[ "$(cat /proc/device-tree/model  | grep TEK3-IMX8MP)" ]];then
  GPIO_BANK=0
  GPIO_LINE=8
else
  GPIO_BANK=0
  GPIO_LINE=8
fi

while true
do
  gpioset gpiochip"$GPIO_BANK" "$GPIO_LINE"=1
  usleep 200000
  gpioset gpiochip"$GPIO_BANK" "$GPIO_LINE"=0
  usleep 200000
done
