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

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Duel;
import net.sf.l2j.util.Rnd;

public class CTFEventTeleporter implements Runnable
{
	/** The instance of the player to teleport */
	private L2PcInstance _activeChar = null;
	/** Coordinates of the spot to teleport to */
	private int[] _coordinates = new int[3];
	/** Admin removed this player from event */
	private boolean _adminRemove = false;
	
	/**
	 * Initialize the teleporter and start the delayed task.
	 * @param playerInstance
	 * @param coordinates
	 * @param fastSchedule
	 * @param adminRemove
	 */
	public CTFEventTeleporter(L2PcInstance playerInstance, int[] coordinates, boolean fastSchedule, boolean adminRemove)
	{
		_activeChar = playerInstance;
		_coordinates = coordinates;
		_adminRemove = adminRemove;
		
		long delay = (CTFEvent.isStarted() ? CTFConfig.CTF_EVENT_RESPAWN_TELEPORT_DELAY : CTFConfig.CTF_EVENT_START_LEAVE_TELEPORT_DELAY) * 1000;
		
		ThreadPoolManager.getInstance().scheduleGeneral(this, fastSchedule ? 0 : delay);
	}
		
	/**
	 * The task method to teleport the player<br>
	 * 1. Unsummon pet if there is one<br>
	 * 2. Remove all effects<br>
	 * 3. Revive and full heal the player<br>
	 * 4. Teleport the player<br>
	 * 5. Broadcast status and user info
	 */
	@Override
	public void run()
	{
		if (_activeChar == null)
			return;
		
		L2Summon summon = _activeChar.getPet();
		
		if (summon != null)
			summon.unSummon(_activeChar);
		
		if ((CTFConfig.CTF_EVENT_EFFECTS_REMOVAL == 0) || ((CTFConfig.CTF_EVENT_EFFECTS_REMOVAL == 1) && ((_activeChar.getTeam() == 0) || (_activeChar.isInDuel() && (_activeChar.getDuelState() != Duel.DUELSTATE_INTERRUPTED)))))
			_activeChar.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		if (_activeChar.isInDuel())
			_activeChar.setDuelState(Duel.DUELSTATE_INTERRUPTED);
		
		_activeChar.doRevive();
		
		_activeChar.teleToLocation((_coordinates[0] + Rnd.get(101)) - 50, (_coordinates[1] + Rnd.get(101)) - 50, _coordinates[2], 0);
		
		// Reset flag carrier
		if (CTFEvent.playerIsCarrier(_activeChar))
		{
			CTFEvent.removeFlagCarrier(_activeChar);
			CTFEvent.sysMsgToAllParticipants("The " + CTFEvent.getParticipantEnemyTeam(_activeChar.getObjectId()).getName() + " flag has been returned!");
		}
		
		if (CTFEvent.isStarted() && !_adminRemove)
			_activeChar.setTeam(CTFEvent.getParticipantTeamId(_activeChar.getObjectId()) + 1);
		else
			_activeChar.setTeam(0);
		
		_activeChar.setCurrentCp(_activeChar.getMaxCp());
		_activeChar.setCurrentHp(_activeChar.getMaxHp());
		_activeChar.setCurrentMp(_activeChar.getMaxMp());
		
		_activeChar.broadcastStatusUpdate();
		_activeChar.broadcastUserInfo();
	}
}