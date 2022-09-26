#
# JYMAG hand-made Makefile for creating distribution packages.
# Best with GNU make.
# Copyright (C) 2008-2012 Bogdan 'bogdro' Drozdowski, bogdandr @ op . pl
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 3
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software Foundation:
#               Free Software Foundation
#               51 Franklin Street, Fifth Floor
#               Boston, MA 02110-1301
#               USA
#

# JYMAG version
VER		= 1.2

SHELL		= /bin/sh
DEL		= /bin/rm -fr
COPY		= /bin/cp -fr
PACK		= tar jcf
PACK_EXT	= tar.bz2
GNUPG		= gpg
GNUPG_OPTS	= --yes --digest-algo RIPEMD160 -ba
HTMLDOC		= htmldoc
HTMLDOC_OPTS 	= --charset iso-8859-2 -t pdf14 --encryption --duplex --permissions none,print \
	--compression=9 --links --linkcolor blue \
	--linkstyle underline --header 'd.c' --footer '1.h' --webpage
ANT		= ant
LAUNCH4J	= launch4j
SED		= sed
SED_OPTS	= -i
NSIS		= makensis
UNIX2DOS	= unix2dos
UPX		= upx


# ------------- targets:
# NOTE: using "&&" is required, because we're changing directories and we want
# the subsequent commands to be executed in the directory we've just changed to.
# This won't work if we write separate commands one per line, because each
# is run in its own subshell, so changing directories wouldn't affect the
# subsequent commands.

all:	jar
# dist-src should be last, because it deletes elements used by the other targets
dist:	dist-javadoc dist-bin installer dist-src

dist-src:	../JYMAG-src-$(VER).$(PACK_EXT) Makefile

../JYMAG-src-$(VER).$(PACK_EXT): clean Makefile
	cd .. && \
	$(DEL) JYMAG-src-$(VER).$(PACK_EXT) JYMAG-src-$(VER).$(PACK_EXT).asc && \
	$(COPY) JYMAG JYMAG-$(VER) && \
	$(PACK) JYMAG-src-$(VER).$(PACK_EXT) JYMAG-$(VER) && \
	$(DEL) JYMAG-$(VER) && \
	$(GNUPG) $(GNUPG_OPTS) JYMAG-src-$(VER).$(PACK_EXT)

dist-bin:	../JYMAG-bin-$(VER).$(PACK_EXT) Makefile

../JYMAG-bin-$(VER).$(PACK_EXT):	manual jar Makefile
	$(DEL) dist/javadoc && \
	cd .. && \
	$(DEL) JYMAG-bin-$(VER).$(PACK_EXT) JYMAG-bin-$(VER).$(PACK_EXT).asc && \
	$(COPY) JYMAG JYMAG-$(VER) && \
	$(PACK) JYMAG-bin-$(VER).$(PACK_EXT) \
		JYMAG-$(VER)/AUTHORS	\
		JYMAG-$(VER)/COPYING	\
		JYMAG-$(VER)/ChangeLog	\
		JYMAG-$(VER)/INSTALL	\
		JYMAG-$(VER)/README	\
		JYMAG-$(VER)/dist	\
		JYMAG-$(VER)/run*.sh	\
		JYMAG-$(VER)/run*.bat	\
		JYMAG-$(VER)/manual	\
		JYMAG-$(VER)/JYMAG-manual-*.pdf	\
		JYMAG-$(VER)/THANKS	&& \
	$(DEL) JYMAG-$(VER) && \
	$(GNUPG) $(GNUPG_OPTS) JYMAG-bin-$(VER).$(PACK_EXT)

manual: JYMAG-manual-EN.pdf JYMAG-manual-PL.pdf Makefile

