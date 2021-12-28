#!/bin/sh
MMC_TAR=$(cat /proc/cmdline | grep -Poi "mmcblk\d")
ARCH=$(uname -m)

if [ ${ARCH} == 'aarch64' ];then
       echo -e "/dev/${MMC_TAR}\t0x400000\t0x4000" > /etc/fw_env.config
else
       echo -e "/dev/${MMC_TAR}\t0xc0000\t0x2000" > /etc/fw_env.config
fi

