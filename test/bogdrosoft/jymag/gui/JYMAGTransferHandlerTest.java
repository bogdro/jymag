/*
 * JYMAGTransferHandlerTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
package bogdrosoft.jymag.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.activation.DataHandler;
import javax.swing.TransferHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JYMAGTransferHandlerTest - a test for the JYMAGTransferHandler class.
 * @author Bogdan Drozdowski
 */
public class JYMAGTransferHandlerTest
{
	/**
	 * Test of canImport method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testCanImportNoDropJavaFileListFlavor()
	{
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new DataHandler(mw,
			DataFlavor.javaFileListFlavor.getMimeType()
		);
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		//support.setDropAction(TransferHandler.COPY);
		assertFalse(handler.canImport(support));
	}

	/**
	 * Test of canImport method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testCanImportNull()
	{
		JYMAGTransferHandler handler = new JYMAGTransferHandler(
			new MainWindow()
		);
		assertFalse(handler.canImport(null));
	}

	/**
	 * Test of canImport method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testCanImportWrongType()
	{
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new StringSelection("aaa");
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		assertFalse(handler.canImport(support));
	}

	/**
	 * Test of canImport method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testCanImportNonDrop()
	{
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new DataHandler(mw,
			DataFlavor.javaFileListFlavor.getMimeType()
		);
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		//support.setDropAction(TransferHandler.COPY);
		assertFalse(handler.canImport(support));
	}

	/**
	 * Test of canImport method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testCanImportNonCopy()
	{
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new DataHandler(mw,
			DataFlavor.javaFileListFlavor.getMimeType()
		);
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		//support.setDropAction(TransferHandler.MOVE);
		assertFalse(handler.canImport(support));
	}

	/**
	 * Test of importData method, of class JYMAGTransferHandler.
	 */
	@Test
	public void testImportData()
	{
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new DataHandler(mw,
			DataFlavor.javaFileListFlavor.getMimeType()
		);
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		//support.setDropAction(TransferHandler.COPY);
		assertFalse(handler.importData(support));
	}
}
