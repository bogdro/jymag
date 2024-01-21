/*
 * UtilsTest.java, part of the JYMAG package.
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

package BogDroSoft.jymag;

import BogDroSoft.jymag.gui.UiUtils;
import java.awt.Component;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.filechooser.FileFilter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * UtilsTest - a test for the Utils class.
 * @author Bogdan Drozdowski
 */
public class UtilsTest
{
	@BeforeClass
	public static void setUpClass () throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass () throws Exception
	{
	}

	@Before
	public void setUp ()
	{
	}

	@After
	public void tearDown ()
	{
	}

	/**
	 * Test of handleException method, of class Utils.
	 */
	@Test
	public void testHandleException ()
	{
		System.out.println ("handleException");
		Throwable ex = null;
		Object data = null;
		Utils.handleException (ex, data);
		ex = new Exception ("test1");
		Utils.handleException (ex, data);
		data = "test1msg";
		Utils.handleException (ex, data);
		ex = null;
		Utils.handleException (ex, data);
	}

	/**
	 * Test of changeGUI method, of class Utils.
	 */
	@Test
	//@Ignore("Can't test GUI from within a test")
	public void testChangeGUI ()
	{
		System.out.println ("changeGUI");
		Runnable r = null;
		UiUtils.changeGUI (r);
		// passing anything different from null can't be checked,
		// because there's no GUI
	}

	/**
	 * Test of setFontSize method, of class Utils.
	 */
	@Test
	public void testSetFontSize ()
	{
		System.out.println ("setFontSize");
		Component c = new JLabel ("testLabel");
		float newSize = 11.0F;
		UiUtils.setFontSize (c, newSize);
		if ( Math.abs (newSize - c.getFont ().getSize ()) > 0.1 )
		{
			fail ("Size differs: new=" + c.getFont ().getSize ()
				+ ", expected: " + newSize);
		}
	}

	/**
	 * Test of isAllowableSpeed method, of class Utils.
	 */
	@Test
	public void testIsAllowableSpeed ()
	{
		System.out.println ("isAllowableSpeed");
		int speed = 0;
		boolean expResult = false;
		boolean result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = -1;
		expResult = false;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 12345;
		expResult = false;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 1200;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 2400;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 4800;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 9600;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 19200;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 38400;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 57600;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 115200;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 230400;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 460800;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 500000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 576000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 921600;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 1000000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 1152000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 1500000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 2000000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 2500000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 3000000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 3500000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);

