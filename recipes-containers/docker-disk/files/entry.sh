#!/bin/sh

set -o errexit
set -o nounset

DOCKER_TIMEOUT=20 # Wait 20 seconds for docker to start
DATA_VOLUME=/docker-partition
BUILD=/build
SRC=/src
PARTITION_SIZE=${PARTITION_SIZE:-2048}
PARTITION_IMAGE=${PARTITION_IMAGE:-docker-data-partition.img}
CONTAINER_SUFFIX=${CONTAINER_SUFFIX:-.tar.gz}
IMAGE_SUFFIX=".tar"

finish() {
	# Make all files owned by the build system
	chown -R "$USER_ID:$USER_GID" "${BUILD}"
}
trap finish EXIT

# Create user
echo "[INFO] Creating and setting $USER_ID:$USER_GID."
groupadd -g "$USER_GID" docker-disk-group || true
useradd -u "$USER_ID" -g "$USER_GID" -p "" docker-disk-user || true

mkdir -p $DATA_VOLUME/docker
# Start docker
echo "Starting docker daemon with $STORAGE_DRIVER storage driver."
dockerd --data-root ${DATA_VOLUME}/docker -s "${STORAGE_DRIVER}" -b none --experimental &
echo "Waiting for docker to become ready.."
STARTTIME="$(date +%s)"
ENDTIME="$STARTTIME"
while [ ! -S /var/run/docker.sock ]
do
    if [ $((ENDTIME - STARTTIME)) -le $DOCKER_TIMEOUT ]; then
        sleep 1 && ENDTIME=$((ENDTIME + 1))
    else
        echo "Timeout while waiting for docker to come up."
        exit 1
    fi
done
echo "Start building docker-disk..."

if [ -n "${PRIVATE_REGISTRY}" ] && [ -n "${PRIVATE_REGISTRY_USER}" ] && [ -n "${PRIVATE_REGISTRY_PASSWORD}" ]; then
	echo "login ${PRIVATE_REGISTRY}..."
	docker login -u "${PRIVATE_REGISTRY_USER}" -p "${PRIVATE_REGISTRY_PASSWORD}" "${PRIVATE_REGISTRY}"
fi

# Pull in the technexion image from dockerhub
if [ -n "${TARGET_REPOSITORY}" -a -n "${TARGET_TAG}" ]; then
  echo "Pulling ${TARGET_REPOSITORY}:${TARGET_TAG}..."
  docker pull "${TARGET_REPOSITORY}:${TARGET_TAG}"
fi

# Pull in arch specific hello-world image and tag it tn-healthcheck-image
if [ -n "${HEALTHCHECK_REPOSITORY}" ]; then
  echo "Pulling ${HEALTHCHECK_REPOSITORY}:latest..."
  docker pull --platform "${HEALTHCHECK_PLATFORM}" "${HEALTHCHECK_REPOSITORY}"
  docker tag "${HEALTHCHECK_REPOSITORY}" ${HEALTHCHECK_EXPORT_IMAGE//${IMAGE_SUFFIX}}
  docker rmi "${HEALTHCHECK_REPOSITORY}"
  docker save ${HEALTHCHECK_EXPORT_IMAGE//${IMAGE_SUFFIX}} > ${BUILD}/${HEALTHCHECK_EXPORT_IMAGE}
fi

# Import the container image from local build
if [ -n "${CONTAINER_IMAGE}" -a -f "${SRC}/${CONTAINER_IMAGE}" ]; then
  echo "Importing ${SRC}/${CONTAINER_IMAGE}..."
  docker import ${SRC}/${CONTAINER_IMAGE} technexion/${CONTAINER_IMAGE//".${CONTAINER_SUFFIX}"}:latest
fi

echo "Imported Docker Images..."
docker images

echo "Stop building docker-disk..."
kill -TERM "$(cat /var/run/docker.pid)"
# don't let wait() error out and crash the build if the docker daemon has already been stopped
wait "$(cat /var/run/docker.pid)" || true

# Export the final data filesystem
dd if=/dev/zero of=${BUILD}/${PARTITION_IMAGE} bs=1M count=0 seek="${PARTITION_SIZE}"
mkfs.ext4 -E lazy_itable_init=0,lazy_journal_init=0 -i 8192 -d ${DATA_VOLUME}/docker -F ${BUILD}/${PARTITION_IMAGE}
tar zcf ${BUILD}/${PARTITION_IMAGE}.${CONTAINER_SUFFIX} -C ${DATA_VOLUME}/docker .
