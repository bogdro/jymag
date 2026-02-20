/*
 * PhoneMessageTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PhoneMessageTest - a test for the PhoneMessage class.
 * @author Bogdan Drozdowski
 */
public class PhoneMessageTest
{
	/**
	 * Test of getMessage method, of class PhoneMessage.
	 */
	@Test
	public void testGetDefaultMessage ()
	{
		System.out.println ("getDefaultMessage");
		PhoneMessage instance = new PhoneMessage ();
		assertNull(instance.getMessage());
	}

	/**
	 * Test of getRecipientNum method, of class PhoneMessage.
	 */
	@Test
	public void testGetDefaultRecipientNum ()
	{
		System.out.println ("getDefaultRecipientNum");
		PhoneMessage instance = new PhoneMessage ();
		assertNull(instance.getRecipientNum());
	}

	/**
	 * Test of getID method, of class PhoneMessage.
	 */
	@Test
	public void testGetDefaultID ()
	{
		System.out.println ("getDefaultID");
		PhoneMessage instance = new PhoneMessage ();
		assertNull(instance.getID());
	}

	/**
	 * Test of getDateTime method, of class PhoneMessage.
	 */
	@Test
	public void testGetDefaultDateTime ()
	{
		System.out.println ("getDefaultDateTime");
		PhoneMessage instance = new PhoneMessage ();
		assertNull(instance.getDateTime());
	}

	/**
	 * Test of getStatus method, of class PhoneMessage.
	 */
	@Test
	public void testGetDefaultStatus ()
	{
		System.out.println ("getDefaultStatus");
		PhoneMessage instance = new PhoneMessage ();
		assertNull(instance.getStatus());
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
		assertEquals(newMsg, result);
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
		assertEquals(newRecip, result);
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
		assertEquals(newID, result);
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
		assertEquals(newDateTime, result);
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
		assertEquals(newStatus, result);
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
	public void testParseReponse()
	{
		System.out.println("parseReponse");

		String response = "+CMGL: 31415,\"1\",\"12345\",\"01/02/03,04:05:06+07\",,333\nbody\n";
		PhoneMessage result = PhoneMessage.parseReponse(response);
		assertNotNull (result);
		assertEquals("body", result.getMessage());
		assertEquals("12345", result.getRecipientNum());
		assertEquals("31415", result.getID());
		assertEquals("01/02/03,04:05:06+07", result.getDateTime());
		assertEquals("1", result.getStatus());

		response = "+CMGr: \"1\",\nbody\n";
		result = PhoneMessage.parseReponse (response);
		assertNotNull(result);
		assertEquals("body", result.getMessage ());
		assertNull(result.getRecipientNum());
		assertNull(result.getDateTime());
		assertEquals("1", result.getStatus());

		response = null;
		result = PhoneMessage.parseReponse(response);
		assertNull(result);
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
