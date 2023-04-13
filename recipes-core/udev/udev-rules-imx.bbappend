# Update 10-imx.rules
FILESEXTRAPATHS:prepend := "${@bb.utils.contains('UBUNTU_TARGET_ARCH', 'arm64', '', '${THISDIR}/${PN}:', d)}"

