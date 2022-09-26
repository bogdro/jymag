/*
 * TransferUtils.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2014 Bogdan Drozdowski, bogdandr (at) op.pl
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
import BogDroSoft.jymag.Utils;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

/**
 * TransferUtils - utility methods connected to transfering data.
 * @author Bogdan Drozdowski
 */
public class TransferUtils
{
	// ------------ i18n stuff
	private static final ResourceBundle rb = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");

	private static final String tryPortStr   = rb.getString("Trying_port_");
	private static final String gotAnsStr    = rb.getString("Got_answer!");
	private static final String noAnsStr     = rb.getString("No_answers_received");
	private static final String errString    = rb.getString("Error");
	private static final String unsuppType   = rb.getString("Unsupported_file_type:");
	private static final String getFileStr   = rb.getString("Getting_file");
	// error messages for file upload:
	private static final String uploadMsg1   = rb.getString("File_upload_init_failed");
	private static final String uploadMsg2   = rb.getString("Sending_file_names_length_failed");
	private static final String uploadMsg3   = rb.getString("Sending_file_name_failed");
	private static final String uploadMsg4   = rb.getString("Format_not_suported_by_phone");
	private static final String uploadMsg5   = rb.getString("File_not_accepted");
	private static final String uploadMsg6   = rb.getString("Closing_transmission_failed");
	private static final String uploadMsg7   = rb.getString("Exception_occurred");
	private static final String uploadMsg8   = rb.getString("Incorrect_parameter");
	private static final String uploadMsg9   = rb.getString("Format_not_suported_by_JYMAG");
	private static final String uploadMsg10  = rb.getString("Number_of_attempts_exceeded");
	private static final String uploadMsg11  = rb.getString("Incorrect_parameter");
	// error messages for file download:
	private static final String downloadMsg1 = rb.getString("Exception_occurred");
	private static final String downloadMsg2 = rb.getString("No_data");
	private static final String downloadMsg3 = rb.getString("Incorrect_parameter");

	private static final String emptyStr = "";				// NOI18N
	private static final String comma = ",";				// NOI18N
	private static final String dot = ".";					// NOI18N
	private static final String colon = ":";				// NOI18N
	private static final String space = " ";				// NOI18N
	private static final String apos = "'";					// NOI18N
	private static final String filenameForbiddenCharsRegex = "\\s";	// NOI18N
	private static final String filenameForbiddenCharsReplace = "_";	// NOI18N
	private static final String zero = "0";					// NOI18N
	private static final String qMark = "?";				// NOI18N
	private static final String ellipsis = "...";				// NOI18N

	// non-instantiable
	private TransferUtils () {}

	/**
	 * A class describing the operation to perform.
	 */
	private static abstract class TUOperation<T>
	{
		private String opName;
		private String opErrorParams;
		private Runnable opOnDone;
		private boolean opWaitFor;
		private Object opSync;
		private boolean opQuiet;
		private boolean opQuietGUI;
		private Component opParentFrame;

		/**
		 * The constructor, setting basic data.
		 * @param name the name of this operation.
		 * @param errorParams a description or value of any paramters.
		 * @param onDone the code to run after performing the operation.
		 * @param waitFor whether the program should wait until the
		 * 	operation is completed.
		 * @param sync the transmission synchronization object.
		 *	 Can't be null.
		 * @param quiet whether the operation should NOT display
		 *	any messages.
		 * @param quietGUI whether the operation should NOT display
		 *	any GUI messages.
		 * @param parentFrame the parent frame for displaying
		 *	GUI messages.
		 */
		public TUOperation (String name, String errorParams,
			Runnable onDone, boolean waitFor, Object sync,
			boolean quiet, boolean quietGUI, Component parentFrame)
		{
			if ( sync == null )
			{
				throw new IllegalArgumentException (
					"TUOperation.TUOperation: sync==null");
			}
			opName = name;
			opErrorParams = errorParams;
			opOnDone = onDone;
			opWaitFor = waitFor;
			opSync = sync;
			opQuiet = quiet;
			opQuietGUI = quietGUI;
			opParentFrame = parentFrame;
		}

		/**
		 * Performs the operation.
		 * @return the result of the operation.
		 */
		public abstract T perform () throws Exception;

