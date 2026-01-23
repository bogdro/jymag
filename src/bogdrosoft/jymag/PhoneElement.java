/*
 * PhoneElement.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import java.util.Locale;

/**
 * This class represents an element in the phone (picture, ringtone, ...).
 * @author Bogdan Drozdowski
 */
public class PhoneElement implements Serializable
{
	private static final long serialVersionUID = 91L;

	private static final String TOSTRING_BEGIN = "PhoneElement[";	// NOI18N
	private static final String TOSTRING_END = "]";			// NOI18N
	private static final String TOSTRING_ID = "ID=";		// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String TOSTRING_NAME = "";			// NOI18N

	private final String id;
	private final String type;
	private final String filename;

	/**
	 * Creates a new instance of PhoneElement.
	 * @param elementId ID number of the element in the phone.
	 * @param format Element type (format), like FGIF, JPEG, MIDI.
	 * @param name Element's name.
	 */
	public PhoneElement (String elementId, String format, String name)
	{
		id = elementId;
		type = format;
		filename = name;
	}

	/**
	 * Returns this element's ID number in the phone.
	 * @return This element's ID number in the phone.
	 */
	public String getID ()
	{
		return id;
	}

	/**
	 * Returns the file extension suitable for this element.
	 * @return A file extension suitable for this element.
	 */
	public String getExt ()
	{
		if ( type == null )
		{
			return Utils.EMPTY_STR;
		}
		if ( "FGIF".equals (type) )
		{
			return "gif";				// NOI18N
		}
		else if ( "JPEG".equals (type) )
		{
			return "jpg";				// NOI18N
		}
		else if ( "MIDI".equals (type) )
		{
			return "mid";				// NOI18N
		}
		else if ( "VCRD".equals (type) )
		{
			return "vcf";				// NOI18N
		}
		else if ( "VCAL".equals (type) )
		{
			return "ics";				// NOI18N
		}
		else if ( "WBMP".equals (type) )
		{
			return "wbm";				// NOI18N
		}
		else if ( "TIFF".equals (type) )
		{
			return "tif";				// NOI18N
		}
		else if ( "PICT".equals (type) )
		{
			return "pct";				// NOI18N
		}
		else if ( "SVGZ".equals (type) )
		{
			return "svz";				// NOI18N
		}
		else if ( "AIFF".equals (type) )
		{
			return "aif";				// NOI18N
		}
		else if ( "MPEG".equals (type) )
		{
			return "mpg";				// NOI18N
		}
		else if ( "EMS_GR".equals (type) )
		{
			return "emg";				// NOI18N
		}
		else if ( "ASG1".equals (type) )
		{
			return "as1";				// NOI18N
		}
		else if ( "ASG2".equals (type) )
		{
			return "as2";				// NOI18N
		}
		else if ( "EMS_AN".equals (type) )
		{
			return "ema";				// NOI18N
		}
		else if ( "MJPG".equals (type) )
		{
			return "mjp";				// NOI18N
		}
		else if ( "3GP2".equals (type) )
		{
			return "3gp";				// NOI18N
		}
		else if ( "3GPP".equals (type) )
		{
			return "3gp";				// NOI18N
		}
		return type.toLowerCase (Locale.ENGLISH);
	}

	/**
	 * Returns the filename with all blanks and unwanted characters
	 * replaced with underscores.
	 * @return The file name for this element.
	 */
	public String getFilename ()
	{
		if ( filename == null )
		{
			return Utils.EMPTY_STR;
		}
		return filename.replaceAll("[^a-zA-Z0-9_-]", "_");	// NOI18N
	}

	/**
	 * Returns a String representation of this PhoneElement.
	 * @return a String representation of this PhoneElement. The syntax is
	 *	PhoneMessage[ID=xxx,name].
	 */
	@Override
	public String toString ()
	{
		return TOSTRING_BEGIN + TOSTRING_ID + id + Utils.COMMA
			+ TOSTRING_NAME	+ filename + Utils.DOT + getExt ()
			+ TOSTRING_END;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 73 * hash + (this.type != null ? this.type.hashCode() : 0);
		hash = 73 * hash + (this.filename != null ? this.filename.hashCode() : 0);
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
		final PhoneElement other = (PhoneElement) obj;
		if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id))
		{
			return false;
		}
		if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type))
		{
			return false;
		}
		return (this.filename == null) ? (other.filename == null) : this.filename.equals(other.filename);
	}
}
