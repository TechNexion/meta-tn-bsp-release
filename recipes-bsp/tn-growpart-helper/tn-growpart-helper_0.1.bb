SUMMARY = "SystemD service to expand partition size for TechNexion products"
SECTION = "devel"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://tn-standby.jpg;md5=bbe6e85d1751df4073252a3cdabc80f9"

SRC_URI += " \
    file://tn-standby.jpg \
    file://tn-growpart-helper \
    file://tn-growpart-helper_wayland.service \
    file://tn-growpart-helper_xwindow.service \
"

S = "${WORKDIR}"

inherit systemd allarch

USE_WL = "${@bb.utils.contains("DISTRO_FEATURES", "wayland", "yes", "no", d)}"
USE_X11 = "${@bb.utils.contains("DISTRO_FEATURES", "x11", "yes", "no", d)}"

do_install () {
    # add the jpg to /usr/share/technexion/
    install -d ${D}${datadir}/technexion
    install -m 0644 ${S}/tn-standby.jpg ${D}${datadir}/technexion
    if [ "${USE_WL}" = "yes" ]; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/tn-growpart-helper_wayland.service ${D}${systemd_unitdir}/system/tn-growpart-helper.service
    elif  [ "${USE_X11}" = "yes" ]; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/tn-growpart-helper_xwindow.service ${D}${systemd_unitdir}/system/tn-growpart-helper.service
    fi
    install -d ${D}${sbindir}
    install -m 0755 ${S}/tn-growpart-helper ${D}${sbindir}
}

FILES_${PN} = "${datadir}/technexion/tn-standby.jpg \
               ${sbindir}/tn-growpart-helper"


SYSTEMD_SERVICE_${PN} = "tn-growpart-helper.service"
RDEPENDS_${PN} += "bash util-linux e2fsprogs"
