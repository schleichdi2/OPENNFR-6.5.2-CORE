SUMMARY = "preinstall several wifi driver packages"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

ALLOW_EMPTY_${PN} = "1"
PACKAGES = "${PN}"

PV = "${IMAGE_VERSION}"
PR = "r0"

DEPENDS = "enigma2 enigma2-plugins enigma2-oe-alliance-plugins network-usb-drivers-meta"

RDEPENDS_${PN} = "\
	${@bb.utils.contains("DISTRO_NAME", "opennfr", "enigma2-plugin-systemplugins-wirelesslan", "", d)} \
	${@bb.utils.contains("DISTRO_NAME", "opennfr", "enigma2-plugin-drivers-network-usb-r8188eu firmware-rtl8188eu kernel-module-r8188eu", "", d)} \
	${@bb.utils.contains("MACHINE", "sf8008m", "enigma2-plugin-drivers-network-usb-mt7601u kernel-module-mt7601u firmware-mt7601u", "", d)} \
	${@bb.utils.contains("MACHINE", "8100s", "enigma2-plugin-drivers-network-usb-r8188eu firmware-rtl8188eu kernel-module-r8188eu", "", d)} \
	${@bb.utils.contains("BRAND_OEM", "dinobot", "enigma2-plugin-drivers-network-usb-rtl8192eu", "", d)} \
    "
