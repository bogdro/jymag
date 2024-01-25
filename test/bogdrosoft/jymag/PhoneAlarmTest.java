/*
 * PhoneAlarmTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import java.util.Set;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PhoneAlarmTest - a test for the PhoneAlarm class.
 * @author Bogdan Drozdowski
 */
public class PhoneAlarmTest
{
	/**
	 * Test of getTime method, of class PhoneAlarm.
	 */
	@Test
	public void testGetTime ()
	{
		System.out.println ("getTime");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		Calendar expResult = null;
		Calendar result = instance.getTime ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isOneTimeAlarm method, of class PhoneAlarm.
	 */
	@Test
	public void testIsOneTimeAlarm ()
	{
		System.out.println ("isOneTimeAlarm");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		boolean expResult = false;
		boolean result = instance.isOneTimeAlarm ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isForAllDays method, of class PhoneAlarm.
	 */
	@Test
	public void testIsForAllDays ()
	{
		System.out.println ("isForAllDays");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		boolean expResult = false;
		boolean result = instance.isForAllDays ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDays method, of class PhoneAlarm.
	 */
	@Test
	public void testGetDays ()
	{
		System.out.println ("getDays");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		Set<Integer> expResult = null;
		Set<Integer> result = instance.getDays ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getNumber method, of class PhoneAlarm.
	 */
	@Test
	public void testGetNumber ()
	{
		System.out.println ("getNumber");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		int expResult = 0;
		int result = instance.getNumber ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of setTime method, of class PhoneAlarm.
	 */
	@Test
	public void testSetTime ()
	{
		System.out.println ("setTime");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setTime (c);
		Calendar c2 = instance.getTime ();
		assertEquals (c, c2);
	}

	/**
	 * Test of setOneTimeAlarm method, of class PhoneAlarm.
	 */
	@Test
	public void testSetOneTimeAlarm ()
	{
		System.out.println ("setOneTimeAlarm");
		Calendar c = Calendar.getInstance ();
		boolean isOneTime = false;
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setOneTimeAlarm (isOneTime);
		boolean isOneTime2 = instance.isOneTimeAlarm ();
		assertEquals (isOneTime, isOneTime2);
	}

	/**
	 * Test of setForAllDays method, of class PhoneAlarm.
	 */
	@Test
	public void testSetForAllDays ()
	{
		System.out.println ("setForAllDays");
		Calendar c = Calendar.getInstance ();
		boolean isForAllDays = false;
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setForAllDays (isForAllDays);
		boolean isForAllDays2 = instance.isForAllDays ();
		assertEquals (isForAllDays, isForAllDays2);
	}

	/**
	 * Test of setDays method, of class PhoneAlarm.
	 */
	@Test
	public void testSetDaysWithIntArray ()
	{
		System.out.println ("setDays");
		Calendar c = Calendar.getInstance ();
		int[] alarmDays = new int[] {1};
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setDays (alarmDays);
		Set<Integer> d = instance.getDays ();
		Set<Integer> d2 = new HashSet<Integer> (alarmDays.length);
		for ( int a : alarmDays )
		{
			d2.add (a);
		}
		assertEquals (d, d2);
	}

	/**
	 * Test of setDays method, of class PhoneAlarm.
	 */
	@Test
	public void testSetDaysWithSet ()
	{
		System.out.println ("setDays");
		Calendar c = Calendar.getInstance ();
		Set<? extends Integer> alarmDays = null;
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setDays (alarmDays);
		Set<? extends Integer> alarmDays2 = instance.getDays ();
		assertEquals (alarmDays, alarmDays2);
	}

	/**
	 * Test of setNumber method, of class PhoneAlarm.
	 */
	@Test
	public void testSetNumber ()
	{
		System.out.println ("setNumber");
		Calendar c = Calendar.getInstance ();
		int alarmNumber = 0;
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		instance.setNumber (alarmNumber);
		int alarmNumber2 = instance.getNumber ();
		assertEquals (alarmNumber, alarmNumber2);
	}

	/**
	 * Test of getAlarmString method, of class PhoneAlarm.
	 */
	@Test
	public void testGetAlarmString ()
	{
		System.out.println ("getAlarmString");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		String expResult = "\""
			+ ((c.get(Calendar.HOUR_OF_DAY) < 10)? "0" : "")
			+ c.get(Calendar.HOUR_OF_DAY)
			+ ":" + ((c.get(Calendar.MINUTE) < 10)? "0" : "")
			+ c.get(Calendar.MINUTE)
			+ ":" + ((c.get(Calendar.SECOND) < 10)? "0" : "")
			+ c.get(Calendar.SECOND)
			+ "\",1,0";
		String result = instance.getAlarmString ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDateString method, of class PhoneAlarm.
	 */
	@Test
	public void testGetDateString ()
	{
		System.out.println ("getDateString");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, true, true, (int[]) null, 1);
		int month = c.get(Calendar.MONTH);
		if ( month == Calendar.JANUARY )
		{
			month =  1;
		}
		else if ( month == Calendar.FEBRUARY )
		{
			month =  2;
		}
		else if ( month == Calendar.MARCH )
		{
			month =  3;
		}
		else if ( month == Calendar.APRIL )
		{
			month =  4;
		}
		else if ( month == Calendar.MAY )
		{
			month =  5;
		}
		else if ( month == Calendar.JUNE )
		{
			month =  6;
		}
		else if ( month == Calendar.JULY )
		{
			month =  7;
		}
		else if ( month == Calendar.AUGUST )
		{
			month =  8;
		}
		else if ( month == Calendar.SEPTEMBER )
		{
			month =  9;
		}
		else if ( month == Calendar.OCTOBER )
		{
			month = 10;
		}
		else if ( month == Calendar.NOVEMBER )
		{
			month = 11;
		}
		else if ( month == Calendar.DECEMBER )
		{
			month = 12;
		}
		String expResult = ""
			+ ((c.get(Calendar.DAY_OF_MONTH) < 10)? "0" : "")
			+ c.get(Calendar.DAY_OF_MONTH)
			+ "/" + ((month < 10)? "0" : "")
			+ month
			+ "/" + ((c.get(Calendar.YEAR) % 100 < 10)? "0" : "")
			+ c.get(Calendar.YEAR) % 100
			+ "";
		String result = instance.getDateString ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getTimeString method, of class PhoneAlarm.
	 */
	@Test
	public void testGetTimeString ()
	{
		System.out.println ("getTimeString");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		String expResult = ""
			+ ((c.get(Calendar.HOUR_OF_DAY) < 10)? "0" : "")
			+ c.get(Calendar.HOUR_OF_DAY)
			+ ":" + ((c.get(Calendar.MINUTE) < 10)? "0" : "")
			+ c.get(Calendar.MINUTE)
			+ ":" + ((c.get(Calendar.SECOND) < 10)? "0" : "")
			+ c.get(Calendar.SECOND)
			;
		String result = instance.getTimeString ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDaysString method, of class PhoneAlarm.
	 */
	@Test
	public void testGetDaysString ()
	{
		System.out.println ("getDaysString");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		String expResult = "";
		String result = instance.getDaysString ();
		assertEquals (expResult, result);

		Set<Integer> d = new HashSet<Integer> (1);
		d.add (1);
		instance.setDays (d);
		expResult = "1";
		result = instance.getDaysString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (0);
		instance.setDays (d);
		expResult = "0";
		result = instance.getDaysString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (0);
		d.add (1);
		d.add (2);
		instance.setDays (d);
		expResult = "0";
		result = instance.getDaysString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (1);
		d.add (2);
		d.add (3);
		instance.setDays (d);
		expResult = "1,2,3";
		result = instance.getDaysString ();
		assertEquals (expResult, result);

		d.clear ();
		instance.setDays (d);
		expResult = "0";
		result = instance.getDaysString ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of parseReponse method, of class PhoneAlarm.
	 */
	@Test
	public void testParseReponse ()
	{
		System.out.println ("parseReponse");
		String response = "+CALA:\"01/02/03,11:22:33\",4,5,66\"";
		Calendar c = Calendar.getInstance ();
		c.set(Calendar.YEAR, 2003);
		c.set(Calendar.MONTH, Calendar.FEBRUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 22);
		c.set(Calendar.SECOND, 33);
		PhoneAlarm expResult = new PhoneAlarm (c, true, false, (int[]) null, 4);
		PhoneAlarm result = PhoneAlarm.parseReponse (response);
		assertEquals (expResult.getTime ().get(Calendar.YEAR),
			result.getTime ().get(Calendar.YEAR));
		assertEquals (expResult.getTime ().get(Calendar.MONTH),
			result.getTime ().get(Calendar.MONTH));
		assertEquals (expResult.getTime ().get(Calendar.DAY_OF_MONTH),
			result.getTime ().get(Calendar.DAY_OF_MONTH));
		assertEquals (expResult.getTime ().get(Calendar.HOUR_OF_DAY),
			result.getTime ().get(Calendar.HOUR_OF_DAY));
		assertEquals (expResult.getTime ().get(Calendar.MINUTE),
			result.getTime ().get(Calendar.MINUTE));
		assertEquals (expResult.getTime ().get(Calendar.SECOND),
			result.getTime ().get(Calendar.SECOND));
		assertEquals (expResult.isOneTimeAlarm (), result.isOneTimeAlarm ());
		assertEquals (expResult.isForAllDays (), result.isForAllDays ());
		assertEquals (expResult.getDays (), result.getDays ());
		assertEquals (expResult.getNumber (), result.getNumber ());
		assertEquals (expResult.getAlarmString (), result.getAlarmString ());
		assertEquals (expResult.getDateString (), result.getDateString ());
		assertEquals (expResult.getTimeString (), result.getTimeString ());
		assertEquals (expResult.getDaysString (), result.getDaysString ());
		assertEquals (expResult.toString (), result.toString ());

		response = "+CaLA:\"11:22:33\",5\"";
		c = Calendar.getInstance ();
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 22);
		c.set(Calendar.SECOND, 33);
		expResult = new PhoneAlarm (c, false, true, (int[]) null, 5);
		result = PhoneAlarm.parseReponse (response);
		assertEquals (expResult.getTime ().get(Calendar.HOUR_OF_DAY),
			result.getTime ().get(Calendar.HOUR_OF_DAY));
		assertEquals (expResult.getTime ().get(Calendar.MINUTE),
			result.getTime ().get(Calendar.MINUTE));
		assertEquals (expResult.getTime ().get(Calendar.SECOND),
			result.getTime ().get(Calendar.SECOND));
		assertEquals (expResult.isOneTimeAlarm (), result.isOneTimeAlarm ());
		assertEquals (expResult.isForAllDays (), result.isForAllDays ());
		assertEquals (expResult.getDays (), result.getDays ());
		assertEquals (expResult.getNumber (), result.getNumber ());
		assertEquals (expResult.getAlarmString (), result.getAlarmString ());
		assertEquals (expResult.getDateString (), result.getDateString ());
		assertEquals (expResult.getTimeString (), result.getTimeString ());
		assertEquals (expResult.getDaysString (), result.getDaysString ());
		assertEquals (expResult.toString (), result.toString ());
	}

	/**
	 * Test of toString method, of class PhoneAlarm.
	 */
	@Test
	public void testToString ()
	{
		System.out.println ("toString");
		Calendar c = Calendar.getInstance ();
		int month = c.get(Calendar.MONTH);
		if ( month == Calendar.JANUARY )
		{
			month =  1;
		}
		else if ( month == Calendar.FEBRUARY )
		{
			month =  2;
		}
		else if ( month == Calendar.MARCH )
		{
			month =  3;
		}
		else if ( month == Calendar.APRIL )
		{
			month =  4;
		}
		else if ( month == Calendar.MAY )
		{
			month =  5;
		}
		else if ( month == Calendar.JUNE )
		{
			month =  6;
		}
		else if ( month == Calendar.JULY )
		{
			month =  7;
		}
		else if ( month == Calendar.AUGUST )
		{
			month =  8;
		}
		else if ( month == Calendar.SEPTEMBER )
		{
			month =  9;
		}
		else if ( month == Calendar.OCTOBER )
		{
			month = 10;
		}
		else if ( month == Calendar.NOVEMBER )
		{
			month = 11;
		}
		else if ( month == Calendar.DECEMBER )
		{
			month = 12;
		}
		String time = ""
			+ ((c.get(Calendar.DAY_OF_MONTH) < 10)? "0" : "")
			+ c.get(Calendar.DAY_OF_MONTH)
			+ "/" + ((month < 10)? "0" : "")
			+ month
			+ "/" + ((c.get(Calendar.YEAR) % 100 < 10)? "0" : "")
			+ c.get(Calendar.YEAR) % 100
			+ ","
			+ ((c.get(Calendar.HOUR_OF_DAY) < 10)? "0" : "")
			+ c.get(Calendar.HOUR_OF_DAY)
			+ ":" + ((c.get(Calendar.MINUTE) < 10)? "0" : "")
			+ c.get(Calendar.MINUTE)
			+ ":" + ((c.get(Calendar.SECOND) < 10)? "0" : "")
			+ c.get(Calendar.SECOND)
			;

		PhoneAlarm instance = new PhoneAlarm (c, false, true, (int[]) null, 1);
		String expResult = "PhoneAlarm[ID=1," + time + ",]";
		String result = instance.toString ();
		assertEquals (expResult, result);

		Set<Integer> d = new HashSet<Integer> (1);
		d.add (1);
		instance.setDays (d);
		expResult = "PhoneAlarm[ID=1," + time + ",1]";
		result = instance.toString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (0);
		instance.setDays (d);
		expResult = "PhoneAlarm[ID=1," + time + ",0]";
		result = instance.toString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (0);
		d.add (1);
		d.add (2);
		instance.setDays (d);
		expResult = "PhoneAlarm[ID=1," + time + ",0]";
		result = instance.toString ();
		assertEquals (expResult, result);

		d.clear ();
		d.add (1);
		d.add (2);
		d.add (3);
		instance.setDays (d);
		expResult = "PhoneAlarm[ID=1," + time + ",1,2,3]";
		result = instance.toString ();
		assertEquals (expResult, result);

		d.clear ();
		instance.setDays (d);
		expResult = "PhoneAlarm[ID=1," + time + ",0]";
		result = instance.toString ();
		assertEquals (expResult, result);
	}
}
