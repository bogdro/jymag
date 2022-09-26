/*
 * SerialPortStub.java, part of the BogDroSoft.jymag.comm package.
 *
 * Copyright (C) 2014 Bogdan Drozdowski, bogdandr (at) op.pl
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

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * SerialPortStub - a stub stat simulates a SerialPort.
 * @author Bogdan Drozdowski
 */
public class SerialPortStub extends SerialPort
{

        /** Creates a new instance of SerialPortStub, using defaults. */
        public SerialPortStub ()
        {
        }

	@Override
	public void setSerialPortParams (int i, int i1, int i2, int i3) throws UnsupportedCommOperationException
	{
	}

	@Override
	public int getBaudRate ()
	{
		return 115200;
	}

	@Override
	public int getDataBits ()
	{
		return SerialPort.DATABITS_8;
	}

	@Override
	public int getStopBits ()
	{
		return SerialPort.STOPBITS_1;
	}

	@Override
	public int getParity ()
	{
		return SerialPort.PARITY_NONE;
	}

	@Override
	public void setFlowControlMode (int i) throws UnsupportedCommOperationException
	{
	}

	@Override
	public int getFlowControlMode ()
	{
		return SerialPort.FLOWCONTROL_NONE;
	}

	@Override
	public boolean isDTR ()
	{
		return true;
	}

	@Override
	public void setDTR (boolean bln)
	{
	}

	@Override
	public void setRTS (boolean bln)
	{
	}

	@Override
	public boolean isCTS ()
	{
		return false;
	}

	@Override
	public boolean isDSR ()
	{
		return false;
	}

	@Override
	public boolean isCD ()
	{
		return false;
	}

	@Override
	public boolean isRI ()
	{
		return false;
	}

	@Override
	public boolean isRTS ()
	{
		return false;
	}

	@Override
	public void sendBreak (int i)
	{
	}

	@Override
	public void addEventListener (SerialPortEventListener sl) throws TooManyListenersException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void removeEventListener ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnDataAvailable (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnOutputEmpty (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnCTS (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnDSR (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnRingIndicator (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnCarrierDetect (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnOverrunError (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnParityError (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnFramingError (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void notifyOnBreakInterrupt (boolean bln)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public byte getParityErrorChar () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setParityErrorChar (byte b) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public byte getEndOfInputChar () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setEndOfInputChar (byte b) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setUARTType (String string, boolean bln) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public String getUARTType () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setBaudBase (int i) throws UnsupportedCommOperationException, IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getBaudBase () throws UnsupportedCommOperationException, IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setDivisor (int i) throws UnsupportedCommOperationException, IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getDivisor () throws UnsupportedCommOperationException, IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setLowLatency () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean getLowLatency () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean setCallOutHangup (boolean bln) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean getCallOutHangup () throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void enableReceiveFraming (int i) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void disableReceiveFraming ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean isReceiveFramingEnabled ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getReceiveFramingByte ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void disableReceiveTimeout ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void enableReceiveTimeout (int i) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean isReceiveTimeoutEnabled ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getReceiveTimeout ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void enableReceiveThreshold (int i) throws UnsupportedCommOperationException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void disableReceiveThreshold ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getReceiveThreshold ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean isReceiveThresholdEnabled ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void setInputBufferSize (int i)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getInputBufferSize ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void setOutputBufferSize (int i)
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public int getOutputBufferSize ()
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public InputStream getInputStream () throws IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public OutputStream getOutputStream () throws IOException
	{
		throw new UnsupportedOperationException ("Not supported yet.");
	}

}
