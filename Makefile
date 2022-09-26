#
# JYMAG hand-made Makefile for creating distribution packages.
# Copyright (C) 2008-2011 Bogdan 'bogdro' Drozdowski, bogdandr @ op . pl
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

VER	= 1.1

HTMLDOC_OPTS = --charset iso-8859-2 -t pdf14 --encryption --duplex --permissions none,print \
	--compression=9 --links --linkcolor blue \
	--linkstyle underline --header 'd.c' --footer '1.h' --webpage

HASH_ALGO = RIPEMD160

all:	dist
# dist-src should be last, because it deletes elements used by dist-bin and dist-javadoc
dist:	dist-javadoc dist-bin dist-src

dist-src:	../JYMAG-src-$(VER).tar.gz

../JYMAG-src-$(VER).tar.gz:
	rm -fr JYMAG-manual-PL.pdf JYMAG-manual-EN.pdf dist/javadoc setup/jymag.exe dist/JYMAG.jar && \
	cd .. && \
	rm -f JYMAG-src-$(VER).tar.gz JYMAG-src-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-src-$(VER).tar.gz JYMAG-$(VER)/ && \
	rm -rf JYMAG-$(VER) && \
	gpg --digest-algo $(HASH_ALGO) -ba JYMAG-src-$(VER).tar.gz

dist-bin:	../JYMAG-bin-$(VER).tar.gz

../JYMAG-bin-$(VER).tar.gz:	manual
	rm -fr dist/javadoc && \
	cd .. && \
	rm -f JYMAG-bin-$(VER).tar.gz JYMAG-bin-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-bin-$(VER).tar.gz \
		JYMAG-$(VER)/AUTHORS	\
		JYMAG-$(VER)/COPYING	\
		JYMAG-$(VER)/ChangeLog	\
		JYMAG-$(VER)/INSTALL	\
		JYMAG-$(VER)/README	\
		JYMAG-$(VER)/dist	\
		JYMAG-$(VER)/run.sh	\
		JYMAG-$(VER)/run.bat	\
		JYMAG-$(VER)/run-en.sh	\
		JYMAG-$(VER)/run-en.bat	\
		JYMAG-$(VER)/manual	\
		JYMAG-$(VER)/JYMAG-manual-EN.pdf	\
		JYMAG-$(VER)/JYMAG-manual-PL.pdf	\
		JYMAG-$(VER)/THANKS	&& \
	rm -rf JYMAG-$(VER) && \
	gpg --digest-algo $(HASH_ALGO) -ba JYMAG-bin-$(VER).tar.gz

manual:	JYMAG-manual-EN.pdf JYMAG-manual-PL.pdf

JYMAG-manual-EN.pdf: manual/en/*.html manual/rsrc/css/* manual/rsrc/img/* manual/rsrc/license.html
	htmldoc $(HTMLDOC_OPTS) -f JYMAG-manual-EN.pdf \
		manual/en/index.html \
		manual/en/readme.html \
		manual/en/main_window.html \
		manual/en/tables.html \
		manual/en/about_window.html \
		manual/en/capability.html \
		manual/en/manual_cmd.html \
		manual/en/signal_power.html \
		manual/rsrc/license.html

JYMAG-manual-PL.pdf: manual/pl/*.html manual/rsrc/css/* manual/rsrc/img/* manual/rsrc/license.html
	htmldoc $(HTMLDOC_OPTS) -f JYMAG-manual-PL.pdf \
		manual/pl/index.html \
		manual/pl/readme.html \
		manual/pl/main_window.html \
		manual/pl/tables.html \
		manual/pl/about_window.html \
		manual/pl/capability.html \
		manual/pl/manual_cmd.html \
		manual/pl/signal_power.html \
		manual/rsrc/license.html

dist-javadoc:	../JYMAG-javadoc-$(VER).tar.gz

../JYMAG-javadoc-$(VER).tar.gz:	dist/javadoc
	cd .. && \
	rm -f JYMAG-javadoc-$(VER).tar.gz JYMAG-javadoc-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-javadoc-$(VER).tar.gz \
		JYMAG-$(VER)/dist/javadoc && \
	rm -rf JYMAG-$(VER) && \
	gpg --digest-algo $(HASH_ALGO) -ba JYMAG-javadoc-$(VER).tar.gz


.PHONY:	dist-src dist-bin dist all manual dist-javadoc

