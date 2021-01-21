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
    # enable cardX output section
    if ! grep -q "^name=${IFNAME#*-}" /etc/xdg/weston/weston.ini ; then
      echo -e "\n[output]" >> /etc/xdg/weston/weston.ini
      echo "name=${IFNAME#*-}" >> /etc/xdg/weston/weston.ini
      echo "mode=${RESOL}@60" >> /etc/xdg/weston/weston.ini
      if [ $HEIGHT -gt $WIDTH ]; then
        echo "transform=rotate-270" >> /etc/xdg/weston/weston.ini
      fi
    fi
  fi
done
