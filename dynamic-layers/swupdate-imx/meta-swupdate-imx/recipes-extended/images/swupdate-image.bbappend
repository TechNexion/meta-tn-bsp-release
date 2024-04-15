# remove unneeded technexion packagegroups
IMAGE_INSTALL:remove = "packagegroup-tn-tools packagegroup-tn-nfc packagegroup-tn-voicehat packagegroup-tn-wlan packagegroup-tn-vizionsdk"

DISTRO_FEATURES:remove = "irda alsa bluetooth wifi nfs 3g nfc ptest bluez bluez5 vulkan optee pulseaudio multiarch \
                                 acl argp ext2 pcmcia usbgadget xattr zeroconf pci jailhouse virtualization"