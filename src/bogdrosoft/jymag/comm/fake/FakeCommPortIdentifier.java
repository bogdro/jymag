/*
 * FakeCommPortIdentifier.java, part of the JYMAG package.
 *
 * Copyright (C) 2022-2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
package bogdrosoft.jymag.comm.fake;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.util.Enumeration;
import java.util.Vector;

/**
 * FakeCommPortIdentifier - a fake CommPortIdentifier implementation for tests.
 * @author Bogdan Drozdowski
 */
public class FakeCommPortIdentifier //extends CommPortIdentifier // illegal access, even if in the gnu.io package.
{
	//private static final Vector<CommPortIdentifier> fakePorts = new Vector<CommPortIdentifier> (1);
	private static final Vector<FakeCommPortIdentifier> FAKE_PORTS
		= new Vector<FakeCommPortIdentifier> (1);
	static
	{
		FAKE_PORTS.add(new FakeCommPortIdentifier());
	}
	private final SerialPort sp = new FakeSerialPort();

	public FakeCommPortIdentifier()
	{
		//super ("COMfaked", null, PORT_SERIAL, null);
	}
	
	public static Enumeration<?> getPortIdentifiers ()
	{
		return FAKE_PORTS.elements();
	}

	//@Override
	public String getName() 
	{ 
		return "COMfaked";
	}

	//@Override
	public CommPort open(String owner, int i)
	{
		return sp;
	}

	//@Override
	public int getPortType ()
	{
		return CommPortIdentifier.PORT_SERIAL;
	}

	//public static CommPortIdentifier getPortIdentifier (String port)
	public static FakeCommPortIdentifier getPortIdentifier (String port)
	{
		return FAKE_PORTS.get(0);
	}
}
