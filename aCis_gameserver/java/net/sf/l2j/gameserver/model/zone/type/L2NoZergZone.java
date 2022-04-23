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
 * this program. If not, see <[url="http://www.gnu.org/licenses/>."]http://www.gnu.org/licenses/>.[/url]
 */
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.custom.ZergManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class L2NoZergZone extends L2SpawnZone
{
	private int _maxClanMembers;
	private int _maxAllyMembers;
	private int _minPartyMembers;
	private boolean _isflagZone;
	private boolean _checkParty;
	private boolean _checkClan;
	private boolean _checkAlly;
	
	public L2NoZergZone(int id)
	{
		super(id);
		
		_maxClanMembers = 0;
		_maxAllyMembers = 0;
		_minPartyMembers = 0;
		_isflagZone = false;
		_checkParty = false;
		_checkClan = false;
		_checkAlly = false;
	}

	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("MaxClanMembers"))
			_maxClanMembers = Integer.parseInt(value);
		else if (name.equals("MaxAllyMembers"))
			_maxAllyMembers = Integer.parseInt(value);
		else if (name.equals("MinPartyMembers"))
			_minPartyMembers = Integer.parseInt(value);
		else if (name.equals("isflagZone"))
			_isflagZone = Boolean.parseBoolean(value);
		else if (name.equals("checkParty"))
			_checkParty = Boolean.parseBoolean(value);
		else if (name.equals("checkClan"))
			_checkClan = Boolean.parseBoolean(value);
		else if (name.equals("checkAlly"))
			_checkAlly = Boolean.parseBoolean(value);
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.NO_ZERG, true);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance activeChar = (L2PcInstance) character;
	
			if (_isflagZone)
			{
				activeChar.sendPacket(new ExShowScreenMessage("You have entered a Flag PvP zone!", 4000));
				activeChar.updatePvPStatus();
			}
			
			if (_checkParty)
			{
				if (!activeChar.isInParty() || activeChar.getParty().getMemberCount() < _minPartyMembers)
				{
					activeChar.sendPacket(new ExShowScreenMessage("Your party does not have " + _minPartyMembers + " members to enter on this zone!", 6 * 1000));
					ZergManager.getInstance().RandomTeleport(activeChar);
				}
			}

			if (_checkClan)
				MaxClanMembersOnArea(activeChar);

			if (_checkAlly)
				MaxAllyMembersOnArea(activeChar);
		}
	}
	
	public boolean MaxClanMembersOnArea(L2PcInstance activeChar)
	{
		return ZergManager.getInstance().checkClanArea(activeChar, _maxClanMembers, true);
	}
	
	public boolean MaxAllyMembersOnArea(L2PcInstance activeChar)
	{
		return ZergManager.getInstance().checkAllyArea(activeChar, _maxAllyMembers, L2World.getInstance().getAllPlayers().values(), true);
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.NO_ZERG, false);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance activeChar = (L2PcInstance) character;
			
			if(_isflagZone)
			{
				PvpFlagTaskManager.getInstance().add(activeChar, Config.PVP_NORMAL_TIME);
				activeChar.sendMessage("You have left a flag pvp zone!");
			}
		}
	}

	@Override
	public void onDieInside(L2Character character)
	{
	}

	@Override
	public void onReviveInside(L2Character character)
	{
	}
}