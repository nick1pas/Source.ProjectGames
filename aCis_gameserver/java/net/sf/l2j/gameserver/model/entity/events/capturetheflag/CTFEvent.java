/*
 * Copyright (C) 2004-2015 L2J Server
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
package net.sf.l2j.gameserver.model.entity.events.capturetheflag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.custom.IPManager;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Party.MessageType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.events.teamvsteam.TvTConfig;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.util.StringUtil;

/**
 * @author HorridoJoho
 */
public class CTFEvent
{
	enum EventState
	{
		INACTIVE,
		INACTIVATING,
		PARTICIPATING,
		STARTING,
		STARTED,
		REWARDING
	}
	
	protected static final Logger _log = Logger.getLogger(CTFEvent.class.getName());
	/** html path **/
	private static final String htmlPath = "data/html/mods/events/ctf/";
	/** The teams of the CTFEvent<br> */
	private static CTFEventTeam[] _teams = new CTFEventTeam[2];
	/** The state of the CTFEvent<br> */
	private static EventState _state = EventState.INACTIVE;
	/** The spawn of the participation npc<br> */
	private static L2Spawn _npcSpawn = null;
	/** the npc instance of the participation npc<br> */
	private static L2Npc _lastNpcSpawn = null;
	/** The spawn of Team1 flag<br> */
	private static L2Spawn _flag1Spawn = null;
	/** the npc instance Team1 flag<br> */
	private static L2Npc _lastFlag1Spawn = null;
	/** The spawn of Team2 flag<br> */
	private static L2Spawn _flag2Spawn = null;
	/** the npc instance of Team2 flag<br> */
	private static L2Npc _lastFlag2Spawn = null;
	/** the Team 1 flag carrier L2PcInstance<br> */
	private static L2PcInstance _team1Carrier = null;
	/** the Team 2 flag carrier L2PcInstance<br> */
	private static L2PcInstance _team2Carrier = null;
	/** the Team 1 flag carrier right hand item<br> */
	private static ItemInstance _team1CarrierRHand = null;
	/** the Team 2 flag carrier right hand item<br> */
	private static ItemInstance _team2CarrierRHand = null;
	/** the Team 1 flag carrier left hand item<br> */
	private static ItemInstance _team1CarrierLHand = null;
	/** the Team 2 flag carrier left hand item<br> */
	private static ItemInstance _team2CarrierLHand = null;
	
	/**
	 * No instance of this class!<br>
	 */
	private CTFEvent()
	{
	}
	
	/**
	 * Teams initializing<br>
	 */
	public static void init()
	{
		_teams[0] = new CTFEventTeam(CTFConfig.CTF_EVENT_TEAM_1_NAME, CTFConfig.CTF_EVENT_TEAM_1_COORDINATES);
		_teams[1] = new CTFEventTeam(CTFConfig.CTF_EVENT_TEAM_2_NAME,CTFConfig.CTF_EVENT_TEAM_2_COORDINATES);
	}
	
