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
package net.sf.l2j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.gameserver.model.holder.ItemHolder;
import net.sf.l2j.gameserver.model.holder.RewardHolder;
import net.sf.l2j.gameserver.util.FloodProtectorConfig;
import net.sf.l2j.util.StringUtil;

/**
 * This class contains global server configuration.<br>
 * It has static final fields initialized from configuration files.<br>
 * @author mkizub
 */
public final class Config
{
	protected static final Logger _log = Logger.getLogger(Config.class.getName());
	
	public static final String BANNED_IP_XML = "./config/banned.xml";
	public static final String CLANS_FILE = "./config/clans.properties";
	public static final String EVENTS_FILE = "./config/events.properties";
	public static final String FLOOD_PROTECTOR_FILE = "./config/floodprotector.properties";
	public static final String HEXID_FILE = "./config/hexid.txt";
	public static final String SAY_FILTER_FILE = "./config/sayfilter.txt";
	public static final String LOGIN_CONFIGURATION_FILE = "./config/loginserver.properties";
	public static final String NPCS_FILE = "./config/npcs.properties";
	public static final String PLAYERS_FILE = "./config/players.properties";
	public static final String SERVER_FILE = "./config/server.properties";
	public static final String SIEGE_FILE = "./config/siege.properties";
	public static final String L2JMOD_FILE = "./config/custom/l2jmod.properties";
	public static final String L2JEVENT_FILE = "./config/custom/l2jevents.properties";
 	public static final String ANNOUCEMENTS_FILE = "./config/custom/annoucements.properties";
 	public static final String STARTUP_FILE = "./config/custom/startup.properties";
 	public static final String BOOM_FILE = "./config/custom/boom.properties";
	public static final String BALANCE_FILE = "./config/balance/balance.properties";
 	public static final String CLASS_DAMAGES_FILE = "./config/balance/class_damages.properties";
 	public static final String OLY_CLASS_DAMAGES_FILE = "./config/balance/oly_class_damages.properties";
 	
	// --------------------------------------------------
	// Clans settings
	// --------------------------------------------------
	
	/** Clans */
	public static int MAX_CLAN_MEMBERS;
	public static int MAX_CLAN_MEMBERS_ROYALS;
	public static int MAX_CLAN_MEMBERS_KNIGHTS;
	
	public static int ALT_CLAN_JOIN_DAYS;
	public static int ALT_CLAN_CREATE_DAYS;
	public static int ALT_CLAN_DISSOLVE_DAYS;
	public static int ALT_ALLY_JOIN_DAYS_WHEN_LEAVED;
	public static int ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED;
	public static int ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED;
	public static int ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED;
	public static int ALT_MAX_NUM_OF_CLANS_IN_ALLY;
	public static int ALT_CLAN_MEMBERS_FOR_WAR;
	public static int ALT_CLAN_WAR_PENALTY_WHEN_ENDED;
	public static boolean ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH;
	public static boolean REMOVE_CASTLE_CIRCLETS;
	
    public static boolean ENABLE_WINNNER_REWARD_SIEGE_CLAN;
    public static int[][] REWARD_WINNER_SIEGE_CLAN;
    public static int[][] LEADER_REWARD_WINNER_SIEGE_CLAN;
    public static int PLAYER_COUNT_KILLS_INSIEGE;
	
	/** Manor */
	public static int ALT_MANOR_REFRESH_TIME;
	public static int ALT_MANOR_REFRESH_MIN;
	public static int ALT_MANOR_APPROVE_TIME;
	public static int ALT_MANOR_APPROVE_MIN;
	public static int ALT_MANOR_MAINTENANCE_PERIOD;
	public static boolean ALT_MANOR_SAVE_ALL_ACTIONS;
	public static int ALT_MANOR_SAVE_PERIOD_RATE;
	
	/** Clan Hall function */
	public static long CH_TELE_FEE_RATIO;
	public static int CH_TELE1_FEE;
	public static int CH_TELE2_FEE;
	public static long CH_ITEM_FEE_RATIO;
	public static int CH_ITEM1_FEE;
	public static int CH_ITEM2_FEE;
	public static int CH_ITEM3_FEE;
	public static long CH_MPREG_FEE_RATIO;
	public static int CH_MPREG1_FEE;
	public static int CH_MPREG2_FEE;
	public static int CH_MPREG3_FEE;
	public static int CH_MPREG4_FEE;
	public static int CH_MPREG5_FEE;
	public static long CH_HPREG_FEE_RATIO;
	public static int CH_HPREG1_FEE;
	public static int CH_HPREG2_FEE;
	public static int CH_HPREG3_FEE;
	public static int CH_HPREG4_FEE;
	public static int CH_HPREG5_FEE;
	public static int CH_HPREG6_FEE;
	public static int CH_HPREG7_FEE;
	public static int CH_HPREG8_FEE;
	public static int CH_HPREG9_FEE;
	public static int CH_HPREG10_FEE;
	public static int CH_HPREG11_FEE;
	public static int CH_HPREG12_FEE;
	public static int CH_HPREG13_FEE;
	public static long CH_EXPREG_FEE_RATIO;
	public static int CH_EXPREG1_FEE;
	public static int CH_EXPREG2_FEE;
	public static int CH_EXPREG3_FEE;
	public static int CH_EXPREG4_FEE;
	public static int CH_EXPREG5_FEE;
	public static int CH_EXPREG6_FEE;
	public static int CH_EXPREG7_FEE;
	public static long CH_SUPPORT_FEE_RATIO;
	public static int CH_SUPPORT1_FEE;
	public static int CH_SUPPORT2_FEE;
	public static int CH_SUPPORT3_FEE;
	public static int CH_SUPPORT4_FEE;
	public static int CH_SUPPORT5_FEE;
	public static int CH_SUPPORT6_FEE;
	public static int CH_SUPPORT7_FEE;
	public static int CH_SUPPORT8_FEE;
	public static long CH_CURTAIN_FEE_RATIO;
	public static int CH_CURTAIN1_FEE;
	public static int CH_CURTAIN2_FEE;
	public static long CH_FRONT_FEE_RATIO;
	public static int CH_FRONT1_FEE;
	public static int CH_FRONT2_FEE;
	
	// --------------------------------------------------
	// Events settings
	// --------------------------------------------------
	
	/** Olympiad */
	public static int ALT_OLY_START_TIME;
	public static int ALT_OLY_MIN;
	public static long ALT_OLY_CPERIOD;
	public static long ALT_OLY_BATTLE;
	public static long ALT_OLY_WPERIOD;
	public static long ALT_OLY_VPERIOD;
	public static int ALT_OLY_WAIT_TIME;
	public static int ALT_OLY_WAIT_BATTLE;
	public static int ALT_OLY_WAIT_END;
	public static int ALT_OLY_START_POINTS;
	public static int ALT_OLY_WEEKLY_POINTS;
	public static int ALT_OLY_MIN_MATCHES;
	public static int ALT_OLY_CLASSED;
	public static int ALT_OLY_NONCLASSED;
	public static int[][] ALT_OLY_CLASSED_REWARD;
	public static int[][] ALT_OLY_NONCLASSED_REWARD;
	public static int ALT_OLY_COMP_RITEM;
	public static int ALT_OLY_GP_PER_POINT;
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int ALT_OLY_MAX_POINTS;
	public static int ALT_OLY_DIVIDER_CLASSED;
	public static int ALT_OLY_DIVIDER_NON_CLASSED;
	public static boolean ALT_OLY_ANNOUNCE_GAMES;
	public static int ALT_OLY_ENCHANT_LIMIT;
	public static boolean ALT_OLY_USE_CUSTOM_PERIOD_SETTINGS;
	public static String ALT_OLY_PERIOD;
	public static int ALT_OLY_PERIOD_MULTIPLIER;
	
	/** SevenSigns Festival */
	public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
	public static boolean ALT_GAME_CASTLE_DAWN;
	public static boolean ALT_GAME_CASTLE_DUSK;
	public static int ALT_FESTIVAL_MIN_PLAYER;
	public static int ALT_MAXIMUM_PLAYER_CONTRIB;
	public static long ALT_FESTIVAL_MANAGER_START;
	public static long ALT_FESTIVAL_LENGTH;
	public static long ALT_FESTIVAL_CYCLE_LENGTH;
	public static long ALT_FESTIVAL_FIRST_SPAWN;
	public static long ALT_FESTIVAL_FIRST_SWARM;
	public static long ALT_FESTIVAL_SECOND_SPAWN;
	public static long ALT_FESTIVAL_SECOND_SWARM;
	public static long ALT_FESTIVAL_CHEST_SPAWN;
	public static boolean ALT_SEVENSIGNS_LAZY_UPDATE;
	
	/** Four Sepulchers */
	public static int FS_TIME_ATTACK;
	public static int FS_TIME_COOLDOWN;
	public static int FS_TIME_ENTRY;
	public static int FS_TIME_WARMUP;
	public static int FS_PARTY_MEMBER_COUNT;
	
	/** dimensional rift */
	public static int RIFT_MIN_PARTY_SIZE;
	public static int RIFT_SPAWN_DELAY;
	public static int RIFT_MAX_JUMPS;
	public static int RIFT_AUTO_JUMPS_TIME_MIN;
	public static int RIFT_AUTO_JUMPS_TIME_MAX;
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;
	public static double RIFT_BOSS_ROOM_TIME_MUTIPLY;
	
	/** Wedding system */
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;
	
	/** Lottery */
	public static int ALT_LOTTERY_PRIZE;
	public static int ALT_LOTTERY_TICKET_PRICE;
	public static double ALT_LOTTERY_5_NUMBER_RATE;
	public static double ALT_LOTTERY_4_NUMBER_RATE;
	public static double ALT_LOTTERY_3_NUMBER_RATE;
	public static int ALT_LOTTERY_2_AND_1_NUMBER_PRIZE;
	
	/** Fishing tournament */
	public static boolean ALT_FISH_CHAMPIONSHIP_ENABLED;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_ITEM;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_1;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_2;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_3;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_4;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_5;
	
	// --------------------------------------------------
	// HexID
	// --------------------------------------------------
	
	public static int SERVER_ID;
	public static byte[] HEX_ID;
	
	// --------------------------------------------------
	// FloodProtectors
	// --------------------------------------------------
	
	public static FloodProtectorConfig FLOOD_PROTECTOR_ROLL_DICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SHOUT_VOICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_TRADE_CHAT;
	public static FloodProtectorConfig FLOOD_PROTECTOR_HERO_VOICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SUBCLASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_DROP_ITEM;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SERVER_BYPASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MULTISELL;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANUFACTURE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANOR;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SENDMAIL;
	public static FloodProtectorConfig FLOOD_PROTECTOR_CHARACTER_SELECT;
	
	// --------------------------------------------------
	// Loginserver
	// --------------------------------------------------
	
	public static String LOGIN_BIND_ADDRESS;
	public static int PORT_LOGIN;
	
	public static boolean ACCEPT_NEW_GAMESERVER;
	public static int REQUEST_ID;
	public static boolean ACCEPT_ALTERNATE_ID;
	public static String GAMESERVER_SESSION_KEY;
	
	public static int LOGIN_TRY_BEFORE_BAN;
	public static int LOGIN_BLOCK_AFTER_BAN;
	
	public static boolean LOG_LOGIN_CONTROLLER;
	
	public static boolean SHOW_LICENCE;
	public static int IP_UPDATE_TIME;
	public static boolean FORCE_GGAUTH;
	
	public static boolean AUTO_CREATE_ACCOUNTS;
	
	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int NORMAL_CONNECTION_TIME;
	public static int FAST_CONNECTION_TIME;
	public static int MAX_CONNECTION_PER_IP;
	
	// --------------------------------------------------
	// NPCs / Monsters
	// --------------------------------------------------
	
	/** Champion Mod */
	public static int CHAMPION_FREQUENCY;
	public static int CHAMP_MIN_LVL;
	public static int CHAMP_MAX_LVL;
	public static int CHAMPION_HP;
	public static int CHAMPION_REWARDS;
	public static int CHAMPION_ADENAS_REWARDS;
	public static double CHAMPION_HP_REGEN;
	public static double CHAMPION_ATK;
	public static double CHAMPION_SPD_ATK;
	public static int CHAMPION_REWARD;
	public static int CHAMPION_REWARD_ID;
	public static int CHAMPION_REWARD_QTY;
	
	/** Buffer */
	public static int BUFFER_MAX_SCHEMES;
	public static int BUFFER_STATIC_BUFF_COST;
	
	public static String FIGHTER_BUFF;
	public static ArrayList<Integer> FIGHTER_BUFF_LIST = new ArrayList<>();
	public static String MAGE_BUFF;
	public static ArrayList<Integer> MAGE_BUFF_LIST = new ArrayList<>();
	
	/** Misc */
	public static boolean ALLOW_CLASS_MASTERS;
	public static ClassMasterSettings CLASS_MASTER_SETTINGS;
	public static boolean ALLOW_ENTIRE_TREE;
	public static boolean ALTERNATE_CLASS_MASTER;
	public static boolean ANNOUNCE_MAMMON_SPAWN;
	public static boolean ALT_MOB_AGRO_IN_PEACEZONE;
	public static boolean ALT_GAME_FREE_TELEPORT;
	public static boolean SHOW_NPC_LVL;
	public static boolean SHOW_NPC_CREST;
	public static boolean SHOW_SUMMON_CREST;
	
	/** Wyvern Manager */
	public static boolean WYVERN_ALLOW_UPGRADER;
	public static int WYVERN_REQUIRED_LEVEL;
	public static int WYVERN_REQUIRED_CRYSTALS;
	
	/** Raid Boss */
	public static double RAID_HP_REGEN_MULTIPLIER;
	public static double RAID_MP_REGEN_MULTIPLIER;
	public static double RAID_DEFENCE_MULTIPLIER;
	public static double RAID_MINION_RESPAWN_TIMER;
	
	public static boolean PLAYERS_CAN_HEAL_RB;
	public static boolean RAID_DISABLE_CURSE;
	public static int RAID_CHAOS_TIME;
	public static int GRAND_CHAOS_TIME;
	public static int MINION_CHAOS_TIME;
	
	/** Grand Boss */
	public static int SPAWN_INTERVAL_AQ;
	public static int RANDOM_SPAWN_TIME_AQ;
	
	public static int SPAWN_INTERVAL_ANTHARAS;
	public static int RANDOM_SPAWN_TIME_ANTHARAS;
	public static int WAIT_TIME_ANTHARAS;
	
	public static int SPAWN_INTERVAL_BAIUM;
	public static int RANDOM_SPAWN_TIME_BAIUM;
	
	public static int SPAWN_INTERVAL_CORE;
	public static int RANDOM_SPAWN_TIME_CORE;
	
	public static int SPAWN_INTERVAL_FRINTEZZA;
	public static int RANDOM_SPAWN_TIME_FRINTEZZA;
	public static boolean BYPASS_FRINTEZZA_PARTIES_CHECK;
	public static int FRINTEZZA_MIN_PARTIES;
	public static int FRINTEZZA_MAX_PARTIES;
    public static int FRINTEZZA_TIME_CHALLENGE;
	public static int WAIT_TIME_FRINTEZZA;
	public static int DESPAWN_TIME_FRINTEZZA;
	
	public static int SPAWN_INTERVAL_ORFEN;
	public static int RANDOM_SPAWN_TIME_ORFEN;
	
	public static int SPAWN_INTERVAL_SAILREN;
	public static int RANDOM_SPAWN_TIME_SAILREN;
	public static int WAIT_TIME_SAILREN;
	
	public static int SPAWN_INTERVAL_VALAKAS;
	public static int RANDOM_SPAWN_TIME_VALAKAS;
	public static int WAIT_TIME_VALAKAS;
	
	public static int SPAWN_INTERVAL_ZAKEN;
	public static int RANDOM_SPAWN_TIME_ZAKEN;
	
	public static String FWA_FIXTIMEPATTERNOFANTHARAS;
	public static String FWA_FIXTIMEPATTERNOFBAIUM;
	public static String FWA_FIXTIMEPATTERNOFCORE;
	public static String FWA_FIXTIMEPATTERNOFORFEN;
	public static String FWA_FIXTIMEPATTERNOFVALAKAS;
	public static String FWA_FIXTIMEPATTERNOFZAKEN;
	public static String FWA_FIXTIMEPATTERNOFQA;
	
	/** IA */
	public static boolean GUARD_ATTACK_AGGRO_MOB;
	public static int MAX_DRIFT_RANGE;
	public static int MAX_DRIFT_RANGE_EPIC;
	public static long KNOWNLIST_UPDATE_INTERVAL;
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;
	public static int MIN_MONSTER_ANIMATION;
	public static int MAX_MONSTER_ANIMATION;
	
	public static boolean GRIDS_ALWAYS_ON;
	public static int GRID_NEIGHBOR_TURNON_TIME;
	public static int GRID_NEIGHBOR_TURNOFF_TIME;
	
	public static boolean ENABLE_SKIPPING;
	public static int ITEM_ID_BUY_CLAN_HALL;

	// --------------------------------------------------
	// Players
	// --------------------------------------------------
	
	/** Misc */
	public static int STARTING_ADENA;
	
	public static boolean CUSTOM_STARTER_ITEMS_ENABLED;
	public static List<int[]> STARTING_CUSTOM_ITEMS_F = new ArrayList<>();
	public static List<int[]> STARTING_CUSTOM_ITEMS_M = new ArrayList<>();
	
	public static boolean EFFECT_CANCELING;
	public static double HP_REGEN_MULTIPLIER;
	public static double MP_REGEN_MULTIPLIER;
	public static double CP_REGEN_MULTIPLIER;
	public static int PLAYER_SPAWN_PROTECTION;
	public static int PLAYER_FAKEDEATH_UP_PROTECTION;
	public static double RESPAWN_RESTORE_HP;
	public static double RESPAWN_RESTORE_MP;
	public static double RESPAWN_RESTORE_CP;
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static boolean DEEPBLUE_DROP_RULES;
	public static boolean ALT_GAME_DELEVEL;
	public static int DEATH_PENALTY_CHANCE;
	
	/** Inventory & WH */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_QUEST_ITEMS;
	public static int INVENTORY_MAXIMUM_PET;
	public static int MAX_ITEM_IN_PACKET;
	public static double ALT_WEIGHT_LIMIT;
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;
	public static int FREIGHT_SLOTS;
	public static boolean ALT_GAME_FREIGHTS;
	public static int ALT_GAME_FREIGHT_PRICE;
	
	/** Augmentations */
	public static int AUGMENTATION_NG_SKILL_CHANCE;
	public static int AUGMENTATION_NG_GLOW_CHANCE;
	public static int AUGMENTATION_NG_BASESTAT_CHANCE;
	
	public static int AUGMENTATION_MID_SKILL_CHANCE;
	public static int AUGMENTATION_MID_GLOW_CHANCE;
	public static int AUGMENTATION_MID_BASESTAT_CHANCE;
	
	public static int AUGMENTATION_HIGH_SKILL_CHANCE;
	public static int AUGMENTATION_HIGH_GLOW_CHANCE;
	public static int AUGMENTATION_HIGH_BASESTAT_CHANCE;
	
	public static int AUGMENTATION_TOP_SKILL_CHANCE;
	public static int AUGMENTATION_TOP_GLOW_CHANCE;
	public static int AUGMENTATION_TOP_BASESTAT_CHANCE;
	
	//public static int AUGMENTATION_BASESTAT_CHANCE;
	
	/** Karma & PvP */
	public static boolean KARMA_PLAYER_CAN_BE_KILLED_IN_PZ;
	public static boolean KARMA_PLAYER_CAN_SHOP;
	public static boolean KARMA_PLAYER_CAN_USE_GK;
	public static boolean KARMA_PLAYER_CAN_TELEPORT;
	public static boolean KARMA_PLAYER_CAN_TRADE;
	public static boolean KARMA_PLAYER_CAN_USE_WH;
	
	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_AWARD_PK_KILL;
	public static int KARMA_PK_LIMIT;
	public static int KARMA_LOST_BASE;
	
