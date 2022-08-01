FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
   file://settings \
"

do_install:append() {
	install -d ${D}${localstatedir}/lib/connman
    install -m 0644 ${WORKDIR}/settings ${D}${localstatedir}/lib/connman
}

COMPATIBLE_MACHINE = "(mx6-nxp-bsp|mx6ul-nxp-bsp|mx7-nxp-bsp|mx8-nxp-bsp)"
