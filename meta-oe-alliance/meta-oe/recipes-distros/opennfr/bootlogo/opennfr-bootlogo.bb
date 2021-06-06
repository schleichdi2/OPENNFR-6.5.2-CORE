DESCRIPTION = "OpenNFR bootlogo"
SECTION = "base"
PRIORITY = "required"
MAINTAINER = "opennfr"
PACKAGE_ARCH = "${MACHINE_ARCH}"

require conf/license/license-gplv2.inc

RDEPENDS_${PN} += "showiframe"

PV = "${IMAGE_VERSION}"
PR = "r0"

S = "${WORKDIR}"

INITSCRIPT_NAME = "bootlogo"
INITSCRIPT_PARAMS = "start 06 S ."
INITSCRIPT_PARAMS_inihdp = "start 45 S ."

inherit update-rc.d

SRC_URI = "file://bootlogo.mvi  file://radio.mvi file://bootlogo.sh \
    ${@bb.utils.contains("MACHINE_FEATURES", "bootsplash", "file://splash.bin" , "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd220", "file://lcdsplash220.bin file://lcdwaitkey220.bin file://lcdwarning220.bin file://lcdcomplete220.bin" , "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd400", "file://lcdsplash400.bin file://lcdwaitkey400.bin file://lcdwarning400.bin file://lcdcomplete400.bin" , "", d)} \
"

SRC_URI_append_inihdp = "file://inihdp/cfe.bmp file://inihdp/finished.bmp file://inihdp/imageversion.bmp file://inihdp/kernel.bmp file://inihdp/rootfs.bmp file://inihdp/splash.bmp"

SRC_URI_append_8100s = "file://8100s/lcdwarning220.bin"


FILES_${PN} = "/boot /usr/share /etc/init.d"

INSANE_SKIP_${PN} = "already-stripped"

do_install() {
    install -d ${D}/usr/share
    install -m 0644 bootlogo.mvi ${D}/usr/share/bootlogo.mvi
    ln -sf /usr/share/bootlogo.mvi ${D}/usr/share/backdrop.mvi
    install -d ${D}/usr/share/enigma2
    install -m 0644 radio.mvi ${D}/usr/share/enigma2/radio.mvi
    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 bootlogo.sh ${D}/${sysconfdir}/init.d/bootlogo
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd400", "install -m 0644 lcdwaitkey400.bin ${D}/usr/share/lcdwaitkey.bin" , "", d)}
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd400", "install -m 0644 lcdwarning400.bin ${D}/usr/share/lcdwarning.bin" , "", d)}
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd400", "install -m 0644 lcdcomplete400.bin ${D}/usr/share/lcdcomplete.bin" , "", d)}
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd220", "install -m 0644 lcdwaitkey220.bin ${D}/usr/share/lcdwaitkey.bin" , "", d)}
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd220", "install -m 0644 lcdwarning220.bin ${D}/usr/share/lcdwarning.bin" , "", d)}
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd220", "install -m 0644 lcdcomplete220.bin ${D}/usr/share/lcdcomplete.bin" , "", d)}
}


do_install_append_inihdp() {
    install -m 0644 ${S}/inihdp/cfe.bmp ${DEPLOY_DIR_IMAGE}/cfe.bmp
    install -m 0644 ${S}/inihdp/finished.bmp ${DEPLOY_DIR_IMAGE}/finished.bmp
    install -m 0644 ${S}/inihdp/imageversion.bmp ${DEPLOY_DIR_IMAGE}/imageversion.bmp
    install -m 0644 ${S}/inihdp/kernel.bmp ${DEPLOY_DIR_IMAGE}/kernel.bmp
    install -m 0644 ${S}/inihdp/rootfs.bmp ${DEPLOY_DIR_IMAGE}/rootfs.bmp   
    install -m 0644 ${S}/inihdp/splash.bmp ${DEPLOY_DIR_IMAGE}/splash.bmp  
}

do_install_append_8100s() {
     install -d ${D}/usr/share
     install -m 0644 ${WORKDIR}/8100s/lcdwarning220.bin ${D}/usr/share/lcdflashing.bmp
}

inherit deploy
do_deploy() {
    if [ -e splash.bin ]; then
        install -m 0644 splash.bin ${DEPLOYDIR}/${BOOTLOGO_FILENAME}
    fi
    if [ -e lcdsplash220.bin ]; then
        install -m 0644 lcdsplash220.bin ${DEPLOYDIR}/lcdsplash220.bin
    fi

    if [ -e lcdsplash400.bin ]; then
        install -m 0644 lcdsplash400.bin ${DEPLOYDIR}/lcdsplash400.bin
    fi
}

do_package_qa[noexec] = "1"
addtask deploy before do_build after do_install

