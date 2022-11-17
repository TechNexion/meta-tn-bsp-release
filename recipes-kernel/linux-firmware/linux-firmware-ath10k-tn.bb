SUMMARY = "WiFi firmware files for TechNexion QCA PCIe module"

SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_FLAGS = "commercial_qca"
HOMEPAGE = "https://github.com/kvalo/ath10k-firmware.git"

LIC_FILES_CHKSUM = "file://ath10k/LICENSE.qca_firmware;md5=35657eea665397848d5e3c6d8fa6710d"

SRCREVATH10K = "2dd48ff50858e2561d020bc72887efbb8cff77bf"
SRCREVQCABT = "be98108d402e3aa76e78b978a986db052497014d"

SRC_URI = "git://github.com/kvalo/ath10k-firmware.git;protocol=https;branch=master;rev=${SRCREVATH10K};destsuffix=ath10k \
           ${SRCSERVER};branch=${SRCBRANCH}${SRCOPTION};rev=${SRCREVQCABT};subpath=qca;destsuffix=qca \
"

S = "${WORKDIR}"

# Gitlab Personal Access Token: e.g. SbtQ_mC4fvJRA88_9jB7
OVERRIDES:append = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else ':token'}"
TOKEN = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else '%s' % d.getVar('PA_TOKEN', True)}"
SRCSERVER:token = "git://gitlab.com/technexion-imx/qca_firmware.git"
SRCOPTION:token = ";protocol=https;user=oauth2:${TOKEN}"
SRCBRANCH:token = "caf-wlan/CNSS.LEA.NRT_3.0"

do_configure() {
        :
}

do_compile() {
        :
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/ath10k/QCA9377/hw1.0
    install -d ${D}${nonarch_base_libdir}/firmware/qca

    #Install QCA9377 wifi firmware
    install -m 0755 ${S}/ath10k/QCA9377/hw1.0/board-2.bin ${D}${nonarch_base_libdir}/firmware/ath10k/QCA9377/hw1.0
    install -m 0755 ${S}/ath10k/QCA9377/hw1.0/board.bin ${D}${nonarch_base_libdir}/firmware/ath10k/QCA9377/hw1.0
    install -m 0755 ${S}/ath10k/LICENSE.qca_firmware ${D}${nonarch_base_libdir}/firmware/ath10k/QCA9377/hw1.0
    install -m 0755 ${S}/ath10k/QCA9377/hw1.0/CNSS.TF.1.0/firmware-5.bin_CNSS.TF.1.0-00267-QCATFSWPZ-1 ${D}${nonarch_base_libdir}/firmware/ath10k/QCA9377/hw1.0/firmware-5.bin

    #Install QCA9377 bt firmware
    install -m 0755 ${S}/qca/notice.txt ${D}${nonarch_base_libdir}/firmware/qca
    install -m 0755 ${S}/qca/nvm_usb_00000302.bin ${D}${nonarch_base_libdir}/firmware/qca
    install -m 0755 ${S}/qca/rampatch_usb_00000302.bin ${D}${nonarch_base_libdir}/firmware/qca
}

FILES:${PN}-dbg += "${nonarch_base_libdir}/firmware/.debug"
FILES:${PN} += "${nonarch_base_libdir}/firmware/"

COMPATIBLE_MACHINE = "(mx6-nxp-bsp|mx7-nxp-bsp|mx8-nxp-bsp)"
