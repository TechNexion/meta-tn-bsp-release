# Copyright 2022 TechNexion Ltd.
SUMMARY = "QCACLD driver for QCA9377-based BD-SDMAC module"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${S}/CORE/HDD/src/wlan_hdd_main.c;beginline=1;endline=21;md5=b7f10cf73d37777b9ab8bacb6b162452"

inherit module

SRCREV = "d846ae05c093ef09df0ecbba8604b89d0473c25f"

SRC_URI = "git://github.com/TechNexion/qcacld-2.0.git;protocol=https;branch=${SRCBRANCH} \
"
SRCBRANCH = "tn-CNSS.LEA.NRT_3.1"

S = "${WORKDIR}/git"

EXTRA_OEMAKE_append = " CONFIG_CLD_HL_SDIO_CORE=y TARGET_BUILD_VARIANT=user CONFIG_P2P_INTERFACE=y"

COMPATIBLE_MACHINE = "mx6|mx7|mx8"