	/**
	 * Starts the participation of the CTFEvent<br>
	 * 1. Get L2NpcTemplate by CTFConfig.CTF_EVENT_PARTICIPATION_NPC_ID<br>
	 * 2. Try to spawn a new npc of it<br>
	 * <br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startParticipation()
	{
		NpcTemplate tmpl = NpcTable.getInstance().getTemplate(CTFConfig.CTF_EVENT_PARTICIPATION_NPC_ID);
		
		if (tmpl == null)
		{
			_log.warning("CTFEventEngine: L2EventManager is a NullPointer -> Invalid npc id in configs?");
			return false;
		}
		
		try
		{
			_npcSpawn = new L2Spawn(tmpl);
			
			_npcSpawn.setLocx(CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[0]);
			_npcSpawn.setLocy(CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[1]);
			_npcSpawn.setLocz(CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[2]);
			_npcSpawn.getAmount();
			_npcSpawn.setHeading(CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[3]);
			_npcSpawn.setRespawnDelay(1);
			// later no need to delete spawn from db, we don't store it (false)
			SpawnTable.getInstance().addNewSpawn(_npcSpawn, false);
			_npcSpawn.init();
			_lastNpcSpawn = _npcSpawn.getLastSpawn();
			_lastNpcSpawn.setCurrentHp(_lastNpcSpawn.getMaxHp());
			_lastNpcSpawn.setTitle("CTF Event");
			_lastNpcSpawn.isAggressive();
			_lastNpcSpawn.decayMe();
			_lastNpcSpawn.spawnMe(_npcSpawn.getLastSpawn().getX(), _npcSpawn.getLastSpawn().getY(), _npcSpawn.getLastSpawn().getZ());
			_lastNpcSpawn.broadcastPacket(new MagicSkillUse(_lastNpcSpawn, _lastNpcSpawn, 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "CTFEventEngine: exception: " + e.getMessage(), e);
			return false;
		}

		setState(EventState.PARTICIPATING);
		return true;
	}
	
	private static int highestLevelPcInstanceOf(Map<Integer, L2PcInstance> players)
	{
		int maxLevel = Integer.MIN_VALUE, maxLevelId = -1;
		for (L2PcInstance player : players.values())
		{
			if (player.getLevel() >= maxLevel)
			{
				maxLevel = player.getLevel();
				maxLevelId = player.getObjectId();
			}
		}
		return maxLevelId;
	}
	
	/**
	 * Starts the CTFEvent fight<br>
	 * 1. Set state EventState.STARTING<br>
	 * 2. Close doors specified in CTFConfigs<br>
	 * 3. Abort if not enought participants(return false)<br>
	 * 4. Set state EventState.STARTED<br>
	 * 5. Teleport all participants to team spot<br>
	 * <br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startFight()
	{
		// Set state to STARTING
		setState(EventState.STARTING);
		
		// Randomize and balance team distribution
		Map<Integer, L2PcInstance> allParticipants = new HashMap<>();
		allParticipants.putAll(_teams[0].getParticipatedPlayers());
		allParticipants.putAll(_teams[1].getParticipatedPlayers());
		_teams[0].cleanMe();
		_teams[1].cleanMe();
		
		L2PcInstance player;
		Iterator<L2PcInstance> iter;
		if (needParticipationFee())
		{
			iter = allParticipants.values().iterator();
			while (iter.hasNext())
			{
				player = iter.next();
				if (!hasParticipationFee(player))
				{
					iter.remove();
				}
			}
		}
		
		int balance[] =
		{
			0,
			0
		}, priority = 0, highestLevelPlayerId;
		L2PcInstance highestLevelPlayer;
		// TODO: allParticipants should be sorted by level instead of using highestLevelPcInstanceOf for every fetch
		while (!allParticipants.isEmpty())
		{
			// Priority team gets one player
			highestLevelPlayerId = highestLevelPcInstanceOf(allParticipants);
			highestLevelPlayer = allParticipants.get(highestLevelPlayerId);
			allParticipants.remove(highestLevelPlayerId);
			_teams[priority].addPlayer(highestLevelPlayer);
			balance[priority] += highestLevelPlayer.getLevel();
			// Exiting if no more players
			if (allParticipants.isEmpty())
			{
				break;
			}
			// The other team gets one player
			// TODO: Code not dry
			priority = 1 - priority;
			highestLevelPlayerId = highestLevelPcInstanceOf(allParticipants);
			highestLevelPlayer = allParticipants.get(highestLevelPlayerId);
			allParticipants.remove(highestLevelPlayerId);
			_teams[priority].addPlayer(highestLevelPlayer);
			balance[priority] += highestLevelPlayer.getLevel();
			// Recalculating priority
			priority = balance[0] > balance[1] ? 1 : 0;
		}
		
		// Check for enought participants
		if ((_teams[0].getParticipatedPlayerCount() < CTFConfig.CTF_EVENT_MIN_PLAYERS_IN_TEAMS) || (_teams[1].getParticipatedPlayerCount() < CTFConfig.CTF_EVENT_MIN_PLAYERS_IN_TEAMS))
		{
			// Set state INACTIVE
			setState(EventState.INACTIVE);
			// Cleanup of teams
			_teams[0].cleanMe();
			_teams[1].cleanMe();
			// Unspawn the event NPC
			unSpawnNpc();
			return false;
		}
		
		if (needParticipationFee())
		{
			iter = _teams[0].getParticipatedPlayers().values().iterator();
			while (iter.hasNext())
			{
				player = iter.next();
				if (!payParticipationFee(player))
				{
					iter.remove();
				}
			}
			iter = _teams[1].getParticipatedPlayers().values().iterator();
			while (iter.hasNext())
			{
				player = iter.next();
				if (!payParticipationFee(player))
				{
					iter.remove();
				}
			}
		}
		
		// Spawn Flag Quarters
		SpawnFirstHeadQuarters();
		SpawnSecondHeadQuarters();
		
		// Closes all doors specified in CTFConfigs for CTF
		closeDoors(CTFConfig.CTF_DOORS_IDS_TO_CLOSE);
		// Set state STARTED
		setState(EventState.STARTED);
		
		// Iterate over all teams
		for (CTFEventTeam team : _teams)
		{
			// Iterate over all participated player instances in this team
			for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
			{
				if (playerInstance != null)
				{
					// Teleporter implements Runnable and starts itself
					new CTFEventTeleporter(playerInstance, team.getCoordinates(), false, false);
				}
			}
		}

		return true;
	}
	
	/**
	 * Calculates the CTFEvent reward<br>
	 * 1. If both teams are at a tie(points equals), send it as system message to all participants, if one of the teams have 0 participants left online abort rewarding<br>
	 * 2. Wait till teams are not at a tie anymore<br>
	 * 3. Set state EvcentState.REWARDING<br>
	 * 4. Reward team with more points<br>
	 * 5. Show win html to wining team participants<br>
	 * <br>
	 * @return String: winning team name<br>
	 */
	public static String calculateRewards()
	{
		if (_teams[0].getPoints() == _teams[1].getPoints())
		{
			// Check if one of the teams have no more players left
			if ((_teams[0].getParticipatedPlayerCount() == 0) || (_teams[1].getParticipatedPlayerCount() == 0))
			{
				// set state to rewarding
				setState(EventState.REWARDING);
				return "CTF Event: Event has ended. No team won due to inactivity!";
			}
			
			sysMsgToAllParticipants("Event has ended, both teams have tied.");
			if (CTFConfig.CTF_REWARD_TEAM_TIE)
			{
				rewardTeam(_teams[0]);
				rewardTeam(_teams[1]);
				return "CTF Event: Event has ended with both teams tying.";
			}
			return "CTF Event: Event has ended with both teams tying.";
		}
		
		// Set state REWARDING so nobody can point anymore
		setState(EventState.REWARDING);
		
		// Get team which has more points
		CTFEventTeam team = _teams[_teams[0].getPoints() > _teams[1].getPoints() ? 0 : 1];
		rewardTeam(team);
		
		return "CTF Event: Event finish. Team " + team.getName() + " won with " + team.getPoints() + " points.";
	}
	
