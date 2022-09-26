/*
 * PhotoPanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2013-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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

import BogDroSoft.jymag.PhoneElement;
import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.comm.TransferParameters;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.gui.MainWindow;
import BogDroSoft.jymag.gui.JYMAGTransferHandler;

import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

/**
 * This class is the photos' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class PhotoPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 87L;

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

	// synchronization variable:
	private Object sync;

	private MainWindow mw;

	private Vector<PhoneElement> currentPhotoElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadPictFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String exOverString = mwBundle.getString("_exists._Overwrite");
	private static final String overwriteStr = mwBundle.getString("Overwrite?");
	private static final String errString = mwBundle.getString("Error");
	private static final String fileNotWriteMsg = mwBundle.getString("Cant_write_to_file");
	private static final String picsString = mwBundle.getString("Supported_pictures");
	private static final String deleteQuestion = mwBundle.getString("want_to_delete");
	private static final String questionString = mwBundle.getString("Question");


	/** Creates new form PhotoPanel */
	public PhotoPanel()
	{
		initComponents();

		photoTable.setFillsViewportHeight (true);
		photoTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		photoTable.getTableHeader ().addMouseListener (
			new Utils.TableMouseListener (photoTable));
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jScrollPane1 = new javax.swing.JScrollPane();
                photoTable = new javax.swing.JTable();
                downloadPhotoBut = new javax.swing.JButton();
                uploadPhotoBut = new javax.swing.JButton();
                deletePhotoBut = new javax.swing.JButton();
                getPhotoListBut = new javax.swing.JButton();

                setBackground(new java.awt.Color(184, 242, 255));

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

                downloadPhotoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                downloadPhotoBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadPhotoBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadPhotoButdownloadButActionPerformed(evt);
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
                                deletePhotoButdeleteButActionPerformed(evt);
                        }
                });

                getPhotoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
                getPhotoListBut.setText(bundle.getString("Get_list")); // NOI18N
                getPhotoListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getPhotoListButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getPhotoListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadPhotoBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadPhotoBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deletePhotoBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getPhotoListBut)
                                                .addComponent(downloadPhotoBut)
                                                .addComponent(uploadPhotoBut)
                                                .addComponent(deletePhotoBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deletePhotoBut, downloadPhotoBut, getPhotoListBut, uploadPhotoBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getPhotoListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getPhotoListButActionPerformed
	{//GEN-HEADEREND:event_getPhotoListButActionPerformed

		if ( currentPhotoElements == null )
		{
			currentPhotoElements = new Vector<PhoneElement> (1);
		}

		mw.putListInTable ("PICTURES",	// NOI18N
			(DefaultTableModel) photoTable.getModel (),
			currentPhotoElements);
	}//GEN-LAST:event_getPhotoListButActionPerformed

	private void downloadPhotoButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadPhotoButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadPhotoButdownloadButActionPerformed

		int[] selectedRows = photoTable.getSelectedRows ();
		if ( selectedRows == null || currentPhotoElements == null )
		{
			return;
		}
		if ( selectedRows.length == 0 )
		{
			return;
		}

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

		int dialogRes = downloadFC.showSaveDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			String dstFileName = null;
			try
			{
				File destDir = downloadFC.getSelectedFile ();
				if ( destDir == null )
				{
					return;
				}
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				final AtomicInteger threads = new AtomicInteger (0);
				TransferParameters tp = getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					String fullFileName =
						currentPhotoElements.get (toGet).getFilename ()
						+ "." 	// NOI18N
						+ currentPhotoElements.get (toGet).getExt ();
					dstFileName = destDir.getAbsolutePath ()
						+ File.separator
						+ fullFileName;
					final File received = new File (dstFileName);
					if ( received.exists () )
					{
						int res = JOptionPane.showConfirmDialog (
							this,
							fullFileName
							+ " " +	// NOI18N
							exOverString, overwriteStr,
							JOptionPane.YES_NO_CANCEL_OPTION);
						if ( res != JOptionPane.YES_OPTION )
						{
							continue;
						}
					}
					if ( received.exists () && ! received.canWrite () )
					{
						JOptionPane.showMessageDialog (this,
							fileNotWriteMsg
							+ ": "	// NOI18N
							+ received.getName (),
							errString, JOptionPane.ERROR_MESSAGE);
						continue;
					}
					Utils.updateStatusLabel (status, Utils.STATUS.RECEIVING);
					threads.incrementAndGet ();
					TransferUtils.downloadFile (received,
						currentPhotoElements.get (toGet),
						tp,
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
					}, this, false, false, false);
				} // for
			}
			catch (Exception ex)
			{
				Utils.updateStatusLabel (status, Utils.STATUS.READY);
				Utils.handleException (ex, "Download"	// NOI18N
					+ ((dstFileName != null)? ": " + dstFileName : ""));	// NOI18N
			}
		}
	}//GEN-LAST:event_downloadPhotoButdownloadButActionPerformed

	private void uploadPhotoButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadPhotoButActionPerformed
	{//GEN-HEADEREND:event_uploadPhotoButActionPerformed

		if ( uploadPictFC == null )
		{
			uploadPictFC = Utils.createOpenFileChooser (
				picsString, Utils.getPhotofileIDs ());
		}

		int dialogRes = uploadPictFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadPictFC.getSelectedFile ();
			if ( res == null )
			{
				return;
			}
			mw.dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadPhotoButActionPerformed

	private void deletePhotoButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deletePhotoButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deletePhotoButdeleteButActionPerformed

		int[] selectedRows = photoTable.getSelectedRows ();
		if ( selectedRows == null || currentPhotoElements == null )
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
				TransferParameters tp = getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					threads.incrementAndGet ();
					TransferUtils.deleteFile (
						currentPhotoElements.get (toGet),
						tp,
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
					}, this, false, false, false);
				}
			}
			catch (Exception ex)
			{
				Utils.updateStatusLabel (status, Utils.STATUS.READY);
				Utils.handleException (ex, "delete");	// NOI18N
			}
		}
	}//GEN-LAST:event_deletePhotoButdeleteButActionPerformed

	private TransferParameters getTransferParameters ()
	{
		return new TransferParameters (
			portCombo, speedCombo, dataBitsCombo, stopBitsCombo,
			parityCombo, flowSoft, flowHard, sync);
	}

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
		destDirName = destDir;
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
		photoTable.setTransferHandler (new JYMAGTransferHandler (
			portCombo, speedCombo, dataBitsCombo, stopBitsCombo,
			parityCombo, flowSoft, flowHard, sync, mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton deletePhotoBut;
        private javax.swing.JButton downloadPhotoBut;
        private javax.swing.JButton getPhotoListBut;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable photoTable;
        private javax.swing.JButton uploadPhotoBut;
        // End of variables declaration//GEN-END:variables

}
