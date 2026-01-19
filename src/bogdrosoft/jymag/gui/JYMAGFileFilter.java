/*
 * JYMAGFileFilter.java, part of the JYMAG package.
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

package bogdrosoft.jymag.gui;

import bogdrosoft.jymag.Utils;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.swing.filechooser.FileFilter;

/**
 * A FileFilter implementation for UI file chooser windows.
 * @author Bogdan Drozdowski
 */
public class JYMAGFileFilter extends FileFilter {

	private static final String ALL_FILE_NAMES = "*.";	// NOI18N

	private final String filterDescription;
	private final Map<String, Integer> filterFiletypes;

	/**
	 * Creates a JYMAGFileFilter instance.
	 * @param description the description of the filter.
	 * @param filetype the types of the files to be accepted by the filter.
	 */
	public JYMAGFileFilter (String description, Map<String, Integer> filetype)
	{
		StringBuilder desc = new StringBuilder (200);
		if ( description != null )
		{
			desc.append (description);
		}
		if ( filetype != null )
		{
			Iterator<String> keys = filetype.keySet ().iterator ();
			if ( keys != null )
			{
				desc.append (Utils.SPACE).append (Utils.L_PAREN);
				while ( keys.hasNext () )
				{
					desc.append (ALL_FILE_NAMES);
					desc.append (keys.next ());
					if ( keys.hasNext () )
					{
						desc.append (Utils.COMMA)
							.append (Utils.SPACE);
					}
				}
				desc.append (Utils.R_PAREN);
			}
		}
		filterDescription = desc.toString ();
		if ( filetype != null )
		{
			filterFiletypes = Collections.unmodifiableMap(filetype);
		}
		else
		{
			filterFiletypes = null;
		}
	}

	@Override
	public boolean accept ( File f )
	{
		if ( f.isDirectory () )
		{
			return true;
		}
		String name = f.getName ();
		if ( name == null )
		{
			return false;
		}
		int dotIndex = name.lastIndexOf('.');
		if ( dotIndex >= 0 && dotIndex+1 < name.length()
			&& filterFiletypes != null )
		{
			if ( filterFiletypes.containsKey (
				name.substring (dotIndex+1)
				.toLowerCase (Locale.ENGLISH)))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription ()
	{
		return filterDescription;
	}

	@Override
	public String toString ()
	{
		return "JYMAGFileFilter(" + filterDescription + ")";	// NOI18N
	}
}
