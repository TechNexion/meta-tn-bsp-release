FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SUMMARY = "u-boot bootscript for TechNexion specific image"
DESCRIPTION = "Boot script for launching bootable disk images on TechNexion products"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README;md5=34187c3c5d1f19b2facd3f93dad26def"

DEPENDS = "u-boot-mkimage-native"

PR = "r0"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

SRC_URI = "file://README \
		   file://bootscript-tsl-arm64.txt \
		  "
S = "${WORKDIR}"

do_compile:rescue () {
	mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
		-n "TechNexion boot script" -d ${S}/bootscript-tsl-arm64.txt \
		${S}/boot.scr
}

do_install:rescue () {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 ${S}/boot.scr ${DEPLOY_DIR_IMAGE}
}
