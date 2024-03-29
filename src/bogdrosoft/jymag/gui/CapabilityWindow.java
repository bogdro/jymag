/*
 * CapabilityWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.jymag.gui;

import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.DataTransporter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingWorker;

/**
 * Capabilitywindow - a window that allows the user to see the phone's
 * capabilities.
 * @author Bogdan Drozdowski
 */
public class CapabilityWindow extends javax.swing.JDialog
{
	private static final long serialVersionUID = 75L;
	private final transient DataTransporter dtr;
	private final transient Object sync;
	private final AtomicBoolean isFinished = new AtomicBoolean(true);

	// ------------ i18n stuff
	private static final String EX_STRING
		= ResourceBundle.getBundle("bogdrosoft/jymag/i18n/CapabilityWindow")
			.getString("Exception");
	private final MainWindow mw;

	/**
	 * Creates new form CapabilityWindow.
	 * @param dt The DataTransporter instance to use for communications.
	 * Must already be open.
	 * @param parent The parent frame.
	 * @param synchro A synchronization variable.
	 * @param fontSize The font size for this window.
	 */
	public CapabilityWindow (DataTransporter dt, MainWindow parent,
		Object synchro, float fontSize)
	{
		// make always modal
		super (parent, true);
		dtr = dt;
		sync = synchro;
		mw = parent;
		if ( dt == null || synchro == null )
		{
			dispose ();
			return;
		}

		initComponents ();

		/* add the Esc key listener to the frame and all components. */
		new EscKeyListener (this).install();

		UiUtils.changeSizeToScreen(this);

		fontSizeSpin.setValue (fontSize);	// refresh the font in the window
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

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
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/CapabilityWindow"); // NOI18N
                setTitle(bundle.getString("JYMAG_-_Capabilities")); // NOI18N
                addWindowListener(new java.awt.event.WindowAdapter()
                {
                        public void windowClosing(java.awt.event.WindowEvent evt)
                        {
                                formWindowClosing(evt);
                        }
                });

                getCapBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/capability.png"))); // NOI18N
                getCapBut.setText(bundle.getString("Get_selected_capabilities")); // NOI18N
                getCapBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                getCapButActionPerformed(evt);
                        }
                });

                exitBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/exit.png"))); // NOI18N
                exitBut.setText(bundle.getString("Exit")); // NOI18N
                exitBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
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

                capabText.setEditable(false);
                capabText.setColumns(20);
                capabText.setRows(5);
                jScrollPane1.setViewportView(capabText);
                capabText.getAccessibleContext().setAccessibleName(bundle.getString("Received_capabilities")); // NOI18N

                fontSizeSpin.setModel(new javax.swing.SpinnerNumberModel(12, 1, null, 1));
                fontSizeSpin.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                fontSizeSpinStateChanged(evt);
                        }
                });

                fontSizeLab.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                fontSizeLab.setText(bundle.getString("font_size")); // NOI18N

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(pictCapRB)
                                                        .addComponent(ringCapRB)
                                                        .addComponent(abookCapRB)
                                                        .addComponent(todoCapRB))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addComponent(fontSizeLab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(getCapBut, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(exitBut, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(eventCapRB)
                                                        .addComponent(animCapRB))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                );

                jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {exitBut, getCapBut});

                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(getCapBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(exitBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(fontSizeLab)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pictCapRB)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ringCapRB)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(abookCapRB)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(todoCapRB)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eventCapRB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(animCapRB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(22, Short.MAX_VALUE))
                );

                getCapBut.getAccessibleContext().setAccessibleName(bundle.getString("cap_button")); // NOI18N
                exitBut.getAccessibleContext().setAccessibleName(bundle.getString("cap_exit")); // NOI18N
                jLabel1.getAccessibleContext().setAccessibleName(bundle.getString("cap_label")); // NOI18N
                pictCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_pict")); // NOI18N
                ringCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_ring")); // NOI18N
                abookCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_addrs")); // NOI18N
                todoCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_todo")); // NOI18N
                eventCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_event")); // NOI18N
                animCapRB.getAccessibleContext().setAccessibleName(bundle.getString("cap_vid")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N
                fontSizeLab.getAccessibleContext().setAccessibleName(bundle.getString("cap_fontlabel")); // NOI18N

                jScrollPane2.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                );

                getAccessibleContext().setAccessibleName(bundle.getString("access_title_capab")); // NOI18N

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void exitButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		exit ();
	}//GEN-LAST:event_exitButActionPerformed

	private void getCapButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCapButActionPerformed

		capabText.setText ("");					// NOI18N
		String type;

		if ( abookCapRB.isSelected () )
		{
			type = "VCARDS";	// NOI18N
		}
		else if ( animCapRB.isSelected () )
		{
			type = "ANIMATIONS";// NOI18N
		}
		else if ( eventCapRB.isSelected () )
		{
			type = "VEVENT";	// NOI18N
		}
		else if ( pictCapRB.isSelected () )
		{
			type = "PICTURES";	// NOI18N
		}
		else if ( ringCapRB.isSelected () )
		{
			type = "SOUNDS";	// NOI18N
		}
		else if ( todoCapRB.isSelected () )
		{
			type = "VTODO";	// NOI18N
		}
		else
		{
			return;
		}

		final String typeToGet = type;
		getCapBut.setEnabled (false);
        	exitBut.setEnabled (false);
        	abookCapRB.setEnabled (false);
        	animCapRB.setEnabled (false);
        	eventCapRB.setEnabled (false);
        	pictCapRB.setEnabled (false);
        	ringCapRB.setEnabled (false);
        	todoCapRB.setEnabled (false);
		mw.setReceivingStatus ();

		SwingWorker<String, Void> sw =
			new SwingWorker<String, Void> ()
		{
			@Override
			protected String doInBackground ()
			{
				try
				{
					isFinished.set (false);
					String rcvd;
					synchronized (sync)
					{
						rcvd = dtr.getCapabilities (typeToGet);
					}
					return rcvd;
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "CapabilityWindow: send/recv");	// NOI18N
					return "<" 					// NOI18N
						+ EX_STRING
						+ ": " + ex + ">"; 			// NOI18N
				}
				finally
				{
					isFinished.set (true);
				}
			}

			@Override
			protected void done ()
			{
				try
				{
					String rcvd = get ();
					if ( rcvd != null )
					{
						capabText.setText (rcvd);
					}
					else
					{
						capabText.setText ("<"				// NOI18N
							+ EX_STRING
							+ ">\n");				// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "CapabilityWindow.getCapabilities.SW.done");	// NOI18N
				}
				getCapBut.setEnabled (true);
				exitBut.setEnabled (true);
				abookCapRB.setEnabled (true);
				animCapRB.setEnabled (true);
				eventCapRB.setEnabled (true);
				pictCapRB.setEnabled (true);
				ringCapRB.setEnabled (true);
				todoCapRB.setEnabled (true);
				mw.setReadyStatus ();
			}

			@Override
			public String toString ()
			{
				return "CapabilityWindow.getCapButActionPerformed.SwingWorker";	// NOI18N
			}
		};
		sw.execute ();

	}//GEN-LAST:event_getCapButActionPerformed

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		UiUtils.setFontSize (this, UiUtils.getFontSize (fontSizeSpin));

	}//GEN-LAST:event_fontSizeSpinStateChanged

	private void formWindowClosing (java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
	{//GEN-HEADEREND:event_formWindowClosing
		exit ();
	}//GEN-LAST:event_formWindowClosing

	private void exit ()
	{
		while ( ! isFinished.get () )
		{
			Utils.sleepIgnoreException(10);
		}
		mw.setReadyStatus ();
		dispose ();
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
