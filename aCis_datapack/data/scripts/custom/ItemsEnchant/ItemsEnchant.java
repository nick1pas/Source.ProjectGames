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
package custom.ItemsEnchant;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.util.Rnd;

public class ItemsEnchant extends Quest
{
	private static final String qn = "ItemsEnchant";

	// Chance
	private static final int JEWELS_ENCHANT_CHANCE = 40;
	private static final int WEAPON_ENCHANT_CHANCE = 50;
	// Gold Token
	public static final int GOLD_COUNT_MIN = 250000;
	public static final int GOLD_COUNT_MAX = 500000;
	//Event Token
	public static final int EVENT_COUNT_MIN = 50;
	public static final int EVENT_COUNT_MAX = 150;
	//Secret Book
	public static final int CODEX_COUNT_MIN = 30;
	public static final int CODEX_COUNT_MAX = 50;
	//Life Stone
	public static final int LS_COUNT_MIN = 30;
	public static final int LS_COUNT_MAX = 50;
	
	// Raid Boss: Antharas 3 Id's
	private static final int ANTHARAS_A = 29066;
	private static final int ANTHARAS_B = 29067;
	private static final int ANTHARAS_C = 29068;
	// Raid Boss
	private static final int BAIUM = 29020;
	private static final int FRINTEZZA = 29047;
	private static final int QUEEN_ANT = 29001;
	private static final int VALAKAS = 29028;
	private static final int ZAKEN = 29022;
	private static final int ORFEN = 29014;
	private static final int CORE = 29006;
	
	// Jewels Boss
	private static final int ANTHARAS_EARRING = 9616;
	private static final int BAIUM_RING = 9618;
	private static final int FRINTEZZA_NECKLACE = 9623;
	private static final int RING_QUEEN_ANT = 9620;
	private static final int NECKLACE_VALAKAS = 9617;
	private static final int ZAKEN_EARRING = 9619;
	private static final int ORFEN_EARRING = 6661;
	private static final int CORE_RING = 6662;

	boolean _canReward = false;
	static HashMap<String, Integer> playerIps = new HashMap<>();

