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
package net.sf.l2j.gameserver.handler.itemhandlers.custom.special;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.custom.AchievementsManager;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Achievement;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class AchievementsBook implements IItemHandler
{
	private boolean first = true;
	
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;
		
		L2PcInstance activeChar = (L2PcInstance) playable;

		activeChar.getAchievemntData();
		showChatWindow(activeChar);
	}
	
	public void showChatWindow(L2PcInstance activeChar)
	{
		if (first)
		{
			AchievementsManager.getInstance().loadUsed();
			first = false;
		}

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		StringBuilder tb = new StringBuilder();
		
		tb.append("<html><title>Achievements Book</title><body><br><center>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		tb.append("<table width=\"265\" cellpadding=\"5\" bgcolor=\"000000\"><tr>");
		tb.append("<td width=\"45\" valign=\"top\" align=\"center\"><img src=\"L2PvpZone.achivement_book\" width=\"32\" height=\"32\"></td>");
		tb.append("<td valign=\"top\"><font color=\"FF6600\">Character Achievements</font><br1>Beat your goals and collect trophies.</td></tr></table><br1>");

		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		if (AchievementsManager.getInstance().getAchievementList().isEmpty())
		{
			tb.append("There are no Achievements created yet!");
		}
		else
		{
			int i = 0;

			for (Achievement a: AchievementsManager.getInstance().getAchievementList().values())
			{
				tb.append(getTableColor(i));
				tb.append("<tr><td width=270 align=\"left\">" + a.getName() + "</td><td width=50 align=\"right\"><a action=\"bypass -h achievementInfo "
						+ a.getID() + "\">info</a></td><td width=200 align=\"center\">" + getStatusString(a.getID(), activeChar) + "</td></tr></table>");
				i++;
			}

			tb.append("<br><img src=\"l2ui.squaregray\" width=\"230\" height=\"1\">");
		}
		
		tb.append("</body></html>");

		msg.setHtml(tb.toString());
		activeChar.sendPacket(msg);
	}
	
	private static String getStatusString(int achievementID, L2PcInstance player)
	{
		if (player.getCompletedAchievements().contains(achievementID))
		{
			return "<font color=\"5EA82E\">Completed</font>";
		}

		if (AchievementsManager.getInstance().getAchievementList().get(achievementID).meetAchievementRequirements(player))
		{
			return "<font color=\"LEVEL\">Get Reward</font>";
		}

		return "<font color=\"FF0000\">Not Completed</font>";
	}

	private static String getTableColor(int i)
	{
		if (i % 2 == 0)
			return "<table width=265 border=0 bgcolor=\"000000\">";

		return "<table width=265 border=0>";
	}
}