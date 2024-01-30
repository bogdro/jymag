/*
 * JYMAGTransferHandler.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import bogdrosoft.jymag.comm.TransferUtils;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.TransferHandler;

/**
 * JYMAGTransferHandler - a class that allows users to drag and drop files
 * onto the main JYMAG window. The dropped files will then be sent directly to the phone.
 * @author Bogdan Drozdowski
 */
public class JYMAGTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 77L;

	private final MainWindow mw;

	// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
	private static transient DataFlavor uriFileListFlavor;
	private static final String URI_FILE_LIST_FLAVOR_TYPE = "text/uri-list;class=java.lang.String";	// NOI18N

	private static final String NEWLINES = "\r\n";	// NOI18N

	static
	{
		// Linux:
		// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
		try
		{
			uriFileListFlavor = new DataFlavor (URI_FILE_LIST_FLAVOR_TYPE);
		}
		catch (ClassNotFoundException ex)
		{
			Utils.handleException (ex,
				"JYMAGTransferHandler.uriFileListFlavor");	// NOI18N
			uriFileListFlavor = null;
		}
	}

	/**
	 * Creates a new TransferHandler that takes its transfer
	 *	parameters from the given fields.
	 * @param parentFrame The parent frame.
	 */
	public JYMAGTransferHandler (MainWindow parentFrame)
	{
		if ( parentFrame == null)
		{
			throw new IllegalArgumentException ("JYMAGTransferHandler: null");	// NOI18N
		}
		mw = parentFrame;
	}

	/**
	 * Checks if JYMAG can accept the given transfer.
	 * @param support The transfer to check.
	 * @return true if JYMAG can accept the given transfer, false otherwise.
	 */
	@Override
	public boolean canImport (TransferHandler.TransferSupport support)
	{
		if ( support == null )
		{
			return false;
		}
		if ( ! support.isDrop () )
		{
			return false;
		}
		if ( (support.getSourceDropActions () & COPY) != COPY )
		{
			return false;
		}
		// MS Windows, some Linux systems:
		if ( support.isDataFlavorSupported (DataFlavor.javaFileListFlavor) )
		{
			return true;
		}
		// Linux:
		// http://www.davidgrant.ca/drag_drop_from_linux_kde_gnome_file_managers_konqueror_nautilus_to_java_applications
		return uriFileListFlavor != null
			&& support.isDataFlavorSupported (uriFileListFlavor);
	}

	/**
	 * Imports the given data into JYMAG.
	 * @param support The transfer to import.
	 * @return true if JYMAG accepted the given transfer, false otherwise.
	 */
	@Override
	public boolean importData (TransferHandler.TransferSupport support)
	{
		if ( ! canImport (support) )
		{
			return false;
		}

		Transferable t = support.getTransferable ();
		try
		{
			// MS Windows, some Linux systems:
			if ( t.isDataFlavorSupported (DataFlavor.javaFileListFlavor) )
			{
				Object data = t.getTransferData (DataFlavor.javaFileListFlavor);
				if ( data != null && data instanceof List<?> )
				{
					List<?> fileList = (List<?>) data;
					int fileListSize = fileList.size ();
					for ( int i = 0; i < fileListSize; i++ )
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
			else if ( uriFileListFlavor != null
				&& t.isDataFlavorSupported (uriFileListFlavor) )
			{
				Object data = t.getTransferData (uriFileListFlavor);
				if ( data != null )
				{
					String[] fileURIs = data.toString ().split (NEWLINES);
					if ( fileURIs != null )
					{
						for ( int i = 0; i < fileURIs.length; i++ )
						{
							String s = fileURIs[i];
							if ( s == null )
							{
								continue;
							}
							// comment:
							if ( s.charAt (0) == '#' )
							{
								continue;
							}
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
					return true;
				}
			}
		}
		catch (Throwable ioex)
		{
			Utils.handleException (ioex,
				"JYMAGTransferHandler.importData.Transferable.getTransferData");	// NOI18N
		}
		mw.setReadyStatus ();
		return false;
	}

	/**
	 * Imports a single File object into JYMAG.
	 * @param f The File to import. If the File is a directory,
	 *	it is recursively browsed and all the contained files are imported.
	 */
	private void importSingleFile (File f)
	{
		if ( f == null )
		{
			return;
		}
		if ( ! f.exists () )
		{
			return;
		}
		if ( f.isDirectory () )
		{
			File[] files = f.listFiles ();
			if ( files != null )
			{
				for ( int i = 0; i < files.length; i++ )
				{
					importSingleFile (files[i]);
				}
			}
			return;
		}
		if ( f.isFile () )
		{
			// NOTE: create new TransferParameters each time,
			// because the values of the GUI elements could have changed.
			mw.setSendingStatus ();
			TransferUtils.uploadFile (f, mw.getTransferParameters (),
				null, mw, false, false, true);
			mw.setReadyStatus ();
		}
	}
}
