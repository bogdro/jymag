/*
 * Starter.java, part of the JYMAG package.
 *
 * Copyright (C) 2009-2014 Bogdan Drozdowski, bogdandr (at) op.pl
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

import BogDroSoft.jymag.gui.MainWindow;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 * This class starts the JYMAG program or reports an error to the user.
 * @author Bogdan Drozdowski
 */
public class Starter
{
	private static final String startError
		= ResourceBundle.getBundle("BogDroSoft/jymag/i18n/Starter")
		.getString("JYMAG_Start_Error");
	private static final String errString
		= ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow")
		.getString("Error");

	private Starter ()
	{
		// private constructor so the class can't be instantiated
	}

	/**
	 * JYMAG program starting point.
	 * @param args the command line arguments.
	 */
	public static void main (String[] args)
	{
		try
		{
			MainWindow.start (args);
		}
		catch (Throwable ex)
		{
			Utils.handleException (ex, "*** " + startError + " ***"); // NOI18N
			try
			{
				JOptionPane.showMessageDialog (null, startError,
					errString, JOptionPane.ERROR_MESSAGE);
			}
			catch (Exception ex2)
			{
				// ignore exceptions thrown when displaying an exception
			}
			// close the log file:
			Utils.redirectStderrToFile (null);
			// exit the program:
			System.exit (-1);
		}
	}
}
