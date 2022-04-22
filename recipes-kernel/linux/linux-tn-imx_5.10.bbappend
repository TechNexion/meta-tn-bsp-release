FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SRC_URI_append_rescue = " \
       file://logo.ppm \
       file://rescue-fragment.cfg \
       "
DELTA_KERNEL_DEFCONFIG_rescue = "rescue-fragment.cfg"

do_copy_defconfig_append_rescue () {
    cp ${WORKDIR}/logo.ppm ${S}/drivers/video/logo/logo_linux_clut224.ppm
}
