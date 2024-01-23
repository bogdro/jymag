;
; JYMAG NSIS script for creating installers.
; Copyright (C) 2011-2024 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
;
; This program is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
;
; This program is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
;
; You should have received a copy of the GNU General Public License
; along with this program.  If not, see <http://www.gnu.org/licenses/>.

; JYMAG version included in the installer. Must be all-numeric!
;!define VERSION A.B ; now provided by 'make' on the command line

; The JYMAG publisher
!define PUBLISHER "Bogdan 'bogdro' Drozdowski"

; The JYMAG website address
!define JYMAGURL "https://jymag.sourceforge.io/"

!define REG_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"
!define REG_DIR_INSTDIR "Software\JYMAG"
!define REG_KEY_INSTDIR "Install_Dir"

SetCompressor /solid lzma
!if "${OLDSETUP}" != 1
	Unicode true	; use only in the modern installer
!endif

!include "Sections.nsh"
!include "FileFunc.nsh"

!if "${OLDSETUP}" != 1
	!include "MUI2.nsh"
!endif

; The name of the installer, displayed in the title bar:
Name "JYMAG ${VERSION}"

; The name of the installer FILE (.exe)
OutFile "JYMAG-${VERSION}-setup.exe"

; The default installation directory:
InstallDir "$PROGRAMFILES\JYMAG"

; Registry key to check for directory (so if you install again, it will
; overwrite the old one automatically)
InstallDirRegKey HKLM "${REG_DIR_INSTDIR}" "${REG_KEY_INSTDIR}"

CRCCheck force

; Parse the version number:
!searchparse /noerrors "${VERSION}" "" VER_MAJOR "." VER_MINOR "." VER_RELEASE "." VER_PATCH
!ifndef VER_MAJOR
	!define VER_MAJOR ""
!endif
!ifndef VER_MINOR
	!define VER_MINOR ""
!endif
!ifndef VER_RELEASE
	!define VER_RELEASE ""
!endif
!ifndef VER_PATCH
	!define VER_PATCH ""
!endif

; check how many version elements we have
!if "${VER_PATCH}" != ""
	VIProductVersion "${VER_MAJOR}.${VER_MINOR}.${VER_RELEASE}.${VER_PATCH}"
!else
	!if "${VER_RELEASE}" != ""
		VIProductVersion "${VER_MAJOR}.${VER_MINOR}.${VER_RELEASE}.0"
	!else
		!if "${VER_MINOR}" != ""
			VIProductVersion "${VER_MAJOR}.${VER_MINOR}.0.0"
		!else
			!if "${VER_MAJOR}" != ""
				VIProductVersion "${VER_MAJOR}.0.0.0"
			!else
				VIProductVersion "0.0.0.0"
			!endif
		!endif
	!endif
!endif

; the current date is now provided by 'make' on the command line
;!searchparse /noerrors "${__DATE__}" "" YEAR "-"
;!define YEAR 2020
;!define MONTH 02
;!define DAYOFMONTH 03

VIAddVersionKey "ProductName" "JYMAG"
; comments seem to appear only in MSI files
VIAddVersionKey "Comments" "Installer created on ${YEAR}-${MONTH}-${DAYOFMONTH}, ${__TIME__} with the free Nullsoft Scriptable Install System, https://nsis.sourceforge.io"
VIAddVersionKey "LegalCopyright" "Copyright (c) 2008-${YEAR} ${PUBLISHER}"
VIAddVersionKey "FileDescription" "The JYMAG installer"
VIAddVersionKey "FileVersion" "${VERSION}"
VIAddVersionKey "ProductVersion" "${VERSION}"

!if "${OLDSETUP}" != 1
	RequestExecutionLevel none
!else
	; prevent trashing the common menu for all users:
	RequestExecutionLevel admin
!endif

; leave the default NSIS icon for now, otherwise many files will have the same icon
!if "${OLDSETUP}" != 1
	;!define MUI_ICON jymag.ico
	;!define MUI_UNICON jymag.ico
!else
	;Icon jymag.ico
!endif

LicenseData "license.txt"

; -----------------------------
; Variables:

Var win_root
Var inst_root
Var is_removable
Var has_start_menu

; -----------------------------
; Installer pages (screens)

!if "${OLDSETUP}" != 1

	!define MUI_FINISHPAGE_NOAUTOCLOSE
	!define MUI_UNFINISHPAGE_NOAUTOCLOSE

	!insertmacro MUI_PAGE_WELCOME
	!insertmacro MUI_PAGE_LICENSE "license.txt"
	!insertmacro MUI_PAGE_COMPONENTS

	!define MUI_PAGE_CUSTOMFUNCTION_LEAVE afterDirSelect
	!insertmacro MUI_PAGE_DIRECTORY

	!define MUI_PAGE_CUSTOMFUNCTION_LEAVE afterInstall
	!insertmacro MUI_PAGE_INSTFILES

	!insertmacro MUI_PAGE_FINISH

	!insertmacro MUI_UNPAGE_WELCOME
	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
	!insertmacro MUI_UNPAGE_FINISH

	!insertmacro MUI_LANGUAGE "English"
