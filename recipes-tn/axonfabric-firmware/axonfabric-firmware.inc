TECHNEXION_GITHUB_MIRROR ?= "git://github.com/TechNexion/axonfabric-firmware.git"
SRCREV = "136984cd1e8f30543a56342467353d6dabd52bf5"

SRC_URI = "${TECHNEXION_GITHUB_MIRROR};branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

FWDIR = "/lib/firmware/axonfabric"

do_install() {
    install -d ${D}${FWDIR}
	install -m 0755 ${S}/${SRCBRANCH}/axon_fabric_imx8mm_f01-${SRCBRANCH}.jed ${D}${FWDIR}
	install -m 0755 ${S}/${SRCBRANCH}/axon_fabric_imx8mm_f03-${SRCBRANCH}.jed ${D}${FWDIR}
}

FILES_${PN}-dbg += "${FWDIR}/.debug"
FILES_${PN} += "${FWDIR}/"
