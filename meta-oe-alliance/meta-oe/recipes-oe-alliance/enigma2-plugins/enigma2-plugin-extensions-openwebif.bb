MODULE = "OpenWebif"
DESCRIPTION = "Control your receiver with a browser"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;firstline=10;lastline=12;md5=26abba37d1c2fcbf96a087ceb8e1db86"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

BRANCH="master"

DEPENDS = "${PYTHON_PN}-cheetah-native"
RDEPENDS_${PN} = "\
	aio-grab \
	${PYTHON_PN}-cheetah \
	${PYTHON_PN}-compression \
	${PYTHON_PN}-ipaddress \
	${PYTHON_PN}-json \
	${PYTHON_PN}-misc \
	${PYTHON_PN}-numbers \
	${PYTHON_PN}-pyopenssl \
	${PYTHON_PN}-pprint \
	${PYTHON_PN}-shell \
	${PYTHON_PN}-twisted-web \
	${PYTHON_PN}-unixadmin \
	oe-alliance-branding \
	"

inherit gitpkgv ${@bb.utils.contains("PYTHON_PN", "python", "distutils-openplugins", "distutils3-openplugins", d)} gettext

DISTUTILS_INSTALL_ARGS = "--root=${D} --install-lib=${libdir}/enigma2/python/Plugins"

SRCREV = "${AUTOREV}"
PV = "1.4.6+git${SRCPV}"
PKGV = "1.4.6+git${GITPKGV}"

SRC_URI = "git://github.com/E2OpenPlugins/e2openplugin-${MODULE}.git;protocol=git;branch=${BRANCH} \
	file://transcoding.py \
	file://0001-add-fileupload.patch"

S="${WORKDIR}/git"

# Just a quick hack to "compile" it
do_compile() {
	cp ${WORKDIR}/transcoding.py ${S}/plugin/controllers/transcoding.py
	rm -rf ${S}/plugin/public/static/remotes >/dev/null 2>&1 || true
	find ${S}/plugin/public/ -empty -type d -delete >/dev/null 2>&1 || true
	find ${S}/plugin/public/images/boxes/ ! -name 'unknown.png' -type f -exec rm -f {} +
	find ${S}/plugin/public/images/remotes/ ! -name 'ow_remote.png' -type f -exec rm -f {} +
	cheetah-compile -R --nobackup ${S}/plugin
}

PLUGINPATH = "${libdir}/enigma2/python/Plugins/Extensions/${MODULE}"
do_install_append() {
	install -d ${D}${PLUGINPATH}
	cp -r ${S}/plugin/* ${D}${PLUGINPATH}
	chmod a+rX ${D}${PLUGINPATH}
}

python populate_packages_prepend() {
    enigma2_plugindir = bb.data.expand('${libdir}/enigma2/python/Plugins', d)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/public/themes/.*$', 'enigma2-plugin-%s-themes', '%s (Additional themes for OpenWebif)', recursive=True, match_path=True, prepend=True, extra_depends="${PN}")
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/public/webtv/.*$', 'enigma2-plugin-%s-webtv', '%s (WebTV for OpenWebif)', recursive=True, match_path=True, prepend=True, extra_depends="${PN}")
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/public/vxg/.*$', 'enigma2-plugin-%s-vxg', '%s (WebTV for Google Chrome)', recursive=True, match_path=True, prepend=True, extra_depends="${PN}-webtv")
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/.*\.pexe$', 'enigma2-plugin-%s-vxg', '%s (WebTV support for Google Chrome)', recursive=True, match_path=True, prepend=True, extra_depends="${PN}-webtv")
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/[a-zA-Z0-9_]+.*$', 'enigma2-plugin-%s', '%s', recursive=True, match_path=True, prepend=True, extra_depends="enigma2")
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/.*\.la$', 'enigma2-plugin-%s-dev', '%s (development)', recursive=True, match_path=True, prepend=True)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/.*\.a$', 'enigma2-plugin-%s-staticdev', '%s (static development)', recursive=True, match_path=True, prepend=True)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/(.*/)?\.debug/.*$', 'enigma2-plugin-%s-dbg', '%s (debug)', recursive=True, match_path=True, prepend=True)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/.*\/.*\.po$', 'enigma2-plugin-%s-po', '%s (translations)', recursive=True, match_path=True, prepend=True)
}

INSANE_SKIP_${PN} += "build-deps"
INSANE_SKIP_${PN}-terminal += "build-deps"
INSANE_SKIP_${PN}-vxg += "build-deps"

# Required empty packages for build compatibility with distros still using OWIF 0.x.y - 1.0.z
PACKAGES =+ "${PN}-terminal ${PN}-themes ${PN}-webtv"
RDEPENDS_${PN}-terminal = "${PN} shellinabox"
RDEPENDS_${PN}-themes = "${PN}"
RDEPENDS_${PN}-webtv = "${PN}"
ALLOW_EMPTY_${PN}-terminal = "1"
ALLOW_EMPTY_${PN}-themes = "1"
ALLOW_EMPTY_${PN}-webtv = "1"
