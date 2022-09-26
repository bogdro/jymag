#
# JYMAG hand-made Makefile for creating distribution packages.
# Best with GNU make.
# Copyright (C) 2008-2018 Bogdan 'bogdro' Drozdowski, bogdandr @ op . pl
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
# along with this program; if not, write to the Free Software Foundation
#               Free Software Foundation
#               51 Franklin Street, Fifth Floor
#               Boston, MA 02110-1301
#               USA
#

###########################################################################
# General variables
###########################################################################

# core system utilities
COPY		= /bin/cp -fr
DEL		= /bin/rm -fr
MOVE		= /bin/mv -f
PACK		= tar jcf
PACK_EXT	= tar.bz2
PACK_EXCL_SRC	= jymag.exclude
PACK_OTPS	= --exclude-from=$(PACK_EXCL_SRC)
SED		= sed
SED_OPTS	= -i
SHELL		= /bin/sh
TOUCH		= touch
UNIX2DOS	= unix2dos

# www.gnupg.org
GNUPG		= gpg
GNUPG_OPTS	= --yes --digest-algo RIPEMD160 -ba

# www.perl.org
PERL		= perl
PERL_OPTS	= -e

# pdfgen.sh is a wrapper around htmldoc
# (www.htmldoc.org, www.msweet.org/projects.php?Z1)
PDFGEN		= pdfgen.sh

# Apache Ant (ant.apache.org)
ANT		= ant

# Launch4j (launch4j.sf.net)
LAUNCH4J	= launch4j

# Nullsoft Scriptable Install System (nsis.sf.net)
NSIS		= makensis

# Ultimate Packer for eXecutables (upx.sf.net)
UPX		= upx

# URL of a Timestamping Authority
TSAURL		= http://time.certum.pl/
#TSAURL		= http://timestamp.verisign.com/scripts/timstamp.dll
#TSAURL		= http://timestamp.comodoca.com
#TSAURL		= http://timestamp.digicert.com
#TSAURL		= http://tsa.izenpe.com/
#TSAURL		= http://tsa.starfieldtech.com/
#TSAURL		= http://timestamp.globalsign.com/scripts/timstamp.dll

# osslsigncode-bogdro.sh is a wrapper around osslsigncode (osslsigncode.sf.net)
# that provides the default certificate. Osslsigncode allows digital signing
# of EXE files with certificates.
OSSLSIGNCODE	= osslsigncode-bogdro.sh
# Options for osslsigncode - timestamp server, name, URL and the
# hashing algorithm.
# Even though osslsigncode supports other hashes, only MD5 and SHA-1 seem to
# be compatible with WinXP (as far as I know).
OSSLSIGNCODE_O  = -t $(TSAURL) -n JYMAG -i http://jymag.sf.net -h sha1

# Java jar signer
JARSIGN		= jarsigner
JARSIGN_ALIAS	= 'jymag-sign-cert'
JARSIGN_OPTS	= -digestalg SHA-512 -sigalg SHA512withRSA -tsa $(TSAURL)

# pdfsign.sh is a wrapper around PortableSigner (portablesigner.sf.net)
PDFSIGNER	= pdfsign.sh

###########################################################################
# Internal variables
###########################################################################

# JYMAG version
VER		= 1.6

# The current year, month and day of month used for formatting the dates.
# Watch out for values < 10.
YEAR		= $(shell $(PERL) $(PERL_OPTS) 'print 1900+((localtime)[5]);')
MONTH		= $(shell $(PERL) $(PERL_OPTS) 'printf("%02d", (localtime)[4]+1);')
DAYOFMONTH	= $(shell $(PERL) $(PERL_OPTS) 'printf("%02d", (localtime)[3]);')

