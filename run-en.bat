@echo off
if exist dist\JYMAG.jar javaw -jar -Duser.language=en -Duser.country=US dist\JYMAG.jar %*
if exist JYMAG.jar javaw -jar -Duser.language=en -Duser.country=US JYMAG.jar %*
