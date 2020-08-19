#!/bin/sh
#
# i.MX Yocto Project Build Environment Setup Script
#
# Copyright (C) 2011-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Copyright 2020 TechNexion Ltd.
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

CWD=`pwd`
PROGNAME="$CWD/sources/meta-tn-imx-bsp/tools/setup-environment.sh"
exit_message ()
{
   echo "To return to this build environment later please run:"
   echo "    source setup-environment <build_dir>"

}

usage()
{
    echo -e "\nUsage: source tn-setup-release.sh
    Optional parameters: [-b build-dir] [-h]"
echo "
    * [-b build-dir]: Build directory, if unspecified script uses 'build' as output directory
    * [-h]: help
"
}


clean_up()
{
    unset OLD_OPTIND
    unset OPTIND
    unset CPU_TYPE
    unset CWD BUILD_DIR FSLDISTRO
    unset fsl_setup_help fsl_setup_error fsl_setup_flag
    unset usage clean_up
    unset ARM_DIR META_FSL_BSP_RELEASE
    exit_message clean_up
}

# get command line options
OLD_OPTIND=$OPTIND
unset FSLDISTRO

while getopts "k:r:t:b:e:gh" fsl_setup_flag
do
    case $fsl_setup_flag in
        b) BUILD_DIR="$OPTARG";
           echo -e "\nBuild directory: $BUILD_DIR"
           ;;
        h) fsl_setup_help='true';
           ;;
        \?) fsl_setup_error='true';
           ;;
    esac
done
shift $((OPTIND-1))
if [ $# -ne 0 ]; then
    fsl_setup_error=true
    echo -e "Invalid command line ending: '$@'"
fi
OPTIND=$OLD_OPTIND
if test $fsl_setup_help; then
    usage && clean_up && return 1
elif test $fsl_setup_error; then
    clean_up && return 1
fi


if [ -z "$DISTRO" ]; then
    if [ -z "$FSLDISTRO" ]; then
        FSLDISTRO='fsl-imx-xwayland'
    fi
else
    FSLDISTRO="$DISTRO"
fi

if [ -z "$BUILD_DIR" ]; then
    BUILD_DIR='build'
fi

if [ -z "$MACHINE" ]; then
    echo setting to default machine
    MACHINE='imx6qpsabresd'
fi

case $MACHINE in
imx8*)
    case $DISTRO in
    *wayland)
        : ok
        ;;
    *)
        echo -e "\n ERROR - Only Wayland distros are supported for i.MX 8 or i.MX 8M"
        echo -e "\n"
        return 1
        ;;
    esac
    ;;
*)
    : ok
    ;;
esac

# Cleanup previous meta-freescale/EULA overrides
cd $CWD/sources/meta-freescale
if [ -h EULA ]; then
    echo Cleanup meta-freescale/EULA...
    git checkout -- EULA
fi
if [ ! -f classes/fsl-eula-unpack.bbclass ]; then
    echo Cleanup meta-freescale/classes/fsl-eula-unpack.bbclass...
    git checkout -- classes/fsl-eula-unpack.bbclass
fi
cd -

# Override the click-through in meta-freescale/EULA
FSL_EULA_FILE=$CWD/sources/meta-imx/EULA.txt

# Set up the basic yocto environment by calling our setup-environment.sh
if [ -n "$FSLDISTRO" ]; then
  echo "TechNexion Setup BSP Release: source TechNexion setup-environment.sh wrapper scripts"
  echo "    MACHINE=$MACHINE FSLDISTRO=$FSLDISTRO source $PROGNAME $BUILD_DIR"
  echo ""
  DISTRO=$FSLDISTRO MACHINE=$MACHINE source $PROGNAME $BUILD_DIR
elif [ -n "$DISTRO" ]; then
  echo "TechNexion Setup BSP Release: Source TechNexion setup-environment.sh wrapper scripts"
  echo "    MACHINE=$MACHINE DISTRO=$DISTRO source $PROGNAME $BUILD_DIR"
  echo ""
  DISTRO=$DISTRO MACHINE=$MACHINE source $PROGNAME $BUILD_DIR
else
  echo "TechNexion Setup BSP Release: Source TechNexion setup-environment.sh wrapper scripts"
  echo "    MACHINE=$MACHINE source $PROGNAME $BUILD_DIR"
  echo ""
  MACHINE=$MACHINE source $PROGNAME $BUILD_DIR
fi

echo -e "\nTechNexion Setup BSP Release: Further modification to local.conf and bblayers.conf"
# Point to the current directory since the source setup-environment.sh changed the directory to $BUILD_DIR
BUILD_DIR=.

if [ ! -e $BUILD_DIR/conf/local.conf ]; then
    echo -e "\nERROR - No build directory is set yet. Run the 'setup-environment' script before running this script to create " $BUILD_DIR
    echo -e "\n"
    return 1
fi

# On the first script run, backup the local.conf file
# Consecutive runs, it restores the backup and changes are appended on this one.
if [ ! -e $BUILD_DIR/conf/local.conf.org ]; then
    cp $BUILD_DIR/conf/local.conf $BUILD_DIR/conf/local.conf.org
else
    cp $BUILD_DIR/conf/local.conf.org $BUILD_DIR/conf/local.conf
fi

echo >> conf/local.conf
echo "# Switch to Debian packaging and include package-management in the image" >> conf/local.conf
echo "PACKAGE_CLASSES = \"package_deb\"" >> conf/local.conf
echo "EXTRA_IMAGE_FEATURES += \"package-management\"" >> conf/local.conf

