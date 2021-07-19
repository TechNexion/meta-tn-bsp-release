# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "NXP SE050 SSSCLI tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
AUTHOR = "Wig Cheng <wig.cheng@technexion.com"

inherit setuptools3 pypi

BBCLASSEXTEND = "nativesdk"
DEPENDS += "python3 python3-click python3-cryptography (= 2.6.1) python3-setuptools python3-cffi se050-tools"

RDEPENDS_${PN} = "se050-tools"
S = "${WORKDIR}/simw-top/pycli/src"

SRC_URI = "file://SE-PLUG-TRUST-MW.zip"
SRC_URI = "https://github.com/TechNexion-customization/se050-tools/raw/master/SE-PLUG-TRUST-MW.zip;md5sum=a367e7cee3fd238d108a427e137c3ed7 \
           "
