/*
 * SMSWindowTest.java, part of the JYMAG package.
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

import bogdrosoft.jymag.PhoneMessage;
import bogdrosoft.jymag.comm.TransferParameters;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import java.awt.GraphicsEnvironment;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * SMSWindowTest - a test for the SMSWindow class.
 * @author Bogdan Drozdowski
 */
public class SMSWindowTest
{
	@Test
	public void testConstructNullTransferParameters()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		PhoneMessage msg = new PhoneMessage();
		MainWindow mw = new MainWindow();
		SMSWindow w = new SMSWindow (null, mw, 12.0f, msg);
		assertTrue(UiTestHelper.isKeyListenerPresent(w));
	}

	@Test
	public void testConstructNullMessage()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		TransferParameters tp = new TransferParameters(
			new FakeCommPortIdentifier(),
			115200,
			8,
			1,
			0,
			0,
			new Object()
		);
		MainWindow mw = new MainWindow();
		SMSWindow w = new SMSWindow (tp, mw, 12.0f, null);
		assertTrue(UiTestHelper.isKeyListenerPresent(w));
	}
	@Test
	public void testConstructNullTransferParametersAndMessage()
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}
		MainWindow mw = new MainWindow();
		SMSWindow w = new SMSWindow (null, mw, 12.0f, null);
		assertFalse(UiTestHelper.isKeyListenerPresent(w));
	}
}
