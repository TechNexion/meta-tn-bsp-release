# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "TEK3-IMX8MP: POWER KEY Listener"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit systemd allarch

SRC_URI = "file://power_key_listener.service;md5sum=6fe9afeed4d2e8fb390a2963f7bf5748 \
	file://power_key_listener.sh;md5sum=9f37abdb36a3259d3f2662b73c8f3a93 \
	"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/power_key_listener.service ${D}${systemd_unitdir}/system/power_key_listener.service
    install -d ${D}${sbindir}
    install -m 0755 ${S}/power_key_listener.sh ${D}${sbindir}
}

FILES:${PN} = "${sbindir}/power_key_listener.sh"

SYSTEMD_SERVICE:${PN} = "power_key_listener.service"
RDEPENDS:${PN} = "bash util-linux e2fsprogs"
RDEPENDS:${PN}:ubuntu = "bash e2fsprogs"
