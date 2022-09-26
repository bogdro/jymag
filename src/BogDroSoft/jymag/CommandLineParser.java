/*
 * CommandLineParser.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2020 Bogdan Drozdowski, bogdandr (at) op.pl
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

import BogDroSoft.jymag.comm.DataTransporter;
import BogDroSoft.jymag.comm.TransferParameters;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.gui.MainWindow;
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
	private static volatile float sBits = 1;
	private static volatile int speed = 115200;
	private static volatile int flow = 0;
	private static volatile int parity = 0;
	private static volatile String portName = null;
	private static volatile boolean isMax = false;
	private static volatile int x = 0;
	private static volatile int y = 0;
	private static boolean deleteAfterDownload = false;

	// ----------- i18n stuff
	private static final ResourceBundle b = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String progIntroStr = b.getString("is_a_program_") +
		b.getString("rxtx_multimedia_Sagem");
	private static final String rxtxReqStr = b.getString("need_rxtx");
	private static final String cmdLineStr = b.getString("Command-line_options:")+
		":" +	// NOI18N
		"\n--conf <file>\t\t- " +	// NOI18N
		b.getString("read_configuration_from_<file>") +
		"\n--databits <5,6,7,8>\t- " +	// NOI18N
		b.getString("set_the_number_of_data_bits")+
		"\n--delete-after-download\t- " +	// NOI18N
		b.getString("delete_downloaded")+
		"\n--delete-alarm <N>\t- " +	// NOI18N
		b.getString("delete_alarm") +
		"\n--delete-element <ID>\t- " +	// NOI18N
		b.getString("delete_element") +
		"\n--delete-sms <N>\t- "+	// NOI18N
		b.getString("delete_sms")+
		"\n--dial-data <number>\t- "+	// NOI18N
		b.getString("help_dial_data")+
		"\n--dial-voice <number>\t- "+	// NOI18N
		b.getString("help_dial_voice")+
		"\n--download-dir <dir>\t- "+	// NOI18N
		b.getString("set_default_download_dir")+
		"\n--download-all-animations\t- "+	// NOI18N
		b.getString("download_all_videos")+
		"\n--download-all-events\t- "+	// NOI18N
		b.getString("download_all_events")+
		"\n--download-all-photos\t- "+	// NOI18N
		b.getString("download_all_photos")+
		"\n--download-all-ringtones - "+	// NOI18N
		b.getString("download_all_ringtones")+
		"\n--download-all-todo\t- "+	// NOI18N
		b.getString("download_all_to-do_tasks")+
		"\n--download-all-vcards\t- "+	// NOI18N
		b.getString("download_all_addressbook")+
		"\n--download-all\t\t- "+	// NOI18N
		b.getString("combine_all_download")+
		"\n--flow <none,soft,hard,soft+hard>\t- "+	// NOI18N
		b.getString("set_the_flow_control_mode")+
		"\n--hangup\t\t- "+	// NOI18N
		b.getString("help_hangup")+
		"\n--help, -h, -?, /?\t- "+	// NOI18N
		b.getString("display_help")+
		"\n--lang LL_CC_VV\t\t- "+	// NOI18N
		b.getString("select_the_language")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("LL_is_the_language_CC")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("__country_code,_VV")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("__Separate_them_using_underscores._Only_LL_is_required.")+
		"\n--licence, --license\t- "+	// NOI18N
		b.getString("display_license_information")+
		"\n--list-alarms\t\t- "+	// NOI18N
		b.getString("list_alarms")+
		"\n--list-elements\t\t- "+	// NOI18N
		b.getString("list_elements")+
		"\n--list-sms\t\t- "+	// NOI18N
		b.getString("list_sms")+
		"\n--parity <none,even,odd,space,mark>\t- "+	// NOI18N
		b.getString("set_the_parity_mode")+
		"\n--port <filename>\t- "+	// NOI18N
		b.getString("set_the_default_port")+
		"\n--scan\t\t\t- "+	// NOI18N
		b.getString("scan_available_ports")+
		"\n--send-cmd-file <file>\t- "+	// NOI18N
		b.getString("send_cmd_file")+
		"\n--send-sms <number> <msg>\t- "+	// NOI18N
		b.getString("send_sms")+
		"\n--speed"+	// NOI18N
		" <1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200," +	// NOI18N
		"\n\t230400, 460800, 500000, 576000, 921600, 1000000, 1152000,\n" +	// NOI18N
		"\t1500000, 2000000, 2500000, 3000000, 3500000, 4000000>\n\t\t\t- " +	// NOI18N
		b.getString("set_the_port_speed")+
		"\n--stopbits <1,1.5,2>\t- "+	// NOI18N
		b.getString("set_stop_bits")+
		"\n--upload <filename>\t- "+	// NOI18N
		b.getString("upload_file")+
		"\n--update-alarm \"DD/MM/YY,HH:MM:SS\",N,\"days\"\t- "+	// NOI18N
		b.getString("update_alarm")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("N_IS_THE_NUMBER")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("COMMA_SEPARATED_DAYS")+
		"\n\t\t\t  " +	// NOI18N
		b.getString("ZERO_MEANS_ALL")+
		"\n--version, -v\t\t- "+	// NOI18N
		b.getString("display_version")+
		"\n" +	// NOI18N
		"\n" +	// NOI18N
		b.getString("exit_zero_code");
	private static final String verWord = b.getString("Version");
	//private static final String getListStr = b.getString("Getting_list_of_");
	//private static final String getFileStr = b.getString("Getting_file");

	// non-instantiable
	private CommandLineParser () {}

	/**
	 * Gets the current destination directory name.
	 * @return the current destination directory name.
	 */
	public synchronized static String getDstDirName ()
	{
		return destDirName;
	}

	/**
	 * Gets the current number of data bits.
	 * @return the current number of data bits.
	 */
	public synchronized static int getDBits ()
	{
		return dBits;
	}

	/**
	 * Gets the current number of stop bits.
	 * @return the current number of stop bits.
	 */
	public synchronized static float getSBits ()
	{
		return sBits;
	}

	/**
	 * Gets the current port speed.
	 * @return the current port speed.
	 */
	public synchronized static int getSpeed ()
	{
		return speed;
	}

	/**
	 * Gets the current flow control mode.
	 * @return the current flow control mode.
	 */
	public synchronized static int getFlowMode ()
	{
		return flow;
	}

	/**
	 * Gets the current parity mode.
	 * @return the current parity mode.
	 */
	public synchronized static int getParityMode ()
	{
		return parity;
	}

	/**
	 * Gets the current port name.
	 * @return the current port name.
	 */
	public synchronized static String getPortName ()
	{
		return portName;
	}

	/**
	 * Gets the current "is maximized" property value.
	 * @return the current  "is maximized" property value.
	 */
	public synchronized static boolean isMax ()
	{
		return isMax;
	}

	/**
	 * Gets the current X coordinate.
	 * @return the current X coordinate.
	 */
	public synchronized static int getX ()
	{
		return x;
	}

	/**
	 * Gets the current Y coordinate.
	 * @return the current Y coordinate.
	 */
	public synchronized static int getY ()
	{
		return y;
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
			int l_sBits = cfg.getSBits ();
			if ( l_sBits == 2 )
			{
				sBits = 2.0f;
			}
			else if ( l_sBits == 1 )
			{
				sBits = 1.5f;
			}
			else
			{
				sBits = 1.0f;
			}
			flow = cfg.getFlowCtl ();
			isMax = cfg.getIsMax ();
			x = cfg.getX ();
			y = cfg.getY ();
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
			if ( currentArg.equals ("--help")	// NOI18N
				|| currentArg.equals ("-h")	// NOI18N
				|| currentArg.equals ("-?")	// NOI18N
				|| currentArg.equals ("/?") )	// NOI18N
			{
				System.out.println ("JYMAG (Java Your Music and Graphics) " + progIntroStr +	// NOI18N
					"\n\n*** " + rxtxReqStr + " ***\n\n" +	// NOI18N
					"Author: Bogdan Drozdowski, bogdandr @ op . pl\n" +	// NOI18N
					"License: GPLv3+\n" +	// NOI18N
					"http://jymag.sf.net\n\n" +	// NOI18N
					cmdLineStr
					);
				Starter.closeProgram (0);
			}
			else if ( currentArg.equals ("--license")	// NOI18N
				|| currentArg.equals ("--licence") )	// NOI18N
			{
				System.out.println ("JYMAG (Java Your Music and Graphics) "+ progIntroStr +	// NOI18N
					"\nSee http://jymag.sf.net\n" +	// NOI18N
					"Author: Bogdan 'bogdro' Drozdowski, bogdandr @ op . pl.\n\n" +	// NOI18N
					"    This program is free software; you can redistribute it and/or\n" +	// NOI18N
					"    modify it under the terms of the GNU General Public License\n" +	// NOI18N
					"    as published by the Free Software Foundation; either version 3\n" +	// NOI18N
					"    of the License, or (at your option) any later version.\n\n" +	// NOI18N
					"    This program is distributed in the hope that it will be useful,\n" +	// NOI18N
					"    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +	// NOI18N
					"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +	// NOI18N
					"    GNU General Public License for more details.\n\n" +	// NOI18N
					"    You should have received a copy of the GNU General Public License\n" +	// NOI18N
					"    along with this program; if not, write to the Free Software Foundation:\n" +	// NOI18N
					"               Free Software Foundation\n" +	// NOI18N
					"               51 Franklin Street, Fifth Floor\n" +	// NOI18N
					"               Boston, MA 02110-1301\n" +	// NOI18N
					"               USA\n");	// NOI18N
				Starter.closeProgram (0);
			}
			else if ( currentArg.equals ("--version")	// NOI18N
				|| currentArg.equals ("-v") )	// NOI18N
			{
				System.out.println ("JYMAG " + verWord + " " + MainWindow.JYMAG_VERSION);	// NOI18N
				Starter.closeProgram (0);
			}
			else if ( currentArg.equals ("--port") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					portName = args[i+1];
					i++;
				}
			}
			else if ( currentArg.equals ("--parity") )	// NOI18N
			{
				parity = 0;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					// <none, even, odd, space, mark>
					if ( nextArg.equals ("none") )	// NOI18N
					{
						parity = 0;
					}
					else if ( nextArg.equals ("even") )	// NOI18N
					{
						parity = 1;
					}
					else if ( nextArg.equals ("odd") )	// NOI18N
					{
						parity = 2;
					}
					else if ( nextArg.equals ("space") )	// NOI18N
					{
						parity = 3;
					}
					else if ( nextArg.equals ("mark") )	// NOI18N
					{
						parity = 4;
					}
					i++;
				}
			}
			else if ( currentArg.equals ("--databits") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						dBits = Integer.parseInt (args[i+1]);
					}
					catch (Exception ex)
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
			else if ( currentArg.equals ("--speed") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						speed = Integer.parseInt (args[i+1]);
					}
					catch (Exception ex)
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
			else if ( currentArg.equals ("--stopbits") )	// NOI18N
			{
				sBits = 1;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					if ( nextArg.equals ("2") )	// NOI18N
					{
						sBits = 2;
					}
					else if ( nextArg.equals ("1.5")	// NOI18N
						|| nextArg.equals ("1,5") )	// NOI18N
					{
						sBits = 1.5f;
					}
					else
					{
						sBits = 1;
					}
					i++;
				}
			}
			else if ( currentArg.equals ("--flow") )	// NOI18N
			{
				flow = 0;
				if ( i < args.length-1 )
				{
					String nextArg = args[i+1].toLowerCase (Locale.ENGLISH);
					// <none,soft,hard>
					if ( nextArg.equals ("none") )	// NOI18N
					{
						flow = 0;
					}
					else if ( nextArg.equals ("soft") )	// NOI18N
					{
						flow = 1;
					}
					else if ( nextArg.equals ("hard") )	// NOI18N
					{
						flow = 2;
					}
					else if ( nextArg.equals ("soft+hard")	// NOI18N
						|| nextArg.equals ("hard+soft") )	// NOI18N
					{
						flow = 3;
					}
					i++;
				}
			}
			else if ( currentArg.equals ("--scan") )	// NOI18N
			{
				Starter.closeProgram (
					TransferUtils.scanPorts (false,
						getTransferParameters (sync),
						null, null, null, null, null, null));
			}
			else if ( currentArg.equals ("--update-alarm") )	// NOI18N
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
			else if ( currentArg.equals ("--delete-alarm") )	// NOI18N
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
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDelete(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( currentArg.equals ("--list-alarms") )	// NOI18N
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
			else if ( currentArg.equals ("--list-elements") )	// NOI18N
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
			else if ( currentArg.equals ("--delete-element") )	// NOI18N
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
			else if ( currentArg.equals ("--list-sms") )	// NOI18N
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
			else if ( currentArg.equals ("--send-sms") )	// NOI18N
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
			else if ( currentArg.equals ("--delete-sms") )	// NOI18N
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
			else if ( currentArg.equals ("--upload") )	// NOI18N
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
			else if ( currentArg.equals ("--download-all-photos") )	// NOI18N
			{
				Starter.closeProgram (getAllPics (sync));
			}
			else if ( currentArg.equals ("--download-all-ringtones") )	// NOI18N
			{
				Starter.closeProgram (getAllRings (sync));
			}
			else if ( currentArg.equals ("--download-all-todo") )	// NOI18N
			{
				Starter.closeProgram (getAllTODOs (sync));
			}
			else if ( currentArg.equals ("--download-all-events") )	// NOI18N
			{
				Starter.closeProgram (getAllEvents (sync));
			}
			else if ( currentArg.equals ("--download-all-vcards") )	// NOI18N
			{
				Starter.closeProgram (getAllVcards (sync));
			}
			else if ( currentArg.equals ("--download-all-animations") )	// NOI18N
			{
				Starter.closeProgram (getAllAnimations (sync));
			}
			else if ( currentArg.equals ("--download-all") )	// NOI18N
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
			else if ( currentArg.equals ("--download-dir") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					destDirName = args[i+1];
					i++;
				}
			}
			else if ( currentArg.equals ("--delete-after-download") )	// NOI18N
			{
				deleteAfterDownload = true;
			}
			else if ( currentArg.equals ("--lang") )	// NOI18N
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
			else if ( currentArg.equals ("--conf") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					readConfig (new File (args[i+1]));
				}
			}
			else if ( currentArg.equals ("--dial-voice") )	// NOI18N
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
			else if ( currentArg.equals ("--dial-data") )	// NOI18N
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
			else if ( currentArg.equals ("--hangup") )	// NOI18N
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
			else if ( currentArg.equals ("--send-cmd-file") )	// NOI18N
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
		}	// for i
	}
}
