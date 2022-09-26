/*
 * JYMAGTab.java, part of the JYMAG package.
 *
 * Copyright (C) 2012-2020 Bogdan Drozdowski, bogdandr (at) op.pl
 * License: GNU General Public License, v3+
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foudation:
 *		Free Software Foundation
 *		51 Franklin Street, Fifth Floor
 *		Boston, MA 02110-1301
 *		USA
 *
 */

package BogDroSoft.jymag.gui.panels;

import BogDroSoft.jymag.gui.MainWindow;

import javax.swing.JProgressBar;
import javax.swing.JSpinner;

/**
 * This class is the panel interface. All panels in all tabs
 * of the JYMAG program should implement this.
 * @author Bogdan Drozdowski
 */
public interface JYMAGTab
{
	/**
	 * Sets the main progress bar component.
	 * @param progressBar The main progress bar component.
	 */
	public void setProgressBar (JProgressBar progressBar);

	/**
	 * Sets the destination directory for downloads.
	 * @param destDirName The destination directory.
	 */
	public void setDestDir (String destDirName);

	/**
	 * Sets the reference to the main window of JYMAG.
	 * @param mainWindow The reference to the main window of JYMAG.
	 */
	public void setMainWindow (MainWindow mainWindow);

	/**
	 * Sets the font size spinner.
	 * @param fontSizeSpinner The font size spinner.
	 */
	public void setFontSizeSpin (JSpinner fontSizeSpinner);
}