SED_FIX_FILEVERSION = 's/<fileVersion>[^<]*<\/fileVersion>/<fileVersion>$(VER).0.0<\/fileVersion>/'
SED_FIX_TXTFILEVERSION = 's/<fileVersion>[^<]*<\/fileVersion>/<fileVersion>$(VER).0.0<\/fileVersion>/'
SED_FIX_PRODUCTVERSION = 's/<txtFileVersion>[^<]*<\/txtFileVersion>/<txtFileVersion>$(VER)<\/txtFileVersion>/'
SED_FIX_TXTPRODUCTVERSION = 's/<txtProductVersion>[^<]*<\/txtProductVersion>/<txtProductVersion>$(VER)<\/txtProductVersion>/'
SED_FIX_COPYRIGHT = 's/<copyright>[^<]*<\/copyright>/<copyright>Bogdan \&apos;bogdro\&apos; Drozdowski 2008-$(YEAR)<\/copyright>/'

FILE_L4J_CONFIG = setup/launch4j-jymag.xml
FILE_L4J_CONFIG_EN = setup/launch4j-jymag-en.xml
FILE_L4J_EXE = setup/jymag.exe
FILE_L4J_EXE_EN = setup/jymag-en.exe
FILE_L4J_EXE_SIGNED = setup/jymag-signed.exe
FILE_L4J_EXE_SIGNED_EN = setup/jymag-en-signed.exe

FILE_ARCH_SRC = JYMAG-src-$(VER).$(PACK_EXT)
FILE_ARCH_BIN = JYMAG-bin-$(VER).$(PACK_EXT)
FILE_ARCH_JAVADOC = JYMAG-javadoc-$(VER).$(PACK_EXT)

FILE_MANUAL_EN = JYMAG-manual-EN.pdf
FILE_MANUAL_EN_SIGNED = JYMAG-manual-EN-signed.pdf
FILE_MANUAL_PL = JYMAG-manual-PL.pdf
FILE_MANUAL_PL_SIGNED = JYMAG-manual-PL-signed.pdf

FILE_INSTALLER = Setup-JYMAG-$(VER).exe
FILE_INSTALLER_SIGNED = Setup-JYMAG-$(VER)-signed.exe
FILE_INSTALLER_CFG = setup/jymag.nsi

FILE_PROGRAM = dist/JYMAG.jar
FILE_PROGRAM_SIGNED = dist/JYMAG-signed.jar

FILE_ANY_SOURCE_FILE = src/BogDroSoft/jymag/Starter.java

DIR_TMP_DIST = JYMAG-$(VER)

###########################################################################
# Targets
###########################################################################

# NOTE - using "&&" is required, because we're changing directories and
# we want the subsequent commands to be executed in the directory we've
# just changed to.
# This won't work if we write separate commands one per line, because each
# is run in its own subshell, so changing directories wouldn't affect the
# subsequent commands.

all:	jar

###########################################################################
# Distribution packages
###########################################################################

# pack-src should be last, because it deletes elements used by other targets
pack:	pack-javadoc pack-bin installer installer-signed pack-src

pack-src:	../$(FILE_ARCH_SRC)

# better not use tar jcf (or zip) ../JYMAG-xxx ../JYMAG, because
# some systems don't like it
../$(FILE_ARCH_SRC): clean Makefile
	cd .. && \
	$(DEL) $(DIR_TMP_DIST) $(FILE_ARCH_SRC) $(FILE_ARCH_SRC).asc && \
	$(COPY) JYMAG $(DIR_TMP_DIST) && \
	$(TOUCH) $(PACK_EXCL_SRC) && \
	$(PACK) $(FILE_ARCH_SRC) $(PACK_OTPS) $(DIR_TMP_DIST) && \
	$(DEL) $(DIR_TMP_DIST) && \
	$(GNUPG) $(GNUPG_OPTS) $(FILE_ARCH_SRC)

pack-bin:	../$(FILE_ARCH_BIN) test

