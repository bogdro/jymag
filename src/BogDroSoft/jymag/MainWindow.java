/*
 * MainWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2011 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * This class represents the main window of the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 65L;
	private final MainWindow mw = this;

	/** Current version number as a String. */
	public static final String verString = "1.1";	// NOI18N
	private Vector<PhoneElement> currentRingElements;
	private Vector<PhoneElement> currentPhotoElements;
	private Vector<PhoneElement> currentAddrElements;
	private Vector<PhoneElement> currentTodoElements;
	private Vector<PhoneElement> currentEventElements;
	private Vector<PhoneElement> currentAnimElements;
	private Vector<PhoneElement> currentJavaElements;
	private Vector<PhoneAlarm>   currentAlarmElements;
	private Vector<PhoneMessage> currentMessageElements;

	private String lastSelDir;
	// synchronization variable:
	private static final Object sync = new Object ();
	// port-firmware pairs and the firmware version pattern, used for displaying:
	private volatile Hashtable<String, String> firmwares;
	private volatile Hashtable<String, String> phoneTypes;
	private volatile Hashtable<String, String> phoneIMEIs;
	private volatile Hashtable<String, String> phoneSubsNums;

	private static String logFile = "jymag.log";	// NOI18N

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
	private static final String noAnsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("No_answers_received");
	private static final String errString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Error");
	private static final String multiAnsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Multiple_answers");
	private static final String whichString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Which_one");
	private static final String exOverString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("_exists._Overwrite");
	private static final String overwriteStr = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Overwrite?");
	private static final String picsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_pictures");
	private static final String soundsString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_sounds");
	private static final String addrString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_addressbook_files");
	private static final String todoString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_to-do_files");
	private static final String eventString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_event_and_task_files");
	private static final String animString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_animation/video_files");
	private static final String javaString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Supported_Java_files");
	private static final String deleteQuestion = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("want_to_delete");
	private static final String questionString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Question");
	private static final String fileNotWriteMsg = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Cant_write_to_file");

	private static final String pressScanMsg = "(" + java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("(press_Scan)") + ")";	// NOI18N

	// ------------ static variables for command-line

	// read from the command-line:
	private static volatile String destDirName;
	private static volatile int dBits = 8;
	private static volatile float sBits = 1;
	private static volatile int speed = 115200;
	private static volatile int flow = 0;
	private static volatile int parity = 0;
	private static volatile String portName = null;
	private static volatile boolean isMax = false;
	private static volatile int x = 0;
	private static volatile int y = 0;

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

		destDirName = CommandLineParser.getDstDirName ();
		dBits = CommandLineParser.getDBits ();
		sBits = CommandLineParser.getSBits ();
		speed = CommandLineParser.getSpeed ();
		flow = CommandLineParser.getFlowMode ();
		parity = CommandLineParser.getParityMode ();
		portName = CommandLineParser.getPortName ();
		isMax = CommandLineParser.isMax ();
		x = CommandLineParser.getX ();
		y = CommandLineParser.getY ();

		initComponents ();

		setTitle (getTitle () + " " + verString);	// NOI18N
		fontSizeSpin.setValue (fontSizeSpin.getValue ());	// refresh the font in the window
		firmware.setText (pressScanMsg);
		phone.setText (pressScanMsg);
		IMEI.setText (pressScanMsg);
		subsNum.setText (pressScanMsg);
		Utils.updateStatusLabel (status, Utils.STATUS.READY);
		fontSizeLab.setHorizontalAlignment (JLabel.RIGHT);

		Vector<String> portList = TransferUtils.getSerialPortNames ();
		if ( portList != null )
		{
			for ( String s : portList )
			{
				portCombo.addItem (s);
			}
		}
		updateControls ();

		photoTable.setFillsViewportHeight (true);
		photoTable.setDragEnabled (true);
		photoTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		ringTable.setFillsViewportHeight (true);
		ringTable.setDragEnabled (true);
		ringTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		addrTable.setFillsViewportHeight (true);
		addrTable.setDragEnabled (true);
		addrTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		todoTable.setFillsViewportHeight (true);
		todoTable.setDragEnabled (true);
		todoTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		eventTable.setFillsViewportHeight (true);
		eventTable.setDragEnabled (true);
		eventTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		animTable.setFillsViewportHeight (true);
		animTable.setDragEnabled (true);
		animTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		javaTable.setFillsViewportHeight (true);
		javaTable.setDragEnabled (true);
		javaTable.setTransferHandler (new JYMAGTransferHandler (portCombo, speedCombo,
			dataBitsCombo, stopBitsCombo, parityCombo, flowSoft,
			flowHard, sync, this));

		/* Make clicking on the table header select all the table's rows */
		photoTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (photoTable));
		ringTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (ringTable));
		addrTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (addrTable));
		todoTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (todoTable));
		eventTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (eventTable));
		animTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (animTable));
		javaTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (javaTable));
		alarmTable.getTableHeader ().addMouseListener (new Utils.TableMouseListener (alarmTable));

		/* add the Esc key listener to the frame and all components. */
		new Utils.EscKeyListener (this);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {
                bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

                jScrollPane15 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                loadConfBut = new javax.swing.JButton();
                phone = new javax.swing.JLabel();
                speedCombo = new javax.swing.JComboBox();
                speedLabel = new javax.swing.JLabel();
                parityLabel = new javax.swing.JLabel();
                aboutBut = new javax.swing.JButton();
                flowSoft = new javax.swing.JCheckBox();
                progressLabel = new javax.swing.JLabel();
                rawBut = new javax.swing.JButton();
                firmwareLabel = new javax.swing.JLabel();
                statusLabel = new javax.swing.JLabel();
                scanButton = new javax.swing.JButton();
                flowLabel = new javax.swing.JLabel();
                databitsLabel = new javax.swing.JLabel();
                portLabel = new javax.swing.JLabel();
                getCapBut = new javax.swing.JButton();
                bpsLabel = new javax.swing.JLabel();
                IMEI = new javax.swing.JLabel();
                exitBut = new javax.swing.JButton();
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
                jScrollPane16 = new javax.swing.JScrollPane();
                alarmPane = new javax.swing.JPanel();
                jScrollPane17 = new javax.swing.JScrollPane();
                alarmTable = new javax.swing.JTable();
                getAlarmListBut = new javax.swing.JButton();
                downloadAlarmBut = new javax.swing.JButton();
                uploadAlarmBut = new javax.swing.JButton();
                deleteAlarmBut = new javax.swing.JButton();
                jScrollPane18 = new javax.swing.JScrollPane();
                smsPane = new javax.swing.JPanel();
                jScrollPane19 = new javax.swing.JScrollPane();
                smsTable = new javax.swing.JTable();
                getSmsListBut = new javax.swing.JButton();
                downloadSmsBut = new javax.swing.JButton();
                uploadSmsBut = new javax.swing.JButton();
                deleteSmsBut = new javax.swing.JButton();
                phoneTypeLabel = new javax.swing.JLabel();
                flowHard = new javax.swing.JCheckBox();
                dataBitsCombo = new javax.swing.JComboBox();
                stopbitsLabel = new javax.swing.JLabel();
                IMEILabel = new javax.swing.JLabel();
                saveConfBut = new javax.swing.JButton();
                portCombo = new javax.swing.JComboBox();
                subsNumLabel = new javax.swing.JLabel();
                firmware = new javax.swing.JLabel();
                stopBitsCombo = new javax.swing.JComboBox();
                parityCombo = new javax.swing.JComboBox();
                subsNum = new javax.swing.JLabel();
                fontSizeSpin = new javax.swing.JSpinner();
                fontSizeLab = new javax.swing.JLabel();
                signalButton = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Your Music and Graphics"); // NOI18N
                setIconImage((new javax.swing.ImageIcon (getClass ().getResource ("/BogDroSoft/jymag/rsrc/jymag.png"))).getImage ());
                addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent evt) {
                                formWindowClosing(evt);
                        }
                });

                loadConfBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/loadconf.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                loadConfBut.setText(bundle.getString("Load_configuration")); // NOI18N
                loadConfBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                loadConfButActionPerformed(evt);
                        }
                });

                phone.setText("-"); // NOI18N

                speedCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000" }));
                speedCombo.setSelectedIndex(7);

                speedLabel.setText(bundle.getString("Speed:")); // NOI18N

                parityLabel.setText(bundle.getString("Parity:")); // NOI18N

                aboutBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/QMARK8.png"))); // NOI18N
                aboutBut.setText(bundle.getString("About_JYMAG...")); // NOI18N
                aboutBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                aboutButActionPerformed(evt);
                        }
                });

                flowSoft.setText(bundle.getString("Software_(XON/XOFF)")); // NOI18N

                progressLabel.setText(bundle.getString("Progress:")); // NOI18N

                rawBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/tools1.png"))); // NOI18N
                rawBut.setText(bundle.getString("Manual_commands")); // NOI18N
                rawBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                rawButActionPerformed(evt);
                        }
                });

                status.setForeground(new java.awt.Color(0, 204, 0));
                status.setText("READY"); // NOI18N

                firmwareLabel.setText(bundle.getString("Firmware:")); // NOI18N

                statusLabel.setText(bundle.getString("Program_status:")); // NOI18N

                scanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/SCAN5.png"))); // NOI18N
                scanButton.setText(bundle.getString("Scan_ports")); // NOI18N
                scanButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                scanButtonActionPerformed(evt);
                        }
                });

                flowLabel.setText(bundle.getString("Flow_control:")); // NOI18N

                databitsLabel.setText(bundle.getString("Data_bits:")); // NOI18N

                portLabel.setText(bundle.getString("Port:")); // NOI18N

                getCapBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/capab.png"))); // NOI18N
                getCapBut.setText(bundle.getString("Get_capabilities")); // NOI18N
                getCapBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getCapButActionPerformed(evt);
                        }
                });

                bpsLabel.setText("bps"); // NOI18N

                IMEI.setText("-"); // NOI18N

                exitBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/exit.png"))); // NOI18N
                exitBut.setText(bundle.getString("Exit")); // NOI18N
                exitBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exitButActionPerformed(evt);
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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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

                javaPane.setBackground(new java.awt.Color(122, 189, 255));

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
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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

                alarmPane.setBackground(java.awt.Color.pink);

                binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), alarmPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
                bindingGroup.addBinding(binding);
                binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), alarmPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
                bindingGroup.addBinding(binding);
                binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), alarmPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
                bindingGroup.addBinding(binding);

                alarmTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_number"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_date"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_time"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_days")
                        }
                ) {
                        private static final long serialVersionUID = 76L;
                        Class[] types = new Class [] {
                                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean [] {
                                false, true, true, true
                        };

                        public Class getColumnClass(int columnIndex) {
                                return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit [columnIndex];
                        }
                }
        );
        jScrollPane17.setViewportView(alarmTable);

        getAlarmListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getAlarmListBut.setText(bundle.getString("Get_list")); // NOI18N
        getAlarmListBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        getAlarmListButActionPerformed(evt);
                }
        });

        downloadAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadAlarmBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadAlarmBut.setEnabled(false);
        downloadAlarmBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        downloadButActionPerformed(evt);
                }
        });

        uploadAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadAlarmBut.setText(bundle.getString("Update_sel")); // NOI18N
        uploadAlarmBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        uploadAlarmButActionPerformed(evt);
                }
        });

        deleteAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteAlarmBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteAlarmBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        deleteButActionPerformed(evt);
                }
        });

        javax.swing.GroupLayout alarmPaneLayout = new javax.swing.GroupLayout(alarmPane);
        alarmPane.setLayout(alarmPaneLayout);
        alarmPaneLayout.setHorizontalGroup(
                alarmPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(alarmPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(alarmPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                .addGroup(alarmPaneLayout.createSequentialGroup()
                                        .addComponent(getAlarmListBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(downloadAlarmBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(uploadAlarmBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deleteAlarmBut)))
                        .addContainerGap())
        );
        alarmPaneLayout.setVerticalGroup(
                alarmPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alarmPaneLayout.createSequentialGroup()
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alarmPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(downloadAlarmBut)
                                .addComponent(uploadAlarmBut)
                                .addComponent(deleteAlarmBut)
                                .addComponent(getAlarmListBut))
                        .addContainerGap())
        );

        getAlarmListBut.getAccessibleContext().setAccessibleName(bundle.getString("get_list_of_alarms")); // NOI18N
        downloadAlarmBut.getAccessibleContext().setAccessibleName(bundle.getString("download_alarms")); // NOI18N
        uploadAlarmBut.getAccessibleContext().setAccessibleName(bundle.getString("update_alarms")); // NOI18N
        deleteAlarmBut.getAccessibleContext().setAccessibleName(bundle.getString("delete_alarms")); // NOI18N

        jScrollPane16.setViewportView(alarmPane);

        tabPane.addTab(bundle.getString("Alarms"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/alarmclk.png")), jScrollPane16); // NOI18N

        smsPane.setBackground(new java.awt.Color(234, 187, 0));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${maximumSize}"), smsPane, org.jdesktop.beansbinding.BeanProperty.create("maximumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${minimumSize}"), smsPane, org.jdesktop.beansbinding.BeanProperty.create("minimumSize"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, photoPane, org.jdesktop.beansbinding.ELProperty.create("${preferredSize}"), smsPane, org.jdesktop.beansbinding.BeanProperty.create("preferredSize"));
        bindingGroup.addBinding(binding);

        smsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("smsTable_ID"), java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("smsTable_Status"), java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("smsTable_PhoneNum"), java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("smsTable_DateTime"), java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("smsTable_message")
                }
        ) {
                private static final long serialVersionUID = 79L;
                Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                        false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                }
        });
        jScrollPane19.setViewportView(smsTable);

        getSmsListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
        getSmsListBut.setText(bundle.getString("Get_list")); // NOI18N
        getSmsListBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        getSmsListButActionPerformed(evt);
                }
        });

        downloadSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
        downloadSmsBut.setText(bundle.getString("Download_selected")); // NOI18N
        downloadSmsBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        downloadSmsButActionPerformed(evt);
                }
        });

        uploadSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        uploadSmsBut.setText(bundle.getString("Upload")); // NOI18N
        uploadSmsBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        uploadSmsButActionPerformed(evt);
                }
        });

        deleteSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
        deleteSmsBut.setText(bundle.getString("Delete_selected")); // NOI18N
        deleteSmsBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        deleteButActionPerformed(evt);
                }
        });

        javax.swing.GroupLayout smsPaneLayout = new javax.swing.GroupLayout(smsPane);
        smsPane.setLayout(smsPaneLayout);
        smsPaneLayout.setHorizontalGroup(
                smsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(smsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(smsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                .addGroup(smsPaneLayout.createSequentialGroup()
                                        .addComponent(getSmsListBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(downloadSmsBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(uploadSmsBut)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deleteSmsBut)))
                        .addContainerGap())
        );
        smsPaneLayout.setVerticalGroup(
                smsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, smsPaneLayout.createSequentialGroup()
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(smsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(getSmsListBut)
                                .addComponent(downloadSmsBut)
                                .addComponent(uploadSmsBut)
                                .addComponent(deleteSmsBut))
                        .addContainerGap())
        );

        getSmsListBut.getAccessibleContext().setAccessibleName(bundle.getString("Get_list_of_sms")); // NOI18N
        downloadSmsBut.getAccessibleContext().setAccessibleName(bundle.getString("Download_selected_sms")); // NOI18N
        uploadSmsBut.getAccessibleContext().setAccessibleName(bundle.getString("Upload_sms")); // NOI18N
        deleteSmsBut.getAccessibleContext().setAccessibleName(bundle.getString("Delete_selected_sms")); // NOI18N

        jScrollPane18.setViewportView(smsPane);

        tabPane.addTab(bundle.getString("SMS"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/sms.png")), jScrollPane18); // NOI18N

        phoneTypeLabel.setText(bundle.getString("Phone_type:")); // NOI18N

        flowHard.setText(bundle.getString("Hardware_(RTS/CTS)")); // NOI18N

        dataBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "7", "6", "5" }));

        stopbitsLabel.setText(bundle.getString("Stop_bits:")); // NOI18N

        IMEILabel.setText("IMEI:"); // NOI18N

        saveConfBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/saveconf.png"))); // NOI18N
        saveConfBut.setText(bundle.getString("Save_configuration")); // NOI18N
        saveConfBut.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        saveConfButActionPerformed(evt);
                }
        });

        portCombo.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        portComboItemStateChanged(evt);
                }
        });

        subsNumLabel.setText(bundle.getString("Subscriber_number")); // NOI18N

        firmware.setText("-"); // NOI18N

        stopBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

        parityCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Even", "Odd", "Space", "Mark" }));

        subsNum.setText("-"); // NOI18N

        fontSizeSpin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
        fontSizeSpin.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        fontSizeSpinStateChanged(evt);
                }
        });

        fontSizeLab.setText(bundle.getString("Font_size")); // NOI18N

        signalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/antenna.png"))); // NOI18N
        signalButton.setText(bundle.getString("signal_power")); // NOI18N
        signalButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        signalButtonActionPerformed(evt);
                }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(470, 470, 470)
                        .addComponent(fontSizeLab, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(365, 365, 365))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(816, Short.MAX_VALUE)
                        .addComponent(signalButton)
                        .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(816, Short.MAX_VALUE)
                        .addComponent(exitBut)
                        .addContainerGap())
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(portLabel)
                                                                        .addComponent(speedLabel)
                                                                        .addComponent(databitsLabel))
                                                                .addGap(12, 12, 12)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                                .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(bpsLabel))
                                                                                        .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(36, 36, 36)
                                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(parityLabel)
                                                                                        .addComponent(stopbitsLabel)))
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(flowLabel)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(flowSoft)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(flowHard))))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(phoneTypeLabel)
                                                                        .addComponent(firmwareLabel)
                                                                        .addComponent(IMEILabel)
                                                                        .addComponent(subsNumLabel)
                                                                        .addComponent(loadConfBut)
                                                                        .addComponent(statusLabel)
                                                                        .addComponent(progressLabel))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(status)
                                                                        .addComponent(subsNum)
                                                                        .addComponent(IMEI)
                                                                        .addComponent(phone)
                                                                        .addComponent(firmware)
                                                                        .addComponent(saveConfBut)
                                                                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                                                                .addGap(30, 30, 30)))
                                                .addGap(46, 46, 46)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(getCapBut)
                                                        .addComponent(rawBut)
                                                        .addComponent(aboutBut)
                                                        .addComponent(scanButton))))
                                .addContainerGap()))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {aboutBut, exitBut, getCapBut, rawBut, scanButton, signalButton});

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fontSizeLab))
                        .addGap(171, 171, 171)
                        .addComponent(signalButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitBut)
                        .addContainerGap(394, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(parityLabel)
                                                                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(7, 7, 7)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(stopbitsLabel)
                                                                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(portLabel)
                                                                        .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(7, 7, 7)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(speedLabel)
                                                                        .addComponent(speedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(bpsLabel))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(databitsLabel)
                                                                        .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(flowLabel)
                                                                        .addComponent(flowSoft)
                                                                        .addComponent(flowHard))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(loadConfBut)
                                                        .addComponent(saveConfBut))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(phoneTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(firmwareLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(IMEILabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(subsNumLabel))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(phone)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(firmware)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(IMEI)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(subsNum)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(statusLabel)
                                                        .addComponent(status))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(progressLabel)
                                                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(scanButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(aboutBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rawBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(getCapBut)))
                                .addGap(29, 29, 29)
                                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                                .addContainerGap()))
        );

        loadConfBut.getAccessibleContext().setAccessibleName(bundle.getString("loadconf_button")); // NOI18N
        phone.getAccessibleContext().setAccessibleName(bundle.getString("phone_type")); // NOI18N
        speedCombo.getAccessibleContext().setAccessibleName(bundle.getString("port_speed")); // NOI18N
        speedLabel.getAccessibleContext().setAccessibleName(bundle.getString("speed_label")); // NOI18N
        parityLabel.getAccessibleContext().setAccessibleName(bundle.getString("parity_label")); // NOI18N
        aboutBut.getAccessibleContext().setAccessibleName(bundle.getString("about_button")); // NOI18N
        flowSoft.getAccessibleContext().setAccessibleName(bundle.getString("flowsoft_label")); // NOI18N
        progressLabel.getAccessibleContext().setAccessibleName(bundle.getString("pbar_label")); // NOI18N
        rawBut.getAccessibleContext().setAccessibleName(bundle.getString("mancomm_button")); // NOI18N
        status.getAccessibleContext().setAccessibleName(bundle.getString("program_status")); // NOI18N
        firmwareLabel.getAccessibleContext().setAccessibleName(bundle.getString("firmware_label")); // NOI18N
        statusLabel.getAccessibleContext().setAccessibleName(bundle.getString("status_label")); // NOI18N
        scanButton.getAccessibleContext().setAccessibleName(bundle.getString("scan_button")); // NOI18N
        flowLabel.getAccessibleContext().setAccessibleName(bundle.getString("flow_label")); // NOI18N
        databitsLabel.getAccessibleContext().setAccessibleName(bundle.getString("dbits_label")); // NOI18N
        portLabel.getAccessibleContext().setAccessibleName(bundle.getString("port_label")); // NOI18N
        progressBar.getAccessibleContext().setAccessibleName(bundle.getString("operation_progress")); // NOI18N
        getCapBut.getAccessibleContext().setAccessibleName(bundle.getString("capab_button")); // NOI18N
        IMEI.getAccessibleContext().setAccessibleName(bundle.getString("phone_imei")); // NOI18N
        exitBut.getAccessibleContext().setAccessibleName(bundle.getString("exit_button")); // NOI18N
        tabPane.getAccessibleContext().setAccessibleName(bundle.getString("tabPane")); // NOI18N
        phoneTypeLabel.getAccessibleContext().setAccessibleName(bundle.getString("ptype_label")); // NOI18N
        flowHard.getAccessibleContext().setAccessibleName(bundle.getString("flowhard_label")); // NOI18N
        dataBitsCombo.getAccessibleContext().setAccessibleName(bundle.getString("data_bits")); // NOI18N
        stopbitsLabel.getAccessibleContext().setAccessibleName(bundle.getString("sbits_label")); // NOI18N
        IMEILabel.getAccessibleContext().setAccessibleName(bundle.getString("imei_label")); // NOI18N
        saveConfBut.getAccessibleContext().setAccessibleName(bundle.getString("saveconf_button")); // NOI18N
        portCombo.getAccessibleContext().setAccessibleName(bundle.getString("port_list")); // NOI18N
        subsNumLabel.getAccessibleContext().setAccessibleName(bundle.getString("subnum_label")); // NOI18N
        firmware.getAccessibleContext().setAccessibleName(bundle.getString("phone_firmware")); // NOI18N
        stopBitsCombo.getAccessibleContext().setAccessibleName(bundle.getString("stop_bits")); // NOI18N
        parityCombo.getAccessibleContext().setAccessibleName(bundle.getString("parity_type")); // NOI18N
        subsNum.getAccessibleContext().setAccessibleName(bundle.getString("subscriber_number")); // NOI18N
        fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
        fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N
        fontSizeLab.getAccessibleContext().setAccessibleName(bundle.getString("font_label")); // NOI18N
        signalButton.getAccessibleContext().setAccessibleName(bundle.getString("signal_button")); // NOI18N

        jScrollPane15.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
        );

        bindingGroup.bind();

        pack();
        }// </editor-fold>//GEN-END:initComponents

	private void scanButtonActionPerformed (java.awt.event.ActionEvent evt)	{//GEN-FIRST:event_scanButtonActionPerformed

		// these MUST be read here, on the EDT
		final int cSpeed = Integer.parseInt (speedCombo.getSelectedItem ().toString ());
		final int cDataBits = Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ());
		final float cStopBits = Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ());
		final int cParity = parityCombo.getSelectedIndex ();
		final int cFlow = ((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0);

		Utils.updateStatusLabel (status, Utils.STATUS.SENDING);
		progressBar.setValue (0);
		progressBar.setMinimum (0);
		int max = 0;
		Vector<String> ports = TransferUtils.getSerialPortNames ();
		if ( ports != null ) max = ports.size ();
		progressBar.setMaximum (max);
		// always create new:
		firmwares = new Hashtable<String, String> (max);
		phoneTypes = new Hashtable<String, String> (max);
		phoneIMEIs = new Hashtable<String, String> (max);
		phoneSubsNums = new Hashtable<String, String> (max);

		SwingWorker<Vector<String>, Void> sw =
			new SwingWorker<Vector<String>, Void> ()
		{
			@Override
			protected Vector<String> doInBackground ()
			{
				Vector<String> active = new Vector<String> (10);
				TransferUtils.scanPorts (true, cSpeed, cDataBits, cStopBits,
					cParity, cFlow, firmwares, phoneTypes,
					phoneIMEIs, phoneSubsNums, active,
					new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							Utils.changeGUI (new Runnable ()
							{
								@Override
								public synchronized void run ()
								{
									progressBar.setValue (progressBar.getValue ()+1);
									if ( progressBar.getValue () == progressBar.getMaximum () )
									{
										progressBar.setValue (0);
									}
								}
							});
						}
					});
				return active;
			}

			@Override
			protected void done ()
			{
				try
				{
					Utils.updateStatusLabel (status, Utils.STATUS.READY);
					progressBar.setValue (0);
					Vector<String> active = get ();
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
						// clear the selection to make sure the listener gets called:
						portCombo.setSelectedItem (null);
						// NOT setSelectedIndex (0)!
						portCombo.setSelectedItem (active.get (0));

					}
					else
					{
						// let the user choose
						int res = JOptionPane.showOptionDialog (mw,
							multiAnsString,
							whichString,
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							active.toArray (),
							active.get (0));
						if ( res != JOptionPane.CLOSED_OPTION )
						{
							// clear the selection to make sure the listener gets called:
							portCombo.setSelectedIndex (-1);
							portCombo.setSelectedIndex (res);
						}
						else	// the user closed the window - choose the first one
						{
							// clear the selection to make sure the listener gets called:
							portCombo.setSelectedItem (null);
							// NOT setSelectedIndex (0)!
							portCombo.setSelectedItem (active.get (0));
						}
					}
				}
				catch (Exception ex)
				{
					Utils.updateStatusLabel (status, Utils.STATUS.READY);
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
		else return;
		final Vector<PhoneElement> currentElements = temp;
		if ( selectedRows == null || currentElements == null ) return;
		if ( selectedRows.length == 0 ) return;

		if ( downloadFC == null )
		{
			downloadFC = new JFileChooser ();
			downloadFC.setMultiSelectionEnabled (false);
			downloadFC.setDialogType (JFileChooser.SAVE_DIALOG);
			downloadFC.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
		}
		// destDirName is the static field
		if ( destDirName != null )
		{
			if ( ! destDirName.isEmpty () )
			{
				File d = new File (destDirName);
				downloadFC.setSelectedFile (d);
				downloadFC.setCurrentDirectory (d);
			}
		}
		if ( lastSelDir != null )
		{
			File d = new File (lastSelDir);
			//downloadFC.setSelectedFile (d);
			downloadFC.setCurrentDirectory (d);
		}

		int dialogRes = downloadFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			String dstFileName = null;
			try
			{
				File destDir = downloadFC.getSelectedFile ();
				if ( destDir == null ) return;
				lastSelDir = destDir.getAbsolutePath ();
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				final AtomicInteger threads = new AtomicInteger (0);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					dstFileName = destDir.getAbsolutePath () + File.separator
						+ currentElements.get (toGet).getFilename ()
						+ "." 	// NOI18N
						+ currentElements.get (toGet).getExt ();
					final File received = new File (dstFileName);
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (this,
							currentElements.get (toGet).getFilename ()
							+ "." 	// NOI18N
							+ currentElements.get (toGet).getExt () +
							" " +	// NOI18N
							exOverString, overwriteStr,
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
					Utils.updateStatusLabel (status, Utils.STATUS.RECEIVING);
					threads.incrementAndGet ();
					TransferUtils.downloadFile (received, currentElements.get (toGet),
						TransferUtils.getIdentifierForPort
						(portCombo.getSelectedItem ().toString ()),
						Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
						Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
						Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
						parityCombo.getSelectedIndex (),
						((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
						new Runnable ()
						{
							@Override
							public synchronized void run ()
							{
								int th = threads.decrementAndGet ();
								progressBar.setValue (progressBar.getValue () + 1);
								if ( th == 0 )
								{
									Utils.updateStatusLabel (status,
										Utils.STATUS.READY);
									progressBar.setValue (0);
								}
							}
						}, this, sync, false, false, false);
				} // for
			}
			catch (Exception ex)
			{
				Utils.updateStatusLabel (status, Utils.STATUS.READY);
				Utils.handleException (ex, "Download"	// NOI18N
					+ ((dstFileName != null)? ": " + dstFileName : ""));	// NOI18N
			}
		}

	}//GEN-LAST:event_downloadButActionPerformed

	private void aboutButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButActionPerformed

		try
		{
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
				fontSize = ((Number)val).floatValue ();
			new AboutBox (this, true, fontSize).setVisible (true);
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.AboutBox.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex2) {}
		}
	}//GEN-LAST:event_aboutButActionPerformed

	private void uploadPhotoButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadPhotoButActionPerformed

		if ( uploadPictFC == null )
		{
			uploadPictFC = Utils.createOpenFileChooser (picsString, Utils.photofileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadPictFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadPictFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadPictFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadPhotoButActionPerformed

	private void uploadRingButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadRingButActionPerformed

		if ( uploadRingFC == null )
		{
			uploadRingFC = Utils.createOpenFileChooser (soundsString, Utils.ringfileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadRingFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadRingFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadRingFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadRingButActionPerformed

	private void uploadAddrButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAddrButActionPerformed

		if ( uploadAddrFC == null )
		{
			uploadAddrFC = Utils.createOpenFileChooser (addrString, Utils.addrfileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadAddrFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadAddrFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadAddrFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAddrButActionPerformed

	private void uploadTodoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadTodoButActionPerformed

		if ( uploadTODOFC == null )
		{
			uploadTODOFC = Utils.createOpenFileChooser (todoString, Utils.todofileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadTODOFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadTODOFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadTODOFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadTodoButActionPerformed

	private void uploadEventButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadEventButActionPerformed

		if ( uploadEventFC == null )
		{
			uploadEventFC = Utils.createOpenFileChooser (eventString, Utils.eventfileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadEventFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadEventFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadEventFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private void uploadAnimButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAnimButActionPerformed

		if ( uploadAnimFC == null )
		{
			uploadAnimFC = Utils.createOpenFileChooser (animString, Utils.animfileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadAnimFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadAnimFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadAnimFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAnimButActionPerformed

	private void uploadJavaButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadJavaButActionPerformed

		if ( uploadJavaFC == null )
		{
			uploadJavaFC = Utils.createOpenFileChooser (javaString, Utils.javafileIDs);
		}

		//if ( lastSelDir != null )
		//	uploadJavaFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = uploadJavaFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadJavaFC.getSelectedFile ();
			if ( res == null ) return;
			lastSelDir = res.getParent ();
			dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadJavaButActionPerformed

	private void dynamicUpload (final File f)
	{
		try
		{
			Utils.updateStatusLabel (status, Utils.STATUS.SENDING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);

			TransferUtils.uploadFile (f, TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()),
				Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
				new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						Utils.updateStatusLabel (status, Utils.STATUS.READY);
						progressBar.setValue (0);
					}
				}, this, sync, false, false, false);
		}
		catch (Exception ex)
		{
			Utils.updateStatusLabel (status, Utils.STATUS.READY);
			Utils.handleException (ex, "dynUpload");	// NOI18N
		}
	}

	private void deleteButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButActionPerformed

		int[] selectedRows = null;
		Vector<PhoneElement> temp = null;
		Vector<PhoneAlarm> tempAlarms = null;
		Vector<PhoneMessage> tempMessages = null;
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
		else if ( tabPane.getSelectedIndex () == 7 )
		{
			selectedRows = alarmTable.getSelectedRows ();
			tempAlarms = currentAlarmElements;
		}
		else if ( tabPane.getSelectedIndex () == 8 )
		{
			selectedRows = smsTable.getSelectedRows ();
			tempMessages = currentMessageElements;
		}
		else return;
		if ( selectedRows == null || (temp == null
			&& tempAlarms == null && tempMessages == null) ) return;
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
				final AtomicInteger threads = new AtomicInteger (0);
				Utils.updateStatusLabel (status, Utils.STATUS.SENDING);
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				for ( int i=0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					if ( temp != null )
					{
						threads.incrementAndGet ();
						TransferUtils.deleteFile (temp.get (toGet),
							TransferUtils.getIdentifierForPort
							(portCombo.getSelectedItem ().toString ()),
							Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
							Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
							Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
							parityCombo.getSelectedIndex (),
							((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
							new Runnable ()
							{
								@Override
								public synchronized void run ()
								{
									progressBar.setValue (progressBar.getValue () + 1);
									if ( threads.decrementAndGet () == 0 )
									{
										Utils.updateStatusLabel (status,
											Utils.STATUS.READY);
										progressBar.setValue (0);
									}
								}
							}, this, sync, false, false, false);
					}
					else if ( tempAlarms != null )
					{
						threads.incrementAndGet ();
						TransferUtils.deleteAlarm (toGet+1,
							TransferUtils.getIdentifierForPort
							(portCombo.getSelectedItem ().toString ()),
							Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
							Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
							Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
							parityCombo.getSelectedIndex (),
							((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
							new Runnable ()
							{
								@Override
								public synchronized void run ()
								{
									progressBar.setValue (progressBar.getValue () + 1);
									if ( threads.decrementAndGet () == 0 )
									{
										Utils.updateStatusLabel (status,
											Utils.STATUS.READY);
										progressBar.setValue (0);
									}
								}
							}, this, sync, false, false, false);
					}
					else if ( tempMessages != null )
					{
						threads.incrementAndGet ();
						TransferUtils.deleteMessage (tempMessages.get (toGet),
							TransferUtils.getIdentifierForPort
							(portCombo.getSelectedItem ().toString ()),
							Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
							Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
							Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
							parityCombo.getSelectedIndex (),
							((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
							new Runnable ()
							{
								@Override
								public synchronized void run ()
								{
									progressBar.setValue (progressBar.getValue () + 1);
									if ( threads.decrementAndGet () == 0 )
									{
										Utils.updateStatusLabel (status,
											Utils.STATUS.READY);
										progressBar.setValue (0);
									}
								}
							}, this, sync, false, false, false);
					}
				}
			}
			catch (Exception ex)
			{
				Utils.updateStatusLabel (status, Utils.STATUS.READY);
				Utils.handleException (ex, "delete");	// NOI18N
			}
		}
	}//GEN-LAST:event_deleteButActionPerformed

	private void rawButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rawButActionPerformed

		try
		{
			DataTransporter dt = new DataTransporter (TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()));
			dt.open (Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
				fontSize = ((Number)val).floatValue ();
			new RawCommunicator (dt, this, sync, fontSize).setVisible (true);
			dt.close ();
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.RawCommunnicator.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex2) {}
		}
	}//GEN-LAST:event_rawButActionPerformed

	private void getCapButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCapButActionPerformed

		try
		{
			DataTransporter dt = new DataTransporter (TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()));
			dt.open (Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
				fontSize = ((Number)val).floatValue ();
			new CapabilityWindow (dt, this, sync, fontSize).setVisible (true);
			dt.close ();
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.CapabilityWindow.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex2) {}
		}
	}//GEN-LAST:event_getCapButActionPerformed

	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

		dispose ();
		Utils.closeProgram (logFile, 0);
	}//GEN-LAST:event_formWindowClosing

	private void portComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_portComboItemStateChanged

		if ( evt.getStateChange () == ItemEvent.SELECTED )
		{
			Object item = evt.getItem ();
			if ( item != null )
			{
				String itemName = item.toString ();
				if ( firmwares != null )
				{
					if ( firmwares.containsKey (itemName) )
					{
						firmware.setText (firmwares.get (itemName));
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
					if ( phoneTypes.containsKey (itemName) )
					{
						phone.setText (phoneTypes.get (itemName));
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
				if ( phoneIMEIs != null )
				{
					if ( phoneIMEIs.containsKey (itemName) )
					{
						IMEI.setText (phoneIMEIs.get (itemName));
					}
					else
					{
						IMEI.setText (pressScanMsg);
					}
				}
				else
				{
					IMEI.setText (pressScanMsg);
				}
				if ( phoneSubsNums != null )
				{
					if ( phoneSubsNums.containsKey (itemName) )
					{
						subsNum.setText (phoneSubsNums.get (itemName));
					}
					else
					{
						subsNum.setText (pressScanMsg);
					}
				}
				else
				{
					subsNum.setText (pressScanMsg);
				}
			} // item != null
		} // selected
	}//GEN-LAST:event_portComboItemStateChanged

	private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		dispose ();
		Utils.closeProgram (logFile, 0);
	}//GEN-LAST:event_exitButActionPerformed

	private void loadConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadConfButActionPerformed

		if ( cfgFC == null )
		{
			cfgFC = new JFileChooser ();
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
		}
		cfgFC.setDialogType (JFileChooser.OPEN_DIALOG);
		//if ( lastSelDir != null )
		//	cfgFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = cfgFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				if ( res == null ) return;
				lastSelDir = res.getParent ();
				ConfigFile cfg = new ConfigFile (res);
				cfg.read ();

				portName = cfg.getPort ();
				speed = cfg.getSpeed ();
				dBits = cfg.getDBits ();
				parity = cfg.getParity ();
				int stopbits = cfg.getSBits ();
				if ( stopbits == 1 ) sBits = 1.5f;
				else if ( stopbits == 2 ) sBits = 2.0f;
				else sBits = 1.0f;
				flow = cfg.getFlowCtl ();
				isMax = cfg.getIsMax ();
				x = cfg.getX ();
				y = cfg.getY ();

				updateControls ();
				fontSizeSpin.setValue (new Integer (cfg.getFontSizeValue ()).floatValue ());
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "config.load");	// NOI18N
			}
		}
	}//GEN-LAST:event_loadConfButActionPerformed

	private void saveConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfButActionPerformed

		if ( cfgFC == null )
		{
			cfgFC = new JFileChooser ();
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
		}
		cfgFC.setDialogType (JFileChooser.SAVE_DIALOG);

		//if ( lastSelDir != null )
		//	cfgFC.setCurrentDirectory (new File (lastSelDir));
		int dialogRes = cfgFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				if ( res == null ) return;
				lastSelDir = res.getParent ();
				ConfigFile cfg = new ConfigFile (res);
				cfg.setPort (portCombo.getSelectedItem ().toString ());
				cfg.setSpeed (Integer.parseInt (speedCombo.getSelectedItem ().toString ()));
				cfg.setDataBits (Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()));
				cfg.setParity (parityCombo.getSelectedIndex ());
				cfg.setStopBits (stopBitsCombo.getSelectedIndex ());
				cfg.setFlow (((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0));
				cfg.setX (getX ());
				cfg.setY (getY ());
				cfg.setWidth (getWidth ());
				cfg.setHeight (getHeight ());
				cfg.setIsMaximized ((getExtendedState () & JFrame.MAXIMIZED_BOTH) != 0);
				Object val = fontSizeSpin.getValue ();
				if ( val != null && val instanceof Number )
					cfg.setFontSizeValue (((Number)val).intValue ());
				cfg.write ();
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "config.save");	// NOI18N
			}
		}
	}//GEN-LAST:event_saveConfButActionPerformed

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		Object val = fontSizeSpin.getValue ();
		if ( val != null && val instanceof Number )
			Utils.setFontSize (this, ((Number)val).floatValue ());
	}//GEN-LAST:event_fontSizeSpinStateChanged

	private void getAlarmListButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getAlarmListButActionPerformed

		try
		{
			Utils.updateStatusLabel (status, Utils.STATUS.RECEIVING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);
			if ( currentAlarmElements == null )
			{
				currentAlarmElements = new Vector<PhoneAlarm> (10);
			}

			TransferUtils.downloadAlarmList (TransferUtils.getIdentifierForPort
					(portCombo.getSelectedItem ().toString ()),
				Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
				new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						progressBar.setValue (1);
						progressBar.setValue (0);
						Utils.updateStatusLabel (status, Utils.STATUS.READY);
					}
				}, this, sync, false, false, false, alarmTable, currentAlarmElements);
		}
		catch (Exception ex)
		{
			Utils.updateStatusLabel (status, Utils.STATUS.READY);
			Utils.handleException (ex, "getAlarmList");	// NOI18N
		}
	}//GEN-LAST:event_getAlarmListButActionPerformed

	private void uploadAlarmButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadAlarmButActionPerformed

		try
		{
			int[] rows = alarmTable.getSelectedRows ();
			TableModel model = alarmTable.getModel ();
			if ( rows == null || model == null ) return;
			if ( rows.length == 0 ) return;
			final Vector<PhoneAlarm> toUpload = new Vector<PhoneAlarm> (rows.length);
			if ( toUpload == null ) return;
			for ( int i = 0; i < rows.length; i++ )
			{
				Object date = model.getValueAt (rows[i], 1);
				Object time = model.getValueAt (rows[i], 2);
				Object days = model.getValueAt (rows[i], 3);
				toUpload.add (new PhoneAlarm (rows[i]+1,
					// column 0 has the alarm number, which is the row number+1
					(date != null)? date.toString (): "",	// NOI18N
					(time != null)? time.toString (): "",	// NOI18N
					(days != null)? days.toString (): ""	// NOI18N
					));
			}
			Utils.updateStatusLabel (status, Utils.STATUS.SENDING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (toUpload.size ());
			for ( int i = 0; i < toUpload.size (); i++ )
			{
				TransferUtils.uploadAlarm (toUpload.get (i),
					TransferUtils.getIdentifierForPort
					(portCombo.getSelectedItem ().toString ()),
					Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
					Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
					Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
					parityCombo.getSelectedIndex (),
					((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
					new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							progressBar.setValue (progressBar.getValue () + 1);
							if ( progressBar.getValue () == toUpload.size () )
							{
								progressBar.setValue (0);
								Utils.updateStatusLabel (status,
									Utils.STATUS.READY);
							}
						}
					}, this, sync, false, false, false);
			}
		}
		catch (Exception ex)
		{
			Utils.updateStatusLabel (status, Utils.STATUS.READY);
			Utils.handleException (ex, "uploadAlarm");	// NOI18N
		}
	}//GEN-LAST:event_uploadAlarmButActionPerformed

	private void signalButtonActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_signalButtonActionPerformed
	{//GEN-HEADEREND:event_signalButtonActionPerformed
		try
		{
			DataTransporter dt = new DataTransporter (TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()));
			dt.open (Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
				fontSize = ((Number)val).floatValue ();
			new SignalDisplayer (dt, sync, fontSize).setVisible (true);
			//dt.close ();	// do NOT close, because the window is NOT modal
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.SignalDisplayer.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex2) {}
		}
	}//GEN-LAST:event_signalButtonActionPerformed

	private void getSmsListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getSmsListButActionPerformed
	{//GEN-HEADEREND:event_getSmsListButActionPerformed
		try
		{
			Utils.updateStatusLabel (status, Utils.STATUS.RECEIVING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);
			if ( currentMessageElements == null )
			{
				currentMessageElements = new Vector<PhoneMessage> (10);
			}

			TransferUtils.downloadMessageList (TransferUtils.getIdentifierForPort
					(portCombo.getSelectedItem ().toString ()),
				Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
				new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						progressBar.setValue (1);
						progressBar.setValue (0);
						Utils.updateStatusLabel (status, Utils.STATUS.READY);
					}
				}, this, sync, false, false, false, smsTable, currentMessageElements);
		}
		catch (Exception ex)
		{
			Utils.updateStatusLabel (status, Utils.STATUS.READY);
			Utils.handleException (ex, "getMessageList");	// NOI18N
		}
	}//GEN-LAST:event_getSmsListButActionPerformed

	private void downloadSmsButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadSmsButActionPerformed
	{//GEN-HEADEREND:event_downloadSmsButActionPerformed

		int[] selectedRows = smsTable.getSelectedRows ();
		if ( selectedRows.length == 0 ) return;

		for ( int i = 0; i < selectedRows.length; i++ )
		{
			if ( currentMessageElements != null )
			{
				float fontSize = 12;
				Object val = fontSizeSpin.getValue ();
				if ( val != null && val instanceof Number )
					fontSize = ((Number)val).floatValue ();
				new SMSWindow (null, this, null, fontSize,
					currentMessageElements.get (selectedRows[i])).setVisible (true);
			}
		}
	}//GEN-LAST:event_downloadSmsButActionPerformed

	private void uploadSmsButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadSmsButActionPerformed
	{//GEN-HEADEREND:event_uploadSmsButActionPerformed
		try
		{
			DataTransporter dt = new DataTransporter (TransferUtils.getIdentifierForPort
				(portCombo.getSelectedItem ().toString ()));
			dt.open (Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0)
				);
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
				fontSize = ((Number)val).floatValue ();
			new SMSWindow (dt, this, sync, fontSize, null).setVisible (true);
			dt.close ();
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.SMSWindow.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex2) {}
		}
	}//GEN-LAST:event_uploadSmsButActionPerformed

	private void putListInTable (final String ofWhat,
		final DefaultTableModel dtm,
		final Vector<PhoneElement> placeForData)
	{
		try
		{
			Utils.updateStatusLabel (status, Utils.STATUS.RECEIVING);
			progressBar.setValue (0);
			progressBar.setMinimum (0);
			progressBar.setMaximum (1);

			TransferUtils.downloadList (ofWhat,
				TransferUtils.getIdentifierForPort
					(portCombo.getSelectedItem ().toString ()),
				Integer.parseInt (speedCombo.getSelectedItem ().toString ()),
				Integer.parseInt (dataBitsCombo.getSelectedItem ().toString ()),
				Float.parseFloat (stopBitsCombo.getSelectedItem ().toString ()),
				parityCombo.getSelectedIndex (),
				((flowSoft.isSelected ())? 1 : 0) + ((flowHard.isSelected ())? 2 : 0),
				new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						progressBar.setValue (1);
						progressBar.setValue (0);
						Utils.updateStatusLabel (status, Utils.STATUS.READY);
					}
				}, this, sync, false, false, false, dtm, placeForData);
		}
		catch (Exception ex)
		{
			Utils.updateStatusLabel (status, Utils.STATUS.READY);
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
	private void updateControls ()
	{
		if ( portName != null ) portCombo.setSelectedItem (portName);
// 		dataBitsCombo.setSelectedItem (String.valueOf (dBits));
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
		else setExtendedState (getExtendedState () & ~JFrame.MAXIMIZED_BOTH);
		setLocation (x, y);
		if ( (getExtendedState () & JFrame.MAXIMIZED_BOTH) == 0 )
		{
			// if not maximized, verify position and size
			GraphicsConfiguration gc = null;
			Insets is = null;
			try
			{
				gc = getGraphicsConfiguration ();
				Toolkit tk = Toolkit.getDefaultToolkit ();
				if ( tk != null ) is = tk.getScreenInsets (gc);
			} catch (Exception ex) {}
			int maxX = 800;
			int maxY = 600;
			if ( gc != null )
			{
				Rectangle bounds = gc.getBounds ();
				if ( bounds != null )
				{
					if ( is != null )
					{
						maxX = bounds.width - is.left - is.right;
						maxY = bounds.height - is.top - is.bottom;
					}
					else
					{
						maxX = bounds.width;
						maxY = bounds.height;
					}
				}
			}
			if ( getWidth () <= 0 )
			{
				setSize (maxX, getHeight ());
			}
			if ( getHeight () <= 0 )
			{
				setSize (getWidth (), maxY);
			}
			if ( getX () + getWidth () < 0
				|| getX () > maxX )
			{
				setLocation (0, getY ());
			}
			if ( getY () + getHeight () < 0
				|| getY () > maxY )
			{
				setLocation (getX (), 0);
			}
			Dimension size = getSize ();
			if ( size != null )
			{
				size.height += 50;
				size.width += 50;
				setSize (size);
			}
		}
		speedCombo.setMaximumRowCount (speedCombo.getItemCount ());
	}

	// =============================== static methods

	/**
	 * Real program starting point.
	 * @param args the command line arguments.
	 */
	/*public*/ static void main (String args[])
	{
		// redirect stderr (caught and uncaught exceptions) to a file
		logFile = Utils.redirectStderrToFile (logFile);

		// set default uncaught exception handler:
		Thread.setDefaultUncaughtExceptionHandler (Utils.handler);

		// parse the command line:
		CommandLineParser.parse (args, sync, logFile);

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
			public synchronized void run ()
			{
				new MainWindow ().setVisible (true);
			}
		});
	}	// main ()

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel IMEI;
        private javax.swing.JLabel IMEILabel;
        private javax.swing.JButton aboutBut;
        private javax.swing.JPanel addrBookPane;
        private javax.swing.JTable addrTable;
        private javax.swing.JPanel alarmPane;
        private javax.swing.JTable alarmTable;
        private javax.swing.JPanel animPane;
        private javax.swing.JTable animTable;
        private javax.swing.JLabel bpsLabel;
        private javax.swing.JComboBox dataBitsCombo;
        private javax.swing.JLabel databitsLabel;
        private javax.swing.JButton deleteAddrBut;
        private javax.swing.JButton deleteAlarmBut;
        private javax.swing.JButton deleteAnimBut;
        private javax.swing.JButton deleteEventBut;
        private javax.swing.JButton deleteJavaBut;
        private javax.swing.JButton deletePhotoBut;
        private javax.swing.JButton deleteRingBut;
        private javax.swing.JButton deleteSmsBut;
        private javax.swing.JButton deleteTodoBut;
        private javax.swing.JButton downloadAddrBut;
        private javax.swing.JButton downloadAlarmBut;
        private javax.swing.JButton downloadAnimBut;
        private javax.swing.JButton downloadEventBut;
        private javax.swing.JButton downloadJavaBut;
        private javax.swing.JButton downloadPhotoBut;
        private javax.swing.JButton downloadRingBut;
        private javax.swing.JButton downloadSmsBut;
        private javax.swing.JButton downloadTodoBut;
        private javax.swing.JPanel eventPane;
        private javax.swing.JTable eventTable;
        private javax.swing.JButton exitBut;
        private javax.swing.JLabel firmware;
        private javax.swing.JLabel firmwareLabel;
        private javax.swing.JCheckBox flowHard;
        private javax.swing.JLabel flowLabel;
        private javax.swing.JCheckBox flowSoft;
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JButton getAddrListBut;
        private javax.swing.JButton getAlarmListBut;
        private javax.swing.JButton getAnimListBut;
        private javax.swing.JButton getCapBut;
        private javax.swing.JButton getEventListBut;
        private javax.swing.JButton getJavaListBut;
        private javax.swing.JButton getPhotoListBut;
        private javax.swing.JButton getRingListBut;
        private javax.swing.JButton getSmsListBut;
        private javax.swing.JButton getTodoListBut;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane10;
        private javax.swing.JScrollPane jScrollPane11;
        private javax.swing.JScrollPane jScrollPane12;
        private javax.swing.JScrollPane jScrollPane13;
        private javax.swing.JScrollPane jScrollPane14;
        private javax.swing.JScrollPane jScrollPane15;
        private javax.swing.JScrollPane jScrollPane16;
        private javax.swing.JScrollPane jScrollPane17;
        private javax.swing.JScrollPane jScrollPane18;
        private javax.swing.JScrollPane jScrollPane19;
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
        private final javax.swing.JProgressBar progressBar = new javax.swing.JProgressBar();
        private javax.swing.JLabel progressLabel;
        private javax.swing.JButton rawBut;
        private javax.swing.JPanel ringPane;
        private javax.swing.JTable ringTable;
        private javax.swing.JButton saveConfBut;
        private javax.swing.JButton scanButton;
        private javax.swing.JButton signalButton;
        private javax.swing.JPanel smsPane;
        private javax.swing.JTable smsTable;
        private javax.swing.JComboBox speedCombo;
        private javax.swing.JLabel speedLabel;
        private final javax.swing.JLabel status = new javax.swing.JLabel();
        private javax.swing.JLabel statusLabel;
        private javax.swing.JComboBox stopBitsCombo;
        private javax.swing.JLabel stopbitsLabel;
        private javax.swing.JLabel subsNum;
        private javax.swing.JLabel subsNumLabel;
        private javax.swing.JTabbedPane tabPane;
        private javax.swing.JPanel todoPane;
        private javax.swing.JTable todoTable;
        private javax.swing.JButton uploadAddrBut;
        private javax.swing.JButton uploadAlarmBut;
        private javax.swing.JButton uploadAnimBut;
        private javax.swing.JButton uploadEventBut;
        private javax.swing.JButton uploadJavaBut;
        private javax.swing.JButton uploadPhotoBut;
        private javax.swing.JButton uploadRingBut;
        private javax.swing.JButton uploadSmsBut;
        private javax.swing.JButton uploadTodoBut;
        private org.jdesktop.beansbinding.BindingGroup bindingGroup;
        // End of variables declaration//GEN-END:variables

}
