#!/bin/sh

get_mode () {
  IFNAME=${1##*/}
  RESOL=$(cat $1/modes | head -n 1)
  WIDTH=${RESOL//x*}
  HEIGHT=${RESOL//*x}
}

for card in /sys/class/drm/card?-*
do
  get_mode $card
  if [ -f /etc/xdg/weston/weston.ini -a -n $RESOL ]; then
    # check existing core section for cardX
    if grep -q "^drm-device=" /etc/xdg/weston/weston.ini; then
      sed -e 's,drm-device.*,drm-device='${IFNAME%%-*}',g' -i /etc/xdg/weston/weston.ini
    fi

    if ! grep -q "^name=${IFNAME#*-}" /etc/xdg/weston/weston.ini || ! grep -q "^mode=${RESOL}" /etc/xdg/weston/weston.ini ; then
        # If the interface name and resolution in weston.ini doesn't match the settings detected by DRM,
        # then, clear the old settings in westion.ini and create a new one.

        # clear [output] display section
        sed -i '/^\[output\]/,/^$/d' /etc/xdg/weston/weston.ini

        # enable cardX output section
        echo >> /etc/xdg/weston/weston.ini
        echo -e "[output]" >> /etc/xdg/weston/weston.ini
        echo "name=${IFNAME#*-}" >> /etc/xdg/weston/weston.ini
        echo "mode=${RESOL}@60" >> /etc/xdg/weston/weston.ini
        if [ $HEIGHT -gt $WIDTH ]; then
          echo "transform=rotate-270" >> /etc/xdg/weston/weston.ini
        fi
        sync
    fi

  fi
done
