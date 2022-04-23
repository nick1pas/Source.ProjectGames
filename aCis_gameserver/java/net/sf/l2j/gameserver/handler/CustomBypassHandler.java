/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This 'Bypass Handler' is a handy tool indeed!<br>
 * Basically, you can send any custom bypass commmands to it from ANY npc and it will call the appropriate function.<br>
 * @author JStar
 */
public class CustomBypassHandler
{
	private static CustomBypassHandler _instance = null;
	private Map<String, ICustomByPassHandler> _handlers;
	
	private CustomBypassHandler()
	{
		_handlers = new HashMap<String, ICustomByPassHandler>();
		
	}
	
	/**
	 * Receives the non-static instance of the RebirthManager.
	 * @return
	 */
	public static CustomBypassHandler getInstance()
	{
		if (_instance == null)
		{
			_instance = new CustomBypassHandler();
		}
		
		return _instance;
	}
	
	/**
	 * @param handler as ICustomByPassHandler
	 */
	public void registerCustomBypassHandler(ICustomByPassHandler handler)
	{
		for (String s : handler.getByPassCommands())
		{
			_handlers.put(s, handler);
		}
	}
	
	/**
	 * Handles player's Bypass request to the Custom Content.
	 * @param player
	 * @param command
	 */
	public void handleBypass(L2PcInstance player, String command)
	{
		// Rebirth Manager and Engine Caller
		
		String cmd = "";
		String params = "";
		int iPos = command.indexOf(" ");
		if (iPos != -1)
		{
			cmd = command.substring(7, iPos);
			params = command.substring(iPos + 1);
		}
		else
		{
			cmd = command.substring(7);
		}
		ICustomByPassHandler ch = _handlers.get(cmd);
		if (ch != null)
		{
			ch.handleCommand(cmd, player, params);
		}
		else
		{
			// Caller Tester
		}
	}
}