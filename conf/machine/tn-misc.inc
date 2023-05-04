
#------ For BSP Unit Test ------
TN_MISC ?= "lshw iw wireless-tools evtest tar zstd gzip vim nano tree"
TN_MISC += "${@bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', 'git evemu-tools', '', d)}"
#TN_MISC += "hwinfo gpiod"
#------------------------

#------ For GnuPG ------
TN_MISC += "gnupg"
#------------------------

#------ Others ------
TN_MISC += "${@bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', 'yad', '', d)}"
#------------------------

TN_MISC_UBUNTU += "${TN_MISC} hwinfo gpiod v4l-utils"

IMAGE_INSTALL:append = "${@bb.utils.contains_any('UBUNTU_TARGET_ARCH', 'arm64 arm', '', ' ${TN_MISC}', d)}"
APTGET_EXTRA_PACKAGES += "${TN_MISC_UBUNTU}"
