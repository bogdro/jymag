/*
 * DataTransporterTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * DataTransporterTest - a test for the DataTransporter class.
 * @author Bogdan Drozdowski
 */
public class DataTransporterTest
{
	//private static CommPortIdentifier cpid;
	//private static DataTransporter dtr;

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		/*
		id = mock (CommPortIdentifier.class);
			//new CommPortIdentifierStub ("testPort", null, 0, null);
		when(id.getName ()).thenReturn ("COMSTUB");
		when(id.open (anyString (), anyInt ())).thenReturn (new SerialPortStub ());
		dt = new DataTransporter (id);
		 */
	}

	@AfterClass
	public static void tearDownClass () throws Exception
	{
		/*
		if ( dt != null )
		{
			dt.close ();
		}
		 */
	}

	@Before
	public void setUp ()
	{
	}

	@After
	public void tearDown ()
	{
	}

	private DataTransporter prepareDT() throws Exception
	{
		CommPortIdentifier id = mock (CommPortIdentifier.class);
			//new CommPortIdentifierStub ("testPort", null, 0, null);
		when(id.getName ()).thenReturn ("COMSTUB");
		when(id.open (anyString (), anyInt ())).thenReturn (new SerialPortStub ());
		int speed = 0;
		int dataBits = 0;
		float stopBits = 0.0F;
		int parity = 0;
		int flowControl = 0;
		DataTransporter dt = new DataTransporter (id);
		dt.open (speed, dataBits, stopBits, parity, flowControl);
		return dt;
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
		prepareDT();
	}

	/**
	 * Test of open method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test(expected=IOException.class)
	public void testOpen_fail () throws Exception
	{
		System.out.println ("open");
		CommPortIdentifier id = mock (CommPortIdentifier.class);
			//new CommPortIdentifierStub ("testPort", null, 0, null);
		when(id.getName ()).thenReturn ("fake");
		when(id.open (anyString (), anyInt ())).thenReturn (new SerialPortStub ());
		DataTransporter dt = new DataTransporter (id);
		int speed = 0;
		int dataBits = 0;
		float stopBits = 0.0F;
		int parity = 0;
		int flowControl = 0;
		dt.open (speed, dataBits, stopBits, parity, flowControl);
	}

	/**
	 * Test of recv method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testRecv () throws Exception
	{
		System.out.println ("recv");
		DataTransporter dt = prepareDT();
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
	@Ignore
	public void testSend_byteArr () throws Exception
	{
		System.out.println ("send");
		DataTransporter dt = prepareDT();
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
	@Ignore
	public void testSend_3args () throws Exception
	{
		System.out.println ("send");
		DataTransporter dt = prepareDT();
		byte[] b = null;
		int start = 0;
		int length = 0;
		dt.send (b, start, length);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testClose () throws Exception
	{
		System.out.println ("close");
		DataTransporter dt = prepareDT();
		dt.close ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of putFile method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testPutFile () throws Exception
	{
		System.out.println ("putFile");
		DataTransporter dt = prepareDT();
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
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetFile () throws Exception
	{
		System.out.println ("getFile");
		DataTransporter dt = prepareDT();
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
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetList () throws Exception
	{
		System.out.println ("getList");
		DataTransporter dt = prepareDT();
		String ofWhat = "";
		Vector<PhoneElement> expResult = null;
		Vector<PhoneElement> result = dt.getList (ofWhat);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteFile method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testDeleteFile () throws Exception
	{
		System.out.println ("deleteFile");
		DataTransporter dt = prepareDT();
		PhoneElement el = null;
		int expResult = 0;
		int result = dt.deleteFile (el);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of reopen method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testReopen () throws Exception
	{
		System.out.println ("reopen");
		DataTransporter dt = prepareDT();
		dt.reopen ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of test method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testTest () throws Exception
	{
		System.out.println ("test");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.test ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getFirmwareVersion method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetFirmwareVersion () throws Exception
	{
		System.out.println ("getFirmwareVersion");
		DataTransporter dt = prepareDT();
		String expResult = "";
		String result = dt.getFirmwareVersion ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getDeviceType method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetDeviceType () throws Exception
	{
		System.out.println ("getDeviceType");
		DataTransporter dt = prepareDT();
		String expResult = "";
		String result = dt.getDeviceType ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getExtraDeviceType method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetExtraDeviceType () throws Exception
	{
		System.out.println ("getExtraDeviceType");
		DataTransporter dt = prepareDT();
		String expResult = "";
		String result = dt.getExtraDeviceType ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getIMEI method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetIMEI () throws Exception
	{
		System.out.println ("getIMEI");
		DataTransporter dt = prepareDT();
		String expResult = "";
		String result = dt.getIMEI ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getSubscriberNumbers method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetSubscriberNumbers () throws Exception
	{
		System.out.println ("getSubscriberNumbers");
		DataTransporter dt = prepareDT();
		String expResult = "";
		String result = dt.getSubscriberNumbers ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getCapabilities method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetCapabilities () throws Exception
	{
		System.out.println ("getCapabilities");
		DataTransporter dt = prepareDT();
		String type = "";
		String expResult = "";
		String result = dt.getCapabilities (type);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of poweroff method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testPoweroff () throws Exception
	{
		System.out.println ("poweroff");
		DataTransporter dt = prepareDT();
		dt.poweroff ();
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getPINStatus method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetPINStatus () throws Exception
	{
		System.out.println ("getPINStatus");
		DataTransporter dt = prepareDT();
		PIN_STATUS expResult = null;
		PIN_STATUS result = dt.getPINStatus ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendPIN method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSendPIN_String_String () throws Exception
	{
		System.out.println ("sendPIN");
		DataTransporter dt = prepareDT();
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
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSendPIN_int_int () throws Exception
	{
		System.out.println ("sendPIN");
		DataTransporter dt = prepareDT();
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
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetNumberOfAlarms () throws Exception
	{
		System.out.println ("getNumberOfAlarms");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getNumberOfAlarms ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteAlarm method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testDeleteAlarm () throws Exception
	{
		System.out.println ("deleteAlarm");
		DataTransporter dt = prepareDT();
		int number = 0;
		int expResult = 0;
		int result = dt.deleteAlarm (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of addAlarm method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testAddAlarm () throws Exception
	{
		System.out.println ("addAlarm");
		DataTransporter dt = prepareDT();
		PhoneAlarm al = null;
		int expResult = 0;
		int result = dt.addAlarm (al);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getAlarms method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetAlarms () throws Exception
	{
		System.out.println ("getAlarms");
		DataTransporter dt = prepareDT();
		Vector<PhoneAlarm> expResult = null;
		Vector<PhoneAlarm> result = dt.getAlarms ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testDeleteMessage () throws Exception
	{
		System.out.println ("deleteMessage");
		DataTransporter dt = prepareDT();
		int number = 0;
		int expResult = 0;
		int result = dt.deleteMessage (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getMessages method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetMessages () throws Exception
	{
		System.out.println ("getMessages");
		DataTransporter dt = prepareDT();
		Vector<PhoneMessage> expResult = null;
		Vector<PhoneMessage> result = dt.getMessages ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetMessage () throws Exception
	{
		System.out.println ("getMessage");
		DataTransporter dt = prepareDT();
		int number = 0;
		PhoneMessage expResult = null;
		PhoneMessage result = dt.getMessage (number);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSendMessage () throws Exception
	{
		System.out.println ("sendMessage");
		DataTransporter dt = prepareDT();
		PhoneMessage msg = null;
		int expResult = 0;
		int result = dt.sendMessage (msg);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setMessageStorage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSetMessageStorage () throws Exception
	{
		System.out.println ("setMessageStorage");
		DataTransporter dt = prepareDT();
		STORAGE_TYPE stor = null;
		int expResult = 0;
		int result = dt.setMessageStorage (stor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setDTR method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSetDTR () throws Exception
	{
		System.out.println ("setDTR");
		DataTransporter dt = prepareDT();
		boolean on = false;
		dt.setDTR (on);
		boolean expResult = false;
		boolean result = dt.isDTR ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of setRTS method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSetRTS () throws Exception
	{
		System.out.println ("setRTS");
		DataTransporter dt = prepareDT();
		boolean on = true;
		dt.setRTS (on);
		boolean expResult = true;
		boolean result = dt.isRTS ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isCD method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsCD () throws Exception
	{
		System.out.println ("isCD");
		DataTransporter dt = prepareDT();
		boolean expResult = false;
		boolean result = dt.isCD ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isCTS method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsCTS () throws Exception
	{
		System.out.println ("isCTS");
		DataTransporter dt = prepareDT();
		boolean expResult = false;
		boolean result = dt.isCTS ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isDSR method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsDSR () throws Exception
	{
		System.out.println ("isDSR");
		DataTransporter dt = prepareDT();
		boolean expResult = false;
		boolean result = dt.isDSR ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isDTR method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsDTR () throws Exception
	{
		System.out.println ("isDTR");
		DataTransporter dt = prepareDT();
		boolean expResult = true;
		boolean result = dt.isDTR ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isRI method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsRI () throws Exception
	{
		System.out.println ("isRI");
		DataTransporter dt = prepareDT();
		boolean expResult = false;
		boolean result = dt.isRI ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of isRTS method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testIsRTS () throws Exception
	{
		System.out.println ("isRTS");
		DataTransporter dt = prepareDT();
		boolean expResult = false;
		boolean result = dt.isRTS ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getSignalPower method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetSignalPower () throws Exception
	{
		System.out.println ("getSignalPower");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getSignalPower ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of dialNumber method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testDialNumber () throws Exception
	{
		System.out.println ("dialNumber");
		DataTransporter dt = prepareDT();
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
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testHangup () throws Exception
	{
		System.out.println ("hangup");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.hangup ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of answer method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testAnswer () throws Exception
	{
		System.out.println ("answer");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.answer ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getVolume method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetVolume () throws Exception
	{
		System.out.println ("getVolume");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getVolume ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of setVolume method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSetVolume () throws Exception
	{
		System.out.println ("setVolume");
		DataTransporter dt = prepareDT();
		int vol = 0;
		int expResult = 0;
		int result = dt.setVolume (vol);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getAvailableBytes method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testGetAvailableBytes () throws Exception
	{
		System.out.println ("getAvailableBytes");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getAvailableBytes ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}
}
