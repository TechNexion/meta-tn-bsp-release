SUMMARY  = "TechNexion Rescue Loader Python Scripts"
DESCRIPTION = "Python scripts to run on target board to download sdcard image and program the eMMC"
HOMEPAGE = "https://github.com/TechNexion-customization/rescue-loader"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"


SRC_URI = "${SRCSERVER};branch=${SRCBRANCH}"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
	bash \
	cairo \
	libudev \
	${PYTHON_PN}-debugger \
	${PYTHON_PN}-threading \
	${PYTHON_PN}-datetime \
	${PYTHON_PN}-pickle \
	${PYTHON_PN}-jsonpatch \
	${PYTHON_PN}-pygobject \
	${PYTHON_PN}-dbus \
	${PYTHON_PN}-lxml \
	${PYTHON_PN}-pyudev \
	${PYTHON_PN}-pyserial \
	${PYTHON_PN}-psutil \
	${PYTHON_PN}-pyusb \
	${PYTHON_PN}-html \
	${PYTHON_PN}-pyqrcode \
	${PYTHON_PN}-pyqt5 \
	${PYTHON_PN}-pycairo \
	"

inherit setuptools3

REQUIRED_DISTRO_FEATURES = "systemd"

BBCLASSEXTEND = "nativesdk"

do_install_append() {
	install -d ${D}${systemd_unitdir}/system/
	install -d ${D}${systemd_unitdir}/system/dbus.target.wants/
	install -d ${D}${systemd_unitdir}/system/sockets.target.wants/
	install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
	install -d ${D}${sysconfdir}/profile.d/
	install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
	install -d ${D}${sbindir}

	# tsl-loader xml config file
	install -m 0644 ${S}/tsl/installer.xml ${D}${sysconfdir}

	# tsl-loader installerd systemd service files
	install -m 0644 ${S}/tsl/installerd.service ${D}${systemd_unitdir}/system/
	ln -sf ${systemd_unitdir}/system/installerd.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/installerd.service

	#guiclientd.sh
	install -m 755 ${S}/tsl/guiclientd.sh ${D}${sbindir}/guiclientd.sh

	# tsl-loader guiclientd systemd service files
	install -m 0644 ${S}/tsl/guiclientd.service ${D}${systemd_unitdir}/system/
	ln -sf ${systemd_unitdir}/system/guiclientd.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/guiclientd.service

	# dbus session socket
	install -m 0644 ${S}/tsl/dbus-sess.socket ${D}${systemd_unitdir}/system/
	ln -sf ${systemd_unitdir}/system/dbus-sess.socket ${D}${systemd_unitdir}/system/dbus.target.wants/dbus-sess.socket
	ln -sf ${systemd_unitdir}/system/dbus-sess.socket ${D}${systemd_unitdir}/system/sockets.target.wants/dbus-sess.socket

	# dbus session service
	install -m 0644 ${S}/tsl/dbus-sess.service ${D}${systemd_unitdir}/system/
	ln -sf ${systemd_unitdir}/system/dbus-sess.service ${D}${systemd_unitdir}/system/multi-user.target.wants/dbus-sess.service

	# export DBUS_SESSION_BUS_ADDDRESS=unix:path=/var/run/dbus/session_bus_socket through dbus-export.sh in /etc/profile
	install -m 0755 ${S}/tsl/dbus-export.sh ${D}${sysconfdir}/profile.d/dbus-export.sh
}

FILES_${PN} += " \
	${sbindir}/guiclientd.sh \
	${sysconfdir}/installer.xml \
	${systemd_unitdir}/system/installerd.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/installerd.service \
	${systemd_unitdir}/system/guiclientd.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/guiclientd.service \
	${systemd_unitdir}/system/dbus-sess.socket \
	${systemd_unitdir}/system/dbus.target.wants/dbus-sess.socket \
	${systemd_unitdir}/system/sockets.target.wants/dbus-sess.socket \
	${systemd_unitdir}/system/dbus-sess.service \
	${systemd_unitdir}/system/multi-user.target.wants/dbus-sess.service \
	${sysconfdir}/profile.d/dbus-export.sh \
	${sysconfdir}/profile.d/qt4-export.sh "

