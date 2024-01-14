/*
 * FakeDevice.java, part of the JYMAG package.
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
package BogDroSoft.jymag.comm.fake;

import BogDroSoft.jymag.Utils;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.TooManyListenersException;

/**
 * FakeDevice - a phone emulator for tests.
 * @author Bogdan Drozdowski
 */
class FakeDevice
{
	// output from application, input to device:
	private byte[] currInput;
	private int currInputPosition;
	// input to application, output from device:
	private byte[] currOutput;
	private int currOutputPosition;
	
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
	private SerialPortEventListener l;
	private final SerialPort port;
	private int volume = 1; // must be static to pass between instances
	private boolean inFileSending = false; // must be static to pass between instances
	private static final boolean DEBUG = false;

	/** The default encoding for commands. */
	private static final String DEFAULT_ENCODING = "US-ASCII";

	private static final String OK_STRING = "OK";					// NOI18N
	private static final String ERROR_STRING = "ERROR";				// NOI18N
	private static final String NOCAR_STRING = "NO CARRIER";			// NOI18N
	private static final String CONN_STRING = "CONNECT";				// NOI18N
	private static final String TRANSFER_RESET_BYTES = "AT+KDOBJ=1,0\r";		// NOI18N
	private static final String TRANSFER_INIT_CMD = "AT+KDOBJ=1,1\r";		// NOI18N
	private static final String TRANSFER_FILE_LEN_CMD = "AT+KDOBJ=2,1,3,0,";	// NOI18N
	private static final String TRANSFER_FILE_TYPE_CMD = "AT+KDOBJ=2,1,0,";		// NOI18N
	private static final String TRANSFER_FILE_END_CMD = "AT+KDOBJ=1,0\r";		// NOI18N
	private static final String FILE_RETR_CMD_START = "AT+KPSR=\"";			// NOI18N
	private static final String CHARSET_CMD = "AT+CSCS=\"8859-1\"\r";		// NOI18N
	private static final String LIST_CMD_START = "AT+KPSL=\"";			// NOI18N
	private static final String DEL_CMD_START = "AT+KPSD=\"";			// NOI18N
	private static final String AT_CMD = "AT\r";					// NOI18N
	private static final String ATZ_CMD = "ATZ\r";					// NOI18N
	private static final String VERSION_CMD = "AT+KPSV\r";				// NOI18N
	private static final String IMEI_CMD = "ATIMEI\r";				// NOI18N
	private static final String IMEI_REPLY = "ATIMEI";				// NOI18N
	private static final String TYPE_CMD = "AT+CGMR\r";				// NOI18N
	private static final String SERIALNUM_CMD = "AT+CGSN\r";			// NOI18N
	private static final String SERIALNUM_REPLY = "CGSN";				// NOI18N
	private static final String SUBSCR_NUM_CMD = "AT+CNUM\r";			// NOI18N
	private static final String CAPABILITY_CMD = "AT+KPSCAP=\"";			// NOI18N
 	private static final String POWEROFF_CMD = "AT*PSCPOF\r";			// NOI18N
	private static final String SEND_PIN_CMD_START = "AT+CPIN=";			// NOI18N
	private static final String ALARM_NUM_CMD = "AT+CALA=?\r";			// NOI18N
	private static final String ALARM_DEL_CMD = "AT+CALD=";				// NOI18N
	private static final String ALARM_ADD_CMD = "AT+CALA=";				// NOI18N
	private static final String ALARM_LIST_CMD = "AT+CALA?\r";			// NOI18N
	private static final String MSG_DEL_CMD = "AT+CMGD=";				// NOI18N
	private static final String MSG_LIST_CMD = "AT+CMGL\r";				// NOI18N
	private static final String MSG_GET_CMD = "AT+CMGR=";				// NOI18N
	private static final String MSG_SEND_CMD = "AT+CMGS=";				// NOI18N
	private static final String SIGNAL_POWER_CMD = "AT+CSQ\r";			// NOI18N
	private static final String MSG_TEXT_MODE_CMD = "AT+CMGF=1\r";			// NOI18N
	private static final String MSG_STORAGE_CMD = "AT+CPMS=";			// NOI18N
	private static final String MSG_PROMPT = ">";					// NOI18N
	private static final String DIAL_CMD_AUTO = "ATD";				// NOI18N
	private static final String HANGUP_CMD = "ATH\r";				// NOI18N
	private static final String ANSWER_CALL_CMD = "ATA\r";				// NOI18N
	private static final String CURRENT_VOLUME_CMD = "AT+CLVL?\r";			// NOI18N
	private static final String SET_VOLUME_CMD = "AT+CLVL=";			// NOI18N

	FakeDevice(SerialPort sp)
	{
		port = sp;
	}

