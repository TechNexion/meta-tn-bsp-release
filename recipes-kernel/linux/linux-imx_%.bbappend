FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tn-camera = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tn-camera = "tn-imx_6.1.55_2.2.0-next"
KERNEL_SRC:tn-camera = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tn-camera = "57eba74e53e5ec914ad87b524d121fb8a5f78be1"
DELTA_KERNEL_DEFCONFIG:tn-camera = "tn-camera.cfg"
