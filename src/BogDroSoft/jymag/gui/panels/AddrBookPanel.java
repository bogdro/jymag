/*
 * AddrBookPanel.java, part of the JYMAG package.
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
import javax.swing.table.DefaultTableModel;

/**
 * This class is the addressbook panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class AddrBookPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 82L;

	private volatile MainWindow mw;

	private Vector<PhoneElement> currentAddrElements;
	private JFileChooser downloadFC;
	private JFileChooser uploadAddrFC;
	private String destDirName;

	// i18n stuff:
	private static final ResourceBundle mwBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow");
	private static final String ADDRESSBOOK_STRING = mwBundle.getString("Supported_addressbook_files");

	/** Creates new form AddrBookPanel. */
	public AddrBookPanel()
	{
		initComponents();

		addrTable.setFillsViewportHeight (true);
		addrTable.setDragEnabled (true);
		/* Make clicking on the table header select all the table's rows */
		addrTable.getTableHeader ().addMouseListener (
			new UiUtils.TableMouseListener (addrTable));
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

                jScrollPane3 = new javax.swing.JScrollPane();
                addrTable = new javax.swing.JTable();
                getAddrListBut = new javax.swing.JButton();
                downloadAddrBut = new javax.swing.JButton();
                uploadAddrBut = new javax.swing.JButton();
                deleteAddrBut = new javax.swing.JButton();

                setBackground(new java.awt.Color(255, 255, 193));

                jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 0));

                addrTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][]
                        {

                        },
                        new String []
                        {
                                java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Name")
                        }
                )
                {
                        private static final long serialVersionUID = 68L;
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
                jScrollPane3.setViewportView(addrTable);

                getAddrListBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/get-list.png"))); // NOI18N
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow"); // NOI18N
                getAddrListBut.setText(bundle.getString("Get_list")); // NOI18N
                getAddrListBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getAddrListButActionPerformed(evt);
                        }
                });

                downloadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/download.png"))); // NOI18N
                downloadAddrBut.setText(bundle.getString("Download_selected")); // NOI18N
                downloadAddrBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                downloadAddrButdownloadButActionPerformed(evt);
                        }
                });

                uploadAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
                uploadAddrBut.setText(bundle.getString("Upload")); // NOI18N
                uploadAddrBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                uploadAddrButActionPerformed(evt);
                        }
                });

                deleteAddrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/delete.png"))); // NOI18N
                deleteAddrBut.setText(bundle.getString("Delete_selected")); // NOI18N
                deleteAddrBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                deleteAddrButdeleteButActionPerformed(evt);
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
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getAddrListBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(downloadAddrBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(uploadAddrBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(deleteAddrBut)))
                                        .addContainerGap()))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 265, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(getAddrListBut)
                                                .addComponent(downloadAddrBut)
                                                .addComponent(uploadAddrBut)
                                                .addComponent(deleteAddrBut))
                                        .addGap(25, 25, 25)))
                );

                layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {deleteAddrBut, downloadAddrBut, getAddrListBut, uploadAddrBut});

        }// </editor-fold>//GEN-END:initComponents

	private void getAddrListButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_getAddrListButActionPerformed
	{//GEN-HEADEREND:event_getAddrListButActionPerformed

		if ( currentAddrElements == null )
		{
			currentAddrElements = new Vector<PhoneElement> (1);
		}

		PanelUtils.putListInTable (mw, "VCARDS",	// NOI18N
			(DefaultTableModel) addrTable.getModel (),
			currentAddrElements);
	}//GEN-LAST:event_getAddrListButActionPerformed

	private void downloadAddrButdownloadButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_downloadAddrButdownloadButActionPerformed
	{//GEN-HEADEREND:event_downloadAddrButdownloadButActionPerformed

		if ( downloadFC == null )
		{
			downloadFC = new JFileChooser ();
		}

		PanelUtils.download(mw, addrTable.getSelectedRows (),
			currentAddrElements, downloadFC, destDirName);
	}//GEN-LAST:event_downloadAddrButdownloadButActionPerformed

	private void uploadAddrButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_uploadAddrButActionPerformed
	{//GEN-HEADEREND:event_uploadAddrButActionPerformed

		if ( uploadAddrFC == null )
		{
			uploadAddrFC = UiUtils.createOpenFileChooser (
				ADDRESSBOOK_STRING, Utils.getAddrfileIDs ());
		}

		PanelUtils.upload(mw, uploadAddrFC);
	}//GEN-LAST:event_uploadAddrButActionPerformed

	private void deleteAddrButdeleteButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteAddrButdeleteButActionPerformed
	{//GEN-HEADEREND:event_deleteAddrButdeleteButActionPerformed
		
		PanelUtils.delete(mw, addrTable.getSelectedRows (),
			currentAddrElements);
	}//GEN-LAST:event_deleteAddrButdeleteButActionPerformed

	@Override
	public void setDestDir (String destDir)
	{
		destDirName = destDir;
	}

	@Override
	public void setMainWindow (MainWindow mainWindow)
	{
		mw = mainWindow;
		addrTable.setTransferHandler (new JYMAGTransferHandler (mw));
	}

	@Override
	public void setFontSizeSpin (JSpinner fontSizeSpinner)
	{
		// not needed
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTable addrTable;
        private javax.swing.JButton deleteAddrBut;
        private javax.swing.JButton downloadAddrBut;
        private javax.swing.JButton getAddrListBut;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JButton uploadAddrBut;
        // End of variables declaration//GEN-END:variables

}
