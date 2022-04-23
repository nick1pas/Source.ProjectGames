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
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

/**
 * An arena
 * @author durgus
 */
public class L2ArenaEventZone extends L2SpawnZone
{
	public L2ArenaEventZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.ARENA_EVENT, true);
		character.setInsideZone(ZoneId.FLAG_AREA, true);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance player = (L2PcInstance) character;
			
			if (player.isArenaProtection())
			{
				if (player.getPvpFlag() > 0)
					PvpFlagTaskManager.getInstance().remove(player);
				
				player.updatePvPFlag(1);
				player.broadcastUserInfo();
			}
			
			player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
			
		}
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.ARENA_EVENT, false);
		character.setInsideZone(ZoneId.FLAG_AREA, false);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance player = (L2PcInstance) character;
			
			player.updatePvPFlag(0);
			player.broadcastUserInfo();
			
			player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
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