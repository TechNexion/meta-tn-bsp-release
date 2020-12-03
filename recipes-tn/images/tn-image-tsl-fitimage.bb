# Copyright (C) 2015 Freescale Semiconductor
# Copyright 2017-2018 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Freescale Image to validate i.MX machines. \
This image contains everything used to test i.MX machines including GUI, \
demos and lots of applications. This creates a very large image, not \
suitable for production."
LICENSE = "MIT"

IMAGE_FSTYPES_append = " wic wic.xz"
IMAGE_FSTYPES_remove = "tar tar.bz2 ext4 sdcard sdcard.bz2 sdcard.xz sdcard.md5sum"
WKS_FILE = "tn-spl-uboot-fit.wks.in"
WKS_FILE_mx8 = "tn-imx8-imxboot-fit.wks.in"
#WKS_FILE_DEPENDS += ""
IMAGE_BOOT_FILES = "tnrescue.itb;tnrescue.itb"
IMAGE_BOOT_FILES_append_arm = " u-boot-${MACHINE}.${UBOOT_SUFFIX};u-boot.img"

# We do not want to install anything, only need the wic packaging"
IMAGE_INSTALL_remove = "packagegroup-core-boot packagegroup-base-extended get-support-info packagegroup-tn-wlan packagegroup-tn-tools packagegroup-tn-voicehat packagegroup-tn-nfc"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

LICENSE = "MIT"

inherit core-image

# Following is for appearance as we are only building /boot vfat partition
IMAGE_ROOTFS_SIZE = "0"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
IMAGE_OVERHEAD_FACTOR = "1.0"

python do_check_initramfs_image () {
    initramfs_image = d.getVar("INITRAMFS_IMAGE", True)
    if initramfs_image is None or initramfs_image != "tn-image-tsl-initramfs":
        bb.fatal("Must set INITRAMFS_IMAGE = \"tn-image-tsl-initramfs\" in local.conf to build tn-image-tsl-fitimage")
}
addtask check_initramfs_image before do_rootfs

RDEPENDS_${PN} += "bash"
DEPENDS += "e2fsprogs-native"
