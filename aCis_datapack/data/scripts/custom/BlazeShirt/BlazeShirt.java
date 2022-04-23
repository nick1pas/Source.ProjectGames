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
package custom.BlazeShirt;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.util.Rnd;

public class BlazeShirt extends Quest
{
	private static final String qn = "PartyDrop";

	// Chance
	private static final int BLESSED_CHANCE = 40;

	// Boss Id
	private static final int SBLESSED_ANTHARAS = 29066;
	private static final int DBLESSED_ANTHARAS = 29067;
	private static final int TBLESSED_ANTHARAS = 29068;

	private static final int BLESSED_BAIUM = 29020;
	private static final int BLESSED_FRINTEZZA = 29047;
	private static final int BLESSED_QUEEN_ANT = 29001;
	private static final int BLESSED_VALAKAS = 29028;
	private static final int BLESSED_ZAKEN = 29022;
	//private static final int BLESSED_ORFEN = 29014;
	// static final int BLESSED_CORE = 29006;
	
	// Items
	private static final int LIFE_ESSENCE = 9508;
	private static final int BLAZE_SHIRT = 9620;

	boolean _canReward = false;
	static HashMap<String, Integer> playerIps = new HashMap<>();

	public BlazeShirt()
	{
		super(-1, qn, "custom");

		addKillId(SBLESSED_ANTHARAS, DBLESSED_ANTHARAS, TBLESSED_ANTHARAS, BLESSED_BAIUM, BLESSED_FRINTEZZA, BLESSED_QUEEN_ANT, BLESSED_VALAKAS, BLESSED_ZAKEN);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		if (npcId == SBLESSED_ANTHARAS)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == DBLESSED_ANTHARAS)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == TBLESSED_ANTHARAS)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == BLESSED_BAIUM)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == BLESSED_FRINTEZZA)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == BLESSED_QUEEN_ANT)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == BLESSED_VALAKAS)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		else if (npcId == BLESSED_ZAKEN)
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
						if (Rnd.get(100) < BLESSED_CHANCE)
						{
							if ((member.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (member.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
							{
								member.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, member, null);
								member.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, member, null);
								member.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
								member.addItem("Blessed Blaze Shirt", 9622, 1, member, true);
								member.getInventory().updateDatabase();
								member.sendPacket(new ItemList(member, true));
								member.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
							}
							else
								member.sendMessage("You do not have the required items to create your blessed shirt.");
						}
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < BLESSED_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(LIFE_ESSENCE, 0) >= 40) && (player.getInventory().getInventoryItemCount(BLAZE_SHIRT, -1, true) >= 1))
					{
						player.getInventory().destroyItemByItemId("Life Essence", LIFE_ESSENCE, 40, player, null);
						player.getInventory().destroyItemByItemId("Blaze Shirt", BLAZE_SHIRT, 1, player, null);
						player.sendMessage("40 Life Essence and 1 Fusion Blaze Shirt was consumed.");
						player.addItem("Blessed Blaze Shirt", 9622, 1, player, true);
						player.getInventory().updateDatabase();
						player.sendPacket(new ItemList(player, true));
						player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
					}
					else
						player.sendMessage("You do not have the required items to create your blessed shirt.");
				}
			}
		}
		return null;
	}

	public static void main(String[] args)
	{
		new BlazeShirt();
	}
}