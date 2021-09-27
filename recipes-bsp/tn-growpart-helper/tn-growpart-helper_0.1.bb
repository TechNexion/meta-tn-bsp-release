SUMMARY = "SystemD service to expand partition size for TechNexion products"
SECTION = "devel"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://tn-standby.jpg;md5=bbe6e85d1751df4073252a3cdabc80f9"

SRC_URI += " \
    file://tn-standby.jpg \
    file://tn-growpart-helper \
    file://tn-growpart-helper.service \
"

S = "${WORKDIR}"

inherit systemd allarch

do_install () {
    # add the jpg to /usr/share/technexion/
    install -d ${D}${datadir}/technexion
    install -m 0644 ${S}/tn-standby.jpg ${D}${datadir}/technexion

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/tn-growpart-helper.service ${D}${systemd_unitdir}/system

    install -d ${D}${sbindir}
    install -m 0755 ${S}/tn-growpart-helper ${D}${sbindir}
}

FILES_${PN} = "${datadir}/technexion/tn-standby.jpg \
               ${sbindir}/tn-growpart-helper"


SYSTEMD_SERVICE_${PN} = "tn-growpart-helper.service"
RDEPENDS_${PN} += "bash util-linux e2fsprogs"
