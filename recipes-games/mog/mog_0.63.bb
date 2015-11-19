HOMEPAGE = "http://www.braingames.getput.com/mog/"
SUMMARY = "The Maze of Galious was originally a Konami game for MSX computer system"
DESCRIPTION = "The Maze of Galious (MoG in short) was originally a Konami game for the \
MSX computer system. Its real name is Knightmare II: The Maze of \
Galious and is the sequel of another Konami game called Knightmare. \
MoG is a very addictive game where you have to kill thousands of \
enemies, collect items in order to obtain new powers and defeat some \
really great demons at the end of each level."

DEPENDS = "libsdl libsdl-image libsdl-mixer"

PATCHREV = "1548"

SRC_URI = "http://urchlay.naptime.net/~urchlay/src/${BPN}.src_${PV}-${PATCHREV}.tgz"
SRC_URI[md5sum] = "68604c258ce7347662777d853d895769"
SRC_URI[sha256sum] = "707bbc1b167a55989916f803dbe55c288652fb54df6194be029c41be99e9ec3a"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=393a5ca445f6965873eca0259a17f833"

S = "${WORKDIR}/${BPN}-${PV}.${PATCHREV}"
