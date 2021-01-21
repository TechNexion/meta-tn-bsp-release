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

	# guiclientd.sh
	install -m 755 ${S}/tsl/guiclientd.sh ${D}${sbindir}/guiclientd.sh

	# guiclientd.conf
	# DBUS_SESSION_BUS_ADDRESS=unix:path=/var/run/dbus/session_bus_socket
	# QT_QPA_PLATFORM="wayland"
	# XDG_RUNTIME_DIR="/run/user/0"
	echo "DBUS_SESSION_BUS_ADDRESS=unix:path=/var/run/dbus/session_bus_socket" > ${D}${sysconfdir}/systemd/guiclientd.conf
	echo "QT_QPA_PLATFORM=wayland" >> ${D}${sysconfdir}/systemd/guiclientd.conf
	echo "XDG_RUNTIME_DIR=/run/user/0" >> ${D}${sysconfdir}/systemd/guiclientd.conf

	# tsl-loader guiclientd systemd service files
	if [ "${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)}" ]; then
		sed -e 's,@ENV_FILE@,${sysconfdir}/systemd/guiclientd.conf,g' -i ${S}/tsl/guiclientd.service
		sed -e 's,@EXEC_START@,${bindir}/python3 ${libdir}/python3.7/site-packages/tsl/guiview.py,g' -i ${S}/tsl/guiclientd.service
	else
		sed -e 's,@ENV_FILE@,,g' -i ${S}/tsl/guiclientd.service
		sed -e 's,@EXEC_START@,/usr/sbin/guiclientd.sh,g' -i ${S}/tsl/guiclientd.service
	fi
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
	${sysconfdir}/systemd/system/guiclientd.conf \
	${systemd_unitdir}/system/guiclientd.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/guiclientd.service \
	${systemd_unitdir}/system/dbus-sess.socket \
	${systemd_unitdir}/system/dbus.target.wants/dbus-sess.socket \
	${systemd_unitdir}/system/sockets.target.wants/dbus-sess.socket \
	${systemd_unitdir}/system/dbus-sess.service \
	${systemd_unitdir}/system/multi-user.target.wants/dbus-sess.service \
	${sysconfdir}/profile.d/dbus-export.sh \
	${sysconfdir}/profile.d/qt4-export.sh "

