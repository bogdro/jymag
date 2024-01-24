/*
 * UiUtils.java, part of the JYMAG package.
 *
 * Copyright (C) 2022-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * A utility class for the user interface, containing some useful methods.
 * @author Bogdan Drozdowski
 */
public class UiUtils {

	/* use our own in case MainWindow doesn't load */
	private static final String ERR_STRING
		= ResourceBundle.getBundle("BogDroSoft/jymag/i18n/MainWindow")
		.getString("Error");

	private UiUtils ()
	{
		// non-instantiable
	}

	/**
	 * Crates a file chooser for opening single files of the given type.
	 * @param description The description to display in the filters list.
	 * @param filetype The Map with file extensions as keys.
	 * @return The file chooser for opening of selected file types.
	 */
	public static JFileChooser createOpenFileChooser(
		final String description, final Map<String, Integer> filetype)
	{
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setFileFilter(new JYMAGFileFilter(description, filetype));
		return fc;
	}

	/**
	 * Method used to change the GUI - runs r.run () on the
	 * EventDispatchThread and waits for it to exit.
	 * @param r The code to run.
	 */
	public static synchronized void changeGUI(final Runnable r)
	{
		if (r == null)
		{
			return;
		}
		if (SwingUtilities.isEventDispatchThread())
		{
			try
			{
				r.run();
			}
			catch (Throwable ex)
			{
				Utils.handleException(ex, "changeGUI->r.run"); // NOI18N
			}
		}
		else
		{
			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					@Override
					public synchronized void run()
					{
						try
						{
							r.run();
						}
						catch (Throwable ex)
						{
							Utils.handleException(ex,
								"changeGUI->invokeAndWait->r.run"); // NOI18N
						}
					}

					@Override
					public String toString()
					{
						return "UiUtils.changeGUI.Runnable"; // NOI18N
					}
				});
			}
			catch (InterruptedException ex)
			{
				// can be called when closing the program, so ignore
				//Utils.handleException (ex, "changeGUI->invokeAndWait->Interrupt");// NOI18N
			}
			catch (InvocationTargetException ex)
			{
				Utils.handleException(ex, "changeGUI->invokeAndWait"); // NOI18N
			}
		}
	}

	/**
	 * Set the given font size in all the components in the given
	 * Component (recursively, if it's a Container).
	 * @param c The Component with Components that will have their font
	 *	size changed.
	 * @param newSize The new font size to set.
	 */
	public static void setFontSize(Component c, float newSize)
	{
		if (c == null)
		{
			return;
		}
		c.setFont(c.getFont().deriveFont(newSize));
		if (c instanceof Container)
		{
			Component[] subComps = ((Container) c).getComponents();
			if (subComps != null)
			{
				for (int i = 0; i < subComps.length; i++)
				{
					if (subComps[i] != null)
					{
						setFontSize(subComps[i], newSize);
					}
				}
			}
		}
	}

	/**
	 * Returns the font size as intepreted from the given spinner.
	 * @param spinner the spinner to read the font size from.
	 * @return the font size as intepreted from the given spinner,
	 *	or a default value.
	 */
	public static float getFontSize(JSpinner spinner)
	{
		float fontSize = 12;
		if (spinner != null)
		{
			Object val = spinner.getValue();
			if (val != null && (val instanceof Number))
			{
				fontSize = ((Number) val).floatValue();
			}
		}
		return fontSize;
	}

	/**
	 * Resizes the given to fit the screen if it is larger than it.
	 * @param w the Window to resize.
	 */
	public static void changeSizeToScreen(Window w)
	{
		GraphicsConfiguration gc = null;
		Insets is = null;
		try
		{
			gc = w.getGraphicsConfiguration ();
			Toolkit tk = Toolkit.getDefaultToolkit ();
			if ( tk != null )
			{
				is = tk.getScreenInsets (gc);
			}
		}
		catch (HeadlessException ex)
		{
			Utils.handleException (ex,
				"UiUtils:changeSizeToScreen:GraphicsConfiguration/Toolkit");	// NOI18N
		}
		int maxX = 800;
		int maxY = 600;
		if ( gc != null )
		{
			Rectangle bounds = gc.getBounds ();
			if ( bounds != null )
			{
				if ( is != null )
				{
					maxX = bounds.width - is.left - is.right;
					maxY = bounds.height - is.top - is.bottom;
				}
				else
				{
					maxX = bounds.width;
					maxY = bounds.height;
				}
			}
		}
		if ( (w instanceof JFrame)
			&& (((JFrame)w).getExtendedState () & JFrame.MAXIMIZED_BOTH) == 0 )
		{
			// if not maximized, verify position and size
			if ( w.getWidth () <= 0 )
			{
				w.setSize (maxX, w.getHeight ());
			}
			if ( w.getHeight () <= 0 )
			{
				w.setSize (w.getWidth (), maxY);
			}
			if ( w.getX () + w.getWidth () < 0
				|| w.getX () > maxX )
			{
				w.setLocation (0, w.getY ());
			}
			if ( w.getY () + w.getHeight () < 0
				|| w.getY () > maxY )
			{
				w.setLocation (w.getX (), 0);
			}
		}
		Dimension size = w.getSize ();
		if ( size != null )
		{
			if ( size.width > maxX )
			{
				w.setSize (maxX, w.getHeight ());
			}
			if ( size.height > maxY )
			{
				w.setSize (w.getWidth (), maxY);
			}
			/*if ( size.width <= maxX && size.height <= maxY )
			{
				// change the size so that the scrollbars fit:
				size.height += 50;
				size.width += 50;
				w.setSize (size);
			}*/
		}
	}

	/**
	 * Shows a messagebox with the given error message.
	 * @param c The parent Component of the box. Can be <b>null</b>.
	 * @param msg The message to show in the messagebox.
	 */
	public static void showErrorMessage(Component c, String msg)
	{
		try
		{
			JOptionPane.showMessageDialog (c, msg,
				ERR_STRING, JOptionPane.ERROR_MESSAGE);
		}
		catch (HeadlessException ex)
		{
			// don't display exceptions about displaying exceptions
			Utils.handleException(ex, "UiUtils.showErrorMessage");
		}
	}

	/**
	 * A class that selects all rows of a JTable when its header is clicked.
	 */
	public static class TableMouseListener extends MouseAdapter
	{
		private final JTable table;

		/**
		 * Creates a new TableMouseListener for the given JTable.
		 * @param datatable The JTable to select on mouse click.
		 */
		public TableMouseListener (JTable datatable)
		{
			table = datatable;
		}

		@Override
		public void mouseClicked (MouseEvent me)
		{
			if ( me.getButton () == MouseEvent.BUTTON1 )
			{
				if ( table != null )
				{
					table.selectAll ();
				}
			}
		}

		@Override
		public String toString ()
		{
			return "UiUtils.TableMouseListener";	// NOI18N
		}
	}
}
