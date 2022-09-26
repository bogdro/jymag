/*
 * SerialPortStub.java, part of the BogDroSoft.jymag.comm package.
 *
 * Copyright (C) 2014-2022 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
 * License: GNU General Public License, v3+
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package BogDroSoft.jymag.comm;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
	private int flowControl = SerialPort.FLOWCONTROL_NONE;
	private boolean dtr = true;
	private boolean rts = false;
	private SerialPortEventListener spl = null;
	private boolean eventNotifyOnDataAvailable = false;
	private int baudRate = 115200;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

        /** Creates a new instance of SerialPortStub, using defaults. */
        public SerialPortStub ()
        {
        }

	@Override
	public void setSerialPortParams (int speed, int dBits, int sBits, int par)
		throws UnsupportedCommOperationException
	{
		baudRate = speed;
		dataBits = dBits;
		stopBits = sBits;
		parity = par;
	}

	@Override
	public int getBaudRate ()
	{
		return baudRate;
	}

	@Override
	public int getDataBits ()
	{
		return dataBits;
	}

	@Override
	public int getStopBits ()
	{
		return stopBits;
	}

	@Override
	public int getParity ()
	{
		return parity;
	}

	@Override
	public void setFlowControlMode (int i) throws UnsupportedCommOperationException
	{
		flowControl = i;
	}

	@Override
	public int getFlowControlMode ()
	{
		return flowControl;
	}

	@Override
	public boolean isDTR ()
	{
		return dtr;
	}

	@Override
	public void setDTR (boolean bln)
	{
		dtr = bln;
	}

	@Override
	public void setRTS (boolean bln)
	{
		rts = bln;
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
		return rts;
	}

	@Override
	public void sendBreak (int i)
	{
	}

	@Override
	public void addEventListener (SerialPortEventListener sl) throws TooManyListenersException
	{
		spl = sl;
	}

	@Override
	public void removeEventListener ()
	{
		spl = null;
	}

	@Override
	public void notifyOnDataAvailable (boolean bln)
	{
		eventNotifyOnDataAvailable = bln;
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
		// TODO
		return new ByteArrayInputStream (new byte[0]);
	}

	@Override
	public OutputStream getOutputStream () throws IOException
	{
		// TODO
		return new ByteArrayOutputStream (0);
	}

}
