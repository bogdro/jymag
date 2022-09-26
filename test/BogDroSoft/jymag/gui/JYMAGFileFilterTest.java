/*
 * JYMAGFileFilterTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2014-2022 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

package BogDroSoft.jymag.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JYMAGFileFilterTest - a test for the JYMAGFileFilter class.
 * @author Bogdan Drozdowski
 */
public class JYMAGFileFilterTest {

	/**
	 * Test of accept method, of class JYMAGFileFilter.
	 */
	@Test
	public void testAccept()
	{
		System.out.println("accept");
		Map<String, Integer> fileTypes = new HashMap<String, Integer>(1);
		fileTypes.put("txt", Integer.MIN_VALUE);
		JYMAGFileFilter instance = new JYMAGFileFilter("test_filter", fileTypes);
		assertTrue(instance.accept(new File("aaa.txt")));
		assertTrue(instance.accept(new File("aaa.png.txt")));
		assertFalse(instance.accept(new File("aaa.txt.png")));
	}

	/**
	 * Test of getDescription method, of class JYMAGFileFilter.
	 */
	@Test
	public void testGetDescription()
	{
		System.out.println("getDescription");
		JYMAGFileFilter instance = new JYMAGFileFilter("test_filter", null);
		assertNotNull(instance.getDescription());
		assertTrue(instance.getDescription().contains("test_filter"));
	}

	/**
	 * Test of toString method, of class JYMAGFileFilter.
	 */
	@Test
	public void testToString()
	{
		System.out.println("toString");
		JYMAGFileFilter instance = new JYMAGFileFilter("test_filter", null);
		assertNotNull(instance.toString());
		assertTrue(instance.toString().contains("test_filter"));
	}
}
