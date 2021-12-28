SUMMARY = "SystemD service to add fw_env.config by case"
SECTION = "devel"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://tn-u-boot-fw-env.sh;md5=6d8562755e058d8e8c274e7579feb3c0"

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

FILES_${PN} = "${sbindir}/tn-u-boot-fw-env.sh"

SYSTEMD_SERVICE_${PN} = "tn-u-boot-fw-env.service"
RDEPENDS_${PN} += "bash util-linux e2fsprogs"
