# Copyright 2019 TechNexion
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "\
 A small image just capable of allowing a device to boot with\
 docker container engine.\
"
DESCRIPTION = "\
 TechNexion Docker OS Image. This image contains everything used\
 to start a docker container, e.g. arm64v8/ubuntu docker container.\
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# multiconfig dependency to built container images
do_image[mcdepends] = "multiconfig:container:${TN_CONTAINER_IMAGE}:do_image_complete"

IMAGE_INSTALL = "\
	${QT5_IMAGE_INSTALL} \
	${CORE_IMAGE_EXTRA_INSTALL} \
	packagegroup-core-boot \
	docker \
	python3-docker-compose \
	libvirt \
	libvirt-python \
	openflow \
	kernel-modules \
	connman \
	jq \
	tn-container-service \
	"

# Select Image Features
IMAGE_FEATURES += " \
    splash \
    hwcodecs \
    ssh-server-openssh \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11-base x11-sato', '', d), d)} \
"

IMAGE_LINGUAS = " "

QT5_FONTS = "ttf-dejavu-common ttf-dejavu-sans ttf-dejavu-sans-mono ttf-dejavu-serif "
QT5_IMAGE_INSTALL_APPS = ""
QT5_IMAGE_INSTALL_common = " \
    packagegroup-qt5-demos \
    ${QT5_FONTS} \
    ${QT5_IMAGE_INSTALL_APPS} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libxkbcommon', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland qtwayland-plugins', '', d)}\
"

QT5_IMAGE_INSTALL_imxgpu2d = "${@bb.utils.contains('DISTRO_FEATURES', 'x11','${QT5_IMAGE_INSTALL_common}', \
    'qtbase qtbase-plugins', d)}"

QT5_IMAGE_INSTALL_imxpxp = "${@bb.utils.contains('DISTRO_FEATURES', 'x11','${QT5_IMAGE_INSTALL_common}', \
    'qtbase qtbase-examples qtbase-plugins', d)}"

QT5_IMAGE_INSTALL_imxgpu3d = " \
    ${QT5_IMAGE_INSTALL_common} \
    gstreamer1.0-plugins-good-qt"

CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-fsl-gstreamer1.0 \
    packagegroup-fsl-gstreamer1.0-full \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-init', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland xterm', '', d)} \
"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "4096"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

WKS_FILE = "${@bb.utils.contains("DISTRO_FEATURES", "virtualization", "tn-spl-rootfs-container.wks.in", "tn-spl-bootpart-rootfs.wks.in", d)}"
WKS_FILE_mx8 = "${@bb.utils.contains("DISTRO_FEATURES", "virtualization", "tn-imx8-imxboot-rootfs-container.wks.in", "tn-imx8-imxboot-bootpart-rootfs.wks.in", d)}"

EXTRA_USERS_PARAMS = " \
useradd -P technexion technexion; \
usermod -a -G sudo,users,plugdev,docker technexion; \
"

inherit core-image extrausers
inherit distro_features_check ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qt5-layer', 'populate_sdk_qt5', '', d)}
