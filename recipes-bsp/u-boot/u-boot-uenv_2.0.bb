SUMMARY = "u-boot uEnv.txt"
DESCRIPTION = "u-boot uEnv.txt"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README;md5=84bf3d71eb40db8daf2ba7e156e31099"

SRC_URI += " \
   file://README \
"
S = "${WORKDIR}"

inherit deploy

# bitbake style python functions
python do_setuenv() {
    def parse_baseboard(mach, bboard):
        supported_boards = {"pico-imx6": ("pi", "dwarf", "hobbit", "nymph"), \
                            "pico-imx7": ("pi", "dwarf", "hobbit", "nymph"), \
                            "pico-imx6ul": ("pi", "dwarf", "hobbit", "nymph"), \
                            "pico-imx8mq": ("pi"), \
                            "pico-imx8mm": ("pi"), \
                            "flex-imx8mm": ("pi"), \
                            "edm-imx6": ("gnome", "fairy", "tc0700", "tc1000"), \
                            "edm-imx7": ("gnome"), \
                            "edm-imx8mq": ("wizard")}
        default_boards = {"pico-imx6": "pi", \
                          "pico-imx7": "pi", \
                          "pico-imx6ul": "pi", \
                          "pico-imx8mq": "pi", \
                          "pico-imx8mm": "pi", \
                          "flex-imx8mm": "pi", \
                          "edm-imx6": "fairy", \
                          "edm-imx7": "gnome", \
                          "edm-imx8mq": "wizard", \
                          "tep1-imx7": None, \
                          "tep1-imx6ul": None, \
                          "tek-imx6": None, \
                          "tek3-imx6ul": None}
        if mach in supported_boards.keys():
            if bboard in supported_boards[mach]:
                return bboard
        if mach in default_boards.keys():
            return default_boards[mach]
        if len(bboard) > 0:
            return bboard
        return None

    def gen_videoarg(index, disp):
        if disp == "custom":
            return "video=mxcfb{}:dev=hdmi,1280x720M@60,if=RGB24 fbmem=28M ".format(index)
        elif disp == "hdmi":
            return "video=mxcfb{}:dev=hdmi,1280x720M@60,if=RGB24 fbmem=28M ".format(index)
        elif disp == "hdmi1080p":
            return "video=mxcfb{}:dev=hdmi,1920x1080M@60,if=RGB24 fbmem=28M ".format(index)
        elif disp == "hdmi720p":
            return "video=mxcfb{}:dev=hdmi,1280x720M@60,if=RGB24 fbmem=28M ".format(index)
        elif disp == "lcd":
            return "video=mxcfb{}:dev=lcd,800x480@60,if=RGB24 ".format(index)
        elif disp == "lvds10":
            return "video=mxcfb{}:dev=ldb,1280x800@60,if=RGB24 ".format(index)
        elif disp == "lvds15":
            return "video=mxcfb{}:dev=ldb,1366x768@60,if=RGB24 ".format(index)
        elif disp == "lvds7":
            return "video=mxcfb{}:dev=ldb,1024x600@60,if=RGB24 ".format(index)
        elif disp == "lvds7_hdmi720p":
            return "video=mxcfb0:dev=hdmi,1280x720M@60,if=RGB24 video=mxcfb1:dev=ldb,1024x600@60,if=RGB24"
        elif disp == "mipi5":
            return None
        return None

    def parse_display(mach, bboard, disp):
        # tek/tep-imx6 dual display with 15inch + hdmi: video=mxcfb0:dev=ldb,1366x768@60,if=RGB24,bpp=32 video=mxcfb1:dev=hdmi,1920x1080M@60,if=RGB24,bpp=32 28M
        supported_displays = {"pico-imx6": ("lcd", "lvds7", "lvds10", "lvds15", \
                                            "hdmi", "hdmi720p", "hdmi1080p", \
                                            "lvds7_hdmi720p", "custom"), \
                              "edm-imx6": ("lcd", "lvds7", "lvds10", "hdmi", "hdmi720p", \
                                           "lvds7_hdmi720p", "custom"), \
                              "tek-imx6": ("lvds10", "lvds15", "hdmi", "hdmi720p", "hdmi1080p"), \
                              "pico-imx7": ("lcd", "custom"), \
                              "pico-imx8mq": ("mipi5", "hdmi", "custom"), \
                              "pico-imx8mm": ("mipi5", "custom"), \
                              "flex-imx8mm": ("mipi5", "custom"), \
                              "edm-imx8mq": ("mipi5", "hdmi", "custom"), \
                              "axon-imx6": ("hdmi", "custom")}
        default_displays = {"tc0700": "lvds7", \
                            "tc1000": "lvds10", \
                            "tep5": "lvds10", \
                            "pico-imx6": "hdmi720p", \
                            "edm-imx6": "hdmi720p", \
                            "axon-imx6": "hdmi720p"}
        # output string
        videoarg = ""
        for i, d in enumerate(disp, start=0):
            bb.note("index: {}, display: {}".format(i, d))
            if mach in supported_displays.keys():
                if d in supported_displays[mach]:
                    videoarg += gen_videoarg(i, d)
        if len(videoarg) > 0:
            return videoarg

        if bboard in default_displays.keys():
            return gen_videoarg(0, default_displays[bboard])
        if mach in default_displays.keys():
            return gen_videoarg(0, default_displays[mach])
        return None

    def parse_radio(radios):
        supported_radios = ("qca", "brcm", "ath-pci")
        radio_list = radios.split(" ") if radios is not None else None
        bb.note("radio_list: {}".format(radio_list))
        if radio_list is not None and radio_list[0] in supported_radios:
            return radio_list[0]
        return None

    def parse_uenvcmd(mach):
        if mach in ("pico-imx6", "edm-imx6"):
            return "if test -n ${som} && test ${som} = imx6solo; then setenv som imx6dl; fi; "
        return None

    def gen_uenvtxt(d):
        envfile = "{}/uEnv.txt".format(d.getVar("S"))
        machine = d.getVar("MACHINE")
        board = d.getVar("BASE_BOARD")
        display = d.getVar("DISPLAY_INFO").split(" ")
        radios = d.getVar("RF_FIRMWARES")
        panel = d.getVar("DISPLAY_PANEL")
        alt_fdt = d.getVar("ALT_FDTNAME")
        if machine is None:
            bb.warn("Generating uEnv.txt requires MACHINE variable")
        if board is None:
            bb.warn("Generating uEnv.txt requires BASE_BOARD variable")
        bb.note("Generating uEnv.txt with machine:{} board:{} display:{} radios:{} panel:{} alt_fdt:{}".format(machine, board, display, radios, panel, alt_fdt))
        baseboard = parse_baseboard(machine, board)
        displayinfo = parse_display(machine, board, display)
        wifi_module = parse_radio(radios)
        uenv_cmd = parse_uenvcmd(machine)
        with open(envfile, 'w+') as f:
            if baseboard is not None:
                f.write("baseboard={}\n".format(baseboard))
            if displayinfo is not None:
                if machine in ["pico-imx6ul", "axon-imx6", "edm-imx6", "pico-imx6", "tek-imx6"]:
                    f.write("display_autodetect=off\n")
                f.write("displayinfo={}\n".format(displayinfo))
                f.write("mmcargs=setenv bootargs console=${console},${baudrate} root=${mmcroot} ${displayinfo}\n")
            if wifi_module is not None:
                f.write("wifi_module={}\n".format(wifi_module))
            if panel is not None and len(panel) > 0:
                f.write("panel={}\n".format(panel))
            f.write("bootcmd_mmc=run loadimage;run mmcboot;\n")
            if alt_fdt is not None:
                f.write("alt_fdtname={}\n".format(alt_fdt))
                f.write("uenvcmd=setenv has_fdt 1; setenv fdtname ${alt_fdtname}; setenv fdtfile ${alt_fdtname}.dtb; {}run bootcmd_mmc;\n".format(uenv_cmd if uenv_cmd is not None else ""))
            else:
                f.write("uenvcmd={}run bootcmd_mmc;\n".format(uenv_cmd if uenv_cmd is not None else ""))
            f.seek(0, 0)
            bb.note("Generated uEnv.txt\n{}".format(f.read()))

    # Conjure up appropriate uEnv.txt settings
    gen_uenvtxt(d)
}

addtask setuenv after do_configure before do_compile

do_compile_append_rescue() {
	if [ -f "${S}/uEnv.txt" ]; then
		sed -e 's|^bootcmd_mmc.*||g' -i ${S}/uEnv.txt
		sed -e 's|run bootcmd_mmc;||g' -i ${S}/uEnv.txt
	fi
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install ${S}/uEnv.txt ${DEPLOYDIR}/uEnv.txt
}

addtask deploy after do_install before do_build

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