# better not use tar jcf (or zip) ../JYMAG-xxx ../JYMAG, because
# some systems don't like it
../$(FILE_ARCH_BIN):	manual jar Makefile
	$(DEL) dist/javadoc && \
	cd .. && \
	$(DEL) $(DIR_TMP_DIST) $(FILE_ARCH_BIN) $(FILE_ARCH_BIN).asc && \
	$(COPY) JYMAG $(DIR_TMP_DIST) && \
	$(TOUCH) $(PACK_EXCL_SRC) && \
	$(PACK) $(FILE_ARCH_BIN) $(PACK_OTPS) \
		$(DIR_TMP_DIST)/AUTHORS	\
		$(DIR_TMP_DIST)/COPYING	\
		$(DIR_TMP_DIST)/ChangeLog	\
		$(DIR_TMP_DIST)/INSTALL	\
		$(DIR_TMP_DIST)/README	\
		$(DIR_TMP_DIST)/dist	\
		$(DIR_TMP_DIST)/run*.sh	\
		$(DIR_TMP_DIST)/run*.bat	\
		$(DIR_TMP_DIST)/manual	\
		$(DIR_TMP_DIST)/JYMAG-manual-*.pdf	\
		$(DIR_TMP_DIST)/THANKS	\
		$(DIR_TMP_DIST)/jymag.desktop	&& \
	$(DEL) $(DIR_TMP_DIST) && \
	$(GNUPG) $(GNUPG_OPTS) $(FILE_ARCH_BIN)

pack-javadoc:	../$(FILE_ARCH_JAVADOC)

# better not use tar jcf (or zip) ../JYMAG-xxx ../JYMAG, because
# some systems don't like it
../$(FILE_ARCH_JAVADOC):	dist/javadoc Makefile
	cd .. && \
	$(DEL) $(DIR_TMP_DIST) $(FILE_ARCH_JAVADOC) $(FILE_ARCH_JAVADOC).asc && \
	$(COPY) JYMAG $(DIR_TMP_DIST) && \
	$(TOUCH) $(PACK_EXCL_SRC) && \
	$(PACK) $(FILE_ARCH_JAVADOC) $(PACK_OTPS) $(DIR_TMP_DIST)/dist/javadoc && \
	$(DEL) $(DIR_TMP_DIST) && \
	$(GNUPG) $(GNUPG_OPTS) $(FILE_ARCH_JAVADOC)

dist/javadoc:	$(shell find src) Makefile
	$(DEL) dist/javadoc
	$(ANT) javadoc

###########################################################################
# Manuals
###########################################################################

manual: $(FILE_MANUAL_EN) $(FILE_MANUAL_PL)

