/*
 * DataTransporter.java, part of the JYMAG package.
 *
 * Copyright (C) 2007 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag;

import gnu.io.CommPortIdentifier;
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
import java.util.Vector;
import java.util.regex.Matcher;

/**
 * This class is used to send and receive data.
 * @author Bogdan Drozdowski
 */
public class DataTransporter
{
	private CommPortIdentifier portID;
	private SerialPort s;
	private InputStream inputStream;
	private OutputStream outputStream;
	/** Maximum number of attempts to establish communication. */
	public static final int MAX_TRIALS = 3;
	/** Timeout betwenn up/download stages, in milliseconds. */
	private static final int TIMEOUT = 250;

	private SPL spl = new SPL ();

	// file headers:
	private final byte[] JPG = new byte[] { (byte) 0xff, (byte) 0xd8, (byte) 0xff };
	// MTh
	private final byte[] MID = new byte[] { (byte) 0x4d, (byte) 0x54, (byte) 0x68 };
	// #!AMR = 23 21 41 4D  52
	private final byte[] AMR = new byte[] { (byte) 0x23, (byte) 0x21, (byte) 0x41,
		(byte) 0x4D, (byte) 0x52 };
	// BM  = 42 4D
	private final byte[] BMP = new byte[] { (byte) 0x42, (byte) 0x4D };
	// GIF = 47 49 46
	private final byte[] GIF = new byte[] { (byte) 0x47, (byte) 0x49, (byte) 0x46 };
	// PNG
	private final byte[] PNG = new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47 };
	// WAV = RIFF = 52 49 46 46
	private final byte[] WAV = new byte[] { (byte) 0x52, (byte) 0x49, (byte) 0x46, (byte) 0x46 };
	//// "BEGIN:VCALENDAR"
	private final byte[] VCAL = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x49,
		(byte) 0x4E, (byte) 0x3A, (byte) 0x56, (byte) 0x43,
		(byte) 0x41, (byte) 0x4C, (byte) 0x45, (byte) 0x4E,
		(byte) 0x44, (byte) 0x41, (byte) 0x52 };
	//// "BEGIN:vCard"
	private final byte[] VCRD = new byte[] {
		(byte) 0x42, (byte) 0x45, (byte) 0x47, (byte) 0x49,
		(byte) 0x4E, (byte) 0x3A, (byte) 0x76, (byte) 0x43,
		(byte) 0x61, (byte) 0x72, (byte) 0x64 };
	// WBMP: 0, 0
	//private final byte[] WBMP = new byte[] { (byte) 0x00, (byte) 0x00 };
	// the 2-bytes-zero header dosen't work well. Instead, let's look for '$'
	// and skip 3 bytes after it.
	private final byte[] WBMP = new byte[] { (byte) 0x24 };

	// MNG: 8A 4D 4E 47
	private final byte[] MNG = new byte[] {
		(byte) 0x8A, (byte) 0x4D, (byte) 0x4E, (byte) 0x47 };

	// "CONNECT\r\n"
	private final byte[] START = new byte[] {
		(byte) 0x43, (byte) 0x4F, (byte) 0x4E, (byte) 0x4E,
		(byte) 0x45, (byte) 0x43, (byte) 0x54, (byte) 0x0D,
		(byte) 0x0A };

	// "\r\nNO CARRIER"
	private final byte[] FINISH = new byte[] {
		(byte) 0x0D, (byte) 0x0A,
		(byte) 0x4E, (byte) 0x4F, (byte) 0x20, (byte) 0x43,
		(byte) 0x41, (byte) 0x52, (byte) 0x52, (byte) 0x49,
		(byte) 0x45, (byte) 0x52 };

	// i18n stuff:
	private final String ansString = "answer";

	/**
	 * Creates a new instance of DataTransporter.
	 * @param id Port identifier for the port which will be used to
	 * transfer data.
	 */
	public DataTransporter (CommPortIdentifier id)
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
		if ( portID == null ) throw new NullPointerException ("DataTransporter.open: portID == null");
		if ( ! portID.getName ().startsWith ("COM") )
		{
			File portFile = new File (portID.getName ());
			if ( ! portFile.exists () ) throw new IOException ("DataTransporter.open: ! portFile.exists: "
					+ portID.getName ());
		}
		s = (SerialPort) portID.open ("JYMAG", 2000);	// NOI18N
		inputStream  = s.getInputStream  ();
		outputStream = s.getOutputStream ();

		int dBits = SerialPort.DATABITS_8;
		if ( dataBits == 7 ) dBits = SerialPort.DATABITS_7;
		else if ( dataBits == 6 ) dBits = SerialPort.DATABITS_6;
		else if ( dataBits == 5 ) dBits = SerialPort.DATABITS_5;

		int sBits = SerialPort.STOPBITS_1;
		if ( Math.abs (stopBits - 1.5f) < 0.0001 ) sBits = SerialPort.STOPBITS_1_5;
		else if ( Math.abs (stopBits - 2.0f) < 0.0001 ) sBits = SerialPort.STOPBITS_2;

		int par = SerialPort.PARITY_NONE;
		// Combo: none, even, odd, space, mark
		if ( parity == 1 ) par = SerialPort.PARITY_EVEN;
		else if ( parity == 2 ) par = SerialPort.PARITY_ODD;
		else if ( parity == 3 ) par = SerialPort.PARITY_SPACE;
		else if ( parity == 4 ) par = SerialPort.PARITY_MARK;

		int flow = SerialPort.FLOWCONTROL_NONE;
		// Combo: none, XONN/XOFF, RTS/CTS
		if ( flowControl == 1 ) flow = SerialPort.FLOWCONTROL_XONXOFF_IN
			+ SerialPort.FLOWCONTROL_XONXOFF_OUT;
		else if ( flowControl == 2 ) flow = SerialPort.FLOWCONTROL_RTSCTS_IN
			+ SerialPort.FLOWCONTROL_RTSCTS_OUT;

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
		if ( inputStream == null ) return res;
		while (true)
		{
			try
			{
				if ( inputStream.available () == 0 )
				{
					synchronized (inputStream)
					{
						inputStream.wait (5*1000);
					}
					if ( inputStream.available () > 0 )
					{
						byte[] readBuffer = new byte[inputStream.available ()];
						int wasRead = inputStream.read (readBuffer);
						if ( wasRead < 0 ) break;
						String curr = new String (readBuffer, 0, wasRead);
						res = joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
						if ( isTerminatorPresent ( curr.trim (), extraTerminators ) )
						{
							break;
						}
					}
					else return res;
				}
				else if ( inputStream.available () > 0 )
				{
					byte[] readBuffer = new byte[inputStream.available ()];
					int wasRead = inputStream.read (readBuffer);
					if ( wasRead < 0 ) break;
					String curr = new String (readBuffer, 0, wasRead);
					res = joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
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
					if ( inputStream.available () > 0 )
					{
						byte[] readBuffer = new byte[inputStream.available ()];
						int wasRead = inputStream.read (readBuffer);
						if ( wasRead < 0 ) break;
						String curr = new String (readBuffer, 0, wasRead);
						res = joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
						if ( isTerminatorPresent ( curr.trim (), extraTerminators ) )
						{
							break;
						}
					}
				}
				catch (Exception ex) {}
			}
			catch (Exception ex)
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
		if ( haystack == null ) return false;
		if ( haystack.trim ().startsWith ("OK")				// NOI18N
				|| haystack.trim ().endsWith ("OK")			// NOI18N
				|| haystack.trim ().startsWith ("ERROR")	// NOI18N
				|| haystack.trim ().endsWith ("ERROR")		// NOI18N
				|| haystack.contains ("NO CARRIER") ) return true;		// NOI18N
		if ( needles != null )
		{
			for ( int i=0; i < needles.length; i++ )
			{
				if ( haystack.contains (needles[i].toString ())) return true;
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
		if ( b == null || outputStream == null ) return;
		outputStream.write (b);
		outputStream.flush ();
	}

	/**
	 * Closes the port.
	 */
	public void close ()
	{
		if ( s != null ) s.close ();
	}

	/**
	 * Sends the specified file to the phone.
	 * @param f The file to send.
	 * @param newName Name of the object to be sent to the phone.
	 * @return 0 in case of success.
	 */
	public int putFile (File f, String newName)
	{
		if ( f == null ) return -11;
		// check if type is supported.
		if ( ! f.getName ().contains (".")) return -8;	// NOI18N
		if ( ! Utils.filetypeIDs.containsKey (f.getName ().substring
			(f.getName ().lastIndexOf (".")+1)	// NOI18N
			.toLowerCase ()))
		{
			return -9;
		}
		if ( newName == null ) newName = f.getName ().substring (0,
			f.getName ().indexOf ("."));

		String rcvd;
		byte[] recvdB;
		// number of attempts.
		int trials = 0;
		MAIN: while (trials <= MAX_TRIALS)
		{
			// stage variable, useful when an exception is caught.
			int stage = 0;
			try
			{
				// reset file upload
				send ("AT+KDOBJ=1,0\r".getBytes ());	// NOI18N
				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("")		// NOI18N
					|| (! rcvd.contains ("OK")	// NOI18N
					&& ! rcvd.contains ("ERROR")) )	// NOI18N
				{
					reopen ();
					trials++;
					continue MAIN;
				}
			}
			catch ( Exception e )
			{
				Utils.handleException (e, "DataTransporter.putFile: init:"	// NOI18N
					+ f.getName () + ", newName=" + newName);	// NOI18N
				reopen ();
				trials++;
				continue MAIN;
			}
			try
			{
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// init file upload
				do
				{
					send ("AT+KDOBJ=1,1\r".getBytes ());	// NOI18N
					recvdB = recv (null);
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -1;
				if ( ! rcvd.contains ("OK") )	// NOI18N
				{
					System.out.println (ansString + "1=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -1;
				}

				stage++;
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send filename length (the last parameter)
				do
				{
					send ( ("AT+KDOBJ=2,1,3,0," + newName.length () + "\r")	// NOI18N
						.getBytes ());
					recvdB = recv (new String[] { "CONNECT" });	// NOI18N
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -2;
				if ( rcvd.contains ("ERROR") || ! rcvd.contains ("CONNECT") )	// NOI18N
				{
					System.out.println (ansString + "2=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -2;
				}

				stage++;
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send filename
				do
				{
					send ( (newName+"\r").getBytes ());	// NOI18N
					recvdB = recv (null);
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -3;
				if ( ! rcvd.contains ("OK") )	// NOI18N
				{
					System.out.println (ansString + "3=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -3;
				}

				stage++;
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send file type (4th param) and length (5th parameter)
				do
				{
					send ( ("AT+KDOBJ=2,1,0,"	// NOI18N
						+ Utils.filetypeIDs.get
							(f.getName ().substring
								(f.getName ().lastIndexOf (".")+1) // NOI18N
							.toLowerCase ()).intValue ()
						+ "," + f.length () + "\r").getBytes ());	// NOI18N
					recvdB = recv (new String[] { "CONNECT" });	// NOI18N
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -4;
				if ( rcvd.contains ("ERROR") || ! rcvd.contains ("CONNECT") )	// NOI18N
				{
					System.out.println (ansString + "4=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -4;
				}

				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send file data here:
				FileInputStream fis = new FileInputStream (f);
				byte[] contents = new byte[10240];
				int read = -1;
				stage++;
				do
				{
					read = fis.read (contents);
					if ( read < 0 ) break;
					send (Arrays.copyOf (contents, read));

				} while (read == contents.length);
				fis.close ();
				do
				{
					recvdB = recv (null);
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -5;
				if ( ! rcvd.contains ("OK") )	// NOI18N
				{
					System.out.println (ansString + "5=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -5;
				}

				stage++;
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// close file upload
				do
				{
					send ("AT+KDOBJ=1,0\r".getBytes ());	// NOI18N
					recvdB = recv (null);
					rcvd = new String (recvdB);

					if ( rcvd.trim ().equals ("") )	// NOI18N
					{
						reopen ();
						trials++;
						if ( trials > MAX_TRIALS ) break;
						continue MAIN;
					}
				} while (rcvd.trim ().equals ("") && trials <= MAX_TRIALS);	// NOI18N
				if ( trials > MAX_TRIALS ) return -6;
				if ( ! rcvd.contains ("OK") )	// NOI18N
				{
					System.out.println (ansString + "6=" + rcvd);	// NOI18N
					reopen ();
					trials++;
					continue MAIN;
					//return -6;
				}
				return 0;
			}
			catch ( Exception ex )
			{
				/*
				if ( stage > 0 )
				{
					try
					{
						// close file upload
						send ("AT+KDOBJ=1,0\r".getBytes ());	// NOI18N
						recv (null);
					}
					catch ( Exception e ) {}
				}*/
				Utils.handleException (ex, "DataTransporter.putFile:end"	// NOI18N
					+ f.getName () + ", newName=" + newName);	// NOI18N
				//return -7;
				reopen ();
				trials++;
				continue MAIN;
			}
		} // MAIN while
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
			if ( needle == null ) return 0;
			if ( needle.length > haystack.length ) return -1;
			HAY: for ( int i=0; i < haystack.length; i++ )
			{
				for ( int j=0; j < needle.length; j++ )
				{
					if ( haystack[i+j] != needle[j] ) continue HAY;
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
		if ( f == null || el == null ) return -3;
		try
		{
			String rcvd;
			byte[] recvdB;
			int trials = 0;
			do
			{
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send file retrieve command
				send (("AT+KPSR=\"" + el.getID () + "\"\r").getBytes ());	// NOI18N
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
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") || recvdB.length == 0 )	// NOI18N
				{
					reopen ();
					trials++;
				}
			} while ( (rcvd.trim ().equals ("") || recvdB.length == 0)	// NOI18N
				&& trials < MAX_TRIALS);
			if ( rcvd.trim ().equals ("") || recvdB.length == 0	// NOI18N
				|| ! rcvd.contains ("NO CARRIER") ) return -2;	// NOI18N

			// check file type
			FileOutputStream fos = new FileOutputStream (f);
			if ( findBytes (recvdB, JPG) >= 0 )
			{
				// check if single or double match
				int pos = findBytes (recvdB, JPG);
				int pos2 = findBytes (Arrays.copyOfRange (recvdB, pos+1, recvdB.length),
					JPG);
				if ( pos2 >= 0 )
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
			else if ( findBytes (recvdB, BMP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, BMP),
					findBytes (recvdB, FINISH) - findBytes (recvdB, BMP) );
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
			else if ( findBytes (recvdB, WBMP) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, WBMP) + 4,
					findBytes (recvdB, FINISH) - (findBytes (recvdB, WBMP)+4) );
			}
			else if ( findBytes (recvdB, MNG) >= 0 )
			{
				fos.write (recvdB, findBytes (recvdB, MNG),
					findBytes (recvdB, FINISH) - findBytes (recvdB, MNG) );
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
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.getFile:"	// NOI18N
				+ f.getName () + ", id="	// NOI18N
				+ el.getID ());
			return -1;
		}
	}

	/**
	 * Gets the list of objects from the phone.
	 * @param ofWhat The type of the object, e.g. "PICTURES".
	 * @return A Vector containing information about the elements.
	 */
	public Vector<PhoneElement> getList (String ofWhat)
	{
		Vector<PhoneElement> res = new Vector<PhoneElement> (1);
		if ( ofWhat == null || res == null ) return res;
		try
		{
			byte[] rcvdB;
			String rcvd = "";	// NOI18N
			int trials = 0;
			// removed: sending ATZ and AT115200
			trials = 0;
			do
			{
				// set charset information
				send ("AT+CSCS=\"8859-1\"\r".getBytes ());	// NOI18N
				rcvdB = recv (null);
				rcvd += new String(rcvdB);

				if ( rcvd.trim ().equals ("") )	// NOI18N
				{
					reopen ();
					trials++;
				}
			} while (rcvd.trim ().equals ("") && trials < MAX_TRIALS);	// NOI18N

			if ( ! rcvd.contains ("OK") ) return res;	// NOI18N
			trials = 0;
			do
			{
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send "get list" command
				send (("AT+KPSL=\"" + ofWhat + "\",1\r").getBytes ());	// NOI18N
			/*
			 * Receiving format:
			 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
			 * +KPSL: "53036500050220030045",0,48006,"RINGTONES","AMR","0000000069","","aaa"
					Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
			 */
				rcvdB = recv (null);
				rcvd += new String(rcvdB);

				if ( rcvd.trim ().equals ("") )	// NOI18N
				{
					reopen ();
					trials++;
				}
			} while (rcvd.trim ().equals ("") && trials < MAX_TRIALS);	// NOI18N
			if ( rcvd.trim ().equals ("") || ! rcvd.contains ("OK") ) return null;	// NOI18N

			Matcher m;
			// split into lines
			String[] lines = rcvd.split ("[\\r\\n]{1,2}");	// NOI18N
			for ( int i=0; i < lines.length; i++ )
			{
				m = Utils.listPattern.matcher (lines[i]);
				if ( m.matches () )
				{
					if ( m.group (2).equals ("0"))	// NOI18N
						res.add (new PhoneElement (m.group (1),
							m.group (3), m.group (4) ));
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
		if ( el == null ) return -3;
		try
		{
			String rcvd;
			byte[] recvdB;
			int trials = 0;
			do
			{
				try
				{
					Thread.sleep (TIMEOUT);
				} catch (Exception ex) {}
				// send file retrieve command
				send (("AT+KPSD=\"" + el.getID () + "\"\r").getBytes ());	// NOI18N

				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )	// NOI18N
				{
					reopen ();
					trials++;
				}
			} while (rcvd.trim ().equals ("") && trials < MAX_TRIALS);	// NOI18N
			if ( rcvd.trim ().equals ("") || trials >= MAX_TRIALS )		// NOI18N
				return -2;
			return 0;
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.deleteFile:"	// NOI18N
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
		if ( portID == null ) return;
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
			s = (SerialPort) portID.open ("JYMAG", 2000);	// NOI18N
			if ( s != null )
			{
				inputStream  = s.getInputStream  ();
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
				+ portID.getName ());
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
			// 3 times gets bigger probability for an answer
			send ("AT\r".getBytes ());	// NOI18N
			send ("AT\r".getBytes ());	// NOI18N
			send ("AT\r".getBytes ());	// NOI18N
			String res = new String (recv (null));
			if ( res.contains ("OK") )	// NOI18N
			{
				return 0;
			}
		}
		catch ( Exception ex )
		{
			Utils.handleException (ex, "DataTransporter.test:"	// NOI18N
				+ portID.getName ());
		}
		return -1;
	}

	/**
	 * This function joins 2 arrays of bytes together.
	 * @param orig The first array.
	 * @param toAdd The array to add.
	 * @return an array starting with "orig" followed by "toAdd".
	 */
	private byte[] joinArrays (byte[] orig, byte[] toAdd)
	{
		if ( orig == null ) return toAdd;
		if ( toAdd == null ) return orig;
		byte[] ret = new byte[orig.length + toAdd.length];
		if ( ret == null ) return null;
		System.arraycopy (orig, 0, ret, 0, orig.length);
		System.arraycopy (toAdd, 0, ret, orig.length, toAdd.length);
		return ret;
	}

	private class SPL implements SerialPortEventListener
	{
		/**
		 * Used to receive events for the port.
		 * @param event A received port event.
		 */
		public void serialEvent (SerialPortEvent event)
		{
			if ( event == null ) return;
			switch (event.getEventType ())
			{
				case SerialPortEvent.DATA_AVAILABLE:
					synchronized (inputStream)
					{
						inputStream.notifyAll ();
					}
					break;
			}
		}
	}
}
