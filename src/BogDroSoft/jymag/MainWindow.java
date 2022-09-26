/*
 * MainWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2007 Bogdan Drozdowski, bogdandr (at) op.pl
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
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
	implements KeyListener
{
	private static final long serialVersionUID = 65L;
	private final MainWindow mw = this;
	private static final String verString = "0.5";	// NOI18N
	private Vector<PhoneElement> currentElements;
	private Vector<PhoneElement> currentRingElements;
	private Vector<PhoneElement> currentPhotoElements;
	private Vector<PhoneElement> currentAddrElements;
	private Vector<PhoneElement> currentTodoElements;
	private Vector<PhoneElement> currentEventElements;

	private String lastSelDir;
	// synchronization variable:
	private Object sync = new Object ();
	// port-firmware pairs and the firmware version pattern, used for displaying:
	private Hashtable<String, String> firmwares;
	private static Pattern verPattern;

	private final static String logFile = "jymag.log";

	// ------------ i18n stuff
	private static String noAnsString = "No answers received";
	private static String errString = "Error";
	private static String multiAnsString = "Multiple answers received. Select the correct port.";
	private static String whichString = "Which one?";
	private static String exOverString = " exists. Overwrite?";
	private static String overwriteStr = "Overwrite?";
	private static String suppOperThank = "Supported opertations:"+
		"\n" +	// NOI18N
		"1) Getting lists of: pictures, ringtones, addressbook entries, to-do tasks and events"+
		"\n" +	// NOI18N
		"2) Downloading:"+
		"\n" +	// NOI18N
		"   a) pictures:"+
		" JPG (" + 	// NOI18N
		"both Sagem and non-Sagem" +
		"), GIF, BMP, PNG\n" +	// NOI18N
		"   b) ringtones:"+
		" MIDI, AMR, WAV\n" +	// NOI18N
		"   c) addressbook entries (vCards)"+
		"\n" +	// NOI18N
		"   d) to-do tasks"+
		"\n" +	// NOI18N
		"   e) events (reminders, ...)"+
		"\n" +	// NOI18N
		"3) Uploading:"+
		"\n" +	// NOI18N
		"   a) pictures:"+
		" JPG, GIF, BMP, PNG\n" +	// NOI18N
		"   b) ringtones: MIDI, WAV (IMA ADPCM, 8000Hz 16bit Mono?)"+
		"\n" +	// NOI18N
		"4) Deleting pictures, ringtones, addressbook entries, to-do tasks and events"
		+"\n" +	// NOI18N
		"\n" +
		"Run with --help for a list of command-line options."+
		"\n\n" +	// NOI18N
		"Thanks to:";
	private static String aboutStr = "About";
	private static String freeMPASStr = "a Free 'My Pictures and Sounds' replacement.";
	private static String verWord = "Version";
	private static String picsString = "Supported pictures";
	private static String soundsString = "Supported sounds";
	private static String addrString = "Supported addressbook files";
	private static String todoString = "Supported to-do files";
	private static String eventString = "Supported event files";
	private static String getListStr = "Getting list of ";
	private static String getFileStr = "Getting file";
	private static String unsuppType = "Unsupported file type:";
	private static String tryPortStr = "Trying port ";
	private static String gotAnsStr = "Got answer!";
	private static String noAnsStr = "No answers received";
	private static String progIntroStr = "is a program " +
		"for retrieving and sending multimedia from and to a Sagem mobile phone.";
	private static String rxtxReqStr = "You need the RxTx Java Transmission package from www.rxtx.org";
	private static String cmdLineStr = "Command-line options:"+
		"\n--help, -h, -?, /?\t- "+	// NOI18N
		"display help"+
		"\n--licence, --license\t- "+	// NOI18N
		"display license information"+
		"\n--version, -v\t\t- "+	// NOI18N
		"display version"+
		"\n--download-dir <dir>\t- "+	// NOI18N
		"set the default directory"+
		"\n--flow <none,soft,hard>\t- "+	// NOI18N
		"set the flow control mode"+
		"\n--port <filename>\t- "+	// NOI18N
		"set the default port"+
		"\n--parity <none,even,odd,space,mark>\t- "+	// NOI18N
		"set the parity mode"+
		"\n--databits <5,6,7,8>\t- "+	// NOI18N
		"set the number of data bits"+
		"\n--speed"+	// NOI18N
		" <1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200," +	// NOI18N
		"\n\t230400, 460800, 500000, 576000, 921600, 1000000, 1152000,\n" +	// NOI18N
		"\t1500000, 2000000, 2500000, 3000000, 3500000, 4000000>\n\t\t\t- " +	// NOI18N
		"set the port speed"+
		"\n--stopbits <1,1.5,2>\t- "+	// NOI18N
		"set the number of stop bits"+
		"\n--upload <filename>\t- "+	// NOI18N
		"upload the given file to the phone and exit"+
		"\n--scan\t\t\t- "+	// NOI18N
		"scan available ports for OK answers and exit"+
		"\n--download-all-photos\t- "+	// NOI18N
		"download all photos from the phone and exit"+
		"\n--download-all-ringtones - "+	// NOI18N
		"download all ringtones from the phone and exit"+
		"\n--download-all-todo\t- "+	// NOI18N
		"download all to-do tasks from the phone and exit"+
		"\n--download-all-events\t- "+	// NOI18N
		"download all events from the phone and exit"+
		"\n--download-all-vcards\t- "+	// NOI18N
		"download all addressbook entries from the phone and exit"+
		"\n--download-all\t\t- "+	// NOI18N
		"combine all 'download' options and exit"+
		"\n--delete-after-download\t- "+	// NOI18N
		"delete the downloaded elements if downloaded succesfully"+
		"\n" +	// NOI18N
		"\n" +	// NOI18N
		"JYMAG exits with 0 code if command-line operation (download, upload or scan) was successful.";
	private static String deleteQuestion = "Are you quite sure you want to delete?";
	private static String questionString = "Question";
	private static String fileNotWriteMsg = "Can't write to file";

	// error messages for file upload:
	private static String uploadMsg1  = "File upload init failed";
	private static String uploadMsg2  = "Sending file name's length failed";
	private static String uploadMsg3  = "Sending file name failed";
	private static String uploadMsg4  = "Format not suported by phone/file too big";
	private static String uploadMsg5  = "File not accepted (too big?)";
	private static String uploadMsg6  = "Closing transmission failed";
	private static String uploadMsg7  = "Exception occurred";
	private static String uploadMsg8  = "Incorrect parameter";
	private static String uploadMsg9  = "Format not suported by JYMAG";
	private static String uploadMsg10 = "Number of attempts exceeded";
	private static String uploadMsg11 = "Incorrect parameter";

	private static String downloadMsg1  = "Exception occurred";
	private static String downloadMsg2  = "No data received";
	private static String downloadMsg3  = "Incorrect parameter";

	private static String defFirmware = "(press Scan)";

	// ------------ static variables for command-line

	// read from the command-line:
	private static String destDirName;
	private static int dBits = 8;
	private static float sBits = 1;
	private static int speed = 115200;
	private static int flow = 0;
	private static int parity = 0;
	private static String portName;
	private static boolean deleteAfterDownload = false;

	/**
	 * Creates new form MainWindow.
	 */
        public MainWindow ()
        {
		initComponents ();
		firmware.setText (defFirmware);
		updateStatusLabel (STATUS.READY);
                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();
		while (portList.hasMoreElements ())
		{
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

			if (id.getPortType () == CommPortIdentifier.PORT_SERIAL)
			{
				portCombo.addItem (id.getName ());
				if ( portName != null )
				{
					if ( id.getName ().equals (portName) )
					{
						portCombo.setSelectedItem (id.getName ());
					}
				}
			}
		}
		dataBitsCombo.setSelectedItem (String.valueOf (dBits));
		stopBitsCombo.setSelectedItem (String.valueOf (sBits));
		speedCombo.setSelectedItem (String.valueOf (speed));
		flowCombo.setSelectedIndex (flow);
		parityCombo.setSelectedIndex (parity);

        	aboutBut.addKeyListener (this);
        	addrBookPane.addKeyListener (this);
        	addrTable.addKeyListener (this);
        	dataBitsCombo.addKeyListener (this);
        	deleteAddrBut.addKeyListener (this);
        	deleteEventBut.addKeyListener (this);
        	deletePhotoBut.addKeyListener (this);
        	deleteRingBut.addKeyListener (this);
        	deleteTodoBut.addKeyListener (this);
        	downloadAddrBut.addKeyListener (this);
        	downloadEventBut.addKeyListener (this);
        	downloadPhotoBut.addKeyListener (this);
        	downloadRingBut.addKeyListener (this);
        	downloadTodoBut.addKeyListener (this);
        	eventPane.addKeyListener (this);
        	eventTable.addKeyListener (this);
        	flowCombo.addKeyListener (this);
        	getAddrListBut.addKeyListener (this);
        	getEventListBut.addKeyListener (this);
        	getPhotoListBut.addKeyListener (this);
        	getRingListBut.addKeyListener (this);
        	getTodoListBut.addKeyListener (this);
        	jScrollPane1.addKeyListener (this);
        	jScrollPane2.addKeyListener (this);
        	jScrollPane3.addKeyListener (this);
        	jScrollPane4.addKeyListener (this);
        	jScrollPane5.addKeyListener (this);
        	parityCombo.addKeyListener (this);
        	photoPane.addKeyListener (this);
        	photoTable.addKeyListener (this);
        	portCombo.addKeyListener (this);
        	rawBut.addKeyListener (this);
        	ringPane.addKeyListener (this);
        	ringTable.addKeyListener (this);
        	scanButton.addKeyListener (this);
        	speedCombo.addKeyListener (this);
        	stopBitsCombo.addKeyListener (this);
        	tabPane.addKeyListener (this);
        	todoPane.addKeyListener (this);
        	todoTable.addKeyListener (this);
        	uploadAddrBut.addKeyListener (this);
        	uploadEventBut.addKeyListener (this);
        	uploadPhotoBut.addKeyListener (this);
        	uploadRingBut.addKeyListener (this);
        	uploadTodoBut.addKeyListener (this);
	}

        /**
	 * This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                portCombo = new javax.swing.JComboBox();
                speedCombo = new javax.swing.JComboBox();
                dataBitsCombo = new javax.swing.JComboBox();
                parityCombo = new javax.swing.JComboBox();
                stopBitsCombo = new javax.swing.JComboBox();
                scanButton = new javax.swing.JButton();
                tabPane = new javax.swing.JTabbedPane();
                photoPane = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                photoTable = new javax.swing.JTable();
                getPhotoListBut = new javax.swing.JButton();
                downloadPhotoBut = new javax.swing.JButton();
                uploadPhotoBut = new javax.swing.JButton();
                deletePhotoBut = new javax.swing.JButton();
                ringPane = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                ringTable = new javax.swing.JTable();
                getRingListBut = new javax.swing.JButton();
                downloadRingBut = new javax.swing.JButton();
                uploadRingBut = new javax.swing.JButton();
                deleteRingBut = new javax.swing.JButton();
                addrBookPane = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                addrTable = new javax.swing.JTable();
                getAddrListBut = new javax.swing.JButton();
                downloadAddrBut = new javax.swing.JButton();
                uploadAddrBut = new javax.swing.JButton();
                deleteAddrBut = new javax.swing.JButton();
                todoPane = new javax.swing.JPanel();
                jScrollPane4 = new javax.swing.JScrollPane();
                todoTable = new javax.swing.JTable();
                getTodoListBut = new javax.swing.JButton();
                downloadTodoBut = new javax.swing.JButton();
                uploadTodoBut = new javax.swing.JButton();
                deleteTodoBut = new javax.swing.JButton();
                eventPane = new javax.swing.JPanel();
                jScrollPane5 = new javax.swing.JScrollPane();
                eventTable = new javax.swing.JTable();
                getEventListBut = new javax.swing.JButton();
                downloadEventBut = new javax.swing.JButton();
                uploadEventBut = new javax.swing.JButton();
                deleteEventBut = new javax.swing.JButton();
                portLabel = new javax.swing.JLabel();
                speedLabel = new javax.swing.JLabel();
                databitsLabel = new javax.swing.JLabel();
                parityLabel = new javax.swing.JLabel();
                stopbitsLabel = new javax.swing.JLabel();
                flowLabel = new javax.swing.JLabel();
                flowCombo = new javax.swing.JComboBox();
                aboutBut = new javax.swing.JButton();
                rawBut = new javax.swing.JButton();
                bpsLabel = new javax.swing.JLabel();
                statusLabel = new javax.swing.JLabel();
                status = new javax.swing.JLabel();
                firmwareLabel = new javax.swing.JLabel();
                firmware = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Your Music and Graphics");
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

                scanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/SCAN5.png"))); // NOI18N
                scanButton.setText("Scan ports");
                scanButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                scanButtonActionPerformed(evt);
                        }
                });

                photoTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Name"
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

                getPhotoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/list.png"))); // NOI18N
                getPhotoListBut.setText("Get list");
                getPhotoListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getPhotoListButActionPerformed(evt);
                        }
                });

                downloadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12A.png"))); // NOI18N
                downloadPhotoBut.setText("Download selected");
                downloadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12D.png"))); // NOI18N
                uploadPhotoBut.setText("Upload");
                uploadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadPhotoButActionPerformed(evt);
                        }
                });

                deletePhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/delete.png"))); // NOI18N
                deletePhotoBut.setText("Delete selected");
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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getPhotoListBut)
                                        .addComponent(downloadPhotoBut)
                                        .addComponent(uploadPhotoBut)
                                        .addComponent(deletePhotoBut))
                                .addContainerGap())
                );

                tabPane.addTab("Photos", new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/pict-1.png")), photoPane); // NOI18N

                ringTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Name"
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

                getRingListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/list.png"))); // NOI18N
                getRingListBut.setText("Get list");
                getRingListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getRingListButActionPerformed(evt);
                        }
                });

                downloadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12A.png"))); // NOI18N
                downloadRingBut.setText("Download selected");
                downloadRingBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12D.png"))); // NOI18N
                uploadRingBut.setText("Upload");
                uploadRingBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadRingButActionPerformed(evt);
                        }
                });

                deleteRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/delete.png"))); // NOI18N
                deleteRingBut.setText("Delete selected");
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
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getRingListBut)
                                        .addComponent(downloadRingBut)
                                        .addComponent(uploadRingBut)
                                        .addComponent(deleteRingBut))
                                .addContainerGap())
                );

                tabPane.addTab("Ringtones", new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ring.png")), ringPane); // NOI18N

                addrTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Name"
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

                getAddrListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/list.png"))); // NOI18N
                getAddrListBut.setText("Get list");
                getAddrListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getAddrListButActionPerformed(evt);
                        }
                });

                downloadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12A.png"))); // NOI18N
                downloadAddrBut.setText("Download selected");
                downloadAddrBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12D.png"))); // NOI18N
                uploadAddrBut.setText("Upload");
                uploadAddrBut.setEnabled(false);
                uploadAddrBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadAddrButActionPerformed(evt);
                        }
                });

                deleteAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/delete.png"))); // NOI18N
                deleteAddrBut.setText("Delete selected");
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
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getAddrListBut)
                                        .addComponent(downloadAddrBut)
                                        .addComponent(uploadAddrBut)
                                        .addComponent(deleteAddrBut))
                                .addContainerGap())
                );

                tabPane.addTab("Addressbook", new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/abook.png")), addrBookPane); // NOI18N

                todoTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Name"
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

                getTodoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/list.png"))); // NOI18N
                getTodoListBut.setText("Get list");
                getTodoListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getTodoListButActionPerformed(evt);
                        }
                });

                downloadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12A.png"))); // NOI18N
                downloadTodoBut.setText("Download selected");
                downloadTodoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12D.png"))); // NOI18N
                uploadTodoBut.setText("Upload");
                uploadTodoBut.setEnabled(false);
                uploadTodoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadTodoButActionPerformed(evt);
                        }
                });

                deleteTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/delete.png"))); // NOI18N
                deleteTodoBut.setText("Delete selected");
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
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getTodoListBut)
                                        .addComponent(downloadTodoBut)
                                        .addComponent(uploadTodoBut)
                                        .addComponent(deleteTodoBut))
                                .addContainerGap())
                );

                tabPane.addTab("To do", new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/todo.png")), todoPane); // NOI18N

                eventTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Name"
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

                getEventListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/list.png"))); // NOI18N
                getEventListBut.setText("Get list");
                getEventListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getEventListButActionPerformed(evt);
                        }
                });

                downloadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12A.png"))); // NOI18N
                downloadEventBut.setText("Download selected");
                downloadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/ARROW12D.png"))); // NOI18N
                uploadEventBut.setText("Upload");
                uploadEventBut.setEnabled(false);
                uploadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadEventButActionPerformed(evt);
                        }
                });

                deleteEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/delete.png"))); // NOI18N
                deleteEventBut.setText("Delete selected");
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
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getEventListBut)
                                        .addComponent(downloadEventBut)
                                        .addComponent(uploadEventBut)
                                        .addComponent(deleteEventBut))
                                .addContainerGap())
                );

                tabPane.addTab("Events", new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/event.png")), eventPane); // NOI18N

                portLabel.setText("Port:");

                speedLabel.setText("Speed:");

                databitsLabel.setText("Data bits:");

                parityLabel.setText("Parity:");

                stopbitsLabel.setText("Stop bits:");

                flowLabel.setText("Flow control:");

                flowCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Software (XON/XOFF)", "Hardware (CTS/RTS)" }));

                aboutBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/QMARK8.png"))); // NOI18N
                aboutBut.setText("About...");
                aboutBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                aboutButActionPerformed(evt);
                        }
                });

                rawBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/gfx/tools1.png"))); // NOI18N
                rawBut.setText("Manual commands");
                rawBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                rawButActionPerformed(evt);
                        }
                });

                bpsLabel.setText("bps");

                statusLabel.setText("Program status:");

                status.setForeground(new java.awt.Color(0, 204, 0));
                status.setText("READY");

                firmwareLabel.setText("Firmware:");

                firmware.setText("-");

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tabPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(firmwareLabel)
                                                        .addGroup(layout.createSequentialGroup()
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
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(flowLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(stopbitsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(parityLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                                                        .addComponent(statusLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(status)
                                                                        .addComponent(firmware))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(aboutBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(scanButton)
                                                                        .addComponent(rawBut)))
                                                        .addComponent(flowCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
                );

                layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {aboutBut, rawBut, scanButton});

                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
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
                                                        .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(parityLabel)
                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(stopbitsLabel)
                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(flowCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(flowLabel)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(scanButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(aboutBut)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(rawBut)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(firmwareLabel)
                                                        .addComponent(firmware))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(status)
                                                        .addComponent(statusLabel))))
                                .addGap(18, 18, 18)
                                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                                .addContainerGap())
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void scanButtonActionPerformed (java.awt.event.ActionEvent evt)	{//GEN-FIRST:event_scanButtonActionPerformed

		updateStatusLabel (STATUS.SENDING);
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

					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
					{
						// scan ports for "AT"-"OK"
						try
						{
							synchronized (sync)
							{
								DataTransporter dt = new DataTransporter (id);
								dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
									Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
									Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
									parityCombo.getSelectedIndex (),
									flowCombo.getSelectedIndex ()
									);
								int testRes = dt.test ();
								if ( testRes == 0 )
								{
									if ( firmwares == null )
										firmwares = new Hashtable<String, String> (1);
									active.add (id);
									// get firmware version
									String rcvd = "";
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
											String[] lines = rcvd.split ("\r");
											for ( int i=0; i < lines.length; i++ )
											{
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
										catch (Exception ex)
										{
											Utils.handleException (ex,
												"pattern/matcher");	// NOI18N
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
					}
					else if (active.size () == 0)
					{
						JOptionPane.showMessageDialog (mw,
							noAnsString, errString,
							JOptionPane.ERROR_MESSAGE);
					}
					else if (active.size () == 1)
					{
						portCombo.setSelectedItem (active.get (0).getName ());
						if ( firmwares != null )
						{
							if ( firmwares.containsKey (active.get (0).getName ()) )
							{
								firmware.setText (firmwares.get (active.get (0).getName ()));
							}
							else
							{
								firmware.setText (defFirmware);
							}
						}
						else
						{
							firmware.setText (defFirmware);
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
									firmware.setText (defFirmware);
								}
							}
							else
							{
								firmware.setText (defFirmware);
							}
						}
						else
						{
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
									firmware.setText (defFirmware);
								}
							}
							else
							{
								firmware.setText (defFirmware);
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

	private void downloadButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButActionPerformed

		int[] selectedRows = null;
		if ( tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = photoTable.getSelectedRows ();
			currentElements = currentPhotoElements;
		}
		else if ( tabPane.getSelectedIndex () == 1 )
		{
			selectedRows = ringTable.getSelectedRows ();
			currentElements = currentRingElements;
		}
		else if ( tabPane.getSelectedIndex () == 2 )
		{
			selectedRows = addrTable.getSelectedRows ();
			currentElements = currentAddrElements;
		}
		else if ( tabPane.getSelectedIndex () == 3 )
		{
			selectedRows = todoTable.getSelectedRows ();
			currentElements = currentTodoElements;
		}

		else //if ( tabPane.getSelectedIndex () == 4 )
		{
			selectedRows = eventTable.getSelectedRows ();
			currentElements = currentEventElements;
		}
		if ( selectedRows == null || currentElements == null ) return;
		if ( selectedRows.length == 0 ) return;

		JFileChooser jfc = new JFileChooser ();
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		jfc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		// destDirName is the static field
		if ( destDirName != null )
		{
			if ( destDirName.length () > 0 )
			{
				File d = new File (destDirName);
				jfc.setSelectedFile (d);
				jfc.setCurrentDirectory (d);
			}
		}
		if ( lastSelDir != null )
		{
			File d = new File (lastSelDir);
			jfc.setSelectedFile (d);
			jfc.setCurrentDirectory (d);
		}
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File destDir = jfc.getSelectedFile ();
				lastSelDir = destDir.getAbsolutePath ();
				final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(portCombo.getSelectedItem ().toString ());
				final AtomicInteger threads = new AtomicInteger (0);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					final File received = new File (
						destDir.getAbsolutePath () + File.separator
						+ currentElements.get (selectedRows[i]).getFilename ()
						+ "." 	// NOI18N
						+ currentElements.get (selectedRows[i]).getExt ());
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (this,
							currentElements.get (selectedRows[i]).getFilename ()
							+ "." 	// NOI18N
							+ currentElements.get (selectedRows[i]).getExt () +
							exOverString,
							overwriteStr,
							JOptionPane.YES_NO_CANCEL_OPTION);
						if ( res != JOptionPane.YES_OPTION ) continue;
					}
					if ( received.exists () && ! received.canWrite () )
					{
						JOptionPane.showMessageDialog (this,
							fileNotWriteMsg + ": " + received.getName (),
							errString, JOptionPane.ERROR_MESSAGE);
						continue;
					}
					final int toGet = selectedRows[i];
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
									dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
										Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
										Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
										parityCombo.getSelectedIndex (),
										flowCombo.getSelectedIndex ()
										);
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
							}
							catch (Exception ex)
							{
								if ( th == 0 )
								{
									updateStatusLabel (STATUS.READY);
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

		JOptionPane.showMessageDialog (this,
			// Object, not necessarily a String
			"JYMAG - Java Your Music and Graphics - " + freeMPASStr + "\n" +	// NOI18N
			verWord + ": " + verString + "\n" +		// NOI18N
			"Author: Bogdan Drozdowski, bogdandr@op.pl\n" +	// NOI18N
			"http://rudy.mif.pg.gda.pl/~bogdro/soft, http://rudy.mif.pg.gda.pl/~bogdro/inne\n" +	// NOI18N
			"License: GPLv3+\n\n" +				// NOI18N
			suppOperThank +
			"\nSharp (sharpy+at+xox.pl) for GetPic.pl\n"+	// NOI18N
			"sebtx452 @ gmail.com for wxPicSound\n" +	// NOI18N
			"MIKSOFT for \"Mobile Media Converter\"\n" +	// NOI18N
			"\"ffmpeg project\" for ffmpeg",		// NOI18N
			aboutStr, JOptionPane.INFORMATION_MESSAGE );

	}//GEN-LAST:event_aboutButActionPerformed

	private void uploadPhotoButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadPhotoButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
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

			public String getDescription ()
			{
				return picsString;
			}
		} );

		jfc.setAcceptAllFileFilterUsed (false);
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		if ( lastSelDir != null )
			jfc.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadPhotoButActionPerformed

	private void uploadRingButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadRingButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
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

			public String getDescription ()
			{
				return soundsString;
			}
		} );

		jfc.setAcceptAllFileFilterUsed (false);
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		if ( lastSelDir != null )
			jfc.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadRingButActionPerformed

	private void uploadAddrButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAddrButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
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

			public String getDescription ()
			{
				return addrString;
			}
		} );

		jfc.setAcceptAllFileFilterUsed (false);
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		if ( lastSelDir != null )
			jfc.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAddrButActionPerformed

	private void uploadTodoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadTodoButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
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

			public String getDescription ()
			{
				return todoString;
			}
		} );

		jfc.setAcceptAllFileFilterUsed (false);
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		if ( lastSelDir != null )
			jfc.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadTodoButActionPerformed

	private void uploadEventButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadEventButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
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

			public String getDescription ()
			{
				return eventString;
			}
		} );

		jfc.setAcceptAllFileFilterUsed (false);
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		if ( lastSelDir != null )
			jfc.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private void dynamicUpload (final File f)
	{
		try
		{
			final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			updateStatusLabel (STATUS.SENDING);
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
							dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
								Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
								Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
								parityCombo.getSelectedIndex (),
								flowCombo.getSelectedIndex ()
								);
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
						updateStatusLabel (STATUS.READY);
						int put = get ().intValue ();
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
					}
					catch (Exception ex)
					{
						updateStatusLabel (STATUS.READY);
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

		int[] selectedRows;
		if ( tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = photoTable.getSelectedRows ();
			currentElements = currentPhotoElements;
		}
		else if ( tabPane.getSelectedIndex () == 1 )
		{
			selectedRows = ringTable.getSelectedRows ();
			currentElements = currentRingElements;
		}
		else if ( tabPane.getSelectedIndex () == 2 )
		{
			selectedRows = addrTable.getSelectedRows ();
			currentElements = currentAddrElements;
		}
		else if ( tabPane.getSelectedIndex () == 3 )
		{
			selectedRows = todoTable.getSelectedRows ();
			currentElements = currentTodoElements;
		}

		else //if ( tabPane.getSelectedIndex () == 4 )
		{
			selectedRows = eventTable.getSelectedRows ();
			currentElements = currentEventElements;
		}
		if ( selectedRows == null ) return;
		if ( selectedRows.length == 0 ) return;

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
				for ( int i=0; i < selectedRows.length; i++ )
				{
					updateStatusLabel (STATUS.SENDING);
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
									dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
										Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
										Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
										parityCombo.getSelectedIndex (),
										flowCombo.getSelectedIndex ()
										);
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
							if ( threads.decrementAndGet () == 0 )
							{
								updateStatusLabel (STATUS.READY);
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
				flowCombo.getSelectedIndex ()
				);
			new RawCommunicator (dt, this, sync).setVisible (true);
			dt.close ();
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "MainWindow.RawCommunnicator.start");	// NOI18N
		}
	}//GEN-LAST:event_rawButActionPerformed

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
						firmware.setText (defFirmware);
					}
				}
				else
				{
					firmware.setText (defFirmware);
				}
			}
		}
	}//GEN-LAST:event_portComboItemStateChanged

	private void putListInTable (final String ofWhat,
		final DefaultTableModel dtm,
		final Vector<PhoneElement> placeForData)
	{
		try
		{
			final CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(portCombo.getSelectedItem ().toString ());
			updateStatusLabel (STATUS.RECEIVING);
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
							dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
								Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
								Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
								parityCombo.getSelectedIndex (),
								flowCombo.getSelectedIndex ()
								);
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
					updateStatusLabel (STATUS.READY);
					Vector<PhoneElement> ret = null;
					try
					{
						ret = get ();
						if ( ret != null )
						{
							dtm.setRowCount (0);
							if ( placeForData != null )
							{
								placeForData.removeAllElements ();
								placeForData.addAll (ret);
							}
							for ( int i=0; i < ret.size (); i++ )
							{
								dtm.addRow (new String[]
									{ret.get (i).getFilename ()
									+ "." + ret.get (i).getExt () }	// NOI18N
									);
							}
						}
					}
					catch (Exception ex)
					{
						Utils.handleException (ex,
							"putListInTable.SW.done:"	// NOI18N
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

	/**
	 * Receives key-typed events (called when the user types a key).
	 * Used to react on pressing the Esc key.
	 * @param ke The key-typed event.
	 */
	public void keyTyped (KeyEvent ke)
	{
		if ( ke.getKeyChar () == KeyEvent.VK_ESCAPE )
		{
			dispose ();
		}
	}

	/**
	 * Receives key-pressed events (unused).
	 * @param ke The key-pressed event.
	 */
	public void keyPressed (KeyEvent ke) {}

	/**
	 * Receives key-released events (unused).
	 * @param ke The key-released event.
	 */
	public void keyReleased (KeyEvent ke) {}

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

	private enum STATUS
	{
		READY,
		SENDING,
		RECEIVING;
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

	private static int staticUpload (File f, String whichPort) throws Exception
	{
		if ( f == null ) return -7;
		if ( ! f.exists () || ! f.canRead () || ! f.isFile ()) return -8;
		if ( ! f.getName ().contains (".") ) return -9;	// NOI18N
		if ( ! Utils.filetypeIDs.containsKey (f.getName ().substring
			(f.getName ().lastIndexOf (".")+1)	// NOI18N
			.toLowerCase ()))
		{
			System.out.println (unsuppType+" '"	// NOI18N
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
		// firmware version pattern
		verPattern = Pattern.compile ("\\s*\\+KPSV:\\s*(.+)");

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
					System.exit (0);
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
					System.exit (0);
				}
				else if ( args[i].toLowerCase ().equals ("--version")	// NOI18N
					|| args[i].toLowerCase ().equals ("-v") )	// NOI18N
				{
					System.out.println ("JYMAG " + verWord + " " + verString);	// NOI18N
					System.exit (0);
				}
				else if ( args[i].toLowerCase ().equals ("--port"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						portName = args[i+1];
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--parity"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						// <none, even, odd, space, mark>
						if ( args[i+1].toLowerCase ().equals ("none"))	// NOI18N
						{
							parity = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("even"))	// NOI18N
						{
							parity = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("odd"))	// NOI18N
						{
							parity = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("space"))	// NOI18N
						{
							parity = 3;
						}
						else if ( args[i+1].toLowerCase ().equals ("mark"))	// NOI18N
						{
							parity = 4;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--databits"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						try
						{
							dBits = Integer.parseInt (args[i+1]);
						}
						catch (Exception ex)
						{
							Utils.handleException (ex,
								"Integer.parseInt:'" + args[i+1] + "'");	// NOI18N
						}
						if ( dBits != 5 && dBits != 6
							&& dBits != 7 && dBits != 8 )
						{
							dBits = 8;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--speed"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						try
						{
							speed = Integer.parseInt (args[i+1]);
						}
						catch (Exception ex)
						{
							Utils.handleException (ex,
								"Integer.parseInt:'" + args[i+1] + "'");	// NOI18N
						}
						if ( speed != 1200
							&& speed != 2400
							&& speed != 4800
							&& speed != 9600
							&& speed != 19200
							&& speed != 38400
							&& speed != 57600
							&& speed != 115200
							&& speed != 230400
							&& speed != 460800
							&& speed != 500000
							&& speed != 576000
							&& speed != 921600
							&& speed != 1000000
							&& speed != 1152000
							&& speed != 1500000
							&& speed != 2000000
							&& speed != 2500000
							&& speed != 3000000
							&& speed != 3500000
							&& speed != 4000000
							)
						{
							speed = 115200;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--stopbits"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						if ( args[i+1].toLowerCase ().equals ("2"))	// NOI18N
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
				else if ( args[i].toLowerCase ().equals ("--flow"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						// <none,soft,hard>
						if ( args[i+1].toLowerCase ().equals ("none"))	// NOI18N
						{
							flow = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("soft"))	// NOI18N
						{
							flow = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("hard"))	// NOI18N
						{
							flow = 2;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--scan"))	// NOI18N
				{
					closeProgram (scanPorts ());
				}
				else if ( args[i].toLowerCase ().equals ("--upload"))	// NOI18N
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
				else if ( args[i].toLowerCase ().equals ("--download-all-photos"))	// NOI18N
				{
					closeProgram (getAllPics ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-ringtones"))	// NOI18N
				{
					closeProgram (getAllRings ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-todo"))	// NOI18N
				{
					closeProgram (getAllTODOs ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-events"))	// NOI18N
				{
					closeProgram (getAllEvents ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-vcards"))	// NOI18N
				{
					closeProgram (getAllVcards ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all"))	// NOI18N
				{
					closeProgram (getAllPics ()
						+ getAllRings ()
						+ getAllTODOs ()
						+ getAllEvents ()
						+ getAllVcards ()
						);
				}
				else if ( args[i].toLowerCase ().equals ("--download-dir"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						destDirName = args[i+1];
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--delete-after-download"))	// NOI18N
				{
					deleteAfterDownload = true;
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
                        public void run ()
                        {
                                new MainWindow ().setVisible (true);
                        }
                });
        }	// main ()

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton aboutBut;
        private javax.swing.JPanel addrBookPane;
        private javax.swing.JTable addrTable;
        private javax.swing.JLabel bpsLabel;
        private javax.swing.JComboBox dataBitsCombo;
        private javax.swing.JLabel databitsLabel;
        private javax.swing.JButton deleteAddrBut;
        private javax.swing.JButton deleteEventBut;
        private javax.swing.JButton deletePhotoBut;
        private javax.swing.JButton deleteRingBut;
        private javax.swing.JButton deleteTodoBut;
        private javax.swing.JButton downloadAddrBut;
        private javax.swing.JButton downloadEventBut;
        private javax.swing.JButton downloadPhotoBut;
        private javax.swing.JButton downloadRingBut;
        private javax.swing.JButton downloadTodoBut;
        private javax.swing.JPanel eventPane;
        private javax.swing.JTable eventTable;
        private javax.swing.JLabel firmware;
        private javax.swing.JLabel firmwareLabel;
        private javax.swing.JComboBox flowCombo;
        private javax.swing.JLabel flowLabel;
        private javax.swing.JButton getAddrListBut;
        private javax.swing.JButton getEventListBut;
        private javax.swing.JButton getPhotoListBut;
        private javax.swing.JButton getRingListBut;
        private javax.swing.JButton getTodoListBut;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JScrollPane jScrollPane5;
        private javax.swing.JComboBox parityCombo;
        private javax.swing.JLabel parityLabel;
        private javax.swing.JPanel photoPane;
        private javax.swing.JTable photoTable;
        private javax.swing.JComboBox portCombo;
        private javax.swing.JLabel portLabel;
        private javax.swing.JButton rawBut;
        private javax.swing.JPanel ringPane;
        private javax.swing.JTable ringTable;
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
        private javax.swing.JButton uploadEventBut;
        private javax.swing.JButton uploadPhotoBut;
        private javax.swing.JButton uploadRingBut;
        private javax.swing.JButton uploadTodoBut;
        // End of variables declaration//GEN-END:variables

}
