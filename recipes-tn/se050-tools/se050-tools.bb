# Copyright (C) 2021 Wig Cheng <wig.cheng@technexion.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "NXP SE050 recipe"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
AUTHOR = "Ettore Chimenti <ettore.chimenti@seco.com> Tommaso Merciai <tommaso.merciai@seco.com>"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit cmake dos2unix pkgconfig deploy

DEPENDS += "python3 systemd openssl"
RDEPENDS:${PN} += "libcrypto"

S = "${WORKDIR}/simw-top"

SRC_URI = "https://github.com/TechNexion-customization/se050-tools/raw/master/SE05x-MW-v04.03.00.zip;md5sum=d8fa4eac5b9ca3701f7dafc8e463c260 \
	file://SE050F2HQ1_scp_keys.txt;md5sum=0ceee41787e383da36fec3bdb9390a70 \
	"

EXTRA_OECMAKE += "\
    -DCMAKE_BUILD_TYPE=Debug \
    -DPTMW_SE05X_Ver=03_XX \
    -DPTMW_Applet=SE05X_C \
    -DPTMW_Host=iMXLinux \
    -DPTMW_HostCrypto=OPENSSL \
    -DPTMW_OpenSSL=3_0 \
    -DPTMW_SMCOM=T1oI2C \
    -DPTMW_FIPS=SE050 \
    -DPTMW_SCP=SCP03_SSS \
    -DSSSFTR_SE05X_RSA=1 \
    -DPTMW_SE05X_Auth=PlatfSCP03 \
    -DSSS_PFSCP_ENABLE_SE050F2_0001A92A=1 \
    "

FILES:${PN} = "${datadir}/se05x \
                ${bindir} \
                ${libdir}/*.so \
                "

FILES:${PN}-dev = "${includedir} ${libdir}/cmake"

do_install() {
	install -d ${D}${datadir}/se05x
	install -m 0644 ${WORKDIR}/SE050F2HQ1_scp_keys.txt ${D}${datadir}/se05x/
}
