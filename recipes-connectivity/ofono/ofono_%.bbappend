FILESEXTRAPATHS_prepend := "${THISDIR}/ofono:"

PV = "1.31"

SRC_URI_remove = "file://use-python3.patch"
SRC_URI += " \
            file://0002-plugins-udevng-add-support-for-Huawei-ME906s.patch \
            file://0003-plugins-udevng-add-support-for-Telit-LE910-EU-V2.patch \
            file://0001-plugins-udevng-add-support-for-Fibocom-NL668-EU.patch \
            "

SRC_URI[md5sum] = "1c26340e3c6ed132cc812595081bb3dc"
SRC_URI[sha256sum] = "a15c5d28096c10eb30e47a68b6dc2e7c4a5a99d7f4cfedf0b69624f33d859e9b"

DEPENDS += "ell"

EXTRA_OECONF += "--enable-external-ell"
