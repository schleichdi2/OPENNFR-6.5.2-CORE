SUMMARY = "rsync for cloud storage"
DESCRIPTION = "Rclone is a command line program to sync files and directories to and from different cloud storage providers \
    Alibaba Cloud (Aliyun) Object Storage System (OSS) Amazon Drive Amazon S3 Backblaze B2 Box Ceph DigitalOcean Spaces \
    Dreamhost Dropbox FTP Google Cloud Storage Google Drive HTTP Hubic Jottacloud IBM COS S3 Koofr Memset Memstore Mega \
    Microsoft Azure Blob Storage Microsoft OneDrive Minio Nextcloud OVH OpenDrive OpenStack Swift Oracle Cloud Storage \
    ownCloud pCloud put.io QingStor Rackspace Cloud Files Scaleway SFTP Wasabi WebDAV Yandex Disk The local filesystem"
HOMEPAGE = "https://rclone.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/COPYING;md5=bed161b82a1ecab65ff7ba3c3b960439"

DEPENDS = "go-cross-${TUNE_PKGARCH}"

RDEPENDS_${PN} += "bash"
RDEPENDS_${PN}-dev += "bash python3-core"

inherit gitpkgv

SRCREV = "${AUTOREV}"
PV = "1.54-DEV+git${SRCPV}"

GO_IMPORT = "github.com/rclone/rclone"

SRC_URI = "git://${GO_IMPORT};protocol=https;branch=master \
           file://0001-Revert-lib-add-plugin-support.patch \
           file://rclonefs"

inherit go-mod upx-compress

GO_DYNLINK_aarch64 = ""
GO_DYNLINK_arm = ""

do_install_append() {
    rm -rf ${D}${bindir}/test*
    install -m 755 ${WORKDIR}/rclonefs ${D}${bindir}
    ln -sf rclone ${D}${bindir}/mount.rclone
}

# module cloud.google.com contains test files for various arches
# which are causing false positives at do_package_qa

do_package_qa() {
}

INSANE_SKIP_${PN} = "already-stripped"
