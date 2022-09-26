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
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the main window of the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MainWindow extends javax.swing.JFrame
{
	private static final long serialVersionUID = 65L;
	private static final String verString = "0.3";
	private Vector<PhoneElement> currentElements;
	private Vector<PhoneElement> currentRingElements;
	private Vector<PhoneElement> currentPhotoElements;
	private Vector<PhoneElement> currentAddrElements;
	private Vector<PhoneElement> currentTodoElements;
	private Vector<PhoneElement> currentEventElements;

	/**
	 * A pattern describing the format of lists of elements received from
	 * the phone.
	 */
	public static Pattern listPattern;
	/**A pattern describing the format of JPG pictures received from the phone. */
	public static Pattern photoContentsPattern;
	/**A pattern describing the format of MIDI ringtones received from the phone. */
	public static Pattern midiContentsPattern;
	/**A pattern describing the format of AMR ringtones received from the phone. */
	public static Pattern amrContentsPattern;
	/**A pattern describing the format of unknown contents received from the phone. */
	public static Pattern generalContentsPattern;
	/**A pattern describing the format of BMP pictures received from the phone. */
	public static Pattern bmpContentsPattern;
	/**A pattern describing the format of GIF pictures received from the phone. */
	public static Pattern gifContentsPattern;
	/**A pattern describing the format of PNG pictures received from the phone. */
	public static Pattern pngContentsPattern;
	/**A pattern describing the format of WAV pictures received from the phone. */
	public static Pattern wavContentsPattern;
	/**A pattern describing the format of vCalendar objects received from the phone. */
	public static Pattern vCalContentsPattern;
	/**A pattern describing the format of vCard objects received from the phone. */
	public static Pattern vCardContentsPattern;

	/**
	 * A Hashtable containing file ID numbers connected to the given file
	 * extensions, used for file uploading.
	 */
	public  static Hashtable<String, Integer> filetypeIDs;
	private static Hashtable<String, Integer> photofileIDs;
	private static Hashtable<String, Integer> ringfileIDs;
	private static Hashtable<String, Integer> addrfileIDs;
	private static Hashtable<String, Integer> todofileIDs;
	private static Hashtable<String, Integer> eventfileIDs;

	private String lastSelDir;

	// ------------ i18n stuff
	private static String noAnsString = "No answers received";
	private static String errString = "Error";
	private static String multiAnsString = "Multiple answers received. Select the correct port.";
	private static String whichString = "Which one?";
	private static String exOverString = " exists. Overwrite?";
	private static String suppOperThank = "Supported opertations:\n" +
			"1) Getting lists of: pictures, ringtones, addressbook entries, to-do tasks and events\n" +
			"2) Downloading:\n" +
			"   a) pictures: JPG, GIF, BMP, PNG\n" +
			"   b) ringtones: MIDI, AMR, WAV\n" +
			"   c) addressbook entries (vCards)\n" +
			"   d) to-do tasks\n" +
			"   e) events (reminders, ...)\n" +
			"3) Uploading:\n" +
			"   a) pictures: JPG, GIF, BMP, PNG\n" +
			"   b) ringtones: MIDI, WAV (IMA ADPCM, 8000Hz 16bit Mono?)\n" +
			"\n" +
			"Run with --help for a list of command-line options.\n\n" +
			"Thanks to:";
	private static String aboutStr = "About";
	private static String freeMPASStr = "a Free 'My Pictures and Sounds' replacement.";
	private static String verWord = "Version";
	private static String picsString = "Pictures";
	private static String soundsString = "Sounds";
	private static String addrString = "Addressbook files";
	private static String todoString = "To-do files";
	private static String eventString = "Event files";
	private static String getListStr = "Getting list of ";
	private static String getFileStr = "Getting file";
	private static String unsuppType = "Unsupported file type:";
	private static String tryPortStr = "Trying port ";
	private static String gotAnsStr = "Got answer!";
	private static String noAnsStr = "No answers received";
	private static String progIntroStr = "is a program " +
		"for retrieving and sending multimedia from and to a Sagem mobile phone.";
	private static String rxtxReqStr = "You need the RxTx Java Transmission package from www.rxtx.org";
	private static String cmdLineStr = "Command-line options:\n" +
		"--help, -h, -?, /?\t- display help\n" +
		"--licence, --license\t- display license information\n" +
		"--version, -v\t\t- display version\n" +
		"--download-dir <dir>\t- set the default directory\n" +
		"--flow <none,soft,hard>\t- set the flow control mode\n" +
		"--port <filename>\t- set the default port\n" +
		"--parity <none,even,odd,space,mark>\t- set the parity mode\n" +
		"--databits <5,6,7,8>\t- set the number of data bits\n" +
		"--speed <1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200,\n" +
		"\t230400, 460800, 500000, 576000, 921600, 1000000, 1152000,\n" +
		"\t1500000, 2000000, 2500000, 3000000, 3500000, 4000000>\n" +
		"\t\t\t- set the port speed\n" +
		"--stopbits <1,1.5,2>\t- set the number of stop bits\n" +
		"--upload <filename>\t- upload the given file to the phone and exit\n" +
		"--scan\t\t\t- scan available ports for \"OK\" answers and exit\n" +
		"--download-all-photos\t- download all photos from the phone and exit\n" +
		"--download-all-ringtones - download all ringtones from the phone and exit\n" +
		"--download-all-todo\t- download all to-do tasks from the phone and exit\n" +
		"--download-all-events\t- download all events from the phone and exit\n" +
		"--download-all-vcards\t- download all addressbook entries from the phone and exit\n" +
		"--download-all\t\t- combine all \"download\" options and exit\n" +
		"\nJYMAG exits with 0 code if command-line operation" +
		" (download, upload or scan) was successful.";

	// error messages for file upload:
	private static String msg1 = "File upload init failed";
	private static String msg2 = "Sending file name's length failed";
	private static String msg3 = "Sending file name failed";
	private static String msg4 = "Format not suported by phone";
	private static String msg5 = "File not accepted (too big?)";
	private static String msg6 = "Closing transmission failed";
	private static String msg7 = "Format not suported by JYMAG";
	// ------------

	// read from the command-line:
	private static String destDirName;
	private static int dBits = 8;
	private static float sBits = 1;
	private static int speed = 115200;
	private static int flow = 0;
	private static int parity = 0;
	private static String portName;

	/** Creates new form MainWindow */
        public MainWindow ()
        {
		initComponents ();
                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

		while (portList.hasMoreElements ())
		{
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				this.portCombo.addItem (id.getName ());
				if ( portName != null )
				{
					if ( id.getName ().equals (portName) )
					{
						this.portCombo.setSelectedItem (id.getName ());
					}
				}
			}
		}
		this.dataBitsCombo.setSelectedItem (String.valueOf (dBits));
		this.stopBitsCombo.setSelectedItem (String.valueOf (sBits));
		this.speedCombo.setSelectedItem (String.valueOf (speed));
		this.flowCombo.setSelectedIndex (flow);
		this.parityCombo.setSelectedIndex (parity);
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
                getPhotoListButton = new javax.swing.JButton();
                downloadPhotoBut = new javax.swing.JButton();
                uploadPhotoBut = new javax.swing.JButton();
                ringPane = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                ringTable = new javax.swing.JTable();
                getRingListbut = new javax.swing.JButton();
                downloadRingBut = new javax.swing.JButton();
                uploadRingBut = new javax.swing.JButton();
                addrBookPane = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                addrTable = new javax.swing.JTable();
                getAddrListBut = new javax.swing.JButton();
                downloadAddrBut = new javax.swing.JButton();
                uploadAddrBut = new javax.swing.JButton();
                todoPane = new javax.swing.JPanel();
                jScrollPane4 = new javax.swing.JScrollPane();
                todoTable = new javax.swing.JTable();
                getTodoListBut = new javax.swing.JButton();
                downloadTodoBut = new javax.swing.JButton();
                uploadTodoBut = new javax.swing.JButton();
                eventPane = new javax.swing.JPanel();
                jScrollPane5 = new javax.swing.JScrollPane();
                eventTable = new javax.swing.JTable();
                getEventList = new javax.swing.JButton();
                downloadEventBut = new javax.swing.JButton();
                uploadEventBut = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                jLabel3 = new javax.swing.JLabel();
                jLabel4 = new javax.swing.JLabel();
                jLabel5 = new javax.swing.JLabel();
                jLabel6 = new javax.swing.JLabel();
                flowCombo = new javax.swing.JComboBox();
                aboutBut = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Your Music and Graphics");

                speedCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000" }));
                speedCombo.setSelectedIndex(7);

                dataBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "7", "6", "5" }));

                parityCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Even", "Odd", "Space", "Mark" }));

                stopBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

                scanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/SCAN5.png"))); // NOI18N
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

                getPhotoListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/list.png"))); // NOI18N
                getPhotoListButton.setText("Get list");
                getPhotoListButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getPhotoListButtonActionPerformed(evt);
                        }
                });

                downloadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12A.png"))); // NOI18N
                downloadPhotoBut.setText("Download selected");
                downloadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12D.png"))); // NOI18N
                uploadPhotoBut.setText("Upload");
                uploadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadPhotoButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout photoPaneLayout = new javax.swing.GroupLayout(photoPane);
                photoPane.setLayout(photoPaneLayout);
                photoPaneLayout.setHorizontalGroup(
                        photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(photoPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addGroup(photoPaneLayout.createSequentialGroup()
                                                .addComponent(getPhotoListButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadPhotoBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadPhotoBut)))
                                .addContainerGap())
                );
                photoPaneLayout.setVerticalGroup(
                        photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, photoPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getPhotoListButton)
                                        .addComponent(downloadPhotoBut)
                                        .addComponent(uploadPhotoBut))
                                .addContainerGap())
                );

                tabPane.addTab("Photos", photoPane);

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

                getRingListbut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/list.png"))); // NOI18N
                getRingListbut.setText("Get list");
                getRingListbut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getRingListbutActionPerformed(evt);
                        }
                });

                downloadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12A.png"))); // NOI18N
                downloadRingBut.setText("Download selected");
                downloadRingBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadRingBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12D.png"))); // NOI18N
                uploadRingBut.setText("Upload");
                uploadRingBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadRingButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout ringPaneLayout = new javax.swing.GroupLayout(ringPane);
                ringPane.setLayout(ringPaneLayout);
                ringPaneLayout.setHorizontalGroup(
                        ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ringPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addGroup(ringPaneLayout.createSequentialGroup()
                                                .addComponent(getRingListbut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadRingBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadRingBut)))
                                .addContainerGap())
                );
                ringPaneLayout.setVerticalGroup(
                        ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ringPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getRingListbut)
                                        .addComponent(downloadRingBut)
                                        .addComponent(uploadRingBut))
                                .addContainerGap())
                );

                tabPane.addTab("Ringtones", ringPane);

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

                getAddrListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/list.png"))); // NOI18N
                getAddrListBut.setText("Get list");
                getAddrListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getAddrListButActionPerformed(evt);
                        }
                });

                downloadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12A.png"))); // NOI18N
                downloadAddrBut.setText("Download selected");
                downloadAddrBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12D.png"))); // NOI18N
                uploadAddrBut.setText("Upload");
                uploadAddrBut.setEnabled(false);
                uploadAddrBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadAddrButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout addrBookPaneLayout = new javax.swing.GroupLayout(addrBookPane);
                addrBookPane.setLayout(addrBookPaneLayout);
                addrBookPaneLayout.setHorizontalGroup(
                        addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addrBookPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addGroup(addrBookPaneLayout.createSequentialGroup()
                                                .addComponent(getAddrListBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadAddrBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadAddrBut)))
                                .addContainerGap())
                );
                addrBookPaneLayout.setVerticalGroup(
                        addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addrBookPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getAddrListBut)
                                        .addComponent(downloadAddrBut)
                                        .addComponent(uploadAddrBut))
                                .addContainerGap())
                );

                tabPane.addTab("Addressbook", addrBookPane);

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

                getTodoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/list.png"))); // NOI18N
                getTodoListBut.setText("Get list");
                getTodoListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getTodoListButActionPerformed(evt);
                        }
                });

                downloadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12A.png"))); // NOI18N
                downloadTodoBut.setText("Download selected");
                downloadTodoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12D.png"))); // NOI18N
                uploadTodoBut.setText("Upload");
                uploadTodoBut.setEnabled(false);
                uploadTodoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadTodoButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout todoPaneLayout = new javax.swing.GroupLayout(todoPane);
                todoPane.setLayout(todoPaneLayout);
                todoPaneLayout.setHorizontalGroup(
                        todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(todoPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addGroup(todoPaneLayout.createSequentialGroup()
                                                .addComponent(getTodoListBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadTodoBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadTodoBut)))
                                .addContainerGap())
                );
                todoPaneLayout.setVerticalGroup(
                        todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, todoPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getTodoListBut)
                                        .addComponent(downloadTodoBut)
                                        .addComponent(uploadTodoBut))
                                .addContainerGap())
                );

                tabPane.addTab("To do", todoPane);

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

                getEventList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/list.png"))); // NOI18N
                getEventList.setText("Get list");
                getEventList.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getEventListActionPerformed(evt);
                        }
                });

                downloadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12A.png"))); // NOI18N
                downloadEventBut.setText("Download selected");
                downloadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

                uploadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/ARROW12D.png"))); // NOI18N
                uploadEventBut.setText("Upload");
                uploadEventBut.setEnabled(false);
                uploadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadEventButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout eventPaneLayout = new javax.swing.GroupLayout(eventPane);
                eventPane.setLayout(eventPaneLayout);
                eventPaneLayout.setHorizontalGroup(
                        eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(eventPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addGroup(eventPaneLayout.createSequentialGroup()
                                                .addComponent(getEventList)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadEventBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadEventBut)))
                                .addContainerGap())
                );
                eventPaneLayout.setVerticalGroup(
                        eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eventPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getEventList)
                                        .addComponent(downloadEventBut)
                                        .addComponent(uploadEventBut))
                                .addContainerGap())
                );

                tabPane.addTab("Events", eventPane);

                jLabel1.setText("Port:");

                jLabel2.setText("Speed:");

                jLabel3.setText("Data bits:");

                jLabel4.setText("Parity:");

                jLabel5.setText("Stop bits:");

                jLabel6.setText("Flow control:");

                flowCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Software (XON/XOFF)", "Hardware (CTS/RTS)" }));

                aboutBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/QMARK8.png"))); // NOI18N
                aboutBut.setText("About...");
                aboutBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                aboutButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tabPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(aboutBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(scanButton)))
                                                        .addComponent(flowCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(7, 7, 7)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(flowCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel6)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(scanButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(aboutBut)))
                                .addGap(18, 18, 18)
                                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                .addContainerGap())
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void scanButtonActionPerformed (java.awt.event.ActionEvent evt)	{//GEN-FIRST:event_scanButtonActionPerformed

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
					DataTransporter dt = new DataTransporter (id);
					dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
						Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
						Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
						parityCombo.getSelectedIndex (),
						flowCombo.getSelectedIndex ()
						);
					if ( dt.test () == 0 )
					{
						active.add (id);
					}
					dt.close ();
				}
				catch (Exception ex) {}
			}
		}
		if (active.size () == 0)
		{
			JOptionPane.showMessageDialog (this, noAnsString, errString,
				JOptionPane.ERROR_MESSAGE);
		}
		else if (active.size () == 1)
		{
			portCombo.setSelectedItem (active.get (0).getName ());
		}
		else
		{
			Vector<String> names = new Vector<String> (active.size ());
			for ( int i=0; i < active.size (); i++ )
			{
				names.add ( active.get (i).getName () );
			}
			// let the user choose
			int res = JOptionPane.showOptionDialog (this,
				multiAnsString,
				whichString,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				names.toArray (),
				active.get (0).getName ());
			if ( res != JOptionPane.CLOSED_OPTION )
				this.portCombo.setSelectedIndex (res);
			else
				this.portCombo.setSelectedItem (active.get (0).getName ());
		}
	}//GEN-LAST:event_scanButtonActionPerformed

	private void getPhotoListButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPhotoListButtonActionPerformed

		this.currentPhotoElements = putListInTable ("PICTURES",
			(DefaultTableModel) this.photoTable.getModel ());
	}//GEN-LAST:event_getPhotoListButtonActionPerformed

	private void downloadButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButActionPerformed

		int[] selectedRows;
		if ( this.tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = this.photoTable.getSelectedRows ();
			this.currentElements = this.currentPhotoElements;
		}
		else if ( this.tabPane.getSelectedIndex () == 1 )
		{
			selectedRows = this.ringTable.getSelectedRows ();
			this.currentElements = this.currentRingElements;
		}
		else if ( this.tabPane.getSelectedIndex () == 2 )
		{
			selectedRows = this.addrTable.getSelectedRows ();
			this.currentElements = this.currentAddrElements;
		}
		else if ( this.tabPane.getSelectedIndex () == 3 )
		{
			selectedRows = this.todoTable.getSelectedRows ();
			this.currentElements = this.currentTodoElements;
		}

		else //if ( this.tabPane.getSelectedIndex () == 4 )
		{
			selectedRows = this.eventTable.getSelectedRows ();
			this.currentElements = this.currentEventElements;
		}
		if ( selectedRows == null ) return;
		if ( selectedRows.length == 0 ) return;

		JFileChooser jfc = new JFileChooser ();
		jfc.setMultiSelectionEnabled (false);
		jfc.setDialogType (JFileChooser.OPEN_DIALOG);
		jfc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		if ( destDirName != null )
		{
			if ( destDirName.length () > 0 )
			{
				File d = new File (destDirName);
				jfc.setCurrentDirectory (d);
				jfc.setSelectedFile (d);
			}
		}
		if ( lastSelDir != null )
		{
			File d = new File (lastSelDir);
			jfc.setCurrentDirectory (d);
			jfc.setSelectedFile (d);
		}
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File destDir = jfc.getSelectedFile ();
				lastSelDir = destDir.getAbsolutePath ();
				CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(this.portCombo.getSelectedItem ().toString ());
				DataTransporter dt = new DataTransporter (id);
				dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
					Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
					Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
					parityCombo.getSelectedIndex (),
					flowCombo.getSelectedIndex ()
					);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					File received = new File (
						destDir.getAbsolutePath () + File.separator
						+ currentElements.get (selectedRows[i]).getFilename ()
						+ "." + currentElements.get (selectedRows[i]).getExt ());
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (this,
							currentElements.get (selectedRows[i]).getFilename ()
							+ "." + currentElements.get (selectedRows[i]).getExt () +
							exOverString);
						if ( res != JOptionPane.YES_OPTION ) continue;
					}
					dt.getFile (received, currentElements.get (selectedRows[i]));
				}
				dt.close ();
			}
			catch (Exception ex)
			{
				System.out.println (ex);
				ex.printStackTrace ();
			}
		}
	}//GEN-LAST:event_downloadButActionPerformed

	private void getRingListbutActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRingListbutActionPerformed

		this.currentRingElements = putListInTable ("RINGTONES",
			(DefaultTableModel) this.ringTable.getModel ());
	}//GEN-LAST:event_getRingListbutActionPerformed

	private void aboutButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButActionPerformed

		JOptionPane.showMessageDialog (this,
			"JYMAG - Java Your Music and Graphics - " + freeMPASStr + "\n" +
			verWord + ": " + verString + "\n" +
			"Author: Bogdan Drozdowski, bogdandr@op.pl\n" +
			"See http://rudy.mif.pg.gda.pl/~bogdro/soft, http://rudy.mif.pg.gda.pl/~bogdro/inne\n" +
			"License: GPLv3+\n\n" +
			suppOperThank +
			"\nSharp (sharpy+at+xox.pl) for GetPic.pl\n"+
			"sebtx452 @ gmail.com for wxPicSound\n" +
			"MIKSOFT for \"Mobile Media Converter\"\n" +
			"\"ffmpeg project\" for ffmpeg",
			aboutStr, JOptionPane.INFORMATION_MESSAGE );

	}//GEN-LAST:event_aboutButActionPerformed

	private void uploadPhotoButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadPhotoButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))
				{
					if ( photofileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
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
				if ( f.getName ().contains ("."))
				{
					if ( ringfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadRingButActionPerformed

	private void dynamicUpload (File f)
	{
		try
		{
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(this.portCombo.getSelectedItem ().toString ());
			DataTransporter dt = new DataTransporter (id);
			dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
				Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
				Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				flowCombo.getSelectedIndex ()
				);

			int put = dt.putFile (f, f.getName ().substring
				(0, f.getName ().indexOf (".")).replaceAll ("\\s", ""));

			dt.close ();
			if ( put != 0 )
			{
				String msg = String.valueOf (put);
				if ( put == -1 ) msg = msg1;
				else if ( put == -2 ) msg = msg2;
				else if ( put == -3 ) msg = msg3;
				else if ( put == -4 ) msg = msg4;
				else if ( put == -5 ) msg = msg5;
				else if ( put == -6 ) msg = msg6;
				else if ( put == -7 ) msg = msg7;

				JOptionPane.showMessageDialog (this,
				errString + ": " + msg,
				errString, JOptionPane.ERROR_MESSAGE );
			}
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
	}

	private void getAddrListButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getAddrListButActionPerformed

		this.currentAddrElements = putListInTable ("VCARDS",
			(DefaultTableModel) this.addrTable.getModel ());
	}//GEN-LAST:event_getAddrListButActionPerformed

	private void getTodoListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getTodoListButActionPerformed

		this.currentTodoElements = putListInTable ("VTODO",
			(DefaultTableModel) this.todoTable.getModel ());
	}//GEN-LAST:event_getTodoListButActionPerformed

	private void getEventListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getEventListActionPerformed

		this.currentEventElements = putListInTable ("VEVENT",
			(DefaultTableModel) this.eventTable.getModel ());
	}//GEN-LAST:event_getEventListActionPerformed

	private void uploadAddrButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAddrButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))
				{
					if ( addrfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
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
				if ( f.getName ().contains ("."))
				{
					if ( todofileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
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
				if ( f.getName ().contains ("."))
				{
					if ( eventfileIDs.containsKey (f.getName ().substring
						(f.getName ().lastIndexOf (".")+1)
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private Vector<PhoneElement> putListInTable (String ofWhat, DefaultTableModel dtm)
	{
		try
		{
			Vector<PhoneElement> ret;
			dtm.setRowCount (0);
			CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
				(this.portCombo.getSelectedItem ().toString ());
			DataTransporter dt = new DataTransporter (id);
			dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
				Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
				Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				flowCombo.getSelectedIndex ()
				);
			ret = dt.getList (ofWhat);
			dt.close ();
			if ( ret != null )
			{
				for ( int i=0; i < ret.size (); i++ )
				{
					dtm.addRow (new String[] {ret.get (i).getFilename ()
						+ "." + ret.get (i).getExt () });
				}
			}
			return ret;
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
			return null;
		}
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

					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
					{
						portName = id.getName ();
					}
				}
			}
			if ( destDirName == null )
			{
				destDirName = ".";
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
						+ "." + elems.get (i).getExt ());
					System.out.println (getFileStr + " '"
						+ elems.get (i).getFilename ()
						+ "." + elems.get (i).getExt ()
						+ "'");
					int res = dt.getFile (received, elems.get (i));
					if ( res != 0 )
					{
						ret = res;
					}
				}
			}
			dt.close ();
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
		return ret;
	}

	private static int getAllPics ()
	{
		return getElementsOfType ("PICTURES");
	}

	private static int getAllRings ()
	{
		return getElementsOfType ("RINGTONES");
	}

	private static int getAllTODOs ()
	{
		return getElementsOfType ("VTODO");
	}

	private static int getAllEvents ()
	{
		return getElementsOfType ("VEVENT");
	}

	private static int getAllVcards ()
	{
		return getElementsOfType ("VCARDS");
	}

	private static int upload (File f, String whichPort) throws Exception
	{
		if ( f == null ) return -7;
		if ( ! f.exists () || ! f.canRead () || ! f.isFile ()) return -8;
		if ( ! f.getName ().contains (".") ) return -9;
		if ( ! filetypeIDs.containsKey (f.getName ().substring
			(f.getName ().lastIndexOf (".")+1)
			.toLowerCase ()))
		{
			System.out.println (unsuppType+" '"
				+f.getName ().substring
				(f.getName ().lastIndexOf (".")+1)
				.toLowerCase () + "'");
			return -10;
		}

		if ( whichPort == null )
		{
	                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

			while (portList.hasMoreElements ())
			{
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
				{
					whichPort = id.getName ();
				}
			}
		}
		CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
			(whichPort);
		DataTransporter dt = new DataTransporter (id);
		dt.open (speed, dBits, sBits, parity, flow);

		int put = dt.putFile (f, f.getName ().substring
			(0, f.getName ().indexOf (".")).replaceAll ("\\s", ""));

		dt.close ();
		if ( put != 0 )
		{
			String msg = String.valueOf (put);
			if ( put == -1 ) msg = msg1;
			else if ( put == -2 ) msg = msg2;
			else if ( put == -3 ) msg = msg3;
			else if ( put == -4 ) msg = msg4;
			else if ( put == -5 ) msg = msg5;
			else if ( put == -6 ) msg = msg6;
			else if ( put == -7 ) msg = msg7;

			System.out.println ( errString+": " + msg );
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

			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				// scan ports for "AT"-"OK"
				System.out.print (tryPortStr + id.getName () + "...");
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
				catch (Exception ex) {}
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
		/*
		 * List receiving format:
		 * +KPSL: "5303650005022001FFFF",1,2016,"PICTURES","FGIF","0000065535","","Zzz"
				Id	    HIDDEN,LENG, CATEGORY, CONTENT, LOCATION  FLAG, NAME
		 */
		listPattern = Pattern.compile ("\\s*\\+KPSL:\\s*\\" + '"'
				+ "([0-9A-Fa-f]+)" + "\\" + '"' + ",(\\d+),\\d+,\\" + '"'
				+ "\\w+\\" + '"' + ",\\" + '"' + "(\\w+)\\"+ '"'
				+ ",[^,]+,[^,]+,\\"+ '"' + "([^\"]+)\\" + '"');
		/*
		 * Element receiving format:
		 * AT+KPSR="<ID repeated>"
		 * +KPSR: 49203
		 * CONNECT
		 * <binary data>
		 * NO CARRIER
		 */
		String jpegHeader = new String (new byte[] { (byte) 0xff, (byte) 0xd8, (byte) 0xff} );
		photoContentsPattern = Pattern.compile
			(".*" + jpegHeader + ".*(" + jpegHeader + ".*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		String midiHeader = new String (new byte[] { (byte) 0x4d, (byte) 0x54, (byte) 0x68} );
		midiContentsPattern = Pattern.compile
			(".*(" + midiHeader + ".*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		amrContentsPattern = Pattern.compile
			(".*(#!AMR.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		generalContentsPattern = Pattern.compile
			(".*CONNECT\\s(.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		bmpContentsPattern = Pattern.compile
			(".*(BM.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		gifContentsPattern = Pattern.compile
			(".*(GIF.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		String pngHeader = new String (new byte[] { (byte) 0x89, (byte) 0x50,
			(byte) 0x4E, (byte) 0x47} );
		pngContentsPattern = Pattern.compile
			(".*(" + pngHeader + ".*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		wavContentsPattern = Pattern.compile
			(".*(RIFF.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		vCalContentsPattern = Pattern.compile
			(".*(BEGIN:VCALENDAR.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE
			//| Pattern.MULTILINE
			);
		vCardContentsPattern = Pattern.compile
			(".*(BEGIN:vCard.*)\\s+NO\\s+CARRIER\\s*",
			Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE
			//| Pattern.MULTILINE
			);

		ringfileIDs = new Hashtable<String, Integer> (6);
		// seem to do something: 201-202, 205-207
		// rejected: 3-12, 17, 20-22, 30-32, 40-42, 50-52, 60-62,
		//	70-72, 80-82, 90-101, 106-111, 200, 203-204, 208-2012
		//	120-122
		ringfileIDs.put ("wav" ,   1);
//		ringfileIDs.put ("amr" ,   212);
		ringfileIDs.put ("mid" ,   2);
		ringfileIDs.put ("midi",   2);
		//ringfileIDs.put ("rmi" ,   2);	// ?
		//ringfileIDs.put ("kar" ,   2);	// ?
//		ringfileIDs.put ("imy" ,   4);	// ?

		photofileIDs = new Hashtable<String, Integer> (6);
		photofileIDs.put ("bmp" , 102);
		photofileIDs.put ("png" , 103);
		photofileIDs.put ("jpg" , 104);
		photofileIDs.put ("jpeg", 104);
		photofileIDs.put ("jpe" , 104);
		photofileIDs.put ("gif" , 105);

		addrfileIDs = new Hashtable<String, Integer> (2);
		// vCard: not 201, 202, 205-207
		//addrfileIDs.put  ("vcf"  , 207);
		//addrfileIDs.put  ("vcard", 207);

		todofileIDs = new Hashtable<String, Integer> (4);
		// vTODO: not 201, 202, 205-207
		//todofileIDs.put  ("ics" , 207);
		//todofileIDs.put  ("ical", 207);
		//todofileIDs.put  ("ifb" , 207);
		//todofileIDs.put  ("icalendar" , 207);

		eventfileIDs = new Hashtable<String, Integer> (todofileIDs.size ());
		// vEvent: not 201, 202, 205-207
		eventfileIDs.putAll (todofileIDs);

		filetypeIDs = new Hashtable<String, Integer> (ringfileIDs.size ()
			+ photofileIDs.size () + addrfileIDs.size ()
			+ todofileIDs.size () + eventfileIDs.size ());
		filetypeIDs.putAll (photofileIDs);
		filetypeIDs.putAll (ringfileIDs);
		filetypeIDs.putAll (addrfileIDs);
		filetypeIDs.putAll (todofileIDs);
		filetypeIDs.putAll (eventfileIDs);
		// TODO: AMR, mp3?, vCard, vTODO, vEvent

		if ( args != null )
		{
			for ( int i=0; i < args.length; i++ )
			{
				if ( args[i].toLowerCase ().equals ("--help")
					|| args[i].toLowerCase ().equals ("-h")
					|| args[i].toLowerCase ().equals ("-?")
					|| args[i].toLowerCase ().equals ("/?") )
				{
					System.out.println ("JYMAG (Java Your Music and Graphics) "+ progIntroStr +
						"\n\n*** " + rxtxReqStr +" ***\n\n" +
						"Author: Bogdan Drozdowski, bogdandr @ op . pl\n" +
						"License: GPLv3+\n" +
				                "See http://rudy.mif.pg.gda.pl/~bogdro/soft\n\n" +
				                cmdLineStr
						);
					System.exit (0);
				}
				else if ( args[i].toLowerCase ().equals ("--license")
					|| args[i].toLowerCase ().equals ("--licence") )
				{
					System.out.println ("JYMAG (Java Your Music and Graphics) "+ progIntroStr +
						"\nSee http://rudy.mif.pg.gda.pl/~bogdro/soft\n" +
						"Author: Bogdan 'bogdro' Drozdowski, bogdandr @ op . pl.\n\n" +
						"    This program is free software; you can redistribute it and/or\n" +
						"    modify it under the terms of the GNU General Public License\n" +
						"    as published by the Free Software Foundation; either version 3\n" +
						"    of the License, or (at your option) any later version.\n\n" +
						"    This program is distributed in the hope that it will be useful,\n" +
						"    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
						"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
						"    GNU General Public License for more details.\n\n" +
						"    You should have received a copy of the GNU General Public License\n" +
						"    along with this program; if not, write to the Free Software Foundation:\n" +
						"               Free Software Foundation\n" +
						"               51 Franklin Street, Fifth Floor\n" +
						"               Boston, MA 02110-1301\n" +
						"               USA\n");
					System.exit (0);
				}
				else if ( args[i].toLowerCase ().equals ("--version")
					|| args[i].toLowerCase ().equals ("-v") )
				{
					System.out.println ("JYMAG " + verWord + " " + verString);
					System.exit (0);
				}
				else if ( args[i].toLowerCase ().equals ("--port"))
				{
					if ( i < args.length-1 )
					{
						portName = args[i+1];
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--parity"))
				{
					if ( i < args.length-1 )
					{
						// <none, even, odd, space, mark>
						if ( args[i+1].toLowerCase ().equals ("none"))
						{
							parity = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("even"))
						{
							parity = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("odd"))
						{
							parity = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("space"))
						{
							parity = 3;
						}
						else if ( args[i+1].toLowerCase ().equals ("mark"))
						{
							parity = 4;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--databits"))
				{
					if ( i < args.length-1 )
					{
						try
						{
							dBits = Integer.parseInt (args[i+1]);
						} catch (Exception ex) {}
						if ( dBits != 5 && dBits != 6
							&& dBits != 7 && dBits != 8 )
						{
							dBits = 8;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--speed"))
				{
					if ( i < args.length-1 )
					{
						try
						{
							speed = Integer.parseInt (args[i+1]);
						} catch (Exception ex) {}
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
				else if ( args[i].toLowerCase ().equals ("--stopbits"))
				{
					if ( i < args.length-1 )
					{
						if ( args[i+1].toLowerCase ().equals ("2"))
						{
							sBits = 2;
						}
						else if ( args[i+1].toLowerCase ().equals ("1.5")
							|| args[i+1].toLowerCase ().equals ("1,5") )
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
				else if ( args[i].toLowerCase ().equals ("--flow"))
				{
					if ( i < args.length-1 )
					{
						// <none,soft,hard>
						if ( args[i+1].toLowerCase ().equals ("none"))
						{
							flow = 0;
						}
						else if ( args[i+1].toLowerCase ().equals ("soft"))
						{
							flow = 1;
						}
						else if ( args[i+1].toLowerCase ().equals ("hard"))
						{
							flow = 2;
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--scan"))
				{
					System.exit (scanPorts ());
				}
				else if ( args[i].toLowerCase ().equals ("--upload"))
				{
					if ( i < args.length-1 )
					{
						try
						{
							int res = upload ( new File (args[i+1]), portName );
							System.exit (res);
						}
						catch ( Exception ex )
						{
							System.out.println (ex);
							ex.printStackTrace ();
						}
						i++;
					}
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-photos"))
				{
					System.exit (getAllPics ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-ringtones"))
				{
					System.exit (getAllRings ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-todo"))
				{
					System.exit (getAllTODOs ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-events"))
				{
					System.exit (getAllEvents ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-vcards"))
				{
					System.exit (getAllVcards ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all"))
				{
					System.exit (getAllPics ()
						+ getAllRings ()
						+ getAllTODOs ()
						+ getAllEvents ()
						+ getAllVcards ()
						);
				}
				else if ( args[i].toLowerCase ().equals ("--download-dir"))
				{
					if ( i < args.length-1 )
					{
						destDirName = args[i+1];
						i++;
					}
				}
			}	// for i
		}
		try
		{
			UIManager.setLookAndFeel (
				UIManager.getSystemLookAndFeelClassName ());
		}
		catch (Exception ex) {}

		//JFrame.setDefaultLookAndFeelDecorated (true);

                SwingUtilities.invokeLater (new Runnable ()
                {
                        public void run ()
                        {
                                new MainWindow ().setVisible (true);
                        }
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton aboutBut;
        private javax.swing.JPanel addrBookPane;
        private javax.swing.JTable addrTable;
        private javax.swing.JComboBox dataBitsCombo;
        private javax.swing.JButton downloadAddrBut;
        private javax.swing.JButton downloadEventBut;
        private javax.swing.JButton downloadPhotoBut;
        private javax.swing.JButton downloadRingBut;
        private javax.swing.JButton downloadTodoBut;
        private javax.swing.JPanel eventPane;
        private javax.swing.JTable eventTable;
        private javax.swing.JComboBox flowCombo;
        private javax.swing.JButton getAddrListBut;
        private javax.swing.JButton getEventList;
        private javax.swing.JButton getPhotoListButton;
        private javax.swing.JButton getRingListbut;
        private javax.swing.JButton getTodoListBut;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JScrollPane jScrollPane5;
        private javax.swing.JComboBox parityCombo;
        private javax.swing.JPanel photoPane;
        private javax.swing.JTable photoTable;
        private javax.swing.JComboBox portCombo;
        private javax.swing.JPanel ringPane;
        private javax.swing.JTable ringTable;
        private javax.swing.JButton scanButton;
        private javax.swing.JComboBox speedCombo;
        private javax.swing.JComboBox stopBitsCombo;
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
