#!/bin/sh

if ( [ -e dist/JYMAG.jar ] ); then
	java -jar dist/JYMAG.jar
elif ( [ -e JYMAG.jar ] ); then
	java -jar JYMAG.jar
fi