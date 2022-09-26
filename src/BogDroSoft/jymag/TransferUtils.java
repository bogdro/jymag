/*
 * TransferUtils.java, part of the JYMAG package.
 *
 * Copyright (C) 2011 Bogdan Drozdowski, bogdandr (at) op.pl
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

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JFrame;
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
	private static final String tryPortStr = rb.getString("Trying_port_");
	private static final String gotAnsStr = rb.getString("Got_answer!");
	private static final String noAnsStr = rb.getString("No_answers_received");
	private static final String errString = rb.getString("Error");
	private static final String unsuppType = rb.getString("Unsupported_file_type:");
	private static final String getFileStr = rb.getString("Getting_file");
	// error messages for file upload:
	private static final String uploadMsg1  = rb.getString("File_upload_init_failed");
	private static final String uploadMsg2  = rb.getString("Sending_file_names_length_failed");
	private static final String uploadMsg3  = rb.getString("Sending_file_name_failed");
	private static final String uploadMsg4  = rb.getString("Format_not_suported_by_phone");
	private static final String uploadMsg5  = rb.getString("File_not_accepted");
	private static final String uploadMsg6  = rb.getString("Closing_transmission_failed");
	private static final String uploadMsg7  = rb.getString("Exception_occurred");
	private static final String uploadMsg8  = rb.getString("Incorrect_parameter");
	private static final String uploadMsg9  = rb.getString("Format_not_suported_by_JYMAG");
	private static final String uploadMsg10 = rb.getString("Number_of_attempts_exceeded");
	private static final String uploadMsg11 = rb.getString("Incorrect_parameter");
	// error messages for file download:
	private static final String downloadMsg1  = rb.getString("Exception_occurred");
	private static final String downloadMsg2  = rb.getString("No_data");
	private static final String downloadMsg3  = rb.getString("Incorrect_parameter");


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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null ) return -7;
		if ( ! f.exists () || ! f.canRead () || ! f.isFile () ) return -8;


		final String fname = f.getName ();
		if ( ! fname.contains (dot) ) return -9;

		final String fext = fname.substring
			(fname.lastIndexOf (dot)+1)
			.toLowerCase (Locale.ENGLISH);

		if ( ! Utils.filetypeIDs.containsKey (fext))
		{
			if ( ! quiet )
			{
				System.out.println (unsuppType + colon + space + apos
					+ fext + apos);
			}
			return -10;
		}

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.putFile (f, fname.substring
							(0, fname.indexOf (dot))	// NOI18N
							.replaceAll (filenameForbiddenCharsRegex,
								filenameForbiddenCharsReplace)
							);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.uploadFile.SW.doInBackground: " + fname);	// NOI18N
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						String msg = String.valueOf (put);
						if ( put == -1 ) msg = uploadMsg1;
						else if ( put == -2 ) msg = uploadMsg2;
						else if ( put == -3 ) msg = uploadMsg3;
						else if ( put == -4 ) msg = uploadMsg4;
						else if ( put == -5 ) msg = uploadMsg5;
						else if ( put == -6 ) msg = uploadMsg6;
						else if ( put == -7 ) msg = uploadMsg7;
						else if ( put == -8 ) msg = uploadMsg8;
						else if ( put == -9 ) msg = uploadMsg9;
						else if ( put == -10 ) msg = uploadMsg10;
						else if ( put == -11 ) msg = uploadMsg11;

						if ( ! quiet )
						{
							System.out.println (errString + colon + space + msg);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + msg,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.uploadFile.SW.done:" + fname);	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.uploadFile.SW.done:" + fname);	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( f == null ) return -7;
		if ( element == null ) return -8;
		if ( f.exists () && ((! f.canWrite ()) || ! f.isFile ()) ) return -9;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.getFile (f, element);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.downloadFile.SW.doInBackground: "	// NOI18N
							+ element.getFilename () + ", "	// NOI18N
							+ f.getName ());
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						String msg = String.valueOf (put);
						if ( put == -1 ) msg = downloadMsg1;
						else if ( put == -2 ) msg = downloadMsg2;
						else if ( put == -3 ) msg = downloadMsg3;
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + msg);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + msg
									+ element.getFilename () + comma + space
									+ f.getName (),
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.downloadFile.SW.done: "	// NOI18N
						+ element.getFilename () + comma + space
						+ f.getName ());
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.downloadFile.SW.done: "	// NOI18N
							+ element.getFilename () + comma + space
							+ f.getName ());
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null ) return -7;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.deleteFile (element);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.deleteFile.SW.doInBackground: "	// NOI18N
							+ element.getFilename ());
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.deleteFile.SW.done: "	// NOI18N
						+ element.getFilename ());
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.deleteFile.SW.done: "	// NOI18N
							+ element.getFilename ());
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarm == null ) return -7;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.addAlarm (alarm);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.uploadAlarm.SW.doInBackground"	// NOI18N
							+ alarm.getAlarmString ());
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.uploadAlarm.SW.done:"	// NOI18N
						+ alarm.getAlarmString ());
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.uploadAlarm.SW.done:"	// NOI18N
							+ alarm.getAlarmString ());
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( alarmNo <= 0 ) return -7;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.deleteAlarm (alarmNo);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.deleteAlarm.SW.doInBackground: " + alarmNo);	// NOI18N
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.deleteAlarm.SW.done: " + alarmNo);	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.deleteAlarm.SW.done: " + alarmNo);	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
	}

	/**
	 * Checks if the given String represents a known type of phone elements.
	 * @param type The type string to check.
	 * @return TRUE if the given String represents a known type of phone elements
	 *	and FALSE otherwise.
	 */
	private static boolean isAllowedType (String type)
	{
		if ( type == null ) return false;
		if ( type.equals ("PICTURES") ) return true;	// NOI18N
		if ( type.equals ("RINGTONES") ) return true;	// NOI18N
		if ( type.equals ("VTODO") ) return true;	// NOI18N
		if ( type.equals ("VEVENT") ) return true;	// NOI18N
		if ( type.equals ("VCARDS") ) return true;	// NOI18N
		if ( type.equals ("ANIMATIONS") ) return true;	// NOI18N
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor, final String destDir,
		final boolean deleteAfterDownload)
	{
		if ( ! isAllowedType (type) )
		{
			return -8;
		}

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						int ret = 0;
						final DataTransporter dt = new DataTransporter (id);
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
										+apos);
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
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.downloadFiles.SW.doInBackground: " + type);	// NOI18N
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.downloadFiles.SW.done: " + type);	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.downloadFiles.SW.done: " + type);	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final DefaultTableModel dtm, final Vector<PhoneElement> placeForData)
	{
		if ( ! isAllowedType (ofWhat) )
		{
			return -8;
		}

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Vector<PhoneElement>, Void> sw =
			new SwingWorker<Vector<PhoneElement>, Void> ()
		{
			@Override
			protected Vector<PhoneElement> doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						int ret = 0;
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						Vector<PhoneElement> elems = dt.getList (ofWhat);
						dt.close ();
						return elems;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.downloadList.SW.doInBackground: " + ofWhat);	// NOI18N
					}
					return null;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					Vector<PhoneElement> ret = get ();
					if ( ret != null )
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
					else
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + downloadMsg2);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + downloadMsg2,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
						result.set (-2);
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.downloadList.SW.done: " + ofWhat);	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.downloadList.SW.done: " + ofWhat);	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable alarms, final Vector<PhoneAlarm> placeForData)
	{
		final AtomicInteger result = new AtomicInteger (0);
		final AtomicInteger alarmNumber = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Vector<PhoneAlarm>, Void> sw =
			new SwingWorker<Vector<PhoneAlarm>, Void> ()
		{
			@Override
			protected Vector<PhoneAlarm> doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						int ret = 0;
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						alarmNumber.set (dt.getNumberOfAlarms ());
						Vector<PhoneAlarm> elems = dt.getAlarms ();
						dt.close ();
						return elems;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.downloadAlarmList.SW.doInBackground");	// NOI18N
					}
					return null;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					Vector<PhoneAlarm> ret = get ();
					if ( ret != null )
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
									if ( colName == null ) colName = emptyStr;
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
								if ( al == null ) continue;
								String date = null;
								if ( al.isOneTimeAlarm () )
								{
									date = al.getDateString ();
								}
								if ( date == null ) date = emptyStr;
								String time = al.getTimeString ();
								if ( time == null ) time = qMark;
								String days = al.getDaysString ();
								if ( days == null ) days = zero;
								if ( days.isEmpty () ) days = zero;
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
					else
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + downloadMsg2);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + downloadMsg2,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
						result.set (-2);
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.downloadAlarmList.SW.done");	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.downloadAlarmList.SW.done");	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor,
		final JTable messages, final Vector<PhoneMessage> placeForData)
	{
		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Vector<PhoneMessage>, Void> sw =
			new SwingWorker<Vector<PhoneMessage>, Void> ()
		{
			@Override
			protected Vector<PhoneMessage> doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						int ret = 0;
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						Vector<PhoneMessage> elems = dt.getMessages ();
						dt.close ();
						return elems;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.downloadMessageList.SW.doInBackground");	// NOI18N
					}
					return null;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					Vector<PhoneMessage> ret = get ();
					if ( ret != null )
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
									if ( colName == null ) colName = emptyStr;
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
								if ( msg == null ) continue;
								String id = msg.getID ();
								if ( id == null ) id = emptyStr;
								String status = msg.getStatus ();
								if ( status == null ) status = emptyStr;
								String phoneNum = msg.getRecipientNum ();
								if ( phoneNum == null ) phoneNum = emptyStr;
								String datetime = msg.getDateTime ();
								if ( datetime == null ) datetime = emptyStr;
								String msgBody = msg.getMessage ();
								if ( msgBody == null ) msgBody = emptyStr;
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
					else
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + downloadMsg2);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + downloadMsg2,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
						result.set (-2);
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.downloadMessageList.SW.done");	// NOI18N
					result.set (-1);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.downloadMessageList.SW.done");	// NOI18N
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null ) return -7;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.deleteMessage (Integer.parseInt(element.getID ()));
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.deleteMessage.SW.doInBackground: "	// NOI18N
							+ element.getID ());
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.deleteMessage.SW.done: "	// NOI18N
						+ element.getID ());
					result.set (-10);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.deleteMessage.SW.done: "	// NOI18N
							+ element.getID ());
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
		final JFrame parent, final Object sync, final boolean quiet,
		final boolean quietGUI, final boolean waitFor)
	{
		if ( element == null ) return -7;

		final AtomicInteger result = new AtomicInteger (0);
		final AtomicBoolean isDone = new AtomicBoolean (false);

		SwingWorker<Integer, Void> sw =
			new SwingWorker<Integer, Void> ()
		{
			@Override
			protected Integer doInBackground ()
			{
				synchronized (sync)
				{
					try
					{
						final DataTransporter dt = new DataTransporter (id);
						dt.open (speed, dataBits, stopBits,
							parity, flow);
						int ret = dt.sendMessage (element);
						dt.close ();
						return ret;
					}
					catch (Exception e)
					{
						Utils.handleException (e,
							"TU.sendMessage.SW.doInBackground: "	// NOI18N
							+ element.getRecipientNum ());
					}
					return -1;
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					int put = 0;
					Integer res = get ();
					if ( res != null ) put = res.intValue ();
					result.set (put);
					if ( put != 0 )
					{
						if ( ! quiet )
						{
							System.out.println (errString + colon + space + uploadMsg11);
						}
						if ( ! quietGUI )
						{
							try
							{
								JOptionPane.showMessageDialog (parent,
									errString + colon + space + uploadMsg11,
									errString, JOptionPane.ERROR_MESSAGE );
							}
							catch (Exception ex) {}
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex,
						"TU.sendMessage.SW.done: "	// NOI18N
						+ element.getRecipientNum ());
					result.set (-10);
				}
				if ( onDone != null )
				{
					try
					{
						onDone.run ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"TU.sendMessage.SW.done: "	// NOI18N
							+ element.getRecipientNum ());
					}
				}
				isDone.set (true);
			}
		};
		sw.execute ();
		if ( waitFor )
		{
			while ( ! isDone.get () )
			{
				try
				{
					Thread.sleep (10);
				} catch (InterruptedException ex) {}
			}
		}
		return result.get ();
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
			else return CommPortIdentifier.getPortIdentifier (port);
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
			if ( portID == null || ! (portID instanceof CommPortIdentifier) ) continue;

			CommPortIdentifier id = (CommPortIdentifier) portID;

			if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
			{
				String portName = id.getName ();
				// scan ports for "AT"-"OK"
				if ( ! quiet ) System.out.print (tryPortStr + portName + ellipsis);
				try
				{
					DataTransporter dt = new DataTransporter (id);
					dt.open (speed, dataBits, stopBits, parity, flow);
					int scanRes = dt.test ();
					if ( dt.test () != 0 )
					{
						if ( ! quiet ) System.out.println ();
						if ( afterPort != null ) afterPort.run ();
						continue;
					}
					if ( ! quiet ) System.out.println (gotAnsStr);
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
							} catch (Exception ex) {}
							if ( type == null )
								type = emptyStr;
							if ( ! type.isEmpty () )
								type += comma + space;
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
					if ( afterPort != null ) afterPort.run ();
				}
			}
		}
		if (active == 0)
		{
			if ( ! quiet ) System.out.println (noAnsStr);
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
				if ( portID == null || ! (portID instanceof CommPortIdentifier) ) continue;

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
