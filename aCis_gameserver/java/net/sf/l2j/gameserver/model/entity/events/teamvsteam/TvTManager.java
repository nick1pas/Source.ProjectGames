/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.entity.events.teamvsteam;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.util.Broadcast;

public class TvTManager
{
	protected static final Logger _log = Logger.getLogger(TvTManager.class.getName());
	
	/** Task for event cycles<br> */
	private TvTStartTask _task;
	
	/**
	 * New instance only by getInstance()<br>
	 */
	protected TvTManager()
	{
		if (TvTConfig.TVT_EVENT_ENABLED)
		{
			TvTEvent.init();
			
			scheduleEventStart();
			_log.info("Team vs Team Engine: is Started.");
		}
		else
		{
			_log.info("Team vs Team Engine: is disabled.");
		}
	}
	
	/**
	 * Initialize new/Returns the one and only instance<br>
	 * <br>
	 * @return TvTManager<br>
	 */
	public static TvTManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * Starts TvTStartTask
	 */
	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			for (String timeOfDay : TvTConfig.TVT_EVENT_INTERVAL)
			{
				// Creating a Calendar object from the specified interval value
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				// If the date is in the past, make it the next day (Example: Checking for "1:00", when the time is 23:57.)
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				// Check for the test date to be the minimum (smallest in the specified list)
				if ((nextStartTime == null) || (testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis()))
				{
					nextStartTime = testStartTime;
				}
			}
			if (nextStartTime != null)
			{
				_task = new TvTStartTask(nextStartTime.getTimeInMillis());
				ThreadPoolManager.getInstance().executeTask(_task);
			}
		}
		catch (Exception e)
		{
			_log.warning("TvTEventEngine: Error figuring out a start time. Check TvTEventInterval in config file.");
		}
	}
	
	public static boolean join_tvt = false;
	
	/**
	 * Method to start participation
	 */
	public void startReg()
	{
		join_tvt = true;
		
		if (!TvTEvent.startParticipation())
		{
			Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: Event was cancelled.");
			_log.warning("TvTEventEngine: Error spawning event npc for participation.");
			
			scheduleEventStart();
		}
		else
		{
			Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: Joinable in " + TvTConfig.TVT_NPC_LOC_NAME + "!");
			
			if (Config.ALLOW_EVENT_COMMANDS)
			     Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: Command: .tvtjoin / .tvtleave / .tvtinfo");
			
			if (TvTConfig.ALLOW_TVT_DLG)
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
							ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.TVT.getId());
							confirm.addString("Do you wish to Join TvT Event?");
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
			
			// schedule registration end
			_task.setStartTime(System.currentTimeMillis() + (60000L * TvTConfig.TVT_EVENT_PARTICIPATION_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to start the fight
	 */
	public void startEvent()
	{
		if (!TvTEvent.startFight())
		{
			Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: Event cancelled due to lack of Participation.");
			_log.info("TvTEventEngine: Lack of registration, abort event.");
			
			scheduleEventStart();
		}
		else
		{
			TvTEvent.sysMsgToAllParticipants("Teleporting participants in " + TvTConfig.TVT_EVENT_START_LEAVE_TELEPORT_DELAY + " second(s).");
			_task.setStartTime(System.currentTimeMillis() + (60000L * TvTConfig.TVT_EVENT_RUNNING_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to end the event and reward
	 */
	public void endEvent()
	{
		Broadcast.gameAnnounceToOnlinePlayers(TvTEvent.calculateRewards());
		TvTEvent.sysMsgToAllParticipants("Teleporting back to town in " + TvTConfig.TVT_EVENT_START_LEAVE_TELEPORT_DELAY + " second(s).");
		TvTEvent.stopFight();
		
		scheduleEventStart();
	}
	
	public void skipDelay()
	{
		if (_task.nextRun.cancel(false))
		{
			_task.setStartTime(System.currentTimeMillis());
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Class for TvT cycles
	 */
	class TvTStartTask implements Runnable
	{
		private long _startTime;
		public ScheduledFuture<?> nextRun;
		
		public TvTStartTask(long startTime)
		{
			_startTime = startTime;
		}
		
		public void setStartTime(long startTime)
		{
			_startTime = startTime;
		}
		
		@Override
		public void run()
		{
			int delay = (int) Math.round((_startTime - System.currentTimeMillis()) / 1000.0);
			
			if (delay > 0)
			{
				announce(delay);
			}
			
			int nextMsg = 0;
			if (delay > 3600)
			{
				nextMsg = delay - 3600;
			}
			else if (delay > 1800)
			{
				nextMsg = delay - 1800;
			}
			else if (delay > 900)
			{
				nextMsg = delay - 900;
			}
			else if (delay > 600)
			{
				nextMsg = delay - 600;
			}
			else if (delay > 300)
			{
				nextMsg = delay - 300;
			}
			else if (delay > 60)
			{
				nextMsg = delay - 60;
			}
			else if (delay > 5)
			{
				nextMsg = delay - 5;
			}
			else if (delay > 0)
			{
				nextMsg = delay;
			}
			else
			{
				// start
				if (TvTEvent.isInactive())
				{
					startReg();
				}
				else if (TvTEvent.isParticipating())
				{
					startEvent();
				}
				else
				{
					endEvent();
				}
			}
			
			if (delay > 0)
			{
				nextRun = ThreadPoolManager.getInstance().scheduleGeneral(this, nextMsg * 1000);
			}
		}
		
		private void announce(long time)
		{
			if ((time >= 3600) && ((time % 3600) == 0))
			{
				if (TvTEvent.isParticipating())
				{
					Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: " + (time / 60 / 60) + " hour(s) until registration is closed!");
				}
				else if (TvTEvent.isStarted())
				{
					TvTEvent.sysMsgToAllParticipants("" + (time / 60 / 60) + " hour(s) until event is finished!");
				}
			}
			else if (time >= 60)
			{
				if (TvTEvent.isParticipating())
				{
					Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: " + (time / 60) + " minute(s) until registration is closed!");
				}
				else if (TvTEvent.isStarted())
				{
					TvTEvent.sysMsgToAllParticipants("" + (time / 60) + " minute(s) until the event is finished!");
				}
			}
			else
			{
				if (TvTEvent.isParticipating())
				{
					Broadcast.gameAnnounceToOnlinePlayers("Team vs Team: " + time + " second(s) until registration is closed!");
				}
				else if (TvTEvent.isStarted())
				{
					TvTEvent.sysMsgToAllParticipants("" + time + " second(s) until the event is finished!");
				}
			}
		}
	}
	
	private static class SingletonHolder
	{
		protected static final TvTManager _instance = new TvTManager();
	}
}