		/**
		 * Gets an error message for the given error code.
		 * @param errCode the error code to get the message for.
		 * @return an error message for the given error code.
		 */
		public String msgForError (int errCode)
		{
			return emptyStr;
		}

		/**
		 * The name of this operation.
		 * @return the name of this operation.
		 */
		public String getName ()
		{
			return opName;
		}

		/**
		 * A description or value of any paramters that may
		 * have cause an error and can be useful for error messages.
		 * @return a description or value of any paramters.
		 */
		public String getErrorParams ()
		{
			return opErrorParams;
		}

		/**
		 * This method runs after performing the operation.
		 */
		public void runOnDone ()
		{
			if ( opOnDone != null )
			{
				opOnDone.run ();
			}
		}

		/**
		 * Tells whether the program should wait until the
		 * operation is completed.
		 * @return TRUE if the program should wait until the
		 * operation is completed.
		 */
		public boolean isWaitFor ()
		{
			return opWaitFor;
		}

		/**
		 * Gets the transmission synchronization object.
		 * @return the transmission synchronization object.
		 */
		public Object getSync ()
		{
			return opSync;
		}

		/**
		 * Tells whether the operation should NOT display any messages.
		 * @return TRUE if the operation should NOT display any messages.
		 */
		public boolean isQuiet ()
		{
			return opQuiet;
		}

		/**
		 * Tells whether the operation should NOT display any GUI messages.
		 * @return TRUE if the operation should NOT display any GUI messages.
		 */
		public boolean isQuietGUI ()
		{
			return opQuietGUI;
		}

		/**
		 * Returns the parent frame for displaying GUI messages.
		 * @return the parent frame for displaying GUI messages.
		 */
		public Component getParentFrame ()
		{
			return opParentFrame;
		}

		/**
		 * Performs any additional processing of the received data
		 * if the data is not a Number.
		 * @param t the received data to process.
		 */
		public void processData (T t)
		{
			// empty by default
		}
	}

	/**
	 * Common method to perform the given operation.
	 * @param op the operation to perform.
	 * @return the result of the operation.
	 */
	private static <T> int performOperation (final TUOperation<T> op)
	{
		if ( op == null )
		{
			return -100;
		}

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<T, Void> sw =
			new SwingWorker<T, Void> ()
		{
			@Override
			protected T doInBackground ()
			{
				synchronized (op.getSync ())
				{
					try
					{
						return op.perform ();
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU." + op.getName ()		// NOI18N
							+ ".SW.doInBackground: "	// NOI18N
							+ op.getErrorParams ());
					}
					return null;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					T res = get ();
					if ( res != null )
					{
						if ( res instanceof Number )
						{
							put = ((Number)res).intValue ();
						}
						else
						{
							// another data type - process it
							op.processData (res);
						}
					}
					else
					{
						// another data type, but null means error
						put = -1;
					}
					result.set (put);
					if ( put != 0 )
					{
						String msg = op.msgForError (put);

						if ( ! op.isQuiet () )
						{
							System.out.println (
								errString + colon
								+ space + msg
								+ space + op.getErrorParams ());
						}
						if ( ! op.isQuietGUI () )
						{
							try
							{
								JOptionPane.showMessageDialog (
									op.getParentFrame (),
									errString + colon + space + msg
									+ space + op.getErrorParams (),
									errString,
									JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex)
							{
								Utils.handleException (ex,
									"JOptionPane.showMessageDialog:"	// NOI18N
									+ op.getName ());
							}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU." + op.getName ()		// NOI18N
						+ ".SW.done:"	// NOI18N
						+ op.getErrorParams ());
					result.set (-10);
				}
				try
				{
					op.runOnDone ();
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU." + op.getName ()		// NOI18N
						+ ".SW.onDone:"	// NOI18N
						+ op.getErrorParams ());
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( op.isWaitFor () )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				}
				catch (InterruptedException ex)
				{
					// ignore - just wait again
				}
			}
		}
		return result.get ();
	}

