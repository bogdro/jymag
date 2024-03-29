/*
 * MoviePanel.java, part of the JYMAG package.
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
 * This class is the movies'/animations' panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class MoviePanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 86L;

	private transient MainWindow mw;

	private Vector<PhoneElement> currentAnimElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadAnimFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle MSGS
		= ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow");
	private static final String ANIM_VIDEO_TYPES_STRING
		= MSGS.getString("Supported_animation/video_files");

	/** Creates new form MoviePanel */
	public MoviePanel()
	{
		initComponents();

		animTable.setFillsViewportHeight (true);
		animTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		animTable.getTableHeader ().addMouseListener (
			new UiUtils.TableMouseListener (animTable));
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

                deleteAnimBut = new javax.swing.JButton();
                uploadAnimBut = new javax.swing.JButton();
                downloadAnimBut = new javax.swing.JButton();
                getAnimListBut = new javax.swing.JButton();
                jScrollPane6 = new javax.swing.JScrollPane();
                animTable = new javax.swing.JTable();

                setBackground(new java.awt.Color(171, 171, 255));

                deleteAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/delete.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow"); // NOI18N
                deleteAnimBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteAnimBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                deleteAnimButdeleteButActionPerformed(evt);
                        }
                });

                uploadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadAnimBut.setText(bundle.getString("Upload")); // NOI18N
                uploadAnimBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                uploadAnimButActionPerformed(evt);
                        }
                });

                downloadAnimBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/download.png"))); // NOI18N
                downloadAnimBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadAnimBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                downloadAnimButdownloadButActionPerformed(evt);
                        }
                });

                getAnimListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/get-list.png"))); // NOI18N
                getAnimListBut.setText(bundle.getString("Get_list")); // NOI18N
                getAnimListBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getAnimListButActionPerformed(evt);
                        }
                });

                jScrollPane6.setPreferredSize(new java.awt.Dimension(0, 0));

                animTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][]
                        {

                        },
                        new String []
                        {
                                java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow").getString("Name")
                        }
                )
                {
                        private static final long serialVersionUID = 73L;
                        Class[] types = new Class []
                        {
                                java.lang.String.class
                        };
                        boolean[] canEdit = new boolean []
                        {
                                false
                        };

                        @Override
                        public Class getColumnClass(int columnIndex)
                        {
                                return types [columnIndex];
                        }

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex)
                        {
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
                        .addGap(0, 265, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
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

		PanelUtils.putListInTable (mw, "ANIMATIONS",	// NOI18N
			(DefaultTableModel) animTable.getModel (),
			currentAnimElements);
	}//GEN-LAST:event_getAnimListButActionPerformed

	private void downloadAnimButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadAnimButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadAnimButdownloadButActionPerformed

		if ( downloadFC == null )
		{
			downloadFC = new JFileChooser ();
		}

		PanelUtils.download(mw, animTable.getSelectedRows (),
			currentAnimElements, downloadFC, destDirName);
	}//GEN-LAST:event_downloadAnimButdownloadButActionPerformed

	private void uploadAnimButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadAnimButActionPerformed
	{//GEN-HEADEREND:event_uploadAnimButActionPerformed

		if ( uploadAnimFC == null )
		{
			uploadAnimFC = UiUtils.createOpenFileChooser (
				ANIM_VIDEO_TYPES_STRING, Utils.getAnimfileIDs ());
		}

		PanelUtils.upload(mw, uploadAnimFC);
	}//GEN-LAST:event_uploadAnimButActionPerformed

	private void deleteAnimButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteAnimButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteAnimButdeleteButActionPerformed
		
		PanelUtils.delete(mw, animTable.getSelectedRows (),
			currentAnimElements);
	}//GEN-LAST:event_deleteAnimButdeleteButActionPerformed

	@Override
	public void setDestDir (String destDir)
	{
		destDirName = destDir;
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
		animTable.setTransferHandler (new JYMAGTransferHandler (mw));
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
