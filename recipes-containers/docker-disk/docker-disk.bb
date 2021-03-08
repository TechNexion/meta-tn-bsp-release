DESCRIPTION = "Package to create Docker data partition image using DinD container"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INSANE_SKIP_${PN} += "already-stripped"
SKIP_FILEDEPS = "1"
EXCLUDE_FROM_SHLIBS = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
	file://Dockerfile \
	file://entry.sh \
	"

S = "${WORKDIR}"
B = "${S}/build"

#
# IMPORTANT:
#
# docker-disk recipe uses "docker" command (pre-installed in yocto build environment)
# to start a docker-in-docker container for generating the require /var/lib/docker
# directory, setup and utilised by the docker daemon (dockerd)
#
# IMPLICATION:
#
# 1. Using docker/dockerd-native for the hosttools in the bitbake architecture
#    is troublesome because docker requires a dockerd to run in the hosttools,
#    and this is not easily setup within the bitbake build time. (Hence the
#    use of 'docker' command in the pre-installed yocto build environment)
#
# 2. By using pre-installed 'docker' command in yocto build environment, it
#    implies that we are using Host OS's dockerd, therefore the user(account)
#    bitbaking this docker-disk recipe needs access to dockerd's /var/run/docker.sock
#    on Host OS, thus allowing docker command to start a docker container.
#
# 3. In the case where yocto build environment is running in a docker container
#    (referred to as "yocto-builder-container")
#    - From 1. it requires yocto-builder-container to install a 'docker' command (binary file).
#    - From 2. When running the "yocto-builder-container", in order to use 'docker' command
#              properly, we need to share Host OS's /var/run/docker.sock with
#              the "yocto-builder-container".
#
# 4. In "yocto-builder-container", when bitbaking docker-disk recipe, a sibling
#    container (i.e. docker-in-docker container) is created on the Host OS,
#    because we are using dockerd's /var/run/docker.sock on Host OS.
#    (i.e. imagining a sister "docker-in-docker" is started on the Host OS next
#    to the "yocto-builder-contaier")
#
#      +--------------------------------------+
#      | Host OS (dockerd)                    |
#      |                                      |
#      | +----------+          +-----------+  |
#      | | yocto-   |     +--> | docker-   |  |
#      | | builder  |     |    | in-docker |  |
#      | |          |     |    |           |  |
#      | | $docker  |-----+    |           |  |
#      | +----------+  starts  +-----------+  |
#      |                                      |
#      +--------------------------------------+
#
# 5. From 4, when doing do_install() and do_deploy() tasks of docker-disk recipe
#     in the "yocto-builder-container", both "yocto-builder-container" and
#    "docker-in-docker" containers must share a OUTPUT_IMAGE_DIR directory on
#    Host OS, otherwise no files can be found within either containers.
#
#      +----------------------------------------------------------+
#      | Host OS (dockerd)    /path/to/shared/OUTPUT_IMAGE_DIR    |
#      |                           |                              |
#      | +------------------+      |      +--------------------+  |
#      | | yocto-builder    |      |      | docker-in-docker   |  |
#      | |                  |      |      |                    |  |
#      | |                  |      |      |                    |  |
#      | | ${MOUNT_DIR}/    |      |      | ${MOUNT_PATH}/     |  |
#      | |   OUT_IMAGE_DIR  | <----+----> |   OUT_IMAGE_DIR    |  |
#      | +------------------+             +--------------------+  |
#      |                                                          |
#      +----------------------------------------------------------+
#
#    Consequently, DOCKER_SHAREDIR variable is introduced to set /path/to/shared/
#    path on the Host OS (see-able to both sibling containers)
#
#    For example, on technexion's jenkins server which runs yocto builds in a
#    "yocto-builder-container". Set the following in local.conf
#
#    DOCKER_SHAREDIR = "/home/admin/jenkins_home"
#
DOCKER_SHAREDIR ?= "${HOME}"
SHAREDSRC = "${@ '%s' % (d.getVar('S', True).replace(d.getVar('HOME', True), d.getVar('DOCKER_SHAREDIR', True)))}"
SHAREDBUILD = "${@ '%s' % (d.getVar('B', True).replace(d.getVar('HOME', True), d.getVar('DOCKER_SHAREDIR', True)))}"

inherit deploy
require docker-disk.inc

# By default pull technexion dockerhub's images
TARGET_REPOSITORY ?= "${TN_DOCKER_REPOSITORY}"
TARGET_TAG ?= "${TN_DOCKER_TAG}"

python () {
    import re
    repo = d.getVar("TARGET_REPOSITORY", True)
    tag = d.getVar("TARGET_TAG", True)
    pv = re.sub(r"[^a-z0-9A-Z_.-]", "_", "%s-%s" % (repo,tag))
    d.setVar('PV', pv)
}

PV = "${TARGET_TAG}"

# multiconfig dependancy only works on yocto warrior onward
do_postfetch[mcdepends] = "mc:${MACHINE}:container:${TN_CONTAINER_IMAGE}:do_image_complete"
do_postfetch () {
	if [ -f ${TOPDIR}/tmp-container/deploy/images/tn-container/${TN_DOCKER_CONTAINER_IMAGE} ]; then
		mkdir -p ${S}/container
		install -m 644 ${TOPDIR}/tmp-container/deploy/images/tn-container/${TN_DOCKER_CONTAINER_IMAGE} ${S}/container/${TN_DOCKER_CONTAINER_IMAGE}
	else
		bbwarn "${TOPDIR}/tmp-container/deploy/images/tn-container/${TN_DOCKER_CONTAINER_IMAGE} not found. For yocto version earlier than warrior, please build ${TN_DOCKER_CONTAINER_IMAGE} separately with the following command:\n\tbitbake mc:container:${TN_CONTAINER_IMAGE}"
	fi
}
addtask postfetch before do_compile and after do_fetch

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_package_qa[noexec] = "1"

