/*
 * JYMAGTab.java, part of the JYMAG package.
 *
 * Copyright (C) 2012 Bogdan Drozdowski, bogdandr (at) op.pl
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

import javax.swing.JComboBox;
import javax.swing.JCheckBox;

/**
 * This class is the panel interface. All panels in all tabs
 * of the JYMAG should implement this.
 * @author Bogdan Drozdowski
 */
public interface JYMAGTab
{
	/**
	 * Sets the port list combo box field.
	 * @param combo The component which the port name should be taken from.
	 */
	public void setPortCombo (JComboBox combo);

	/**
	 * Sets the transmission speed combo box field.
	 * @param combo The component which the transmission speed should be taken from.
	 */
	public void setSpeedCombo (JComboBox combo);

	/**
	 * Sets the data bits' combo box field.
	 * @param combo The component which the number of data bits should be taken from.
	 */
	public void setDataBitsCombo (JComboBox combo);

	/**
	 * Sets the stop bits' combo box field.
	 * @param combo The component which the number of stop bits should be taken from.
	 */
	public void setStopBitsCombo (JComboBox combo);

	/**
	 * Sets the parity combo box field.
	 * @param combo The component which the parity mode should be taken from.
	 */
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
}
