/*
 * CapabilityWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2010 Bogdan Drozdowski, bogdandr (at) op.pl
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
 *              Free Software Foundation
 *              51 Franklin Street, Fifth Floor
 *              Boston, MA 02110-1301
 *              USA
 *
 */

package BogDroSoft.jymag;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JLabel;

/**
 * Capabilitywindow - a window that allows the user to see the phone's
 * capabilities.
 * @author Bogdan Drozdowski
 */
public class CapabilityWindow extends javax.swing.JDialog
{
	private static final long serialVersionUID = 75L;
	private DataTransporter dtr;
	private final Object sync;
	private final KL kl = new KL ();

	// ------------ i18n stuff
	private static String exString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/CapabilityWindow").getString("Exception");

	/**
	 * Creates new form CapabilityWindow.
	 * @param dt The DataTransporter instance to use for communications.
	 * Must already be open.
	 * @param parent The parent frame.
	 * @param synchro A synchronization variable.
	 * @param fontSize The font size for this window.
	 */
	public CapabilityWindow (DataTransporter dt, java.awt.Frame parent,
		Object synchro, float fontSize)
	{
		// make always modal
		super (parent, true);
		dtr = dt;
		sync = synchro;
		if ( dt == null || synchro == null )
		{
			dispose ();
			return;
		}

		initComponents ();

		abookCapRB.addKeyListener (kl);
		animCapRB.addKeyListener (kl);
		eventCapRB.addKeyListener (kl);
		pictCapRB.addKeyListener (kl);
		ringCapRB.addKeyListener (kl);
		todoCapRB.addKeyListener (kl);
		capabText.addKeyListener (kl);
		exitBut.addKeyListener (kl);
		getCapBut.addKeyListener (kl);
		jScrollPane1.addKeyListener (kl);
		jScrollPane2.addKeyListener (kl);
		jPanel1.addKeyListener (kl);
		fontSizeSpin.addKeyListener (kl);

		Dimension size = getSize ();
		if ( size != null )
		{
			size.height += 50;
			size.width += 50;
			setSize (size);
		}
		fontSizeSpin.setValue (fontSize);	// refresh the font in the window
		fontSizeLab.setHorizontalAlignment (JLabel.RIGHT);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                capabilityRadioGroup = new javax.swing.ButtonGroup();
                jScrollPane2 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                getCapBut = new javax.swing.JButton();
                exitBut = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                pictCapRB = new javax.swing.JRadioButton();
                ringCapRB = new javax.swing.JRadioButton();
                abookCapRB = new javax.swing.JRadioButton();
                todoCapRB = new javax.swing.JRadioButton();
                eventCapRB = new javax.swing.JRadioButton();
                animCapRB = new javax.swing.JRadioButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                capabText = new javax.swing.JTextArea();
                fontSizeSpin = new javax.swing.JSpinner();
                fontSizeLab = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/CapabilityWindow"); // NOI18N
                setTitle(bundle.getString("JYMAG_-_Capabilities")); // NOI18N

                getCapBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/capab.png"))); // NOI18N
                getCapBut.setText(bundle.getString("Get_selected_capabilities")); // NOI18N
                getCapBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                getCapButActionPerformed(evt);
                        }
                });

                exitBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/exit.png"))); // NOI18N
                exitBut.setText(bundle.getString("Exit")); // NOI18N
                exitBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exitButActionPerformed(evt);
                        }
                });

                jLabel1.setText(bundle.getString("Available_capabilities:")); // NOI18N

                capabilityRadioGroup.add(pictCapRB);
                pictCapRB.setSelected(true);
                pictCapRB.setText(bundle.getString("Pictures")); // NOI18N

                capabilityRadioGroup.add(ringCapRB);
                ringCapRB.setText(bundle.getString("Ringtones")); // NOI18N

                capabilityRadioGroup.add(abookCapRB);
                abookCapRB.setText(bundle.getString("Addressbook")); // NOI18N

                capabilityRadioGroup.add(todoCapRB);
                todoCapRB.setText(bundle.getString("To-do")); // NOI18N

                capabilityRadioGroup.add(eventCapRB);
                eventCapRB.setText(bundle.getString("Events/tasks")); // NOI18N

                capabilityRadioGroup.add(animCapRB);
                animCapRB.setText(bundle.getString("Animation/videos")); // NOI18N

                capabText.setColumns(20);
                capabText.setEditable(false);
                capabText.setRows(5);
                jScrollPane1.setViewportView(capabText);
                capabText.getAccessibleContext().setAccessibleName("null");

                fontSizeSpin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
                fontSizeSpin.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                                fontSizeSpinStateChanged(evt);
                        }
                });

                fontSizeLab.setText(bundle.getString("font_size")); // NOI18N

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(269, 269, 269)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(exitBut, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(getCapBut, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(fontSizeLab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                                                .addComponent(jLabel1)
                                                .addComponent(animCapRB)
                                                .addComponent(eventCapRB)
                                                .addComponent(todoCapRB)
                                                .addComponent(abookCapRB)
                                                .addComponent(pictCapRB)
                                                .addComponent(ringCapRB))
                                        .addContainerGap()))
                );

                jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {exitBut, getCapBut});

                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(getCapBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exitBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fontSizeLab))
                                .addContainerGap(345, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pictCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ringCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(abookCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(todoCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eventCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(animCapRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                                        .addContainerGap()))
                );

                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N

                jScrollPane2.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		dispose ();
	}//GEN-LAST:event_exitButActionPerformed

	private void getCapButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCapButActionPerformed

		capabText.setText ("");					// NOI18N
		String rcvd = null;
		String type = null;
		if ( abookCapRB.isSelected () ) type = "VCARDS";	// NOI18N
		else if ( animCapRB.isSelected () ) type = "ANIMATIONS";// NOI18N
		else if ( eventCapRB.isSelected () ) type = "VEVENT";	// NOI18N
		else if ( pictCapRB.isSelected () ) type = "PICTURES";	// NOI18N
		else if ( ringCapRB.isSelected () ) type = "SOUNDS";	// NOI18N
		else if ( todoCapRB.isSelected () ) type = "VTODO";	// NOI18N
		else return;
		synchronized (sync)
		{
			rcvd = dtr.getCapabilities (type);
		}
		if ( rcvd != null )
		{
			capabText.setText (rcvd);
		}
		else
		{
			capabText.setText ("<"				// NOI18N
				+ exString
				+ ">\n");				// NOI18N
		}

		/*
		String cmd = "AT+KPSCAP=\"";				// NOI18N
		if ( abookCapRB.isSelected () ) cmd += "VCARDS";	// NOI18N
		else if ( animCapRB.isSelected () ) cmd += "ANIMATIONS";// NOI18N
		else if ( eventCapRB.isSelected () ) cmd += "VEVENT";	// NOI18N
		else if ( pictCapRB.isSelected () ) cmd += "PICTURES";	// NOI18N
		else if ( ringCapRB.isSelected () ) cmd += "SOUNDS";	// NOI18N
		else if ( todoCapRB.isSelected () ) cmd += "VTODO";	// NOI18N
		cmd += "\"";	// NOI18N
		try
		{
			String rcvd;
			synchronized (sync)
			{
				dtr.send ((cmd+"\r").getBytes ());	// NOI18N
				rcvd = "";				// NOI18N
				int trial = 0;
				do
				{
					byte[] recvdB = dtr.recv (null);
					if ( recvdB != null ) rcvd = new String (recvdB);
					trial++;
				} while (rcvd.trim ().equals ("")	// NOI18N
					&& trial < DataTransporter.MAX_TRIALS);
			}
			if ( rcvd.trim ().length () > 0 )
			{
				capabText.setText (rcvd.substring (
					rcvd.indexOf ("CONNECT")+7,	// NOI18N
					rcvd.indexOf ("NO CARRIER")	// NOI18N
					));
			}
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "CapabilityWindow: send/recv");	// NOI18N
			capabText.setText ("<"				// NOI18N
				+ exString
				+ ": " + ex + ">\n");			// NOI18N
		}*/
	}//GEN-LAST:event_getCapButActionPerformed

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		Object val = fontSizeSpin.getValue ();
		if ( val != null && val instanceof Number )
			Utils.setFontSize (this, ((Number)val).floatValue ());
	}//GEN-LAST:event_fontSizeSpinStateChanged

	private class KL extends KeyAdapter
	{
		/**
		 * Receives key-typed events (called when the user types a key).
		 * @param ke The key-typed event.
		 */
		@Override
		public void keyTyped (KeyEvent ke)
		{
			if ( ke == null ) return;
			if ( ke.getKeyChar () == KeyEvent.VK_ESCAPE )
			{
				dispose ();
			}
		}
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JRadioButton abookCapRB;
        private javax.swing.JRadioButton animCapRB;
        private javax.swing.JTextArea capabText;
        private javax.swing.ButtonGroup capabilityRadioGroup;
        private javax.swing.JRadioButton eventCapRB;
        private javax.swing.JButton exitBut;
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JButton getCapBut;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JRadioButton pictCapRB;
        private javax.swing.JRadioButton ringCapRB;
        private javax.swing.JRadioButton todoCapRB;
        // End of variables declaration//GEN-END:variables

}
