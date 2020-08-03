SUMMARY = "This bash shell script gathers system information for technexion support"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS_${PN} += "bash"

SRC_URI = "git://github.com/TechNexion-customization/get-support-info.git"
SRCREV = "9f05701334f92e169aecf68b718042cf0eb80b85"

S = "${WORKDIR}/git"

do_install() {
    # Copy get-support-info.sh to ${sbindir} i.e. /usr/sbin
    install -d ${D}${sbindir}
    install -m 0755 ${S}/get-support-info ${D}${sbindir}
}

FILES_${PN} += "${sbindir}/get-support-info"

