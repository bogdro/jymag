/*
 * Utils.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.jymag;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
	public static final Pattern LIST_PATTERN;

	/**
	 * A Map containing all ID numbers connected to the given file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> FILE_TYPE_IDS;

	/**
	 * A Map containing ID numbers connected to the given photo file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> PHOTO_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given ringtone file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> RING_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given addressbook file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> ADDR_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given to-do file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> TODO_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given event file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> EVENT_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given animation/video file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> ANIM_FILE_IDS;

	/**
	 * A Map containing ID numbers connected to the given Java file
	 * extensions, used for file uploading.
	 */
	private static final Map<String, Integer> JAVA_FILE_IDS;

	/** An empty String. */
	public static final String EMPTY_STR = "";	// NOI18N
	/** A String with the Carriage Return character. */
	public static final String CR = "\r";		// NOI18N
	/** A String with the Line Feed character. */
	public static final String LF = "\n";		// NOI18N
	/** A single colon String. */
	public static final String COLON = ":";		// NOI18N
	/** A String with a single comma character. */
	public static final String COMMA = ",";		// NOI18N
	/** A single zero ("0") String. */
	public static final String ZERO = "0";		// NOI18N
	/** A single space String. */
	public static final String SPACE = " ";		// NOI18N
	/** A String with a single apostrophe character. */
	public static final String APOSTROPHE = "'";	// NOI18N
	/** A String with a single dot. */
	public static final String DOT = ".";		// NOI18N
	/** A single left parenthesis String. */
	public static final String L_PAREN = "(";	// NOI18N
	/** A single right parenthesis String. */
	public static final String R_PAREN = ")";	// NOI18N
	/** A single question-mark String. */
	public static final String QUESTION_MARK = "?";	// NOI18N
	/** A String with a single "double quote" character. */
	public static final String DQUOT = "\"";					// NOI18N

	private static final String DASH = "-";					// NOI18N
	private static final String MSG_START = ", Message='";			// NOI18N
	private static final String DATA_START = ", Data='";			// NOI18N
	private static final String EXCEPTION_AT_STR = "\tat\t";		// NOI18N
	private static final String UNKNOWN_CLASS = "<Unknown class>";		// NOI18N
	private static final String UNKNOWN_METHOD = "<Unknown method>";	// NOI18N
	private static final String UNKNOWN_FILE = "<Unknown file>";		// NOI18N

	private static final boolean DEBUG_EXCEPTIONS = true;

	static
	{
		/*
		 * List receiving format:
		 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
				Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
		 */
		LIST_PATTERN = Pattern.compile ("\\s*\\+KPSL:\\s*\\" + '"'	// NOI18N
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
		RING_FILE_IDS = Collections.unmodifiableMap (tempMap);

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
		PHOTO_FILE_IDS = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (3);
		tempMap.put  ("vcf"  , 220);	// NOI18N
		tempMap.put  ("vcard", 220);	// NOI18N
		tempMap.put  ("vcrd" , 220);	// NOI18N
		ADDR_FILE_IDS = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (5);
		tempMap.put  ("ics" , 221);		// NOI18N
		tempMap.put  ("ical", 221);		// NOI18N
		tempMap.put  ("ifb" , 221);		// NOI18N
		tempMap.put  ("icalendar", 221);	// NOI18N
		tempMap.put  ("vcs" , 221);		// NOI18N
		TODO_FILE_IDS = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (TODO_FILE_IDS.size ());
		tempMap.putAll (TODO_FILE_IDS);
		EVENT_FILE_IDS = Collections.unmodifiableMap (tempMap);

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
		ANIM_FILE_IDS = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (3);
		// UNCHECKED:
		tempMap.put  ("jar" , 1001);	// NOI18N
		tempMap.put  ("jad" , 1002);	// NOI18N
		tempMap.put  ("jam" , 1003);	// NOI18N
		JAVA_FILE_IDS = Collections.unmodifiableMap (tempMap);

		tempMap = new HashMap<String, Integer> (RING_FILE_IDS.size ()
			+ PHOTO_FILE_IDS.size () + ADDR_FILE_IDS.size ()
			+ TODO_FILE_IDS.size () + EVENT_FILE_IDS.size ()
			+ ANIM_FILE_IDS.size () + JAVA_FILE_IDS.size ());
		tempMap.putAll (PHOTO_FILE_IDS);
		tempMap.putAll (RING_FILE_IDS);
		tempMap.putAll (ADDR_FILE_IDS);
		tempMap.putAll (TODO_FILE_IDS);
		tempMap.putAll (EVENT_FILE_IDS);
		tempMap.putAll (ANIM_FILE_IDS);
		tempMap.putAll (JAVA_FILE_IDS);
		FILE_TYPE_IDS = Collections.unmodifiableMap (tempMap);
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

	private static void displayExceptionLine (String line, boolean newLine)
	{
		if ( line == null || (System.out == null && System.err == null) )
		{
			return;
		}
		if ( System.out != null )
		{
			System.out.print (line);
			if (newLine)
			{
				System.out.println ();
			}
			System.out.flush ();
		}
		if ( System.err != null )
		{
			System.err.print (line);
			if (newLine)
			{
				System.err.println ();
			}
			System.err.flush ();
		}
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

			String time = c.get (Calendar.YEAR) + DASH
				+ ((month  < 10)? ZERO : EMPTY_STR ) + month  + DASH
				+ ((day    < 10)? ZERO : EMPTY_STR ) + day    + SPACE
				+ ((hour   < 10)? ZERO : EMPTY_STR ) + hour   + COLON
				+ ((minute < 10)? ZERO : EMPTY_STR ) + minute + COLON
				+ ((second < 10)? ZERO : EMPTY_STR ) + second + COLON + SPACE;

			displayExceptionLine (time + ex, false);
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
				displayExceptionLine (MSG_START + msg + APOSTROPHE, false);
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
				displayExceptionLine (DATA_START + data.toString () + APOSTROPHE, false);
			}
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		try
		{
			displayExceptionLine (EMPTY_STR, true);
		}
		catch (Throwable e)
		{
			// ignore here to avoid recurrency
		}

		StackTraceElement[] ste = ex.getStackTrace ();
		if ( ste != null )
		{
			for ( int i = 0; i < ste.length; i++ )
			{
				if ( ste[i] != null )
				{
					try
					{
						String clazz = ste[i].getClassName ();
						String file = ste[i].getFileName ();
						String function = ste[i].getMethodName ();
						int line = ste[i].getLineNumber ();
						String toShow = EXCEPTION_AT_STR;
						if ( clazz != null )
						{
							// let's display only our files
							if ( ! DEBUG_EXCEPTIONS
								&& ! clazz.startsWith
								("bogdro") )	// NOI18N
							{
								continue;
							}
							toShow += clazz;
						}
						else
						{
							toShow += UNKNOWN_CLASS;
						}
						if ( function != null )
						{
							toShow += DOT + function;
						}
						else
						{
							toShow += DOT + UNKNOWN_METHOD;
						}
						if ( file != null )
						{
							toShow += SPACE + L_PAREN + file;
						}
						else
						{
							toShow += SPACE + L_PAREN + UNKNOWN_FILE;
						}
						toShow += COLON + String.valueOf (line) + R_PAREN;

						displayExceptionLine (toShow, true);
					}
					catch (Throwable e)
					{
						// ignore here to avoid recurrency
					}
				}
			}
		}
		handleException (ex.getCause(), null);
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
	 * This function joins 2 arrays of bytes together.
	 * @param orig The first array.
	 * @param toAdd The array to add.
	 * @return an array starting with "orig" followed by "toAdd".
	 */
	public static byte[] joinArrays (byte[] orig, byte[] toAdd)
	{
		if ( orig == null )
		{
			return toAdd;
		}
		if ( toAdd == null )
		{
			return orig;
		}
		byte[] ret = new byte[orig.length + toAdd.length];
		System.arraycopy (orig, 0, ret, 0, orig.length);
		System.arraycopy (toAdd, 0, ret, orig.length, toAdd.length);
		return ret;
	}

	/**
	 * Sleep for the given time, ignoring exceptions.
	 * @param millis the time to sleep for, in milliseconds.
	 */
	public static void sleepIgnoreException(long millis)
	{
		try
		{
			Thread.sleep (millis);
		}
		catch (InterruptedException ex)
		{
			// ignore
		}
	}

	/**
	 * Gets the filetypeIDs map.
	 * @return the filetypeIDs map.
	 */
	public static Map<String, Integer> getFiletypeIDs ()
	{
		return FILE_TYPE_IDS;
	}

	/**
	 * Gets the photofileIDs map.
	 * @return the photofileIDs map.
	 */
	public static Map<String, Integer> getPhotofileIDs ()
	{
		return PHOTO_FILE_IDS;
	}

	/**
	 * Gets the ringfileIDs map.
	 * @return the ringfileIDs map.
	 */
	public static Map<String, Integer> getRingfileIDs ()
	{
		return RING_FILE_IDS;
	}

	/**
	 * Gets the addrfileIDs map.
	 * @return the addrfileIDs map.
	 */
	public static Map<String, Integer> getAddrfileIDs ()
	{
		return ADDR_FILE_IDS;
	}

	/**
	 * Gets the todofileIDs map.
	 * @return the todofileIDs map.
	 */
	public static Map<String, Integer> getTodofileIDs ()
	{
		return TODO_FILE_IDS;
	}

	/**
	 * Gets the eventfileIDs map.
	 * @return the eventfileIDs map.
	 */
	public static Map<String, Integer> getEventfileIDs ()
	{
		return EVENT_FILE_IDS;
	}

	/**
	 * Gets the animfileIDs map.
	 * @return the animfileIDs map.
	 */
	public static Map<String, Integer> getAnimfileIDs ()
	{
		return ANIM_FILE_IDS;
	}

	/**
	 * Gets the javafileIDs map.
	 * @return the javafileIDs map.
	 */
	public static Map<String, Integer> getJavafileIDs ()
	{
		return JAVA_FILE_IDS;
	}
}
