/*
 * RawCommunicator.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2020 Bogdan Drozdowski, bogdandr (at) op.pl
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

package BogDroSoft.jymag.gui;

import BogDroSoft.jymag.comm.DataTransporter;
import BogDroSoft.jymag.Utils;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

/**
 * RawCommunicator - a window which allows the user to enter commands which
 * will be sent directly to the phone.
 * @author Bogdan Drozdowski
 */
public class RawCommunicator extends javax.swing.JDialog
{
	private static final long serialVersionUID = 71L;
	private final DataTransporter dtr;
	private final Object sync;
	/** The Thread that updates the control line signal display. */
	private Thread updater = null;
	/** The refresh interval, in milliseconds. */
	private static final int refreshTime = 1000;
	/** The "line disabled" icon. */
	private final ImageIcon black_icon
		= new ImageIcon (getClass ().getResource ("/BogDroSoft/jymag/rsrc/black_circle.png")); // NOI18N
	/** The "line enabled" icon. */
	private final ImageIcon green_icon
		= new ImageIcon (getClass ().getResource ("/BogDroSoft/jymag/rsrc/green_circle.png")); // NOI18N
	/** The file chooser for choosing the file to send. */
	private JFileChooser fc;
	private final AtomicBoolean isFinished = new AtomicBoolean(true);

	// ------------ i18n stuff
	private static final ResourceBundle rcBundle = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/RawCommunicator");
	private static final String exString = rcBundle.getString("Exception");
	private static final String fileNotReadMsg = rcBundle.getString("file_not_readable");
	private static final String noFileMsg = rcBundle.getString("no_file_sel");
	private static final String errString = ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow").getString("Error");
	private static final String fileSentOkMsg = rcBundle.getString("file_sent_ok");

	private static final String emptyStr = "";					// NOI18N
	private static final String CRStr = "\r";					// NOI18N
	private static final String LFStr = "\n";					// NOI18N

	private final MainWindow mw;

