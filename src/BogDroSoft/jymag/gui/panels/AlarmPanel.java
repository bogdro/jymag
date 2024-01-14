/*
 * AlarmPanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2013-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
 * License: GNU General Public License, v3+
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package BogDroSoft.jymag.gui.panels;

import BogDroSoft.jymag.PhoneAlarm;
import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.comm.TransferParameters;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.gui.MainWindow;
import BogDroSoft.jymag.gui.UiUtils;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.table.TableModel;

/**
 * This class is the alarms' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class AlarmPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 83L;

	private volatile MainWindow mw;

	private Vector<PhoneAlarm> currentAlarmElements;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String DELETE_QUESTION = mwBundle.getString("want_to_delete");
	private static final String QUESTION_STRING = mwBundle.getString("Question");

	/** Creates new form AlarmPanel. */
	public AlarmPanel()
	{
		initComponents();

		/* Make clicking on the table header select all the table's rows */
		alarmTable.getTableHeader ().addMouseListener (
			new UiUtils.TableMouseListener (alarmTable));
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

                deleteAlarmBut = new javax.swing.JButton();
                uploadAlarmBut = new javax.swing.JButton();
                downloadAlarmBut = new javax.swing.JButton();
                getAlarmListBut = new javax.swing.JButton();
                jScrollPane17 = new javax.swing.JScrollPane();
                alarmTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(255, 175, 175));

                deleteAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                deleteAlarmBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteAlarmBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                deleteAlarmButdeleteButActionPerformed(evt);
                        }
                });

                uploadAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadAlarmBut.setText(bundle.getString("Update_sel")); // NOI18N
                uploadAlarmBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                uploadAlarmButActionPerformed(evt);
                        }
                });

                downloadAlarmBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadAlarmBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadAlarmBut.setEnabled(false);
                downloadAlarmBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                downloadAlarmButdownloadButActionPerformed(evt);
                        }
                });

                getAlarmListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/get-list.png"))); // NOI18N
                getAlarmListBut.setText(bundle.getString("Get_list")); // NOI18N
                getAlarmListBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getAlarmListButActionPerformed(evt);
                        }
                });

                alarmTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][]
                        {

                        },
                        new String []
                        {
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_number"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_date"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_time"),
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Alarm_days")
                        }
                )
                {
                        private static final long serialVersionUID = 76L;
                        Class[] types = new Class []
                        {
                                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean []
                        {
                                false, true, true, true
                        };

                        public Class getColumnClass(int columnIndex)
                        {
                                return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex)
                        {
                                return canEdit [columnIndex];
                        }
                }
        );
        jScrollPane17.setViewportView(alarmTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 1012, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(getAlarmListBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downloadAlarmBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(uploadAlarmBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deleteAlarmBut)))
                                .addContainerGap()))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 265, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(downloadAlarmBut)
                                        .addComponent(uploadAlarmBut)
                                        .addComponent(deleteAlarmBut)
                                        .addComponent(getAlarmListBut))
                                .addGap(25, 25, 25)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteAlarmBut, downloadAlarmBut, getAlarmListBut, uploadAlarmBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getAlarmListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getAlarmListButActionPerformed
	{//GEN-HEADEREND:event_getAlarmListButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			mw.setReceivingStatus ();
			mw.setProgressCurrentValue (0);
			mw.setProgressMinimumValue (0);
			mw.setProgressMaximumValue (1);
			if ( currentAlarmElements == null )
			{
				currentAlarmElements = new Vector<PhoneAlarm> (10);
			}

			TransferUtils.downloadAlarmList (tp,
				new Runnable ()
			{
				@Override
				public synchronized void run ()
				{
					mw.setProgressCurrentValue (1);
					mw.setReadyStatus ();
				}

				@Override
				public String toString ()
				{
					return "AlarmPanel.getAlarmListButActionPerformed.Runnable";	// NOI18N
				}
			}, this, false, false, false, alarmTable, currentAlarmElements);
		}
		catch (Exception ex)
		{
			mw.setReadyStatus ();
			Utils.handleException (ex, "getAlarmList");	// NOI18N
		}
	}//GEN-LAST:event_getAlarmListButActionPerformed

	private void downloadAlarmButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadAlarmButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadAlarmButdownloadButActionPerformed


	}//GEN-LAST:event_downloadAlarmButdownloadButActionPerformed

	private void uploadAlarmButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadAlarmButActionPerformed
	{//GEN-HEADEREND:event_uploadAlarmButActionPerformed

		try
		{
			int[] rows = alarmTable.getSelectedRows ();
			TableModel model = alarmTable.getModel ();
			if ( rows == null || model == null )
			{
				return;
			}
			if ( rows.length == 0 )
			{
				return;
			}
			final Vector<PhoneAlarm> toUpload = new Vector<PhoneAlarm> (rows.length);
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
			mw.setSendingStatus ();
			mw.setProgressCurrentValue (0);
			mw.setProgressMinimumValue (0);
			mw.setProgressMaximumValue (toUpload.size ());
			TransferParameters tp = mw.getTransferParameters ();
			for ( int i = 0; i < toUpload.size (); i++ )
			{
				TransferUtils.uploadAlarm (toUpload.get (i),
					tp,
					new Runnable ()
				{
					@Override
					public synchronized void run ()
					{
						mw.setProgressCurrentValue (
							mw.getProgressCurrentValue() + 1);
						if ( mw.getProgressCurrentValue() == toUpload.size () )
						{
							mw.setReadyStatus ();
						}
					}

					@Override
					public String toString ()
					{
						return "AlarmPanel.uploadAlarmButActionPerformed.Runnable";	// NOI18N
					}
				}, this, false, false, false);
			}
		}
		catch (Exception ex)
		{
			mw.setReadyStatus ();
			Utils.handleException (ex, "uploadAlarm");	// NOI18N
		}
	}//GEN-LAST:event_uploadAlarmButActionPerformed

	private void deleteAlarmButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteAlarmButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteAlarmButdeleteButActionPerformed

		int[] selectedRows = alarmTable.getSelectedRows ();
		if ( selectedRows == null || currentAlarmElements == null )
		{
			return;
		}
		if ( selectedRows.length == 0 )
		{
			return;
		}

		int dialogRes = JOptionPane.showConfirmDialog (this,
			DELETE_QUESTION,
			QUESTION_STRING,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
			);

		if ( dialogRes == JOptionPane.YES_OPTION )
		{
			try
			{
				final AtomicInteger threads = new AtomicInteger (0);
				mw.setSendingStatus ();
				mw.setProgressCurrentValue (0);
				mw.setProgressMinimumValue (0);
				mw.setProgressMaximumValue (selectedRows.length);
				TransferParameters tp = mw.getTransferParameters ();
				for ( int i = 0; i < selectedRows.length; i++ )
				{
					final int toGet = selectedRows[i];
					threads.incrementAndGet ();
					TransferUtils.deleteAlarm (toGet+1,
						tp,
						new Runnable ()
					{
						@Override
						public synchronized void run ()
						{
							mw.setProgressCurrentValue (
								mw.getProgressCurrentValue() + 1);
							if ( threads.decrementAndGet () == 0 )
							{
								mw.setReadyStatus ();
							}
						}

						@Override
						public String toString ()
						{
							return "AlarmPanel.deleteAlarmButdeleteButActionPerformed.Runnable";	// NOI18N
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
	}//GEN-LAST:event_deleteAlarmButdeleteButActionPerformed

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
		// not needed
	}


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTable alarmTable;
        private javax.swing.JButton deleteAlarmBut;
        private javax.swing.JButton downloadAlarmBut;
        private javax.swing.JButton getAlarmListBut;
        private javax.swing.JScrollPane jScrollPane17;
        private javax.swing.JButton uploadAlarmBut;
        // End of variables declaration//GEN-END:variables

}
