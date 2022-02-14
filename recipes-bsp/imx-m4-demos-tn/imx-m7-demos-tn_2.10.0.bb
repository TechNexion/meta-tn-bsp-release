SUMMARY = "i.MX M7 core Demo images"
DESCRIPTION = "Prebuilt FreeRTOS firmware for Cortex-M core"
SECTION = "app"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCBRANCH = "freertos_2.10.x"
M4_SRC = "git://github.com/TechNexion/imx-m4-demos-tn.git;protocol=https"
SRC_URI = "${M4_SRC};branch=${SRCBRANCH}"
SRCREV = "edef3eeae14a530af02afe81cca853472e497cdd"

S = "${WORKDIR}/git"

inherit deploy

do_deploy() {
	install -d ${DEPLOYDIR}
	install ${S}/${M7_MACHINE}/* ${DEPLOYDIR}/
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8mp)"
