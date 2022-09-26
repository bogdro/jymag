/*
 * CommandLineParser.java, part of the JYMAG package.
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

import java.io.File;
import java.util.Locale;

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
	private static final String progIntroStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("is_a_program_") +
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("rxtx_multimedia_Sagem");
	private static final String rxtxReqStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("need_rxtx");
	private static final String cmdLineStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Command-line_options:")+
		":" +	// NOI18N
		"\n--conf <file>\t\t- " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("read_configuration_from_<file>") +
		"\n--databits <5,6,7,8>\t- " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_number_of_data_bits")+
		"\n--delete-after-download\t- " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("delete_downloaded")+
		"\n--delete-alarm <N>\t- " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("delete_alarm") +
		"\n--download-dir <dir>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_default_download_dir")+
		"\n--download-all-animations\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_videos")+
		"\n--download-all-events\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_events")+
		"\n--download-all-photos\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_photos")+
		"\n--download-all-ringtones - "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_ringtones")+
		"\n--download-all-todo\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_to-do_tasks")+
		"\n--download-all-vcards\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("download_all_addressbook")+
		"\n--download-all\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("combine_all_download")+
		"\n--flow <none,soft,hard,soft+hard>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_flow_control_mode")+
		"\n--help, -h, -?, /?\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("display_help")+
		"\n--lang LL_CC_VV\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("select_the_language")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("LL_is_the_language_CC")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("__country_code,_VV")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("__Separate_them_using_underscores._Only_LL_is_required.")+
		"\n--licence, --license\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("display_license_information")+
		"\n--parity <none,even,odd,space,mark>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_parity_mode")+
		"\n--port <filename>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_default_port")+
		"\n--scan\t\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("scan_available_ports")+
		"\n--speed"+	// NOI18N
		" <1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200," +	// NOI18N
		"\n\t230400, 460800, 500000, 576000, 921600, 1000000, 1152000,\n" +	// NOI18N
		"\t1500000, 2000000, 2500000, 3000000, 3500000, 4000000>\n\t\t\t- " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_port_speed")+
		"\n--stopbits <1,1.5,2>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_stop_bits")+
		"\n--upload <filename>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("upload_file")+
		"\n--update-alarm \"DD/MM/YY,HH:MM:SS\",N,\"days\"\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("update_alarm")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("N_IS_THE_NUMBER")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("COMMA_SEPARATED_DAYS")+
		"\n\t\t\t  " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("ZERO_MEANS_ALL")+
		"\n--version, -v\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("display_version")+
		"\n" +	// NOI18N
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("exit_zero_code");
	private static final String verWord = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Version");
	private static final String getListStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Getting_list_of_");
	private static final String getFileStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Getting_file");

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
		if ( f == null ) return;
		if ( (! f.exists ()) || (! f.canRead ()) ) return;

		try
		{
			ConfigFile cfg = new ConfigFile (f);
			cfg.read ();
			portName = cfg.getPort ().trim ();
			speed = cfg.getSpeed ();
			dBits = cfg.getDBits ();
			parity = cfg.getParity ();
			int l_sBits = cfg.getSBits ();
			if ( l_sBits == 2 ) sBits = 2.0f;
			else if ( l_sBits == 1 ) sBits = 1.5f;
			else sBits = 1.0f;
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
			type,
			TransferUtils.getIdentifierForPort
			(portName), speed, dBits, sBits,
			parity, flow, null, null, sync,
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
	 * Gets all the TODO entries from the phone to the directory specified
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
	 * @param logfile The name of the log file.
	 */
	public static void parse (String[] args, Object sync, String logfile)
	{
		if ( args == null ) return;

		for ( int i = 0; i < args.length; i++ )
		{
			if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--help")	// NOI18N
				|| args[i].toLowerCase (Locale.ENGLISH).equals ("-h")	// NOI18N
				|| args[i].toLowerCase (Locale.ENGLISH).equals ("-?")	// NOI18N
				|| args[i].toLowerCase (Locale.ENGLISH).equals ("/?") )	// NOI18N
			{
				System.out.println ("JYMAG (Java Your Music and Graphics) " + progIntroStr +	// NOI18N
					"\n\n*** " + rxtxReqStr + " ***\n\n" +	// NOI18N
					"Author: Bogdan Drozdowski, bogdandr @ op . pl\n" +	// NOI18N
					"License: GPLv3+\n" +	// NOI18N
					"http://rudy.mif.pg.gda.pl/~bogdro/soft\n\n" +	// NOI18N
					cmdLineStr
					);
				Utils.closeProgram (logfile, 0);
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--license")	// NOI18N
				|| args[i].toLowerCase (Locale.ENGLISH).equals ("--licence") )	// NOI18N
			{
				System.out.println ("JYMAG (Java Your Music and Graphics) "+ progIntroStr +	// NOI18N
					"\nSee http://rudy.mif.pg.gda.pl/~bogdro/soft\n" +	// NOI18N
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
				Utils.closeProgram (logfile, 0);
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--version")	// NOI18N
				|| args[i].toLowerCase (Locale.ENGLISH).equals ("-v") )	// NOI18N
			{
				System.out.println ("JYMAG " + verWord + " " + MainWindow.verString);	// NOI18N
				Utils.closeProgram (logfile, 0);
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--port") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					portName = args[i+1];
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--parity") )	// NOI18N
			{
				parity = 0;
				if ( i < args.length-1 )
				{
					// <none, even, odd, space, mark>
					if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("none") )	// NOI18N
					{
						parity = 0;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("even") )	// NOI18N
					{
						parity = 1;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("odd") )	// NOI18N
					{
						parity = 2;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("space") )	// NOI18N
					{
						parity = 3;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("mark") )	// NOI18N
					{
						parity = 4;
					}
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--databits") )	// NOI18N
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
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--speed") )	// NOI18N
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
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--stopbits") )	// NOI18N
			{
				sBits = 1;
				if ( i < args.length-1 )
				{
					if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("2") )	// NOI18N
					{
						sBits = 2;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("1.5")	// NOI18N
						|| args[i+1].toLowerCase (Locale.ENGLISH).equals ("1,5") )	// NOI18N
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
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--flow") )	// NOI18N
			{
				flow = 0;
				if ( i < args.length-1 )
				{
					// <none,soft,hard>
					if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("none") )	// NOI18N
					{
						flow = 0;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("soft") )	// NOI18N
					{
						flow = 1;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("hard") )	// NOI18N
					{
						flow = 2;
					}
					else if ( args[i+1].toLowerCase (Locale.ENGLISH).equals ("soft+hard")	// NOI18N
								|| args[i+1].toLowerCase (Locale.ENGLISH).equals ("hard+soft") )	// NOI18N
					{
						flow = 3;
					}
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--scan") )	// NOI18N
			{
				Utils.closeProgram (logfile,
					TransferUtils.scanPorts (false, speed, dBits, sBits,
					parity, flow, null, null, null, null, null, null));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--update-alarm") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.uploadAlarm (
							PhoneAlarm.parseReponse (args[i+1]),
							TransferUtils.getIdentifierForPort
							(portName), speed, dBits, sBits,
							parity, flow, null, null, sync,
							false, true, true);
						Utils.closeProgram (logfile, res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticUpload(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--delete-alarm") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.deleteAlarm (
							Integer.parseInt (args[i+1]),
							TransferUtils.getIdentifierForPort
							(portName), speed, dBits, sBits,
							parity, flow, null, null, sync,
							false, true, true);
						Utils.closeProgram (logfile, res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticDelete(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--upload") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					try
					{
						int res = TransferUtils.uploadFile (new File (args[i+1]),
							TransferUtils.getIdentifierForPort
							(portName), speed, dBits, sBits,
							parity, flow, null, null, sync,
							false, true, true);
						Utils.closeProgram (logfile, res);
					}
					catch ( Exception ex )
					{
						Utils.handleException (ex,
							"cmdline.staticUpload(" + args[i+1] + ")");	// NOI18N
					}
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-photos") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllPics (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-ringtones") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllRings (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-todo") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllTODOs (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-events") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllEvents (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-vcards") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllVcards (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all-vcards") )	// NOI18N
			{
				Utils.closeProgram (logfile, getAllAnimations (sync));
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-all") )	// NOI18N
			{
				Utils.closeProgram (logfile,
					getAllPics (sync)
					+ getAllRings (sync)
					+ getAllTODOs (sync)
					+ getAllEvents (sync)
					+ getAllVcards (sync)
					+ getAllAnimations (sync)
					);
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--download-dir") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					destDirName = args[i+1];
					i++;
				}
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--delete-after-download") )	// NOI18N
			{
				deleteAfterDownload = true;
			}
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--lang") )	// NOI18N
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
								newLoc = new Locale (locale[0]);
							else if (locale.length == 2)
								newLoc = new Locale (locale[0],
									locale[1]);
							else if (locale.length == 3)
								newLoc = new Locale (locale[0],
									locale[1], locale[2]);
							if ( newLoc != null )
								Locale.setDefault (newLoc);
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
			else if ( args[i].toLowerCase (Locale.ENGLISH).equals ("--conf") )	// NOI18N
			{
				if ( i < args.length-1 )
				{
					readConfig (new File (args[i+1]));
				}
			}
		}	// for i
	}
}