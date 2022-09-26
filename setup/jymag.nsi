; JYMAG version included in the installer. Must be all-numeric!
!define VERSION 1.5

; The JYMAG publisher
!define PUBLISHER "Bogdan 'bogdro' Drozdowski"

; The JYMAG website address
!define JYMAGURL "http://jymag.sf.net"

!include "Sections.nsh"
!include "FileFunc.nsh"

; The name of the installer, displayed in the title bar:
Name "JYMAG ${VERSION}"

; The name of the installer FILE (.exe)
OutFile "Setup-JYMAG-${VERSION}.exe"

; The default installation directory:
InstallDir "$PROGRAMFILES\JYMAG"

; Registry key to check for directory (so if you install again, it will
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\JYMAG" "Install_Dir"

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

; the current year is now provided by 'make'
;!searchparse /noerrors "${__DATE__}" "" YEAR "-"
!define YEAR 2016
!define MONTH 06
!define DAYOFMONTH 30

VIAddVersionKey "ProductName" "JYMAG"
VIAddVersionKey "Comments" "Installer created on ${YEAR}-${MONTH}-${DAYOFMONTH}, ${__TIME__} with the free Nullsoft Scriptable Install System, http://nsis.sf.net"
VIAddVersionKey "LegalCopyright" "Copyright (c) 2008-${YEAR} ${PUBLISHER}"
VIAddVersionKey "FileDescription" "The JYMAG installer for Windows"
VIAddVersionKey "FileVersion" "${VERSION}"
VIAddVersionKey "ProductVersion" "${VERSION}"

RequestExecutionLevel none
LicenseData "license.txt"

; -----------------------------
; Variables:

Var win_root
Var inst_root
Var is_removable
Var has_start_menu

; -----------------------------
; Installer pages (screens)

Page license
Page components
; "" "" afterComponentSelect
Page directory "" "" afterDirSelect
Page instfiles "" "" afterInstall

UninstPage uninstConfirm
UninstPage instfiles

; -----------------------------
; Functions:

Function afterInstall

	StrCmp $is_removable "1" skip_reg

		; Write the installation path into the registry
		WriteRegStr HKLM "SOFTWARE\JYMAG" "Install_Dir" "$INSTDIR"

		; write unsintallation information for the Control Panel:
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"DisplayName" "JYMAG ${VERSION} ($INSTDIR)"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"UninstallString" "$INSTDIR\uninstall.exe"
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"NoModify" 1
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"NoRepair" 1
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"InstallLocation" "$INSTDIR"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"Publisher" "${PUBLISHER}"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"HelpLink" "${JYMAGURL}"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"URLUpdateInfo" "${JYMAGURL}"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"URLInfoAbout" "${JYMAGURL}"
		WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"DisplayVerstion" "${VERSION}"
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"VersionMajor" ${VER_MAJOR}
		WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"\
			"VersionMinor" ${VER_MINOR}

	skip_reg:

		WriteUninstaller "uninstall.exe"

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
	DeleteRegKey HKLM "SOFTWARE\JYMAG"
	; delete uninstallation information from the Control Panel:
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JYMAG"

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

	IfRebootFlag 0 end
		MessageBox MB_YESNOCANCEL "The system needs to be rebooted to finish the uninstallation.\
			$\r$\nDo you wish to reboot now?" IDNO end IDCANCEL end
		Reboot

	end:

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

