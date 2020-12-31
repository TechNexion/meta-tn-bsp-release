#!/bin/sh
#
# i.MX Yocto Project Build Environment Setup Script
#
# Copyright (C) 2011-2016 Freescale Semiconductor
# Copyright 2018 TechNexion Ltd.
# Copyright 2017 NXP
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

. sources/meta-imx/tools/setup-utils.sh

CWD=`pwd`
if [ -d "$CWD/sources/meta-boot2qt" ]; then
  # Check if MACHINE has already been exported, if not fail out
  ENVVARS=`printenv`
  if grep -q "MACHINE=" <<< $ENVVARS; then
	PROGNAME="$CWD/sources/meta-boot2qt/scripts/setup-environment.sh"
	# Get TechNexion MACHINE configs from boot2qt
	TNCONFIGS=$(ls $CWD/sources/meta-boot2qt/meta-boot2qt-distro/conf/distro/include/*.conf | xargs -n 1 basename | grep -E -c "$MACHINE")
  else
    echo -e "Try to setup boot2qt build environment. Please export MACHINE=xxx first."
    return 1
  fi
else
  PROGNAME="$CWD/sources/base/setup-environment"
  # Get TechNexion MACHINE configs from yocto
  TNCONFIGS=$(ls $CWD/sources/meta-tn-imx-bsp/conf/machine/*.conf | xargs -n 1 basename | grep -E -c "$MACHINE")
fi

usage ()
{
  echo -e "Usage: . $0 [build directory]"
}

if [ $? != 0 -o $# -lt 1 ]; then
  usage
  return 1
fi

while test -n "$1"; do
  case "$1" in
    "--help" | "-h")
      usage
      return 0
      ;;
    *)
      BUILDDIRECTORY=$1
    ;;
  esac
  shift
done

THIS_SCRIPT="setup-environment.sh"
if [ "$(basename -- $0)" = "${THIS_SCRIPT}" ]; then
    echo "Error: This script needs to be sourced. Please run as '. $0'"
    return 1
fi

if [ -z "$MACHINE" ]; then
  echo "Error: MACHINE environment variable not defined"
  return 1
fi

if [ ! -d "$CWD/sources/meta-boot2qt" ]; then
  if [ -z "$DISTRO" ]; then
      echo "Error: DISTRO environment variable not defined"
      return 1
  fi
fi

# Get i.MX MACHINE configs
FSLCONFIGS=$(ls $CWD/sources/meta-imx/meta-bsp/conf/machine/*.conf $CWD/sources/meta-freescale*/conf/machine/*.conf | xargs -n 1 basename | grep -E -c "$MACHINE")
# Set up the basic yocto environment by sourcing fsl community's setup-environment bash script with/without TEMPLATECONF
if [ -n "${DISTRO}" ]; then
  if [ ${TNCONFIGS} != 0 ]; then
    echo "Setup TechNexion Yocto"
    echo "    TEMPLATECONF=$CWD/sources/meta-tn-imx-bsp/conf MACHINE=$MACHINE DISTRO=$DISTRO source $PROGNAME $BUILDDIRECTORY"
    echo ""
    TEMPLATECONF="$CWD/sources/meta-tn-imx-bsp/conf" MACHINE=$MACHINE DISTRO=$DISTRO source $PROGNAME $BUILDDIRECTORY
  elif [ ${FSLCONFIGS} != 0 ]; then
    echo "Setup Freescale/i.MX Yocto"
    echo "    MACHINE=$MACHINE DISTRO=$DISTRO source $PROGNAME $BUILDDIRECTORY"
    echo ""
    MACHINE=$MACHINE DISTRO=$DISTRO source $PROGNAME $BUILDDIRECTORY
  fi
else
  if [ ${TNCONFIGS} != 0 ]; then
    echo "Setup Boot2qt"
    echo "    export MACHINE=$MACHINE"
    echo "    source $PROGNAME $BUILDDIRECTORY"
    echo ""
    source $PROGNAME $BUILDDIRECTORY
  else
    echo "Setup OpenEmbedded Yocto"
    echo "    MACHINE=$MACHINE source $PROGNAME $BUILDDIRECTORY"
    echo ""
    MACHINE=$MACHINE source $PROGNAME $BUILDDIRECTORY
  fi
fi

#
# For some reason, our modified original bblayers.conf.sample are replaced by
# FSL community's base/conf/bblayer.conf
# So workaround by appending additional layers
#

# both imx and technexion MACHINE configs
echo -e "\n# TechNexion setup-environment.sh wrapper: Further modification to bblayers.conf and local.conf" | tee -a conf/local.conf

if [ ${TNCONFIGS} != 0 -o ${FSLCONFIGS} != 0 ]; then
  if [ -d $PWD/../sources/meta-imx ]; then
    # copy new EULA into community so setup uses latest i.MX EULA
    cp $PWD/../sources/meta-imx/EULA.txt $PWD/../sources/meta-freescale/EULA
    if ! grep -Fq "meta-imx" $PWD/conf/bblayers.conf; then
      # add i.MX bsp layers to bblayers.conf
      echo -e "\n# setup i.MX Yocto Project Release layers in bblayers.conf" | tee -a $PWD/conf/bblayers.conf
      hook_in_layer meta-imx/meta-bsp
      hook_in_layer meta-imx/meta-sdk
      hook_in_layer meta-imx/meta-ml
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-nxp-demo-experience \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-openembedded/meta-gnome \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-openembedded/meta-networking \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-openembedded/meta-filesystems \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-python2 \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-browser \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-clang \"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-rust\"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-qt5 \"" >> $PWD/conf/bblayers.conf
    fi
  fi
  if [ -d ${PWD}/../sources/meta-ivi ]; then
	if ! grep -Fq "meta-ivi" $PWD/conf/bblayers.conf; then
      echo -e "\n# setup Genivi layers in bblayers.conf" | tee -a $PWD/conf/bblayers.conf
      echo "BBLAYERS += \"\${BSPDIR}/sources/meta-gplv2\"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi\"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi-bsp\"" >> $PWD/conf/bblayers.conf
      echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi-test\"" >> $PWD/conf/bblayers.conf
    fi
  fi
fi

# technexion MACHINE configs
if [ ${TNCONFIGS} != 0 ] ; then
  # add technexion bsp layers to bblayers.conf
  if [ -d $PWD/../sources/meta-tn-imx-bsp ]; then
    if ! grep -Fq "meta-tn-imx-bsp" $PWD/conf/bblayers.conf; then
      echo "" >> $PWD/conf/bblayers.conf
      echo "# setup Technexion i.MX Yocto Project Release Layers in bblayers.conf" | tee -a $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-tn-imx-bsp \"" >> $PWD/conf/bblayers.conf
    fi
  fi
  # add technexion nfc bsp layers (from nxp) to bblayers.conf
  if [ -d $PWD/../sources/meta-nxp-nfc ]; then
    if ! grep -Fq "meta-nxp-nfc" $PWD/conf/bblayers.conf; then
      echo "" >> $PWD/conf/bblayers.conf
      echo "# setup NXP nfc release layer in bblayers.conf" | tee -a $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-nxp-nfc \"" >> $PWD/conf/bblayers.conf
    fi
  fi
  # add technexion virtualization bsp layers (virtualization/boot2qt) to bblayers.conf
  if [ -d $PWD/../sources/meta-virtualization ]; then
    # has meta-virtualization
    if ! grep -Fq "meta-virtualization" $PWD/conf/bblayers.conf; then
      echo "" >> $PWD/conf/bblayers.conf
      echo "# setup i.MX Container OS and OTA layers in bblayers.conf" | tee -a $PWD/conf/bblayers.conf
      echo "BBLAYERS += \" \${BSPDIR}/sources/meta-virtualization \"" >> $PWD/conf/bblayers.conf
    fi
    if ! grep -Fq "BBMULTICONFIG" $PWD/conf/local.conf; then
      mkdir -p $PWD/conf/multiconfig
      cat > $PWD/conf/multiconfig/container.conf << EOF
MACHINE = "tn-container"
DISTRO = "fsl-imx-xwayland"
DISTRO_FEATURES_append = " virtualization"
TMPDIR = "\${TOPDIR}/tmp-container"
TN_CONTAINER_IMAGE_TYPE ?= "tar.gz"
EOF
      echo "TN_CONTAINER_IMAGE_TYPE = \"tar.gz\"" >> $PWD/conf/local.conf
      echo "BBMULTICONFIG = \"container\"" >> $PWD/conf/local.conf
      echo "# setup BBMULTICONFIG in local.conf with conf/multiconfig/container.conf"
      cat $PWD/conf/multiconfig/container.conf
    fi
  else
    # no meta-virtualization
    if ! grep -Fq "meta-tn-imx-bsp/recipes-containers/docker-disk/docker-disk.bb" $PWD/conf/local.conf; then
      echo "BBMASK += \"meta-tn-imx-bsp/recipes-containers/docker-disk/docker-disk.bb\"" >> $PWD/conf/local.conf
    fi
    if ! grep -Fq "meta-tn-imx-bsp/recipes-containers/docker/docker-ce_%.bbappend" $PWD/conf/local.conf; then
      echo "BBMASK += \"meta-tn-imx-bsp/recipes-containers/docker/docker-ce_%.bbappend\"" >> $PWD/conf/local.conf
    fi
    # for boot2qt
    if grep -Fq "DISTRO ?= \"b2qt\"" $PWD/conf/local.conf; then
      if ! grep -Fq "meta-tn-imx-bsp/recipes-graphics/wayland/weston_%.bbappend" $PWD/conf/local.conf; then
        echo "BBMASK += \"meta-tn-imx-bsp/recipes-graphics/wayland/weston_%.bbappend\"" >> $PWD/conf/local.conf
      fi
      if ! grep -Fq "meta-tn-imx-bsp/recipes-qt/qt5/qtbase_%.bbappend" $PWD/conf/local.conf; then
        echo "BBMASK += \"meta-tn-imx-bsp/recipes-qt/qt5/qtbase_%.bbappend\"" >> $PWD/conf/local.conf
      fi
    fi
  fi
fi

unset CWD
unset PROGNAME
unset THIS_SCRIPT
unset TNCONFIGS
unset FSLCONFIGS
unset MACHINE
unset DISTRO
unset BUILDDIRECTORY
unset TEMPLATECONF
unset LAYERSCONF
