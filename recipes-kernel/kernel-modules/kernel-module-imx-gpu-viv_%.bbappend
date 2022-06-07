# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2021 TechNexion Ltd.
# Copyright 2017-2021 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "tn-imx_5.15.5_1.0.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('SRCBRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCREV = "539e9f4741b1844f0f23e63a5a4efb48341dd820"
