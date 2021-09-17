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
    # add the jpg to /etc/xdg/weston/
    install -d ${D}${sysconfdir}/xdg/weston/
    install -m 0644 ${S}/tn-standby.jpg ${D}${sysconfdir}/xdg/weston/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/tn-growpart-helper.service ${D}${systemd_unitdir}/system

    install -d ${D}${sbindir}
    install -m 0755 ${S}/tn-growpart-helper ${D}${sbindir}
}

SYSTEMD_SERVICE_${PN} = "tn-growpart-helper.service"
#RDEPENDS_${PN} += "e2fsprogs-resize2fs gptfdisk parted util-linux udev"
RDEPENDS_${PN} += "bash util-linux e2fsprogs"
