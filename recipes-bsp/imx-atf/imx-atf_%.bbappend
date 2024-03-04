FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:axon-imx8mm = " file://0001-imx8mm-rdc-assign-uart4-to-a53-domain.patch \
                               file://0002-imx8mm-change-debug-console-from-UART2-to-UART1.patch \
                             "
SRC_URI:append:pico-imx8mm = " file://0003-pico-imx8mmq-poweroff-fail.patch"
SRC_URI:append:pico-imx8mq = " file://0003-pico-imx8mmq-poweroff-fail.patch"
SRC_URI:append:tek-imx8mp = " file://0003-pico-imx8mmq-poweroff-fail.patch"

COMPATIBLE_MACHINE:axon-imx8mm = "axon-imx8mm"