	/**
	 * Uploads the given file to the phone.
	 * @param f The file to upload. Can't be null.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int uploadFile (final File f, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null )
		{
			return -7;
		}
		if ( ! f.exists () || ! f.canRead () || ! f.isFile () )
		{
			return -8;
		}

		final String fname = f.getName ();
		if ( ! fname.contains (dot) )
		{
			return -9;
		}

		final String fext = fname.substring
			(fname.lastIndexOf (dot)+1)
			.toLowerCase (Locale.ENGLISH);

		if ( ! Utils.getFiletypeIDs ().containsKey (fext))
		{
			if ( ! quiet )
			{
				System.out.println (unsuppType + colon
					+ space + apos + fext + apos);
			}
			return -10;
		}

		return performOperation (new TUOperation<Integer>
			("uploadFile", fname, onDone, waitFor, sync,
			quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.putFile (f, fname.substring
						(0, fname.indexOf (dot))
						.replaceAll (filenameForbiddenCharsRegex,
							filenameForbiddenCharsReplace)
						);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					String msg = String.valueOf (errCode);
					if ( errCode == -1 )
					{
						msg = uploadMsg1;
					}
					else if ( errCode == -2 )
					{
						msg = uploadMsg2;
					}
					else if ( errCode == -3 )
					{
						msg = uploadMsg3;
					}
					else if ( errCode == -4 )
					{
						msg = uploadMsg4;
					}
					else if ( errCode == -5 )
					{
						msg = uploadMsg5;
					}
					else if ( errCode == -6 )
					{
						msg = uploadMsg6;
					}
					else if ( errCode == -7 )
					{
						msg = uploadMsg7;
					}
					else if ( errCode == -8 )
					{
						msg = uploadMsg8;
					}
					else if ( errCode == -9 )
					{
						msg = uploadMsg9;
					}
					else if ( errCode == -10 )
					{
						msg = uploadMsg10;
					}
					else if ( errCode == -11 )
					{
						msg = uploadMsg11;
					}
					return msg;
				}
			});
	}

	/**
	 * Downloads the given file from the phone.
	 * @param f The file to save the data to. Can't be null.
	 * @param element The element to download.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadFile (final File f, final PhoneElement element,
		final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null )
		{
			return -7;
		}
		if ( element == null )
		{
			return -8;
		}
		if ( f.exists () && ((! f.canWrite ()) || ! f.isFile ()) )
		{
			return -9;
		}

		return performOperation (new TUOperation<Integer>
			("downloadFile", element.getFilename ()	// NOI18N
			+ comma + space + f.getName (), onDone, waitFor,
			sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.getFile (f, element);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					String msg = String.valueOf (errCode);
					if ( errCode == -1 )
					{
						msg = downloadMsg1;
					}
					else if ( errCode == -2 )
					{
						msg = downloadMsg2;
					}
					else if ( errCode == -3 )
					{
						msg = downloadMsg3;
					}
					return msg;
				}
			});
	}

	/**
	 * Delete the given element from the phone.
	 * @param element The element to delete.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteFile (final PhoneElement element, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("deleteFile", element.getFilename (),	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.deleteFile (element);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Uploads the given alarm to the phone.
	 * @param alarm The alarm to upload. Can't be null.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int uploadAlarm (final PhoneAlarm alarm, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarm == null )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("uploadAlarm", alarm.getAlarmString (),	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.addAlarm (alarm);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Delete the given alarm from the phone.
	 * @param alarmNo The alarm number to delete.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteAlarm (final int alarmNo, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarmNo <= 0 )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("deleteAlarm", String.valueOf(alarmNo),	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.deleteAlarm (alarmNo);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Checks if the given String represents a known type of phone elements.
	 * @param type The type string to check.
	 * @return TRUE if the given String represents a known type of phone elements
	 *	and FALSE otherwise.
	 */
	private static boolean isAllowedType (String type)
	{
		if ( type == null )
		{
			return false;
		}
		if ( type.equals ("PICTURES") )		// NOI18N
		{
			return true;
		}
		if ( type.equals ("RINGTONES") )	// NOI18N
		{
			return true;
		}
		if ( type.equals ("VTODO") )		// NOI18N
		{
			return true;
		}
		if ( type.equals ("VEVENT") )		// NOI18N
		{
			return true;
		}
		if ( type.equals ("VCARDS") )		// NOI18N
		{
			return true;
		}
		if ( type.equals ("ANIMATIONS") )	// NOI18N
		{
			return true;
		}
		return false;
	}

