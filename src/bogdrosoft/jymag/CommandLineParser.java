/*
 * CommandLineParser.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.jymag;

import bogdrosoft.jymag.comm.DataTransporter;
import bogdrosoft.jymag.comm.TransferParameters;
import bogdrosoft.jymag.comm.TransferUtils;
import bogdrosoft.jymag.gui.MainWindow;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * CommandLineParser - utility methods connected to parsing the command line.
 * @author Bogdan Drozdowski
 */
public class CommandLineParser
{
	private static String destDirName;
	private static int dBits = 8;
	private static double sBits = 1;
	private static int speed = 115200;
	private static int flow = 0;
	private static int parity = 0;
	private static String portName = null;
	private static boolean isMax = false;
	private static int x = 0;
	private static int y = 0;
	private static int width;
	private static int height;
	private static int fontSize;
	private static int selectedTab;
	private static boolean deleteAfterDownload = false;
	public static boolean mock = false;

	// ----------- i18n stuff
	private static final ResourceBundle MSGS
		= ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow");
	private static final String PROG_INTRO_STRING
		= MSGS.getString("is_a_program_")
		+ MSGS.getString("rxtx_multimedia_Sagem");
	private static final String RXTX_REQ_STRING
		= MSGS.getString("need_rxtx");
	private static final String CMD_LINE_STR
		= MSGS.getString("Command-line_options:")+
		":" +	// NOI18N
		"\n--conf <file>\t\t- " +	// NOI18N
		MSGS.getString("read_configuration_from_<file>") +
		"\n--databits <5,6,7,8>\t- " +	// NOI18N
		MSGS.getString("set_the_number_of_data_bits")+
		"\n--delete-after-download\t- " +	// NOI18N
		MSGS.getString("delete_downloaded")+
		"\n--delete-alarm <N>\t- " +	// NOI18N
		MSGS.getString("delete_alarm") +
		"\n--delete-element <ID>\t- " +	// NOI18N
		MSGS.getString("delete_element") +
		"\n--delete-sms <N>\t- "+	// NOI18N
		MSGS.getString("delete_sms")+
		"\n--dial-data <number>\t- "+	// NOI18N
		MSGS.getString("help_dial_data")+
		"\n--dial-voice <number>\t- "+	// NOI18N
		MSGS.getString("help_dial_voice")+
		"\n--download-dir <dir>\t- "+	// NOI18N
		MSGS.getString("set_default_download_dir")+
		"\n--download-all-animations\t- "+	// NOI18N
		MSGS.getString("download_all_videos")+
		"\n--download-all-events\t- "+	// NOI18N
		MSGS.getString("download_all_events")+
		"\n--download-all-photos\t- "+	// NOI18N
		MSGS.getString("download_all_photos")+
		"\n--download-all-ringtones - "+	// NOI18N
		MSGS.getString("download_all_ringtones")+
		"\n--download-all-todo\t- "+	// NOI18N
		MSGS.getString("download_all_to-do_tasks")+
		"\n--download-all-vcards\t- "+	// NOI18N
		MSGS.getString("download_all_addressbook")+
		"\n--download-all\t\t- "+	// NOI18N
		MSGS.getString("combine_all_download")+
		"\n--flow <none,soft,hard,soft+hard>\t- "+	// NOI18N
		MSGS.getString("set_the_flow_control_mode")+
		"\n--hangup\t\t- "+	// NOI18N
		MSGS.getString("help_hangup")+
		"\n--help, -h, -?, /?\t- "+	// NOI18N
		MSGS.getString("display_help")+
		"\n--lang LL_CC_VV\t\t- "+	// NOI18N
		MSGS.getString("select_the_language")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("LL_is_the_language_CC")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("__country_code,_VV")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("__Separate_them_using_underscores._Only_LL_is_required.")+
		"\n--licence, --license\t- "+	// NOI18N
		MSGS.getString("display_license_information")+
		"\n--list-alarms\t\t- "+	// NOI18N
		MSGS.getString("list_alarms")+
		"\n--list-elements\t\t- "+	// NOI18N
		MSGS.getString("list_elements")+
		"\n--list-sms\t\t- "+	// NOI18N
		MSGS.getString("list_sms")+
		"\n--parity <none,even,odd,space,mark>\t- "+	// NOI18N
		MSGS.getString("set_the_parity_mode")+
		"\n--port <filename>\t- "+	// NOI18N
		MSGS.getString("set_the_default_port")+
		"\n--scan\t\t\t- "+	// NOI18N
		MSGS.getString("scan_available_ports")+
		"\n--send-cmd-file <file>\t- "+	// NOI18N
		MSGS.getString("send_cmd_file")+
		"\n--send-sms <number> <msg>\t- "+	// NOI18N
		MSGS.getString("send_sms")+
		"\n--speed"+	// NOI18N
		" <1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200," +	// NOI18N
		"\n\t230400, 460800, 500000, 576000, 921600, 1000000, 1152000,\n" +	// NOI18N
		"\t1500000, 2000000, 2500000, 3000000, 3500000, 4000000>\n\t\t\t- " +	// NOI18N
		MSGS.getString("set_the_port_speed")+
		"\n--stopbits <1,1.5,2>\t- "+	// NOI18N
		MSGS.getString("set_stop_bits")+
		"\n--upload <filename>\t- "+	// NOI18N
		MSGS.getString("upload_file")+
		"\n--update-alarm \"DD/MM/YY,HH:MM:SS\",N,\"days\"\t- "+	// NOI18N
		MSGS.getString("update_alarm")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("N_IS_THE_NUMBER")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("COMMA_SEPARATED_DAYS")+
		"\n\t\t\t  " +	// NOI18N
		MSGS.getString("ZERO_MEANS_ALL")+
		"\n--version, -v\t\t- "+	// NOI18N
		MSGS.getString("display_version")+
		"\n" +	// NOI18N
		"\n" +	// NOI18N
		MSGS.getString("exit_zero_code");
	private static final String VER_WORD = MSGS.getString("Version");
	//private static final String getListStr = b.getString("Getting_list_of_");
	//private static final String getFileStr = b.getString("Getting_file");

