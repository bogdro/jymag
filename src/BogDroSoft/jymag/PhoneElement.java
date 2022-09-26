/*
 * PhoneElement.java, part of the JYMAG package.
 *
 * Copyright (C) 2007 Bogdan Drozdowski, bogdandr (at) op.pl
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

/**
 * This class represents an element in the phone (picture, ringtone, ...).
 * @author Bogdan Drozdowski
 */
public class PhoneElement
{
	private String id;
	private String type;
	private String filename;

	/**
	 * Creates a new instance of PhoneElement, using defaults.
	 * @param ID ID number of the element in the phone.
	 * @param format Element type (format), like FGIF, JPEG, MIDI.
	 * @param name Element's name.
	 */
        public PhoneElement (String ID, String format, String name)
        {
		id = new String (ID);
		type = new String (format);
		filename = new String (name);
        }

	/**
	 * Returns this element's ID number in the phone.
	 * @return This element's ID number in the phone.
	 */
	public String getID ()
	{
		return new String (id);
	}

	/**
	 * Returns the file extension suitable for this element.
	 * @return A file extension suitable for this element.
	 */
	public String getExt ()
	{
		     if ( type.equals ("FGIF") ) return "gif";	// NOI18N
		else if ( type.equals ("JPEG") ) return "jpg";	// NOI18N
		else if ( type.equals ("MIDI") ) return "mid";	// NOI18N
		else if ( type.equals ("VCRD") ) return "vcf";	// NOI18N
		else if ( type.equals ("VCAL") ) return "ics";	// NOI18N
		else return type.toLowerCase ();
	}

	/**
	 * Returns the filename with all blanks replaced with underscores.
	 * @return The file name for this element.
	 */
	public String getFilename ()
	{
		return filename.replaceAll ("\\s", "_");	// NOI18N
	}
}
