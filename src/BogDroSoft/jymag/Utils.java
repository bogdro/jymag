/*
 * Utils.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

/**
 * A utility class, containing some useful methods and fields.
 * @author Bogdan Drozdowski
 */
public class Utils
{
	/**
	 * A pattern describing the format of lists of elements received from
	 * the phone.
	 */
	public final static Pattern listPattern;

	/**
	 * A Map containing all ID numbers connected to the given file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> filetypeIDs;

	/**
	 * A Map containing ID numbers connected to the given photo file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> photofileIDs;

	/**
	 * A Map containing ID numbers connected to the given ringtone file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> ringfileIDs;

	/**
	 * A Map containing ID numbers connected to the given addressbook file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> addrfileIDs;

	/**
	 * A Map containing ID numbers connected to the given to-do file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> todofileIDs;

	/**
	 * A Map containing ID numbers connected to the given event file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> eventfileIDs;

	/**
	 * A Map containing ID numbers connected to the given animation/video file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> animfileIDs;

	/**
	 * A Map containing ID numbers connected to the given Java file
	 * extensions, used for file uploading.
	 */
	private final static Map<String, Integer> javafileIDs;

	private static final String emptyStr = "";				// NOI18N
	private static final String dash = "-";					// NOI18N
	private static final String colon = ":";				// NOI18N
	private static final String comma = ",";				// NOI18N
	private static final String zero = "0";					// NOI18N
	private static final String space = " ";				// NOI18N
	private static final String apos = "'";					// NOI18N
	private static final String msgStart = ", Message='";			// NOI18N
	private static final String dataStart = ", Data='";			// NOI18N
	private static final String atStr = "\tat\t";				// NOI18N
	private static final String unknClass = "<Unknown class>";		// NOI18N
	private static final String dot = ".";					// NOI18N
	private static final String unknMethod = "<Unknown method>";		// NOI18N
	private static final String lParen = "(";				// NOI18N
	private static final String rParen = ")";				// NOI18N
	private static final String unknFile = "<Unknown file>";		// NOI18N
	private static final String allFileNames = "*.";			// NOI18N

