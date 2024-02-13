/*
 * DataTransporterTest.java, part of the JYMAG package.
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

package bogdrosoft.jymag.comm;

import bogdrosoft.jymag.PhoneAlarm;
import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.comm.DataTransporter.DIAL_MODE;
import bogdrosoft.jymag.comm.DataTransporter.PIN_STATUS;
import bogdrosoft.jymag.comm.DataTransporter.STORAGE_TYPE;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import bogdrosoft.jymag.comm.fake.FakeSerialPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * DataTransporterTest - a test for the DataTransporter class.
 * @author Bogdan Drozdowski
 */
public class DataTransporterTest
{
	private static final String FILENAME = "test.jpg";

	private static final PhoneElement PICTURE_ELEMENT =
		new PhoneElement("5303650005022001FFFF", "FGIF", "TestPicture");

	private static PhoneMessage MESSAGE;

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		MESSAGE = new PhoneMessage();
		MESSAGE.setDateTime("08/08/02,06:30:00+00");
		MESSAGE.setID("1");
		MESSAGE.setMessage("Test");
		MESSAGE.setRecipientNum("+001123456789");
		MESSAGE.setStatus("STOR");
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
		new File(FILENAME).delete();
	}

	private static DataTransporter prepareDT() throws Exception
	{
		Object id = new FakeCommPortIdentifier();
		int speed = 115200;
		int dataBits = SerialPort.DATABITS_8;
		float stopBits = SerialPort.STOPBITS_1;
		int parity = SerialPort.PARITY_NONE;
		int flowControl = SerialPort.FLOWCONTROL_NONE;
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
		DataTransporter dt = prepareDT();
		assertFalse(dt.isCTS());
	}

	/**
	 * Test of open method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test(expected=IOException.class)
	public void testOpenFail () throws Exception
	{
		System.out.println ("open");
		CommPortIdentifier id = mock (CommPortIdentifier.class);
			//new CommPortIdentifierStub ("testPort", null, 0, null);
		when(id.getName ()).thenReturn ("fake");
		when(id.open (anyString (), anyInt ())).thenReturn (new FakeSerialPort());
		DataTransporter dt = new DataTransporter (id);
		int speed = 115200;
		int dataBits = SerialPort.DATABITS_8;
		float stopBits = SerialPort.STOPBITS_1;
		int parity = SerialPort.PARITY_NONE;
		int flowControl = SerialPort.FLOWCONTROL_NONE;
		dt.open (speed, dataBits, stopBits, parity, flowControl);
	}

	/**
	 * Test of recv method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRecv () throws Exception
	{
		System.out.println ("recv");
		DataTransporter dt = prepareDT();
		Object[] extraTerminators = null;
		dt.send("AT\r".getBytes());
		byte[] expResult = "OK\r".getBytes();
		byte[] result = dt.recv (extraTerminators);
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of send method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSendByteArray () throws Exception
	{
		System.out.println ("send");
		DataTransporter dt = prepareDT();
		dt.send("AT\r".getBytes());
	}

	/**
	 * Test of send method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSend3args () throws Exception
	{
		System.out.println ("send");
		DataTransporter dt = prepareDT();
		byte[] b = "AAT\r".getBytes();
		int start = 1;
		int length = 3;
		dt.send (b, start, length);
	}

	/**
	 * Test of close method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testClose () throws Exception
	{
		System.out.println ("close");
		DataTransporter dt = prepareDT();
		dt.close ();
	}

	/**
	 * Test of putFile method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testPutFile () throws Exception
	{
		System.out.println ("putFile");
		DataTransporter dt = prepareDT();
		File f = new File(FILENAME);
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(new byte[] {1, 2, 3});
		fos.close();
		String newName = FILENAME;
		int expResult = 0;
		int result = dt.putFile (f, newName);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFile method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetFile () throws Exception
	{
		System.out.println ("getFile");
		DataTransporter dt = prepareDT();
		File f = new File(FILENAME);
		PhoneElement el = new PhoneElement("1", "jpg", FILENAME);
		int expResult = 0;
		int result = dt.getFile (f, el);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getList method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetList () throws Exception
	{
		System.out.println ("getList");
		DataTransporter dt = prepareDT();
		String ofWhat = "PICTURES";
		Vector<PhoneElement> expResult = new Vector<PhoneElement>(1);
		expResult.add(PICTURE_ELEMENT);
		Vector<PhoneElement> result = dt.getList (ofWhat);
		assertEquals (expResult, result);
	}

	/**
	 * Test of deleteFile method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteFile () throws Exception
	{
		System.out.println ("deleteFile");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.deleteFile (PICTURE_ELEMENT);
		assertEquals (expResult, result);
	}

	/**
	 * Test of reopen method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReopen () throws Exception
	{
		System.out.println ("reopen");
		DataTransporter dt = prepareDT();
		dt.reopen ();
		assertFalse(dt.isCTS());
	}

	/**
	 * Test of test method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testTest () throws Exception
	{
		System.out.println ("test");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.test ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFirmwareVersion method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetFirmwareVersion () throws Exception
	{
		System.out.println ("getFirmwareVersion");
		DataTransporter dt = prepareDT();
		String expResult = "2.04";
		String result = dt.getFirmwareVersion ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDeviceType method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetDeviceType () throws Exception
	{
		System.out.println ("getDeviceType");
		DataTransporter dt = prepareDT();
		String expResult = "myX5-2 GPRS";
		String result = dt.getDeviceType ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getExtraDeviceType method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetExtraDeviceType () throws Exception
	{
		System.out.println ("getExtraDeviceType");
		DataTransporter dt = prepareDT();
		String expResult = "FakeSAGEM KB3,ME";
		String result = dt.getExtraDeviceType ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getIMEI method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetIMEI () throws Exception
	{
		System.out.println ("getIMEI");
		DataTransporter dt = prepareDT();
		String expResult = "353512345678901";
		String result = dt.getIMEI ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getSubscriberNumbers method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetSubscriberNumbers () throws Exception
	{
		System.out.println ("getSubscriberNumbers");
		DataTransporter dt = prepareDT();
		String expResult = "+123456789";
		String result = dt.getSubscriberNumbers ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getCapabilities method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetCapabilities () throws Exception
	{
		System.out.println ("getCapabilities");
		DataTransporter dt = prepareDT();
		String type = "PICTURES";
		String expResult = "CAPABILITY\r\n";
		String result = dt.getCapabilities (type);
		assertEquals (expResult, result);
	}

	/**
	 * Test of poweroff method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testPoweroff () throws Exception
	{
		System.out.println ("poweroff");
		DataTransporter dt = prepareDT();
		dt.poweroff ();
	}

	/**
	 * Test of getPINStatus method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetPINStatus () throws Exception
	{
		System.out.println ("getPINStatus");
		DataTransporter dt = prepareDT();
		PIN_STATUS expResult = null;
		PIN_STATUS result = dt.getPINStatus ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of sendPIN method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSendPINWithStringString () throws Exception
	{
		System.out.println ("sendPIN");
		DataTransporter dt = prepareDT();
		String PIN = "1234";
		String newPIN = "2345";
		int expResult = -1;
		int result = dt.sendPIN (PIN, newPIN);
		assertEquals (expResult, result);
	}

	/**
	 * Test of sendPIN method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSendPINWithIntInt () throws Exception
	{
		System.out.println ("sendPIN");
		DataTransporter dt = prepareDT();
		int PIN = 1234;
		int newPIN = 2345;
		int expResult = -1;
		int result = dt.sendPIN (PIN, newPIN);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getNumberOfAlarms method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetNumberOfAlarms () throws Exception
	{
		System.out.println ("getNumberOfAlarms");
		DataTransporter dt = prepareDT();
		int expResult = 1;
		int result = dt.getNumberOfAlarms ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of deleteAlarm method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteAlarm () throws Exception
	{
		System.out.println ("deleteAlarm");
		DataTransporter dt = prepareDT();
		int number = 1;
		int expResult = 0;
		int result = dt.deleteAlarm (number);
		assertEquals (expResult, result);
	}

	/**
	 * Test of addAlarm method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAddAlarm () throws Exception
	{
		System.out.println ("addAlarm");
		DataTransporter dt = prepareDT();
		PhoneAlarm al = new PhoneAlarm(1, "03/04/22", "06:00:00", "0");
		int expResult = 0;
		int result = dt.addAlarm (al);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getAlarms method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetAlarms () throws Exception
	{
		System.out.println ("getAlarms");
		DataTransporter dt = prepareDT();
		Vector<PhoneAlarm> expResult = new Vector<PhoneAlarm>(1);
		PhoneAlarm alarm = new PhoneAlarm(0, "08/08/02", "06:30:00", null);
		alarm.setOneTimeAlarm(true);
		alarm.setForAllDays(false);
		expResult.add(alarm);
		Vector<PhoneAlarm> result = dt.getAlarms ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of deleteMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteMessage () throws Exception
	{
		System.out.println ("deleteMessage");
		DataTransporter dt = prepareDT();
		int number = 1;
		int expResult = 0;
		int result = dt.deleteMessage (number);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getMessages method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetMessages () throws Exception
	{
		System.out.println ("getMessages");
		DataTransporter dt = prepareDT();
		Vector<PhoneMessage> expResult = new Vector<PhoneMessage>(1);
		expResult.add(MESSAGE);
		Vector<PhoneMessage> result = dt.getMessages ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetMessage () throws Exception
	{
		System.out.println ("getMessage");
		DataTransporter dt = prepareDT();
		int number = 1;
		PhoneMessage result = dt.getMessage (number);
		assertEquals (MESSAGE, result);
	}

	/**
	 * Test of sendMessage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSendMessage () throws Exception
	{
		System.out.println ("sendMessage");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.sendMessage (MESSAGE);
		assertEquals (expResult, result);
	}

	/**
	 * Test of setMessageStorage method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSetMessageStorage () throws Exception
	{
		System.out.println ("setMessageStorage");
		DataTransporter dt = prepareDT();
		STORAGE_TYPE stor = STORAGE_TYPE.SM;
		int expResult = 0;
		int result = dt.setMessageStorage (stor);
		assertEquals (expResult, result);
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
		boolean expResult = false;
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
	public void testGetSignalPower () throws Exception
	{
		System.out.println ("getSignalPower");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getSignalPower ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of dialNumber method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDialNumber () throws Exception
	{
		System.out.println ("dialNumber");
		DataTransporter dt = prepareDT();
		String number = "+00123456789";
		boolean isVoice = false;
		DIAL_MODE dialMode = DIAL_MODE.TONE;
		int expResult = 0;
		int result = dt.dialNumber (number, isVoice, dialMode);
		assertNotEquals (expResult, result);
	}

	/**
	 * Test of hangup method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testHangup () throws Exception
	{
		System.out.println ("hangup");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.hangup ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of answer method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAnswer () throws Exception
	{
		System.out.println ("answer");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.answer ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getVolume method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetVolume () throws Exception
	{
		System.out.println ("getVolume");
		DataTransporter dt = prepareDT();
		int expResult = 1;
		int result = dt.getVolume ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of setVolume method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSetVolume () throws Exception
	{
		System.out.println ("setVolume");
		DataTransporter dt = prepareDT();
		int vol = 0;
		int expResult = 0;
		int result = dt.setVolume (vol);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getAvailableBytes method, of class DataTransporter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetAvailableBytes () throws Exception
	{
		System.out.println ("getAvailableBytes");
		DataTransporter dt = prepareDT();
		int expResult = 0;
		int result = dt.getAvailableBytes ();
		assertEquals (expResult, result);
	}
}
