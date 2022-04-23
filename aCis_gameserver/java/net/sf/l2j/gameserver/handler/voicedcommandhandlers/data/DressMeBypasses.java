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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers.data;

import java.util.StringTokenizer;

import net.sf.l2j.Config;

import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.VoicedDressMe;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Anarchy
 *
 */
public class DressMeBypasses implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{
		"bp_changedressmestatus",
		"bp_editWindow",
		"bp_setArmor", 
		"bp_main" 
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("bp_changedressmestatus"))
		{
			if (activeChar.isDressMeEnabled())
			{
				activeChar.setDressMeEnabled(false);
				activeChar.broadcastUserInfo();
			}
			else
			{
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
			}
			
			VoicedDressMe.sendMainWindow(activeChar);
		}
		
		if (command.startsWith("bp_editWindow"))
		{
			String bp = command.substring(14);
			StringTokenizer st = new StringTokenizer(bp);
			
			sendEditWindow(activeChar, st.nextToken());
		}
		
		if (command.startsWith("bp_setArmor"))
		{
			String bp = command.substring(11);
			StringTokenizer st = new StringTokenizer(bp);
			
			String armor = st.nextToken();
			String type = st.nextToken();
			
			setArmor(activeChar, armor, type);
		}
		
		if (command.equals("bp_main"))
			VoicedDressMe.sendMainWindow(activeChar);
		
		return true;
	}
	
	public void setArmor(L2PcInstance player, String armor, String type)
	{
		if (player.getDressMeData() == null)
		{
			DressMeData dmd = new DressMeData();
			player.setDressMeData(dmd);
		}
		
		switch (armor)
		{
			case "heavy":
			{
				if (Config.DRESS_ME_HEAVY_CHESTS.keySet().contains(type))
				{
					player.getDressMeData().setChestId(Config.DRESS_ME_HEAVY_CHESTS.get(type));
				}
				if (Config.DRESS_ME_HEAVY_LEGS.keySet().contains(type))
				{
					player.getDressMeData().setLegsId(Config.DRESS_ME_HEAVY_LEGS.get(type));
				}
				if (Config.DRESS_ME_HEAVY_GLOVES.keySet().contains(type))
				{
					player.getDressMeData().setGlovesId(Config.DRESS_ME_HEAVY_GLOVES.get(type));
				}
				if (Config.DRESS_ME_HEAVY_BOOTS.keySet().contains(type))
				{
					player.getDressMeData().setBootsId(Config.DRESS_ME_HEAVY_BOOTS.get(type));
				}
				
				break;
			}
			case "light":
			{
				if (Config.DRESS_ME_LIGHT_CHESTS.keySet().contains(type))
				{
					player.getDressMeData().setChestId(Config.DRESS_ME_LIGHT_CHESTS.get(type));
				}
				if (Config.DRESS_ME_LIGHT_LEGS.keySet().contains(type))
				{
					player.getDressMeData().setLegsId(Config.DRESS_ME_LIGHT_LEGS.get(type));
				}
				if (Config.DRESS_ME_LIGHT_GLOVES.keySet().contains(type))
				{
					player.getDressMeData().setGlovesId(Config.DRESS_ME_LIGHT_GLOVES.get(type));
				}
				if (Config.DRESS_ME_LIGHT_BOOTS.keySet().contains(type))
				{
					player.getDressMeData().setBootsId(Config.DRESS_ME_LIGHT_BOOTS.get(type));
				}
				
				break;
			}
			case "robe":
			{
				if (Config.DRESS_ME_ROBE_CHESTS.keySet().contains(type))
				{
					player.getDressMeData().setChestId(Config.DRESS_ME_ROBE_CHESTS.get(type));
				}
				if (Config.DRESS_ME_ROBE_LEGS.keySet().contains(type))
				{
					player.getDressMeData().setLegsId(Config.DRESS_ME_ROBE_LEGS.get(type));
				}
				if (Config.DRESS_ME_ROBE_GLOVES.keySet().contains(type))
				{
					player.getDressMeData().setGlovesId(Config.DRESS_ME_ROBE_GLOVES.get(type));
				}
				if (Config.DRESS_ME_ROBE_BOOTS.keySet().contains(type))
				{
					player.getDressMeData().setBootsId(Config.DRESS_ME_ROBE_BOOTS.get(type));
				}
				
				break;
			}
			case "custom":
			{
				if (Config.DRESS_ME_CUSTOM_CHESTS.keySet().contains(type))
				{
					player.getDressMeData().setChestId(Config.DRESS_ME_CUSTOM_CHESTS.get(type));
				}
				if (Config.DRESS_ME_CUSTOM_LEGS.keySet().contains(type))
				{
					player.getDressMeData().setLegsId(Config.DRESS_ME_CUSTOM_LEGS.get(type));
				}
				if (Config.DRESS_ME_CUSTOM_GLOVES.keySet().contains(type))
				{
					player.getDressMeData().setGlovesId(Config.DRESS_ME_CUSTOM_GLOVES.get(type));
				}
				if (Config.DRESS_ME_CUSTOM_BOOTS.keySet().contains(type))
				{
					player.getDressMeData().setBootsId(Config.DRESS_ME_CUSTOM_BOOTS.get(type));
				}
				if (Config.DRESS_ME_CUSTOM_HAIR.keySet().contains(type))
				{
					player.getDressMeData().setHairId(Config.DRESS_ME_CUSTOM_HAIR.get(type));
				}
				
				break;
			}
		}
		
		player.broadcastUserInfo();
		sendEditWindow(player, armor);
	}
	
	public void sendEditWindow(L2PcInstance player, String part)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile("./data/html/mods/dressme/edit.htm");
		htm.replace("%part%", part);
		switch (part)
		{
			case "heavy":
			{
				if (player.getDressMeData() == null)
				{
					htm.replace("%partinfo%", "You have no custom armor.");
				}
				else
				{
					htm.replace("%partinfo%", player.getDressMeData().getChestId() == 0 ? "You have no custom armor." : ItemTable.getInstance().getTemplate(player.getDressMeData().getChestId()).getName());
				}
				String temp = "";
				for (String s : Config.DRESS_ME_HEAVY_CHESTS.keySet())
				{
					temp += s+";";
				}
				htm.replace("%dropboxdata%", temp);
				break;
			}
			case "light":
			{
				if (player.getDressMeData() == null)
				{
					htm.replace("%partinfo%", "You have no custom armor.");
				}
				else
				{
					htm.replace("%partinfo%", player.getDressMeData().getChestId() == 0 ? "You have no custom armor." : ItemTable.getInstance().getTemplate(player.getDressMeData().getChestId()).getName());
				}
				String temp = "";
				for (String s : Config.DRESS_ME_LIGHT_CHESTS.keySet())
				{
					temp += s+";";
				}
				htm.replace("%dropboxdata%", temp);
				break;
			}
			case "robe":
			{
				if (player.getDressMeData() == null)
				{
					htm.replace("%partinfo%", "You have no custom armor.");
				}
				else
				{
					htm.replace("%partinfo%", player.getDressMeData().getChestId() == 0 ? "You have no custom armor." : ItemTable.getInstance().getTemplate(player.getDressMeData().getChestId()).getName());
				}
				String temp = "";
				for (String s : Config.DRESS_ME_ROBE_CHESTS.keySet())
				{
					temp += s+";";
				}
				htm.replace("%dropboxdata%", temp);
				break;
			}
			case "custom":
			{
				if (player.getDressMeData() == null)
				{
					htm.replace("%partinfo%", "You have no custom armor.");
				}
				else
				{
					htm.replace("%partinfo%", player.getDressMeData().getChestId() == 0 ? "You have no custom armor." : ItemTable.getInstance().getTemplate(player.getDressMeData().getChestId()).getName());
				}
				String temp = "";
				for (String s : Config.DRESS_ME_CUSTOM_CHESTS.keySet())
				{
					temp += s+";";
				}
				htm.replace("%dropboxdata%", temp);
				break;
			}
		}
		
		player.sendPacket(htm);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}