FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tn-camera = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tn-camera = "tn-imx_6.6.52_2.2.0-next"
LINUX_IMX_SRC:tn-camera = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tn-camera = "6cf80ede731e17099c83b529c0b2e212f3c892e0"
DELTA_KERNEL_DEFCONFIG:tn-camera = "tn-camera.cfg"
