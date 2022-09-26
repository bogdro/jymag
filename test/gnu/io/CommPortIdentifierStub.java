/*
 * CommPortIdentifierStub.java, part of the BogDroSoft.jymag.comm package.
 *
 * Copyright (C) 2014 Bogdan Drozdowski, bogdandr (at) op.pl
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

package gnu.io;

/**
 * CommPortIdentifierStub - a stub stat simulates a CommPortIdentifier.
 * @author Bogdan Drozdowski
 */
public class CommPortIdentifierStub extends CommPortIdentifier
{

        /**
	 * Creates a new instance of CommPortIdentifierStub.
	 *
	 * @param pn the port name
	 * @param cp the port instance
	 * @param pt the port type
	 * @param driver the port driver
	 */
	public CommPortIdentifierStub (String pn, CommPort cp, int pt, CommDriver driver)
        {
		super (pn, cp, pt, driver);
                /*PortName        = pn;
                commport        = cp;
                PortType        = pt;
                next            = null;
                RXTXDriver      = driver;*/

        }

}
