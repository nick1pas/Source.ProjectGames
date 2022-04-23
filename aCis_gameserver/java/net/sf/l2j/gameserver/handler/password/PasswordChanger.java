/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.handler.password;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.CustomBypassHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;

public class PasswordChanger 
{
	private static final Logger _log = Logger.getLogger(PasswordChanger.class.getName());
	
	private static PasswordChanger _instance;
	private static PasswordChangerHandler _menuHandler;

	public static final PasswordChanger getInstance() 
	{
		if (_instance == null)
		{
			_instance = new PasswordChanger();
		}
		return _instance;
	}

	private PasswordChanger()
	{
		_log.log(Level.INFO, "Initializing PasswordChanger.");
		
		_menuHandler = new PasswordChangerHandler();

		CustomBypassHandler.getInstance().registerCustomBypassHandler(_menuHandler);
		VoicedCommandHandler.getInstance().registerHandler(_menuHandler);
	}
}