	static
	{
		/*
		 * List receiving format:
		 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
				Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
		 */
		listPattern = Pattern.compile ("\\s*\\+KPSL:\\s*\\" + '"'	// NOI18N
				+ "([0-9A-Fa-f]+)" + "\\" + '"' + ",(\\d+),\\d+,\\" + '"'	// NOI18N
				+ "\\w+\\" + '"' + ",\\" + '"' + "(\\w+)\\"+ '"'	// NOI18N
				+ ",[^,]+,[^,]+,\\"+ '"' + "([^\"]+)\\" + '"');	// NOI18N

		Map<String, Integer> tempMap = new HashMap<String, Integer> (12);
		tempMap.put ("wav" ,   1);		// NOI18N
		tempMap.put ("mid" ,   2);		// NOI18N
		tempMap.put ("midi",   2);		// NOI18N
		//tempMap.put ("rmi" ,   2);// ?	// NOI18N
		//tempMap.put ("kar" ,   2);// ?	// NOI18N
		tempMap.put ("amr" ,  14);		// NOI18N
		// UNCHECKED:
		tempMap.put ("mp3" ,   3);		// NOI18N
		tempMap.put ("imy" ,   4);		// NOI18N
		tempMap.put ("asg1",   5);		// NOI18N
		tempMap.put ("asg2",   6);		// NOI18N
		tempMap.put ("mfi" ,  15);		// NOI18N
		tempMap.put ("aac" ,  17);		// NOI18N
		tempMap.put ("m4a" ,  17);		// NOI18N
		tempMap.put ("awb" ,  18);		// NOI18N
		ringfileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (16);
		tempMap.put ("wbmp", 101);		// NOI18N
		tempMap.put ("bmp" , 102);		// NOI18N
		tempMap.put ("png" , 103);		// NOI18N
		tempMap.put ("jpg" , 104);		// NOI18N
		tempMap.put ("jpeg", 104);		// NOI18N
		tempMap.put ("jpe" , 104);		// NOI18N
		tempMap.put ("jif" , 104);		// NOI18N
		tempMap.put ("gif" , 105);		// NOI18N
		// UNCHECKED:
		tempMap.put ("tif" , 106);		// NOI18N
		tempMap.put ("tiff", 106);		// NOI18N
		tempMap.put ("pct" , 107);		// NOI18N
		tempMap.put ("pict", 107);		// NOI18N
		tempMap.put ("ai"  , 108);		// NOI18N
		tempMap.put ("eps" , 108);		// NOI18N
		tempMap.put ("ps"  , 108);		// NOI18N
		tempMap.put ("ems_gr", 109 );	// NOI18N
		photofileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (3);
		tempMap.put  ("vcf"  , 220);	// NOI18N
		tempMap.put  ("vcard", 220);	// NOI18N
		tempMap.put  ("vcrd" , 220);	// NOI18N
		addrfileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (5);
		tempMap.put  ("ics" , 221);		// NOI18N
		tempMap.put  ("ical", 221);		// NOI18N
		tempMap.put  ("ifb" , 221);		// NOI18N
		tempMap.put  ("icalendar", 221);	// NOI18N
		tempMap.put  ("vcs" , 221);		// NOI18N
		todofileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (todofileIDs.size ());
		tempMap.putAll (todofileIDs);
		eventfileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (15);
		tempMap.put  ("gif" , 105);		// NOI18N
		tempMap.put  ("mng" , 202);		// NOI18N
		// UNCHECKED:
		tempMap.put  ("sg1" , 203);		// NOI18N
		tempMap.put  ("sg2" , 204);		// NOI18N
		tempMap.put  ("ems_an", 205);	// NOI18N
		tempMap.put  ("ssa" , 206);		// NOI18N
		tempMap.put  ("mjpg", 207);		// NOI18N
		tempMap.put  ("mjpeg",207);		// NOI18N
		tempMap.put  ("avi" , 231);		// NOI18N
		tempMap.put  ("mp4" , 232);		// NOI18N
		tempMap.put  ("mpeg", 232);		// NOI18N
		tempMap.put  ("mpg" , 232);		// NOI18N
		tempMap.put  ("3gp" , 233);		// NOI18N
		tempMap.put  ("3gpp", 233);		// NOI18N
		tempMap.put  ("3g2" , 233);		// NOI18N
		animfileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (3);
		// UNCHECKED:
		tempMap.put  ("jar" , 1001);	// NOI18N
		tempMap.put  ("jad" , 1002);	// NOI18N
		tempMap.put  ("jam" , 1003);	// NOI18N
		javafileIDs = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (ringfileIDs.size ()
			+ photofileIDs.size () + addrfileIDs.size ()
			+ todofileIDs.size () + eventfileIDs.size ()
			+ animfileIDs.size () + javafileIDs.size ());
		tempMap.putAll (photofileIDs);
		tempMap.putAll (ringfileIDs);
		tempMap.putAll (addrfileIDs);
		tempMap.putAll (todofileIDs);
		tempMap.putAll (eventfileIDs);
		tempMap.putAll (animfileIDs);
		tempMap.putAll (javafileIDs);
		filetypeIDs = Collections.unmodifiableMap (tempMap);
	}

	private Utils ()
	{
		// non-instantiable
	}

	public static int convertCalendarMonthToReal (int month)
	{
		if ( month == Calendar.JANUARY )
		{
			return 1;
		}
		else if ( month == Calendar.FEBRUARY )
		{
			return 2;
		}
		else if ( month == Calendar.MARCH )
		{
			return 3;
		}
		else if ( month == Calendar.APRIL )
		{
			return 4;
		}
		else if ( month == Calendar.MAY )
		{
			return 5;
		}
		else if ( month == Calendar.JUNE )
		{
			return 6;
		}
		else if ( month == Calendar.JULY )
		{
			return 7;
		}
		else if ( month == Calendar.AUGUST )
		{
			return 8;
		}
		else if ( month == Calendar.SEPTEMBER )
		{
			return 9;
		}
		else if ( month == Calendar.OCTOBER )
		{
			return 10;
		}
		else if ( month == Calendar.NOVEMBER )
		{
			return 11;
		}
		else if ( month == Calendar.DECEMBER )
		{
			return 12;
		}
		return -1;
	}

