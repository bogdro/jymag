/*
 * UiTestHelper.java, part of the JYMAG package.
 *
 * Copyright (C) 2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.awt.Window;
import java.awt.event.KeyListener;

/**
 * UiTestHelper - a utility class for user interface tests.
 * @author Bogdan Drozdowski
 */
public class UiTestHelper
{
	private UiTestHelper() { /* utility class */ }

	public static boolean isKeyListenerPresent(Window w)
	{
		KeyListener[] listeners = w.getKeyListeners();
		for (int i = 0; i < listeners.length; i++)
		{
			if (listeners[i] instanceof EscKeyListener)
			{
				return true;
			}
		}
		return false;
	}
}
