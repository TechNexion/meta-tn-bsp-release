SUMMARY = "WiFi firmware files for TechNexion QCA module"
SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_FLAGS = "commercial_qca"
LIC_FILES_CHKSUM = "\
    file://qca9377/CadenceLicense.txt;md5=2d7b27d27db072fdc7383e37e76b848f \
"

SRC_URI += " \
    file://qca9377/CadenceLicense.txt \
    file://qca9377/bdwlan30.bin \
    file://qca9377/otp30.bin \
    file://qca9377/qwlan30.bin \
    file://qca9377/utf30.bin \
    file://qca6174/CadenceLicense.txt \
    file://qca6174/bdwlan30.bin \
    file://qca6174/otp30.bin \
    file://qca6174/qwlan30.bin \
    file://qca6174/utf30.bin \
    file://wlan/cfg.dat \
    file://wlan/qca9377/qcom_cfg.ini \
    file://wlan/qca6174/qcom_cfg.ini \
    file://qca/notice.txt \
    file://qca/nvm_tlv_3.2.bin \
    file://qca/rampatch_tlv_3.2.tlv \
"

S = "${WORKDIR}"

# Gitlab Personal Access Token: e.g. SbtQ_mC4fvJRA88_9jB7
OVERRIDES:append = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else ':token'}"
TOKEN = "${@'' if (d.getVar('PA_TOKEN', True) is None or len(d.getVar('PA_TOKEN', True)) == 0) else '%s' % d.getVar('PA_TOKEN', True)}"
SRCSERVER:token = "git://gitlab.com/technexion-imx/qca_firmware.git"
SRCOPTION:token = ";protocol=https;user=oauth2:${TOKEN}"
SRCBRANCH:token = "caf-wlan/CNSS.LEA.NRT_3.0"
SRCREV:token = "e095f250346d337d91eacc95393f21e3e58140e2"
SRC_URI:token = "${SRCSERVER};branch=${SRCBRANCH}${SRCOPTION}"
S:token = "${WORKDIR}/git"

python do_pre_fetch() {
    # check for existing qca firmware files and use them if available
    src_uri = (d.getVar('SRC_URI') or "").split()
    if all(('file://' in p and os.path.exists(p.replace('file://', d.getVar('FILE_DIRNAME') + '/files/'))) for p in src_uri):
        bb.warn("Using existing QCA Firmware from %s (Please contact TechNexion: sales@technexion.com for newer QCA firmware files)" % (d.getVar('FILE_DIRNAME') + '/files/'))
        return
    # if token has been declared, use token to get latest qca firmware files
    token = d.getVar('PA_TOKEN')
    if token is None or len(token) == 0:
        bb.error("Download QCA Firmware requires personal access token (Please contact TechNexion: sales@technexion.com)")
}
addtask pre_fetch before do_fetch

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/qca9377/
    install -m 0755 ${S}/qca9377/bdwlan30.bin ${D}${nonarch_base_libdir}/firmware/qca9377/
    install -m 0755 ${S}/qca9377/otp30.bin ${D}${nonarch_base_libdir}/firmware/qca9377/
    install -m 0755 ${S}/qca9377/qwlan30.bin ${D}${nonarch_base_libdir}/firmware/qca9377/
    install -m 0755 ${S}/qca9377/utf30.bin ${D}${nonarch_base_libdir}/firmware/qca9377/
    install -d ${D}${nonarch_base_libdir}/firmware/qca6174/
    install -m 0755 ${S}/qca6174/bdwlan30.bin ${D}${nonarch_base_libdir}/firmware/qca6174/
    install -m 0755 ${S}/qca6174/otp30.bin ${D}${nonarch_base_libdir}/firmware/qca6174/
    install -m 0755 ${S}/qca6174/qwlan30.bin ${D}${nonarch_base_libdir}/firmware/qca6174/
    install -m 0755 ${S}/qca6174/utf30.bin ${D}${nonarch_base_libdir}/firmware/qca6174/
    install -d ${D}${nonarch_base_libdir}/firmware/wlan/qca9377/
    install -d ${D}${nonarch_base_libdir}/firmware/wlan/qca6174/
    install -m 0755 ${S}/wlan/cfg.dat ${D}${nonarch_base_libdir}/firmware/wlan/
    install -m 0755 ${S}/wlan/qca9377/qcom_cfg.ini ${D}${nonarch_base_libdir}/firmware/wlan/qca9377/
    install -m 0755 ${S}/wlan/qca6174/qcom_cfg.ini ${D}${nonarch_base_libdir}/firmware/wlan/qca6174/
    install -d ${D}${nonarch_base_libdir}/firmware/qca
    install -m 0755 ${S}/qca/nvm_tlv_3.2.bin ${D}${nonarch_base_libdir}/firmware/qca
    install -m 0755 ${S}/qca/rampatch_tlv_3.2.tlv ${D}${nonarch_base_libdir}/firmware/qca
}

FILES:${PN}-dbg += "${nonarch_base_libdir}/firmware/.debug"
FILES:${PN} += "${nonarch_base_libdir}/firmware/"

COMPATIBLE_MACHINE = "(mx6|mx6ul|mx7|mx8)"

