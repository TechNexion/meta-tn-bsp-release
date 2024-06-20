# Copyright 2017-2018 TN
# Released under the GPLv2 license (see COPYING.MIT for the terms)

SUMMARY = "Build an external Linux kernel module"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit packagegroup

# For VizionSDK/VizionViewer
RDEPENDS:${PN}:append:mx9-nxp-bsp = " \
    tn-apt-list \
    vizionsdk-dev \
    vizionviewer \
"
RDEPENDS:${PN}:append:mx8mp-nxp-bsp = " \
    tn-apt-list \
    vizionsdk-dev \
    vizionviewer \
"
RDEPENDS:${PN}:append:mx8mm-nxp-bsp = " \
    tn-apt-list \
    vizionsdk-dev \
    vizionviewer \
"
