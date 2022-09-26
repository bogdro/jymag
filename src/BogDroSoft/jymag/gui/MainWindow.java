/*
 * MainWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2013 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag.gui;

import BogDroSoft.jymag.CommandLineParser;
import BogDroSoft.jymag.ConfigFile;
import BogDroSoft.jymag.comm.DataTransporter;
import BogDroSoft.jymag.PhoneElement;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.gui.panels.JYMAGTab;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the main window of the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 65L;
	private final MainWindow mw = this;

	/** Current version number as a String. */
	public static final String verString = "1.3";	// NOI18N

	// synchronization variable:
	private static final Object sync = new Object ();
	// port-firmware pairs and the firmware version pattern, used for displaying:
	private volatile Hashtable<String, String> firmwares;
	private volatile Hashtable<String, String> phoneTypes;
	private volatile Hashtable<String, String> phoneIMEIs;
	private volatile Hashtable<String, String> phoneSubsNums;

	private static String logFile = "jymag.log";	// NOI18N

	private JFileChooser cfgFC;

	// ------------ i18n stuff
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String noAnsString = mwBundle.getString("No_answers_received");
	public static final String errString = mwBundle.getString("Error");
	private static final String multiAnsString = mwBundle.getString("Multiple_answers");
	private static final String whichString = mwBundle.getString("Which_one");

	private static final String pressScanMsg = "(" + mwBundle.getString("(press_Scan)") + ")";	// NOI18N

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

		/* add the Esc key listener to the frame and all components. */
		new Utils.EscKeyListener (this);
		setPanelConnections (this);
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
                photoPanel = new BogDroSoft.jymag.gui.panels.PhotoPanel();
                jScrollPane8 = new javax.swing.JScrollPane();
                ringtonePanel = new BogDroSoft.jymag.gui.panels.RingtonePanel();
                jScrollPane9 = new javax.swing.JScrollPane();
                addrBookPanel = new BogDroSoft.jymag.gui.panels.AddrBookPanel();
                jScrollPane10 = new javax.swing.JScrollPane();
                tasksPanel = new BogDroSoft.jymag.gui.panels.TasksPanel();
                jScrollPane11 = new javax.swing.JScrollPane();
                eventsPanel = new BogDroSoft.jymag.gui.panels.EventsPanel();
                jScrollPane12 = new javax.swing.JScrollPane();
                moviePanel = new BogDroSoft.jymag.gui.panels.MoviePanel();
                jScrollPane13 = new javax.swing.JScrollPane();
                javasPanel = new BogDroSoft.jymag.gui.panels.JavasPanel();
                jScrollPane16 = new javax.swing.JScrollPane();
                alarmPanel = new BogDroSoft.jymag.gui.panels.AlarmPanel();
                jScrollPane18 = new javax.swing.JScrollPane();
                sMSPanel = new BogDroSoft.jymag.gui.panels.SMSPanel();
                jScrollPane20 = new javax.swing.JScrollPane();
                dialPanel = new BogDroSoft.jymag.gui.panels.DialPanel();
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

                jScrollPane7.setViewportView(photoPanel);

                tabPane.addTab(bundle.getString("Photos"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/pict-1.png")), jScrollPane7); // NOI18N

                jScrollPane8.setViewportView(ringtonePanel);

                tabPane.addTab(bundle.getString("Ringtones"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/ring.png")), jScrollPane8); // NOI18N

                jScrollPane9.setViewportView(addrBookPanel);

                tabPane.addTab(bundle.getString("Addressbook"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/abook.png")), jScrollPane9); // NOI18N

                jScrollPane10.setViewportView(tasksPanel);

                tabPane.addTab(bundle.getString("ToDoTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/todo.png")), jScrollPane10); // NOI18N

                jScrollPane11.setViewportView(eventsPanel);

                tabPane.addTab(bundle.getString("EventsTasksTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/event.png")), jScrollPane11); // NOI18N

                jScrollPane12.setViewportView(moviePanel);

                tabPane.addTab(bundle.getString("AnimationsVideosTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/video.png")), jScrollPane12); // NOI18N

                jScrollPane13.setViewportView(javasPanel);

                tabPane.addTab(bundle.getString("JavaTab"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/java.png")), jScrollPane13); // NOI18N

                jScrollPane16.setViewportView(alarmPanel);

                tabPane.addTab(bundle.getString("Alarms"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/alarmclk.png")), jScrollPane16); // NOI18N

                jScrollPane18.setViewportView(sMSPanel);

                tabPane.addTab(bundle.getString("SMS"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/sms.png")), jScrollPane18); // NOI18N

                jScrollPane20.setViewportView(dialPanel);

                tabPane.addTab(bundle.getString("tab_dial"), new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/dialtab.png")), jScrollPane20); // NOI18N

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
                                        .addGap(58, 58, 58)
                                        .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                );

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
		if ( ports != null )
		{
			max = ports.size ();
		}
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


	private void aboutButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButActionPerformed

		try
		{
			float fontSize = 12;
			Object val = fontSizeSpin.getValue ();
			if ( val != null && val instanceof Number )
			{
				fontSize = ((Number)val).floatValue ();
			}
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

	/**
	 * Uploads the given file using the current settings.
	 * @param f the fie to upload.
	 */
	public void dynamicUpload (final File f)
	{
		if ( f == null )
		{
			return;
		}
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
			{
				fontSize = ((Number)val).floatValue ();
			}
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
			{
				fontSize = ((Number)val).floatValue ();
			}
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
				String fw = pressScanMsg;
				if ( firmwares != null )
				{
					if ( firmwares.containsKey (itemName) )
					{
						fw = firmwares.get (itemName);
					}
				}
				firmware.setText (fw);

				String pType = pressScanMsg;
				if ( phoneTypes != null )
				{
					if ( phoneTypes.containsKey (itemName) )
					{
						pType = phoneTypes.get (itemName);
					}
				}
				phone.setText (pType);

				String pIMEI = pressScanMsg;
				if ( phoneIMEIs != null )
				{
					if ( phoneIMEIs.containsKey (itemName) )
					{
						pIMEI = phoneIMEIs.get (itemName);
					}
				}
				IMEI.setText (pIMEI);

				String pNumber = pressScanMsg;
				if ( phoneSubsNums != null )
				{
					if ( phoneSubsNums.containsKey (itemName) )
					{
						pNumber = phoneSubsNums.get (itemName);
					}
				}
				subsNum.setText (pNumber);
			} // item != null
		} // selected
	}//GEN-LAST:event_portComboItemStateChanged

	private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		dispose ();
		Utils.closeProgram (logFile, 0);
	}//GEN-LAST:event_exitButActionPerformed

	private synchronized void loadConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadConfButActionPerformed

		if ( cfgFC == null )
		{
			cfgFC = new JFileChooser ();
			cfgFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
			{
				@Override
				public boolean accept ( File f )
				{
					if ( f == null )
					{
						return false;
					}
					if ( f.isDirectory () ||
						f.getName ().endsWith (".cfg"))	// NOI18N
					{
						return true;
					}
					return false;
				}

				@Override
				public String getDescription ()
				{
					return mwBundle.getString("JYMAG_configuration_files")
						+ " (*.cfg)";	// NOI18N
				}
			} );
			cfgFC.setAcceptAllFileFilterUsed (false);
			cfgFC.setMultiSelectionEnabled (false);
		}
		cfgFC.setDialogType (JFileChooser.OPEN_DIALOG);
		int dialogRes = cfgFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				if ( res == null )
				{
					return;
				}
				ConfigFile cfg = new ConfigFile (res);
				cfg.read ();

				portName = cfg.getPort ();
				speed = cfg.getSpeed ();
				dBits = cfg.getDBits ();
				parity = cfg.getParity ();
				int stopbits = cfg.getSBits ();
				if ( stopbits == 1 )
				{
					sBits = 1.5f;
				}
				else if ( stopbits == 2 )
				{
					sBits = 2.0f;
				}
				else
				{
					sBits = 1.0f;
				}
				flow = cfg.getFlowCtl ();
				isMax = cfg.getIsMax ();
				x = cfg.getX ();
				y = cfg.getY ();

				updateControls ();
				fontSizeSpin.setValue ((float)cfg.getFontSizeValue ());
				tabPane.setSelectedIndex(cfg.getSelectedTab ());
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "config.load");	// NOI18N
			}
		}
	}//GEN-LAST:event_loadConfButActionPerformed

	private synchronized void saveConfButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfButActionPerformed

		if ( cfgFC == null )
		{
			cfgFC = new JFileChooser ();
			cfgFC.setFileFilter ( new javax.swing.filechooser.FileFilter ()
			{
				@Override
				public boolean accept ( File f )
				{
					if ( f == null )
					{
						return false;
					}
					if ( f.isDirectory () ||
						f.getName ().endsWith (".cfg"))	// NOI18N
					{
						return true;
					}
					return false;
				}

				@Override
				public String getDescription ()
				{
					return mwBundle.getString("JYMAG_configuration_files")
						+ " (*.cfg)";	// NOI18N
				}
			} );
			cfgFC.setAcceptAllFileFilterUsed (false);
			cfgFC.setMultiSelectionEnabled (false);
		}
		cfgFC.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = cfgFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = cfgFC.getSelectedFile ();
				if ( res == null )
				{
					return;
				}
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
				{
					cfg.setFontSizeValue (((Number)val).intValue ());
				}
				cfg.setSelectedTab (tabPane.getSelectedIndex());
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
		{
			Utils.setFontSize (this, ((Number)val).floatValue ());
		}
	}//GEN-LAST:event_fontSizeSpinStateChanged

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
			{
				fontSize = ((Number)val).floatValue ();
			}
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

	/**
	 * Puts the list of the given elements in the given table.
	 * @param ofWhat Tells which elements should be downloaded.
	 * @param dtm The table to put the data in.
	 * @param placeForData The place for the downloaded elements.
	 */
	public void putListInTable (final String ofWhat,
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
		{
			stopBitsCombo.setSelectedIndex (2);
		}
		else if ( Math.abs (sBits-1.5f) < 0.0001 )
		{
			stopBitsCombo.setSelectedIndex (1);
		}
		else
		{
			stopBitsCombo.setSelectedIndex (0);
		}
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
		if ( isMax )
		{
			setExtendedState (JFrame.MAXIMIZED_BOTH);
		}
		else
		{
			setExtendedState (getExtendedState () & ~JFrame.MAXIMIZED_BOTH);
		}
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
				if ( tk != null )
				{
					is = tk.getScreenInsets (gc);
				}
			}
			catch (Exception ex)
			{
				Utils.handleException (ex,
					"MainWindow:updateControls:GraphicsConfiguration/Toolkit");	// NOI18N
			}
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

	/**
	 * Sets the elements from the main window on the tabs' panels' components in the given
	 * Component (recursively, if it's a Container).
	 * @param c The Component with Components that will be connected to the main window.
	 */
	public void setPanelConnections (Component c)
	{
		if ( c == null )
		{
			return;
		}
		if ( c instanceof JYMAGTab )
		{
			JYMAGTab tab = (JYMAGTab)c;
			tab.setPortCombo (portCombo);
			tab.setSpeedCombo (speedCombo);
			tab.setDataBitsCombo (dataBitsCombo);
			tab.setStopBitsCombo (stopBitsCombo);
			tab.setParityCombo (parityCombo);
			tab.setFlowSoftCheckbox (flowSoft);
			tab.setFlowHardCheckbox (flowHard);
			tab.setSync (sync);
			tab.setProgressBar (progressBar);
			tab.setStatusLabel (status);
			tab.setDestDir (destDirName);
			tab.setMainWindow (this);
			tab.setFontSizeSpin (fontSizeSpin);
		}
		if ( c instanceof Container )
		{
			Component[] subComps = ((Container)c).getComponents ();
			if ( subComps != null )
			{
				for ( int i = 0; i < subComps.length; i++ )
				{
					if ( subComps[i] != null )
					{
						setPanelConnections (subComps[i]);
					}
				}
			}
		}
	}

	// =============================== static methods

	/**
	 * Real program starting point.
	 * @param args the command line arguments.
	 */
	public static void start (String args[])
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
        private BogDroSoft.jymag.gui.panels.AddrBookPanel addrBookPanel;
        private BogDroSoft.jymag.gui.panels.AlarmPanel alarmPanel;
        private javax.swing.JLabel bpsLabel;
        private javax.swing.JComboBox dataBitsCombo;
        private javax.swing.JLabel databitsLabel;
        private BogDroSoft.jymag.gui.panels.DialPanel dialPanel;
        private BogDroSoft.jymag.gui.panels.EventsPanel eventsPanel;
        private javax.swing.JButton exitBut;
        private javax.swing.JLabel firmware;
        private javax.swing.JLabel firmwareLabel;
        private javax.swing.JCheckBox flowHard;
        private javax.swing.JLabel flowLabel;
        private javax.swing.JCheckBox flowSoft;
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JButton getCapBut;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane10;
        private javax.swing.JScrollPane jScrollPane11;
        private javax.swing.JScrollPane jScrollPane12;
        private javax.swing.JScrollPane jScrollPane13;
        private javax.swing.JScrollPane jScrollPane15;
        private javax.swing.JScrollPane jScrollPane16;
        private javax.swing.JScrollPane jScrollPane18;
        private javax.swing.JScrollPane jScrollPane20;
        private javax.swing.JScrollPane jScrollPane7;
        private javax.swing.JScrollPane jScrollPane8;
        private javax.swing.JScrollPane jScrollPane9;
        private BogDroSoft.jymag.gui.panels.JavasPanel javasPanel;
        private javax.swing.JButton loadConfBut;
        private BogDroSoft.jymag.gui.panels.MoviePanel moviePanel;
        private javax.swing.JComboBox parityCombo;
        private javax.swing.JLabel parityLabel;
        private javax.swing.JLabel phone;
        private javax.swing.JLabel phoneTypeLabel;
        private BogDroSoft.jymag.gui.panels.PhotoPanel photoPanel;
        private javax.swing.JComboBox portCombo;
        private javax.swing.JLabel portLabel;
        private final javax.swing.JProgressBar progressBar = new javax.swing.JProgressBar();
        private javax.swing.JLabel progressLabel;
        private javax.swing.JButton rawBut;
        private BogDroSoft.jymag.gui.panels.RingtonePanel ringtonePanel;
        private BogDroSoft.jymag.gui.panels.SMSPanel sMSPanel;
        private javax.swing.JButton saveConfBut;
        private javax.swing.JButton scanButton;
        private javax.swing.JButton signalButton;
        private javax.swing.JComboBox speedCombo;
        private javax.swing.JLabel speedLabel;
        private final javax.swing.JLabel status = new javax.swing.JLabel();
        private javax.swing.JLabel statusLabel;
        private javax.swing.JComboBox stopBitsCombo;
        private javax.swing.JLabel stopbitsLabel;
        private javax.swing.JLabel subsNum;
        private javax.swing.JLabel subsNumLabel;
        private javax.swing.JTabbedPane tabPane;
        private BogDroSoft.jymag.gui.panels.TasksPanel tasksPanel;
        // End of variables declaration//GEN-END:variables

}