	public static String KARMA_NONDROPPABLE_PET_ITEMS;
	public static String KARMA_NONDROPPABLE_ITEMS;
	public static int[] KARMA_LIST_NONDROPPABLE_PET_ITEMS;
	public static int[] KARMA_LIST_NONDROPPABLE_ITEMS;
	
	public static int PVP_NORMAL_TIME;
	public static int PVP_PVP_TIME;
	
	/** Party */
	public static String PARTY_XP_CUTOFF_METHOD;
	public static int PARTY_XP_CUTOFF_LEVEL;
	public static double PARTY_XP_CUTOFF_PERCENT;
	public static int ALT_PARTY_RANGE;
	public static int ALT_PARTY_RANGE2;
	public static boolean ALT_LEAVE_PARTY_LEADER;
	
	/** GMs & Admin Stuff */
	public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;
	public static int MASTERACCESS_LEVEL;
	public static int MASTERACCESS_NAME_COLOR;
	public static int MASTERACCESS_TITLE_COLOR;
	public static boolean GM_HERO_AURA;
	public static boolean GM_STARTUP_INVULNERABLE;
	public static boolean GM_STARTUP_INVISIBLE;
	public static boolean GM_STARTUP_SILENCE;
	public static boolean GM_STARTUP_AUTO_LIST;
	
	/** petitions */
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;
	
	/** Crafting **/
	public static boolean IS_CRAFTING_ENABLED;
	public static int DWARF_RECIPE_LIMIT;
	public static int COMMON_RECIPE_LIMIT;
	public static boolean ALT_BLACKSMITH_USE_RECIPES;
	
	/** Skills & Classes **/
	public static boolean AUTO_LEARN_SKILLS;
	public static boolean ALT_GAME_MAGICFAILURES;
	public static boolean ALT_GAME_SHIELD_BLOCKS;
	public static int ALT_PERFECT_SHLD_BLOCK;
	public static boolean LIFE_CRYSTAL_NEEDED;
	public static boolean SP_BOOK_NEEDED;
	public static boolean ES_SP_BOOK_NEEDED;
	public static boolean AUTO_LEARN_DIVINE_INSPIRATION;
	public static boolean DIVINE_SP_BOOK_NEEDED;
	public static boolean ALT_GAME_SUBCLASS_WITHOUT_QUESTS;
	public static boolean ALT_GAME_SUBCLASS_EVERYWHERE;
	
	/** Buffs */
	public static boolean STORE_SKILL_COOLTIME;
	public static int BUFFS_MAX_AMOUNT;
	
	// --------------------------------------------------
	// L2Jmod
	// --------------------------------------------------
	public static boolean ALLOW_MOD_MENU;
	public static boolean ALLOW_NEW_COLOR_MANAGER;
	public static String RAID_INFO_IDS;
	public static List<Integer> RAID_INFO_IDS_LIST;
	
	public static int RAID_BOSS_INFO_PAGE_LIMIT;
	public static int RAID_BOSS_DROP_PAGE_LIMIT;
	public static String RAID_BOSS_DATE_FORMAT;
	public static String RAID_BOSS_IDS;
	public static List<Integer> LIST_RAID_BOSS_IDS;
	
	public static String GRAND_BOSS_IDS;
	public static List<Integer> LIST_GRAND_BOSS_IDS;

	public static int BANKING_SYSTEM_GOLDBARS;
	public static int BANKING_SYSTEM_ADENA;
	public static int PVP_POINT_ID;
	public static int PVP_POINT_COUNT;
	public static boolean ALLOW_EVENT_COMMANDS;
	public static boolean ALLOW_STATUS_COMMANDS;
    public static boolean ALLOW_DONATE_COMMANDS;
	public static int DONATE_COIN_ID;
	public static int DONATE_COIN_COUNT;
	public static boolean ALLOW_WELCOME_TO_LINEAGE;
	public static boolean ALT_GIVE_PVP_IN_ARENA;
	public static boolean SHOW_HP_PVP;

	public static String PVPS_COLORS;
	public static HashMap<Integer, Integer> PVPS_COLORS_LIST;
	public static String PKS_COLORS;
	public static HashMap<Integer, Integer> PKS_COLORS_LIST;
	
	public static boolean TIME_TELEPORTER_ENABLE;
	public static ArrayList<Integer> TIME_TELEPORTERS = new ArrayList<>();

	public static boolean VOTE_FOR_PVPZONE;
	public static boolean OFFLINE_TRADE_ENABLE;
	public static boolean OFFLINE_CRAFT_ENABLE;
	public static boolean RESTORE_OFFLINERS;
	public static int OFFLINE_MAX_DAYS;
	public static boolean OFFLINE_DISCONNECT_FINISHED;
	public static boolean OFFLINE_MODE_IN_PEACE_ZONE;
	public static boolean OFFLINE_MODE_NO_DAMAGE;
	public static boolean OFFLINE_LOGOUT;
	public static boolean OFFLINE_SLEEP_EFFECT;
	/** Aio System */
	public static boolean ENABLE_AIO_SYSTEM;
	public static Map<Integer, Integer> AIO_SKILLS;
	public static boolean ALLOW_AIO_NCOLOR;
	public static int AIO_NCOLOR;
	public static boolean ALLOW_AIO_TCOLOR;
	public static int AIO_TCOLOR;
	public static boolean ALLOW_AIO_ITEM;
	public static int AIO_ITEMID;
	/** VIP System */
	public static boolean ENABLE_VIP_SYSTEM;
	public static boolean VIP_EFFECT;
	public static Map<Integer, Integer> VIP_SKILLS;
	public static boolean ALLOW_VIP_NCOLOR;
	public static int VIP_NCOLOR;
	public static boolean ALLOW_VIP_TCOLOR;
	public static int VIP_TCOLOR;
	public static double VIP_XP_SP_RATE;
	public static double VIP_ADENA_RATE;
	public static int VIP_DROP_RATE;
	public static double VIP_SPOIL_RATE;
	public static boolean ALLOW_VIP_ITEM;
	public static int VIP_ITEMID; 
	public static boolean ALLOW_DRESS_ME_VIP;
	
	public static boolean NOBLESS_FROM_BOSS;
	public static int BOSS_ID;
	public static int RADIUS_TO_RAID;
	
	public static boolean ALLOW_FLAG_ONKILL_BY_ID;
	public static String NPCS_FLAG_IDS;
	public static List<Integer> NPCS_FLAG_LIST = new ArrayList<>();
	public static int NPCS_FLAG_RANGE;
	
	public static boolean LEAVE_BUFFS_ON_DIE;
	public static boolean CHAOTIC_LEAVE_BUFFS_ON_DIE;
	
	public static int CUSTOM_START_LVL;
	public static int CUSTOM_SUBCLASS_LVL;
	
	public static boolean CHAR_TITLE;
	public static String ADD_CHAR_TITLE;
	
	public static String[] FORBIDDEN_NAMES;
	public static String[] GM_NAMES;
	
	public static boolean PRIVATE_STORE_NEED_PVPS;
	public static int MIN_PVP_TO_USE_STORE;
	
	public static boolean PRIVATE_STORE_NEED_LEVELS;
	public static int MIN_LEVEL_TO_USE_STORE;
	
	public static boolean EXPERTISE_PENALTY;
	
	public static boolean ALLOW_HERO_SUBSKILL;
	public static int HERO_COUNT;
    
	public static boolean ALT_RESTORE_EFFECTS_ON_SUBCLASS_CHANGE;

	public static boolean CHECK_SKILLS_ON_ENTER;
	public static String ALLOWED_SKILLS;
	public static ArrayList<Integer> ALLOWED_SKILLS_LIST = new ArrayList<Integer>();
	
	public static boolean DISABLE_ATTACK_NPC_TYPE;
	public static String ALLOWED_NPC_TYPES;
	public static ArrayList<String> LIST_ALLOWED_NPC_TYPES = new ArrayList<String>();
	
	public static boolean ALLOW_PVP_REWARD;
	public static List<int[]> PVP_ITEMS_REWARD;

	public static boolean ALLOW_PK_REWARD;
	public static List<int[]> PK_ITEMS_REWARD;
	
    public static boolean ANTI_FARM_ENABLED;
	public static boolean ANTI_FARM_CLAN_ALLY_ENABLED;
	public static boolean ANTI_FARM_LVL_DIFF_ENABLED;
	public static int ANTI_FARM_MAX_LVL_DIFF;
	public static boolean ANTI_FARM_PARTY_ENABLED;
	public static boolean ANTI_FARM_IP_ENABLED;

	public static boolean ALLOW_DRESS_ME_SYSTEM;
	public static boolean DRESS_ME_NEED_PVPS;
	public static int PVPS_TO_USE_DRESS_ME;
	
	public static Map<String, Integer> DRESS_ME_HEAVY_CHESTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_HEAVY_LEGS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_HEAVY_GLOVES = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_HEAVY_BOOTS = new HashMap<>();
	
	public static Map<String, Integer> DRESS_ME_LIGHT_CHESTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_LIGHT_LEGS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_LIGHT_GLOVES = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_LIGHT_BOOTS = new HashMap<>();
	
	public static Map<String, Integer> DRESS_ME_ROBE_CHESTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_ROBE_LEGS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_ROBE_GLOVES = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_ROBE_BOOTS = new HashMap<>();
	
	public static Map<String, Integer> DRESS_ME_CUSTOM_CHESTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_CUSTOM_LEGS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_CUSTOM_GLOVES = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_CUSTOM_BOOTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_CUSTOM_HAIR = new HashMap<>();
    
    public static boolean SUMMON_MOUNT_PROTECTION;
    public static String ID_RESTRICT;
    public static List<Integer> LISTID_RESTRICT;
    
   	public static boolean ANTIBOT_ENABLE;
   	public static int ANTIBOT_TIME_JAIL;
   	public static int ANTIBOT_TIME_VOTE;
   	public static int ANTIBOT_KILL_MOBS;
   	public static int ANTIBOT_MIN_LEVEL;
   	
	public static boolean DELETE_AUGM_PASSIVE_ON_CHANGE;
	public static boolean DELETE_AUGM_ACTIVE_ON_CHANGE;
   	
	public static int REWARD_LV_1;
	public static int REWARD_COUNT_1;
   	
	public static int REWARD_LV_2;
	public static int REWARD_COUNT_2;
   	
	public static int REWARD_LV_3;
	public static int REWARD_COUNT_3;
   	
	public static int REWARD_LV_4;
	public static int REWARD_COUNT_4;
	
	public static int HERO_COIN_ID1;
	public static int HERO_DAYS_ID1;
	
	public static int HERO_COIN_ID2;
	public static int HERO_DAYS_ID2;
	
	public static int HERO_COIN_ID3;
	public static int HERO_DAYS_ID3;
	
	public static int VIP_COIN_ID1;
	public static int VIP_DAYS_ID1;
	
	public static int VIP_COIN_ID2;
	public static int VIP_DAYS_ID2;
	
	public static int VIP_COIN_ID3;
	public static int VIP_DAYS_ID3;
	
	//Party Farm
	public static boolean PARTY_EVENT_START;	
	public static String[] PARTY_DROP_EVENT_INTERVAL;
	public static int PARTY_DROP_EVENT_TIME;
	
	public static String PARTY_ZONE_CHAMPIONS;
	public static int[] PARTY_ZONE_LIST_CHAMPIONS;
	
	public static List<int[]> PARTY_ZONE_REWARD;
	
    public static String PART_ZONE_MONSTERS;
    public static List<Integer> PART_ZONE_MONSTERS_ID;

    public static List<RewardHolder> PARTY_ZONE_REWARDS = new ArrayList<>();
    
	public static int CHAMPION_ID;
	public static int MONSTER_LOCS_COUNT;
	public static int[][] MONSTER_LOCS;
	
	//donate
    public static int DONATE_TICKET;
    public static int AUGM_PRICE;
    
	public static int MAX_HEALER_ON_PARTY;
	public static int MAX_COMBAT_HEALER_ON_PARTY;
    
	// --------------------------------------------------
	// L2JEvents
	// --------------------------------------------------

	public static boolean CKM_ENABLED;
	public static long CKM_CYCLE_LENGTH;
	public static String CKM_PVP_NPC_TITLE;
	public static int CKM_PVP_NPC_TITLE_COLOR;
	public static int CKM_PVP_NPC_NAME_COLOR;
	public static String CKM_PK_NPC_TITLE;
	public static int CKM_PK_NPC_TITLE_COLOR;
	public static int CKM_PK_NPC_NAME_COLOR;

	public static boolean PCB_ENABLE;
	public static int PCB_MIN_LEVEL;
	public static int PCB_POINT_MIN;
	public static int PCB_POINT_MAX;
    public static int PCB_CHANCE_DUAL_POINT;
	public static int PCB_INTERVAL;
	
	public static boolean AUTOVOTEREWARD_ENABLED;
	public static int VOTE_DUALBOXES_ALLOWED;
	public static int VOTES_FOR_REWARD;
	public static String VOTES_REWARDS;
	public static String VOTES_SITE_TOPZONE_URL;
	public static String VOTES_SITE_HOPZONE_URL;
	public static String VOTES_SITE_L2NETWORK_URL;
	public static int[][] VOTES_REWARDS_LIST;
	public static int VOTES_SYSYEM_INITIAL_DELAY;
	public static int VOTES_SYSYEM_STEP_DELAY;
	public static String SERVER_WEB_SITE;
	
	public static boolean HIDE_EVENT;
	public static int HIDE_EVENT_REWARD_ID;
	public static int HIDE_EVENT_REWARD_COUNT;
	public static int HIDE_EVENT_ITEM_WILL_DROP;
	public static int HIDE_EVENT_DELAY_BEFORE_START;

	public static boolean KBE_ENABLE;
	public static int KBE_INTERVAL;
	public static int KBE_DURATION;
	public static int[] KBE_LIST_BOSS_ID;
	public static String KBE_TITLE;
	public static int[][] KBE_SPAWN;
	public static String[] KBE_ANNOUNCE_START;
	public static String[] KBE_ANNOUNCE_END;

	// --------------------------------------------------
	// Balance
	// --------------------------------------------------

 	public static int RUN_SPD_BOOST;
    public static int MAX_RUN_SPEED;
    public static int MAX_PCRIT_RATE;
    public static int MAX_MCRIT_RATE;
    public static int MAX_PATK_SPEED;
    public static int MAX_MATK_SPEED;
    public static int MAX_EVASION;
    public static int MAX_ACCURACY;
	public static int MAGIC_CRITICAL_POWER;
	
	public static boolean ENABLE_MODIFY_SKILL_DURATION;
	public static Map<Integer, Integer> SKILL_DURATION_LIST;
	public static int MODIFIED_SKILL_COUNT;
	
	public static boolean MASTERY_PENALTY;
	public static int LEVEL_TO_GET_PENALITY;
	public static int ARMOR_PENALTY_SKILL;
	
	public static boolean MASTERY_WEAPON_PENALTY;
	public static int LEVEL_TO_GET_WEAPON_PENALITY;
	public static int WEAPON_PENALTY_SKILL;
	
	public static int ANTI_SS_BUG_1;
	public static int ANTI_SS_BUG_2;	
	
	// Alternative damage for dagger skills VS heavy
	public static float ALT_DAGGER_DMG_VS_HEAVY;
	// Alternative damage for dagger skills VS robe
	public static float ALT_DAGGER_DMG_VS_ROBE;
	// Alternative damage for dagger skills VS light
	public static float ALT_DAGGER_DMG_VS_LIGHT;
	
	// Alternative damage for DUAL skills VS heavy
	public static float ALT_DUAL_DMG_VS_HEAVY;
	// Alternative damage for DUAL skills VS robe
	public static float ALT_DUAL_DMG_VS_ROBE;
	// Alternative damage for DUAL skills VS light
	public static float ALT_DUAL_DMG_VS_LIGHT;

	public static float ALT_ATTACK_DELAY;	 // Alternative config for next hit delay

	public static boolean ENABLE_CLASS_DAMAGES;
	public static boolean ENABLE_CLASS_DAMAGES_LOGGER;
	public static boolean ENABLE_OLY_CLASS_DAMAGES;
	public static boolean ENABLE_OLY_CLASS_DAMAGES_LOGGER;
	
	public static String PROTECTED_SKILLS;
	public static List<Integer> NOT_CANCELED_SKILLS;
	public static int CANCEL_BACK_TIME;

	// --------------------------------------------------
	// Annoucements
	// --------------------------------------------------
	
 	public static boolean ANNOUNCE_PK_PVP;
 	public static boolean ANNOUNCE_PK_PVP_NORMAL_MESSAGE;
 	public static String ANNOUNCE_PK_MSG;
 	public static String ANNOUNCE_PVP_MSG;
 	
 	public static boolean ANNOUNCE_RAID_BOSS_ALIVE;
 	public static boolean ANNOUNCE_RAID_BOSS_DEATH;
 	
 	public static boolean ANNOUNCE_CASTLE_LORDS;
 	public static boolean ANNOUNCE_AIO_LOGIN;
 	public static boolean ANNOUNCE_HERO_LOGIN;
 	
	public static boolean ENABLE_ENCHANT_ANNOUNCE;
	public static int ENCHANT_ANNOUNCE_LEVEL;
 	
	public static boolean ALLOW_QUAKE_SYSTEM;
    public static Map<Integer, String> QUAKE_VALUES = new HashMap<>();
    
    public static boolean ALLOW_QUAKE_SOUND;
    public static boolean ALLOW_QUAKE_SOUND_SCREEN;
    
    public static int QUAKE_SOUND_TIME;

	public static boolean WAR_LEGEND_AURA;
	public static int KILLS_TO_GET_WAR_LEGEND_AURA;

    public static String  DEFAULT_GLOBAL_CHAT;
	public static String  DEFAULT_TRADE_CHAT;
    
    public static boolean CHAT_SHOUT_NEED_PVPS;
    public static int PVPS_TO_USE_CHAT_SHOUT;
    public static boolean CHAT_TRADE_NEED_PVPS;
    public static int PVPS_TO_USE_CHAT_TRADE;
    public static boolean CHAT_HERO_NEED_PVPS;
    public static int PVPS_TO_USE_CHAT_HERO;
    
    public static boolean ALT_OLY_END_ANNOUNCE;
    
	public static int CHAT_FILTER_PUNISHMENT_PARAM1;
	public static int CHAT_FILTER_PUNISHMENT_PARAM2;
	public static int CHAT_FILTER_PUNISHMENT_PARAM3;
	public static boolean USE_SAY_FILTER;
	public static String CHAT_FILTER_CHARS;
	public static String CHAT_FILTER_PUNISHMENT;
	public static boolean TALK_CHAT_ALL_CONFIG;
	public static int TALK_CHAT_ALL_TIME;
	public static ArrayList<String> FILTER_LIST = new ArrayList<>();
	
	// --------------------------------------------------
	// Server
	// --------------------------------------------------
	
	public static String GAMESERVER_HOSTNAME;
	public static int PORT_GAME;
	public static String EXTERNAL_HOSTNAME;
	public static String INTERNAL_HOSTNAME;
	public static int GAME_SERVER_LOGIN_PORT;
	public static String GAME_SERVER_LOGIN_HOST;
	
	/** Access to database */
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIME;
	
	/** serverList & Test */
	public static boolean SERVER_LIST_BRACKET;
	public static boolean SERVER_LIST_CLOCK;
	public static boolean SERVER_LIST_TESTSERVER;
	public static boolean SERVER_GMONLY;
	public static boolean TEST_SERVER;
	
	/** clients related */
	public static int DELETE_DAYS;
	public static int MAXIMUM_ONLINE_USERS;
	//public static int PROTOCOL_REVISION;
	//public static int MAX_PLAYER_PER_PC;
	public static int MIN_PROTOCOL_REVISION;
	public static int MAX_PROTOCOL_REVISION;
	
