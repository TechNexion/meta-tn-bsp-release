SUMMARY = "SystemD service to expand partition size for TechNexion products"
SECTION = "devel"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=783b7e40cdfb4a1344d15b1f7081af66"


SRC_URI += " \
    file://LICENSE \
    file://rc-local.service \
    file://rc.local \
"

S = "${WORKDIR}"

inherit systemd allarch

do_install () {
    install -d ${D}${sysconfdir}/systemd/system
    install -m 0644 ${S}/rc-local.service ${D}${sysconfdir}/systemd/system/rc-local.service

    install -d ${D}${sysconfdir}
    install -m 0755 ${S}/rc.local ${D}${sysconfdir}
}

FILES_${PN} = "${sysconfdir}/rc.local"

SYSTEMD_SERVICE_${PN} = "rc-local.service"
RDEPENDS_${PN} += "bash"
