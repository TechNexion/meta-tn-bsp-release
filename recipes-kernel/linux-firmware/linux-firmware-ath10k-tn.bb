SUMMARY = "WiFi firmware files for TechNexion QCA PCIe module"

SECTION = "kernel"
LICENSE = "Proprietary"

HOMEPAGE = "https://github.com/kvalo/ath10k-firmware.git"

LIC_FILES_CHKSUM = "file://LICENSE.qca_firmware;md5=35657eea665397848d5e3c6d8fa6710d"

SRCREV = "2dd48ff50858e2561d020bc72887efbb8cff77bf"

SRC_URI = "git://github.com/kvalo/ath10k-firmware.git"

S = "${WORKDIR}/git"

do_configure() {
        :
}

do_compile() {
        :
}

do_install() {
    install -d ${D}/lib/firmware/ath10k/QCA9377/hw1.0

    #Install QCA9377 wifi firmware
    install -m 0755 ${S}/QCA9377/hw1.0/board-2.bin ${D}/lib/firmware/ath10k/QCA9377/hw1.0
    install -m 0755 ${S}/QCA9377/hw1.0/board.bin ${D}/lib/firmware/ath10k/QCA9377/hw1.0
    install -m 0755 ${S}/LICENSE.qca_firmware ${D}/lib/firmware/ath10k/QCA9377/hw1.0

    install -m 0755 ${S}/QCA9377/hw1.0/CNSS.TF.1.0/firmware-5.bin_CNSS.TF.1.0-00267-QCATFSWPZ-1 ${D}/lib/firmware/ath10k/QCA9377/hw1.0/firmware-5.bin
}

FILES_${PN}-dbg += "/lib/firmware/.debug"
FILES_${PN} += "/lib/firmware/"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
