# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "NXP SE050 recipe"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
AUTHOR = "Ettore Chimenti <ettore.chimenti@seco.com> Tommaso Merciai <tommaso.merciai@seco.com>"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit cmake dos2unix pkgconfig

DEPENDS += "python3 systemd openssl"
RDEPENDS:${PN} += "libcrypto"

S = "${WORKDIR}/simw-top"

SRC_URI = "https://github.com/TechNexion-customization/se050-tools/raw/master/SE05x_MW.zip;md5sum=1046c81bb2ff215dafbbcb16ee5cbf18 \
           "
EXTRA_OECMAKE += "\
    -DCMAKE_BUILD_TYPE=Debug \
    -DPTMW_Host=iMXLinux \
    -DPTMW_HostCrypto=OPENSSL \
    -DPTMW_SMCOM=T1oI2C \
    -DSE05X_Auth=None \
    -DIOT=None \
    -DPTMW_Applet=SE05X_A \
    "

FILES:${PN} = "${datadir}/se05x \
                ${bindir} \
                ${libdir}/*.so \
                "

FILES:${PN}-dev = "${includedir} ${libdir}/cmake"
