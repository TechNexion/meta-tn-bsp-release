# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2018 (C) O.S. Systems Software LTDA.
# Copyright 2017-2024 NXP
# Copyright (C) 2024 TechNexion.

DESCRIPTION = "i.MX U-Boot suppporting TechNexion i.MX boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "flex-native bison-native bc-native dtc-native gnutls-native"
RDEPENDS:${PN}:append:uenv = " u-boot-uenv"
RDEPENDS:${PN}:append:bootscr = " u-boot-script-technexion"


LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PR = "r0"
SRCSERVER = "git://github.com/TechNexion/u-boot-tn-imx.git;protocol=https"
SRCOPTIONS = ""
SRCBRANCH = "tn-imx_v2024.04_6.6.23_2.0.0-next"
SRC_URI = "${SRCSERVER};branch=${SRCBRANCH}${SRCOPTIONS}"
SRCREV = "62195f94d39d51af87799ac38081493e27868561"
SRC_URI:append = " file://splash.bmp"
SRC_URI:append:rescue = " file://rescue-fragment-uboot.cfg"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-${SRCREV}"

BOOT_TOOLS = "imx-boot-tools"

###############################################################
# require recipes-bsp/u-boot/u-boot-imx-common_${PV}.inc

PROVIDES += "u-boot"

inherit uuu_bootloader_tag

UUU_BOOTLOADER            = ""
UUU_BOOTLOADER:mx6-generic-bsp        = "${UBOOT_BINARY}"
UUU_BOOTLOADER:mx7-generic-bsp        = "${UBOOT_BINARY}"
UUU_BOOTLOADER_TAGGED     = ""
UUU_BOOTLOADER_TAGGED:mx6-generic-bsp = "u-boot-tagged.${UBOOT_SUFFIX}"
UUU_BOOTLOADER_TAGGED:mx7-generic-bsp = "u-boot-tagged.${UBOOT_SUFFIX}"

do_deploy:append () {
	install -d ${DEPLOYDIR}
	install ${WORKDIR}/splash.bmp ${DEPLOYDIR}/splash.bmp
}

do_deploy:append:mx8m-generic-bsp() {
    # Deploy u-boot-nodtb.bin and fsl-imx8m*-XX.dtb for mkimage to generate boot binary
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
COMPATIBLE_MACHINE = "(imx-nxp-bsp)"