if [ ! -e $BUILD_DIR/conf/bblayers.conf.org ]; then
    cp $BUILD_DIR/conf/bblayers.conf $BUILD_DIR/conf/bblayers.conf.org
else
    cp $BUILD_DIR/conf/bblayers.conf.org $BUILD_DIR/conf/bblayers.conf
fi

# Pass in the extra variables for uEnv.txt recipe
if [ -n "$TOKEN" ]; then
  echo "Specified PA_TOKEN: $TOKEN"
  export PA_TOKEN=$TOKEN
  if ! grep -qF "PA_TOKEN" <<< $BB_ENV_EXTRAWHITE; then
    echo "Export PA_TOKEN=$TOKEN to yocto via BB_ENV_EXTRAWHITE"
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE PA_TOKEN"
  fi
fi
if [ -n "$DISPLAY" ]; then
  echo "Specified DISPLAY_INFO: $DISPLAY"
  export DISPLAY_INFO=$DISPLAY
  if ! grep -qF "DISPLAY_INFO" <<< $BB_ENV_EXTRAWHITE; then
    echo "Export DISPLAY_INFO=$DISPLAY to yocto via BB_ENV_EXTRAWHITE"
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE DISPLAY_INFO"
  fi
fi
if [ -n "$PANEL" ]; then
    export DISPLAY_PANEL=$PANEL
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE DISPLAY_PANEL"
fi
if [ -n "$BASEBOARD" ]; then
  echo "Specified BASE_BOARD: $BASEBOARD"
  export BASE_BOARD=$BASEBOARD
  if ! grep -qF "BASE_BOARD" <<< $BB_ENV_EXTRAWHITE; then
    echo "Export BASE_BOARD=$BASEBOARD to yocto via BB_ENV_EXTRAWHITE"
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE BASE_BOARD"
  fi
fi
if [ -n "$FDTNAME" ]; then
    export ALT_FDTNAME=$FDTNAME
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE ALT_FDTNAME"
fi

# Identify SOC type
CPU_TYPE=$(echo $MACHINE | sed 's/.*-\(imx[5-8][a-z]*\)[- $]*.*/\1/g')

# Choose corresponding firmware package for different WLAN (QCA or BRCM), e.g. 'linux-firmware-brcm-tn' or 'linux-firmware-qca-tn'
if [ -z "${WIFI_FIRMWARE#"${WIFI_FIRMWARE%%[! ]*}"}" ]; then
    echo "WARNING - No WIFI_FIRMWARE specified"
    RF_FIRMWARES=""
else
    if [ "$WIFI_FIRMWARE" == "all" ]; then
        if [ "$CPU_TYPE" == "imx8mq" ] || [ "$CPU_TYPE" == "imx8mm" ] || [ "$CPU_TYPE" == "imx8mp" ]; then
            echo "WARNING - imx8mq/imx8mm/imx8mp SOM only supports qca wireless module, so load qca firmware"
            RF_FIRMWARES="qca ath-pci"
        elif [ "$CPU_TYPE" == "imx6" ] || [ "$CPU_TYPE" == "imx7" ] || [ "$CPU_TYPE" == "imx6ul" ]; then
            RF_FIRMWARES="qca brcm ath-pci"
        else
            echo "WARNING - No matched CPU_TYPE: $CPU_TYPE, hence no WIFI_FIRMWARE"
            RF_FIRMWARES=""
        fi
    elif [ "$WIFI_FIRMWARE" == "y" ] || [ "$WIFI_FIRMWARE" == "Y" ]; then
        if [ "$CPU_TYPE" == "imx8mq" ] || [ "$CPU_TYPE" == "imx8mm" ] || [ "$CPU_TYPE" == "imx8mp" ]; then
            echo "WARNING - imx8mq/imx8mm/imx8mp SOM only supports qca wireless module, so load qca firmware"
            RF_FIRMWARES="qca"
        else
            if  [ -z "${WIFI_MODULE#"${WIFI_MODULE%%[! ]*}"}" ]; then
                echo "WARNING - No WIFI_MODULE specified."
                RF_FIRMWARES=""
            else
                RF_FIRMWARES=$WIFI_MODULE
            fi
        fi
    else
        echo "WARNING - Unrecognized WIFI_FIRMWARE specified"
        RF_FIRMWARES=""
    fi
fi
if [ -n "$RF_FIRMWARES" ]; then
  echo "Specified wifi firmwares: $RF_FIRMWARES"
  export RF_FIRMWARES=$RF_FIRMWARES
  if ! grep -qF "RF_FIRMWARES" <<< $BB_ENV_EXTRAWHITE; then
    echo "Export RF_FIRMWARES=$RF_FIRMWARES to yocto via BB_ENV_EXTRAWHITE"
    export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE RF_FIRMWARES"
  fi
fi

# Support integrating community meta-freescale instead of meta-fsl-arm
if [ -d ../sources/meta-freescale ]; then
    echo "meta-freescale directory found, so use freescale community bsp bblayer instead."
    # Change settings according to environment
    sed -e "s,meta-fsl-arm\s,meta-freescale ,g" -i conf/bblayers.conf
    sed -e "s,\$.BSPDIR./sources/meta-fsl-arm-extra\s,,g" -i conf/bblayers.conf
fi

cd $BUILD_DIR
clean_up

