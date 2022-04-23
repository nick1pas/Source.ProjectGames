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
package custom.EventMonster;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.util.Rnd;

public class EventMonster extends Quest
{
	private static final String qn = "EventMonster";
	
	//Gold Coin
	public static int GOLD_COUNT_MIN = 100000;
	public static int GOLD_COUNT_MAX = 200000;
	
	//Event Token
	public static int EVENT_COUNT_MIN = 100;
	public static int EVENT_COUNT_MAX = 150;
	
	//Book
	public static int BOOK_COUNT_MIN = 5;
	public static int BOOK_COUNT_MAX = 10;
	
	//Life Stone
	public static int STONE_COUNT_MIN = 5;
	public static int STONE_COUNT_MAX = 10;
	
	//Gold Enchant Weapon
	public static int WEAPON_COUNT_MIN = 5;
	public static int WEAPON_COUNT_MAX = 10;
	
	//Gold Enchant Armor
	public static int ARMOR_COUNT_MIN = 5;
	public static int ARMOR_COUNT_MAX = 10;

	public EventMonster()
	{
		super(-1, qn, "custom");

		addKillId(50020);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		for (L2PcInstance obj : player.getKnownList().getKnownTypeInRadius(L2PcInstance.class, 5000))
		{
			if (!(obj.equals(player)))
			{
				obj.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), obj, true);
				obj.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), obj, true);
				
				obj.addItem("Book Codex", 6622, Rnd.get(BOOK_COUNT_MIN, BOOK_COUNT_MAX), obj, true);
				obj.addItem("Gold Life Stone", 8762, Rnd.get(STONE_COUNT_MIN, STONE_COUNT_MAX), obj, true);
				
				obj.addItem("Gold Enchant Weapon", 9552, Rnd.get(WEAPON_COUNT_MIN, WEAPON_COUNT_MAX), obj, true);
				obj.addItem("Gold Enchant Armor", 9555, Rnd.get(ARMOR_COUNT_MIN, ARMOR_COUNT_MAX), obj, true);

				obj.sendPacket(new ItemList(obj, true));
			}
		}

		return null;
	}

	public static void main(String[] args)
	{
		new EventMonster();
	}
}