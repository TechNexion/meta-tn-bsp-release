# Copyright 2017-2019 TN
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Detemine Correct TechNexion's WIFI firmware and drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup


RDEPENDS_${PN} = " \
    openssh-sftp-server \
    libsocketcan \
    bash \
    cpulimit \
    hostapd \
    dnf \
    dnsmasq \
    dtc \
    haveged \
    hdparm \
    memtester \
    create-ap \
    iptables \
    iperf3 \
    rsync \
    picocom \
    libgpiod \
    spidev-test \
    stress-ng \
    cpulimit \
    thermal-imx-test \
    bashtop \
    stressapptest \
    fb-test \
    fbida \
    ramsmp \
    wireless-tools \
    sysbench \
    fio \
    iozone3 \
    lmbench \
    ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'sudo glmark2 net-tools opencv packagegroup-core-ssh-openssh wpa-supplicant bluez5 firmware-imx-sdma', '', d)} \
"

RDEPENDS_${PN}_append_mx7 = " voicehat-test"
RDEPENDS_${PN}_append_mx8 = " voicehat-test"
