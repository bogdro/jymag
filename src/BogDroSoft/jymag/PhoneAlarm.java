/*
 * PhoneAlarm.java, part of the JYMAG package.
 *
 * Copyright (C) 2010-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an alarm in the phone.
 * @author Bogdan Drozdowski
 */
public class PhoneAlarm
{
	private Calendar time;
	private boolean oneTime;
	private boolean forAllDays;
	private Set<Integer> days;
	private int number;

	// alarm recurrences could also show up here
	private static final Pattern datetimePattern
		= Pattern.compile ("(\\+CALA:)?(\\s*)?\"?(\\d{2})/(\\d{2})/(\\d{2}),"		// NOI18N
			+ "(\\d{2}):(\\d{2}):(\\d{2})\"?(\\s*,(\\d+))?(\\s*,\""			// NOI18N
			+ "(\\d)((,\\d))+\")?\\s*.*",						// NOI18N
			Pattern.CASE_INSENSITIVE);

	private static final Pattern timePattern
		= Pattern.compile ("(\\+CALA:)?(\\s*)?\"?(\\d{2}):(\\d{2}):(\\d{2})\"?"		// NOI18N
			+ "(\\s*,(\\d+))?(\\s*,\"(\\d)((,\\d)+)\")?\\s*.*",			// NOI18N
			Pattern.CASE_INSENSITIVE);

	private static final String dQuote = "\"";		// NOI18N
	private static final String comma = ",";		// NOI18N
	private static final String slash = "/";		// NOI18N
	private static final String colon = ":";		// NOI18N
	private static final String zero = "0";			// NOI18N
	private static final String empty = "";			// NOI18N

	private static final String toStringStart = "PhoneAlarm[";				// NOI18N
	private static final String toStringEnd = "]";						// NOI18N
	private static final String toStringID = "ID=";						// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String toStringDateTime = "";					// NOI18N
	private static final String toStringDays = "";						// NOI18N

	private static final SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yy,HH:mm:ss");

	private static final Integer ALL_DAYS = Integer.valueOf (0);

	/**
	 * Creates a new instance of PhoneAlarm.
	 * @param alarmTime The time to start the alarm.
	 * @param isOneTimeAlarm Is the alarm one-time (include the date and the time)
	 *	or repetitive (include only the time).
	 * @param isForAllDays Is the alarm for all days of week.
	 * @param alarmDays The days to start the alarms on (if not for all days).
	 * @param alarmNumber The number of the alarm.
	 */
	public PhoneAlarm (Calendar alarmTime, boolean isOneTimeAlarm,
		boolean isForAllDays, int[] alarmDays, int alarmNumber)
	{
		if ( alarmTime == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:alarmTime==null");	// NOI18N
		}
		if ( (! isForAllDays) && (! isOneTimeAlarm) && alarmDays == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:alarmDays==null");	// NOI18N
		}

		time = alarmTime;
		oneTime = isOneTimeAlarm;
		forAllDays = isForAllDays;
		days = null;
		if ( isForAllDays )
		{
			days = null;
		}
		else
		{
			days = makeSetFromArray (alarmDays);
			if ( days != null )
			{
				if ( days.contains (ALL_DAYS) )
				{
					forAllDays = true;
				}
			}
		}
		number = alarmNumber;
	}

	/**
	 * Creates a new instance of PhoneAlarm.
	 * @param alarmTime The time to start the alarm.
	 * @param isOneTimeAlarm Is the alarm one-time (include the date and the time)
	 *	or repetitive (include only the time).
	 * @param isForAllDays Is the alarm for all days of week.
	 * @param alarmDays The days to start the alarms on (if not for all days).
	 * @param alarmNumber The number of the alarm.
	 */
	public PhoneAlarm (Calendar alarmTime, boolean isOneTimeAlarm,
		boolean isForAllDays, Set<? extends Integer> alarmDays, int alarmNumber)
	{
		if ( alarmTime == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:alarmTime==null");	// NOI18N
		}
		if ( (! isForAllDays) && (! isOneTimeAlarm) && alarmDays == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:alarmDays==null");	// NOI18N
		}

		time = alarmTime;
		oneTime = isOneTimeAlarm;
		forAllDays = isForAllDays;
		days = null;
		if ( isForAllDays )
		{
			days = null;
		}
		else
		{
			if ( alarmDays != null )
			{
				days = new HashSet<Integer> (alarmDays);
				if ( alarmDays.contains (ALL_DAYS) )
				{
					forAllDays = true;
				}
			}
		}
		number = alarmNumber;
	}

