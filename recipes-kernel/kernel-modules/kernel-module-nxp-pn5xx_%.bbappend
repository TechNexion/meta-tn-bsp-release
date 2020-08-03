# Copyright 2018 TechNexion Ltd.
SUMMARY = "Driver for NXP-nTAG pn544"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2c1c00f9d3ed9e24fa69b932b7e7aff2"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0003-pn544_enable.patch"

