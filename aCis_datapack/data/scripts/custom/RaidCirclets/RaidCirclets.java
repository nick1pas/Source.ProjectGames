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
package custom.RaidCirclets;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.util.Rnd;

public class RaidCirclets extends Quest
{
	private static final String qn = "PartyDrop";

	// Chance
	private static final int ICE_FAIRY_CIRCLET_CHANCE = 20;
	private static final int ANTHARAS_CIRCLET_CHANCE = 20;
	private static final int VALAKAS_CIRCLET_CHANCE = 20;

	// Boss Id
	private static final int ANTHARAS = 29066;
	private static final int VALAKAS = 29028;
	private static final int ICE_FAIRY_SIRRA = 29056;
	
	// Ice Fairy Circlet
	private static final int ICE_FAIRY_CIRCLET = 8180;
	
	// Valakas and Antharas
	private static final int ANTHARAS_CIRCLET = 8568;
	private static final int VALAKAS_CIRCLET = 8567;

	boolean _canReward = false;
	static HashMap<String, Integer> playerIps = new HashMap<>();

	public RaidCirclets()
	{
		super(-1, qn, "custom");

		addKillId(ANTHARAS, VALAKAS, ICE_FAIRY_SIRRA);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		if (npcId == ANTHARAS)
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
						if (Rnd.get(100) < ANTHARAS_CIRCLET_CHANCE)
						{
							if (member.isInsideRadius(npc, 1000, false, false))
								member.addItem("Circlet", ANTHARAS_CIRCLET, 1, member, true);
							else
								member.sendMessage("You are too far to be rewarded.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < ANTHARAS_CIRCLET_CHANCE)
				{
					if (player.isInsideRadius(npc, 1000, false, false))
						player.addItem("Circlet", ANTHARAS_CIRCLET, 1, player, true);
					else
						player.sendMessage("You are too far to be rewarded.");
				}
			}
		}
		else if (npcId == VALAKAS)
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
						if (Rnd.get(100) < VALAKAS_CIRCLET_CHANCE)
						{
							if (member.isInsideRadius(npc, 1000, false, false))
								member.addItem("Circlet", VALAKAS_CIRCLET, 1, member, true);
							else
								member.sendMessage("You are too far to be rewarded.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < VALAKAS_CIRCLET_CHANCE)
				{
					if (player.isInsideRadius(npc, 1000, false, false))
						player.addItem("Circlet", VALAKAS_CIRCLET, 1, player, true);
					else
						player.sendMessage("You are too far to be rewarded.");
				}
			}
		}
		else if (npcId == ICE_FAIRY_SIRRA)
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
						if (Rnd.get(100) < ICE_FAIRY_CIRCLET_CHANCE)
						{
							if (member.isInsideRadius(npc, 1000, false, false))
								member.addItem("Circlet", ICE_FAIRY_CIRCLET, 1, member, true);
							else
								member.sendMessage("You are too far to be rewarded.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < ICE_FAIRY_CIRCLET_CHANCE)
				{
					if (player.isInsideRadius(npc, 1000, false, false))
						player.addItem("Circlet", ICE_FAIRY_CIRCLET, 1, player, true);
					else
						player.sendMessage("You are too far to be rewarded.");
				}
			}
		}
		return null;
	}

	public static void main(String[] args)
	{
		new RaidCirclets();
	}
}