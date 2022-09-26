#
# JYMAG hand-made Makefile for creating distribution packages.
#

VER	= 0.9

all:	dist
dist:	dist-src dist-bin

dist-src:
	cd .. && \
	rm -f JYMAG-src-$(VER).tar.gz JYMAG-src-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-src-$(VER).tar.gz JYMAG-$(VER)/ && \
	rm -rf JYMAG-$(VER) && \
	gpg --digest-algo RIPEMD160 -ba JYMAG-src-$(VER).tar.gz

dist-bin:
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
		JYMAG-$(VER)/THANKS	&& \
	rm -rf JYMAG-$(VER) && \
	gpg --digest-algo RIPEMD160 -ba JYMAG-bin-$(VER).tar.gz

.PHONY:	dist-src dist-bin dist all

