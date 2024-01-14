/*
 * JavasPanel.java, part of the JYMAG package.
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

import BogDroSoft.jymag.PhoneElement;
import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.gui.JYMAGTransferHandler;
import BogDroSoft.jymag.gui.MainWindow;
import BogDroSoft.jymag.gui.UiUtils;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;

/**
 * This class is the Java elements' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class JavasPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 85L;

	private volatile MainWindow mw;

	private Vector<PhoneElement> currentJavaElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadJavaFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String JAVA_TYPES_STRING = mwBundle.getString("Supported_Java_files");

	/** Creates new form JavasPanel */
	public JavasPanel()
	{
		initComponents();

		javaTable.setFillsViewportHeight (true);
		javaTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		javaTable.getTableHeader ().addMouseListener (
			new UiUtils.TableMouseListener (javaTable));
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

                deleteJavaBut = new javax.swing.JButton();
                uploadJavaBut = new javax.swing.JButton();
                downloadJavaBut = new javax.swing.JButton();
                getJavaListBut = new javax.swing.JButton();
                jScrollPane14 = new javax.swing.JScrollPane();
                javaTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(122, 189, 255));

                deleteJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                deleteJavaBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteJavaBut.setEnabled(false);
                deleteJavaBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                deleteJavaButdeleteButActionPerformed(evt);
                        }
                });

                uploadJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadJavaBut.setText(bundle.getString("Upload")); // NOI18N
                uploadJavaBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                uploadJavaButActionPerformed(evt);
                        }
                });

                downloadJavaBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadJavaBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadJavaBut.setEnabled(false);
                downloadJavaBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                downloadJavaButdownloadButActionPerformed(evt);
                        }
                });

                getJavaListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/get-list.png"))); // NOI18N
                getJavaListBut.setText(bundle.getString("Get_list")); // NOI18N
                getJavaListBut.setEnabled(false);
                getJavaListBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getJavaListButActionPerformed(evt);
                        }
                });

                jScrollPane14.setPreferredSize(new java.awt.Dimension(0, 0));

                javaTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][]
                        {

                        },
                        new String []
                        {
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
                        }
                )
                {
                        private static final long serialVersionUID = 74L;
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
                jScrollPane14.setViewportView(javaTable);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1012, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getJavaListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadJavaBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadJavaBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteJavaBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 265, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getJavaListBut)
                                                .addComponent(downloadJavaBut)
                                                .addComponent(uploadJavaBut)
                                                .addComponent(deleteJavaBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteJavaBut, downloadJavaBut, getJavaListBut, uploadJavaBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getJavaListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getJavaListButActionPerformed
	{//GEN-HEADEREND:event_getJavaListButActionPerformed
		if ( currentJavaElements == null )
		{
			currentJavaElements = new Vector<PhoneElement> (1);
		}

		/* TODO
		PanelUtils.putListInTable (mw, "JAR",	// NOI18N
			(DefaultTableModel) javaTable.getModel (),
			currentJavaElements);*/
	}//GEN-LAST:event_getJavaListButActionPerformed

	private void downloadJavaButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadJavaButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadJavaButdownloadButActionPerformed

		if ( downloadFC == null )
		{
			downloadFC = new JFileChooser ();
		}

		PanelUtils.download(mw, javaTable.getSelectedRows (),
			currentJavaElements, downloadFC, destDirName);
	}//GEN-LAST:event_downloadJavaButdownloadButActionPerformed

	private void uploadJavaButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadJavaButActionPerformed
	{//GEN-HEADEREND:event_uploadJavaButActionPerformed

		if ( uploadJavaFC == null )
		{
			uploadJavaFC = UiUtils.createOpenFileChooser (
				JAVA_TYPES_STRING, Utils.getJavafileIDs ());
		}

		PanelUtils.upload(mw, uploadJavaFC);
	}//GEN-LAST:event_uploadJavaButActionPerformed

	private void deleteJavaButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteJavaButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteJavaButdeleteButActionPerformed
		
		PanelUtils.delete(mw, javaTable.getSelectedRows (),
			currentJavaElements);
	}//GEN-LAST:event_deleteJavaButdeleteButActionPerformed

	@Override
	public void setDestDir (String destDir)
	{
		destDirName = destDir;
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
		javaTable.setTransferHandler (new JYMAGTransferHandler (mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton deleteJavaBut;
        private javax.swing.JButton downloadJavaBut;
        private javax.swing.JButton getJavaListBut;
        private javax.swing.JScrollPane jScrollPane14;
        private javax.swing.JTable javaTable;
        private javax.swing.JButton uploadJavaBut;
        // End of variables declaration//GEN-END:variables

}
