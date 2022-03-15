# Copyright 2017-2019 TN
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Detemine Correct TechNexion's WIFI firmware and drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup


RDEPENDS_${PN} = " \
    alsa-utils \
    alsa-tools \
    bash \
    bashtop \
    bc \
    coreutils \
    create-ap \
    cpulimit \
    dnsmasq \
    dtc \
    e2fsprogs-mke2fs \
    e2fsprogs-resize2fs \
    evtest \
    ethtool \
    fbset \
    fb-test \
    fio \
    glmark2 \
    haveged \
    thermal-imx-test \
    hdparm \
    hostapd \
    i2c-tools \
    iozone3 \
    iptables \
    iproute2 \
    iperf3 \
    libgpiod \
    libgpiod-tools \
    libsocketcan \
    lmbench \
    memtester \
    mmc-utils \
    net-tools \
    openssh-sftp-server \
    picocom \
    ramsmp \
    read-edid \
    rsync \
    spidev-test \
    stress-ng \
    stressapptest \
    sysbench \
    v4l-utils \
    wireless-tools \
    cloud-utils-growpart \
    udev \
    libubootenv \
    libubootenv-bin \
    tn-u-boot-fw-env \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'v4l-utils gtk+3-demo fbida', '', d)} \
"
#    ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'sudo net-tools opencv packagegroup-core-ssh-openssh wpa-supplicant bluez5 firmware-imx-sdma', '', d)}


RDEPENDS_${PN}_append_mx7 = " voicehat-test"
RDEPENDS_${PN}_append_mx8 = " voicehat-test"
