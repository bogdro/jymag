#
# JYMAG hand-made Makefile for creating distribution packages.
#

VER	= 0.5

all:	dist
dist:	dist-src dist-bin

dist-src:
	cd .. && \
	rm -f JYMAG-src-$(VER).tar.gz && \
	rm -f JYMAG-src-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-src-$(VER).tar.gz JYMAG-$(VER)/ && \
	rm -rf JYMAG-$(VER) && \
	gpg -ba JYMAG-src-$(VER).tar.gz

dist-bin:
	cd .. && \
	rm -f JYMAG-bin-$(VER).tar.gz && \
	rm -f JYMAG-bin-$(VER).tar.gz.asc && \
	cp -r JYMAG JYMAG-$(VER) && \
	tar zcf JYMAG-bin-$(VER).tar.gz \
		JYMAG-$(VER)/AUTHORS	\
		JYMAG-$(VER)/COPYING	\
		JYMAG-$(VER)/INSTALL	\
		JYMAG-$(VER)/README	\
		JYMAG-$(VER)/run.sh	\
		JYMAG-$(VER)/dist	\
		JYMAG-$(VER)/run.bat	\
		JYMAG-$(VER)/THANKS	&& \
	rm -rf JYMAG-$(VER) && \
	gpg -ba JYMAG-bin-$(VER).tar.gz

.PHONY:	dist-src dist-bin

