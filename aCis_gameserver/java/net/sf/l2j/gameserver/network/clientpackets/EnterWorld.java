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
package net.sf.l2j.gameserver.network.clientpackets;

import hwid.Hwid;
import hwid.HwidConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.communitybbs.Manager.MailBBSManager;
import net.sf.l2j.gameserver.datatables.AdminCommandAccessRights;
import net.sf.l2j.gameserver.datatables.AnnouncementTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.custom.HwidManager;
import net.sf.l2j.gameserver.instancemanager.custom.IPManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Clan.SubPledge;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2ClassMasterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PunishLevel;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Couple;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.events.ColorSystem;
import net.sf.l2j.gameserver.model.entity.events.StartupSystem;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEvent;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMEvent;
import net.sf.l2j.gameserver.model.entity.events.lastman.LMEvent;
import net.sf.l2j.gameserver.model.entity.events.teamvsteam.TvTEvent;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.L2GameClient.GameClientState;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.Die;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ExMailArrived;
import net.sf.l2j.gameserver.network.serverpackets.ExStorageMaxCount;
import net.sf.l2j.gameserver.network.serverpackets.FriendList;
import net.sf.l2j.gameserver.network.serverpackets.HennaInfo;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeSkillList;
import net.sf.l2j.gameserver.network.serverpackets.PledgeStatusChanged;
import net.sf.l2j.gameserver.network.serverpackets.QuestList;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.network.serverpackets.SkillCoolTime;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.gameserver.util.HwidLog;
import net.sf.l2j.gameserver.util.Util;

public class EnterWorld extends L2GameClientPacket
{
	long _daysleft;
	SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
	
	@Override
	protected void readImpl()
	{
		// this is just a trigger packet. it has no content
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			_log.warning("EnterWorld failed! activeChar is null...");
			getClient().closeNow();
			return;
		}
		
		getClient().setState(GameClientState.IN_GAME);
		
        // Multibox Protection by IP
        if (Config.MULTIBOX_PROTECTION_ENABLED)
        	IPManager.getInstance().validBox(activeChar, Config.MULTIBOX_PROTECTION_CLIENTS_PER_PC, L2World.getInstance().getAllPlayers().values(), true);
		
        // Multibox Protection by Hwid
        if (Config.HWID_MULTIBOX_PROTECTION_ENABLED)
            HwidManager.getInstance().validBox(activeChar, Config.HWID_MULTIBOX_PROTECTION_CLIENTS_PER_PC, L2World.getInstance().getAllPlayers().values(), true);
		
        if (L2World.getInstance().findObject(activeChar.getObjectId()) != null)
        {
        	_log.warning("User already exist in OID map! User " + activeChar.getName() + " is character clone.");
        }
		
		if (activeChar.isGM())
		{
		    activeChar.getAppearance().setNameColor(Config.MASTERACCESS_NAME_COLOR);
		    activeChar.getAppearance().setTitleColor(Config.MASTERACCESS_TITLE_COLOR);
		    
			if (Config.GM_STARTUP_INVULNERABLE && AdminCommandAccessRights.getInstance().hasAccess("admin_invul", activeChar.getAccessLevel()))
				activeChar.setIsInvul(true);
			
			if (!Util.contains(Config.GM_NAMES, activeChar.getName()))
				activeChar.setPunishLevel(PunishLevel.ACC, 0);

			if (Config.GM_STARTUP_INVISIBLE && AdminCommandAccessRights.getInstance().hasAccess("admin_hide", activeChar.getAccessLevel()))
				activeChar.getAppearance().setInvisible();
			
			if (Config.GM_STARTUP_SILENCE && AdminCommandAccessRights.getInstance().hasAccess("admin_silence", activeChar.getAccessLevel()))
				activeChar.setInRefusalMode(true);
			
			if (Config.GM_STARTUP_AUTO_LIST && AdminCommandAccessRights.getInstance().hasAccess("admin_gmliston", activeChar.getAccessLevel()))
				GmListTable.getInstance().addGm(activeChar, false);
			else
				GmListTable.getInstance().addGm(activeChar, true);
		}
		
		// Set dead status if applies
		if (activeChar.getCurrentHp() < 0.5)
			activeChar.setIsDead(true);
		
		final L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			activeChar.sendPacket(new PledgeSkillList(clan));
			notifyClanMembers(activeChar);
			notifySponsorOrApprentice(activeChar);
			
