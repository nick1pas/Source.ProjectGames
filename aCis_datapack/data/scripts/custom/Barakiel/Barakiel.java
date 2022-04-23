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
package custom.Barakiel;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.util.Rnd;

public class Barakiel extends Quest
{
	private static final String qn = "Barakiel";

	private static final int PARTYMOBS = 25325;

	boolean _canReward = false;
	static HashMap<String, Integer> playerIps = new HashMap<>();

	public Barakiel()
	{
		super(-1, qn, "custom");

		addKillId(PARTYMOBS);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
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
					member.addItem("Luck Box", 9504, 1, member, true);
					member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
				}
            }
			playerIps.clear();   
		}
		else
		{
			player.addItem("Luck Box", 9504, Rnd.get(100, 200) * Config.VIP_DROP_RATE, player, true);
			player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
		}

		return null;
	}

	public static void main(String[] args)
	{
		new Barakiel();
	}
}