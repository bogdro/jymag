/*
 * ConfigFile.java, part of the JYMAG package.
 *
 * Copyright (C) 2008-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for reading and writing JYMAG configuration files.
 * @author Bogdan Drozdowski
 */
public class ConfigFile
{
	private final File cfgFile;

	// communication parameters:
	private String port;
	private int speed;
	private int dBits;
	private int parity;
	private int sBits;
	private int flowCtl;
	// main window's parameters:
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean isMax;
	private int fontSize;
	private int selectedTab;

	// patterns for matching:
	private static final Pattern PORT_PATTERN = Pattern.compile
			("port\\s*=\\s*([^\\s]+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern SPEED_PATTERN = Pattern.compile
			("speed\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern DATA_BITS_PATTERN = Pattern.compile
			("databits\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern PARITY_PATTERN = Pattern.compile
			("parity\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern STOP_BITS_PATTERN = Pattern.compile
			("stopbits\\s*=\\s*([\\d\\.]+)", Pattern.CASE_INSENSITIVE);	// NOI18N
	private static final Pattern FLOW_CONTROL_PATTERN = Pattern.compile
			("flowcontrol\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);	// NOI18N
	private static final Pattern X_COORD_PATTERN = Pattern.compile
			("x\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);			// NOI18N
	private static final Pattern Y_COORD_PATTERN = Pattern.compile
			("y\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);			// NOI18N
	private static final Pattern WIDTH_PATTERN = Pattern.compile
			("width\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern HEIGHT_PATTERN = Pattern.compile
			("height\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern IS_MAXIMIZED_PATTERN = Pattern.compile
			("ismax\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern FONT_SIZE_PATTERN = Pattern.compile
			("font_size\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern SELECTED_TAB_PATTERN = Pattern.compile
			("tab\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern COMMENT_PATTERN = Pattern.compile
			("^#.*");		// NOI18N

	/**
	 * Creates a new instance of ConfigFile.
	 * @param f The file to read/write parameters from/to.
	 */
	public ConfigFile (File f)
	{
		if ( f == null )
		{
			throw new IllegalArgumentException ("ConfigFile:f==null");	// NOI18N
		}
		cfgFile = f;
	}

	/**
	 * Read the parameters from the given file and stores them inside private
	 * fields.
	 * @throws Exception on file error.
	 */
	public void read () throws Exception
	{
		port = Utils.EMPTY_STR;
		speed = 115200;
		dBits = 8;
		parity = 0;
		sBits = 1;
		flowCtl = 0;
		x = 0;
		y = 0;
		width = 800;
		height = 600;
		isMax = true;
		fontSize = 12;
		selectedTab = 0;

		BufferedReader br = null;
		try
		{
			// don't force any encodings, because the file may be in a different encoding
			br = new BufferedReader (new FileReader (cfgFile));
			String line;
			do
			{
				try
				{
					line = br.readLine ();
				}
				catch (IOException ex)
				{
					break;
				}
				if ( line == null )
				{
					break;
				}
				if ( line.isEmpty () )
				{
					continue;
				}
				Matcher portM = PORT_PATTERN.matcher (line);
				Matcher speedM = SPEED_PATTERN.matcher (line);
				Matcher dBitsM = DATA_BITS_PATTERN.matcher (line);
				Matcher parityM = PARITY_PATTERN.matcher (line);
				Matcher sBitsM = STOP_BITS_PATTERN.matcher (line);
				Matcher flowCtlM = FLOW_CONTROL_PATTERN.matcher (line);
				Matcher xM = X_COORD_PATTERN.matcher (line);
				Matcher yM = Y_COORD_PATTERN.matcher (line);
				Matcher widthM = WIDTH_PATTERN.matcher (line);
				Matcher heightM = HEIGHT_PATTERN.matcher (line);
				Matcher isMaxM = IS_MAXIMIZED_PATTERN.matcher (line);
				Matcher fontSizeM = FONT_SIZE_PATTERN.matcher (line);
				Matcher selectedTabM = SELECTED_TAB_PATTERN.matcher (line);
				Matcher commentM = COMMENT_PATTERN.matcher (line);

				if ( commentM.matches () )
				{
					continue;
				}
				if ( portM.matches () )
				{
					try
					{
						port = portM.group (1);
					}
					catch (Exception ex)
					{
						Utils.handleException (ex, "ConfigFile.read.group");	// NOI18N
					}
				}
				else if ( speedM.matches () )
				{
					speed = readIntUsingMatcher (speedM, 1, "speed");	// NOI18N
				}
				else if ( dBitsM.matches () )
				{
					dBits = readIntUsingMatcher (dBitsM, 1, "data bits");	// NOI18N
				}
				else if ( parityM.matches () )
				{
					parity = readIntUsingMatcher (parityM, 1, "parity");	// NOI18N
				}
				else if ( sBitsM.matches () )
				{
					sBits = readIntUsingMatcher (sBitsM, 1, "stop bits");	// NOI18N
				}
				else if ( flowCtlM.matches () )
				{
					flowCtl = readIntUsingMatcher (flowCtlM, 1, "flow control");	// NOI18N
				}
				else if ( xM.matches () )
				{
					x = readIntUsingMatcher (xM, 1, "x");	// NOI18N
				}
				else if ( yM.matches () )
				{
					y = readIntUsingMatcher (yM, 1, "y");	// NOI18N
				}
				else if ( widthM.matches () )
				{
					width = readIntUsingMatcher (widthM, 1, "width");	// NOI18N
				}
				else if ( heightM.matches () )
				{
					height = readIntUsingMatcher (heightM, 1, "height");	// NOI18N
				}
				else if ( isMaxM.matches () )
				{
					isMax = readIntUsingMatcher (isMaxM, 1, "maximized") != 0;	// NOI18N
				}
				else if ( fontSizeM.matches () )
				{
					fontSize = readIntUsingMatcher (fontSizeM, 1, "font_size");	// NOI18N
				}
				else if ( selectedTabM.matches () )
				{
					selectedTab = readIntUsingMatcher (selectedTabM, 1, "tab");	// NOI18N
				}
			} while (true);
		}
		finally
		{
			if (br != null)
			{
				br.close ();
			}
		}

		// verify here
		if ( ! Utils.isAllowableSpeed (speed) )
		{
			speed = 115200;
		}
		if ( ! Utils.isAllowableDataBits (dBits) )
		{
			dBits = 8;
		}
		if ( parity < 0 || parity > 4 )
		{
			parity = 0;
		}
		if ( sBits < 0 || sBits > 2 )
		{
			sBits = 0;
		}
		if ( flowCtl < 0 || flowCtl > 3 )
		{
			flowCtl = 0;
		}
		if ( x < 0 )
		{
			x = 0;
		}
		if ( y < 0 )
		{
			y = 0;
		}
		if ( width < 0 )
		{
			width = 0;
		}
		if ( height < 0 )
		{
			height = 0;
		}
		if ( fontSize < 0 )
		{
			fontSize = 12;
		}
		if ( selectedTab < 0 )
		{
			selectedTab = 0;
		}
	}

	/**
	 * Writes the parameters to the given file
	 * @throws Exception on file error.
	 */
	public void write () throws Exception
	{
		// don't force any encodings, because the filesystems' names may be in a different encoding
		BufferedWriter w = null;
		try
		{
			w = new BufferedWriter(new PrintWriter (cfgFile));
			w.write ("port = " + port);	// NOI18N
			w.newLine ();
			w.write ("speed = " + speed);	// NOI18N
			w.newLine ();
			w.write ("databits = " + dBits);	// NOI18N
			w.newLine ();
			w.write ("# Parity: 0=none, 1=even, 2=odd, 3=space, 4=mark.");	// NOI18N
			w.newLine ();
			w.write ("parity = " + parity);	// NOI18N
			w.newLine ();
			w.write ("# Stop bits: 0=1 bit, 1=1.5 bits, 2=2 bits.");	// NOI18N
			w.newLine ();
			w.write ("stopbits = " + sBits);	// NOI18N
			w.newLine ();
			w.write ("# Flow control: 0=none, 1=software (XON/XOFF), 2=hardware (RTS/CTS), 3=software+hardware.");	// NOI18N
			w.newLine ();
			w.write ("flowcontrol = " + flowCtl);	// NOI18N
			w.newLine ();
			w.write ("x = " + x);	// NOI18N
			w.newLine ();
			w.write ("y = " + y);	// NOI18N
			w.newLine ();
			w.write ("width = " + width);	// NOI18N
			w.newLine ();
			w.write ("height = " + height);	// NOI18N
			w.newLine ();
			w.write ("ismax = " + (isMax? 1 : 0));	// NOI18N
			w.newLine ();
			w.write ("font_size = " + fontSize);	// NOI18N
			w.newLine ();
			w.write ("tab = " + selectedTab);	// NOI18N
			w.newLine ();
			w.close ();
		}
		catch (IOException t)
		{
			Utils.handleException(t, "ConfigFile.write");
		}
		finally
		{
			if (w != null)
			{
				try
				{
					w.close();
				}
				catch (IOException t2)
				{
					Utils.handleException(t2, "ConfigFile.write->exception");
				}
			}
		}
	}

	// ================ setters:

	/**
	 * Sets the port variable.
	 * @param v the new value.
	 */
	public void setPort (String v)
	{
		port = v;
	}

	/**
	 * Sets the speed variable.
	 * @param v the new value.
	 */
	public void setSpeed (int v)
	{
		speed = v;
	}

	/**
	 * Sets the data bits' variable.
	 * @param v the new value.
	 */
	public void setDataBits (int v)
	{
		dBits = v;
	}

	/**
	 * Sets the parity variable.
	 * @param v the new value.
	 */
	public void setParity (int v)
	{
		parity = v;
	}

	/**
	 * Sets the stop bits' variable.
	 * @param v the new value.
	 */
	public void setStopBits (int v)
	{
		sBits = v;
	}

	/**
	 * Sets the flow control variable.
	 * @param v the new value.
	 */
	public void setFlow (int v)
	{
		flowCtl = v;
	}

	/**
	 * Sets the X variable.
	 * @param v the new value.
	 */
	public void setX (int v)
	{
		x = v;
	}

	/**
	 * Sets the Y variable.
	 * @param v the new value.
	 */
	public void setY (int v)
	{
		y = v;
	}

	/**
	 * Sets the width variable.
	 * @param v the new value.
	 */
	public void setWidth (int v)
	{
		width = v;
	}

	/**
	 * Sets the height variable.
	 * @param v the new value.
	 */
	public void setHeight (int v)
	{
		height = v;
	}

	/**
	 * Sets the isMaximized variable.
	 * @param v the new value.
	 */
	public void setIsMaximized (boolean v)
	{
		isMax = v;
	}

	/**
	 * Sets the font size variable.
	 * @param v the new value.
	 */
	public void setFontSizeValue (int v)
	{
		fontSize = v;
	}

	/**
	 * Sets the selected tab.
	 * @param v the new value.
	 */
	public void setSelectedTab (int v)
	{
		selectedTab = v;
	}

	// ================ getters:


	/**
	 * Gets the port variable.
	 * @return the variable's value.
	 */
	public String getPort ()
	{
		return port;
	}

	/**
	 * Gets the speed variable.
	 * @return the variable's value.
	 */
	public int getSpeed ()
	{
		return speed;
	}

	/**
	 * Gets the data bits' variable.
	 * @return the variable's value.
	 */
	public int getDBits ()
	{
		return dBits;
	}

	/**
	 * Gets the parity variable.
	 * @return the variable's value.
	 */
	public int getParity ()
	{
		return parity;
	}

	/**
	 * Gets the stop bits' variable.
	 * @return the variable's value.
	 */
	public int getSBits ()
	{
		return sBits;
	}

	/**
	 * Gets the flow control variable.
	 * @return the variable's value.
	 */
	public int getFlowCtl ()
	{
		return flowCtl;
	}

	/**
	 * Gets the X variable.
	 * @return the variable's value.
	 */
	public int getX ()
	{
		return x;
	}

	/**
	 * Gets the Y variable.
	 * @return the variable's value.
	 */
	public int getY ()
	{
		return y;
	}

	/**
	 * Gets the width variable.
	 * @return the variable's value.
	 */
	public int getWidth ()
	{
		return width;
	}

	/**
	 * Gets the height variable.
	 * @return the variable's value.
	 */
	public int getHeight ()
	{
		return height;
	}

	/**
	 * Gets the isMax variable.
	 * @return the variable's value.
	 */
	public boolean getIsMax ()
	{
		return isMax;
	}

	/**
	 * Gets the font size variable.
	 * @return the variable's value.
	 */
	public int getFontSizeValue ()
	{
		return fontSize;
	}

	/**
	 * Gets the selected tab.
	 * @return the selected tab's index.
	 */
	public int getSelectedTab ()
	{
		return selectedTab;
	}

	private int readIntUsingMatcher(Matcher m, int group, String field)
	{
		try
		{
			return Integer.parseInt (m.group (group));
		}
		catch (NumberFormatException ex)
		{
			Utils.handleException (ex, "ConfigFile.read.parseInt (" + field + ")");	// NOI18N
			return -1;
		}
	}
}
