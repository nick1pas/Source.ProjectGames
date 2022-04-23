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
package custom.RaidDrop;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.util.Rnd;

public class RaidDrop extends Quest
{
	private static final String qn = "PartyDrop";

	private static final int[] RAID_BOSSES =
	{
		25299,
		25302,
		25305,
		25309,
		25315,
		25312,
		25514,
		25319,
		25126,
		25450,
		25245,
		25143,
		25205,
		25524,
		25282,
		25276,
		25266,
		25249,
		25244,
		25229,
		25054
	};
    
	//Gold Coin
	public static int GoldCountMin = 10000;
	public static int GoldCountMax = 20000;
	//Event Coin
	public static int EventCountMin = 10;
	public static int EventCountMax = 15;

	boolean _canReward = false;
	static HashMap<String, Integer> playerIps = new HashMap<>();

	public RaidDrop()
	{
		super(-1, qn, "custom");

		addKillId(RAID_BOSSES);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		String htmltext = "";
		
		if (player.isInParty())
		{
			List<L2PcInstance> party = player.getParty().getPartyMembers();

			for (L2PcInstance member : party)
            {
				String pIp = member.getClient().getConnection().getInetAddress().getHostAddress();
                    
				if (!playerIps.containsKey(pIp))
                {
					playerIps.put(pIp, 1);
					_canReward = true;
                }
				else
				{
					int count = playerIps.get(pIp);
                
					if (count < 1)
                    {
						playerIps.remove(pIp);
						playerIps.put(pIp, count + 1);
						_canReward = true;
                    }
					else
                    {
						member.sendMessage("Dualbox is not allowed, so this character won't be rewarded.");
						_canReward = false;
                    }
				}
				if (_canReward)
				{
					member.addItem("Gold Coin", 9500, Rnd.get(GoldCountMin, GoldCountMax), member, true);
					member.addItem("Event Coin", 9501, Rnd.get(EventCountMin, EventCountMax), member, true);
					member.addItem("Essence Life", 9508, 1, member, true);
					member.addItem("Luck Box", 9509, 1, member, true);
					member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					htmltext = "Reward.htm";
				}
            }
			playerIps.clear();   
		}
		else
		{
			player.addItem("Gold Coin", 9500, Rnd.get(GoldCountMin, GoldCountMax), player, true);
			player.addItem("Event Coin", 9501, Rnd.get(EventCountMin, EventCountMax), player, true);
			player.addItem("Essence Life", 9508, 1, player, true);
			player.addItem("Luck Box", 9509, 1, player, true);
			player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
			htmltext = "Reward.htm";
		}

		return htmltext;
	}

	public static void main(String[] args)
	{
		new RaidDrop();
	}
}