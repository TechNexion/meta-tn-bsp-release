# Copyright 2017-2018 TN
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Detemine Correct TechNexion's WIFI firmware and drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

CONNMAN_UTILS = "connman-tools connman-tests connman-client"
NETWORKMANAGER_UTILS = "networkmanager"

3GTOOLS = "ofono-tests"

5GTOOLS = "libmbim libqmi modemmanager"

WIFITOOLS = "${@bb.utils.contains('MACHINE_FEATURES', 'nmcli', "${NETWORKMANAGER_UTILS}", "${CONNMAN_UTILS}", d)}"

RDEPENDS:${PN}:append = "\
    ${WIFITOOLS} \
    ${@bb.utils.contains('DISTRO_FEATURES', '3g', "${3GTOOLS}", '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'nmcli', "${5GTOOLS}", '', d)} \
    "
#It would cause package conflict to install networkmanager/connman in ubuntu image
RDEPENDS:${PN}:remove:ubuntu = "${WIFITOOLS}"