############################################################################
##
## Copyright (C) 2019 TechNexion.
## Contact: https://www.technexion.com/
##
## This file is part of the TechNexion bitbake/yocto meta-layer
##
## GNU General Public License Usage
## Alternatively, this file may be used under the terms of the GNU
## General Public License version 3 or (at your option) any later version
## approved by the KDE Free Qt Foundation. The licenses are as published by
## the Free Software Foundation and appearing in the file LICENSE.GPL3
## included in the packaging of this file. Please review the following
## information to ensure the GNU General Public License requirements will
## be met: https://www.gnu.org/licenses/gpl-3.0.html.
##
############################################################################

# Extra MACHINEOVERRIDES for External Kernel Firmwares/Modules
OVERRIDES:append = ":tn"
OVERRIDES:append = "${@'' if (d.getVar('RF_FIRMWARES', True) is None or len(d.getVar('RF_FIRMWARES', True)) == 0) else ':%s' % ':'.join([fw.lower() for fw in d.getVar('RF_FIRMWARES', True).split(' ') if fw])}"
OVERRIDES:append = "${@'' if (d.getVar('SOUNDCARD', True) is None or len(d.getVar('SOUNDCARD', True)) == 0) else ':%s' % d.getVar('SOUNDCARD', True).lower()}"
OVERRIDES:append = "${@'' if (d.getVar('NFC', True) is None or len(d.getVar('NFC', True)) == 0) else ':%s' % d.getVar('NFC', True).lower()}"
#OVERRIDES:append = "${@bb.utils.contains('BBLAYERS', '${BSPDIR}/sources/meta-virtualization', ':virtualization', '', d)}"
OVERRIDES:append = "${@':tools' if (d.getVar('DISTRO', True) == 'fsl-imx-xwayland' or d.getVar('DISTRO', True) == 'fsl-imx-wayland' or d.getVar('DISTRO', True) == 'fsl-imx-x11') else ''}"
OVERRIDES:append = "${@':rescue:bootscr' if (d.getVar('INITRAMFS_IMAGE', True) == 'tn-image-loader-initramfs') else ''}"
OVERRIDES:append = "${@bb.utils.contains_any('DISTRO', 'imx-desktop-xwayland imx-desktop-x11', ':ubuntu', '', d)}"
OVERRIDES:append = "${@':tn-vizionsdk' if bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', True, False, d) and bb.utils.contains('BBLAYERS', '${BSPDIR}/sources/meta-tn-vizionsdk', True, False, d) else ''}"

#
# Release Info
#
DISTRO_NAME:tn = "TechNexion i.MX Release Distro"
RELEASE_NAME := "${@'Q%s' % (lambda dt: (dt.datetime.now().month+2)//3)(__import__('datetime')) if (d.getVar('PREVIEW', True) is None) else 'Preview'}"
INHERIT += "image-buildinfo"
IMAGE_BUILDINFO_VARS:append = " RELEASE_NAME"

#
# Kernel Specifics
#
PREFERRED_PROVIDER_kernel:tn = "linux-tn-imx"
PREFERRED_PROVIDER_virtual/kernel:tn = "linux-tn-imx"

PREFERRED_PROVIDER_linux-imx-mfgtool:remove = "linux-imx-mfgtool"

#
# Bootloader Specifics
#
PREFERRED_PROVIDER_u-boot:tn = "u-boot-tn-imx"
PREFERRED_PROVIDER_virtual/bootloader:tn = "u-boot-tn-imx"
PREFERRED_PROVIDER_u-boot-fw-utils = "u-boot-tn-imx"
PREFERRED_RPROVIDER_u-boot-fw-utils = "u-boot-tn-imx"
PREFERRED_RPROVIDER_u-boot-default-env = "u-boot-tn-imx"
UBOOT_CONFIG:mx8-nxp-bsp ??= "sd"
SPL_BINARY = "SPL"
SPL_BINARY:mx8-nxp-bsp = "spl/u-boot-spl.bin"
UBOOT_SUFFIX = "img"
UBOOT_SUFFIX:mx8-nxp-bsp = "bin"
UBOOT_MAKE_TARGET = ""
# UBOOT_SEEK is for dd u-boot.imx to the raw sectors,
# and it should be specified and the same in u-boot c-codes.
UBOOT_SEEK = "69"
BOOT_SPACE:mx6-nxp-bsp = "16384"
BOOT_SPACE:mx-nxp-bsp = "16384"
BOOT_SPACE:mx8-nxp-bsp = "32768"
LOADADDR:mx8-nxp-bsp = ""
# u-boot splash file
SPLASH_FILENAME = "splash.bmp"
BOOT_SCRIPTS:append = " ${SPLASH_FILENAME}:splash.bmp"

#
# u-boot env (uEnv.txt + boot.scr.uimg)
#
BOOT_SCRIPTS = ""
UENV_FILENAME:uenv = "uEnv.txt"
BOOT_SCRIPTS:append:uenv = " ${UENV_FILENAME}:uEnv.txt"
UENV_SCRNAME:bootscr = "boot.scr"
BOOT_SCRIPTS:append:bootscr = " ${UENV_SCRNAME}:boot.scr"

