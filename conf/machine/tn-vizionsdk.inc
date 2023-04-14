
#------ For VizionSDK/VizionViewer ------
IMAGE_INSTALL:append = "${@bb.utils.contains('UBUNTU_TARGET_ARCH', 'arm64', '', ' vizionviewer', d)}"
#------------------------