	public static int convertRealMonthToCalendar (int month)
	{
		if ( month == 1 )
		{
			return Calendar.JANUARY;
		}
		else if ( month == 2 )
		{
			return Calendar.FEBRUARY;
		}
		else if ( month == 3 )
		{
			return Calendar.MARCH;
		}
		else if ( month == 4 )
		{
			return Calendar.APRIL;
		}
		else if ( month == 5 )
		{
			return Calendar.MAY;
		}
		else if ( month == 6 )
		{
			return Calendar.JUNE;
		}
		else if ( month == 7 )
		{
			return Calendar.JULY;
		}
		else if ( month == 8 )
		{
			return Calendar.AUGUST;
		}
		else if ( month == 9 )
		{
			return Calendar.SEPTEMBER;
		}
		else if ( month == 10 )
		{
			return Calendar.OCTOBER;
		}
		else if ( month == 11 )
		{
			return Calendar.NOVEMBER;
		}
		else if ( month == 12 )
		{
			return Calendar.DECEMBER;
		}
		return -1;
	}

	/**
	 * Displays all the important information about exceptions.
	 * @param ex The exception to display.
	 * @param data Any additional data to display.
	 */
	public synchronized static void handleException (Throwable ex, Object data)
	{
		if ( ex == null || (System.out == null && System.err == null) )
		{
			return;
		}
		try
		{
			Calendar c = Calendar.getInstance ();

			int month  = convertCalendarMonthToReal (c.get (Calendar.MONTH));
			int day    = c.get (Calendar.DAY_OF_MONTH);
			int hour   = c.get (Calendar.HOUR_OF_DAY);
			int minute = c.get (Calendar.MINUTE);
			int second = c.get (Calendar.SECOND);

			String time = c.get (Calendar.YEAR) + dash
				+ ((month<10)?  zero : emptyStr ) + month  + dash
				+ ((day<10)?    zero : emptyStr ) + day    + space
				+ ((hour<10)?   zero : emptyStr ) + hour   + colon
				+ ((minute<10)? zero : emptyStr ) + minute + colon
				+ ((second<10)? zero : emptyStr ) + second + colon + space;

			if ( System.out != null )
			{
				System.out.print (time + ex);
				System.out.flush ();
			}
			if ( System.err != null )
			{
				System.err.print (time + ex);
				System.err.flush ();
			}
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		try
		{
			String msg = ex.getMessage ();
			if ( msg != null )
			{
				if ( System.out != null )
				{
					System.out.print (msgStart + msg + apos);
					System.out.flush ();
				}
				if ( System.err != null )
				{
					System.err.print (msgStart + msg + apos);
					System.err.flush ();
				}
			}
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		try
		{
			if ( data != null )
			{
				if ( System.out != null )
				{
					System.out.print (dataStart + data.toString () + apos);
					System.out.flush ();
				}
				if ( System.err != null )
				{
					System.err.print (dataStart + data.toString () + apos);
					System.err.flush ();
				}
			}
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		try
		{
			if ( System.out != null )
			{
				System.out.println ();
				System.out.flush ();
			}
			if ( System.err != null )
			{
				System.err.println ();
				System.err.flush ();
			}
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		StackTraceElement[] ste = ex.getStackTrace ();
		if ( ste != null )
		{
			for ( int i=0; i < ste.length; i++ )
			{
				if ( ste[i] != null )
				{
					try
					{
						String clazz = ste[i].getClassName ();
						String file = ste[i].getFileName ();
						String function = ste[i].getMethodName ();
						int line = ste[i].getLineNumber ();
						String toShow = atStr;
						if ( clazz != null )
						{
							// let's display only our files
							if ( ! clazz.startsWith
								("BogDro") )	// NOI18N
							{
								continue;
							}
							toShow += clazz;
						}
						else
						{
							toShow += unknClass;
						}
						if ( function != null )
						{
							toShow += dot + function;
						}
						else
						{
							toShow += dot + unknMethod;
						}
						if ( file != null )
						{
							toShow += space + lParen + file;
						}
						else
						{
							toShow += space + lParen + unknFile;
						}
						toShow += colon + String.valueOf (line) + rParen;

						if ( System.out != null )
						{
							System.out.println (toShow);
							System.out.flush ();
						}
						if ( System.err != null )
						{
							System.err.println (toShow);
							System.err.flush ();
						}
					}
					catch (Throwable e)
					{
						// ignore here to avoid recurrency
					}
				}
			}
		}
	}

	/**
	 * Method used to change the GUI - runs r.run () on the
	 * EventDispatchThread and waits for it to exit.
	 * @param r The code to run.
	 */
	public synchronized static void changeGUI (final Runnable r)
	{
		if ( r == null )
		{
			return;
		}
		if ( SwingUtilities.isEventDispatchThread () )
		{
			try
			{
				r.run ();
			}
			catch (Throwable ex)
			{
				Utils.handleException (ex, "changeGUI->r.run");	// NOI18N
			}
		}
		else
		{
			try
			{
				SwingUtilities.invokeAndWait (new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						try
						{
							r.run ();
						}
						catch (Throwable ex)
						{
							Utils.handleException (ex,
								"changeGUI->invokeAndWait->r.run");// NOI18N
						}
					}
				});
			}
			catch (InterruptedException ex)
			{
				// can be called when closing the program, so ignore
				//Utils.handleException (ex, "changeGUI->invokeAndWait->Interrupt");// NOI18N
			}
			catch (Throwable ex)
			{
				Utils.handleException (ex, "changeGUI->invokeAndWait");	// NOI18N
			}
		}
	}

	/**
	 * Set the given font size in all the components in the given
	 * Component (recursively, if it's a Container).
	 * @param c The Component with Components that will have their font
	 *	size changed.
	 * @param newSize The new font size to set.
	 */
	public static void setFontSize (Component c, float newSize)
	{
		if ( c == null )
		{
			return;
		}
		c.setFont (c.getFont ().deriveFont (newSize));
		if ( c instanceof Container )
		{
			Component[] subComps = ((Container)c).getComponents ();
			if ( subComps != null )
			{
				for ( int i = 0; i < subComps.length; i++ )
				{
					if ( subComps[i] != null )
					{
						setFontSize (subComps[i], newSize);
					}
				}
			}
		}
	}

	/**
	 * Tells whether the given integer is a valid speed value.
	 * @param speed The value to test.
	 * @return true if it is.
	 */
	public static boolean isAllowableSpeed (int speed)
	{
		return
			speed == 1200 ||
			speed == 2400 ||
			speed == 4800 ||
			speed == 9600 ||
			speed == 19200 ||
			speed == 38400 ||
			speed == 57600 ||
			speed == 115200 ||
			speed == 230400 ||
			speed == 460800 ||
			speed == 500000 ||
			speed == 576000 ||
			speed == 921600 ||
			speed == 1000000 ||
			speed == 1152000 ||
			speed == 1500000 ||
			speed == 2000000 ||
			speed == 2500000 ||
			speed == 3000000 ||
			speed == 3500000 ||
			speed == 4000000;
	}

	/**
	 * Tells whether the given integer is a valid data bits' value.
	 * @param dBits The value to test.
	 * @return true if it is.
	 */
	public static boolean isAllowableDataBits (int dBits)
	{
		return
			dBits == 5 ||
			dBits == 6 ||
			dBits == 7 ||
			dBits == 8;
	}

	/**
	 * Redirects the standard error output to a log file.
	 * @param filename The name of the file to redirect the output to.
	 * @return The filename the output was actually redirected to.
	 */
	public static String redirectStderrToFile (String filename)
	{
		if ( filename == null )
		{
			System.err.close ();
			return null;
		}
		try
		{
			// don't force any encodings, because the translated messages may
			// be in another encoding
			System.setErr (new PrintStream (new File (filename)));
		}
		catch (Exception ex)
		{
			String dirSep = null;
			try
			{
				dirSep = System.getProperty ("file.separator", "/");	// NOI18N
			} catch (Exception e) {}
			if ( dirSep == null )
			{
				dirSep = File.separator;
			}
			if ( dirSep == null )
			{
				dirSep = "/";	// NOI18N
			}
			String[] dirs = new String[7];
			try
			{
				dirs[0] = System.getProperty ("user.dir");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[1] = System.getProperty ("user.home");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[2] = System.getenv ("HOME");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[3] = System.getenv ("TMP");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[4] = System.getenv ("TEMP");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[5] = System.getenv ("TMPDIR");	// NOI18N
			} catch (Exception e) {}
			try
			{
				dirs[6] = System.getenv ("TEMPDIR");	// NOI18N
			} catch (Exception e) {}
			int i;
			for ( i = 0; i < dirs.length; i++ )
			{
				if ( dirs[i] == null )
				{
					continue;
				}
				if ( dirs[i].isEmpty () )
				{
					continue;
				}
				try
				{
					// don't force any encodings, because the translated messages may
					// be in another encoding
					System.setErr (new PrintStream (new File (
						dirs[i] + dirSep + filename)));
					filename = dirs[i] + dirSep + filename;
					break;
				}
				catch (Exception e) {}
			}
			if ( i == dirs.length )
			{
				Utils.handleException (ex, "stderr");	// NOI18N
			}
		}
		return filename;
	}

	/**
	 * Crates a file chooser for opening single files of the given type.
	 * @param description The description to display in the filters list.
	 * @param filetype The Map with file extensiotns as keys.
	 * @return The file chooser for opening of selected file types.
	 */
	public static JFileChooser createOpenFileChooser (
		final String description, final Map<String, Integer> filetype)
	{
		JFileChooser fc = new JFileChooser ();
		fc.setAcceptAllFileFilterUsed (false);
		fc.setMultiSelectionEnabled (false);
		fc.setDialogType (JFileChooser.OPEN_DIALOG);
		fc.setFileFilter (new FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () )
				{
					return true;
				}
				String name = f.getName ();
				if ( name == null )
				{
					return false;
				}
				if ( name.contains (dot) && filetype != null )
				{
					if ( filetype.containsKey (name.substring
						(name.lastIndexOf (dot)+1)
						.toLowerCase (Locale.ENGLISH)))
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				StringBuilder desc = new StringBuilder (200);
				if ( description != null )
				{
					desc.append (description);
				}
				if ( filetype != null )
				{
					Iterator<String> keys = filetype.keySet ().iterator ();
					if ( keys != null )
					{
						desc.append (space + lParen);
						while ( keys.hasNext () )
						{
							desc.append (allFileNames + keys.next ());
							if ( keys.hasNext () )
							{
								desc.append (comma + space);
							}
						}
						desc.append (rParen);
					}
				}
				return desc.toString ();
			}
		});
		return fc;
	}

