# Copyright 2019 TechNexion Ltd.
# Released under the MIT license

SUMMARY = "Axon Fabric Loader (afloader) Utility"
DESCRIPTION = "Utility for loading Axon Fabric with a JEDEC file\
via SPI bus."
DEPENDS = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

# Override of the main URI can be done in local.conf
# by setting TECHNEXION_GITHUB_MIRROR_pn-afloader = "<your git url>"
TECHNEXION_GITHUB_MIRROR ?= "git://github.com/TechNexion/afloader.git"

# Override of the main URI can be done in local.conf
# by setting SRCBRANCH_pn-afloader = "<your branch>"
SRCBRANCH = "master"

SRCREV = "${AUTOREV}"

SRC_URI = "${TECHNEXION_GITHUB_MIRROR};branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

inherit autotools

