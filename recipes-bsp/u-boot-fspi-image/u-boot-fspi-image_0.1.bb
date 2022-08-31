SUMMARY = "flexspi u-boot image"
SECTION = "devel"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://fspi_u-boot.bin;md5=36b7f209b4d799b6435176ee8670b340"

SRC_URI += " \
    file://fspi_u-boot.bin \
"

S = "${WORKDIR}"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0755 ${S}/fspi_u-boot.bin ${DEPLOYDIR}/
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8mp-nxp-bsp)"
