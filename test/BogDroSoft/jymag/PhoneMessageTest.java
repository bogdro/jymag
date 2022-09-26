/*
 * PhoneMessageTest.java, part of the JYMAG package.
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PhoneMessageTest - a test for the PhoneMessage class.
 * @author Bogdan Drozdowski
 */
public class PhoneMessageTest
{
	public PhoneMessageTest ()
	{
	}

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
	 * Test of getMessage method, of class PhoneMessage.
	 */
	@Test
	@Ignore("Will be tested in the setter test")
	public void testGetMessage ()
	{
		System.out.println ("getMessage");
		PhoneMessage instance = new PhoneMessage ();
		String expResult = "";
		String result = instance.getMessage ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getRecipientNum method, of class PhoneMessage.
	 */
	@Test
	@Ignore("Will be tested in the setter test")
	public void testGetRecipientNum ()
	{
		System.out.println ("getRecipientNum");
		PhoneMessage instance = new PhoneMessage ();
		String expResult = "";
		String result = instance.getRecipientNum ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getID method, of class PhoneMessage.
	 */
	@Test
	@Ignore("Will be tested in the setter test")
	public void testGetID ()
	{
		System.out.println ("getID");
		PhoneMessage instance = new PhoneMessage ();
		String expResult = "";
		String result = instance.getID ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getDateTime method, of class PhoneMessage.
	 */
	@Test
	@Ignore("Will be tested in the setter test")
	public void testGetDateTime ()
	{
		System.out.println ("getDateTime");
		PhoneMessage instance = new PhoneMessage ();
		String expResult = "";
		String result = instance.getDateTime ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getStatus method, of class PhoneMessage.
	 */
	@Test
	@Ignore("Will be tested in the setter test")
	public void testGetStatus ()
	{
		System.out.println ("getStatus");
		PhoneMessage instance = new PhoneMessage ();
		String expResult = "";
		String result = instance.getStatus ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setMessage method, of class PhoneMessage.
	 */
	@Test
	public void testSetMessage ()
	{
		System.out.println ("setMessage");
		String newMsg = "test";
		PhoneMessage instance = new PhoneMessage ();
		instance.setMessage (newMsg);
		String result = instance.getMessage ();
		assertEquals (result, newMsg);
	}

	/**
	 * Test of setRecipientNum method, of class PhoneMessage.
	 */
	@Test
	public void testSetRecipientNum ()
	{
		System.out.println ("setRecipientNum");
		String newRecip = "12345";
		PhoneMessage instance = new PhoneMessage ();
		instance.setRecipientNum (newRecip);
		String result = instance.getRecipientNum ();
		assertEquals (result, newRecip);
	}

	/**
	 * Test of setID method, of class PhoneMessage.
	 */
	@Test
	public void testSetID ()
	{
		System.out.println ("setID");
		String newID = "54321";
		PhoneMessage instance = new PhoneMessage ();
		instance.setID (newID);
		String result = instance.getID ();
		assertEquals (result, newID);
	}

	/**
	 * Test of setDateTime method, of class PhoneMessage.
	 */
	@Test
	public void testSetDateTime ()
	{
		System.out.println ("setDateTime");
		String newDateTime = "2000-01-01";
		PhoneMessage instance = new PhoneMessage ();
		instance.setDateTime (newDateTime);
		String result = instance.getDateTime ();
		assertEquals (result, newDateTime);
	}

	/**
	 * Test of setStatus method, of class PhoneMessage.
	 */
	@Test
	public void testSetStatus ()
	{
		System.out.println ("setStatus");
		String newStatus = "0";
		PhoneMessage instance = new PhoneMessage ();
		instance.setStatus (newStatus);
		String result = instance.getStatus ();
		assertEquals (result, newStatus);
	}

	/**
	 * Test of getMessageString method, of class PhoneMessage.
	 */
	@Test
	public void testGetMessageString ()
	{
		System.out.println ("getMessageString");
		PhoneMessage instance = new PhoneMessage ();
		String newMsg = "test";
		String expResult = newMsg + "\033";
		instance.setMessage (newMsg);
		String result = instance.getMessageString ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of parseReponse method, of class PhoneMessage.
	 */
	@Test
	public void testParseReponse ()
	{
		System.out.println ("parseReponse");

		String response = "+CMGL: 31415,\"1\",\"12345\",\"01/02/03,04:05:06+07\",,333\nbody\n";
		PhoneMessage result = PhoneMessage.parseReponse (response);
		assertNotNull (result);
		assertEquals (result.getMessage (), "body");
		assertEquals (result.getRecipientNum (), "12345");
		assertEquals (result.getID (), "31415");
		assertEquals (result.getDateTime (), "01/02/03,04:05:06+07");
		assertEquals (result.getStatus (), "1");

		response = "+CMGr: \"1\",\nbody\n";
		result = PhoneMessage.parseReponse (response);
		assertNotNull (result);
		assertEquals (result.getMessage (), "body");
		assertNull (result.getRecipientNum ());
		assertNull (result.getDateTime ());
		assertEquals (result.getStatus (), "1");

		response = null;
		result = PhoneMessage.parseReponse (response);
		assertNull (result);
	}

	/**
	 * Test of toString method, of class PhoneMessage.
	 */
	@Test
	public void testToString ()
	{
		System.out.println ("toString");
		PhoneMessage instance = new PhoneMessage ();
		instance.setMessage ("test2");
		instance.setRecipientNum ("123456");
		instance.setID ("0123");
		instance.setDateTime ("2001-01-01");
		instance.setStatus ("1");
		String result = instance.toString ();
		String expResult = "PhoneMessage[ID=0123,1,123456,2001-01-01,test2]";
		assertEquals (expResult, result);
	}
}
