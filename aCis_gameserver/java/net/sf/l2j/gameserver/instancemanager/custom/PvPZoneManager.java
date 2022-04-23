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
package net.sf.l2j.gameserver.instancemanager.custom;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEvent;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMEvent;
import net.sf.l2j.gameserver.model.entity.events.lastman.LMEvent;
import net.sf.l2j.gameserver.model.entity.events.teamvsteam.TvTEvent;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.type.L2ChangePvpZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.taskmanager.tasks.TaskMessenge;
import net.sf.l2j.gameserver.taskmanager.tasks.TaskZone;
import net.sf.l2j.gameserver.util.Broadcast;

public class PvPZoneManager
{
	private static final Logger _log = Logger.getLogger(PvPZoneManager.class.getName());

	public static int changeZoneTime, announceTimer, voteWindows, voteWindowsRetart;
	public static boolean allowDlgInvite, allowVoteWindow;
	public static String serverName;
	private ScheduledFuture<?> _timer, _leftTime;
	private Map<L2ZoneType, Integer> _zones;
	private L2ZoneType _zone, _newzone;
	private List<Integer> _vote;

	public PvPZoneManager()
	{
		_zones = new ConcurrentHashMap<>();
		_vote = new CopyOnWriteArrayList<>();

		loadConfig();

		for (L2ZoneType zone : ZoneManager.getInstance().getAllZones(L2ChangePvpZone.class))
		{
			_zones.put(zone, 0);
		}
		
		if (allowVoteWindow)
			VoteWindowTimer();

		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new TaskMessenge(), announceTimer * 60 * 1000, (changeZoneTime + announceTimer) * 60 * 1000);
	}

	public void MessengeTask()
	{
		L2ZoneType _newzone = checkZone();
		int time = announceTimer * 60 * 1000;

		Broadcast.ServerAnnounce("PvP Zone will change to " + ((L2ChangePvpZone) _newzone).getAnnouce() + " in " + announceTimer + " minute's");
		Broadcast.ServerAnnounce("You can vote for the next area on .vote command!");
		
		_timer = ThreadPoolManager.getInstance().scheduleGeneral(new TaskZone(), time);
	}

	private void VoteWindowTimer()
	{
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()            
		{               
			@Override                    
			public void run()                    
			{ 
				for (L2PcInstance player : L2World.getInstance().getAllPlayers().values())
				{
					if (player != null && player.isOnline() && !player.inObserverMode() || !player.isInsideZone(ZoneId.FLAG_AREA) || !player.isInsideZone(ZoneId.FLAG_AREA_SELF) || !player.isInsideZone(ZoneId.ZONE_PVP) || !player.isInsideZone(ZoneId.NO_ZERG) || !player.isInsideZone(ZoneId.PARTY_FARM) || !player.isInsideZone(ZoneId.BOSS_AREA) || !player.isInsideZone(ZoneId.SIEGE) || !player.isInsideZone(ZoneId.CASTLE) || !OlympiadManager.getInstance().isRegistered(player) || !CTFEvent.isPlayerParticipant(player.getObjectId()) || !DMEvent.isPlayerParticipant(player.getObjectId()) || !LMEvent.isPlayerParticipant(player.getObjectId()) || !TvTEvent.isPlayerParticipant(player.getObjectId()))      				
						ShowHtml(player);
				}	
				
				ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()            
		        {               
		            @Override                    
		            public void run()                    
		            {  
		            	VoteWindowTimer();
		            }
		         
		        }, voteWindowsRetart * 60 * 1000);
			}

		}, voteWindows * 60 * 1000);
	}
	
	private static void loadConfig()
	{
		final ExProperties pvpZone = new ExProperties();
		try
		{
			pvpZone.load(new File("./config/custom/autopvpzone.properties"));

			changeZoneTime = pvpZone.getProperty("ChangeZoneTime", 60);
			announceTimer = pvpZone.getProperty("AnnounceTimer", 5);
			allowVoteWindow = pvpZone.getProperty("AllowVoteWindow", false);
			voteWindows = pvpZone.getProperty("VoteWindowTimer", 2);
			voteWindowsRetart = pvpZone.getProperty("VoteWindowRestart", 55);
			allowDlgInvite = pvpZone.getProperty("AllowDlgInvite", false);
			serverName = pvpZone.getProperty("ServerName", "L2Server");
		}
		catch (IOException e)
		{
			_log.warning("Config: Error loading \"" + pvpZone + "\" config.");
		}
	}

	public L2ZoneType getZone()
	{
		return _zone;
	}

	public L2ZoneType getNewZone()
	{
		return _newzone;
	}

	public L2ZoneType checkZone()
	{
		L2ZoneType zone = null;
		int vote = 0;

		for (Entry<L2ZoneType, Integer> activeZone : _zones.entrySet())
		{
			if (activeZone.getValue() >= vote)
			{
				zone = activeZone.getKey();
				vote = activeZone.getValue();
			}
		}

		_newzone = zone;
		return zone;
	}

	public void setVoteZone(int player, String name)
	{
		for (Entry<L2ZoneType, Integer> activeZone : _zones.entrySet())
		{
			if (((L2ChangePvpZone) activeZone.getKey()).getName().equals(name))
			{
				int vote = activeZone.getValue() + 1;
				activeZone.setValue(vote);
				break;
			}
		}
		_vote.add(player);
	}

	private static String timeToLeft(ScheduledFuture<?> timer)
	{
		long time = timer.getDelay(TimeUnit.MILLISECONDS);
		return String.format("%d mins, %d sec", TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}

	public StringBuilder getMessage(int player, StringBuilder tb)
	{
		try
		{
			if (getZone() != null)
			{
				tb.append("Current Zone is: <font color=58CC00>" + ((L2ChangePvpZone) getZone()).getName() + "</font><br1>");
				tb.append("<a action=\"bypass -h npc_%objectId%_gotoZone \">Teleport to " + ((L2ChangePvpZone) getZone()).getName() + "</a>");
			}

			if (getNewZone() != null && !getNewZone().equals(getZone()))
			{
				tb.append("<br1>Next Zone is: <font color=58CC00>" + ((L2ChangePvpZone) getNewZone()).getName() + "</font><br1>");
				tb.append("Activated in: <font color=FF4E00>" + timeToLeft(_timer) + "</font> ");
				tb.append("<br1><img src=\"l2ui.squaregray\" width=\"230\" height=\"1s\"><br1>");
			}
			else
			{
				//boolean has = _vote.contains(player);
				tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\">");
				tb.append("<table border=\"0\" width=\"250\" height=\"12\"><tr>");
				tb.append("<td width=\"120\"> Zone's </td><td width=\"120\"> Vote's </td></tr></table>");
				tb.append("<img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\">");
				for (Entry<L2ZoneType, Integer> zone : _zones.entrySet())
				{
					if (!((L2ChangePvpZone) zone.getKey()).isActive())
					{
						String zoneName = ((L2ChangePvpZone) zone.getKey()).getName();
						tb.append("<table border=\"0\" width=\"250\" height=\"12\"><tr>");

						//if (has)
							tb.append("<td width=\"120\">" + zoneName + "</td>");
						//else
						//	tb.append("<td width=\"120\"><a action=\"bypass -h npc_%objectId%_voteZone " + player + " " + zoneName + " \">" + zoneName + "</a></td>");

						tb.append("<td width=\"120\"><font color=FFDF00>" + zone.getValue() + "</font></td></tr></table>");
						tb.append("<img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\">");
					}
				}
				tb.append("<br>Vote Time Left <font color=FF4E00>" + timeToLeft(_leftTime) + "</font>");
			}
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		return tb;
	}

	public void ShowHtml(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		StringBuilder sb = new StringBuilder();

		sb.append("<html><head><title>" + serverName + "</title></head><body><center><br><br><br>");
		sb.append("<img src=\"Kraken.Top\" width=\"220\" height=\"32\"><br><br>");
		sb.append("<font color=LEVEL>Welcome! give your vote for a new PvP Zone!</font>");

		VoteWindows(player.getObjectId(), sb);

		sb.append("</center></body></html>");
		html.setHtml(sb.toString());
		player.sendPacket(html);
	}
	
	public StringBuilder VoteWindows(int player, StringBuilder tb)
	{
		try
		{
			boolean has = _vote.contains(player);
			tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\">");
			for (Entry<L2ZoneType, Integer> zone : _zones.entrySet())
			{
				if (!((L2ChangePvpZone) zone.getKey()).isActive())
				{
					String zoneName = ((L2ChangePvpZone) zone.getKey()).getName();
					tb.append("<table border=\"0\" width=\"250\" height=\"12\"><tr>");

					if (has)
						tb.append("<td width=\"120\">" + zoneName + "</td>");
					else
				    	tb.append("<td width=\"70\"><button value="+ zoneName +" action=\"bypass -h voteZone " + player + " " + zoneName +" \" back=\"buttons_bs.bs_64x27_1\" fore=\"buttons_bs.bs_64x27_2\" width=120 height=18></td>");

					tb.append("<td width=\"40\"><font color=FFDF00>" + zone.getValue() + "</font> - Vote's </td></tr></table>");
					tb.append("<img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\">");
				}
			}
			tb.append("<br>Vote Time Left <font color=FF4E00>" + timeToLeft(_leftTime) + "</font>");
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		return tb;
	}

	public static boolean teleport_pvpzone = false;

	public void ZoneTask()
	{
		teleport_pvpzone = true;

		Broadcast.ServerAnnounce("PvP Zone Changed to " + ((L2ChangePvpZone) _newzone).getAnnouce() + ".");
		Broadcast.ServerAnnounce("Next PvP Zone change in " + changeZoneTime + " minute's!");

		for (Entry<L2ZoneType, Integer> zone : _zones.entrySet())
		{
			zone.setValue(0);
		}
		_vote.clear();

		if (_zone != null)
		{
			((L2ChangePvpZone) _zone).active(false);
		}

		if (_newzone != null)
		{
			((L2ChangePvpZone) _newzone).active(true);

			if (allowDlgInvite)
			{
				for (L2PcInstance players : L2World.getInstance().getAllPlayers().values())
				{
					try
					{
						if (OlympiadManager.getInstance().isRegistered(players) || players.isAlikeDead() || players.isTeleporting() || players.isDead() || players.inObserverMode() || players.isInStoreMode() || players.isInTVTEvent() || players.isInArenaEvent() || players.isArena3x3() || players.isArena5x5() || players.isArena9x9() || players.isInsideZone(ZoneId.CHANGE_PVP) || players.isInsideZone(ZoneId.PARTY_FARM) || players.isInsideZone(ZoneId.NO_ZERG) || players.isInsideZone(ZoneId.CASTLE) || players.isInsideZone(ZoneId.SIEGE) || players.getClassId() == ClassId.bishop || players.getClassId() == ClassId.cardinal || players.getClassId() == ClassId.shillenElder || players.getClassId() == ClassId.shillienSaint || players.getClassId() == ClassId.elder || players.getClassId() == ClassId.evaSaint || players.getClassId() == ClassId.prophet || players.getClassId() == ClassId.hierophant)
						{
							continue;
						}
						else
						{
							ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.EVENT.getId());
							confirm.addString("Do you wish to Teleport to new PvP Zone?");
							confirm.addTime(45000);
							confirm.addRequesterId(players.getObjectId());
							players.sendPacket(confirm);
						}
						players = null;
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			}

			if (_zone != null)
			{
				for (L2Character character : _zone.getCharactersInside())
				{
					if (character instanceof L2Playable)
						character.teleToLocation(((L2SpawnZone) getNewZone()).getSpawnLoc(), 20);
				}
			}
			_zone = _newzone;
		}
	}

	private static class SingletonHolder
	{
		protected static final PvPZoneManager _instance = new PvPZoneManager();
	}

	public static PvPZoneManager getInstance()
	{
		return SingletonHolder._instance;
	}
}