	private static void rewardTeam(CTFEventTeam team)
	{
		// Iterate over all participated player instances of the winning team
		for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
		{
			// Check for nullpointer
			if (playerInstance == null)
				continue;
			
			SystemMessage systemMessage = null;
			
			// Iterate over all CTF event rewards
			for (int[] reward : CTFConfig.CTF_EVENT_REWARDS)
			{
				PcInventory inv = playerInstance.getInventory();
				
				// Check for stackable item, non stackabe items need to be added one by one
				if (ItemTable.getInstance().getTemplate(reward[0]).isStackable())
				{
					inv.addItem("CTF Event", reward[0], reward[1], playerInstance, playerInstance);
					
					if (reward[1] > 1)
					{
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
						systemMessage.addItemName(reward[0]);
						systemMessage.addItemNumber(reward[1]);
					}
					else
					{
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
						systemMessage.addItemName(reward[0]);
					}
					
					playerInstance.sendPacket(systemMessage);
				}
				else
				{
					for (int i = 0; i < reward[1]; ++i)
					{
						inv.addItem("CTF Event", reward[0], 1, playerInstance, playerInstance);
						systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
						systemMessage.addItemName(reward[0]);
						playerInstance.sendPacket(systemMessage);
					}
				}
			}
			
			StatusUpdate statusUpdate = new StatusUpdate(playerInstance);
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
			
			statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, playerInstance.getCurrentLoad());
			npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Reward.htm"));
			playerInstance.sendPacket(statusUpdate);
			playerInstance.sendPacket(npcHtmlMessage);
		}
	}
	
	/**
	 * Stops the CTFEvent fight<br>
	 * 1. Set state EventState.INACTIVATING<br>
	 * 2. Remove CTF npc from world<br>
	 * 3. Open doors specified in CTFConfigs<br>
	 * 4. Teleport all participants back to participation npc location<br>
	 * 5. Teams cleaning<br>
	 * 6. Set state EventState.INACTIVE<br>
	 */
	public static void stopFight()
	{
		// Set state INACTIVATING
		setState(EventState.INACTIVATING);
		// Unspawn event npc
		unSpawnNpc();
		// Opens all doors specified in CTFConfigs for CTF
		openDoors(CTFConfig.CTF_DOORS_IDS_TO_CLOSE);
		// Closes all doors specified in CTFConfigs for CTF
		closeDoors(CTFConfig.CTF_DOORS_IDS_TO_OPEN);
		
		// Reset flag carriers
		if (_team1Carrier != null)
		{
			removeFlagCarrier(_team1Carrier);
		}
		
		if (_team2Carrier != null)
		{
			removeFlagCarrier(_team2Carrier);
		}
		
		// Iterate over all teams
		for (CTFEventTeam team : _teams)
		{
			for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
			{
				// Check for nullpointer
				if (playerInstance != null)
				{
					
					// Teleport back.
					new CTFEventTeleporter(playerInstance, CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES, false, false);
				}
			}
		}
		
		// Cleanup of teams
		_teams[0].cleanMe();
		_teams[1].cleanMe();
		// Set state INACTIVE
		setState(EventState.INACTIVE);
	}
	
	/**
	 * Adds a player to a CTFEvent team<br>
	 * 1. Calculate the id of the team in which the player should be added<br>
	 * 2. Add the player to the calculated team<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static synchronized boolean addParticipant(L2PcInstance playerInstance)
	{
		// Check for nullpoitner
		if (playerInstance == null)
			return false;
		
		byte teamId = 0;
		
		// Check to which team the player should be added
		if (_teams[0].getParticipatedPlayerCount() == _teams[1].getParticipatedPlayerCount())
			teamId = (byte) (Rnd.get(2));
		else
			teamId = (byte) (_teams[0].getParticipatedPlayerCount() > _teams[1].getParticipatedPlayerCount() ? 1 : 0);
		
		return _teams[teamId].addPlayer(playerInstance);
	}
	
	/**
	 * Removes a CTFEvent player from it's team<br>
	 * 1. Get team id of the player<br>
	 * 2. Remove player from it's team<br>
	 * <br>
	 * @param playerObjectId
	 * @return boolean: true if success, otherwise false
	 */
	public static boolean removeParticipant(int playerObjectId)
	{
		// Get the teamId of the player
		byte teamId = getParticipantTeamId(playerObjectId);
		
		// Check if the player is participant
		if (teamId != -1)
		{
			// Remove the player from team
			_teams[teamId].removePlayer(playerObjectId);
			return true;
		}
		
		return false;
	}
	
	
	public static boolean needParticipationFee()
	{
		return (CTFConfig.CTF_EVENT_PARTICIPATION_FEE[0] != 0) && (CTFConfig.CTF_EVENT_PARTICIPATION_FEE[1] != 0);
	}
	
	public static boolean hasParticipationFee(L2PcInstance playerInstance)
	{
		return playerInstance.getInventory().getInventoryItemCount(CTFConfig.CTF_EVENT_PARTICIPATION_FEE[0], -1) >= CTFConfig.CTF_EVENT_PARTICIPATION_FEE[1];
	}
	
	public static boolean payParticipationFee(L2PcInstance playerInstance)
	{
		return playerInstance.destroyItemByItemId("CTF Participation Fee", CTFConfig.CTF_EVENT_PARTICIPATION_FEE[0], CTFConfig.CTF_EVENT_PARTICIPATION_FEE[1], _lastNpcSpawn, true);
	}
	
	public static String getParticipationFee()
	{
		int itemId = CTFConfig.CTF_EVENT_PARTICIPATION_FEE[0];
		int itemNum = CTFConfig.CTF_EVENT_PARTICIPATION_FEE[1];
		
		if ((itemId == 0) || (itemNum == 0))
		{
			return "-";
		}
		
		return StringUtil.concat(String.valueOf(itemNum), " ", ItemTable.getInstance().getTemplate(itemId).getName());
	}
	
	/**
	 * Send a SystemMessage to all participated players<br>
	 * 1. Send the message to all players of team number one<br>
	 * 2. Send the message to all players of team number two<br>
	 * <br>
	 * @param message as String<br>
	 */
	public static void sysMsgToAllParticipants(String message)
	{
		CreatureSay cs = new CreatureSay(0, Say2.PARTY, "Event Manager", message);
		
		for (L2PcInstance playerInstance : _teams[0].getParticipatedPlayers().values())
		{
			if (playerInstance != null)
			{
				playerInstance.sendPacket(cs);
			}
		}
		
		for (L2PcInstance playerInstance : _teams[1].getParticipatedPlayers().values())
		{
			if (playerInstance != null)
			{
				playerInstance.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Close doors specified in CTFConfigs
	 * @param doors
	 */
	private static void closeDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			L2DoorInstance doorInstance = DoorTable.getInstance().getDoor(doorId);
			
			if (doorInstance != null)
			{
				doorInstance.closeMe();
			}
		}
	}
	
	/**
	 * Open doors specified in CTFConfigs
	 * @param doors
	 */
	private static void openDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			L2DoorInstance doorInstance = DoorTable.getInstance().getDoor(doorId);
			
			if (doorInstance != null)
			{
				doorInstance.openMe();
			}
		}
	}
	
	private static void SpawnFirstHeadQuarters()
	{
		NpcTemplate tmpl = NpcTable.getInstance().getTemplate(CTFConfig.CTF_EVENT_TEAM_1_HEADQUARTERS_ID);
		
		if (tmpl == null)
		{
			_log.warning("CTFEventEngine: First Head Quater is a NullPointer -> Invalid npc id in configs?");
			return;
		}
		
		try
		{
			_flag1Spawn = new L2Spawn(tmpl);
			
			_flag1Spawn.setLocx(CTFConfig.CTF_EVENT_TEAM_1_FLAG_COORDINATES[0]);
			_flag1Spawn.setLocy(CTFConfig.CTF_EVENT_TEAM_1_FLAG_COORDINATES[1]);
			_flag1Spawn.setLocz(CTFConfig.CTF_EVENT_TEAM_1_FLAG_COORDINATES[2]);
			_flag1Spawn.getAmount();
			_flag1Spawn.setHeading(CTFConfig.CTF_EVENT_TEAM_1_FLAG_COORDINATES[3]);
			_flag1Spawn.setRespawnDelay(1);
			// later no need to delete spawn from db, we don't store it (false)
			SpawnTable.getInstance().addNewSpawn(_flag1Spawn, false);
			_flag1Spawn.init();
			_lastFlag1Spawn = _flag1Spawn.getLastSpawn();
			_lastFlag1Spawn.setCurrentHp(_lastFlag1Spawn.getMaxHp());
			_lastFlag1Spawn.setTitle(CTFConfig.CTF_EVENT_TEAM_1_NAME);
			_lastFlag1Spawn.isAggressive();
			_lastFlag1Spawn.decayMe();
			_lastFlag1Spawn.spawnMe(_flag1Spawn.getLastSpawn().getX(), _flag1Spawn.getLastSpawn().getY(), _flag1Spawn.getLastSpawn().getZ());
			_lastFlag1Spawn.broadcastPacket(new MagicSkillUse(_lastFlag1Spawn, _lastFlag1Spawn, 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "SpawnFirstHeadQuaters: exception: " + e.getMessage(), e);
			return;
		}
	}
	
	private static void SpawnSecondHeadQuarters()
	{
		NpcTemplate tmpl = NpcTable.getInstance().getTemplate(CTFConfig.CTF_EVENT_TEAM_2_HEADQUARTERS_ID);
		
		if (tmpl == null)
		{
			_log.warning("CTFEventEngine: Second Head Quater is a NullPointer -> Invalid npc id in configs?");
			return;
		}
		
		try
		{
			_flag2Spawn = new L2Spawn(tmpl);
			
			_flag2Spawn.setLocx(CTFConfig.CTF_EVENT_TEAM_2_FLAG_COORDINATES[0]);
			_flag2Spawn.setLocy(CTFConfig.CTF_EVENT_TEAM_2_FLAG_COORDINATES[1]);
			_flag2Spawn.setLocz(CTFConfig.CTF_EVENT_TEAM_2_FLAG_COORDINATES[2]);
			_flag2Spawn.getAmount();
			_flag2Spawn.setHeading(CTFConfig.CTF_EVENT_TEAM_2_FLAG_COORDINATES[3]);
			_flag2Spawn.setRespawnDelay(1);
			// later no need to delete spawn from db, we don't store it (false)
			SpawnTable.getInstance().addNewSpawn(_flag2Spawn, false);
			_flag2Spawn.init();
			_lastFlag2Spawn = _flag2Spawn.getLastSpawn();
			_lastFlag2Spawn.setCurrentHp(_lastFlag2Spawn.getMaxHp());
			_lastFlag2Spawn.setTitle(CTFConfig.CTF_EVENT_TEAM_2_NAME);
			_lastFlag2Spawn.isAggressive();
			_lastFlag2Spawn.decayMe();
			_lastFlag2Spawn.spawnMe(_flag2Spawn.getLastSpawn().getX(), _flag2Spawn.getLastSpawn().getY(), _flag2Spawn.getLastSpawn().getZ());
			_lastFlag2Spawn.broadcastPacket(new MagicSkillUse(_lastFlag2Spawn, _lastFlag2Spawn, 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "SpawnSecondHeadQuaters: exception: " + e.getMessage(), e);
			return;
		}
	}
	
	/**
	 * UnSpawns the CTFEvent npc
	 */
	private static void unSpawnNpc()
	{
		// Delete the npc
		_lastNpcSpawn.deleteMe();
		SpawnTable.getInstance().deleteSpawn(_lastNpcSpawn.getSpawn(), false);
		
		// Stop respawning of the npc
		_npcSpawn.stopRespawn();
		_npcSpawn = null;
		_lastNpcSpawn = null;
		
		// Remove flags
		if (_lastFlag1Spawn != null)
		{
			_lastFlag1Spawn.deleteMe();
			_lastFlag2Spawn.deleteMe();
			SpawnTable.getInstance().deleteSpawn(_lastFlag1Spawn.getSpawn(), false);
			SpawnTable.getInstance().deleteSpawn(_lastFlag2Spawn.getSpawn(), false);
			_flag1Spawn.stopRespawn();
			_flag2Spawn.stopRespawn();
			_flag1Spawn = null;
			_flag2Spawn = null;
			_lastFlag1Spawn = null;
			_lastFlag2Spawn = null;
		}
	}
	
	/**
	 * Called when a player logs in<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 */
	public static void onLogin(L2PcInstance playerInstance)
	{
		if ((playerInstance == null) || (!isStarting() && !isStarted()))
		{
			return;
		}
		
		byte teamId = getParticipantTeamId(playerInstance.getObjectId());
		
		if (teamId == -1)
		{
			return;
		}
		
		_teams[teamId].addPlayer(playerInstance);
		new CTFEventTeleporter(playerInstance, _teams[teamId].getCoordinates(), true, false);
	}
	
	/**
	 * Called when a player logs out<br>
	 * <br>
	 * @param playerInstance as L2PcInstance<br>
	 */
	public static void onLogout(L2PcInstance playerInstance)
	{
		if ((playerInstance != null) && (isStarting() || isStarted() || isParticipating()))
		{
			if (removeParticipant(playerInstance.getObjectId()))
				playerInstance.teleToLocation((CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[0] + Rnd.get(101)) - 50, (CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[1] + Rnd.get(101)) - 50, CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES[2], 0);
		}
	}
	
	
	/**
	 * Called on every bypass by npc of type L2TvTEventNpc
	 * Needs synchronization cause of the max player check
	 * 
	 * @param command as String
	 * @param playerInstance as L2PcInstance
	 */
	public static synchronized void onBypass(String command, L2PcInstance playerInstance)
	{
		if (playerInstance == null || !isParticipating())
			return;
		
		final String htmContent;
		
		if (command.equals("ctf_event_participation"))
		{
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
			int playerLevel = playerInstance.getLevel();
			
			if (playerInstance.isCursedWeaponEquipped())
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "CursedWeaponEquipped.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}			
			else if (playerInstance.isInArenaEvent())
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Tournament.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}
			else if (OlympiadManager.getInstance().isRegistered(playerInstance))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Olympiad.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}
			else if (playerInstance.getKarma() > 0)
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Karma.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}
			else if ((playerLevel < CTFConfig.CTF_EVENT_MIN_LVL) || (playerLevel > CTFConfig.CTF_EVENT_MAX_LVL))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Level.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%min%", String.valueOf(CTFConfig.CTF_EVENT_MIN_LVL));
					npcHtmlMessage.replace("%max%", String.valueOf(CTFConfig.CTF_EVENT_MAX_LVL));
				}
			}
			else if ((_teams[0].getParticipatedPlayerCount() == CTFConfig.CTF_EVENT_MAX_PLAYERS_IN_TEAMS) && (_teams[1].getParticipatedPlayerCount() == CTFConfig.CTF_EVENT_MAX_PLAYERS_IN_TEAMS))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "TeamsFull.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%max%", String.valueOf(CTFConfig.CTF_EVENT_MAX_PLAYERS_IN_TEAMS));
				}
			}
            else if (CTFConfig.CTF_EVENT_MULTIBOX_PROTECTION_ENABLE && onMultiBoxRestriction(playerInstance))
            {
                htmContent = HtmCache.getInstance().getHtm(htmlPath + "MultiBox.htm");
                if (htmContent != null)
                {
                    npcHtmlMessage.setHtml(htmContent);
                    npcHtmlMessage.replace("%maxbox%", String.valueOf(CTFConfig.CTF_EVENT_NUMBER_BOX_REGISTER));
                }
            }
			else if (!payParticipationFee(playerInstance))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "ParticipationFee.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%fee%", getParticipationFee());
				}
			}
			else if (addParticipant(playerInstance))
				npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Registered.htm"));
			else
				return;
			
			playerInstance.sendPacket(npcHtmlMessage);
		}
		else if (command.equals("ctf_event_remove_participation"))
		{
			removeParticipant(playerInstance.getObjectId());
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
			npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Unregistered.htm"));
			playerInstance.sendPacket(npcHtmlMessage);
		}
	}
	
	
	/**
	 * Called on every onAction in L2PcIstance<br>
	 * <br>
	 * @param playerInstance
	 * @param targetedPlayerObjectId
	 * @return boolean: true if player is allowed to target, otherwise false
	 */
	public static boolean onAction(L2PcInstance playerInstance, int targetedPlayerObjectId)
	{
		if ((playerInstance == null) || !isStarted())
			return true;
		
		if (playerInstance.isGM())
			return true;
		
		byte playerTeamId = getParticipantTeamId(playerInstance.getObjectId());
		byte targetedPlayerTeamId = getParticipantTeamId(targetedPlayerObjectId);
		
		if (((playerTeamId != -1) && (targetedPlayerTeamId == -1)) || ((playerTeamId == -1) && (targetedPlayerTeamId != -1)))
		{
			playerInstance.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if ((playerTeamId != -1) && (targetedPlayerTeamId != -1) && (playerTeamId == targetedPlayerTeamId) && (playerInstance.getObjectId() != targetedPlayerObjectId) && !CTFConfig.CTF_EVENT_TARGET_TEAM_MEMBERS_ALLOWED)
		{
			playerInstance.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every scroll use<br>
	 * <br>
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to use scroll, otherwise false
	 */
	public static boolean onScrollUse(int playerObjectId)
	{
		if (!isStarted())
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !CTFConfig.CTF_EVENT_SCROLL_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every potion use
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to use potions, otherwise false
	 */
	public static boolean onPotionUse(int playerObjectId)
	{
		if (!isStarted())
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !CTFConfig.CTF_EVENT_POTIONS_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every escape use(thanks to nbd)
	 * @param playerObjectId
	 * @return boolean: true if player is not in CTF event, otherwise false
	 */
	public static boolean onEscapeUse(int playerObjectId)
	{
		if (!isStarted())
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Called on every summon item use
	 * @param playerObjectId
	 * @return boolean: true if player is allowed to summon by item, otherwise false
	 */
	public static boolean onItemSummon(int playerObjectId)
	{
		if (!isStarted())
		{
			return true;
		}
		
		if (isPlayerParticipant(playerObjectId) && !CTFConfig.CTF_EVENT_SUMMON_BY_ITEM_ALLOWED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Is called when a player is killed<br>
	 * <br>
	 * @param killerCharacter as L2Character<br>
	 * @param killedPlayerInstance as L2PcInstance<br>
	 */
	public static void onKill(L2Character killerCharacter, L2PcInstance killedPlayerInstance)
	{
		if (killedPlayerInstance == null || !isStarted())
			return;
		
		byte killedTeamId = getParticipantTeamId(killedPlayerInstance.getObjectId());
		
		if (killedTeamId == -1)
			return;

		new CTFEventTeleporter(killedPlayerInstance, _teams[killedTeamId].getCoordinates(), false, false);;
		
		if (killerCharacter == null)
			return;
		
		L2PcInstance killerPlayerInstance = null;
		
		if (killerCharacter instanceof L2PetInstance || killerCharacter instanceof L2SummonInstance)
		{
			killerPlayerInstance = ((L2Summon) killerCharacter).getOwner();		
			if (killerPlayerInstance == null) 
				return;
		}
		else if (killerCharacter instanceof L2PcInstance)
			killerPlayerInstance = (L2PcInstance) killerCharacter;
		else
			return;
		
		byte killerTeamId = getParticipantTeamId(killerPlayerInstance.getObjectId());
		
		if ((killerTeamId != -1) && (killedTeamId != -1) && (killerTeamId != killedTeamId))
		{
			sysMsgToAllParticipants(killerPlayerInstance.getName() + " Hunted Player " + killedPlayerInstance.getName() + "!");
		}
	}
	
	/**
	 * Called on Appearing packet received (player finished teleporting)
	 * @param playerInstance
	 */
	public static void onTeleported(L2PcInstance playerInstance)
	{
		if (!isStarted() || (playerInstance == null) || !isPlayerParticipant(playerInstance.getObjectId()))
			return;
		
		if (playerInstance.isMageClass())
		{
			if (CTFConfig.CTF_EVENT_MAGE_BUFFS != null && !CTFConfig.CTF_EVENT_MAGE_BUFFS.isEmpty())
			{
				for (int i : CTFConfig.CTF_EVENT_MAGE_BUFFS.keySet())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(i, CTFConfig.CTF_EVENT_MAGE_BUFFS.get(i));
					if (skill != null)
						skill.getEffects(playerInstance, playerInstance);
				}
			}
		}
		else
		{
			if (CTFConfig.CTF_EVENT_FIGHTER_BUFFS != null && !CTFConfig.CTF_EVENT_FIGHTER_BUFFS.isEmpty())
			{
				for (int i : CTFConfig.CTF_EVENT_FIGHTER_BUFFS.keySet())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(i, CTFConfig.CTF_EVENT_FIGHTER_BUFFS.get(i));
					if (skill != null)
						skill.getEffects(playerInstance, playerInstance);
				}
			}
		}
		removeParty(playerInstance);
	}
	
	/**
	 * @param source
	 * @param target
	 * @param skill
	 * @return true if player valid for skill
	 */
	public static final boolean checkForCTFSkill(L2PcInstance source, L2PcInstance target, L2Skill skill)
	{
		if (!isStarted())
		{
			return true;
		}
		
		// CTF is started
		final int sourcePlayerId = source.getObjectId();
		final int targetPlayerId = target.getObjectId();
		final boolean isSourceParticipant = isPlayerParticipant(sourcePlayerId);
		final boolean isTargetParticipant = isPlayerParticipant(targetPlayerId);
		
		// both players not participating
		if (!isSourceParticipant && !isTargetParticipant)
		{
			return true;
		}
		// one player not participating
		if (!(isSourceParticipant && isTargetParticipant))
		{
			return false;
		}
		// players in the different teams ?
		if (getParticipantTeamId(sourcePlayerId) != getParticipantTeamId(targetPlayerId))
		{
			if (!skill.isOffensive())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the CTFEvent state<br>
	 * <br>
	 * @param state as EventState<br>
	 */
	private static void setState(EventState state)
	{
		synchronized (_state)
		{
			_state = state;
		}
	}
	
	/**
	 * Is CTFEvent inactive?<br>
	 * <br>
	 * @return boolean: true if event is inactive(waiting for next event cycle), otherwise false<br>
	 */
	public static boolean isInactive()
	{
		boolean isInactive;
		
		synchronized (_state)
		{
			isInactive = _state == EventState.INACTIVE;
		}
		
		return isInactive;
	}
	
	/**
	 * Is CTFEvent in inactivating?<br>
	 * <br>
	 * @return boolean: true if event is in inactivating progress, otherwise false<br>
	 */
	public static boolean isInactivating()
	{
		boolean isInactivating;
		
		synchronized (_state)
		{
			isInactivating = _state == EventState.INACTIVATING;
		}
		
		return isInactivating;
	}
	
	/**
	 * Is CTFEvent in participation?<br>
	 * <br>
	 * @return boolean: true if event is in participation progress, otherwise false<br>
	 */
	public static boolean isParticipating()
	{
		boolean isParticipating;
		
		synchronized (_state)
		{
			isParticipating = _state == EventState.PARTICIPATING;
		}
		
		return isParticipating;
	}
	
	/**
	 * Is CTFEvent starting?<br>
	 * <br>
	 * @return boolean: true if event is starting up(setting up fighting spot, teleport players etc.), otherwise false<br>
	 */
	public static boolean isStarting()
	{
		boolean isStarting;
		
		synchronized (_state)
		{
			isStarting = _state == EventState.STARTING;
		}
		
		return isStarting;
	}
	
	/**
	 * Is CTFEvent started?<br>
	 * <br>
	 * @return boolean: true if event is started, otherwise false<br>
	 */
	public static boolean isStarted()
	{
		boolean isStarted;
		
		synchronized (_state)
		{
			isStarted = _state == EventState.STARTED;
		}
		
		return isStarted;
	}
	
	/**
	 * Is CTFEvent rewarding?<br>
	 * <br>
	 * @return boolean: true if event is currently rewarding, otherwise false<br>
	 */
	public static boolean isRewarding()
	{
		boolean isRewarding;
		
		synchronized (_state)
		{
			isRewarding = _state == EventState.REWARDING;
		}
		
		return isRewarding;
	}
	
	/**
	 * Returns the team id of a player, if player is not participant it returns -1
	 * @param playerObjectId
	 * @return byte: team name of the given playerName, if not in event -1
	 */
	public static byte getParticipantTeamId(int playerObjectId)
	{
		return (byte) (_teams[0].containsPlayer(playerObjectId) ? 0 : (_teams[1].containsPlayer(playerObjectId) ? 1 : -1));
	}
	
	/**
	 * Returns the team of a player, if player is not participant it returns null
	 * @param playerObjectId
	 * @return CTFEventTeam: team of the given playerObjectId, if not in event null
	 */
	public static CTFEventTeam getParticipantTeam(int playerObjectId)
	{
		return (_teams[0].containsPlayer(playerObjectId) ? _teams[0] : (_teams[1].containsPlayer(playerObjectId) ? _teams[1] : null));
	}
	
	/**
	 * Returns the enemy team of a player, if player is not participant it returns null
	 * @param playerObjectId
	 * @return CTFEventTeam: enemy team of the given playerObjectId, if not in event null
	 */
	public static CTFEventTeam getParticipantEnemyTeam(int playerObjectId)
	{
		return (_teams[0].containsPlayer(playerObjectId) ? _teams[1] : (_teams[1].containsPlayer(playerObjectId) ? _teams[0] : null));
	}
	
	/**
	 * Returns the team coordinates in which the player is in, if player is not in a team return null
	 * @param playerObjectId
	 * @return int[]: coordinates of teams, 2 elements, index 0 for team 1 and index 1 for team 2
	 */
	public static int[] getParticipantTeamCoordinates(int playerObjectId)
	{
		return _teams[0].containsPlayer(playerObjectId) ? _teams[0].getCoordinates() : (_teams[1].containsPlayer(playerObjectId) ? _teams[1].getCoordinates() : null);
	}
	
	/**
	 * Is given player participant of the event?
	 * @param playerObjectId
	 * @return boolean: true if player is participant, ohterwise false
	 */
	public static boolean isPlayerParticipant(int playerObjectId)
	{
		if (!isParticipating() && !isStarting() && !isStarted())
		{
			return false;
		}
		
		return _teams[0].containsPlayer(playerObjectId) || _teams[1].containsPlayer(playerObjectId);
	}
	
	/**
	 * Returns participated player count<br>
	 * <br>
	 * @return int: amount of players registered in the event<br>
	 */
	public static int getParticipatedPlayersCount()
	{
		if (!isParticipating() && !isStarting() && !isStarted())
		{
			return 0;
		}
		
		return _teams[0].getParticipatedPlayerCount() + _teams[1].getParticipatedPlayerCount();
	}
	
	/**
	 * Returns teams names<br>
	 * <br>
	 * @return String[]: names of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static String[] getTeamNames()
	{
		return new String[]
		{
			_teams[0].getName(),
			_teams[1].getName()
		};
	}
	
	/**
	 * Returns player count of both teams<br>
	 * <br>
	 * @return int[]: player count of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static int[] getTeamsPlayerCounts()
	{
		return new int[]
		{
			_teams[0].getParticipatedPlayerCount(),
			_teams[1].getParticipatedPlayerCount()
		};
	}
	
	/**
	 * Returns points count of both teams
	 * @return int[]: points of teams, 2 elements, index 0 for team 1 and index 1 for team 2<br>
	 */
	public static int[] getTeamsPoints()
	{
		return new int[]
		{
			_teams[0].getPoints(),
			_teams[1].getPoints()
		};
	}
	
	/**
	 * Used when carrier scores, dies or game ends
	 * @param player L2PcInstance
	 */
	public static void removeFlagCarrier(L2PcInstance player)
	{
		// un-equip - destroy flag
		player.getInventory().unEquipItemInSlot(Inventory.PAPERDOLL_RHAND);
		player.destroyItemByItemId("ctf", getEnemyTeamFlagId(player), 1, player, false);
		
		// unblock inventory
		player.getInventory().unblock();
		
		// re-equip player items
		final ItemInstance carrierRHand = _teams[0].containsPlayer(player.getObjectId()) ? _team1CarrierRHand : _team2CarrierRHand;
		final ItemInstance carrierLHand = _teams[0].containsPlayer(player.getObjectId()) ? _team1CarrierLHand : _team2CarrierLHand;
		if ((carrierRHand != null) && (player.getInventory().getItemByItemId(carrierRHand.getItemId()) != null))
		{
			player.getInventory().equipItem(carrierRHand);
		}
		if ((carrierLHand != null) && (player.getInventory().getItemByItemId(carrierLHand.getItemId()) != null))
		{
			player.getInventory().equipItem(carrierLHand);
		}
		setCarrierUnequippedWeapons(player, null, null);
		
		// flag carrier removal
		if (_teams[0].containsPlayer(player.getObjectId()))
		{
			_team1Carrier = null;
		}
		else
		{
			_team2Carrier = null;
		}
		
		// show re-equipped weapons
		player.broadcastUserInfo();
	}
	
	/**
	 * Assign the Ctf team flag carrier
	 * @param player L2PcInstance
	 */
	public static void setTeamCarrier(L2PcInstance player)
	{
		if (_teams[0].containsPlayer(player.getObjectId()))
		{
			_team1Carrier = player;
		}
		else
		{
			_team2Carrier = player;
		}
	}
	
	/**
	 * @param player L2PcInstance
	 * @return the team carrier L2PcInstance
	 */
	public static L2PcInstance getTeamCarrier(L2PcInstance player)
	{
		// check if team carrier has disconnected
		if (((_teams[0].containsPlayer(player.getObjectId()) == true) && (_team1Carrier != null) && (!_team1Carrier.isOnline() || ((_teams[1].containsPlayer(player.getObjectId()) == true) && (_team2Carrier != null) && (!_team2Carrier.isOnline())))))
		{
			player.destroyItemByItemId("ctf", getEnemyTeamFlagId(player), 1, player, false);
			return null;
		}
		
		// return team carrier
		return (_teams[0].containsPlayer(player.getObjectId()) ? _team1Carrier : _team2Carrier);
	}
	
	/**
	 * @param player L2PcInstance
	 * @return the enemy team carrier L2PcInstance
	 */
	public static L2PcInstance getEnemyCarrier(L2PcInstance player)
	{
		// check if enemy carrier has disconnected
		if (((_teams[0].containsPlayer(player.getObjectId()) == true) && (_team2Carrier != null) && (!_team2Carrier.isOnline() || ((_teams[1].containsPlayer(player.getObjectId()) == true) && (_team1Carrier != null) && (!_team1Carrier.isOnline())))))
		{
			player.destroyItemByItemId("ctf", getEnemyTeamFlagId(player), 1, player, false);
			return null;
		}
		
		// return enemy carrier
		return (_teams[0].containsPlayer(player.getObjectId()) ? _team2Carrier : _team1Carrier);
	}
	
	/**
	 * @param player L2PcInstance
	 * @return true if player is the carrier
	 */
	public static boolean playerIsCarrier(L2PcInstance player)
	{
		return ((player == _team1Carrier) || (player == _team2Carrier)) ? true : false;
	}
	
	/**
	 * @param player L2ItemInstance
	 * @return int The enemy flag id
	 */
	public static int getEnemyTeamFlagId(L2PcInstance player)
	{
		return (_teams[0].containsPlayer(player.getObjectId()) ? CTFConfig.CTF_EVENT_TEAM_2_FLAG : CTFConfig.CTF_EVENT_TEAM_1_FLAG);
	}
	
	/**
	 * Stores the carrier equipped weapons
	 * @param player L2PcInstance
	 * @param itemRight L2ItemInstance
	 * @param itemLeft L2ItemInstance
	 */
	public static void setCarrierUnequippedWeapons(L2PcInstance player, ItemInstance itemRight, ItemInstance itemLeft)
	{
		if (_teams[0].containsPlayer(player.getObjectId()))
		{
			_team1CarrierRHand = itemRight;
			_team1CarrierLHand = itemLeft;
		}
		else
		{
			_team2CarrierRHand = itemRight;
			_team2CarrierLHand = itemLeft;
		}
	}
	
	/**
	 * Broadcast a message to all participant screens
	 * @param message String
	 * @param duration int (in seconds)
	 */
	public static void broadcastScreenMessage(String message, int duration)
	{
		for (CTFEventTeam team : _teams)
		{
			for (L2PcInstance playerInstance : team.getParticipatedPlayers().values())
			{
				if (playerInstance != null)
				{
					playerInstance.sendPacket(new ExShowScreenMessage(message, duration * 1000));
				}
			}
		}
	}
	
	public static void removeParty(L2PcInstance activeChar)
	{
		if (activeChar.getParty() != null)
		{
			L2Party party = activeChar.getParty();
			party.removePartyMember(activeChar, MessageType.Left);
		}
	}
	
    public static List<L2PcInstance> allParticipants()
    {
        List<L2PcInstance> players = new ArrayList<L2PcInstance>();
        players.addAll(_teams[0].getParticipatedPlayers().values());
        players.addAll(_teams[1].getParticipatedPlayers().values());
        return players;
    }
   
    public static boolean onMultiBoxRestriction(L2PcInstance activeChar)
    {
    	return IPManager.getInstance().validBox(activeChar, TvTConfig.TVT_EVENT_NUMBER_BOX_REGISTER, allParticipants(), false);
    }
}