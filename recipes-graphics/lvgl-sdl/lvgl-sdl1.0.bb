# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Open-source Embedded GUI Library"
HOMEPAGE = "https://littlevgl.com/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# DEPENDS = ""
DEPENDS += "libsdl2"


SRC_URI = " \
	gitsm://github.com/lvgl/lv_sim_eclipse_sdl.git;protocol=https;"

SRCREV = "b4a74a7316ae0cb06bc1b656243640454e050e3c"

S = "${WORKDIR}/git"

inherit cmake

do_install() {
	install -d ${D}${sbindir}
	install -m 0644 ${WORKDIR}/git/bin/main ${D}${sbindir}/lvgl-demo
}

BBCLASSEXTEND = "native nativesdk"