#
# SDMA Firmware (NXP firmware)
#
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS:append = "${MACHINE_FIRMWARE} firmwared"

# DDR Firmware Name (NXP firmwares)
#
DDR_FIRMWARE_NAME:mx8-nxp-bsp = "lpddr4_pmu_train_1d_imem.bin lpddr4_pmu_train_1d_dmem.bin lpddr4_pmu_train_2d_imem.bin lpddr4_pmu_train_2d_dmem.bin"

#
# IMX-BOOT specifics (NXP u-boot packaged binary, i.e. a.k.a flash.bin)
#
IMXBOOT_TARGETS = "flash_evk"
#IMXBOOT_TARGETS:mx8mm = "flash_evk_no_hdmi"
IMAGE_BOOTLOADER:mx8-nxp-bsp = "imx-boot"
IMX_BOOT_SEEK = "1"
IMX_BOOT_SEEK:mx8-nxp-bsp = "33"
#OPTEE_BIN_EXT:mx8mm = "8mm"

#
# Extra Stuff for TechNexion
#
MACHINE_FIRMWARE:remove:mx8-nxp-bsp = "linux-firmware-ath10k"
MACHINE_FEATURES:append = " pci wifi bluetooth touchscreen"
MACHINE_FEATURES:remove:mx8-nxp-bsp = "touchscreen"
IMAGE_INSTALL:append = " get-support-info"
IMAGE_INSTALL:append = " packagegroup-tn-wlan"
IMAGE_INSTALL:append:tools = " packagegroup-tn-tools"
#IMAGE_INSTALL:append:voicehat = " packagegroup-tn-voicehat"
IMAGE_INSTALL:append:nfc = " packagegroup-tn-nfc"
IMAGE_INSTALL:append:ubuntu = " hciattach-qca tn-growpart-helper glmark2 tn-ubuntu-custom"
IMAGE_FSTYPES:append = " ext4 wic.md5sum wic.xz wic.bz2"
LICENSE_FLAGS_ACCEPTED:append = " commercial_qca commercial_brcm commercial_tn"
MACHINE_FEATURES_BACKFILL:append:simcom = " nmcli"
DISTRO_FEATURES_BACKFILL:append:simcom = " nmcli"
PACKAGECONFIG_GRAPHICS:append:mx6-nxp-bsp:pn-qtbase = " gles2"

#
# Avoid conflict errir during build image of Ubuntu
RDEPENDS:tn-growpart-helper:remove:ubuntu = "util-linux e2fsprogs"
RDEPENDS:lpc802-lanbypass:remove:ubuntu = "util-linux"

# wic (sdcard) image
WKS_FILE = "imx-uboot-spl-bootpart.wks.in"
WKS_FILE:mx8-nxp-bsp = "imx-imx-boot-bootpart.wks.in"
WKS_FILE_DEPENDS += "u-boot-tn-imx"
WKS_FILE_DEPENDS:mx8-nxp-bsp += "imx-boot"
IMAGE_BOOT_FILES:append = " ${SPLASH_FILENAME}"
IMAGE_BOOT_FILES:append:uenv = " ${UENV_FILENAME}"
IMAGE_BOOT_FILES:append:bootscr = " ${UENV_SCRNAME};boot.scr"

# xz compression options
XZ_COMPRESSION_LEVEL = "-9"
XZ_INTEGRITY_CHECK = "crc64"

# Additional BitBake/Yocto build tool
HOSTTOOLS_NONFATAL += "git-lfs"

# meta-virtualization
#DISTRO_FEATURES:append:virtualization = " virtualization"

# technexion rescue-loader
DISTRO_FEATURES:remove:rescue = "irda alsa bluetooth wifi nfs 3g nfc ptest bluez bluez5 vulkan optee pulseaudio multiarch \
                                 acl argp ext2 pcmcia usbgadget xattr zeroconf pci jailhouse virtualization"
USE_VT:rescue = "0"

RDEPENDS:packagegroup-core-tools-testapps:remove:simcom = "mesa-demos connman-client connman-tools"
RDEPENDS:packagegroup-base-3g:remove:simcom = "ofono"

#------ For Ubuntu ------
APTGET_EXTRA_PACKAGES:append:ubuntu = " libglu1-mesa"

#------ For VizionSDK/VizionViewer ------
IMAGE_INSTALL:append:tn-vizionsdk = " tn-apt-list vizionsdk-dev vizionviewer"
APTGET_EXTRA_PACKAGES:append:tn-vizionsdk = " gnupg"
IMAGE_INSTALL:remove:tn-vizionsdk:pico-imx8mq = " tn-apt-list vizionsdk-dev vizionviewer"

#------ For Ubuntu inspect V4L2 ------
APTGET_EXTRA_PACKAGES:append:ubuntu = " v4l-utils"
