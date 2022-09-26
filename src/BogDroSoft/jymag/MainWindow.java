/*
 * MainWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2008 Bogdan Drozdowski, bogdandr (at) op.pl
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
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the main window of the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MainWindow extends javax.swing.JFrame
{
	private static final long serialVersionUID = 65L;
	private final MainWindow mw = this;
	private final KL kl = new KL ();

	/** Current version number as a String. */
	public static final String verString = "0.7";	// NOI18N
	private Vector<PhoneElement> currentRingElements;
	private Vector<PhoneElement> currentPhotoElements;
	private Vector<PhoneElement> currentAddrElements;
	private Vector<PhoneElement> currentTodoElements;
	private Vector<PhoneElement> currentEventElements;
	private Vector<PhoneElement> currentAnimElements;
	private Vector<PhoneElement> currentJavaElements;

	private String lastSelDir;
	// synchronization variable:
	private Object sync = new Object ();
	// port-firmware pairs and the firmware version pattern, used for displaying:
	private Hashtable<String, String> firmwares;
	private Hashtable<String, String> phoneTypes;
	private static Pattern verPattern;

	private final static String logFile = "jymag.log";	// NOI18N

	private JFileChooser downloadFC;
	private JFileChooser uploadPictFC;
	private JFileChooser uploadRingFC;
	private JFileChooser uploadAddrFC;
	private JFileChooser uploadTODOFC;
	private JFileChooser uploadEventFC;
	private JFileChooser uploadAnimFC;
	private JFileChooser uploadJavaFC;
	private JFileChooser cfgFC;

	// ------------ i18n stuff
	private static String noAnsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("No_answers_received");
	private static String errString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Error");
	private static String multiAnsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Multiple_answers");
	private static String whichString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Which_one");
	private static String exOverString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("_exists._Overwrite");
	private static String overwriteStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Overwrite?");
	/** List of supported operations. */
	private static String suppOperThank = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_opertations:")+
		": \n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Getting_lists")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Downloading")+
		":\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___a)_pictures")+
		": JPG (" + 	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("both_Sagem_and_non-Sagem") +
		"), GIF, BMP, PNG, WBMP, " +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified") +
		":\n\tTIFF, PICT, EPS, PS, SVG, SVG+GZIP, SVG+ZIP\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___b)_ringtones")+
		": MIDI, AMR, WAV, "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": AIFF, IMY, AAC, MP3\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___c)_addressbook_entries_(vCards)")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___d)_to-do_tasks")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___e)_events_(reminders,_)")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___f)_animation/videos:")+
		": MNG, GIF, "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": WMV, MP4, MPEG\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Uploading:")+
		": \n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___a)_pictures:")+
		": JPG, GIF, BMP, PNG, WBMP, "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": EMS_GR, TIFF, PICT, AI, EPS, PS\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___b)_ringtones")+
		": MIDI, WAV (IMA ADPCM, 8000Hz 16bit Mono?), AMR, "+ 	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": IMY, ASG1,\n\tASG2, SSA, MP3, MFI, AAC, AWB, SG1, SG2\n"+	// NOI18N
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___c)_addressbook_entries_(vCards)")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___d)_to-do_tasks")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___e)_events_(reminders,_)")+
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___f)_animation/videos")+
		": MNG, GIF, "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": EMS_AN, MJPG, AVI, MP4, MPEG, 3GP, 3GP2\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("___g)_Java_files")+
		": "+          	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("unverified")+
		": JAR, JAD, JAM\n"+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Deleting")
		+"\n" +	// NOI18N
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Run_with_help")+
		"\n\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Thanks_to:");
	//private static String aboutStr = "About";
	/** A String that says "a Free 'My Pictures and Sounds' replacement." */
	private static String freeMPASStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Free_My_Pictures_and_Sounds");
	/** The "version" word. */
	private static String verWord = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Version");
	private static String picsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_pictures");
	private static String soundsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_sounds");
	private static String addrString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_addressbook_files");
	private static String todoString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_to-do_files");
	private static String eventString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_event_and_task_files");
	private static String animString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_animation/video_files");
	private static String javaString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_Java_files");
	private static String getListStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Getting_list_of_");
	private static String getFileStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Getting_file");
	private static String unsuppType = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Unsupported_file_type:");
	private static String tryPortStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Trying_port_");
	private static String gotAnsStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Got_answer!");
	private static String noAnsStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("No_answers_received");
	private static String progIntroStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("is_a_program_") +
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("rxtx_multimedia_Sagem");
	private static String rxtxReqStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("need_rxtx");
	private static String cmdLineStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Command-line_options:")+
		":" +	// NOI18N	
		"\n--conf <file>\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("read_configuration_from_<file>") +
		"\n--databits <5,6,7,8>\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("set_the_number_of_data_bits")+
		"\n--delete-after-download\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("delete_downloaded")+
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
		"\n--version, -v\t\t- "+	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("display_version")+
		"\n" +	// NOI18N
		"\n" +	// NOI18N
		java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("exit_zero_code");
	private static String deleteQuestion = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("want_to_delete");
	private static String questionString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Question");
	private static String fileNotWriteMsg = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Cant_write_to_file");

	// error messages for file upload:
	private static String uploadMsg1  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("File_upload_init_failed");
	private static String uploadMsg2  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Sending_file_names_length_failed");
	private static String uploadMsg3  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Sending_file_name_failed");
	private static String uploadMsg4  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Format_not_suported_by_phone");
	private static String uploadMsg5  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("File_not_accepted");
	private static String uploadMsg6  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Closing_transmission_failed");
	private static String uploadMsg7  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Exception_occurred");
	private static String uploadMsg8  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Incorrect_parameter");
	private static String uploadMsg9  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Format_not_suported_by_JYMAG");
	private static String uploadMsg10 = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Number_of_attempts_exceeded");
	private static String uploadMsg11 = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Incorrect_parameter");

	private static String downloadMsg1  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Exception_occurred");
	private static String downloadMsg2  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("No_data");
	private static String downloadMsg3  = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Incorrect_parameter");

	private static String pressScanMsg = "(" + java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("(press_Scan)") + ")";	// NOI18N

	// ------------ static variables for command-line

	// read from the command-line:
	private static String destDirName;
	private static int dBits = 8;
	private static float sBits = 1;
	private static int speed = 115200;
	private static int flow = 0;
	private static int parity = 0;
	private static String portName = null;
	private static boolean isMax = false;
	private static int x = 0;
	private static int y = 0;
	private static boolean deleteAfterDownload = false;

	/**
	 * Creates new form MainWindow.
	 */
	private MainWindow ()
	{
		// set uncaught exception handler for GUI threads, just in case:
		try
		{
			Thread[] ths = new Thread[Thread.activeCount () * 5];
			if ( ths != null )
			{
				final int nThreads = Thread.enumerate (ths);
				for ( int i=0; i < nThreads; i++ )
				{
					String name = ths[i].getName ();
					if ( name == null ) continue;
					if ( name.contains ("AWT") // NOI18N
						|| name.contains ("Swing") // NOI18N
						|| name.contains ("Image") // NOI18N
					   )
					{
						ths[i].setUncaughtExceptionHandler (Utils.handler);
					}
				}
			}
		} catch (Throwable th) {}

		initComponents ();

		setTitle (getTitle () + " " + verString);	// NOI18N
		firmware.setText (pressScanMsg);
		phone.setText (pressScanMsg);
		updateStatusLabel (STATUS.READY);

		Enumeration portList = CommPortIdentifier.getPortIdentifiers ();
		while (portList.hasMoreElements ())
		{
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

			if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
			{
				portCombo.addItem (id.getName ());
			}
		}
		if ( portName != null ) portCombo.setSelectedItem (portName);
		dataBitsCombo.setSelectedItem (String.valueOf (dBits));
		if ( Math.abs (sBits-2.0f) < 0.0001 )
			stopBitsCombo.setSelectedIndex (2);
		else if ( Math.abs (sBits-1.5f) < 0.0001 )
			stopBitsCombo.setSelectedIndex (1);
		else stopBitsCombo.setSelectedIndex (0);
		speedCombo.setSelectedItem (String.valueOf (speed));
		switch (flow)
		{
			case 1:
				flowSoft.setSelected (true);
				flowHard.setSelected (false);
				break;
			case 2:
				flowSoft.setSelected (false);
				flowHard.setSelected (true);
				break;
			case 3:
				flowSoft.setSelected (true);
				flowHard.setSelected (true);
				break;
			default:
				flowSoft.setSelected (false);
				flowHard.setSelected (false);
				break;
		}
		parityCombo.setSelectedIndex (parity);
		if ( isMax ) setExtendedState (JFrame.MAXIMIZED_BOTH);
		else setLocation (x, y);
		speedCombo.setMaximumRowCount (speedCombo.getItemCount ());

		/* Make clicking on the table header select all rows */
		photoTable.getTableHeader ().addMouseListener (new MouseAdapter ()
		{
			@Override
			public void mouseClicked (MouseEvent me)
			{
				if ( me.getButton () == MouseEvent.BUTTON1 )
				{
					photoTable.selectAll ();
				}
			}
		});

		ringTable.getTableHeader ().addMouseListener (new MouseAdapter ()
		{
			@Override
			public void mouseClicked (MouseEvent me)
			{
				if ( me.getButton () == MouseEvent.BUTTON1 )
				{
					ringTable.selectAll ();
				}
			}
		});

		addrTable.getTableHeader ().addMouseListener (new MouseAdapter ()
		{
			@Override
			public void mouseClicked (MouseEvent me)
			{
				if ( me.getButton () == MouseEvent.BUTTON1 )
				{
					addrTable.selectAll ();
				}
			}
		});

		todoTable.getTableHeader ().addMouseListener (new MouseAdapter ()
		{
			@Override
			public void mouseClicked (MouseEvent me)
			{
				if ( me.getButton () == MouseEvent.BUTTON1 )
				{
					todoTable.selectAll ();
				}
			}
		});

		eventTable.getTableHeader ().addMouseListener (new MouseAdapter ()
		{
			@Override
			public void mouseClicked (MouseEvent me)
			{
				if ( me.getButton () == MouseEvent.BUTTON1 )
				{
					eventTable.selectAll ();
				}
			}
		});

		aboutBut.addKeyListener (kl);
		dataBitsCombo.addKeyListener (kl);
		flowSoft.addKeyListener (kl);
		flowHard.addKeyListener (kl);
		parityCombo.addKeyListener (kl);
		portCombo.addKeyListener (kl);
		rawBut.addKeyListener (kl);
		scanButton.addKeyListener (kl);
		speedCombo.addKeyListener (kl);
		stopBitsCombo.addKeyListener (kl);
		getCapBut.addKeyListener (kl);
		loadConfBut.addKeyListener (kl);
		saveConfBut.addKeyListener (kl);

		tabPane.addKeyListener (kl);
		addrBookPane.addKeyListener (kl);
		addrTable.addKeyListener (kl);
		eventPane.addKeyListener (kl);
		eventTable.addKeyListener (kl);
		photoPane.addKeyListener (kl);
		photoTable.addKeyListener (kl);
		ringPane.addKeyListener (kl);
		ringTable.addKeyListener (kl);
		todoPane.addKeyListener (kl);
		todoTable.addKeyListener (kl);
		animPane.addKeyListener (kl);
		animTable.addKeyListener (kl);
		javaPane.addKeyListener (kl);
		javaTable.addKeyListener (kl);

		jScrollPane1.addKeyListener (kl);
		jScrollPane2.addKeyListener (kl);
		jScrollPane3.addKeyListener (kl);
		jScrollPane4.addKeyListener (kl);
		jScrollPane5.addKeyListener (kl);
		jScrollPane6.addKeyListener (kl);
		jScrollPane7.addKeyListener (kl);
		jScrollPane8.addKeyListener (kl);
		jScrollPane9.addKeyListener (kl);
		jScrollPane10.addKeyListener (kl);
		jScrollPane11.addKeyListener (kl);
		jScrollPane12.addKeyListener (kl);
		jScrollPane13.addKeyListener (kl);
		jScrollPane14.addKeyListener (kl);

		deleteAddrBut.addKeyListener (kl);
		deleteEventBut.addKeyListener (kl);
		deletePhotoBut.addKeyListener (kl);
		deleteRingBut.addKeyListener (kl);
		deleteTodoBut.addKeyListener (kl);
		deleteAnimBut.addKeyListener (kl);
		deleteJavaBut.addKeyListener (kl);

		downloadAddrBut.addKeyListener (kl);
		downloadEventBut.addKeyListener (kl);
		downloadPhotoBut.addKeyListener (kl);
		downloadRingBut.addKeyListener (kl);
		downloadTodoBut.addKeyListener (kl);
		downloadAnimBut.addKeyListener (kl);
		downloadJavaBut.addKeyListener (kl);

		getAddrListBut.addKeyListener (kl);
		getEventListBut.addKeyListener (kl);
		getPhotoListBut.addKeyListener (kl);
		getRingListBut.addKeyListener (kl);
		getTodoListBut.addKeyListener (kl);
		getAnimListBut.addKeyListener (kl);
		getJavaListBut.addKeyListener (kl);

		uploadAddrBut.addKeyListener (kl);
		uploadEventBut.addKeyListener (kl);
		uploadPhotoBut.addKeyListener (kl);
		uploadRingBut.addKeyListener (kl);
		uploadTodoBut.addKeyListener (kl);
		uploadAnimBut.addKeyListener (kl);
		uploadJavaBut.addKeyListener (kl);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        portCombo = new javax.swing.JComboBox();
        speedCombo = new javax.swing.JComboBox();
        dataBitsCombo = new javax.swing.JComboBox();
        parityCombo = new javax.swing.JComboBox();
        stopBitsCombo = new javax.swing.JComboBox();
        scanButton = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        photoPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        photoTable = new javax.swing.JTable();
        getPhotoListBut = new javax.swing.JButton();
        downloadPhotoBut = new javax.swing.JButton();
        uploadPhotoBut = new javax.swing.JButton();
        deletePhotoBut = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        ringPane = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ringTable = new javax.swing.JTable();
        getRingListBut = new javax.swing.JButton();
        downloadRingBut = new javax.swing.JButton();
        uploadRingBut = new javax.swing.JButton();
        deleteRingBut = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        addrBookPane = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        addrTable = new javax.swing.JTable();
        getAddrListBut = new javax.swing.JButton();
        downloadAddrBut = new javax.swing.JButton();
        uploadAddrBut = new javax.swing.JButton();
        deleteAddrBut = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        todoPane = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        todoTable = new javax.swing.JTable();
        getTodoListBut = new javax.swing.JButton();
        downloadTodoBut = new javax.swing.JButton();
        uploadTodoBut = new javax.swing.JButton();
        deleteTodoBut = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        eventPane = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable();
        getEventListBut = new javax.swing.JButton();
        downloadEventBut = new javax.swing.JButton();
        uploadEventBut = new javax.swing.JButton();
        deleteEventBut = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        animPane = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        animTable = new javax.swing.JTable();
        getAnimListBut = new javax.swing.JButton();
        downloadAnimBut = new javax.swing.JButton();
        uploadAnimBut = new javax.swing.JButton();
        deleteAnimBut = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        javaPane = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        javaTable = new javax.swing.JTable();
        getJavaListBut = new javax.swing.JButton();
        downloadJavaBut = new javax.swing.JButton();
        uploadJavaBut = new javax.swing.JButton();
        deleteJavaBut = new javax.swing.JButton();
        portLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        databitsLabel = new javax.swing.JLabel();
        parityLabel = new javax.swing.JLabel();
        stopbitsLabel = new javax.swing.JLabel();
        flowLabel = new javax.swing.JLabel();
        aboutBut = new javax.swing.JButton();
        rawBut = new javax.swing.JButton();
        bpsLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        firmwareLabel = new javax.swing.JLabel();
        firmware = new javax.swing.JLabel();
        progressLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        phoneTypeLabel = new javax.swing.JLabel();
        phone = new javax.swing.JLabel();
        exitBut = new javax.swing.JButton();
        getCapBut = new javax.swing.JButton();
        loadConfBut = new javax.swing.JButton();
        saveConfBut = new javax.swing.JButton();
        flowSoft = new javax.swing.JCheckBox();
        flowHard = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Your Music and Graphics"); // NOI18N
        setIconImage((new javax.swing.ImageIcon (getClass ().getResource ("/BogDroSoft/jymag/rsrc/jymag.png"))).getImage ());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        portCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                portComboItemStateChanged(evt);
            }
        });

        speedCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000" }));
        speedCombo.setSelectedIndex(7);

        dataBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "7", "6", "5" }));

        parityCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Even", "Odd", "Space", "Mark" }));

        stopBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

        scanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/SCAN5.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
        scanButton.setText(bundle.getString("Scan_ports")); // NOI18N
        scanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanButtonActionPerformed(evt);
            }
        });

        tabPane.setPreferredSize(new java.awt.Dimension(800, 600));

        photoPane.setBackground(new java.awt.Color(184, 242, 255));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        photoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 66L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(photoTable);

        getPhotoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getPhotoListBut.setText(bundle.getString("Get_list")); // NOI18N
        getPhotoListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getPhotoListButActionPerformed(evt);
            }
        });

        downloadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadPhotoBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadPhotoBut.setText(bundle.getString("Upload")); // NOI18N
        uploadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadPhotoButActionPerformed(evt);
            }
        });

        deletePhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deletePhotoBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deletePhotoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout photoPaneLayout = new javax.swing.GroupLayout(photoPane);
        photoPane.setLayout(photoPaneLayout);
        photoPaneLayout.setHorizontalGroup(
            photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(photoPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(photoPaneLayout.createSequentialGroup()
                        .addComponent(getPhotoListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadPhotoBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadPhotoBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deletePhotoBut)))
                .addContainerGap())
        );
        photoPaneLayout.setVerticalGroup(
            photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, photoPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getPhotoListBut)
                    .addComponent(downloadPhotoBut)
                    .addComponent(uploadPhotoBut)
                    .addComponent(deletePhotoBut))
                .addContainerGap())
        );

        getPhotoListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_pictures")); // NOI18N
        downloadPhotoBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_pictures")); // NOI18N
        uploadPhotoBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_pictures")); // NOI18N
        deletePhotoBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_pictures")); // NOI18N

        jScrollPane7.setViewportView(photoPane);

        tabPane.addTab(bundle.getString("Photos"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/pict-1.png")), jScrollPane7); // NOI18N

        ringPane.setBackground(new java.awt.Color(179, 255, 179));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), ringPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), ringPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), ringPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 0));

        ringTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 67L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(ringTable);

        getRingListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getRingListBut.setText(bundle.getString("Get_list")); // NOI18N
        getRingListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getRingListButActionPerformed(evt);
            }
        });

        downloadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadRingBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadRingBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadRingBut.setText(bundle.getString("Upload")); // NOI18N
        uploadRingBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadRingButActionPerformed(evt);
            }
        });

        deleteRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteRingBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteRingBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ringPaneLayout = new javax.swing.GroupLayout(ringPane);
        ringPane.setLayout(ringPaneLayout);
        ringPaneLayout.setHorizontalGroup(
            ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ringPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(ringPaneLayout.createSequentialGroup()
                        .addComponent(getRingListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadRingBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadRingBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteRingBut)))
                .addContainerGap())
        );
        ringPaneLayout.setVerticalGroup(
            ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ringPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getRingListBut)
                    .addComponent(downloadRingBut)
                    .addComponent(uploadRingBut)
                    .addComponent(deleteRingBut))
                .addContainerGap())
        );

        getRingListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_ringtones")); // NOI18N
        downloadRingBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_ringtones")); // NOI18N
        uploadRingBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_ringtones")); // NOI18N
        deleteRingBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_ringtones")); // NOI18N

        jScrollPane8.setViewportView(ringPane);

        tabPane.addTab(bundle.getString("Ringtones"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/ring.png")), jScrollPane8); // NOI18N

        addrBookPane.setBackground(new java.awt.Color(255, 255, 193));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), addrBookPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), addrBookPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), addrBookPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 0));

        addrTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 68L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(addrTable);

        getAddrListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getAddrListBut.setText(bundle.getString("Get_list")); // NOI18N
        getAddrListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getAddrListButActionPerformed(evt);
            }
        });

        downloadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadAddrBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadAddrBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadAddrBut.setText(bundle.getString("Upload")); // NOI18N
        uploadAddrBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadAddrButActionPerformed(evt);
            }
        });

        deleteAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteAddrBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteAddrBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addrBookPaneLayout = new javax.swing.GroupLayout(addrBookPane);
        addrBookPane.setLayout(addrBookPaneLayout);
        addrBookPaneLayout.setHorizontalGroup(
            addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addrBookPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(addrBookPaneLayout.createSequentialGroup()
                        .addComponent(getAddrListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadAddrBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadAddrBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteAddrBut)))
                .addContainerGap())
        );
        addrBookPaneLayout.setVerticalGroup(
            addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addrBookPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getAddrListBut)
                    .addComponent(downloadAddrBut)
                    .addComponent(uploadAddrBut)
                    .addComponent(deleteAddrBut))
                .addContainerGap())
        );

        getAddrListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_addressbook_entries")); // NOI18N
        downloadAddrBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_addressbook_entries")); // NOI18N
        uploadAddrBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_addressbook_entries")); // NOI18N
        deleteAddrBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_addressbook_entries")); // NOI18N

        jScrollPane9.setViewportView(addrBookPane);

        tabPane.addTab(bundle.getString("Addressbook"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/abook.png")), jScrollPane9); // NOI18N

        todoPane.setBackground(new java.awt.Color(127, 196, 127));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), todoPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), todoPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), todoPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(0, 0));

        todoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 69L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(todoTable);

        getTodoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getTodoListBut.setText(bundle.getString("Get_list")); // NOI18N
        getTodoListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getTodoListButActionPerformed(evt);
            }
        });

        downloadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadTodoBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadTodoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadTodoBut.setText(bundle.getString("Upload")); // NOI18N
        uploadTodoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadTodoButActionPerformed(evt);
            }
        });

        deleteTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteTodoBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteTodoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout todoPaneLayout = new javax.swing.GroupLayout(todoPane);
        todoPane.setLayout(todoPaneLayout);
        todoPaneLayout.setHorizontalGroup(
            todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(todoPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(todoPaneLayout.createSequentialGroup()
                        .addComponent(getTodoListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadTodoBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadTodoBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteTodoBut)))
                .addContainerGap())
        );
        todoPaneLayout.setVerticalGroup(
            todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, todoPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getTodoListBut)
                    .addComponent(downloadTodoBut)
                    .addComponent(uploadTodoBut)
                    .addComponent(deleteTodoBut))
                .addContainerGap())
        );

        getTodoListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_to-do_tasks")); // NOI18N
        downloadTodoBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_to-do_tasks")); // NOI18N
        uploadTodoBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_to-do_tasks")); // NOI18N
        deleteTodoBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_to-do_tasks")); // NOI18N

        jScrollPane10.setViewportView(todoPane);

        tabPane.addTab(bundle.getString("ToDoTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/todo.png")), jScrollPane10); // NOI18N

        eventPane.setBackground(new java.awt.Color(219, 218, 156));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), eventPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), eventPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), eventPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane5.setPreferredSize(new java.awt.Dimension(0, 0));

        eventTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 70L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(eventTable);

        getEventListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getEventListBut.setText(bundle.getString("Get_list")); // NOI18N
        getEventListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEventListButActionPerformed(evt);
            }
        });

        downloadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadEventBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadEventBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadEventBut.setText(bundle.getString("Upload")); // NOI18N
        uploadEventBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadEventButActionPerformed(evt);
            }
        });

        deleteEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteEventBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteEventBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eventPaneLayout = new javax.swing.GroupLayout(eventPane);
        eventPane.setLayout(eventPaneLayout);
        eventPaneLayout.setHorizontalGroup(
            eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(eventPaneLayout.createSequentialGroup()
                        .addComponent(getEventListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadEventBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadEventBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteEventBut)))
                .addContainerGap())
        );
        eventPaneLayout.setVerticalGroup(
            eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eventPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getEventListBut)
                    .addComponent(downloadEventBut)
                    .addComponent(uploadEventBut)
                    .addComponent(deleteEventBut))
                .addContainerGap())
        );

        getEventListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_events")); // NOI18N
        downloadEventBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_events")); // NOI18N
        uploadEventBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_events")); // NOI18N
        deleteEventBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_events")); // NOI18N

        jScrollPane11.setViewportView(eventPane);

        tabPane.addTab(bundle.getString("EventsTasksTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/event.png")), jScrollPane11); // NOI18N

        animPane.setBackground(new java.awt.Color(171, 171, 255));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), animPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), animPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), animPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane6.setPreferredSize(new java.awt.Dimension(0, 0));

        animTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 73L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(animTable);

        getAnimListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getAnimListBut.setText(bundle.getString("Get_list")); // NOI18N
        getAnimListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getAnimListButActionPerformed(evt);
            }
        });

        downloadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadAnimBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadAnimBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadAnimBut.setText(bundle.getString("Upload")); // NOI18N
        uploadAnimBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadAnimButActionPerformed(evt);
            }
        });

        deleteAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteAnimBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteAnimBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout animPaneLayout = new javax.swing.GroupLayout(animPane);
        animPane.setLayout(animPaneLayout);
        animPaneLayout.setHorizontalGroup(
            animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(animPaneLayout.createSequentialGroup()
                        .addComponent(getAnimListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadAnimBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadAnimBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteAnimBut)))
                .addContainerGap())
        );
        animPaneLayout.setVerticalGroup(
            animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, animPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(animPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getAnimListBut)
                    .addComponent(downloadAnimBut)
                    .addComponent(uploadAnimBut)
                    .addComponent(deleteAnimBut))
                .addContainerGap())
        );

        getAnimListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_videos")); // NOI18N
        downloadAnimBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_videos")); // NOI18N
        uploadAnimBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_videos")); // NOI18N
        deleteAnimBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_videos")); // NOI18N

        jScrollPane12.setViewportView(animPane);

        tabPane.addTab(bundle.getString("AnimationsVideosTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/video.png")), jScrollPane12); // NOI18N

        javaPane.setBackground(new java.awt.Color(0, 102, 255));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), javaPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), javaPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), javaPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        jScrollPane14.setPreferredSize(new java.awt.Dimension(0, 0));

        javaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
            }
        ) {
            private static final long serialVersionUID = 74L;
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(javaTable);

        getJavaListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getJavaListBut.setText(bundle.getString("Get_list")); // NOI18N
        getJavaListBut.setEnabled(false);
        getJavaListBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getJavaListButActionPerformed(evt);
            }
        });

        downloadJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadJavaBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadJavaBut.setEnabled(false);
        downloadJavaBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButActionPerformed(evt);
            }
        });

        uploadJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadJavaBut.setText(bundle.getString("Upload")); // NOI18N
        uploadJavaBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadJavaButActionPerformed(evt);
            }
        });

        deleteJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteJavaBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteJavaBut.setEnabled(false);
        deleteJavaBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout javaPaneLayout = new javax.swing.GroupLayout(javaPane);
        javaPane.setLayout(javaPaneLayout);
        javaPaneLayout.setHorizontalGroup(
            javaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javaPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(javaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                    .addGroup(javaPaneLayout.createSequentialGroup()
                        .addComponent(getJavaListBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadJavaBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadJavaBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteJavaBut)))
                .addContainerGap())
        );
        javaPaneLayout.setVerticalGroup(
            javaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, javaPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(javaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getJavaListBut)
                    .addComponent(downloadJavaBut)
                    .addComponent(uploadJavaBut)
                    .addComponent(deleteJavaBut))
                .addContainerGap())
        );

        getJavaListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_Java_elements")); // NOI18N
        downloadJavaBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected__Java_elements")); // NOI18N
        uploadJavaBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_Java")); // NOI18N
        deleteJavaBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_Java_elements")); // NOI18N

        jScrollPane13.setViewportView(javaPane);

        tabPane.addTab(bundle.getString("JavaTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/java.png")), jScrollPane13); // NOI18N

        portLabel.setText(bundle.getString("Port:")); // NOI18N

        speedLabel.setText(bundle.getString("Speed:")); // NOI18N

        databitsLabel.setText(bundle.getString("Data_bits:")); // NOI18N

        parityLabel.setText(bundle.getString("Parity:")); // NOI18N

        stopbitsLabel.setText(bundle.getString("Stop_bits:")); // NOI18N

        flowLabel.setText(bundle.getString("Flow_control:")); // NOI18N

        aboutBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/QMARK8.png"))); // NOI18N
        aboutBut.setText(bundle.getString("About_JYMAG...")); // NOI18N
        aboutBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButActionPerformed(evt);
            }
        });

        rawBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/tools1.png"))); // NOI18N
        rawBut.setText(bundle.getString("Manual_commands")); // NOI18N
        rawBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rawButActionPerformed(evt);
            }
        });

        bpsLabel.setText("bps"); // NOI18N

        statusLabel.setText(bundle.getString("Program_status:")); // NOI18N

        status.setForeground(new java.awt.Color(0, 204, 0));
        status.setText("READY"); // NOI18N

        firmwareLabel.setText(bundle.getString("Firmware:")); // NOI18N

        firmware.setText("-"); // NOI18N

        progressLabel.setText(bundle.getString("Progress:")); // NOI18N

        phoneTypeLabel.setText(bundle.getString("Phone_type:")); // NOI18N

        phone.setText("-"); // NOI18N

        exitBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/exit.png"))); // NOI18N
        exitBut.setText(bundle.getString("Exit")); // NOI18N
        exitBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButActionPerformed(evt);
            }
        });

        getCapBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/capab.png"))); // NOI18N
        getCapBut.setText(bundle.getString("Get_capabilities")); // NOI18N
        getCapBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCapButActionPerformed(evt);
            }
        });

        loadConfBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/loadconf.png"))); // NOI18N
        loadConfBut.setText(bundle.getString("Load_configuration")); // NOI18N
        loadConfBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadConfButActionPerformed(evt);
            }
        });

        saveConfBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/saveconf.png"))); // NOI18N
        saveConfBut.setText(bundle.getString("Save_configuration")); // NOI18N
        saveConfBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConfButActionPerformed(evt);
            }
        });

        flowSoft.setText(bundle.getString("Software_(XON/XOFF)")); // NOI18N

        flowHard.setText(bundle.getString("Hardware_(RTS/CTS)")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(speedLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(databitsLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bpsLabel))
                                    .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(parityLabel)
                            .addComponent(stopbitsLabel)
                            .addComponent(flowLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(flowSoft))
                        .addGap(2, 2, 2)
                        .addComponent(flowHard)
                        .addGap(110, 110, 110))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(loadConfBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveConfBut))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(196, 196, 196)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(phoneTypeLabel)
                                    .addComponent(statusLabel)
                                    .addComponent(progressLabel)
                                    .addComponent(firmwareLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(phone)
                                    .addComponent(firmware)
                                    .addComponent(status)
                                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))))
                        .addGap(284, 284, 284))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
                        .addComponent(scanButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
                        .addComponent(aboutBut))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
                        .addComponent(rawBut))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
                        .addComponent(getCapBut))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 718, Short.MAX_VALUE)
                        .addComponent(exitBut))
                    .addComponent(tabPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {aboutBut, exitBut, getCapBut, rawBut, scanButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(portLabel)
                                    .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(speedLabel)
                                    .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bpsLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(databitsLabel)
                                    .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(flowHard)
                                    .addComponent(flowLabel)
                                    .addComponent(flowSoft)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(parityLabel)
                                    .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(stopbitsLabel)
                                    .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(phoneTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(firmwareLabel)
                                    .addComponent(firmware))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(statusLabel)
                                    .addComponent(status)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(loadConfBut)
                                .addComponent(saveConfBut)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(progressLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scanButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aboutBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rawBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getCapBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitBut)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addContainerGap())
        );

        portCombo.getAccessibleContext().setAccessibleName(bundle.getString("port_list")); // NOI18N
        speedCombo.getAccessibleContext().setAccessibleName(bundle.getString("port_speed")); // NOI18N
        dataBitsCombo.getAccessibleContext().setAccessibleName(bundle.getString("data_bits")); // NOI18N
        parityCombo.getAccessibleContext().setAccessibleName(bundle.getString("parity_type")); // NOI18N
        stopBitsCombo.getAccessibleContext().setAccessibleName(bundle.getString("stop_bits")); // NOI18N
        tabPane.getAccessibleContext().setAccessibleName(bundle.getString("tabPane")); // NOI18N
        status.getAccessibleContext().setAccessibleName(bundle.getString("program_status")); // NOI18N
        firmware.getAccessibleContext().setAccessibleName(bundle.getString("phone_firmware")); // NOI18N
        progressBar.getAccessibleContext().setAccessibleName(bundle.getString("operation_progress")); // NOI18N
        phone.getAccessibleContext().setAccessibleName(bundle.getString("phone_type")); // NOI18N

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void scanButtonActionPerformed (java.awt.event.ActionEvent evt)	{//GEN-FIRST:event_scanButtonActionPerformed

		final int cSpeed = Integer.valueOf (speedCombo.getSelectedItem ().toString ());
		final int cDataBits = Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ());
		final float cStopBits = Float.valueOf (stopBitsCombo.getSelectedItem ().toString ());
		final int cParity = parityCombo.getSelectedIndex ();
		final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);

		// always create new:
		firmwares = new Hashtable<String, String> (1);
		phoneTypes = new Hashtable<String, String> (1);

		updateStatusLabel (STATUS.SENDING);
		progressBar.setValue (0);
		progressBar.setMinimum (0);
		int max = 0;
		Enumeration temp = CommPortIdentifier.getPortIdentifiers ();
		if ( temp != null )
		{
			while (temp.hasMoreElements ())
			{
				CommPortIdentifier id = (CommPortIdentifier) temp.nextElement ();
				if ( id == null ) continue;
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
				{
					max++;
				}
			}
		}
		progressBar.setMaximum (max);
		SwingWorker<Vector<CommPortIdentifier>, Void> sw =
			new SwingWorker<Vector<CommPortIdentifier>, Void> ()
		{
			@Override
			protected Vector<CommPortIdentifier> doInBackground ()
			{
				Vector<CommPortIdentifier> active = new Vector<CommPortIdentifier> (1);

                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();
				while (portList.hasMoreElements ())
				{
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();
					if ( id == null ) continue;

					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
					{
						// scan ports for "AT"-"OK"
						try
						{
							synchronized (sync)
							{
								DataTransporter dt = new DataTransporter (id);
								dt.open (cSpeed, cDataBits, cStopBits,
									cParity, cFlow);
								int testRes = dt.test ();
								if ( testRes == 0 )
								{
									active.add (id);
									// get firmware version
									String rcvd = "";	// NOI18N
									int trials = 0;
									do
									{
										dt.send ( ("AT+KPSV\r")	// NOI18N
											.getBytes ());
										byte[] recvdB = dt.recv (null);	// NOI18N
										rcvd = new String (recvdB);

										if ( rcvd.trim ().equals ("") )	// NOI18N
										{
											dt.reopen ();
											trials++;
										}
									} while (rcvd.trim ().equals ("")	// NOI18N
										&& trials <= DataTransporter.MAX_TRIALS);
									if ( trials <= DataTransporter.MAX_TRIALS )
									{
										// sample: "+KPSV: 2.04"
										try
										{
											// have to split to make it work
											String[] lines = rcvd.split ("\r");	// NOI18N
											if ( lines != null )
											{
												for ( int i=0; i < lines.length; i++ )
												{
													if ( lines[i] == null ) continue;
													Matcher m = verPattern
														.matcher (lines[i]);
													if ( m.matches () )
													{
														firmwares.put (
															id.getName (),
															m.group (1));
														break;
													}
												}
											}
										}
										catch (Exception ex)
										{
											Utils.handleException (ex,
												"pattern/matcher");	// NOI18N
										}
									}
									// get phone type
									rcvd = "";	// NOI18N
									trials = 0;
									do
									{
										dt.send ( ("ATIMEI\r")	// NOI18N
											.getBytes ());
										byte[] recvdB = dt.recv (null);	// NOI18N
										rcvd = new String (recvdB);

										if ( rcvd.trim ().equals ("") )	// NOI18N
										{
											dt.reopen ();
											trials++;
										}
									} while ( //(
											rcvd.trim ().equals ("")	// NOI18N
											//|| rcvd.contains ("ATIMEI"))
										&& trials <= DataTransporter.MAX_TRIALS);
									if ( trials <= DataTransporter.MAX_TRIALS )
									{
										// sample: "myX5-2 GPRS"
										try
										{
											// have to split to make it work
											String[] lines = rcvd.split ("\r");	// NOI18N
											if ( lines != null )
											{
												for ( int i=0; i < lines.length; i++ )
												{
													if ( lines[i] == null ) continue;
													if ( lines[i].trim ().length () > 0
															&& ! lines[i].contains ("ATIMEI") )	// NOI18N
													{
														phoneTypes.put (
															id.getName (),
															lines[i].trim ());
														break;
													}
												}
											}
										}
										catch (Exception ex)
										{
											Utils.handleException (ex,
												"phone type");	// NOI18N
										}
									}
								}
								dt.close ();
							}
						}
						catch (Exception ex)
						{
							Utils.handleException (ex,
								"scanning.SW:" + id.getName ());	// NOI18N
						}
						Utils.changeGUI (new Runnable ()
						{
							@Override
							public void run ()
							{
								progressBar.setValue (progressBar.getValue ()+1);
								if ( progressBar.getValue () == progressBar.getMaximum () )
								{
									progressBar.setValue (0);
								}
							}
						});
					}
				}
				return active;
			}

			@Override
			protected void done ()
			{
				try
				{
					updateStatusLabel (STATUS.READY);
					Vector<CommPortIdentifier> active = get ();
					if ( active == null )
					{
						JOptionPane.showMessageDialog (mw,
							noAnsString, errString,
							JOptionPane.ERROR_MESSAGE);
						firmware.setText (pressScanMsg);
						phone.setText (pressScanMsg);
					}
					else if (active.size () == 0)
					{
						JOptionPane.showMessageDialog (mw,
							noAnsString, errString,
							JOptionPane.ERROR_MESSAGE);
						firmware.setText (pressScanMsg);
						phone.setText (pressScanMsg);
					}
					else if (active.size () == 1)
					{
						// NOT setSelectedIndex (0)!
						portCombo.setSelectedItem (active.get (0).getName ());
						if ( firmwares != null )
						{
							if ( firmwares.containsKey (active.get (0).getName ()) )
							{
								firmware.setText (firmwares.get (active.get (0).getName ()));
							}
							else
							{
								firmware.setText (pressScanMsg);
							}
						}
						else
						{
							firmware.setText (pressScanMsg);
						}
						if ( phoneTypes != null )
						{
							if ( phoneTypes.containsKey (active.get (0).getName ()) )
							{
								phone.setText (phoneTypes.get (active.get (0).getName ()));
							}
							else
							{
								phone.setText (pressScanMsg);
							}
						}
						else
						{
							phone.setText (pressScanMsg);
						}
					}
					else
					{
						Vector<String> names = new
							Vector<String> (active.size ());
						for ( int i=0; i < active.size (); i++ )
						{
							names.add ( active.get (i).getName () );
						}
						// let the user choose
						int res = JOptionPane.showOptionDialog (mw,
							multiAnsString,
							whichString,
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							names.toArray (),
							active.get (0).getName ());
						if ( res != JOptionPane.CLOSED_OPTION )
						{
							portCombo.setSelectedIndex (res);
							if ( firmwares != null )
							{
								if ( firmwares.containsKey (active.get (res).getName ()) )
								{
									firmware.setText (firmwares.get (active.get (res).getName ()));
								}
								else
								{
									firmware.setText (pressScanMsg);
								}
							}
							else
							{
								firmware.setText (pressScanMsg);
							}
							if ( phoneTypes != null )
							{
								if ( phoneTypes.containsKey (active.get (res).getName ()) )
								{
									phone.setText (phoneTypes.get (active.get (res).getName ()));
								}
								else
								{
									phone.setText (pressScanMsg);
								}
							}
							else
							{
								phone.setText (pressScanMsg);
							}
						}
						else	// the user closed the window - choose the first one
						{
							// NOT setSelectedIndex (0)!
							portCombo.setSelectedItem
								(active.get (0).getName ());
							if ( firmwares != null )
							{
								if ( firmwares.containsKey (active.get (0).getName ()) )
								{
									firmware.setText (firmwares.get (active.get (0).getName ()));
								}
								else
								{
									firmware.setText (pressScanMsg);
								}
							}
							else
							{
								firmware.setText (pressScanMsg);
							}
							if ( phoneTypes != null )
							{
								if ( phoneTypes.containsKey (active.get (0).getName ()) )
								{
									phone.setText (phoneTypes.get (active.get (0).getName ()));
								}
								else
								{
									phone.setText (pressScanMsg);
								}
							}
							else
							{
								phone.setText (pressScanMsg);
							}
						}
					}
				}
				catch (Exception ex)
				{
					updateStatusLabel (STATUS.READY);
					Utils.handleException (ex, "scanning.SW.done");	// NOI18N
				}
			}
		};
		sw.execute ();
	}//GEN-LAST:event_scanButtonActionPerformed

	private void getPhotoListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPhotoListButActionPerformed

		if ( currentPhotoElements == null ) currentPhotoElements =
			new Vector<PhoneElement> (1);

		putListInTable ("PICTURES",	// NOI18N
			(DefaultTableModel) photoTable.getModel (),
			currentPhotoElements);
	}//GEN-LAST:event_getPhotoListButActionPerformed

	private void getRingListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRingListButActionPerformed

		if ( currentRingElements == null ) currentRingElements =
			new Vector<PhoneElement> (1);

		putListInTable ("RINGTONES",	// NOI18N
			(DefaultTableModel) ringTable.getModel (),
			currentRingElements);
	}//GEN-LAST:event_getRingListButActionPerformed

	private void getAddrListButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getAddrListButActionPerformed

		if ( currentAddrElements == null ) currentAddrElements =
			new Vector<PhoneElement> (1);

		putListInTable ("VCARDS",	// NOI18N
			(DefaultTableModel) addrTable.getModel (),
			currentAddrElements);
	}//GEN-LAST:event_getAddrListButActionPerformed

	private void getTodoListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getTodoListButActionPerformed

		if ( currentTodoElements == null ) currentTodoElements =
			new Vector<PhoneElement> (1);

		putListInTable ("VTODO",	// NOI18N
			(DefaultTableModel) todoTable.getModel (),
			currentTodoElements);
	}//GEN-LAST:event_getTodoListButActionPerformed

	private void getEventListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getEventListButActionPerformed

		if ( currentEventElements == null ) currentEventElements =
			new Vector<PhoneElement> (1);

		putListInTable ("VEVENT",	// NOI18N
			(DefaultTableModel) eventTable.getModel (),
			currentEventElements);
	}//GEN-LAST:event_getEventListButActionPerformed

	private void getAnimListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getAnimListButActionPerformed
		if ( currentAnimElements == null ) currentAnimElements =
			new Vector<PhoneElement> (1);

		putListInTable ("ANIMATIONS",	// NOI18N
			(DefaultTableModel) animTable.getModel (),
			currentAnimElements);
	}//GEN-LAST:event_getAnimListButActionPerformed

	private void getJavaListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getJavaListButActionPerformed
		if ( currentJavaElements == null ) currentJavaElements =
			new Vector<PhoneElement> (1);

		/* TODO
		putListInTable ("JAR",	// NOI18N
			(DefaultTableModel) javaTable.getModel (),
			currentJavaElements);*/
	}//GEN-LAST:event_getJavaListButActionPerformed


	private void downloadButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButActionPerformed

		int[] selectedRows = null;
		Vector<PhoneElement> temp = null;
		if ( tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = photoTable.getSelectedRows ();
			temp = currentPhotoElements;
		}
		else if ( tabPane.getSelectedIndex () == 1 )
		{
			selectedRows = ringTable.getSelectedRows ();
			temp = currentRingElements;
		}
		else if ( tabPane.getSelectedIndex () == 2 )
		{
			selectedRows = addrTable.getSelectedRows ();
			temp = currentAddrElements;
		}
		else if ( tabPane.getSelectedIndex () == 3 )
		{
			selectedRows = todoTable.getSelectedRows ();
			temp = currentTodoElements;
		}
		else if ( tabPane.getSelectedIndex () == 4 )
		{
			selectedRows = eventTable.getSelectedRows ();
			temp = currentEventElements;
		}
		else if ( tabPane.getSelectedIndex () == 5 )
		{
			selectedRows = animTable.getSelectedRows ();
			temp = currentAnimElements;
		}
		else if ( tabPane.getSelectedIndex () == 6 )
		{
			selectedRows = javaTable.getSelectedRows ();
			temp = currentJavaElements;
		}
		final Vector<PhoneElement> currentElements = temp;
		if ( selectedRows == null || currentElements == null ) return;
		if ( selectedRows.length == 0 ) return;

		if ( downloadFC == null ) downloadFC = new JFileChooser ();
		downloadFC.setMultiSelectionEnabled (false);
		downloadFC.setDialogType (JFileChooser.SAVE_DIALOG);
		downloadFC.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		// destDirName is the static field
		if ( destDirName != null )
		{
			if ( destDirName.length () > 0 )
			{
				File d = new File (destDirName);
				downloadFC.setSelectedFile (d);
				downloadFC.setCurrentDirectory (d);
			}
		}
		/*
		if ( lastSelDir != null )
		{
			File d = new File (lastSelDir);
			downloadFC.setSelectedFile (d);
			downloadFC.setCurrentDirectory (d);
		}*/
		int dialogRes = downloadFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File destDir = downloadFC.getSelectedFile ();
				lastSelDir = destDir.getAbsolutePath ();
				final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(portCombo.getSelectedItem ().toString ());
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				final int cSpeed = Integer.valueOf (speedCombo.getSelectedItem ().toString ());
				final int cDataBits = Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ());
				final float cStopBits = Float.valueOf (stopBitsCombo.getSelectedItem ().toString ());
				final int cParity = parityCombo.getSelectedIndex ();
				final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);
				final AtomicInteger threads = new AtomicInteger (0);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					final File received = new File (
						destDir.getAbsolutePath () + File.separator
						+ currentElements.get (toGet).getFilename ()
						+ "." 	// NOI18N
						+ currentElements.get (toGet).getExt ());
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (this,
							currentElements.get (toGet).getFilename ()
							+ "." 	// NOI18N
							+ currentElements.get (toGet).getExt () +
							exOverString,
							overwriteStr,
							JOptionPane.YES_NO_CANCEL_OPTION);
						if ( res != JOptionPane.YES_OPTION ) continue;
					}
					if ( received.exists () && ! received.canWrite () )
					{
						JOptionPane.showMessageDialog (this,
							fileNotWriteMsg + ": " + received.getName (),	// NOI18N
							errString, JOptionPane.ERROR_MESSAGE);
						continue;
					}
					updateStatusLabel (STATUS.RECEIVING);
					SwingWorker<Integer, Void> sw =
						new SwingWorker<Integer, Void> ()
					{
						@Override
						protected Integer doInBackground ()
						{
							threads.incrementAndGet ();
							synchronized (sync)
							{
								try
								{
									final DataTransporter dt = new DataTransporter (id);
									dt.open (cSpeed, cDataBits, cStopBits,
										cParity, cFlow);
									int ret = dt.getFile (received,
										currentElements.get (toGet));
									dt.close ();
									return ret;
								}
								catch (Exception e)
								{
									Utils.handleException (e,
										"downloadButActionPerformed.SW.doInBackground");	// NOI18N
								}
								return -1;
							}
						}

						@Override
						protected void done ()
						{
							int th = threads.decrementAndGet ();
							try
							{
								progressBar.setValue (progressBar.getValue () + 1);
								if ( th == 0 )
								{
									updateStatusLabel (STATUS.READY);
								}
								int res = get ().intValue ();
								if ( res != 0 )
								{
									String msg = String.valueOf (res);
									if ( res == -1 ) msg = downloadMsg1;
									else if ( res == -2 ) msg = downloadMsg2;
									else if ( res == -3 ) msg = downloadMsg3;

									JOptionPane.showMessageDialog
										(mw,
										errString + ": " + msg,	// NOI18N
										errString,
										JOptionPane.ERROR_MESSAGE );
								}
								if ( th == 0 )
								{
									progressBar.setValue (0);
								}
							}
							catch (Exception ex)
							{
								if ( th == 0 )
								{
									updateStatusLabel (STATUS.READY);
									progressBar.setValue (0);
								}
								Utils.handleException (ex,
									"download.SW.done:"			// NOI18N
									+ currentElements.get (toGet).getFilename ()
									+ "." + currentElements.get (toGet).getExt ());	// NOI18N
							}
						}
					};
					sw.execute ();
				} // for
			}
			catch (Exception ex)
			{
				updateStatusLabel (STATUS.READY);
				Utils.handleException (ex, "download");	// NOI18N
			}
		}

	}//GEN-LAST:event_downloadButActionPerformed

	private void aboutButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButActionPerformed

		new AboutBox (this, true).setVisible (true);
	}//GEN-LAST:event_aboutButActionPerformed

	private void uploadPhotoButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadPhotoButActionPerformed

		if ( uploadPictFC == null ) uploadPictFC = new JFileChooser ();
		uploadPictFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.photofileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = picsString;
				try
				{
					Enumeration<String> keys = Utils.photofileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "photofileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadPictFC.setAcceptAllFileFilterUsed (false);
		uploadPictFC.setMultiSelectionEnabled (false);
		uploadPictFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadPictFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadPictFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadPictFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadPhotoButActionPerformed

	private void uploadRingButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadRingButActionPerformed

		if ( uploadRingFC == null ) uploadRingFC = new JFileChooser ();
		uploadRingFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.ringfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = soundsString;
				try
				{
					Enumeration<String> keys = Utils.ringfileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ringfileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadRingFC.setAcceptAllFileFilterUsed (false);
		uploadRingFC.setMultiSelectionEnabled (false);
		uploadRingFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadRingFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadRingFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadRingFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadRingButActionPerformed

	private void uploadAddrButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAddrButActionPerformed

		if ( uploadAddrFC == null ) uploadAddrFC = new JFileChooser ();
		uploadAddrFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.addrfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = addrString;
				try
				{
					Enumeration<String> keys = Utils.addrfileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "addrfileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadAddrFC.setAcceptAllFileFilterUsed (false);
		uploadAddrFC.setMultiSelectionEnabled (false);
		uploadAddrFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadAddrFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadAddrFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadAddrFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAddrButActionPerformed

	private void uploadTodoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadTodoButActionPerformed

		if ( uploadTODOFC == null ) uploadTODOFC = new JFileChooser ();
		uploadTODOFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.todofileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = todoString;
				try
				{
					Enumeration<String> keys = Utils.todofileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "todofileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadTODOFC.setAcceptAllFileFilterUsed (false);
		uploadTODOFC.setMultiSelectionEnabled (false);
		uploadTODOFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadTODOFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadTODOFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadTODOFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadTodoButActionPerformed

	private void uploadEventButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadEventButActionPerformed

		if ( uploadEventFC == null ) uploadEventFC = new JFileChooser ();
		uploadEventFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.eventfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = eventString;
				try
				{
					Enumeration<String> keys = Utils.eventfileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "eventfileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadEventFC.setAcceptAllFileFilterUsed (false);
		uploadEventFC.setMultiSelectionEnabled (false);
		uploadEventFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadEventFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadEventFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadEventFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private void uploadAnimButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAnimButActionPerformed

		if ( uploadAnimFC == null ) uploadAnimFC = new JFileChooser ();
		uploadAnimFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.animfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = animString;
				try
				{
					Enumeration<String> keys = Utils.animfileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "animfileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadAnimFC.setAcceptAllFileFilterUsed (false);
		uploadAnimFC.setMultiSelectionEnabled (false);
		uploadAnimFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadAnimFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadAnimFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadAnimFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAnimButActionPerformed

	private void uploadJavaButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadJavaButActionPerformed

		if ( uploadJavaFC == null ) uploadJavaFC = new JFileChooser ();
		uploadJavaFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( Utils.javafileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)	// NOI18N
						.toLowerCase ()))

						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				String desc = javaString;
				try
				{
					Enumeration<String> keys = Utils.javafileIDs.keys ();
					if ( keys != null )
					{
						desc += " (";	// NOI18N
						while ( keys.hasMoreElements () )
						{
							desc += "*." + keys.nextElement () + ", ";	// NOI18N
						}
						// remove the last comma and space
						desc = desc.substring (0, desc.length () - 2);
						desc += ")";	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "javafileIDs.keys");	// NOI18N
				}
				return desc;
			}
		} );

		uploadJavaFC.setAcceptAllFileFilterUsed (false);
		uploadJavaFC.setMultiSelectionEnabled (false);
		uploadJavaFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	uploadJavaFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadJavaFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadJavaFC.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadJavaButActionPerformed

	private void dynamicUpload (final File f)
	{
		try
		{
			final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			updateStatusLabel (STATUS.SENDING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);
			final int cSpeed = Integer.valueOf (speedCombo.getSelectedItem ().toString ());
			final int cDataBits = Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ());
			final float cStopBits = Float.valueOf (stopBitsCombo.getSelectedItem ().toString ());
			final int cParity = parityCombo.getSelectedIndex ();
			final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);
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
							dt.open (cSpeed, cDataBits, cStopBits,
								cParity, cFlow);
							int ret = dt.putFile (f, f.getName ().substring
								(0, f.getName ().indexOf ("."))	// NOI18N
								.replaceAll ("\\s", "_")	// NOI18N
								);
							dt.close ();
							return ret;
						}
						catch (Exception e)
						{
							Utils.handleException (e, "dynamicUpload.SW.doInBackground");	// NOI18N
						}
						return -1;
					}
				}

				@Override
				protected void done ()
				{
					try
					{
						progressBar.setValue (progressBar.getValue () + 1);
						updateStatusLabel (STATUS.READY);
						int put = 0;
						Integer res = get ();
						if ( res != null ) put = res.intValue ();
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

							JOptionPane.showMessageDialog (mw,
								errString + ": " + msg,	// NOI18N
								errString, JOptionPane.ERROR_MESSAGE );
						}
						progressBar.setValue (0);
					}
					catch (Exception ex)
					{
						updateStatusLabel (STATUS.READY);
						progressBar.setValue (0);
						Utils.handleException (ex,
							"dynUpload.SW.done:" + f.getName ());	// NOI18N
					}
				}
			};
			sw.execute ();
		}
		catch (Exception ex)
		{
			updateStatusLabel (STATUS.READY);
			Utils.handleException (ex, "dynUpload");	// NOI18N
		}
	}

	private void deleteButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButActionPerformed

		int[] selectedRows = null;
		Vector<PhoneElement> temp = null;
		if ( tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = photoTable.getSelectedRows ();
			temp = currentPhotoElements;
		}
		else if ( tabPane.getSelectedIndex () == 1 )
		{
			selectedRows = ringTable.getSelectedRows ();
			temp = currentRingElements;
		}
		else if ( tabPane.getSelectedIndex () == 2 )
		{
			selectedRows = addrTable.getSelectedRows ();
			temp = currentAddrElements;
		}
		else if ( tabPane.getSelectedIndex () == 3 )
		{
			selectedRows = todoTable.getSelectedRows ();
			temp = currentTodoElements;
		}
		else if ( tabPane.getSelectedIndex () == 4 )
		{
			selectedRows = eventTable.getSelectedRows ();
			temp = currentEventElements;
		}
		else if ( tabPane.getSelectedIndex () == 5 )
		{
			selectedRows = animTable.getSelectedRows ();
			temp = currentAnimElements;
		}
		else if ( tabPane.getSelectedIndex () == 6 )
		{
			selectedRows = javaTable.getSelectedRows ();
			temp = currentJavaElements;
		}
		if ( selectedRows == null || temp == null ) return;
		if ( selectedRows.length == 0 ) return;

		final Vector<PhoneElement> currentElements = temp;

		int dialogRes = JOptionPane.showConfirmDialog (this,
			deleteQuestion,
			questionString,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
			);

		if ( dialogRes == JOptionPane.YES_OPTION )
		{
			try
			{
				final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(portCombo.getSelectedItem ().toString ());
				final AtomicInteger threads = new AtomicInteger (0);
				updateStatusLabel (STATUS.SENDING);
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				final int cSpeed = Integer.valueOf (speedCombo.getSelectedItem ().toString ());
				final int cDataBits = Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ());
				final float cStopBits = Float.valueOf (stopBitsCombo.getSelectedItem ().toString ());
				final int cParity = parityCombo.getSelectedIndex ();
				final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					SwingWorker<Integer, Void> sw =
						new SwingWorker<Integer, Void> ()
					{
						@Override
						protected Integer doInBackground ()
						{
							threads.incrementAndGet ();
							synchronized (sync)
							{
								try
								{
									final DataTransporter dt = new DataTransporter (id);
									dt.open (cSpeed, cDataBits, cStopBits,
										cParity, cFlow);
									dt.deleteFile
										(currentElements.get (toGet));
									dt.close ();
								}
								catch (Exception e)
								{
									Utils.handleException (e,
										"deleteButActionPerformed.SW");	// NOI18N
								}
							}
							return 0;
						}

						@Override
						protected void done ()
						{
							progressBar.setValue (progressBar.getValue () + 1);
							if ( threads.decrementAndGet () == 0 )
							{
								updateStatusLabel (STATUS.READY);
								progressBar.setValue (0);
							}
						}
					};
					sw.execute ();
				}
			}
			catch (Exception ex)
			{
				updateStatusLabel (STATUS.READY);
				Utils.handleException (ex, "delete");	// NOI18N
			}
		}
	}//GEN-LAST:event_deleteButActionPerformed

	private void rawButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rawButActionPerformed

		try
		{
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			DataTransporter dt = new DataTransporter (id);
			dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
				Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
				Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			new RawCommunicator (dt, this, sync).setVisible (true);
			dt.close ();
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "MainWindow.RawCommunnicator.start");	// NOI18N
		}
	}//GEN-LAST:event_rawButActionPerformed

	private void getCapButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCapButActionPerformed

		try
		{
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			DataTransporter dt = new DataTransporter (id);
			dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
				Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
				Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			new CapabilityWindow (dt, this, sync).setVisible (true);
			dt.close ();
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "MainWindow.CapabilityWindow.start");	// NOI18N
		}
	}//GEN-LAST:event_getCapButActionPerformed

	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

		dispose ();
		closeProgram (0);
	}//GEN-LAST:event_formWindowClosing

	private void portComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_portComboItemStateChanged

		if ( evt.getStateChange () == ItemEvent.SELECTED )
		{
			Object item = evt.getItem ();
			if ( item != null )
			{
				if ( firmwares != null )
				{
					if ( firmwares.containsKey (item.toString ()) )
					{
						firmware.setText (firmwares.get (item.toString ()));
					}
					else
					{
						firmware.setText (pressScanMsg);
					}
				}
				else
				{
					firmware.setText (pressScanMsg);
				}
				if ( phoneTypes != null )
				{
					if ( phoneTypes.containsKey (item.toString ()) )
					{
						phone.setText (phoneTypes.get (item.toString ()));
					}
					else
					{
						phone.setText (pressScanMsg);
					}
				}
				else
				{
					phone.setText (pressScanMsg);
				}
			}
		}
	}//GEN-LAST:event_portComboItemStateChanged

	private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		dispose ();
		closeProgram (0);
	}//GEN-LAST:event_exitButActionPerformed

	private void loadConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadConfButActionPerformed

		if ( cfgFC == null ) cfgFC = new JFileChooser ();
		cfgFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().endsWith (".cfg"))	// NOI18N
				{
						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				return java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("JYMAG_configuration_files")
						+ " (*.cfg)";	// NOI18N
			}
		} );

		cfgFC.setAcceptAllFileFilterUsed (false);
		cfgFC.setMultiSelectionEnabled (false);
		cfgFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	cfgFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = cfgFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				lastSelDir = res.getParent ();
				ConfigFile cfg = new ConfigFile (res);
				cfg.read ();
				portCombo.setSelectedItem (cfg.getPort ());
				speedCombo.setSelectedItem (String.valueOf (cfg.getSpeed ()));
				dataBitsCombo.setSelectedItem (String.valueOf (cfg.getDBits ()));
				int lparity = cfg.getParity ();
				if ( lparity < parityCombo.getItemCount () )
					parityCombo.setSelectedIndex (lparity);
				int stopbits = cfg.getSBits ();
				if ( stopbits < stopBitsCombo.getItemCount () )
					stopBitsCombo.setSelectedIndex (stopbits);
				int lflow = cfg.getFlowCtl ();
				switch (lflow)
				{
					case 1:
						flowSoft.setSelected (true);
						flowHard.setSelected (false);
						break;
					case 2:
						flowSoft.setSelected (false);
						flowHard.setSelected (true);
						break;
					case 3:
						flowSoft.setSelected (true);
						flowHard.setSelected (true);
						break;
					default:
						flowSoft.setSelected (false);
						flowHard.setSelected (false);
						break;
				}
				//flowCombo.setSelectedIndex ();	// FIXME:
				setLocation (cfg.getX (), cfg.getY ());
				setSize (cfg.getWidth (), cfg.getHeight ());
				if ( cfg.getIsMax () ) setExtendedState (JFrame.MAXIMIZED_BOTH);
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "config.load");	// NOI18N
			}
		}
	}//GEN-LAST:event_loadConfButActionPerformed

	private void saveConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfButActionPerformed

		if ( cfgFC == null ) cfgFC = new JFileChooser ();
		cfgFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			@Override
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().endsWith (".cfg"))	// NOI18N
				{
						return true;
				}
				return false;
			}

			@Override
			public String getDescription ()
			{
				return java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("JYMAG_configuration_files") 
						+ " (*.cfg)";	// NOI18N
			}
		} );

		cfgFC.setAcceptAllFileFilterUsed (false);
		cfgFC.setMultiSelectionEnabled (false);
		cfgFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	cfgFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = cfgFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				lastSelDir = res.getParent ();
				ConfigFile cfg = new ConfigFile (res);
				cfg.setPort (portCombo.getSelectedItem ().toString ());
				cfg.setSpeed (Integer.valueOf (speedCombo.getSelectedItem ().toString ()));
				cfg.setDataBits (Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()));
				cfg.setParity (parityCombo.getSelectedIndex ());
				cfg.setStopBits (stopBitsCombo.getSelectedIndex ());
				cfg.setFlow (((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0));
				cfg.setX (getX ());
				cfg.setY (getY ());
				cfg.setWidth (getWidth ());
				cfg.setHeight (getHeight ());
				cfg.setIsMaximized ((getExtendedState () & JFrame.MAXIMIZED_BOTH) != 0);
				cfg.write ();
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "config.save");	// NOI18N
			}
		}
	}//GEN-LAST:event_saveConfButActionPerformed

	private void putListInTable (final String ofWhat,
		final DefaultTableModel dtm,
		final Vector<PhoneElement> placeForData)
	{
		try
		{
			final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			updateStatusLabel (STATUS.RECEIVING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);
			final int cSpeed = Integer.valueOf (speedCombo.getSelectedItem ().toString ());
			final int cDataBits = Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ());
			final float cStopBits = Float.valueOf (stopBitsCombo.getSelectedItem ().toString ());
			final int cParity = parityCombo.getSelectedIndex ();
			final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);
			SwingWorker<Vector<PhoneElement>, Void> sw =
				new SwingWorker<Vector<PhoneElement>, Void> ()
			{
				@Override
				protected Vector<PhoneElement> doInBackground ()
				{
					Vector<PhoneElement> ret;
					synchronized (sync)
					{
						try
						{
							final DataTransporter dt = new DataTransporter (id);
							dt.open (cSpeed, cDataBits, cStopBits,
								cParity, cFlow);
							ret = dt.getList (ofWhat);
							dt.close ();
							return ret;
						}
						catch (Exception e)
						{
							Utils.handleException (e, "putListInTable.SW.doInBackground");	// NOI18N
						}
						return null;
					}
				}

				@Override
				protected void done ()
				{
					progressBar.setValue (progressBar.getValue () + 1);
					updateStatusLabel (STATUS.READY);
					Vector<PhoneElement> ret = null;
					try
					{
						ret = get ();
						if ( ret != null )
						{
							if ( placeForData != null )
							{
								placeForData.removeAllElements ();
								placeForData.addAll (ret);
							}
							dtm.setRowCount (0);
							for ( int i=0; i < ret.size (); i++ )
							{
								dtm.addRow (new String[]
									{ret.get (i).getFilename ()
									+ "." + ret.get (i).getExt () }	// NOI18N
									);
							}
						}
						progressBar.setValue (0);
					}
					catch (Exception ex)
					{
						progressBar.setValue (0);
						Utils.handleException (ex,
							"putListInTable.SW.done: ret.size="	// NOI18N
							+ ((ret != null)? ret.size () : "0") );	// NOI18N
					}
				}
			};
			sw.execute ();
		}
		catch (Exception ex)
		{
			updateStatusLabel (STATUS.READY);
			Utils.handleException (ex, "putListInTable");	// NOI18N
		}
	}
