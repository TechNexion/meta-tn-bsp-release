# Copyright 2017-2018 TN
# Released under the GPLv2 license (see COPYING.MIT for the terms)

SUMMARY = "Build an external Linux kernel module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit packagegroup

# Extra Kernel Driver/Firmware for technexion's voicehat
RDEPENDS_${PN}_append_voicehat = " kernel-module-tfa98xx"

