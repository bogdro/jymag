/*
 * AboutBox.java, part of the JYMAG package.
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
 *		Free Software Foundation
 *		51 Franklin Street, Fifth Floor
 *		Boston, MA 02110-1301
 *		USA
 *
 */

package BogDroSoft.jymag;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URI;
import javax.swing.JLabel;

/**
 * The "About..." window box, containing the program name, its version number,
 * author, license, thanks and all the necessary stuff.
 * @author Bogdan Drozdowski
 */
public class AboutBox extends javax.swing.JDialog
{
	private static final long serialVersionUID = 72L;
	private final String emailAddr = "bogdandr@op.pl";		// NOI18N
	private final String host1 = "jymag.sf.net";			// NOI18N
	private final String path1 = "/";				// NOI18N
	private final String www1Addr = "http://" + host1 + path1;	// NOI18N
	private final String host2 = "rudy.mif.pg.gda.pl";		// NOI18N
	private final String path2 = "/~bogdro/soft";			// NOI18N
	private final String www2Addr = "http://" + host2 + path2;	// NOI18N
	private final String host3 = "rudy.mif.pg.gda.pl";		// NOI18N
	private final String path3 = "/~bogdro/inne";			// NOI18N
	private final String www3Addr = "http://" + host3 + path3;	// NOI18N

	private final KL kl = new KL ();

