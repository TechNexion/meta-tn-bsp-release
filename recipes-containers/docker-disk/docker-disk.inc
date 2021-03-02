DOCKERHUB_CHECK_CONN_URL ?= "index.docker.io"

# Check if host can reach a specific URL
# Used for connectivity check
def connected(d):
    import socket

    REMOTE_SERVER = d.getVar('DOCKERHUB_CHECK_CONN_URL', True)
    try:
        host = socket.gethostbyname(REMOTE_SERVER)
        testSocket = socket.create_connection((host, 80), 2)
        testSocket.close()
        return "yes"
    except:
        pass
    return "no"

# Check if docker is running and usable for current user
def usable_docker(d):
    import subprocess

    # Test docker execute permission
    cmd = "docker images > /dev/null 2>&1"
    child = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
    cmd_output = child.communicate()[0]
    if child.returncode != 0:
        return "no"

    return "yes"

python () {
    # We need docker on host
    if usable_docker(d) != "yes":
        bb.fatal("docker-disk: Requires docker to run on your host OS and correct permission for current user.\n"
                 "(If building from container, you must be able to access host OS's docker.sock to create sibling containers.)")
}

# Default storage driver type used by docker daemon, e.g. aufs, overlay2, etc
TN_STORAGE_DRIVER ?= "overlay2"
TN_CONTAINER_IMAGE_TYPE ??= "tar.gz"

# TechNexion docker containers with specific CPU architectures
TN_DOCKER_REPOSITORY ?= "technexion/debian-buster-wayland"
TN_DOCKER_TAG ?= "latest"
TN_CONTAINER_IMAGE ?= "tn-container-image-lighttpd"
TN_DOCKER_CONTAINER_IMAGE ?= "${TN_CONTAINER_IMAGE}-tn-container.${TN_CONTAINER_IMAGE_TYPE}"
TN_DOCKER_PARTITION_SIZE ?= "2048"
TN_DOCKER_PARTITION_IMAGE ?= "docker-data-partition.img"

# Using dockerhub images for health check
HEALTHCHECK_REPOSITORY_aarch64 = "arm64v8/hello-world"
HEALTHCHECK_PLATFORM_aarch64 = "linux/arm64"
HEALTHCHECK_EXPORT_IMAGE = "tn-healthcheck-image.tar"
