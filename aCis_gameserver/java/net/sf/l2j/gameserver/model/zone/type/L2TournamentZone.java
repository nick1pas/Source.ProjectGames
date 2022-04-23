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

/**
 * An arena
 * @author durgus
 */
public class L2TournamentZone extends L2SpawnZone
{
	public L2TournamentZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.PEACE, true);
		character.setInsideZone(ZoneId.TOURNAMENT, true);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance player = (L2PcInstance) character;
			player.setTournamentTeleport(true);

			if (!player.isOlympiadProtection())
			{
				player.sendMessage("You have entered a Tournament zone.");
				player.getAppearance().setInvisible();
				player.broadcastUserInfo();
			}
		}
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.TOURNAMENT, false);
		character.setInsideZone(ZoneId.PEACE, false);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance player = (L2PcInstance) character;
			
			if (!player.isArenaProtection() && !player.isOlympiadProtection())
			{
				player.setTournamentTeleport(false);
				
				if (!player.inObserverMode() && !player.isOlympiadProtection() && !player.isGM())
					player.getAppearance().setVisible();
				
				/*
				if (!player.inObserverMode())
				{
					player._player_n = 1;
					player.setPartySearch4x4(false);
					player.setPartySearch9x9(false);
					player.sendMessage("Tournament: You have been removed from the waiting list!");
				}
				*/
				
				player.sendMessage("You left a Tournament zone.");
				player.broadcastUserInfo();
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