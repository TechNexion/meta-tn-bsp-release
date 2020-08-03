FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
   file://settings \
"

do_install_append() {
	install -d ${D}${localstatedir}/lib/connman
    install -m 0644 ${WORKDIR}/settings ${D}${localstatedir}/lib/connman
}

COMPATIBLE_MACHINE = "(mx6|mx6ul|mx7|mx8)"