	/**
	 * Creates new form AboutBox.
	 * @param parent The parent frame.
	 * @param modal Whether this dialog box should be modal.
	 * @param fontSize The font size for this window.
	 */
	public AboutBox (java.awt.Frame parent, boolean modal, float fontSize)
	{
		// make modal
		super (parent, true);
		initComponents ();
		licenseArea.setCaretPosition (0);
		emailLabel.setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		www1Label .setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		www2Label .setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		www3Label .setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		setSize (700, 765);
		fontSizeSpin.setValue (fontSize);	// refresh the font in the window
		fontSizeLab.setHorizontalAlignment (JLabel.RIGHT);

		fontSizeSpin.addKeyListener (kl);
		licenseArea.addKeyListener (kl);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jScrollPane2 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                jymagLabel = new javax.swing.JLabel();
                authorLabel = new javax.swing.JLabel();
                emailLabel = new javax.swing.JLabel();
                wwwLabel = new javax.swing.JLabel();
                www1Label = new javax.swing.JLabel();
                www2Label = new javax.swing.JLabel();
                www3Label = new javax.swing.JLabel();
                licLabel = new javax.swing.JLabel();
                suppOperLabel = new javax.swing.JLabel();
                thanksLabel = new javax.swing.JLabel();
                thanksTo = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                licenseArea = new javax.swing.JTextArea();
                licValue = new javax.swing.JLabel();
                authorVal = new javax.swing.JLabel();
                fontSizeSpin = new javax.swing.JSpinner();
                fontSizeLab = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("BogDroSoft/jymag/i18n/AboutBox"); // NOI18N
                setTitle(bundle.getString("About_JYMAG")); // NOI18N
                setModal(true);

                jymagLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/QMARK8.png"))); // NOI18N
                jymagLabel.setText("JYMAG - Java Your Music And Graphics " + MainWindow.verString);
                jymagLabel.setIconTextGap(40);

                authorLabel.setText(bundle.getString("author")); // NOI18N

                emailLabel.setText("<html><a href=\"mailto:" + emailAddr + "\">" + emailAddr + "</a></html>");
                emailLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                emailLabelMouseClicked(evt);
                        }
                });

                wwwLabel.setText("WWW:"); // NOI18N

                www1Label.setText("<html><a href=\"" + www1Addr + "\">" + www1Addr + "</a></html>");
                www1Label.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                www1LabelMouseClicked(evt);
                        }
                });

                www2Label.setText("<html><a href=\"" + www2Addr + "\">" + www2Addr + "</a></html>");
                www2Label.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                www2LabelMouseClicked(evt);
                        }
                });

                www3Label.setText("<html><a href=\"" + www3Addr + "\">" + www3Addr + "</a></html>");
                www3Label.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                www3LabelMouseClicked(evt);
                        }
                });

                licLabel.setText(bundle.getString("License:_")); // NOI18N

                suppOperLabel.setText(bundle.getString("supp_oper")); // NOI18N

                thanksLabel.setText(bundle.getString("Thanks_to:")); // NOI18N

                thanksTo.setText("<html>Sharp (sharpy+at+xox.pl) for GetPic.pl, sebtx452 @ gmail.com for wxPicSound,<br>\nMIKSOFT for \"Mobile Media Converter\", \"ffmpeg project\" for ffmpeg\n</html>"); // NOI18N

                licenseArea.setColumns(20);
                licenseArea.setEditable(false);
                licenseArea.setRows(3);
                licenseArea.setText(getFileContents (getClass ().getClassLoader ().getResourceAsStream ("BogDroSoft/jymag/rsrc/GNU-GPLv3.txt")));
                jScrollPane1.setViewportView(licenseArea);
                licenseArea.getAccessibleContext().setAccessibleName(bundle.getString("license_text")); // NOI18N

                licValue.setText("GPLv3+"); // NOI18N

                authorVal.setText("Bogdan Drozdowski"); // NOI18N

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
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(licLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(licValue))
                                        .addComponent(suppOperLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(thanksLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(thanksTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(authorLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(authorVal)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(emailLabel))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(wwwLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(www2Label)
                                                        .addComponent(www1Label)
                                                        .addComponent(www3Label)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jymagLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(fontSizeLab, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(21, 21, 21)))
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                                        .addContainerGap()))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jymagLabel)
                                                .addGap(9, 9, 9)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(authorLabel)
                                                        .addComponent(authorVal)
                                                        .addComponent(emailLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(wwwLabel)
                                                        .addComponent(www1Label))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(www2Label)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(www3Label)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(licLabel)
                                                        .addComponent(licValue))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(suppOperLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(fontSizeLab)
                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(thanksLabel)
                                        .addComponent(thanksTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(92, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(643, 643, 643)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                                        .addContainerGap()))
                );

                jymagLabel.getAccessibleContext().setAccessibleName(bundle.getString("prog_name")); // NOI18N
                emailLabel.getAccessibleContext().setAccessibleName(bundle.getString("e-mail_addr")); // NOI18N
                www1Label.getAccessibleContext().setAccessibleName(bundle.getString("1stWWW")); // NOI18N
                www2Label.getAccessibleContext().setAccessibleName(bundle.getString("2ndWWW")); // NOI18N
                www3Label.getAccessibleContext().setAccessibleName(bundle.getString("3rdWWW")); // NOI18N
                suppOperLabel.getAccessibleContext().setAccessibleName(bundle.getString("acc_supp_oper")); // NOI18N
                thanksTo.getAccessibleContext().setAccessibleName(bundle.getString("thanks")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N

                jScrollPane2.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	private void emailLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailLabelMouseClicked

		if ( evt.getButton () == MouseEvent.BUTTON1 )
		{
			if ( Desktop.isDesktopSupported () )
			{
				try
				{
					Desktop d = Desktop.getDesktop ();
					if ( d != null )
					{
						if ( d.isSupported (Desktop.Action.MAIL) )
							d.mail (new URI ("mailto",	// NOI18N
								emailAddr + "?subject=SOFT - JYMAG", null));	// NOI18N
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "Desktop.mail");	// NOI18N
				}
			}
		}
	}//GEN-LAST:event_emailLabelMouseClicked

	private void www1LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_www1LabelMouseClicked

		if ( evt.getButton () == MouseEvent.BUTTON1 )
		{
			if ( Desktop.isDesktopSupported () )
			{
				try
				{
					Desktop d = Desktop.getDesktop ();
					if ( d != null )
					{
						if ( d.isSupported (Desktop.Action.BROWSE) )
							Desktop.getDesktop ().browse (new URI ("http", null,	// NOI18N
								host1, 80, path1, null, null));
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "Desktop.browse1");	// NOI18N
				}
			}
		}
	}//GEN-LAST:event_www1LabelMouseClicked

	private void www2LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_www2LabelMouseClicked

		if ( evt.getButton () == MouseEvent.BUTTON1 )
		{
			if ( Desktop.isDesktopSupported () )
			{
				try
				{
					Desktop d = Desktop.getDesktop ();
					if ( d != null )
					{
						if ( d.isSupported (Desktop.Action.BROWSE) )
							Desktop.getDesktop ().browse (new URI ("http", null,	// NOI18N
								host2, 80, path2, null, null));
							//Desktop.getDesktop ().browse (new URI (www2Addr));
							//Desktop.getDesktop ().browse (new URI ("http",	// NOI18N
							//	host2, path2, null));
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "Desktop.browse2");	// NOI18N
				}
			}
		}
	}//GEN-LAST:event_www2LabelMouseClicked

	private void www3LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_www3LabelMouseClicked

		if ( evt.getButton () == MouseEvent.BUTTON1 )
		{
			if ( Desktop.isDesktopSupported () )
			{
				try
				{
					Desktop d = Desktop.getDesktop ();
					if ( d != null )
					{
						if ( d.isSupported (Desktop.Action.BROWSE) )
							Desktop.getDesktop ().browse (new URI ("http", null,	// NOI18N
								host3, 80, path3, null, null));
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "Desktop.browse3");	// NOI18N
				}
			}
		}
	}//GEN-LAST:event_www3LabelMouseClicked

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		Object val = fontSizeSpin.getValue ();
		if ( val != null && val instanceof Number )
			Utils.setFontSize (this, ((Number)val).floatValue ());
	}//GEN-LAST:event_fontSizeSpinStateChanged

	/*
	private String getFileContents (String fileName)
	{
		File f = new File (fileName);
		if ( (! f.exists ()) || (! f.canRead ()) )
		{
			return "";	// NOI18N
		}
		return getFileContents (f);
	}

	private String getFileContents (URL fileName)
	{
		try
		{
			return getFileContents (new File (fileName.toURI ()));
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, fileName.toString ());
			return "";	// NOI18N
		}
	}

	private String getFileContents (File f)
	{
		try
		{
			return getFileContents (new FileInputStream (f));
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "new FileInputStream (" + f.getName () + ")");	// NOI18N
			return "";	// NOI18N
		}
	}
	*/

	private String getFileContents (InputStream is)
	{
		String ret = "";	// NOI18N
		byte[] read = new byte[1024];
		int wasRead;
		do
		{
			try
			{
				wasRead = is.read (read);
				ret += new String (read, 0, wasRead);
			}
			catch (Exception ex)
			{
				Utils.handleException (ex, "InputStream.read");	// NOI18N
				wasRead = 0;
			}
		} while (wasRead == read.length);
		try
		{
			is.close ();
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "InputStream.close");	// NOI18N
		}
		return ret;
	}

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
        private javax.swing.JLabel authorLabel;
        private javax.swing.JLabel authorVal;
        private javax.swing.JLabel emailLabel;
        private javax.swing.JLabel fontSizeLab;
        private javax.swing.JSpinner fontSizeSpin;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JLabel jymagLabel;
        private javax.swing.JLabel licLabel;
        private javax.swing.JLabel licValue;
        private javax.swing.JTextArea licenseArea;
        private javax.swing.JLabel suppOperLabel;
        private javax.swing.JLabel thanksLabel;
        private javax.swing.JLabel thanksTo;
        private javax.swing.JLabel www1Label;
        private javax.swing.JLabel www2Label;
        private javax.swing.JLabel www3Label;
        private javax.swing.JLabel wwwLabel;
        // End of variables declaration//GEN-END:variables

}