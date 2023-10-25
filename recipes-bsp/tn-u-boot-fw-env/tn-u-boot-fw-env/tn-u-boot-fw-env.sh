#!/bin/sh
MMC_TAR=$(cat /proc/cmdline | grep -io "mmcblk[[:digit:]]")
QSPI_BOOT=$(cat /proc/cmdline  |grep -Poi "qspi_boot=\w*" |cut -d '=' -f 2)
ARCH=$(uname -m)

if [ -z "$MMC_TAR" ]; then
	echo "Can not find MMC device path from kernel argument!"
	exit 1
fi

if [[ ${QSPI_BOOT} == 'yes' ]];then
	BOOT_DEV='mtd0'
else
	BOOT_DEV=${MMC_TAR}
fi

if [ ${ARCH} == 'aarch64' ];then
	echo -e "/dev/${BOOT_DEV}\t0x400000\t0x4000" > /etc/fw_env.config
else
	echo -e "/dev/${BOOT_DEV}\t0xc0000\t0x2000" > /etc/fw_env.config
fi
