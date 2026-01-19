/*
 * SignalDisplayerTest.java, part of the JYMAG package.
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

import bogdrosoft.jymag.comm.DataTransporter;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import java.awt.GraphicsEnvironment;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SignalDisplayerTest - a test for the SignalDisplayer class.
 * @author Bogdan Drozdowski
 */
public class SignalDisplayerTest
{
	@Test
	public void testConstructNullDt()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		SignalDisplayer sd = new SignalDisplayer (
			null, mw, new Object(), 12.0f
		);
		assertFalse(UiTestHelper.isKeyListenerPresent(sd));
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
		SignalDisplayer sd = new SignalDisplayer (
			dt, mw,	null, 12.0f
		);
		assertFalse(UiTestHelper.isKeyListenerPresent(sd));
	}

	@Test
	public void testConstruct()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		SignalDisplayer sd = constructSignalDisplayer();
		assertTrue(UiTestHelper.isKeyListenerPresent(sd));
	}

	@Test
	public void testSetLevelZero()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		SignalDisplayer sd = constructSignalDisplayer();
		sd.setLevel(0);
	}

	private static SignalDisplayer constructSignalDisplayer()
	{
		DataTransporter dt = new DataTransporter(
			new FakeCommPortIdentifier()
		);
		MainWindow mw = new MainWindow();
		SignalDisplayer sd = new SignalDisplayer (
			dt, mw,	new Object(), 12.0f
		);
		sd.exit();
		return sd;
	}
}