	/**
	 * Creates a new instance of PhoneAlarm.
	 * @param alarmNumber The number of the alarm.
	 * @param dateString The date string for the alarm in "DD/MM/YY" format
	 *	(or null for repetitive alarms).
	 * @param timeString The time string for the alarm in "HH:MM:SS" format (can't be null).
	 * @param daysString The days to start the alarms on, in "N,N,N" format
	 *	(can be null or "0" for all days).
	 */
	public PhoneAlarm (int alarmNumber, String dateString, String timeString,
		String daysString)
	{
		if ( timeString == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString==null");	// NOI18N
		}
		number = alarmNumber;
		time = Calendar.getInstance ();
		if ( dateString == null )
		{
			oneTime = true;
		}
		else if ( dateString.isEmpty () )
		{
			oneTime = true;
		}
		else
		{
			String[] parts = dateString.split (slash);
			if ( parts.length != 3 )
			{
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:dateString: " + dateString);	// NOI18N
			}
			oneTime = false;
			int month = Utils.convertRealMonthToCalendar (Integer.parseInt (parts[1]));
			int day = Integer.parseInt (parts[0]);
			int year = Integer.parseInt (parts[2]);
			time.set (Calendar.MONTH, month);
			time.set (Calendar.DAY_OF_MONTH, day);
			if ( year < 80 )
			{
				time.set (Calendar.YEAR, 2000+year);
			}
			else
			{
				time.set (Calendar.YEAR, 1900+year);
			}
		}
		if ( timeString.isEmpty () )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString:length=0");	// NOI18N
		}
		else
		{
			String[] parts = timeString.split (colon);
			if ( parts.length != 3 )
			{
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString: " + timeString);	// NOI18N
			}
			time.set (Calendar.HOUR_OF_DAY, Integer.parseInt (parts[0]));
			time.set (Calendar.MINUTE, Integer.parseInt (parts[1]));
			time.set (Calendar.SECOND, Integer.parseInt (parts[2]));
		}
		days = null;
		if ( daysString == null )
		{
			forAllDays = true;
		}
		else if ( daysString.isEmpty () )
		{
			forAllDays = true;
		}
		else
		{
			forAllDays = false;
			String[] recurrs = daysString.split (comma);
			if ( recurrs != null )
			{
				Set<Integer> tmpDays = makeSetFromArray (recurrs);
				if ( tmpDays != null )
				{
					if ( tmpDays.isEmpty () )
					{
						forAllDays = true;
					}
					else if ( tmpDays.contains (ALL_DAYS) )
					{
						forAllDays = true;
					}
					else
					{
						days = tmpDays;
					}
				}
			}
		}
	}

	/**
	 * Returns the time at which this PhoneAlarm will start.
	 * @return the time at which this PhoneAlarm will start.
	 */
	public synchronized Calendar getTime ()
	{
		return time;
	}

	/**
	 * Tells if this PhoneAlarm is a one-time alarm.
	 * @return TRUE if this PhoneAlarm is a one-time alarm.
	 */
	public synchronized boolean isOneTimeAlarm ()
	{
		return oneTime;
	}

	/**
	 * Tells if this PhoneAlarm is set for all days of the week.
	 * @return TRUE if this PhoneAlarm is set for all days of the week.
	 */
	public synchronized boolean isForAllDays ()
	{
		return forAllDays;
	}

	/**
	 * Returns the days at which this PhoneAlarm will start.
	 * @return the days at which this PhoneAlarm will start or
	 *	null in case of everyday alarms.
	 */
	public synchronized Set<Integer> getDays ()
	{
		if ( days == null )
		{
			return null;
		}
		return new HashSet<Integer> (days);
	}

	/**
	 * Returns the number (position) at which this PhoneAlarm
	 *	will be put in the phone.
	 * @return the number at which this PhoneAlarm will
	 *	be put in the phone.
	 */
	public synchronized int getNumber ()
	{
		return number;
	}

	/* ============= setters: =============== */

	/**
	 * Sets the time at which this PhoneAlarm will start.
	 * @param c the time at which this PhoneAlarm will start.
	 */
	public synchronized void setTime (Calendar c)
	{
		if ( c == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.setTime:alarmTime==null");	// NOI18N
		}
		time = c;
	}

	/**
	 * Sets if this PhoneAlarm is a one-time alarm.
	 * @param isOneTime TRUE if this PhoneAlarm is a one-time alarm.
	 */
	public synchronized void setOneTimeAlarm (boolean isOneTime)
	{
		oneTime = isOneTime;
	}

	/**
	 * Sets if this PhoneAlarm is set for all days of the week.
	 * @param isForAllDays TRUE if this PhoneAlarm is set for all days of the week.
	 */
	public synchronized void setForAllDays (boolean isForAllDays)
	{
		forAllDays = isForAllDays;
	}

	/**
	 * Sets the days at which this PhoneAlarm will start.
	 * @param alarmDays the days at which this PhoneAlarm will start or
	 *	null in case of everyday alarms.
	 */
	public synchronized void setDays (int[] alarmDays)
	{
		if ( (! forAllDays) && (! oneTime) && alarmDays == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.setDays:alarmDays==null");	// NOI18N
		}
		days = makeSetFromArray (alarmDays);
		if ( days != null )
		{
			if ( days.contains (ALL_DAYS) )
			{
				forAllDays = true;
			}
		}
	}

	/**
	 * Sets the days at which this PhoneAlarm will start.
	 * @param alarmDays the days at which this PhoneAlarm will start or
	 *	null in case of everyday alarms.
	 */
	public synchronized void setDays (Set<? extends Integer> alarmDays)
	{
		if ( (! forAllDays) && (! oneTime) && alarmDays == null )
		{
			throw new IllegalArgumentException ("PhoneAlarm.setDays:alarmDays==null");	// NOI18N
		}
		if ( alarmDays != null )
		{
			days = new HashSet<Integer> (alarmDays);
			if ( alarmDays.contains (ALL_DAYS) )
			{
				forAllDays = true;
			}
		}
		else
		{
			days = null;
		}
	}

	/**
	 * Sets the number (position) at which this PhoneAlarm
	 *	will be put in the phone.
	 * @param alarmNumber the number at which this PhoneAlarm
	 *	will be put in the phone.
	 */
	public synchronized void setNumber (int alarmNumber)
	{
		number = alarmNumber;
	}

	/**
	 * Gets the alarm string for this alarm.
	 * @return the alarm string for this alarm, suitable for commands sent
	 *	to the phone.
	 */
	public synchronized String getAlarmString ()
	{
		String result = dQuote;
		if ( oneTime )
		{
			result += getDateString () + comma;
		}
		result += getTimeString () + dQuote;
		if ( number != -1 )
		{
			result += comma + number + comma;
			if ( forAllDays || days == null )
			{
				result += zero;
			}
			else
			{
				result += dQuote + getDaysString () + dQuote;
			}
		}
		return result;
	}

	/**
	 * Get the date String for this alarm, if any.
	 * @return the date String for this alarm (in "DD/MM/YY" format)
	 *	or null for not one-time alarms.
	 */
	public synchronized String getDateString ()
	{
		if ( oneTime )
		{
			int year  = time.get (Calendar.YEAR) % 100;
			int month = Utils.convertCalendarMonthToReal (time.get (Calendar.MONTH));
			int day   = time.get (Calendar.DAY_OF_MONTH);

			return ((day<10)? zero : empty ) + day + slash
				+ ((month<10)? zero : empty ) + month + slash
				+ ((year<10)? zero : empty ) + year;
		}
		return null;
	}

	/**
	 * Get the time String for this alarm.
	 * @return the time String for this alarm (in "HH:MM:SS" format).
	 */
	public synchronized String getTimeString ()
	{
		int hour   = time.get (Calendar.HOUR_OF_DAY);
		int minute = time.get (Calendar.MINUTE);
		int second = time.get (Calendar.SECOND);

		return ((hour<10)? zero : empty ) + hour + colon
			+ ((minute<10)? zero : empty ) + minute + colon
			+ ((second<10)? zero : empty ) + second;
	}

	/**
	 * Get the days String for this alarm.
	 * @return the days String for this alarm (in "N,N,N" format).
	 */
	public synchronized String getDaysString ()
	{
		if ( days == null )
		{
			return empty;
		}
		if ( days.contains (ALL_DAYS) )
		{
			return zero;
		}
		else
		{
			StringBuilder result = new StringBuilder (20);
			Iterator<Integer> it = days.iterator ();
			if ( it != null )
			{
				while ( it.hasNext () )
				{
					Integer nextInt = it.next ();
					if ( nextInt == null )
					{
						continue;
					}
					result.append (nextInt.toString ());
					if ( it.hasNext () )
					{
						result.append (comma);
					}
				}
			}
			if ( result.length () == 0 )
			{
				result.append (zero);
			}
			return result.toString ();
		}
	}

	/**
	 * Converts the given integer array into a Set of Integers.
	 * @param arr the array to convert.
	 * @return a Set of Integers from the array.
	 */
	private static Set<Integer> makeSetFromArray (int[] arr)
	{
		Set<Integer> ret = null;
		if ( arr != null )
		{
			ret = new HashSet<Integer> (arr.length);
			for ( int i = 0 ; i < arr.length; i++ )
			{
				ret.add (Integer.valueOf (arr[i]));
			}
		}
		return ret;
	}

	/**
	 * Converts the non-empty elements of the given String array
	 * into a Set of Integers.
	 * @param arr the array to convert.
	 * @return a Set of Integers parsed from the array.
	 */
	private static Set<Integer> makeSetFromArray (String[] arr)
	{
		Set<Integer> ret = null;
		if ( arr != null )
		{
			ret = new HashSet<Integer> (arr.length);
			for ( int i = 0 ; i < arr.length; i++ )
			{
				if ( arr[i] == null )
				{
					continue;
				}
				if ( arr[i].isEmpty () )
				{
					continue;
				}
				ret.add (Integer.valueOf (arr[i]));
			}
		}
		return ret;
	}

	/**
	 * Parses the given phone response and creates a PhoneAlarm that matches it.
	 * @param response The response to parse.
	 * @return a PhoneAlarm that matches the given response.
	 */
	public synchronized static PhoneAlarm parseReponse (String response)
	{
		if ( response == null )
		{
			return null;
		}
		Matcher m = datetimePattern.matcher (response);
		if ( m.matches () )
		{
			Calendar c = Calendar.getInstance ();
			int year;
			int month;
			int day;
			int hour;
			int minute;
			int second;
			int alNumber = 0;
			try
			{
				day = Integer.parseInt (m.group (3));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (day)");	// NOI18N
				return null;
			}
			try
			{
				month = Integer.parseInt (m.group (4));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (month)");	// NOI18N
				return null;
			}
			try
			{
				year = Integer.parseInt (m.group (5));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (year)");	// NOI18N
				return null;
			}
			try
			{
				hour = Integer.parseInt (m.group (6));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (hour)");	// NOI18N
				return null;
			}
			try
			{
				minute = Integer.parseInt (m.group (7));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (minute)");	// NOI18N
				return null;
			}
			try
			{
				second = Integer.parseInt (m.group (8));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (second)");	// NOI18N
				return null;
			}
			try
			{
				String g10 = m.group (10);
				if ( g10 != null )
				{
					alNumber = Integer.parseInt (g10);
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (number)");	// NOI18N
				return null;
			}

			c.set (Calendar.MONTH, Utils.convertRealMonthToCalendar (month));
			c.set (Calendar.DAY_OF_MONTH, day);
			c.set (Calendar.HOUR_OF_DAY, hour);
			c.set (Calendar.MINUTE, minute);
			c.set (Calendar.SECOND, second);
			if ( year < 80 )
			{
				c.set (Calendar.YEAR, 2000+year);
			}
			else
			{
				c.set (Calendar.YEAR, 1900+year);
			}

			return new PhoneAlarm (c, true, false, (int[])null, alNumber);
		}
		m = timePattern.matcher (response);
		if ( m.matches () )
		{
			Calendar c = Calendar.getInstance ();
			int hour;
			int minute;
			int second;
			int alNumber = 0;
			int firstrec = -1;
			Set<Integer> tmpDays = null;

			try
			{
				hour = Integer.parseInt (m.group (3));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (hour) (2)");	// NOI18N
				return null;
			}
			try
			{
				minute = Integer.parseInt (m.group (4));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (minute) (2)");	// NOI18N
				return null;
			}
			try
			{
				second = Integer.parseInt (m.group (5));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (second) (2)");	// NOI18N
				return null;
			}
			try
			{
				String g7 = m.group (7);
				if ( g7 != null )
				{
					alNumber = Integer.parseInt (g7);
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (number) (2)");	// NOI18N
				return null;
			}
			// groups 9 and 10 contain the recurrences
			try
			{
				String g9 = m.group (9);
				if ( g9 != null )
				{
					firstrec = Integer.parseInt (g9);
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (recurr1)");	// NOI18N
				return null;
			}
			try
			{
				String g10 = m.group (10);
				if ( g10 != null )
				{
					String[] recurrs = g10.split (comma);
					if ( recurrs != null )
					{
						tmpDays = makeSetFromArray (recurrs);
						if ( tmpDays != null && firstrec != -1 )
						{
							tmpDays.add (Integer.valueOf (firstrec));
						}
					}
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (recurr2)");	// NOI18N
				return null;
			}

			c.set (Calendar.HOUR_OF_DAY, hour);
			c.set (Calendar.MINUTE, minute);
			c.set (Calendar.SECOND, second);

			boolean isForAllDays = false;
			if ( tmpDays != null )
			{
				if ( tmpDays.isEmpty () )
				{
					isForAllDays = true;
				}
				else if ( tmpDays.contains (ALL_DAYS) )
				{
					isForAllDays = true;
				}
			}
			else
			{
				isForAllDays = true;
			}

			return new PhoneAlarm (c, false, isForAllDays, tmpDays, alNumber);
		}
		return null;
	}

	/**
	 * Returns a String representation of this PhoneAlarm.
	 * @return a String representation of this PhoneAlarm. The syntax is
	 *	PhoneAlarm[ID=xxx,date/time,days].
	 */
	@Override
	public synchronized String toString ()
	{
		return toStringStart + toStringID + number + comma + toStringDateTime
			+ sdf.format (time.getTime ()) + comma
			+ toStringDays + getDaysString () + toStringEnd;
	}
}