	/**
	 * Creates new form RawCommunicator.
	 * @param dt The DataTransporter instance to use for communications.
	 * Must already be open.
	 * @param parent The parent frame for this window.
	 * @param synchro A synchronization variable.
	 * @param fontSize The font size for this window.
	 */
	public RawCommunicator (DataTransporter dt, MainWindow parent,
		Object synchro, float fontSize)
	{
		// make modal
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
		// change the size so that the scrollbars fit:
		Dimension size = getSize ();
		if ( size != null )
		{
			size.height += 50;
			size.width += 50;
			setSize (size);
		}
		fontSizeSpin.setValue (fontSize);	// refresh the font in the window
		fontSizeLab.setHorizontalAlignment (JLabel.RIGHT);

		/* add the Esc key listener to the frame and all components. */
		new EscKeyListener (this).install();

		/* start the signal listening Thread. */
		start ();
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jScrollPane4 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                jSplitPane1 = new javax.swing.JSplitPane();
                jScrollPane1 = new javax.swing.JScrollPane();
                cmdArea = new javax.swing.JTextArea();
                jScrollPane2 = new javax.swing.JScrollPane();
                answerArea = new javax.swing.JTextArea();
                jScrollPane3 = new javax.swing.JScrollPane();
                currCommArea = new javax.swing.JTextArea();
                sendBut = new javax.swing.JButton();
                closeBut = new javax.swing.JButton();
                fontSizeSpin = new javax.swing.JSpinner();
                fontSizeLab = new javax.swing.JLabel();
                sendFileBut = new javax.swing.JButton();
                clearBut = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/RawCommunicator"); // NOI18N
                setTitle(bundle.getString("Manual_communication")); // NOI18N
                addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent evt) {
                                formWindowClosing(evt);
                        }
                });

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

                fontSizeSpin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
                fontSizeSpin.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                                fontSizeSpinStateChanged(evt);
                        }
                });

                fontSizeLab.setText(bundle.getString("font_size")); // NOI18N

                rtsBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                rtsBut.setText("RTS"); // NOI18N
                rtsBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                rtsButActionPerformed(evt);
                        }
                });

                dtrBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                dtrBut.setText("DTR"); // NOI18N
                dtrBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                dtrButActionPerformed(evt);
                        }
                });

                dcdLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                dcdLabel.setText("DCD"); // NOI18N

                ctsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                ctsLabel.setText("CTS"); // NOI18N

                dsrLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                dsrLabel.setText("DSR"); // NOI18N

                riLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/black_circle.png"))); // NOI18N
                riLabel.setText("RI"); // NOI18N

                sendFileBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/sendfile.png"))); // NOI18N
                sendFileBut.setText(bundle.getString("send_file")); // NOI18N
                sendFileBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                sendFileButActionPerformed(evt);
                        }
                });

                clearBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/clear.png"))); // NOI18N
                clearBut.setText(bundle.getString("clear_but_text")); // NOI18N
                clearBut.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                clearButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(490, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(fontSizeLab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(riLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(dsrLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(ctsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(dcdLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(dtrBut, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addComponent(rtsBut, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(sendFileBut, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(clearBut, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                                                        .addGap(229, 229, 229))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(sendBut)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(closeBut)))
                                        .addContainerGap()))
                );

                jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ctsLabel, dcdLabel, dsrLabel, dtrBut, riLabel, rtsBut});

                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fontSizeLab))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendFileBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addComponent(rtsBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtrBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dcdLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ctsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dsrLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(riLabel)
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(sendBut)
                                                        .addComponent(closeBut))
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                                        .addContainerGap()))
                );

                sendBut.getAccessibleContext().setAccessibleName(bundle.getString("access_send")); // NOI18N
                closeBut.getAccessibleContext().setAccessibleName(bundle.getString("access_exit")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N
                fontSizeLab.getAccessibleContext().setAccessibleName(bundle.getString("font_label")); // NOI18N
                sendFileBut.getAccessibleContext().setAccessibleName(bundle.getString("access_send_file")); // NOI18N
                clearBut.getAccessibleContext().setAccessibleName(bundle.getString("access_clear_but_text")); // NOI18N

                jScrollPane4.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                );

                getAccessibleContext().setAccessibleName(bundle.getString("access_title_rawcomm")); // NOI18N

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void closeButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButActionPerformed

		exit();
	}//GEN-LAST:event_closeButActionPerformed

	private void sendButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButActionPerformed

		final String cmd = currCommArea.getText ();
		if ( cmd == null )
		{
			return;
		}
		if ( cmd.isEmpty () )
		{
			return;
		}
		cmdArea.setText (cmdArea.getText () + cmd + LFStr);
		currCommArea.setEditable (false);
		currCommArea.setEnabled (false);
		sendBut.setEnabled (false);
		sendFileBut.setEnabled (false);
		currCommArea.setText (emptyStr);
		mw.setSendingStatus ();

		SwingWorker<String, Void> sw =
			new SwingWorker<String, Void> ()
		{
			@Override
			protected String doInBackground ()
			{
				try
				{
					isFinished.set (false);
					String rcvd = emptyStr;
					synchronized (sync)
					{
						// don't force any encodings, because the command may
						// be in another encoding
						dtr.send ((cmd+CRStr).getBytes ());
						int trial = 0;
						do
						{
							byte[] recvdB = dtr.recv (null);
							// don't force any encodings, because the reply may
							// be in another encoding
							if ( recvdB != null )
							{
								rcvd = new String (recvdB);
							}
							trial++;
						} while (rcvd.trim ().equals (emptyStr)
							&& trial < DataTransporter.MAX_TRIALS);
					}
					return rcvd;
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "RawCommunicator: send/recv");	// NOI18N
					return "<" 					// NOI18N
						+ exString
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
						if ( ! rcvd.trim ().isEmpty () )
						{
							answerArea.setText (answerArea.getText ()
								+ rcvd + LFStr);
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "RawCommunicator.send.SW.done");	// NOI18N
				}
				currCommArea.setEditable (true);
				currCommArea.setEnabled (true);
				sendBut.setEnabled (true);
				sendFileBut.setEnabled (true);
				currCommArea.requestFocusInWindow ();
				mw.setReadyStatus ();
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.sendButActionPerformed.SwingWorker";	// NOI18N
			}
		};
		sw.execute ();
	}//GEN-LAST:event_sendButActionPerformed

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		Utils.setFontSize (this, Utils.getFontSize (fontSizeSpin));

	}//GEN-LAST:event_fontSizeSpinStateChanged

	private void rtsButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_rtsButActionPerformed
	{//GEN-HEADEREND:event_rtsButActionPerformed

		final boolean isSet = rtsBut.isSelected ();
		SwingWorker<Void, Void> sw =
			new SwingWorker<Void, Void> ()
		{
			@Override
			protected Void doInBackground ()
			{
				try
				{
					isFinished.set (false);
					synchronized (sync)
					{
						dtr.setRTS (isSet);
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "RawCommunicator: setRTS");	// NOI18N
				}
				finally
				{
					isFinished.set (true);
				}
				return null;
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.rtsButActionPerformed.SwingWorker";	// NOI18N
			}
		};
		sw.execute ();
	}//GEN-LAST:event_rtsButActionPerformed

	private void dtrButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_dtrButActionPerformed
	{//GEN-HEADEREND:event_dtrButActionPerformed

		final boolean isSet = dtrBut.isSelected ();
		SwingWorker<Void, Void> sw =
			new SwingWorker<Void, Void> ()
		{
			@Override
			protected Void doInBackground ()
			{
				try
				{
					isFinished.set (false);
					synchronized (sync)
					{
						dtr.setDTR (isSet);
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "RawCommunicator: setDTR");	// NOI18N
				}
				finally
				{
					isFinished.set (true);
				}
				return null;
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.dtrButActionPerformed.SwingWorker";	// NOI18N
			}
		};
		sw.execute ();
	}//GEN-LAST:event_dtrButActionPerformed

	private void formWindowClosing (java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
	{//GEN-HEADEREND:event_formWindowClosing
		exit ();
	}//GEN-LAST:event_formWindowClosing

	private void sendFileButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_sendFileButActionPerformed
	{//GEN-HEADEREND:event_sendFileButActionPerformed
		if ( fc == null )
		{
			fc = new JFileChooser ();
			fc.setMultiSelectionEnabled (false);
			fc.setDialogType (JFileChooser.OPEN_DIALOG);
		}
		int dialogRes = fc.showOpenDialog (this);

		if ( dialogRes == JFileChooser.APPROVE_OPTION )
		{
			final File f = fc.getSelectedFile ();
			if ( f == null )
			{
				JOptionPane.showMessageDialog (this,
					noFileMsg, errString, JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ( ! f.exists () || ! f.canRead () || ! f.isFile () )
			{
				JOptionPane.showMessageDialog (this,
					fileNotReadMsg + ": " + f.getName (),		// NOI18N
					errString, JOptionPane.ERROR_MESSAGE);
				return;
			}

			sendBut.setEnabled (false);
			sendFileBut.setEnabled (false);
			// allow typing new commands while the file is being sent:
			//currCommArea.setEditable (false);
			//currCommArea.setEnabled (false);
			mw.setSendingStatus ();
			SwingWorker<Void, Void> sw =
				new SwingWorker<Void, Void> ()
			{
				@Override
				protected Void doInBackground ()
				{
					try
					{
						isFinished.set (false);
						int read = -1;
						byte[] b = new byte[1024];
						FileInputStream fis = new FileInputStream (f);
						synchronized (sync)
						{
							do
							{
								read = fis.read (b);
								if ( read <= 0 )
								{
									break;
								}
								dtr.send (b, 0, read);
							} while ( true );
						}
						fis.close ();
					}
					catch (Exception ex)
					{
						Utils.handleException (ex, "RawCommunicator: send file");	// NOI18N
					}
					finally
					{
						isFinished.set (true);
					}
					return null;
				}

				@Override
				protected void done ()
				{
					//currCommArea.setEditable (true);
					//currCommArea.setEnabled (true);
					sendBut.setEnabled (true);
					sendFileBut.setEnabled (true);
					mw.setReadyStatus ();
					JOptionPane.showMessageDialog (mw,
						fileSentOkMsg + ": " + f.getName (),		// NOI18N
						fileSentOkMsg, JOptionPane.INFORMATION_MESSAGE);
				}

				@Override
				public String toString ()
				{
					return "RawCommunicator.sendFileButActionPerformed.SwingWorker";	// NOI18N
				}
			};
			sw.execute ();
		}
	}//GEN-LAST:event_sendFileButActionPerformed

	private void clearButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearButActionPerformed
	{//GEN-HEADEREND:event_clearButActionPerformed
		answerArea.setText (emptyStr);
	}//GEN-LAST:event_clearButActionPerformed

	private void exit ()
	{
		if ( updater != null )
		{
			while ( updater.isAlive () )
			{
				updater.interrupt ();
				try
				{
					Thread.sleep (10);
				}
				catch (Exception ex)
				{
					// ignore, just wait again
				}
			}
		}
		while ( ! isFinished.get () )
		{
			try
			{
				Thread.sleep (10);
			}
			catch (Exception ex)
			{
				// ignore
			}
		}
		mw.setReadyStatus ();
		dispose ();
	}

	private void setSignal (final JToggleButton but, final boolean on)
	{
		if ( but == null )
		{
			return;
		}
		Utils.changeGUI (new Runnable ()
		{
			@Override
			public synchronized void run ()
			{
				if ( on )
				{
					but.setIcon (green_icon);
				}
				else
				{
					but.setIcon (black_icon);
				}
				but.setSelected (on);
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.setSignal1.Runnable";	// NOI18N
			}
		});
	}

	private void setSignal (final JLabel l, final boolean on)
	{
		if ( l == null )
		{
			return;
		}
		Utils.changeGUI (new Runnable ()
		{
			@Override
			public synchronized void run ()
			{
				if ( on )
				{
					l.setIcon (green_icon);
				}
				else
				{
					l.setIcon (black_icon);
				}
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.setSignal2.Runnable";	// NOI18N
			}
		});
	}

	private void start ()
	{
		updater = new Thread (new Runnable ()
		{
			private boolean prevRTS = false;
			private boolean currRTS = false;

			private boolean prevDTR = false;
			private boolean currDTR = false;

			private boolean prevDCD = false;
			private boolean currDCD = false;

			private boolean prevRI  = false;
			private boolean currRI  = false;

			private boolean prevCTS = false;
			private boolean currCTS = false;

			private boolean prevDSR = false;
			private boolean currDSR = false;

			private int availBytes = 0;

			@Override
			public synchronized void run ()
			{
				while (! Thread.interrupted ())
				{
					synchronized (sync)
					{
						currRTS = dtr.isRTS ();
						currDTR = dtr.isDTR ();
						currDCD = dtr.isCD  ();
						currRI  = dtr.isRI  ();
						currCTS = dtr.isCTS ();
						currDSR = dtr.isDSR ();
						availBytes = dtr.getAvailableBytes();
					}

					if ( currRTS != prevRTS )
					{
						setSignal (rtsBut, currRTS);
						prevRTS = currRTS;
					}
					if ( currDTR != prevDTR )
					{
						setSignal (dtrBut, currDTR);
						prevDTR = currDTR;
					}
					if ( currDCD != prevDCD )
					{
						setSignal (dcdLabel, currDCD);
						prevDCD = currDCD;
					}
					if ( currRI != prevRI )
					{
						setSignal (riLabel, currRI);
						prevRI = currRI;
					}
					if ( currCTS != prevCTS )
					{
						setSignal (ctsLabel, currCTS);
						prevCTS = currCTS;
					}
					if ( currDSR != prevDSR )
					{
						setSignal (dsrLabel, currDSR);
						prevDSR = currDSR;
					}
					if ( availBytes != 0 )
					{
						byte[] recvdB = dtr.recv (null);
						if ( recvdB != null )
						{
							// don't force any encodings,
							// because the reply may
							// be in another encoding
							final String rcvd = new String (recvdB);
							if ( ! rcvd.trim ().isEmpty () )
							{
								Utils.changeGUI (new Runnable ()
								{
									@Override
									public synchronized void run ()
									{
										answerArea.setText (
											answerArea.getText ()
											+ rcvd + LFStr);
									}

									@Override
									public String toString ()
									{
										return "RawCommunicator.start.Runnable.Runnable";	// NOI18N
									}
								});
							}
						}
					}
					try
					{
						Thread.sleep (refreshTime);
					}
					catch (InterruptedException intex)
					{
						break;
					}
				}
			}

			@Override
			public String toString ()
			{
				return "RawCommunicator.start.Runnable";	// NOI18N
			}
		});
		updater.start ();
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTextArea answerArea;
        private javax.swing.JButton clearBut;
        private javax.swing.JButton closeBut;
        private javax.swing.JTextArea cmdArea;
        private final javax.swing.JLabel ctsLabel = new javax.swing.JLabel();
        private javax.swing.JTextArea currCommArea;
        private final javax.swing.JLabel dcdLabel = new javax.swing.JLabel();
        private final javax.swing.JLabel dsrLabel = new javax.swing.JLabel();
        private final javax.swing.JToggleButton dtrBut = new javax.swing.JToggleButton();
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JSplitPane jSplitPane1;
        private final javax.swing.JLabel riLabel = new javax.swing.JLabel();
        private final javax.swing.JToggleButton rtsBut = new javax.swing.JToggleButton();
        private javax.swing.JButton sendBut;
        private javax.swing.JButton sendFileBut;
        // End of variables declaration//GEN-END:variables

}