	/**
	 * Called when the programs needs to close.
	 * @param filename The name of the log file.
	 * @param retval Return value passed to System.exit ().
	 */
	public static void closeProgram (String filename, int retval)
	{
		// close logging
		if ( System.err != null )
		{
			System.err.close ();
		}
		if ( filename != null )
		{
			// remove the log file if empty
			File log = new File (filename);
			if ( log.exists () && log.length() == 0 )
			{
				if ( (! log.delete ()) && retval == 0 )
				{
					retval = 1;
				}
			}
		}
		System.exit (retval);
	}

	private static final Color greenStatusColour = new Color (0, 204, 0)/*Color.GREEN*/;

	/**
	 * Sets the given status label.
	 * @param status The label to set.
	 * @param s The status to set on the label.
	 */
	public static void updateStatusLabel (final JLabel status, final STATUS s)
	{
		Utils.changeGUI (new Runnable ()
		{
			@Override
			public synchronized void run ()
			{
				if ( status == null || s == null )
				{
					return;
				}
				status.setText (s.toString ());
				if ( s.equals (STATUS.READY) )
				{
					status.setForeground (greenStatusColour);
				}
				else
				{
					status.setForeground (Color.BLUE);
				}
				status.validate ();
			}
		});
	}

	/**
	 * A sample uncaught-exception handler instance for threads.
	 */
	public static final UncExHndlr handler = new UncExHndlr ();

