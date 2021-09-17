#
# Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

SUMMARY = "Script for growing a partition"
DESCRIPTION = "\
This package provides the growpart script for growing a partition. It is \
primarily used in cloud images in conjunction with the dracut-modules-growroot \
package to grow the root partition on first boot. \
"
HOMEPAGE = "https://launchpad.net/cloud-utils"
LICENSE = "GPLv3"

LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "https://launchpad.net/cloud-utils/trunk/${PV}/+download/cloud-utils-${PV}.tar.gz"

SRC_URI[md5sum] = "9b256ff2dbfabaaaf2298d0052eaa5ae"
SRC_URI[sha256sum] = "132255cbda774834695e2912e09b9058d3281a94874be57e48f2f04f4b89ad77"

S = "${WORKDIR}/cloud-utils-${PV}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/bin/growpart ${D}${bindir}
}

RDEPENDS_${PN} = " \
    gawk \
    util-linux \
"
