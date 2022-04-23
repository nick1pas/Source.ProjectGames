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
import net.sf.l2j.gameserver.instancemanager.custom.PvPZoneManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class VoicedPvpZone implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{ 
		"vote"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("vote"))
			showVoteHtml(activeChar);      
		
		return true;
	}

	private static void showVoteHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		StringBuilder sb = new StringBuilder();

		sb.append("<html><head><title>" + PvPZoneManager.serverName + "</title></head><body><center><br><br><br><br>");

		sb.append("<img src=L2UI.SquareGray width=230 height=1>");
		sb.append("<br1>");
		sb.append("<table width=265 cellpadding=5 bgcolor=000000>");
		sb.append("<tr>");
		sb.append("<td width=45 valign=top align=center><img src=L2PvpZone.pvp_zone width=32 height=32></td>");
		sb.append("<td valign=top><font color=FF6600>" + PvPZoneManager.serverName + " Vote System</font><br1>Vote for next PvPZone Change.</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<br1>");
		sb.append("<img src=L2UI.SquareGray width=230 height=1><br>");

		PvPZoneManager.getInstance().VoteWindows(activeChar.getObjectId(), sb);

		sb.append("<br><br>");
		//sb.append("<center><font color=FF8000>Web Site:</font> www.l2playland.com</center><br1>");
	    //sb.append("<center><font color=FF8000>Facebook Page:</font> facebook.com/l2playland</center>");

		sb.append("</center></body></html>");
		html.setHtml(sb.toString());
		activeChar.sendPacket(html);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}