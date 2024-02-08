/*
 * PhoneMessage.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a message in the phone.
 * @author Bogdan Drozdowski
 */
public class PhoneMessage implements Serializable
{
	private static final long serialVersionUID = 92L;

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
	private static final Pattern MESSAGE_LIST_PATTERN
		= Pattern.compile ("(\\+CMGL:)?\\s*(\\d+)\\s*,\\s*\"([\\w\\s]*)\"\\s*,"		// NOI18N
			+ "\\s*\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+[\r\n]+([\\w\\s]+)[\r\n]",					// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern MESSAGE_LIST_PATTERN_NOT_FULL
		= Pattern.compile ("(\\+CMGL:)?\\s*(\\d+)\\s*,\\s*\"([\\w\\s]*)\"\\s*,"		// NOI18N
			+ "\\s*(\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+)?[\r\n]+([\\w\\s]+)[\r\n]",				// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern MESSAGE_PATTERN_CMGR
		= Pattern.compile ("(\\+CMGR:)?\\s*\"([\\w\\s]*)\"\\s*,"			// NOI18N
			+ "\\s*\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+[\r\n]+([\\w\\s]+)[\r\n]",					// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final Pattern MESSAGE_PATTERN_CMGR_NOT_FULL
		= Pattern.compile ("(\\+CMGR:)?\\s*\"([\\w\\s]*)\"\\s*,"			// NOI18N
			+ "\\s*(\"(\\+?\\d+)\"\\s*,"						// NOI18N
			+ "\\s*\"(\\d{2}/\\d{2}/\\d{2},\\d{2}:\\d{2}:\\d{2}\\+\\d{2})\"\\s*,"	// NOI18N
			+ "\\s*,\\d+)?[\r\n]+([\\w\\s]+)[\r\n]",				// NOI18N
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	private static final String ESC = "\033" /* ESC */;					// NOI18N
	private static final String TOSTRING_BEGIN = "PhoneMessage[";				// NOI18N
	private static final String TOSTRING_END = "]";						// NOI18N
	private static final String TOSTRING_ID = "ID=";						// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String TOSTRING_STATUS = "";					// NOI18N
	private static final String TOSTRING_NUMBER = "";					// NOI18N
	private static final String TOSTRING_DATETIME = "";					// NOI18N
	private static final String TOSTRING_BODY = "";						// NOI18N

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
		return message + ESC;
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

		Matcher m = MESSAGE_LIST_PATTERN.matcher (response);
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
		m = MESSAGE_LIST_PATTERN_NOT_FULL.matcher (response);
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
		m = MESSAGE_PATTERN_CMGR.matcher (response);
		if ( m.matches () )
		{
			PhoneMessage msg = new PhoneMessage();
			msg.setStatus (m.group (2));
			msg.setRecipientNum (m.group (3));
			msg.setDateTime (m.group (4));
			msg.setMessage (m.group (5));
			return msg;
		}
		m = MESSAGE_PATTERN_CMGR_NOT_FULL.matcher (response);
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

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 83 * hash + (this.message != null ? this.message.hashCode() : 0);
		hash = 83 * hash + (this.recipientNum != null ? this.recipientNum.hashCode() : 0);
		hash = 83 * hash + (this.datetime != null ? this.datetime.hashCode() : 0);
		hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 83 * hash + (this.status != null ? this.status.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final PhoneMessage other = (PhoneMessage) obj;
		if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message))
		{
			return false;
		}
		if ((this.recipientNum == null) ? (other.recipientNum != null) : !this.recipientNum.equals(other.recipientNum))
		{
			return false;
		}
		if ((this.datetime == null) ? (other.datetime != null) : !this.datetime.equals(other.datetime))
		{
			return false;
		}
		if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id))
		{
			return false;
		}
		return (this.status == null) ? (other.status == null) : this.status.equals(other.status);
	}

	/**
	 * Returns a String representation of this PhoneMessage.
	 * @return a String representation of this PhoneMessage. The syntax is
	 *	PhoneMessage[ID=xxx,status,number,datetime,body].
	 */
	@Override
	public synchronized String toString ()
	{
		return TOSTRING_BEGIN + TOSTRING_ID + id + Utils.COMMA
			+ TOSTRING_STATUS + status + Utils.COMMA
			+ TOSTRING_NUMBER + recipientNum + Utils.COMMA
			+ TOSTRING_DATETIME + datetime + Utils.COMMA
			+ TOSTRING_BODY + message + TOSTRING_END;
	}
}
