# Copyright (C) 2019 TechNexion Ltd.
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Packagegroup used by TechNexion to provide runtime support for node.js"
SUMMARY = "TechNexion packagroup - nodejs"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    nodejs \
    nodejs-npm \
"
