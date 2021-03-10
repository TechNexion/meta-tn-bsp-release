SUMMARY = "External Linux kernel module for audio codec tfa98xx on voicehat"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d2f18c7c8390c6dc7f1db6b14bbb1f0a"

FILESEXTRAPATHS_prepend := "$THISDIR}/${PN}:"

inherit module

SRCREV = "8bf1e8de411308587f5c0e4567185512e1097753"
SRCBRANCH = "tn-DIN_v6x"
SRC_URI = "git://github.com/TechNexion/tfa98xx.git;branch=${SRCBRANCH} \
           https://source.codeaurora.org/external/imxsupport/meta-avs-demos/plain/recipes-kernel/tfa98xx/files/TFA9892N1A_stereo_32FS.cnt?h=imx-alexa-sdk;downloadfilename=TFA9892N1A_stereo_32FS.cnt \
"

SRC_URI[md5sum] = "8a422ce9779c00d91b5b4fac1cbbca5a"
SRC_URI[sha256sum] = "e58912a3016eed5d316e81f0e0630b0d8bcf3ac9b93bcf0b7be8a7c8cf0971f5"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "KDIR='${STAGING_KERNEL_DIR}'"

do_install_append() {
	install -d -m 0755 ${D}/lib/firmware/tfa98/9912/
	install -m 0444 ${WORKDIR}/TFA9892N1A_stereo_32FS.cnt ${D}/lib/firmware/tfa98/9912/
	ln -sf ./tfa98/9912/TFA9892N1A_stereo_32FS.cnt ${D}/lib/firmware/tfa98xx.cnt
}

FILES_${PN} += "/lib/firmware/"
BBCLASSEXTEND = "native"

KERNEL_MODULE_AUTOLOAD += "snd-soc-tfa98xx"
RPROVIDES_${PN} += "kernel-module-tfa98xx"

COMPATIBLE_MACHINE = "mx6|mx7|mx8"