$(FILE_MANUAL_EN): manual/en/*.html manual/rsrc/css/* manual/en/rsrc/* \
	manual/pl/rsrc/* manual/rsrc/license.html Makefile
	$(PDFGEN) $(FILE_MANUAL_EN) \
		manual/en/index.html \
		manual/en/readme.html \
		manual/en/main_window.html \
		manual/en/tables.html \
		manual/en/about_window.html \
		manual/en/capability.html \
		manual/en/manual_cmd.html \
		manual/en/signal_power.html \
		manual/rsrc/license.html
	$(PDFSIGNER) $(FILE_MANUAL_EN_SIGNED) $(FILE_MANUAL_EN)
	$(MOVE) $(FILE_MANUAL_EN_SIGNED)  $(FILE_MANUAL_EN)

$(FILE_MANUAL_PL): manual/pl/*.html manual/rsrc/css/* manual/en/rsrc/* \
	manual/pl/rsrc/* manual/rsrc/license.html Makefile
	$(PDFGEN) $(FILE_MANUAL_PL) \
		manual/pl/index.html \
		manual/pl/readme.html \
		manual/pl/main_window.html \
		manual/pl/tables.html \
		manual/pl/about_window.html \
		manual/pl/capability.html \
		manual/pl/manual_cmd.html \
		manual/pl/signal_power.html \
		manual/rsrc/license.html
	$(PDFSIGNER) $(FILE_MANUAL_PL_SIGNED) $(FILE_MANUAL_PL)
	$(MOVE) $(FILE_MANUAL_PL_SIGNED)  $(FILE_MANUAL_PL)

manual-clean:
	$(DEL) $(FILE_MANUAL_EN)
	$(DEL) $(FILE_MANUAL_PL)

###########################################################################
# Application
###########################################################################

jar: jar-clean $(FILE_PROGRAM)

$(FILE_PROGRAM):	$(shell find src) build.xml \
	nbproject/build-impl.xml Makefile
	$(ANT) jar
	$(TOUCH) $(FILE_PROGRAM)

jar-clean:
	$(ANT) clean

###########################################################################
# Installer
###########################################################################

installer:	jar manual test $(FILE_L4J_EXE) $(FILE_L4J_EXE_EN) \
	setup/$(FILE_INSTALLER)

$(FILE_L4J_EXE):	$(FILE_L4J_CONFIG) setup/jymag.ico Makefile
	$(SED) $(SED_OPTS) $(SED_FIX_FILEVERSION) $(FILE_L4J_CONFIG)
	$(SED) $(SED_OPTS) $(SED_FIX_TXTFILEVERSION) $(FILE_L4J_CONFIG)
	$(SED) $(SED_OPTS) $(SED_FIX_PRODUCTVERSION) $(FILE_L4J_CONFIG)
	$(SED) $(SED_OPTS) $(SED_FIX_TXTPRODUCTVERSION) $(FILE_L4J_CONFIG)
	$(SED) $(SED_OPTS) $(SED_FIX_COPYRIGHT) $(FILE_L4J_CONFIG)
	$(LAUNCH4J) $(PWD)/$(FILE_L4J_CONFIG)
	$(DEL) ../jymag-$(VER).exe
	$(COPY) $@ ../jymag-$(VER).exe

$(FILE_L4J_EXE_EN):	$(FILE_L4J_CONFIG_EN) setup/jymag.ico Makefile
	$(SED) $(SED_OPTS) $(SED_FIX_FILEVERSION) $(FILE_L4J_CONFIG_EN)
	$(SED) $(SED_OPTS) $(SED_FIX_TXTFILEVERSION) $(FILE_L4J_CONFIG_EN)
	$(SED) $(SED_OPTS) $(SED_FIX_PRODUCTVERSION) $(FILE_L4J_CONFIG_EN)
	$(SED) $(SED_OPTS) $(SED_FIX_TXTPRODUCTVERSION) $(FILE_L4J_CONFIG_EN)
	$(SED) $(SED_OPTS) $(SED_FIX_COPYRIGHT) $(FILE_L4J_CONFIG_EN)
	$(LAUNCH4J) $(PWD)/$(FILE_L4J_CONFIG_EN)
	$(DEL) ../jymag-en-$(VER).exe
	$(COPY) $@ ../jymag-en-$(VER).exe

setup/$(FILE_INSTALLER):	$(FILE_INSTALLER_CFG) AUTHORS ChangeLog \
		INSTALL COPYING README THANKS jar Makefile
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
	$(SED) $(SED_OPTS) 's/!define VERSION .*/!define VERSION $(VER)/' $(FILE_INSTALLER_CFG)
	$(SED) $(SED_OPTS) 's/!define YEAR .*/!define YEAR $(YEAR)/' $(FILE_INSTALLER_CFG)
	$(SED) $(SED_OPTS) 's/!define MONTH .*/!define MONTH $(MONTH)/' $(FILE_INSTALLER_CFG)
	$(SED) $(SED_OPTS) 's/!define DAYOFMONTH .*/!define DAYOFMONTH $(DAYOFMONTH)/' $(FILE_INSTALLER_CFG)
	$(NSIS) $(FILE_INSTALLER_CFG)
	#$(UPX) setup/$(FILE_INSTALLER)
	#$(OSSLSIGNCODE) $(OSSLSIGNCODE_O) -in $@ -out $@-signed.exe
	#$(MOVE) $@-signed.exe $@
	$(DEL) ../$(FILE_INSTALLER)
	$(COPY) $@ ..
	$(GNUPG) $(GNUPG_OPTS) ../$(FILE_INSTALLER)

