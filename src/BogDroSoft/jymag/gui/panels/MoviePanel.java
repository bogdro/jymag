/*
 * MoviePanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2013-2018 Bogdan Drozdowski, bogdandr (at) op.pl
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
 * This class is the movies'/animations' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MoviePanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 86L;

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

	private volatile MainWindow mw;

	private Vector<PhoneElement> currentAnimElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadAnimFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String exOverString = mwBundle.getString("_exists._Overwrite");
	private static final String overwriteStr = mwBundle.getString("Overwrite?");
	private static final String errString = mwBundle.getString("Error");
	private static final String fileNotWriteMsg = mwBundle.getString("Cant_write_to_file");
	private static final String animString = mwBundle.getString("Supported_animation/video_files");
	private static final String deleteQuestion = mwBundle.getString("want_to_delete");
	private static final String questionString = mwBundle.getString("Question");


	/** Creates new form MoviePanel */
	public MoviePanel()
	{
		initComponents();

		animTable.setFillsViewportHeight (true);
		animTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		animTable.getTableHeader ().addMouseListener (
			new Utils.TableMouseListener (animTable));
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

                deleteAnimBut = new javax.swing.JButton();
                uploadAnimBut = new javax.swing.JButton();
                downloadAnimBut = new javax.swing.JButton();
                getAnimListBut = new javax.swing.JButton();
                jScrollPane6 = new javax.swing.JScrollPane();
                animTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(171, 171, 255));

                deleteAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                deleteAnimBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteAnimBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                deleteAnimButdeleteButActionPerformed(evt);
                        }
                });

                uploadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadAnimBut.setText(bundle.getString("Upload")); // NOI18N
                uploadAnimBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadAnimButActionPerformed(evt);
                        }
                });

                downloadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadAnimBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadAnimBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadAnimButdownloadButActionPerformed(evt);
                        }
                });

                getAnimListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
                getAnimListBut.setText(bundle.getString("Get_list")); // NOI18N
                getAnimListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getAnimListButActionPerformed(evt);
                        }
                });

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

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getAnimListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadAnimBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadAnimBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteAnimBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getAnimListBut)
                                                .addComponent(downloadAnimBut)
                                                .addComponent(uploadAnimBut)
                                                .addComponent(deleteAnimBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteAnimBut, downloadAnimBut, getAnimListBut, uploadAnimBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getAnimListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getAnimListButActionPerformed
	{//GEN-HEADEREND:event_getAnimListButActionPerformed
		if ( currentAnimElements == null )
		{
			currentAnimElements = new Vector<PhoneElement> (1);
		}

		mw.putListInTable ("ANIMATIONS",	// NOI18N
			(DefaultTableModel) animTable.getModel (),
			currentAnimElements);
	}//GEN-LAST:event_getAnimListButActionPerformed

	private void downloadAnimButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadAnimButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadAnimButdownloadButActionPerformed

		int[] selectedRows = animTable.getSelectedRows ();
		if ( selectedRows == null || currentAnimElements == null )
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
						currentAnimElements.get (toGet).getFilename ()
						+ "." 	// NOI18N
						+ currentAnimElements.get (toGet).getExt ();
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
					mw.setReceivingStatus ();
					threads.incrementAndGet ();
					TransferUtils.downloadFile (received,
						currentAnimElements.get (toGet),
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
								mw.setReadyStatus ();
							}
						}

						@Override
						public String toString ()
						{
							return "MoviePanel.downloadAnimButdownloadButActionPerformed.Runnable";	// NOI18N
						}
					}, this, false, false, false);
				} // for
			}
			catch (Exception ex)
			{
				mw.setReadyStatus ();
				Utils.handleException (ex, "Download"	// NOI18N
					+ ((dstFileName != null)? ": " + dstFileName : ""));	// NOI18N
			}
		}
	}//GEN-LAST:event_downloadAnimButdownloadButActionPerformed

	private void uploadAnimButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadAnimButActionPerformed
	{//GEN-HEADEREND:event_uploadAnimButActionPerformed

		if ( uploadAnimFC == null )
		{
			uploadAnimFC = Utils.createOpenFileChooser (
				animString, Utils.getAnimfileIDs ());
		}

		int dialogRes = uploadAnimFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadAnimFC.getSelectedFile ();
			if ( res == null )
			{
				return;
			}
			mw.dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadAnimButActionPerformed

	private void deleteAnimButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteAnimButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteAnimButdeleteButActionPerformed

		int[] selectedRows = animTable.getSelectedRows ();
		if ( selectedRows == null || currentAnimElements == null )
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
				mw.setSendingStatus ();
				progressBar.setValue (0);
				progressBar.setMinimum (0);
				progressBar.setMaximum (selectedRows.length);
				TransferParameters tp = getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					threads.incrementAndGet ();
					TransferUtils.deleteFile (
						currentAnimElements.get (toGet),
						tp,
						new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							progressBar.setValue (progressBar.getValue () + 1);
							if ( threads.decrementAndGet () == 0 )
							{
								mw.setReadyStatus ();
							}
						}

						@Override
						public String toString ()
						{
							return "MoviePanel.deleteAnimButdeleteButActionPerformed.Runnable";	// NOI18N
						}
					}, this, false, false, false);
				}
			}
			catch (Exception ex)
			{
				mw.setReadyStatus ();
				Utils.handleException (ex, "delete");	// NOI18N
			}
		}
	}//GEN-LAST:event_deleteAnimButdeleteButActionPerformed

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
		animTable.setTransferHandler (new JYMAGTransferHandler (
			portCombo, speedCombo, dataBitsCombo, stopBitsCombo,
			parityCombo, flowSoft, flowHard, sync, mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTable animTable;
        private javax.swing.JButton deleteAnimBut;
        private javax.swing.JButton downloadAnimBut;
        private javax.swing.JButton getAnimListBut;
        private javax.swing.JScrollPane jScrollPane6;
        private javax.swing.JButton uploadAnimBut;
        // End of variables declaration//GEN-END:variables

}
