package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

public class VoicedBossSpawn implements IVoicedCommandHandler
{
	private static Logger _log = Logger.getLogger(VoicedBossSpawn.class.getName());

	private static final String[] VOICED_COMMANDS = 
	{ 
		"raidinfo"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("raidinfo")) 
			showInfoPage(activeChar);
		
		return true;
	}
	
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private static void showInfoPage(L2PcInstance activeChar)
	{
		StringBuilder tb = new StringBuilder();
		
		tb.append("<html>");
	    tb.append("<title>Raid Boss Spawn Info</title>");
	    tb.append("<body>");
	    tb.append("<center>");
	    tb.append("<br><br><br>");
	    tb.append("<center><img src=\"L2UI.SquareGray\" width=270 height=1></center>");
	    tb.append("<center><table width=\"256\">");

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
				
                tb.append("<tr>");
                tb.append((new StringBuilder()).append("<td width=\"146\" align=\"left\">").append(name).append("</td>").toString());
                tb.append("<td width=\"110\" align=\"right\"><font color=\"9CC300\">Alive</font></td>");
                tb.append("</tr>");
			}
			else
			{
                tb.append("<tr>");
                tb.append((new StringBuilder()).append("<td width=\"146\" align=\"left\">").append(name).append("</td>").toString());
	            tb.append("<td width=\"110\" align=\"right\"><font color=\"FB5858\">Dead</font> " + new SimpleDateFormat(Config.RAID_BOSS_DATE_FORMAT).format(new Date(delay)) + "</td>");
				tb.append("</tr>");
			}
		}
        
		tb.append("</table>");
		tb.append("<center><img src=\"L2UI.SquareGray\" width=270 height=1></center>");
		tb.append("<br>");
        tb.append("<center><a action=\"bypass voiced_menu\">Back</a></center>");
        tb.append("</body></html>");
        NpcHtmlMessage msg = new NpcHtmlMessage(1);
        msg.setHtml(tb.toString());
        activeChar.sendPacket(msg);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}