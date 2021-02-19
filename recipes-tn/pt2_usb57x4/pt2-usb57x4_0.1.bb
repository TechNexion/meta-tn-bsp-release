FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SUMMARY = "This script creates a usb util to control microchip USB5734"
HOMEPAGE = "https://www.microchip.com/SWLibraryWeb/product.aspx?product=PT2_USB57X4_Linux_SDK"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://libpt2/README;md5=ba82e2d248e9d829eea1a0c90254022a"

inherit pkgconfig

DEPENDS += "libusb1"
RDEPENDS_${PN} += "libusb1"
RDEPENDS_${PN}_append_tools = " bash"

SRC_URI = "file://pt2_usb57x4_linux_sdk_1.0.zip \
           file://0001-libpt2-Modify-Interface-API-to-include-Device-ID-par.patch \
           file://0002-Examples-Ensure-to-pass-in-device-id.patch "
SRC_URI_append_tools = " file://power-cycle.sh file://det-gpio.sh file://pt2usb-util.sh"

S = "${WORKDIR}"

do_compile() {
    cd ${S}/libpt2
    sed -e 's|^LIB_DIR.*|LIB_DIR =|g' -i Makefile
    sed -e 's|^INC_DIR.*|INC_DIR =|g' -i Makefile
    sed -e 's|^CCC.*|CCC = $(CXX)|g' -i Makefile
    sed -e 's|^GCC.*|GCC = CC|g' -i Makefile
    oe_runmake all
    cd -

    USB57x4_EXAMPLES="Flexconnect gpio I2C_Bridging OTPProgrammer register_rw SPI_Bridging"
    for prj in ${USB57x4_EXAMPLES}; do
        cp ${S}/libpt2/libpt2.a ${S}/examples/${prj}
        cd ${S}/examples/${prj}
        sed -e 's|^LIB_DIR.*|LIB_DIR =|g' -i Makefile
        sed -e 's|^INC_DIR.*|INC_DIR = ../../libpt2|g' -i Makefile
        oe_runmake
        cd -
    done
}

do_install() {
    USB57x4_EXAMPLES="Flexconnect gpio I2C_Bridging OTPProgrammer register_rw SPI_Bridging"
    install -d ${D}${sbindir}
    for prj in ${USB57x4_EXAMPLES}; do
        install -m 0755 ${S}/examples/${prj}/out/* ${D}${sbindir}
    done
}

do_install_append_tools() {
    install -m 0755 ${S}/power-cycle.sh ${D}${sbindir}
    install -m 0755 ${S}/det-gpio.sh ${D}${sbindir}
    install -m 0755 ${S}/pt2usb-util.sh ${D}${sbindir}
}

INSANE_SKIP_${PN} = "ldflags"

FILES_${PN} += "${libdir}/*.so.*"
FILES_${PN}-staticdev += "${libdir}/*.a"
FILES_${PN}-dev += "${libdir}/*.so ${libdir}/*.la"
