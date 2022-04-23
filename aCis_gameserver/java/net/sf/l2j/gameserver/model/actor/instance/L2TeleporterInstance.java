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

import java.util.Calendar;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.custom.PvPZoneManager;
import net.sf.l2j.gameserver.model.L2TeleportLocation;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.type.L2ChangePvpZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.Rnd;

/**
 * @author NightMarez
 */
public final class L2TeleporterInstance extends L2NpcInstance
{
	private static final int COND_ALL_FALSE = 0;
	private static final int COND_BUSY_BECAUSE_OF_SIEGE = 1;
	private static final int COND_OWNER = 2;
	private static final int COND_REGULAR = 3;

	public L2TeleporterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		int condition = validateCondition(player);
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		if (actualCommand.equalsIgnoreCase("goto"))
		{
			if (st.countTokens() <= 0)
				return;

			if (condition == COND_REGULAR || condition == COND_OWNER)
			{
				doTeleport(player, Integer.parseInt(st.nextToken()));
				return;
			}
		}
		else if (command.startsWith("Chat"))
		{
			Calendar cal = Calendar.getInstance();
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			
			if (val == 1 && cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7))
			{
				showHalfPriceHtml(player);
				return;
			}
			showChatWindow(player, val);
		}		
		else if (command.startsWith("giran_random_teleport"))
		{
			GiranRandomTeleport(player, this);
		}
		else if (command.startsWith("change_zone"))
		{
			showChangeWindow(player);
		}
		else if (command.startsWith("gotoZone"))
		{
			if (player.getClassId() == ClassId.bishop || player.getClassId() == ClassId.cardinal || player.getClassId() == ClassId.shillenElder || player.getClassId() == ClassId.shillienSaint || player.getClassId() == ClassId.elder || player.getClassId() == ClassId.evaSaint || player.getClassId() == ClassId.prophet || player.getClassId() == ClassId.hierophant)
			{
				player.sendMessage("Your class is not allowed in PvP Zone!");
				return;
			}
			player.teleToLocation(((L2ChangePvpZone) PvPZoneManager.getInstance().getZone()).getSpawnLoc(), 20);
		}
		else if (command.startsWith("voteZone"))
		{
			int playerId = Integer.parseInt(st.nextToken());
			String name = st.nextToken();
			PvPZoneManager.getInstance().setVoteZone(playerId, name);
			showChangeWindow(player);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/teleporter/" + filename + ".htm";
	}
	
	private void showHalfPriceHtml(L2PcInstance player)
	{
		if (player == null)
			return;
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		
		String content = HtmCache.getInstance().getHtm("data/html/teleporter/half/" + getNpcId() + ".htm");
		if (content == null)
			content = HtmCache.getInstance().getHtmForce("data/html/teleporter/" + getNpcId() + "-1.htm");
		
		html.setHtml(content);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		String filename = "data/html/teleporter/castleteleporter-no.htm";
		
		int condition = validateCondition(player);
		if (condition == COND_REGULAR)
		{
			super.showChatWindow(player);
			return;
		}
		else if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
				filename = "data/html/teleporter/castleteleporter-busy.htm"; // Busy because of siege
			else if (condition == COND_OWNER) // Clan owns castle
				filename = getHtmlPath(getNpcId(), 0); // Owner message window
		}
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}

	private void doTeleport(L2PcInstance player, int val)
	{
		L2TeleportLocation list = TeleportLocationTable.getInstance().getTemplate(val);
		if (list != null)
		{
			// you cannot teleport to village that is in siege
			if (SiegeManager.getSiege(list.getLocX(), list.getLocY(), list.getLocZ()) != null && !player.isNoble())
			{
				player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
				return;
			}
			else if (!SiegeManager.getInstance().is_teleport_to_siege_town_allowed() && MapRegionTable.townHasCastleInSiege(list.getLocX(), list.getLocY()) && isInsideZone(ZoneId.TOWN) && !player.isNoble())
			{
				player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
				return;
			}
			else if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0) // karma
			{
				player.sendMessage("Go away, you're not welcome here.");
				return;
			}
			else if (list.getIsForNoble() && !player.isNoble())
			{
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/teleporter/nobleteleporter-no.htm");
				html.replace("%objectId%", getObjectId());
				html.replace("%npcname%", getName());
				player.sendPacket(html);
				return;
			}
			else if (player.isAlikeDead())
				return;
			
			Calendar cal = Calendar.getInstance();
			int price = list.getPrice();
			
			if (!list.getIsForNoble())
			{
				if (cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7))
					price /= 2;
			}

			if ((Config.TIME_TELEPORTER_ENABLE) && (Config.TIME_TELEPORTERS.contains(Integer.valueOf(getTemplate().getNpcId()))))
			{
				ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.TIME_REQUEST_TELEPORTER.getId());
				confirm.addString(getTemplate().getName());
				confirm.addZoneName(list.getLocX(), list.getLocY(), list.getLocZ());
				confirm.addTime(15000);
				confirm.addRequesterId(player.getObjectId());
				int[] cords = { list.getLocX(), list.getLocY(), list.getLocZ() };
				player.TimeTeleporterCoords(cords);
				player.sendPacket(confirm);
			}
			
			else if (Config.ALT_GAME_FREE_TELEPORT || player.destroyItemByItemId("Teleport " + (list.getIsForNoble() ? " nobless" : ""), 57, price, this, true))
			{
				if (Config.DEBUG)
					_log.fine("Teleporting player " + player.getName() + " to new location: " + list.getLocX() + ":" + list.getLocY() + ":" + list.getLocZ());
				
				player.teleToLocation(list.getLocX(), list.getLocY(), list.getLocZ(), 20);
			}
		}
		else
			_log.warning("No teleport destination with id:" + val);
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private int validateCondition(L2PcInstance player)
	{
		if (CastleManager.getInstance().getCastleIndex(this) < 0) // Teleporter isn't on castle ground
			return COND_REGULAR; // Regular access
		else if (getCastle().getSiege().isInProgress()) // Teleporter is on castle ground and siege is in progress
			return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
		else if (player.getClan() != null) // Teleporter is on castle ground and player is in a clan
		{
			if (getCastle().getOwnerId() == player.getClanId()) // Clan owns castle
				return COND_OWNER; // Owner
		}
		
		return COND_ALL_FALSE;
	}
	
	private void showChangeWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><title>" + PvPZoneManager.serverName + " - Gatekeeper</title></head><body><center><br><br><br>");
		
		sb.append("Welcome to <font color=\"FF8000\">" + PvPZoneManager.serverName + "</font> - Gatekeeper<br>");

		PvPZoneManager.getInstance().getMessage(player.getObjectId(), sb);
		
		sb.append("<br><br>");
		sb.append("<font color=\"FF8000\">Special PvP Arenas</font>");
		sb.append("<a action=\"bypass -h npc_%objectId%_goto 12052\">Giran Arena</a><br1>");
		sb.append("<a action=\"bypass -h npc_%objectId%_goto 12051\">Gludin Arena</a><br1>");
		sb.append("<a action=\"bypass -h npc_%objectId%_goto 12053\">Coliseum Arena</a><br1>");
		sb.append("<a action=\"bypass -h npc_%objectId%_goto 12062\">Monster Track Arena</a><br>");
		sb.append("<br1><img src=\"l2ui.squaregray\" width=\"260\" height=\"1s\"><br>");
		sb.append("<a action=\"bypass -h npc_%objectId%_Link teleporter/10001.htm\">Back</a><br1>");
		sb.append("</center></body></html>");
		html.setHtml(sb.toString());
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
	}
	
	public void GiranRandomTeleport(L2PcInstance activeChar, L2Npc npc)
	{
		int x = 83375 + Rnd.get(100);
		int y = 148038 + Rnd.get(100);
		activeChar.teleToLocation(x, y, -3405, 0);
		return;
	}
}