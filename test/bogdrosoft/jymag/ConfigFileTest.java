/*
 * ConfigFileTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * ConfigFileTest - a test for the ConfigFile class.
 * @author Bogdan Drozdowski
 */
public class ConfigFileTest
{
	private static File f;

	@Before
	public void setUp () throws Exception
	{
		f = File.createTempFile ("jym", null);
	}

	/**
	 * Test of read method, of class ConfigFile.
	 *
	 * @throws Exception
	 */
	@Test
	public void testRead () throws Exception
	{
		System.out.println ("read");
		ConfigFile instance = new ConfigFile (f);
		instance.setPort ("testPort");
		instance.setSpeed (1234);
		instance.setDataBits (2345);
		instance.setParity (3456);
		instance.setStopBits (4567);
		instance.setFlow (5678);
		instance.setX (6789);
		instance.setY (7890);
		instance.setWidth (8901);
		instance.setHeight (9012);
		instance.setIsMaximized (false);
		instance.setFontSizeValue (123);
		instance.setSelectedTab (2);
		instance.write ();
		instance = new ConfigFile (f);
		instance.read ();
		// some values below are the defaults
		assertEquals (instance.getPort (), "testPort");
		assertEquals (instance.getSpeed (), 115200);
		assertEquals (instance.getDBits (), 8);
		assertEquals (instance.getParity (), 0);
		assertEquals (instance.getSBits (), 0);
		assertEquals (instance.getFlowCtl (), 0);
		assertEquals (instance.getX (), 6789);
		assertEquals (instance.getY (), 7890);
		assertEquals (instance.getWidth (), 8901);
		assertEquals (instance.getHeight (), 9012);
		assertEquals (instance.getIsMax (), false);
		assertEquals (instance.getFontSizeValue (), 123);
		assertEquals (instance.getSelectedTab (), 2);
	}

	/**
	 * Test of write method, of class ConfigFile.
	 *
	 * @throws Exception
	 */
	@Test
	public void testWrite () throws Exception
	{
		System.out.println ("write");
		ConfigFile instance = new ConfigFile (f);
		instance.write ();
		assertTrue(f.length() > 0);
	}

	/**
	 * Test of setPort method, of class ConfigFile.
	 */
	@Test
	public void testSetPort ()
	{
		System.out.println ("setPort");
		String v = "port";
		ConfigFile instance = new ConfigFile (f);
		instance.setPort (v);
		assertEquals (v, instance.getPort ());
	}

	/**
	 * Test of setSpeed method, of class ConfigFile.
	 */
	@Test
	public void testSetSpeed ()
	{
		System.out.println ("setSpeed");
		int v = 1234;
		ConfigFile instance = new ConfigFile (f);
		instance.setSpeed (v);
		assertEquals (v, instance.getSpeed ());
	}

	/**
	 * Test of setDataBits method, of class ConfigFile.
	 */
	@Test
	public void testSetDataBits ()
	{
		System.out.println ("setDataBits");
		int v = 2345;
		ConfigFile instance = new ConfigFile (f);
		instance.setDataBits (v);
		assertEquals (v, instance.getDBits ());
	}

	/**
	 * Test of setParity method, of class ConfigFile.
	 */
	@Test
	public void testSetParity ()
	{
		System.out.println ("setParity");
		int v = 3456;
		ConfigFile instance = new ConfigFile (f);
		instance.setParity (v);
		assertEquals (v, instance.getParity ());
	}

	/**
	 * Test of setStopBits method, of class ConfigFile.
	 */
	@Test
	public void testSetStopBits ()
	{
		System.out.println ("setStopBits");
		int v = 4567;
		ConfigFile instance = new ConfigFile (f);
		instance.setStopBits (v);
		assertEquals (v, instance.getSBits ());
	}

	/**
	 * Test of setFlow method, of class ConfigFile.
	 */
	@Test
	public void testSetFlow ()
	{
		System.out.println ("setFlow");
		int v = 5678;
		ConfigFile instance = new ConfigFile (f);
		instance.setFlow (v);
		assertEquals (v, instance.getFlowCtl ());
	}

	/**
	 * Test of setX method, of class ConfigFile.
	 */
	@Test
	public void testSetX ()
	{
		System.out.println ("setX");
		int v = 6789;
		ConfigFile instance = new ConfigFile (f);
		instance.setX (v);
		assertEquals (v, instance.getX ());
	}

