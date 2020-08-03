SUMMARY = "Resource monitor that shows usage and stats for processor, memory, disks, network and processes."
HOMEPAGE = "https://github.com/aristocratos/bashtop"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} += "bash grep procps sed gawk lmsensors-sensors"

SRC_URI = "git://github.com/aristocratos/bashtop.git"
SRCREV = "v0.8.29"

S = "${WORKDIR}/git"

do_configuration[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Copy bashtop to ${sbindir} i.e. /usr/sbin
    install -d ${D}${sbindir}
    install -m 0755 ${S}/bashtop ${D}${sbindir}
}

FILES_${PN} += "${sbindir}/bashtop"

