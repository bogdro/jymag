/*
 * DataTransporter.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.jymag.comm;

import bogdrosoft.jymag.PhoneAlarm;
import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to send and receive data.
 * @author Bogdan Drozdowski
 */
public class DataTransporter
{
	//private final CommPortIdentifier portID;
	private final Object portID;
	private SerialPort s;
	private InputStream inputStream;
	private final Object inputStreamLock = new Object ();
	private OutputStream outputStream;
	/** Maximum number of attempts to establish communication. */
	public static final int MAX_TRIALS = 3;
	/** DT_TIMEOUT betwenn up/download stages, in milliseconds. */
	private static final int DT_TIMEOUT = 250;

	/** The firmware version Pattern. */
	private static final Pattern VERSION_PATTERN
		= Pattern.compile ("\\s*\\+KPSV:\\s*(.+)");				// NOI18N
	/** The PIN status Pattern. */
	private static final Pattern PIN_PATTERN
		= Pattern.compile ("\\s*\\+CPIN:\\s*(.+)");				// NOI18N
	/** The alarm number Pattern. */
	private static final Pattern ALNUM_PATTERN
		= Pattern.compile ("\\s*\\+CALA:\\s*\\((.+)\\),\\((.+)\\)");		// NOI18N
	/** The signal power Pattern. */
	private static final Pattern SIGNAL_POWER_PATTERN
		= Pattern.compile ("\\s*\\+CSQ:\\s*(\\d+),(\\d+).*");			// NOI18N
	/** The volume level Pattern. */
	private static final Pattern VOLUME_LEVEL_PATTERN
		= Pattern.compile ("\\+CLVL\\s*:\\s*(.*)");				// NOI18N

	/** The default encoding for commands. */
	public static final String DEFAULT_ENCODING = "US-ASCII";

	/** A String with a quotation character and a Carriage Return. */
	public static final String DQUOT_CR = "\"\r";					// NOI18N
	/** A String with a comma followed by a space. */
	public static final String COMMA_SPACE_STR = ", ";				// NOI18N
	/** A String with a single SEMICOLON. */
	public static final String SEMICOLON = ";";					// NOI18N

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
	private static final String LIST_CMD_END = "\",1\r";				// NOI18N
	private static final String NEWLINE_REGEX = "[\\r\\n]{1,2}";			// NOI18N
	private static final String DEL_CMD_START = "AT+KPSD=\"";			// NOI18N
	private static final String PORT_OPEN_PROG_NAME = "JYMAG";			// NOI18N
	private static final String AT_CMD = "AT\r";					// NOI18N
	private static final String VERSION_CMD = "AT+KPSV\r";				// NOI18N
	private static final String IMEI_CMD = "ATIMEI\r";				// NOI18N
	private static final String IMEI_REPLY = "ATIMEI";				// NOI18N
	private static final String TYPE_CMD = "AT+CGMR\r";				// NOI18N
	private static final String TYPE_REPLY_REGEX = "\\+CGMR:\\s*";			// NOI18N
	private static final String SERIALNUM_CMD = "AT+CGSN\r";			// NOI18N
	private static final String SERIALNUM_REPLY = "CGSN";				// NOI18N
	private static final String SUBSCR_NUM_CMD = "AT+CNUM\r";			// NOI18N
	private static final String SUBSCR_NUM_REPLY_REGEX = "\\+CNUM: ";		// NOI18N
	private static final String CAPABILITY_CMD = "AT+KPSCAP=\"";			// NOI18N
 	private static final String POWEROFF_CMD = "AT*PSCPOF\r";			// NOI18N
	private static final String PIN_STATUS_CMD = "AT+CPIN?\r";			// NOI18N
	private static final String SEND_PIN_CMD_START = "AT+CPIN=";			// NOI18N
	private static final String ALARM_NUM_CMD = "AT+CALA=?\r";			// NOI18N
	private static final String ALARM_DEL_CMD = "AT+CALD=";				// NOI18N
	private static final String ALARM_ADD_CMD = "AT+CALA=";				// NOI18N
	private static final String ALARM_LIST_CMD = "AT+CALA?\r";			// NOI18N
	private static final String MSG_DEL_CMD = "AT+CMGD=";				// NOI18N
	private static final String MSG_LIST_CMD = "AT+CMGL\r";				// NOI18N
	private static final String LAST_OK_REGEX = "OK[\r\n]*$";			// NOI18N
	private static final String MSG_LIST_REPLY_REGEX = "\\+CMG[LR]\\s*:\\s*";	// NOI18N
	private static final String MSG_GET_CMD = "AT+CMGR=";				// NOI18N
	private static final String MSG_SEND_CMD = "AT+CMGS=";				// NOI18N
	private static final String SIGNAL_POWER_CMD = "AT+CSQ\r";			// NOI18N
	private static final String MSG_TEXT_MODE_CMD = "AT+CMGF=1\r";			// NOI18N
	private static final String MSG_STORAGE_CMD = "AT+CPMS=";			// NOI18N
	private static final String MSG_PROMPT = ">";					// NOI18N
	private static final String DIAL_CMD_AUTO = "ATD";				// NOI18N
	private static final String DIAL_CMD_TONE = "ATDT";				// NOI18N
	private static final String DIAL_CMD_PULSE = "ATDP";				// NOI18N
	private static final String HANGUP_CMD = "ATH\r";				// NOI18N
	private static final String ANSWER_CALL_CMD = "ATA\r";				// NOI18N
	private static final String CURRENT_VOLUME_CMD = "AT+CLVL?\r";			// NOI18N
	private static final String SET_VOLUME_CMD = "AT+CLVL=";			// NOI18N

	private final SPL spl = new SPL ();

