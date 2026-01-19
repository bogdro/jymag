/*
 * TransferUtilsTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2024-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import bogdrosoft.jymag.CommandLineParser;
import bogdrosoft.jymag.PhoneAlarm;
import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import java.awt.Component;
import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * TransferUtilsTest - a test for the TransferUtils class.
 * @author Bogdan Drozdowski
 */
public class TransferUtilsTest
{
	private static final int SPEED = 115200;
	private static final int DATA_BITS = 8;
	private static final int STOP_BITS = 8;
	private static final int PARITY = 0;
	private static final int FLOW = 2;
	private static final FakeCommPortIdentifier PORT_ID
		= new FakeCommPortIdentifier();
	private static final Object SYNC = new Object();

	private static final String FILENAME = "TestPicture.gif";

	private static final PhoneElement PICTURE_ELEMENT =
		new PhoneElement("5303650005022001FFFF", "FGIF", "TestPicture");

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		CommandLineParser.mock = true;
		System.setProperty("mock", "138b7ce0632d70dd9d6fc7b571fd9199");
		new File(FILENAME).createNewFile();
	}

	@AfterClass
	public static void tearDownClass () throws Exception
	{
		new File(FILENAME).delete();
	}

	/**
	 * Test of uploadFile method, of class TransferUtils.
	 */
	@Test
	public void testUploadFile()
	{
		System.out.println("uploadFile");
		File f = new File(FILENAME);
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.uploadFile(f, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of downloadFile method, of class TransferUtils.
	 */
	@Test
	public void testDownloadFile()
	{
		System.out.println("downloadFile");
		File f = new File(FILENAME);
		PhoneElement element = PICTURE_ELEMENT;
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.downloadFile(f, element, tp,
			onDone, parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of deleteFile method, of class TransferUtils.
	 */
	@Test
	public void testDeleteFile()
	{
		System.out.println("deleteFile");
		PhoneElement element = PICTURE_ELEMENT;
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.deleteFile(element, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of uploadAlarm method, of class TransferUtils.
	 */
	@Test
	public void testUploadAlarm()
	{
		System.out.println("uploadAlarm");
		Calendar c = Calendar.getInstance ();
		PhoneAlarm alarm = new PhoneAlarm (c, false, true,
			(int[]) null, 1);
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.uploadAlarm(alarm, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of deleteAlarm method, of class TransferUtils.
	 */
	@Test
	public void testDeleteAlarm()
	{
		System.out.println("deleteAlarm");
		int alarmNo = 1;
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.deleteAlarm(alarmNo, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of downloadFiles method, of class TransferUtils.
	 */
	@Test
	public void testDownloadFiles()
	{
		System.out.println("downloadFiles");
		String type = "PICTURES";
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		String destDir = ".";
		boolean deleteAfterDownload = false;
		int expResult = 0;
		int result = TransferUtils.downloadFiles(type, tp, onDone,
			parent, quiet, quietGUI, waitFor, destDir,
			deleteAfterDownload);
		assertEquals(expResult, result);
	}

	/**
	 * Test of downloadList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadList()
	{
		System.out.println("downloadList");
		String ofWhat = "PICTURES";
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		DefaultTableModel dtm = null;
		Vector<PhoneElement> placeForData = new Vector<PhoneElement>(1);
		int expResult = 0;
		int result = TransferUtils.downloadList(ofWhat, tp, onDone,
			parent, quiet, quietGUI, waitFor, dtm, placeForData);
		assertEquals(expResult, result);
	}

	/**
	 * Test of downloadAlarmList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadAlarmList()
	{
		System.out.println("downloadAlarmList");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		JTable alarms = null;
		Vector<PhoneAlarm> placeForData = new Vector<PhoneAlarm>(1);
		int expResult = 0;
		int result = TransferUtils.downloadAlarmList(tp, onDone, parent,
			quiet, quietGUI, waitFor, alarms, placeForData);
		assertEquals(expResult, result);
	}

	/**
	 * Test of downloadMessageList method, of class TransferUtils.
	 */
	@Test
	public void testDownloadMessageList()
	{
		System.out.println("downloadMessageList");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		JTable messages = null;
		Vector<PhoneMessage> placeForData = new Vector<PhoneMessage>(1);
		int expResult = 0;
		int result = TransferUtils.downloadMessageList(tp, onDone,
			parent, quiet, quietGUI, waitFor, messages, placeForData);
		assertEquals(expResult, result);
	}

	/**
	 * Test of deleteMessage method, of class TransferUtils.
	 */
	@Test
	public void testDeleteMessage()
	{
		System.out.println("deleteMessage");
		PhoneMessage element = new PhoneMessage();
		element.setID("1");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.deleteMessage(element, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of sendMessage method, of class TransferUtils.
	 */
	@Test
	public void testSendMessage()
	{
		System.out.println("sendMessage");
		PhoneMessage element = new PhoneMessage();
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.sendMessage(element, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of dialNumber method, of class TransferUtils.
	 */
	@Test
	public void testDialNumber()
	{
		System.out.println("dialNumber");
		String number = "123456789";
		boolean isVoice = false;
		DataTransporter.DIAL_MODE dialMode
			= DataTransporter.DIAL_MODE.TONE;
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.dialNumber(number, isVoice, dialMode,
			tp, onDone, parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of hangup method, of class TransferUtils.
	 */
	@Test
	public void testHangup()
	{
		System.out.println("hangup");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.hangup(tp, onDone, parent,
			quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of answer method, of class TransferUtils.
	 */
	@Test
	public void testAnswer()
	{
		System.out.println("answer");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.answer(tp, onDone, parent,
			quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of volumeUp method, of class TransferUtils.
	 */
	@Test
	public void testVolumeUp()
	{
		System.out.println("volumeUp");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.volumeUp(tp, onDone, parent,
			quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of volumeDown method, of class TransferUtils.
	 */
	@Test
	public void testVolumeDown()
	{
		System.out.println("volumeDown");
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.volumeDown(tp, onDone, parent,
			quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of sendFileAsCommands method, of class TransferUtils.
	 */
	@Test
	public void testSendFileAsCommands()
	{
		System.out.println("sendFileAsCommands");
		File f = new File(FILENAME);
		TransferParameters tp = getTransferParameters();
		Runnable onDone = null;
		Component parent = null;
		boolean quiet = true;
		boolean quietGUI = true;
		boolean waitFor = true;
		int expResult = 0;
		int result = TransferUtils.sendFileAsCommands(f, tp, onDone,
			parent, quiet, quietGUI, waitFor);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getIdentifierForPort method, of class TransferUtils.
	 */
	@Test
	public void testGetIdentifierForPort()
	{
		System.out.println("getIdentifierForPort");
		String port = PORT_ID.getName();
		Object result = TransferUtils.getIdentifierForPort(port);
		assertEquals(PORT_ID.getName(),
			((FakeCommPortIdentifier)result).getName());
	}

	/**
	 * Test of scanPorts method, of class TransferUtils.
	 */
	@Test
	public void testScanPorts()
	{
		System.out.println("scanPorts");
		boolean quiet = false;
		TransferParameters tp = getTransferParameters();
		Map<String, String> firmwares = null;
		Map<String, String> phoneTypes = null;
		Map<String, String> phoneIMEIs = null;
		Map<String, String> phoneSubsNums = null;
		Vector<String> replied = new Vector<String>(1);
		Runnable afterPort = null;
		int expResult = 0;
		int result = TransferUtils.scanPorts(quiet, tp, firmwares,
			phoneTypes, phoneIMEIs, phoneSubsNums, replied, afterPort);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getSerialPortNames method, of class TransferUtils.
	 */
	@Test
	public void testGetSerialPortNames()
	{
		System.out.println("getSerialPortNames");
		assertEquals(PORT_ID.getName(),
			TransferUtils.getSerialPortNames().get(0));
	}

	private TransferParameters getTransferParameters()
	{
		return new TransferParameters(
			PORT_ID, SPEED, DATA_BITS, STOP_BITS,
			PARITY, FLOW, SYNC);
	}
}
