/*
 * PanelUtils.java, part of the JYMAG package.
 *
 * Copyright (C) 2022-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import bogdrosoft.jymag.comm.TransferUtils;
import bogdrosoft.jymag.gui.MainWindow;
import bogdrosoft.jymag.gui.UiUtils;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * A utility class for the panel/tab classes.
 * @author Bogdan Drozdowski
 */
public class PanelUtils
{
	private PanelUtils ()
	{
		// non-instantiable
	}

	/**
	 * Puts the list of the given elements in the given table.
	 * @param mw The MainWindow to refer to.
	 * @param ofWhat Tells which elements should be downloaded.
	 * @param dtm The table to put the data in.
	 * @param placeForData The place for the downloaded elements.
	 */
	public static void putListInTable (final MainWindow mw,
		final String ofWhat,
		final DefaultTableModel dtm,
		final Vector<PhoneElement> placeForData)
	{
		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			mw.setReceivingStatus ();
			mw.setProgressCurrentValue (0);
			mw.setProgressMinimumValue (0);
			mw.setProgressMaximumValue (1);

			TransferUtils.downloadList (ofWhat, tp,
				new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						mw.setProgressCurrentValue (1);
						mw.setReadyStatus ();
					}

					@Override
					public String toString ()
					{
						return "PanelUtils.putListInTable.Runnable";	// NOI18N
					}
				}, mw, false, false, false, dtm, placeForData);
		}
		catch (Exception ex)
		{
			mw.setReadyStatus ();
			Utils.handleException (ex, "PanelUtils.putListInTable");	// NOI18N
		}
	}

	/**
	 * Downloads the selected elements and puts them in the directory
	 * pointed to by the file chooser.
	 * @param mw The MainWindow to refer to.
	 * @param selectedRows The indices of the selected elements.
	 * @param elements The list of the all elements.
	 * @param downloadFC The file chooser to use.
	 * @param destDirName The previously-chosen directory name, if any.
	 */
	public static void download(final MainWindow mw,
		int[] selectedRows,
		List<PhoneElement> elements,
		JFileChooser downloadFC,
		String destDirName)
	{
		if ( selectedRows == null || elements == null )
		{
			return;
		}
		if ( selectedRows.length == 0 )
		{
			return;
		}
		if ( downloadFC == null )
		{
			return;
		}

		downloadFC.setMultiSelectionEnabled (false);
		downloadFC.setDialogType (JFileChooser.SAVE_DIALOG);
		downloadFC.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		if ( destDirName != null && ! destDirName.isEmpty () )
		{
			File d = new File (destDirName);
			downloadFC.setSelectedFile (d);
			downloadFC.setCurrentDirectory (d);
		}
		int dialogRes = downloadFC.showSaveDialog (mw);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			String dstFileName = null;
			try
			{
				File destDir = downloadFC.getSelectedFile ();
				if ( destDir == null )
				{
					return;
				}
				mw.setProgressCurrentValue (0);
				mw.setProgressMinimumValue (0);
				mw.setProgressMaximumValue (selectedRows.length);
				final AtomicInteger threads = new AtomicInteger (0);
				TransferParameters tp = mw.getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					String fullFileName =
						elements.get (toGet).getFilename ()
						+ Utils.DOT
						+ elements.get (toGet).getExt ();
					dstFileName = destDir.getAbsolutePath ()
						+ File.separator
						+ fullFileName;
					final File received = new File (dstFileName);
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (
							mw,
							fullFileName
							+ Utils.SPACE +
							MainWindow.FILE_EXISTS_OVERWRITE,
							MainWindow.OVERWRITE_STRING,
							JOptionPane.YES_NO_CANCEL_OPTION);
						if ( res != JOptionPane.YES_OPTION )
						{
							continue;
						}
					}
					if ( received.exists () && ! received.canWrite () )
					{
						UiUtils.showErrorMessage (mw,
							MainWindow.FILE_NOT_WRITABLE_MSG
							+ ": "	// NOI18N
							+ received.getName ());
						continue;
					}
					mw.setReceivingStatus ();
					threads.incrementAndGet ();
					TransferUtils.downloadFile (received,
						elements.get (toGet),
						tp,
						new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							int th = threads.decrementAndGet ();
							mw.setProgressCurrentValue (
								mw.getProgressCurrentValue() + 1);
							if ( th == 0 )
							{
								mw.setReadyStatus ();
							}
						}

						@Override
						public String toString ()
						{
							return "PanelUtils.download.Runnable";	// NOI18N
						}
					}, mw, false, false, false);
				} // for
			}
			catch (HeadlessException ex)
			{
				mw.setReadyStatus ();
				Utils.handleException (ex, "Download"	// NOI18N
					+ ((dstFileName != null)? ": " + dstFileName : Utils.EMPTY_STR)); // NOI18N
			}
		}
	}

	/**
	 * Uploads the selected file selected by the file chooser.
	 * @param mw The MainWindow to refer to.
	 * @param uploadFC The file chooser to use.
	 */
	public static void upload(final MainWindow mw,
		JFileChooser uploadFC)
	{
		int dialogRes = uploadFC.showOpenDialog (mw);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadFC.getSelectedFile ();
			if ( res == null )
			{
				return;
			}
			try
			{
				mw.setSendingStatus ();
				mw.setProgressCurrentValue (0);
				mw.setProgressMinimumValue (0);
				mw.setProgressMaximumValue (1);

				TransferParameters tp = mw.getTransferParameters ();
				TransferUtils.uploadFile (res, tp,
					new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							mw.setReadyStatus ();
						}

						@Override
						public String toString ()
						{
							return "PanelUtils.upload.Runnable";	// NOI18N
						}
					}, mw, false, false, false);
			}
			catch (Exception ex)
			{
				mw.setReadyStatus ();
				Utils.handleException (ex, "PanelUtils.upload");	// NOI18N
			}
		}
	}

	/**
	 * Downloads the selected elements and puts them in the directory
	 * pointed to by the file chooser.
	 * @param mw The MainWindow to refer to.
	 * @param selectedRows The indices of the selected elements.
	 * @param elements The list of the all elements.
	 */
	public static void delete(final MainWindow mw,
		int[] selectedRows,
		List<PhoneElement> elements)
	{
		if ( selectedRows == null || elements == null )
		{
			return;
		}
		if ( selectedRows.length == 0 )
		{
			return;
		}

		int dialogRes = JOptionPane.showConfirmDialog (mw,
			MainWindow.DELETE_QUESTION,
			MainWindow.QUESTION_STRING,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
			);

		if ( dialogRes == JOptionPane.YES_OPTION )
		{
			try
			{
				final AtomicInteger threads = new AtomicInteger (0);
				mw.setSendingStatus ();
				mw.setProgressCurrentValue (0);
				mw.setProgressMinimumValue (0);
				mw.setProgressMaximumValue (selectedRows.length);
				TransferParameters tp = mw.getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					threads.incrementAndGet ();
					TransferUtils.deleteFile (
						elements.get (toGet),
						tp,
						new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							mw.setProgressCurrentValue (
								mw.getProgressCurrentValue() + 1);
							if ( threads.decrementAndGet () == 0 )
							{
								mw.setReadyStatus ();
							}
						}

						@Override
						public String toString ()
						{
							return "PanelUtils.delete.Runnable";	// NOI18N
						}
					}, mw, false, false, false);
				}
			}
			catch (Exception ex)
			{
				mw.setReadyStatus ();
				Utils.handleException (ex, "PanelUtils.delete");	// NOI18N
			}
		}
	}
}