	public ItemsEnchant()
	{
		super(-1, qn, "custom");

		addKillId(ANTHARAS_A, ANTHARAS_B, ANTHARAS_C, BAIUM, FRINTEZZA, QUEEN_ANT, VALAKAS, ZAKEN, ORFEN, CORE);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		
		if (npcId == ANTHARAS_A)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 40) && (member.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, member, null);
									member.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				//Enchanted Jewels
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 40) && (player.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, player, null);
						player.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}

				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == ANTHARAS_B)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 40) && (member.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, member, null);
									member.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				//Enchanted Jewels
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 40) && (player.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, player, null);
						player.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}

				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == ANTHARAS_C)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 40) && (member.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, member, null);
									member.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				//Enchanted Jewels
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 40) && (player.getInventory().getInventoryItemCount(9500, 0) >= 2500000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 40, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 2500000, player, null);
						player.getInventory().addEnchantedItem("Earring of Antharas +25", ANTHARAS_EARRING, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}

				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == BAIUM)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 25) && (member.getInventory().getInventoryItemCount(9500, 0) >= 1000000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 25, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 1000000, member, null);
									member.getInventory().addEnchantedItem("Ring of Baium +25", BAIUM_RING, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				//Enchanted Jewels
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 25) && (player.getInventory().getInventoryItemCount(9500, 0) >= 1000000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 25, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 1000000, player, null);
						player.getInventory().addEnchantedItem("Ring of Baium +25", BAIUM_RING, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}

				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == FRINTEZZA)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 15) && (member.getInventory().getInventoryItemCount(9500, 0) >= 500000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 15, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 500000, member, null);
									member.getInventory().addEnchantedItem("Frintezza's Necklace +25", FRINTEZZA_NECKLACE, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 15) && (player.getInventory().getInventoryItemCount(9500, 0) >= 500000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 15, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 500000, player, null);
						player.getInventory().addEnchantedItem("Frintezza's Necklace +25", FRINTEZZA_NECKLACE, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}

				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == QUEEN_ANT)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 20) && (member.getInventory().getInventoryItemCount(9500, 0) >= 750000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 20, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 750000, member, null);
									member.getInventory().addEnchantedItem("Ring of Queen Ant +25", RING_QUEEN_ANT, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 20) && (player.getInventory().getInventoryItemCount(9500, 0) >= 750000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 20, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 750000, player, null);
						player.getInventory().addEnchantedItem("Ring of Queen Ant +25", RING_QUEEN_ANT, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}
				
				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 50) && (member.getInventory().getInventoryItemCount(9500, 0) >= 3000000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 50, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 3000000, member, null);
									member.getInventory().addEnchantedItem("Necklace of Valakas +25", NECKLACE_VALAKAS, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 50) && (player.getInventory().getInventoryItemCount(9500, 0) >= 3000000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 50, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 3000000, player, null);
						player.getInventory().addEnchantedItem("Necklace of Valakas +25", NECKLACE_VALAKAS, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}
				
				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == ZAKEN)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
							{
								if ((member.getInventory().getInventoryItemCount(9508, 0) >= 15) && (member.getInventory().getInventoryItemCount(9500, 0) >= 750000))
								{
									member.getInventory().destroyItemByItemId("Dark Blood", 9508, 15, member, null);
									member.getInventory().destroyItemByItemId("Golden Coin", 9500, 750000, member, null);
									member.getInventory().addEnchantedItem("Zaken Earring +25", ZAKEN_EARRING, 1, 25, member, null);
								}
								else
									member.sendMessage("You do not have the required items to create your blessed jewels.");
							}

							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
				{
					if ((player.getInventory().getInventoryItemCount(9508, 0) >= 15) && (player.getInventory().getInventoryItemCount(9500, 0) >= 750000))
					{
						player.getInventory().destroyItemByItemId("Dark Blood", 9508, 15, player, null);
						player.getInventory().destroyItemByItemId("Golden Coin", 9500, 750000, player, null);
						player.getInventory().addEnchantedItem("Zaken Earring +25", ZAKEN_EARRING, 1, 25, player, null);
					}
					else
						player.sendMessage("You do not have the required items to create your blessed jewels.");
				}
				
				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == ORFEN)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
								member.getInventory().addEnchantedItem("Earring of Orfen +25", ORFEN_EARRING, 1, 25, member, null);

							//Enchanted Weapon
							if (Rnd.get(100) < WEAPON_ENCHANT_CHANCE)
								WeaponReward(member);
							
							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
					player.getInventory().addEnchantedItem("Earring of Orfen +25", ORFEN_EARRING, 1, 25, player, null);

				if (Rnd.get(100) < WEAPON_ENCHANT_CHANCE)
					WeaponReward(player);
				
				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}
		else if (npcId == CORE)
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
						if (member.isInsideRadius(npc, 1000, false, false))
						{
							//Enchanted Jewels
							if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
								member.getInventory().addEnchantedItem("Ring of Core +25", CORE_RING, 1, 25, member, null);

							//Enchanted Weapon
							if (Rnd.get(100) < WEAPON_ENCHANT_CHANCE)
								WeaponReward(member);
							
							//Random Rewards
							member.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), member, true);
							member.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), member, true);
							member.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), member, true);
							member.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), member, true);
							
							//Update inventory
							member.getInventory().updateDatabase();
							member.sendPacket(new ItemList(member, true));
						}
						else
							member.sendMessage("You are too far away to be rewarded.");
					}
				}
				playerIps.clear();   
			}
			else
			{
				if (Rnd.get(100) < JEWELS_ENCHANT_CHANCE)
					player.getInventory().addEnchantedItem("Ring of Core +25", CORE_RING, 1, 25, player, null);

				if (Rnd.get(100) < WEAPON_ENCHANT_CHANCE)
					WeaponReward(player);
				
				//Random Rewards
				player.addItem("Gold Token", 9500, Rnd.get(GOLD_COUNT_MIN, GOLD_COUNT_MAX), player, true);
				player.addItem("Event Token", 9501, Rnd.get(EVENT_COUNT_MIN, EVENT_COUNT_MAX), player, true);
				player.addItem("Secret Book of Giants", 6622, Rnd.get(CODEX_COUNT_MIN, CODEX_COUNT_MAX), player, true);
				player.addItem("Top-Grade Life Stone: Level 76", 8762, Rnd.get(LS_COUNT_MIN, LS_COUNT_MAX), player, true);
				
				//Update inventory
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, true));
			}
		}

		return null;
	}

	//Reward a custom pvp weapon
	public void WeaponReward(L2PcInstance activeChar)
	{
		switch (Rnd.get(23))
		{
		    case 0:
		    {
		    	activeChar.getInventory().addEnchantedItem("Zeus Mace {PvP} +25", 9450, 1, 25, activeChar, null);
			    break;
		    }
		    case 1:
		    {
		    	activeChar.getInventory().addEnchantedItem("Artemis Bow {PvP} +25", 9451, 1, 25, activeChar, null);
			    break;
		    }
		    case 2:
		    {
		    	activeChar.getInventory().addEnchantedItem("Valkyrie Spear {PvP} +25", 9452, 1, 25, activeChar, null);
			    break;
		    }
		    case 3:
		    {
		    	activeChar.getInventory().addEnchantedItem("Athena Blade {PvP} +25", 9453, 1, 25, activeChar, null);
			    break;
		    }
		    case 4:
		    {
		    	activeChar.getInventory().addEnchantedItem("Hades Dagger {PvP} +25", 9454, 1, 25, activeChar, null);
			    break;
		    }
		    case 5:
		    {
		    	activeChar.getInventory().addEnchantedItem("Thor Hammer {PvP} +25", 9455, 1, 25, activeChar, null);
			    break;
		    }
		    case 6:
		    {
		    	activeChar.getInventory().addEnchantedItem("Odin Cleaver {PvP} +25", 9456, 1, 25, activeChar, null);
			    break;
		    }
		    case 7:
		    {
		    	activeChar.getInventory().addEnchantedItem("Ares Fang {PvP} +25", 9457, 1, 25, activeChar, null);
			    break;
		    }
		    case 8:
		    {
		    	activeChar.getInventory().addEnchantedItem("Aphrodite Sceptre {PvP} +25", 9458, 1, 25, activeChar, null);
			    break;
		    }
		    case 9:
		    {
		    	activeChar.getInventory().addEnchantedItem("Athena Wings Blade {PvP} +25", 9459, 1, 25, activeChar, null);
			    break;
		    }
		    case 10:
		    {
		    	activeChar.getInventory().addEnchantedItem("Bow of Scion {PvP} +25", 9460, 1, 25, activeChar, null);
			    break;
		    }
		    case 11:
		    {
		    	activeChar.getInventory().addEnchantedItem("Glint Eye {PvP} +25", 9461, 1, 25, activeChar, null);
			    break;
		    }
		    case 12:
		    {
		    	activeChar.getInventory().addEnchantedItem("Sword of Templar {PvP} +25", 9462, 1, 25, activeChar, null);
			    break;
		    }
		    case 13:
		    {
		    	activeChar.getInventory().addEnchantedItem("Jade Warhammer {PvP} +25", 9463, 1, 25, activeChar, null);
			    break;
		    }
		    case 14:
		    {
		    	activeChar.getInventory().addEnchantedItem("Dusk Sword {PvP} +25", 9464, 1, 25, activeChar, null);
			    break;
		    }
		    case 15:
		    {
		    	activeChar.getInventory().addEnchantedItem("Dual God's Blade {PvP} +25", 9465, 1, 25, activeChar, null);
			    break;
		    }
		    case 16:
		    {
		    	activeChar.getInventory().addEnchantedItem("Dual Dragon Slayer {PvP} +25", 9466, 1, 25, activeChar, null);
			    break;
		    }
		    case 17:
		    {
		    	activeChar.getInventory().addEnchantedItem("Dual Templar {PvP} +25", 9467, 1, 25, activeChar, null);
			    break;
		    }
		    case 18:
		    {
		    	activeChar.getInventory().addEnchantedItem("Ancient Elven Bow {PvP} +25", 9468, 1, 25, activeChar, null);
			    break;
		    }
		    case 19:
		    {
		    	activeChar.getInventory().addEnchantedItem("Razors Blade {PvP} +25", 9469, 1, 25, activeChar, null);
			    break;
		    }
		    case 20:
		    {
		    	activeChar.getInventory().addEnchantedItem("Imperial Razors {PvP} +25", 9470, 1, 25, activeChar, null);
			    break;
		    }
		    case 21:
		    {
		    	activeChar.getInventory().addEnchantedItem("Tears of Fallen Angel {PvP} +25", 9471, 1, 25, activeChar, null);
			    break;
		    }
		    case 22:
		    {
		    	activeChar.getInventory().addEnchantedItem("Dusk Staff {PvP} +25", 9472, 1, 25, activeChar, null);
			    break;
		    }
	    }
	}

	public static void main(String[] args)
	{
		new ItemsEnchant();
	}
}