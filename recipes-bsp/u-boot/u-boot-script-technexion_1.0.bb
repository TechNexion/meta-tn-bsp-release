FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SUMMARY = "u-boot boot.scr.uimg"
DESCRIPTION = "Boot script for launching bootable disk images on TechNexion products"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README;md5=34187c3c5d1f19b2facd3f93dad26def"

DEPENDS = "u-boot-mkimage-native"

PR = "r0"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

SRC_URI = "file://README file://boot.scr"
SRC_URI_append_rescue = " file://boot.scr.tsl"

S = "${WORKDIR}"

do_compile () {
	mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
		-n "TechNexion boot script" -d ${S}/boot.scr \
		${S}/boot.scr.uimg
}

do_compile_append_rescue () {
	sed -e 's,@FIT_ADDR@,'${UBOOT_FIT_LOADADDRESS}',g' -i ${S}/boot.scr.tsl
	sed -e 's,@FIT_PREFIX@,'${UBOOT_FIT_PREFIX}',g' -i ${S}/boot.scr.tsl
	if [ "${MACHINE}" = "edm-g-imx8mm" ]; then
		sed -e '2 i\setenv dtoverlay imx8mm-edm-g-wb-sn65dsi84-vl10112880.dtbo' -i ${S}/boot.scr.tsl
	elif [ "${MACHINE}" = "pico-imx8mm" ]; then
		sed -e '2 i\setenv dtoverlay imx8mm-pico-pi-ili9881c.dtbo' -i ${S}/boot.scr.tsl
	fi
	mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
		-n "TechNexion boot script" -d ${S}/boot.scr.tsl \
		${S}/boot.scr.uimg
}

do_install () {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 ${S}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
}