	// non-instantiable
	private CommandLineParser () {}

	/**
	 * Gets the current destination directory name.
	 * @return the current destination directory name.
	 */
	public static synchronized String getDstDirName ()
	{
		return destDirName;
	}

	/**
	 * Gets the current number of data bits.
	 * @return the current number of data bits.
	 */
	public static synchronized int getDBits ()
	{
		return dBits;
	}

	/**
	 * Gets the current number of stop bits.
	 * @return the current number of stop bits.
	 */
	public static synchronized double getSBits ()
	{
		return sBits;
	}

	/**
	 * Gets the current port speed.
	 * @return the current port speed.
	 */
	public static synchronized int getSpeed ()
	{
		return speed;
	}

	/**
	 * Gets the current flow control mode.
	 * @return the current flow control mode.
	 */
	public static synchronized int getFlowMode ()
	{
		return flow;
	}

	/**
	 * Gets the current parity mode.
	 * @return the current parity mode.
	 */
	public static synchronized int getParityMode ()
	{
		return parity;
	}

	/**
	 * Gets the current port name.
	 * @return the current port name.
	 */
	public static synchronized String getPortName ()
	{
		return portName;
	}

	/**
	 * Gets the current "is maximized" property value.
	 * @return the current  "is maximized" property value.
	 */
	public static synchronized boolean isMax ()
	{
		return isMax;
	}

	/**
	 * Gets the current X coordinate.
	 * @return the current X coordinate.
	 */
	public static synchronized int getX ()
	{
		return x;
	}

	/**
	 * Gets the current Y coordinate.
	 * @return the current Y coordinate.
	 */
	public static synchronized int getY ()
	{
		return y;
	}

