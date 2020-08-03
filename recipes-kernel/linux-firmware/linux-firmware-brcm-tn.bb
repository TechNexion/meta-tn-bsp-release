SUMMARY = "WiFi firmware files for Broadcom WLAN/BT module"
DESCRIPTION = "Broadcom Wi-fi & bluetooth firmware"
SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_FLAGS = "commercial_brcm"

LIC_FILES_CHKSUM = "file://AP6335_4.2/Wi-Fi/nvram_ap6335.txt;md5=340d39854632bdd68f9dc8aa3d1447b9"

SRC_URI += " \
    file://AP6335_4.2/Wi-Fi/fw_bcm4339a0_ag.bin \
    file://AP6335_4.2/Wi-Fi/fw_bcm4339a0_ag_apsta.bin \
    file://AP6335_4.2/Wi-Fi/nvram_ap6335.txt \
    file://AP6335_4.2/BT/bcm4339a0.hcd \
    file://AP6212_4.2/Wi-Fi/fw_bcm43438a0.bin \
    file://AP6212_4.2/Wi-Fi/fw_bcm43438a0_apsta.bin \
    file://AP6212_4.2/Wi-Fi/nvram_ap6212.txt \
    file://AP6212_4.2/BT/bcm43438a0.hcd \
"

S = "${WORKDIR}"

# Gitlab Personal Access Token: e.g. SbtQ_mC4fvJRA88_9jB7
OVERRIDES_append = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else ':token'}"
TOKEN = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else '%s' % d.getVar('PA_TOKEN', True)}"
SRCSERVER_token = "git://gitlab.com/technexion-imx/brcm_firmware.git"
SRCOPTION_token = ";protocol=https;user=oauth2:${TOKEN}"
SRCBRANCH_token = "ampak_4.2"
SRCREV_token = "${AUTOREV}"
SRC_URI_token = "${SRCSERVER};branch=${SRCBRANCH}${SRCOPTION}"
S_token = "${WORKDIR}/git"

python do_pre_fetch() {
    # check for existing qca firmware files and use them if available
    src_uri = (d.getVar('SRC_URI') or "").split()
    if all(('file://' in p and os.path.exists(p.replace('file://', d.getVar('FILE_DIRNAME') + '/files/'))) for p in src_uri):
        bb.warn("Using existing BRCM Firmware from %s (Please contact TechNexion: sales@technexion.com for newer BRCM firmware files)" % (d.getVar('FILE_DIRNAME') + '/files/'))
        return
    # if token has been declared, use token to get latest brcm firmware files
    token = d.getVar('PA_TOKEN')
    if token is None or len(token) == 0:
        bb.error("Download BRCM Firmware requires personal access token (Please contact TechNexion: sales@technexion.com)")
}
addtask pre_fetch before do_fetch

do_install() {
    install -d ${D}/lib/firmware/brcm

    #Install AP6335 wifi and bluetooth firmware
    install -m 0755 ${S}/AP6335_4.2/Wi-Fi/fw_bcm4339a0_ag.bin ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6335_4.2/Wi-Fi/fw_bcm4339a0_ag_apsta.bin ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6335_4.2/Wi-Fi/nvram_ap6335.txt ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6335_4.2/BT/bcm4339a0.hcd ${D}/lib/firmware/brcm

    #Install AP6212 wifi and bluetooth firmware
    install -m 0755 ${S}/AP6212_4.2/Wi-Fi/fw_bcm43438a0.bin ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6212_4.2/Wi-Fi/fw_bcm43438a0_apsta.bin ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6212_4.2/Wi-Fi/nvram_ap6212.txt ${D}/lib/firmware/brcm
    install -m 0755 ${S}/AP6212_4.2/BT/bcm43438a0.hcd ${D}/lib/firmware/brcm
}

FILES_${PN}-dbg += "/lib/firmware/.debug"
FILES_${PN} += "/lib/firmware/"

COMPATIBLE_MACHINE = "(mx6|mx6ul|mx7)"
