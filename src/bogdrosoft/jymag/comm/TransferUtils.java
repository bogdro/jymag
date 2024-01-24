/*
 * TransferUtils.java, part of the JYMAG package.
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

package bogdrosoft.jymag.comm;

import bogdrosoft.jymag.CommandLineParser;
import bogdrosoft.jymag.PhoneAlarm;
import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import bogdrosoft.jymag.gui.UiUtils;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * TransferUtils - utility methods connected to transfering data.
 * @author Bogdan Drozdowski
 */
public class TransferUtils
{
	// ------------ i18n stuff
	private static final ResourceBundle MSGS
		= ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");	// NOI18N

	private static final String TRY_PORT_STRING
		= MSGS.getString("Trying_port_");		// NOI18N
	private static final String GOT_REPLY_STRING
		= MSGS.getString("Got_answer!");		// NOI18N
	private static final String NO_REPLIES_STRING
		= MSGS.getString("No_answers_received");	// NOI18N
	private static final String ERR_STRING
		= MSGS.getString("Error");			// NOI18N
	private static final String UNSUPPORTED_TYPE
		= MSGS.getString("Unsupported_file_type:");	// NOI18N
	private static final String GETTING_FILE
		= MSGS.getString("Getting_file");		// NOI18N
	// error messages for file upload:
	private static final String UPLOAD_MSG_1
		= MSGS.getString("File_upload_init_failed");	// NOI18N
	private static final String UPLOAD_MSG_2
		= MSGS.getString("Sending_file_names_length_failed");	// NOI18N
	private static final String UPLOAD_MSG_3
		= MSGS.getString("Sending_file_name_failed");	// NOI18N
	private static final String UPLOAD_MSG_4
		= MSGS.getString("Format_not_suported_by_phone");	// NOI18N
	private static final String UPLOAD_MSG_5
		= MSGS.getString("File_not_accepted");		// NOI18N
	private static final String UPLOAD_MSG_6
		= MSGS.getString("Closing_transmission_failed");	// NOI18N
	private static final String UPLOAD_MSG_7
		= MSGS.getString("Exception_occurred");		// NOI18N
	private static final String UPLOAD_MSG_8
		= MSGS.getString("Incorrect_parameter");	// NOI18N
	private static final String UPLOAD_MSG_9
		= MSGS.getString("Format_not_suported_by_JYMAG");	// NOI18N
	private static final String UPLOAD_MSG_10
		= MSGS.getString("Number_of_attempts_exceeded");	// NOI18N
	private static final String UPLOAD_MSG_11
		= MSGS.getString("Incorrect_parameter");	// NOI18N
	// error messages for file download:
	private static final String DOWNLOAD_MSG_1
		= MSGS.getString("Exception_occurred");		// NOI18N
	private static final String DOWNLOAD_MSG_2
		= MSGS.getString("No_data");			// NOI18N
	private static final String DOWNLOAD_MSG_3
		= MSGS.getString("Incorrect_parameter");	// NOI18N

	private static final String FILENAME_FORBIDDEN_CHARS_REGEX = "\\s";	// NOI18N
	private static final String FILENAME_FORBIDDEN_CHARS_REPLACE = "_";	// NOI18N
	private static final String ELLIPSIS = "...";				// NOI18N

	// non-instantiable
	private TransferUtils () {}

