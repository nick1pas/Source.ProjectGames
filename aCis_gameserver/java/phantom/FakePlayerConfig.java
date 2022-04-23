/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package phantom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.commons.config.ExProperties;

public class FakePlayerConfig
{
	protected static final Logger _log = Logger.getLogger(FakePlayerConfig.class.getName());
	
	private static final String PHANTOM_FILE = "./config/phantom/FakePlayers.properties";

	//Social
	public static int FAKE_CHANCE_TO_TALK_SOCIAL;
	public static int FAKE_SOCIAL_CHANCE;
	public static int FAKE_SIT_CHANCE;
	
	//Consumables
	public static int FAKE_PLAYER_ARROW;
	public static int FAKE_PLAYER_SOULSHOT;
	public static int FAKE_PLAYER_BLESSED_SOULSHOT;
	
	//Timer
	public static int DESPAWN_CITIZEN_RANDOM_TIME_1;
	public static int DESPAWN_CITIZEN_RANDOM_TIME_2;
	
	public static int DESPAWN_PVP_RANDOM_TIME_1;
	public static int DESPAWN_PVP_RANDOM_TIME_2;
	
	//Title
	public static String FAKE_PLAYER_FIXED_TITLE;
	
	//Clan
	public static String CLAN_ID;
	public static List<Integer> LIST_CLAN_ID;
	
	//Enchant
	public static int MIN_ENCHANT_ARMOR;
	public static int MAX_ENCHANT_ARMOR;
	
	public static int MIN_ENCHANT_WEAPON;
	public static int MAX_ENCHANT_WEAPON;
	
	public static int MIN_ENCHANT_JEWEL;
	public static int MAX_ENCHANT_JEWEL;
	
	//Armors Robe
	public static String PHANTOM_ROB_ARMOR_1;
	public static List<Integer> LIST_PHANTOM_ROB_ARMOR_1 = new ArrayList<>();

	public static String PHANTOM_ROB_ARMOR_2;
	public static List<Integer> LIST_PHANTOM_ROB_ARMOR_2 = new ArrayList<>();
	
	public static String PHANTOM_ROB_ARMOR_3;
	public static List<Integer> LIST_PHANTOM_ROB_ARMOR_3 = new ArrayList<>();
	
	public static String PHANTOM_ROB_ARMOR_4;
	public static List<Integer> LIST_PHANTOM_ROB_ARMOR_4 = new ArrayList<>();
	
	//Armors Heavy
	public static String PHANTOM_HEAVY_ARMOR_1;
	public static List<Integer> LIST_PHANTOM_HEAVY_ARMOR_1 = new ArrayList<>();

	public static String PHANTOM_HEAVY_ARMOR_2;
	public static List<Integer> LIST_PHANTOM_HEAVY_ARMOR_2 = new ArrayList<>();
	
	public static String PHANTOM_HEAVY_ARMOR_3;
	public static List<Integer> LIST_PHANTOM_HEAVY_ARMOR_3 = new ArrayList<>();
	
	public static String PHANTOM_HEAVY_ARMOR_4;
	public static List<Integer> LIST_PHANTOM_HEAVY_ARMOR_4 = new ArrayList<>();
	
	//Armors Light
	public static String PHANTOM_LIGHT_ARMOR_1;
	public static List<Integer> LIST_PHANTOM_LIGHT_ARMOR_1 = new ArrayList<>();

	public static String PHANTOM_LIGHT_ARMOR_2;
	public static List<Integer> LIST_PHANTOM_LIGHT_ARMOR_2 = new ArrayList<>();
	
	public static String PHANTOM_LIGHT_ARMOR_3;
	public static List<Integer> LIST_PHANTOM_LIGHT_ARMOR_3 = new ArrayList<>();
	
	public static String PHANTOM_LIGHT_ARMOR_4;
	public static List<Integer> LIST_PHANTOM_LIGHT_ARMOR_4 = new ArrayList<>();
	
	//BOW
	public static String FAKE_BOW_ID;
	public static List<Integer> LIST_FAKE_BOW;
	
	//DAGGER
	public static String FAKE_DAGGER_ID;
	public static List<Integer> LIST_FAKE_DAGGER;
	
