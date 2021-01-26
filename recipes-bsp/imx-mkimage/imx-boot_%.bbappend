
do_replace () {
	sed -i 's|dtbs = evk.dtb|dtbs = ${UBOOT_DTB_NAME}|g' ${BOOT_STAGING}/soc.mak
}

#copy from "meta-imx/meta-bsp/recipes-bsp/imx-mkimage/imx-boot_1.0.bb"
compile_mx8m() {
    bbnote 8MQ/8MM boot binary build
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        bbnote "Copy ddr_firmware: ${ddr_firmware} from ${DEPLOY_DIR_IMAGE} -> ${BOOT_STAGING} "
        cp ${DEPLOY_DIR_IMAGE}/${ddr_firmware}               ${BOOT_STAGING}
    done
    cp ${DEPLOY_DIR_IMAGE}/signed_dp_imx8m.bin               ${BOOT_STAGING}
    cp ${DEPLOY_DIR_IMAGE}/signed_hdmi_imx8m.bin             ${BOOT_STAGING}
    cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin
    if [ "${MACHINE}" = "imx8mmevk-qcawifi" ]; then
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${UBOOT_DTB_NAME}   ${BOOT_STAGING}/imx8mm-evk.dtb
    else
	#========= Tweak to be able to deal with multiple DTBs =========
	for DTB in ${UBOOT_DTB_NAME}; do
		cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${DTB}   ${BOOT_STAGING}
	done
	#========= End of tweak =========
    fi
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${UBOOT_CONFIG} \
                                                             ${BOOT_STAGING}/u-boot-nodtb.bin
    bbnote "\
Using standard mkimage from u-boot-tools for FIT image builds. The standard \
mkimage is compatible for this use, and using it saves us from having to \
maintain a custom recipe."
    ln -sf ${STAGING_DIR_NATIVE}${bindir}/mkimage            ${BOOT_STAGING}/mkimage_uboot
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${BOOT_STAGING}/bl31.bin
    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME}                     ${BOOT_STAGING}/u-boot.bin
}

addtask replace before do_compile after do_configure