	/** Jail & Punishements **/
	public static boolean JAIL_IS_PVP;
	public static int DEFAULT_PUNISH;
	public static int DEFAULT_PUNISH_PARAM;
	
	public static boolean MULTIBOX_PROTECTION_ENABLED; 
	public static int MULTIBOX_PROTECTION_CLIENTS_PER_PC; 
	public static int MULTIBOX_PROTECTION_PUNISH; 
	
	/** by Hwid **/
	public static boolean HWID_MULTIBOX_PROTECTION_ENABLED; 
	public static int HWID_MULTIBOX_PROTECTION_CLIENTS_PER_PC; 
	public static int HWID_MULTIBOX_PROTECTION_PUNISH; 
	
	/** Auto-loot */
	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_RAID;
	
	/** Items Management */
	public static boolean ALLOW_DISCARDITEM;
	public static boolean MULTIPLE_ITEM_DROP;
	public static int ITEM_AUTO_DESTROY_TIME;
	public static int HERB_AUTO_DESTROY_TIME;
	public static String PROTECTED_ITEMS;
	
	public static List<Integer> LIST_PROTECTED_ITEMS;
	
	public static boolean DESTROY_DROPPED_PLAYER_ITEM;
	public static boolean DESTROY_EQUIPABLE_PLAYER_ITEM;
	public static boolean SAVE_DROPPED_ITEM;
	public static boolean EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD;
	public static int SAVE_DROPPED_ITEM_INTERVAL;
	public static boolean CLEAR_DROPPED_ITEM_TABLE;
	
	/** Rate control */
	public static double RATE_XP;
	public static double RATE_SP;
	public static double RATE_PARTY_XP;
	public static double RATE_PARTY_SP;
	public static double RATE_DROP_ADENA;
	public static double RATE_CONSUMABLE_COST;
	public static double RATE_DROP_ITEMS;
	public static double RATE_DROP_SEAL_STONES;
	public static double RATE_DROP_ITEMS_BY_RAID;
	public static double RATE_DROP_SPOIL;
	public static int RATE_DROP_MANOR;
	
	public static double RATE_QUEST_DROP;
	public static double RATE_QUEST_REWARD;
	public static double RATE_QUEST_REWARD_XP;
	public static double RATE_QUEST_REWARD_SP;
	public static double RATE_QUEST_REWARD_ADENA;
	
	public static double RATE_KARMA_EXP_LOST;
	public static double RATE_SIEGE_GUARDS_PRICE;
	
	public static int PLAYER_DROP_LIMIT;
	public static int PLAYER_RATE_DROP;
	public static int PLAYER_RATE_DROP_ITEM;
	public static int PLAYER_RATE_DROP_EQUIP;
	public static int PLAYER_RATE_DROP_EQUIP_WEAPON;
	
	public static int KARMA_DROP_LIMIT;
	public static int KARMA_RATE_DROP;
	public static int KARMA_RATE_DROP_ITEM;
	public static int KARMA_RATE_DROP_EQUIP;
	public static int KARMA_RATE_DROP_EQUIP_WEAPON;
	public static boolean AUG_WEAPON_DROPABLE;
	
	public static double PET_XP_RATE;
	public static int PET_FOOD_RATE;
	public static double SINEATER_XP_RATE;
	
	public static double RATE_DROP_COMMON_HERBS;
	public static double RATE_DROP_HP_HERBS;
	public static double RATE_DROP_MP_HERBS;
	public static double RATE_DROP_SPECIAL_HERBS;
	
	/** Allow types */
	public static boolean ALLOW_FREIGHT;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WEAR;
	public static int WEAR_DELAY;
	public static int WEAR_PRICE;
	public static boolean ALLOW_LOTTERY;
	public static boolean ALLOW_RACE;
	public static boolean ALLOW_WATER;
	public static boolean ALLOWFISHING;
	public static boolean ALLOW_BOAT;
	public static boolean ALLOW_CURSED_WEAPONS;
	public static boolean ALLOW_MANOR;
	public static boolean ENABLE_FALLING_DAMAGE;
	
	/** Debug & Dev */
	public static boolean ALT_DEV_NO_SCRIPTS;
	public static boolean ALT_DEV_NO_SPAWNS;
	public static boolean DEBUG;
	public static boolean DEVELOPER;
	public static boolean PACKET_HANDLER_DEBUG;
	
	/** Deadlock Detector */
	public static boolean DEADLOCK_DETECTOR;
	public static int DEADLOCK_CHECK_INTERVAL;
	public static boolean RESTART_ON_DEADLOCK;
	
	/** Logs */
	public static boolean LOG_CHAT;
	public static boolean LOG_ITEMS;
	public static boolean GMAUDIT;
	
	/** Community Board */
	public static boolean ENABLE_COMMUNITY_BOARD;
	public static String BBS_DEFAULT;
	
	/** Geodata */
	public static int COORD_SYNCHRONIZE;
	public static int GEODATA;
	public static boolean FORCE_GEODATA;
	
	public static boolean GEODATA_CELLFINDING;
	public static String PATHFIND_BUFFERS;
	public static double LOW_WEIGHT;
	public static double MEDIUM_WEIGHT;
	public static double HIGH_WEIGHT;
	public static boolean ADVANCED_DIAGONAL_STRATEGY;
	public static double DIAGONAL_WEIGHT;
	public static int MAX_POSTFILTER_PASSES;
	public static boolean DEBUG_PATH;
	
	/** Misc */
	public static boolean L2WALKER_PROTECTION;
	public static boolean AUTODELETE_INVALID_QUEST_DATA;
	public static boolean GAMEGUARD_ENFORCE;
	public static boolean SERVER_NEWS;
	public static int ZONE_TOWN;
	public static boolean DISABLE_TUTORIAL;
	public static boolean LOAD_CUSTOM_MULTISELLS;
	public static int BREAK_ENCHANT;
	
	//Startup
	public static boolean STARTUP_SYSTEM_ENABLED;
	
	public static String BYBASS_HEAVY_ITEMS;
	public static String BYBASS_LIGHT_ITEMS;
	public static String BYBASS_ROBE_ITEMS;

	public static List<int[]> SET_HEAVY_ITEMS = new ArrayList<int[]>();
	public static int[] SET_HEAVY_ITEMS_LIST;
	
	public static List<int[]> SET_LIGHT_ITEMS = new ArrayList<int[]>();
	public static int[] SET_LIGHT_ITEMS_LIST;
	
	public static List<int[]> SET_ROBE_ITEMS = new ArrayList<int[]>();
	public static int[] SET_ROBE_ITEMS_LIST;
	
	public static String BYBASS_WP_01_ITEM;
	public static String BYBASS_WP_02_ITEM;
	public static String BYBASS_WP_03_ITEM;
	public static String BYBASS_WP_04_ITEM;
	public static String BYBASS_WP_05_ITEM;
	public static String BYBASS_WP_06_ITEM;
	public static String BYBASS_WP_07_ITEM;
	public static String BYBASS_WP_08_ITEM;
	public static String BYBASS_WP_09_ITEM;
	public static String BYBASS_WP_10_ITEM;
	public static String BYBASS_WP_11_ITEM;
	public static String BYBASS_WP_12_ITEM;
	public static String BYBASS_WP_13_ITEM;
	public static String BYBASS_WP_14_ITEM;
	public static String BYBASS_WP_15_ITEM;
	public static String BYBASS_WP_16_ITEM;
	public static String BYBASS_WP_17_ITEM;
	public static String BYBASS_WP_18_ITEM;
	public static String BYBASS_WP_19_ITEM;
	public static String BYBASS_WP_20_ITEM;
	public static String BYBASS_WP_21_ITEM;
	public static String BYBASS_WP_22_ITEM;
	public static String BYBASS_WP_23_ITEM;
	public static String BYBASS_WP_24_ITEM;
	public static String BYBASS_WP_25_ITEM;
	public static String BYBASS_WP_26_ITEM;
	public static String BYBASS_WP_27_ITEM;
	public static String BYBASS_WP_28_ITEM;
	public static String BYBASS_WP_29_ITEM;
	public static String BYBASS_WP_30_ITEM;
	public static String BYBASS_WP_31_ITEM;
	public static String BYBASS_WP_SHIELD;
	public static String BYBASS_ARROW;
	public static int WP_01_ID;
	public static int WP_02_ID;
	public static int WP_03_ID;
	public static int WP_04_ID;
	public static int WP_05_ID;
	public static int WP_06_ID;
	public static int WP_07_ID;
	public static int WP_08_ID;
	public static int WP_09_ID;
	public static int WP_10_ID;
	public static int WP_11_ID;
	public static int WP_12_ID;
	public static int WP_13_ID;
	public static int WP_14_ID;
	public static int WP_15_ID;
	public static int WP_16_ID;
	public static int WP_17_ID;
	public static int WP_18_ID;
	public static int WP_19_ID;
	public static int WP_20_ID;
	public static int WP_21_ID;
	public static int WP_22_ID;
	public static int WP_23_ID;
	public static int WP_24_ID;
	public static int WP_25_ID;
	public static int WP_26_ID;
	public static int WP_27_ID;
	public static int WP_28_ID;
	public static int WP_29_ID;
	public static int WP_30_ID;
	public static int WP_31_ID;
	public static int WP_ARROW;
	public static int WP_SHIELD;

	public static boolean BOOM_REWARD_ITEM_ENABLED;
	public static List<int[]> LVL_1_REWARD = new ArrayList<>();
	public static List<int[]> LVL_2_REWARD = new ArrayList<>();
	public static List<int[]> LVL_3_REWARD = new ArrayList<>();
	public static List<int[]> LVL_4_REWARD = new ArrayList<>();
	public static List<int[]> LVL_5_REWARD = new ArrayList<>();
	public static int EVENT_KEY;
	public static int EVENT_KEY_AMOUNT_1;
	public static int EVENT_KEY_AMOUNT_2;
	public static int EVENT_KEY_AMOUNT_3;
	public static int EVENT_KEY_AMOUNT_4;
	public static int EVENT_KEY_AMOUNT_5;

	// --------------------------------------------------
	// Those "hidden" settings haven't configs to avoid admins to fuck their server
	// You still can experiment changing values here. But don't say I didn't warn you.
	// --------------------------------------------------
	
	/** Threads & Packets size */
	public static int THREAD_P_EFFECTS = 6; // default 6
	public static int THREAD_P_GENERAL = 15; // default 15
	public static int GENERAL_PACKET_THREAD_CORE_SIZE = 8; // default 4
	public static int IO_PACKET_THREAD_CORE_SIZE = 4; // default 2
	public static int GENERAL_THREAD_CORE_SIZE = 4; // default 4
	public static int AI_MAX_THREAD = 10; // default 10
	
	/** Packet information */
	public static boolean COUNT_PACKETS = false; // default false
	public static boolean DUMP_PACKET_COUNTS = false; // default false
	public static int DUMP_INTERVAL_SECONDS = 60; // default 60
	
	/** IA settings */
	public static int MINIMUM_UPDATE_DISTANCE = 50; // default 50
	public static int MINIMUN_UPDATE_TIME = 500; // default 500
	public static int KNOWNLIST_FORGET_DELAY = 10000; // default 10000
	
	/** Time after which a packet is considered as lost */
	public static int PACKET_LIFETIME = 0; // default 0 (unlimited)
	
	/** Reserve Host on LoginServerThread */
	public static boolean RESERVE_HOST_ON_LOGIN = false; // default false
	
	/** MMO settings */
	public static int MMO_SELECTOR_SLEEP_TIME = 20; // default 20
	public static int MMO_MAX_SEND_PER_PASS = 12; // default 12
	public static int MMO_MAX_READ_PER_PASS = 12; // default 12
	public static int MMO_HELPER_BUFFER_COUNT = 20; // default 20
	
	/** Client Packets Queue settings */
	public static int CLIENT_PACKET_QUEUE_SIZE = 14; // default MMO_MAX_READ_PER_PASS + 2
	public static int CLIENT_PACKET_QUEUE_MAX_BURST_SIZE = 13; // default MMO_MAX_READ_PER_PASS + 1
	public static int CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND = 80; // default 80
	public static int CLIENT_PACKET_QUEUE_MEASURE_INTERVAL = 5; // default 5
	public static int CLIENT_PACKET_QUEUE_MAX_AVERAGE_PACKETS_PER_SECOND = 40; // default 40
	public static int CLIENT_PACKET_QUEUE_MAX_FLOODS_PER_MIN = 2; // default 2
	public static int CLIENT_PACKET_QUEUE_MAX_OVERFLOWS_PER_MIN = 1; // default 1
	public static int CLIENT_PACKET_QUEUE_MAX_UNDERFLOWS_PER_MIN = 1; // default 1
	public static int CLIENT_PACKET_QUEUE_MAX_UNKNOWN_PER_MIN = 5; // default 5
	
	// --------------------------------------------------
	
	/**
	 * This class initializes all global variables for configuration.<br>
	 * If key doesn't appear in properties file, a default value is setting on by this class.
	 */
	/*
	public static final void loadFacebook()
	{
		final ExProperties fb = load(FACEBOOK_FILE);

		FACEBOOK_API_ENABLED = fb.getProperty("FacebookEnable", false);
		
		if (FACEBOOK_API_ENABLED)
		{
			FACEBOOK_API_TOKEN = fb.getProperty("FacebookApiToken", "123");
			FACEBOOK_PAGE_NAME = fb.getProperty("FacebookPageName" , "L2Tests");
			FACEBOOK_REWARD_ID = fb.getProperty("FacebookRewardId" , 57);
			FACEBOOK_REWARD_COUNT = fb.getProperty("FacebookRewardCount" , 150);
		}
	}
	*/
	
