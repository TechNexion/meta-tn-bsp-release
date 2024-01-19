FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tevi = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tevi = "tn-imx_5.15.71_2.2.0-next"
KERNEL_SRC:tevi = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tevi = "b0c3bb3ea65ff52a6164ce07e885eb2348a1b729"
DELTA_KERNEL_DEFCONFIG:tevi = "tn-camera.cfg"
