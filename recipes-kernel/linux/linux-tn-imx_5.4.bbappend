kernel_do_deploy_append_rescue() {
	echo "Copying fitImage-${INITRAMFS_IMAGE} file for tn-image-tsl-fitimage..."
	install -m 0644 ${B}/arch/${ARCH}/boot/fitImage-${INITRAMFS_IMAGE} ${DEPLOYDIR}/tnrescue.itb
}

