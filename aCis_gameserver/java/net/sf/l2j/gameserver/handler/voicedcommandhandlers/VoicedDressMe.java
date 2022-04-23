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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Anarchy
 *
 */
public class VoicedDressMe implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{ 
		"dressme" 
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("dressme"))
		{
			if (activeChar.isVip() || Config.DRESS_ME_NEED_PVPS)
			{
				if (activeChar.getPvpKills() < Config.PVPS_TO_USE_DRESS_ME)
				{
					activeChar.sendMessage("You must have at least " + Config.PVPS_TO_USE_DRESS_ME + " PvP to use dressme.");
					return false;
				}
			}
			sendMainWindow(activeChar);
		}

		return true;
	}

	public static void sendMainWindow(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile("./data/html/mods/dressme/main.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "Enabled" : "Disabled");
		if (activeChar.getDressMeData() == null)
		{
			htm.replace("%armor%", "You have no custom armor.");
		}
		else
		{
			htm.replace("%armor%", activeChar.getDressMeData().getChestId() == 0 ? "You have no custom armor." : ItemTable.getInstance().getTemplate(activeChar.getDressMeData().getChestId()).getName());
		}

		activeChar.sendPacket(htm);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}