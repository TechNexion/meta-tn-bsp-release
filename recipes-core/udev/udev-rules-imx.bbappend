# Update 10-imx.rules
FILESEXTRAPATHS:prepend := "${@bb.utils.contains_any('UBUNTU_TARGET_ARCH', 'arm64 arm', '', '${THISDIR}/${PN}:', d)}"

