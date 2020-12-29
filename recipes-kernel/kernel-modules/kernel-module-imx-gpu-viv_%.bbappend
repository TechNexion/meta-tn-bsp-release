# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2018 TechNexion Ltd.
# Copyright 2017-2018 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "tn-imx_5.4.70_2.3.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('SRCBRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCREV = "b8471e3a8a528c31ba08cb8aea5330b330763586"
