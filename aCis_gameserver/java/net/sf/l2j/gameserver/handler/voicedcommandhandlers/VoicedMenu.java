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
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class VoicedMenu implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{ 
		"menu",
		"auction",
		"info",
		"info_pt",
		"info_sp",
		"exp",
		"setPartyRefuse", 
		"setTradeRefuse", 
		"setMessageRefuse"
	};

	private static final String ACTIVED = "<font color=00FF00>ON</font>";
	private static final String DESATIVED = "<font color=FF0000>OFF</font>";

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("menu"))
			showMenuHtml(activeChar);        

		else if (command.equals("info"))
			showInfoHtml(activeChar);   
		
		else if (command.equals("auction"))
			showAuctionHtml(activeChar);   
		
		else if (command.equals("info_pt"))
			showInfoPtHtml(activeChar);   
		
		else if (command.equals("info_sp"))
			showInfoSpHtml(activeChar);   

		else if (command.equals("exp"))
		{
			if (activeChar.getStopExp())
				activeChar.setStopExp(false);
			else
				activeChar.setStopExp(true);
			showMenuHtml(activeChar);
		}
		else if (command.equals("setPartyRefuse"))
		{
			if (activeChar.isPartyInRefuse())
				activeChar.setIsPartyInRefuse(false);
			else
				activeChar.setIsPartyInRefuse(true);            
			showMenuHtml(activeChar);
		}    
		else if (command.equals("setTradeRefuse"))
		{
			if (activeChar.getTradeRefusal())
				activeChar.setTradeRefusal(false);
			else
				activeChar.setTradeRefusal(true);
			showMenuHtml(activeChar);
		}        
		else if (command.equals("setMessageRefuse"))
		{        
			if (activeChar.isInRefusalMode())
				activeChar.setInRefusalMode(false);
			else
				activeChar.setInRefusalMode(true);
			showMenuHtml(activeChar);
		}
		return true;
	}

	private static void showMenuHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/Menu.htm"); 
		html.replace("%xp%", activeChar.getStopExp() ? ACTIVED : DESATIVED);
		html.replace("%partyRefusal%", activeChar.isPartyInRefuse() ? ACTIVED : DESATIVED);
		html.replace("%tradeRefusal%", activeChar.getTradeRefusal() ? ACTIVED : DESATIVED);
		html.replace("%messageRefusal%", activeChar.isInRefusalMode() ? ACTIVED : DESATIVED);    
		html.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
		html.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
		html.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
		html.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
		activeChar.sendPacket(html);
	}

	private static void showAuctionHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/AuctionerManager.htm"); 
		activeChar.sendPacket(html);
	}
	
	private static void showInfoHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/Info_Server.htm"); 
		html.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
		html.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
		html.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
		html.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
		html.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
		html.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
		html.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
		html.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ADENA));
		html.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
		html.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
		html.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
		html.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
		html.replace("%pet_rate_xp%", String.valueOf(Config.PET_XP_RATE));
		html.replace("%sineater_rate_xp%", String.valueOf(Config.SINEATER_XP_RATE));
		html.replace("%pet_food_rate%", String.valueOf(Config.PET_FOOD_RATE));
		activeChar.sendPacket(html);
	}

	private static void showInfoPtHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/Info_Server.htm"); 
		html.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
		html.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
		html.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
		html.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
		html.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
		html.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
		html.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
		html.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ADENA));
		html.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
		html.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
		html.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
		html.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
		html.replace("%pet_rate_xp%", String.valueOf(Config.PET_XP_RATE));
		html.replace("%sineater_rate_xp%", String.valueOf(Config.SINEATER_XP_RATE));
		html.replace("%pet_food_rate%", String.valueOf(Config.PET_FOOD_RATE));
		activeChar.sendPacket(html);
	}
	
	private static void showInfoSpHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/Info_Server.htm"); 
		html.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
		html.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
		html.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
		html.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
		html.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
		html.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
		html.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
		html.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ADENA));
		html.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
		html.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
		html.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
		html.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
		html.replace("%pet_rate_xp%", String.valueOf(Config.PET_XP_RATE));
		html.replace("%sineater_rate_xp%", String.valueOf(Config.SINEATER_XP_RATE));
		html.replace("%pet_food_rate%", String.valueOf(Config.PET_FOOD_RATE));
		activeChar.sendPacket(html);
	}
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}