	//SWORD
	public static String FAKE_SWORD_ID;
	public static List<Integer> LIST_FAKE_SWORD;
	
	//SPEAR
	public static String FAKE_SPEAR_ID;
	public static List<Integer> LIST_FAKE_SPEAR;
	
	//DUAL
	public static String FAKE_DUAL_ID;
	public static List<Integer> LIST_FAKE_DUAL;
	
	//FIST
	public static String FAKE_FIST_ID;
	public static List<Integer> LIST_FAKE_FIST;
	
	//BIGSWORD
	public static String FAKE_BIGSWORD_ID;
	public static List<Integer> LIST_FAKE_BIG_SWORD;
	
	//MAGIC
	public static String FAKE_MAGIC_ID;
	public static List<Integer> LIST_FAKE_MAGIC;
	
	//SHIELD
	public static String FAKE_SHIELD_ID;
	public static List<Integer> LIST_FAKE_SHIELD;
	
	//JEWELS
	public static int[] PHANTOM_JEWELS_1;
	public static List<int[]> LIST_PHANTOM_JEWELS_1 = new ArrayList<>();
	
	public static int[] PHANTOM_JEWELS_2;
	public static List<int[]> LIST_PHANTOM_JEWELS_2 = new ArrayList<>();
	
	//Accessory
	public static boolean ALLOW_FAKE_PLAYERS_ACCESSORY;
	public static String FAKE_ACCESSORY_ID;
	public static List<Integer> LIST_FAKE_ACCESSORY;
	
	//Buffer
	public static String NUKER_BUFFER;
	public static List<Integer> NUKER_BUFFER_LIST = new ArrayList<>();
	
	public static String ARCHER_BUFFER;
	public static List<Integer> ARCHER_BUFFER_LIST = new ArrayList<>();
	
	public static String WARRIOR_BUFFER;
	public static List<Integer> WARRIOR_BUFFER_LIST = new ArrayList<>();
	
	public static String DAGGER_BUFFER;
	public static List<Integer> DAGGER_BUFFER_LIST = new ArrayList<>();
	
	public static String RANDOM_BUFFER;
	public static List<Integer> RANDOM_BUFFER_LIST = new ArrayList<>();
	
