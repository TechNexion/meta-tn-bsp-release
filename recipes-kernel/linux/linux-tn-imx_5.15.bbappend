FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

SRC_URI:append:rescue = " \
       file://logo.ppm \
       file://rescue-fragment.cfg \
       "
DELTA_KERNEL_DEFCONFIG:rescue = "rescue-fragment.cfg"

SRC_URI:append = " file://tn-misc.cfg"
DELTA_KERNEL_DEFCONFIG:append = " tn-misc.cfg"

do_copy_defconfig:append:rescue () {
    cp ${WORKDIR}/logo.ppm ${S}/drivers/video/logo/logo_linux_clut224.ppm
}
