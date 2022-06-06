FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://terminal.png \
"

do_install:append() {
    install -d ${D}${datadir}/weston
    install ${WORKDIR}/terminal.png ${D}${datadir}/weston
}