	/**
	 * Downloads files of the given type from the phone.
	 * @param type The type of the files to download. One of
	 *	PICTURES, RINGTONES, VTODO, VEVENT, VCARDS, ANIMATIONS. Can't be null.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param destDir The destination directory to put the downloaded files in.
	 * @param deleteAfterDownload If TRUE, the elements will be deleted from the phone
	 *	after they're successfully downloaded.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadFiles (final String type, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor, final String destDir,
		final boolean deleteAfterDownload)
	{
		if ( ! isAllowedType (type) )
		{
			return -8;
		}

		return performOperation (new TUOperation<Integer>
			("downloadFiles", type,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					int ret = 0;
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					Vector<PhoneElement> elems = dt.getList (type);
					if ( elems != null )
					{
						for ( int i=0; i < elems.size (); i++ )
						{
							File received = new File (
								destDir + File.separator
								+ elems.get (i).getFilename ()
								+ dot + elems.get (i).getExt ());
							if ( ! quiet )
							{
								System.out.println (getFileStr + space + apos
									+ elems.get (i).getFilename ()
									+ dot + elems.get (i).getExt ()
									+ apos);
							}
							int res = dt.getFile (received, elems.get (i));
							if ( res != 0 )
							{
								ret = res;
							}
							else if ( deleteAfterDownload )
							{
								dt.deleteFile (elems.get (i));
							}
						}
					}
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Downloads the list of files of the given type from the phone.
	 * @param ofWhat The type of the files to download. One of
	 *	PICTURES, RINGTONES, VTODO, VEVENT, VCARDS, ANIMATIONS. Can't be null.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param dtm The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadList (final String ofWhat, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final DefaultTableModel dtm, final Vector<PhoneElement> placeForData)
	{
		if ( ! isAllowedType (ofWhat) )
		{
			return -8;
		}

		return performOperation (new TUOperation<Vector<PhoneElement>>
			("downloadList", ofWhat,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneElement> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					Vector<PhoneElement> elems = dt.getList (ofWhat);
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return downloadMsg2;
				}

				@Override
				public void processData (Vector<PhoneElement> ret)
				{
					if ( placeForData != null )
					{
						placeForData.removeAllElements ();
						placeForData.addAll (ret);
					}
					if ( dtm != null )
					{
						dtm.setRowCount (0);
						for ( int i=0; i < ret.size (); i++ )
						{
							dtm.addRow (new String[]
								{ret.get (i).getFilename ()
									+ dot + ret.get (i).getExt () }
								);
						}
					}
				}
			});
	}

	/**
	 * Downloads the list of alarms from the phone.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param alarms The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadAlarmList (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable alarms, final Vector<PhoneAlarm> placeForData)
	{
		final AtomicInteger alarmNumber = new AtomicInteger (0);

		return performOperation (new TUOperation<Vector<PhoneAlarm>>
			("downloadAlarmList", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneAlarm> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					alarmNumber.set (dt.getNumberOfAlarms ());
					Vector<PhoneAlarm> elems = dt.getAlarms ();
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return downloadMsg2;
				}

				@Override
				public void processData (Vector<PhoneAlarm> ret)
				{
					int alNumber = alarmNumber.get ();
					if ( placeForData != null )
					{
						placeForData.removeAllElements ();
						placeForData.addAll (ret);
					}
					if ( alarms != null )
					{
						DefaultTableModel dtm = new DefaultTableModel
							((alNumber > 0)? alNumber : 1, 4);
						TableModel model = alarms.getModel ();
						if ( model != null )
						{
							int cols = model.getColumnCount ();
							Vector<String> colNames =
								new Vector<String> (cols);
							for ( int i = 0; i < cols; i++ )
							{
								String colName = model.getColumnName (i);
								if ( colName == null )
								{
									colName = emptyStr;
								}
								colNames.add (colName);
							}
							dtm.setColumnIdentifiers (colNames);
						}
						else
						{
							dtm.setColumnIdentifiers (new String[]
							{
								rb.getString("Alarm_number"),
								rb.getString("Alarm_date"),
								rb.getString("Alarm_time"),
								rb.getString("Alarm_days")
							});
						}
						dtm.setRowCount (0);
						for ( int i=0; i < ret.size (); i++ )
						{
							PhoneAlarm al = ret.get (i);
							if ( al == null )
							{
								continue;
							}
							String date = null;
							if ( al.isOneTimeAlarm () )
							{
								date = al.getDateString ();
							}
							if ( date == null )
							{
								date = emptyStr;
							}
							String time = al.getTimeString ();
							if ( time == null )
							{
								time = qMark;
							}
							String days = al.getDaysString ();
							if ( days == null )
							{
								days = zero;
							}
							if ( days.isEmpty () )
							{
								days = zero;
							}
							int num = al.getNumber ();
							if ( num <= 0 ) num = 1;
							dtm.insertRow (num-1, new Object[]
								{
									Integer.valueOf (num),
									date, time, days
								}
							);
						}
						for ( int i = ret.size (); i < alNumber; i++ )
						{
							dtm.addRow (new String[]
								{
									String.valueOf (i),
									emptyStr, emptyStr, emptyStr
								}
							);
						}
						alarms.setModel (dtm);
					}
				}
			});
	}

	/**
	 * Downloads the list of messages from the phone.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param messages The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadMessageList (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable messages, final Vector<PhoneMessage> placeForData)
	{
		return performOperation (new TUOperation<Vector<PhoneMessage>>
			("downloadMessageList", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneMessage> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					Vector<PhoneMessage> elems = dt.getMessages ();
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return downloadMsg2;
				}

				@Override
				public void processData (Vector<PhoneMessage> ret)
				{
					if ( placeForData != null )
					{
						placeForData.removeAllElements ();
						placeForData.addAll (ret);
					}
					if ( messages != null )
					{
						DefaultTableModel dtm = new DefaultTableModel
							(ret.size (), 4);
						TableModel model = messages.getModel ();
						if ( model != null )
						{
							int cols = model.getColumnCount ();
							Vector<String> colNames =
								new Vector<String> (cols);
							for ( int i = 0; i < cols; i++ )
							{
								String colName = model.getColumnName (i);
								if ( colName == null )
								{
									colName = emptyStr;
								}
								colNames.add (colName);
							}
							dtm.setColumnIdentifiers (colNames);
						}
						else
						{
							dtm.setColumnIdentifiers (new String[]
							{
								rb.getString("smsTable_ID"),
								rb.getString("smsTable_Status"),
								rb.getString("smsTable_PhoneNum"),
								rb.getString("smsTable_DateTime"),
								rb.getString("smsTable_message")
							});
						}
						dtm.setRowCount (0);
						for ( int i=0; i < ret.size (); i++ )
						{
							PhoneMessage msg = ret.get (i);
							if ( msg == null )
							{
								continue;
							}
							String id = msg.getID ();
							if ( id == null )
							{
								id = emptyStr;
							}
							String status = msg.getStatus ();
							if ( status == null )
							{
								status = emptyStr;
							}
							String phoneNum = msg.getRecipientNum ();
							if ( phoneNum == null )
							{
								phoneNum = emptyStr;
							}
							String datetime = msg.getDateTime ();
							if ( datetime == null )
							{
								datetime = emptyStr;
							}
							String msgBody = msg.getMessage ();
							if ( msgBody == null )
							{
								msgBody = emptyStr;
							}
							dtm.addRow (new Object[]
								{
									Integer.valueOf (id),
									status, phoneNum, datetime, msgBody
								}
							);
						}
						messages.setModel (dtm);
					}
				}
			});
	}

	/**
	 * Delete the given message from the phone.
	 * @param element The message to delete.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteMessage (final PhoneMessage element, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("deleteMessage", element.getID (),	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.deleteMessage (
						Integer.parseInt (element.getID ()));
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Send the given message through the phone.
	 * @param element The message to send.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int sendMessage (final PhoneMessage element, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("sendMessage", element.getRecipientNum (),	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.sendMessage (element);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Dials the given number through the phone.
	 * @param number the number to dial. Must be non-null.
	 * @param isVoice whether the connection is for voice or data. Voice connections
	 *	will be initiated by adding a semicolon to the end of the number.
	 * @param dialMode The dial mode (tone, pulse, auto). Defaults to "auto" if null.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int dialNumber (final String number, final boolean isVoice,
		final DataTransporter.DIAL_MODE dialMode, final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( number == null )
		{
			return -7;
		}

		return performOperation (new TUOperation<Integer>
			("dialNumber", number,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.dialNumber (number,
						isVoice, dialMode);
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Hangs the phone up (stops the current call).
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int hangup (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		return performOperation (new TUOperation<Integer>
			("hangup", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.hangup ();
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Answers the phone (begins an incoming call).
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int answer (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		return performOperation (new TUOperation<Integer>
			("answer", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int ret = dt.answer ();
					dt.close ();
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Increases the volume level.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int volumeUp (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		return performOperation (new TUOperation<Integer>
			("volumeUp", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int volume = dt.getVolume ();
					int ret = 0;
					if ( volume >= 0 )
					{
						ret = dt.setVolume (volume+1);
					}
					dt.close ();
					if ( volume < 0 )
					{
						return Integer.valueOf (volume);
					}
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Decreases the volume level.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int volumeDown (final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		return performOperation (new TUOperation<Integer>
			("volumeDown", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int volume = dt.getVolume ();
					int ret = 0;
					if ( volume > 0 )
					{
						ret = dt.setVolume (volume-1);
					}
					dt.close ();
					if ( volume < 0 )
					{
						return Integer.valueOf (volume);
					}
					return Integer.valueOf (ret);
				}

				@Override
				public String msgForError (int errCode)
				{
					return uploadMsg11;
				}
			});
	}

	/**
	 * Sends the given file (which should contain raw commands, rather
	 * then be a supported media file).
	 * @param f The file to send.
	 * @param id The port ID to use.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param sync The synchronization object.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int sendFileAsCommands (final File f,
		final CommPortIdentifier id,
		final int speed, final int dataBits, final float stopBits,
		final int parity, final int flow, final Runnable onDone,
		final Component parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( ! f.exists () || ! f.canRead () || ! f.isFile () )
		{
			return -10;
		}

		return performOperation (new TUOperation<String>
			("sendFileAsCommands", emptyStr,	// NOI18N
			onDone, waitFor, sync, quiet, quietGUI, parent)
			{
				@Override
				public String perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (id);
					dt.open (speed, dataBits, stopBits,
						parity, flow);
					int read = -1;
					byte[] b = new byte[1024];
					StringBuilder ret = new StringBuilder(1000);
					FileInputStream fis =
						new FileInputStream (f);
					synchronized (sync)
					{
						do
						{
							read = fis.read (b);
							if ( read <= 0 )
							{
								break;
							}
							dt.send (b, 0, read);
							if ( dt.getAvailableBytes () > 0 )
							{
								// don't force any encodings,
								// because the data may
								// be invalid in any given encoding
								ret.append (new String (dt.recv (null)));
							}
						} while ( read > 0 );
						try
						{
							Thread.sleep (1000);
						}
						catch (InterruptedException ex)
						{
							// ignore
						}
						if ( dt.getAvailableBytes () > 0 )
						{
							// don't force any encodings,
							// because the data may
							// be invalid in any given encoding
							ret.append (new String (dt.recv (null)));
						}
					}
					fis.close ();
					dt.close ();
					return ret.toString ();
				}

				@Override
				public void processData (String ret)
				{
					System.out.println (ret);
				}
			});
	}

	/**
	 * Returns the CommPortIdentifier for the given port or the first
	 *	serial port's identifier.
	 * @param port The port to get the identifier for or <code>null</code> to
	 *	get the identifier of the first serial port.
	 * @return The identifier for the selected port, the identifier of the first serial port,
	 *	or null if port/identifier not found.
	 */
	public static CommPortIdentifier getIdentifierForPort (String port)
	{
		try
		{
			if ( port == null )
			{
				// get the first serial port's identifier.
				Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers ();

				while (portList.hasMoreElements ())
				{
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

					if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
					{
						return id;
					}
				}
			}
			else
			{
				return CommPortIdentifier.getPortIdentifier (port);
			}
		}
		catch (NoSuchPortException nspex)
		{
			Utils.handleException (nspex,
				"TransferUtils.getIdentifierForPort");	// NOI18N
		}
		return null;
	}

