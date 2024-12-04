# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2021 TechNexion Ltd.
# Copyright 2017-2021 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "tn-imx_6.6.52_2.2.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('SRCBRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCREV = "6cf80ede731e17099c83b529c0b2e212f3c892e0"
