FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tevi = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tevi = "tn-imx_6.1.55_2.5.0-next"
KERNEL_SRC:tevi = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tevi = "59b3830ebb06881f3f8644e3dee8d0e747bdb53a"
DELTA_KERNEL_DEFCONFIG:tevi = "tn-camera.cfg"
