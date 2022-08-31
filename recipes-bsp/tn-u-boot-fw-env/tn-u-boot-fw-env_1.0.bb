SUMMARY = "SystemD service to add fw_env.config by case"
SECTION = "devel"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
SRC_URI += " \
    file://tn-u-boot-fw-env.sh \
    file://tn-u-boot-fw-env.service \
"

S = "${WORKDIR}"

inherit systemd allarch

do_install () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/tn-u-boot-fw-env.service ${D}${systemd_unitdir}/system/tn-u-boot-fw-env.service
    install -d ${D}${sbindir}
    install -m 0755 ${S}/tn-u-boot-fw-env.sh ${D}${sbindir}
}

FILES:${PN} = "${sbindir}/tn-u-boot-fw-env.sh"

SYSTEMD_SERVICE:${PN} = "tn-u-boot-fw-env.service"
RDEPENDS:${PN} += "bash util-linux e2fsprogs"
