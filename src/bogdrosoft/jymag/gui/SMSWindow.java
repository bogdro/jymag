/*
 * SMSWindow.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.TransferParameters;
import bogdrosoft.jymag.comm.TransferUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;


/**
 * This class is used to send and display SMS messages.
 * @author Bogdan Drozdowski
 */
public class SMSWindow extends javax.swing.JDialog
{
	private static final long serialVersionUID = 80L;
	private final transient TransferParameters tp;
	private final AtomicBoolean isFinished = new AtomicBoolean(true);

	// ------------ i18n stuff
	/*
	private static final ResourceBundle swBundle = ResourceBundle.getBundle("bogdrosoft/jymag/i18n/SMSWindow");
	private static final String ERR_STRING = ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow").getString("Error");
	private static final String OK_STRING = swBundle.getString("message_sent");
	private static final String SEND_ERROR = swBundle.getString("send_error");

	private static final String SPACE = " ";	// NOI18N
	*/
	private final MainWindow mw;

	/**
	 * Creates new form SMSWindow.
	 * @param tParams The TransferParameters instance to use for communications.
	 * @param parent The parent frame for this window.
	 * @param fontSize The font size for this window.
	 * @param msg the message to display, if any.
	 */
	public SMSWindow (TransferParameters tParams, MainWindow parent,
		float fontSize, PhoneMessage msg)
	{
		// make modal
		super (parent, true);

		tp = tParams;
		mw = parent;
		// "msg" is the message to display, if any. If present,
		// transport parameters are not required
		if ( msg == null && tp == null )
		{
			dispose ();
			return;
		}

		initComponents ();

		// If no PhoneMessage given to display, edit mode assumed.
		// Otherwise, disable editing and sending
		if ( msg != null )
		{
			sendBut.setEnabled (false);
			phoneNumField.setEditable (false);
			msgArea.setEditable (false);
			phoneNumField.setText (msg.getRecipientNum ());
			msgArea.setText (msg.getMessage ());
		}

		UiUtils.changeSizeToScreen(this);

		fontSizeSpin.setValue (fontSize);	// refresh the font in the window
		fontSizeLab.setHorizontalAlignment (JLabel.RIGHT);

		/* add the Esc key listener to the frame and all components. */
		new EscKeyListener (this).install();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

                closeBut = new javax.swing.JButton();
                sendBut = new javax.swing.JButton();
                fontSizeLab = new javax.swing.JLabel();
                fontSizeSpin = new javax.swing.JSpinner();
                phoneLab = new javax.swing.JLabel();
                phoneNumField = new javax.swing.JTextField();
                msgLab = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                msgArea = new javax.swing.JTextArea();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/SMSWindow"); // NOI18N
                setTitle(bundle.getString("smswindow_title")); // NOI18N
                addWindowListener(new java.awt.event.WindowAdapter()
                {
                        public void windowClosing(java.awt.event.WindowEvent evt)
                        {
                                formWindowClosing(evt);
                        }
                });

                closeBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/exit.png"))); // NOI18N
                closeBut.setText(bundle.getString("Exit")); // NOI18N
                closeBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                closeButActionPerformed(evt);
                        }
                });

                sendBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/upload.png"))); // NOI18N
                sendBut.setText(bundle.getString("Send")); // NOI18N
                sendBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                sendButActionPerformed(evt);
                        }
                });

                fontSizeLab.setText(bundle.getString("font_size")); // NOI18N

                fontSizeSpin.setModel(new javax.swing.SpinnerNumberModel(12, 1, null, 1));
                fontSizeSpin.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                fontSizeSpinStateChanged(evt);
                        }
                });

                phoneLab.setText(bundle.getString("phone_num")); // NOI18N

                msgLab.setText(bundle.getString("message")); // NOI18N

                msgArea.setColumns(20);
                msgArea.setRows(5);
                jScrollPane1.setViewportView(msgArea);
                msgArea.getAccessibleContext().setAccessibleName(bundle.getString("access_message_area")); // NOI18N

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(sendBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(closeBut)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fontSizeLab, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(169, 169, 169))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(phoneLab)
                                                        .addComponent(msgLab))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                                                        .addComponent(phoneNumField, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                                                .addContainerGap())))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fontSizeLab)
                                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(phoneLab)
                                        .addComponent(phoneNumField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(msgLab)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(closeBut)
                                        .addComponent(sendBut))
                                .addContainerGap())
                );

                closeBut.getAccessibleContext().setAccessibleName(bundle.getString("access_exit")); // NOI18N
                sendBut.getAccessibleContext().setAccessibleName(bundle.getString("access_send")); // NOI18N
                fontSizeLab.getAccessibleContext().setAccessibleName(bundle.getString("font_size")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spin")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("access_font_size")); // NOI18N
                phoneNumField.getAccessibleContext().setAccessibleName(bundle.getString("access_phone")); // NOI18N

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void closeButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButActionPerformed
	{//GEN-HEADEREND:event_closeButActionPerformed

		exit ();
	}//GEN-LAST:event_closeButActionPerformed

	private void sendButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_sendButActionPerformed
	{//GEN-HEADEREND:event_sendButActionPerformed

		if ( tp == null || ! sendBut.isEnabled () )
		{
			return;
		}
		final String phoneNumber = phoneNumField.getText ();
		final String messageBody = msgArea.getText ();
		if (phoneNumber == null || messageBody == null)
		{
			return;
		}
		if (phoneNumber.isEmpty () || messageBody.isEmpty ())
		{
			return;
		}
		phoneNumField.setEditable (false);
		msgArea.setEditable (false);
		closeBut.setEnabled (false);
		sendBut.setEnabled (false);
		mw.setSendingStatus ();

		PhoneMessage msg = new PhoneMessage ();
		msg.setMessage (messageBody);
		msg.setRecipientNum (phoneNumber);
		isFinished.set (false);
		TransferUtils.sendMessage (msg, tp,
			new Runnable ()
		{
			@Override
			public synchronized void run ()
			{
				phoneNumField.setEditable (true);
				msgArea.setEditable (true);
				sendBut.setEnabled (true);
				closeBut.setEnabled (true);
				isFinished.set (true);
				mw.setReadyStatus ();
			}

			@Override
			public String toString ()
			{
				return "SMSWindow.sendButActionPerformed.Runnable";	// NOI18N
			}
		}, this, false, false, false);

	}//GEN-LAST:event_sendButActionPerformed

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_fontSizeSpinStateChanged
	{//GEN-HEADEREND:event_fontSizeSpinStateChanged

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
        private javax.swing.JButton closeBut;
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextArea msgArea;
        private javax.swing.JLabel msgLab;
        private javax.swing.JLabel phoneLab;
        private javax.swing.JTextField phoneNumField;
        private javax.swing.JButton sendBut;
        // End of variables declaration//GEN-END:variables
}
