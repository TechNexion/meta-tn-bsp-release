SUMMARY = "u-boot boot.scr.uimg"
DESCRIPTION = "Boot script for launching bootable disk images on TechNexion products"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README;md5=34187c3c5d1f19b2facd3f93dad26def"

DEPENDS = "u-boot-mkimage-native"

PR = "r0"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

SRC_URI = " \
		file://README \
		file://boot.scr \
"

S = "${WORKDIR}"

inherit deploy

do_compile () {
	mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
	              -n "TechNexion boot script" -d ${S}/boot.scr \
	              ${S}/boot.scr.uimg
}

do_install () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${S}/boot.scr.uimg ${DEPLOYDIR}
}

