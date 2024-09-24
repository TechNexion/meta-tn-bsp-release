FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

NNSHARK_SRC = "git://github.com/nxp-imx/nnshark.git;protocol=https"
SRCBRANCH = "2023.11.imx"
SRC_URI = "${NNSHARK_SRC};branch=${SRCBRANCH};nobranch=1 \
    file://0001-git-change-to-using-https-protocol-for-download-subm.patch \
"
do_configure[network] = "1"

do_configure:prepend() {
    git submodule update --init --recursive
}