	// file headers:
	// JPG/EXIF/MJPG
	private static final byte[] JPG = new byte[] {
		(byte) 0xff, (byte) 0xd8/*, (byte) 0xff*/
		};
	// MTh
	private static final byte[] MID = new byte[] {
		(byte) 0x4d, (byte) 0x54, (byte) 0x68
		};
	// AMR and AMR-WB (AWB) #!AMR = 23 21 41 4D  52
	private static final byte[] AMR = new byte[] {
		(byte) 0x23, (byte) 0x21, (byte) 0x41, (byte) 0x4D,
		(byte) 0x52
		};
	// BM  = 42 4D
	private static final byte[] BMP = new byte[] {
		(byte) 0x42, (byte) 0x4D
		};
	// GIF = 47 49 46
	private static final byte[] GIF = new byte[] {
		(byte) 0x47, (byte) 0x49, (byte) 0x46
		};
	// PNG
	private static final byte[] PNG = new byte[] {
		(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47
		};
	// WAV = RIFF = 52 49 46 46
	private static final byte[] WAV = new byte[] {
		(byte) 0x52, (byte) 0x49, (byte) 0x46, (byte) 0x46
		};
	//// "BEGIN:VCALENDAR"
	private static final byte[] VCAL = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x49,
		(byte) 0x4E, (byte) 0x3A, (byte) 0x56, (byte) 0x43,
		(byte) 0x41, (byte) 0x4C, (byte) 0x45, (byte) 0x4E,
		(byte) 0x44, (byte) 0x41, (byte) 0x52
		};
	//// "BEGIN:vCard"
	private static final byte[] VCRD = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x49,
		(byte) 0x4E, (byte) 0x3A, (byte) 0x76, (byte) 0x43,
		(byte) 0x61, (byte) 0x72, (byte) 0x64
		};
	// WBMP: 0, 0
	//private final byte[] WBMP = new byte[] { (byte) 0x00, (byte) 0x00 };
	// the 2-bytes-zero header dosen't work well. Instead, let's look for '$'
	// and skip 3 bytes after it.
	private static final byte[] WBMP = new byte[] { (byte) 0x24 };

	// MNG: 8A 4D 4E 47
	private static final byte[] MNG = new byte[] {
		(byte) 0x8A, (byte) 0x4D, (byte) 0x4E, (byte) 0x47
		};

	// AIFF: "FORM" or "AIFF"
	private static final byte[] AIFF1 = new byte[] {
		(byte) 0x46, (byte) 0x4F, (byte) 0x52, (byte) 0x4D
		};
	private static final byte[] AIFF2 = new byte[] {
		(byte) 0x41, (byte) 0x49, (byte) 0x46, (byte) 0x46
		};

	// IMY (IMELODY): "BEGIN:IMELODY"
	private static final byte[] IMY = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x49,
		(byte) 0x4E, (byte) 0x3A, (byte) 0x49, (byte) 0x4D,
		(byte) 0x45, (byte) 0x4C, (byte) 0x4F, (byte) 0x44,
		(byte) 0x59
		};

	// AAC: "\0 \0 \0 . ftyp"
	private static final byte[] MPEG = new byte[] {
		//(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x14,
		(byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70
		};

	// GZIP:
	private static final byte[] GZIP = new byte[] {
		(byte) 0x1f, (byte) 0x8b
		};

	// ZIP:
	private static final byte[] ZIP = new byte[] {
		(byte) 0x50, (byte) 0x4b
		};

	// MPEG-audio:
	private static final byte[] MPA1 = new byte[] {
		(byte) 0xFF, (byte) 0xFA
		};
	private static final byte[] MPA2 = new byte[] {
		(byte) 0xFF, (byte) 0xFB
		};
	private static final byte[] MPA3 = new byte[] {
		(byte) 0xFF, (byte) 0xFC
		};

	// WMV1/2:
	private static final byte[] WMV = new byte[] {
		(byte) 0x30, (byte) 0x26, (byte) 0xB2, (byte) 0x75
		/* after these four, there are: 8E 66 CF 11  A6 D9 00 AA  00 62 CE 6C */
		};

	// XML: "<?xml"
	private static final byte[] XML = new byte[] {
		(byte) 0x3C, (byte) 0x3F, (byte) 0x78, (byte) 0x6D, (byte) 0x6C
		};

	// DOCTYPE:
	private static final byte[] DOCTYPE = new byte[] {
		(byte) 0x3C, (byte) 0x21, (byte) 0x44, (byte) 0x4F,
		(byte) 0x43, (byte) 0x54, (byte) 0x59, (byte) 0x50,
		(byte) 0x45
		};

	// SVG:
	private static final byte[] SVG = new byte[] {
		(byte) 0x3C, (byte) 0x73, (byte) 0x76, (byte) 0x67
		};

	// WMF
	private static final byte[] WMF = new byte[] {
		(byte) 0xD7, (byte) 0xCD, (byte) 0xC6, (byte) 0x9A,
		(byte) 0x00, (byte) 0x00
		};

	// PS/EPS: %!PS
	private static final byte[] PS = new byte[] {
		(byte) 0x25, (byte) 0x21, (byte) 0x50, (byte) 0x53
		};

	// TIFF: "II"
	private static final byte[] TIFF = new byte[] {
		(byte) 0x49, (byte) 0x49
		};

	// midlets: "MIDlet"
	private static final byte[] MIDLET = new byte[] {
		(byte) 0x4D, (byte) 0x49, (byte) 0x44, (byte) 0x6C,
		(byte) 0x65, (byte) 0x74
		};

	// text CGM: "BEGMF"
	private static final byte[] CGMtext = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x4D, (byte) 0x46
		};

	// binary CGM: & 0xFFE0 = "0x0020" big-endian
	private static final byte[] CGMbin01 = new byte[] {
		(byte) 0x00, (byte) 0x29
		};

	private static final byte[] CGMbin02 = new byte[] {
		(byte) 0x00, (byte) 0x28
		};

	// character CGM: =="0x3020" big-endian
	private static final byte[] CGMchar = new byte[] {
		(byte) 0x30, (byte) 0x20
		};

	// PMB: "[BitmapInfo2]"
	private static final byte[] PMB = new byte[] {
		(byte) 0x5B, (byte) 0x42, (byte) 0x69, (byte) 0x74,
		(byte) 0x6D, (byte) 0x61, (byte) 0x70, (byte) 0x49,
		(byte) 0x6E, (byte) 0x66, (byte) 0x6F, (byte) 0x32,
		(byte) 0x5D
		};

	// 3GP has a header of 3 zeros, so let's catch it with "generic type" - WBMP
	// DIB is a bitmap with no header, so let's catch it with "generic type" - WBMP


	// "CONNECT\r\n"
	private static final byte[] START = new byte[] {
		(byte) 0x43, (byte) 0x4F, (byte) 0x4E, (byte) 0x4E,
		(byte) 0x45, (byte) 0x43, (byte) 0x54, (byte) 0x0D,
		(byte) 0x0A
		};

	// "\r\nNO CARRIER"
	private static final byte[] FINISH = new byte[] {
		(byte) 0x0D, (byte) 0x0A,
		(byte) 0x4E, (byte) 0x4F, (byte) 0x20, (byte) 0x43,
		(byte) 0x41, (byte) 0x52, (byte) 0x52, (byte) 0x49,
		(byte) 0x45, (byte) 0x52
		};

	/**
	 * Creates a new instance of DataTransporter.
	 * @param id Port identifier for the port which will be used to
	 * transfer data.
	 */
	public DataTransporter (Object /*CommPortIdentifier*/ id)
	{
		portID = id;
	}

	/**
	 * Opens the port for communications.
	 * @param speed Communication speen, in bauds (bits per second).
	 * @param dataBits Number of data bits (5, 6, 7 or 8).
	 * @param stopBits Number of stop bits (1, 1.5 or 2).
	 * @param parity Parity checking type (0 - none, 1 - even, 2 - odd,
	 *	3 - space, 4 - mark).
	 * @param flowControl Flow control type (0 - none, 1 - software XON/XOFF,
	 	2 - hardware CTS/RTS).
	 * @throws java.lang.Exception in case port opening or registering an
	 * event listener failed.
	 */
	public void open (int speed, int dataBits, float stopBits, int parity,
		int flowControl) throws Exception
	{
		if ( portID == null )
		{
			throw new IllegalArgumentException ("DataTransporter.open: portID == null");	// NOI18N
		}
		String portName = getPortName();
		if ( ! portName.startsWith ("COM") )	// NOI18N
		{
			File portFile = new File (portName);
			if ( ! portFile.exists () )
			{
				throw new IOException ("DataTransporter.open: ! portFile.exists: "	// NOI18N
					+ portName);
			}
		}
		reopenRealOrFake();
		synchronized (inputStreamLock)
		{
			inputStream  = s.getInputStream ();
		}
		outputStream = s.getOutputStream ();

		int dBits = SerialPort.DATABITS_8;
		if ( dataBits == 7 )
		{
			dBits = SerialPort.DATABITS_7;
		}
		else if ( dataBits == 6 )
		{
			dBits = SerialPort.DATABITS_6;
		}
		else if ( dataBits == 5 )
		{
			dBits = SerialPort.DATABITS_5;
		}

		int sBits = SerialPort.STOPBITS_1;
		if ( Math.abs (stopBits - 1.5f) < 0.0001 )
		{
			sBits = SerialPort.STOPBITS_1_5;
		}
		else if ( Math.abs (stopBits - 2.0f) < 0.0001 )
		{
			sBits = SerialPort.STOPBITS_2;
		}

		int par = SerialPort.PARITY_NONE;
		// Combo: none, even, odd, space, mark
		if ( parity == 1 )
		{
			par = SerialPort.PARITY_EVEN;
		}
		else if ( parity == 2 )
		{
			par = SerialPort.PARITY_ODD;
		}
		else if ( parity == 3 )
		{
			par = SerialPort.PARITY_SPACE;
		}
		else if ( parity == 4 )
		{
			par = SerialPort.PARITY_MARK;
		}

		int flow = SerialPort.FLOWCONTROL_NONE;
		// checkboxes: none, XONN/XOFF, RTS/CTS
		if ( flowControl == 1 )
		{
			flow = SerialPort.FLOWCONTROL_XONXOFF_IN
				+ SerialPort.FLOWCONTROL_XONXOFF_OUT;
		}
		else if ( flowControl == 2 )
		{
			flow = SerialPort.FLOWCONTROL_RTSCTS_IN
				+ SerialPort.FLOWCONTROL_RTSCTS_OUT;
		}
		else if ( flowControl == 3 )
		{
			flow = SerialPort.FLOWCONTROL_RTSCTS_IN
				+ SerialPort.FLOWCONTROL_RTSCTS_OUT
				+ SerialPort.FLOWCONTROL_XONXOFF_IN
				+ SerialPort.FLOWCONTROL_XONXOFF_OUT;
		}

		s.setSerialPortParams (speed, dBits, sBits, par);
		s.setFlowControlMode (flow);

		s.addEventListener (spl);
		s.notifyOnDataAvailable (true);
	}

	/**
	 * Receives data from the port (waits at most 5 seconds).
	 * @param extraTerminators Any extra elements that are a mark of transmission end.
	 * @return The received bytes.
	 */
	public byte[] recv (Object[] extraTerminators)
	{
		byte[] res = new byte[0];
		synchronized (inputStreamLock)
		{
			if ( inputStream == null )
			{
				return res;
			}
		}
		int avail;
		while (true)
		{
			try
			{
				synchronized (inputStreamLock)
				{
					avail = inputStream.available ();
				}
				if ( avail == 0 )
				{
					synchronized (inputStreamLock)
					{
						//synchronized (inputStream)
						{
							inputStreamLock.wait (5*1000L);//inputStream.wait (5*1000);
						}
					}
					synchronized (inputStreamLock)
					{
						if ( inputStream == null )
						{
							return res;
						}
						avail = inputStream.available ();
					}
					if ( avail > 0 )
					{
						byte[] readBuffer = new byte[avail];
						int wasRead;
						synchronized (inputStreamLock)
						{
							if ( inputStream == null )
							{
								return res;
							}
							wasRead = inputStream.read (readBuffer);
						}
						if ( wasRead < 0 )
						{
							break;
						}
						// don't force any encodings, because the file data may
						// be invalid in any given encoding
						String curr = new String (readBuffer, 0, wasRead);
						res = Utils.joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
						if ( isTerminatorPresent ( curr.trim (), extraTerminators ) )
						{
							break;
						}
					}
					else
					{
						return res;
					}
				}
				else if ( avail > 0 )
				{
					byte[] readBuffer = new byte[avail];
					int wasRead;
					synchronized (inputStreamLock)
					{
						if ( inputStream == null )
						{
							return res;
						}
						wasRead = inputStream.read (readBuffer);
					}
					if ( wasRead < 0 )
					{
						break;
					}
					// don't force any encodings, because the file data may
					// be invalid in any given encoding
					String curr = new String (readBuffer, 0, wasRead);
					res = Utils.joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
					if ( isTerminatorPresent ( curr.trim (), extraTerminators ) )
					{
						break;
					}
				}
			}
			catch (InterruptedException intex)
			{
				try
				{
					// being interrupted not necessarily means that data is available, so check
					synchronized (inputStreamLock)
					{
						if ( inputStream == null )
						{
							return res;
						}
						avail = inputStream.available ();
					}
					if ( avail > 0 )
					{
						byte[] readBuffer = new byte[avail];
						int wasRead;
						synchronized (inputStreamLock)
						{
							if ( inputStream == null )
							{
								return res;
							}
							wasRead = inputStream.read (readBuffer);
						}
						if ( wasRead < 0 )
						{
							break;
						}
						// don't force any encodings, because the file data may
						// be invalid in any given encoding
						String curr = new String (readBuffer, 0, wasRead);
						res = Utils.joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
						if ( isTerminatorPresent ( curr.trim (), extraTerminators ) )
						{
							break;
						}
					}
				}
				catch (IOException ex)
				{
					// ignore
				}
			}
			catch (IOException ex)
			{
				Utils.handleException (ex, "DataTransporter.recv");	// NOI18N
				break;
			}
		}
		return res;
	}

	/**
	 * Checks if the given String contains anything from the array (converted to Strings).
	 * @param haystack The String to look in.
	 * @param needles The elements to search for.
	 * @return true, if contains.
	 */
	private boolean isTerminatorPresent (String haystack, Object[] needles)
	{
		if ( haystack == null )
		{
			return false;
		}
		String haystackTrimmed = haystack.trim ();
		if ( haystackTrimmed.startsWith (OK_STRING)
			|| haystackTrimmed.endsWith (OK_STRING)
			|| haystackTrimmed.startsWith (ERROR_STRING)
			|| haystackTrimmed.endsWith (ERROR_STRING)
			|| haystack.contains (NOCAR_STRING) )
		{
			return true;
		}
		if ( needles != null )
		{
			for ( int i=0; i < needles.length; i++ )
			{
				if ( haystack.contains (needles[i].toString ()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Sends data to the port.
	 * @param b The bytes to send.
	 * @throws java.io.IOException in case of an I/O problem with the port.
	 */
	public void send (byte[] b) throws IOException
	{
		send (b, 0, (b != null)? b.length : 0);
	}

	/**
	 * Sends data to the port.
	 * @param b The bytes to send.
	 * @param start The starting offset in the array.
	 * @param length The number bytes to send, starting from the start offset.
	 * @throws java.io.IOException in case of an I/O problem with the port.
	 */
	public void send (byte[] b, int start, int length) throws IOException
	{
		synchronized (inputStreamLock)
		{
			if ( b == null )
			{
				throw new IllegalArgumentException("b == null");
			}
			if ( outputStream == null )
			{
				throw new IllegalStateException("outputStream == null");
			}
			if ( start + length > b.length || start < 0
				|| length <= 0 )
			{
				return;
			}

			outputStream.write (b, start, length);
			outputStream.flush ();
		}
	}

	/**
	 * Closes the port.
	 */
	public void close ()
	{
		synchronized (inputStreamLock)
		{
			if ( s != null )
			{
				s.close ();
				inputStream = null;
				outputStream = null;
			}
		}
	}

	/**
	 * Sends the specified file to the phone.
	 * @param f The file to send.
	 * @param newName Name of the object to be sent to the phone.
	 * @return 0 in case of success.
	 */
	public int putFile (File f, String newName)
	{
		if ( f == null )
		{
			return -11;
		}
		String fName = f.getName ();
		// check if type is supported.
		if ( ! fName.contains (Utils.DOT))
		{
			return -8;
		}
		if ( ! Utils.getFiletypeIDs ().containsKey (fName.substring
			(fName.lastIndexOf (Utils.DOT)+1)
			.toLowerCase (Locale.ENGLISH)))
		{
			return -9;
		}
		if ( newName == null )
		{
			newName = fName.substring (0,
				fName.indexOf (Utils.DOT));
		}

		String rcvd;
		byte[] recvdB;
		// number of attempts.
		int trials = 0;
		FileInputStream fis = null;
		MAIN: while (trials <= MAX_TRIALS)
		{
			// stage variable, useful when an exception is caught.
			int stage = 0;
			try
			{
				// reset file upload
				send (TRANSFER_RESET_BYTES.getBytes (DEFAULT_ENCODING));
				recvdB = recv (null);
				rcvd = new String (recvdB, DEFAULT_ENCODING);

				if ( rcvd.isEmpty ()
					|| (! rcvd.contains (OK_STRING)
					&& ! rcvd.contains (ERROR_STRING)) )
				{
					reopen ();
					trials++;
					continue;
				}
			}
			catch ( IOException e )
			{
				Utils.handleException (e, "DataTransporter.putFile: init:"	// NOI18N
					+ f.getName () + ", newName=" + newName);	// NOI18N
				reopen ();
				trials++;
				continue;
			}
			try
			{
				//initiate transfer
				rcvd = tryCommand (TRANSFER_INIT_CMD, null);
				if ( ! rcvd.contains (OK_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -1;
					}
					continue;
				}

				stage++;

				// send filename length (the last parameter)
				rcvd = tryCommand (TRANSFER_FILE_LEN_CMD + newName.length () + Utils.CR,
					new String[] { CONN_STRING });
				if ( rcvd.contains (ERROR_STRING) || ! rcvd.contains (CONN_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -2;
					}
					continue;
				}

				stage++;

				// send the filename
				rcvd = tryCommand (newName + Utils.CR, null);
				if ( ! rcvd.contains (OK_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -3;
					}
					continue;
				}

				stage++;

				// send file type (4th param) and length (5th parameter)
				rcvd = tryCommand (TRANSFER_FILE_TYPE_CMD
						+ Utils.getFiletypeIDs ().get
							(f.getName ().substring
								(f.getName ().lastIndexOf (Utils.DOT)+1)
								.toLowerCase (Locale.ENGLISH)
							)
						+ Utils.COMMA + f.length () + Utils.CR,
						new String[] { CONN_STRING });
				if ( rcvd.contains (ERROR_STRING) || ! rcvd.contains (CONN_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -4;
					}
					continue;
				}

				Utils.sleepIgnoreException(DT_TIMEOUT);
				// send file data here:
				fis = new FileInputStream (f);
				byte[] contents = new byte[10240];
				int read;
				stage++;
				do
				{
					read = fis.read (contents);
					if ( read < 0 )
					{
						break;
					}
					send (Arrays.copyOf (contents, read));

				} while (read == contents.length);
				fis.close ();
				do
				{
					recvdB = recv (null);
					rcvd = new String (recvdB, DEFAULT_ENCODING);

					if ( rcvd.trim ().isEmpty () )
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS )
						{
							break;
						}
						continue MAIN;
					}
				} while (rcvd.trim ().isEmpty () && trials <= MAX_TRIALS);
				if ( trials > MAX_TRIALS )
				{
					return -5;
				}
				if ( ! rcvd.contains (OK_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -5;
					}
					continue;
				}

				stage++;

				// close file upload
				rcvd = tryCommand (TRANSFER_FILE_END_CMD, null);
				if ( ! rcvd.contains (OK_STRING) )
				{
					reopen ();
					trials++;
					if ( trials > MAX_TRIALS )
					{
						return -6;
					}
					continue;
				}
				return 0;
			}
			catch ( IOException ex )
			{
				/*
				if ( stage > 0 )
				{
					try
					{
						// close file upload
						send (transferFileEndCmd.getBytes ());
						recv (null);
					}
					catch ( Exception e ) {}
				}*/
				Utils.handleException (ex, "DataTransporter.putFile:end"	// NOI18N
					+ f.getName () + ", newName=" + newName);	// NOI18N
				if (fis != null)
				{
					try
					{
						fis.close();
					}
					catch (IOException t2)
					{
						Utils.handleException(t2,
							"DataTransporter.putFile->close->exception");
					}
				}
				reopen ();
				trials++;
				if ( trials > MAX_TRIALS )
				{
					return -7;
				}
			}
		} // MAIN while // MAIN while // MAIN while // MAIN while
		return -10;	// number of trials exceeded
	}

	/**
	 * Finds needle in haystack.
	 * @param haystack The array to search through.
	 * @param needle The array to search for.
	 * @return The position from which needle starts in haystack or -1 if not found.
	 */
	private int findBytes (byte[] haystack, byte[] needle)
	{
		if ( haystack != null )
		{
			if ( needle == null )
			{
				return 0;
			}
			if ( needle.length > haystack.length )
			{
				return -1;
			}
			HAY: for ( int i=0; i < haystack.length - needle.length; i++ )
			{
				for ( int j=0; j < needle.length; j++ )
				{
					if ( haystack[i+j] != needle[j] )
					{
						continue HAY;
					}
				}
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the specified object and saves it to a file.
	 * @param f The file for the object.
	 * @param el The element to retrieve.
	 * @return 0 in case of success.
	 */
	public int getFile (File f, PhoneElement el)
	{
		if ( f == null || el == null )
		{
			return -3;
		}
		FileOutputStream fos = null;
		try
		{
			String rcvd;
			byte[] recvdB;
			int trials = 0;
			do
			{
				Utils.sleepIgnoreException(DT_TIMEOUT);
				// send file retrieve command
				send ((FILE_RETR_CMD_START + el.getID () + DQUOT_CR).getBytes (DEFAULT_ENCODING));
				/*
				 * Receiving format:
				 * AT+KPSR="<ID repeated>"
				 * +KPSR: 49203
				 * CONNECT\r\n
				 * <binary data>
				 * \r\nNO CARRIER
				 */
				// receive file data
				recvdB = recv (null);
				// don't force any encodings, because the file data may
				// be invalid in any given encoding
				rcvd = new String (recvdB);

				if ( rcvd.trim ().isEmpty () || recvdB.length == 0 )
				{
					reopen ();
					trials++;
				}
			} while ( (rcvd.trim ().isEmpty () || recvdB.length == 0)
				&& trials < MAX_TRIALS);
			if ( rcvd.trim ().isEmpty () || recvdB.length == 0
				|| ! rcvd.contains (NOCAR_STRING) )
			{
				return -2;
			}

			// check file type
			fos = new FileOutputStream (f);
			if ( findBytes (recvdB, JPG) >= 0 )
			{
				// check if single or double match
				int pos = findBytes (recvdB, JPG);
				int pos2 = findBytes (Arrays.copyOfRange (recvdB, pos+1, recvdB.length),
					JPG);
				if ( pos2 >= 0 && ((recvdB[pos+1+pos2+2] & 0xff) == 0xff ||
					recvdB[pos+1+pos2+2] == -1) )
				{
					fos.write (recvdB, pos+1+pos2,
						findBytes (recvdB, FINISH) - pos2-pos-1 );
				}
				else
				{
					fos.write (recvdB, pos, findBytes (recvdB, FINISH) - pos );
				}
			}
			else if ( findBytes (recvdB, MID) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MID),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MID) );
			}
			else if ( findBytes (recvdB, AMR) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, AMR),
					findBytes (recvdB, FINISH) - findBytes (recvdB, AMR) );
			}
			else if ( findBytes (recvdB, WAV) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, WAV),
					findBytes (recvdB, FINISH) - findBytes (recvdB, WAV) );
			}
			else if ( findBytes (recvdB, GIF) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, GIF),
					findBytes (recvdB, FINISH) - findBytes (recvdB, GIF) );
			}
			else if ( findBytes (recvdB, PNG) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, PNG),
					findBytes (recvdB, FINISH) - findBytes (recvdB, PNG) );
			}
			else if ( findBytes (recvdB, VCAL) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, VCAL),
					findBytes (recvdB, FINISH) - findBytes (recvdB, VCAL) );
			}
			else if ( findBytes (recvdB, VCRD) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, VCRD),
					findBytes (recvdB, FINISH) - findBytes (recvdB, VCRD) );
			}
			else if ( findBytes (recvdB, MNG) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MNG),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MNG) );
			}
			else if ( findBytes (recvdB, AIFF1) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, AIFF1),
					findBytes (recvdB, FINISH) - findBytes (recvdB, AIFF1) );
			}
			else if ( findBytes (recvdB, AIFF2) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, AIFF2),
					findBytes (recvdB, FINISH) - findBytes (recvdB, AIFF2) );
			}
			else if ( findBytes (recvdB, IMY) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, IMY),
					findBytes (recvdB, FINISH) - findBytes (recvdB, IMY) );
			}
			else if ( findBytes (recvdB, MPEG) >= 0 )
			{
				// start with 4 bytes before "ftyp"
				fos.write (recvdB, findBytes (recvdB, MPEG) -4,
					findBytes (recvdB, FINISH) - (findBytes (recvdB, MPEG)-4) );
			}
			else if ( findBytes (recvdB, GZIP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, GZIP),
					findBytes (recvdB, FINISH) - findBytes (recvdB, GZIP) );
			}
			else if ( findBytes (recvdB, ZIP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, ZIP),
					findBytes (recvdB, FINISH) - findBytes (recvdB, ZIP) );
			}
			else if ( findBytes (recvdB, MPA1) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MPA1),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MPA1) );
			}
			else if ( findBytes (recvdB, MPA2) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MPA2),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MPA2) );
			}
			else if ( findBytes (recvdB, MPA3) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MPA3),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MPA3) );
			}
			else if ( findBytes (recvdB, WMV) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, WMV),
					findBytes (recvdB, FINISH) - findBytes (recvdB, WMV) );
			}
			else if ( findBytes (recvdB, XML) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, XML),
					findBytes (recvdB, FINISH) - findBytes (recvdB, XML) );
			}
			else if ( findBytes (recvdB, DOCTYPE) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, DOCTYPE),
					findBytes (recvdB, FINISH) - findBytes (recvdB, DOCTYPE) );
			}
			else if ( findBytes (recvdB, SVG) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, SVG),
					findBytes (recvdB, FINISH) - findBytes (recvdB, SVG) );
			}
			else if ( findBytes (recvdB, WMF) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, WMF),
					findBytes (recvdB, FINISH) - findBytes (recvdB, WMF) );
			}
			else if ( findBytes (recvdB, PS) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, PS),
					findBytes (recvdB, FINISH) - findBytes (recvdB, PS) );
			}
			else if ( findBytes (recvdB, TIFF) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, TIFF),
					findBytes (recvdB, FINISH) - findBytes (recvdB, TIFF) );
			}
			else if ( findBytes (recvdB, MIDLET) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MIDLET),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MIDLET) );
			}
			else if ( findBytes (recvdB, CGMtext) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, CGMtext),
					findBytes (recvdB, FINISH) - findBytes (recvdB, CGMtext) );
			}
			else if ( findBytes (recvdB, CGMchar) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, CGMchar),
					findBytes (recvdB, FINISH) - findBytes (recvdB, CGMchar) );
			}
			else if ( findBytes (recvdB, CGMbin01) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, CGMbin01),
					findBytes (recvdB, FINISH) - findBytes (recvdB, CGMbin01) );
			}
			else if ( findBytes (recvdB, CGMbin02) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, CGMbin02),
					findBytes (recvdB, FINISH) - findBytes (recvdB, CGMbin02) );
			}
			else if ( findBytes (recvdB, PMB) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, PMB),
					findBytes (recvdB, FINISH) - findBytes (recvdB, PMB) );
			}
			// BMP after CGM, which can start with "BM"?
			else if ( findBytes (recvdB, BMP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, BMP),
					findBytes (recvdB, FINISH) - findBytes (recvdB, BMP) );
			}
			/* always keep WBMP last, as this is the new generic case */
			else if ( findBytes (recvdB, WBMP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, WBMP) + 4,
					findBytes (recvdB, FINISH) - (findBytes (recvdB, WBMP)+4) );
			}
			else if ( findBytes (recvdB, START) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, START) + START.length,
					findBytes (recvdB, FINISH) -
					(findBytes (recvdB, START) + START.length) );
			}
			fos.close ();
			return 0;
		}
		catch ( IOException ex )
		{
			Utils.handleException (ex, "DataTransporter.getFile:"	// NOI18N
				+ f.getName () + ", id="	// NOI18N
				+ el.getID ());
			try
			{
				if ( fos != null )
				{
					fos.close ();
				}
			}
			catch (IOException exc)
			{
				Utils.handleException (ex, "DataTransporter.getFile:fos.close");
			}
			return -1;
		}
	}

	/**
	 * Tries to send the given command at most MAX_TRIALS times
	 *	and returns the response. The sending stops also when
	 *	the reply is not empty.
	 * @param cmd The command to send.
	 * @return The received reply.
	 */
	private String tryCommand (String cmd, Object[] extraTerminators)
	{
		if ( cmd == null )
		{
			return Utils.EMPTY_STR;
		}
		StringBuilder rcvd = new StringBuilder (10000);
		byte[] recvdB;
		int trials = 0;
		do
		{
			if ( trials >= MAX_TRIALS )
			{
				break;
			}
			Utils.sleepIgnoreException(DT_TIMEOUT);
			try
			{
				// send the command
				send (cmd.getBytes (DEFAULT_ENCODING));

				recvdB = recv (extraTerminators);
			}
			catch (IOException ioex)
			{
				Utils.handleException (ioex, "DataTransporter.tryCommand:"	// NOI18N
					+ cmd);
				trials++;
				continue;
			}
			// don't force any encodings, because the reply may
			// be in another encoding
			rcvd.append (new String (recvdB));

			if ( rcvd.toString ().trim ().isEmpty () )
			{
				reopen ();
				trials++;
			}
		} while (rcvd.toString ().trim ().isEmpty () && trials < MAX_TRIALS);
		return rcvd.toString ();
	}

	/**
	 * Gets the list of objects from the phone.
	 * @param ofWhat The type of the object, e.g. "PICTURES".
	 * @return A Vector containing information about the elements.
	 */
	public Vector<PhoneElement> getList (String ofWhat)
	{
		Vector<PhoneElement> res = new Vector<PhoneElement> (1);
		if ( ofWhat == null )
		{
			return res;
		}
		try
		{
			// removed: sending ATZ and AT115200
			String rcvd = tryCommand (CHARSET_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return res;
			}

			/*
			 * Receiving format:
			 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
			 * +KPSL: "53036500050220030045",0,48006,"RINGTONES","AMR","0000000069","","aaa"
					Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
			 */
			rcvd = tryCommand (LIST_CMD_START + ofWhat + LIST_CMD_END, null);
			if ( rcvd.trim ().isEmpty () || ! rcvd.contains (OK_STRING) )
			{
				return null;
			}

			Matcher m;
			// split into lines
			String[] lines = rcvd.split (NEWLINE_REGEX);
			for ( int i=0; i < lines.length; i++ )
			{
				m = Utils.LIST_PATTERN.matcher (lines[i]);
				if ( m.matches () )
				{
					if ( m.group (2).equals ("0") )			// NOI18N
					{
						res.add (new PhoneElement (m.group (1),
							m.group (3), m.group (4) ));
					}
				}
			}
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getList:"	// NOI18N
				+ ofWhat);
		}
		return res;
	}

	/**
	 * Deletes the specified object from the phone.
	 * @param el The element to delete.
	 * @return 0 in case of success.
	 */
	public int deleteFile (PhoneElement el)
	{
		if ( el == null )
		{
			return -3;
		}
		try
		{
			String rcvd = tryCommand (DEL_CMD_START + el.getID () + DQUOT_CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			return 0;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.deleteFile:"		// NOI18N
				+ el.getFilename () + "." + el.getExt () + ", id=" + el.getID ());	// NOI18N
			return -1;
		}
	}

	// 'public' because used in MainWindow
	/**
	 * Reopens the transmission port used by thie DataTransporter,
	 * saving its parameters.
	 */
	public void reopen ()
	{
		if ( portID == null )
		{
			return;
		}
		int bps = 115200;
		int dbits = SerialPort.DATABITS_8;
		int parity = SerialPort.PARITY_NONE;
		int flow = SerialPort.FLOWCONTROL_NONE;
		int sbits = SerialPort.STOPBITS_1;
		if ( s != null )
		{
			s.removeEventListener ();
			bps = s.getBaudRate ();
			dbits = s.getDataBits ();
			parity = s.getParity ();
			flow = s.getFlowControlMode ();
			sbits = s.getStopBits ();
		}
		try
		{
			close ();
			reopenRealOrFake();
			if ( s != null )
			{
				synchronized (inputStreamLock)
				{
					inputStream  = s.getInputStream  ();
				}
				outputStream = s.getOutputStream ();
				s.setSerialPortParams ( bps, dbits, sbits, parity );
				s.setFlowControlMode ( flow );

				s.addEventListener (spl);
				s.notifyOnDataAvailable (true);
			}
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "DataTransporter.reopen:"	// NOI18N
				+ getPortName());
		}
	}

	private void reopenRealOrFake() throws PortInUseException
	{
		if (portID instanceof CommPortIdentifier)
		{
			s = (SerialPort) ((CommPortIdentifier)portID)
				.open (PORT_OPEN_PROG_NAME, 2000);
		}
		else if (portID instanceof FakeCommPortIdentifier)
		{
			s = (SerialPort) ((FakeCommPortIdentifier)portID)
				.open (PORT_OPEN_PROG_NAME, 2000);
		}
	}

	/**
	 * Tests the port to determine if something is connected. This function
	 * sends "AT" to the port and waits for an "OK" to show up.
	 * @return 0 in case of "OK" response.
	 */
	public int test ()
	{
		try
		{
			byte[] cmd = AT_CMD.getBytes (DEFAULT_ENCODING);
			// 3 times gets bigger probability for an answer
			send (cmd);
			send (cmd);
			send (cmd);
			// don't force any encodings, because the reply may
			// contain trash that may be invalid in any given encoding
			String res = new String (recv (null));
			if ( res.contains (OK_STRING) )
			{
				return 0;
			}
			return -1;
		}
		catch ( IOException ex )
		{
			Utils.handleException (ex, "DataTransporter.test:"	// NOI18N
				+ getPortName());
			return -2;
		}
	}

	/**
	 * Gets the firmware version from the phone.
	 * @return the firmware version or null in case of error.
	 */
	public String getFirmwareVersion ()
	{
		String rcvd = tryCommand (VERSION_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "+KPSV: 2.04"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						Matcher m = VERSION_PATTERN.matcher (lines[i]);
						if ( m.matches () )
						{
							return m.group (1);
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "phone firmware");	// NOI18N
			}
		}
		return null;
	}

	/**
	 * Gets the phone type.
	 * @return the phone type or null in case of error.
	 */
	public String getDeviceType ()
	{
		String rcvd = tryCommand (IMEI_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "myX5-2 GPRS"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						String lineTrimmed = lines[i].trim ();
						if ( (! lineTrimmed.trim ().isEmpty ())
							&& ! lines[i].contains (IMEI_REPLY) )
						{
							return lineTrimmed;
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "phone type");	// NOI18N
			}
		}
		return null;
	}

	/**
	 * Gets the additional phone type data.
	 * @return the additional phone type or null in case of error.
	 */
	public String getExtraDeviceType ()
	{
		String rcvd = tryCommand (TYPE_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "+CGMR: SAGEM KB3,ME"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						lines[i] = lines[i].replaceAll
							(TYPE_REPLY_REGEX, Utils.EMPTY_STR)
							.trim ();
						if ( ! lines[i].isEmpty () )
						{
							return lines[i];
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "extra phone type");	// NOI18N
			}
		}
		return null;
	}

	/**
	 * Gets the IMEI number from the phone.
	 * @return the IMEI number or null in case of error.
	 */
	public String getIMEI ()
	{
		String rcvd = tryCommand (SERIALNUM_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "AT+CGSN
			// 353056005020024
			// OK"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						String lineTrimmed = lines[i].trim ();
						if ( (! lineTrimmed.isEmpty ())
							&& ! lines[i].contains (SERIALNUM_REPLY) )
						{
							return lineTrimmed;
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "phone IMEI");			// NOI18N
			}
		}
		return null;
	}

	/**
	 * Gets the subscriber phone numbers from the phone.
	 * @return the subscriber phone numbers (separated with ", ") or null in case of error / no numbers.
	 */
	public String getSubscriberNumbers ()
	{
		String rcvd = tryCommand (SUBSCR_NUM_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "+CNUM: "","+001123456789",145"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					String type = null;
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						lines[i] = lines[i].replaceAll
							(SUBSCR_NUM_REPLY_REGEX, Utils.EMPTY_STR);
						if ( ! lines[i].trim ().isEmpty () )
						{
							String[] elems = lines[i].trim ().split (Utils.COMMA);
							String newElem = Utils.EMPTY_STR;
							if ( elems != null )
							{
								if ( elems.length > 1 )
								{
									newElem = elems[1].trim()
										.replaceAll (Utils.DQUOT, Utils.EMPTY_STR);
								}
							}
							if ( ! newElem.isEmpty () )
							{
								if ( type == null )
								{
									type = Utils.EMPTY_STR;
								}
								if ( ! type.isEmpty () )
								{
									type += COMMA_SPACE_STR;
								}
								type += newElem;
							}
						}
					}
					return type;
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "phone subscriber numbers");	// NOI18N
			}
		}
		return null;
	}

	/**
	 * Gets the given capabilities from the phone.
	 * @param type The type of the capabilities to get.
	 * @return the capabilities or null in case of error.
	 */
	public String getCapabilities (String type)
	{
		if ( type == null )
		{
			return null;
		}
		String rcvd = tryCommand (CAPABILITY_CMD + type + DQUOT_CR, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			try
			{
				int connIndex = rcvd.indexOf (CONN_STRING);
				int endIndex = rcvd.indexOf (NOCAR_STRING);
				if ( connIndex >= 0 && endIndex >= 0 )
				{
					return rcvd.substring (connIndex+7,
						endIndex);
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "Capability: recv");		// NOI18N
			}
		}
		return null;
	}

	/**
	 * Switches off the phone.
	 */
	public void poweroff ()
	{
		try
		{
			send (POWEROFF_CMD.getBytes (DEFAULT_ENCODING));
		}
		catch (IOException ex)
		{
			Utils.handleException (ex, "power off");		// NOI18N
		}
	}

	/**
	 * Tells which PIN the phone is waiting for.
	 * @return the PIN the phone is waiting for (if any) or null in case of error.
	 */
	public DataTransporter.PIN_STATUS getPINStatus ()
	{
		String rcvd = tryCommand (PIN_STATUS_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						Matcher m = PIN_PATTERN.matcher (lines[i]);
						if ( m.matches () )
						{
							return DataTransporter.PIN_STATUS.
								looseValueOf (m.group (1));
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PIN status");	// NOI18N
			}
		}
		return null;
	}

	/**
	 * Sends the given PIN and new PIN to the phone.
	 * @param PIN The PIN to send.
	 * @param newPIN The new PIN to send (in case when PIN is a PUK or PUK2 code).
	 * @return 0 in case of no error.
	 */
	public int sendPIN (String PIN, String newPIN)
	{
		DataTransporter.PIN_STATUS status = getPINStatus ();
		if ( status == null )
		{
			return -1;
		}
		String cmd;
		if ( status.equals (DataTransporter.PIN_STATUS.SIM_PUK)
			|| status.equals (DataTransporter.PIN_STATUS.SIM_PUK2) )
		{
			cmd = SEND_PIN_CMD_START + PIN + Utils.COMMA + newPIN + Utils.CR;
		}
		else
		{
			cmd = SEND_PIN_CMD_START + PIN + Utils.CR;
		}
		String rcvd = tryCommand (cmd + Utils.CR, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// expecting: "OK"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						if ( lines[i].toUpperCase (Locale.ENGLISH)
							.contains (OK_STRING) )
						{
							return 0;
						}
					}
				}
				return -2;
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PIN ack recv");		// NOI18N
				return -3;
			}
		}
		return -4;
	}

	/**
	 * Sends the given PIN and new PIN to the phone.
	 * @param PIN The PIN to send.
	 * @param newPIN The new PIN to send (in case when PIN is a PUK or PUK2 code).
	 * @return 0 in case of no error.
	 */
	public int sendPIN (int PIN, int newPIN)
	{
		return sendPIN (String.valueOf (PIN), String.valueOf (newPIN));
	}

	/**
	 * Tells how many alarms are supported by the phone.
	 * @return the number of alarms supported or a negative number in case of no error.
	 */
	public int getNumberOfAlarms ()
	{
		String rcvd = tryCommand (ALARM_NUM_CMD, null);
		if ( ! rcvd.trim ().isEmpty () )
		{
			// sample: "+CALA: (1),(sound)
			//	OK"
			try
			{
				// have to split to make it work
				String[] lines = rcvd.split (Utils.CR);
				if ( lines != null )
				{
					for ( int i=0; i < lines.length; i++ )
					{
						if ( lines[i] == null )
						{
							continue;
						}
						Matcher m = ALNUM_PATTERN.matcher (lines[i]);
						if ( m.matches () )
						{
							return Integer.parseInt (m.group (1));
						}
					}
				}
				return -1;
			}
			catch (NumberFormatException ex)
			{
				Utils.handleException (ex, "alarm number recv");	// NOI18N
				return -2;
			}
		}
		return -3;
	}

	/**
	 * Deletes the specified alarm from the phone.
	 * @param number The number of the alarm to delete.
	 * @return 0 in case of success.
	 */
	public int deleteAlarm (int number)
	{
		if ( number < 0 )
		{
			return -1;
		}
		try
		{
			String rcvd = tryCommand (ALARM_DEL_CMD + String.valueOf (number) + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			if ( rcvd.contains (OK_STRING) )
			{
				return 0;
			}
			return -3;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.deleteAlarm:" + number );	// NOI18N
			return -4;
		}
	}

	/**
	 * Adds the specified alarm to the phone.
	 * @param al The alarm to add.
	 * @return 0 in case of success.
	 */
	public int addAlarm (PhoneAlarm al)
	{
		if ( al == null )
		{
			return -1;
		}
		try
		{
			String alString = al.getAlarmString ();
			if ( getNumberOfAlarms () == 1 )
			{
				// when the supported number of alarms is 1, don't send the days
				int num = al.getNumber ();
				boolean oneTime = al.isOneTimeAlarm ();
				al.setNumber (-1);
				al.setOneTimeAlarm (true);
				alString = al.getAlarmString ();
				al.setNumber (num);
				al.setOneTimeAlarm (oneTime);
			}
			String rcvd = tryCommand (ALARM_ADD_CMD + alString + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			if ( rcvd.contains (OK_STRING) )
			{
				return 0;
			}
			return -3;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.addAlarm:"		// NOI18N
				+ al.getAlarmString () );
			return -4;
		}
	}

	/**
	 * Gets the list of current alarms from the phone.
	 * @return the list of current alarms from the phone or null in case of error.
	 */
	public Vector<PhoneAlarm> getAlarms ()
	{
		Vector<PhoneAlarm> res = new Vector<PhoneAlarm> (1);
		try
		{
			String rcvd = tryCommand (ALARM_LIST_CMD, null);
			/*
			 * Receiving format:
			 * +CALA: "08/08/02,06:30:00"
			 * +CALA: "08/08/02,06:30:00"
			 * OK
			 */
			if ( rcvd.trim ().isEmpty () || ! rcvd.contains (OK_STRING) )
			{
				return null;
			}

			// split into lines
			String[] lines = rcvd.split (NEWLINE_REGEX);
			for ( int i=0; i < lines.length; i++ )
			{
				PhoneAlarm pa = PhoneAlarm.parseReponse (lines[i]);
				if ( pa != null )
				{
					res.add (pa);
				}
			}
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getAlarms");	// NOI18N
			return null;
		}
		return res;
	}

	/**
	 * Deletes the specified message from the phone.
	 * @param number The number of the message to delete.
	 * @return 0 in case of success.
	 */
	public int deleteMessage (int number)
	{
		if ( number < 0 )
		{
			return -1;
		}
		try
		{
			String rcvd = tryCommand (MSG_TEXT_MODE_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return -2;
			}
			rcvd = tryCommand (MSG_DEL_CMD + String.valueOf (number) + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -3;
			}
			if ( rcvd.contains (OK_STRING) )
			{
				return 0;
			}
			return -4;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.deleteMessage:" + number );	// NOI18N
			return -5;
		}
	}

	/**
	 * Gets the list of current messages from the phone.
	 * @return the list of current messages from the phone or null in case of error.
	 */
	public Vector<PhoneMessage> getMessages ()
	{
		Vector<PhoneMessage> res = new Vector<PhoneMessage> (1);
		try
		{
			String rcvd = tryCommand (MSG_TEXT_MODE_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return null;
			}

			rcvd = tryCommand (MSG_LIST_CMD, null);
			if ( rcvd.trim ().isEmpty () || ! rcvd.contains (OK_STRING) )
			{
				return null;
			}

			// remove the final "OK"
			rcvd = rcvd.replaceAll (LAST_OK_REGEX, Utils.EMPTY_STR);
			// split into lines
			String[] lines = rcvd.split (MSG_LIST_REPLY_REGEX);
			for ( int i=0; i < lines.length; i++ )
			{
				PhoneMessage pm = PhoneMessage.parseReponse (lines[i]);
				if ( pm != null )
				{
					res.add (pm);
				}
			}
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getMessagess");	// NOI18N
			return null;
		}
		return res;
	}

	/**
	 * Retrieves the specified message from the phone.
	 * @param number The number of the message to retrieve.
	 * @return 0 in case of success.
	 */
	public PhoneMessage getMessage (int number)
	{
		if ( number < 0 )
		{
			return null;
		}
		try
		{
			String rcvd = tryCommand (MSG_TEXT_MODE_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return null;
			}
			rcvd = tryCommand (MSG_GET_CMD + String.valueOf (number) + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return null;
			}
			// remove the final "OK"
			rcvd = rcvd.replaceAll (LAST_OK_REGEX, Utils.EMPTY_STR);
			return PhoneMessage.parseReponse (rcvd);
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getMessage:" + number );	// NOI18N
			return null;
		}
	}

	/**
	 * Sends the specified message with the phone.
	 * @param msg The message to send.
	 * @return 0 in case of success.
	 */
	public int sendMessage (PhoneMessage msg)
	{
		if ( msg == null )
		{
			return -1;
		}
		try
		{
			String rcvd = tryCommand (MSG_TEXT_MODE_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return -2;
			}

			// first send the command and wait for the prompt:
			rcvd = tryCommand (MSG_SEND_CMD + "\"" + msg.getRecipientNum ()
				+ "\"" + Utils.CR, new String[] { MSG_PROMPT });
			if ( ! rcvd.contains (MSG_PROMPT) )
			{
				return -3;
			}

			// after getting the prompt send the message:
			rcvd = tryCommand (msg.getMessageString () + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -4;
			}
			if ( rcvd.contains (OK_STRING) )
			{
				return 0;
			}
			return -5;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.sendMessage: '"		// NOI18N
				+ msg.toString () + "'" );			// NOI18N
			return -6;
		}
	}

	/**
	 * Sets the specified message storage type the phone.
	 * @param stor The storage to use.
	 * @return 0 in case of success.
	 */
	public int setMessageStorage (STORAGE_TYPE stor)
	{
		if ( stor == null )
		{
			return -1;
		}
		try
		{
			String rcvd = tryCommand (MSG_STORAGE_CMD + stor.toString () + Utils.CR, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			if ( rcvd.contains (OK_STRING) )
			{
				return 0;
			}
			return -3;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.setMessageStorage: '"	// NOI18N
				+ stor.toString () + "'" );					// NOI18N
			return -4;
		}
	}

	/**
	 * Sets or clears the DTR line in the port. The port must already be open.
	 * @param on If TRUE, the DTR signal will be set, otherwise it will be cleared.
	 */
	public void setDTR (boolean on)
	{
		if ( s != null )
		{
			s.setDTR (on);
		}
	}

	/**
	 * Sets or clears the RTS line in the port. The port must already be open.
	 * @param on If TRUE, the RTS signal will be set, otherwise it will be cleared.
	 */
	public void setRTS (boolean on)
	{
		if ( s != null )
		{
			s.setRTS (on);
		}
	}

	/**
	 * Gets the state of the Carrier Detect signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isCD ()
	{
		if ( s != null )
		{
			return s.isCD ();
		}
		return false;
	}

	/**
	 * Gets the state of the Clear To Send signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isCTS ()
	{
		if ( s != null )
		{
			return s.isCTS ();
		}
		return false;
	}

	/**
	 * Gets the state of the Data Set Ready signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isDSR ()
	{
		if ( s != null )
		{
			return s.isDSR ();
		}
		return false;
	}

	/**
	 * Gets the state of the Data Terminal Ready signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isDTR ()
	{
		if ( s != null )
		{
			return s.isDTR ();
		}
		return false;
	}

	/**
	 * Gets the state of the Ring Indicator signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isRI ()
	{
		if ( s != null )
		{
			return s.isRI ();
		}
		return false;
	}

	/**
	 * Gets the state of the Request To Send signal.
	 * @return TRUE if the signal is on, FALSE otherwise.
	 */
	public boolean isRTS ()
	{
		if ( s != null )
		{
			return s.isRTS ();
		}
		return false;
	}

	/**
	 * Reads the signal power from the phone.
	 * @return the currently-deteted signal power (0-31 inclusive)
	 *	or a negative value in case of error.
	 */
	public int getSignalPower ()
	{
		try
		{
			String rcvd = tryCommand (SIGNAL_POWER_CMD, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			Matcher sigMatcher = SIGNAL_POWER_PATTERN.matcher (rcvd);
			// NOT "matches()", because we can get "AT+CSQ" in the reply
			if ( sigMatcher.find () )
			{
				try
				{
					return Integer.parseInt (sigMatcher.group (1));
				}
				catch (NumberFormatException ex)
				{
					Utils.handleException (ex,
						"DataTransporter.getSignalPower: '"		// NOI18N
						+ sigMatcher.group (1) + "'");			// NOI18N
				}
			}
			return -3;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getSignalPower");		// NOI18N
			return -1;
		}
	}

	/**
	 * Dials the specified number.
	 * @param number the number to dial. Must be non-null.
	 * @param isVoice whether the connection is for voice or data. Voice connections
	will be initiated by adding a SEMICOLON to the end of the number.
	 * @param dialMode The dial mode (tone, pulse, auto). Defaults to "auto" if null.
	 * @return 0 in case of success or a negative value in case of error.
	 */
	public int dialNumber (String number, boolean isVoice, DIAL_MODE dialMode)
	{
		if ( number == null )
		{
			return -1;
		}
		String command = DIAL_CMD_AUTO;
		if ( dialMode != null && DIAL_MODE.TONE.equals (dialMode) )
		{
			command = DIAL_CMD_TONE;
		}
		if ( dialMode != null && DIAL_MODE.PULSE.equals (dialMode) )
		{
			command = DIAL_CMD_PULSE;
		}
		command += number;
		if ( isVoice )
		{
			command += SEMICOLON;
		}
		try
		{
			send ((command + Utils.CR).getBytes (DEFAULT_ENCODING));
			byte[] recvdB = recv (null);
			String rcvd = new String (recvdB, DEFAULT_ENCODING);

			if ( rcvd.isEmpty ()
				|| rcvd.contains (ERROR_STRING)
				|| rcvd.contains (NOCAR_STRING))
			{
				return -3;
			}
			return 0;
		}
		catch ( IOException e )
		{
			Utils.handleException (e, "DataTransporter.dialNumber");	// NOI18N
			return -2;
		}
	}

	/**
	 * Hangs the phone up (stops the current call).
	 * @return 0 in case of success.
	 */
	public int hangup ()
	{
		try
		{
			String rcvd = tryCommand (HANGUP_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return -2;
			}
			return 0;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.hangup");	// NOI18N
			return -1;
		}
	}

	/**
	 * Answers the phone (receives any incoming call).
	 * @return 0 in case of success.
	 */
	public int answer ()
	{
		try
		{
			String rcvd = tryCommand (ANSWER_CALL_CMD, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return -2;
			}
			return 0;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.answer");	// NOI18N
			return -1;
		}
	}

	/**
	 * Gets the current volume level.
	 * @return the current volume level or -1 in case of error.
	 */
	public int getVolume ()
	{
		try
		{
			String rcvd = tryCommand (CURRENT_VOLUME_CMD, null);
			if ( rcvd.trim ().isEmpty () )
			{
				return -2;
			}
			Matcher volMatcher = VOLUME_LEVEL_PATTERN.matcher (rcvd);
			if ( volMatcher.find () )
			{
				try
				{
					return Integer.parseInt (volMatcher.group (1));
				}
				catch (NumberFormatException ex)
				{
					Utils.handleException (ex,
						"DataTransporter.getVolume: '"		// NOI18N
						+ volMatcher.group (1) + "'");			// NOI18N
				}
			}
			return -3;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getVolume");	// NOI18N
			return -1;
		}
	}

	/**
	 * Sets the volume level.
	 * @param vol The volume level to set.
	 * @return the current volume level or -1 in case of error.
	 */
	public int setVolume (int vol)
	{
		try
		{
			if ( vol < 0 )
			{
				return -4;
			}
			String rcvd = tryCommand (SET_VOLUME_CMD
				+ String.valueOf(vol) + Utils.CR, null);
			if ( ! rcvd.contains (OK_STRING) )
			{
				return -2;
			}
			return 0;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.setVolume");	// NOI18N
			return -1;
		}
	}

	/**
	 * Gets the number of currently-available bytes.
	 * @return the number of currently-available bytes.
	 */
	public int getAvailableBytes ()
	{
		if ( inputStream == null )
		{
			return 0;
		}
		synchronized (inputStreamLock)
		{
			try
			{
				return inputStream.available ();
			}
			catch (IOException ioex)
			{
				return 0;
			}
		}
	}

	private String getPortName()
	{
		if (portID == null)
		{
			return null;
		}
		String portName = null;
		if (portID instanceof CommPortIdentifier)
		{
			portName = ((CommPortIdentifier)portID).getName ();
		}
		else if (portID instanceof FakeCommPortIdentifier)
		{
			portName = ((FakeCommPortIdentifier)portID).getName ();
		}
		return portName;
	}

	private class SPL implements SerialPortEventListener
	{
		/**
		 * Used to receive events for the port.
		 * @param event A received port event.
		 */
		@Override
		public void serialEvent (SerialPortEvent event)
		{
			if ( event == null )
			{
				return;
			}
			switch (event.getEventType ())
			{
				case SerialPortEvent.DATA_AVAILABLE:
					//synchronized (inputStream)
					synchronized (inputStreamLock)
					{
						inputStreamLock.notifyAll ();// inputStream.notifyAll ();
					}
					break;
			}
		}

		@Override
		public String toString ()
		{
			return "DataTransporter.SPL";	// NOI18N
		}
	}

	/**
	 * The Enumeration of the states for PIN entering.
	 */
	public static enum PIN_STATUS
	{
		READY,
		SIM_PIN,
		SIM_PUK,
		SIM_PIN2,
		SIM_PUK2,
		PH_NET_PIN;

		/**
		 * Gets the PIN_STATUS that's name looks like the given String
		 * (except maybe for whitespace, dashes etc.).
		 * @param name The name of the enum value to find.
		 * @return a PIN_STATUS that is roughly equal to the required one.
		 */
		public static PIN_STATUS looseValueOf (String name)
		{
			if ( name == null )
			{
				return null;
			}
			PIN_STATUS[] values = values ();
			if ( values != null )
			{
				for ( int i = 0; i < values.length; i++ )
				{
					if ( values[i].toString ()
						.replaceAll ("\\s", "_")		// NOI18N
						.replaceAll ("-", "_")			// NOI18N
						.trim ().equals (name.trim ()) )
					{
						return values[i];
					}
				}
			}
			return null;
		}
	}

	/**
	 * The Enumeration of the message storage types.
	 */
	public static enum STORAGE_TYPE
	{
		/** SIM storage. */
		SM,
		/** MT storage. */
		MT,
		/** Phone memory storage. */
		ME;
	}

	/**
	 * The Enumeration of the dialing modes.
	 */
	public static enum DIAL_MODE
	{
		/** Automatic select (ATD command). */
		AUTO,
		/** Tone dialing (ATDT command). */
		TONE,
		/** Pulse dialing (ATDP command). */
		PULSE;
	}
}
