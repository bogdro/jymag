/*
 * MainWindowTest.java, part of the JYMAG package.
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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MainWindowTest - a test for the MainWindow class.
 * @author Bogdan Drozdowski
 */
public class MainWindowTest
{
	/**
	 * Test of constructing the MainWindow class.
	 */
	@Test
	public void testConstruct()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		assertTrue(UiTestHelper.isKeyListenerPresent(mw));
	}

	/**
	 * Test of disablePortControls method, of class MainWindow.
	 */
	@Test
	public void testDisablePortControls()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.disablePortControls();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of disablePortControls method, of class MainWindow.
	 */
	@Test
	public void testEnablePortControls()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.enablePortControls();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setSendingStatus method, of class MainWindow.
	 */
	@Test
	public void testSetSendingStatus()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.setSendingStatus();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setReceivingStatus method, of class MainWindow.
	 */
	@Test
	public void testSetReceivingStatus()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.setReceivingStatus();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setReadyStatus method, of class MainWindow.
	 */
	@Test
	public void testSetReadyStatus()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.setReadyStatus();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setProgressCurrentValue method, of class MainWindow.
	 */
	@Test
	public void testSetProgressCurrentValue()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		int value = 35;
		MainWindow mw = new MainWindow();
		mw.setProgressCurrentValue(value);
		assertEquals(value, mw.getProgressCurrentValue());
	}

	/**
	 * Test of setProgressMinimumValue method, of class MainWindow.
	 */
	@Test
	public void testSetProgressMinimumValue()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		int value = 10;
		MainWindow mw = new MainWindow();
		mw.setProgressCurrentValue(value);
		mw.setProgressMinimumValue(0);
		assertEquals(value, mw.getProgressCurrentValue());
	}

	/**
	 * Test of setProgressMaximumValue method, of class MainWindow.
	 */
	@Test
	public void testSetProgressMaximumValue()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		int value = 10;
		MainWindow mw = new MainWindow();
		mw.setProgressCurrentValue(value);
		mw.setProgressMaximumValue(100);
		assertEquals(value, mw.getProgressCurrentValue());
	}

	/**
	 * Test of getTransferParameters method, of class MainWindow.
	 */
	@Test
	public void testGetTransferParameters()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of createConfigFileChooser method, of class MainWindow.
	 */
	@Test
	public void testCreateConfigFileChooser()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.createConfigFileChooser();
		assertNotNull(mw.cfgFC);
	}

	/**
	 * Test of updateControls method, of class MainWindow.
	 */
	@Test
	public void testUpdateControls()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.updateControls();
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setPanelConnections method, of class MainWindow.
	 */
	@Test
	public void testSetPanelConnections()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.setPanelConnections(mw);
		assertNotNull(mw.getTransferParameters());
	}

	/**
	 * Test of setPorts method, of class MainWindow.
	 */
	@Test
	public void testSetPorts()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		mw.setPorts();
		assertNotNull(mw.getTransferParameters());
	}
}
