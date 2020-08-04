# Copyright (C) 2015 Freescale Semiconductor
# Copyright 2017-2018 NXP
# Copyright (C) 2020 TechNexion Ltd.
# Released under the MIT license (see COPYING.MIT for the terms)
#
# 1. Configuration example for setup script:
# TOKEN=XXXxxxXXXX WIFI_FIRMWARE=y DISTRO=fsl-imx-wayland MACHINE=pico-imx8mm source edm-setup-release.sh -b build-wayland-imx8mm
# 2. Add DISTRO_FEATURES_append = " fcc" into conf/local.conf
# 3. bitbake tn-image-fcc-qca
#

DESCRIPTION = "Freescale Image to validate i.MX machines. \
This image contains everything used to test i.MX machines including GUI, \
demos and lots of applications. This creates a very large image, not \
suitable for production."
LICENSE = "MIT"

inherit core-image

### WARNING: This image is NOT suitable for production use and is intended
###          to provide a way for users to reproduce the image used during
###          the validation process of i.MX BSP releases.

#inherit extrausers
#EXTRA_USERS_PARAMS = "usermod -P root root;"

## Select Image Features
IMAGE_FEATURES += " \
    ssh-server-dropbear \
"

IMAGE_INSTALL_remove += " \
    packagegroup-tn-nfc \
    packagegroup-tn-tools \
    packagegroup-tn-voicehat \
    packagegroup-tn-wlan \
"

IMAGE_INSTALL_append += " packagegroup-tn-fcc-qca-wlan"

CORE_IMAGE_EXTRA_INSTALL += " \
    fcc-qca-btdiag \
    fcc-qca-qcmbr \
"
