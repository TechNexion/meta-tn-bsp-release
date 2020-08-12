SUMMARY = "Test suite for Linux framebuffer"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRCREV = "063ec650960c2d79ac51f5c5f026cb05343a33e2"
SRC_URI = "git://git@github.com/ponty/fb-test-app.git;protocol=https"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 fb-test ${D}${sbindir}/fb-test
    install -m 0755 offset ${D}${sbindir}/fb-test-offset
	install -m 0755 perf ${D}${sbindir}/fb-test-perf
    install -m 0755 rect ${D}${sbindir}/fb-test-rect
}
