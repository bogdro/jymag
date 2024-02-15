/*
 * AboutBoxTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * AboutBoxTest - a test for the AboutBox class.
 * @author Bogdan Drozdowski
 */
public class AboutBoxTest
{
	@Test
	public void testConstruct()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		AboutBox ab = new AboutBox(null, false, 12);
		assertTrue(UiTestHelper.isKeyListenerPresent(ab));
	}

	@Test
	public void testCreateWebURI()
	{
		assertNotNull(AboutBox.createWebURI("https://www.example.com"));
	}

	@Test
	public void testCreateURI()
	{
		assertNotNull(AboutBox.createURI("https", "www.example.com"));
	}

	@Test
	public void testGetFileContents() throws UnsupportedEncodingException
	{
		String data = "aaaa";
		assertEquals(
			data.length(),
			AboutBox.getFileContents(
				new ByteArrayInputStream(data.getBytes("UTF-8"))
			).length()
		);
	}
}