	/**
	 * Common method to perform the given operation.
	 * @param op the operation to perform.
	 * @return the result of the operation.
	 */
	private static <T> int performOperation (final TransferOperation<T> op)
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
			public synchronized void done ()
			{
				try
				{
					if ( isDone.get () )
					{
						return;
					}
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
								ERR_STRING + Utils.COLON
								+ Utils.SPACE + msg + Utils.COLON
								+ Utils.SPACE + op.getErrorParams ());
						}
						if ( ! op.isQuietGUI () )
						{
							UiUtils.showErrorMessage(
								op.getParentFrame (),
								ERR_STRING + Utils.COLON
								+ Utils.SPACE + msg
								+ Utils.COLON + Utils.SPACE
								+ op.getErrorParams ());
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU." + op.getName ()		// NOI18N
						+ ".SW.done: "			// NOI18N
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
						+ ".SW.onDone: "		// NOI18N
						+ op.getErrorParams ());
				}
				isDone.set (true);
			}

			@Override
			public String toString ()
			{
				return "TransferUtils.performOperation.SwingWorker";	// NOI18N
			}
		};
		sw.execute ();
		if ( op.isWaitFor () )
		{
			while ( ! sw.isDone () )
			{
				if ( ! isDone.get () )
				{
					Utils.sleepIgnoreException(10);
				}
			}
		}
		return result.get ();
	}

	/**
	 * Uploads the given file to the phone.
	 * @param f The file to upload. Can't be null.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int uploadFile (final File f, final TransferParameters tp,
		final Runnable onDone, final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null || tp == null )
		{
			return -7;
		}
		if ( ! f.exists () || ! f.canRead () || ! f.isFile () )
		{
			return -8;
		}

		final String fname = f.getName ();
		if ( ! fname.contains (Utils.DOT) )
		{
			return -9;
		}

		final String fext = fname.substring
			(fname.lastIndexOf (Utils.DOT)+1)
			.toLowerCase (Locale.ENGLISH);

		if ( ! Utils.getFiletypeIDs ().containsKey (fext))
		{
			if ( ! quiet )
			{
				System.out.println (UNSUPPORTED_TYPE + Utils.COLON
					+ Utils.SPACE + Utils.APOSTROPHE + fext + Utils.APOSTROPHE);
			}
			return -10;
		}

		return performOperation (new TransferOperation<Integer>
			("uploadFile", fname, onDone, waitFor, tp.getSync (),		// NOI18N
			quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.putFile (f, fname.substring
						(0, fname.indexOf (Utils.DOT))
						.replaceAll (FILENAME_FORBIDDEN_CHARS_REGEX,
							FILENAME_FORBIDDEN_CHARS_REPLACE)
						);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					String msg = String.valueOf (errCode);
					if ( errCode == -1 )
					{
						msg = UPLOAD_MSG_1;
					}
					else if ( errCode == -2 )
					{
						msg = UPLOAD_MSG_2;
					}
					else if ( errCode == -3 )
					{
						msg = UPLOAD_MSG_3;
					}
					else if ( errCode == -4 )
					{
						msg = UPLOAD_MSG_4;
					}
					else if ( errCode == -5 )
					{
						msg = UPLOAD_MSG_5;
					}
					else if ( errCode == -6 )
					{
						msg = UPLOAD_MSG_6;
					}
					else if ( errCode == -7 )
					{
						msg = UPLOAD_MSG_7;
					}
					else if ( errCode == -8 )
					{
						msg = UPLOAD_MSG_8;
					}
					else if ( errCode == -9 )
					{
						msg = UPLOAD_MSG_9;
					}
					else if ( errCode == -10 )
					{
						msg = UPLOAD_MSG_10;
					}
					else if ( errCode == -11 )
					{
						msg = UPLOAD_MSG_11;
					}
					return msg;
				}
			});
	}

	/**
	 * Downloads the given file from the phone.
	 * @param f The file to save the data to. Can't be null.
	 * @param element The element to download.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadFile (final File f, final PhoneElement element,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null || tp == null )
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

		return performOperation (new TransferOperation<Integer>
			("downloadFile", element.getFilename ()			// NOI18N
			+ Utils.COMMA + Utils.SPACE + f.getName (), onDone, waitFor,
			tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.getFile (f, element);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					String msg = String.valueOf (errCode);
					if ( errCode == -1 )
					{
						msg = DOWNLOAD_MSG_1;
					}
					else if ( errCode == -2 )
					{
						msg = DOWNLOAD_MSG_2;
					}
					else if ( errCode == -3 )
					{
						msg = DOWNLOAD_MSG_3;
					}
					return msg;
				}
			});
	}

	/**
	 * Delete the given element from the phone.
	 * @param element The element to delete.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteFile (final PhoneElement element,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("deleteFile", element.getFilename (),			// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.deleteFile (element);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Uploads the given alarm to the phone.
	 * @param alarm The alarm to upload. Can't be null.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int uploadAlarm (final PhoneAlarm alarm,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarm == null || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("uploadAlarm", alarm.getAlarmString (),	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.addAlarm (alarm);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Delete the given alarm from the phone.
	 * @param alarmNo The alarm number to delete.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteAlarm (final int alarmNo,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarmNo <= 0 || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("deleteAlarm", String.valueOf(alarmNo),	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.deleteAlarm (alarmNo);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
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
		return
			"PICTURES".equals (type) ||	// NOI18N
			"RINGTONES".equals (type) ||	// NOI18N
			"VTODO".equals (type) ||	// NOI18N
			"VEVENT".equals (type) ||	// NOI18N
			"VCARDS".equals (type) ||	// NOI18N
			"ANIMATIONS".equals (type);	// NOI18N
	}

	/**
	 * Downloads files of the given type from the phone.
	 * @param type The type of the files to download. One of
	 *	PICTURES, RINGTONES, VTODO, VEVENT, VCARDS, ANIMATIONS. Can't be null.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param destDir The destination directory to put the downloaded files in.
	 * @param deleteAfterDownload If TRUE, the elements will be deleted from the phone
	 *	after they're successfully downloaded.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadFiles (final String type,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor, final String destDir,
		final boolean deleteAfterDownload)
	{
		if ( tp == null || ! isAllowedType (type) )
		{
			return -8;
		}

		return performOperation (new TransferOperation<Integer>
			("downloadFiles", type,			// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					int ret = 0;
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					Vector<PhoneElement> elems = dt.getList (type);
					if ( elems != null )
					{
						for ( int i = 0; i < elems.size (); i++ )
						{
							File received = new File (
								destDir + File.separator
								+ elems.get (i).getFilename ()
								+ Utils.DOT + elems.get (i).getExt ());
							if ( ! quiet )
							{
								System.out.println (GETTING_FILE
									+ Utils.SPACE + Utils.APOSTROPHE
									+ elems.get (i).getFilename ()
									+ Utils.DOT + elems.get (i).getExt ()
									+ Utils.APOSTROPHE);
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
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Downloads the list of files of the given type from the phone.
	 * @param ofWhat The type of the files to download. One of
	 *	PICTURES, RINGTONES, VTODO, VEVENT, VCARDS, ANIMATIONS. Can't be null.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param dtm The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadList (final String ofWhat,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final DefaultTableModel dtm, final Vector<PhoneElement> placeForData)
	{
		if ( tp == null || ! isAllowedType (ofWhat) )
		{
			return -8;
		}

		return performOperation (new TransferOperation<Vector<PhoneElement>>
			("downloadList", ofWhat,	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneElement> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					Vector<PhoneElement> elems = dt.getList (ofWhat);
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return DOWNLOAD_MSG_2;
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
									+ Utils.DOT + ret.get (i).getExt () }
								);
						}
					}
				}
			});
	}

	/**
	 * Downloads the list of alarms from the phone.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param alarms The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadAlarmList (
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable alarms, final Vector<PhoneAlarm> placeForData)
	{
		if ( tp == null )
		{
			return -7;
		}

		final AtomicInteger alarmNumber = new AtomicInteger (0);

		return performOperation (new TransferOperation<Vector<PhoneAlarm>>
			("downloadAlarmList", "ALARM",			// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneAlarm> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					alarmNumber.set (dt.getNumberOfAlarms ());
					Vector<PhoneAlarm> elems = dt.getAlarms ();
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return DOWNLOAD_MSG_2;
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
									colName = Utils.EMPTY_STR;
								}
								colNames.add (colName);
							}
							dtm.setColumnIdentifiers (colNames);
						}
						else
						{
							dtm.setColumnIdentifiers (new String[]
							{
								MSGS.getString("Alarm_number"),		// NOI18N
								MSGS.getString("Alarm_date"),		// NOI18N
								MSGS.getString("Alarm_time"),		// NOI18N
								MSGS.getString("Alarm_days")		// NOI18N
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
								date = Utils.EMPTY_STR;
							}
							String time = al.getTimeString ();
							if ( time == null )
							{
								time = Utils.QUESTION_MARK;
							}
							String days = al.getDaysString ();
							if ( days == null )
							{
								days = Utils.ZERO;
							}
							if ( days.isEmpty () )
							{
								days = Utils.ZERO;
							}
							int num = al.getNumber ();
							if ( num <= 0 )
							{
								num = 1;
							}
							dtm.insertRow (num-1, new Object[]
								{
									num, date, time, days
								}
							);
						}
						for ( int i = ret.size (); i < alNumber; i++ )
						{
							dtm.addRow (new String[]
								{
									String.valueOf (i),
									Utils.EMPTY_STR,
									Utils.EMPTY_STR,
									Utils.EMPTY_STR
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
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @param messages The table to put the data in or null.
	 * @param placeForData The place for the found elements or null.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int downloadMessageList (
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable messages, final Vector<PhoneMessage> placeForData)
	{
		if ( tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Vector<PhoneMessage>>
			("downloadMessageList", "SMS",	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Vector<PhoneMessage> perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					Vector<PhoneMessage> elems = dt.getMessages ();
					dt.close ();
					return elems;
				}

				@Override
				public String msgForError (int errCode)
				{
					return DOWNLOAD_MSG_2;
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
									colName = Utils.EMPTY_STR;
								}
								colNames.add (colName);
							}
							dtm.setColumnIdentifiers (colNames);
						}
						else
						{
							dtm.setColumnIdentifiers (new String[]
							{
								MSGS.getString("smsTable_ID"),		// NOI18N
								MSGS.getString("smsTable_Status"),	// NOI18N
								MSGS.getString("smsTable_PhoneNum"),	// NOI18N
								MSGS.getString("smsTable_DateTime"),	// NOI18N
								MSGS.getString("smsTable_message")	// NOI18N
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
								id = Utils.EMPTY_STR;
							}
							String status = msg.getStatus ();
							if ( status == null )
							{
								status = Utils.EMPTY_STR;
							}
							String phoneNum = msg.getRecipientNum ();
							if ( phoneNum == null )
							{
								phoneNum = Utils.EMPTY_STR;
							}
							String datetime = msg.getDateTime ();
							if ( datetime == null )
							{
								datetime = Utils.EMPTY_STR;
							}
							String msgBody = msg.getMessage ();
							if ( msgBody == null )
							{
								msgBody = Utils.EMPTY_STR;
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
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int deleteMessage (final PhoneMessage element,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("deleteMessage", element.getID (),	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.deleteMessage (
						Integer.parseInt (element.getID ()));
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Send the given message through the phone.
	 * @param element The message to send.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int sendMessage (final PhoneMessage element,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("sendMessage", element.getRecipientNum (),	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.sendMessage (element);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Dials the given number through the phone.
	 * @param number the number to dial. Must be non-null.
	 * @param isVoice whether the connection is for voice or data. Voice connections
	 *	will be initiated by adding a semicolon to the end of the number.
	 * @param dialMode The dial mode (tone, pulse, auto). Defaults to "auto" if null.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int dialNumber (final String number, final boolean isVoice,
		final DataTransporter.DIAL_MODE dialMode,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( number == null || tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("dialNumber", number,		// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.dialNumber (number,
						isVoice, dialMode);
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Hangs the phone up (stops the current call).
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int hangup (final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("hangup", Utils.EMPTY_STR,		// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.hangup ();
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Answers the phone (begins an incoming call).
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int answer (final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("answer", Utils.EMPTY_STR,		// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int ret = dt.answer ();
					dt.close ();
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Increases the volume level.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int volumeUp (final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("volumeUp", Utils.EMPTY_STR,		// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int volume = dt.getVolume ();
					int ret = 0;
					if ( volume >= 0 )
					{
						ret = dt.setVolume (volume+1);
					}
					dt.close ();
					if ( volume < 0 )
					{
						return volume;
					}
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Decreases the volume level.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int volumeDown (final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( tp == null )
		{
			return -7;
		}

		return performOperation (new TransferOperation<Integer>
			("volumeDown", Utils.EMPTY_STR,	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public Integer perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int volume = dt.getVolume ();
					int ret = 0;
					if ( volume > 0 )
					{
						ret = dt.setVolume (volume-1);
					}
					dt.close ();
					if ( volume < 0 )
					{
						return volume;
					}
					return ret;
				}

				@Override
				public String msgForError (int errCode)
				{
					return UPLOAD_MSG_11;
				}
			});
	}

	/**
	 * Sends the given file (which should contain raw commands, rather
	 * then be a supported media file).
	 * @param f The file to send.
	 * @param tp The port parameters to use.
	 * @param onDone The code to run at transfer end.
	 * @param parent The parent frame for displaying messages.
	 * @param quiet If TRUE, no messages will be displayed.
	 * @param quietGUI If TRUE, no messageboxes will be displayed.
	 * @param waitFor If TRUE, the background thread will be waited for.
	 * @return the result of the task (if it has finished before the
	 *	function has returned or if waitFor is TRUE) and 0 otherwise.
	 */
	public static int sendFileAsCommands (final File f,
		final TransferParameters tp, final Runnable onDone,
		final Component parent, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null || tp == null )
		{
			return -7;
		}
		if ( ! f.exists () || ! f.canRead () || ! f.isFile () )
		{
			return -10;
		}

		return performOperation (new TransferOperation<String>
			("sendFileAsCommands", Utils.EMPTY_STR,	// NOI18N
			onDone, waitFor, tp.getSync (), quiet, quietGUI, parent)
			{
				@Override
				public String perform () throws Exception
				{
					final DataTransporter dt =
						new DataTransporter (tp.getId ());
					dt.open (tp.getSpeed (), tp.getDataBits (),
						tp.getStopBits (),
						tp.getParity (), tp.getFlow ());
					int read;
					byte[] b = new byte[1024];
					StringBuilder ret = new StringBuilder(1000);
					FileInputStream fis = null;
					try
					{
						fis = new FileInputStream (f);
						synchronized (tp.getSync ())
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
							} while ( true );
							Utils.sleepIgnoreException(1000);
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
					}
					catch (IOException t)
					{
						Utils.handleException(t, "TransferUtils.sendFileAsCommands");
						if (fis != null)
						{
							try
							{
								fis.close();
							}
							catch (IOException t2)
							{
								Utils.handleException(t2,
									"TransferUtils.sendFileAsCommands->exception");
							}
						}
					}
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
	public static Object /*CommPortIdentifier*/ getIdentifierForPort (String port)
	{
		try
		{
			if ( port == null )
			{
				// get the first serial port's identifier.
				Enumeration<?> portList = getPortList();
				while (portList.hasMoreElements ())
				{
					Object portID = portList.nextElement ();
					if ( portID == null )
					{
						continue;
					}
					if (CommandLineParser.mock
						&& "138b7ce0632d70dd9d6fc7b571fd9199".equals(System.getProperty("mock", "")))
					{
						if (! (portID instanceof FakeCommPortIdentifier) )
						{
							continue;
						}
						FakeCommPortIdentifier id = (FakeCommPortIdentifier) portID;

						if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
						{
							return id;
						}
					}
					else
					{
						if (! (portID instanceof CommPortIdentifier) )
						{
							continue;
						}
						CommPortIdentifier id = (CommPortIdentifier) portID;

						if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
						{
							return id;
						}
					}
				}
			}
			else
			{
				if (CommandLineParser.mock
					&& "138b7ce0632d70dd9d6fc7b571fd9199".equals(System.getProperty("mock", "")))
				{
					return FakeCommPortIdentifier.getPortIdentifier (port);
				}
				else
				{
					return CommPortIdentifier.getPortIdentifier (port);
				}
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
	 * @param tp The port parameters to use.
	 * @param firmwares If not null, will get the detected firmware versions for the scanned ports.
	 * @param phoneTypes If not null, will get the detected phone types for the scanned ports.
	 * @param phoneIMEIs If not null, will get the detected phone IMEI numbers for the scanned ports.
	 * @param phoneSubsNums If not null, will get the detected phone subscriber numbers for the scanned ports.
	 * @param replied If not null, will be filled with the names of the ports that have replied.
	 * @param afterPort If not null, will be called after each port is scanned (whether
	 *	successful or not).
	 * @return 0 if at least one port has responded.
	 */
	public static int scanPorts (boolean quiet, final TransferParameters tp,
		Map<String, String> firmwares,
		Map<String, String> phoneTypes,
		Map<String, String> phoneIMEIs,
		Map<String, String> phoneSubsNums,
		Vector<String> replied,
		Runnable afterPort)
	{
		int active = 0;
		String portName;

		Enumeration<?> portList = getPortList();
		while (portList.hasMoreElements ())
		{
			Object portID = portList.nextElement ();
			if ( portID == null )
			{
				continue;
			}
			if (CommandLineParser.mock
				&& "138b7ce0632d70dd9d6fc7b571fd9199".equals(System.getProperty("mock", "")))
			{
				if (! (portID instanceof FakeCommPortIdentifier) )
				{
					continue;
				}
				FakeCommPortIdentifier id = (FakeCommPortIdentifier) portID;

				if (id.getPortType () != CommPortIdentifier.PORT_SERIAL)
				{
					continue;
				}
				portID = id;
				portName = id.getName ();
			}
			else
			{
				if (! (portID instanceof CommPortIdentifier) )
				{
					continue;
				}
				CommPortIdentifier id = (CommPortIdentifier) portID;

				if (id.getPortType () != CommPortIdentifier.PORT_SERIAL)
				{
					continue;
				}
				portID = id;
				portName = id.getName ();
			}

			// scan ports for "AT"-"OK"
			if ( ! quiet )
			{
				System.out.print (TRY_PORT_STRING + portName + ELLIPSIS);
			}
			try
			{
				DataTransporter dt = new DataTransporter (portID);
				dt.open (tp.getSpeed (), tp.getDataBits (),
					tp.getStopBits (),
					tp.getParity (), tp.getFlow ());
				if ( dt.test () != 0 )
				{
					dt.close ();
					if ( ! quiet )
					{
						System.out.println ();
					}
					/* run in "finally"
					if ( afterPort != null )
					{
						afterPort.run ();
					}
					*/
					continue;
				}
				if ( ! quiet )
				{
					System.out.println (GOT_REPLY_STRING);
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
							type = Utils.EMPTY_STR;
						}
						if ( ! type.isEmpty () )
						{
							type += Utils.COMMA + Utils.SPACE;
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
		if (active == 0)
		{
			if ( ! quiet )
			{
				System.out.println (NO_REPLIES_STRING);
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
		Enumeration<?> portList = getPortList();
		if ( portList != null )
		{
			while ( portList.hasMoreElements () )
			{
				Object portID = portList.nextElement ();
				if ( portID == null )
				{
					continue;
				}
				if (CommandLineParser.mock
					&& "138b7ce0632d70dd9d6fc7b571fd9199".equals(System.getProperty("mock", "")))
				{
					if (! (portID instanceof FakeCommPortIdentifier) )
					{
						continue;
					}
					FakeCommPortIdentifier id = (FakeCommPortIdentifier) portID;

					if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
					{
						ret.add (id.getName ());
					}
				}
				else
				{
					if (! (portID instanceof CommPortIdentifier) )
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
		}
		return ret;
	}

	/**
	 * Returns the list of (real or fake) ports.
	 * @return the list of (real or fake) ports.
	 */
	private static Enumeration<?> getPortList()
	{
		Enumeration<?> portList;
		if (CommandLineParser.mock
			&& "138b7ce0632d70dd9d6fc7b571fd9199".equals(System.getProperty("mock", "")))
		{
			portList = FakeCommPortIdentifier.getPortIdentifiers();
		}
		else
		{
			portList = CommPortIdentifier.getPortIdentifiers ();
		}
		return portList;
	}
}
