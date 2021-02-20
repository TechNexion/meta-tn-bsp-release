# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Utility to test NXP NTAG function"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://ntag-i2c-tool.cpp \
           file://i2c-dev.h \
"

INSANE_SKIP_${PN} = "ldflags"
S = "${WORKDIR}"

do_compile() {
	${CXX} ntag-i2c-tool.cpp -o ntag-i2c-tool
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ntag-i2c-tool ${D}${bindir}
}
