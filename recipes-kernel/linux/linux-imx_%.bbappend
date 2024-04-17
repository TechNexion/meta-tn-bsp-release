FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tn-camera = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tn-camera = "tn-imx_6.1.55_2.2.0-next"
KERNEL_SRC:tn-camera = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tn-camera = "af6b1a093228948f61dc43a3dc246e45325bac55"
DELTA_KERNEL_DEFCONFIG:tn-camera = "tn-camera.cfg"
