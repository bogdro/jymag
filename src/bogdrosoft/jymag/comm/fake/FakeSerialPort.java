/*
 * FakeSerialPort.java, part of the JYMAG package.
 *
 * Copyright (C) 2022-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
package bogdrosoft.jymag.comm.fake;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * FakeSerialPort - a fake SerialPort implementation for tests.
 * @author Bogdan Drozdowski
 */
public class FakeSerialPort extends SerialPort
{
	private int baudRate;
	private int dataBits;
	private int stopBits;
	private int parity;
	private int flowControl;
	private boolean dtr;
	private boolean rts;
	private SerialPortEventListener lsn;
	private byte parityErrorChar;
	private byte endOfInputChar;
	private String UARTType;
	private int baudBase;
	private int divisor;
	private boolean lowLatency;
	private boolean callOutHangup;
	private boolean isNotifyOnDataAvailable;
	private boolean isNotifyOnOutputEmpty;
	private boolean isNotifyOnCTS;
	private boolean isNotifyOnDSR;
	private boolean isNotifyOnRingIndicator;
	private boolean isNotifyOnCarrierDetect;
	private boolean isNotifyOnOverrunError;
	private boolean isNotifyOnParityError;
	private boolean isNotifyOnFramingError;
	private boolean isNotifyOnBreakInterrupt;
	private int receiveFraming;
	private boolean receiveFramingEnabled;
	private int receiveTimeout;
	private boolean receiveTimeoutEnabled;
	private int receiveThreshold;
	private boolean receiveThresholdEnabled;
	private int inputBufferSize;
	private int outputBufferSize;
	
	private final FakeDevice dev;

	public FakeSerialPort()
	{
		dev = new FakeDevice(this);
	}

	@Override
	public void setSerialPortParams( int b, int d, int s, int p )
		throws UnsupportedCommOperationException
	{
		baudRate = b;
		dataBits = d;
		stopBits = s;
		parity = p;
	}

	@Override
	public int getBaudRate()
	{
		return baudRate;
	}

	@Override
	public int getDataBits()
	{
		return dataBits;
	}

	@Override
	public int getStopBits()
	{
		return stopBits;
	}

	@Override
	public int getParity()
	{
		return parity;
	}

	@Override
	public void setFlowControlMode( int flowcontrol )
		throws UnsupportedCommOperationException
	{
		flowControl = flowcontrol;
	}

	@Override
	public int getFlowControlMode()
	{
		return flowControl;
	}

	@Override
	public void setDTR( boolean state )
	{
		dtr = state;
	}

	@Override
	public boolean isDTR()
	{
		return dtr;
	}

	@Override
	public void setRTS( boolean state )
	{
		rts = state;
	}

	@Override
	public boolean isRTS()
	{
		return rts;
	}

	@Override
	public boolean isCTS()
	{
		return Math.random() < 0.5;
	}

	@Override
	public boolean isDSR()
	{
		return Math.random() < 0.5;
	}

	@Override
	public boolean isCD()
	{
		return Math.random() < 0.5;
	}

	@Override
	public boolean isRI()
	{
		return Math.random() < 0.5;
	}

	@Override
	public void sendBreak( int duration ) { /* do nothing */ }

	@Override
	public void addEventListener( SerialPortEventListener lsnr )
		throws TooManyListenersException
	{
		lsn = lsnr;
		dev.addEventListener(lsn);
	}

	@Override
	public void removeEventListener()
	{
		lsn = null;
		dev.removeEventListener();
	}

	@Override
	public boolean setParityErrorChar( byte b )
		throws UnsupportedCommOperationException
	{
		parityErrorChar = b;
		return true;
	}

	@Override
	public byte getParityErrorChar( )
		throws UnsupportedCommOperationException
	{
		return parityErrorChar;
	}

	@Override
	public boolean setEndOfInputChar( byte b )
		throws UnsupportedCommOperationException
	{
		endOfInputChar = b;
		return true;
	}

	@Override
	public byte getEndOfInputChar( )
		throws UnsupportedCommOperationException
	{
		return endOfInputChar;
	}

	@Override
	public boolean setUARTType(String type, boolean test)
		throws UnsupportedCommOperationException
	{
		UARTType = type;
		return true;
	}

	@Override
	public String getUARTType()
		throws UnsupportedCommOperationException
	{
		return UARTType;
	}

	@Override
	public boolean setBaudBase(int BaudBase)
		throws UnsupportedCommOperationException,
		IOException
	{
		baudBase = BaudBase;
		return true;
	}

	@Override
	public int getBaudBase()
		throws UnsupportedCommOperationException,
		IOException
	{
		return baudBase;
	}

	@Override
	public boolean setDivisor(int Divisor)
		throws UnsupportedCommOperationException,
		IOException
	{
		divisor = Divisor;
		return true;
	}

