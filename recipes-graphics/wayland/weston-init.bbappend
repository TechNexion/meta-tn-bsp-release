#
# Modified Startup script and systemd unit file for the Weston Wayland compositor
#
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://tn-weston@.service \
             file://setup-weston-init.sh"

S = "${WORKDIR}"

do_install_append() {
	# Overwrite modified Weston systemd service
	install -D -p -m0644 ${WORKDIR}/tn-weston@.service ${D}${systemd_system_unitdir}/weston@.service
	sed -i -e s:/etc:${sysconfdir}:g \
		-e s:/usr/bin:${bindir}:g \
		-e s:/var:${localstatedir}:g \
		${D}${systemd_unitdir}/system/weston@.service
	install -D -p -m0755 ${WORKDIR}/setup-weston-init.sh ${D}${bindir}
}

INI_SHELL_SECT = "\\[shell\\]"
INI_SHELL_SECT_mx8mq = ""

INI_UNCOMMENT_ASSIGNMENTS_append_rescue = " \
	${INI_SHELL_SECT} \
	panel-position \
	background-color \
"

FILES_${PN} += "${bindir}/setup-weston-init.sh"
