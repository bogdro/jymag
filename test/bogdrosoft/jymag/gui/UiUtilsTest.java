/*
 * UiUtilsTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.filechooser.FileFilter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 * UiUtilsTest - a test for the UiUtils class.
 * @author Bogdan Drozdowski
 */
public class UiUtilsTest
{
	/**
	 * Test of createOpenFileChooser method, of class UiUtils.
	 */
	@Test
	public void testCreateOpenFileChooser ()
	{
		System.out.println ("createOpenFileChooser");
		String description = "";
		Map<String, Integer> filetype = new HashMap<String, Integer> (1);
		filetype.put ("ext", 0);
		JFileChooser result = UiUtils.createOpenFileChooser (description, filetype);
		assertNotNull (result);
		FileFilter resultFilter = result.getFileFilter ();
		assertTrue (resultFilter.accept (new File ("test.ext")));
		assertFalse (resultFilter.accept (new File ("test.png")));
		assertTrue (resultFilter.accept (new File ("test.png.ext")));
	}

	/**
	 * Test of changeGUI method, of class UiUtils.
	 */
	@Test
	public void testChangeGUI ()
	{
		System.out.println ("changeGUI");
		Runnable r = null;
		UiUtils.changeGUI (r);
		// passing anything different from null can't be checked,
		// because there's no GUI
	}

	/**
	 * Test of setFontSize method, of class UiUtils.
	 */
	@Test
	public void testSetFontSize ()
	{
		System.out.println ("setFontSize");
		Component c = new JLabel ("testLabel");
		float newSize = 11.0F;
		UiUtils.setFontSize (c, newSize);
		if ( Math.abs (newSize - c.getFont ().getSize ()) > 0.1 )
		{
			fail ("Size differs: new=" + c.getFont ().getSize ()
				+ ", expected: " + newSize);
		}
	}

	/**
	 * Test of getFontSize method, of class UiUtils.
	 */
	@Test
	public void testGetFontSize ()
	{
		int fontSize = 25;
		System.out.println ("getFontSize");
		JSpinner spin = new JSpinner();
		spin.setValue(fontSize);
		assertTrue(Math.abs(fontSize - UiUtils.getFontSize(spin)) < 0.001);
	}

	/**
	 * Test of changeSizeToScreen method, of class UiUtils.
	 */
	@Test
	@Ignore("UI test without UI")
	public void testChangeSizeToScreen()
	{
		System.out.println("changeSizeToScreen");
		Window w = null;
		UiUtils.changeSizeToScreen(w);
	}

	/**
	 * Test of showErrorMessage method, of class UiUtils.
	 */
	@Test
	@Ignore("Requires interactive user action")
	public void testShowErrorMessage()
	{
		System.out.println("showErrorMessage");
		Component c = null;
		String msg = "";
		UiUtils.showErrorMessage(c, msg);
	}
}
