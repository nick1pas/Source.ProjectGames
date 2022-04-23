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

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * A chat handler
 * @author durgus
 */
public class ChatAll implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	/**
	 * Handle chat type 'all'
	 * @see net.sf.l2j.gameserver.handler.IChatHandler#handleChat(int, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance, java.lang.String, java.lang.String)
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String params, String text)
	{
        boolean vcd_used = false;
        if (text.startsWith("."))
        {
            StringTokenizer st = new StringTokenizer(text);
            IVoicedCommandHandler vch;
            String command = "";
		
            if (st.countTokens() > 1)
            {
                command = st.nextToken().substring(1);
                params = text.substring(command.length() + 2);
                vch = VoicedCommandHandler.getInstance().getHandler(command);
            }
            else
            {
                command = text.substring(1);
                vch = VoicedCommandHandler.getInstance().getHandler(command);
            }
                    
            if (vch != null)
            {
                vch.useVoicedCommand(command, activeChar, params);
                vcd_used = true;
            }
        }
        if (!vcd_used)
		{
        	if ((activeChar.getChatAllTimer() - 1500) > System.currentTimeMillis() && Config.TALK_CHAT_ALL_CONFIG)
			{
				if (((activeChar.getChatAllTimer() - System.currentTimeMillis()) / 1000) >= 60)
				{
					activeChar.sendMessage("You need " + Config.TALK_CHAT_ALL_TIME + " minutes online to talk on chat. Finish in " + (activeChar.getChatAllTimer() - System.currentTimeMillis()) / (1000 * 60) + " minute(s).");
					activeChar.sendPacket(new ExShowScreenMessage("You can talk in " + (activeChar.getChatAllTimer() - System.currentTimeMillis()) / (1000 * 60) + " minute(s)", 5 * 1000));
				}
				else
				{
					activeChar.sendMessage("You need " + Config.TALK_CHAT_ALL_TIME + " minutes online to talk on chat. Finish in " + (activeChar.getChatAllTimer() - System.currentTimeMillis()) / 1000 + " second(s).");
					activeChar.sendPacket(new ExShowScreenMessage("You can talk in " + (activeChar.getChatAllTimer() - System.currentTimeMillis()) / 1000 + " second(s)", 5 * 1000));
				}
				return;
			}
        	
            CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
            
            for (L2PcInstance player : activeChar.getKnownList().getKnownTypeInRadius(L2PcInstance.class, 1250))
            {
                if (!BlockList.isBlocked(player, activeChar))
                    player.sendPacket(cs);
            }
            
            activeChar.sendPacket(cs);
        }  
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