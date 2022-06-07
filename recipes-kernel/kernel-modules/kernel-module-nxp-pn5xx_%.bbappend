# Copyright 2018 TechNexion Ltd.
SUMMARY = "Driver for NXP-nTAG pn544"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2c1c00f9d3ed9e24fa69b932b7e7aff2"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "5cabbc58ff17f6023b4d0d217cdfb31b63f737c4"

SRC_URI += " file://0003-pn544_enable.patch \
             file://0001-Use-pr_warn-instead-of-pr_warning-to-adapt-to-Linux-.patch"

