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

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import java.util.StringTokenizer;

public class VoicedClanNotice implements IVoicedCommandHandler
{
	private static String[]  VOICED_COMMANDS = 
	{ 
		"setnotice", 
		"noticeenable", 
		"noticedisable", 
		"setmes" , 
		"clannotice" 
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if(command.equals("clannotice"))
		{
			if (activeChar.getClan() != null)
			{
				if (activeChar.getClan().isNoticeEnabled())
				{
					String getNotice = "data/html/clanNotice.htm";
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					html.setFile(getNotice);
					html.replace("%clan_name%", activeChar.getClan().getName());
					html.replace("%notice_text%", activeChar.getClan().getNotice().replaceAll("\r\n", "<br>").replaceAll("action", "").replaceAll("bypass", ""));
					activeChar.sendPacket(html);
				}
			}
			return true;
		}

		else if(command.equals("setnotice"))
		{
			if (!activeChar.isNoble() || (!activeChar.isClanLeader()))
			{
				activeChar.sendMessage("Only a noble leader can open the notice system.");
				return false;
			}

			else if (activeChar.isNoble() || (activeChar.isClanLeader()))
			{
				mainHtml(activeChar);
				return true;
			}
		}

		else if(command.equals("noticeenable"))
		{
			if(activeChar.isClanLeader())
			{
				activeChar.getClan().setNoticeEnabled(true);
				activeChar.getClan().getClanId();
				activeChar.sendMessage("Clan notice enabled!");
				mainHtml(activeChar);
				return true;
			}
		}

		else if(command.equals("noticedisable"))
		{
			if(activeChar.isClanLeader())
			{
				activeChar.getClan().setNoticeEnabled(false);
				activeChar.getClan().getClanId();
				activeChar.sendMessage("Clan notice disabled!");
				mainHtml(activeChar);
				return true;
			}
		}

		else if(command.startsWith("setmes"))
		{
			if(activeChar.isClanLeader())
			{
				String notice = "";
				StringTokenizer s = new StringTokenizer(command);
				s.nextToken();

				try
				{

					while(s.hasMoreTokens())
						notice = notice + s.nextToken() + " ";
					activeChar.getClan().setNotice(notice);
					mainHtml(activeChar);
					return true;

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
			activeChar.sendMessage("You are not a noble leader of clan");
			return false;
		}

		return true;
	}

	private static String getNoticetxt(L2Clan activeChar)
	{
		String result = "Notice Disabled";
		if (activeChar.isNoticeEnabled())
			result = "Notice Enabled";
		return result;
	}

	public static void mainHtml(L2PcInstance activeChar)
	{
		String htmFile = "data/html/mods/menu/ClanNotice.htm";

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile(htmFile);
		msg.replace("%noticed%", getNoticetxt(activeChar.getClan()));
		msg.replace("%mes%", activeChar.getClan().getNotice());
		activeChar.sendPacket(msg);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}