JYMAG-manual-EN.pdf: manual/en/*.html manual/rsrc/css/* manual/rsrc/img/* manual/rsrc/license.html Makefile
	$(HTMLDOC) $(HTMLDOC_OPTS) -f JYMAG-manual-EN.pdf \
		manual/en/index.html \
		manual/en/readme.html \
		manual/en/main_window.html \
		manual/en/tables.html \
		manual/en/about_window.html \
		manual/en/capability.html \
		manual/en/manual_cmd.html \
		manual/en/signal_power.html \
		manual/rsrc/license.html

JYMAG-manual-PL.pdf: manual/pl/*.html manual/rsrc/css/* manual/rsrc/img/* manual/rsrc/license.html Makefile
	$(HTMLDOC) $(HTMLDOC_OPTS) -f JYMAG-manual-PL.pdf \
		manual/pl/index.html \
		manual/pl/readme.html \
		manual/pl/main_window.html \
		manual/pl/tables.html \
		manual/pl/about_window.html \
		manual/pl/capability.html \
		manual/pl/manual_cmd.html \
		manual/pl/signal_power.html \
		manual/rsrc/license.html

dist-javadoc:	../JYMAG-javadoc-$(VER).$(PACK_EXT) Makefile

../JYMAG-javadoc-$(VER).$(PACK_EXT):	dist/javadoc Makefile
	cd .. && \
	$(DEL) JYMAG-javadoc-$(VER).$(PACK_EXT) JYMAG-javadoc-$(VER).$(PACK_EXT).asc && \
	$(COPY) JYMAG JYMAG-$(VER) && \
	$(PACK) JYMAG-javadoc-$(VER).$(PACK_EXT) JYMAG-$(VER)/dist/javadoc && \
	$(DEL) JYMAG-$(VER) && \
	$(GNUPG) $(GNUPG_OPTS) JYMAG-javadoc-$(VER).$(PACK_EXT)

dist/javadoc:	$(shell find src)
	$(DEL) dist/javadoc
	$(ANT) javadoc

jar: dist/JYMAG.jar

dist/JYMAG.jar:	$(shell find src)
	$(ANT) jar

installer:	jar manual setup/jymag.exe setup/Setup-JYMAG-$(VER).exe Makefile

setup/jymag.exe:	setup/launch4j-jymag.xml setup/jymag.ico Makefile
	$(SED) $(SED_OPTS) 's/<fileVersion>[^<]*<\/fileVersion>/<fileVersion>$(VER).0.0<\/fileVersion>/' \
		setup/launch4j-jymag.xml
	$(SED) $(SED_OPTS) 's/<txtFileVersion>[^<]*<\/txtFileVersion>/<txtFileVersion>$(VER)<\/txtFileVersion>/' \
		setup/launch4j-jymag.xml
	$(SED) $(SED_OPTS) 's/<productVersion>[^<]*<\/productVersion>/<productVersion>$(VER).0.0<\/productVersion>/' \
		setup/launch4j-jymag.xml
	$(SED) $(SED_OPTS) 's/<txtProductVersion>[^<]*<\/txtProductVersion>/<txtProductVersion>$(VER)<\/txtProductVersion>/' \
		setup/launch4j-jymag.xml
	$(LAUNCH4J) $(PWD)/setup/launch4j-jymag.xml
	$(COPY) setup/jymag.exe ../jymag-$(VER).exe

setup/Setup-JYMAG-$(VER).exe:	setup/jymag.nsi AUTHORS ChangeLog INSTALL \
		COPYING README THANKS Makefile
	$(COPY) AUTHORS setup/AUTHORS
	$(COPY) ChangeLog setup/ChangeLog
	$(COPY) INSTALL setup/INSTALL
	$(COPY) COPYING setup/license.txt
	$(COPY) README setup/README
	$(COPY) THANKS setup/THANKS
	$(UNIX2DOS) setup/AUTHORS
	$(UNIX2DOS) setup/ChangeLog
	$(UNIX2DOS) setup/INSTALL
	$(UNIX2DOS) setup/license.txt
	$(UNIX2DOS) setup/README
	$(UNIX2DOS) setup/THANKS
	$(SED) $(SED_OPTS) 's/!define VERSION .*/!define VERSION $(VER)/' setup/jymag.nsi
	$(NSIS) setup/jymag.nsi
	#$(UPX) setup/Setup-JYMAG-$(VER).exe
	$(COPY) setup/Setup-JYMAG-$(VER).exe ..

clean:
	$(ANT) clean
	$(DEL) setup/*.exe JYMAG-manual-*.pdf

.PHONY:	all dist dist-src dist-bin dist-javadoc manual jar clean installer

