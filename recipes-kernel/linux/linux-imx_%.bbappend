FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tevi = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tevi = "tn-imx_5.15.52_2.1.0-next"
KERNEL_SRC:tevi = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tevi = "13029315f657659c4ec1b58386f075a87002a5c3"
DELTA_KERNEL_DEFCONFIG:tevi = "tn-camera.cfg"
