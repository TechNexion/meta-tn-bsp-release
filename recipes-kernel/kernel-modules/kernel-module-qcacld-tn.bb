# Copyright 2022 TechNexion Ltd.
SUMMARY = "QCACLD driver for QCA9377-based BD-SDMAC module"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${S}/CORE/HDD/src/wlan_hdd_main.c;beginline=1;endline=21;md5=b7f10cf73d37777b9ab8bacb6b162452"

inherit module

SRCREV = "2090c864fb1f66646a05811d6bc0f321b99c59c1"

SRC_URI = "git://github.com/TechNexion/qcacld-2.0.git;protocol=https;branch=${SRCBRANCH} \
"
SRCBRANCH = "tn-CNSS.LEA.NRT_3.1"

S = "${WORKDIR}/git"

EXTRA_OEMAKE:append= " CONFIG_CLD_HL_SDIO_CORE=y TARGET_BUILD_VARIANT=user CONFIG_P2P_INTERFACE=y"

COMPATIBLE_MACHINE = "mx6|mx7|mx8"
