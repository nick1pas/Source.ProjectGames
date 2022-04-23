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
package custom.NoblessDrops;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.util.Rnd;

public class NoblessDrops extends Quest
{
	private static final String qn = "NoblessDrops";
	
	private static final int[] CAVE_MOBS =
	{
		20329,
		20340,
		20341,
		20368,
		20375,
		20374,
		20415,
		20416,
		20427,
		20429,
		20487,
		20488,
		20489,
		20491,
		20542,
		20543
	};
	
	// Count
	public static int ItemCountMin = 1;
	public static int ItemCountMax = 2;
	
	// Chance
	private static final int SILVER_CHANCE = 90;
	private static final int GOLDEN_CHANCE = 50;

	public NoblessDrops()
	{
		super(-1, qn, "custom");

		addKillId(CAVE_MOBS);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (Rnd.get(100) < SILVER_CHANCE)
		{
			player.addItem("Silver Shard", 9502, Rnd.get(ItemCountMin, ItemCountMax), player, true);
		}
		if (Rnd.get(100) < GOLDEN_CHANCE)
		{
			player.addItem("Golden Shard", 9503, Rnd.get(ItemCountMin, ItemCountMax), player, true);
		}

		return null;
	}

	public static void main(String[] args)
	{
		new NoblessDrops();
	}
}