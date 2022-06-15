SUMMARY = "Utility to run UART stress test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=544799d0b492f119fa04641d1b8868ed"

SRC_URI = "git://github.com/cbrake/linux-serial-test.git;protocol=https;branch=master"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "f8411775c8f9c6f0d1fdd8446841b17950265e92"

S = "${WORKDIR}/git"

inherit cmake

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${B}/${PN} ${D}${sbindir}/
}

