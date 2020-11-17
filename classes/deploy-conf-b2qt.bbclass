############################################################################
##
## Copyright (C) 2018 The Qt Company Ltd.
## Contact: https://www.qt.io/licensing/
##
## This file is part of the Boot to Qt meta layer.
##
## $QT_BEGIN_LICENSE:GPL$
## Commercial License Usage
## Licensees holding valid commercial Qt licenses may use this file in
## accordance with the commercial license agreement provided with the
## Software or, alternatively, in accordance with the terms contained in
## a written agreement between you and The Qt Company. For licensing terms
## and conditions see https://www.qt.io/terms-conditions. For further
## information use the contact form at https://www.qt.io/contact-us.
##
## GNU General Public License Usage
## Alternatively, this file may be used under the terms of the GNU
## General Public License version 3 or (at your option) any later version
## approved by the KDE Free Qt Foundation. The licenses are as published by
## the Free Software Foundation and appearing in the file LICENSE.GPL3
## included in the packaging of this file. Please review the following
## information to ensure the GNU General Public License requirements will
## be met: https://www.gnu.org/licenses/gpl-3.0.html.
##
## $QT_END_LICENSE$
##
############################################################################

inherit image_types

do_image_conf[depends] += "qtbase-native:do_populate_sysroot"

DEPLOY_CONF_NAME ?= "${MACHINE}"
DEPLOY_CONF_TYPE ?= "Boot2Qt"

IMAGE_CMD_conf() {
    QT_VERSION=$(qmake -query QT_VERSION)
    cat > ${IMGDEPLOYDIR}/${IMAGE_NAME}.conf <<EOF
[${DEPLOY_CONF_NAME}]
platform=${MACHINE}
product=${DEPLOY_CONF_TYPE}
qt=Qt $QT_VERSION
os=linux
imagefile=${IMAGE_LINK_NAME}.sdimg
asroot=true
EOF
}
