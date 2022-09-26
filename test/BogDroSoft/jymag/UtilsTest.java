/*
 * UtilsTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014 Bogdan Drozdowski, bogdandr (at) op.pl
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

import BogDroSoft.jymag.Utils.STATUS;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
		Utils.changeGUI (r);
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
		Utils.setFontSize (c, newSize);
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
	 * Test of redirectStderrToFile method, of class Utils.
	 */
	@Test
	@Ignore("Don't close the error output while testing.")
	public void testRedirectStderrToFile ()
	{
		System.out.println ("redirectStderrToFile");
		String filename = "";
		String expResult = "";
		String result = Utils.redirectStderrToFile (filename);
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
		Hashtable<String, Integer> filetype = new Hashtable<String, Integer> (1);
		filetype.put ("ext", Integer.valueOf (0));
		JFileChooser result = Utils.createOpenFileChooser (description, filetype);
		assertNotNull (result);
		FileFilter resultFilter = result.getFileFilter ();
		assertTrue (resultFilter.accept (new File ("test.ext")));
		assertFalse (resultFilter.accept (new File ("test.png")));
		assertTrue (resultFilter.accept (new File ("test.png.ext")));
	}

	/**
	 * Test of closeProgram method, of class Utils.
	 */
	@Test
	@Ignore("Can't close the VM in a test")
	public void testCloseProgram ()
	{
		System.out.println ("closeProgram");
		String filename = "";
		int retval = 0;
		Utils.closeProgram (filename, retval);
	}

	/**
	 * Test of updateStatusLabel method, of class Utils.
	 */
	@Test
	public void testUpdateStatusLabel ()
	{
		System.out.println ("updateStatusLabel");
		JLabel status = new JLabel ("testLabel");
		STATUS s = null;
		Utils.updateStatusLabel (status, s);
		String result = status.getText ();
		String expResult = "testLabel";
		assertEquals (expResult, result);

		s = STATUS.READY;
		Utils.updateStatusLabel (status, s);
		result = status.getText ();
		expResult = s.toString ();
		assertEquals (expResult, result);
		Color cResult = status.getForeground ();
		Color cExpResult = new Color (0, 204, 0);
		assertEquals (cResult, cExpResult);

		s = STATUS.SENDING;
		Utils.updateStatusLabel (status, s);
		result = status.getText ();
		expResult = s.toString ();
		assertEquals (expResult, result);
		cResult = status.getForeground ();
		cExpResult = Color.BLUE;
		assertEquals (cResult, cExpResult);

		s = STATUS.RECEIVING;
		Utils.updateStatusLabel (status, s);
		result = status.getText ();
		expResult = s.toString ();
		assertEquals (expResult, result);
		cResult = status.getForeground ();
		cExpResult = Color.BLUE;
		assertEquals (cResult, cExpResult);
	}

	/**
	 * Test of getFiletypeIDs method, of class Utils.
	 */
	@Test
	public void testGetFiletypeIDs ()
	{
		System.out.println ("getFiletypeIDs");
		Hashtable<String, Integer> result = Utils.getFiletypeIDs ();
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
		Hashtable<String, Integer> result = Utils.getPhotofileIDs ();
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
		Hashtable<String, Integer> result = Utils.getRingfileIDs ();
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
		Hashtable<String, Integer> result = Utils.getAddrfileIDs ();
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
		Hashtable<String, Integer> result = Utils.getTodofileIDs ();
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
		Hashtable<String, Integer> result = Utils.getEventfileIDs ();
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
		Hashtable<String, Integer> result = Utils.getAnimfileIDs ();
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
		Hashtable<String, Integer> result = Utils.getJavafileIDs ();
		assertNotNull (result);
		assertTrue (result.containsKey ("jad"));
		assertFalse (result.containsKey ("wav"));
	}

}