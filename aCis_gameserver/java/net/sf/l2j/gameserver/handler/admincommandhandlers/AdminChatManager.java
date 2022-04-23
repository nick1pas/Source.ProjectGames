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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.chathandlers.ChatHeroVoice;
import net.sf.l2j.gameserver.handler.chathandlers.ChatShout;
import net.sf.l2j.gameserver.handler.chathandlers.ChatTrade;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;

public class AdminChatManager implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_quiet"
	};

	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_quiet"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();

			try
			{
				String type = st.nextToken();
				if (type.startsWith("all"))
				{
					if (!Say2.isChatDisabled())
					{
						Say2.setIsChatDisabled(true);
						activeChar.sendMessage("All chats have been disabled!");
					}
					else
					{
						Say2.setIsChatDisabled(false);
						activeChar.sendMessage("All Chats have been enabled!");
					}
				}
				else if (type.startsWith("hero"))
				{
					if (!ChatHeroVoice.isChatDisabled())
					{
						ChatHeroVoice.setIsChatDisabled(true);
						activeChar.sendMessage("Hero Voice has been disabled!");
					}
					else
					{
						ChatHeroVoice.setIsChatDisabled(false);
						activeChar.sendMessage("Hero Voice has been enabled!");
					}
				}
				else if (type.startsWith("trade"))
				{
					if (!ChatTrade.isChatDisabled())
					{
						ChatTrade.setIsChatDisabled(true);
						activeChar.sendMessage("Trade Chat has been disabled!");
					}
					else
					{
						ChatTrade.setIsChatDisabled(false);
						activeChar.sendMessage("Trade Chat has been enabled!");
					}
				}
				else if (type.startsWith("global"))
				{
					if (!ChatShout.isChatDisabled())
					{
						ChatShout.setIsChatDisabled(true);
						activeChar.sendMessage("Global Chat has been disabled!");
					}
					else
					{
						ChatShout.setIsChatDisabled(false);
						activeChar.sendMessage("Global Chat has been enabled!");
					}
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage : //quiet <all|hero|trade|global>");
			}
		}
		return true;
	}

	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}