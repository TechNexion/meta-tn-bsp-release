# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017-2018 NXP
# Copyright 2020 TechNexion Ltd.
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SUMMARY = "Linux Kernel provided and supported by NXP"
DESCRIPTION = "Linux Kernel provided and supported by NXP with focus on \
i.MX Family Reference Boards. It includes support for many IPs such as GPU, VPU and IPU."

require recipes-kernel/linux/linux-imx.inc

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "lzop-native bc-native"

KERNEL_BRANCH ?= "tn-imx_5.4.70_2.3.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('KERNEL_BRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC ?= "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCOPTIONS = ""
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}${SRCOPTIONS}"

SRC_URI_append_virtualization = " file://0001-ARM64-configs-tn_imx8_defconfig-btrfs-fuse-overlayfs.patch"

SRCREV = "14c379b53f82042b234a08d4e9468dba8b568892"

FILES_${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "

KERNEL_CONFIG_COMMAND = "oe_runmake_call -C ${S} CC="${KERNEL_CC}" O=${B} olddefconfig"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEFAULT_PREFERENCE = "1"

DO_CONFIG_V7_COPY = "no"
DO_CONFIG_V7_COPY_mx6 = "yes"
DO_CONFIG_V7_COPY_mx7 = "yes"
DO_CONFIG_V7_COPY_mx8 = "no"

addtask copy_defconfig after do_patch before do_preconfigure

# change do_copy_defconfig to use source code from our tn-kernel repository
do_copy_defconfig () {
    install -d ${B}
    if [ ${DO_CONFIG_V7_COPY} = "yes" ]; then
        # copy latest imx_v7_defconfig to use for mx6, mx6ul and mx7
        mkdir -p ${B}
        cp ${S}/arch/arm/configs/tn_imx_defconfig ${B}/.config
        cp ${S}/arch/arm/configs/tn_imx_defconfig ${B}/../defconfig
    else
        # copy latest tn_imx8_defconfig to use for mx8
        mkdir -p ${B}
        cp ${S}/arch/arm64/configs/tn_imx8_defconfig ${B}/.config
        cp ${S}/arch/arm64/configs/tn_imx8_defconfig ${B}/../defconfig
    fi
}

DELTA_KERNEL_DEFCONFIG ?= ""
#DELTA_KERNEL_DEFCONFIG_prepend_mx8 = "sdk_imx.config "

do_merge_delta_config[dirs] = "${B}"
do_merge_delta_config() {
    for deltacfg in ${DELTA_KERNEL_DEFCONFIG}; do
        if [ -f ${S}/arch/${ARCH}/configs/${deltacfg} ]; then
            ${KERNEL_CONFIG_COMMAND}
            oe_runmake_call -C ${S} CC="${KERNEL_CC}" O=${B} ${deltacfg}
        elif [ -f "${WORKDIR}/${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${WORKDIR}/${deltacfg}
        elif [ -f "${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        fi
    done
    cp .config ${WORKDIR}/defconfig
}
addtask merge_delta_config before do_preconfigure after do_copy_defconfig

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
