# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "TEK-IMX8MP: generate clock for mcu to enable lanbypass eth"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit systemd allarch

SRC_URI = "file://enable_lanbypass_eth.service;md5sum=cb2e351f71fcd0faddd133c2eb2ba457 \
	file://enable_lanbypass_eth.sh;md5sum=ffa3207f02dba79af27dadd0bcfd52c7 \
	file://disable_lanbypass_eth.sh;md5sum=8db87f0ffd76e2264d81925d8785b426 \
	"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/enable_lanbypass_eth.service ${D}${systemd_unitdir}/system/enable_lanbypass_eth.service
    install -d ${D}${sbindir}
    install -m 0755 ${S}/enable_lanbypass_eth.sh ${D}${sbindir}
    install -m 0755 ${S}/disable_lanbypass_eth.sh ${D}${sbindir}
}

FILES:${PN} = "${sbindir}/enable_lanbypass_eth.sh \
		${sbindir}/disable_lanbypass_eth.sh"

SYSTEMD_SERVICE:${PN} = "enable_lanbypass_eth.service"
RDEPENDS:${PN} = "bash util-linux e2fsprogs"
RDEPENDS:${PN}:ubuntu = "bash e2fsprogs"
