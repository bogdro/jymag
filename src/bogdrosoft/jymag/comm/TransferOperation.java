/*
 * TransferOperation.java, part of the JYMAG package.
 *
 * Copyright (C) 2011-2024 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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

import bogdrosoft.jymag.Utils;
import java.awt.Component;

/**
 * A class describing the operation to perform.
 * @author Bogdan Drozdowski
 */
abstract class TransferOperation<T>
{
	private String opName;
	private String opErrorParams;
	private Runnable opOnDone;
	private boolean opWaitFor;
	private Object opSync;
	private boolean opQuiet;
	private boolean opQuietGUI;
	private Component opParentFrame;

	/**
	 * The TUOperation constructor, setting basic data.
	 * @param name the name of this operation.
	 * @param errorParams a description or value of any paramters.
	 * @param onDone the code to run after performing the operation.
	 * @param waitFor whether the program should wait until the
	 * 	operation is completed.
	 * @param sync the transmission synchronization object.
	 *	 Can't be null.
	 * @param quiet whether the operation should NOT display
	 *	any messages.
	 * @param quietGUI whether the operation should NOT display
	 *	any GUI messages.
	 * @param parentFrame the parent frame for displaying
	 *	GUI messages.
	 */
	TransferOperation(String name, String errorParams, Runnable onDone,
		boolean waitFor, Object sync, boolean quiet, boolean quietGUI,
		Component parentFrame)
	{
		if (sync == null)
		{
			throw new IllegalArgumentException("TUOperation.TUOperation: sync==null"); // NOI18N
		}
		opName = name;
		opErrorParams = errorParams;
		opOnDone = onDone;
		opWaitFor = waitFor;
		opSync = sync;
		opQuiet = quiet;
		opQuietGUI = quietGUI;
		opParentFrame = parentFrame;
	}

	/**
	 * Performs the operation.
	 * @return the result of the operation.
	 */
	public abstract T perform() throws Exception;

	/**
	 * Gets an error message for the given error code.
	 * @param errCode the error code to get the message for.
	 * @return an error message for the given error code.
	 */
	public String msgForError(int errCode)
	{
		return Utils.EMPTY_STR;
	}

	/**
	 * The name of this operation.
	 * @return the name of this operation.
	 */
	public String getName()
	{
		return opName;
	}

	/**
	 * A description or value of any paramters that may
	 * have cause an error and can be useful for error messages.
	 * @return a description or value of any paramters.
	 */
	public String getErrorParams()
	{
		return opErrorParams;
	}

	/**
	 * This method runs after performing the operation.
	 */
	public void runOnDone()
	{
		if (opOnDone != null)
		{
			opOnDone.run();
		}
	}

	/**
	 * Tells whether the program should wait until the
	 * operation is completed.
	 * @return TRUE if the program should wait until the
	 * operation is completed.
	 */
	public boolean isWaitFor()
	{
		return opWaitFor;
	}

	/**
	 * Gets the transmission synchronization object.
	 * @return the transmission synchronization object.
	 */
	public Object getSync()
	{
		return opSync;
	}

	/**
	 * Tells whether the operation should NOT display any messages.
	 * @return TRUE if the operation should NOT display any messages.
	 */
	public boolean isQuiet()
	{
		return opQuiet;
	}

	/**
	 * Tells whether the operation should NOT display any GUI messages.
	 * @return TRUE if the operation should NOT display any GUI messages.
	 */
	public boolean isQuietGUI()
	{
		return opQuietGUI;
	}

	/**
	 * Returns the parent frame for displaying GUI messages.
	 * @return the parent frame for displaying GUI messages.
	 */
	public Component getParentFrame()
	{
		return opParentFrame;
	}

	/**
	 * Performs any additional processing of the received data
	 * if the data is not a Number.
	 * @param t the received data to process.
	 */
	public void processData(T t)
	{
		// empty by default
	}

	@Override
	public String toString()
	{
		return "TUOperation." + getName(); // NOI18N
	}
	
}
