# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "NXP SE050 recipe"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
AUTHOR = "Ettore Chimenti <ettore.chimenti@seco.com> Tommaso Merciai <tommaso.merciai@seco.com>"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit cmake dos2unix pkgconfig

DEPENDS += "python systemd openssl"
RDEPENDS_${PN} += "libcrypto"

S = "${WORKDIR}/simw-top"

SRC_URI = "https://github.com/Scott31393/recipes-se050/raw/main/se050/files/SE-PLUG-TRUST-MW.zip;md5sum=c4f56dc6a67d5e40e724358bb6a141e4 \
           "
EXTRA_OECMAKE += "\
    -DCMAKE_BUILD_TYPE=Debug \
    -DHost=iMXLinux \
    -DHostCrypto=OPENSSL \
    -DSMCOM=T1oI2C \
    -DSE05X_Auth=None \
    -DIOT=None \
    -DApplet=SE05X_A \
    "

FILES_${PN} = "${datadir}/se05x \
                ${bindir} \
                ${libdir}/*.so \
                "

FILES_${PN}-dev = "${includedir} ${libdir}/cmake"
