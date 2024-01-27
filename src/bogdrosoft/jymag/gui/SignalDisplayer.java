/*
 * SignalDisplayer.java, part of the JYMAG package.
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

import bogdrosoft.jymag.Utils;
import bogdrosoft.jymag.comm.DataTransporter;
import java.awt.Color;
import java.awt.event.ItemEvent;

/**
 * This class represents the signal power displaying window of the JYMAG program.
 * @author Bogdan Drozdowski
 */
public class SignalDisplayer extends javax.swing.JFrame
{
	private static final long serialVersionUID = 78L;

	/** The Thread that updates the power level display. */
	private transient Thread updater = null;
	/** The thread control field. */
	private volatile boolean runUpdater = true;

	/** The DataTransporter used for retrieveing the signal power level. */
	private final transient DataTransporter dt;
	/** The synchronization field. */
	private final transient Object sync;
	/** The refresh interval, in milliseconds. */
	private static final int REFRESH_TIME = 1000;

	private static final String DBM_STR = "dBm";	// NOI18N

	private final MainWindow mw;

	/**
	 * Creates new form SignalDisplayer.
	 * @param dtr The DataTransporter instance to use for communications.
	 * Must already be open.
	 * @param parent The parent MainWindow.
	 * @param synchro A synchronization variable.
	 * @param fontSize The font size for this window.
	 */
	public SignalDisplayer (DataTransporter dtr, MainWindow parent,
		Object synchro, float fontSize)
	{
		dt = dtr;
		sync = synchro;
		mw = parent;
		if ( dtr == null || synchro == null )
		{
			dispose ();
			return;
		}

		initComponents ();
		UiUtils.changeSizeToScreen(this);

		fontSpin.setValue (fontSize);	// refresh the font in the window
		/* add the Esc key listener to the frame and all components. */
		new EscKeyListener (this).install();
		// start the Thread:
		runUpdater = true;
		start ();
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

                jScrollPane1 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                exitBut = new javax.swing.JButton();
                jLabel3 = new javax.swing.JLabel();
                jLabel4 = new javax.swing.JLabel();
                fontSpin = new javax.swing.JSpinner();
                onTopCB = new javax.swing.JCheckBox();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bogdrosoft/jymag/i18n/SignalDisplayer"); // NOI18N
                setTitle(bundle.getString("title_signal")); // NOI18N
                setIconImage((new javax.swing.ImageIcon (getClass ().getResource ("/bogdrosoft/jymag/rsrc/jymag-icon-phone.png"))).getImage ());
                addWindowListener(new java.awt.event.WindowAdapter()
                {
                        public void windowClosing(java.awt.event.WindowEvent evt)
                        {
                                formWindowClosing(evt);
                        }
                });

                jLabel1.setText("-51dBm"); // NOI18N

                jLabel2.setText("-113dBm"); // NOI18N

                powerLevel.setForeground(new java.awt.Color(255, 0, 0));
                powerLevel.setMaximum(31);
                powerLevel.setOrientation(javax.swing.JProgressBar.VERTICAL);

                exitBut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bogdrosoft/jymag/rsrc/exit.png"))); // NOI18N
                exitBut.setText(bundle.getString("exit")); // NOI18N
                exitBut.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                exitButActionPerformed(evt);
                        }
                });

                jLabel3.setText(bundle.getString("power")); // NOI18N

                powerLabel.setText("?"); // NOI18N

                jLabel4.setText(bundle.getString("font_size")); // NOI18N

                fontSpin.setModel(new javax.swing.SpinnerNumberModel(12, 1, null, 1));
                fontSpin.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                fontSpinStateChanged(evt);
                        }
                });

                onTopCB.setText(bundle.getString("on_top")); // NOI18N
                onTopCB.addChangeListener(new javax.swing.event.ChangeListener()
                {
                        public void stateChanged(javax.swing.event.ChangeEvent evt)
                        {
                                onTopCBStateChanged(evt);
                        }
                });
                onTopCB.addItemListener(new java.awt.event.ItemListener()
                {
                        public void itemStateChanged(java.awt.event.ItemEvent evt)
                        {
                                onTopCBItemStateChanged(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(onTopCB)
                                        .addComponent(exitBut, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addGap(20, 20, 20))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                .addComponent(powerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(powerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(fontSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(29, Short.MAX_VALUE))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel2))
                                        .addComponent(powerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(powerLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(onTopCB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(fontSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(exitBut)
                                .addContainerGap())
                );

                exitBut.getAccessibleContext().setAccessibleName(bundle.getString("exit_but")); // NOI18N
                jLabel3.getAccessibleContext().setAccessibleName(bundle.getString("power_label")); // NOI18N
                jLabel4.getAccessibleContext().setAccessibleName(bundle.getString("font_label")); // NOI18N
                fontSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_spin")); // NOI18N
                onTopCB.getAccessibleContext().setAccessibleName(bundle.getString("ontop_cb")); // NOI18N

                jScrollPane1.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                );

                getAccessibleContext().setAccessibleName(bundle.getString("access_title_signal")); // NOI18N

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void exitButActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButActionPerformed

		exit ();
	}//GEN-LAST:event_exitButActionPerformed

	private void fontSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSpinStateChanged

		UiUtils.setFontSize (this, UiUtils.getFontSize (fontSpin));

	}//GEN-LAST:event_fontSpinStateChanged

	private void onTopCBItemStateChanged (java.awt.event.ItemEvent evt)//GEN-FIRST:event_onTopCBItemStateChanged
	{//GEN-HEADEREND:event_onTopCBItemStateChanged
		if ( evt.getStateChange () == ItemEvent.SELECTED )
		{
			setAlwaysOnTop (true);
		}
		else
		{
			setAlwaysOnTop (false);
		}

	}//GEN-LAST:event_onTopCBItemStateChanged

	private void onTopCBStateChanged (javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_onTopCBStateChanged
	{//GEN-HEADEREND:event_onTopCBStateChanged
		if ( onTopCB.isSelected () )
		{
			setAlwaysOnTop (true);
		}
		else
		{
			setAlwaysOnTop (false);
		}
	}//GEN-LAST:event_onTopCBStateChanged

	private void formWindowClosing (java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
	{//GEN-HEADEREND:event_formWindowClosing
		exit ();
	}//GEN-LAST:event_formWindowClosing

	private void exit ()
	{
		runUpdater = false;
		if ( updater != null )
		{
			while ( updater.isAlive () )
			{
				// only hurts: when the thread is interrupted, the window won't close
				//updater.interrupt ();
				Utils.sleepIgnoreException(10);
			}
		}
		dt.close ();
		mw.setReadyStatus ();
		dispose ();
	}

	/**
	 * Sets the given signal level on the progress bar and the label.
	 * @param level The level to set.
	 */
	private void setLevel (final int level)
	{
		UiUtils.changeGUI (new Runnable ()
		{
			@Override
			public synchronized void run ()
			{
				if ( level >= 0 && level <= 31 )
				{
					powerLevel.setValue (level);
					powerLevel.setForeground (
						new Color (
							Math.min (255, 255 - ((level-16)*16)),	// red
							Math.min (255, 0 + (level*16)),	// green
							0			// blue
						));
					powerLabel.setText (
						String.valueOf (-113 + level*2) + DBM_STR);
				}
				else
				{
					powerLevel.setValue (0);
					powerLabel.setText (Utils.QUESTION_MARK);
				}
			}

			@Override
			public String toString ()
			{
				return "SignalDisplayer.setLevel.Runnable";	// NOI18N
			}
		});
	}

	private void start ()
	{
		updater = new Thread (new Runnable ()
		{
			private int prevLevel = 0;
			private int currLevel = 0;

			@Override
			public synchronized void run ()
			{
				while (runUpdater && ! Thread.interrupted ())
				{
					synchronized (sync)
					{
						currLevel = dt.getSignalPower ();
					}
					if ( ! runUpdater )
					{
						break;
					}
					if ( currLevel != prevLevel )
					{
						setLevel (currLevel);
						prevLevel = currLevel;
					}
					if ( ! runUpdater )
					{
						break;
					}
					try
					{
						Thread.sleep (REFRESH_TIME);
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
				return "SignalDisplayer.start.Runnable";	// NOI18N
			}
		});
		mw.setReceivingStatus ();
		updater.start ();
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton exitBut;
        private javax.swing.JSpinner fontSpin;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JCheckBox onTopCB;
        private final javax.swing.JLabel powerLabel = new javax.swing.JLabel();
        private final javax.swing.JProgressBar powerLevel = new javax.swing.JProgressBar();
        // End of variables declaration//GEN-END:variables
}