	/**
	 * Scans all the serial ports with "AT", looking for "OK" responses.
	 * @param quiet If TRUE, no messages will be printed.
	 * @param speed The port speed.
	 * @param dataBits The number of data bits.
	 * @param stopBits The number of stop bits.
	 * @param parity The parity mode.
	 * @param flow The flow control mode.
	 * @param firmwares If not null, will get the detected firmware versions for the scanned ports.
	 * @param phoneTypes If not null, will get the detected phone types for the scanned ports.
	 * @param phoneIMEIs If not null, will get the detected phone IMEI numbers for the scanned ports.
	 * @param phoneSubsNums If not null, will get the detected phone subscriber numbers for the scanned ports.
	 * @param replied If not null, will be filled with the names of the ports that have replied.
	 * @param afterPort If not null, will be called after each port is scanned (whether
	 *	successful or not).
	 * @return 0 if at least one port has responded.
	 */
	public static int scanPorts (boolean quiet, final int speed,
		final int dataBits, final float stopBits,
		final int parity, final int flow,
		Hashtable<String, String> firmwares,
		Hashtable<String, String> phoneTypes,
		Hashtable<String, String> phoneIMEIs,
		Hashtable<String, String> phoneSubsNums,
		Vector<String> replied,
		Runnable afterPort)
	{
		int active = 0;

		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers ();
		while (portList.hasMoreElements ())
		{
			Object portID = portList.nextElement ();
			if ( portID == null || ! (portID instanceof CommPortIdentifier) )
			{
				continue;
			}

			CommPortIdentifier id = (CommPortIdentifier) portID;

			if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
			{
				String portName = id.getName ();
				// scan ports for "AT"-"OK"
				if ( ! quiet )
				{
					System.out.print (tryPortStr + portName + ellipsis);
				}
				try
				{
					DataTransporter dt = new DataTransporter (id);
					dt.open (speed, dataBits, stopBits, parity, flow);
					int scanRes = dt.test ();
					if ( dt.test () != 0 )
					{
						if ( ! quiet )
						{
							System.out.println ();
						}
						if ( afterPort != null )
						{
							afterPort.run ();
						}
						continue;
					}
					if ( ! quiet )
					{
						System.out.println (gotAnsStr);
					}
					active++;

					if ( replied != null )
					{
						replied.add (portName);
					}

					if ( firmwares != null )
					{
						// get firmware version
						firmwares.put (portName, dt.getFirmwareVersion ());
					}
					if ( phoneTypes != null )
					{
						// get phone type
						phoneTypes.put (portName, dt.getDeviceType ());
						// get additional phone type
						String addType = dt.getExtraDeviceType ();
						if ( addType != null )
						{
							String type = null;
							try
							{
								type = phoneTypes.get (portName);
							}
							catch (Exception ex)
							{
								// ignore, defaults provided
							}
							if ( type == null )
							{
								type = emptyStr;
							}
							if ( ! type.isEmpty () )
							{
								type += comma + space;
							}
							type += addType;
							phoneTypes.put (portName, type);
						}
					}
					if ( phoneIMEIs != null )
					{
						// get phone IMEI
						phoneIMEIs.put (portName, dt.getIMEI ());
					}
					if ( phoneSubsNums != null )
					{
						// get subscriber phone numbers:
						phoneSubsNums.put (portName, dt.getSubscriberNumbers ());
					}
					dt.close ();
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "TransferUtils.scanPorts:"	// NOI18N
						+ portName);
				}
				finally
				{
					if ( afterPort != null )
					{
						afterPort.run ();
					}
				}
			}
		}
		if (active == 0)
		{
			if ( ! quiet )
			{
				System.out.println (noAnsStr);
			}
			return -1;
		}
		return 0;
	}

	/**
	 * Gets all the serial ports' names.
	 * @return A list of serial ports' names.
	 */
	public synchronized static Vector<String> getSerialPortNames ()
	{
		Vector<String> ret = new Vector<String> (32);
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers ();
		if ( portList != null )
		{
			while ( portList.hasMoreElements () )
			{
				Object portID = portList.nextElement ();
				if ( portID == null || ! (portID instanceof CommPortIdentifier) )
				{
					continue;
				}

				CommPortIdentifier id = (CommPortIdentifier) portID;

				if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
				{
					ret.add (id.getName ());
				}
			}
		}
		return ret;
	}
}
