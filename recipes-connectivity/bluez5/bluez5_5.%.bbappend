FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add patches for QCA modules with Qca6174 and Qca9377-3 chips
SRC_URI += " \
            file://0001-hciattach_rome-do-not-override-module-MAC-address.patch \
            file://0002-hciattach_rome-set-IBS-to-disable-and-PCM-to-slave-b.patch \
            file://0003-hciattach_rome-load-3.2-version-of-firmware-by-defau.patch \
            file://0004-hciattach_rome-fix-baud-rate-synchronization-issue.patch \
            file://0001-hciattach_rome-use-the-same-firmware-path.patch \
            file://serial-qcabtfw@.service \
"

# As this package is tied to systemd, only build it when we're also building systemd.
inherit distro_features_check
REQUIRED_DISTRO_FEATURES = "systemd"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'fcc', 'false', 'true', d)}; then
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
    fi
}
