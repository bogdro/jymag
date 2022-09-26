/*
 * PhoneElement.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2022 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package BogDroSoft.jymag;

import java.io.Serializable;
import java.util.Locale;

/**
 * This class represents an element in the phone (picture, ringtone, ...).
 * @author Bogdan Drozdowski
 */
public class PhoneElement implements Serializable
{
	private static final long serialVersionUID = 91L;

	private final String id;
	private final String type;
	private final String filename;

	private static final String TOSTRING_BEGIN = "PhoneElement[";	// NOI18N
	private static final String TOSTRING_END = "]";			// NOI18N
	private static final String TOSTRING_ID = "ID=";		// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String TOSTRING_NAME = "";			// NOI18N

	/**
	 * Creates a new instance of PhoneElement.
	 * @param ID ID number of the element in the phone.
	 * @param format Element type (format), like FGIF, JPEG, MIDI.
	 * @param name Element's name.
	 */
	public PhoneElement (String ID, String format, String name)
	{
		id = ID;
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
		if ( type.equals ("FGIF") )
		{
			return "gif";				// NOI18N
		}
		else if ( type.equals ("JPEG") )		// NOI18N
		{
			return "jpg";				// NOI18N
		}
		else if ( type.equals ("MIDI") )		// NOI18N
		{
			return "mid";				// NOI18N
		}
		else if ( type.equals ("VCRD") )		// NOI18N
		{
			return "vcf";				// NOI18N
		}
		else if ( type.equals ("VCAL") )		// NOI18N
		{
			return "ics";				// NOI18N
		}
		else if ( type.equals ("WBMP") )		// NOI18N
		{
			return "wbm";				// NOI18N
		}
		else if ( type.equals ("TIFF") )		// NOI18N
		{
			return "tif";				// NOI18N
		}
		else if ( type.equals ("PICT") )		// NOI18N
		{
			return "pct";				// NOI18N
		}
		else if ( type.equals ("SVGZ") )		// NOI18N
		{
			return "svz";				// NOI18N
		}
		else if ( type.equals ("AIFF") )		// NOI18N
		{
			return "aif";				// NOI18N
		}
		else if ( type.equals ("MPEG") )		// NOI18N
		{
			return "mpg";				// NOI18N
		}
		else if ( type.equals ("EMS_GR") )		// NOI18N
		{
			return "emg";				// NOI18N
		}
		else if ( type.equals ("ASG1") )		// NOI18N
		{
			return "as1";				// NOI18N
		}
		else if ( type.equals ("ASG2") )		// NOI18N
		{
			return "as2";				// NOI18N
		}
		else if ( type.equals ("EMS_AN") )		// NOI18N
		{
			return "ema";				// NOI18N
		}
		else if ( type.equals ("MJPG") )		// NOI18N
		{
			return "mjp";				// NOI18N
		}
		else if ( type.equals ("3GP2") )		// NOI18N
		{
			return "3gp";				// NOI18N
		}
		else if ( type.equals ("3GPP") )		// NOI18N
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
		return filename
			.replaceAll ("\\s", "_")		// NOI18N
			.replaceAll ("\\.", "_")		// NOI18N
			.replaceAll (",", "_")			// NOI18N
			.replaceAll ("\\(", "_")		// NOI18N
			.replaceAll ("\\)", "_")		// NOI18N
			.replaceAll ("\\[", "_")		// NOI18N
			.replaceAll ("\\]", "_")		// NOI18N
			.replaceAll ("\\{", "_")		// NOI18N
			.replaceAll ("\\}", "_")		// NOI18N
			.replaceAll ("~", "_")			// NOI18N
			.replaceAll ("!", "_")			// NOI18N
			.replaceAll ("%", "_")			// NOI18N
			.replaceAll ("\\^", "_")		// NOI18N
			.replaceAll ("&", "_")			// NOI18N
			.replaceAll ("\\*", "_")		// NOI18N
			.replaceAll ("\\+", "_")		// NOI18N
			.replaceAll ("=", "_")			// NOI18N
			.replaceAll ("\\|", "_")		// NOI18N
			.replaceAll ("\\\\", "_")		// NOI18N
			.replaceAll (":", "_")			// NOI18N
			.replaceAll (";", "_")			// NOI18N
			.replaceAll ("\"", "_")			// NOI18N
			.replaceAll ("'", "_")			// NOI18N
			.replaceAll ("<", "_")			// NOI18N
			.replaceAll (">", "_")			// NOI18N
			.replaceAll ("\\?", "_")		// NOI18N
			.replaceAll ("/", "_")			// NOI18N
			;
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
}
