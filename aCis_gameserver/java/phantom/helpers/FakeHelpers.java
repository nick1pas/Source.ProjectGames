package phantom.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phantom.FakePlayer;
import phantom.FakePlayerConfig;
import phantom.FakePlayerNameManager;
import phantom.ai.FakePlayerAI;
import phantom.ai.FakePlayerUtilsAI;
import phantom.ai.FallbackAI;
import phantom.ai.classes.AdventurerAI;
import phantom.ai.classes.ArchmageAI;
import phantom.ai.classes.CardinalAI;
import phantom.ai.classes.DominatorAI;
import phantom.ai.classes.DreadnoughtAI;
import phantom.ai.classes.DuelistAI;
import phantom.ai.classes.GhostHunterAI;
import phantom.ai.classes.GhostSentinelAI;
import phantom.ai.classes.GrandKhavatariAI;
import phantom.ai.classes.MoonlightSentinelAI;
import phantom.ai.classes.MysticMuseAI;
import phantom.ai.classes.SaggitariusAI;
import phantom.ai.classes.SoultakerAI;
import phantom.ai.classes.StormScreamerAI;
import phantom.ai.classes.TitanAI;
import phantom.ai.classes.WindRiderAI;
import net.sf.l2j.gameserver.datatables.CharTemplateTable;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.template.PcTemplate;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.util.Rnd;

public class FakeHelpers
{
	public static Class<? extends L2Character> getTestTargetClass()
	{
		return L2PcInstance.class;
	}
	
	public static int getTestTargetRange()
	{
		return 6000;
	}
	
