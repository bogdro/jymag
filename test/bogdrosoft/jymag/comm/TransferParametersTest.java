/*
 * TransferParametersTest.java, part of the JYMAG package.
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
package bogdrosoft.jymag.comm;

import bogdrosoft.jymag.CommandLineParser;
import bogdrosoft.jymag.comm.fake.FakeCommPortIdentifier;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * TransferParametersTest - a test for the TransferParameters class.
 * @author Bogdan Drozdowski
 */
public class TransferParametersTest
{
	private static final int SPEED = 115200;
	private static final int DATA_BITS = 8;
	private static final int STOP_BITS = 8;
	private static final int PARITY = 0;
	private static final int FLOW = 2;
	private static final FakeCommPortIdentifier PORT_ID
		= new FakeCommPortIdentifier();
	private static final Object SYNC = new Object();

	@BeforeClass
	public static void setUpClass () throws Exception
	{
		CommandLineParser.mock = true;
		System.setProperty("mock", "138b7ce0632d70dd9d6fc7b571fd9199");
	}

	@Test
	public void testConstructor()
	{
		TransferParameters tp = new TransferParameters(
			PORT_ID, SPEED, DATA_BITS, STOP_BITS,
			PARITY, FLOW, SYNC);
		verifyParameters(tp);
	}

	@Test
	public void testConstructorWithStringPortId()
	{
		TransferParameters tp = new TransferParameters(
			PORT_ID.getName(), SPEED, DATA_BITS,
			STOP_BITS, PARITY, FLOW, SYNC);
		verifyParameters(tp);
	}

	@Test
	public void testConstructorWithStringParameters()
	{
		TransferParameters tp = new TransferParameters(
			PORT_ID.getName(), String.valueOf(SPEED),
			String.valueOf(DATA_BITS),
			String.valueOf(STOP_BITS),
			PARITY, FLOW, SYNC);
		verifyParameters(tp);
	}

	@Test
	public void testConstructorWithGuiParameters()
	{
		JComboBox<String> portCombo = new JComboBox<String>();
		Vector<String> portList = new Vector<String>(1);
		portList.add(PORT_ID.getName());
		ComboBoxModel<String> portModel
			= new DefaultComboBoxModel<String>(portList);
		portModel.setSelectedItem(PORT_ID.getName());
		portCombo.setModel(portModel);

		JComboBox<String> speedCombo = new JComboBox<String>();
		Vector<String> speedList = new Vector<String>(1);
		speedList.add(String.valueOf(SPEED));
		ComboBoxModel<String> speedModel
			= new DefaultComboBoxModel<String>(speedList);
		portModel.setSelectedItem(String.valueOf(SPEED));
		speedCombo.setModel(speedModel);

		JComboBox<String> dataBitsCombo = new JComboBox<String>();
		Vector<String> dataBitsList = new Vector<String>(1);
		speedList.add(String.valueOf(DATA_BITS));
		ComboBoxModel<String> dataBitsModel
			= new DefaultComboBoxModel<String>(dataBitsList);
		dataBitsModel.setSelectedItem(String.valueOf(DATA_BITS));
		dataBitsCombo.setModel(dataBitsModel);

		JComboBox<String> stopBitsCombo = new JComboBox<String>();
		Vector<String> stopBitsList = new Vector<String>(1);
		stopBitsList.add(String.valueOf(STOP_BITS));
		ComboBoxModel<String> stopBitsModel
			= new DefaultComboBoxModel<String>(stopBitsList);
		stopBitsModel.setSelectedItem(String.valueOf(STOP_BITS));
		stopBitsCombo.setModel(stopBitsModel);

		JComboBox<String> parityCombo = new JComboBox<String>();
		Vector<String> parityList = new Vector<String>(1);
		parityList.add(String.valueOf(PARITY));
		ComboBoxModel<String> parityModel
			= new DefaultComboBoxModel<String>(parityList);
		parityModel.setSelectedItem(String.valueOf(PARITY));
		parityCombo.setModel(parityModel);

		JCheckBox flowSoft = new JCheckBox();
		flowSoft.setSelected(false);

		JCheckBox flowHard = new JCheckBox();
		flowHard.setSelected(true);

		TransferParameters tp = new TransferParameters(
			portCombo, speedCombo, dataBitsCombo,
			stopBitsCombo, parityCombo, flowSoft, flowHard, SYNC);
		verifyParameters(tp);
	}

	private void verifyParameters(TransferParameters tp)
	{
		assertTrue(tp.getId() instanceof FakeCommPortIdentifier);
		assertEquals(SPEED, tp.getSpeed());
		assertEquals(DATA_BITS, tp.getDataBits());
		assertTrue(Math.abs(STOP_BITS - tp.getStopBits()) < 0.1);
		assertEquals(PARITY, tp.getParity());
		assertEquals(FLOW, tp.getFlow());
		assertEquals(SYNC, tp.getSync());
	}
}
