SUMMARY = "SystemD service to expand partition size for TechNexion products"
SECTION = "devel"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://lanbypass_on.sh;md5=a7aff4c370a4af5a924f3aa6a4f4d298"

SRC_URI += " \
    file://lanbypass_on.sh \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/lanbypass_on.sh ${D}${sbindir}
}

FILES_${PN} = "${sbindir}/lanbypass_on.sh"
RDEPENDS_${PN} += "bash util-linux"
