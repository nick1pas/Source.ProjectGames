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
package net.sf.l2j.gameserver.model.entity.events.partyfarm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

public class PartyFarm
{	
	public static void spawnMonsters()
	{
		int[] coord;
		for (int i = 0; i < Config.MONSTER_LOCS_COUNT; i++)
		{
			coord = Config.MONSTER_LOCS[i];
			monstersArea.add(spawnNPC(coord[0], coord[1], coord[2], Config.CHAMPION_ID));
		}
	}
	
	protected static L2Spawn spawnNPC(int xPos, int yPos, int zPos, int npcId)
	{
		final NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
		
		try
		{
			final L2Spawn spawn = new L2Spawn(template);
			spawn.setLocx(xPos);
			spawn.setLocy(yPos);
			spawn.setLocz(zPos);
			spawn.setHeading(0);
			spawn.setRespawnDelay(3);
			SpawnTable.getInstance().addNewSpawn(spawn, false);
			spawn.init();
		
			spawn.getLastSpawn().setTitle("Event Champion");
			spawn.getLastSpawn().isAggressive();
			spawn.getLastSpawn().decayMe();
			spawn.getLastSpawn().spawnMe(spawn.getLastSpawn().getX(), spawn.getLastSpawn().getY(), spawn.getLastSpawn().getZ());
			spawn.getLastSpawn().broadcastPacket(new MagicSkillUse(spawn.getLastSpawn(), spawn.getLastSpawn(), 1034, 1, 1, 1));

			return spawn;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static List<L2Spawn> monstersArea = new CopyOnWriteArrayList<>();
	
	protected static void unSpawnMonsters()
	{
		for (L2Spawn s : monstersArea)
		{
			if (s == null)
			{
				monstersArea.remove(s);
				continue;
			}
			
			s.getLastSpawn().deleteMe();
			s.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(s, true);
			monstersArea.remove(s);
		}
	}
}