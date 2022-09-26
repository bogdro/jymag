/*
 * PhoneElementTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2016 Bogdan Drozdowski, bogdandr (at) op.pl
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PhoneElementTest - a test for the PhoneElement class.
 * @author Bogdan Drozdowski
 */
public class PhoneElementTest
{
	public PhoneElementTest ()
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
	 * Test of getID method, of class PhoneElement.
	 */
	@Test
	public void testGetID ()
	{
		System.out.println ("getID");
		PhoneElement instance = new PhoneElement ("1", "2", "3");
		String expResult = "1";
		String result = instance.getID ();
		assertEquals (expResult, result);

		instance = new PhoneElement (null, "FGIF", "3");
		result = instance.getID ();
		assertNull (result);
	}

	/**
	 * Test of getExt method, of class PhoneElement.
	 */
	@Test
	public void testGetExt ()
	{
		System.out.println ("getExt");
		PhoneElement instance = new PhoneElement ("1", "2", "3");
		String expResult = "2";
		String result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "FGIF", "3");
		expResult = "gif";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "JPEG", "3");
		expResult = "jpg";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "MIDI", "3");
		expResult = "mid";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "VCRD", "3");
		expResult = "vcf";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "VCRD", "3");
		expResult = "vcf";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "VCAL", "3");
		expResult = "ics";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "WBMP", "3");
		expResult = "wbm";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "TIFF", "3");
		expResult = "tif";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "PICT", "3");
		expResult = "pct";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "SVGZ", "3");
		expResult = "svz";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "AIFF", "3");
		expResult = "aif";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "MPEG", "3");
		expResult = "mpg";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "EMS_GR", "3");
		expResult = "emg";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "ASG1", "3");
		expResult = "as1";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "ASG2", "3");
		expResult = "as2";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "EMS_AN", "3");
		expResult = "ema";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "MJPG", "3");
		expResult = "mjp";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "3GP2", "3");
		expResult = "3gp";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "3GPP", "3");
		expResult = "3gp";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", null, "3");
		expResult = "";
		result = instance.getExt ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "testString", "3");
		expResult = "teststring";
		result = instance.getExt ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFilename method, of class PhoneElement.
	 */
	@Test
	public void testGetFilename ()
	{
		System.out.println ("getFilename");
		PhoneElement instance = new PhoneElement ("1", "2", "3");
		String expResult = "3";
		String result = instance.getFilename ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "type", "a b.c,d");
		expResult = "a_b_c_d";
		result = instance.getFilename ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "type", "d(e)f[g]h{i}j");
		expResult = "d_e_f_g_h_i_j";
		result = instance.getFilename ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "type", "k~l!m%n^o&");
		expResult = "k_l_m_n_o_";
		result = instance.getFilename ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "type", "*p*+r=s|t\\u");
		expResult = "_p__r_s_t_u";
		result = instance.getFilename ();
		assertEquals (expResult, result);

		instance = new PhoneElement ("1", "type", "v:w;x\"y'<z>?/");
		expResult = "v_w_x_y__z___";
		result = instance.getFilename ();
		assertEquals (expResult, result);

	}

	/**
	 * Test of toString method, of class PhoneElement.
	 */
	@Test
	public void testToString ()
	{
		System.out.println ("toString");
		PhoneElement instance = new PhoneElement ("1", "2", "3");
		String expResult = "PhoneElement[ID=1,3.2]";
		String result = instance.toString ();
		assertEquals (expResult, result);
	}
}