			// Add message at connexion if clanHall not paid.
			final ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(clan);
			if (clanHall != null)
			{
				if (!clanHall.getPaid())
					activeChar.sendPacket(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW);
			}
			
			for (Siege siege : SiegeManager.getSieges())
			{
				if (!siege.isInProgress())
					continue;
				
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					if (siege.checkIsAttacker(clan))
					{
						activeChar.setSiegeState((byte) 1);
						activeChar.setSiegeSide(castle.getCastleId());
					}
					else if (siege.checkIsDefender(clan))
					{
						activeChar.setSiegeState((byte) 2);
						activeChar.setSiegeSide(castle.getCastleId());
					}
				}
			}
			
			activeChar.sendPacket(new PledgeShowMemberListAll(clan, 0));
			
			for (SubPledge sp : clan.getAllSubPledges())
				activeChar.sendPacket(new PledgeShowMemberListAll(clan, sp.getId()));
			
			activeChar.sendPacket(new UserInfo(activeChar));
			activeChar.sendPacket(new PledgeStatusChanged(clan));
			clan.broadcastClanStatus();
		}
		
		// Updating Seal of Strife Buff/Debuff
		if (SevenSigns.getInstance().isSealValidationPeriod() && SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) != SevenSigns.CABAL_NULL)
		{
			int cabal = SevenSigns.getInstance().getPlayerCabal(activeChar.getObjectId());
			if (cabal != SevenSigns.CABAL_NULL)
			{
				if (cabal == SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE))
					activeChar.addSkill(FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
				else
					activeChar.addSkill(FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
			}
		}
		else
		{
			activeChar.removeSkill(FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
			activeChar.removeSkill(FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
		}
		
		if (Config.PLAYER_SPAWN_PROTECTION > 0)
			activeChar.setProtection(true);
		
		activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
		
		// engage and notify Partner
		if (Config.ALLOW_WEDDING)
			engage(activeChar);
		
		// Welcome message
		if (Config.ALLOW_WELCOME_TO_LINEAGE)
			activeChar.sendPacket(SystemMessageId.WELCOME_TO_LINEAGE);

		// Check player skills
		if (Config.CHECK_SKILLS_ON_ENTER)
		    if (!activeChar.isAio())
		    		activeChar.checkAllowedSkills();

		// Seven signs period messages
		SevenSigns.getInstance().sendCurrentPeriodMsg(activeChar);
		AnnouncementTable.getInstance().showAnnouncements(activeChar, false);
		
		if (Config.PCB_ENABLE)
            activeChar.showPcBangWindow();

		// Events On Enter
		CTFEvent.onLogin(activeChar);
		DMEvent.onLogin(activeChar);
		LMEvent.onLogin(activeChar);
		TvTEvent.onLogin(activeChar);
		
		if (Config.ALT_OLY_END_ANNOUNCE && activeChar.isNoble() || activeChar.isGM())
			Olympiad.getInstance().olympiadEnd(activeChar);
		
		if (activeChar.isVip())
			onEnterVip(activeChar);

		if (Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
			activeChar.getAppearance().setNameColor(Config.VIP_NCOLOR);

		if (Config.ALLOW_VIP_TCOLOR && activeChar.isVip())
			activeChar.getAppearance().setTitleColor(Config.VIP_TCOLOR);

		if (activeChar.isAio())
			onEnterAio(activeChar);
		
		if (activeChar.isAio())
		{
			activeChar.removeSkills();
			activeChar.rewardAioSkills();
		}
		
		if (Config.ALLOW_AIO_NCOLOR && activeChar.isAio())
			activeChar.getAppearance().setNameColor(Config.AIO_NCOLOR);
		
		if( Config.ALLOW_AIO_TCOLOR && activeChar.isAio())
			activeChar.getAppearance().setTitleColor(Config.AIO_TCOLOR);
			
		// if player is DE, check for shadow sense skill at night
		if (activeChar.getRace() == Race.DarkElf && activeChar.getSkillLevel(294) == 1)
			activeChar.sendPacket(SystemMessage.getSystemMessage((GameTimeController.getInstance().isNight()) ? SystemMessageId.NIGHT_S1_EFFECT_APPLIES : SystemMessageId.DAY_S1_EFFECT_DISAPPEARS).addSkillName(294));

		activeChar.getMacroses().sendUpdate();
		activeChar.sendPacket(new UserInfo(activeChar));
		activeChar.sendPacket(new HennaInfo(activeChar));
		activeChar.sendPacket(new FriendList(activeChar));
		// activeChar.queryGameGuard();
		activeChar.sendPacket(new ItemList(activeChar, false));
		activeChar.sendPacket(new ShortCutInit(activeChar));
		activeChar.sendPacket(new ExStorageMaxCount(activeChar));
		activeChar.updateEffectIcons();
		activeChar.broadcastUserInfo();
		// Only Test
		// activeChar.broadcastTitleInfo();
		activeChar.sendPacket(new EtcStatusUpdate(activeChar));
		activeChar.sendSkillList();
		
		// Pvp/pk Color
		ColorSystem pvpcolor = new ColorSystem();
		pvpcolor.updateNameColor(activeChar);
		pvpcolor.updateTitleColor(activeChar);
		
		// Reload inventory to give SA skill
		activeChar.getInventory().reloadEquippedItems();
		
		// Restores custom status
		activeChar.restoreCustomStatus();
		
		//Hwid Check
		Hwid.enterlog(activeChar, getClient());

		if (HwidConfig.ALLOW_GUARD_SYSTEM)
			HwidLog.auditGMAction(activeChar.getHWid(), activeChar.getName());

		Quest.playerEnter(activeChar);
		if (!Config.DISABLE_TUTORIAL)
			loadTutorial(activeChar);

		for (Quest quest : QuestManager.getInstance().getAllManagedScripts())
		{
			if (quest != null && quest.getOnEnterWorld())
				quest.notifyEnterWorld(activeChar);
		}
		activeChar.sendPacket(new QuestList(activeChar));
		
		// Unread mails make a popup appears.
		if (Config.ENABLE_COMMUNITY_BOARD && MailBBSManager.getInstance().checkUnreadMail(activeChar) > 0)
		{
			activeChar.sendPacket(SystemMessageId.NEW_MAIL);
			activeChar.sendPacket(new PlaySound("systemmsg_e.1233"));
			activeChar.sendPacket(ExMailArrived.STATIC_PACKET);
		}
		
		// Clan notice, if active.
		if (Config.ALLOW_MOD_MENU && clan != null && clan.isNoticeEnabled())
		{
			NpcHtmlMessage notice = new NpcHtmlMessage(0);
			notice.setFile("data/html/clan_notice.htm");
			notice.replace("%clan_name%", clan.getName());
			notice.replace("%notice_text%", clan.getNotice().replaceAll("\r\n", "<br>").replaceAll("action", "").replaceAll("bypass", ""));
			sendPacket(notice);
		}
		else if (Config.SERVER_NEWS)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/servnews.htm");
			sendPacket(html);
		}
		
		if (Config.ANNOUNCE_CASTLE_LORDS)
			notifyCastleOwner(activeChar);
		
		if (Config.ANNOUNCE_AIO_LOGIN)
			notifyAioEnter(activeChar);
		
		if (Config.ANNOUNCE_HERO_LOGIN)
			notifyHeroEnter(activeChar);
		
		PetitionManager.getInstance().checkPetitionMessages(activeChar);
		
		// no broadcast needed since the player will already spawn dead to others
		if (activeChar.isAlikeDead())
			sendPacket(new Die(activeChar));
		
		activeChar.onPlayerEnter();

		sendPacket(new SkillCoolTime(activeChar));
		
		// If player logs back in a stadium, port him in nearest town.
		if (Olympiad.getInstance().playerInStadia(activeChar))
			activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);

		if (DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), false))
			DimensionalRiftManager.getInstance().teleportToWaitingRoom(activeChar);
		
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
			activeChar.sendPacket(SystemMessageId.CLAN_MEMBERSHIP_TERMINATED);
		
		// Attacker or spectator logging into a siege zone will be ported at town.
		if (!activeChar.isGM() && (!activeChar.isInSiege() || activeChar.getSiegeState() < 2) && activeChar.isInsideZone(ZoneId.SIEGE))
			activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);
		
		// Invasion Tournament Enter protection
		if (activeChar.isInsideZone(ZoneId.INVASION) || (activeChar.isInsideZone(ZoneId.FLAG_AREA)))
			activeChar.teleToLocation(80938, 148613, -3469, 0);
		
		L2ClassMasterInstance.showQuestionMark(activeChar);

		if (Config.TALK_CHAT_ALL_CONFIG)
		{
			int _calcule = (int) arredondaValor(1, activeChar.getOnlineTime() / 60);
			
			if (_calcule < Config.TALK_CHAT_ALL_TIME)
			{
				long currentTime = System.currentTimeMillis();
				currentTime += (Config.TALK_CHAT_ALL_TIME - _calcule) * 60000;
				activeChar.setChatAllTimer(currentTime);
			}
		}
		
		if (activeChar.getFirstLog() || activeChar.getSelectArmor() || activeChar.getSelectWeapon() || activeChar.getSelectClasse())
			onEnterNewbie(activeChar);
		
		if (!activeChar.getPincheck())
			onEnterPin(activeChar);
		
		activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private void onEnterPin(L2PcInstance activeChar)
	{
		String last = "";
		String curr = "";
		try
		{
			last = LastIP(activeChar);
			curr = activeChar.getClient().getConnection().getInetAddress().getHostAddress();
		}
		catch (Exception e)
		{
		}

		if (!last.equals(curr))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/mods/Security_Enter.htm");
			sendPacket(html);
			activeChar.startAbnormalEffect(0x0800);
			activeChar.setIsSubmitingPin(true);
			activeChar.setIsImmobilized(true);
		}
	}
	
	public static String LastIP(L2PcInstance player)
	{
		String lastIp = "";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			ResultSet rset;
			PreparedStatement statement = con.prepareStatement("SELECT * FROM `accounts` WHERE login = ?");
			statement.setString(1, player.getAccountName());
			rset = statement.executeQuery();
			while(rset.next())
			{
				lastIp = rset.getString("lastIP");
			}
			con.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "LastIPCheck: Error checking IP: ", e);
		}
		return lastIp;
	}

	private void onEnterVip(L2PcInstance activeChar)
	{
		long now = Calendar.getInstance().getTimeInMillis();
		long endDay = activeChar.getVipEndTime();
		if(now > endDay)
		{
			activeChar.setVip(false);
			activeChar.setVipEndTime(0);
			//activeChar.lostVipSkills();
			if(Config.ALLOW_VIP_ITEM)
			{
				activeChar.getInventory().destroyItemByItemId("", Config.VIP_ITEMID, 1, activeChar, null);
				activeChar.getWarehouse().destroyItemByItemId("", Config.VIP_ITEMID, 1, activeChar, null);
			}
			activeChar.sendPacket(new CreatureSay(0,Say2.HERO_VOICE,"System","Your VIP period is up."));
		}
		else
		{
			Date dt = new Date(endDay);
			_daysleft = (endDay - now)/86400000;
			
			if(_daysleft > 30)
				activeChar.sendMessage("VIP period ends in " + df.format(dt) + ".");
			else if(_daysleft > 0)
				activeChar.sendMessage("Left " + (int)_daysleft + " days for VIP period ends");
			else if(_daysleft < 1)
			{
				long hour = (endDay - now)/3600000;
			    activeChar.sendMessage("Left " + (int)hour + " hours to VIP period ends");
			}
		}
	}

	private void onEnterAio(L2PcInstance activeChar)
	{
		long now = Calendar.getInstance().getTimeInMillis();
		long endDay = activeChar.getAioEndTime();
		
		if(now > endDay)
		{
			activeChar.setAio(false);
			activeChar.setAioEndTime(0);
			activeChar.removeSkills();
			activeChar.removeExpAndSp(6299994999L, 366666666);
			if(Config.ALLOW_AIO_ITEM)
			{
				activeChar.getInventory().destroyItemByItemId("", Config.AIO_ITEMID, 1, activeChar, null);
				activeChar.getWarehouse().destroyItemByItemId("", Config.AIO_ITEMID, 1, activeChar, null);
			}
			activeChar.sendPacket(new CreatureSay(0,Say2.HERO_VOICE, "System", "Your AIO period is up."));
		}
		else
		{
			Date dt = new Date(endDay);
			_daysleft = (endDay - now)/86400000;
			
			if(_daysleft > 30)
				activeChar.sendMessage("AIO period ends in " + df.format(dt) + ".");
			else if(_daysleft > 0)
				activeChar.sendMessage("Left " + (int)_daysleft + " days for AIO period ends");
			else if(_daysleft < 1)
			{
				long hour = (endDay - now)/3600000;
			    activeChar.sendMessage("Left " + (int)hour + " hours to AIO period ends");
			}
		}
	}
	
	private static void engage(L2PcInstance cha)
	{
		int _chaid = cha.getObjectId();
		
		for (Couple cl : CoupleManager.getInstance().getCouples())
		{
			if (cl.getPlayer1Id() == _chaid || cl.getPlayer2Id() == _chaid)
			{
				if (cl.getMaried())
					cha.setMarried(true);
				
				cha.setCoupleId(cl.getId());
			}
		}
	}
	
	private static void notifyClanMembers(L2PcInstance activeChar)
	{
		final L2Clan clan = activeChar.getClan();
		
		// Refresh player instance.
		clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);
		
		final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN).addPcName(activeChar);
		final PledgeShowMemberListUpdate update = new PledgeShowMemberListUpdate(activeChar);
		
		// Send packet to others members.
		for (L2PcInstance member : clan.getOnlineMembers())
		{
			if (member == activeChar)
				continue;
			
			member.sendPacket(msg);
			member.sendPacket(update);
		}
	}
	
	private static void notifySponsorOrApprentice(L2PcInstance activeChar)
	{
		if (activeChar.getSponsor() != 0)
		{
			L2PcInstance sponsor = L2World.getInstance().getPlayer(activeChar.getSponsor());
			if (sponsor != null)
				sponsor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_APPRENTICE_S1_HAS_LOGGED_IN).addPcName(activeChar));
		}
		else if (activeChar.getApprentice() != 0)
		{
			L2PcInstance apprentice = L2World.getInstance().getPlayer(activeChar.getApprentice());
			if (apprentice != null)
				apprentice.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_SPONSOR_S1_HAS_LOGGED_IN).addPcName(activeChar));
		}
	}
	
	private static void loadTutorial(L2PcInstance player)
	{
		QuestState qs = player.getQuestState("Tutorial");
		if (qs != null)
			qs.getQuest().notifyEvent("UC", null, player);
	}

	private void notifyCastleOwner(L2PcInstance activeChar)
	{
	    if (activeChar.isCastleLord(1) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Gludio Castle is Now Online!");

	    else if (activeChar.isCastleLord(2) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Dion Castle is Now Online!");

	    else if (activeChar.isCastleLord(3) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Giran Castle is Now Online!");

	    else if (activeChar.isCastleLord(4) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Oren Castle is Now Online!");
	    
	    else if (activeChar.isCastleLord(5) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Aden Castle is Now Online!");

	    else if (activeChar.isCastleLord(6) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Innadril Castle is Now Online!");

	    else if (activeChar.isCastleLord(7) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Goddard Castle is Now Online!");

	    else if (activeChar.isCastleLord(8) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Rune Castle is Now Online!");

	    else if (activeChar.isCastleLord(9) && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Lord " + activeChar.getName() + " Ruler Of Schuttgart Castle is Now Online!");
	}
	
	private void notifyAioEnter(L2PcInstance activeChar)
	{
	    if (activeChar.isAio() && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Aio " + activeChar.getName() + " is Now Online!");
	}
	
	private void notifyHeroEnter(L2PcInstance activeChar)
	{
	    if (activeChar.isHero() && (!activeChar.isGM()))
	        Broadcast.gameAnnounceToOnlinePlayers("Hero " + activeChar.getName() + " is Now Online!");
	}

	private void onEnterNewbie(L2PcInstance activeChar)
	{
		if (Config.STARTUP_SYSTEM_ENABLED)
		{
			//make char disappears
			activeChar.getAppearance().setInvisible();
			activeChar.broadcastUserInfo();
			activeChar.decayMe();
			activeChar.spawnMe();
			//active start system
			sendPacket(new SpecialCamera(activeChar.getObjectId(), 30, 200, 20, 999999999, 999999999, 0, 0, 1, 0));
			StartupSystem.startSetup(activeChar);
		}
	}
	
	public static double arredondaValor(int casasDecimais, double valor)
	{
		BigDecimal decimal = new BigDecimal(valor);
		return decimal.setScale(casasDecimais, 3).doubleValue();
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}