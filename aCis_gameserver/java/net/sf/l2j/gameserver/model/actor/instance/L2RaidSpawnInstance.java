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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2RaidSpawnInstance extends L2NpcInstance
{	
	public L2RaidSpawnInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("raid_info"))
		{
			showSpawnInfo(player);   
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	public void showChatWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/mods/raidspawner/Main.htm");
		html.replace("%objectId%", String.valueOf(player.getTargetId()));
		player.sendPacket(html);
	}

	private static void showSpawnInfo(L2PcInstance activeChar)
	{
		StringBuilder tb = new StringBuilder();
		tb.append("<html><title>Raid Info</title><body><center>");
		tb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>");
		tb.append("<font color=\"FF6600\">Epic's Boss respawn time</font><br>");
		tb.append("<img src=\"L2UI.SquareGray\" width=230 height=1><br>");

		for(int boss : Config.RAID_INFO_IDS_LIST)
		{
			String name = "";
			NpcTemplate template = null;
			if((template = NpcTable.getInstance().getTemplate(boss)) != null)
			{
				name = template.getName();
			}
			else
			{
				_log.warning("Raid Info: Raid Boss with ID " + boss + " is not defined into NpcXml");
				continue;
			}

			StatsSet actual_boss_stat = null;
			GrandBossManager.getInstance().getStatsSet(boss);
			long delay = 0;

			if (NpcTable.getInstance().getTemplate(boss).isType("L2RaidBoss"))
			{
				actual_boss_stat = RaidBossSpawnManager.getInstance().getStatsSet(boss);
				if (actual_boss_stat!=null)
					delay = actual_boss_stat.getLong("respawnTime");
			}
			else if (NpcTable.getInstance().getTemplate(boss).isType("L2GrandBoss"))
			{
				actual_boss_stat = GrandBossManager.getInstance().getStatsSet(boss);
				if (actual_boss_stat!=null)
					delay = actual_boss_stat.getLong("respawn_time");
			}
			else
				continue;

			if (delay <= System.currentTimeMillis())
			{
				tb.append("<font color=\"00C3FF\">" + name + "</font>: " + "<font color=\"9CC300\">Is Alive</font>"+"<br1>");
			}
			else
			{
				int hours = (int) ((delay - System.currentTimeMillis()) / 1000 / 60 / 60);
				int mins = (int) (((delay - (hours * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000 / 60);
				int seconts = (int) (((delay - ((hours * 60 * 60 * 1000) + (mins * 60 * 1000))) - System.currentTimeMillis()) / 1000);
				tb.append("<font color=\"FF8100\">" + name + "</font>" + "<font color=\"FFFFFF\">" +" " + "Respawn in :</font>" + " " + " <font color=\"FD0000\">" + hours + " : " + mins + " : " + seconts + "</font><br1>");
			}
		}

		tb.append("<img src=\"L2UI.SquareGray\" width=230 height=1><br>");
		tb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32>");
		tb.append("</center></body></html>");

		NpcHtmlMessage msg = new NpcHtmlMessage(1);
		msg.setHtml(tb.toString());

		activeChar.sendPacket(msg);
	}
}