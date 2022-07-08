#
# Modified Startup script and systemd unit file for the Weston Wayland compositor
#
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://setup-weston-init.sh"

S = "${WORKDIR}"

do_install_append() {
	install -D -p -m0755 ${WORKDIR}/setup-weston-init.sh ${D}${bindir}

	sed -i '/^ExecStart=\/usr\/bin\/weston*/i ExecStartPre=-\/usr\/bin\/setup-weston-init.sh' ${D}${systemd_system_unitdir}/weston.service
}

FILES_${PN} += "${bindir}/setup-weston-init.sh"
