/*
 * PhoneMessage.java, part of the JYMAG package.
 *
 * Copyright (C) 2011 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a message in the phone.
 * @author Bogdan Drozdowski
 */
public class PhoneMessage
{
	private String status = null;
	private String alpha = null;
	private byte[] message = null;

	private static final Pattern messagePattern
						// index,    stat
		= Pattern.compile ("(\\+CMGL:)?\\s*\\d+\\s*,\\s*(\\w+)\\s*,\\s*"	// NOI18N
			// [alpha]    length      body
			+ "(\\w+)?\\s*,\\s*\\d+\\s*\r\n(.*)",   			// NOI18N
			Pattern.CASE_INSENSITIVE);


	/**
	 * Creates a new PhoneMessage with the givenstatus, alpha string and body.
	 * @param stat The status of the message.
	 * @param al The alpha string for the message.
	 * @param msg The body of the message.
	 */
	public PhoneMessage (String stat, String al, byte[] msg)
	{
		status = stat;
		alpha = al;
		message = msg;
	}

	/* ==================================================== */

	/**
	 * Returns the status of this message.
	 * @return the status of this message.
	 */
	public synchronized String getStatus ()
	{
		return status;
	}

	/**
	 * Returns the alpha string for the message.
	 * @return the alpha string for the message.
	 */
	public synchronized String getAlpha ()
	{
		return alpha;
	}

	/**
	 * Returns the body of the message.
	 * @return the body of the message.
	 */
	public synchronized byte[] getMessage ()
	{
		return message;
	}

	/**
	 * Returns the length of the message.
	 * @return the length of the message.
	 */
	public synchronized int getLength ()
	{
		return message.length;
	}

	/* ==================================================== */

	/**
	 * Sets the status of this message.
	 * @param newStat the new status of this message.
	 */
	public synchronized void setStatus (String newStat)
	{
		status = newStat;
	}

	/**
	 * Sets the alpha string for the message.
	 * @param newAlpha the new alpha string for the message.
	 */
	public synchronized void setAlpha (String newAlpha)
	{
		alpha = newAlpha;
	}

	/**
	 * Sets the body of the message.
	 * @param newMsg the new body of the message.
	 */
	public synchronized void setMessage (byte[] newMsg)
	{
		message = newMsg;
	}

	/* ==================================================== */

	/**
	 * Gets the message string for this message.
	 * @return the message string for this message, suitable for commands sent
	 *	to the phone.
	 */
	public synchronized String getMessageString ()
	{
		return String.valueOf (message.length) + "\r"			// NOI18N
			+ new String (message) + "\033" /* ESC */;		// NOI18N
	}

	/**
	 * Parses the given phone response and creates a PhoneMessage that matches it.
	 * @param response The response to parse.
	 * @return a PhoneMessage that matches the given response.
	 */
	public synchronized static PhoneMessage parseReponse (String response)
	{
		if ( response == null ) return null;
		Matcher m = messagePattern.matcher (response);
		if ( m.matches () )
		{
			String stat = m.group (2);
			String al = m.group (3);
			byte[] msg = m.group (4).getBytes ();
			return new PhoneMessage (stat, al, msg);
		}
		return null;
	}
}