installer-clean:
	$(DEL) setup/$(FILE_INSTALLER)
	$(DEL) $(FILE_PROGRAM)
	$(DEL) $(FILE_L4J_EXE)
	$(DEL) $(FILE_L4J_EXE_EN)
	$(DEL) setup/AUTHORS
	$(DEL) setup/ChangeLog
	$(DEL) setup/INSTALL
	$(DEL) setup/license.txt
	$(DEL) setup/README
	$(DEL) setup/THANKS

###########################################################################
# Signed installer
###########################################################################

# The signed installer uses the signed JARs and EXEs, so touch the respective
# source files after building, so that building the unsigned installer will
# use the unsigned files again

jar-signed: jar $(FILE_PROGRAM_SIGNED)

$(FILE_PROGRAM_SIGNED): jar Makefile
	$(JARSIGN) -signedjar $@ $(JARSIGN_OPTS) $(FILE_PROGRAM) $(JARSIGN_ALIAS)
	$(MOVE) $@ $(FILE_PROGRAM)
	$(TOUCH) $(FILE_ANY_SOURCE_FILE)

installer-signed:	jar-signed installer manual $(FILE_L4J_EXE) \
		$(FILE_L4J_EXE_EN) setup/$(FILE_INSTALLER_SIGNED)

setup/$(FILE_INSTALLER_SIGNED):	installer $(FILE_INSTALLER_CFG) AUTHORS \
		ChangeLog INSTALL COPYING README THANKS jar-signed Makefile
	$(OSSLSIGNCODE) $(OSSLSIGNCODE_O) -in $(FILE_L4J_EXE) -out $(FILE_L4J_EXE_SIGNED)
	$(MOVE) $(FILE_L4J_EXE_SIGNED) $(FILE_L4J_EXE)
	$(OSSLSIGNCODE) $(OSSLSIGNCODE_O) -in $(FILE_L4J_EXE_EN) -out $(FILE_L4J_EXE_SIGNED_EN)
	$(MOVE) $(FILE_L4J_EXE_SIGNED_EN) $(FILE_L4J_EXE_EN)
	$(NSIS) $(FILE_INSTALLER_CFG)
	$(OSSLSIGNCODE) $(OSSLSIGNCODE_O) -in setup/$(FILE_INSTALLER) -out $@
	#$(MOVE) $@ setup/$(FILE_INSTALLER)
	$(DEL) ../$(FILE_INSTALLER_SIGNED)
	$(COPY) $@ ..
	$(GNUPG) $(GNUPG_OPTS) ../$(FILE_INSTALLER_SIGNED)
	$(TOUCH) $(FILE_L4J_CONFIG) $(FILE_L4J_CONFIG_EN) $(FILE_INSTALLER_CFG)

installer-signed-clean:
	$(DEL) setup/$(FILE_INSTALLER_SIGNED)
	$(DEL) $(FILE_PROGRAM_SIGNED)
	$(DEL) $(FILE_L4J_EXE_SIGNED)
	$(DEL) $(FILE_L4J_EXE_SIGNED_EN)

###########################################################################
# Other targets
###########################################################################

javadoc-clean:
	$(DEL) dist/javadoc

check:	test

test:
	$(ANT) test

clean:	installer-signed-clean installer-clean manual-clean javadoc-clean jar-clean

.PHONY:	all pack pack-src pack-bin pack-javadoc manual jar clean installer \
	jar-signed installer-signed check test installer-signed-clean \
	installer-clean manual-clean javadoc-clean

