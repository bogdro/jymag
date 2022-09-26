/*
 * ConfigFile.java, part of the JYMAG package.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
	private static final String emptyString = "";		// NOI18N

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
	private static final Pattern portPat = Pattern.compile
			("port\\s*=\\s*([^\\s]+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern speedPat = Pattern.compile
			("speed\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern dBitsPat = Pattern.compile
			("databits\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern parityPat = Pattern.compile
			("parity\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern sBitsPat = Pattern.compile
			("stopbits\\s*=\\s*([\\d\\.]+)", Pattern.CASE_INSENSITIVE);	// NOI18N
	private static final Pattern flowCtlPat = Pattern.compile
			("flowcontrol\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);	// NOI18N
	private static final Pattern xPat = Pattern.compile
			("x\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);			// NOI18N
	private static final Pattern yPat = Pattern.compile
			("y\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);			// NOI18N
	private static final Pattern widthPat = Pattern.compile
			("width\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern heightPat = Pattern.compile
			("height\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern isMaxPat = Pattern.compile
			("ismax\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern fontSizePat = Pattern.compile
			("font_size\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern selectedTabPat = Pattern.compile
			("tab\\s*=\\s*(\\d+)", Pattern.CASE_INSENSITIVE);		// NOI18N
	private static final Pattern commentPat = Pattern.compile
			("^#.*");		// NOI18N

	private Matcher portM;
	private Matcher speedM;
	private Matcher dBitsM;
	private Matcher parityM;
	private Matcher sBitsM;
	private Matcher flowCtlM;
	private Matcher xM;
	private Matcher yM;
	private Matcher widthM;
	private Matcher heightM;
	private Matcher isMaxM;
	private Matcher fontSizeM;
	private Matcher selectedTabM;
	private Matcher commentM;

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
		port = emptyString;
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

		// don't force any encodings, because the file may be in a different encoding
		BufferedReader br = new BufferedReader (new FileReader (cfgFile));
		String line;
		do
		{
			line = null;
			try
			{
				line = br.readLine ();
			}
			catch (Exception ex)
			{
				line = null;
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
			portM = portPat.matcher (line);
			speedM = speedPat.matcher (line);
			dBitsM = dBitsPat.matcher (line);
			parityM = parityPat.matcher (line);
			sBitsM = sBitsPat.matcher (line);
			flowCtlM = flowCtlPat.matcher (line);
			xM = xPat.matcher (line);
			yM = yPat.matcher (line);
			widthM = widthPat.matcher (line);
			heightM = heightPat.matcher (line);
			isMaxM = isMaxPat.matcher (line);
			fontSizeM = fontSizePat.matcher (line);
			selectedTabM = selectedTabPat.matcher (line);
			commentM = commentPat.matcher (line);

			if ( commentM.matches () )
			{
				continue;
			}
			else if ( portM.matches () )
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
				try
				{
					speed = Integer.parseInt (speedM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (speed)");	// NOI18N
				}
			}
			else if ( dBitsM.matches () )
			{
				try
				{
					dBits = Integer.parseInt (dBitsM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (data bits)");	// NOI18N
				}
			}
			else if ( parityM.matches () )
			{
				try
				{
					parity = Integer.parseInt (parityM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (parity)");	// NOI18N
				}
			}
			else if ( sBitsM.matches () )
			{
				try
				{
					sBits = Integer.parseInt (sBitsM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (stop bits)");	// NOI18N
				}
			}
			else if ( flowCtlM.matches () )
			{
				try
				{
					flowCtl = Integer.parseInt (flowCtlM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (flow control)");	// NOI18N
				}
			}
			else if ( xM.matches () )
			{
				try
				{
					x = Integer.parseInt (xM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (x)");	// NOI18N
				}
			}
			else if ( yM.matches () )
			{
				try
				{
					y = Integer.parseInt (yM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (y)");	// NOI18N
				}
			}
			else if ( widthM.matches () )
			{
				try
				{
					width = Integer.parseInt (widthM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (width)");	// NOI18N
				}
			}
			else if ( heightM.matches () )
			{
				try
				{
					height = Integer.parseInt (heightM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (height)");	// NOI18N
				}
			}
			else if ( isMaxM.matches () )
			{
				try
				{
					isMax = Integer.parseInt (isMaxM.group (1)) != 0;
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (maximized)");	// NOI18N
				}
			}
			else if ( fontSizeM.matches () )
			{
				try
				{
					fontSize = Integer.parseInt (fontSizeM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (font_size)");	// NOI18N
				}
			}
			else if ( selectedTabM.matches () )
			{
				try
				{
					selectedTab = Integer.parseInt (selectedTabM.group (1));
				}
				catch (Exception ex)
				{
					Utils.handleException (ex, "ConfigFile.read.parseInt (tab)");	// NOI18N
				}
			}
		} while (true);
		br.close ();

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
		BufferedWriter w = new BufferedWriter(new PrintWriter (cfgFile));
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
		w.write ("ismax = " + ((isMax)? 1 : 0));	// NOI18N
		w.newLine ();
		w.write ("font_size = " + fontSize);	// NOI18N
		w.newLine ();
		w.write ("tab = " + selectedTab);	// NOI18N
		w.newLine ();
		w.close ();
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
}
