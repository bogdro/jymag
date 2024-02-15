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

import bogdrosoft.jymag.Utils;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.TransferHandler;
import static org.junit.Assert.*;
import org.junit.Test;

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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new TestTransferable(new Object());
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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new TestTransferable(new Object());
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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new TestTransferable(new Object());
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
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		JYMAGTransferHandler handler = new JYMAGTransferHandler(mw);
		Transferable transferable = new TestTransferable(new Object());
		TransferHandler.TransferSupport support
			= new TransferHandler.TransferSupport(mw, transferable);
		//support.setDropAction(TransferHandler.COPY);
		assertFalse(handler.importData(support));
	}

	private static class TestTransferable implements Transferable
	{
		private static final String URI_FILE_LIST_FLAVOR_TYPE
			= "text/uri-list;class=java.lang.String";	// NOI18N
		private static transient DataFlavor[] flavors;
		private final Object transferData;

		static
		{
			flavors = new DataFlavor[1];
			try
			{
				flavors[0] = new DataFlavor (
					URI_FILE_LIST_FLAVOR_TYPE);
			}
			catch (ClassNotFoundException ex)
			{
				Utils.handleException (ex,
					"JYMAGTransferHandler.uriFileListFlavor");	// NOI18N
			}
		}

		private TestTransferable(Object data)
		{
			transferData = data;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return flavor != null && flavor.equals(flavors[0]);
		}

		@Override
		public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
		{
			if (flavor != null && flavor.equals(flavors[0]))
			{
				return transferData;
			}
			return null;
		}
	}
}
