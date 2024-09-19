FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tn-camera = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tn-camera = "tn-imx_6.6.23_2.0.0-next"
KERNEL_SRC:tn-camera = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tn-camera = "5b65661865f33eadc5aeecb105d4e5b44015ab14"
DELTA_KERNEL_DEFCONFIG:tn-camera = "tn-camera.cfg"
