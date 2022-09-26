/*
 * RawCommunicator.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2009 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * RawCommunicator - a window which allows the user to enter commands which
 * will be sent directly to the phone.
 * @author Bogdan Drozdowski
 */
public class RawCommunicator extends javax.swing.JDialog
{
	private static final long serialVersionUID = 71L;
	private DataTransporter dtr;
	private final Object sync;
	private final KL kl = new KL ();

	// ------------ i18n stuff
	private static String exString = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/RawCommunicator").getString("Exception");

	/**
	 * Creates new form RawCommunicator.
	 * @param dt The DataTransporter instance to use for communications.
	 * Must already be open.
	 * @param parent The parent frame for this window.
	 * @param synchro A synchronization variable.
	 */
	public RawCommunicator (DataTransporter dt, java.awt.Frame parent,
		Object synchro)
	{
		// make modal
		super (parent, true);
		dtr = dt;
		sync = synchro;
		if ( dt == null || synchro == null )
		{
			dispose ();
			return;
		}

		initComponents ();

		answerArea.addKeyListener (kl);
		closeBut.addKeyListener (kl);
		cmdArea.addKeyListener (kl);
		currCommArea.addKeyListener (kl);
		jScrollPane1.addKeyListener (kl);
		jScrollPane2.addKeyListener (kl);
		jScrollPane3.addKeyListener (kl);
		jSplitPane1.addKeyListener (kl);
		sendBut.addKeyListener (kl);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        cmdArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        answerArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        currCommArea = new javax.swing.JTextArea();
        sendBut = new javax.swing.JButton();
        closeBut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/RawCommunicator"); // NOI18N
        setTitle(bundle.getString("Manual_communication")); // NOI18N

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        cmdArea.setColumns(20);
        cmdArea.setEditable(false);
        cmdArea.setRows(5);
        jScrollPane1.setViewportView(cmdArea);
        cmdArea.getAccessibleContext().setAccessibleName(bundle.getString("sent_commands")); // NOI18N

        jSplitPane1.setTopComponent(jScrollPane1);

        answerArea.setColumns(20);
        answerArea.setEditable(false);
        answerArea.setRows(5);
        jScrollPane2.setViewportView(answerArea);
        answerArea.getAccessibleContext().setAccessibleName(bundle.getString("received_answers")); // NOI18N

        jSplitPane1.setRightComponent(jScrollPane2);

        currCommArea.setColumns(20);
        currCommArea.setRows(5);
        jScrollPane3.setViewportView(currCommArea);
        currCommArea.getAccessibleContext().setAccessibleName(bundle.getString("command")); // NOI18N

        sendBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/upload.png"))); // NOI18N
        sendBut.setText(bundle.getString("Send")); // NOI18N
        sendBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButActionPerformed(evt);
            }
        });

        closeBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/exit.png"))); // NOI18N
        closeBut.setText(bundle.getString("Exit")); // NOI18N
        closeBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(sendBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeBut)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sendBut)
                        .addComponent(closeBut))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void closeButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButActionPerformed

		dispose ();
	}//GEN-LAST:event_closeButActionPerformed

	private void sendButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButActionPerformed

		String cmd = currCommArea.getText ();
		if ( cmd == null ) return;
		if ( cmd.trim ().length () == 0 ) return;
		cmdArea.setText (cmdArea.getText ()  + cmd + "\n");	// NOI18N
		currCommArea.setText ("");				// NOI18N
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
				answerArea.setText (answerArea.getText () + rcvd + "\n");	// NOI18N
			}
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "RawCommunicator: send/recv");	// NOI18N
			answerArea.setText (answerArea.getText ()
				+ "<" 					// NOI18N
				+ exString
				+ ": " + ex + ">\n");			// NOI18N
		}
		currCommArea.requestFocusInWindow ();
	}//GEN-LAST:event_sendButActionPerformed

	private class KL implements KeyListener
	{
		/**
		 * Receives key-typed events (called when the user types a key).
		 * @param ke The key-typed event.
		 */
		@Override
		public void keyTyped (KeyEvent ke)
		{
			if ( ke.getKeyChar () == KeyEvent.VK_ESCAPE )
			{
				dispose ();
			}
		}

		/**
		 * Receives key-pressed events (unused).
		 * @param ke The key-pressed event.
		 */
		@Override
		public void keyPressed (KeyEvent ke) {}

		/**
		 * Receives key-released events (unused).
		 * @param ke The key-released event.
	 	 */
		@Override
		public void keyReleased (KeyEvent ke) {}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea answerArea;
    private javax.swing.JButton closeBut;
    private javax.swing.JTextArea cmdArea;
    private javax.swing.JTextArea currCommArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton sendBut;
    // End of variables declaration//GEN-END:variables

}