	// Without Clan
	public static FakePlayer createRandomFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "RandomAcc";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getThirdClasses().get(Rnd.get(0, getThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);

		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	// With Clan
	public static FakePlayer createRandomClanFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "RandomClanAcc";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getThirdClasses().get(Rnd.get(0, getThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		L2Clan clan = ClanTable.getInstance().getClan(FakePlayerUtilsAI.getRandomClan());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (clan != null)
			clan.addClanMember(player);
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	// Without Clan
	public static FakePlayer createArcherFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "ArcherFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getArcherThirdClasses().get(Rnd.get(0, getArcherThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	// With Clan
	public static FakePlayer createArcherClanFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "ArcherClanFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getArcherThirdClasses().get(Rnd.get(0, getArcherThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		L2Clan clan = ClanTable.getInstance().getClan(FakePlayerUtilsAI.getRandomClan());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (clan != null)
			clan.addClanMember(player);
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	//Without Clan
	public static FakePlayer createNukerFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "NukeFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getNukerThirdClasses().get(Rnd.get(0, getNukerThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
	
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	//With Clan
	public static FakePlayer createNukerClanFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "NukeClanFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getNukerThirdClasses().get(Rnd.get(0, getNukerThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		L2Clan clan = ClanTable.getInstance().getClan(FakePlayerUtilsAI.getRandomClan());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (clan != null)
			clan.addClanMember(player);
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}

	//Without Clan
	public static FakePlayer createWarriorFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "WarriorFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getWarriorThirdClasses().get(Rnd.get(0, getWarriorThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	//With Clan
	public static FakePlayer createWarriorClanFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "WarriorClanFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getWarriorThirdClasses().get(Rnd.get(0, getWarriorThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		L2Clan clan = ClanTable.getInstance().getClan(FakePlayerUtilsAI.getRandomClan());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (clan != null)
			clan.addClanMember(player);
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	//Without Clan
	public static FakePlayer createDaggerFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "DaggerFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getDaggerThirdClasses().get(Rnd.get(0, getDaggerThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());

		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	//With Clan
	public static FakePlayer createDaggerClanFakePlayer()
	{
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "DaggerClanFakeAccount";
		
		String playerName = FakePlayerNameManager.INSTANCE.getRandomAvailableName();
		String playerTitle = FakePlayerNameManager.INSTANCE.getRandomTitleFromWordlist();
		
		ClassId classId = getDaggerThirdClasses().get(Rnd.get(0, getDaggerThirdClasses().size() - 1));
		
		final PcTemplate template = CharTemplateTable.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		L2Clan clan = ClanTable.getInstance().getClan(FakePlayerUtilsAI.getRandomClan());
		FakePlayer player = new FakePlayer(objectId, template, accountName, app);
		
		player.setName(playerName);
		player.setAccessLevel(0);

		player.setBaseClass(player.getClassId());
		
		if (clan != null)
			clan.addClanMember(player);
		
		if (!FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE.isEmpty())
			player.setTitle(FakePlayerConfig.FAKE_PLAYER_FIXED_TITLE);
		else
			player.setTitle(playerTitle);
		
		setLevel(player, 81);
		player.rewardSkills();
		
		//Equipment
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player, true);
		giveJewelsByClass(player, true);
		
		if (FakePlayerConfig.ALLOW_FAKE_PLAYERS_ACCESSORY)
			giveAcessoryByClass(player);
		
		giveBuffsByClass(player);
		
		player.heal();
		return player;
	}
	
	public static void giveArmorsByClass(FakePlayer player, boolean randomlyEnchant)
	{
		List<Integer> itemIds = new ArrayList<>();
		switch (player.getClassId())
		{
			case archmage:
			case soultaker:
			case hierophant:
			case arcanaLord:
			case cardinal:
			case mysticMuse:
			case elementalMaster:
			case evaSaint:
			case stormScreamer:
			case spectralMaster:
			case shillienSaint:
			case dominator:
			case doomcryer:
				int randomRobe = Rnd.get(4);
				switch (randomRobe)
				{
					case 0:
						itemIds = FakePlayerConfig.LIST_PHANTOM_ROB_ARMOR_1;
						break;
					case 1:
						itemIds = FakePlayerConfig.LIST_PHANTOM_ROB_ARMOR_2;
						break;
					case 2:
						itemIds = FakePlayerConfig.LIST_PHANTOM_ROB_ARMOR_3;
						break;
					case 3:
						itemIds = FakePlayerConfig.LIST_PHANTOM_ROB_ARMOR_4;
						break;
					default:
						itemIds = FakePlayerConfig.LIST_PHANTOM_ROB_ARMOR_1;
						break;
				}
				break;
			case duelist:
			case dreadnought:
			case phoenixKnight:
			case swordMuse:
			case hellKnight:
			case spectralDancer:
			case evaTemplar:
			case shillienTemplar:
			case titan:
			case maestro:
				int randomHeavy = Rnd.get(4);
				switch (randomHeavy)
				{
					case 0:
						itemIds = FakePlayerConfig.LIST_PHANTOM_HEAVY_ARMOR_1;
						break;
					case 1:
						itemIds = FakePlayerConfig.LIST_PHANTOM_HEAVY_ARMOR_2;
						break;
					case 2:
						itemIds = FakePlayerConfig.LIST_PHANTOM_HEAVY_ARMOR_3;
						break;
					case 3:
						itemIds = FakePlayerConfig.LIST_PHANTOM_HEAVY_ARMOR_4;
						break;
					default:
						itemIds = FakePlayerConfig.LIST_PHANTOM_HEAVY_ARMOR_1;
						break;
				}
				break;
			case sagittarius:
			case adventurer:
			case windRider:
			case moonlightSentinel:
			case ghostHunter:
			case ghostSentinel:
			case fortuneSeeker:
			case grandKhauatari:
				int randomLight = Rnd.get(4);
				switch (randomLight)
				{
					case 0:
						itemIds = FakePlayerConfig.LIST_PHANTOM_LIGHT_ARMOR_1;
						break;
					case 1:
						itemIds = FakePlayerConfig.LIST_PHANTOM_LIGHT_ARMOR_2;
						break;
					case 2:
						itemIds = FakePlayerConfig.LIST_PHANTOM_LIGHT_ARMOR_3;
						break;
					case 3:
						itemIds = FakePlayerConfig.LIST_PHANTOM_LIGHT_ARMOR_4;
						break;
					default:
						itemIds = FakePlayerConfig.LIST_PHANTOM_LIGHT_ARMOR_1;
						break;
				}
				break;
			default:
				break;
		}
		for (int id : itemIds)
		{
			player.getInventory().addItem("Armors", id, 1, player, null);
			ItemInstance item = player.getInventory().getItemByItemId(id);
			
			if (randomlyEnchant)
				item.setEnchantLevel(Rnd.get(FakePlayerConfig.MIN_ENCHANT_ARMOR, FakePlayerConfig.MAX_ENCHANT_ARMOR));
			
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
			player.broadcastCharInfo();
		}
	}
	
	public static void giveWeaponsByClass(FakePlayer player, boolean randomlyEnchant)
	{
		List<Integer> itemIds = new ArrayList<>();
		switch (player.getClassId())
		{
			case ghostHunter:
			case windRider:
			case adventurer:
				itemIds = Arrays.asList(getRandomDagger());
				break;
			case sagittarius:
			case moonlightSentinel:
			case ghostSentinel:
				itemIds = Arrays.asList(getRandomBow());
				break;
			case phoenixKnight:
			case swordMuse:
			case hellKnight:
			case evaTemplar:
			case shillienKnight:
				itemIds = Arrays.asList(getRandomSword(), getRandomShield());
				break;
			case fortuneSeeker:
			case maestro:
				itemIds = Arrays.asList(6585, 6377);
				break;
			case titan:
				itemIds = Arrays.asList(getRandomBigSword());
				break;
			case duelist:
			case spectralDancer:
				itemIds = Arrays.asList(getRandomDualSword());
				break;
			case dreadnought:
				itemIds = Arrays.asList(getRandomSpear());
				break;
			case archmage:
			case soultaker:
			case hierophant:
			case arcanaLord:
			case cardinal:
			case mysticMuse:
			case elementalMaster:
			case evaSaint:
			case stormScreamer:
			case spectralMaster:
			case shillienSaint:
			case dominator:
			case doomcryer:
				itemIds = Arrays.asList(getRandomMagicWeapon(), getRandomShield());
				break;
			case grandKhauatari:
				itemIds = Arrays.asList(getRandomFist());
				break;
			default:
				break;
		}
		for (int id : itemIds)
		{
			player.getInventory().addItem("Weapon", id, 1, player, null);
			ItemInstance item = player.getInventory().getItemByItemId(id);
			
			if (randomlyEnchant)
				item.setEnchantLevel(Rnd.get(FakePlayerConfig.MIN_ENCHANT_WEAPON, FakePlayerConfig.MAX_ENCHANT_WEAPON));
			
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
		}
	}
	
	public static void giveJewelsByClass(FakePlayer player, boolean randomlyEnchant)
	{
		List<int[]> itemIds = new ArrayList<>();
		switch (player.getClassId())
		{
			case ghostHunter:
			case windRider:
			case adventurer:
			case sagittarius:
			case moonlightSentinel:
			case ghostSentinel:
			case phoenixKnight:
			case swordMuse:
			case hellKnight:
			case evaTemplar:
			case shillienKnight:
			case fortuneSeeker:
			case maestro:
			case titan:
			case duelist:
			case spectralDancer:
			case dreadnought:
			case archmage:
			case soultaker:
			case hierophant:
			case arcanaLord:
			case cardinal:
			case mysticMuse:
			case elementalMaster:
			case evaSaint:
			case stormScreamer:
			case spectralMaster:
			case shillienSaint:
			case dominator:
			case doomcryer:
			case grandKhauatari:
				int randomJewels = Rnd.get(2);
				switch (randomJewels)
				{
					case 0:
						itemIds = FakePlayerConfig.LIST_PHANTOM_JEWELS_1;
						break;
					case 1:
						itemIds = FakePlayerConfig.LIST_PHANTOM_JEWELS_2;
						break;
					default:
						itemIds = FakePlayerConfig.LIST_PHANTOM_JEWELS_1;
						break;
				}
				break;
			default:
				break;
		}
		for (int[] id : itemIds)
		{
			ItemInstance item = player.getInventory().addItem("Jewels", id[0], id[1], player, null);

			if (randomlyEnchant)
				item.setEnchantLevel(Rnd.get(FakePlayerConfig.MIN_ENCHANT_JEWEL, FakePlayerConfig.MAX_ENCHANT_JEWEL));
			
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
		}
	}
	
	public static void giveAcessoryByClass(FakePlayer player)
	{
		List<Integer> itemIds = new ArrayList<>();
		switch (player.getClassId())
		{
			case ghostHunter:
			case windRider:
			case adventurer:
			case sagittarius:
			case moonlightSentinel:
			case ghostSentinel:
			case phoenixKnight:
			case swordMuse:
			case hellKnight:
			case evaTemplar:
			case shillienKnight:
			case fortuneSeeker:
			case maestro:
			case titan:
			case duelist:
			case spectralDancer:
			case dreadnought:
			case archmage:
			case soultaker:
			case hierophant:
			case arcanaLord:
			case cardinal:
			case mysticMuse:
			case elementalMaster:
			case evaSaint:
			case stormScreamer:
			case spectralMaster:
			case shillienSaint:
			case dominator:
			case doomcryer:
			case grandKhauatari:
				itemIds = Arrays.asList(getRandomAcessory());
				break;
			default:
				break;
		}
		for (int id : itemIds)
		{
			player.getInventory().addItem("Accessory", id, 1, player, null);
			ItemInstance item = player.getInventory().getItemByItemId(id);
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
		}
	}
	
	public static void giveBuffsByClass(FakePlayer player)
	{
		List<Integer> buffList = new ArrayList<>();
		switch (player.getClassId())
		{
		    case archmage:
			case soultaker:
			case mysticMuse:
			case stormScreamer:
			case dominator:
				buffList = FakePlayerConfig.NUKER_BUFFER_LIST;
				break;
		    case sagittarius:
			case moonlightSentinel:
			case ghostSentinel:
				buffList = FakePlayerConfig.ARCHER_BUFFER_LIST;
				break;
			case duelist:
			case grandKhauatari:
				buffList = FakePlayerConfig.WARRIOR_BUFFER_LIST;
				break;
			case adventurer:
				buffList = FakePlayerConfig.DAGGER_BUFFER_LIST;
				break;
			case ghostHunter:
			case windRider:
			case phoenixKnight:
			case swordMuse:
			case hellKnight:
			case evaTemplar:
			case shillienKnight:
			case fortuneSeeker:
			case maestro:
			case titan:
			case spectralDancer:
			case dreadnought:
			case hierophant:
			case arcanaLord:
			case cardinal:
			case elementalMaster:
			case evaSaint:
			case spectralMaster:
			case shillienSaint:
			case doomcryer:
				buffList = FakePlayerConfig.RANDOM_BUFFER_LIST;
				break;
			default:
				break;
		}
		for (Integer skillid : buffList)
		{
			L2Skill skill = SkillTable.getInstance().getInfo(skillid, SkillTable.getInstance().getMaxLevel(skillid));
			if (skill != null)
				skill.getEffects(player, player);
		}
	}

	public static List<ClassId> getThirdClasses()
	{
		List<ClassId> classes = new ArrayList<>();
		
		classes.add(ClassId.sagittarius);
		classes.add(ClassId.archmage);
		classes.add(ClassId.soultaker);
		classes.add(ClassId.mysticMuse);
		classes.add(ClassId.stormScreamer);
		classes.add(ClassId.moonlightSentinel);
		classes.add(ClassId.ghostSentinel);
		classes.add(ClassId.adventurer);
		classes.add(ClassId.windRider);
		classes.add(ClassId.dominator);
		classes.add(ClassId.titan);
		classes.add(ClassId.cardinal);
		classes.add(ClassId.duelist);
		classes.add(ClassId.grandKhauatari);
		classes.add(ClassId.dreadnought);
		
		return classes;
	}
	
	public static List<ClassId> getArcherThirdClasses()
	{
		List<ClassId> classes = new ArrayList<>();
		
		classes.add(ClassId.sagittarius);
		classes.add(ClassId.moonlightSentinel);
		classes.add(ClassId.ghostSentinel);

		return classes;
	}
	
	public static List<ClassId> getNukerThirdClasses()
	{
		List<ClassId> classes = new ArrayList<>();
		
		classes.add(ClassId.archmage);
		classes.add(ClassId.soultaker);
		classes.add(ClassId.mysticMuse);
		classes.add(ClassId.stormScreamer);
		classes.add(ClassId.dominator);

		return classes;
	}
	
	public static List<ClassId> getWarriorThirdClasses()
	{
		List<ClassId> classes = new ArrayList<>();
		
		classes.add(ClassId.grandKhauatari);
		classes.add(ClassId.duelist);

		return classes;
	}
	
	public static List<ClassId> getDaggerThirdClasses()
	{
		List<ClassId> classes = new ArrayList<>();
		
		classes.add(ClassId.adventurer);

		return classes;
	}
	
	public static Map<ClassId, Class<? extends FakePlayerAI>> getAllAIs()
	{
		Map<ClassId, Class<? extends FakePlayerAI>> ais = new HashMap<>();
		ais.put(ClassId.stormScreamer, StormScreamerAI.class);
		ais.put(ClassId.mysticMuse, MysticMuseAI.class);
		ais.put(ClassId.archmage, ArchmageAI.class);
		ais.put(ClassId.soultaker, SoultakerAI.class);
		ais.put(ClassId.sagittarius, SaggitariusAI.class);
		ais.put(ClassId.moonlightSentinel, MoonlightSentinelAI.class);
		ais.put(ClassId.ghostSentinel, GhostSentinelAI.class);
		ais.put(ClassId.adventurer, AdventurerAI.class);
		ais.put(ClassId.windRider, WindRiderAI.class);
		ais.put(ClassId.ghostHunter, GhostHunterAI.class);
		ais.put(ClassId.dominator, DominatorAI.class);
		ais.put(ClassId.titan, TitanAI.class);
		ais.put(ClassId.cardinal, CardinalAI.class);
		ais.put(ClassId.duelist, DuelistAI.class);
		ais.put(ClassId.grandKhauatari, GrandKhavatariAI.class);
		ais.put(ClassId.dreadnought, DreadnoughtAI.class);
		return ais;
	}
	
	public static PcAppearance getRandomAppearance(Race race)
	{
		boolean randomSex = Rnd.get(0, 1) != 0;
		int hairStyle = Rnd.get(0, 3);
		int hairColor = Rnd.get(0, 3);
		int faceId = Rnd.get(0, 2);
		
		return new PcAppearance((byte) faceId, (byte) hairColor, (byte) hairStyle, randomSex);
	}
	
	public static void setLevel(FakePlayer player, int level)
	{
		if (level >= 1 && level <= Experience.MAX_LEVEL)
		{
			long pXp = player.getExp();
			long tXp = Experience.LEVEL[81];
			
			if (pXp > tXp)
				player.removeExpAndSp(pXp - tXp, 0);
			else if (pXp < tXp)
				player.addExpAndSp(tXp - pXp, 0);
		}
	}
	
	public static Class<? extends FakePlayerAI> getAIbyClassId(ClassId classId)
	{
		Class<? extends FakePlayerAI> ai = getAllAIs().get(classId);
		if (ai == null)
			return FallbackAI.class;
		
		return ai;
	}
	
	public static int getRandomDagger()
	{
		return FakePlayerConfig.LIST_FAKE_DAGGER.get(Rnd.get(FakePlayerConfig.LIST_FAKE_DAGGER.size()));
	}
	
	public static int getRandomBow()
	{
		return FakePlayerConfig.LIST_FAKE_BOW.get(Rnd.get(FakePlayerConfig.LIST_FAKE_BOW.size()));
	}
	
	public static int getRandomSword()
	{
		return FakePlayerConfig.LIST_FAKE_SWORD.get(Rnd.get(FakePlayerConfig.LIST_FAKE_SWORD.size()));
	}
	
	public static int getRandomSpear()
	{
		return FakePlayerConfig.LIST_FAKE_SPEAR.get(Rnd.get(FakePlayerConfig.LIST_FAKE_SPEAR.size()));
	}
	
	public static int getRandomDualSword()
	{
		return FakePlayerConfig.LIST_FAKE_DUAL.get(Rnd.get(FakePlayerConfig.LIST_FAKE_DUAL.size()));
	}
	
	public static int getRandomFist()
	{
		return FakePlayerConfig.LIST_FAKE_FIST.get(Rnd.get(FakePlayerConfig.LIST_FAKE_FIST.size()));
	}
	
	public static int getRandomBigSword()
	{
		return FakePlayerConfig.LIST_FAKE_BIG_SWORD.get(Rnd.get(FakePlayerConfig.LIST_FAKE_BIG_SWORD.size()));
	}
	
	public static int getRandomMagicWeapon()
	{
		return FakePlayerConfig.LIST_FAKE_MAGIC.get(Rnd.get(FakePlayerConfig.LIST_FAKE_MAGIC.size()));
	}
	
	public static int getRandomShield()
	{
		return FakePlayerConfig.LIST_FAKE_SHIELD.get(Rnd.get(FakePlayerConfig.LIST_FAKE_SHIELD.size()));
	}
	
	public static int getRandomAcessory()
	{
		return FakePlayerConfig.LIST_FAKE_ACCESSORY.get(Rnd.get(FakePlayerConfig.LIST_FAKE_ACCESSORY.size()));
	}
}