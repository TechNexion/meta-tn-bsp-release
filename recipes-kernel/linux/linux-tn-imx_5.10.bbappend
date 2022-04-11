FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SRC_URI_append_rescue = " \
       file://logo.ppm \
       "

do_copy_defconfig_append_rescue () {
    cp ${WORKDIR}/logo.ppm ${S}/drivers/video/logo/logo_linux_clut224.ppm
}
