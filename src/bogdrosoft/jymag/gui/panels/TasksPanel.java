/*
 * TasksPanel.java, part of the JYMAG package.
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

package bogdrosoft.jymag.gui.panels;

import bogdrosoft.jymag.PhoneElement;
import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.gui.JYMAGTransferHandler;
import bogdrosoft.jymag.gui.MainWindow;
import bogdrosoft.jymag.gui.UiUtils;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

/**
 * This class is the tasks' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class TasksPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 90L;

	private volatile MainWindow mw;

	private Vector<PhoneElement> currentTodoElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadTODOFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle MSGS
		= ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow");
	private static final String TODO_TYPES_STRING
		= MSGS.getString("Supported_to-do_files");

	/** Creates new form TasksPanel */
	public TasksPanel()
	{
		initComponents();

		todoTable.setFillsViewportHeight (true);
		todoTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		todoTable.getTableHeader ().addMouseListener (
			new UiUtils.TableMouseListener (todoTable));
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

                deleteTodoBut = new javax.swing.JButton();
                uploadTodoBut = new javax.swing.JButton();
                downloadTodoBut = new javax.swing.JButton();
                getTodoListBut = new javax.swing.JButton();
                jScrollPane4 = new javax.swing.JScrollPane();
                todoTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(127, 196, 127));

                deleteTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow"); // NOI18N
                deleteTodoBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteTodoBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                deleteTodoButdeleteButActionPerformed(evt);
                        }
                });

                uploadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadTodoBut.setText(bundle.getString("Upload")); // NOI18N
                uploadTodoBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                uploadTodoButActionPerformed(evt);
                        }
                });

                downloadTodoBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/download.png"))); // NOI18N
                downloadTodoBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadTodoBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                downloadTodoButdownloadButActionPerformed(evt);
                        }
                });

                getTodoListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/get-list.png"))); // NOI18N
                getTodoListBut.setText(bundle.getString("Get_list")); // NOI18N
                getTodoListBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getTodoListButActionPerformed(evt);
                        }
                });

                jScrollPane4.setPreferredSize(new java.awt.Dimension(0, 0));

                todoTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][]
                        {

                        },
                        new String []
                        {
                                java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow").getString("Name")
                        }
                )
                {
                        private static final long serialVersionUID = 69L;
                        Class[] types = new Class []
                        {
                                java.lang.String.class
                        };
                        boolean[] canEdit = new boolean []
                        {
                                false
                        };

                        public Class getColumnClass(int columnIndex)
                        {
                                return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex)
                        {
                                return canEdit [columnIndex];
                        }
                });
                jScrollPane4.setViewportView(todoTable);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getTodoListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadTodoBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadTodoBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteTodoBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 265, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getTodoListBut)
                                                .addComponent(downloadTodoBut)
                                                .addComponent(uploadTodoBut)
                                                .addComponent(deleteTodoBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteTodoBut, downloadTodoBut, getTodoListBut, uploadTodoBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getTodoListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getTodoListButActionPerformed
	{//GEN-HEADEREND:event_getTodoListButActionPerformed

		if ( currentTodoElements == null )
		{
			currentTodoElements = new Vector<PhoneElement> (1);
		}

		PanelUtils.putListInTable (mw, "VTODO",	// NOI18N
			(DefaultTableModel) todoTable.getModel (),
			currentTodoElements);
	}//GEN-LAST:event_getTodoListButActionPerformed

	private void downloadTodoButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadTodoButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadTodoButdownloadButActionPerformed

		if ( downloadFC == null )
		{
			downloadFC = new JFileChooser ();
		}

		PanelUtils.download(mw, todoTable.getSelectedRows (),
			currentTodoElements, downloadFC, destDirName);
	}//GEN-LAST:event_downloadTodoButdownloadButActionPerformed

	private void uploadTodoButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadTodoButActionPerformed
	{//GEN-HEADEREND:event_uploadTodoButActionPerformed

		if ( uploadTODOFC == null )
		{
			uploadTODOFC = UiUtils.createOpenFileChooser (
				TODO_TYPES_STRING, Utils.getTodofileIDs ());
		}

		PanelUtils.upload(mw, uploadTODOFC);
	}//GEN-LAST:event_uploadTodoButActionPerformed

	private void deleteTodoButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteTodoButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteTodoButdeleteButActionPerformed
		
		PanelUtils.delete(mw, todoTable.getSelectedRows (),
			currentTodoElements);
	}//GEN-LAST:event_deleteTodoButdeleteButActionPerformed

	@Override
	public void setDestDir (String destDir)
	{
		destDirName = destDir;
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
		todoTable.setTransferHandler (new JYMAGTransferHandler (mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton deleteTodoBut;
        private javax.swing.JButton downloadTodoBut;
        private javax.swing.JButton getTodoListBut;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JTable todoTable;
        private javax.swing.JButton uploadTodoBut;
        // End of variables declaration//GEN-END:variables

}
