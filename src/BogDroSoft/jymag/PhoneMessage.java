/*
 * PhoneMessage.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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
	private String message = null;
	private String recipientNum = null;
	private String datetime = null;
	private String id = null;
	private String status = null;

/*
	+CMGL: <ID>,"<status>","<number>","<DD/MM/YY,HH:MM:SS+ZZ>",<N>
	<body>
	OK
	+CMGL: <ID>,"<status>",
	<body>
	+CMGR: "<status>","<number>","<DD/MM/YY,HH:MM:SS+ZZ>",<N>
	<body>
	OK
	+CMGR: "<status>",
	<body>
*/
	private static final Pattern messageListPattern
		= Pattern.compile ("(\\+CMGL:)?\\s*(\\d+)\\s*,\\s*\"([\\w\\s]*)\"\\s*,"		// NOI18N
			+ "\\s*\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+[\r\n]+([\\w\\s]+)[\r\n]",					// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern messageListPatternNotFull
		= Pattern.compile ("(\\+CMGL:)?\\s*(\\d+)\\s*,\\s*\"([\\w\\s]*)\"\\s*,"		// NOI18N
			+ "\\s*(\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+)?[\r\n]+([\\w\\s]+)[\r\n]",				// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern messagePatternCMGR
		= Pattern.compile ("(\\+CMGR:)?\\s*\"([\\w\\s]*)\"\\s*,"			// NOI18N
			+ "\\s*\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+[\r\n]+([\\w\\s]+)[\r\n]",					// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern messagePatternCMGRNotFull
		= Pattern.compile ("(\\+CMGR:)?\\s*\"([\\w\\s]*)\"\\s*,"			// NOI18N
			+ "\\s*(\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+)?[\r\n]+([\\w\\s]+)[\r\n]",				// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final String esc = "\033" /* ESC */;					// NOI18N
	private static final String comma = ",";						// NOI18N
	private static final String toStringStart = "PhoneMessage[";				// NOI18N
	private static final String toStringEnd = "]";						// NOI18N
	private static final String toStringID = "ID=";						// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String toStringStatus = "";					// NOI18N
	private static final String toStringNumber = "";					// NOI18N
	private static final String toStringDateTime = "";					// NOI18N
	private static final String toStringBody = "";						// NOI18N

	/**
	 * Creates a new PhoneMessage.
	 */
	public PhoneMessage ()
	{
	}

	/* ==================================================== */

	/**
	 * Returns the body of the message.
	 * @return the body of the message.
	 */
	public synchronized String getMessage ()
	{
		return message;
	}

	/**
	 * Returns the phone number of the recipient of the message.
	 * @return the recipient's phone number.
	 */
	public synchronized String getRecipientNum ()
	{
		return recipientNum;
	}

	/**
	 * Returns the ID of the message.
	 * @return the ID of the message.
	 */
	public synchronized String getID ()
	{
		return id;
	}

	/**
	 * Returns the date/time of the message.
	 * @return the date/time of the message.
	 */
	public synchronized String getDateTime ()
	{
		return datetime;
	}

	/**
	 * Returns the status of the message.
	 * @return the status of the message.
	 */
	public synchronized String getStatus ()
	{
		return status;
	}

	/* ==================================================== */

	/**
	 * Sets the body of the message.
	 * @param newMsg the new body of the message.
	 */
	public synchronized void setMessage (String newMsg)
	{
		message = newMsg;
	}

	/**
	 * Sets the phone number of the recipient of the message.
	 * @param newRecip the new recipient's phone number.
	 */
	public synchronized void setRecipientNum (String newRecip)
	{
		recipientNum = newRecip;
	}

	/**
	 * Sets the ID of the message.
	 * @param newID the new ID of the message.
	 */
	public synchronized void setID (String newID)
	{
		id = newID;
	}

	/**
	 * Sets the date/time of the message.
	 * @param newDateTime the new date/time of the message.
	 */
	public synchronized void setDateTime (String newDateTime)
	{
		datetime = newDateTime;
	}

	/**
	 * Sets the status of the message.
	 * @param newStatus the new status of the message.
	 */
	public synchronized void setStatus (String newStatus)
	{
		status = newStatus;
	}

	/* ==================================================== */

	/**
	 * Gets the message string for this message.
	 * @return the message string for this message, suitable for commands sent
	 *	to the phone.
	 */
	public synchronized String getMessageString ()
	{
		return message + esc;
	}

	/**
	 * Parses the given phone response and creates a PhoneMessage that matches it.
	 * @param response The response to parse.
	 * @return a PhoneMessage that matches the given response.
	 */
	public synchronized static PhoneMessage parseReponse (String response)
	{
		if ( response == null )
		{
			return null;
		}

		Matcher m = messageListPattern.matcher (response);
		if ( m.matches () )
		{
			PhoneMessage msg = new PhoneMessage();
			msg.setID (m.group (2));
			msg.setStatus (m.group (3));
			msg.setRecipientNum (m.group (4));
			msg.setDateTime (m.group (5));
			msg.setMessage (m.group (6));
			return msg;
		}
		m = messageListPatternNotFull.matcher (response);
		if ( m.matches () )
		{
			PhoneMessage msg = new PhoneMessage();
			msg.setID (m.group (2));
			msg.setStatus (m.group (3));
			msg.setRecipientNum (m.group (5));
			msg.setDateTime (m.group (6));
			msg.setMessage (m.group (7));
			return msg;
		}
		m = messagePatternCMGR.matcher (response);
		if ( m.matches () )
		{
			PhoneMessage msg = new PhoneMessage();
			msg.setStatus (m.group (2));
			msg.setRecipientNum (m.group (3));
			msg.setDateTime (m.group (4));
			msg.setMessage (m.group (5));
			return msg;
		}
		m = messagePatternCMGRNotFull.matcher (response);
		if ( m.matches () )
		{
			PhoneMessage msg = new PhoneMessage();
			msg.setStatus (m.group (2));
			msg.setRecipientNum (m.group (4));
			msg.setDateTime (m.group (5));
			msg.setMessage (m.group (6));
			return msg;
		}

		return null;
	}

	/**
	 * Returns a String representation of this PhoneMessage.
	 * @return a String representation of this PhoneMessage. The syntax is
	 *	PhoneMessage[ID=xxx,status,number,datetime,body].
	 */
	@Override
	public synchronized String toString ()
	{
		return toStringStart + toStringID + id + comma + toStringStatus + status + comma
			+ toStringNumber + recipientNum + comma + toStringDateTime + datetime + comma
			+ toStringBody + message + toStringEnd;
	}
}
