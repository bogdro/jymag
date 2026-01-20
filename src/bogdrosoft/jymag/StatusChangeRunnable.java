/*
 * StatusChangeRunnable.java, part of the JYMAG package.
 *
 * Copyright (C) 2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
package bogdrosoft.jymag;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * A "change status label Runnable" class for the UI.
 * @author Bogdan Drozdowski
 */
public class StatusChangeRunnable implements Runnable
{
	private static final Color GREEN_STATUS_COLOR = new Color(0, 204, 0) /*Color.GREEN*/ ;

	private final JLabel statusLabel;
	private final Utils.STATUS statusValue;
	private final Color c;

	public StatusChangeRunnable(JLabel status, Utils.STATUS s)
	{
		if (status == null || s == null)
		{
			throw new IllegalArgumentException("StatusChangeRunnable:StatusChangeRunnable():null"); // NOI18N
		}
		statusLabel = status;
		statusValue = s;
		if (Utils.STATUS.READY.equals(s))
		{
			c = GREEN_STATUS_COLOR;
		}
		else
		{
			c = Color.BLUE;
		}
	}

	@Override
	public synchronized void run()
	{
		statusLabel.setText(statusValue.toString());
		statusLabel.setForeground(c);
		statusLabel.validate();
	}

	@Override
	public String toString()
	{
		return "StatusChangeRunnable." + statusValue; // NOI18N
	}
}
