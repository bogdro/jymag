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
public class DataTransporter implements SerialPortEventListener
{
	private CommPortIdentifier portID;
	private SerialPort s;
	private InputStream inputStream;
	private OutputStream outputStream;

	// i18n stuff:
	private final String ansString = "answer";

	/**
	 * Creates a new instance of DataTransporter, using defaults.
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
		s = (SerialPort) portID.open ("JYMAG", 2000);
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

		s.addEventListener (this);
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
		try
		{
			if ( inputStream.available () == 0 )
			{
				synchronized (inputStream)
				{
					inputStream.wait (5*1000);
				}
				if ( inputStream.available () != 0 )
				{
					res = this.joinArrays (res, recvData (extraTerminators));
				}
			}
			else
			{
					res = this.joinArrays (res, recvData (extraTerminators));
			}
		}
		catch (InterruptedException intex)
		{
			res = this.joinArrays (res, recvData (extraTerminators));
		}
		catch ( Exception ex )
		{
			System.out.println (ex);
			ex.printStackTrace ();
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
		if ( needles != null && haystack != null )
		{
			for ( int i=0; i < needles.length; i++ )
			{
				if ( haystack.contains (needles[i].toString ())) return true;
			}
		}
		return false;
	}

	/**
	 * Receives data from the port with checking for terminators, but with no
	 *	waiting (the data is assumed being ready). This is the worker function
	 *	that actually does the reading.
	 * @param extraTerminators Any extra elements that are a mark of transmission end.
	 * @return The received bytes.
	 */
	private byte[] recvData (Object[] extraTerminators)
	{
		byte[] res = new byte[0];
		while (true)
		{
			try
			{
				int wasRead;
				byte[] readBuffer;
				// wait for synchro and read
				synchronized (inputStream)
				{
					readBuffer = new byte[inputStream.available ()];
					wasRead = inputStream.read (readBuffer);
				}

				if ( wasRead < 0 ) break;
				String curr = new String (readBuffer, 0, wasRead);
				res = this.joinArrays (res, Arrays.copyOf (readBuffer, wasRead));
				if (
					   curr.trim ().startsWith ("OK")
					|| curr.trim ().startsWith ("ERROR")
					|| curr.trim ().endsWith ("OK")
					|| curr.trim ().endsWith ("ERROR")
					|| curr.trim ().endsWith ("NO CARRIER")
					|| isTerminatorPresent ( curr.trim (), extraTerminators )
					)
				{
					break;
				}
			}
			catch ( Exception ex )
			{
				System.out.println (ex);
				ex.printStackTrace ();
				break;
			}
		}
		return res;
	}

	/**
	 * Sends data to the port.
	 * @param b The bytes to send.
	 * @throws java.io.IOException in case of an I/O problem with the port.
	 */
	public void send (byte[] b) throws IOException
	{
		outputStream.write (b);
		outputStream.flush ();
	}

	/**
	 * Closes the port.
	 */
	public void close ()
	{
		s.close ();
	}

