FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_axon-imx8mm = " file://0001-imx8mm-rdc-assign-uart4-to-a53-domain.patch \
                               file://0002-imx8mm-change-debug-console-from-UART2-to-UART1.patch \
                             "

COMPATIBLE_MACHINE_axon-imx8mm = "axon-imx8mm"
