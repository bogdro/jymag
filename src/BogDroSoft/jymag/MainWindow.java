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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	implements KeyListener
{
	private static final long serialVersionUID = 65L;
	private static final String verString = "0.4";	// NOI18N
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
	private static String suppOperThank = "Supported opertations:"+
		"\n" +	// NOI18N
		"1) Getting lists of: pictures, ringtones, addressbook entries, to-do tasks and events"+
		"\n" +	// NOI18N
		"2) Downloading:"+
		"\n" +	// NOI18N
		"   a) pictures:"+
		" JPG, GIF, BMP, PNG\n" +	// NOI18N
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

	// error messages for file staticUpload:
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
	private static boolean deleteAfterDownload = false;

	/**
	 * Creates new form MainWindow.
	 */
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

        	this.aboutBut.addKeyListener (this);
        	this.addrBookPane.addKeyListener (this);
        	this.addrTable.addKeyListener (this);
        	this.dataBitsCombo.addKeyListener (this);
        	this.deleteAddrBut.addKeyListener (this);
        	this.deleteEventBut.addKeyListener (this);
        	this.deletePhotoBut.addKeyListener (this);
        	this.deleteRingBut.addKeyListener (this);
        	this.deleteTodoBut.addKeyListener (this);
        	this.downloadAddrBut.addKeyListener (this);
        	this.downloadEventBut.addKeyListener (this);
        	this.downloadPhotoBut.addKeyListener (this);
        	this.downloadRingBut.addKeyListener (this);
        	this.downloadTodoBut.addKeyListener (this);
        	this.eventPane.addKeyListener (this);
        	this.eventTable.addKeyListener (this);
        	this.flowCombo.addKeyListener (this);
        	this.getAddrListBut.addKeyListener (this);
        	this.getEventListBut.addKeyListener (this);
        	this.getPhotoListBut.addKeyListener (this);
        	this.getRingListBut.addKeyListener (this);
        	this.getTodoListBut.addKeyListener (this);
        	this.jScrollPane1.addKeyListener (this);
        	this.jScrollPane2.addKeyListener (this);
        	this.jScrollPane3.addKeyListener (this);
        	this.jScrollPane4.addKeyListener (this);
        	this.jScrollPane5.addKeyListener (this);
        	this.parityCombo.addKeyListener (this);
        	this.photoPane.addKeyListener (this);
        	this.photoTable.addKeyListener (this);
        	this.portCombo.addKeyListener (this);
        	this.rawBut.addKeyListener (this);
        	this.ringPane.addKeyListener (this);
        	this.ringTable.addKeyListener (this);
        	this.scanButton.addKeyListener (this);
        	this.speedCombo.addKeyListener (this);
        	this.stopBitsCombo.addKeyListener (this);
        	this.tabPane.addKeyListener (this);
        	this.todoPane.addKeyListener (this);
        	this.todoTable.addKeyListener (this);
        	this.uploadAddrBut.addKeyListener (this);
        	this.uploadEventBut.addKeyListener (this);
        	this.uploadPhotoBut.addKeyListener (this);
        	this.uploadRingBut.addKeyListener (this);
        	this.uploadTodoBut.addKeyListener (this);
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

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Your Music and Graphics");

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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(photoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getPhotoListBut)
                                        .addComponent(downloadPhotoBut)
                                        .addComponent(uploadPhotoBut)
                                        .addComponent(deletePhotoBut))
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
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getRingListBut)
                                        .addComponent(downloadRingBut)
                                        .addComponent(uploadRingBut)
                                        .addComponent(deleteRingBut))
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
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(addrBookPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getAddrListBut)
                                        .addComponent(downloadAddrBut)
                                        .addComponent(uploadAddrBut)
                                        .addComponent(deleteAddrBut))
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
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(todoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getTodoListBut)
                                        .addComponent(downloadTodoBut)
                                        .addComponent(uploadTodoBut)
                                        .addComponent(deleteTodoBut))
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
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(eventPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getEventListBut)
                                        .addComponent(downloadEventBut)
                                        .addComponent(uploadEventBut)
                                        .addComponent(deleteEventBut))
                                .addContainerGap())
                );

                tabPane.addTab("Events", eventPane);

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

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tabPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
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
                                                                        .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(flowLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(stopbitsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(parityLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
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
                                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(rawBut)
                                .addGap(18, 18, 18)
                                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
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

	private void getPhotoListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPhotoListButActionPerformed

		this.currentPhotoElements = putListInTable ("PICTURES",	// NOI18N
			(DefaultTableModel) this.photoTable.getModel ());
	}//GEN-LAST:event_getPhotoListButActionPerformed

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
						+ "." 	// NOI18N
						+ currentElements.get (selectedRows[i]).getExt ());
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (this,
							currentElements.get (selectedRows[i]).getFilename ()
							+ "." 	// NOI18N
							+ currentElements.get (selectedRows[i]).getExt () +
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

	private void getRingListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRingListButActionPerformed

		this.currentRingElements = putListInTable ("RINGTONES",	// NOI18N
			(DefaultTableModel) this.ringTable.getModel ());
	}//GEN-LAST:event_getRingListButActionPerformed

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
					if ( photofileIDs.containsKey (f.getName ().substring
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
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( ringfileIDs.containsKey (f.getName ().substring
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
				(0, f.getName ().indexOf (".")).replaceAll ("\\s", ""));	// NOI18N

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
				errString + ": " + msg,	// NOI18N
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

		this.currentAddrElements = putListInTable ("VCARDS",	// NOI18N
			(DefaultTableModel) this.addrTable.getModel ());
	}//GEN-LAST:event_getAddrListButActionPerformed

	private void getTodoListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getTodoListButActionPerformed

		this.currentTodoElements = putListInTable ("VTODO",	// NOI18N
			(DefaultTableModel) this.todoTable.getModel ());
	}//GEN-LAST:event_getTodoListButActionPerformed

	private void getEventListButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getEventListButActionPerformed

		this.currentEventElements = putListInTable ("VEVENT",	// NOI18N
			(DefaultTableModel) this.eventTable.getModel ());
	}//GEN-LAST:event_getEventListButActionPerformed

	private void uploadAddrButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAddrButActionPerformed

		JFileChooser jfc = new JFileChooser ();
		jfc.setFileFilter ( new javax.swing.filechooser.FileFilter ()
		{
			public boolean accept ( File f )
			{
				if ( f.isDirectory () ) return true;
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( addrfileIDs.containsKey (f.getName ().substring
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
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( todofileIDs.containsKey (f.getName ().substring
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
				if ( f.getName ().contains ("."))	// NOI18N
				{
					if ( eventfileIDs.containsKey (f.getName ().substring
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
		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = jfc.getSelectedFile ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private void deleteButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButActionPerformed

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
					dt.deleteFile (currentElements.get (selectedRows[i]));
				}
				dt.close ();
			}
			catch (Exception ex)
			{
				System.out.println (ex);
				ex.printStackTrace ();
			}
		}
	}//GEN-LAST:event_deleteButActionPerformed

	private void rawButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rawButActionPerformed

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
			new RawCommunicator (dt, this).setVisible (true);
			dt.close ();
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
	}//GEN-LAST:event_rawButActionPerformed

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
						+ "." + ret.get (i).getExt () });	// NOI18N
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
			System.out.println (ex);
			ex.printStackTrace ();
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
		if ( ! filetypeIDs.containsKey (f.getName ().substring
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
			(0, f.getName ().indexOf (".")).replaceAll ("\\s", ""));	// NOI18N

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

			System.out.println ( errString+": " + msg );	// NOI18N
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
	 * Receives key-typed events (called when the user types a key).
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
		listPattern = Pattern.compile ("\\s*\\+KPSL:\\s*\\" + '"'	// NOI18N
				+ "([0-9A-Fa-f]+)" + "\\" + '"' + ",(\\d+),\\d+,\\" + '"'	// NOI18N
				+ "\\w+\\" + '"' + ",\\" + '"' + "(\\w+)\\"+ '"'	// NOI18N
				+ ",[^,]+,[^,]+,\\"+ '"' + "([^\"]+)\\" + '"');	// NOI18N
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
			(".*" + jpegHeader + ".*(" + jpegHeader + ".*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		String midiHeader = new String (new byte[] { (byte) 0x4d, (byte) 0x54, (byte) 0x68} );
		midiContentsPattern = Pattern.compile
			(".*(" + midiHeader + ".*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		amrContentsPattern = Pattern.compile
			(".*(#!AMR.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		generalContentsPattern = Pattern.compile
			(".*CONNECT\\s(.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		bmpContentsPattern = Pattern.compile
			(".*(BM.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		gifContentsPattern = Pattern.compile
			(".*(GIF.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		String pngHeader = new String (new byte[] { (byte) 0x89, (byte) 0x50,
			(byte) 0x4E, (byte) 0x47} );
		pngContentsPattern = Pattern.compile
			(".*(" + pngHeader + ".*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		wavContentsPattern = Pattern.compile
			(".*(RIFF.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			//| Pattern.MULTILINE
			);
		vCalContentsPattern = Pattern.compile
			(".*(BEGIN:VCALENDAR.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE
			//| Pattern.MULTILINE
			);
		vCardContentsPattern = Pattern.compile
			(".*(BEGIN:vCard.*)\\s+NO\\s+CARRIER\\s*",	// NOI18N
			Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE
			//| Pattern.MULTILINE
			);

		ringfileIDs = new Hashtable<String, Integer> (6);
		// seem to do something: 201-202, 205-207
		// rejected: 3-12, 17, 20-22, 30-32, 40-42, 50-52, 60-62,
		//	70-72, 80-82, 90-101, 106-111, 200, 203-204, 208-2012
		//	120-122
		ringfileIDs.put ("wav" ,   1);	// NOI18N
//		ringfileIDs.put ("amr" ,   212);	// NOI18N
		ringfileIDs.put ("mid" ,   2);	// NOI18N
		ringfileIDs.put ("midi",   2);	// NOI18N
		//ringfileIDs.put ("rmi" ,   2);	// ?	// NOI18N
		//ringfileIDs.put ("kar" ,   2);	// ?	// NOI18N
//		ringfileIDs.put ("imy" ,   4);	// ?	// NOI18N

		photofileIDs = new Hashtable<String, Integer> (6);
		photofileIDs.put ("bmp" , 102);	// NOI18N
		photofileIDs.put ("png" , 103);	// NOI18N
		photofileIDs.put ("jpg" , 104);	// NOI18N
		photofileIDs.put ("jpeg", 104);	// NOI18N
		photofileIDs.put ("jpe" , 104);	// NOI18N
		photofileIDs.put ("gif" , 105);	// NOI18N

		addrfileIDs = new Hashtable<String, Integer> (2);
		// vCard: not 201, 202, 205-207
		//addrfileIDs.put  ("vcf"  , 207);	// NOI18N
		//addrfileIDs.put  ("vcard", 207);	// NOI18N

		todofileIDs = new Hashtable<String, Integer> (4);
		// vTODO: not 201, 202, 205-207
		//todofileIDs.put  ("ics" , 207);	// NOI18N
		//todofileIDs.put  ("ical", 207);	// NOI18N
		//todofileIDs.put  ("ifb" , 207);	// NOI18N
		//todofileIDs.put  ("icalendar" , 207);	// NOI18N

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
						} catch (Exception ex) {}
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
					System.exit (scanPorts ());
				}
				else if ( args[i].toLowerCase ().equals ("--upload"))	// NOI18N
				{
					if ( i < args.length-1 )
					{
						try
						{
							int res = staticUpload ( new File (args[i+1]), portName );
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
				else if ( args[i].toLowerCase ().equals ("--download-all-photos"))	// NOI18N
				{
					System.exit (getAllPics ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-ringtones"))	// NOI18N
				{
					System.exit (getAllRings ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-todo"))	// NOI18N
				{
					System.exit (getAllTODOs ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-events"))	// NOI18N
				{
					System.exit (getAllEvents ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all-vcards"))	// NOI18N
				{
					System.exit (getAllVcards ());
				}
				else if ( args[i].toLowerCase ().equals ("--download-all"))	// NOI18N
				{
					System.exit (getAllPics ()
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