	private void checkForCommandInBuffer()
	{
		if (DEBUG)
		{
			System.out.println("BogDroSoft.jymag.comm.fake.FakeDevice.checkForCommandInBuffer(): "
				+ new String(currInput, 0, currInputPosition));
		}
		if (currInputPosition <= 0 || currInput == null)
		{
			return;
		}
		if (currInput[currInputPosition-1] != '\r' && ! inFileSending)
		{
			return;
		}
		try
		{
			String buffer = new String(currInput, 0, currInputPosition);
			if (AT_CMD.equals(buffer)
				|| ATZ_CMD.equals(buffer)
				|| CHARSET_CMD.equals(buffer)
				|| POWEROFF_CMD.equals(buffer)
				|| MSG_TEXT_MODE_CMD.equals(buffer)
				|| HANGUP_CMD.equals(buffer)
				|| TRANSFER_RESET_BYTES.equals(buffer)
				|| TRANSFER_INIT_CMD.equals(buffer)
				|| buffer.startsWith(DEL_CMD_START)
				|| buffer.startsWith(SEND_PIN_CMD_START)
				|| buffer.startsWith(ALARM_DEL_CMD)
				|| buffer.startsWith(ALARM_ADD_CMD)
				|| buffer.startsWith(MSG_DEL_CMD)
				|| buffer.startsWith(MSG_STORAGE_CMD)
				)
			{
				sendReply(OK_STRING);
			}
			else if (ANSWER_CALL_CMD.equals(buffer))
			{
				sendReply(OK_STRING);
				if (l != null)
				{
					if (isNotifyOnRingIndicator)
					{
						l.serialEvent(new SerialPortEvent(
							port, SerialPortEvent.RI, true, false));
					}
				}
			}
			else if (VERSION_CMD.equals(buffer))
			{
				sendReply("+KPSV: 2.04\r" + OK_STRING);
			}
			else if (SERIALNUM_CMD.equals(buffer))
			{
				sendReply("AT+" + SERIALNUM_REPLY
					+ "\r353512345678901\r" + OK_STRING);
			}
			else if (TYPE_CMD.equals(buffer))
			{
				sendReply("+CGMR: FakeSAGEM KB3,ME\r" + OK_STRING);
			}
			else if (IMEI_CMD.equals(buffer))
			{
				sendReply(IMEI_REPLY + "\rmyX5-2 GPRS\r" + OK_STRING);
			}
			else if (SUBSCR_NUM_CMD.equals(buffer))
			{
				sendReply("+CNUM: \"\",\"+123456789\",145\r" + OK_STRING);
			}
			else if (CURRENT_VOLUME_CMD.equals(buffer))
			{
				sendReply("+CLVL:" + volume + "\r\n" + OK_STRING);
			}
			else if (buffer.startsWith(SET_VOLUME_CMD))
			{
				try
				{
					volume = Integer.parseInt(
						buffer.substring(
							SET_VOLUME_CMD.length(),
							currInputPosition-1));
					if (volume >= 0)
					{
						sendReply(OK_STRING);
					}
					else
					{
						sendReply(ERROR_STRING);
					}
				}
				catch (NumberFormatException numberFormatException)
				{
					sendReply(ERROR_STRING);
				}
			}
			else if (buffer.startsWith(DIAL_CMD_AUTO))
			{
				sendReply(NOCAR_STRING);
			}
			else if (SIGNAL_POWER_CMD.equals(buffer))
			{
				sendReply("+CSQ:"
					+ Math.round(Math.floor(Math.random() * 32))
					+ ","
					+ Math.round(Math.floor(Math.random() * 32))
					+ "\r\n" + OK_STRING);
			}
			else if (buffer.startsWith(LIST_CMD_START))
			{
				//+KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
				String ofWhat = buffer.substring(LIST_CMD_START.length(), buffer.lastIndexOf('"'));
				if ("PICTURES".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFF\",0,1,\"PICTURES\",\"FGIF\",\"0000065535\",\"\",\"TestPicture\"\r\n"
						+ OK_STRING);
				}
				else if ("RINGTONES".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFE\",0,1,\"RINGTONES\",\"MID\",\"0000065535\",\"\",\"TestRingtone\"\r"
						+ OK_STRING);
				}
				else if ("VCARDS".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFD\",0,1,\"VCARDS\",\"VCARD\",\"0000065535\",\"\",\"TestVcard\"\r"
						+ OK_STRING);
				}
				else if ("VEVENT".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFC\",0,1,\"VEVENT\",\"VCF\",\"0000065535\",\"\",\"TestVEvent\"\r"
						+ OK_STRING);
				}
				else if ("ANIMATIONS".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFB\",0,1,\"ANIMATIONS\",\"AVI\",\"0000065535\",\"\",\"TestMovie\"\r"
						+ OK_STRING);
				}
				else if ("VTODO".equals(ofWhat))
				{
					sendReply("+KPSL: \"5303650005022001FFFA\",0,1,\"VTODO\",\"VCF\",\"0000065535\",\"\",\"TestVTodo\"\r"
						+ OK_STRING);
				}
			}
			else if (ALARM_LIST_CMD.equals(buffer))
			{
				sendReply("+CALA: \"08/08/02,06:30:00\"\r"
					+ OK_STRING);
			}
			else if (ALARM_NUM_CMD.equals(buffer))
			{
				sendReply("+CALA: (1),(sound)\r"
					+ OK_STRING);
			}
			else if (MSG_LIST_CMD.equals(buffer))
			{
				sendReply("+CMGL:1,\"STOR\",\"+001123456789\",\"08/08/02,06:30:00+00\",,1\r\nTest\r\n"
					+ OK_STRING);
			}
			else if (buffer.startsWith(MSG_GET_CMD))
			{
				sendReply("+CMGL:1,\"STOR\",\"+001123456789\",\"08/08/02,06:30:00+00\",,1\r\nTest\r\n"
					+ OK_STRING);
			}
			else if (buffer.startsWith(MSG_SEND_CMD))
			{
				sendReply(MSG_PROMPT + OK_STRING);
			}
			else if (buffer.startsWith(TRANSFER_FILE_LEN_CMD))
			{
				sendReply(CONN_STRING);
				inFileSending = true;
				if (l != null)
				{
					if (isNotifyOnCTS)
					{
						l.serialEvent(new SerialPortEvent(
							port, SerialPortEvent.CTS, false, true));
					}
					if (isNotifyOnCarrierDetect)
					{
						l.serialEvent(new SerialPortEvent(
							port, SerialPortEvent.CD, false, true));
					}
				}
			}
			else if (buffer.startsWith(TRANSFER_FILE_TYPE_CMD))
			{
				sendReply(CONN_STRING);
			}
			else if (buffer.startsWith(TRANSFER_FILE_END_CMD))
			{
				sendReply(OK_STRING);
				inFileSending = false;
			}
			else if (buffer.startsWith(FILE_RETR_CMD_START))
			{
				sendReply("AT+KPSR=\"ID1\"\r+KPSR: 1\rCONNECT\r\nA\r\n" + NOCAR_STRING);
			}
			else if (buffer.startsWith(CAPABILITY_CMD))
			{
				sendReply(CONN_STRING
					+ "+CAPA\r\n"
					+ NOCAR_STRING);
			}
			else if (inFileSending)
			{
				sendReply(OK_STRING);
			}
			else
			{
				sendReply(ERROR_STRING);
			}
		} 
		catch (Exception ex)
		{
			sendReply(ERROR_STRING);
		}
		System.arraycopy(currInput, currInputPosition, currInput, 0, currInput.length - currInputPosition);
		Arrays.fill(currInput, currInput.length - currInputPosition, currInput.length, (byte)0);
		currInputPosition = 0;
	}
	
	private void sendReply(String reply)
	{
		if (DEBUG)
		{
			System.out.println("BogDroSoft.jymag.comm.fake.FakeDevice.sendReply(): "
				+ reply);
		}
		try
		{
			currOutput = (reply + Utils.CR).getBytes(DEFAULT_ENCODING);
			currOutputPosition = 0;
		}
		catch (UnsupportedEncodingException ex)
		{
		}
		if (l != null)
		{
			if (isNotifyOnDataAvailable)
			{
				l.serialEvent(new SerialPortEvent(
					port, SerialPortEvent.DATA_AVAILABLE, false, true));
			}
			if (isNotifyOnDSR)
			{
				l.serialEvent(new SerialPortEvent(
					port, SerialPortEvent.DSR, false, true));
			}
		}
	}
	// --------------------------

	void addEventListener( SerialPortEventListener lsnr )
		throws TooManyListenersException
	{
		l = lsnr;
	}

	void removeEventListener()
	{
		l = null;
	}

	void notifyOnDataAvailable( boolean enable )
	{
		isNotifyOnDataAvailable = enable;
	}

	void notifyOnOutputEmpty( boolean enable )
	{
		isNotifyOnOutputEmpty = enable;
	}

	void notifyOnCTS( boolean enable )
	{
		isNotifyOnCTS = enable;
	}

	void notifyOnDSR( boolean enable )
	{
		isNotifyOnDSR = enable;
	}

	void notifyOnRingIndicator( boolean enable )
	{
		isNotifyOnRingIndicator = enable;
	}

	void notifyOnCarrierDetect( boolean enable )
	{
		isNotifyOnCarrierDetect = enable;
	}

	void notifyOnOverrunError( boolean enable )
	{
		isNotifyOnOverrunError = enable;
	}

	void notifyOnParityError( boolean enable )
	{
		isNotifyOnParityError = enable;
	}

	void notifyOnFramingError( boolean enable )
	{
		isNotifyOnFramingError = enable;
	}

	void notifyOnBreakInterrupt( boolean enable )
	{
		isNotifyOnBreakInterrupt = enable;
	}

	InputStream getInputStream()
	{
		return new FakeInput();
	}

	OutputStream getOutputStream()
	{
		return new FakeOutput();
	}

	// input to application, output from device
	private class FakeInput extends InputStream
	{
		private int markPos = -1;

		FakeInput()
		{
			currOutputPosition = 0;
		}

		@Override
		public int read() throws IOException
		{
			if (currOutput != null && currOutputPosition >= 0
				&& currOutputPosition < currOutput.length)
			{
				return currOutput[currOutputPosition++];
			}
			return signalEnd();
		}

		@Override
		public int read(byte[] b) throws IOException
		{
			return read(b, 0, b.length);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException
		{
			if (currOutput != null && currOutputPosition >= 0
				&& currOutputPosition < currOutput.length)
			{
				int maxLen = Math.min(currOutput.length - currOutputPosition, len);
				System.arraycopy(currOutput, currOutputPosition, b, off, maxLen);
				currOutputPosition += maxLen;
				return maxLen;
			}
			return signalEnd();
		}

		@Override
		public long skip(long n) throws IOException
		{
			if (currOutput != null)
			{
				currOutputPosition += n;
			}
			return signalEnd();
		}

		@Override
		public int available() throws IOException
		{
			if (currOutput != null)
			{
				return currOutput.length - currOutputPosition;
			}
			return 0;
		}

		@Override
		public void close() throws IOException
		{
			currOutput = null;
			currOutputPosition = -1;
			if (l != null)
			{
				if (isNotifyOnOutputEmpty)
				{
					l.serialEvent(new SerialPortEvent(
						port, SerialPortEvent.OUTPUT_BUFFER_EMPTY, false, true));
				}
			}
		}

		@Override
		public void mark(int readlimit)
		{
			markPos = readlimit;
		}

		@Override
		public void reset() throws IOException
		{
			currOutputPosition = markPos;
		}

		@Override
		public boolean markSupported()
		{
			return true;
		}
		
		private int signalEnd() throws IOException
		{
			if (l != null)
			{
				if (isNotifyOnOverrunError)
				{
					l.serialEvent(new SerialPortEvent(
						port, SerialPortEvent.OE, false, true));
				}
				if (isNotifyOnParityError)
				{
					l.serialEvent(new SerialPortEvent(
						port, SerialPortEvent.PE, false, true));
				}
				if (isNotifyOnFramingError)
				{
					l.serialEvent(new SerialPortEvent(
						port, SerialPortEvent.FE, false, true));
				}
				if (isNotifyOnBreakInterrupt)
				{
					l.serialEvent(new SerialPortEvent(
						port, SerialPortEvent.BI, false, true));
				}
			}
			throw new IOException("FakeDevice.FakeInput: No output ready.");
		}
	}	

	// output from application, input to device
	private class FakeOutput extends OutputStream
	{
		FakeOutput()
		{
			currInputPosition = 0;
			if (currInput == null)
			{
				currInput = new byte[1024];
			}
		}

		@Override
		public void write(int b) throws IOException
		{
			if (currInput == null)
			{
				throw new IOException("FakeDevice.FakeInput: No output ready.");
			}
			if (currInputPosition >= currInput.length)
			{
				byte[] a = new byte[2 * currInput.length];
				System.arraycopy(currInput, 0, a, 0, currInput.length);
				currInput = a;
			}
			currInput[currInputPosition++] = (byte)b;
			checkForCommandInBuffer();
		}
		
		@Override
		public void write(byte[] b) throws IOException
		{
			write(b, 0, b.length);
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException
		{
			if (currInput == null)
			{
				throw new IOException("FakeDevice.FakeInput: No output ready.");
			}
			if (currInputPosition + len >= currInput.length)
			{
				byte[] a = new byte[currInput.length + len];
				System.arraycopy(currInput, 0, a, 0, currInput.length);
				currInput = a;
			}
			System.arraycopy(b, off, currInput, currInputPosition, len);
			currInputPosition += len;
			checkForCommandInBuffer();
		}
		
		@Override
		public void flush() throws IOException {}
		
		@Override
		public void close() throws IOException {}
	}
}