!else
	Page license
	Page components
	Page directory "" "" afterDirSelect
	Page instfiles "" "" afterInstall

	UninstPage uninstConfirm
	UninstPage instfiles
!endif

; -----------------------------
; Functions:

Function afterInstall

	; write the uninstaller before we calculate the installation size
	WriteUninstaller "uninstall.exe"

	StrCmp $is_removable "1" skip_reg

		; Write the installation path into the registry
		WriteRegStr   HKLM "${REG_DIR_INSTDIR}" "${REG_KEY_INSTDIR}" "$INSTDIR"

		; write uninstallation information for the Control Panel:
		WriteRegStr   HKLM "${REG_KEY}" "DisplayIcon" "$INSTDIR\jymag-en.exe"
		WriteRegStr   HKLM "${REG_KEY}" "DisplayName" "JYMAG ${VERSION} ($INSTDIR)"
		WriteRegStr   HKLM "${REG_KEY}" "DisplayVersion" "${VERSION}"
		WriteRegStr   HKLM "${REG_KEY}" "HelpLink" "${JYMAGURL}"
		WriteRegStr   HKLM "${REG_KEY}" "InstallLocation" "$INSTDIR"
		WriteRegDWORD HKLM "${REG_KEY}" "NoModify" 1
		WriteRegDWORD HKLM "${REG_KEY}" "NoRepair" 1
		WriteRegStr   HKLM "${REG_KEY}" "Publisher" "${PUBLISHER}"
		WriteRegStr   HKLM "${REG_KEY}" "URLInfoAbout" "${JYMAGURL}"
		WriteRegStr   HKLM "${REG_KEY}" "URLUpdateInfo" "${JYMAGURL}"
		WriteRegStr   HKLM "${REG_KEY}" "UninstallString" "$INSTDIR\uninstall.exe"
		WriteRegDWORD HKLM "${REG_KEY}" "VersionMajor" ${VER_MAJOR}
		WriteRegDWORD HKLM "${REG_KEY}" "VersionMinor" ${VER_MINOR}

		; get the (already-installed) directory size, in kilobytes
		${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2

		WriteRegDWORD HKLM "${REG_KEY}" "EstimatedSize" $0

	skip_reg:

FunctionEnd

; -----------------------------
; The installation sections:

Section "JYMAG base files"

	SectionIn RO

	StrCpy $has_start_menu "0"

	SetOutPath "$INSTDIR"
	File "..\dist\JYMAG.jar"
	File /r "..\dist\lib"
	File "jymag.exe"
	File "jymag-en.exe"
	File /oname=README.txt "README"
	File /oname=ChangeLog.txt "ChangeLog"
	File /oname=AUTHORS.txt "AUTHORS"
	File /oname=COPYING.txt "license.txt"
	File /oname=INSTALL.txt "INSTALL"
	File /oname=THANKS.txt "THANKS"
	File "..\run.bat"
	File "..\run-en.bat"

SectionEnd

SectionGroup "Shortcuts"

	Section "Start menu shortcuts"

		StrCpy $has_start_menu "1"
		CreateDirectory "$SMPROGRAMS\JYMAG"
		CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG ${VERSION}.lnk" "$INSTDIR\jymag.exe"
		CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG ${VERSION} (English).lnk" "$INSTDIR\jymag-en.exe"
		CreateShortCut "$SMPROGRAMS\JYMAG\Uninstall JYMAG.lnk" "$INSTDIR\uninstall.exe"

	SectionEnd

	Section "Desktop shortcut"

		CreateShortCut "$DESKTOP\JYMAG.lnk" "$INSTDIR\jymag.exe"

	SectionEnd

	Section /o "Quick launch shortcut"

		StrCmp "$QUICKLAUNCH" "$TEMP" no_quicklaunch

			CreateShortCut "$QUICKLAUNCH\JYMAG.lnk" "$INSTDIR\jymag.exe"

		no_quicklaunch:

	SectionEnd

	Section /o "Send-to menu shortcut"

		ClearErrors
		FileOpen $0 "jymag-sendto.bat" "w"
		IfErrors file_error

			FileWrite $0 'start java -jar "$INSTDIR\JYMAG.jar" --upload %*$\r$\n'

			FileClose $0

			CreateShortCut "$SENDTO\JYMAG.lnk" "$INSTDIR\jymag-sendto.bat" "" "$INSTDIR\jymag.exe"

		file_error:

	SectionEnd

SectionGroupEnd

SectionGroup "Manuals"

	Section "JYMAG HTML manual (EN)"

		SetOutPath "$INSTDIR\manual"
		File /r "..\manual\en"
		File /r "..\manual\rsrc"
		; start menu shortcut
		StrCmp $has_start_menu "0" no_start_menu

			CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG HTML Manual (EN).lnk" "$INSTDIR\manual\en\index.html"

		no_start_menu:

		SetOutPath "$INSTDIR"

	SectionEnd

	Section /o "JYMAG HTML manual (PL)"

		SetOutPath "$INSTDIR\manual"
		File /r "..\manual\pl"
		File /r "..\manual\rsrc"
		; start menu shortcut
		StrCmp $has_start_menu "0" no_start_menu

			CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG HTML Manual (PL).lnk" "$INSTDIR\manual\pl\index.html"

		no_start_menu:

		SetOutPath "$INSTDIR"

	SectionEnd

	Section "JYMAG PDF manual (EN)"

		File "..\JYMAG-manual-EN.pdf"
		; start menu shortcut
		StrCmp $has_start_menu "0" no_start_menu

			CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG PDF Manual (EN).lnk" "$INSTDIR\JYMAG-manual-EN.pdf"

		no_start_menu:


	SectionEnd

	Section /o "JYMAG PDF manual (PL)"

		File "..\JYMAG-manual-PL.pdf"
		; start menu shortcut
		StrCmp $has_start_menu "0" no_start_menu

			CreateShortCut "$SMPROGRAMS\JYMAG\JYMAG PDF Manual (PL).lnk" "$INSTDIR\JYMAG-manual-PL.pdf"

		no_start_menu:


	SectionEnd

SectionGroupEnd

; -----------------------------
; The uninstall section

Section "Uninstall"

	; delete the last-installed directory information
	DeleteRegKey HKLM "${REG_DIR_INSTDIR}"
	; delete uninstallation information from the Control Panel:
	DeleteRegKey HKLM "${REG_KEY}"

	SetRebootFlag false

	Delete /REBOOTOK "$INSTDIR\uninstall.exe"
	Delete /REBOOTOK "$INSTDIR\README.txt"
	Delete /REBOOTOK "$INSTDIR\JYMAG.jar"
	Delete /REBOOTOK "$INSTDIR\jymag.exe"
	Delete /REBOOTOK "$INSTDIR\jymag-en.exe"
	Delete /REBOOTOK "$INSTDIR\jymag.log"
	Delete /REBOOTOK "$INSTDIR\ChangeLog.txt"
	Delete /REBOOTOK "$INSTDIR\AUTHORS.txt"
	Delete /REBOOTOK "$INSTDIR\COPYING.txt"
	Delete /REBOOTOK "$INSTDIR\INSTALL.txt"
	Delete /REBOOTOK "$INSTDIR\THANKS.txt"
	Delete /REBOOTOK "$INSTDIR\run.bat"
	Delete /REBOOTOK "$INSTDIR\run-en.bat"
	Delete /REBOOTOK "$INSTDIR\JYMAG-manual-EN.pdf"
	Delete /REBOOTOK "$INSTDIR\JYMAG-manual-PL.pdf"
	Delete /REBOOTOK "$INSTDIR\jymag-sendto.bat"

	RMDir /r /REBOOTOK "$INSTDIR\lib"
	RMDir /r /REBOOTOK "$INSTDIR\manual"
	RMDir /REBOOTOK "$INSTDIR"

	RMDir /r /REBOOTOK "$SMPROGRAMS\JYMAG"
	Delete /REBOOTOK "$DESKTOP\JYMAG.lnk"
	Delete /REBOOTOK "$QUICKLAUNCH\JYMAG.lnk"
	Delete /REBOOTOK "$SENDTO\JYMAG.lnk"

	; Reboot is handled by the Modern UI automatically,
	; so handle it manually only in the classic UI:
!if "${OLDSETUP}" == 1

	IfRebootFlag 0 end
		MessageBox MB_YESNOCANCEL "The system needs to be rebooted to finish the uninstallation.\
			$\r$\nDo you wish to reboot now?" IDNO end IDCANCEL end
		Reboot

	end:
!endif

SectionEnd

; -----------------------------

Function driveIterator

	; $9 = drive letter ("c:\")
	; $8 = drive type ("FDD")

	StrCpy $r0 ""

	StrCmp $9 "$inst_root\" 0 end_iter

		StrCmp $8 "FDD" 0 last_iter

			StrCpy $is_removable "1"

		last_iter:
			StrCpy $r0 "StopGetDrives"

	end_iter:
		Push $r0

FunctionEnd

Function afterDirSelect

	; check if we're installing on a removable non-system drive
	; and if we are, ask whether to write to the registry

	${GetRoot} $WINDIR $win_root
	${GetRoot} $INSTDIR $inst_root

	; always allow to install on the same drive as the system drive
	StrCmp $win_root $inst_root dir_ok

		StrCpy $is_removable "0"
		${GetDrives} "" "driveIterator"

		StrCmp $is_removable "0" dir_ok

			MessageBox MB_YESNOCANCEL|MB_ICONQUESTION \
				"The target drive is a removable non-system drive.\
				$\r$\nDo you want to write registry entries to THIS system?"\
				IDYES dir_ok IDCANCEL cancel_change

				; IDNO here
				StrCpy $is_removable "1"
				Return

			cancel_change:
				Abort

	dir_ok:
		StrCpy $is_removable "0"

FunctionEnd
