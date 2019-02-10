DESCRIPTION = "AstroMenace is an astonishing hardcore scroll-shooter"
HOMEPAGE = "http://www.viewizard.com/"
LICENSE = "GPLv3 & OFL-1.1 & CC-BY-SA-4.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.md;md5=9415b740f0926863afe148fdd2f5b926 \
"

DEPENDS = " \
    libsdl2 \
    libglu \
    freealut \
    openal-soft \
    libogg \
    libvorbis \
    freetype \
    fontconfig \
    qemu-native \
"

REQUIRED_DISTRO_FEATURES = "x11"

inherit cmake dos2unix qemu gtk-icon-cache distro_features_check

SRC_URI = " \
    git://github.com/viewizard/astromenace.git \
    file://0001-CMakeLists.txt-use-pkg-config-to-find-freetype.patch \
"
SRCREV = "0adadd1e6a8ba6886c68fe6aa6d1dcf2d31e92a1"
S = "${WORKDIR}/git"

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

EXTRA_OECMAKE = " \
    -DDATADIR=${datadir}/${BPN} \
    -DDONTCREATEVFS=ON \
"

do_configure_append() {
    sed -i "s:isystem:I:g" ${B}/build.ninja
}

do_compile_append() {
    # building native has dependencies which cannot be fullfilled - so run
    # AstroMenace in qemu to build game data
    ${@qemu_run_binary_local(d, '${STAGING_DIR_TARGET}', '${B}/astromenace')} --pack --rawdata=${S}/gamedata --dir=${B}
}

do_install() {
    install -d ${D}/${bindir}
    install -m 755 ${B}/astromenace ${D}${bindir}/

    install -d ${D}/${datadir}/astromenace
    install -m 644 ${B}/gamedata.vfs ${D}${datadir}/astromenace/

    install -d ${D}/${datadir}/icons/hicolor/64x64/apps
    install -m 644 ${S}/share/astromenace_64.png ${D}${datadir}/icons/hicolor/64x64/apps/astromenace.png
    install -d ${D}/${datadir}/icons/hicolor/128x128/apps
    install -m 644 ${S}/share/astromenace_128.png ${D}${datadir}/icons/hicolor/128x128/apps/astromenace.png

    install -d ${D}/${datadir}/applications
    install -m 644 ${S}/share/astromenace.desktop ${D}/${datadir}/applications/
}

FILES_${PN} += "${datadir}/astromenace"
