/*
 * JYMAGTransferHandler.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2012 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.TransferHandler;

/**
 * JYMAGTransferHandler - a class that allows users to drag and drop files
 * onto the main JYMAG window. The dropped files will then be sent directly to the phone.
 * @author Bogdan Drozdowski
 */
public class JYMAGTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 77L;

	private final JComboBox portCombo;
	private final JComboBox speedCombo;
	private final JComboBox dataBitsCombo;
	private final JComboBox stopBitsCombo;
	private final JComboBox parityCombo;
	private final JCheckBox flowSoftBox;
	private final JCheckBox flowHardBox;
	private final Object sync;
	private final JFrame parent;

	// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
	private static volatile DataFlavor uriFileListFlavor = null;
	private static final String uriFileListFlavorType = "text/uri-list;class=java.lang.String";	// NOI18N

	private static final String newLines = "\r\n";	// NOI18N

	/**
	 * Creates a new TransferHandler that takes its transfer
	 *	parameters from the given fields.
	 * @param ports The combo box with the list of the ports. Can't be null.
	 * @param speeds The combo box with the list of the transfer rates. Can't be null.
	 * @param dataBits The combo box with the number of data bits. Can't be null.
	 * @param stopBits The combo box with the number of stop bits. Can't be null.
	 * @param parities The combo box with the list of parity modes. Can't be null.
	 * @param flowSoft The check box for software flow control. Can't be null.
	 * @param flowHard The check box for hardware flow control. Can't be null.
	 * @param synchro The synchronization variable. Can't be null.
	 * @param parentFrame The parent frame.
	 */
	public JYMAGTransferHandler (JComboBox ports, JComboBox speeds,
		JComboBox dataBits, JComboBox stopBits, JComboBox parities,
		JCheckBox flowSoft, JCheckBox flowHard, Object synchro,
		JFrame parentFrame)
	{
		if ( ports == null || speeds == null || dataBits == null
			|| stopBits == null || parities == null || flowSoft == null
			|| flowHard == null || synchro == null )
		{
			throw new NullPointerException ("JYMAGTransferHandler: null");	// NOI18N
		}
		portCombo = ports;
		speedCombo = speeds;
		dataBitsCombo = dataBits;
		stopBitsCombo = stopBits;
		parityCombo = parities;
		flowSoftBox = flowSoft;
		flowHardBox = flowHard;
		sync = synchro;
		parent = parentFrame;

		// Linux:
		// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
		if ( uriFileListFlavor == null )
		{
			try
			{
				uriFileListFlavor = new DataFlavor (uriFileListFlavorType);
			}
			catch (ClassNotFoundException ex)
			{
				Utils.handleException (ex,
					"JYMAGTransferHandler.uriFileListFlavor");	// NOI18N
			}
		}
	}

	/**
	 * Checks if JYMAG can accept the given transfer.
	 * @param support The transfer to check.
	 * @return true if JYMAG can accept the given transfer, false otherwise.
	 */
	@Override
	public boolean canImport (TransferHandler.TransferSupport support)
	{
		if ( support == null ) return false;
		if ( ! support.isDrop () ) return false;
		if ( (support.getSourceDropActions () & COPY) != COPY ) return false;
		// MS Windows:
		if ( support.isDataFlavorSupported (DataFlavor.javaFileListFlavor) ) return true;
		// Linux:
		// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
		if ( uriFileListFlavor != null )
		{
			if ( support.isDataFlavorSupported (uriFileListFlavor) ) return true;
		}
		return false;
	}

	/**
	 * Imports the given data into JYMAG.
	 * @param support The transfer to import.
	 * @return true if JYMAG accepted the given transfer, false otherwise.
	 */
	@Override
	public boolean importData (TransferHandler.TransferSupport support)
	{
		if ( ! canImport (support) ) return false;

		Transferable t = support.getTransferable ();
		try
		{
			// MS Windows:
			if ( t.isDataFlavorSupported (DataFlavor.javaFileListFlavor) )
			{
				Object data = t.getTransferData (DataFlavor.javaFileListFlavor);
				if ( data != null && data instanceof List<?> )
				{
					List<?> fileList = (List<?>) data;
					for ( int i = 0; i < fileList.size (); i++ )
					{
						Object f = fileList.get (i);
						if ( f != null && f instanceof File )
						{
							importSingleFile ((File)f);
						}
					}
					return true;
				}
			}
			// Linux:
			else if ( uriFileListFlavor != null )
			{
				if ( t.isDataFlavorSupported (uriFileListFlavor) )
				{
					Object data = t.getTransferData (uriFileListFlavor);
					if ( data != null )
					{
						String[] fileURIs = data.toString ().split (newLines);
						if ( fileURIs != null )
						{
							for ( String s : fileURIs )
							{
								if ( s == null ) continue;
								// comment:
								if ( s.charAt (0) == '#' ) continue;
								try
								{
									importSingleFile (new File (new URI (s)));
								}
								catch (URISyntaxException uriex)
								{
									Utils.handleException (uriex,
										"JYMAGTransferHandler.importData.Transferable.URI");	// NOI18N
								}
							}
						}
					}
				}
			}
		}
		catch (UnsupportedFlavorException ufex)
		{
			Utils.handleException (ufex,
				"JYMAGTransferHandler.importData.Transferable.getTransferData");	// NOI18N
		}
		catch (IOException ioex)
		{
			Utils.handleException (ioex,
				"JYMAGTransferHandler.importData.Transferable.getTransferData");	// NOI18N
		}
		return false;
	}

	/**
	 * Imports a single File object into JYMAG.
	 * @param f The File to import. If the File is a directory,
	 *	it is recursively browsed and all the contained files are imported.
	 */
	private void importSingleFile (File f)
	{
		if ( f == null ) return;
		if ( ! f.exists () ) return;
		if ( f.isDirectory () )
		{
			File[] files = f.listFiles ();
			if ( files != null )
			{
				for ( File cf : files )
				{
					importSingleFile (cf);
				}
			}
			return;
		}
		if ( f.isFile () )
		{
			TransferUtils.uploadFile (f, TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()),
				Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoftBox.isSelected ())? 1 : 0) + ((flowHardBox.isSelected ())? 2 : 0),
				null, parent, sync, false, false, true);
		}
	}
}

