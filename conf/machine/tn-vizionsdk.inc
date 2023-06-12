
#------ For VizionSDK/VizionViewer ------
LICENSE_FLAGS_ACCEPTED:append = " commercial_tn"
IMAGE_INSTALL:append = "${@bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', ' vizionsdk-dev vizionviewer', '', d)}"
IMAGE_INSTALL:append = "${@bb.utils.contains_any('TARGET_ARCH', 'arm64 aarch64', ' tn-apt-list', '', d)}"
#------------------------