	/**
	 * A sample uncaught-exception handler class for threads.
	 */
	public static class UncExHndlr implements Thread.UncaughtExceptionHandler
	{
		/**
		 * Called when an uncaught exception occurrs.
		 * @param t The thread, in which the exception occurred.
		 * @param ex The exception that occurred.
		 */
		@Override
		public void uncaughtException (Thread t, Throwable ex)
		{
			try
			{
				handleException (ex, "Utils.UncaughtExceptionHandler: Thread="	// NOI18N
					+ ((t != null)? t.getName() : "?"));	// NOI18N
			} catch (Throwable th) {}
		}
	}

	/**
	 * A class that selects all rows of a JTable when its header is clicked.
	 */
	public static class TableMouseListener extends MouseAdapter
	{
		private JTable table;

		/**
		 * Creates a new TableMouseListener for the given JTable.
		 * @param datatable The JTable to select on mouse click.
		 */
		public TableMouseListener (JTable datatable)
		{
			table = datatable;
		}

		@Override
		public void mouseClicked (MouseEvent me)
		{
			if ( me.getButton () == MouseEvent.BUTTON1 )
			{
				if ( table != null )
				{
					table.selectAll ();
				}
			}
		}
	}

	/**
	 * The enumeration of possible program states.
	 */
	public static enum STATUS
	{
		/** The READY state. */
		READY,
		/** The SENDING state. */
		SENDING,
		/** The RECEIVING state. */
		RECEIVING;

