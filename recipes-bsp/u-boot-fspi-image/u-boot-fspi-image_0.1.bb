SUMMARY = "flexspi u-boot image"
SECTION = "devel"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://tek-imx8mp_fspi_u-boot.bin;md5=2e99b3d605f9738d8a835cb7a1d44d7a"

SRC_URI += "file://tek-imx8mp_fspi_u-boot.bin \
	file://hmi-imx8mp_fspi_u-boot.bin \
"

S = "${WORKDIR}"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}
    if [ "${MACHINE}" = "tek-imx8mp" ];then
        install -m 0755 ${S}/tek-imx8mp_fspi_u-boot.bin ${DEPLOYDIR}/
    elif [ "${MACHINE}" = "hmi-imx8mp" ];then
        install -m 0755 ${S}/hmi-imx8mp_fspi_u-boot.bin ${DEPLOYDIR}/
    fi
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8mp-nxp-bsp)"
