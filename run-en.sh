#!/bin/sh

if ( [ -e dist/JYMAG.jar ] ); then
	java -jar -Duser.language=en -Duser.country=US dist/JYMAG.jar "$*"
elif ( [ -e JYMAG.jar ] ); then
	java -jar -Duser.language=en -Duser.country=US JYMAG.jar "$*"
fi