/*
 * JYMAGTab.java, part of the JYMAG package.
 *
 * Copyright (C) 2012-2018 Bogdan Drozdowski, bogdandr (at) op.pl
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

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
	 * Sets the port list combo box field.
	 * @param combo The component which the port name should be taken from.
	 */
	@SuppressWarnings("rawtypes")
	public void setPortCombo (JComboBox combo);

	/**
	 * Sets the transmission speed combo box field.
	 * @param combo The component which the transmission speed should be taken from.
	 */
	@SuppressWarnings("rawtypes")
	public void setSpeedCombo (JComboBox combo);

	/**
	 * Sets the data bits' combo box field.
	 * @param combo The component which the number of data bits should be taken from.
	 */
	@SuppressWarnings("rawtypes")
	public void setDataBitsCombo (JComboBox combo);

	/**
	 * Sets the stop bits' combo box field.
	 * @param combo The component which the number of stop bits should be taken from.
	 */
	@SuppressWarnings("rawtypes")
	public void setStopBitsCombo (JComboBox combo);

	/**
	 * Sets the parity combo box field.
	 * @param combo The component which the parity mode should be taken from.
	 */
	@SuppressWarnings("rawtypes")
	public void setParityCombo (JComboBox combo);

	/**
	 * Sets the software flow control check box field.
	 * @param checkbox The component that tells if software flow
	 *	control should be enabled (yes, when selected).
	 */
	public void setFlowSoftCheckbox (JCheckBox checkbox);

	/**
	 * Sets the hardware flow control check box field.
	 * @param checkbox The component that tells if hardware flow
	 *	control should be enabled (yes, when selected).
	 */
	public void setFlowHardCheckbox (JCheckBox checkbox);

	/**
	 * Sets the data transmission synchronization variable.
	 * @param synch The data transmission synchronization variable.
	 */
	public void setSync (Object synch);

	/**
	 * Sets the main progress bar component.
	 * @param progressBar The main progress bar component.
	 */
	public void setProgressBar (JProgressBar progressBar);

	/**
	 * Sets the status label component.
	 * @param status The status label component.
	 */
	public void setStatusLabel (JLabel status);

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