	/**
	 * Test of setY method, of class ConfigFile.
	 */
	@Test
	public void testSetY ()
	{
		System.out.println ("setY");
		int v = 7890;
		ConfigFile instance = new ConfigFile (f);
		instance.setY (v);
		assertEquals (v, instance.getY ());
	}

	/**
	 * Test of setWidth method, of class ConfigFile.
	 */
	@Test
	public void testSetWidth ()
	{
		System.out.println ("setWidth");
		int v = 8901;
		ConfigFile instance = new ConfigFile (f);
		instance.setWidth (v);
		assertEquals (v, instance.getWidth ());
	}

	/**
	 * Test of setHeight method, of class ConfigFile.
	 */
	@Test
	public void testSetHeight ()
	{
		System.out.println ("setHeight");
		int v = 9012;
		ConfigFile instance = new ConfigFile (f);
		instance.setHeight (v);
		assertEquals (v, instance.getHeight ());
	}

	/**
	 * Test of setIsMaximized method, of class ConfigFile.
	 */
	@Test
	public void testSetIsMaximized ()
	{
		System.out.println ("setIsMaximized");
		boolean v = true;
		ConfigFile instance = new ConfigFile (f);
		instance.setIsMaximized (v);
		assertEquals (v, instance.getIsMax ());
	}

	/**
	 * Test of setFontSizeValue method, of class ConfigFile.
	 */
	@Test
	public void testSetFontSizeValue ()
	{
		System.out.println ("setFontSizeValue");
		int v = 18;
		ConfigFile instance = new ConfigFile (f);
		instance.setFontSizeValue (v);
		assertEquals (v, instance.getFontSizeValue ());
	}

	/**
	 * Test of setSelectedTab method, of class ConfigFile.
	 */
	@Test
	public void testSetSelectedTab ()
	{
		System.out.println ("setSelectedTab");
		int v = 5;
		ConfigFile instance = new ConfigFile (f);
		instance.setSelectedTab (v);
		assertEquals (v, instance.getSelectedTab ());
	}

	/**
	 * Test of getPort method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultPort () throws Exception
	{
		System.out.println ("getDefaultPort");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		String result = instance.getPort();
		assertEquals ("", result);
	}

	/**
	 * Test of getSpeed method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultSpeed () throws Exception
	{
		System.out.println ("getDefaultSpeed");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 115200;
		int result = instance.getSpeed ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDBits method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultDBits () throws Exception
	{
		System.out.println ("getDefaultDBits");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 8;
		int result = instance.getDBits ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getParity method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultParity () throws Exception
	{
		System.out.println ("getDefaultParity");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 0;
		int result = instance.getParity ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getSBits method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultSBits () throws Exception
	{
		System.out.println ("getDefaultSBits");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 1;
		int result = instance.getSBits ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFlowCtl method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultFlowCtl () throws Exception
	{
		System.out.println ("getDefaultFlowCtl");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 0;
		int result = instance.getFlowCtl ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getX method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultX () throws Exception
	{
		System.out.println ("getDefaultX");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 0;
		int result = instance.getX ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getY method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultY () throws Exception
	{
		System.out.println ("getDefaultY");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 0;
		int result = instance.getY ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getWidth method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultWidth () throws Exception
	{
		System.out.println ("getDefaultWidth");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 800;
		int result = instance.getWidth ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getHeight method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultHeight () throws Exception
	{
		System.out.println ("getDefaultHeight");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 600;
		int result = instance.getHeight ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getIsMax method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultIsMax () throws Exception
	{
		System.out.println ("getDefaultIsMax");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		boolean expResult = true;
		boolean result = instance.getIsMax ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFontSizeValue method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultFontSizeValue () throws Exception
	{
		System.out.println ("getDefaultFontSizeValue");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 12;
		int result = instance.getFontSizeValue ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getSelectedTab method, of class ConfigFile.
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultSelectedTab () throws Exception
	{
		System.out.println ("getDefaultSelectedTab");
		ConfigFile instance = new ConfigFile (f);
		instance.read();
		int expResult = 0;
		int result = instance.getSelectedTab ();
		assertEquals (expResult, result);
	}
}
