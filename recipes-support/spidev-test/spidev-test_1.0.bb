# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# Unable to find any files that looked like license statements. Check the accompanying
# documentation and source headers and set LICENSE and LIC_FILES_CHKSUM accordingly.
#
# NOTE: LICENSE is being set to "CLOSED" to allow you to at least start building - if
# this is not accurate with respect to the licensing of the software being built (it
# will not be in most cases) you must specify the correct value before using this
# recipe for anything other than initial testing/development!
SUMMARY = "SPI device driver test application"
SECTION = "examples"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
TARGET_CC_ARCH += "${LDFLAGS}"

SRC_URI = "file://spidev_test.c"

S = "${WORKDIR}"

do_compile () {
	${CC} spidev_test.c -o spidev_test
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 spidev_test ${D}${bindir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"