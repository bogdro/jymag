/*
 * PhoneAlarm.java, part of the JYMAG package.
 *
 * Copyright (C) 2010 Bogdan Drozdowski, bogdandr (at) op.pl
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
	private HashSet<Integer> days;
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
			throw new NullPointerException ("PhoneAlarm.PhoneAlarm:alarmTime==null");	// NOI18N
		if ( (! isForAllDays) && (! isOneTimeAlarm) && alarmDays == null )
			throw new NullPointerException ("PhoneAlarm.PhoneAlarm:alarmDays==null");	// NOI18N

		time = alarmTime;
		oneTime = isOneTimeAlarm;
		forAllDays = isForAllDays;
		days = null;
		if ( isForAllDays ) days = null;
		else
		{
			if ( alarmDays != null )
			{
				days = new HashSet<Integer> (alarmDays.length);
				if ( days != null )
				{
					for ( int i = 0 ; i < alarmDays.length; i++ )
					{
						days.add (alarmDays[i]);
						if ( alarmDays[i] == 0 ) forAllDays = true;
					}
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
		boolean isForAllDays, Set<Integer> alarmDays, int alarmNumber)
	{
		if ( alarmTime == null )
			throw new NullPointerException ("PhoneAlarm.PhoneAlarm:alarmTime==null");	// NOI18N
		if ( (! isForAllDays) && (! isOneTimeAlarm) && alarmDays == null )
			throw new NullPointerException ("PhoneAlarm.PhoneAlarm:alarmDays==null");	// NOI18N

		time = alarmTime;
		oneTime = isOneTimeAlarm;
		forAllDays = isForAllDays;
		days = null;
		if ( isForAllDays ) days = null;
		else
		{
			if ( alarmDays != null )
			{
				days = new HashSet<Integer> (alarmDays);
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
			throw new NullPointerException ("PhoneAlarm.PhoneAlarm:timeString==null");	// NOI18N
		number = alarmNumber;
		time = Calendar.getInstance ();
		if ( dateString == null ) oneTime = true;
		else if ( dateString.length() == 0 ) oneTime = true;
		else
		{
			String[] parts = dateString.split ("/");	// NOI18N
			if ( parts == null )
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:dateString: " + dateString);	// NOI18N
			if ( parts.length != 3 )
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:dateString: " + dateString);	// NOI18N
			oneTime = false;
			int month = Integer.parseInt (parts[1]);
			int day = Integer.parseInt (parts[0]);
			int year = Integer.parseInt (parts[2]);
			     if ( month ==  1 ) month = Calendar.JANUARY;
			else if ( month ==  2 ) month = Calendar.FEBRUARY;
			else if ( month ==  3 ) month = Calendar.MARCH;
			else if ( month ==  4 ) month = Calendar.APRIL;
			else if ( month ==  5 ) month = Calendar.MAY;
			else if ( month ==  6 ) month = Calendar.JUNE;
			else if ( month ==  7 ) month = Calendar.JULY;
			else if ( month ==  8 ) month = Calendar.AUGUST;
			else if ( month ==  9 ) month = Calendar.SEPTEMBER;
			else if ( month == 10 ) month = Calendar.OCTOBER;
			else if ( month == 11 ) month = Calendar.NOVEMBER;
			else if ( month == 12 ) month = Calendar.DECEMBER;
			time.set (Calendar.MONTH, month);
			time.set (Calendar.DAY_OF_MONTH, day);
			if ( year < 80 ) time.set (Calendar.YEAR, 2000+year);
			else time.set (Calendar.YEAR, 1900+year);
		}
		if ( timeString.length () == 0 )
		{
			throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString:length=0");	// NOI18N
		}
		else
		{
			String[] parts = timeString.split (":");	// NOI18N
			if ( parts == null )
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString: " + timeString);	// NOI18N
			if ( parts.length != 3 )
				throw new IllegalArgumentException ("PhoneAlarm.PhoneAlarm:timeString: " + timeString);	// NOI18N
			time.set (Calendar.HOUR_OF_DAY, Integer.parseInt (parts[0]));
			time.set (Calendar.MINUTE, Integer.parseInt (parts[1]));
			time.set (Calendar.SECOND, Integer.parseInt (parts[2]));
		}
		days = null;
		if ( daysString == null ) forAllDays = true;
		else if ( daysString.length() == 0 ) forAllDays = true;
		else
		{
			forAllDays = false;
			String[] recurrs = daysString.split (",");
			if ( recurrs != null )
			{
				HashSet<Integer> tmpDays = new HashSet<Integer> (8);
				if ( tmpDays != null )
				{
					for ( int i = 0; i < recurrs.length; i++ )
					{
						if ( recurrs[i] == null ) continue;
						if ( recurrs[i].length () == 0 ) continue;
						tmpDays.add (Integer.valueOf (recurrs[i]));
					}
				}
				if ( tmpDays != null )
				{
					if ( tmpDays.size () == 0 )
					{
						forAllDays = true;
					}
					else if ( tmpDays.contains (new Integer (0)) )
					{
						forAllDays = true;
					}
					else days = tmpDays;
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
			throw new NullPointerException ("PhoneAlarm.setTime:alarmTime==null");	// NOI18N
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
		if ( (! forAllDays) && (! oneTime) &&alarmDays == null )
			throw new NullPointerException ("PhoneAlarm.setDays:alarmDays==null");	// NOI18N
		if ( alarmDays != null )
		{
			days = new HashSet<Integer> (8);
			for ( int i = 0; i < alarmDays.length; i++ )
			{
				days.add (alarmDays[i]);
				if ( alarmDays[i] == 0 ) forAllDays = true;
			}
		}
		else days = null;
	}

	/**
	 * Sets the days at which this PhoneAlarm will start.
	 * @param alarmDays the days at which this PhoneAlarm will start or
	 *	null in case of everyday alarms.
	 */
	public synchronized void setDays (Set<? extends Integer> alarmDays)
	{
		if ( (! forAllDays) && (! oneTime) &&alarmDays == null )
			throw new NullPointerException ("PhoneAlarm.setDays:alarmDays==null");	// NOI18N
		if ( alarmDays != null )
		{
			days = new HashSet<Integer> (alarmDays);
		}
		else days = null;
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
		String result = "\"";						// NOI18N
		if ( oneTime )
		{
			result += getDateString () + ",";			// NOI18N
		}
		result += getTimeString () + "\"";				// NOI18N
		if ( number != -1 )
		{
			result += "," + number + ",";				// NOI18N
			if ( forAllDays || days == null )
			{
				result += "0";	// NOI18N
			}
			else
			{
				result += "\"" + getDaysString () + "\"";	// NOI18N
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
			int month = time.get (Calendar.MONTH);
			int day   = time.get (Calendar.DAY_OF_MONTH);
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
			return ((day<10)? "0" : "" ) + day + "/"	// NOI18N
				+ ((month<10)? "0" : "" ) + month + "/"	// NOI18N
				+ ((year<10)? "0" : "" ) + year;	// NOI18N
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

		return ((hour<10)? "0" : "" ) + hour + ":"		// NOI18N
			+ ((minute<10)? "0" : "" ) + minute + ":"	// NOI18N
			+ ((second<10)? "0" : "" ) + second;	// NOI18N
	}

	/**
	 * Get the days String for this alarm.
	 * @return the days String for this alarm (in "N,N,N" format).
	 */
	public synchronized String getDaysString ()
	{
		if ( days == null ) return "";	// NOI18N
		if ( days.contains (new Integer (0)) )
		{
			return "0";	// NOI18N
		}
		else
		{
			String result = "";	// NOI18N
			Iterator<Integer> it = days.iterator ();
			if ( it != null )
			{
				while ( it.hasNext () )
				{
					result += it.next ().toString ();
					if ( it.hasNext () ) result += ",";	// NOI18N
				}
			}
			return result;
		}
	}

	/**
	 * Parses the given phone response and creates a PhoneAlarm that matches it.
	 * @param response The response to parse.
	 * @return a PhoneAlarm that matches the given response.
	 */
	public synchronized static PhoneAlarm parseReponse (String response)
	{
		if ( response == null ) return null;
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
			int alNumber = -1;
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
				if ( m.group (10) != null ) alNumber = Integer.parseInt (m.group (10));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (number)");	// NOI18N
				return null;
			}

			     if ( month ==  1 ) month = Calendar.JANUARY;
			else if ( month ==  2 ) month = Calendar.FEBRUARY;
			else if ( month ==  3 ) month = Calendar.MARCH;
			else if ( month ==  4 ) month = Calendar.APRIL;
			else if ( month ==  5 ) month = Calendar.MAY;
			else if ( month ==  6 ) month = Calendar.JUNE;
			else if ( month ==  7 ) month = Calendar.JULY;
			else if ( month ==  8 ) month = Calendar.AUGUST;
			else if ( month ==  9 ) month = Calendar.SEPTEMBER;
			else if ( month == 10 ) month = Calendar.OCTOBER;
			else if ( month == 11 ) month = Calendar.NOVEMBER;
			else if ( month == 12 ) month = Calendar.DECEMBER;
			c.set (Calendar.MONTH, month);
			c.set (Calendar.DAY_OF_MONTH, day);
			c.set (Calendar.HOUR_OF_DAY, hour);
			c.set (Calendar.MINUTE, minute);
			c.set (Calendar.SECOND, second);
			if ( year < 80 ) c.set (Calendar.YEAR, 2000+year);
			else c.set (Calendar.YEAR, 1900+year);

			return new PhoneAlarm (c, true, false, (int[])null, alNumber);
		}
		m = timePattern.matcher (response);
		if ( m.matches () )
		{
			Calendar c = Calendar.getInstance ();
			int hour;
			int minute;
			int second;
			int alNumber = -1;
			int firstrec = -1;
			HashSet<Integer> tmpDays = null;

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
				if ( m.group (7) != null ) alNumber = Integer.parseInt (m.group (7));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (number) (2)");	// NOI18N
				return null;
			}
			// groups 9 and 10 contain the recurrences
			try
			{
				if ( m.group (9) != null ) firstrec = Integer.parseInt (m.group (9));
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "PhoneAlarm.parseReponse.parseInt (recurr1)");	// NOI18N
				return null;
			}
			try
			{
				if ( m.group (10) != null )
				{
					String[] recurrs = m.group (10).split (",");	// NOI18N
					if ( recurrs != null )
					{
						tmpDays = new HashSet<Integer> (8);
						if ( tmpDays != null )
						{
							if ( firstrec != -1 ) tmpDays.add (firstrec);
							for ( int i = 0; i < recurrs.length; i++ )
							{
								if ( recurrs[i] == null ) continue;
								if ( recurrs[i].length () == 0 ) continue;
								tmpDays.add (Integer.valueOf (recurrs[i]));
							}
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
				if ( tmpDays.size () == 0 )
				{
					isForAllDays = true;
				}
				else if ( tmpDays.contains (new Integer (0)) )
				{
					isForAllDays = true;
				}
			}
			else isForAllDays = true;

			return new PhoneAlarm (c, false, isForAllDays, tmpDays, alNumber);
		}
		return null;
	}
}