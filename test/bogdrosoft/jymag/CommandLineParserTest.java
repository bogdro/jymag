/*
 * CommandLineParserTest.java, part of the JYMAG package.
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

import bogdrosoft.jymag.CommandLineParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CommandLineParserTest - a test for the CommandLineParser class.
 * @author Bogdan Drozdowski
 */
public class CommandLineParserTest
{
	private static final Object SYNC = new Object();

	public CommandLineParserTest ()
	{
	}

	@BeforeClass
	public static void setUpClass () throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass () throws Exception
	{
	}

	@Before
	public void setUp ()
	{
	}

	@After
	public void tearDown ()
	{
	}

	/**
	 * Test of getDstDirName method, of class CommandLineParser.
	 */
	@Test
	public void testGetDstDirName ()
	{
		System.out.println ("getDstDirName");
		String expResult = "testDir";
		String[] params = {"--download-dir", expResult};
		CommandLineParser.parse (params, SYNC);
		String result = CommandLineParser.getDstDirName ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getDBits method, of class CommandLineParser.
	 */
	@Test
	public void testGetDBits ()
	{
		System.out.println ("getDBits");
		int expResult = 7;
		String[] params = {"--databits", String.valueOf (expResult)};
		CommandLineParser.parse (params, SYNC);
		int result = CommandLineParser.getDBits ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getSBits method, of class CommandLineParser.
	 */
	@Test
	public void testGetSBits ()
	{
		System.out.println ("getSBits");
		float expResult = 1.5F;
		String[] params = {"--stopbits", String.valueOf (expResult)};
		CommandLineParser.parse (params, SYNC);
		float result = CommandLineParser.getSBits ();
		assertEquals (expResult, result, 0.0001);
	}

	/**
	 * Test of getSpeed method, of class CommandLineParser.
	 */
	@Test
	public void testGetSpeed ()
	{
		System.out.println ("getSpeed");
		int expResult = 1200;
		String[] params = {"--speed", String.valueOf (expResult)};
		CommandLineParser.parse (params, SYNC);
		int result = CommandLineParser.getSpeed ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFlowMode method, of class CommandLineParser.
	 */
	@Test
	public void testGetFlowMode ()
	{
		System.out.println ("getFlowMode");
		int expResult = 1;
		String[] params = {"--flow", "soft"};
		CommandLineParser.parse (params, SYNC);
		int result = CommandLineParser.getFlowMode ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getParityMode method, of class CommandLineParser.
	 */
	@Test
	public void testGetParityMode ()
	{
		System.out.println ("getParityMode");
		int expResult = 2;
		String[] params = {"--parity", "odd"};
		CommandLineParser.parse (params, SYNC);
		int result = CommandLineParser.getParityMode ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getPortName method, of class CommandLineParser.
	 */
	@Test
	public void testGetPortName ()
	{
		System.out.println ("getPortName");
		String expResult = "testPort";
		String[] params = {"--port", expResult};
		CommandLineParser.parse (params, SYNC);
		String result = CommandLineParser.getPortName ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of parse method, of class CommandLineParser.
	 */
	@Test
	public void testParse ()
	{
		System.out.println ("parse");
		String[] args = null;
		CommandLineParser.parse (args, SYNC);
	}
}
