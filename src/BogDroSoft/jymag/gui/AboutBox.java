/*
 * AboutBox.java, part of the JYMAG package.
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

package BogDroSoft.jymag.gui;

import BogDroSoft.jymag.Utils;
import java.awt.Cursor;
import java.awt.Desktop;
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
	private static final String EMAIL_ADDR = "bogdro@users.sourceforge.net";	// NOI18N
	private static final String WWW_ADDR1 = "https://jymag.sourceforge.io/";		// NOI18N
	private static final String WWW_ADDR2 = "http://bogdro.users.sourceforge.net/";	// NOI18N
	private static final URI URI_MAILTO =
		createURI("mailto", EMAIL_ADDR + "?subject=[SOFT] - JYMAG");		// NOI18N
	private static final URI URI_WWW1 = createWebURI(WWW_ADDR1);
	private static final URI URI_WWW2 = createWebURI(WWW_ADDR2);

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

		UiUtils.changeSizeToScreen(this);

		fontSizeSpin.setValue (fontSize);	// refresh the font in the window

		/* add the Esc key listener to the frame and all components. */
		new EscKeyListener (this).install();
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

                jScrollPane2 = new javax.swing.JScrollPane();
                jPanel1 = new javax.swing.JPanel();
                jymagLabel = new javax.swing.JLabel();
                authorLabel = new javax.swing.JLabel();
                emailLabel = new javax.swing.JLabel();
                wwwLabel = new javax.swing.JLabel();
                www1Label = new javax.swing.JLabel();
                www2Label = new javax.swing.JLabel();
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

                jymagLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BogDroSoft/jymag/rsrc/about.png"))); // NOI18N
                jymagLabel.setText("JYMAG - Jig Your Music And Graphics " /* NOI18N */ + MainWindow.JYMAG_VERSION);
                jymagLabel.setIconTextGap(40);

                authorLabel.setText(bundle.getString("author")); // NOI18N

                emailLabel.setText("<html><a href=\"mailto:" + EMAIL_ADDR + "\">" + EMAIL_ADDR + "</a></html>");
                emailLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                emailLabel.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                emailLabelMouseClicked(evt);
                        }
                });

                wwwLabel.setText("WWW:"); // NOI18N

                www1Label.setText("<html><a href=\"" + WWW_ADDR1 + "\">" + WWW_ADDR1 + "</a></html>");
                www1Label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                www1Label.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                www1LabelMouseClicked(evt);
                        }
                });

                www2Label.setText("<html><a href=\"" + WWW_ADDR2 + "\">" + WWW_ADDR2 + "</a></html>");
                www2Label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                www2Label.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                www2LabelMouseClicked(evt);
                        }
                });

                licLabel.setText(bundle.getString("License:_")); // NOI18N

                suppOperLabel.setText(bundle.getString("supp_oper")); // NOI18N

                thanksLabel.setText(bundle.getString("Thanks_to:")); // NOI18N

                thanksTo.setText("<html>Sharp (sharpy+at+xox.pl) - GetPic.pl, sebtx452 @ gmail.com - wxPicSound,<br>\nMIKSOFT - \"Mobile Media Converter\", \"ffmpeg project\" - ffmpeg,<br>\nFizzed, Inc. (http://fizzed.com/) - new RxTx binaries</html>"); // NOI18N

                licenseArea.setEditable(false);
                licenseArea.setColumns(20);
                licenseArea.setRows(3);
                licenseArea.setText(getFileContents (getClass ().getClassLoader ().getResourceAsStream ("BogDroSoft/jymag/rsrc/GNU-GPLv3.txt" /* NOI18N */)));
                jScrollPane1.setViewportView(licenseArea);
                licenseArea.getAccessibleContext().setAccessibleName("license text"); // NOI18N

                licValue.setText("GPLv3+"); // NOI18N

                authorVal.setText("Bogdan Drozdowski"); // NOI18N

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
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jymagLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(fontSizeLab, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(fontSizeSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(52, 52, 52))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                                                        .addComponent(www1Label)))
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                                                .addGap(27, 27, 27)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );

                jymagLabel.getAccessibleContext().setAccessibleName(bundle.getString("prog_name")); // NOI18N
                authorLabel.getAccessibleContext().setAccessibleName(bundle.getString("author_label")); // NOI18N
                emailLabel.getAccessibleContext().setAccessibleName(bundle.getString("e-mail_addr")); // NOI18N
                wwwLabel.getAccessibleContext().setAccessibleName(bundle.getString("web_label")); // NOI18N
                www1Label.getAccessibleContext().setAccessibleName(bundle.getString("1stWWW")); // NOI18N
                www2Label.getAccessibleContext().setAccessibleName(bundle.getString("2ndWWW")); // NOI18N
                licLabel.getAccessibleContext().setAccessibleName(bundle.getString("lic_label")); // NOI18N
                suppOperLabel.getAccessibleContext().setAccessibleName(bundle.getString("acc_supp_oper")); // NOI18N
                thanksLabel.getAccessibleContext().setAccessibleName(bundle.getString("thank_label")); // NOI18N
                thanksTo.getAccessibleContext().setAccessibleName("thanks"); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleName(bundle.getString("font_size_spinner")); // NOI18N
                fontSizeSpin.getAccessibleContext().setAccessibleDescription(bundle.getString("change_font_size")); // NOI18N
                fontSizeLab.getAccessibleContext().setAccessibleName(bundle.getString("font_label")); // NOI18N

                jScrollPane2.setViewportView(jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                );

                getAccessibleContext().setAccessibleName(bundle.getString("access_title_about")); // NOI18N

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
						{
							d.mail (URI_MAILTO);	// NOI18N
						}
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
						{
							Desktop.getDesktop ().browse (URI_WWW1);
						}
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
						{
							Desktop.getDesktop ().browse (URI_WWW2);
						}
					}
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "Desktop.browse2");	// NOI18N
				}
			}
		}
	}//GEN-LAST:event_www2LabelMouseClicked

	private void fontSizeSpinStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpinStateChanged

		UiUtils.setFontSize (this, UiUtils.getFontSize (fontSizeSpin));

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
		StringBuilder ret = new StringBuilder (40000);
		byte[] read = new byte[1024];
		int wasRead;
		do
		{
			try
			{
				wasRead = is.read (read);
				ret.append (new String (read, 0, wasRead, "UTF-8"));	// NOI18N
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
		return ret.toString ();
	}
	
	private static URI createURI (String uriType, String uri)
	{
		try
		{
			return new URI (uriType, uri, null);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "AboutBox.createURI: '" + uri + "'");	// NOI18N
			UiUtils.showErrorMessage(null, ex.toString());
			return null;
		}
	}

	private static URI createWebURI (String webAddr)
	{
		try
		{
			return new URI (webAddr);
		}
		catch (Exception ex)
		{
			Utils.handleException (ex, "AboutBox.createWebURI: '" + webAddr + "'");	// NOI18N
			UiUtils.showErrorMessage(null, ex.toString());
			return null;
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
        private javax.swing.JLabel wwwLabel;
        // End of variables declaration//GEN-END:variables

}