	/**
	 * Gets the current width for the window.
	 * @return the current width for the window.
	 */
	public static synchronized int getWidth ()
	{
		return width;
	}

	/**
	 * Gets the current height for the window.
	 * @return the current height for the window.
	 */
	public static synchronized int getHeight ()
	{
		return height;
	}

	/**
	 * Gets the current font size.
	 * @return the current font size.
	 */
	public static synchronized int getFontSize ()
	{
		return fontSize;
	}

	/**
	 * Gets the current selected tab.
	 * @return the current selected tab.
	 */
	public static synchronized int getSelectedTab ()
	{
		return selectedTab;
	}

        /**
	 * Reads the configuration from the given file and sets fields.
	 * @param f The file to read the config from.
	 */
	private static void readConfig (File f)
	{
		if ( f == null )
		{
			return;
		}
		if ( (! f.exists ()) || (! f.canRead ()) )
		{
			return;
		}

		try
		{
			ConfigFile cfg = new ConfigFile (f);
			cfg.read ();
			portName = cfg.getPort ().trim ();
			speed = cfg.getSpeed ();
			dBits = cfg.getDBits ();
			parity = cfg.getParity ();
			int cfgStopBits = cfg.getSBits ();
			if ( cfgStopBits == 2 )
			{
				sBits = 2.0;
			}
			else if ( cfgStopBits == 1 )
			{
				sBits = 1.5;
			}
			else
			{
				sBits = 1.0;
			}
			flow = cfg.getFlowCtl ();
			isMax = cfg.getIsMax ();
			x = cfg.getX ();
			y = cfg.getY ();
			width = cfg.getWidth();
			height = cfg.getHeight();
			fontSize = cfg.getFontSizeValue();
			selectedTab = cfg.getSelectedTab();
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "static readConfig");	// NOI18N
		}
	}

	private static TransferParameters getTransferParameters (Object sync)
	{
		return new TransferParameters (
			portName, speed, dBits, sBits,
			parity, flow, sync);
	}

	/**
	 * Gets all the elements of the given type from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param type The type of the elements to get.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getElementsOfType (String type, Object sync)
	{
		return TransferUtils.downloadFiles (
			type, getTransferParameters (sync), null, null,
			false, true, true, destDirName, deleteAfterDownload);
	}

	/**
	 * Gets all the pictures from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllPics (Object sync)
	{
		return getElementsOfType ("PICTURES", sync);	// NOI18N
	}

	/**
	 * Gets all the ringtones from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllRings (Object sync)
	{
		return getElementsOfType ("RINGTONES", sync);	// NOI18N
	}

	/**
	 * Gets all the TO-DO entries from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllTODOs (Object sync)
	{
		return getElementsOfType ("VTODO", sync);	// NOI18N
	}

	/**
	 * Gets all the events from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllEvents (Object sync)
	{
		return getElementsOfType ("VEVENT", sync);	// NOI18N
	}

	/**
	 * Gets all the addressbook entries from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllVcards (Object sync)
	{
		return getElementsOfType ("VCARDS", sync);	// NOI18N
	}

	/**
	 * Gets all the videos/animations from the phone to the directory specified
	 *	on the command line or to the current directory.
	 * @param sync The synchronization object.
	 * @return 0 on success.
	 */
	private static int getAllAnimations (Object sync)
	{
		return getElementsOfType ("ANIMATIONS", sync);	// NOI18N
	}

	/**
	 * Parses the given command line and performs actions based on it.
	 * @param args The command line to parse.
	 * @param sync The synchronization variable.
	 */
	public static void parse (String[] args, Object sync)
	{
		if ( args == null )
		{
			return;
		}

		for ( int i = 0; i < args.length; i++ )
		{
			String currentArg = args[i].toLowerCase (Locale.ENGLISH);
			if ( "--help".equals (currentArg)	// NOI18N
				|| "-h".equals (currentArg)	// NOI18N
				|| "-?".equals (currentArg)	// NOI18N
				|| "/?".equals (currentArg) )	// NOI18N
			{
				System.out.println ("JYMAG (Jig Your Music and Graphics) " + PROG_INTRO_STRING +	// NOI18N
					"\n\n*** " + RXTX_REQ_STRING + " ***\n\n" +	// NOI18N
					"Author: Bogdan Drozdowski, bogdro (at) users . sourceforge . net\n" +	// NOI18N
					"License: GPLv3+\n" +	// NOI18N
					"https://jymag.sourceforge.io/\n\n" +	// NOI18N
					CMD_LINE_STR
					);
				Starter.closeProgram (0);
			}
			else if ( "--license".equals (currentArg)	// NOI18N
				|| "--licence".equals (currentArg) )	// NOI18N
			{
				System.out.println ("JYMAG (Jig Your Music and Graphics) "+ PROG_INTRO_STRING +	// NOI18N
					"\nSee https://jymag.sourceforge.io/\n" +	// NOI18N
					"Author: Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net.\n\n" +	// NOI18N
					"    This program is free software: you can redistribute it and/or modify\n" +	// NOI18N
					"    it under the terms of the GNU General Public License as published by\n" +	// NOI18N
					"    the Free Software Foundation, either version 3 of the License, or\n" +	// NOI18N
					"    (at your option) any later version.\n" +	// NOI18N
					"    This program is distributed in the hope that it will be useful,\n" +	// NOI18N
					"    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +	// NOI18N
					"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +	// NOI18N
					"    GNU General Public License for more details.\n" +	// NOI18N
					"    You should have received a copy of the GNU General Public License\n" +	// NOI18N
					"    along with this program.  If not, see <http://www.gnu.org/licenses/>.\n"	// NOI18N
					);
				Starter.closeProgram (0);
			}
			else if ( "--version".equals (currentArg)	// NOI18N
				|| "-v".equals (currentArg) )	// NOI18N
			{
				System.out.println ("JYMAG " + VER_WORD + " " + MainWindow.JYMAG_VERSION);	// NOI18N
				Starter.closeProgram (0);
			}
			else if ( "--port".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					portName = args[i+1];
					i++;
				}
			}
			else if ( "--parity".equals (currentArg) )	// NOI18N
			{
				parity = 0;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					// <none, even, odd, space, mark>
					if ( "none".equals (nextArg) )	// NOI18N
					{
						parity = 0;
					}
					else if ( "even".equals (nextArg) )	// NOI18N
					{
						parity = 1;
					}
					else if ( "odd".equals (nextArg) )	// NOI18N
					{
						parity = 2;
					}
					else if ( "space".equals (nextArg) )	// NOI18N
					{
						parity = 3;
					}
					else if ( "mark".equals (nextArg) )	// NOI18N
					{
						parity = 4;
					}
					i++;
				}
			}
			else if ( "--databits".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						dBits = Integer.parseInt (args[i+1]);
					}
					catch (NumberFormatException ex)
					{
						dBits = -1;
						Utils.handleException (ex,
							"Integer.parseInt:'" + args[i+1] + "'");	// NOI18N
					}
					if ( ! Utils.isAllowableDataBits (dBits) )
					{
						dBits = 8;
					}
					i++;
				}
			}
			else if ( "--speed".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						speed = Integer.parseInt (args[i+1]);
					}
					catch (NumberFormatException ex)
					{
						speed = -1;
						Utils.handleException (ex,
							"Integer.parseInt:'" + args[i+1] + "'");	// NOI18N
					}
					if ( ! Utils.isAllowableSpeed (speed) )
					{
						speed = 115200;
					}
					i++;
				}
			}
			else if ( "--stopbits".equals (currentArg) )	// NOI18N
			{
				sBits = 1;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					if ( "2".equals (nextArg) )	// NOI18N
					{
						sBits = 2;
					}
					else if ( "1.5".equals (nextArg)	// NOI18N
						|| "1,5".equals (nextArg) )	// NOI18N
					{
						sBits = 1.5;
					}
					else
					{
						sBits = 1;
					}
					i++;
				}
			}
			else if ( "--flow".equals (currentArg) )	// NOI18N
			{
				flow = 0;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					// <none,soft,hard>
					if ( "none".equals (nextArg) )	// NOI18N
					{
						flow = 0;
					}
					else if ( "soft".equals (nextArg) )	// NOI18N
					{
						flow = 1;
					}
					else if ( "hard".equals (nextArg) )	// NOI18N
					{
						flow = 2;
					}
					else if ( "soft+hard".equals (nextArg)	// NOI18N
						|| "hard+soft".equals (nextArg) )	// NOI18N
					{
						flow = 3;
					}
					i++;
				}
			}
			else if ( "--scan".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (
					TransferUtils.scanPorts (false,
						getTransferParameters (sync),
						null, null, null, null, null, null));
			}
			else if ( "--update-alarm".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.uploadAlarm (
							PhoneAlarm.parseReponse (args[i+1]),
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticUpload(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--delete-alarm".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.deleteAlarm (
							Integer.parseInt (args[i+1]),
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( NumberFormatException ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDelete(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--list-alarms".equals (currentArg) )	// NOI18N
			{
				try
				{
					Vector<PhoneAlarm> vmsg = new Vector<PhoneAlarm> ();
					int res = TransferUtils.downloadAlarmList (
						getTransferParameters (sync),
						null, null, false, true, true, null, vmsg);
					for ( int j = 0; j < vmsg.size (); j++ )
					{
						if ( vmsg.get (j) == null )
						{
							continue;
						}
						System.out.println (vmsg.get (j));
					}
					Starter.closeProgram (res);
				}
				catch ( Exception ex )
				{
					Utils.handleException (ex,
						"cmdline.downloadAlarmList()");	// NOI18N
				}
			}
			else if ( "--list-elements".equals (currentArg) )	// NOI18N
			{
				try
				{
					TransferParameters tp = getTransferParameters (sync);
					Vector<PhoneElement> total = new Vector<PhoneElement> ();
					Vector<PhoneElement> vmsg = new Vector<PhoneElement> ();
					int res = TransferUtils.downloadList ("PICTURES",	// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					res += TransferUtils.downloadList ("RINGTONES",		// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					res += TransferUtils.downloadList ("VTODO",		// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					res += TransferUtils.downloadList ("VEVENT",		// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					res += TransferUtils.downloadList ("VCARDS",		// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					res += TransferUtils.downloadList ("ANIMATIONS",	// NOI18N
						tp, null, null,
						false, true, true, null, vmsg);
					total.addAll (vmsg);
					for ( int j = 0; j < total.size (); j++ )
					{
						if ( total.get (j) == null )
						{
							continue;
						}
						System.out.println (total.get (j));
					}
					Starter.closeProgram (res);
				}
				catch ( Exception ex )
				{
					Utils.handleException (ex,
						"cmdline.downloadList()");	// NOI18N
				}
			}
			else if ( "--delete-element".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						// only the ID is important
						PhoneElement pe = new PhoneElement(args[i+1], "", "");
						int res = TransferUtils.deleteFile (pe,
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDelete(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--list-sms".equals (currentArg) )	// NOI18N
			{
				try
				{
					Vector<PhoneMessage> vmsg = new Vector<PhoneMessage> ();
					int res = TransferUtils.downloadMessageList (
						getTransferParameters (sync), null, null,
						false, true, true, null, vmsg);
					for ( int j = 0; j < vmsg.size (); j++ )
					{
						if ( vmsg.get (j) == null )
						{
							continue;
						}
						System.out.println (vmsg.get (j));
					}
					Starter.closeProgram (res);
				}
				catch ( Exception ex )
				{
					Utils.handleException (ex,
						"cmdline.downloadMessageList()");	// NOI18N
				}
			}
			else if ( "--send-sms".equals (currentArg) )	// NOI18N
			{
				// we need 2 more elements: the recipient's number and the message body
				if ( i < args.length-2 )
				{
					try
					{
						PhoneMessage pmsg = new PhoneMessage ();
						pmsg.setRecipientNum (args[i+1]);
						pmsg.setMessage (args[i+2]);
						int res = TransferUtils.sendMessage (pmsg,
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.sendMessage(" + args[i+1] + ","	// NOI18N
							+ args[i+2] + ")");				// NOI18N
					}
					i+=2;
				}
			}
			else if ( "--delete-sms".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						PhoneMessage pmsg = new PhoneMessage ();
						pmsg.setID (args[i+1]);
						int res = TransferUtils.deleteMessage (pmsg,
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.deleteMessage(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--upload".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.uploadFile (
							new File (args[i+1]),
							getTransferParameters (sync),
							null, null, false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticUpload(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--download-all-photos".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllPics (sync));
			}
			else if ( "--download-all-ringtones".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllRings (sync));
			}
			else if ( "--download-all-todo".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllTODOs (sync));
			}
			else if ( "--download-all-events".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllEvents (sync));
			}
			else if ( "--download-all-vcards".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllVcards (sync));
			}
			else if ( "--download-all-animations".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (getAllAnimations (sync));
			}
			else if ( "--download-all".equals (currentArg) )	// NOI18N
			{
				Starter.closeProgram (
					getAllPics (sync)
					+ getAllRings (sync)
					+ getAllTODOs (sync)
					+ getAllEvents (sync)
					+ getAllVcards (sync)
					+ getAllAnimations (sync)
					);
			}
			else if ( "--download-dir".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					destDirName = args[i+1];
					i++;
				}
			}
			else if ( "--delete-after-download".equals (currentArg) )	// NOI18N
			{
				deleteAfterDownload = true;
			}
			else if ( "--lang".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						String[] locale = args[i+1].split ("_");	// NOI18N
						if ( locale != null )
						{
							Locale newLoc = null;
							if (locale.length == 1)
							{
								newLoc = new Locale (locale[0]);
							}
							else if (locale.length == 2)
							{
								newLoc = new Locale (locale[0],
									locale[1]);
							}
							else if (locale.length == 3)
							{
								newLoc = new Locale (locale[0],
									locale[1], locale[2]);
							}
							if ( newLoc != null )
							{
								Locale.setDefault (newLoc);
							}
						}
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.lang(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--conf".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					readConfig (new File (args[i+1]));
				}
			}
			else if ( "--dial-voice".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.dialNumber (args[i+1],
							true, DataTransporter.DIAL_MODE.AUTO,
							getTransferParameters (sync), null, null,
							false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDialVoice(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--dial-data".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.dialNumber (args[i+1],
							false, DataTransporter.DIAL_MODE.AUTO,
							getTransferParameters (sync), null, null,
							false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDialData(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--hangup".equals (currentArg) )	// NOI18N
			{
				try
				{
					int res = TransferUtils.hangup (
						getTransferParameters (sync), null, null,
						false, true, true);
					Starter.closeProgram (res);
				}
				catch ( Exception ex )
				{
					Utils.handleException (ex,
						"cmdline.staticHangup()");	// NOI18N
				}
			}
			else if ( "--send-cmd-file".equals (currentArg) )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.sendFileAsCommands (
							new File (args[i+1]),
							getTransferParameters (sync), null, null,
							false, true, true);
						Starter.closeProgram (res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.sendCmdFile()");	// NOI18N
					}
					i++;
				}
			}
			else if ( "--mock".equals (currentArg) )	// NOI18N
			{
				mock = true;
			}
		}	// for i
	}
}
