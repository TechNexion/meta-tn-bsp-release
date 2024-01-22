FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:tevi = " \
       file://tn-camera.cfg \
       "
SRCBRANCH:tevi = "tn-imx_6.1.22_2.0.0-next"
KERNEL_SRC:tevi = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https;branch=${SRCBRANCH}"
SRCREV:tevi = "f2bdb8d458a19b4dbc95af5c1ea75ec426b1ac20"
DELTA_KERNEL_DEFCONFIG:tevi = "tn-camera.cfg"