/*
	private void disablePortControls ()
	{
		dataBitsCombo.setEnabled (false);
		flowCombo.setEnabled (false);
		parityCombo.setEnabled (false);
		portCombo.setEnabled (false);
		speedCombo.setEnabled (false);
		stopBitsCombo.setEnabled (false);
	}

	private void enablePortControls ()
	{
		dataBitsCombo.setEnabled (true);
		flowCombo.setEnabled (true);
		parityCombo.setEnabled (true);
		portCombo.setEnabled (true);
		speedCombo.setEnabled (true);
		stopBitsCombo.setEnabled (true);
	}
*/
	private enum STATUS
	{
		READY,
		SENDING,
		RECEIVING;
		
		@Override
		public String toString ()
		{
			if ( this.equals (STATUS.READY) )
				return java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("READY");
			else if ( this.equals (STATUS.SENDING) )
				return java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("SENDING");
			else if ( this.equals (STATUS.RECEIVING) )
				return java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("RECEIVING");
			return "";	// NOI18N
		}
	}

	private void updateStatusLabel (final STATUS s)
	{
		Utils.changeGUI (new Runnable ()
		{
			@Override
			public void run ()
			{
				if ( status == null || s == null ) return;
				status.setText (s.toString ());
				if ( s.equals (STATUS.READY) )
				{
					status.setForeground (Color.GREEN);
				}
				else
				{
					status.setForeground (Color.BLUE);
				}
				status.validate ();
			}
		});
	}

	// =============================== static methods

	/**
	 * Called when the programs needs to close.
	 * @param retval Return value passed to System.exit ().
	 */
	private static void closeProgram (int retval)
	{
		// close logging
		if ( System.err != null ) System.err.close ();
		// remove the log file if empty
		File log = new File (logFile);
		if ( (! log.exists ()) || log.length () == 0 ) log.delete ();
		// check if GUI was created, or only the command line was used
		/*
		if ( aboutBut != null )
		{
			// the window was constructed
			dispose ();
		}*/
		System.exit (retval);
	}

	private static int getElementsOfType (String type)
	{
		int ret = 0;
		try
		{
			if ( portName == null )
			{
		                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

				while (portList.hasMoreElements ())
				{
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

					if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
					{
						portName = id.getName ();
						break;
					}
				}
			}
			if ( destDirName == null )
			{
				destDirName = ".";	// NOI18N
			}
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portName);
			DataTransporter dt = new DataTransporter (id);
			dt.open (speed, dBits, sBits, parity, flow);
			System.out.println (getListStr + type);
			Vector<PhoneElement> elems = dt.getList (type);
			if ( elems != null )
			{
				for ( int i=0; i < elems.size (); i++ )
				{
					File received = new File (
						destDirName + File.separator
						+ elems.get (i).getFilename ()
						+ "." + elems.get (i).getExt ());	// NOI18N
					System.out.println (getFileStr + " '"	// NOI18N
						+ elems.get (i).getFilename ()
						+ "." + elems.get (i).getExt ()	// NOI18N
						+ "'");				// NOI18N
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
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "getElementsOfType " + type);	// NOI18N
		}
		return ret;
	}

	private static int getAllPics ()
	{
		return getElementsOfType ("PICTURES");	// NOI18N
	}

	private static int getAllRings ()
	{
		return getElementsOfType ("RINGTONES");	// NOI18N
	}

	private static int getAllTODOs ()
	{
		return getElementsOfType ("VTODO");	// NOI18N
	}

	private static int getAllEvents ()
	{
		return getElementsOfType ("VEVENT");	// NOI18N
	}

	private static int getAllVcards ()
	{
		return getElementsOfType ("VCARDS");	// NOI18N
	}

	private static int getAllAnimations ()
	{
		return getElementsOfType ("ANIMATIONS");	// NOI18N
	}

	private static int staticUpload (File f, String whichPort) throws Exception
	{
		if ( f == null ) return -7;
		if ( ! f.exists () || ! f.canRead () || ! f.isFile ()) return -8;
		if ( ! f.getName ().contains (".") ) return -9;	// NOI18N
		if ( ! Utils.filetypeIDs.containsKey (f.getName ().substring
			(f.getName ().lastIndexOf (".")+1)	// NOI18N
			.toLowerCase ()))
		{
			System.out.println (unsuppType+": '"	// NOI18N
				+f.getName ().substring
				(f.getName ().lastIndexOf (".")+1)	// NOI18N
				.toLowerCase () + "'");	// NOI18N
			return -10;
		}

		if ( whichPort == null )
		{
	                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

			while (portList.hasMoreElements ())
			{
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

				if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
				{
					whichPort = id.getName ();
					break;
				}
			}
		}
		CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
			(whichPort);
		DataTransporter dt = new DataTransporter (id);
		dt.open (speed, dBits, sBits, parity, flow);

		int put = dt.putFile (f, f.getName ().substring
			(0, f.getName ().indexOf (".")).replaceAll ("\\s", "_"));	// NOI18N

		dt.close ();
		if ( put != 0 )
		{
			String msg = String.valueOf (put);
			if ( put == -1 ) msg = uploadMsg1;
			else if ( put ==  -2 ) msg = uploadMsg2;
			else if ( put ==  -3 ) msg = uploadMsg3;
			else if ( put ==  -4 ) msg = uploadMsg4;
			else if ( put ==  -5 ) msg = uploadMsg5;
			else if ( put ==  -6 ) msg = uploadMsg6;
			else if ( put ==  -7 ) msg = uploadMsg7;
			else if ( put ==  -8 ) msg = uploadMsg8;
			else if ( put ==  -9 ) msg = uploadMsg9;
			else if ( put == -10 ) msg = uploadMsg10;
			else if ( put == -11 ) msg = uploadMsg11;

			System.out.println ( errString + ": " + msg );	// NOI18N
		}
		return put;
	}

	private static int scanPorts ()
	{
		int active = 0;

		Enumeration portList = CommPortIdentifier.getPortIdentifiers ();
		while (portList.hasMoreElements ())
		{
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

			if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
			{
				// scan ports for "AT"-"OK"
				System.out.print (tryPortStr + id.getName () + "...");	// NOI18N
				try
				{
					DataTransporter dt = new DataTransporter (id);
					dt.open (speed, dBits, sBits, parity, flow);
					if ( dt.test () == 0 )
					{
						System.out.print (gotAnsStr);
						active++;
					}
					System.out.println ();
					dt.close ();
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "scanPorts:"	// NOI18N
						+ id.getName ());
				}
			}
		}
		if (active == 0)
		{
			System.out.println (noAnsStr);
			return -1;
		}
		return 0;
	}

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
	 * Program starting point.
	 * @param args the command line arguments.
	 */
	public static void main (String args[])
	{
		// redirect stderr (caught and uncaught exceptions) to a file
		try
		{
			System.setErr (new PrintStream (new File (logFile)));
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "stderr");	// NOI18N
		}

		// set default uncaught exception handler:
		Thread.setDefaultUncaughtExceptionHandler (Utils.handler);

		// firmware version pattern
		verPattern = Pattern.compile ("\\s*\\+KPSV:\\s*(.+)");	// NOI18N

		if ( args != null )
		{
			for ( int i=0; i < args.length; i++ )
			{
				if ( args[i].toLowerCase ().equals ("--help")	// NOI18N
					|| args[i].toLowerCase ().equals ("-h")	// NOI18N
					|| args[i].toLowerCase ().equals ("-?")	// NOI18N
					|| args[i].toLowerCase ().equals ("/?") )	// NOI18N
				{
					System.out.println ("JYMAG (Java Your Music and Graphics) "+ progIntroStr +	// NOI18N
						"\n\n*** " + rxtxReqStr +" ***\n\n" +	// NOI18N
						"Author: Bogdan Drozdowski, bogdandr @ op . pl\n" +	// NOI18N
						"License: GPLv3+\n" +	// NOI18N
				                "http://rudy.mif.pg.gda.pl/~bogdro/soft\n\n" +	// NOI18N
				                cmdLineStr
						);
					closeProgram (0);
				}
				else if ( args[i].toLowerCase ().equals ("--license")	// NOI18N
					|| args[i].toLowerCase ().equals ("--licence") )	// NOI18N
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
					closeProgram (0);
				}
				else if ( args[i].toLowerCase ().equals ("--version")	// NOI18N
					|| args[i].toLowerCase ().equals ("-v") )	// NOI18N
				{
					System.out.println ("JYMAG " + verWord + " " + verString);	// NOI18N
					closeProgram (0);
				}
				else if ( args[i].toLowerCase ().equals ("--port") )	// NOI18N
				{
					if ( i < args.length-1 )
					{
						portName = args[i+1];
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--parity") )	// NOI18N
				{
					parity = 0;
					if ( i < args.length-1 )
					{
						// <none, even, odd, space, mark>
						if ( args[i+1].toLowerCase ().equals ("none") )	// NOI18N
						{
							parity = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("even") )	// NOI18N
						{
							parity = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("odd") )	// NOI18N
						{
							parity = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("space") )	// NOI18N
						{
							parity = 3;
						}
						else if ( args[i+1].toLowerCase ().equals ("mark") )	// NOI18N
						{
							parity = 4;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--databits") )	// NOI18N
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
				else if ( args[i].toLowerCase ().equals ("--speed") )	// NOI18N
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
				else if ( args[i].toLowerCase ().equals ("--stopbits") )	// NOI18N
				{
					sBits = 1;
					if ( i < args.length-1 )
					{
						if ( args[i+1].toLowerCase ().equals ("2") )	// NOI18N
						{
							sBits = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("1.5")	// NOI18N
							|| args[i+1].toLowerCase ().equals ("1,5") )	// NOI18N
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
				else if ( args[i].toLowerCase ().equals ("--flow") )	// NOI18N
				{
					flow = 0;
					if ( i < args.length-1 )
					{
						// <none,soft,hard>
						if ( args[i+1].toLowerCase ().equals ("none") )	// NOI18N
						{
							flow = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("soft") )	// NOI18N
						{
							flow = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("hard") )	// NOI18N
						{
							flow = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("soft+hard")	// NOI18N
								 || args[i+1].toLowerCase ().equals ("hard+soft") )	// NOI18N
						{
							flow = 3;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--scan") )	// NOI18N
				{
					closeProgram (scanPorts ());
				}
				else if ( args[i].toLowerCase ().equals ("--upload") )	// NOI18N
				{
					if ( i < args.length-1 )
					{
						try
						{
							int res = staticUpload (
								new File (args[i+1]), portName );
							closeProgram (res);
						}
						catch ( Exception ex )
						{
							Utils.handleException (ex,
								"main.staticUpload(" + args[i+1] + ")");	// NOI18N
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-photos") )	// NOI18N
				{
					closeProgram (getAllPics ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-ringtones") )	// NOI18N
				{
					closeProgram (getAllRings ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-todo") )	// NOI18N
				{
					closeProgram (getAllTODOs ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-events") )	// NOI18N
				{
					closeProgram (getAllEvents ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-vcards") )	// NOI18N
				{
					closeProgram (getAllVcards ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-vcards") )	// NOI18N
				{
					closeProgram (getAllAnimations ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all") )	// NOI18N
				{
					closeProgram (getAllPics ()
						+ getAllRings ()
						+ getAllTODOs ()
						+ getAllEvents ()
						+ getAllVcards ()
						+ getAllAnimations ()
						);
				}
				else if ( args[i].toLowerCase ().equals ("--download-dir") )	// NOI18N
				{
					if ( i < args.length-1 )
					{
						destDirName = args[i+1];
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--delete-after-download") )	// NOI18N
				{
					deleteAfterDownload = true;
				}
				else if ( args[i].toLowerCase ().equals ("--lang") )	// NOI18N
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
								"main.lang(" + args[i+1] + ")");	// NOI18N
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--conf") )	// NOI18N
				{
					if ( i < args.length-1 )
					{
						readConfig (new File (args[i+1]));
					}
				}
			}	// for i
		}
		try
		{
			UIManager.setLookAndFeel (
				UIManager.getSystemLookAndFeelClassName ());
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "UIManager.setLookAndFeel");	// NOI18N
		}

		//JFrame.setDefaultLookAndFeelDecorated (true);

		SwingUtilities.invokeLater (new Runnable ()
		{
			@Override
			public void run ()
			{
				new MainWindow ().setVisible (true);
			}
		});
	}	// main ()

	private class KL extends KeyAdapter
	{
		/**
		 * Receives key-typed events (called when the user types a key).
		 * @param ke The key-typed event.
		 */
		@Override
		public void keyTyped (KeyEvent ke)
		{
			if ( ke.getKeyChar () == KeyEvent.VK_ESCAPE )
			{
				dispose ();
			}
		}
	}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutBut;
    private javax.swing.JPanel addrBookPane;
    private javax.swing.JTable addrTable;
    private javax.swing.JPanel animPane;
    private javax.swing.JTable animTable;
    private javax.swing.JLabel bpsLabel;
    private javax.swing.JComboBox dataBitsCombo;
    private javax.swing.JLabel databitsLabel;
    private javax.swing.JButton deleteAddrBut;
    private javax.swing.JButton deleteAnimBut;
    private javax.swing.JButton deleteEventBut;
    private javax.swing.JButton deleteJavaBut;
    private javax.swing.JButton deletePhotoBut;
    private javax.swing.JButton deleteRingBut;
    private javax.swing.JButton deleteTodoBut;
    private javax.swing.JButton downloadAddrBut;
    private javax.swing.JButton downloadAnimBut;
    private javax.swing.JButton downloadEventBut;
    private javax.swing.JButton downloadJavaBut;
    private javax.swing.JButton downloadPhotoBut;
    private javax.swing.JButton downloadRingBut;
    private javax.swing.JButton downloadTodoBut;
    private javax.swing.JPanel eventPane;
    private javax.swing.JTable eventTable;
    private javax.swing.JButton exitBut;
    private javax.swing.JLabel firmware;
    private javax.swing.JLabel firmwareLabel;
    private javax.swing.JCheckBox flowHard;
    private javax.swing.JLabel flowLabel;
    private javax.swing.JCheckBox flowSoft;
    private javax.swing.JButton getAddrListBut;
    private javax.swing.JButton getAnimListBut;
    private javax.swing.JButton getCapBut;
    private javax.swing.JButton getEventListBut;
    private javax.swing.JButton getJavaListBut;
    private javax.swing.JButton getPhotoListBut;
    private javax.swing.JButton getRingListBut;
    private javax.swing.JButton getTodoListBut;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel javaPane;
    private javax.swing.JTable javaTable;
    private javax.swing.JButton loadConfBut;
    private javax.swing.JComboBox parityCombo;
    private javax.swing.JLabel parityLabel;
    private javax.swing.JLabel phone;
    private javax.swing.JLabel phoneTypeLabel;
    private javax.swing.JPanel photoPane;
    private javax.swing.JTable photoTable;
    private javax.swing.JComboBox portCombo;
    private javax.swing.JLabel portLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JButton rawBut;
    private javax.swing.JPanel ringPane;
    private javax.swing.JTable ringTable;
    private javax.swing.JButton saveConfBut;
    private javax.swing.JButton scanButton;
    private javax.swing.JComboBox speedCombo;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JLabel status;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox stopBitsCombo;
    private javax.swing.JLabel stopbitsLabel;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JPanel todoPane;
    private javax.swing.JTable todoTable;
    private javax.swing.JButton uploadAddrBut;
    private javax.swing.JButton uploadAnimBut;
    private javax.swing.JButton uploadEventBut;
    private javax.swing.JButton uploadJavaBut;
    private javax.swing.JButton uploadPhotoBut;
    private javax.swing.JButton uploadRingBut;
    private javax.swing.JButton uploadTodoBut;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}
