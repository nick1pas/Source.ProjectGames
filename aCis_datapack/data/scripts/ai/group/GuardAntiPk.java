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
package ai.group;

import ai.AbstractNpcAI;

import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.L2Attackable;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.util.Util;

public class GuardAntiPk extends AbstractNpcAI
{
	private static final int[] GUARD = 
	{
		18001,
		18002
	};

	public GuardAntiPk(String name, String descr)
	{
		super(name, descr);
		
		for (L2Spawn npc : SpawnTable.getInstance().getSpawnTable())
			if (Util.contains(GUARD, npc.getNpcId()) && npc.getLastSpawn() != null && npc.getLastSpawn() instanceof L2Attackable)
				((L2Attackable) npc.getLastSpawn()).seeThroughSilentMove(true);
		
		addSpawnId(GUARD);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc instanceof L2Attackable)
			((L2Attackable) npc).seeThroughSilentMove(true);
		
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new GuardAntiPk(GuardAntiPk.class.getSimpleName(), "ai/group");
	}
}