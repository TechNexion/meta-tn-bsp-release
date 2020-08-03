FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://terminal.png \
"

do_install_append() {
    install -d ${D}${datadir}/weston
    install ${WORKDIR}/terminal.png ${D}${datadir}/weston
}
