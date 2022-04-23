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
package net.sf.l2j.gameserver.handler.chathandlers;

import java.util.Collection;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

/**
 * A chat handler
 * @author durgus
 */
public class ChatShout implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		1
	};
	
	private static boolean _chatDisabled = false;
	
	/**
	 * Handle chat type 'shout'
	 * @see net.sf.l2j.gameserver.handler.IChatHandler#handleChat(int, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance, java.lang.String, java.lang.String)
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (isChatDisabled() && !activeChar.isGM())
		{
			activeChar.sendPacket(SystemMessageId.GM_NOTICE_CHAT_DISABLED);
			return;
		}
			
		if (Config.CHAT_SHOUT_NEED_PVPS)
		{
             if (activeChar.getPvpKills() < Config.PVPS_TO_USE_CHAT_SHOUT)
             {
                 activeChar.sendMessage("You must have at least " + Config.PVPS_TO_USE_CHAT_SHOUT + " PvP to speak in the shout chat.");
                 return;
             }
		}
		
		if (!activeChar.isGM() && !activeChar.getFloodProtectors().getGlobalChat().tryPerformAction("globalChat"))
		{
			activeChar.sendMessage("You must wait " + (activeChar.getFloodProtectors().getGlobalChat().getNextGameTick() - GameTimeController.getInstance().getGameTicks()) / 10 + " seconds to use global chat.");
			return;
		}
		
        CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
        
        Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();
                       
        if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("gm") && activeChar.isGM()))
        {
        	int region = MapRegionTable.getMapRegion(activeChar.getX(), activeChar.getY());
        	{
        		for (L2PcInstance player : pls)
        		{
        			if (!BlockList.isBlocked(player, activeChar))
        				if (region == MapRegionTable.getMapRegion(player.getX(), player.getY()))
        					player.sendPacket(cs);
        		}
        	}
        }
        else if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("global"))
        {
        	for (L2PcInstance player : pls)
        	{
        		if (!BlockList.isBlocked(player, activeChar))
        			player.sendPacket(cs);
        	}
        }
	}
	
 	/**
	 * @return Returns the chatDisabled.
	 */
	public static boolean isChatDisabled()
	{
		return _chatDisabled;
	}

	/**
	 * @param chatDisabled The chatDisabled to set.
	 */
	public static void setIsChatDisabled(boolean chatDisabled)
	{
		_chatDisabled = chatDisabled;
	}
	
	/**
	 * Returns the chat types registered to this handler
	 * @see net.sf.l2j.gameserver.handler.IChatHandler#getChatTypeList()
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}