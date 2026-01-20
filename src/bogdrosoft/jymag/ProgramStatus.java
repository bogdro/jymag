/*
 * ProgramStatus.java, part of the JYMAG package.
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

import java.util.ResourceBundle;

/**
 * The enumeration of possible program states.
 * @author Bogdan Drozdowski
 */
public enum ProgramStatus
{
	/** The READY state. */
	READY,
	/** The SENDING state. */
	SENDING,
	/** The RECEIVING state. */
	RECEIVING;

	private static final ResourceBundle rcBundle =
		ResourceBundle.getBundle("bogdrosoft/jymag/i18n/MainWindow"); // NOI18N
	private static final String readyString =
		rcBundle.getString("READY"); // NOI18N
	private static final String sendingString =
		rcBundle.getString("SENDING"); // NOI18N
	private static final String recvString =
		rcBundle.getString("RECEIVING"); // NOI18N

	@Override
	public String toString()
	{
		switch (this)
		{
			case READY:
				return readyString;
			case SENDING:
				return sendingString;
			case RECEIVING:
				return recvString;
			default:
				break;
		}
		return Utils.EMPTY_STR;
	}	
}
