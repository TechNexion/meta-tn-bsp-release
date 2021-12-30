SUMMARY = "flexspi u-boot image"
SECTION = "devel"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://fspi_u-boot.bin;md5=36b7f209b4d799b6435176ee8670b340"

SRC_URI += " \
    file://fspi_u-boot.bin \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}/lib/firmware/flesspi_uboot
    install -m 0755 fspi_u-boot.bin ${D}/lib/firmware/flesspi_uboot/fspi_u-boot.bin
}

FILES_${PN} = "/lib/firmware/"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8mp)"
