# Copyright 2017-2018 TN
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Detemine Correct TechNexion's WIFI firmware and drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

# QCA
PACKAGES_remove_qca = "linux-firmware-qca"
RDEPENDS_${PN}_append_qca = " linux-firmware-qca-tn"
# BRCM
PACKAGES_remove_brcm = "linux-firmware-brcm"
RDEPENDS_${PN}_append_brcm = " linux-firmware-brcm-tn"
# ATH10K
PACKAGES_remove_ath-pci = "linux-firmware-ath10k"
RDEPENDS_${PN}_append_ath-pci = " linux-firmware-ath10k-tn"

# Extra Kernel Modules
RDEPENDS_${PN}_append = " ${@bb.utils.contains('COMBINED_FEATURES', 'wifi', 'kernel-module-qcacld-tn', '', d)}"

CONNMAN_UTILS = "connman-tools connman-tests connman-client"
NETWORKMANAGER_UTILS = "networkmanager"

3GTOOLS = "ofono-tests"

5GTOOLS = "libmbim libqmi modemmanager"

WIFITOOLS = "${@bb.utils.contains('MACHINE_FEATURES', 'nmcli', "${NETWORKMANAGER_UTILS}", "${CONNMAN_UTILS}", d)}"

RDEPENDS_${PN}_append = "\
    ${WIFITOOLS} \
    ${@bb.utils.contains('DISTRO_FEATURES', '3g', "${3GTOOLS}", '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'nmcli', "${5GTOOLS}", '', d)} \
    "
