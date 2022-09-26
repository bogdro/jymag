/*
 * TransferUtilsTest.java, part of the JYMAG package.
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
import gnu.io.CommPortIdentifier;
import gnu.io.CommPortIdentifierStub;
import java.awt.Component;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TransferUtilsTest - a test for the TransferUtils class.
 * @author Bogdan Drozdowski
 */
public class TransferUtilsTest
{
	private static CommPortIdentifier id;
	private static Object sync;
	private static Component parent;
	private static int speed;
	private static int dataBits;
	private static float stopBits;
	private static int parity;
	private static int flow;
	private static Runnable onDone;
	private static boolean quiet;
	private static boolean quietGUI;
	private static boolean waitFor;

	public TransferUtilsTest ()
	{
	}

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		id = new CommPortIdentifierStub ("testPort", null, 0, null);
		sync = new Object ();
		parent = null;
		speed = 115200;
		dataBits = 8;
		stopBits = 1.0F;
		parity = 0;
		flow = 0;
		onDone = null;
		quiet = true;
		quietGUI = true;
		waitFor = true;
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
	 * Test of uploadFile method, of class TransferUtils.
	 */
	@Test
	public void testUploadFile ()
	{
		System.out.println ("uploadFile");
		File f = null;
		int expResult = 0;
		int result = TransferUtils.uploadFile (f, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of downloadFile method, of class TransferUtils.
	 */
	@Test
	public void testDownloadFile ()
	{
		System.out.println ("downloadFile");
		File f = null;
		PhoneElement element = null;
		int expResult = 0;
		int result = TransferUtils.downloadFile (f, element, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteFile method, of class TransferUtils.
	 */
	@Test
	public void testDeleteFile ()
	{
		System.out.println ("deleteFile");
		PhoneElement element = null;
		int expResult = 0;
		int result = TransferUtils.deleteFile (element, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of uploadAlarm method, of class TransferUtils.
	 */
	@Test
	public void testUploadAlarm ()
	{
		System.out.println ("uploadAlarm");
		PhoneAlarm alarm = null;
		int expResult = 0;
		int result = TransferUtils.uploadAlarm (alarm, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteAlarm method, of class TransferUtils.
	 */
	@Test
	public void testDeleteAlarm ()
	{
		System.out.println ("deleteAlarm");
		int alarmNo = 0;
		int expResult = 0;
		int result = TransferUtils.deleteAlarm (alarmNo, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of downloadFiles method, of class TransferUtils.
	 */
	@Test
	public void testDownloadFiles ()
	{
		System.out.println ("downloadFiles");
		String type = "";
		String destDir = "";
		boolean deleteAfterDownload = false;
		int expResult = 0;
		int result = TransferUtils.downloadFiles (type, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor, destDir, deleteAfterDownload);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of downloadList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadList ()
	{
		System.out.println ("downloadList");
		String ofWhat = "";
		DefaultTableModel dtm = null;
		Vector<PhoneElement> placeForData = null;
		int expResult = 0;
		int result = TransferUtils.downloadList (ofWhat, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor, dtm, placeForData);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of downloadAlarmList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadAlarmList ()
	{
		System.out.println ("downloadAlarmList");
		JTable alarms = null;
		Vector<PhoneAlarm> placeForData = null;
		int expResult = 0;
		int result = TransferUtils.downloadAlarmList (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor, alarms, placeForData);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of downloadMessageList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadMessageList ()
	{
		System.out.println ("downloadMessageList");
		JTable messages = null;
		Vector<PhoneMessage> placeForData = null;
		int expResult = 0;
		int result = TransferUtils.downloadMessageList (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor, messages, placeForData);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of deleteMessage method, of class TransferUtils.
	 */
	@Test
	public void testDeleteMessage ()
	{
		System.out.println ("deleteMessage");
		PhoneMessage element = null;
		int expResult = 0;
		int result = TransferUtils.deleteMessage (element, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendMessage method, of class TransferUtils.
	 */
	@Test
	public void testSendMessage ()
	{
		System.out.println ("sendMessage");
		PhoneMessage element = null;
		int expResult = 0;
		int result = TransferUtils.sendMessage (element, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of dialNumber method, of class TransferUtils.
	 */
	@Test
	public void testDialNumber ()
	{
		System.out.println ("dialNumber");
		String number = "";
		boolean isVoice = false;
		DIAL_MODE dialMode = null;
		int expResult = 0;
		int result = TransferUtils.dialNumber (number, isVoice, dialMode, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of hangup method, of class TransferUtils.
	 */
	@Test
	public void testHangup ()
	{
		System.out.println ("hangup");
		int expResult = 0;
		int result = TransferUtils.hangup (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of answer method, of class TransferUtils.
	 */
	@Test
	public void testAnswer ()
	{
		System.out.println ("answer");
		int expResult = 0;
		int result = TransferUtils.answer (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of volumeUp method, of class TransferUtils.
	 */
	@Test
	public void testVolumeUp ()
	{
		System.out.println ("volumeUp");
		int expResult = 0;
		int result = TransferUtils.volumeUp (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of volumeDown method, of class TransferUtils.
	 */
	@Test
	public void testVolumeDown ()
	{
		System.out.println ("volumeDown");
		int expResult = 0;
		int result = TransferUtils.volumeDown (id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of sendFileAsCommands method, of class TransferUtils.
	 */
	@Test
	public void testSendFileAsCommands ()
	{
		System.out.println ("sendFileAsCommands");
		File f = null;
		int expResult = 0;
		int result = TransferUtils.sendFileAsCommands (f, id, speed, dataBits, stopBits, parity, flow, onDone, parent, sync, quiet, quietGUI, waitFor);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getIdentifierForPort method, of class TransferUtils.
	 */
	@Test
	public void testGetIdentifierForPort ()
	{
		System.out.println ("getIdentifierForPort");
		String port = "";
		CommPortIdentifier expResult = null;
		CommPortIdentifier result = TransferUtils.getIdentifierForPort (port);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of scanPorts method, of class TransferUtils.
	 */
	@Test
	public void testScanPorts ()
	{
		System.out.println ("scanPorts");
		Hashtable<String, String> firmwares = null;
		Hashtable<String, String> phoneTypes = null;
		Hashtable<String, String> phoneIMEIs = null;
		Hashtable<String, String> phoneSubsNums = null;
		Vector<String> replied = null;
		Runnable afterPort = null;
		int expResult = 0;
		int result = TransferUtils.scanPorts (quiet, speed, dataBits, stopBits, parity, flow, firmwares, phoneTypes, phoneIMEIs, phoneSubsNums, replied, afterPort);
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}

	/**
	 * Test of getSerialPortNames method, of class TransferUtils.
	 */
	@Test
	public void testGetSerialPortNames ()
	{
		System.out.println ("getSerialPortNames");
		Vector<String> expResult = null;
		Vector<String> result = TransferUtils.getSerialPortNames ();
		assertEquals (expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail ("The test case is a prototype.");
	}
}
