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
	Vector<PhoneElement> currentElements;

	public static Pattern listPattern;
	public static Pattern photoContentsPattern;
	public static Pattern midiContentsPattern;
	public static Pattern amrContentsPattern;
	public static Pattern generalContentsPattern;
	public static Pattern bmpContentsPattern;
	public static Pattern gifContentsPattern;
	public static Pattern pngContentsPattern;

	public static Hashtable<String, Integer> filetypeIDs;
	private Hashtable<String, Integer> photofileIDs;
	private Hashtable<String, Integer> ringfileIDs;

	/** Creates new form MainWindow */
        public MainWindow ()
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

		ringfileIDs = new Hashtable<String, Integer> (6);
		// seem to do something: 1, 201-202, 205-207
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

		filetypeIDs = new Hashtable<String, Integer> (ringfileIDs.size ()
			+ photofileIDs.size ());
		filetypeIDs.putAll (this.photofileIDs);
		filetypeIDs.putAll (this.ringfileIDs);
		// TODO: AMR, mp3, vCard, vTODO, vEvent

		initComponents ();
                Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

		while (portList.hasMoreElements ())
		{
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement ();

			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				this.portCombo.addItem (id.getName ());
			}
		}
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

                getPhotoListButton.setText("Get list");
                getPhotoListButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getPhotoListButtonActionPerformed(evt);
                        }
                });

                downloadPhotoBut.setText("Download selected");
                downloadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
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
                        .addGroup(photoPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

                getRingListbut.setText("Get list");
                getRingListbut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getRingListbutActionPerformed(evt);
                        }
                });

                downloadRingBut.setText("Download selected");
                downloadRingBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadButActionPerformed(evt);
                        }
                });

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
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
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
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ringPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getRingListbut)
                                        .addComponent(downloadRingBut)
                                        .addComponent(uploadRingBut))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                tabPane.addTab("Ringtones", ringPane);

                jLabel1.setText("Port:");

                jLabel2.setText("Speed:");

                jLabel3.setText("Data bits:");

                jLabel4.setText("Parity bits:");

                jLabel5.setText("Stop bits:");

                jLabel6.setText("Flow control:");

                flowCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Software (XON/XOFF)", "Hardware (CTS/RTS)" }));

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
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
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
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void scanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanButtonActionPerformed

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
			JOptionPane.showMessageDialog (this, "No answers received", "Error",
				JOptionPane.ERROR_MESSAGE);
		}
		else if (active.size () == 1)
		{
			portCombo.setSelectedItem (active.get (0).getName ());
		}
		else
		{
			// let the user choose
			int res = JOptionPane.showOptionDialog (this,
				"Multiple answers received. Select the correct port.",
				"Which one?",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				active.toArray (),
				active.get(0).getName ());
			if ( res != JOptionPane.CLOSED_OPTION )
				this.portCombo.setSelectedIndex (res);
			else
				this.portCombo.setSelectedItem (active.get (0).getName ());
		}
	}//GEN-LAST:event_scanButtonActionPerformed

	private void getPhotoListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPhotoListButtonActionPerformed

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
			currentElements = dt.getList ("PICTURES");
			dt.close ();
			if ( currentElements != null )
			{
				DefaultTableModel tm = (DefaultTableModel) this.photoTable.getModel ();
				for ( int i=0; i < currentElements.size (); i++ )
				{
					tm.addRow (new String[] {currentElements.get (i).getFilename ()
						+ "." + currentElements.get (i).getExt () });
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
}//GEN-LAST:event_getPhotoListButtonActionPerformed

	private void downloadButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButActionPerformed

		int[] selectedRows;
		if ( this.tabPane.getSelectedIndex () == 0 )
		{
			selectedRows = this.photoTable.getSelectedRows ();
		}
		else
		{
			selectedRows = this.ringTable.getSelectedRows ();
		}
		if ( selectedRows == null ) return;
		if ( selectedRows.length == 0 ) return;

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
				File received = new File (currentElements.get (selectedRows[i]).getFilename ()
						+ "." + currentElements.get (selectedRows[i]).getExt ());
				if ( received.exists () )
				{
					int res = JOptionPane.showConfirmDialog (this,
						currentElements.get (selectedRows[i]).getFilename ()
						+ "." + currentElements.get (selectedRows[i]).getExt () +
						" exists. Overwrite?");
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
}//GEN-LAST:event_downloadButActionPerformed

	private void getRingListbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRingListbutActionPerformed

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
			currentElements = dt.getList ("RINGTONES");
			dt.close ();
			if ( currentElements != null )
			{
				DefaultTableModel tm = (DefaultTableModel) this.ringTable.getModel ();
				for ( int i=0; i < currentElements.size (); i++ )
				{
					tm.addRow (new String[] {currentElements.get (i).getFilename ()
						+ "." + currentElements.get (i).getExt () });
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println (ex);
			ex.printStackTrace ();
		}
	}//GEN-LAST:event_getRingListbutActionPerformed

	private void aboutButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButActionPerformed

		JOptionPane.showMessageDialog (this,
			"JYMAG - Java Your Music and Graphics - a Free 'My Pictures and Sounds' replacement.\n" +
			"Author: Bogdan Drozdowski, bogdandr@op.pl\n" +
			"License: GPLv3+\n\n" +
			"Supported opertations:\n" +
			"1) Getting lists of: pictures and ringtones\n" +
			"2) Downloading:\n" +
			"   a) pictures: JPG, GIF, BMP, PNG\n" +
			"   b) ringtones: MIDI, AMR\n" +
			"3) Uploading:\n" +
			"   a) pictures: JPG, GIF, BMP, PNG\n" +
			"   b) ringtones: MIDI\n" +
			"\n" +
			"Thanks to: Sharp (sharpy+at+xox.pl) for GetPic.pl\n"+
			"sebtx452 @ gmail.com for wxPicSound\n" +
			"MIKSOFT for \"Mobile Media Converter\"\n" +
			"\"ffmpeg project\" for ffmpeg",
			"About", JOptionPane.INFORMATION_MESSAGE );

	}//GEN-LAST:event_aboutButActionPerformed

	private void uploadPhotoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadPhotoButActionPerformed

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
				return "Pictures";
			}
		} );

		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = jfc.getSelectedFile ();
				CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(this.portCombo.getSelectedItem ().toString ());
				DataTransporter dt = new DataTransporter (id);
				dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
					Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
					Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
					parityCombo.getSelectedIndex (),
					flowCombo.getSelectedIndex ()
					);

				int put = dt.putFile (res, res.getName ().substring
					(0, res.getName ().indexOf (".")).replaceAll ("\\s", ""));

				dt.close ();
				if ( put != 0 )
				{
					JOptionPane.showMessageDialog (this,
					"Error " + put,
					"Error", JOptionPane.ERROR_MESSAGE );

				}
			}
			catch (Exception ex)
			{
				System.out.println (ex);
				ex.printStackTrace ();
			}
		}
	}//GEN-LAST:event_uploadPhotoButActionPerformed

	private void uploadRingButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadRingButActionPerformed

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
				return "Sounds";
			}
		} );

		jfc.setDialogType (JFileChooser.SAVE_DIALOG);
		int dialogRes = jfc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				File res = jfc.getSelectedFile ();
				CommPortIdentifier id = CommPortIdentifier.getPortIdentifier
					(this.portCombo.getSelectedItem ().toString ());
				DataTransporter dt = new DataTransporter (id);
				dt.open (Integer.valueOf (speedCombo.getSelectedItem ().toString ()),
					Integer.valueOf (dataBitsCombo.getSelectedItem ().toString ()),
					Float.valueOf (stopBitsCombo.getSelectedItem ().toString ()),
					parityCombo.getSelectedIndex (),
					flowCombo.getSelectedIndex ()
					);

				int put = dt.putFile (res, res.getName ().substring
					(0, res.getName ().indexOf (".")).replaceAll ("\\s", ""));

				dt.close ();
				if ( put != 0 )
				{
					JOptionPane.showMessageDialog (this,
					"Error " + put,
					"Error", JOptionPane.ERROR_MESSAGE );
				}
			}
			catch (Exception ex)
			{
				System.out.println (ex);
				ex.printStackTrace ();
			}
		}
	}//GEN-LAST:event_uploadRingButActionPerformed

        /**
         * Program starting point.
         * @param args the command line arguments.
         */
        public static void main (String args[])
        {
		if ( args != null )
		{
			for ( int i=0; i < args.length; i++ )
			{
				if ( args[i].equals ("--help") || args[i].equals ("-h")
					|| args[i].equals ("-?") || args[i].equals ("/?") )
				{
					System.out.println ("JYMAG (Java Your Music and Graphics) is a program " +
						"for retrieving and sending multimedia from and to a Sagem mobile phone.\n" +
						"You need the RxTx Java Transmission package from www.rxtx.org\n" +
						"Author: Bogdan Drozdowski, bogdandr @ op . pl\n" +
						"License: GPLv3+\n" +
				                "See http://rudy.mif.pg.gda.pl/~bogdro/soft\n\n" +
						"Command-line options:\n" +
						"--help, -h, -?, /?\tdisplay help\n" +
						"--licence, --license\tdisplay license\n" +
						"--version, -v\t\tdisplay version\n"
						);
					System.exit (0);
				}
				else if ( args[i].equals ("--license")
					|| args[i].equals ("--licence") )
				{
					System.out.println ("JYMAG (Java Your Music and Graphics) is a program " +
						"for retrieving and sending multimedia from and to a Sagem mobile phone.\n" +
						"See http://rudy.mif.pg.gda.pl/~bogdro/soft\n" +
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
				else if ( args[i].equals ("--version")  || args[i].equals ("-v") )
				{
					System.out.println ("JYMAG version 0.1");
					System.exit (0);
				}
			}
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
        private javax.swing.JComboBox dataBitsCombo;
        private javax.swing.JButton downloadPhotoBut;
        private javax.swing.JButton downloadRingBut;
        private javax.swing.JComboBox flowCombo;
        private javax.swing.JButton getPhotoListButton;
        private javax.swing.JButton getRingListbut;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
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
        private javax.swing.JButton uploadPhotoBut;
        private javax.swing.JButton uploadRingBut;
        // End of variables declaration//GEN-END:variables

}