do_compile () {
	# Some sanity first for the technexion docker variables
	if [ -z "${TARGET_REPOSITORY}" ] || [ -z "${TARGET_TAG}" ]; then
		bbfatal "docker-disk: TARGET_REPOSITORY and/or TARGET_TAG not set."
	fi
	if [ -z "${TN_DOCKER_PARTITION_SIZE}" ]; then
		bbfatal "docker-disk: TN_DOCKER_PARTITION_SIZE needs to have a value (megabytes)."
	fi
	if [ -z "${TN_DOCKER_PARTITION_IMAGE}" ]; then
		bbfatal "docker-disk: TN_DOCKER_PARTITION_IMAGE needs to have an image name."
	fi

	# At this point we really need Internet connectivity for building the docker image
	if [ "x${@connected(d)}" != "xyes" ]; then
		bbfatal "docker-disk: Can't compile as there is no Internet connectivity on this host."
	fi

	# We force the PATH to be the standard linux path in order to use the host's
	# docker daemon instead of the result of docker-native. This avoids version
	# mismatches
	DOCKER=$(PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" which docker)

	bbnote "docker-disk: starting docker..."
	bbnote "docker-disk: SHAREDSRC=${SHAREDSRC}/container"
	bbnote "docker-disk: SHAREDBUILD=${SHAREDBUILD}"

	# Generate the data filesystem (i.e. /var/lib/docker) using DinD with docker daemon
	RANDOM=$$
	_image_name="docker-disk-$RANDOM"
	_container_name="docker-disk-$RANDOM"
	$DOCKER rmi ${_image_name} > /dev/null 2>&1 || true
	$DOCKER build -t ${_image_name} -f ${WORKDIR}/Dockerfile ${WORKDIR}
	$DOCKER run --privileged --rm \
		-e STORAGE_DRIVER=${TN_STORAGE_DRIVER} \
		-e USER_ID=$(id -u) -e USER_GID=$(id -u) \
		-e TARGET_REPOSITORY="${TARGET_REPOSITORY}" \
		-e TARGET_TAG="${TARGET_TAG}" \
		-e CONTAINER_IMAGE="${TN_DOCKER_CONTAINER_IMAGE}" \
		-e CONTAINER_SUFFIX="${TN_CONTAINER_IMAGE_TYPE}" \
		-e HEALTHCHECK_REPOSITORY="${HEALTHCHECK_REPOSITORY}" \
		-e HEALTHCHECK_PLATFORM="${HEALTHCHECK_PLATFORM}" \
		-e HEALTHCHECK_EXPORT_IMAGE="${HEALTHCHECK_EXPORT_IMAGE}" \
		-e PRIVATE_REGISTRY="${PRIVATE_REGISTRY}" \
		-e PRIVATE_REGISTRY_USER="${PRIVATE_REGISTRY_USER}" \
		-e PRIVATE_REGISTRY_PASSWORD="${PRIVATE_REGISTRY_PASSWORD}" \
		-e PARTITION_SIZE="${TN_DOCKER_PARTITION_SIZE}" \
		-e PARTITION_IMAGE="${TN_DOCKER_PARTITION_IMAGE}" \
		-v /sys/fs/cgroup:/sys/fs/cgroup:ro \
		-v ${SHAREDSRC}/container:/src \
		-v ${SHAREDBUILD}:/build \
		--name ${_container_name} ${_image_name}
	$DOCKER rmi ${_image_name}
}

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	if [ -f ${B}/${TN_DOCKER_PARTITION_IMAGE} ]; then
		install -m 644 ${B}/${TN_DOCKER_PARTITION_IMAGE} ${DEPLOY_DIR_IMAGE}/${TN_DOCKER_PARTITION_IMAGE}
	else
		bbfatal "${B}/${TN_DOCKER_PARTITION_IMAGE} not found. Please ensure docker-disk exported docker container images correctly. (Please also check your DOCKER_SHAREDIR setting is accessible for both build and dind container environments"
	fi
	if [ -f ${B}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE} ]; then
		install -m 644 ${B}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE} ${DEPLOY_DIR_IMAGE}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE}
	fi
}
addtask deploy before do_package after do_install


do_install[fakeroot] = "1"

fakeroot do_install_append() {
	if [ -n "${TN_DOCKER_PARTITION_MOUNT}" ]; then
		install -d ${D}${TN_DOCKER_PARTITION_MOUNT}
		if [ -f ${B}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE} ]; then
			tar zxf ${B}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE} -C ${D}${TN_DOCKER_PARTITION_MOUNT}
		else
			bbfatal "${B}/${TN_DOCKER_PARTITION_IMAGE}.${TN_CONTAINER_IMAGE_TYPE} not found. Please ensure docker-disk exported docker containers directory as tar.gz file correctly. (Please also check your DOCKER_SHAREDIR setting is accessible for both build and dind container environment)"
		fi
	fi
}

FILES_${PN} += "${TN_DOCKER_PARTITION_MOUNT}"
