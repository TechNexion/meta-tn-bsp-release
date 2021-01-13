# This image extends fsl-image-multimedia-full with additional
# utils

require recipes-fsl/images/fsl-image-multimedia-full.bb

IMAGE_INSTALL += " bash rsync alsa-utils alsa-tools packagegroup-core-ssh-openssh nano canutils"

IMAGE_INSTALL_remove_mender-image = "growpart"
