/*
 * RawCommunicatorTest.java, part of the JYMAG package.
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

import bogdrosoft.jymag.comm.DataTransporter;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import java.awt.GraphicsEnvironment;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * RawCommunicatorTest - a test for the RawCommunicator class.
 * @author Bogdan Drozdowski
 */
public class RawCommunicatorTest
{
	@Test
	public void testConstruct()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		RawCommunicator rc = constructRawCommunicator();
		assertTrue(UiTestHelper.isKeyListenerPresent(rc));
	}

	@Test
	public void testConstructNullDt()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		RawCommunicator rc = new RawCommunicator (
			null, mw, new Object(), 12.0f
		);
		assertFalse(UiTestHelper.isKeyListenerPresent(rc));
	}

	@Test
	public void testConstructNullSynchro()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		DataTransporter dt = new DataTransporter(
			new FakeCommPortIdentifier()
		);
		MainWindow mw = new MainWindow();
		RawCommunicator rc = new RawCommunicator (
			dt, mw,	null, 12.0f
		);
		assertFalse(UiTestHelper.isKeyListenerPresent(rc));
	}

	@Test
	public void testSetSignalToggle()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		RawCommunicator rc = constructRawCommunicator();
		JToggleButton but = new JToggleButton();
		rc.setSignal(but, true);
	}

	@Test
	public void testSetSignalToggleNullButton()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		RawCommunicator rc = constructRawCommunicator();
		rc.setSignal((JToggleButton)null, true);
	}

	@Test
	public void testSetSignalLabel()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		RawCommunicator rc = constructRawCommunicator();
		JLabel label = new JLabel();
		rc.setSignal(label, true);
	}

	@Test
	public void testSetSignalLabelNullButton()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		RawCommunicator rc = constructRawCommunicator();
		rc.setSignal((JLabel)null, true);
	}

	private static RawCommunicator constructRawCommunicator()
	{
		DataTransporter dt = new DataTransporter(
			new FakeCommPortIdentifier()
		);
		MainWindow mw = new MainWindow();
		return new RawCommunicator (dt, mw, new Object(), 12.0f);
	}
}
