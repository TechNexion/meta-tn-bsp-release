# This image extends fsl-image-multimedia-full with additional
# utils

require recipes-fsl/images/fsl-image-multimedia-full.bb

IMAGE_INSTALL += " bash rsync alsa-utils alsa-tools packagegroup-core-ssh-openssh nano canutils"

IMAGE_INSTALL:remove:mender-image = "growpart"

IMAGE_FSTYPES:append = " wic wic.xz"
