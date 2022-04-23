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
package custom.PartyDrop;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

public class PartyDrop extends Quest
{
	private static final String qn = "PartyDrop";

	boolean _canReward = false;
	static HashMap<String, Integer> playerHwids = new HashMap<>();
	
	public PartyDrop()
	{
		super(-1, qn, "custom");

		addKillId(Config.PARTY_ZONE_LIST_CHAMPIONS);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (player.isInParty())
		{
			List<L2PcInstance> party = player.getParty().getPartyMembers();

			for (L2PcInstance member : party)
            {
				String pHwid = member.getHWid();
                    
				if (!playerHwids.containsKey(pHwid))
                {
					playerHwids.put(pHwid, 1);
					_canReward = true;
                }
				else
				{
					int count = playerHwids.get(pHwid);
                
					if (count < 1)
                    {
						playerHwids.remove(pHwid);
						playerHwids.put(pHwid, count + 1);
						_canReward = true;
                    }
					else
                    {
						member.sendMessage("Already 1 member of your hwid have been rewarded, so this character won't be rewarded.");
						_canReward = false;
                    }
				}
				if (_canReward)
				{
					if (member.isInsideRadius(npc, 1000, false, false))
				    {
						for (int[] reward : Config.PARTY_ZONE_REWARD)
						{
							member.addItem("Party Reward", reward[0], reward[1], member, true);
						}
				    }
				    else
				    {
					    member.sendMessage("You are too far to be rewarded.");
				    }
				}
            }
			playerHwids.clear();   
		}
		else
		{
			for (int[] reward : Config.PARTY_ZONE_REWARD)
			{
				player.addItem("Party Reward", reward[0], reward[1], player, true);
			}
		}

		return null;
	}

	public static void main(String[] args)
	{
		new PartyDrop();
	}
}