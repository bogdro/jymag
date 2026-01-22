/*
 * PanelUtilsTest.java, part of the JYMAG package.
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
package bogdrosoft.jymag.gui.panels;

import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.TransferParameters;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import bogdrosoft.jymag.gui.MainWindow;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PanelUtilsTest - a test for the PanelUtils class.
 * @author Bogdan Drozdowski
 */
public class PanelUtilsTest
{
	private static final String FILENAME = "test.jpg";
	private static final String FILE_FORMAT = "jpg";

	/**
	 * Test of putListInTable method, of class PanelUtils.
	 */
	@Test
	public void testPutListInTable()
	{
		System.out.println("putListInTable");
		int count = 1;
		MainWindow mw = createMainWindow();
		String ofWhat = "PICTURES";
		DefaultTableModel dtm = new DefaultTableModel(count, 1);
		Vector<PhoneElement> placeForData = new Vector<PhoneElement>(count);
		PanelUtils.putListInTable(mw, ofWhat, dtm, placeForData);
		Utils.sleepIgnoreException(5000);
		assertEquals(count, placeForData.size());
	}

	/**
	 * Test of download method, of class PanelUtils.
	 */
	@Test
	@Ignore("Requires interactive user action")
	public void testDownload()
	{
		System.out.println("download");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[] {0};
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		JFileChooser downloadFC = new JFileChooser();
		String destDirName = null;
		PanelUtils.download(mw, selectedRows, elements, downloadFC, destDirName);
		assertTrue(new File(FILENAME).exists());
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDownloadNullRows()
	{
		System.out.println("testDownloadNullRows");
		MainWindow mw = createMainWindow();
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		JFileChooser downloadFC = new JFileChooser();
		String destDirName = null;
		PanelUtils.download(mw, null, elements, downloadFC, destDirName);
		verify(mw, never()).getTransferParameters();
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDownloadNullFileChooser()
	{
		System.out.println("testDownloadNullFileChooser");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[] {0};
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		String destDirName = null;
		PanelUtils.download(mw, selectedRows, elements, null, destDirName);
		verify(mw, never()).getTransferParameters();
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDownloadNoRows()
	{
		System.out.println("testDownloadNoRows");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[0];
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		JFileChooser downloadFC = new JFileChooser();
		String destDirName = null;
		PanelUtils.download(mw, selectedRows, elements, downloadFC, destDirName);
		verify(mw, never()).getTransferParameters();
	}

	/**
	 * Test of upload method, of class PanelUtils.
	 */
	@Test
	@Ignore("Requires interactive user action")
	public void testUpload()
	{
		System.out.println("upload");
		MainWindow mw = createMainWindow();
		JFileChooser uploadFC = new JFileChooser();
		try
		{
			PanelUtils.upload(mw, uploadFC);
		}
		catch (Exception ex)
		{
			fail("Exception during upload: " + ex);
		}
	}

	/**
	 * Test of upload method, of class PanelUtils.
	 */
	@Test
	public void testUploadNullFileChooser()
	{
		System.out.println("testUploadNullFileChooser");
		MainWindow mw = createMainWindow();
		try
		{
			PanelUtils.upload(mw, null);
		}
		catch (Exception ex)
		{
			fail("Exception during upload: " + ex);
		}
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	@Ignore("Requires interactive user action")
	public void testDelete()
	{
		System.out.println("delete");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[] {0};
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		try
		{
			PanelUtils.delete(mw, selectedRows, elements);
		}
		catch (Exception ex)
		{
			fail("Exception during delete: " + ex);
		}
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDeleteNullRows()
	{
		System.out.println("testDeleteNullRows");
		MainWindow mw = createMainWindow();
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		PanelUtils.delete(mw, null, elements);
		verify(mw, never()).getTransferParameters();
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDeleteNoRows()
	{
		System.out.println("testDeleteNoRows");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[0];
		List<PhoneElement> elements = new ArrayList<PhoneElement>(1);
		elements.add(new PhoneElement("1", FILE_FORMAT, FILENAME));
		PanelUtils.delete(mw, selectedRows, elements);
		verify(mw, never()).getTransferParameters();
	}

	/**
	 * Test of delete method, of class PanelUtils.
	 */
	@Test
	public void testDeleteNullElements()
	{
		System.out.println("testDeleteNullElements");
		MainWindow mw = createMainWindow();
		int[] selectedRows = new int[0];
		PanelUtils.delete(mw, selectedRows, null);
		verify(mw, never()).getTransferParameters();
	}

	private static MainWindow createMainWindow()
	{
		MainWindow mw = mock(MainWindow.class);
		TransferParameters tp = new TransferParameters(
			new FakeCommPortIdentifier(), 115200,
			8, 1, 0, 0, new Object()
		);
		when(mw.getTransferParameters()).thenReturn(tp);
		return mw;
	}
}