	/**
	 * Sends the specified file to the phone.
	 * @param f The file to send.
	 * @param newName Name of the object to be sent to the phone.
	 * @return 0 in case of success.
	 */
	public int putFile (File f, String newName)
	{
		// check if type is supported.
		if ( ! f.getName ().contains (".")) return -8;
		if ( ! MainWindow.filetypeIDs.containsKey (f.getName ().substring
			(f.getName ().lastIndexOf (".")+1)
			.toLowerCase ()))
		{
			return -9;
		}

		// stage variable, useful when an exception is caught.
		int stage = 0;
		try
		{
			String rcvd;
			byte[] recvdB;

			// init file upload
			do
			{
				send ("AT+KDOBJ=1,1\r".getBytes ());
				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( ! rcvd.contains ("OK") )
			{
				System.out.println (ansString + "1=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -1;
			}

			stage++;
			// send filename length (the last parameter)
			do
			{
				send ( ("AT+KDOBJ=2,1,3,0," + newName.length () + "\r").getBytes ());
				recvdB = recv (new String[] { "CONNECT" });
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( rcvd.contains ("ERROR") || ! rcvd.contains ("CONNECT") )
			{
				System.out.println (ansString + "2=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -2;
			}

			stage++;
			// send filename
			do
			{
				send ( (newName+"\r").getBytes ());
				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( ! rcvd.contains ("OK") )
			{
				System.out.println (ansString + "3=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -3;
			}

			stage++;
			// send file type (4th param) and length (5th parameter)
			do
			{
				send ( ("AT+KDOBJ=2,1,0,"
					+ MainWindow.filetypeIDs.get
						(f.getName ().substring
							(f.getName ().lastIndexOf (".")+1)
						.toLowerCase ()).intValue ()
					+ "," + f.length () + "\r").getBytes ());
				recvdB = recv (new String[] { "CONNECT" });
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( rcvd.contains ("ERROR") || ! rcvd.contains ("CONNECT") )
			{
				System.out.println (ansString + "4=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -4;
			}

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

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( ! rcvd.contains ("OK") )
			{
				System.out.println (ansString + "5=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -5;
			}

			stage++;
			// close file upload
			do
			{
				send ("AT+KDOBJ=1,0\r".getBytes ());
				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			if ( ! rcvd.contains ("OK") )
			{
				System.out.println (ansString + "6=" + rcvd);
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					recvdB = recv (null);
				}
				catch ( Exception e ) {}
				return -6;
			}
		}
		catch ( Exception ex )
		{
			if ( stage > 0 )
			{
				try
				{
					// close file upload
					send ("AT+KDOBJ=1,0\r".getBytes ());
					byte[] recvdB = recv (null);
				}
				catch ( Exception e ) {}
			}
			System.out.println (ex);
			ex.printStackTrace ();
			return -7;
		}
		return 0;
	}

	/**
	 * Gets the specified object and saves it to a file.
	 * @param f The file for the object.
	 * @param el The element to retrieve.
	 * @return 0 in case of success.
	 */
	public int getFile (File f, PhoneElement el)
	{
		try
		{
			String rcvd;
			byte[] recvdB;
			do
			{
				// send file retrieve command
				send (("AT+KPSR=\"" + el.getID () + "\"\r").getBytes ());
				/*
				 * Receiving format:
				 * AT+KPSR="<ID repeated>"
				 * +KPSR: 49203
				 * CONNECT
				 * <binary data>
				 * NO CARRIER
				 */
				// receive file data
				recvdB = recv (null);
				rcvd = new String (recvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));

			// check file type
			Matcher photoMatcher = MainWindow.photoContentsPattern.matcher (rcvd);
			Matcher midiMatcher  = MainWindow.midiContentsPattern.matcher  (rcvd);
			Matcher amrMatcher   = MainWindow.amrContentsPattern.matcher   (rcvd);
			Matcher bmpMatcher   = MainWindow.bmpContentsPattern.matcher   (rcvd);
			Matcher gifMatcher   = MainWindow.gifContentsPattern.matcher   (rcvd);
			Matcher pngMatcher   = MainWindow.pngContentsPattern.matcher   (rcvd);
			Matcher wavMatcher   = MainWindow.wavContentsPattern.matcher   (rcvd);
			Matcher vCalMatcher  = MainWindow.vCalContentsPattern.matcher  (rcvd);
			Matcher vCardMatcher = MainWindow.vCardContentsPattern.matcher (rcvd);

			Matcher genMatcher = MainWindow.generalContentsPattern.matcher (rcvd);
			if ( photoMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				fos.write (recvdB, photoMatcher.start (1),
					photoMatcher.end (1) - photoMatcher.start (1));
				fos.close ();
			}
			else if ( midiMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, midiMatcher.start (1),
					midiMatcher.end (1) - midiMatcher.start (1));
				fos.close ();
			}
			else if ( amrMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, amrMatcher.start (1),
					amrMatcher.end (1) - amrMatcher.start (1));
				fos.close ();
			}
			else if ( wavMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, wavMatcher.start (1),
					wavMatcher.end (1) - wavMatcher.start (1));
				fos.close ();
			}
			else if ( bmpMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, bmpMatcher.start (1),
					bmpMatcher.end (1) - bmpMatcher.start (1));
				fos.close ();
			}
			else if ( gifMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, gifMatcher.start (1),
					gifMatcher.end (1) - gifMatcher.start (1));
				fos.close ();
			}
			else if ( pngMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, pngMatcher.start (1),
					pngMatcher.end (1) - pngMatcher.start (1));
				fos.close ();
			}
			else if ( vCalMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, vCalMatcher.start (1),
					vCalMatcher.end (1) - vCalMatcher.start (1));
				fos.close ();
			}
			else if ( vCardMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				//fos.write (midiMatcher.group (1).getBytes ());
				fos.write (recvdB, vCardMatcher.start (1),
					vCardMatcher.end (1) - vCardMatcher.start (1));
				fos.close ();
			}
			else if ( genMatcher.matches () )
			{
				FileOutputStream fos = new FileOutputStream (f);
				fos.write (recvdB, genMatcher.start (1),
					genMatcher.end (1) - genMatcher.start (1));
				fos.write (recvdB);
				fos.close ();
			}
		}
		catch ( Exception ex )
		{
			System.out.println (ex);
			ex.printStackTrace ();
			return -1;
		}
		return 0;
	}

	/**
	 * Gets the list of objects from the phone.
	 * @param ofWhat The type of the object, e.g. "PICTURES".
	 * @return A Vector containing information about the elements.
	 */
	public Vector<PhoneElement> getList (String ofWhat)
	{
		Vector<PhoneElement> res = new Vector<PhoneElement> (1);
		try
		{
			byte[] rcvdB;
			String rcvd = "";
			/*
			do
			{
				send ("ATZ\r".getBytes ());
				rcvd = recv ();

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			do
			{
				send ("AT115200\r".getBytes ());
				rcvd = recv ();

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));
			 */
			do
			{
				// set charset information
				send ("AT+CSCS=\"8859-1\"\r".getBytes ());
				rcvdB = recv (null);
				rcvd += new String(rcvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));

			if ( ! rcvd.contains ("OK") ) return res;
			do
			{
				// send "get list" command
				send (("AT+KPSL=\"" + ofWhat + "\",1\r").getBytes ());
			/*
			 * Receiving format:
			 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
			 * +KPSL: "53036500050220030045",0,48006,"RINGTONES","AMR","0000000069","","ASTEROID"
					Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
			 */
				rcvdB = recv (null);
				rcvd += new String(rcvdB);

				if ( rcvd.trim ().equals ("") )
				{
					reopen ();
				}
			} while (rcvd.trim ().equals (""));

			Matcher m;
			// split into lines
			String[] lines = rcvd.split ("[\\r\\n]{1,2}");
			for ( int i=0; i < lines.length; i++ )
			{
				m = MainWindow.listPattern.matcher (lines[i]);
				if ( m.matches () )
				{
					if ( m.group (2).equals ("0"))
						res.add (new PhoneElement (m.group (1),
							m.group (3), m.group (4) ));
				}
			}
		}
		catch ( Exception ex )
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
		return res;
	}

	/**
	 * Reopens the transmission port, saving its parameters.
	 */
	private void reopen ()
	{
		int bps = s.getBaudRate ();
		int dbits = s.getDataBits ();
		int parity = s.getParity ();
		int flow = s.getFlowControlMode ();
		int sbits = s.getStopBits ();
		try
		{
			s.close ();
			s = (SerialPort) portID.open ("JYMAG", 2000);
			inputStream  = s.getInputStream  ();
			outputStream = s.getOutputStream ();
			s.setSerialPortParams ( bps, dbits, sbits, parity );
			s.setFlowControlMode ( flow );

			s.addEventListener (this);
			s.notifyOnDataAvailable (true);
		}
		catch (Exception ex) {}
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
			send ("AT\r".getBytes ());
			send ("AT\r".getBytes ());
			send ("AT\r".getBytes ());
			String res = new String (recv (null));
			if ( res.contains ("OK") )
			{
				return 0;
			}
		}
		catch ( Exception ex )
		{
			System.out.println (ex);
			ex.printStackTrace ();
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
		System.arraycopy (orig, 0, ret, 0, orig.length);
		System.arraycopy (toAdd, 0, ret, orig.length, toAdd.length);
		return ret;
	}

	/**
	 * Used to receive events for the port.
	 * @param event A received port event.
	 */
	public void serialEvent (SerialPortEvent event)
	{
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
