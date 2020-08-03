SUMMARY = "cpulimit is a tool which limits the CPU usage of a process"
HOMEPAGE = "http://cpulimit.sourceforge.net"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86c1c0d961a437e529db93aa3bb32dc4"
SRCREV ?= "f4d2682804931e7aea02a869137344bb5452a3cd"

SRC_URI = "git://git@github.com/opsengine/cpulimit.git;protocol=https"

S = "${WORKDIR}/git"

do_compile() {
    oe_runmake all
}
do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${B}/src/${PN} ${D}${sbindir}/
}

CFLAGS += "${LDFLAGS}"