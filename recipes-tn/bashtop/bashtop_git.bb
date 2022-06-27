SUMMARY = "Resource monitor that shows usage and stats for processor, memory, disks, network and processes."
HOMEPAGE = "https://github.com/aristocratos/bashtop"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS:${PN} += "bash grep procps sed gawk lmsensors-sensors"

PV = "0.9.25+git${SRCPV}"
SRC_URI = "git://github.com/aristocratos/bashtop.git;protocol=https;branch=master"
SRCREV = "c76573ac7dd08cf08fbd576768efb151fcbadb6c"

S = "${WORKDIR}/git"

CLEANBROKEN = "1" 

do_configuration[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Copy bashtop to ${sbindir} i.e. /usr/sbin
    install -d ${D}${sbindir}
    install -m 0755 ${S}/bashtop ${D}${sbindir}
}

FILES:${PN} += "${sbindir}/bashtop"

