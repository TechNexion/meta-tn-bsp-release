# Copyright (C) 2019 Technexion Ltd.

SUMMARY = "TechNexion Voicehat Test Suite"
DESCRIPTION = "Unit test for TechNexion Voicehat"
SECTION = "app"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRCBRANCH = "yocto_voicehat_test_1.0"
TN_VOICEHAT_TEST_SRC = "git://github.com/TechNexion-customization/voicehat_test.git"
SRC_URI = "${TN_VOICEHAT_TEST_SRC};branch=${SRCBRANCH} \
           file://voicehat-test.sh \
"
SRCREV = "f349770eeaa8696c66557035f56f993b89273e63"

S = "${WORKDIR}/git"


do_install() {
    install -d "${D}/opt/${PN}"
    install -m 755 *.sh "${D}/opt/${PN}"

#   install -d ${D}${sysconfdir}/profile.d/
#   install -m 0755 ${WORKDIR}/voicehat-test.sh ${D}${sysconfdir}/profile.d/
}

FILES_${PN} += "/opt/${PN}"
RDEPENDS_${PN} = "bash alsa-utils evtest"

FILES_${PN}-dbg += "/opt/${PN}/.debug"

COMPATIBLE_MACHINE = "(mx7|mx8)"