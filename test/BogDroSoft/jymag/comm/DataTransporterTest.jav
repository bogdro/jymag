/*
 * DataTransporterTest.java, part of the JYMAG package.
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

package BogDroSoft.jymag.comm;

import BogDroSoft.jymag.PhoneAlarm;
import BogDroSoft.jymag.PhoneElement;
import BogDroSoft.jymag.PhoneMessage;
import BogDroSoft.jymag.comm.DataTransporter.DIAL_MODE;
import BogDroSoft.jymag.comm.DataTransporter.PIN_STATUS;
import BogDroSoft.jymag.comm.DataTransporter.STORAGE_TYPE;
import gnu.io.CommPortIdentifier;
import gnu.io.CommPortIdentifierStub;
import java.io.File;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DataTransporterTest - a test for the DataTransporter class.
 * @author Bogdan Drozdowski
 */
public class DataTransporterTest
{
	private static CommPortIdentifier id;
	private static DataTransporter dt;

	public DataTransporterTest ()
	{
	}

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		id = new CommPortIdentifierStub ("testPort", null, 0, null);
		dt = new DataTransporter (id);
	}

	@AfterClass
	public static void tearDownClass () throws Exception
	{
		if ( dt != null )
		{
			dt.close ();
		}
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
	 * Test of open method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testOpen () throws Exception
	{
		System.out.println ("open");
		int speed = 0;
		int dataBits = 0;
		float stopBits = 0.0F;
		int parity = 0;
		int flowControl = 0;
		dt.open (speed, dataBits, stopBits, parity, flowControl);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of recv method, of class DataTransporter.
	 */
	@Test
	public void testRecv ()
	{
		System.out.println ("recv");
		Object[] extraTerminators = null;
		byte[] expResult = null;
		byte[] result = dt.recv (extraTerminators);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSend_byteArr () throws Exception
	{
		System.out.println ("send");
		byte[] b = null;
		dt.send (b);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSend_3args () throws Exception
	{
		System.out.println ("send");
		byte[] b = null;
		int start = 0;
		int length = 0;
		dt.send (b, start, length);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class DataTransporter.
	 */
	@Test
	public void testClose ()
	{
		System.out.println ("close");
		dt.close ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of putFile method, of class DataTransporter.
	 */
	@Test
	public void testPutFile ()
	{
		System.out.println ("putFile");
		File f = null;
		String newName = "";
		int expResult = 0;
		int result = dt.putFile (f, newName);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class DataTransporter.
	 */
	@Test
	public void testGetFile ()
	{
		System.out.println ("getFile");
		File f = null;
		PhoneElement el = null;
		int expResult = 0;
		int result = dt.getFile (f, el);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getList method, of class DataTransporter.
	 */
	@Test
	public void testGetList ()
	{
		System.out.println ("getList");
		String ofWhat = "";
		Vector<PhoneElement> expResult = null;
		Vector<PhoneElement> result = dt.getList (ofWhat);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteFile method, of class DataTransporter.
	 */
	@Test
	public void testDeleteFile ()
	{
		System.out.println ("deleteFile");
		PhoneElement el = null;
		int expResult = 0;
		int result = dt.deleteFile (el);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of reopen method, of class DataTransporter.
	 */
	@Test
	public void testReopen ()
	{
		System.out.println ("reopen");
		dt.reopen ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of test method, of class DataTransporter.
	 */
	@Test
	public void testTest ()
	{
		System.out.println ("test");
		int expResult = 0;
		int result = dt.test ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getFirmwareVersion method, of class DataTransporter.
	 */
	@Test
	public void testGetFirmwareVersion ()
	{
		System.out.println ("getFirmwareVersion");
		String expResult = "";
		String result = dt.getFirmwareVersion ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getDeviceType method, of class DataTransporter.
	 */
	@Test
	public void testGetDeviceType ()
	{
		System.out.println ("getDeviceType");
		String expResult = "";
		String result = dt.getDeviceType ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getExtraDeviceType method, of class DataTransporter.
	 */
	@Test
	public void testGetExtraDeviceType ()
	{
		System.out.println ("getExtraDeviceType");
		String expResult = "";
		String result = dt.getExtraDeviceType ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getIMEI method, of class DataTransporter.
	 */
	@Test
	public void testGetIMEI ()
	{
		System.out.println ("getIMEI");
		String expResult = "";
		String result = dt.getIMEI ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getSubscriberNumbers method, of class DataTransporter.
	 */
	@Test
	public void testGetSubscriberNumbers ()
	{
		System.out.println ("getSubscriberNumbers");
		String expResult = "";
		String result = dt.getSubscriberNumbers ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getCapabilities method, of class DataTransporter.
	 */
	@Test
	public void testGetCapabilities ()
	{
		System.out.println ("getCapabilities");
		String type = "";
		String expResult = "";
		String result = dt.getCapabilities (type);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of poweroff method, of class DataTransporter.
	 */
	@Test
	public void testPoweroff ()
	{
		System.out.println ("poweroff");
		dt.poweroff ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getPINStatus method, of class DataTransporter.
	 */
	@Test
	public void testGetPINStatus ()
	{
		System.out.println ("getPINStatus");
		PIN_STATUS expResult = null;
		PIN_STATUS result = dt.getPINStatus ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendPIN method, of class DataTransporter.
	 */
	@Test
	public void testSendPIN_String_String ()
	{
		System.out.println ("sendPIN");
		String PIN = "";
		String newPIN = "";
		int expResult = 0;
		int result = dt.sendPIN (PIN, newPIN);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendPIN method, of class DataTransporter.
	 */
	@Test
	public void testSendPIN_int_int ()
	{
		System.out.println ("sendPIN");
		int PIN = 0;
		int newPIN = 0;
		int expResult = 0;
		int result = dt.sendPIN (PIN, newPIN);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getNumberOfAlarms method, of class DataTransporter.
	 */
	@Test
	public void testGetNumberOfAlarms ()
	{
		System.out.println ("getNumberOfAlarms");
		int expResult = 0;
		int result = dt.getNumberOfAlarms ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteAlarm method, of class DataTransporter.
	 */
	@Test
	public void testDeleteAlarm ()
	{
		System.out.println ("deleteAlarm");
		int number = 0;
		int expResult = 0;
		int result = dt.deleteAlarm (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of addAlarm method, of class DataTransporter.
	 */
	@Test
	public void testAddAlarm ()
	{
		System.out.println ("addAlarm");
		PhoneAlarm al = null;
		int expResult = 0;
		int result = dt.addAlarm (al);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getAlarms method, of class DataTransporter.
	 */
	@Test
	public void testGetAlarms ()
	{
		System.out.println ("getAlarms");
		Vector<PhoneAlarm> expResult = null;
		Vector<PhoneAlarm> result = dt.getAlarms ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteMessage method, of class DataTransporter.
	 */
	@Test
	public void testDeleteMessage ()
	{
		System.out.println ("deleteMessage");
		int number = 0;
		int expResult = 0;
		int result = dt.deleteMessage (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getMessages method, of class DataTransporter.
	 */
	@Test
	public void testGetMessages ()
	{
		System.out.println ("getMessages");
		Vector<PhoneMessage> expResult = null;
		Vector<PhoneMessage> result = dt.getMessages ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getMessage method, of class DataTransporter.
	 */
	@Test
	public void testGetMessage ()
	{
		System.out.println ("getMessage");
		int number = 0;
		PhoneMessage expResult = null;
		PhoneMessage result = dt.getMessage (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendMessage method, of class DataTransporter.
	 */
	@Test
	public void testSendMessage ()
	{
		System.out.println ("sendMessage");
		PhoneMessage msg = null;
		int expResult = 0;
		int result = dt.sendMessage (msg);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setMessageStorage method, of class DataTransporter.
	 */
	@Test
	public void testSetMessageStorage ()
	{
		System.out.println ("setMessageStorage");
		STORAGE_TYPE stor = null;
		int expResult = 0;
		int result = dt.setMessageStorage (stor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setDTR method, of class DataTransporter.
	 */
	@Test
	public void testSetDTR ()
	{
		System.out.println ("setDTR");
		boolean on = false;
		dt.setDTR (on);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setRTS method, of class DataTransporter.
	 */
	@Test
	public void testSetRTS ()
	{
		System.out.println ("setRTS");
		boolean on = false;
		dt.setRTS (on);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isCD method, of class DataTransporter.
	 */
	@Test
	public void testIsCD ()
	{
		System.out.println ("isCD");
		boolean expResult = false;
		boolean result = dt.isCD ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isCTS method, of class DataTransporter.
	 */
	@Test
	public void testIsCTS ()
	{
		System.out.println ("isCTS");
		boolean expResult = false;
		boolean result = dt.isCTS ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isDSR method, of class DataTransporter.
	 */
	@Test
	public void testIsDSR ()
	{
		System.out.println ("isDSR");
		boolean expResult = false;
		boolean result = dt.isDSR ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isDTR method, of class DataTransporter.
	 */
	@Test
	public void testIsDTR ()
	{
		System.out.println ("isDTR");
		boolean expResult = false;
		boolean result = dt.isDTR ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isRI method, of class DataTransporter.
	 */
	@Test
	public void testIsRI ()
	{
		System.out.println ("isRI");
		boolean expResult = false;
		boolean result = dt.isRI ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of isRTS method, of class DataTransporter.
	 */
	@Test
	public void testIsRTS ()
	{
		System.out.println ("isRTS");
		boolean expResult = false;
		boolean result = dt.isRTS ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getSignalPower method, of class DataTransporter.
	 */
	@Test
	public void testGetSignalPower ()
	{
		System.out.println ("getSignalPower");
		int expResult = 0;
		int result = dt.getSignalPower ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of dialNumber method, of class DataTransporter.
	 */
	@Test
	public void testDialNumber ()
	{
		System.out.println ("dialNumber");
		String number = "";
		boolean isVoice = false;
		DIAL_MODE dialMode = null;
		int expResult = 0;
		int result = dt.dialNumber (number, isVoice, dialMode);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of hangup method, of class DataTransporter.
	 */
	@Test
	public void testHangup ()
	{
		System.out.println ("hangup");
		int expResult = 0;
		int result = dt.hangup ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of answer method, of class DataTransporter.
	 */
	@Test
	public void testAnswer ()
	{
		System.out.println ("answer");
		int expResult = 0;
		int result = dt.answer ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getVolume method, of class DataTransporter.
	 */
	@Test
	public void testGetVolume ()
	{
		System.out.println ("getVolume");
		int expResult = 0;
		int result = dt.getVolume ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setVolume method, of class DataTransporter.
	 */
	@Test
	public void testSetVolume ()
	{
		System.out.println ("setVolume");
		int vol = 0;
		int expResult = 0;
		int result = dt.setVolume (vol);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getAvailableBytes method, of class DataTransporter.
	 */
	@Test
	public void testGetAvailableBytes ()
	{
		System.out.println ("getAvailableBytes");
		int expResult = 0;
		int result = dt.getAvailableBytes ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}
}
