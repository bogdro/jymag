/*
 * PhoneElement.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2020 Bogdan Drozdowski, bogdandr (at) op.pl
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

import java.util.Locale;

/**
 * This class represents an element in the phone (picture, ringtone, ...).
 * @author Bogdan Drozdowski
 */
public class PhoneElement
{
	private String id;
	private String type;
	private String filename;

	private static final String comma = ",";						// NOI18N
	private static final String dot = ".";							// NOI18N
	private static final String toStringStart = "PhoneElement[";				// NOI18N
	private static final String toStringEnd = "]";						// NOI18N
	private static final String toStringID = "ID=";						// NOI18N
	// the rest can be empty. The user needs to know only where the ID is
	private static final String toStringName = "";						// NOI18N
	private static final String empty = "";						// NOI18N

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
			return empty;
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
			return empty;
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
		return toStringStart + toStringID + id + comma + toStringName
			+ filename + dot + getExt () + toStringEnd;
	}
}