		private static final ResourceBundle rcBundle =
			ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");	// NOI18N

		private static final String readyString =
			rcBundle.getString("READY");		// NOI18N

		private static final String sendingString =
			rcBundle.getString("SENDING");		// NOI18N

		private static final String recvString =
			rcBundle.getString("RECEIVING");	// NOI18N

		@Override
		public String toString ()
		{
			if ( this.equals (STATUS.READY) )
			{
				return readyString;
			}
			else if ( this.equals (STATUS.SENDING) )
			{
				return sendingString;
			}
			else if ( this.equals (STATUS.RECEIVING) )
			{
				return recvString;
			}
			return emptyStr;
		}
	}

	/**
	 * A class that closes the window when the Esc key is pressed. Must
	 * be registered on all elements of the window that are desired to
	 * catch this key.
	 */
	public static class EscKeyListener extends KeyAdapter
	{
		private Window frame; // need Window for dispose().

		/**
		 * Creates an EscKeyListener for the given Window.
		 * @param wFrame The frame to connect this listener to. All
		 *	of the frame's Components will have this listener
		 *	installed, too.
		 */
		public EscKeyListener (Window wFrame)
		{
			frame = wFrame;
			addKeyListeners (frame);
		}

		/**
		 * Recursively adds this keylistener to the given Component
		 *	and all its subcomponents.
		 * @param c The Component with Components that will have this keylistener.
		 */
		private void addKeyListeners (Component c)
		{
			if ( c == null )
			{
				return;
			}
			c.addKeyListener (this);
			if ( c instanceof Container )
			{
				Component[] subComps = ((Container)c).getComponents ();
				if ( subComps != null )
				{
					for ( int i = 0; i < subComps.length; i++ )
					{
						if ( subComps[i] != null )
						{
							addKeyListeners (subComps[i]);
						}
					}
				}
			}
		}

		/**
		 * Receives key-typed events (called when the user types a key).
		 * @param ke The key-typed event.
		 */
		@Override
		public void keyTyped (KeyEvent ke)
		{
			if ( ke == null || frame == null )
			{
				return;
			}
			if ( ke.getKeyChar () == KeyEvent.VK_ESCAPE )
			{
				WindowListener[] listeners = frame.getWindowListeners ();
				if ( listeners != null )
				{
					for ( int i = 0; i < listeners.length; i++ )
					{
						if ( listeners[i] == null )
						{
							continue;
						}
						listeners[i].windowClosing (null);
					}
				}
				frame.dispose ();
			}
		}
	}

	/**
	 * Gets the filetypeIDs map.
	 * @return the filetypeIDs map.
	 */
	public static Map<String, Integer> getFiletypeIDs ()
	{
		return filetypeIDs;
	}

	/**
	 * Gets the photofileIDs map.
	 * @return the photofileIDs map.
	 */
	public static Map<String, Integer> getPhotofileIDs ()
	{
		return photofileIDs;
	}

	/**
	 * Gets the ringfileIDs map.
	 * @return the ringfileIDs map.
	 */
	public static Map<String, Integer> getRingfileIDs ()
	{
		return ringfileIDs;
	}

	/**
	 * Gets the addrfileIDs map.
	 * @return the addrfileIDs map.
	 */
	public static Map<String, Integer> getAddrfileIDs ()
	{
		return addrfileIDs;
	}

	/**
	 * Gets the todofileIDs map.
	 * @return the todofileIDs map.
	 */
	public static Map<String, Integer> getTodofileIDs ()
	{
		return todofileIDs;
	}

	/**
	 * Gets the eventfileIDs map.
	 * @return the eventfileIDs map.
	 */
	public static Map<String, Integer> getEventfileIDs ()
	{
		return eventfileIDs;
	}

	/**
	 * Gets the animfileIDs map.
	 * @return the animfileIDs map.
	 */
	public static Map<String, Integer> getAnimfileIDs ()
	{
		return animfileIDs;
	}

	/**
	 * Gets the javafileIDs map.
	 * @return the javafileIDs map.
	 */
	public static Map<String, Integer> getJavafileIDs ()
	{
		return javafileIDs;
	}
}
