FILESEXTRAPATHS_prepend := "${THISDIR}/file:"
SUMMARY = "Lato fonts"
HOMEPAGE = "https://www.latofonts.com/lato-free-fonts/"
SECTION = "fonts"
LICENSE = "OFL-1.1"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/OFL-1.1;md5=fac3a519e5e9eb96316656e0ca4f2b90"

SRC_URI = "file://Lato2OFL.zip"
SRC_URI[md5sum] = "3b43a5cb33196ec25e44d5fcb40219e1"
SRC_URI[sha256sum] = "c825453253f590cfe62557733e7173f9a421fff103b00f57d33c4ad28ae53baf"

S = "${WORKDIR}/Lato2OFL/"

do_install () {
	ttfs="Lato-Bold.ttf Lato-Italic.ttf Lato-BoldItalic.ttf Lato-Regular.ttf"
	install -d ${D}/usr/lib/fonts/
	for i in $ttfs; do
		install -m 0644 ${i} ${D}/usr/lib/fonts/${i}
	done
}

FILES_${PN} = "/usr/lib/fonts/*.ttf"

