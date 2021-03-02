SUMMARY = "Package a systemd tn-container.service for docker containers in the partition image (i.e. mounted to /var/lib/docker)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "file://tn-container.service"

TN_DOCKER_REPOSITORY ?= "technexion/debian-buster-wayland"
TN_DOCKER_TAG ?= "latest"
TN_DOCKER_APPLICATION ?= "glmark2-es2-wayland"

# setup dependency to docker_disk's do_deploy task
do_install[depends] = "docker-disk:do_deploy"
do_compile[noexec] = "1"

do_install () {
	# add the service to systemd
	install -d ${D}${systemd_unitdir}/system/
	install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
	install -m 0644 ${WORKDIR}/tn-container.service ${D}${systemd_unitdir}/system/
	sed -e "s|\@TN_DOCKER_REPOSITORY\@|${TN_DOCKER_REPOSITORY}|g" -i ${D}${systemd_unitdir}/system/tn-container.service
	sed -e "s|\@TN_DOCKER_TAG\@|${TN_DOCKER_TAG}|g" -i ${D}${systemd_unitdir}/system/tn-container.service
	sed -e "s|\@TN_DOCKER_APPLICATION\@|${TN_DOCKER_APPLICATION}|g" -i ${D}${systemd_unitdir}/system/tn-container.service
	# enable the service
	ln -sf ${systemd_unitdir}/system/tn-container.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/tn-container.service
}

FILES_${PN} += "/lib/systemd/system/"

RDEPENDS_${PN} += "systemd-container"
