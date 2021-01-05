#!/bin/sh

get_mode () {
  RESOL=$(cat $1/modes | head -n 1)
  WIDTH=${RESOL//x*}
  HEIGHT=${RESOL//*x}
}

for card in /sys/class/drm/card?-*
do
  get_mode $card
  if [ -f /etc/xdg/weston/weston.ini -a -n $RESOL ]; then
    if ! grep -q "^name=${card#*-}" /etc/xdg/weston/weston.ini ; then
      echo -e "\n[output]" >> /etc/xdg/weston/weston.ini
      echo "name=${card#*-}" >> /etc/xdg/weston/weston.ini
      echo "mode=${RESOL}@60" >> /etc/xdg/weston/weston.ini
      if [ $HEIGHT -gt $WIDTH ]; then
        echo "transform=rotate-270" >> /etc/xdg/weston/weston.ini
      fi
    fi
  fi
done
