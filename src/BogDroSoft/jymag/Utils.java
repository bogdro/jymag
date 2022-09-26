/*
 * Utils.java, part of the JYMAG package.
 *
 * Copyright (C) 2008 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.util.Calendar;
import java.util.Hashtable;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;


/**
 * A utility class, containing some useful methods and fields.
 * This is certainly NOT the place for keyListeners, because they
 * wouldn't know which window to close.
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
	 * A Hashtable containing all ID numbers connected to the given file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> filetypeIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given photo file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> photofileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given ringtone file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> ringfileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given addressbook file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> addrfileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given to-do file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> todofileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given event file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> eventfileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given animation/video file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> animfileIDs;

	/**
	 * A Hashtable containing ID numbers connected to the given Java file
	 * extensions, used for file uploading.
	 */
	public final static Hashtable<String, Integer> javafileIDs;

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

		ringfileIDs = new Hashtable<String, Integer> (11);
		ringfileIDs.put ("wav" ,   1);		// NOI18N
		ringfileIDs.put ("mid" ,   2);		// NOI18N
		ringfileIDs.put ("midi",   2);		// NOI18N
		//ringfileIDs.put ("rmi" ,   2);// ?	// NOI18N
		//ringfileIDs.put ("kar" ,   2);// ?	// NOI18N
		ringfileIDs.put ("amr" ,  14);		// NOI18N
		// UNCHECKED:
		ringfileIDs.put ("mp3" ,   3);		// NOI18N
		ringfileIDs.put ("imy" ,   4);		// NOI18N
		ringfileIDs.put ("asg1",   5);		// NOI18N
		ringfileIDs.put ("asg2",   6);		// NOI18N
		ringfileIDs.put ("mfi" ,  15);		// NOI18N
		ringfileIDs.put ("aac" ,  17);		// NOI18N
		ringfileIDs.put ("m4a" ,  17);		// NOI18N
		ringfileIDs.put ("awb" ,  18);		// NOI18N

		photofileIDs = new Hashtable<String, Integer> (16);
		photofileIDs.put ("wbmp", 101);		// NOI18N
		photofileIDs.put ("bmp" , 102);		// NOI18N
		photofileIDs.put ("png" , 103);		// NOI18N
		photofileIDs.put ("jpg" , 104);		// NOI18N
		photofileIDs.put ("jpeg", 104);		// NOI18N
		photofileIDs.put ("jpe" , 104);		// NOI18N
		photofileIDs.put ("jif" , 104);		// NOI18N
		photofileIDs.put ("gif" , 105);		// NOI18N
		// UNCHECKED:
		photofileIDs.put ("tif" , 106);		// NOI18N
		photofileIDs.put ("tiff", 106);		// NOI18N
		photofileIDs.put ("pct" , 107);		// NOI18N
		photofileIDs.put ("pict", 107);		// NOI18N
		photofileIDs.put ("ai"  , 108);		// NOI18N
		photofileIDs.put ("eps" , 108);		// NOI18N
		photofileIDs.put ("ps"  , 108);		// NOI18N
		photofileIDs.put ("ems_gr", 109 );	// NOI18N

		addrfileIDs = new Hashtable<String, Integer> (3);
		addrfileIDs.put  ("vcf"  , 220);	// NOI18N
		addrfileIDs.put  ("vcard", 220);	// NOI18N
		addrfileIDs.put  ("vcrd" , 220);	// NOI18N

		todofileIDs = new Hashtable<String, Integer> (5);
		todofileIDs.put  ("ics" , 221);		// NOI18N
		todofileIDs.put  ("ical", 221);		// NOI18N
		todofileIDs.put  ("ifb" , 221);		// NOI18N
		todofileIDs.put  ("icalendar", 221);	// NOI18N
		todofileIDs.put  ("vcs" , 221);		// NOI18N

		eventfileIDs = new Hashtable<String, Integer> (todofileIDs.size ());
		eventfileIDs.putAll (todofileIDs);

		animfileIDs = new Hashtable<String, Integer> (15);
		animfileIDs.put  ("gif" , 105);		// NOI18N
		animfileIDs.put  ("mng" , 202);		// NOI18N
		// UNCHECKED:
		animfileIDs.put  ("sg1" , 203);		// NOI18N
		animfileIDs.put  ("sg2" , 204);		// NOI18N
		animfileIDs.put  ("ems_an", 205);	// NOI18N
		animfileIDs.put  ("ssa" , 206);		// NOI18N
		animfileIDs.put  ("mjpg", 207);		// NOI18N
		animfileIDs.put  ("mjpeg",207);		// NOI18N
		animfileIDs.put  ("avi" , 231);		// NOI18N
		animfileIDs.put  ("mp4" , 232);		// NOI18N
		animfileIDs.put  ("mpeg", 232);		// NOI18N
		animfileIDs.put  ("mpg" , 232);		// NOI18N
		animfileIDs.put  ("3gp" , 233);		// NOI18N
		animfileIDs.put  ("3gpp", 233);		// NOI18N
		animfileIDs.put  ("3g2" , 233);		// NOI18N

		javafileIDs = new Hashtable<String, Integer> (3);
		// UNCHECKED:
		javafileIDs.put  ("jar" , 1001);	// NOI18N
		javafileIDs.put  ("jad" , 1002);	// NOI18N
		javafileIDs.put  ("jam" , 1003);	// NOI18N

		filetypeIDs = new Hashtable<String, Integer> (ringfileIDs.size ()
			+ photofileIDs.size () + addrfileIDs.size ()
			+ todofileIDs.size () + eventfileIDs.size ()
			+ animfileIDs.size () + javafileIDs.size ());
		filetypeIDs.putAll (photofileIDs);
		filetypeIDs.putAll (ringfileIDs);
		filetypeIDs.putAll (addrfileIDs);
		filetypeIDs.putAll (todofileIDs);
		filetypeIDs.putAll (eventfileIDs);
		filetypeIDs.putAll (animfileIDs);
		filetypeIDs.putAll (javafileIDs);
	}

	// non-instantiable
	private Utils () {}

	/**
	 * Displays all the important information about exceptions.
	 * @param ex The exception to display.
	 * @param data Any additional data to display.
	 */
	public static void handleException (Throwable ex, Object data)
	{
		if ( ex == null || (System.out == null && System.err == null) ) return;
		try
		{
			Calendar c = Calendar.getInstance ();

			int month  = c.get (Calendar.MONTH);
			int day    = c.get (Calendar.DAY_OF_MONTH);
			int hour   = c.get (Calendar.HOUR_OF_DAY);
			int minute = c.get (Calendar.MINUTE);
			int second = c.get (Calendar.SECOND);
			     if ( month == Calendar.JANUARY   ) month =  1;
			else if ( month == Calendar.FEBRUARY  ) month =  2;
			else if ( month == Calendar.MARCH     ) month =  3;
			else if ( month == Calendar.APRIL     ) month =  4;
			else if ( month == Calendar.MAY       ) month =  5;
			else if ( month == Calendar.JUNE      ) month =  6;
			else if ( month == Calendar.JULY      ) month =  7;
			else if ( month == Calendar.AUGUST    ) month =  8;
			else if ( month == Calendar.SEPTEMBER ) month =  9;
			else if ( month == Calendar.OCTOBER   ) month = 10;
			else if ( month == Calendar.NOVEMBER  ) month = 11;
			else if ( month == Calendar.DECEMBER  ) month = 12;

			String time = c.get (Calendar.YEAR) + "-"		// NOI18N
				+ ((month<10)?  "0" : "" ) + month  + "-"	// NOI18N
				+ ((day<10)?    "0" : "" ) + day    + " "	// NOI18N
				+ ((hour<10)?   "0" : "" ) + hour   + ":"	// NOI18N
				+ ((minute<10)? "0" : "" ) + minute + ":"	// NOI18N
				+ ((second<10)? "0" : "" ) + second + ": ";	// NOI18N

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

		} catch (Throwable e) {}

		try
		{
			String msg = ex.getMessage ();
			if ( msg != null )
			{
				if ( System.out != null )
				{
					System.out.print (", Message='" + msg + "'");	// NOI18N
					System.out.flush ();
				}
				if ( System.err != null )
				{
					System.err.print (", Message='" + msg + "'");	// NOI18N
					System.err.flush ();
				}
			}
		} catch (Throwable e) {}

		try
		{
			if ( data != null )
			{
				if ( System.out != null )
				{
					System.out.print (", Data='" + data.toString () + "'");	// NOI18N
					System.out.flush ();
				}
				if ( System.err != null )
				{
					System.err.print (", Data='" + data.toString () + "'");	// NOI18N
					System.err.flush ();
				}
			}
		} catch (Throwable e) {}

		try
		{
			if ( System.out != null )
			{
				System.out.println ();	// NOI18N
				System.out.flush ();
			}
			if ( System.err != null )
			{
				System.err.println ();	// NOI18N
				System.err.flush ();
			}
		} catch (Throwable e) {}

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
						String toShow = "\tat\t";	// NOI18N
						if ( clazz != null )
						{
							// let's display only our files
							if ( ! clazz.startsWith
								(Utils.class.getPackage ().getName ()) )
							{
								continue;
							}
							toShow += clazz;
						}
						else
						{
							toShow += "<Unknown class>";	// NOI18N
						}
						if ( function != null )
						{
							toShow += "." + function;	// NOI18N
						}
						else
						{
							toShow += ".<Unknown method>";	// NOI18N
						}
						if ( file != null )
						{
							toShow += " (" + file;	// NOI18N
						}
						else
						{
							toShow += " (<Unknown file>";	// NOI18N
						}
						toShow += ":" + String.valueOf (line) + ")";	// NOI18N

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
					} catch (Throwable e) {}
				}
			}
		}
        }

	/**
	 * Method used to change the GUI - runs r.run () on the
	 * EventDispatchThread and waits for it to exit.
	 * @param r The code to run.
	 */
	public static void changeGUI (final Runnable r)
	{
		if ( r == null ) return;
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
					public void run ()
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
	 * Tells whether the given integer is a valid speed value.
	 * @param speed The value to test.
	 * @return true if it is.
	 */
	public static boolean isAllowableSpeed (int speed)
	{
		if ( speed != 1200
			&& speed != 2400
			&& speed != 4800
			&& speed != 9600
			&& speed != 19200
			&& speed != 38400
			&& speed != 57600
			&& speed != 115200
			&& speed != 230400
			&& speed != 460800
			&& speed != 500000
			&& speed != 576000
			&& speed != 921600
			&& speed != 1000000
			&& speed != 1152000
			&& speed != 1500000
			&& speed != 2000000
			&& speed != 2500000
			&& speed != 3000000
			&& speed != 3500000
			&& speed != 4000000 ) return false;

		return true;
	}

	/**
	 * Tells whether the given integer is a valid data bits' value.
	 * @param dBits The value to test.
	 * @return true if it is.
	 */
	public static boolean isAllowableDataBits (int dBits)
	{
		if ( dBits != 5 && dBits != 6
			&& dBits != 7 && dBits != 8 ) return false;
		return true;
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
}