	public static void load()
	{
		if (Server.serverMode == Server.MODE_GAMESERVER)
		{
			_log.info("Loading flood protectors.");
			FLOOD_PROTECTOR_ROLL_DICE = new FloodProtectorConfig("RollDiceFloodProtector");
			FLOOD_PROTECTOR_SHOUT_VOICE = new FloodProtectorConfig("GlobalChatFloodProtector");
			FLOOD_PROTECTOR_TRADE_CHAT = new FloodProtectorConfig("TradeChatFloodProtector");
			FLOOD_PROTECTOR_HERO_VOICE = new FloodProtectorConfig("HeroVoiceFloodProtector");
			FLOOD_PROTECTOR_SUBCLASS = new FloodProtectorConfig("SubclassFloodProtector");
			FLOOD_PROTECTOR_DROP_ITEM = new FloodProtectorConfig("DropItemFloodProtector");
			FLOOD_PROTECTOR_SERVER_BYPASS = new FloodProtectorConfig("ServerBypassFloodProtector");
			FLOOD_PROTECTOR_MULTISELL = new FloodProtectorConfig("MultiSellFloodProtector");
			FLOOD_PROTECTOR_MANUFACTURE = new FloodProtectorConfig("ManufactureFloodProtector");
			FLOOD_PROTECTOR_MANOR = new FloodProtectorConfig("ManorFloodProtector");
			FLOOD_PROTECTOR_SENDMAIL = new FloodProtectorConfig("SendMailFloodProtector");
			FLOOD_PROTECTOR_CHARACTER_SELECT = new FloodProtectorConfig("CharacterSelectFloodProtector");
			
			_log.info("Loading gameserver configuration files.");
			
			// Clans settings
			ExProperties clans = load(CLANS_FILE);
			
			MAX_CLAN_MEMBERS = clans.getProperty("MaxMembersMain", 80);
			MAX_CLAN_MEMBERS_ROYALS = clans.getProperty("MaxMembersForRoyals", 80);
			MAX_CLAN_MEMBERS_KNIGHTS = clans.getProperty("MaxMembersForKnights", 80);
			
			ALT_CLAN_JOIN_DAYS = clans.getProperty("DaysBeforeJoinAClan", 5);
			ALT_CLAN_CREATE_DAYS = clans.getProperty("DaysBeforeCreateAClan", 10);
			ALT_MAX_NUM_OF_CLANS_IN_ALLY = clans.getProperty("AltMaxNumOfClansInAlly", 3);
			ALT_CLAN_MEMBERS_FOR_WAR = clans.getProperty("AltClanMembersForWar", 15);
			ALT_CLAN_WAR_PENALTY_WHEN_ENDED = clans.getProperty("AltClanWarPenaltyWhenEnded", 5);
			ALT_CLAN_DISSOLVE_DAYS = clans.getProperty("DaysToPassToDissolveAClan", 7);
			ALT_ALLY_JOIN_DAYS_WHEN_LEAVED = clans.getProperty("DaysBeforeJoinAllyWhenLeaved", 1);
			ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED = clans.getProperty("DaysBeforeJoinAllyWhenDismissed", 1);
			ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED = clans.getProperty("DaysBeforeAcceptNewClanWhenDismissed", 1);
			ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED = clans.getProperty("DaysBeforeCreateNewAllyWhenDissolved", 10);
			ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH = clans.getProperty("AltMembersCanWithdrawFromClanWH", false);
			REMOVE_CASTLE_CIRCLETS = clans.getProperty("RemoveCastleCirclets", true);
			
	        ENABLE_WINNNER_REWARD_SIEGE_CLAN = clans.getProperty("EnableRewardWinnerClan", false);
			REWARD_WINNER_SIEGE_CLAN = parseItemsList(clans.getProperty("PlayerRewardsID", "57,100"));
			LEADER_REWARD_WINNER_SIEGE_CLAN = parseItemsList(clans.getProperty("LeaderRewardsID", "57,400"));
			PLAYER_COUNT_KILLS_INSIEGE = clans.getProperty("KillsToReceiveReward", 0);

			ALT_MANOR_REFRESH_TIME = clans.getProperty("AltManorRefreshTime", 20);
			ALT_MANOR_REFRESH_MIN = clans.getProperty("AltManorRefreshMin", 0);
			ALT_MANOR_APPROVE_TIME = clans.getProperty("AltManorApproveTime", 6);
			ALT_MANOR_APPROVE_MIN = clans.getProperty("AltManorApproveMin", 0);
			ALT_MANOR_MAINTENANCE_PERIOD = clans.getProperty("AltManorMaintenancePeriod", 360000);
			ALT_MANOR_SAVE_ALL_ACTIONS = clans.getProperty("AltManorSaveAllActions", false);
			ALT_MANOR_SAVE_PERIOD_RATE = clans.getProperty("AltManorSavePeriodRate", 2);
			
			CH_TELE_FEE_RATIO = clans.getProperty("ClanHallTeleportFunctionFeeRatio", 86400000);
			CH_TELE1_FEE = clans.getProperty("ClanHallTeleportFunctionFeeLvl1", 7000);
			CH_TELE2_FEE = clans.getProperty("ClanHallTeleportFunctionFeeLvl2", 14000);
			CH_SUPPORT_FEE_RATIO = clans.getProperty("ClanHallSupportFunctionFeeRatio", 86400000);
			CH_SUPPORT1_FEE = clans.getProperty("ClanHallSupportFeeLvl1", 17500);
			CH_SUPPORT2_FEE = clans.getProperty("ClanHallSupportFeeLvl2", 35000);
			CH_SUPPORT3_FEE = clans.getProperty("ClanHallSupportFeeLvl3", 49000);
			CH_SUPPORT4_FEE = clans.getProperty("ClanHallSupportFeeLvl4", 77000);
			CH_SUPPORT5_FEE = clans.getProperty("ClanHallSupportFeeLvl5", 147000);
			CH_SUPPORT6_FEE = clans.getProperty("ClanHallSupportFeeLvl6", 252000);
			CH_SUPPORT7_FEE = clans.getProperty("ClanHallSupportFeeLvl7", 259000);
			CH_SUPPORT8_FEE = clans.getProperty("ClanHallSupportFeeLvl8", 364000);
			CH_MPREG_FEE_RATIO = clans.getProperty("ClanHallMpRegenerationFunctionFeeRatio", 86400000);
			CH_MPREG1_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl1", 14000);
			CH_MPREG2_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl2", 26250);
			CH_MPREG3_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl3", 45500);
			CH_MPREG4_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl4", 96250);
			CH_MPREG5_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl5", 140000);
			CH_HPREG_FEE_RATIO = clans.getProperty("ClanHallHpRegenerationFunctionFeeRatio", 86400000);
			CH_HPREG1_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl1", 4900);
			CH_HPREG2_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl2", 5600);
			CH_HPREG3_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl3", 7000);
			CH_HPREG4_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl4", 8166);
			CH_HPREG5_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl5", 10500);
			CH_HPREG6_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl6", 12250);
			CH_HPREG7_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl7", 14000);
			CH_HPREG8_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl8", 15750);
			CH_HPREG9_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl9", 17500);
			CH_HPREG10_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl10", 22750);
			CH_HPREG11_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl11", 26250);
			CH_HPREG12_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl12", 29750);
			CH_HPREG13_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl13", 36166);
			CH_EXPREG_FEE_RATIO = clans.getProperty("ClanHallExpRegenerationFunctionFeeRatio", 86400000);
			CH_EXPREG1_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl1", 21000);
			CH_EXPREG2_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl2", 42000);
			CH_EXPREG3_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl3", 63000);
			CH_EXPREG4_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl4", 105000);
			CH_EXPREG5_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl5", 147000);
			CH_EXPREG6_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl6", 163331);
			CH_EXPREG7_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl7", 210000);
			CH_ITEM_FEE_RATIO = clans.getProperty("ClanHallItemCreationFunctionFeeRatio", 86400000);
			CH_ITEM1_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl1", 210000);
			CH_ITEM2_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl2", 490000);
			CH_ITEM3_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl3", 980000);
			CH_CURTAIN_FEE_RATIO = clans.getProperty("ClanHallCurtainFunctionFeeRatio", 86400000);
			CH_CURTAIN1_FEE = clans.getProperty("ClanHallCurtainFunctionFeeLvl1", 2002);
			CH_CURTAIN2_FEE = clans.getProperty("ClanHallCurtainFunctionFeeLvl2", 2625);
			CH_FRONT_FEE_RATIO = clans.getProperty("ClanHallFrontPlatformFunctionFeeRatio", 86400000);
			CH_FRONT1_FEE = clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl1", 3031);
			CH_FRONT2_FEE = clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl2", 9331);
			
			// Events config
			ExProperties events = load(EVENTS_FILE);
			ALT_OLY_START_TIME = events.getProperty("AltOlyStartTime", 18);
			ALT_OLY_MIN = events.getProperty("AltOlyMin", 0);
			ALT_OLY_CPERIOD = events.getProperty("AltOlyCPeriod", 21600000);
			ALT_OLY_BATTLE = events.getProperty("AltOlyBattle", 180000);
			ALT_OLY_WPERIOD = events.getProperty("AltOlyWPeriod", 604800000);
			ALT_OLY_VPERIOD = events.getProperty("AltOlyVPeriod", 86400000);
			ALT_OLY_WAIT_TIME = events.getProperty("AltOlyWaitTime", 30);
			ALT_OLY_WAIT_BATTLE = events.getProperty("AltOlyWaitBattle", 60);
			ALT_OLY_WAIT_END = events.getProperty("AltOlyWaitEnd", 40);
			ALT_OLY_START_POINTS = events.getProperty("AltOlyStartPoints", 18);
			ALT_OLY_WEEKLY_POINTS = events.getProperty("AltOlyWeeklyPoints", 3);
			ALT_OLY_MIN_MATCHES = events.getProperty("AltOlyMinMatchesToBeClassed", 5);
			ALT_OLY_CLASSED = events.getProperty("AltOlyClassedParticipants", 5);
			ALT_OLY_NONCLASSED = events.getProperty("AltOlyNonClassedParticipants", 9);
			ALT_OLY_CLASSED_REWARD = parseItemsList(events.getProperty("AltOlyClassedReward", "6651,50"));
			ALT_OLY_NONCLASSED_REWARD = parseItemsList(events.getProperty("AltOlyNonClassedReward", "6651,30"));
			ALT_OLY_COMP_RITEM = events.getProperty("AltOlyCompRewItem", 6651);
			ALT_OLY_GP_PER_POINT = events.getProperty("AltOlyGPPerPoint", 1000);
			ALT_OLY_HERO_POINTS = events.getProperty("AltOlyHeroPoints", 300);
			ALT_OLY_RANK1_POINTS = events.getProperty("AltOlyRank1Points", 100);
			ALT_OLY_RANK2_POINTS = events.getProperty("AltOlyRank2Points", 75);
			ALT_OLY_RANK3_POINTS = events.getProperty("AltOlyRank3Points", 55);
			ALT_OLY_RANK4_POINTS = events.getProperty("AltOlyRank4Points", 40);
			ALT_OLY_RANK5_POINTS = events.getProperty("AltOlyRank5Points", 30);
			ALT_OLY_MAX_POINTS = events.getProperty("AltOlyMaxPoints", 10);
			ALT_OLY_DIVIDER_CLASSED = events.getProperty("AltOlyDividerClassed", 3);
			ALT_OLY_DIVIDER_NON_CLASSED = events.getProperty("AltOlyDividerNonClassed", 3);
			ALT_OLY_ANNOUNCE_GAMES = events.getProperty("AltOlyAnnounceGames", true);
			ALT_OLY_ENCHANT_LIMIT = events.getProperty("AltOlyMaxEnchant", -1);
			ALT_OLY_USE_CUSTOM_PERIOD_SETTINGS = events.getProperty("AltOlyUseCustomPeriodSettings", false);
			ALT_OLY_PERIOD = events.getProperty("AltOlyPeriod", "MONTH");
			ALT_OLY_PERIOD_MULTIPLIER = events.getProperty("AltOlyPeriodMultiplier", 1);
			
			ALT_GAME_REQUIRE_CLAN_CASTLE = events.getProperty("AltRequireClanCastle", false);
			ALT_GAME_CASTLE_DAWN = events.getProperty("AltCastleForDawn", true);
			ALT_GAME_CASTLE_DUSK = events.getProperty("AltCastleForDusk", true);
			ALT_FESTIVAL_MIN_PLAYER = events.getProperty("AltFestivalMinPlayer", 5);
			ALT_MAXIMUM_PLAYER_CONTRIB = events.getProperty("AltMaxPlayerContrib", 1000000);
			ALT_FESTIVAL_MANAGER_START = events.getProperty("AltFestivalManagerStart", 120000);
			ALT_FESTIVAL_LENGTH = events.getProperty("AltFestivalLength", 1080000);
			ALT_FESTIVAL_CYCLE_LENGTH = events.getProperty("AltFestivalCycleLength", 2280000);
			ALT_FESTIVAL_FIRST_SPAWN = events.getProperty("AltFestivalFirstSpawn", 120000);
			ALT_FESTIVAL_FIRST_SWARM = events.getProperty("AltFestivalFirstSwarm", 300000);
			ALT_FESTIVAL_SECOND_SPAWN = events.getProperty("AltFestivalSecondSpawn", 540000);
			ALT_FESTIVAL_SECOND_SWARM = events.getProperty("AltFestivalSecondSwarm", 720000);
			ALT_FESTIVAL_CHEST_SPAWN = events.getProperty("AltFestivalChestSpawn", 900000);
			ALT_SEVENSIGNS_LAZY_UPDATE = events.getProperty("AltSevenSignsLazyUpdate", true);
			
			FS_TIME_ATTACK = events.getProperty("TimeOfAttack", 50);
			FS_TIME_COOLDOWN = events.getProperty("TimeOfCoolDown", 5);
			FS_TIME_ENTRY = events.getProperty("TimeOfEntry", 3);
			FS_TIME_WARMUP = events.getProperty("TimeOfWarmUp", 2);
			FS_PARTY_MEMBER_COUNT = events.getProperty("NumberOfNecessaryPartyMembers", 4);
			
			RIFT_MIN_PARTY_SIZE = events.getProperty("RiftMinPartySize", 2);
			RIFT_MAX_JUMPS = events.getProperty("MaxRiftJumps", 4);
			RIFT_SPAWN_DELAY = events.getProperty("RiftSpawnDelay", 10000);
			RIFT_AUTO_JUMPS_TIME_MIN = events.getProperty("AutoJumpsDelayMin", 480);
			RIFT_AUTO_JUMPS_TIME_MAX = events.getProperty("AutoJumpsDelayMax", 600);
			RIFT_ENTER_COST_RECRUIT = events.getProperty("RecruitCost", 18);
			RIFT_ENTER_COST_SOLDIER = events.getProperty("SoldierCost", 21);
			RIFT_ENTER_COST_OFFICER = events.getProperty("OfficerCost", 24);
			RIFT_ENTER_COST_CAPTAIN = events.getProperty("CaptainCost", 27);
			RIFT_ENTER_COST_COMMANDER = events.getProperty("CommanderCost", 30);
			RIFT_ENTER_COST_HERO = events.getProperty("HeroCost", 33);
			RIFT_BOSS_ROOM_TIME_MUTIPLY = events.getProperty("BossRoomTimeMultiply", 1.);
			
			ALLOW_WEDDING = events.getProperty("AllowWedding", false);
			WEDDING_PRICE = events.getProperty("WeddingPrice", 1000000);
			WEDDING_SAMESEX = events.getProperty("WeddingAllowSameSex", false);
			WEDDING_FORMALWEAR = events.getProperty("WeddingFormalWear", true);
			
			ALT_LOTTERY_PRIZE = events.getProperty("AltLotteryPrize", 50000);
			ALT_LOTTERY_TICKET_PRICE = events.getProperty("AltLotteryTicketPrice", 2000);
			ALT_LOTTERY_5_NUMBER_RATE = events.getProperty("AltLottery5NumberRate", 0.6);
			ALT_LOTTERY_4_NUMBER_RATE = events.getProperty("AltLottery4NumberRate", 0.2);
			ALT_LOTTERY_3_NUMBER_RATE = events.getProperty("AltLottery3NumberRate", 0.2);
			ALT_LOTTERY_2_AND_1_NUMBER_PRIZE = events.getProperty("AltLottery2and1NumberPrize", 200);
			
			ALT_FISH_CHAMPIONSHIP_ENABLED = events.getProperty("AltFishChampionshipEnabled", true);
			ALT_FISH_CHAMPIONSHIP_REWARD_ITEM = events.getProperty("AltFishChampionshipRewardItemId", 57);
			ALT_FISH_CHAMPIONSHIP_REWARD_1 = events.getProperty("AltFishChampionshipReward1", 800000);
			ALT_FISH_CHAMPIONSHIP_REWARD_2 = events.getProperty("AltFishChampionshipReward2", 500000);
			ALT_FISH_CHAMPIONSHIP_REWARD_3 = events.getProperty("AltFishChampionshipReward3", 300000);
			ALT_FISH_CHAMPIONSHIP_REWARD_4 = events.getProperty("AltFishChampionshipReward4", 200000);
			ALT_FISH_CHAMPIONSHIP_REWARD_5 = events.getProperty("AltFishChampionshipReward5", 100000);
			
			// FloodProtector
			ExProperties security = load(FLOOD_PROTECTOR_FILE);
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_ROLL_DICE, "RollDice", "42");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SHOUT_VOICE, "GlobalChat", "100");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_TRADE_CHAT, "TradeChat", "100");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_HERO_VOICE, "HeroVoice", "100");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SUBCLASS, "Subclass", "20");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_DROP_ITEM, "DropItem", "10");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SERVER_BYPASS, "ServerBypass", "5");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MULTISELL, "MultiSell", "1");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MANUFACTURE, "Manufacture", "3");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MANOR, "Manor", "30");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SENDMAIL, "SendMail", "100");
			loadFloodProtectorConfig(security, FLOOD_PROTECTOR_CHARACTER_SELECT, "CharacterSelect", "30");
			
			// HexID
			ExProperties hexid = load(HEXID_FILE);
			SERVER_ID = Integer.parseInt(hexid.getProperty("ServerID"));
			HEX_ID = new BigInteger(hexid.getProperty("HexID"), 16).toByteArray();
			
			// NPCs / Monsters
			ExProperties npcs = load(NPCS_FILE);
			CHAMPION_FREQUENCY = npcs.getProperty("ChampionFrequency", 0);
			CHAMP_MIN_LVL = npcs.getProperty("ChampionMinLevel", 20);
			CHAMP_MAX_LVL = npcs.getProperty("ChampionMaxLevel", 70);
			CHAMPION_HP = npcs.getProperty("ChampionHp", 8);
			CHAMPION_HP_REGEN = npcs.getProperty("ChampionHpRegen", 1.);
			CHAMPION_REWARDS = npcs.getProperty("ChampionRewards", 8);
			CHAMPION_ADENAS_REWARDS = npcs.getProperty("ChampionAdenasRewards", 1);
			CHAMPION_ATK = npcs.getProperty("ChampionAtk", 1.);
			CHAMPION_SPD_ATK = npcs.getProperty("ChampionSpdAtk", 1.);
			CHAMPION_REWARD = npcs.getProperty("ChampionRewardItem", 0);
			CHAMPION_REWARD_ID = npcs.getProperty("ChampionRewardItemID", 6393);
			CHAMPION_REWARD_QTY = npcs.getProperty("ChampionRewardItemQty", 1);
			
			BUFFER_MAX_SCHEMES = npcs.getProperty("BufferMaxSchemesPerChar", 4);
			BUFFER_STATIC_BUFF_COST = npcs.getProperty("BufferStaticCostPerBuff", -1);
			
			FIGHTER_BUFF = npcs.getProperty("FighterBuffList", "0");
			FIGHTER_BUFF_LIST = new ArrayList<>();
			for (String id : FIGHTER_BUFF.trim().split(","))
			{
				FIGHTER_BUFF_LIST.add(Integer.parseInt(id.trim()));
			}
			
			MAGE_BUFF = npcs.getProperty("MageBuffList", "0");
			MAGE_BUFF_LIST = new ArrayList<>();
			for (String id : MAGE_BUFF.trim().split(","))
			{
				MAGE_BUFF_LIST.add(Integer.parseInt(id.trim()));
			}
			
			ALLOW_CLASS_MASTERS = npcs.getProperty("AllowClassMasters", false);
			ALLOW_ENTIRE_TREE = npcs.getProperty("AllowEntireTree", false);
			if (ALLOW_CLASS_MASTERS)
				CLASS_MASTER_SETTINGS = new ClassMasterSettings(npcs.getProperty("ConfigClassMaster"));
			
			ALTERNATE_CLASS_MASTER = npcs.getProperty("AlternateClassMaster", false);
			ALT_GAME_FREE_TELEPORT = npcs.getProperty("AltFreeTeleporting", false);
			ANNOUNCE_MAMMON_SPAWN = npcs.getProperty("AnnounceMammonSpawn", true);
			ALT_MOB_AGRO_IN_PEACEZONE = npcs.getProperty("AltMobAgroInPeaceZone", true);
			SHOW_NPC_LVL = npcs.getProperty("ShowNpcLevel", false);
			SHOW_NPC_CREST = npcs.getProperty("ShowNpcCrest", false);
			SHOW_SUMMON_CREST = npcs.getProperty("ShowSummonCrest", false);
			
			WYVERN_ALLOW_UPGRADER = npcs.getProperty("AllowWyvernUpgrader", true);
			WYVERN_REQUIRED_LEVEL = npcs.getProperty("RequiredStriderLevel", 55);
			WYVERN_REQUIRED_CRYSTALS = npcs.getProperty("RequiredCrystalsNumber", 10);
			
			RAID_HP_REGEN_MULTIPLIER = npcs.getProperty("RaidHpRegenMultiplier", 1.);
			RAID_MP_REGEN_MULTIPLIER = npcs.getProperty("RaidMpRegenMultiplier", 1.);
			RAID_DEFENCE_MULTIPLIER = npcs.getProperty("RaidDefenceMultiplier", 1.);
			RAID_MINION_RESPAWN_TIMER = npcs.getProperty("RaidMinionRespawnTime", 300000);
			
			PLAYERS_CAN_HEAL_RB = npcs.getProperty("PlayersCanHealRaid", false);
			RAID_DISABLE_CURSE = npcs.getProperty("DisableRaidCurse", false);
			RAID_CHAOS_TIME = npcs.getProperty("RaidChaosTime", 30);
			GRAND_CHAOS_TIME = npcs.getProperty("GrandChaosTime", 30);
			MINION_CHAOS_TIME = npcs.getProperty("MinionChaosTime", 30);
			
			SPAWN_INTERVAL_AQ = npcs.getProperty("AntQueenSpawnInterval", 36);
			RANDOM_SPAWN_TIME_AQ = npcs.getProperty("AntQueenRandomSpawn", 17);
			
			SPAWN_INTERVAL_ANTHARAS = npcs.getProperty("AntharasSpawnInterval", 264);
			RANDOM_SPAWN_TIME_ANTHARAS = npcs.getProperty("AntharasRandomSpawn", 72);
			WAIT_TIME_ANTHARAS = npcs.getProperty("AntharasWaitTime", 30) * 60000;
			
			SPAWN_INTERVAL_BAIUM = npcs.getProperty("BaiumSpawnInterval", 168);
			RANDOM_SPAWN_TIME_BAIUM = npcs.getProperty("BaiumRandomSpawn", 48);
			
			SPAWN_INTERVAL_CORE = npcs.getProperty("CoreSpawnInterval", 60);
			RANDOM_SPAWN_TIME_CORE = npcs.getProperty("CoreRandomSpawn", 23);
			
			SPAWN_INTERVAL_FRINTEZZA = npcs.getProperty("FrintezzaSpawnInterval", 48);
			RANDOM_SPAWN_TIME_FRINTEZZA = npcs.getProperty("FrintezzaRandomSpawn", 8);
			BYPASS_FRINTEZZA_PARTIES_CHECK = npcs.getProperty("BypassPartiesCheck", false);
			FRINTEZZA_MIN_PARTIES = Integer.parseInt(npcs.getProperty("FrintezzaMinParties", "4"));
			FRINTEZZA_MAX_PARTIES = Integer.parseInt(npcs.getProperty("FrintezzaMaxParties", "5"));
            WAIT_TIME_FRINTEZZA = npcs.getProperty("FrintezzaWaitTime", 1) * 60000;
            DESPAWN_TIME_FRINTEZZA = npcs.getProperty("FrintezzaDespawnTime", 1) * 60000;
            FRINTEZZA_TIME_CHALLENGE = npcs.getProperty("FrintezzaTimeChallenge", 1) * 60000;
			
			SPAWN_INTERVAL_ORFEN = npcs.getProperty("OrfenSpawnInterval", 48);
			RANDOM_SPAWN_TIME_ORFEN = npcs.getProperty("OrfenRandomSpawn", 20);
			
			SPAWN_INTERVAL_SAILREN = npcs.getProperty("SailrenSpawnInterval", 36);
			RANDOM_SPAWN_TIME_SAILREN = npcs.getProperty("SailrenRandomSpawn", 24);
			WAIT_TIME_SAILREN = npcs.getProperty("SailrenWaitTime", 5) * 60000;
			
			SPAWN_INTERVAL_VALAKAS = npcs.getProperty("ValakasSpawnInterval", 264);
			RANDOM_SPAWN_TIME_VALAKAS = npcs.getProperty("ValakasRandomSpawn", 72);
			WAIT_TIME_VALAKAS = npcs.getProperty("ValakasWaitTime", 30) * 60000;
			
			SPAWN_INTERVAL_ZAKEN = npcs.getProperty("ZakenSpawnInterval", 60);
			RANDOM_SPAWN_TIME_ZAKEN = npcs.getProperty("ZakenRandomSpawn", 20);
			
			FWA_FIXTIMEPATTERNOFANTHARAS = npcs.getProperty("AntharasRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFBAIUM = npcs.getProperty("BaiumRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFCORE = npcs.getProperty("CoreRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFORFEN = npcs.getProperty("OrfenRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFVALAKAS = npcs.getProperty("ValakasRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFZAKEN = npcs.getProperty("ZakenRespawnTimePattern", "");
			FWA_FIXTIMEPATTERNOFQA = npcs.getProperty("QueenAntRespawnTimePattern", "");
			
			GUARD_ATTACK_AGGRO_MOB = npcs.getProperty("GuardAttackAggroMob", false);
			MAX_DRIFT_RANGE = npcs.getProperty("MaxDriftRange", 300);
			MAX_DRIFT_RANGE_EPIC = npcs.getProperty("MaxDriftRangeEpic", 300);
			KNOWNLIST_UPDATE_INTERVAL = npcs.getProperty("KnownListUpdateInterval", 1250);
			MIN_NPC_ANIMATION = npcs.getProperty("MinNPCAnimation", 20);
			MAX_NPC_ANIMATION = npcs.getProperty("MaxNPCAnimation", 40);
			MIN_MONSTER_ANIMATION = npcs.getProperty("MinMonsterAnimation", 10);
			MAX_MONSTER_ANIMATION = npcs.getProperty("MaxMonsterAnimation", 40);
			
			GRIDS_ALWAYS_ON = npcs.getProperty("GridsAlwaysOn", false);
			GRID_NEIGHBOR_TURNON_TIME = npcs.getProperty("GridNeighborTurnOnTime", 1);
			GRID_NEIGHBOR_TURNOFF_TIME = npcs.getProperty("GridNeighborTurnOffTime", 90);
			
			ENABLE_SKIPPING = npcs.getProperty("EnableSkippingItems", false);
		    ITEM_ID_BUY_CLAN_HALL = npcs.getProperty("ItemIDBuyClanHall", 57);
			 
			// players
			ExProperties players = load(PLAYERS_FILE);
			STARTING_ADENA = players.getProperty("StartingAdena", 100);
			
			CUSTOM_STARTER_ITEMS_ENABLED = players.getProperty("CustomStarterItemsEnabled", false);
			if (Config.CUSTOM_STARTER_ITEMS_ENABLED)
			{
				String[] propertySplit = players.getProperty("StartingCustomItemsMage", "57,0").split(";");
				STARTING_CUSTOM_ITEMS_M.clear();
				for (final String reward : propertySplit)
				{
					final String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2)
						_log.warning("StartingCustomItemsMage[Config.load()]: invalid config property -> StartingCustomItemsMage \"" + reward + "\"");
					else
					{
						try
						{
							STARTING_CUSTOM_ITEMS_M.add(new int[]
							{
								Integer.parseInt(rewardSplit[0]),
								Integer.parseInt(rewardSplit[1])
							});
						}
						catch (final NumberFormatException nfe)
						{
							if (!reward.isEmpty())
								_log.warning("StartingCustomItemsMage[Config.load()]: invalid config property -> StartingCustomItemsMage \"" + reward + "\"");
						}
					}
				}
				
				propertySplit = players.getProperty("StartingCustomItemsFighter", "57,0").split(";");
				STARTING_CUSTOM_ITEMS_F.clear();
				for (final String reward : propertySplit)
				{
					final String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2)
						_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
					else
					{
						try
						{
							STARTING_CUSTOM_ITEMS_F.add(new int[]
							{
								Integer.parseInt(rewardSplit[0]),
								Integer.parseInt(rewardSplit[1])
							});
						}
						catch (final NumberFormatException nfe)
						{
							if (!reward.isEmpty())
								_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}

			
			EFFECT_CANCELING = players.getProperty("CancelLesserEffect", true);
			HP_REGEN_MULTIPLIER = players.getProperty("HpRegenMultiplier", 1.);
			MP_REGEN_MULTIPLIER = players.getProperty("MpRegenMultiplier", 1.);
			CP_REGEN_MULTIPLIER = players.getProperty("CpRegenMultiplier", 1.);
			PLAYER_SPAWN_PROTECTION = players.getProperty("PlayerSpawnProtection", 0);
			PLAYER_FAKEDEATH_UP_PROTECTION = players.getProperty("PlayerFakeDeathUpProtection", 0);
			RESPAWN_RESTORE_HP = players.getProperty("RespawnRestoreHP", 0.7);
			RESPAWN_RESTORE_MP = players.getProperty("RespawnRestoreMP", 0.7);
			RESPAWN_RESTORE_CP = players.getProperty("RespawnRestoreCP", 0.7);
			MAX_PVTSTORE_SLOTS_DWARF = players.getProperty("MaxPvtStoreSlotsDwarf", 5);
			MAX_PVTSTORE_SLOTS_OTHER = players.getProperty("MaxPvtStoreSlotsOther", 4);
			DEEPBLUE_DROP_RULES = players.getProperty("UseDeepBlueDropRules", true);
			ALT_GAME_DELEVEL = players.getProperty("Delevel", true);
			DEATH_PENALTY_CHANCE = players.getProperty("DeathPenaltyChance", 20);
			
			INVENTORY_MAXIMUM_NO_DWARF = players.getProperty("MaximumSlotsForNoDwarf", 80);
			INVENTORY_MAXIMUM_DWARF = players.getProperty("MaximumSlotsForDwarf", 100);
			INVENTORY_MAXIMUM_QUEST_ITEMS = players.getProperty("MaximumSlotsForQuestItems", 100);
			INVENTORY_MAXIMUM_PET = players.getProperty("MaximumSlotsForPet", 12);
			MAX_ITEM_IN_PACKET = Math.max(INVENTORY_MAXIMUM_NO_DWARF, INVENTORY_MAXIMUM_DWARF);
			ALT_WEIGHT_LIMIT = players.getProperty("AltWeightLimit", 1);
			WAREHOUSE_SLOTS_NO_DWARF = players.getProperty("MaximumWarehouseSlotsForNoDwarf", 100);
			WAREHOUSE_SLOTS_DWARF = players.getProperty("MaximumWarehouseSlotsForDwarf", 120);
			WAREHOUSE_SLOTS_CLAN = players.getProperty("MaximumWarehouseSlotsForClan", 150);
			FREIGHT_SLOTS = players.getProperty("MaximumFreightSlots", 20);
			ALT_GAME_FREIGHTS = players.getProperty("AltGameFreights", false);
			ALT_GAME_FREIGHT_PRICE = players.getProperty("AltGameFreightPrice", 1000);

			AUGMENTATION_NG_SKILL_CHANCE = players.getProperty("AugmentationNGSkillChance", 15);
			AUGMENTATION_NG_GLOW_CHANCE = players.getProperty("AugmentationNGGlowChance", 0);
			AUGMENTATION_NG_BASESTAT_CHANCE = players.getProperty("AugmentationNGBaseStatChance", 1);
			
			AUGMENTATION_MID_SKILL_CHANCE = players.getProperty("AugmentationMidSkillChance", 30);
			AUGMENTATION_MID_GLOW_CHANCE = players.getProperty("AugmentationMidGlowChance", 40);
			AUGMENTATION_MID_BASESTAT_CHANCE = players.getProperty("AugmentationMidBaseStatChance", 1);
			
			AUGMENTATION_HIGH_SKILL_CHANCE = players.getProperty("AugmentationHighSkillChance", 45);
			AUGMENTATION_HIGH_GLOW_CHANCE = players.getProperty("AugmentationHighGlowChance", 70);
			AUGMENTATION_HIGH_BASESTAT_CHANCE = players.getProperty("AugmentationHighBaseStatChance", 1);
			
			AUGMENTATION_TOP_SKILL_CHANCE = players.getProperty("AugmentationTopSkillChance", 60);
			AUGMENTATION_TOP_GLOW_CHANCE = players.getProperty("AugmentationTopGlowChance", 100);
			AUGMENTATION_TOP_BASESTAT_CHANCE = players.getProperty("AugmentationTopBaseStatChance", 1);
			
			//AUGMENTATION_BASESTAT_CHANCE = players.getProperty("AugmentationBaseStatChance", 1);
			
			KARMA_PLAYER_CAN_BE_KILLED_IN_PZ = players.getProperty("KarmaPlayerCanBeKilledInPeaceZone", false);
			KARMA_PLAYER_CAN_SHOP = players.getProperty("KarmaPlayerCanShop", true);
			KARMA_PLAYER_CAN_USE_GK = players.getProperty("KarmaPlayerCanUseGK", false);
			KARMA_PLAYER_CAN_TELEPORT = players.getProperty("KarmaPlayerCanTeleport", true);
			KARMA_PLAYER_CAN_TRADE = players.getProperty("KarmaPlayerCanTrade", true);
			KARMA_PLAYER_CAN_USE_WH = players.getProperty("KarmaPlayerCanUseWareHouse", true);
			KARMA_DROP_GM = players.getProperty("CanGMDropEquipment", false);
			KARMA_AWARD_PK_KILL = players.getProperty("AwardPKKillPVPPoint", true);
			KARMA_PK_LIMIT = players.getProperty("MinimumPKRequiredToDrop", 5);
			KARMA_LOST_BASE = players.getProperty("BaseKarmaLost", 100);
			KARMA_NONDROPPABLE_PET_ITEMS = players.getProperty("ListOfPetItems", "2375,3500,3501,3502,4422,4423,4424,4425,6648,6649,6650");
			KARMA_NONDROPPABLE_ITEMS = players.getProperty("ListOfNonDroppableItemsForPK", "1147,425,1146,461,10,2368,7,6,2370,2369");
			
			String[] array = KARMA_NONDROPPABLE_PET_ITEMS.split(",");
			KARMA_LIST_NONDROPPABLE_PET_ITEMS = new int[array.length];
			
			for (int i = 0; i < array.length; i++)
				KARMA_LIST_NONDROPPABLE_PET_ITEMS[i] = Integer.parseInt(array[i]);
			
			array = KARMA_NONDROPPABLE_ITEMS.split(",");
			KARMA_LIST_NONDROPPABLE_ITEMS = new int[array.length];
			
			for (int i = 0; i < array.length; i++)
				KARMA_LIST_NONDROPPABLE_ITEMS[i] = Integer.parseInt(array[i]);
			
			// sorting so binarySearch can be used later
			Arrays.sort(KARMA_LIST_NONDROPPABLE_PET_ITEMS);
			Arrays.sort(KARMA_LIST_NONDROPPABLE_ITEMS);
			
			PVP_NORMAL_TIME = players.getProperty("PvPVsNormalTime", 15000);
			PVP_PVP_TIME = players.getProperty("PvPVsPvPTime", 30000);
			
			PARTY_XP_CUTOFF_METHOD = players.getProperty("PartyXpCutoffMethod", "level");
			PARTY_XP_CUTOFF_PERCENT = players.getProperty("PartyXpCutoffPercent", 3.);
			PARTY_XP_CUTOFF_LEVEL = players.getProperty("PartyXpCutoffLevel", 20);
			ALT_PARTY_RANGE = players.getProperty("AltPartyRange", 1600);
			ALT_PARTY_RANGE2 = players.getProperty("AltPartyRange2", 1400);
			ALT_LEAVE_PARTY_LEADER = players.getProperty("AltLeavePartyLeader", false);
			
			EVERYBODY_HAS_ADMIN_RIGHTS = players.getProperty("EverybodyHasAdminRights", false);
			MASTERACCESS_LEVEL = players.getProperty("MasterAccessLevel", 127);
			MASTERACCESS_NAME_COLOR = Integer.decode(StringUtil.concat("0x", players.getProperty("MasterNameColor", "00FF00")));
			MASTERACCESS_TITLE_COLOR = Integer.decode(StringUtil.concat("0x", players.getProperty("MasterTitleColor", "00FF00")));
			GM_HERO_AURA = players.getProperty("GMHeroAura", false);
			GM_STARTUP_INVULNERABLE = players.getProperty("GMStartupInvulnerable", true);
			GM_STARTUP_INVISIBLE = players.getProperty("GMStartupInvisible", true);
			GM_STARTUP_SILENCE = players.getProperty("GMStartupSilence", true);
			GM_STARTUP_AUTO_LIST = players.getProperty("GMStartupAutoList", true);
			
			PETITIONING_ALLOWED = players.getProperty("PetitioningAllowed", true);
			MAX_PETITIONS_PER_PLAYER = players.getProperty("MaxPetitionsPerPlayer", 5);
			MAX_PETITIONS_PENDING = players.getProperty("MaxPetitionsPending", 25);
			
			IS_CRAFTING_ENABLED = players.getProperty("CraftingEnabled", true);
			DWARF_RECIPE_LIMIT = players.getProperty("DwarfRecipeLimit", 50);
			COMMON_RECIPE_LIMIT = players.getProperty("CommonRecipeLimit", 50);
			ALT_BLACKSMITH_USE_RECIPES = players.getProperty("AltBlacksmithUseRecipes", true);
			
			AUTO_LEARN_SKILLS = players.getProperty("AutoLearnSkills", false);
			ALT_GAME_MAGICFAILURES = players.getProperty("MagicFailures", true);
			ALT_GAME_SHIELD_BLOCKS = players.getProperty("AltShieldBlocks", false);
			ALT_PERFECT_SHLD_BLOCK = players.getProperty("AltPerfectShieldBlockRate", 10);
			LIFE_CRYSTAL_NEEDED = players.getProperty("LifeCrystalNeeded", true);
			SP_BOOK_NEEDED = players.getProperty("SpBookNeeded", true);
			ES_SP_BOOK_NEEDED = players.getProperty("EnchantSkillSpBookNeeded", true);
			AUTO_LEARN_DIVINE_INSPIRATION = players.getProperty("AutoLearnDivineInspiration", false);
			DIVINE_SP_BOOK_NEEDED = players.getProperty("DivineInspirationSpBookNeeded", true);
			ALT_GAME_SUBCLASS_WITHOUT_QUESTS = players.getProperty("AltSubClassWithoutQuests", false);
			ALT_GAME_SUBCLASS_EVERYWHERE = players.getProperty("AltSubclassEverywhere", false);
			BUFFS_MAX_AMOUNT = players.getProperty("MaxBuffsAmount", 20);
			STORE_SKILL_COOLTIME = players.getProperty("StoreSkillCooltime", true);

			// server
			ExProperties server = load(SERVER_FILE);
			
			GAMESERVER_HOSTNAME = server.getProperty("GameserverHostname");
			PORT_GAME = server.getProperty("GameserverPort", 7777);
			
			EXTERNAL_HOSTNAME = server.getProperty("ExternalHostname", "*");
			INTERNAL_HOSTNAME = server.getProperty("InternalHostname", "*");
			
			GAME_SERVER_LOGIN_PORT = server.getProperty("LoginPort", 9014);
			GAME_SERVER_LOGIN_HOST = server.getProperty("LoginHost", "127.0.0.1");
			
			REQUEST_ID = server.getProperty("RequestServerID", 0);
			ACCEPT_ALTERNATE_ID = server.getProperty("AcceptAlternateID", true);
			
			DATABASE_URL = server.getProperty("URL", "jdbc:mysql://localhost/acis");
			DATABASE_LOGIN = server.getProperty("Login", "root");
			DATABASE_PASSWORD = server.getProperty("Password", "");
			DATABASE_MAX_CONNECTIONS = server.getProperty("MaximumDbConnections", 10);
			DATABASE_MAX_IDLE_TIME = server.getProperty("MaximumDbIdleTime", 0);
			
			SERVER_LIST_BRACKET = server.getProperty("ServerListBrackets", false);
			SERVER_LIST_CLOCK = server.getProperty("ServerListClock", false);
			SERVER_GMONLY = server.getProperty("ServerGMOnly", false);
			TEST_SERVER = server.getProperty("TestServer", false);
			SERVER_LIST_TESTSERVER = server.getProperty("TestServer", false);
			
			DELETE_DAYS = server.getProperty("DeleteCharAfterDays", 7);
			MAXIMUM_ONLINE_USERS = server.getProperty("MaximumOnlineUsers", 100);

			JAIL_IS_PVP = server.getProperty("JailIsPvp", true);
			DEFAULT_PUNISH = server.getProperty("DefaultPunish", 2);
			DEFAULT_PUNISH_PARAM = server.getProperty("DefaultPunishParam", 0);
			
			MULTIBOX_PROTECTION_ENABLED = server.getProperty("MultiboxProtectionEnabled", false); 
			MULTIBOX_PROTECTION_CLIENTS_PER_PC = server.getProperty("ClientsPerPc", 2); 
			MULTIBOX_PROTECTION_PUNISH = server.getProperty("MultiboxPunish", 2);
			
			HWID_MULTIBOX_PROTECTION_ENABLED = server.getProperty("HwidMultiboxProtectionEnabled", false); 
			HWID_MULTIBOX_PROTECTION_CLIENTS_PER_PC = server.getProperty("HwidClientsPerPc", 2); 
			HWID_MULTIBOX_PROTECTION_PUNISH = server.getProperty("HwidMultiboxPunish", 2);
			
			//PROTOCOL_REVISION = server.getProperty("ProtocolRevision", 730);
			//GAMESERVER_SESSION_KEY = DatatypeConverter.printBase64Binary(server.getProperty("SessionKey", "").getBytes());
			//MAX_PLAYER_PER_PC = server.getProperty("MaxPlayerPerPc", 0);

			MIN_PROTOCOL_REVISION = server.getProperty("MinProtocolRevision", 730);
			MAX_PROTOCOL_REVISION = server.getProperty("MaxProtocolRevision", 746);
			if (MIN_PROTOCOL_REVISION > MAX_PROTOCOL_REVISION)
				throw new Error("MinProtocolRevision is bigger than MaxProtocolRevision in server.properties.");

			AUTO_LOOT = server.getProperty("AutoLoot", false);
			AUTO_LOOT_HERBS = server.getProperty("AutoLootHerbs", false);
			AUTO_LOOT_RAID = server.getProperty("AutoLootRaid", false);
			
			ALLOW_DISCARDITEM = server.getProperty("AllowDiscardItem", true);
			MULTIPLE_ITEM_DROP = server.getProperty("MultipleItemDrop", true);
			ITEM_AUTO_DESTROY_TIME = server.getProperty("AutoDestroyItemTime", 0) * 1000;
			HERB_AUTO_DESTROY_TIME = server.getProperty("AutoDestroyHerbTime", 15) * 1000;
			PROTECTED_ITEMS = server.getProperty("ListOfProtectedItems");
			
			LIST_PROTECTED_ITEMS = new ArrayList<>();
			for (String id : PROTECTED_ITEMS.split(","))
				LIST_PROTECTED_ITEMS.add(Integer.parseInt(id));
			
			DESTROY_DROPPED_PLAYER_ITEM = server.getProperty("DestroyPlayerDroppedItem", false);
			DESTROY_EQUIPABLE_PLAYER_ITEM = server.getProperty("DestroyEquipableItem", false);
			SAVE_DROPPED_ITEM = server.getProperty("SaveDroppedItem", false);
			EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD = server.getProperty("EmptyDroppedItemTableAfterLoad", false);
			SAVE_DROPPED_ITEM_INTERVAL = server.getProperty("SaveDroppedItemInterval", 0) * 60000;
			CLEAR_DROPPED_ITEM_TABLE = server.getProperty("ClearDroppedItemTable", false);
			
			RATE_XP = server.getProperty("RateXp", 1.);
			RATE_SP = server.getProperty("RateSp", 1.);
			RATE_PARTY_XP = server.getProperty("RatePartyXp", 1.);
			RATE_PARTY_SP = server.getProperty("RatePartySp", 1.);
			RATE_DROP_ADENA = server.getProperty("RateDropAdena", 1.);
			RATE_CONSUMABLE_COST = server.getProperty("RateConsumableCost", 1.);
			RATE_DROP_ITEMS = server.getProperty("RateDropItems", 1.);
			RATE_DROP_SEAL_STONES = server.getProperty("RateDropSealStones", 1.);
			RATE_DROP_ITEMS_BY_RAID = server.getProperty("RateRaidDropItems", 1.);
			RATE_DROP_SPOIL = server.getProperty("RateDropSpoil", 1.);
			RATE_DROP_MANOR = server.getProperty("RateDropManor", 1);
			RATE_QUEST_DROP = server.getProperty("RateQuestDrop", 1.);
			RATE_QUEST_REWARD = server.getProperty("RateQuestReward", 1.);
			RATE_QUEST_REWARD_XP = server.getProperty("RateQuestRewardXP", 1.);
			RATE_QUEST_REWARD_SP = server.getProperty("RateQuestRewardSP", 1.);
			RATE_QUEST_REWARD_ADENA = server.getProperty("RateQuestRewardAdena", 1.);
			RATE_KARMA_EXP_LOST = server.getProperty("RateKarmaExpLost", 1.);
			RATE_SIEGE_GUARDS_PRICE = server.getProperty("RateSiegeGuardsPrice", 1.);
			RATE_DROP_COMMON_HERBS = server.getProperty("RateCommonHerbs", 1.);
			RATE_DROP_HP_HERBS = server.getProperty("RateHpHerbs", 1.);
			RATE_DROP_MP_HERBS = server.getProperty("RateMpHerbs", 1.);
			RATE_DROP_SPECIAL_HERBS = server.getProperty("RateSpecialHerbs", 1.);
			PLAYER_DROP_LIMIT = server.getProperty("PlayerDropLimit", 3);
			PLAYER_RATE_DROP = server.getProperty("PlayerRateDrop", 5);
			PLAYER_RATE_DROP_ITEM = server.getProperty("PlayerRateDropItem", 70);
			PLAYER_RATE_DROP_EQUIP = server.getProperty("PlayerRateDropEquip", 25);
			PLAYER_RATE_DROP_EQUIP_WEAPON = server.getProperty("PlayerRateDropEquipWeapon", 5);
			PET_XP_RATE = server.getProperty("PetXpRate", 1.);
			PET_FOOD_RATE = server.getProperty("PetFoodRate", 1);
			SINEATER_XP_RATE = server.getProperty("SinEaterXpRate", 1.);
			KARMA_DROP_LIMIT = server.getProperty("KarmaDropLimit", 10);
			KARMA_RATE_DROP = server.getProperty("KarmaRateDrop", 70);
			KARMA_RATE_DROP_ITEM = server.getProperty("KarmaRateDropItem", 50);
			KARMA_RATE_DROP_EQUIP = server.getProperty("KarmaRateDropEquip", 40);
			KARMA_RATE_DROP_EQUIP_WEAPON = server.getProperty("KarmaRateDropEquipWeapon", 10);
			AUG_WEAPON_DROPABLE = server.getProperty("AugmentedWeaponDropable", false);
			
			ALLOW_FREIGHT = server.getProperty("AllowFreight", true);
			ALLOW_WAREHOUSE = server.getProperty("AllowWarehouse", true);
			ALLOW_WEAR = server.getProperty("AllowWear", true);
			WEAR_DELAY = server.getProperty("WearDelay", 5);
			WEAR_PRICE = server.getProperty("WearPrice", 10);
			ALLOW_LOTTERY = server.getProperty("AllowLottery", true);
			ALLOW_RACE = server.getProperty("AllowRace", true);
			ALLOW_WATER = server.getProperty("AllowWater", true);
			ALLOWFISHING = server.getProperty("AllowFishing", false);
			ALLOW_MANOR = server.getProperty("AllowManor", true);
			ALLOW_BOAT = server.getProperty("AllowBoat", true);
			ALLOW_CURSED_WEAPONS = server.getProperty("AllowCursedWeapons", true);
			
			String str = server.getProperty("EnableFallingDamage", "auto");
			ENABLE_FALLING_DAMAGE = "auto".equalsIgnoreCase(str) ? GEODATA > 0 : Boolean.parseBoolean(str);
			
			ALT_DEV_NO_SCRIPTS = server.getProperty("NoScripts", false);
			ALT_DEV_NO_SPAWNS = server.getProperty("NoSpawns", false);
			DEBUG = server.getProperty("Debug", false);
			DEVELOPER = server.getProperty("Developer", false);
			PACKET_HANDLER_DEBUG = server.getProperty("PacketHandlerDebug", false);
			
			DEADLOCK_DETECTOR = server.getProperty("DeadLockDetector", false);
			DEADLOCK_CHECK_INTERVAL = server.getProperty("DeadLockCheckInterval", 20);
			RESTART_ON_DEADLOCK = server.getProperty("RestartOnDeadlock", false);
			
			LOG_CHAT = server.getProperty("LogChat", false);
			LOG_ITEMS = server.getProperty("LogItems", false);
			GMAUDIT = server.getProperty("GMAudit", false);
			
			ENABLE_COMMUNITY_BOARD = server.getProperty("EnableCommunityBoard", false);
			BBS_DEFAULT = server.getProperty("BBSDefault", "_bbshome");
			
			COORD_SYNCHRONIZE = server.getProperty("CoordSynchronize", -1);
			GEODATA = server.getProperty("GeoData", 0);
			FORCE_GEODATA = server.getProperty("ForceGeoData", true);
			
			GEODATA_CELLFINDING = server.getProperty("CellPathFinding", false);
			PATHFIND_BUFFERS = server.getProperty("PathFindBuffers", "100x6;128x6;192x6;256x4;320x4;384x4;500x2");
			LOW_WEIGHT = server.getProperty("LowWeight", 0.5);
			MEDIUM_WEIGHT = server.getProperty("MediumWeight", 2);
			HIGH_WEIGHT = server.getProperty("HighWeight", 3);
			ADVANCED_DIAGONAL_STRATEGY = server.getProperty("AdvancedDiagonalStrategy", true);
			DIAGONAL_WEIGHT = server.getProperty("DiagonalWeight", 0.707);
			MAX_POSTFILTER_PASSES = server.getProperty("MaxPostfilterPasses", 3);
			DEBUG_PATH = server.getProperty("DebugPath", false);
			
			L2WALKER_PROTECTION = server.getProperty("L2WalkerProtection", false);
			AUTODELETE_INVALID_QUEST_DATA = server.getProperty("AutoDeleteInvalidQuestData", false);
			GAMEGUARD_ENFORCE = server.getProperty("GameGuardEnforce", false);
			ZONE_TOWN = server.getProperty("ZoneTown", 0);
			SERVER_NEWS = server.getProperty("ShowServerNews", false);
			DISABLE_TUTORIAL = server.getProperty("DisableTutorial", false);
			LOAD_CUSTOM_MULTISELLS = server.getProperty("LoadCustomMultiSells", false);
			BREAK_ENCHANT = server.getProperty("BreakEnchant", 0);
			
			// L2Jmods config
			ExProperties l2jmod = load(L2JMOD_FILE);
			
			ALLOW_MOD_MENU = l2jmod.getProperty("AllowMenu", false);
			ALLOW_NEW_COLOR_MANAGER = l2jmod.getProperty("AllowNewColor", false);
			RAID_INFO_IDS = l2jmod.getProperty("RaidSpawnInfoIDs");
			
			RAID_INFO_IDS_LIST = new ArrayList<>();
			for(String id : RAID_INFO_IDS.split(","))
				RAID_INFO_IDS_LIST.add(Integer.parseInt(id));
			
			RAID_BOSS_INFO_PAGE_LIMIT = l2jmod.getProperty("RaidBossInfoPageLimit", 15);
			RAID_BOSS_DROP_PAGE_LIMIT = l2jmod.getProperty("RaidBossDropPageLimit", 15);
			RAID_BOSS_DATE_FORMAT = l2jmod.getProperty("RaidBossDateFormat", "MMM dd, HH:mm");
			
			RAID_BOSS_IDS = l2jmod.getProperty("RaidBossIds", "0,0");
			LIST_RAID_BOSS_IDS = new ArrayList<>();
			for (String val : RAID_BOSS_IDS.split(","))
			{
				int npcId = Integer.parseInt(val);
				LIST_RAID_BOSS_IDS.add(npcId);
			} 
			
			GRAND_BOSS_IDS = l2jmod.getProperty("GrandBossIds", "0,0");
			LIST_GRAND_BOSS_IDS = new ArrayList<>();
			for (String val : GRAND_BOSS_IDS.split(","))
			{
				int npcId = Integer.parseInt(val);
				LIST_GRAND_BOSS_IDS.add(npcId);
			} 
			
			BANKING_SYSTEM_GOLDBARS = l2jmod.getProperty("BankingGoldbarCount", 1);
			BANKING_SYSTEM_ADENA = l2jmod.getProperty("BankingAdenaCount", 500000000);	
			PVP_POINT_ID = l2jmod.getProperty("ColorCoinID", 57);
			PVP_POINT_COUNT = l2jmod.getProperty("ColorCoinCount", 200);	
			ALLOW_EVENT_COMMANDS = l2jmod.getProperty("AllowEventCommands", false);
			ALLOW_STATUS_COMMANDS = l2jmod.getProperty("AllowStatusCommands", false);
	        ALLOW_DONATE_COMMANDS = l2jmod.getProperty("AllowDonate", false);
			DONATE_COIN_ID = l2jmod.getProperty("DonateColorCoinID", 57);
			DONATE_COIN_COUNT = l2jmod.getProperty("DonateColorCoinCount", 200);
			ALLOW_WELCOME_TO_LINEAGE = l2jmod.getProperty("AllowWelcome", false);
			ALT_GIVE_PVP_IN_ARENA = l2jmod.getProperty("AltGivePvpInArena", false);
			SHOW_HP_PVP = l2jmod.getProperty("ShowHpPvP", false);
			
			PVPS_COLORS = l2jmod.getProperty("PvpsColorsName", "");
			PVPS_COLORS_LIST = new HashMap<>();
			
			String[] splitted_pvps_colors = PVPS_COLORS.split(";");
			
			for (String iii : splitted_pvps_colors)
			{
				String[] pvps_colors = iii.split(",");
				
				if (pvps_colors.length != 2)
				{
					System.out.println("Invalid properties.");
				}
				else
				{
					PVPS_COLORS_LIST.put(Integer.parseInt(pvps_colors[0]), Integer.decode("0x" + pvps_colors[1]));
				}
			}
			
			PKS_COLORS = l2jmod.getProperty("PksColorsTitle", "");
			PKS_COLORS_LIST = new HashMap<>();
			
			String[] splitted_pks_colors = PKS_COLORS.split(";");
			
			for (String iii : splitted_pks_colors)
			{
				String[] pks_colors = iii.split(",");
				
				if (pks_colors.length != 2)
				{
					System.out.println("Invalid properties.");
				}
				else
				{
					PKS_COLORS_LIST.put(Integer.parseInt(pks_colors[0]), Integer.decode("0x" + pks_colors[1]));
				}
			}
			
			TIME_TELEPORTER_ENABLE = l2jmod.getProperty("TimeTeleporter", false);
			if (TIME_TELEPORTER_ENABLE)
			{
				TIME_TELEPORTERS = new ArrayList<>();
				for (String type : l2jmod.getProperty("TimeTeleportersId", "10001").split(","))
				{
					TIME_TELEPORTERS.add(Integer.valueOf(type));
				}
			}
			
			VOTE_FOR_PVPZONE = l2jmod.getProperty("VoteForNextPvpZone", false);

			OFFLINE_TRADE_ENABLE = l2jmod.getProperty("OfflineTradeEnable", false);
			OFFLINE_CRAFT_ENABLE = l2jmod.getProperty("OfflineCraftEnable", false);
			RESTORE_OFFLINERS = l2jmod.getProperty("RestoreOffliners", false);
			OFFLINE_MAX_DAYS = l2jmod.getProperty("OfflineMaxDays", 10);
			OFFLINE_DISCONNECT_FINISHED = l2jmod.getProperty("OfflineDisconnectFinished", true);
            OFFLINE_MODE_IN_PEACE_ZONE = l2jmod.getProperty("OfflineModeInPeaceZone", false);
            OFFLINE_MODE_NO_DAMAGE = l2jmod.getProperty("OfflineModeNoDamage", false);
            OFFLINE_LOGOUT = l2jmod.getProperty("OfflineLogout", false);
            OFFLINE_SLEEP_EFFECT = l2jmod.getProperty("OfflineSleepEffect", true);
            
   			ENABLE_AIO_SYSTEM = l2jmod.getProperty("EnableAioSystem", true);
			ALLOW_AIO_NCOLOR = l2jmod.getProperty("AllowAioNameColor", true);
			AIO_NCOLOR = Integer.decode(StringUtil.concat("0x" + l2jmod.getProperty("AioNameColor", "88AA88")));
			ALLOW_AIO_TCOLOR = l2jmod.getProperty("AllowAioTitleColor", true);
			AIO_TCOLOR = Integer.decode(StringUtil.concat("0x" + l2jmod.getProperty("AioTitleColor", "88AA88")));
			ALLOW_AIO_ITEM = l2jmod.getProperty("AllowAIOItem", false);
			AIO_ITEMID = l2jmod.getProperty("ItemIdAio", 0);
			
			if(ENABLE_AIO_SYSTEM)
			{
				String[] AioSkillsSplit = l2jmod.getProperty("AioSkills", "").split(";");
				AIO_SKILLS = new HashMap<>(AioSkillsSplit.length);
				for (String skill : AioSkillsSplit)
				{
					String[] skillSplit = skill.split(",");
					if (skillSplit.length != 2)
						System.out.println("[Aio System]: invalid config property in players.properties -> AioSkills \"" + skill + "\"");
					else
					{
						try
						{
							AIO_SKILLS.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
						}
						catch (NumberFormatException nfe)
						{
							if (!skill.equals(""))
								System.out.println("[Aio System]: invalid config property in players.props -> AioSkills \"" + skillSplit[0] + "\"" + skillSplit[1]);
						}
					}
				}
			}
			
			ENABLE_VIP_SYSTEM = l2jmod.getProperty("EnableVipSystem", false);
			ALLOW_VIP_NCOLOR = l2jmod.getProperty("AllowVipNameColor", false);
			VIP_NCOLOR = Integer.decode("0x" + l2jmod.getProperty("VipNameColor", "88AA88"));
			ALLOW_VIP_TCOLOR = l2jmod.getProperty("AllowVipTitleColor", false);
			VIP_TCOLOR = Integer.decode("0x" + l2jmod.getProperty("VipTitleColor", "88AA88"));
			VIP_XP_SP_RATE = l2jmod.getProperty("VIPXpSpRate", 1.);
			VIP_ADENA_RATE = l2jmod.getProperty("VIPAdenaRate", 1.);
			VIP_DROP_RATE = l2jmod.getProperty("VIPDropRate", 1);
			VIP_SPOIL_RATE = l2jmod.getProperty("VIPSpoilRate", 1.);
			VIP_ITEMID = l2jmod.getProperty("ItemIdVip", 0);
			ALLOW_VIP_ITEM = l2jmod.getProperty("AllowVIPItem", false);
			ALLOW_DRESS_ME_VIP = l2jmod.getProperty("AllowVIPDress", false);
			VIP_EFFECT = l2jmod.getProperty("VipEffect", false);

			if(ENABLE_VIP_SYSTEM) //create map if system is enabled
			{
				String[] VipSkillsSplit = l2jmod.getProperty("VipSkills", "").split(";");
				VIP_SKILLS = new HashMap<>(VipSkillsSplit.length);
				for (String skill : VipSkillsSplit)
				{
					String[] skillSplit = skill.split(",");
					if (skillSplit.length != 2)
					{
						System.out.println("[VIP System]: invalid config property in players.properties -> VipSkills \"" + skill + "\"");
					}
					else
					{
						try
						{
							VIP_SKILLS.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
						}
						catch (NumberFormatException nfe)
						{
							if (!skill.equals(""))
							{
								System.out.println("[VIP System]: invalid config property in players.props -> VipSkills \"" + skillSplit[0] + "\"" + skillSplit[1]);
							}
						}
					}
				}
			}

            NOBLESS_FROM_BOSS = l2jmod.getProperty("NoblessFromBoss", false);
            BOSS_ID = l2jmod.getProperty("BossId", 25325);
            RADIUS_TO_RAID = l2jmod.getProperty("RadiusToRaid", 1000);
			
            NPCS_FLAG_RANGE = l2jmod.getProperty("NpcsFlagRangeDistanceOnKill", 1000);
            ALLOW_FLAG_ONKILL_BY_ID = l2jmod.getProperty("AllowFlagNpcOnKill", false);
            NPCS_FLAG_IDS = l2jmod.getProperty("NpcsFlagIDsOnKill", "29020,29019,25517,25523,25524");
            NPCS_FLAG_LIST = new ArrayList<>();
            for (final String id : NPCS_FLAG_IDS.split(","))
            {
            	NPCS_FLAG_LIST.add(Integer.parseInt(id));
            }
            
            LEAVE_BUFFS_ON_DIE = l2jmod.getProperty("LoseBuffsOnDeath", false);
            CHAOTIC_LEAVE_BUFFS_ON_DIE = l2jmod.getProperty("ChaoticLoseBuffsOnDeath", false);

           // PVPZONE_RESPAWN_DELAY = l2jmod.getProperty("PvpZoneRespawn", 10000);
           // RESTART_PACKET_PUNISH = server.getProperty("RespawnPacketPunish", 2);
            
            CUSTOM_START_LVL = l2jmod.getProperty("CustomStartLvl", 1);
            CUSTOM_SUBCLASS_LVL = l2jmod.getProperty("CustomSubclassLvl", 40);
            
			CHAR_TITLE = l2jmod.getProperty("CharTitle", false);
	        ADD_CHAR_TITLE = l2jmod.getProperty("CharAddTitle", "Welcome");
	        
	        FORBIDDEN_NAMES = l2jmod.getProperty("ForbiddenNames", "").split(",");
	        GM_NAMES = l2jmod.getProperty("GmNames", "").split(",");
	        
	        PRIVATE_STORE_NEED_PVPS = l2jmod.getProperty("AllowPvPToUseStore", false);
	        MIN_PVP_TO_USE_STORE = l2jmod.getProperty("PvPToUseStore", 0);
	        
	        PRIVATE_STORE_NEED_LEVELS = l2jmod.getProperty("AllowLevelToUseStore", false);
	        MIN_LEVEL_TO_USE_STORE = l2jmod.getProperty("LevelToUseStore", 0);
	        
			EXPERTISE_PENALTY = l2jmod.getProperty("ExpertisePenality", false);

			ALLOW_HERO_SUBSKILL = l2jmod.getProperty("CustomHeroSubSkill", false);
			HERO_COUNT = l2jmod.getProperty("HeroCount", 1);
			
			ALT_RESTORE_EFFECTS_ON_SUBCLASS_CHANGE = l2jmod.getProperty("AltRestoreEffectOnSub", false);
			
			CHECK_SKILLS_ON_ENTER = l2jmod.getProperty("CheckSkillsOnEnter", false);
			ALLOWED_SKILLS = l2jmod.getProperty("AllowedSkills", "541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,617,618,619");
			ALLOWED_SKILLS_LIST = new ArrayList<Integer>();
			for(String id : ALLOWED_SKILLS.trim().split(","))
				ALLOWED_SKILLS_LIST.add(Integer.parseInt(id.trim()));
			
			DISABLE_ATTACK_NPC_TYPE = l2jmod.getProperty("DisableAttackToNpcs", false);
			ALLOWED_NPC_TYPES = l2jmod.getProperty("AllowedNPCTypes");
			LIST_ALLOWED_NPC_TYPES = new ArrayList<String>();
			for (String npc_type : ALLOWED_NPC_TYPES.split(","))
				LIST_ALLOWED_NPC_TYPES.add(npc_type);
			 
			ALLOW_PVP_REWARD = l2jmod.getProperty("PvpRewardEnabled", false);
			PVP_ITEMS_REWARD = new ArrayList<>();
			String[] pvpReward = l2jmod.getProperty("PvpItemsReward", "57,100000").split(";");
			for (String reward : pvpReward)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2)
				{
					_log.warning(StringUtil.concat("PvpItemsReward: invalid config property -> PvpItemsReward \"", reward, "\""));
				}
				else
				{
					try
					{
						PVP_ITEMS_REWARD.add(new int[]
						{
							Integer.parseInt(rewardSplit[0]),
							Integer.parseInt(rewardSplit[1])
						});
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty())
						{
							_log.warning(StringUtil.concat("PvpItemsReward: invalid config property -> PvpItemsReward \"", reward, "\""));
						}
					}
				}
			}

			ALLOW_PK_REWARD = l2jmod.getProperty("PKRewardEnabled", false);
			PK_ITEMS_REWARD = new ArrayList<>();
			String[] pkReward = l2jmod.getProperty("PKItemsReward", "57,100000").split(";");
			for (String reward : pkReward)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2)
				{
					_log.warning(StringUtil.concat("PKItemsReward: invalid config property -> PKItemsReward \"", reward, "\""));
				}
				else
				{
					try
					{
						PK_ITEMS_REWARD.add(new int[]
						{
							Integer.parseInt(rewardSplit[0]),
							Integer.parseInt(rewardSplit[1])
						});
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty())
						{
							_log.warning(StringUtil.concat("PKItemsReward: invalid config property -> PKItemsReward \"", reward, "\""));
						}
					}
				}
			}
			
			ANTI_FARM_ENABLED = l2jmod.getProperty("AntiFarmEnabled", false);
        	ANTI_FARM_CLAN_ALLY_ENABLED = l2jmod.getProperty("AntiFarmClanAlly", false);
        	ANTI_FARM_LVL_DIFF_ENABLED = l2jmod.getProperty("AntiFarmLvlDiff", false);
        	ANTI_FARM_MAX_LVL_DIFF = l2jmod.getProperty("AntiFarmMaxLvlDiff", 40);
        	ANTI_FARM_PARTY_ENABLED = l2jmod.getProperty("AntiFarmParty", false);
        	ANTI_FARM_IP_ENABLED = l2jmod.getProperty("AntiFarmIP", false);   
 			
		    ALLOW_DRESS_ME_SYSTEM = l2jmod.getProperty("AllowDressMeSystem", false);
		    DRESS_ME_NEED_PVPS = l2jmod.getProperty("DressMeNeedPvp", false);
		    PVPS_TO_USE_DRESS_ME = l2jmod.getProperty("DressMePvPAmount", 0);
            
			// Heavy
			String temp = l2jmod.getProperty("HeavyChests", "");
			String[] temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_HEAVY_CHESTS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("HeavyLegs", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_HEAVY_LEGS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("HeavyGloves", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_HEAVY_GLOVES.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("HeavyBoots", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_HEAVY_BOOTS.put(t[0], Integer.parseInt(t[1]));
			}
			// Light
			temp = l2jmod.getProperty("LightChests", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_LIGHT_CHESTS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("LightLegs", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_LIGHT_LEGS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("LightGloves", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_LIGHT_GLOVES.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("LightBoots", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_LIGHT_BOOTS.put(t[0], Integer.parseInt(t[1]));
			}
			// Robe
			temp = l2jmod.getProperty("RobeChests", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_ROBE_CHESTS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("RobeLegs", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_ROBE_LEGS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("RobeGloves", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_ROBE_GLOVES.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("RobeBoots", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_ROBE_BOOTS.put(t[0], Integer.parseInt(t[1]));
			}
			// Custom
			temp = l2jmod.getProperty("EventChests", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_CUSTOM_CHESTS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("EventLegs", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_CUSTOM_LEGS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("EventGloves", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_CUSTOM_GLOVES.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("EventBoots", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_CUSTOM_BOOTS.put(t[0], Integer.parseInt(t[1]));
			}
			temp = l2jmod.getProperty("EventHair", "");
			temp2 = temp.split(";");
			for (String s : temp2)
			{
				String[] t = s.split(",");
				DRESS_ME_CUSTOM_HAIR.put(t[0], Integer.parseInt(t[1]));
			}
            SUMMON_MOUNT_PROTECTION = l2jmod.getProperty("SummonRestriction", false);
			ID_RESTRICT = l2jmod.getProperty("SummonItemID");
			
			LISTID_RESTRICT = new ArrayList<>();
			for(String id : ID_RESTRICT.split(","))
				LISTID_RESTRICT.add(Integer.parseInt(id));

			ANTIBOT_ENABLE = l2jmod.getProperty("AntiBotEnable", true);
			ANTIBOT_TIME_JAIL = l2jmod.getProperty("AntiBotTimeJail", 1);
			ANTIBOT_TIME_VOTE = l2jmod.getProperty("AntiBotTimeVote", 30);
			ANTIBOT_KILL_MOBS = l2jmod.getProperty("AntiBotKillMobs", 1);
			ANTIBOT_MIN_LEVEL = l2jmod.getProperty("AntiBotMinLevel", 1);	
			
			DELETE_AUGM_PASSIVE_ON_CHANGE = l2jmod.getProperty("DeleteAgmentPassiveEffectOnChangeWep", true);
			DELETE_AUGM_ACTIVE_ON_CHANGE = l2jmod.getProperty("DeleteAgmentActiveEffectOnChangeWep", true);

			REWARD_LV_1 = l2jmod.getProperty("Reward_Lv_1_ID", 57);
			REWARD_COUNT_1 = l2jmod.getProperty("Reward_Lv_1_Count", 200);
			
			REWARD_LV_2 = l2jmod.getProperty("Reward_Lv_2_ID", 57);
			REWARD_COUNT_2 = l2jmod.getProperty("Reward_Lv_2_Count", 200);
			
			REWARD_LV_3 = l2jmod.getProperty("Reward_Lv_3_ID", 57);
			REWARD_COUNT_3 = l2jmod.getProperty("Reward_Lv_3_Count", 200);
			
			REWARD_LV_4 = l2jmod.getProperty("Reward_Lv_4_ID", 57);
			REWARD_COUNT_4 = l2jmod.getProperty("Reward_Lv_4_Count", 200);

			PARTY_ZONE_CHAMPIONS = l2jmod.getProperty("ListChampions", "2375,3500,3501,3502,4422,4423,4424,4425,6648,6649,6650");

			array = PARTY_ZONE_CHAMPIONS.split(",");
			PARTY_ZONE_LIST_CHAMPIONS = new int[array.length];

			for (int i = 0; i < array.length; i++)
				PARTY_ZONE_LIST_CHAMPIONS[i] = Integer.parseInt(array[i]);

			Arrays.sort(PARTY_ZONE_LIST_CHAMPIONS);
			
			PARTY_ZONE_REWARD = new ArrayList<>();
			String[] partyReward = l2jmod.getProperty("PartyZoneRewardList", "57,100000").split(";");
			for (String reward : partyReward)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2)
				{
					_log.warning(StringUtil.concat("PartyZoneReward: invalid config property -> PartyZoneReward \"", reward, "\""));
				}
				else
				{
					try
					{
						PARTY_ZONE_REWARD.add(new int[]
						{
							Integer.parseInt(rewardSplit[0]),
							Integer.parseInt(rewardSplit[1])
						});
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty())
						{
							_log.warning(StringUtil.concat("PartyZoneReward: invalid config property -> PartyZoneReward \"", reward, "\""));
						}
					}
				}
			}
			
			// Hero / Vip Coins
			HERO_COIN_ID1 = l2jmod.getProperty("HeroCoin1", 57);
			HERO_DAYS_ID1 = l2jmod.getProperty("HeroCoinDays1", 57);
			HERO_COIN_ID2 = l2jmod.getProperty("HeroCoin2", 57);
			HERO_DAYS_ID2 = l2jmod.getProperty("HeroCoinDays2", 57);
			HERO_COIN_ID3 = l2jmod.getProperty("HeroCoin3", 57);
			HERO_DAYS_ID3 = l2jmod.getProperty("HeroCoinDays3", 57);
			
			VIP_COIN_ID1 = l2jmod.getProperty("VipCoin1", 57);
			VIP_DAYS_ID1 = l2jmod.getProperty("VipCoinDays1", 57);
			VIP_COIN_ID2 = l2jmod.getProperty("VipCoin2", 57);
			VIP_DAYS_ID2 = l2jmod.getProperty("VipCoinDays2", 57);
			VIP_COIN_ID3 = l2jmod.getProperty("VipCoin3", 57);
			VIP_DAYS_ID3 = l2jmod.getProperty("VipCoinDays3", 57);

			PARTY_EVENT_START = l2jmod.getProperty("PartyFarmEventEnable", true);
			PARTY_DROP_EVENT_INTERVAL = l2jmod.getProperty("PartyDropStartTime", "20:00").split(",");
			PARTY_DROP_EVENT_TIME = l2jmod.getProperty("PartyDropEventTime", 1);
			
			PART_ZONE_MONSTERS = l2jmod.getProperty("ListChampions");
			PART_ZONE_MONSTERS_ID = new ArrayList<>();
			for(String id : PART_ZONE_MONSTERS.split(","))
				PART_ZONE_MONSTERS_ID.add(Integer.parseInt(id));
			
			PARTY_ZONE_REWARDS = parseReward(l2jmod, "PartyZoneReward");
			
			String[] monsterLocs2 = l2jmod.getProperty("PartyMonsterLoc", "").split(";");
			String[] locSplit3 = null;
			
			CHAMPION_ID = l2jmod.getProperty("PartyMonsterId", 1);
			
			MONSTER_LOCS_COUNT = monsterLocs2.length;
			MONSTER_LOCS = new int[MONSTER_LOCS_COUNT][3];
			for (int i = 0; i < MONSTER_LOCS_COUNT; i++)
			{
				locSplit3 = monsterLocs2[i].split(",");
				for (int j = 0; j < 3; j++)
				{
					MONSTER_LOCS[i][j] = Integer.parseInt(locSplit3[j].trim());
				}
			}

			DONATE_TICKET = l2jmod.getProperty("DonateTicketID", 57);
			AUGM_PRICE = l2jmod.getProperty("StatusPrice", 57);
			
			MAX_HEALER_ON_PARTY = l2jmod.getProperty("MaxHealerOnParty", 1);
			MAX_COMBAT_HEALER_ON_PARTY = l2jmod.getProperty("MaxCombatHealerOnParty", 1);
			
			// L2Jmods config
			ExProperties l2jevent = load(L2JEVENT_FILE);
			
			CKM_ENABLED = l2jevent.getProperty("CKMEnabled", false);
			CKM_CYCLE_LENGTH = l2jevent.getProperty("CKMCycleLength", 86400000);
			CKM_PVP_NPC_TITLE = l2jevent.getProperty("CKMPvPNpcTitle", "%kills% PvPs in the last 24h");
			CKM_PVP_NPC_TITLE_COLOR = Integer.decode(StringUtil.concat("0x", l2jevent.getProperty("CKMPvPNpcTitleColor", "00CCFF")));
			CKM_PVP_NPC_NAME_COLOR = Integer.decode(StringUtil.concat("0x", l2jevent.getProperty("CKMPvPNpcNameColor", "FFFFFF")));
			CKM_PK_NPC_TITLE = l2jevent.getProperty("CKMPKNpcTitle", "%kills% PKs in the last 24h");
			CKM_PK_NPC_TITLE_COLOR = Integer.decode(StringUtil.concat("0x", l2jevent.getProperty("CKMPKNpcTitleColor", "00CCFF")));
			CKM_PK_NPC_NAME_COLOR = Integer.decode(StringUtil.concat("0x", l2jevent.getProperty("CKMPKNpcNameColor", "FFFFFF")));

			PCB_ENABLE = l2jevent.getProperty("PcBangPointEnable", false);
			PCB_MIN_LEVEL = l2jevent.getProperty("PcBangPointMinLevel", 20);
			PCB_POINT_MIN = l2jevent.getProperty("PcBangPointMinCount", 20);
			PCB_POINT_MAX = l2jevent.getProperty("PcBangPointMaxCount", 1000000);
			 
			if (PCB_POINT_MAX < 1)
			{
	               PCB_POINT_MAX = Integer.MAX_VALUE;
			}
			
			PCB_CHANCE_DUAL_POINT = l2jevent.getProperty("PcBangPointDualChance", 20);
			PCB_INTERVAL = l2jevent.getProperty("PcBangPointTimeStamp", 900);
			
			AUTOVOTEREWARD_ENABLED = l2jevent.getProperty("VoteRewardSystem", false);
			VOTE_DUALBOXES_ALLOWED = l2jevent.getProperty("DualboxesAllowed", 1);
			VOTES_FOR_REWARD = l2jevent.getProperty("VotesRequiredForReward", 00);
			VOTES_SYSYEM_INITIAL_DELAY = l2jevent.getProperty("VotesSystemInitialDelay", 60000);
			VOTES_SYSYEM_STEP_DELAY = l2jevent.getProperty("VotesSystemStepDelay", 1800000);
			VOTES_SITE_HOPZONE_URL = l2jevent.getProperty("VotesSiteHopZoneUrl", "");
			VOTES_SITE_TOPZONE_URL = l2jevent.getProperty("VotesSiteTopZoneUrl", "");
			VOTES_SITE_L2NETWORK_URL = l2jevent.getProperty("VotesSiteL2NetworkUrl", "");
			SERVER_WEB_SITE = l2jevent.getProperty("ServerWebSite", "");
			VOTES_REWARDS_LIST = parseItemsList(l2jevent.getProperty("VotesRewards", "57,100000000;"));
			
			// Balance config
			ExProperties balance = load(BALANCE_FILE);

            RUN_SPD_BOOST = balance.getProperty("RunSpeedBoost", 0);
            MAX_RUN_SPEED = balance.getProperty("MaxRunSpeed", 250);
            MAX_PCRIT_RATE = balance.getProperty("MaxPCritRate", 500);
            MAX_MCRIT_RATE = balance.getProperty("MaxMCritRate", 200);
            MAX_PATK_SPEED = balance.getProperty("MaxPAtkSpeed", 1500);
            MAX_MATK_SPEED = balance.getProperty("MaxMAtkSpeed", 1999);
            MAX_EVASION = balance.getProperty("MaxEvasion", 250);
            MAX_ACCURACY = balance.getProperty("MaxAccuracy", 300);
            MAGIC_CRITICAL_POWER = balance.getProperty("MagicCriticalPower", 4);
            
        	ENABLE_MODIFY_SKILL_DURATION = balance.getProperty("EnableModifySkillDuration", false);
        	if (ENABLE_MODIFY_SKILL_DURATION)
        	{
        		SKILL_DURATION_LIST = new HashMap<>();
        		array = balance.getProperty("SkillDurationList", "").split(";");
        		for (String skill : array)
        		{
        			String[] skillSplit = skill.split(",");
        			if (skillSplit.length != 2)
        			{
        				System.out.println("SkillDurationList: invalid config property -> SkillDurationList \"" + skill + "\"");
        			}
        			else
        			{
        				try
        				{
        					SKILL_DURATION_LIST.put(Integer.valueOf(skillSplit[0]), Integer.valueOf(skillSplit[1]));
        				}
        				catch (NumberFormatException nfe)
        				{
        					if (!skill.equals(""))
        					{
        						System.out.println("SkillDurationList: invalid config property -> SkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
        					}
        				}
        			}
        		}
        	}
        	
			MASTERY_PENALTY = balance.getProperty("MasteryPenality", false);
			LEVEL_TO_GET_PENALITY = balance.getProperty("LevelToGetPenalty", 20);
			ARMOR_PENALTY_SKILL = balance.getProperty("ArmorPenaltySkill", 8000);
		
			MASTERY_WEAPON_PENALTY = balance.getProperty("MasteryWeaponPenality", false);
			LEVEL_TO_GET_WEAPON_PENALITY = balance.getProperty("LevelToGetWeaponPenalty", 20);
			WEAPON_PENALTY_SKILL = balance.getProperty("WeaponPenaltySkill", 8001);
			
			ANTI_SS_BUG_1 = balance.getProperty("Delay", 2700);
			ANTI_SS_BUG_2 = balance.getProperty("DelayNextAttack", 470000);		
			
			ALT_DAGGER_DMG_VS_HEAVY = Float.parseFloat(balance.getProperty("DaggerVSHeavy", "2.50"));
			ALT_DAGGER_DMG_VS_ROBE = Float.parseFloat(balance.getProperty("DaggerVSRobe", "1.80"));
			ALT_DAGGER_DMG_VS_LIGHT = Float.parseFloat(balance.getProperty("DaggerVSLight", "2.00"));
			
			ALT_DUAL_DMG_VS_HEAVY = Float.parseFloat(balance.getProperty("DualVSHeavy", "2.50"));
			ALT_DUAL_DMG_VS_ROBE = Float.parseFloat(balance.getProperty("DualVSRobe", "1.80"));
			ALT_DUAL_DMG_VS_LIGHT = Float.parseFloat(balance.getProperty("DualVSLight", "2.00"));
			
 			ENABLE_CLASS_DAMAGES = balance.getProperty("EnableClassDamagesSettings", true);
 			ENABLE_CLASS_DAMAGES_LOGGER = balance.getProperty("EnableClassDamagesLogger", true);
 			
 			ENABLE_OLY_CLASS_DAMAGES = balance.getProperty("EnableClassDamagesSettingsInOly", true);
 			ENABLE_OLY_CLASS_DAMAGES_LOGGER = balance.getProperty("EnableClassDamagesLoggerInOly", true);
 			
			PROTECTED_SKILLS = balance.getProperty("NotCanceledSkills");
			
			NOT_CANCELED_SKILLS = new ArrayList<>();
			for (String id : PROTECTED_SKILLS.split(","))
				NOT_CANCELED_SKILLS.add(Integer.parseInt(id));
 			
			CANCEL_BACK_TIME = balance.getProperty("CancelTimer", 5);
			
			// Annoucements config
			ExProperties annouce = load(ANNOUCEMENTS_FILE);
 			
			ANNOUNCE_PK_PVP = annouce.getProperty("AnnouncePkPvP", false);
			ANNOUNCE_PK_PVP_NORMAL_MESSAGE = annouce.getProperty("AnnouncePkPvPNormalMessage", true);
			ANNOUNCE_PK_MSG = annouce.getProperty("AnnouncePkMsg", "Player $killer has slaughtered $target .");
			ANNOUNCE_PVP_MSG = annouce.getProperty("AnnouncePvpMsg", "Player $killer has defeated $target .");
			
			ANNOUNCE_RAID_BOSS_ALIVE = annouce.getProperty("AnnounceRaidAlive", false);
			ANNOUNCE_RAID_BOSS_DEATH = annouce.getProperty("AnnounceRaidDeath", false);
			
			ANNOUNCE_CASTLE_LORDS = annouce.getProperty("AnnounceLordOnEnter", false);
			ANNOUNCE_AIO_LOGIN = annouce.getProperty("AnnounceAioOnEnter", false);
			ANNOUNCE_HERO_LOGIN = annouce.getProperty("AnnounceHeroOnEnter", false);
			
			ENABLE_ENCHANT_ANNOUNCE = annouce.getProperty("EnableEnchantAnnounce", false);
			ENCHANT_ANNOUNCE_LEVEL = annouce.getProperty("EnchantAnnounceLevel", 16);
			
			ALLOW_QUAKE_SYSTEM = annouce.getProperty("AllowQuakeSystem", false);
			
			String killing_spree_values = annouce.getProperty("QuakeSystemValues", "");
			String killing_spree_values_splitted_1[] = killing_spree_values.split(";");
			for (String s : killing_spree_values_splitted_1)
			{
				String killing_spree_values_splitted_2[] = s.split(",");
				QUAKE_VALUES.put(Integer.parseInt(killing_spree_values_splitted_2[0]), killing_spree_values_splitted_2[1]);
			}
			
			ALLOW_QUAKE_SOUND = annouce.getProperty("AllowQuakeSound", false);
			ALLOW_QUAKE_SOUND_SCREEN = annouce.getProperty("AllowQuakeSoundScreen", false);
			
			QUAKE_SOUND_TIME = annouce.getProperty("QuakeSoundTimer", 5000);
			
			WAR_LEGEND_AURA = annouce.getProperty("QuakeWarLegend", false);
			KILLS_TO_GET_WAR_LEGEND_AURA = annouce.getProperty("KillsToGetWarLegend", 30);

			DEFAULT_GLOBAL_CHAT = annouce.getProperty("GlobalChat", "ON");
			DEFAULT_TRADE_CHAT = annouce.getProperty("TradeChat", "ON");
			
            CHAT_SHOUT_NEED_PVPS = annouce.getProperty("ShoutChatWithPvP", false);
            PVPS_TO_USE_CHAT_SHOUT = annouce.getProperty("ShoutPvPAmount", 0);
            CHAT_TRADE_NEED_PVPS = annouce.getProperty("TradeChatWithPvP", false);
            PVPS_TO_USE_CHAT_TRADE = annouce.getProperty("TradePvPAmount", 0);
            CHAT_HERO_NEED_PVPS = annouce.getProperty("HeroChatWithPvP", false);
            PVPS_TO_USE_CHAT_HERO = annouce.getProperty("HeroPvPAmount", 0);
            
            ALT_OLY_END_ANNOUNCE = annouce.getProperty("AltOlyEndAnnounce", false);

    		TALK_CHAT_ALL_CONFIG = annouce.getProperty("ChatAllProtection", false);
    		TALK_CHAT_ALL_TIME = annouce.getProperty("TalkChatAllTime", 15);
    		
			USE_SAY_FILTER = annouce.getProperty("AllowSayFilter", false);
			CHAT_FILTER_CHARS = annouce.getProperty("SayFilterWrite", "********");
			CHAT_FILTER_PUNISHMENT = annouce.getProperty("SayFilterPunishment", "off");
			CHAT_FILTER_PUNISHMENT_PARAM1 = annouce.getProperty("SayFilterPunishmentParam1", 1);
			CHAT_FILTER_PUNISHMENT_PARAM2 = annouce.getProperty("SayFilterPunishmentParam2", 1000);
			
			if (USE_SAY_FILTER)
			{
	             try
	             {
	                 @SuppressWarnings("resource")
				 	 LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(new File(SAY_FILTER_FILE))));
	                 String line = null;
	                 while ((line = lnr.readLine()) != null)
	                 {
	                     if (line.trim().length() == 0 || line.startsWith("#"))
	                     {
	                         continue;
	                     }
	                     FILTER_LIST.add(line.trim());
	                 }
	                 _log.info("Chat Filter: Loaded " + FILTER_LIST.size() + " words");
	            }
	            catch (Exception e)
	            {
	                 e.printStackTrace();
	                 throw new Error("Failed to Load "+SAY_FILTER_FILE+" File.");
				}
			}
			
			// Startup config
			ExProperties start = load(STARTUP_FILE);

			STARTUP_SYSTEM_ENABLED = start.getProperty("EnableStartupSystem", false);

			String[] propertySplit = start.getProperty("SetRobe", "4223,1").split(";");
			SET_ROBE_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_ROBE_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty()) 
						{
							_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}
			propertySplit = start.getProperty("SetLight", "4223,1").split(";");
			SET_LIGHT_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_LIGHT_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty())
						{
							_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}
			propertySplit = start.getProperty("SetHeavy", "4223,1").split(";");
			SET_HEAVY_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_HEAVY_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty()) 
						{
							_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}

			BYBASS_ROBE_ITEMS = start.getProperty("htm_robe", "startup");
			BYBASS_LIGHT_ITEMS = start.getProperty("htm_light", "startup");
			BYBASS_HEAVY_ITEMS = start.getProperty("htm_heavy", "startup");

			BYBASS_WP_01_ITEM = start.getProperty("BpWeapon_01", "startup");
			WP_01_ID = start.getProperty("Wp_01_ID", 5);

			BYBASS_WP_02_ITEM = start.getProperty("BpWeapon_02", "startup");
			WP_02_ID = start.getProperty("Wp_02_ID", 5);

			BYBASS_WP_03_ITEM = start.getProperty("BpWeapon_03", "startup");
			WP_03_ID = start.getProperty("Wp_03_ID", 5);

			BYBASS_WP_04_ITEM = start.getProperty("BpWeapon_04", "startup");
			WP_04_ID = start.getProperty("Wp_04_ID", 5);

			BYBASS_WP_05_ITEM = start.getProperty("BpWeapon_05", "startup");
			WP_05_ID = start.getProperty("Wp_05_ID", 5);

			BYBASS_WP_06_ITEM = start.getProperty("BpWeapon_06", "startup");
			WP_06_ID = start.getProperty("Wp_06_ID", 5);

			BYBASS_WP_07_ITEM = start.getProperty("BpWeapon_07", "startup");
			WP_07_ID = start.getProperty("Wp_07_ID", 5);

			BYBASS_WP_08_ITEM = start.getProperty("BpWeapon_08", "startup");
			WP_08_ID = start.getProperty("Wp_09_ID", 5);

			BYBASS_WP_09_ITEM = start.getProperty("BpWeapon_09", "startup");
			WP_09_ID = start.getProperty("Wp_09_ID", 5);

			BYBASS_WP_10_ITEM = start.getProperty("BpWeapon_10", "startup");
			WP_10_ID = start.getProperty("Wp_10_ID", 5);

			BYBASS_WP_11_ITEM = start.getProperty("BpWeapon_11", "startup");
			WP_11_ID = start.getProperty("Wp_11_ID", 5);

			BYBASS_WP_12_ITEM = start.getProperty("BpWeapon_12", "startup");
			WP_12_ID = start.getProperty("Wp_12_ID", 5);

			BYBASS_WP_13_ITEM = start.getProperty("BpWeapon_13", "startup");
			WP_13_ID = start.getProperty("Wp_13_ID", 5);

			BYBASS_WP_14_ITEM = start.getProperty("BpWeapon_14", "startup");
			WP_14_ID = start.getProperty("Wp_14_ID", 5);

			BYBASS_WP_15_ITEM = start.getProperty("BpWeapon_15", "startup");
			WP_15_ID = start.getProperty("Wp_15_ID", 5);

			BYBASS_WP_16_ITEM = start.getProperty("BpWeapon_16", "startup");
			WP_16_ID = start.getProperty("Wp_16_ID", 5);

			BYBASS_WP_17_ITEM = start.getProperty("BpWeapon_17", "startup");
			WP_17_ID = start.getProperty("Wp_17_ID", 5);

			BYBASS_WP_18_ITEM = start.getProperty("BpWeapon_18", "startup");
			WP_18_ID = start.getProperty("Wp_18_ID", 5);

			BYBASS_WP_19_ITEM = start.getProperty("BpWeapon_19", "startup");
			WP_19_ID = start.getProperty("Wp_19_ID", 5);

			BYBASS_WP_20_ITEM = start.getProperty("BpWeapon_20", "startup");
			WP_20_ID = start.getProperty("Wp_20_ID", 5);

			BYBASS_WP_21_ITEM = start.getProperty("BpWeapon_21", "startup");
			WP_21_ID = start.getProperty("Wp_21_ID", 5);

			BYBASS_WP_22_ITEM = start.getProperty("BpWeapon_22", "startup");
			WP_22_ID = start.getProperty("Wp_22_ID", 5);

			BYBASS_WP_23_ITEM = start.getProperty("BpWeapon_23", "startup");
			WP_23_ID = start.getProperty("Wp_23_ID", 5);

			BYBASS_WP_24_ITEM = start.getProperty("BpWeapon_24", "startup");
			WP_24_ID = start.getProperty("Wp_24_ID", 5);

			BYBASS_WP_25_ITEM = start.getProperty("BpWeapon_25", "startup");
			WP_25_ID = start.getProperty("Wp_25_ID", 5);

			BYBASS_WP_26_ITEM = start.getProperty("BpWeapon_26", "startup");
			WP_26_ID = start.getProperty("Wp_26_ID", 5);

			BYBASS_WP_27_ITEM = start.getProperty("BpWeapon_27", "startup");
			WP_27_ID = start.getProperty("Wp_27_ID", 5);

			BYBASS_WP_28_ITEM = start.getProperty("BpWeapon_28", "startup");
			WP_28_ID = start.getProperty("Wp_28_ID", 5);

			BYBASS_WP_29_ITEM = start.getProperty("BpWeapon_29", "startup");
			WP_29_ID = start.getProperty("Wp_29_ID", 5);

			BYBASS_WP_30_ITEM = start.getProperty("BpWeapon_30", "startup");
			WP_30_ID = start.getProperty("Wp_30_ID", 5);

			BYBASS_WP_31_ITEM = start.getProperty("BpWeapon_31", "startup");
			WP_31_ID = start.getProperty("Wp_31_ID", 5);

			WP_ARROW = start.getProperty("Arrow_ID", 5);
			WP_SHIELD = start.getProperty("Shield_ID", 5);

			// Startup config
			ExProperties boomevent = load(BOOM_FILE);

			EVENT_KEY = boomevent.getProperty("EventKeyID", 9595);
			EVENT_KEY_AMOUNT_1 = boomevent.getProperty("EventKeyAmount1", 1);
			EVENT_KEY_AMOUNT_2 = boomevent.getProperty("EventKeyAmount2", 2);
			EVENT_KEY_AMOUNT_3 = boomevent.getProperty("EventKeyAmount3", 3);
			EVENT_KEY_AMOUNT_4 = boomevent.getProperty("EventKeyAmount4", 4);
			EVENT_KEY_AMOUNT_5 = boomevent.getProperty("EventKeyAmount5", 5);

			BOOM_REWARD_ITEM_ENABLED = boomevent.getProperty("BoomRewardEnabled", false);

			if (BOOM_REWARD_ITEM_ENABLED)
			{
				String[] propertySplitboom = boomevent.getProperty("Reward_Lvl_1", "57,0").split(";");
				LVL_1_REWARD.clear();
				for (String reward : propertySplitboom)
				{
					String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2) 
					{
						_log.warning("StartingCustomItemsMage[Config.load()]: invalid config property -> StartingCustomItemsMage \"" + reward + "\"");
					} 
					else 
					{
						try
						{
							LVL_1_REWARD.add(new int[]{Integer.parseInt(rewardSplit[0]),Integer.parseInt(rewardSplit[1])});
						}
						catch (NumberFormatException nfe)
						{
							if (!reward.isEmpty())
							{
								_log.warning("StartingCustomItemsMage[Config.load()]: invalid config property -> StartingCustomItemsMage \"" + reward + "\"");
							}
						}
					}
				}
				propertySplitboom = boomevent.getProperty("Reward_Lvl_2", "57,0").split(";");
				LVL_2_REWARD.clear();
				for (String reward : propertySplitboom)
				{
					String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2) 
					{
						_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
					} 
					else 
					{
						try
						{
							LVL_2_REWARD.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
						}
						catch (NumberFormatException nfe)
						{
							if (!reward.isEmpty()) 
							{
								_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
							}
						}
					}
				}
				propertySplitboom = boomevent.getProperty("Reward_Lvl_3", "57,0").split(";");
				LVL_3_REWARD.clear();
				for (String reward : propertySplitboom)
				{
					String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2) 
					{
						_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
					} 
					else 
					{
						try
						{
							LVL_3_REWARD.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
						}
						catch (NumberFormatException nfe)
						{
							if (!reward.isEmpty()) 
							{
								_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
							}
						}
					}
				}
				propertySplitboom = boomevent.getProperty("Reward_Lvl_4", "57,0").split(";");
				LVL_4_REWARD.clear();
				for (String reward : propertySplitboom)
				{
					String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2) 
					{
						_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
					}
					else 
					{
						try
						{
							LVL_4_REWARD.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
						}
						catch (NumberFormatException nfe)
						{
							if (!reward.isEmpty())
							{
								_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
							}
						}
					}
				}
				propertySplitboom = boomevent.getProperty("Reward_Lvl_5", "57,0").split(";");
				LVL_5_REWARD.clear();
				for (String reward : propertySplitboom)
				{
					String[] rewardSplit = reward.split(",");
					if (rewardSplit.length != 2)
					{
						_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
					}
					else 
					{
						try
						{
							LVL_5_REWARD.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
						}
						catch (NumberFormatException nfe)
						{
							if (!reward.isEmpty())
							{
								_log.warning("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
							}
						}
					}
				}
			}
		}
		else if (Server.serverMode == Server.MODE_LOGINSERVER)
		{
			_log.info("Loading loginserver configuration files.");
			
			ExProperties server = load(LOGIN_CONFIGURATION_FILE);
			GAME_SERVER_LOGIN_HOST = server.getProperty("LoginHostname", "*");
			GAME_SERVER_LOGIN_PORT = server.getProperty("LoginPort", 9013);
			
			LOGIN_BIND_ADDRESS = server.getProperty("LoginserverHostname", "*");
			PORT_LOGIN = server.getProperty("LoginserverPort", 2106);
			
			DEBUG = server.getProperty("Debug", false);
			DEVELOPER = server.getProperty("Developer", false);
			PACKET_HANDLER_DEBUG = server.getProperty("PacketHandlerDebug", false);
			ACCEPT_NEW_GAMESERVER = server.getProperty("AcceptNewGameServer", true);
			REQUEST_ID = server.getProperty("RequestServerID", 0);
			ACCEPT_ALTERNATE_ID = server.getProperty("AcceptAlternateID", true);
			
			LOGIN_TRY_BEFORE_BAN = server.getProperty("LoginTryBeforeBan", 10);
			LOGIN_BLOCK_AFTER_BAN = server.getProperty("LoginBlockAfterBan", 600);
			
			LOG_LOGIN_CONTROLLER = server.getProperty("LogLoginController", false);
			
			INTERNAL_HOSTNAME = server.getProperty("InternalHostname", "localhost");
			EXTERNAL_HOSTNAME = server.getProperty("ExternalHostname", "localhost");
			
			DATABASE_URL = server.getProperty("URL", "jdbc:mysql://localhost/acis");
			DATABASE_LOGIN = server.getProperty("Login", "root");
			DATABASE_PASSWORD = server.getProperty("Password", "");
			DATABASE_MAX_CONNECTIONS = server.getProperty("MaximumDbConnections", 10);
			DATABASE_MAX_IDLE_TIME = server.getProperty("MaximumDbIdleTime", 0);
			
			SHOW_LICENCE = server.getProperty("ShowLicence", true);
			IP_UPDATE_TIME = server.getProperty("IpUpdateTime", 15);
			FORCE_GGAUTH = server.getProperty("ForceGGAuth", false);
			
			AUTO_CREATE_ACCOUNTS = server.getProperty("AutoCreateAccounts", true);
			
			FLOOD_PROTECTION = server.getProperty("EnableFloodProtection", true);
			FAST_CONNECTION_LIMIT = server.getProperty("FastConnectionLimit", 15);
			NORMAL_CONNECTION_TIME = server.getProperty("NormalConnectionTime", 700);
			FAST_CONNECTION_TIME = server.getProperty("FastConnectionTime", 350);
			MAX_CONNECTION_PER_IP = server.getProperty("MaxConnectionPerIP", 50);
		}
		else
			_log.severe("Couldn't load configs: server mode wasn't set.");
	}
	
	// It has no instances
	private Config()
	{
	}
	
	public static void saveHexid(int serverId, String string)
	{
		Config.saveHexid(serverId, string, HEXID_FILE);
	}
	
	public static void saveHexid(int serverId, String hexId, String fileName)
	{
		try
		{
			Properties hexSetting = new Properties();
			File file = new File(fileName);
			file.createNewFile();
			
			OutputStream out = new FileOutputStream(file);
			hexSetting.setProperty("ServerID", String.valueOf(serverId));
			hexSetting.setProperty("HexID", hexId);
			hexSetting.store(out, "the hexID to auth into login");
			out.close();
		}
		catch (Exception e)
		{
			_log.warning("Failed to save hex id to " + fileName + " file.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads single flood protector configuration.
	 * @param properties L2Properties file reader
	 * @param config flood protector configuration instance
	 * @param configString flood protector configuration string that determines for which flood protector configuration should be read
	 * @param defaultInterval default flood protector interval
	 */
	private static void loadFloodProtectorConfig(final Properties properties, final FloodProtectorConfig config, final String configString, final String defaultInterval)
	{
		config.FLOOD_PROTECTION_INTERVAL = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "Interval"), defaultInterval));
		config.LOG_FLOODING = Boolean.parseBoolean(properties.getProperty(StringUtil.concat("FloodProtector", configString, "LogFlooding"), "False"));
		config.PUNISHMENT_LIMIT = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentLimit"), "0"));
		config.PUNISHMENT_TYPE = properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentType"), "none");
		config.PUNISHMENT_TIME = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentTime"), "0"));
	}
	
	public static class ClassMasterSettings
	{
		private final Map<Integer, Boolean> _allowedClassChange;
		private final Map<Integer, List<ItemHolder>> _claimItems;
		private final Map<Integer, List<ItemHolder>> _rewardItems;
		
		public ClassMasterSettings(String configLine)
		{
			_allowedClassChange = new HashMap<>(3);
			_claimItems = new HashMap<>(3);
			_rewardItems = new HashMap<>(3);
			
			if (configLine != null)
				parseConfigLine(configLine.trim());
		}
		
		private void parseConfigLine(String configLine)
		{
			StringTokenizer st = new StringTokenizer(configLine, ";");
			
			while (st.hasMoreTokens())
			{
				// get allowed class change
				int job = Integer.parseInt(st.nextToken());
				
				_allowedClassChange.put(job, true);
				
				List<ItemHolder> items = new ArrayList<>();
				
				// Parse items needed for class change.
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						items.add(new ItemHolder(Integer.parseInt(st3.nextToken()), Integer.parseInt(st3.nextToken())));
					}
				}
				
				// Feed the map, and clean the list.
				_claimItems.put(job, items);
				items = new ArrayList<>();
				
				// Parse gifts after class change.
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						items.add(new ItemHolder(Integer.parseInt(st3.nextToken()), Integer.parseInt(st3.nextToken())));
					}
				}
				
				_rewardItems.put(job, items);
			}
		}
		
		public boolean isAllowed(int job)
		{
			if (_allowedClassChange == null)
				return false;
			
			if (_allowedClassChange.containsKey(job))
				return _allowedClassChange.get(job);
			
			return false;
		}
		
		public List<ItemHolder> getRewardItems(int job)
		{
			return _rewardItems.get(job);
		}
		
		public List<ItemHolder> getRequiredItems(int job)
		{
			return _claimItems.get(job);
		}
	}
	
	/**
	 * itemId1,itemNumber1;itemId2,itemNumber2... to the int[n][2] = [itemId1][itemNumber1],[itemId2][itemNumber2]...
	 * @param line
	 * @return an array consisting of parsed items.
	 */
	private static int[][] parseItemsList(String line)
	{
		final String[] propertySplit = line.split(";");
		if (propertySplit.length == 0)
			return null;
		
		int i = 0;
		String[] valueSplit;
		final int[][] result = new int[propertySplit.length][];
		for (String value : propertySplit)
		{
			valueSplit = value.split(",");
			if (valueSplit.length != 2)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid entry -> \"", valueSplit[0], "\", should be itemId,itemNumber"));
				return null;
			}
			
			result[i] = new int[2];
			try
			{
				result[i][0] = Integer.parseInt(valueSplit[0]);
			}
			catch (NumberFormatException e)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid itemId -> \"", valueSplit[0], "\""));
				return null;
			}
			
			try
			{
				result[i][1] = Integer.parseInt(valueSplit[1]);
			}
			catch (NumberFormatException e)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid item number -> \"", valueSplit[1], "\""));
				return null;
			}
			i++;
		}
		return result;
	}
	
	private static List<RewardHolder> parseReward(ExProperties propertie, String configName)
	{
		List<RewardHolder> auxReturn = new ArrayList<>();
		
		String aux = propertie.getProperty(configName).trim();
		for (String randomReward : aux.split(";"))
		{
			final String[] infos = randomReward.split(",");
			if (infos.length > 2)
				auxReturn.add(new RewardHolder(Integer.valueOf(infos[0]), Integer.valueOf(infos[1]), Integer.valueOf(infos[2])));
			else
				auxReturn.add(new RewardHolder(Integer.valueOf(infos[0]), Integer.valueOf(infos[1])));
		}
		return auxReturn;
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