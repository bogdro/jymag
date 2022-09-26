/*
 * SMSPanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2013 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag.gui.panels;

import BogDroSoft.jymag.PhoneMessage;
import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.comm.DataTransporter;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.gui.MainWindow;
import BogDroSoft.jymag.gui.SMSWindow;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;

/**
 * This class is the SMS' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class SMSPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 89L;

	@SuppressWarnings("rawtypes")
	private JComboBox portCombo;
	@SuppressWarnings("rawtypes")
	private JComboBox speedCombo;
	@SuppressWarnings("rawtypes")
	private JComboBox dataBitsCombo;
	@SuppressWarnings("rawtypes")
	private JComboBox stopBitsCombo;
	@SuppressWarnings("rawtypes")
	private JComboBox parityCombo;

	private JCheckBox flowSoft;
	private JCheckBox flowHard;
        private JProgressBar progressBar;
        private JLabel status;
	private JSpinner fontSizeSpin;

	// synchronization variable:
	private Object sync;

	private MainWindow mw;

	private Vector<PhoneMessage> currentMessageElements;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String deleteQuestion = mwBundle.getString("want_to_delete");
	private static final String questionString = mwBundle.getString("Question");


	/** Creates new form SMSPanel */
	public SMSPanel()
	{
		initComponents();
	}

	/** This method is called from within the constructor to
	* initialize the form.
	* WARNING: Do NOT modify this code. The content of this method is
	* always regenerated by the Form Editor.
	*/
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                deleteSmsBut = new javax.swing.JButton();
                uploadSmsBut = new javax.swing.JButton();
                downloadSmsBut = new javax.swing.JButton();
                getSmsListBut = new javax.swing.JButton();
                jScrollPane19 = new javax.swing.JScrollPane();
                smsTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(234, 187, 0));

                deleteSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                deleteSmsBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteSmsBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                deleteSmsButdeleteButActionPerformed(evt);
                        }
                });

                uploadSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadSmsBut.setText(bundle.getString("Upload")); // NOI18N
                uploadSmsBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadSmsButActionPerformed(evt);
                        }
                });

                downloadSmsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadSmsBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadSmsBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadSmsButActionPerformed(evt);
                        }
                });

                getSmsListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
                getSmsListBut.setText(bundle.getString("Get_list")); // NOI18N
                getSmsListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getSmsListButActionPerformed(evt);
                        }
                });

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

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getSmsListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadSmsBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadSmsBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteSmsBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getSmsListBut)
                                                .addComponent(downloadSmsBut)
                                                .addComponent(uploadSmsBut)
                                                .addComponent(deleteSmsBut))
                                        .addGap(25, 25, 25)))
                );
        }// </editor-fold>//GEN-END:initComponents

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
				{
					fontSize = ((Number)val).floatValue ();
				}
				new SMSWindow (null, mw, null, fontSize,
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
			{
				fontSize = ((Number)val).floatValue ();
			}
			new SMSWindow (dt, mw, sync, fontSize, null).setVisible (true);
			dt.close ();
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "MainWindow.SMSWindow.start");	// NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, ex.toString (),
					MainWindow.errString, JOptionPane.ERROR_MESSAGE);
			}
			catch (Exception ex2)
			{}
		}
	}//GEN-LAST:event_uploadSmsButActionPerformed

	private void deleteSmsButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteSmsButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteSmsButdeleteButActionPerformed

		int[] selectedRows = smsTable.getSelectedRows ();
		if ( selectedRows == null || currentMessageElements == null )
		{
			return;
		}
		if ( selectedRows.length == 0 )
		{
			return;
		}

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
					threads.incrementAndGet ();
					TransferUtils.deleteMessage (
						currentMessageElements.get (toGet),
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
			catch (Exception ex)
			{
				Utils.updateStatusLabel (status, Utils.STATUS.READY);
				Utils.handleException (ex, "delete");	// NOI18N
			}
		}
	}//GEN-LAST:event_deleteSmsButdeleteButActionPerformed


	@Override
	@SuppressWarnings("rawtypes")
	public void setPortCombo (JComboBox combo)
	{
		portCombo = combo;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setSpeedCombo (JComboBox combo)
	{
		speedCombo = combo;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setDataBitsCombo (JComboBox combo)
	{
		dataBitsCombo = combo;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setStopBitsCombo (JComboBox combo)
	{
		stopBitsCombo = combo;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setParityCombo (JComboBox combo)
	{
		parityCombo = combo;
	}

	@Override
	public void setFlowSoftCheckbox (JCheckBox checkbox)
	{
		flowSoft = checkbox;
	}

	@Override
	public void setFlowHardCheckbox (JCheckBox checkbox)
	{
		flowHard = checkbox;
	}

	@Override
	public void setSync (Object synch)
	{
		sync = synch;
	}

	@Override
	public void setProgressBar (JProgressBar mainProgressBar)
	{
		progressBar = mainProgressBar;
	}

	@Override
	public void setStatusLabel (JLabel statusLabel)
	{
		status = statusLabel;
	}

	@Override
	public void setDestDir (String destDir)
	{
		// not needed
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		fontSizeSpin = fontSizeSpinner;
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton deleteSmsBut;
        private javax.swing.JButton downloadSmsBut;
        private javax.swing.JButton getSmsListBut;
        private javax.swing.JScrollPane jScrollPane19;
        private javax.swing.JTable smsTable;
        private javax.swing.JButton uploadSmsBut;
        // End of variables declaration//GEN-END:variables

}
