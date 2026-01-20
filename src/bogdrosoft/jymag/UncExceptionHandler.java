/*
 * UncExceptionHandler.java, part of the JYMAG package.
 *
 * Copyright (C) 2026 Bogdan Drozdowski, bogdro (at) users . sourceforge . net
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
package bogdrosoft.jymag;

import java.awt.Component;

/**
 * A sample uncaught-exception handler class for threads.
 * @author Bogdan Drozdowski
 */
public class UncExceptionHandler implements Thread.UncaughtExceptionHandler
{
	/**
	 * A sample uncaught-exception handler instance for threads.
	 */
	public static final UncExceptionHandler HANDLER = new UncExceptionHandler();

	private final Component target;

	public UncExceptionHandler()
	{
		target = null;
	}

	public UncExceptionHandler(Component c)
	{
		target = c;
	}

	/**
	 * Called when an uncaught exception occurrs.
	 * @param t The thread, in which the exception occurred.
	 * @param ex The exception that occurred.
	 */
	@Override
	public void uncaughtException(Thread t, Throwable ex)
	{
		try
		{
			Utils.handleException(ex, "UncExceptionHandler: Thread=" // NOI18N
			 + ((t != null) ? t.getName() : "?")); // NOI18N
		}
		catch (Throwable th)
		{
			/* ignore */
		}
		try
		{
			if (target != null)
			{
				target.paintAll(target.getGraphics());
			}
		} catch (Throwable th) {}
	}

	public static void setHandlerForGuiThreads(Component c)
	{
		try
		{
			Thread[] ths = new Thread[Thread.activeCount() * 5];
			final int nThreads = Thread.enumerate(ths);
			for (int i = 0; i < nThreads; i++)
			{
				String name = ths[i].getName();
				if (name == null)
				{
					continue;
				}
				if (name.contains("AWT") // NOI18N
				 || name.contains("Swing") // NOI18N
				 || name.contains("Image") // NOI18N
				)
				{
					ths[i].setUncaughtExceptionHandler(new UncExceptionHandler(c));
				}
			}
		}
		catch (Throwable th)
		{
			// don't care for exceptions, this is optional
		}
	}

	@Override
	public String toString() {
		return "UncExceptionHandler"; // NOI18N
	}
}
