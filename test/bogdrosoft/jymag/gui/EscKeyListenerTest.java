/*
 * EscKeyListenerTest.java, part of the JYMAG package.
 *
 * Copyright (C) 2024-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EscKeyListenerTest - a test for the EscKeyListener class.
 * @author Bogdan Drozdowski
 */
public class EscKeyListenerTest
{
	/**
	 * Test of install method, of class EscKeyListener.
	 */
	@Test
	public void testInstall()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		JFrame f = new JFrame();
		EscKeyListener listener = new EscKeyListener(f);
		listener.install();
		KeyListener[] listeners = f.getKeyListeners();
		for (int i = 0; i < listeners.length; i++)
		{
			if (listeners[i] == listener)
			{
				return;
			}
		}
		fail("Listener not found");
	}

	/**
	 * Test of keyTyped method, of class EscKeyListener.
	 */
	@Test
	public void testKeyTyped()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		try
		{
			JFrame f = new JFrame();
			EscKeyListener listener = new EscKeyListener(f);
			listener.install();
			listener.keyTyped(
				new KeyEvent(f, 1, 0, 0, 0, (char)KeyEvent.VK_ESCAPE)
			);
		}
		catch (Exception ex)
		{
			fail("Exception during keyTyped: " + ex);
		}
	}

	/**
	 * Test of keyTyped method, of class EscKeyListener.
	 */
	@Test
	public void testKeyTypedNullFrame()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		try
		{
			EscKeyListener listener = new EscKeyListener(null);
			listener.install();
			listener.keyTyped(
				new KeyEvent(new JFrame(), 1, 0, 0, 0, (char)KeyEvent.VK_ESCAPE)
			);
		}
		catch (Exception ex)
		{
			fail("Exception during keyTyped: " + ex);
		}
	}

	/**
	 * Test of keyTyped method, of class EscKeyListener.
	 */
	@Test
	public void testKeyTypedNullEvent()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		try
		{
			JFrame f = new JFrame();
			EscKeyListener listener = new EscKeyListener(f);
			listener.install();
			listener.keyTyped(null);
		}
		catch (Exception ex)
		{
			fail("Exception during keyTyped: " + ex);
		}
	}

	/**
	 * Test of toString method, of class EscKeyListener.
	 */
	@Test
	public void testToString()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		String name = "testName";
		JFrame f = new JFrame();
		f.setName(name);
		EscKeyListener listener = new EscKeyListener(f);
		assertTrue(listener.toString().contains(name));
	}
}
