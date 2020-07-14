PACKAGECONFIG_remove_rescue = "vconsole"

do_install_append_rescue () {
	find ${D} -name "getty@*.service" -delete
}
