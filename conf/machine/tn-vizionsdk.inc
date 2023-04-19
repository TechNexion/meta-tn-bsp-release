
#------ For VizionSDK/VizionViewer ------
LICENSE_FLAGS_ACCEPTED:append = " commercial_tn"
IMAGE_INSTALL:append = "${@bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', ' vizionviewer', '', d)}"
#------------------------
