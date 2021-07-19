SUMMARY = "i.MX M7 core Demo images"
DESCRIPTION = "Prebuilt FreeRTOS firmware for Cortex-M core"
SECTION = "app"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCBRANCH = "freertos_2.9.x"
M4_SRC = "git://github.com/TechNexion/imx-m4-demos-tn.git"
SRC_URI = "${M4_SRC};branch=${SRCBRANCH}"
SRCREV = "d732fb1e4c006ba6c305dbdd8940acaaf8e44155"

S = "${WORKDIR}/git"

inherit deploy

do_deploy() {
	install -d ${DEPLOYDIR}
	install ${S}/${M7_MACHINE}/* ${DEPLOYDIR}/
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8mp|mx8mn)"
