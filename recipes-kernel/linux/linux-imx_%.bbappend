FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tevi = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tevi = "tn-imx_6.1.55_2.2.0-next"
KERNEL_SRC:tevi = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tevi = "4ce21f30076b1014fbf051ac8be01f94b4d6a88e"
DELTA_KERNEL_DEFCONFIG:tevi = "tn-camera.cfg"
