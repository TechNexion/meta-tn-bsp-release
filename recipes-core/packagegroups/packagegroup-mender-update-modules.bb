# Copyright 2017-2018 TN
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Packagegroup for including mender update modules plugins to mender client"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

# dfu
RDEPENDS_${PN} += "mender-dfu dfu-util"
# dir-overlay
RDEPENDS_${PN} += "mender-dir-overlay"
# ipk
RDEPENDS_${PN} += "${@bb.utils.contains('PACKAGE_CLASSES', 'package_ipk', 'mender-ipk', '', d)}"
