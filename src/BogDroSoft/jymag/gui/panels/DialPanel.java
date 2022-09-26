/*
 * DialPanel.java, part of the JYMAG package.
 *
 * Copyright (C) 2012-2022 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import BogDroSoft.jymag.Utils;
import BogDroSoft.jymag.comm.DataTransporter;
import BogDroSoft.jymag.comm.TransferParameters;
import BogDroSoft.jymag.comm.TransferUtils;
import BogDroSoft.jymag.gui.MainWindow;
import BogDroSoft.jymag.gui.UiUtils;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;

/**
 * This class is the dial panel in the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class DialPanel extends javax.swing.JPanel implements JYMAGTab
{
	private static final long serialVersionUID = 81L;

	private volatile MainWindow mw;

	/** Creates new form DialPanel. */
	public DialPanel()
	{
		initComponents();
		if ( dialNumRadio.isSelected () )
		{
			numberField.setEnabled (true);
			dialCmdField.setEnabled (false);
		}
		else
		{
			numberField.setEnabled (false);
			dialCmdField.setEnabled (true);
		}
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

                dialTypeGroup = new javax.swing.ButtonGroup();
                voiceDataDialGroup = new javax.swing.ButtonGroup();
                tonePulseDialGroup = new javax.swing.ButtonGroup();
                dialNumRadio = new javax.swing.JRadioButton();
                dialCmdRadio = new javax.swing.JRadioButton();
                numberField = new javax.swing.JTextField();
                but1 = new javax.swing.JButton();
                but2 = new javax.swing.JButton();
                but3 = new javax.swing.JButton();
                but4 = new javax.swing.JButton();
                but5 = new javax.swing.JButton();
                but6 = new javax.swing.JButton();
                but7 = new javax.swing.JButton();
                but8 = new javax.swing.JButton();
                but9 = new javax.swing.JButton();
                butAst = new javax.swing.JButton();
                but0 = new javax.swing.JButton();
                butHash = new javax.swing.JButton();
                butPlus = new javax.swing.JButton();
                dialCmdField = new javax.swing.JTextField();
                voiceRadio = new javax.swing.JRadioButton();
                dataRadio = new javax.swing.JRadioButton();
                dialBut = new javax.swing.JButton();
                hangUpBut = new javax.swing.JButton();
                toneDialRadio = new javax.swing.JRadioButton();
                pulseDialRadio = new javax.swing.JRadioButton();
                autoDialRadio = new javax.swing.JRadioButton();
                answerBut = new javax.swing.JButton();
                volumeUpBut = new javax.swing.JButton();
                volumeDownBut = new javax.swing.JButton();

                dialTypeGroup.add(dialNumRadio);
                dialNumRadio.setSelected(true);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/DialPanel"); // NOI18N
                dialNumRadio.setText(bundle.getString("dial_the_number")); // NOI18N
                dialNumRadio.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                dialNumRadioStateChanged(evt);
                        }
                });
                dialNumRadio.addItemListener(new java.awt.event.ItemListener()
                {
                        public void itemStateChanged(java.awt.event.ItemEvent evt)
                        {
                                dialNumRadioItemStateChanged(evt);
                        }
                });

                dialTypeGroup.add(dialCmdRadio);
                dialCmdRadio.setText(bundle.getString("dial_command")); // NOI18N
                dialCmdRadio.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                dialCmdRadioStateChanged(evt);
                        }
                });
                dialCmdRadio.addItemListener(new java.awt.event.ItemListener()
                {
                        public void itemStateChanged(java.awt.event.ItemEvent evt)
                        {
                                dialCmdRadioItemStateChanged(evt);
                        }
                });

                but1.setText("1");
                but1.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but2.setText("2");
                but2.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but3.setText("3");
                but3.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but4.setText("4");
                but4.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but5.setText("5");
                but5.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but6.setText("6");
                but6.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but7.setText("7");
                but7.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but8.setText("8");
                but8.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but9.setText("9");
                but9.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                butAst.setText("*");
                butAst.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                but0.setText("0");
                but0.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                butHash.setText("#");
                butHash.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                butPlus.setText("+");
                butPlus.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                numberButActionPerformed(evt);
                        }
                });

                voiceDataDialGroup.add(voiceRadio);
                voiceRadio.setSelected(true);
                voiceRadio.setText(bundle.getString("call_type_voice")); // NOI18N

                voiceDataDialGroup.add(dataRadio);
                dataRadio.setText(bundle.getString("call_type_data")); // NOI18N

                dialBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/dialnum.png"))); // NOI18N
                dialBut.setText(bundle.getString("dial_button_text")); // NOI18N
                dialBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                dialButActionPerformed(evt);
                        }
                });

                hangUpBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/hangup.png"))); // NOI18N
                hangUpBut.setText(bundle.getString("hangup_button_text")); // NOI18N
                hangUpBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                hangUpButActionPerformed(evt);
                        }
                });

                tonePulseDialGroup.add(toneDialRadio);
                toneDialRadio.setText(bundle.getString("dial_method_tone")); // NOI18N

                tonePulseDialGroup.add(pulseDialRadio);
                pulseDialRadio.setText(bundle.getString("dial_method_pulse")); // NOI18N

                tonePulseDialGroup.add(autoDialRadio);
                autoDialRadio.setSelected(true);
                autoDialRadio.setText(bundle.getString("dial_method_auto")); // NOI18N

                answerBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/pickup.png"))); // NOI18N
                answerBut.setText(bundle.getString("answer_call")); // NOI18N
                answerBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                answerButActionPerformed(evt);
                        }
                });

                volumeUpBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/volup.png"))); // NOI18N
                volumeUpBut.setText(bundle.getString("volume_up")); // NOI18N
                volumeUpBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                volumeUpButActionPerformed(evt);
                        }
                });

                volumeDownBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/voldown.png"))); // NOI18N
                volumeDownBut.setText(bundle.getString("volume_down")); // NOI18N
                volumeDownBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                volumeDownButActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(dialNumRadio)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(dataRadio)
                                                                        .addComponent(voiceRadio))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(butAst)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(but0)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(butHash)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                                                                .addComponent(volumeUpBut)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(volumeDownBut))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(numberField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 287, Short.MAX_VALUE)
                                                                .addComponent(dialBut))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(but1)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but2)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but3)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(butPlus))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(but4)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but5)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but6))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(but7)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but8)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(but9)))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(autoDialRadio)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                                                                                .addComponent(answerBut))
                                                                        .addComponent(pulseDialRadio)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(toneDialRadio)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                                                                                .addComponent(hangUpBut))))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(dialCmdRadio)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dialCmdField, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                );

                layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {volumeDownBut, volumeUpBut});

                layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {answerBut, dialBut, hangUpBut});

                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(dialNumRadio)
                                                        .addComponent(numberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(but1)
                                                        .addComponent(but2)
                                                        .addComponent(but3)
                                                        .addComponent(butPlus)
                                                        .addComponent(voiceRadio)
                                                        .addComponent(toneDialRadio))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(but4)
                                                        .addComponent(but5)
                                                        .addComponent(but6)
                                                        .addComponent(dataRadio)
                                                        .addComponent(pulseDialRadio))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(but7)
                                                        .addComponent(but8)
                                                        .addComponent(but9)
                                                        .addComponent(autoDialRadio))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(butAst)
                                                        .addComponent(but0)
                                                        .addComponent(butHash))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(dialCmdRadio)
                                                        .addComponent(dialCmdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(dialBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(hangUpBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(answerBut)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(volumeDownBut)
                                                        .addComponent(volumeUpBut))))
                                .addContainerGap(12, Short.MAX_VALUE))
                );

                numberField.getAccessibleContext().setAccessibleDescription(bundle.getString("acc_number_field")); // NOI18N
                dialCmdField.getAccessibleContext().setAccessibleDescription(bundle.getString("acc_dial_cmd")); // NOI18N
        }// </editor-fold>//GEN-END:initComponents

	private void numberButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_numberButActionPerformed
	{//GEN-HEADEREND:event_numberButActionPerformed
		if ( evt == null )
		{
			return;
		}
		Object source = evt.getSource ();
		if ( source != null && (source instanceof JButton) )
		{
			numberField.setText (numberField.getText () + ((JButton)source).getText ());
		}

	}//GEN-LAST:event_numberButActionPerformed

	private void dialButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_dialButActionPerformed
	{//GEN-HEADEREND:event_dialButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			if ( dialNumRadio.isSelected () )
			{
				// dial a number
				DataTransporter.DIAL_MODE mode = DataTransporter.DIAL_MODE.AUTO;
				if ( toneDialRadio.isSelected () )
				{
					mode = DataTransporter.DIAL_MODE.TONE;
				}
				else if ( pulseDialRadio.isSelected () )
				{
					mode = DataTransporter.DIAL_MODE.PULSE;
				}
				TransferUtils.dialNumber (numberField.getText (),
					voiceRadio.isSelected (), mode,
					tp, null, this, false, false, false);
			}
			else
			{
				final DataTransporter dt =
					new DataTransporter (tp.getId ());
				dt.open (tp.getSpeed (), tp.getDataBits (),
					tp.getStopBits (),
					tp.getParity (), tp.getFlow ());
				final byte[] cmd = (dialCmdField.getText () + Utils.CR)
					.getBytes (DataTransporter.DEFAULT_ENCODING);
				// send a custom command
				SwingWorker<Void, Void> sw =
					new SwingWorker<Void, Void> ()
				{
					@Override
					protected Void doInBackground ()
					{
						try
						{
							dt.send (cmd);
						}
						catch (Exception ex)
						{
							Utils.handleException (ex, "DialPanel: send command");	// NOI18N
						}
						return null;
					}

					@Override
					protected void done ()
					{
						dt.close ();
					}

					@Override
					public String toString ()
					{
						return "DialPanel.dialButActionPerformed.SwingWorker";	// NOI18N
					}
				};
				sw.execute ();
			}
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "dialButActionPerformed");	// NOI18N
			UiUtils.showErrorMessage(mw, ex.toString ());
		}
	}//GEN-LAST:event_dialButActionPerformed

	private void hangUpButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_hangUpButActionPerformed
	{//GEN-HEADEREND:event_hangUpButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			TransferUtils.hangup (tp,
				null, this, false, false, false);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "hangUpButActionPerformed");	// NOI18N
		}
	}//GEN-LAST:event_hangUpButActionPerformed

	private void dialNumRadioItemStateChanged (java.awt.event.ItemEvent evt)//GEN-FIRST:event_dialNumRadioItemStateChanged
	{//GEN-HEADEREND:event_dialNumRadioItemStateChanged
		if ( evt.getStateChange () == ItemEvent.SELECTED )
		{
			switchToNumberEntry ();
		}
	}//GEN-LAST:event_dialNumRadioItemStateChanged

	private void dialNumRadioStateChanged (javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dialNumRadioStateChanged
	{//GEN-HEADEREND:event_dialNumRadioStateChanged
		if ( dialNumRadio.isSelected () )
		{
			switchToNumberEntry ();
		}
	}//GEN-LAST:event_dialNumRadioStateChanged

	private void dialCmdRadioItemStateChanged (java.awt.event.ItemEvent evt)//GEN-FIRST:event_dialCmdRadioItemStateChanged
	{//GEN-HEADEREND:event_dialCmdRadioItemStateChanged
		if ( evt.getStateChange () == ItemEvent.SELECTED )
		{
			switchToCommandEntry ();
		}
	}//GEN-LAST:event_dialCmdRadioItemStateChanged

	private void dialCmdRadioStateChanged (javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dialCmdRadioStateChanged
	{//GEN-HEADEREND:event_dialCmdRadioStateChanged
		if ( dialCmdRadio.isSelected () )
		{
			switchToCommandEntry ();
		}
	}//GEN-LAST:event_dialCmdRadioStateChanged

	private void answerButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_answerButActionPerformed
	{//GEN-HEADEREND:event_answerButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			TransferUtils.answer (tp,
				null, this, false, false, false);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "answerButActionPerformed");	// NOI18N
		}
	}//GEN-LAST:event_answerButActionPerformed

	private void volumeUpButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_volumeUpButActionPerformed
	{//GEN-HEADEREND:event_volumeUpButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			TransferUtils.volumeUp (tp,
				null, this, false, false, false);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "volumeUpButActionPerformed");	// NOI18N
		}
	}//GEN-LAST:event_volumeUpButActionPerformed

	private void volumeDownButActionPerformed (java.awt.event.ActionEvent evt)//GEN-FIRST:event_volumeDownButActionPerformed
	{//GEN-HEADEREND:event_volumeDownButActionPerformed

		TransferParameters tp = mw.getTransferParameters ();
		if ( tp == null || tp.getId () == null )
		{
			UiUtils.showErrorMessage(mw, MainWindow.NO_PORT_MSG);
			return;
		}
		try
		{
			TransferUtils.volumeDown (tp,
				null, this, false, false, false);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "volumeDownButActionPerformed");	// NOI18N
		}
	}//GEN-LAST:event_volumeDownButActionPerformed

	private void switchToNumberEntry ()
	{
		numberField.setEnabled (true);
		but0.setEnabled (true);
		but1.setEnabled (true);
		but2.setEnabled (true);
		but3.setEnabled (true);
		but4.setEnabled (true);
		but5.setEnabled (true);
		but6.setEnabled (true);
		but7.setEnabled (true);
		but8.setEnabled (true);
		but9.setEnabled (true);
		butAst.setEnabled (true);
		butHash.setEnabled (true);
		butPlus.setEnabled (true);
		dataRadio.setEnabled (true);
		voiceRadio.setEnabled (true);
		pulseDialRadio.setEnabled (true);
		toneDialRadio.setEnabled (true);
		autoDialRadio.setEnabled (true);

		dialCmdField.setEnabled (false);
	}

	private void switchToCommandEntry ()
	{
		numberField.setEnabled (false);
		but0.setEnabled (false);
		but1.setEnabled (false);
		but2.setEnabled (false);
		but3.setEnabled (false);
		but4.setEnabled (false);
		but5.setEnabled (false);
		but6.setEnabled (false);
		but7.setEnabled (false);
		but8.setEnabled (false);
		but9.setEnabled (false);
		butAst.setEnabled (false);
		butHash.setEnabled (false);
		butPlus.setEnabled (false);
		dataRadio.setEnabled (false);
		voiceRadio.setEnabled (false);
		pulseDialRadio.setEnabled (false);
		toneDialRadio.setEnabled (false);
		autoDialRadio.setEnabled (false);

		dialCmdField.setEnabled (true);
	}

	@Override
	public void setDestDir (String destDirName)
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
        private javax.swing.JButton answerBut;
        private javax.swing.JRadioButton autoDialRadio;
        private javax.swing.JButton but0;
        private javax.swing.JButton but1;
        private javax.swing.JButton but2;
        private javax.swing.JButton but3;
        private javax.swing.JButton but4;
        private javax.swing.JButton but5;
        private javax.swing.JButton but6;
        private javax.swing.JButton but7;
        private javax.swing.JButton but8;
        private javax.swing.JButton but9;
        private javax.swing.JButton butAst;
        private javax.swing.JButton butHash;
        private javax.swing.JButton butPlus;
        private javax.swing.JRadioButton dataRadio;
        private javax.swing.JButton dialBut;
        private javax.swing.JTextField dialCmdField;
        private javax.swing.JRadioButton dialCmdRadio;
        private javax.swing.JRadioButton dialNumRadio;
        private javax.swing.ButtonGroup dialTypeGroup;
        private javax.swing.JButton hangUpBut;
        private javax.swing.JTextField numberField;
        private javax.swing.JRadioButton pulseDialRadio;
        private javax.swing.JRadioButton toneDialRadio;
        private javax.swing.ButtonGroup tonePulseDialGroup;
        private javax.swing.ButtonGroup voiceDataDialGroup;
        private javax.swing.JRadioButton voiceRadio;
        private javax.swing.JButton volumeDownBut;
        private javax.swing.JButton volumeUpBut;
        // End of variables declaration//GEN-END:variables

}
