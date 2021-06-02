# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2021 TechNexion Ltd.
# Copyright 2017-2021 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "tn-imx_5.10.9_1.0.0-next"
LOCALVERSION = "${@'-%s' % '-'.join(d.getVar('SRCBRANCH', True).split('_')[2:]).lower()}"
KERNEL_SRC = "git://github.com/TechNexion/linux-tn-imx.git;protocol=https"
SRCREV = "c51aa2337f1b313ccb1f1c3c0dd083b7aa30eac2"
