FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://docker.service.template"

TN_DOCKER_PARTITION_MOUNT ?= "/var/lib/docker"

MOUNT_PREFIX = ""
MOUNT_PREFIX_mender-image = "/data"

do_install_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -m 644 ${WORKDIR}/docker.service.template ${D}/${systemd_unitdir}/system/docker.service
		sed -e "s|\@TN_DOCKER_PARTITION_MOUNT\@|${MOUNT_PREFIX}${TN_DOCKER_PARTITION_MOUNT}|g" -i ${D}/${systemd_unitdir}/system/docker.service
	fi
}
