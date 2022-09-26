#
# JYMAG hand-made Makefile for creating distribution packages.
#

VER	= 0.3

dist:	dist-src dist-bin

dist-src:
	cd .. && rm -f JYMAG-src-$(VER).tar.gz && tar zcf JYMAG-src-$(VER).tar.gz JYMAG/

dist-bin:
	cd .. && rm -f JYMAG-bin-$(VER).tar.gz && tar zcf JYMAG-bin-$(VER).tar.gz \
		JYMAG/AUTHORS	\
		JYMAG/COPYING	\
		JYMAG/INSTALL	\
		JYMAG/README	\
		JYMAG/run.sh	\
		JYMAG/dist	\
		JYMAG/run.bat	\
		JYMAG/THANKS

