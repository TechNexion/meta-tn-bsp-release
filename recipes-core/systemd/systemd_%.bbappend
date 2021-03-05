FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append_b2qt =  " file://0001-systemd-disable-getty-.service-in-90-systemd.preset.patch"
SRC_URI_append_rescue =  " file://0001-systemd-disable-getty-.service-in-90-systemd.preset.patch"

do_install_append_rescue () {
	find ${D} -name "getty@*.service" -delete
}
