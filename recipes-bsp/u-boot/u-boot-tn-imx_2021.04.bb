# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017-2018 NXP
# Copyright (C) 2020 TechNexion.

DESCRIPTION = "i.MX U-Boot suppporting TechNexion i.MX boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

require recipes-bsp/u-boot/u-boot.inc
inherit pythonnative

PROVIDES += "u-boot"
DEPENDS:append = " python dtc-native flex-native bison-native"
RDEPENDS_${PN}:append:uenv = " u-boot-uenv"
RDEPENDS_${PN}:append:bootscr = " u-boot-script-technexion"


LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PR = "r0"
SRCSERVER = "git://github.com/TechNexion/u-boot-tn-imx.git;protocol=https"
SRCOPTIONS = ""
SRCBRANCH = "tn-imx_v2021.04_5.15.5_1.0.0-next"
SRC_URI = "${SRCSERVER};branch=${SRCBRANCH}${SRCOPTIONS}"
SRCREV = "4c4f0116bd43632ac8e1fdb60c0d2d394dac603d"
SRC_URI:append = " file://splash.bmp"
SRC_URI:append:rescue += " file://rescue-fragment-uboot.cfg"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-${SRCREV}"

BOOT_TOOLS = "imx-boot-tools"

do_deploy:append () {
	install -d ${DEPLOYDIR}
	install ${WORKDIR}/splash.bmp ${DEPLOYDIR}/splash.bmp
}

do_deploy:append:mx8m () {
    # Deploy u-boot-nodtb.bin and fsl-imx8mq-XX.dtb, to be packaged in boot binary by imx-boot
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    for DTB in ${UBOOT_DTB_NAME}; do
                        install -m 0777 ${B}/${config}/arch/arm/dts/${DTB}  ${DEPLOYDIR}/${BOOT_TOOLS}
                    done
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${UBOOT_CONFIG}
                fi
            done
            unset  j
        done
        unset  i
    fi

}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

UBOOT_NAME:mx6 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
UBOOT_NAME:mx7 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
UBOOT_NAME:mx8 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
