# Copyright 2019 TechNexion
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Small image capable of booting a device. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs), which finds the \
first 'init' program more efficiently. The rescue-loader is included, and is \
used to program the target board with demo images."

TOUCH = "tslib tslib-calibrate tslib-tests"
QTPLUGINS = "qtsvg-plugins qtbase-plugins"

# reassigned to RDEPNDS in image.bbclass
PACKAGE_INSTALL = " \
	${VIRTUAL-RUNTIME_base-utils} \
	udev \
	firmware-imx-sdma \
	packagegroup-core-boot \
	kernel-modules \
	ttf-lato-font \
	mmc-utils \
	xz \
	connman \
	tsl-loader \
	${QTPLUGINS} \
	${@bb.utils.contains("DISTRO_FEATURES", "wayland", "qtwayland-plugins weston-init", "", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "x11 wayland", "weston-xwayland", "", d)} \
	${@bb.utils.contains("MACHINE_FEATURES", "touchscreen", "${TOUCH}", "", d)} \
	"

# remove unneeded technexion packagegroups
IMAGE_INSTALL_remove = "packagegroup-tn-tools packagegroup-tn-nfc packagegroup-tn-voicehat"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "empty-root-password"
IMAGE_LINGUAS = ""
NO_RECOMMENDATIONS = "1"
LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_FSTYPES_remove = "ext4 sdcard.xz sdcard.md5sum wic wic.gz wic.md5sum"

inherit core-image

IMAGE_ROOTFS_SIZE = "0"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
IMAGE_OVERHEAD_FACTOR = "1.0"

XZ_INTEGRITY_CHECK = "crc32"

# Workaround /var/volatile for now
ROOTFS_POSTPROCESS_COMMAND += "rootfs_fixup_var_volatile ; "

rootfs_fixup_var_volatile () {
	install -m 1777 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/tmp
	install -m 755 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/log
}
