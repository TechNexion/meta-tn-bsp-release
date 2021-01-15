# Copyright (C) 2021 Po Cheng - TechNexion

# additional qt5 features for tsl
PACKAGECONFIG_remove_rescue = "examples tests"
PACKAGECONFIG_append_rescue = "${@bb.utils.contains('MACHINE_FEATURES', 'touchscreen', 'tslib', '', d)}"

