# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Tweaked hciattach utility for ubuntu"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://hciattach-qca \
          file://serial-qcabtfw@.service \
"
S = "${WORKDIR}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
inherit features_check
REQUIRED_DISTRO_FEATURES = "systemd"

FILES:${PN} = " ${libdir}/systemd/* \
              ${sysconfdir}/systemd/* \
              ${bindir}/* \
"

do_install() {

	if [ ! -z "${SERIAL_BLUETOOTH}" ] ; then
		default_baudrate=`echo "${SERIAL_BLUETOOTH}" | sed 's/\;.*//'`
		install -d ${D}${systemd_unitdir}/system/
		install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
		install -m 0644 ${WORKDIR}/serial-qcabtfw@.service ${D}${systemd_unitdir}/system/
		sed -i -e s/\@BAUDRATE\@/$default_baudrate/g ${D}${systemd_unitdir}/system/serial-qcabtfw@.service

		tmp="${SERIAL_BLUETOOTH}"
		for entry in $tmp ; do
			baudrate=`echo $entry | sed 's/\;.*//'`
			ttydev=`echo $entry | sed -e 's/^[0-9]*\;//' -e 's/\;.*//'`
			if [ "$baudrate" = "$default_baudrate" ] ; then
				# enable the service
				ln -sf ${systemd_unitdir}/system/serial-qcabtfw@.service \
				${D}${sysconfdir}/systemd/system/multi-user.target.wants/serial-qcabtfw@$ttydev.service
			else
				# install custom service file for the non-default baudrate
				install -m 0644 ${WORKDIR}/serial-qcabtfw@.service ${D}${systemd_unitdir}/system/serial-qcabtfw$baudrate@.service
				sed -i -e s/\@BAUDRATE\@/$baudrate/g ${D}${systemd_unitdir}/system/serial-qcabtfw$baudrate@.service
				# enable the service
				ln -sf ${systemd_unitdir}/system/serial-qcabtfw$baudrate@.service \
				${D}${sysconfdir}/systemd/system/multi-user.target.wants/serial-qcabtfw$baudrate@$ttydev.service
			fi
		done
	fi

	install -d ${D}${bindir}
	install -m 0755 ${S}/hciattach-qca ${D}${bindir}
}
