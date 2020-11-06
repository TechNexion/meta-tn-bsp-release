# Copyright 2019 TechNexion
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Small image capable of booting a device. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs), which finds the \
first 'init' program more efficiently. The rescue-loader is included, and is \
used to program the target board with demo images."

TOUCH = ' ${@bb.utils.contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests qt4-embedded-plugin-mousedriver-tslib", "",d)}'

# reassigned to RDEPNDS in image.bbclass
PACKAGE_INSTALL = " \
	${VIRTUAL-RUNTIME_base-utils} \
	directfb \
	udev \
	firmware-imx-sdma \
	packagegroup-core-boot \
	kernel-modules \
	rescue-loader \
	qt4-embedded-plugin-iconengine-svgicon \
	qt4-embedded-plugin-imageformat-svg \
	qt4-embedded-plugin-gfxdriver-directfbscreen \
	qt4-embedded-plugin-gfxdriver-gfxtransformed \
	ttf-lato-font \
	mmc-utils \
	xz \
	connman \
	${TOUCH} \
	"

# default from core-image-minimal (core-image.bbclass)
#IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

# remove unneeded technexion packagegroups
IMAGE_INSTALL_remove = "packagegroup-tn-tools packagegroup-tn-nfc packagegroup-tn-voicehat"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "empty-root-password"
IMAGE_LINGUAS = ""
NO_RECOMMENDATIONS = "1"
LICENSE = "MIT"

IMAGE_FSTYPES = "cpio.xz ${INITRAMFS_FSTYPES}"
IMAGE_FSTYPES_remove = "ext4 sdcard.xz sdcard.md5sum"

inherit core-image

IMAGE_ROOTFS_SIZE = "0"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
IMAGE_OVERHEAD_FACTOR = "1.0"
# Use the default 131072 KB setting, anything bigger will cause dd problem in rescue-loader
# INITRAMFS_MAXSIZE = "262144000"

XZ_INTEGRITY_CHECK = "crc32"

# Workaround /var/volatile for now
ROOTFS_POSTPROCESS_COMMAND += "rootfs_fixup_var_volatile ; "

rootfs_fixup_var_volatile () {
	install -m 1777 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/tmp
	install -m 755 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/log
}