		speed = 4000000;
		expResult = true;
		result = Utils.isAllowableSpeed (speed);
		assertEquals (expResult, result);
	}

	/**
	 * Test of isAllowableDataBits method, of class Utils.
	 */
	@Test
	public void testIsAllowableDataBits ()
	{
		System.out.println ("isAllowableDataBits");
		int dBits = 0;
		boolean expResult = false;
		boolean result = Utils.isAllowableDataBits (dBits);
		assertEquals (expResult, result);

		dBits = 5;
		expResult = true;
		result = Utils.isAllowableDataBits (dBits);
		assertEquals (expResult, result);

		dBits = 6;
		expResult = true;
		result = Utils.isAllowableDataBits (dBits);
		assertEquals (expResult, result);

		dBits = 7;
		expResult = true;
		result = Utils.isAllowableDataBits (dBits);
		assertEquals (expResult, result);

		dBits = 8;
		expResult = true;
		result = Utils.isAllowableDataBits (dBits);
		assertEquals (expResult, result);
	}

	/**
	 * Test of createOpenFileChooser method, of class Utils.
	 */
	@Test
	public void testCreateOpenFileChooser ()
	{
		System.out.println ("createOpenFileChooser");
		String description = "";
		Map<String, Integer> filetype = new HashMap<String, Integer> (1);
		filetype.put ("ext", 0);
		JFileChooser result = UiUtils.createOpenFileChooser (description, filetype);
		assertNotNull (result);
		FileFilter resultFilter = result.getFileFilter ();
		assertTrue (resultFilter.accept (new File ("test.ext")));
		assertFalse (resultFilter.accept (new File ("test.png")));
		assertTrue (resultFilter.accept (new File ("test.png.ext")));
	}

	/**
	 * Test of createOpenFileChooser method, of class Utils.
	 */
	@Test
	public void testJoinArrays ()
	{
		System.out.println ("joinArrays");
		byte[] a1 = new byte[] {1, 2};
		byte[] a2 = new byte[] {3, 4, 5};
		byte[] res = Utils.joinArrays(a1, a2);
		assertEquals (res.length, a1.length + a2.length);
		for (int i = 0; i < a1.length; i++ )
		{
			assertEquals (res[i], a1[i]);
		}
		for (int i = 0; i < a2.length; i++ )
		{
			assertEquals (res[a1.length + i], a2[i]);
		}
	}

	/**
	 * Test of getFiletypeIDs method, of class Utils.
	 */
	@Test
	public void testGetFiletypeIDs ()
	{
		System.out.println ("getFiletypeIDs");
		Map<String, Integer> result = Utils.getFiletypeIDs ();
		assertNotNull (result);
		// check at least one extension of each type
		assertTrue (result.containsKey ("wav"));
		assertTrue (result.containsKey ("bmp"));
		assertTrue (result.containsKey ("vcf"));
		assertTrue (result.containsKey ("ics"));
		assertTrue (result.containsKey ("mng"));
		assertTrue (result.containsKey ("jar"));
		assertFalse (result.containsKey (""));
	}

	/**
	 * Test of getPhotofileIDs method, of class Utils.
	 */
	@Test
	public void testGetPhotofileIDs ()
	{
		System.out.println ("getPhotofileIDs");
		Map<String, Integer> result = Utils.getPhotofileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("jpg"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of getRingfileIDs method, of class Utils.
	 */
	@Test
	public void testGetRingfileIDs ()
	{
		System.out.println ("getRingfileIDs");
		Map<String, Integer> result = Utils.getRingfileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("mid"));
		assertFalse (result.containsKey ("jpg"));
	}

	/**
	 * Test of getAddrfileIDs method, of class Utils.
	 */
	@Test
	public void testGetAddrfileIDs ()
	{
		System.out.println ("getAddrfileIDs");
		Map<String, Integer> result = Utils.getAddrfileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("vcard"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of getTodofileIDs method, of class Utils.
	 */
	@Test
	public void testGetTodofileIDs ()
	{
		System.out.println ("getTodofileIDs");
		Map<String, Integer> result = Utils.getTodofileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("ical"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of getEventfileIDs method, of class Utils.
	 */
	@Test
	public void testGetEventfileIDs ()
	{
		System.out.println ("getEventfileIDs");
		Map<String, Integer> result = Utils.getEventfileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("vcs"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of getAnimfileIDs method, of class Utils.
	 */
	@Test
	public void testGetAnimfileIDs ()
	{
		System.out.println ("getAnimfileIDs");
		Map<String, Integer> result = Utils.getAnimfileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("mp4"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of getJavafileIDs method, of class Utils.
	 */
	@Test
	public void testGetJavafileIDs ()
	{
		System.out.println ("getJavafileIDs");
		Map<String, Integer> result = Utils.getJavafileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("jad"));
		assertFalse (result.containsKey ("wav"));
	}

	/**
	 * Test of convertCalendarMonthToReal method, of class Utils.
	 */
	@Test
	public void testConvertCalendarMonthToReal ()
	{
		System.out.println ("convertCalendarMonthToReal");
		assertEquals(1, Utils.convertCalendarMonthToReal(Calendar.JANUARY));
		assertEquals(2, Utils.convertCalendarMonthToReal(Calendar.FEBRUARY));
		assertEquals(3, Utils.convertCalendarMonthToReal(Calendar.MARCH));
		assertEquals(4, Utils.convertCalendarMonthToReal(Calendar.APRIL));
		assertEquals(5, Utils.convertCalendarMonthToReal(Calendar.MAY));
		assertEquals(6, Utils.convertCalendarMonthToReal(Calendar.JUNE));
		assertEquals(7, Utils.convertCalendarMonthToReal(Calendar.JULY));
		assertEquals(8, Utils.convertCalendarMonthToReal(Calendar.AUGUST));
		assertEquals(9, Utils.convertCalendarMonthToReal(Calendar.SEPTEMBER));
		assertEquals(10, Utils.convertCalendarMonthToReal(Calendar.OCTOBER));
		assertEquals(11, Utils.convertCalendarMonthToReal(Calendar.NOVEMBER));
		assertEquals(12, Utils.convertCalendarMonthToReal(Calendar.DECEMBER));
	}

	/**
	 * Test of convertRealMonthToCalendar method, of class Utils.
	 */
	@Test
	public void testConvertRealMonthToCalendar ()
	{
		System.out.println ("convertRealMonthToCalendar");
		assertEquals(Calendar.JANUARY, Utils.convertRealMonthToCalendar(1));
		assertEquals(Calendar.FEBRUARY, Utils.convertRealMonthToCalendar(2));
		assertEquals(Calendar.MARCH, Utils.convertRealMonthToCalendar(3));
		assertEquals(Calendar.APRIL, Utils.convertRealMonthToCalendar(4));
		assertEquals(Calendar.MAY, Utils.convertRealMonthToCalendar(5));
		assertEquals(Calendar.JUNE, Utils.convertRealMonthToCalendar(6));
		assertEquals(Calendar.JULY, Utils.convertRealMonthToCalendar(7));
		assertEquals(Calendar.AUGUST, Utils.convertRealMonthToCalendar(8));
		assertEquals(Calendar.SEPTEMBER, Utils.convertRealMonthToCalendar(9));
		assertEquals(Calendar.OCTOBER, Utils.convertRealMonthToCalendar(10));
		assertEquals(Calendar.NOVEMBER, Utils.convertRealMonthToCalendar(11));
		assertEquals(Calendar.DECEMBER, Utils.convertRealMonthToCalendar(12));
	}

	/**
	 * Test of getFontSize method, of class Utils.
	 */
	@Test
	public void testGetFontSize ()
	{
		int fontSize = 25;
		System.out.println ("getFontSize");
		JSpinner spin = new JSpinner();
		spin.setValue(fontSize);
		assertTrue(Math.abs(fontSize - UiUtils.getFontSize(spin)) < 0.001);
	}

}
