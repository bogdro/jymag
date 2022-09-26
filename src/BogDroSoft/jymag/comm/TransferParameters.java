/*
 * TransferParameters.java, part of the BogDroSoft.jymag.comm package.
 *
 * Copyright (C) 2016-2018 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag.comm;

import gnu.io.CommPortIdentifier;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 * A class container for transfer parameters.
 * @author Bogdan Drozdowski
 */
public class TransferParameters
{
	private final CommPortIdentifier id;
	private final int speed;
	private final int dataBits;
	private final float stopBits;
	private final int parity;
	private final int flow;
	private final Object sync;

	/**
	 * Creates a new instance of TransferParameters, using defaults.
	 * @param cpid Port ID.
	 * @param pSpeed Port transfer speed (bit rate).
	 * @param pDataBits Number of port data bits.
	 * @param pStopBits Number of port stop bits.
	 * @param pParity Port parity setting.
	 * @param pFlow Port data flow control setting.
	 * @param dSync Transmission synchronisation object.
	 */
        public TransferParameters (
		final CommPortIdentifier cpid,
		final int pSpeed,
		final int pDataBits,
		final float pStopBits,
		final int pParity,
		final int pFlow,
		final Object dSync)
        {
		id = cpid;
		speed = pSpeed;
		dataBits = pDataBits;
		stopBits = pStopBits;
		parity = pParity;
		flow = pFlow;
		sync = dSync;
        }

	/**
	 * Creates a new instance of TransferParameters, using defaults.
	 * @param cpid Port ID.
	 * @param pSpeed Port transfer speed (bit rate).
	 * @param pDataBits Number of port data bits.
	 * @param pStopBits Number of port stop bits.
	 * @param pParity Port parity setting.
	 * @param pFlow Port data flow control setting.
	 * @param dSync Transmission synchronisation object.
	 */
        public TransferParameters (
		final String cpid,
		final int pSpeed,
		final int pDataBits,
		final float pStopBits,
		final int pParity,
		final int pFlow,
		final Object dSync)
        {
		this (TransferUtils.getIdentifierForPort (cpid),
			pSpeed,
			pDataBits,
			pStopBits,
			pParity,
			pFlow,
			dSync);
        }

	/**
	 * Creates a new instance of TransferParameters, using defaults.
	 * @param cpid Port ID.
	 * @param pSpeed Port transfer speed (bit rate).
	 * @param pDataBits Number of port data bits.
	 * @param pStopBits Number of port stop bits.
	 * @param pParity Port parity setting.
	 * @param pFlow Port data flow control setting.
	 * @param dSync Transmission synchronisation object.
	 */
        public TransferParameters (
		final String cpid,
		final String pSpeed,
		final String pDataBits,
		final String pStopBits,
		final int pParity,
		final int pFlow,
		final Object dSync)
        {
		this (TransferUtils.getIdentifierForPort (cpid),
			Integer.parseInt (pSpeed),
			Integer.parseInt (pDataBits),
			Float.parseFloat (pStopBits),
			pParity,
			pFlow,
			dSync);
        }

	/**
	 * Creates a new instance of TransferParameters, using defaults.
	 * @param portCombo Port ID combo box.
	 * @param speedCombo Port transfer speed (bit rate) combo box.
	 * @param dataBitsCombo Number of port data bits' combo box.
	 * @param stopBitsCombo Number of port stop bits' combo box.
	 * @param parityCombo Port parity setting combo box.
	 * @param flowSoft Hardware port data flow control setting checkbox.
	 * @param flowHard Software port data flow control setting checkbox.
	 * @param dSync Transmission synchronisation object.
	 */
	@SuppressWarnings("rawtypes")
        public TransferParameters (
		final JComboBox portCombo,
		final JComboBox speedCombo,
		final JComboBox dataBitsCombo,
		final JComboBox stopBitsCombo,
		final JComboBox parityCombo,
		final JCheckBox flowSoft,
		final JCheckBox flowHard,
		final Object dSync)
        {
		Object selectedPort = portCombo.getSelectedItem ();
		if ( selectedPort != null )
		{
			id = TransferUtils.getIdentifierForPort (selectedPort.toString ());
		}
		else
		{
			// NOTE that the port can be NULL if there aren't any
			// ports detected at all. We need to manage that gracefully.
			id = null;
		}
		speed = Integer.parseInt (speedCombo.getSelectedItem ().toString ());
		dataBits = Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ());
		stopBits = Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ());
		parity = parityCombo.getSelectedIndex ();
		flow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);
		sync = dSync;
        }

	public int getDataBits ()
	{
		return dataBits;
	}

	public int getFlow ()
	{
		return flow;
	}

	public CommPortIdentifier getId ()
	{
		return id;
	}

	public int getParity ()
	{
		return parity;
	}

	public int getSpeed ()
	{
		return speed;
	}

	public float getStopBits ()
	{
		return stopBits;
	}

	public Object getSync ()
	{
		return sync;
	}
}
