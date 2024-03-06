#
# Modified Startup script and systemd unit file for the Weston Wayland compositor
#
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://setup-weston-init.sh"

S = "${WORKDIR}"

do_install:append() {
	install -d ${D}${bindir}
	install -p -m 0755 ${WORKDIR}/setup-weston-init.sh ${D}${bindir}

	sed -i '/^ExecStart=\/usr\/bin\/weston*/i ExecStartPre=-\/usr\/bin\/setup-weston-init.sh' ${D}${systemd_system_unitdir}/weston.service
}

FILES:${PN} += "${bindir}/setup-weston-init.sh"
