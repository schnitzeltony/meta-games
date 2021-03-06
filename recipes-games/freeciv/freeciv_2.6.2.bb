FREECIV_GUI = "gtk3.22,qt,sdl2"

require freeciv.inc

DEPENDS += "libsdl2-image libsdl2-ttf libsdl2-gfx freetype"

SRC_URI += "\
${SOURCEFORGE_MIRROR}/freeciv/freeciv-${PV}.tar.bz2 \
file://QPainterPathInc.patch \
"

SRC_URI[md5sum] = "2c8b388ff8b814487477793d72462cbb"
SRC_URI[sha256sum] = "6181ef3d3c76264383aabbe0eaf1550d8a65ca42639e6c17cc2938165e176c8f"
SRC_URI[sha1sum] = "d459c711a164fbaf215e0083aaf5784253a1492f"
SRC_URI[sha384sum] = "723cb04256875f9d1e40b34c674529ee00128f7192ddab1a569e6344138c080375b99f9a959fe9ebae890ef210fc1b96"
SRC_URI[sha512sum] = "b11752f38027fcc8b092f323d4e76cf3c4c426bfcc811eb3a76daca075dc391d8179cd140abf1534abc56409344c53238b9fee2ee10d08a688e15671f3a70ef3"

PACKAGES =+ "${PN}-sdl2 ${PN}-gtk3.22"

RDEPENDS_${PN}-gtk3.22 = "freeciv-common freeciv-client-common"
RPROVIDES_${PN}-gtk3.22 = "freeciv-client"
FILES_${PN}-gtk3.22 = "\
${bindir}/freeciv-gtk3.22 \
${datadir}/freeciv/themes/gui-gtk-3.22 \
${datadir}/freeciv/gtk3.22_menus.xml \
"

RDEPENDS_${PN}-sdl2 = "freeciv-common freeciv-client-common"
RPROVIDES_${PN}-sdl2 = "freeciv-client"
FILES_${PN}-sdl2 = "\
${bindir}/freeciv-sdl2 \
${datadir}/freeciv/themes/gui-sdl2 \
"