	public static void init()
	{
		ExProperties phantom = load(PHANTOM_FILE);
	
		//Social
		FAKE_CHANCE_TO_TALK_SOCIAL = phantom.getProperty("FakeTalkChance", 3000);
		FAKE_SOCIAL_CHANCE = phantom.getProperty("FakeSocialChance", 3000);
		FAKE_SIT_CHANCE = phantom.getProperty("FakeSitChance", 10);
		
		//Consumables
		FAKE_PLAYER_ARROW = phantom.getProperty("FakePlayerArrow", 0);
		FAKE_PLAYER_SOULSHOT = phantom.getProperty("FakePlayerSoulShot", 0);
		FAKE_PLAYER_BLESSED_SOULSHOT = phantom.getProperty("FakePlayerBlessedSoulShot", 0);
		
		//Timer
		DESPAWN_CITIZEN_RANDOM_TIME_1 = phantom.getProperty("FakeCitizenDespawnMinTime", 1);
		DESPAWN_CITIZEN_RANDOM_TIME_2 = phantom.getProperty("FakeCitizenDespawnMaxTime", 1);
		
		DESPAWN_PVP_RANDOM_TIME_1 = phantom.getProperty("FakePvpDespawnMinTime", 1);
		DESPAWN_PVP_RANDOM_TIME_2 = phantom.getProperty("FakePvpDespawnMaxTime", 1);
		
		//Title
		FAKE_PLAYER_FIXED_TITLE = phantom.getProperty("FakePlayerTitle", "");
		
		//Clan
		CLAN_ID = phantom.getProperty("FakeClanIDList", "");
		LIST_CLAN_ID = new ArrayList<>();
		for (String itemId : CLAN_ID.split(","))
		{
			LIST_CLAN_ID.add(Integer.parseInt(itemId));
		}
		
		//Enchant
		MIN_ENCHANT_ARMOR = phantom.getProperty("MinEnchantAmor", 0);
		MAX_ENCHANT_ARMOR = phantom.getProperty("MaxEnchantAmor", 0);
		
		MIN_ENCHANT_WEAPON = phantom.getProperty("MinEnchantWeapon", 0);
		MAX_ENCHANT_WEAPON = phantom.getProperty("MaxEnchantWeapon", 0);
		
		MIN_ENCHANT_JEWEL = phantom.getProperty("MinEnchantJewel", 0);
		MAX_ENCHANT_JEWEL = phantom.getProperty("MaxEnchantJewel", 0);
		
		//Armors Robe
		PHANTOM_ROB_ARMOR_1 = phantom.getProperty("ListRobeArmor1", "0");
		LIST_PHANTOM_ROB_ARMOR_1 = new ArrayList<>();
		for (String listid : PHANTOM_ROB_ARMOR_1.split(","))
		{
			LIST_PHANTOM_ROB_ARMOR_1.add(Integer.parseInt(listid));
		}
		
		PHANTOM_ROB_ARMOR_2 = phantom.getProperty("ListRobeArmor2", "0");
		LIST_PHANTOM_ROB_ARMOR_2 = new ArrayList<>();
		for (String listid : PHANTOM_ROB_ARMOR_2.split(","))
		{
			LIST_PHANTOM_ROB_ARMOR_2.add(Integer.parseInt(listid));
		}
		
		PHANTOM_ROB_ARMOR_3 = phantom.getProperty("ListRobeArmor3", "0");
		LIST_PHANTOM_ROB_ARMOR_3 = new ArrayList<>();
		for (String listid : PHANTOM_ROB_ARMOR_3.split(","))
		{
			LIST_PHANTOM_ROB_ARMOR_3.add(Integer.parseInt(listid));
		}
		
		PHANTOM_ROB_ARMOR_4 = phantom.getProperty("ListRobeArmor4", "0");
		LIST_PHANTOM_ROB_ARMOR_4 = new ArrayList<>();
		for (String listid : PHANTOM_ROB_ARMOR_4.split(","))
		{
			LIST_PHANTOM_ROB_ARMOR_4.add(Integer.parseInt(listid));
		}
		
		//Armors Heavy
		PHANTOM_HEAVY_ARMOR_1 = phantom.getProperty("ListHeavyArmor1", "0");
		LIST_PHANTOM_HEAVY_ARMOR_1 = new ArrayList<>();
		for (String listid : PHANTOM_HEAVY_ARMOR_1.split(","))
		{
			LIST_PHANTOM_HEAVY_ARMOR_1.add(Integer.parseInt(listid));
		}
		
		PHANTOM_HEAVY_ARMOR_2 = phantom.getProperty("ListHeavyArmor2", "0");
		LIST_PHANTOM_HEAVY_ARMOR_2 = new ArrayList<>();
		for (String listid : PHANTOM_HEAVY_ARMOR_2.split(","))
		{
			LIST_PHANTOM_HEAVY_ARMOR_2.add(Integer.parseInt(listid));
		}
		
		PHANTOM_HEAVY_ARMOR_3 = phantom.getProperty("ListHeavyArmor3", "0");
		LIST_PHANTOM_HEAVY_ARMOR_3 = new ArrayList<>();
		for (String listid : PHANTOM_HEAVY_ARMOR_3.split(","))
		{
			LIST_PHANTOM_HEAVY_ARMOR_3.add(Integer.parseInt(listid));
		}
		
		PHANTOM_HEAVY_ARMOR_4 = phantom.getProperty("ListHeavyArmor4", "0");
		LIST_PHANTOM_HEAVY_ARMOR_4 = new ArrayList<>();
		for (String listid : PHANTOM_HEAVY_ARMOR_4.split(","))
		{
			LIST_PHANTOM_HEAVY_ARMOR_4.add(Integer.parseInt(listid));
		}
		
		//Armors Light
		PHANTOM_LIGHT_ARMOR_1 = phantom.getProperty("ListLightArmor1", "0");
		LIST_PHANTOM_LIGHT_ARMOR_1 = new ArrayList<>();
		for (String listid : PHANTOM_LIGHT_ARMOR_1.split(","))
		{
			LIST_PHANTOM_LIGHT_ARMOR_1.add(Integer.parseInt(listid));
		}
		
		PHANTOM_LIGHT_ARMOR_2 = phantom.getProperty("ListLightArmor2", "0");
		LIST_PHANTOM_LIGHT_ARMOR_2 = new ArrayList<>();
		for (String listid : PHANTOM_LIGHT_ARMOR_2.split(","))
		{
			LIST_PHANTOM_LIGHT_ARMOR_2.add(Integer.parseInt(listid));
		}
		
		PHANTOM_LIGHT_ARMOR_3 = phantom.getProperty("ListLightArmor3", "0");
		LIST_PHANTOM_LIGHT_ARMOR_3 = new ArrayList<>();
		for (String listid : PHANTOM_LIGHT_ARMOR_3.split(","))
		{
			LIST_PHANTOM_LIGHT_ARMOR_3.add(Integer.parseInt(listid));
		}
		
		PHANTOM_LIGHT_ARMOR_4 = phantom.getProperty("ListLightArmor4", "0");
		LIST_PHANTOM_LIGHT_ARMOR_4 = new ArrayList<>();
		for (String listid : PHANTOM_LIGHT_ARMOR_4.split(","))
		{
			LIST_PHANTOM_LIGHT_ARMOR_4.add(Integer.parseInt(listid));
		}
		
		//Bow Fake
		FAKE_BOW_ID = phantom.getProperty("FakeBowIDList", "");
		LIST_FAKE_BOW = new ArrayList<>();
		for (String itemId : FAKE_BOW_ID.split(","))
		{
			LIST_FAKE_BOW.add(Integer.parseInt(itemId));
		}

		//Dagger Fake
		FAKE_DAGGER_ID = phantom.getProperty("FakeDaggerIDList", "");
		LIST_FAKE_DAGGER = new ArrayList<>();
		for (String itemId : FAKE_DAGGER_ID.split(","))
		{
			LIST_FAKE_DAGGER.add(Integer.parseInt(itemId));
		}
		
		//Sword Fake
		FAKE_SWORD_ID = phantom.getProperty("FakeSwordIDList", "");
		LIST_FAKE_SWORD = new ArrayList<>();
		for (String itemId : FAKE_SWORD_ID.split(","))
		{
			LIST_FAKE_SWORD.add(Integer.parseInt(itemId));
		}
		
		//Spear Fake
		FAKE_SPEAR_ID = phantom.getProperty("FakeSpearIDList", "");
		LIST_FAKE_SPEAR = new ArrayList<>();
		for (String itemId : FAKE_SPEAR_ID.split(","))
		{
			LIST_FAKE_SPEAR.add(Integer.parseInt(itemId));
		}
		
		//Dual Fake
		FAKE_DUAL_ID = phantom.getProperty("FakeDualIDList", "");
		LIST_FAKE_DUAL = new ArrayList<>();
		for (String itemId : FAKE_DUAL_ID.split(","))
		{
			LIST_FAKE_DUAL.add(Integer.parseInt(itemId));
		}
		
		//Fist Fake
		FAKE_FIST_ID = phantom.getProperty("FakeFistIDList", "");
		LIST_FAKE_FIST = new ArrayList<>();
		for (String itemId : FAKE_FIST_ID.split(","))
		{
			LIST_FAKE_FIST.add(Integer.parseInt(itemId));
		}
		
		//BigSword Fake
		FAKE_BIGSWORD_ID = phantom.getProperty("FakeBigSwordIDList", "");
		LIST_FAKE_BIG_SWORD = new ArrayList<>();
		for (String itemId : FAKE_BIGSWORD_ID.split(","))
		{
			LIST_FAKE_BIG_SWORD.add(Integer.parseInt(itemId));
		}
		
		//Magic Fake
		FAKE_MAGIC_ID = phantom.getProperty("FakeMagicWeaponIDList", "");
		LIST_FAKE_MAGIC = new ArrayList<>();
		for (String itemId : FAKE_MAGIC_ID.split(","))
		{
			LIST_FAKE_MAGIC.add(Integer.parseInt(itemId));
		}
		
		//Shield Fake
		FAKE_SHIELD_ID = phantom.getProperty("FakeShieldIDList", "");
		LIST_FAKE_SHIELD = new ArrayList<>();
		for (String itemId : FAKE_SHIELD_ID.split(","))
		{
			LIST_FAKE_SHIELD.add(Integer.parseInt(itemId));
		}
		
		//Jewels Set
		String[] propertySplit = phantom.getProperty("JewelSetList1", "0,0").split(";");
		LIST_PHANTOM_JEWELS_1.clear();
		for (String reward : propertySplit)
		{
			String[] rewardSplit = reward.split(",");
			if (rewardSplit.length != 2) 
			{
				_log.warning("JewelSetList1[FakePlayerConfig.load()]: invalid config property -> JewelSetList1 \"" + reward + "\"");
			} 
			else 
			{
				try
				{
					LIST_PHANTOM_JEWELS_1.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
				}
				catch (NumberFormatException nfe)
				{
					if (!reward.isEmpty())
					{
						_log.warning("JewelSetList1[FakePlayerConfig.load()]: invalid config property -> JewelSetList1 \"" + reward + "\"");
					}
				}
			}
		}
		propertySplit = phantom.getProperty("JewelSetList2", "0,0").split(";");
		LIST_PHANTOM_JEWELS_2.clear();
		for (String reward : propertySplit)
		{
			String[] rewardSplit = reward.split(",");
			if (rewardSplit.length != 2) 
			{
				_log.warning("JewelSetList2[FakePlayerConfig.load()]: invalid config property -> JewelSetList2 \"" + reward + "\"");
			} 
			else 
			{
				try
				{
					LIST_PHANTOM_JEWELS_2.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
				}
				catch (NumberFormatException nfe)
				{
					if (!reward.isEmpty()) 
					{
						_log.warning("JewelSetList2[FakePlayerConfig.load()]: invalid config property -> JewelSetList2 \"" + reward + "\"");
					}
				}
			}
		}

		//Accessory Fake
		ALLOW_FAKE_PLAYERS_ACCESSORY = phantom.getProperty("AllowFakePlayerAccesory", false);
		FAKE_ACCESSORY_ID = phantom.getProperty("FakeAccessoryIDList", "");
		LIST_FAKE_ACCESSORY = new ArrayList<>();
		for (String itemId : FAKE_ACCESSORY_ID.split(","))
		{
			LIST_FAKE_ACCESSORY.add(Integer.parseInt(itemId));
		}
		
		//Buffer List
		NUKER_BUFFER = phantom.getProperty("NukerBufferList", "0");
		NUKER_BUFFER_LIST = new ArrayList<>();
		for (String listid : NUKER_BUFFER.split(","))
		{
			NUKER_BUFFER_LIST.add(Integer.parseInt(listid));
		}

		ARCHER_BUFFER = phantom.getProperty("ArcherBufferList", "0");
		ARCHER_BUFFER_LIST = new ArrayList<>();
		for (String listid : ARCHER_BUFFER.split(","))
		{
			ARCHER_BUFFER_LIST.add(Integer.parseInt(listid));
		}

		DAGGER_BUFFER = phantom.getProperty("DaggerBufferList", "0");
		DAGGER_BUFFER_LIST = new ArrayList<>();
		for (String listid : DAGGER_BUFFER.split(","))
		{
			DAGGER_BUFFER_LIST.add(Integer.parseInt(listid));
		}

		WARRIOR_BUFFER = phantom.getProperty("WarriorBufferList", "0");
		WARRIOR_BUFFER_LIST = new ArrayList<>();
		for (String listid : WARRIOR_BUFFER.split(","))
		{
			WARRIOR_BUFFER_LIST.add(Integer.parseInt(listid));
		}

		RANDOM_BUFFER = phantom.getProperty("RandomBufferList", "0");
		RANDOM_BUFFER_LIST = new ArrayList<>();
		for (String listid : RANDOM_BUFFER.split(","))
		{
			RANDOM_BUFFER_LIST.add(Integer.parseInt(listid));
		}
	}
	
	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}

	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();

		try
		{
			result.load(file);
		}
		catch (IOException e)
		{
			_log.warning("Error loading config : " + file.getName() + "!");
		}

		return result;
	}
}