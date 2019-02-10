DESCRIPTION = "AstroMenace is an astonishing hardcore scroll-shooter"
HOMEPAGE = "http://www.viewizard.com/"
LICENSE = "GPLv3 & OFL-1.1 & CC-BY-SA-3.0"
LIC_FILES_CHKSUM = " \
    file://gpl-3.0.txt;md5=3c34afdc3adf82d2448f12715a255122 \
    file://License.txt;md5=022aa28c3afa9de245970d5aa93bcb89 \
"

DEPENDS = "libsdl freealut openal-soft libogg libvorbis freetype libglu libxinerama fontconfig qemu-native"

REQUIRED_DISTRO_FEATURES = "x11"

inherit cmake qemu gtk-icon-cache distro_features_check

SRC_URI = " \
    ${SOURCEFORGE_MIRROR}/project/${BPN}/${PV}/astromenace-src-${PV}.tar.bz2 \
    file://0001-CMakeLists.txt-use-pkg-config-to-find-freetype.patch \
    file://0002-CMakeLists.txt-use-pkg-config-to-find-sdl.patch \
    file://0003-search-for-game-data-in-datadir-astromenace.patch \
    file://0004-Loading.cpp-fix-level2-crash.patch \
    file://astromenace.desktop \
"
SRC_URI[md5sum] = "a1481ca7cf498773fddc16a3b41c9b9b"
SRC_URI[sha256sum] = "9b775df2b157565b97aca008dd879b867cd3377c07b829cee6b5342639357fe6"

S = "${WORKDIR}/AstroMenace"

# slightly reworked qemu_run_binary: qemu.bbclass expects binary in sysroot but
# we run it before copying to sysroot
# ${@qemu_run_binary_local(d, '$D', '/usr/bin/test_app')} [test_app arguments]
#
def qemu_run_binary_local(data, rootfs_path, binary):
    qemu_binary = qemu_target_binary(data)
    if qemu_binary == "qemu-allarch":
        qemu_binary = "qemuwrapper"

    libdir = rootfs_path + data.getVar("libdir", False)
    base_libdir = rootfs_path + data.getVar("base_libdir", False)
    qemu_options = data.getVar("QEMU_OPTIONS", True)

    return "PSEUDO_UNLOAD=1 " + qemu_binary + " " + qemu_options + " -L " + rootfs_path\
            + " -E LD_LIBRARY_PATH=" + libdir + ":" + base_libdir + " "\
            + binary


do_compile_append() {
    # building native has dependencies which cannot be fullfilled - so run
    # AstroMenace in qemu to build game data
    ${@qemu_run_binary_local(d, '${STAGING_DIR_TARGET}', '${B}/AstroMenace')} --pack --rawdata=${S}/RAW_VFS_DATA --dir=${B}
}

do_install() {
    install -d ${D}/${bindir}
    install -m 755 ${B}/AstroMenace ${D}${bindir}/astromenace

    install -d ${D}/${datadir}/astromenace
    install -m 644 ${B}/gamedata.vfs ${D}${datadir}/astromenace

    install -d ${D}/${datadir}/icons/hicolor/64x64/apps
    install -m 644 ${S}/astromenace_64.png ${D}${datadir}/icons/hicolor/64x64/apps/astromenace.png
    install -d ${D}/${datadir}/icons/hicolor/128x128/apps
    install -m 644 ${S}/astromenace_128.png ${D}${datadir}/icons/hicolor/128x128/apps/astromenace.png

    
}

FILES_${PN} += "${datadir}/astromenace"
