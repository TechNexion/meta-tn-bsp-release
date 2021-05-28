FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SRC_URI_append_rescue = " \
       file://0001-dtb-tsl-reduce-cma-size-for-arm32-aarch64-soms.patch \
       file://0004-video-fbdev-core-fbmem-modify-to-a-single-graph.patch \
       file://logo.ppm \
       "

do_copy_defconfig_append_rescue () {
    cp ${WORKDIR}/logo.ppm ${S}/drivers/video/logo/logo_linux_clut224.ppm
}

kernel_do_deploy_append_rescue() {
	echo "Copying fitImage-${INITRAMFS_IMAGE} file for tn-image-tsl-fitimage..."
	install -m 0644 ${B}/arch/${ARCH}/boot/fitImage-${INITRAMFS_IMAGE} ${DEPLOYDIR}/tnrescue.itb
}