	@Override
	public int getDivisor()
		throws UnsupportedCommOperationException,
		IOException
	{
		return divisor;
	}

	@Override
	public boolean setLowLatency()
		throws UnsupportedCommOperationException
	{
		lowLatency = true;
		return true;
	}

	@Override
	public boolean getLowLatency()
		throws UnsupportedCommOperationException
	{
		return lowLatency;
	}

	@Override
	public boolean setCallOutHangup(boolean NoHup)
		throws UnsupportedCommOperationException
	{
		callOutHangup = NoHup;
		return true;
	}

	@Override
	public boolean getCallOutHangup()
		throws UnsupportedCommOperationException
	{
		return callOutHangup;
	}

	@Override
	public void notifyOnDataAvailable( boolean enable )
	{
		isNotifyOnDataAvailable = enable;
		dev.notifyOnDataAvailable(isNotifyOnDataAvailable);
	}

	@Override
	public void notifyOnOutputEmpty( boolean enable )
	{
		isNotifyOnOutputEmpty = enable;
		dev.notifyOnOutputEmpty(isNotifyOnOutputEmpty);
	}

	@Override
	public void notifyOnCTS( boolean enable )
	{
		isNotifyOnCTS = enable;
		dev.notifyOnCTS(isNotifyOnCTS);
	}

	@Override
	public void notifyOnDSR( boolean enable )
	{
		isNotifyOnDSR = enable;
		dev.notifyOnDSR(isNotifyOnDSR);
	}

	@Override
	public void notifyOnRingIndicator( boolean enable )
	{
		isNotifyOnRingIndicator = enable;
		dev.notifyOnRingIndicator(isNotifyOnRingIndicator);
	}

	@Override
	public void notifyOnCarrierDetect( boolean enable )
	{
		isNotifyOnCarrierDetect = enable;
		dev.notifyOnCarrierDetect(isNotifyOnCarrierDetect);
	}

	@Override
	public void notifyOnOverrunError( boolean enable )
	{
		isNotifyOnOverrunError = enable;
		dev.notifyOnOverrunError(isNotifyOnOverrunError);
	}

	@Override
	public void notifyOnParityError( boolean enable )
	{
		isNotifyOnParityError = enable;
		dev.notifyOnParityError(isNotifyOnParityError);
	}

	@Override
	public void notifyOnFramingError( boolean enable )
	{
		isNotifyOnFramingError = enable;
		dev.notifyOnFramingError(isNotifyOnFramingError);
	}

	@Override
	public void notifyOnBreakInterrupt( boolean enable )
	{
		isNotifyOnBreakInterrupt = enable;
		dev.notifyOnBreakInterrupt(isNotifyOnBreakInterrupt);
	}

	@Override
	public void enableReceiveFraming( int f ) 
		throws UnsupportedCommOperationException
	{
		receiveFraming = f;
		receiveFramingEnabled = true;
	}

	@Override
	public void disableReceiveFraming()
	{
		receiveFramingEnabled = false;
	}

	@Override
	public boolean isReceiveFramingEnabled()
	{
		return receiveFramingEnabled;
	}

	@Override
	public int getReceiveFramingByte()
	{
		return receiveFraming;
	}


	@Override
	public void disableReceiveTimeout()
	{
		receiveTimeoutEnabled = false;
	}

	@Override
	public void enableReceiveTimeout( int time )
		throws UnsupportedCommOperationException
	{
		receiveTimeout = time;
		receiveTimeoutEnabled = true;
	}

	@Override
	public boolean isReceiveTimeoutEnabled()
	{
		return receiveTimeoutEnabled;
	}

	@Override
	public int getReceiveTimeout()
	{
		return receiveTimeout;
	}


	@Override
	public void enableReceiveThreshold( int thresh )
		throws UnsupportedCommOperationException
	{
		receiveThreshold = thresh;
		receiveThresholdEnabled = true;
	}

	@Override
	public void disableReceiveThreshold()
	{
		receiveThresholdEnabled = false;
	}

	@Override
	public int getReceiveThreshold()
	{
		return receiveThreshold;
	}

	@Override
	public boolean isReceiveThresholdEnabled()
	{
		return receiveThresholdEnabled;
	}

	@Override
	public void setInputBufferSize( int size )
	{
		inputBufferSize = size;
	}

	@Override
	public int getInputBufferSize()
	{
		return inputBufferSize;
	}

	@Override
	public void setOutputBufferSize( int size )
	{
		outputBufferSize = size;
	}

	@Override
	public int getOutputBufferSize()
	{
		return outputBufferSize;
	}

	@Override
	public void close() { /* do nothing */ }

	@Override
	public InputStream getInputStream() throws IOException
	{
		return dev.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return dev.getOutputStream();
	}

	@Override
	public String getName()
	{ 
		return "COMfaked";
	}

	@Override
	public String toString()
	{ 
		return "COMfaked";
	}
}
