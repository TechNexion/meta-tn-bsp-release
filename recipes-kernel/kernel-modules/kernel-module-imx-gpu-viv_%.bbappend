# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2021 TechNexion Ltd.
# Copyright 2017-2021 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "tn-imx_5.10.72_2.2.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('SRCBRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCREV = "a054436f9a317a64fa742d4044fa32d76dfa656a"
