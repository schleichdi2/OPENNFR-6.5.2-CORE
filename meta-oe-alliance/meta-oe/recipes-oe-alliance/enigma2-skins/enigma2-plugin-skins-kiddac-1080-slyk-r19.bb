DESCRIPTION = "Kiddac Slyk_1080_R19 skin"

MAINTAINER = "Kiddac"
PRIORITY = "optional"
require conf/license/license-gplv2.inc

RDEPENDS_${PN} = "enigma2-plugin-skincomponents-kiddac-shared-skin"

inherit gitpkgv

SRCREV = "${AUTOREV}"

PV = "2.16+git${SRCPV}"
PKGV = "2.16+git${GITPKGV}"
PR = "r1"

SRC_URI="git://github.com/kiddac/Enigma2_Skins.git;protocol=git;branch=master"

S = "${WORKDIR}/git/1080_Skins/Slyk_1080_R19/Slyk_1080_R19"

FILES_${PN} = "${datadir}"

do_install() {
    install -d ${D}${datadir}
    cp -rf ${S}${datadir}/* ${D}${datadir}/
}
