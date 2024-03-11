# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "TEK-IMX8MP: generate clock for mcu to enable lanbypass eth"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit systemd allarch

SRC_URI = "file://enable_lanbypass_eth.service;md5sum=262d4d3e575bbcace928b9b3bed0ca0f \
	file://enable_lanbypass_eth.sh;md5sum=533a467176815b704adc471b68988bbc \
	"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/enable_lanbypass_eth.service ${D}${systemd_unitdir}/system/enable_lanbypass_eth.service
    install -d ${D}${sbindir}
    install -m 0755 ${S}/enable_lanbypass_eth.sh ${D}${sbindir}
}

FILES_${PN} = "${sbindir}/enable_lanbypass_eth.sh"

SYSTEMD_SERVICE_${PN} = "enable_lanbypass_eth.service"
RDEPENDS_${PN} += "bash util-linux e2fsprogs"
