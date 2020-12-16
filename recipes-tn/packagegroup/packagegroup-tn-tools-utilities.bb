# Copyright (C) 2012-2014, 2016 Freescale Semiconductor
# Copyright (C) 2015, 2016 O.S. Systems Software LTDA.
# Copyright (C) 2019, 2016 TechNexion Ltd.
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Packagegroup used by TechNexion to provide a set of runtime utilities"
SUMMARY = "TechNexion packagroup - tools/utilities"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup


RDEPENDS_${PN} = " \
    parted \
    e2fsprogs-resize2fs \
"
