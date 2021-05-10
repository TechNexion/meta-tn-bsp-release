SUMMARY = "Firmware files for Axon Fabric"
SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_FLAGS = "commercial_tn"
LIC_FILES_CHKSUM = "\
    file://tn-license.txt;md5=e45d720ee54cbc714f0698499845fa04 \
"

require axonfabric-firmware.inc

SRCBRANCH = "21.05.02"

COMPATIBLE_MACHINE = "(mx6|mx8)"
