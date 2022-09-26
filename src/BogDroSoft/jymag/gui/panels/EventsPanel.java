/*
 * EventsPanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2013-2020 Bogdan Drozdowski, bogdandr (at) op.pl
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
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

/**
 * This class is the addressbook panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class EventsPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 84L;

        private JProgressBar progressBar;
	private volatile MainWindow mw;

	private Vector<PhoneElement> currentEventElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadEventFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String exOverString = mwBundle.getString("_exists._Overwrite");
	private static final String overwriteStr = mwBundle.getString("Overwrite?");
	private static final String errString = mwBundle.getString("Error");
	private static final String fileNotWriteMsg = mwBundle.getString("Cant_write_to_file");
	private static final String eventString = mwBundle.getString("Supported_event_and_task_files");
	private static final String deleteQuestion = mwBundle.getString("want_to_delete");
	private static final String questionString = mwBundle.getString("Question");

	/** Creates new form EventsPanel. */
	public EventsPanel()
	{
		initComponents();

		eventTable.setFillsViewportHeight (true);
		eventTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		eventTable.getTableHeader ().addMouseListener (
			new Utils.TableMouseListener (eventTable));
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

                deleteEventBut = new javax.swing.JButton();
                uploadEventBut = new javax.swing.JButton();
                downloadEventBut = new javax.swing.JButton();
                getEventListBut = new javax.swing.JButton();
                jScrollPane5 = new javax.swing.JScrollPane();
                eventTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(219, 218, 156));

                deleteEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                deleteEventBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                deleteEventButdeleteButActionPerformed(evt);
                        }
                });

                uploadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadEventBut.setText(bundle.getString("Upload")); // NOI18N
                uploadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                uploadEventButActionPerformed(evt);
                        }
                });

                downloadEventBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadEventBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadEventBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                downloadEventButdownloadButActionPerformed(evt);
                        }
                });

                getEventListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/list.png"))); // NOI18N
                getEventListBut.setText(bundle.getString("Get_list")); // NOI18N
                getEventListBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getEventListButActionPerformed(evt);
                        }
                });

                jScrollPane5.setPreferredSize(new java.awt.Dimension(0, 0));

                eventTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
                        }
                ) {
                        private static final long serialVersionUID = 70L;
                        Class<?>[] types = new Class<?> [] {
                                java.lang.String.class
                        };
                        boolean[] canEdit = new boolean [] {
                                false
                        };

                        public Class<?> getColumnClass(int columnIndex) {
                                return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit [columnIndex];
                        }
                });
                jScrollPane5.setViewportView(eventTable);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getEventListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadEventBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadEventBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteEventBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getEventListBut)
                                                .addComponent(downloadEventBut)
                                                .addComponent(uploadEventBut)
                                                .addComponent(deleteEventBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteEventBut, downloadEventBut, getEventListBut, uploadEventBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getEventListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getEventListButActionPerformed
	{//GEN-HEADEREND:event_getEventListButActionPerformed

		if ( currentEventElements == null )
		{
			currentEventElements = new Vector<PhoneElement> (1);
		}

		mw.putListInTable ("VEVENT",	// NOI18N
			(DefaultTableModel) eventTable.getModel (),
			currentEventElements);
	}//GEN-LAST:event_getEventListButActionPerformed

	private void downloadEventButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadEventButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadEventButdownloadButActionPerformed

		int[] selectedRows = eventTable.getSelectedRows ();
		if ( selectedRows == null || currentEventElements == null )
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
				TransferParameters tp = mw.getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					String fullFileName =
						currentEventElements.get (toGet).getFilename ()
						+ "." 	// NOI18N
						+ currentEventElements.get (toGet).getExt ();
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
						currentEventElements.get (toGet),
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
							return "EventsPanel.downloadEventButdownloadButActionPerformed.Runnable";	// NOI18N
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
	}//GEN-LAST:event_downloadEventButdownloadButActionPerformed

	private void uploadEventButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadEventButActionPerformed
	{//GEN-HEADEREND:event_uploadEventButActionPerformed

		if ( uploadEventFC == null )
		{
			uploadEventFC = Utils.createOpenFileChooser (
				eventString, Utils.getEventfileIDs ());
		}

		int dialogRes = uploadEventFC.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			File res = uploadEventFC.getSelectedFile ();
			if ( res == null )
			{
				return;
			}
			mw.dynamicUpload (res);
		}
	}//GEN-LAST:event_uploadEventButActionPerformed

	private void deleteEventButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteEventButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteEventButdeleteButActionPerformed

		int[] selectedRows = eventTable.getSelectedRows ();
		if ( selectedRows == null || currentEventElements == null )
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
				TransferParameters tp = mw.getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					threads.incrementAndGet ();
					TransferUtils.deleteFile (
						currentEventElements.get (toGet),
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
							return "EventsPanel.deleteEventButdeleteButActionPerformed.Runnable";	// NOI18N
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
	}//GEN-LAST:event_deleteEventButdeleteButActionPerformed

	@Override
	public void setProgressBar (JProgressBar mainProgressBar)
	{
		progressBar = mainProgressBar;
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
		eventTable.setTransferHandler (new JYMAGTransferHandler (mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton deleteEventBut;
        private javax.swing.JButton downloadEventBut;
        private javax.swing.JTable eventTable;
        private javax.swing.JButton getEventListBut;
        private javax.swing.JScrollPane jScrollPane5;
        private javax.swing.JButton uploadEventBut;
        // End of variables declaration//GEN-END:variables

}
