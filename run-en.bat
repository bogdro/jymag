@echo off
rem
rem JYMAG startup shell script for starting in English.
rem
rem Copyright (C) 2008-2024 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
rem
rem This program is free software: you can redistribute it and/or modify
rem it under the terms of the GNU General Public License as published by
rem the Free Software Foundation, either version 3 of the License, or
rem (at your option) any later version.
rem
rem This program is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
rem GNU General Public License for more details.
rem
rem You should have received a copy of the GNU General Public License
rem along with this program.  If not, see <http://www.gnu.org/licenses/>.

if exist dist\JYMAG.jar javaw -jar -Duser.language=en -Duser.country=US dist\JYMAG.jar %*
if exist JYMAG.jar javaw -jar -Duser.language=en -Duser.country=US JYMAG.jar %*
