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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.entity.events.teamvsteam.TvTEvent;
import net.sf.l2j.gameserver.model.entity.events.tournaments.Arena3x3;
import net.sf.l2j.gameserver.model.entity.events.tournaments.Arena5x5;
import net.sf.l2j.gameserver.model.entity.events.tournaments.Arena9x9;
import net.sf.l2j.gameserver.model.entity.events.tournaments.properties.ArenaConfig;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class L2TournamentInstance extends L2NpcInstance
{
	public L2TournamentInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(L2PcInstance player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		String filename = "data/html/mods/tournament/index.htm";
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		
		if (Arena3x3.registered.size() == 0)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_0_over\" fore=\"L2UI_CH3.calculate1_0\">");
		else if (Arena3x3.registered.size() == 1)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_1_over\" fore=\"L2UI_CH3.calculate1_1\">");
		else if (Arena3x3.registered.size() == 2)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_2_over\" fore=\"L2UI_CH3.calculate1_2\">");
		else if (Arena3x3.registered.size() == 3)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_3_over\" fore=\"L2UI_CH3.calculate1_3\">");
		else if (Arena3x3.registered.size() == 4)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_4_over\" fore=\"L2UI_CH3.calculate1_4\">");
		else if (Arena3x3.registered.size() == 5)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_5_over\" fore=\"L2UI_CH3.calculate1_5\">");
		else if (Arena3x3.registered.size() == 6)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_6_over\" fore=\"L2UI_CH3.calculate1_6\">");
		else if (Arena3x3.registered.size() == 7)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_7_over\" fore=\"L2UI_CH3.calculate1_7\">");
		else if (Arena3x3.registered.size() == 8)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_8_over\" fore=\"L2UI_CH3.calculate1_8\">");
		else if (Arena3x3.registered.size() >= 9)
			html.replace("%3x3%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_9_over\" fore=\"L2UI_CH3.calculate1_9\">");
		
		if (Arena5x5.registered.size() == 0)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_0_over\" fore=\"L2UI_CH3.calculate1_0\">");
		else if (Arena5x5.registered.size() == 1)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_1_over\" fore=\"L2UI_CH3.calculate1_1\">");
		else if (Arena5x5.registered.size() == 2)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_2_over\" fore=\"L2UI_CH3.calculate1_2\">");
		else if (Arena5x5.registered.size() == 3)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_3_over\" fore=\"L2UI_CH3.calculate1_3\">");
		else if (Arena5x5.registered.size() == 4)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_4_over\" fore=\"L2UI_CH3.calculate1_4\">");
		else if (Arena5x5.registered.size() == 5)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_5_over\" fore=\"L2UI_CH3.calculate1_5\">");
		else if (Arena5x5.registered.size() == 6)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_6_over\" fore=\"L2UI_CH3.calculate1_6\">");
		else if (Arena5x5.registered.size() == 7)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_7_over\" fore=\"L2UI_CH3.calculate1_7\">");
		else if (Arena5x5.registered.size() == 8)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_8_over\" fore=\"L2UI_CH3.calculate1_8\">");
		else if (Arena5x5.registered.size() >= 9)
			html.replace("%5x5%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_9_over\" fore=\"L2UI_CH3.calculate1_9\">");
		
		if (Arena9x9.registered.size() == 0)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_0_over\" fore=\"L2UI_CH3.calculate1_0\">");
		else if (Arena9x9.registered.size() == 1)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_1_over\" fore=\"L2UI_CH3.calculate1_1\">");
		else if (Arena9x9.registered.size() == 2)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_2_over\" fore=\"L2UI_CH3.calculate1_2\">");
		else if (Arena9x9.registered.size() == 3)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_3_over\" fore=\"L2UI_CH3.calculate1_3\">");
		else if (Arena9x9.registered.size() == 4)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_4_over\" fore=\"L2UI_CH3.calculate1_4\">");
		else if (Arena9x9.registered.size() == 5)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_5_over\" fore=\"L2UI_CH3.calculate1_5\">");
		else if (Arena9x9.registered.size() == 6)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_6_over\" fore=\"L2UI_CH3.calculate1_6\">");
		else if (Arena9x9.registered.size() == 7)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_7_over\" fore=\"L2UI_CH3.calculate1_7\">");
		else if (Arena9x9.registered.size() == 8)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_8_over\" fore=\"L2UI_CH3.calculate1_8\">");
		else if (Arena9x9.registered.size() >= 9)
			html.replace("%9x9%", "<button value=\"\" action=\"\" width=32 height=32 back=\"L2UI_CH3.calculate1_9_over\" fore=\"L2UI_CH3.calculate1_9\">");
		
		player.sendPacket(html);
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("3x3"))
		{
			if (!ArenaConfig.ALLOW_3X3_REGISTER)
			{
				player.sendMessage("Tournament 3x3 is not enabled.");
				return;
			}
			
			/*
			if (player._active_boxes > 1 && !ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				final List<String> players_in_boxes = player.active_boxes_characters;
				
				if (players_in_boxes != null && players_in_boxes.size() > 1)
				{
					for (final String character_name : players_in_boxes)
					{
						final L2PcInstance ppl = L2World.getInstance().getPlayer(character_name);
						
						if (ppl != null && ppl.isArenaProtection())
						{
							player.sendMessage("You are already participating in Tournament with another char!");
							return;
						}
					}
				}
			}
			*/
			
			if (player.isArena3x3() || player.isArena5x5() || player.isArena9x9() || player.isArenaProtection())
			{
				player.sendMessage("Tournament: You already registered!");
				return;
			}
			else if (!player.isInParty())
			{
				player.sendMessage("Tournament: You dont have a party.");
				return;
			}
			else if (!player.getParty().isLeader(player))
			{
				player.sendMessage("Tournament: You are not the party leader!");
				return;
			}
			else if (player.getParty().getMemberCount() < 3)
			{
				player.sendMessage("Tournament: Your party does not have 3 members.");
				player.sendPacket(new ExShowScreenMessage("Your party does not have 3 members", 6 * 1000));
				return;
			}
			else if (player.getParty().getMemberCount() > 3)
			{
				player.sendMessage("Tournament: Your Party can not have more than 3 members.");
				player.sendPacket(new ExShowScreenMessage("Your Party can not have more than 3 members", 6 * 1000));
				return;
			}
			
			L2PcInstance assist1 = player.getParty().getPartyMembers().get(1);
			L2PcInstance assist2 = player.getParty().getPartyMembers().get(2);
			
			if (player.isCursedWeaponEquipped() || assist1.isCursedWeaponEquipped() || assist2.isCursedWeaponEquipped() || player.isInStoreMode() || assist1.isInStoreMode() || assist2.isInStoreMode() || player.getKarma() > 0 || assist1.getKarma() > 0 || assist2.getKarma() > 0)
			{
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist1.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist2.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				return;
			}
			
			if (OlympiadManager.getInstance().isRegistered(player) || OlympiadManager.getInstance().isRegistered(assist1) || OlympiadManager.getInstance().isRegistered(assist2))
			{
				player.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist1.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist2.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				return;
			}
			
			if (TvTEvent.isPlayerParticipant(player.getObjectId()) || TvTEvent.isPlayerParticipant(assist1.getObjectId()) || TvTEvent.isPlayerParticipant(assist2.getObjectId())) 
			{
				player.sendMessage("Tournament: You already participated in another event!");
				assist1.sendMessage("Tournament: You already participated in another event!");
				assist2.sendMessage("Tournament: You already participated in another event!");
				return;
			}
			
			if (!ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				String ip1 = player.getHWid();
				String ip2 = assist1.getHWid();
				String ip3 = assist2.getHWid();
				
				if (ip1.equals(ip2) || ip1.equals(ip3))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
				else if (ip2.equals(ip1) || ip2.equals(ip3))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
				else if (ip3.equals(ip1) || ip3.equals(ip2))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
			}
			
			if (ArenaConfig.ARENA_SKILL_PROTECT)
			{
				for (L2Effect effect : player.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						player.stopSkillEffects(effect.getSkill().getId());
				}
				
				for (L2Effect effect : assist1.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist1.stopSkillEffects(effect.getSkill().getId());
				}
				
				for (L2Effect effect : assist2.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist2.stopSkillEffects(effect.getSkill().getId());
				}
				
				for (L2Effect effect : player.getAllEffects())
				{
					if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
						player.stopSkillEffects(effect.getSkill().getId());
				}
				
				for (L2Effect effect : assist1.getAllEffects())
				{
					if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
						assist1.stopSkillEffects(effect.getSkill().getId());
				}
				
				for (L2Effect effect : assist2.getAllEffects())
				{
					if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
						assist2.stopSkillEffects(effect.getSkill().getId());
				}
			}
			
			if (Arena3x3.getInstance().register(player, assist1, assist2))
			{
				player.sendMessage("Tournament: Your participation has been approved.");
				assist1.sendMessage("Tournament: Your participation has been approved.");
				assist2.sendMessage("Tournament: Your participation has been approved.");
				player.setArenaProtection(true);
				assist1.setArenaProtection(true);
				assist2.setArenaProtection(true);
				player.setArena3x3(true);
				assist1.setArena3x3(true);
				assist2.setArena3x3(true);
				showChatWindow(player);
			}
			else
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
		}
		
		if (command.startsWith("5x5")) 
		{
			if (!ArenaConfig.ALLOW_5X5_REGISTER)
			{
				player.sendMessage("Tournament 5x5 is not enabled.");
				return;
			}
			
			/*
			if (player._active_boxes > 1 && !ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				final List<String> players_in_boxes = player.active_boxes_characters;
				
				if (players_in_boxes != null && players_in_boxes.size() > 1)
				{
					for (final String character_name : players_in_boxes)
					{
						final L2PcInstance ppl = L2World.getInstance().getPlayer(character_name);
						
						if (ppl != null && ppl.isArenaProtection())
						{
							player.sendMessage("You are already participating in Tournament with another char!");
							return;
						}
					}
				}
			}
			*/
			
			if (player.isArena3x3() || player.isArena5x5() || player.isArena9x9() || player.isArenaProtection())
			{
				player.sendMessage("Tournament: You already registered!");
				return;
			}
			else if (!player.isInParty())
			{
				player.sendMessage("Tournament: You dont have a party.");
				return;
			}
			else if (!player.getParty().isLeader(player))
			{
				player.sendMessage("Tournament: You are not the party leader!");
				return;
			}
			else if (player.getParty().getMemberCount() < 5)
			{
				player.sendMessage("Tournament: Your party does not have 4 members.");
				player.sendPacket(new ExShowScreenMessage("Your party does not have 4 members", 6 * 1000));
				return;
			}
			else if (player.getParty().getMemberCount() > 5)
			{
				player.sendMessage("Tournament: Your Party can not have more than 4 members.");
				player.sendPacket(new ExShowScreenMessage("Your Party can not have more than 4 members", 6 * 1000));
				return;
			}
			
			L2PcInstance assist1 = player.getParty().getPartyMembers().get(1);
			L2PcInstance assist2 = player.getParty().getPartyMembers().get(2);
			L2PcInstance assist3 = player.getParty().getPartyMembers().get(3);
			L2PcInstance assist4 = player.getParty().getPartyMembers().get(4);
			
			if (player.isCursedWeaponEquipped() || assist1.isCursedWeaponEquipped() || assist2.isCursedWeaponEquipped() || assist3.isCursedWeaponEquipped() || assist4.isCursedWeaponEquipped() || player.isInStoreMode() || assist1.isInStoreMode() || assist2.isInStoreMode() || assist3.isInStoreMode() || assist4.isInStoreMode() || player.getKarma() > 0 || assist1.getKarma() > 0 || assist2.getKarma() > 0 || assist3.getKarma() > 0 || assist4.getKarma() > 0)
			{
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist1.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist2.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist3.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist4.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				return;
			}
			
			if (OlympiadManager.getInstance().isRegistered(player) || OlympiadManager.getInstance().isRegistered(assist1) || OlympiadManager.getInstance().isRegistered(assist2) || OlympiadManager.getInstance().isRegistered(assist3) || OlympiadManager.getInstance().isRegistered(assist4))
			{
				player.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist1.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist2.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist3.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist4.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				return;
			}
			
			if (TvTEvent.isPlayerParticipant(player.getObjectId()) || TvTEvent.isPlayerParticipant(assist1.getObjectId()) || TvTEvent.isPlayerParticipant(assist2.getObjectId()) || TvTEvent.isPlayerParticipant(assist3.getObjectId()) || TvTEvent.isPlayerParticipant(assist4.getObjectId())) 
			{
				player.sendMessage("Tournament: You already participated in TvT event!");
				assist1.sendMessage("Tournament: You already participated in TvT event!");
				assist2.sendMessage("Tournament: You already participated in TvT event!");
				assist3.sendMessage("Tournament: You already participated in TvT event!");
				assist4.sendMessage("Tournament: You already participated in TvT event!");
				return;
			}
			
			if (!ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				String ip1 = player.getHWid();
				String ip2 = assist1.getHWid();
				String ip3 = assist2.getHWid();
				String ip4 = assist3.getHWid();
				String ip5 = assist4.getHWid();
				
				if (ip1.equals(ip2) || ip1.equals(ip3) || ip1.equals(ip4) || ip1.equals(ip5))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					assist3.sendMessage("Tournament: Register only 1 player per Computer");
					assist4.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
				else if (ip2.equals(ip1) || ip2.equals(ip3) || ip2.equals(ip4) || ip2.equals(ip5))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					assist3.sendMessage("Tournament: Register only 1 player per Computer");
					assist4.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
				else if (ip3.equals(ip1) || ip3.equals(ip2) || ip3.equals(ip4) || ip3.equals(ip5))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					assist3.sendMessage("Tournament: Register only 1 player per Computer");
					assist4.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
				else if (ip4.equals(ip1) || ip4.equals(ip2) || ip4.equals(ip3) || ip3.equals(ip5))
				{
					player.sendMessage("Tournament: Register only 1 player per Computer");
					assist1.sendMessage("Tournament: Register only 1 player per Computer");
					assist2.sendMessage("Tournament: Register only 1 player per Computer");
					assist3.sendMessage("Tournament: Register only 1 player per Computer");
					assist4.sendMessage("Tournament: Register only 1 player per Computer");
					return;
				}
			}
			
			for (L2Effect effect : player.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					player.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist1.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist1.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist2.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist2.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist3.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist3.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist4.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist4.stopSkillEffects(effect.getSkill().getId());
			}
			
			ClasseCheck(player);
			
			if (player.duelist_cont > ArenaConfig.duelist_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.duelist_COUNT_5X5 + " Duelist's or " + ArenaConfig.duelist_COUNT_5X5 + " Grand Khauatari's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.duelist_COUNT_5X5 + " Duelist's or " + ArenaConfig.duelist_COUNT_5X5 + " Grand Khauatari'sallowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dreadnought_cont > ArenaConfig.dreadnought_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dreadnought_COUNT_5X5 + " Dread Nought's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dreadnought_COUNT_5X5 + " Dread Nought's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.tanker_cont > ArenaConfig.tanker_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.tanker_COUNT_5X5 + " Tanker's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.tanker_COUNT_5X5 + " Tanker's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dagger_cont > ArenaConfig.dagger_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dagger_COUNT_5X5 + " Dagger's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dagger_COUNT_5X5 + " Dagger's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.archer_cont > ArenaConfig.archer_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.archer_COUNT_5X5 + " Archer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.archer_COUNT_5X5 + " Archer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.bs_cont > ArenaConfig.bs_COUNT_5X5)
			{
				if (ArenaConfig.bs_COUNT_5X5 == 0)
				{
					player.sendMessage("Tournament: Bishop's not allowed in 5x5.");
					player.sendPacket(new ExShowScreenMessage("Tournament: Bishop's not allowed in 5x5.", 6 * 1000));
				}
				else
				{
					player.sendMessage("Tournament: Only " + ArenaConfig.bs_COUNT_5X5 + " Bishop's allowed per party.");
					player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.bs_COUNT_5X5 + " Bishop's allowed per party.", 6 * 1000));
				}
				clean(player);
				return;
			}
			else if (player.archmage_cont > ArenaConfig.archmage_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.archmage_COUNT_5X5 + " Archmage's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.archmage_COUNT_5X5 + " Archmage's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.soultaker_cont > ArenaConfig.soultaker_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.soultaker_COUNT_5X5 + " Soultaker's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.soultaker_COUNT_5X5 + " Soultaker's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.mysticMuse_cont > ArenaConfig.mysticMuse_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.mysticMuse_COUNT_5X5 + " Mystic Muse's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.mysticMuse_COUNT_5X5 + " Mystic Muse's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.stormScreamer_cont > ArenaConfig.stormScreamer_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.stormScreamer_COUNT_5X5 + " Storm Screamer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.stormScreamer_COUNT_5X5 + " Storm Screamer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.titan_cont > ArenaConfig.titan_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.titan_COUNT_5X5 + " Titan's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.titan_COUNT_5X5 + " Titan's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dominator_cont > ArenaConfig.dominator_COUNT_5X5)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dominator_COUNT_5X5 + " Dominator's or " + ArenaConfig.dominator_COUNT_5X5 + " Doomcryer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dominator_COUNT_5X5 + " Dominator's or " + ArenaConfig.dominator_COUNT_5X5 + " Doomcryer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (Arena5x5.getInstance().register(player, assist1, assist2, assist3, assist4) && (player.getParty().getPartyMembers().get(1) != null && player.getParty().getPartyMembers().get(2) != null && player.getParty().getPartyMembers().get(3) != null && player.getParty().getPartyMembers().get(4) != null))
			{
				player.sendMessage("Tournament: Your participation has been approved.");
				assist1.sendMessage("Tournament: Your participation has been approved.");
				assist2.sendMessage("Tournament: Your participation has been approved.");
				assist3.sendMessage("Tournament: Your participation has been approved.");
				assist4.sendMessage("Tournament: Your participation has been approved.");
				
				player.setArenaProtection(true);
				assist1.setArenaProtection(true);
				assist2.setArenaProtection(true);
				assist3.setArenaProtection(true);
				assist4.setArenaProtection(true);
				
				player.setArena5x5(true);
				assist1.setArena5x5(true);
				assist2.setArena5x5(true);
				assist3.setArena5x5(true);
				assist4.setArena5x5(true);
				clean(player);
				showChatWindow(player);
			}
			else
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
		}
		
		if (command.startsWith("9x9")) 
		{
			if (!ArenaConfig.ALLOW_9X9_REGISTER)
			{
				player.sendMessage("Tournament 9x9 is not enabled.");
				return;
			}
			
			/*
			if (player._active_boxes > 1 && !ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				final List<String> players_in_boxes = player.active_boxes_characters;
				
				if (players_in_boxes != null && players_in_boxes.size() > 1)
				{
					for (final String character_name : players_in_boxes)
					{
						final L2PcInstance ppl = L2World.getInstance().getPlayer(character_name);
						
						if (ppl != null && ppl.isArenaProtection())
						{
							player.sendMessage("You are already participating in Tournament with another char!");
							return;
						}
					}
				}
			}
			*/
			
			if (player.isArena3x3() || player.isArena5x5() || player.isArena9x9() || player.isArenaProtection())
			{
				player.sendMessage("Tournament: You already registered!");
				return;
			}
			else if (!player.isInParty())
			{
				player.sendMessage("Tournament: You dont have a party.");
				return;
			}
			else if (!player.getParty().isLeader(player))
			{
				player.sendMessage("Tournament: You are not the party leader!");
				return;
			}
			else if (player.getParty().getMemberCount() < 9)
			{
				player.sendMessage("Tournament: Your party does not have 9 members.");
				player.sendPacket(new ExShowScreenMessage("Your party does not have 9 members", 6 * 1000));
				return;
			}
			else if (player.getParty().getMemberCount() > 9)
			{
				player.sendMessage("Tournament: Your Party can not have more than 9 members.");
				player.sendPacket(new ExShowScreenMessage("Your Party can not have more than 9 members", 6 * 1000));
				return;
			}
			
			L2PcInstance assist = player.getParty().getPartyMembers().get(1);
			L2PcInstance assist2 = player.getParty().getPartyMembers().get(2);
			L2PcInstance assist3 = player.getParty().getPartyMembers().get(3);
			L2PcInstance assist4 = player.getParty().getPartyMembers().get(4);
			L2PcInstance assist5 = player.getParty().getPartyMembers().get(5);
			L2PcInstance assist6 = player.getParty().getPartyMembers().get(6);
			L2PcInstance assist7 = player.getParty().getPartyMembers().get(7);
			L2PcInstance assist8 = player.getParty().getPartyMembers().get(8);
			
			if (player.isCursedWeaponEquipped() || assist.isCursedWeaponEquipped() || assist2.isCursedWeaponEquipped() || assist3.isCursedWeaponEquipped() || assist4.isCursedWeaponEquipped() || assist5.isCursedWeaponEquipped() || assist6.isCursedWeaponEquipped() || assist7.isCursedWeaponEquipped() || assist8.isCursedWeaponEquipped() || player.isInStoreMode() || assist.isInStoreMode() || assist2.isInStoreMode() || assist3.isInStoreMode() || assist4.isInStoreMode() || assist5.isInStoreMode() || assist6.isInStoreMode() || assist7.isInStoreMode() || assist8.isInStoreMode() || player.getKarma() > 0 || assist.getKarma() > 0 || assist2.getKarma() > 0 || assist3.getKarma() > 0 || assist4.getKarma() > 0 || assist5.getKarma() > 0 || assist6.getKarma() > 0 || assist7.getKarma() > 0 || assist8.getKarma() > 0)
			{
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist2.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist3.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist4.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist5.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist6.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist7.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				assist8.sendMessage("Tournament: You or your member does not have the necessary requirements.");
				return;
			}
			
			if (OlympiadManager.getInstance().isRegistered(player) || OlympiadManager.getInstance().isRegistered(assist) || OlympiadManager.getInstance().isRegistered(assist2) || OlympiadManager.getInstance().isRegistered(assist3) || OlympiadManager.getInstance().isRegistered(assist4) || OlympiadManager.getInstance().isRegistered(assist5) || OlympiadManager.getInstance().isRegistered(assist6) || OlympiadManager.getInstance().isRegistered(assist7) || OlympiadManager.getInstance().isRegistered(assist8))
			{
				player.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist2.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist3.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist4.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist5.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist6.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist7.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				assist8.sendMessage("Tournament: You or your member is registered in the Olympiad.");
				return;
			}
			
			if (TvTEvent.isPlayerParticipant(player.getObjectId()) || TvTEvent.isPlayerParticipant(assist.getObjectId()) || TvTEvent.isPlayerParticipant(assist2.getObjectId()) || TvTEvent.isPlayerParticipant(assist3.getObjectId()) || TvTEvent.isPlayerParticipant(assist4.getObjectId()) || TvTEvent.isPlayerParticipant(assist5.getObjectId()) || TvTEvent.isPlayerParticipant(assist6.getObjectId()) || TvTEvent.isPlayerParticipant(assist7.getObjectId()) || TvTEvent.isPlayerParticipant(assist8.getObjectId())) 
			{
				player.sendMessage("Tournament: You already participated in TvT event!");
				assist.sendMessage("Tournament: You already participated in TvT event!");
				assist2.sendMessage("Tournament: You already participated in TvT event!");
				assist3.sendMessage("Tournament: You already participated in TvT event!");
				assist4.sendMessage("Tournament: You already participated in TvT event!");
				assist5.sendMessage("Tournament: You already participated in TvT event!");
				assist6.sendMessage("Tournament: You already participated in TvT event!");
				assist7.sendMessage("Tournament: You already participated in TvT event!");
				assist8.sendMessage("Tournament: You already participated in TvT event!");
				return;
			}
			
			if (!ArenaConfig.Allow_Same_HWID_On_Tournament)
			{
				if (player.isInParty() && (player.getParty().getPartyMembers().get(1) != null && player.getParty().getPartyMembers().get(2) != null && player.getParty().getPartyMembers().get(3) != null && player.getParty().getPartyMembers().get(4) != null && player.getParty().getPartyMembers().get(5) != null && player.getParty().getPartyMembers().get(6) != null && player.getParty().getPartyMembers().get(7) != null && player.getParty().getPartyMembers().get(8) != null))
				{
					String ip1 = player.getHWid();
					String ip2 = assist.getHWid();
					String ip3 = assist2.getHWid();
					String ip4 = assist3.getHWid();
					String ip5 = assist4.getHWid();
					String ip6 = assist5.getHWid();
					String ip7 = assist6.getHWid();
					String ip8 = assist7.getHWid();
					String ip9 = assist8.getHWid();
					
					if (ip1.equals(ip2) || ip1.equals(ip3) || ip1.equals(ip4) || ip1.equals(ip5) || ip1.equals(ip6) || ip1.equals(ip7) || ip1.equals(ip8) || ip1.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip2.equals(ip1) || ip2.equals(ip3) || ip2.equals(ip4) || ip2.equals(ip5) || ip2.equals(ip6) || ip2.equals(ip7) || ip2.equals(ip8) || ip2.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip3.equals(ip1) || ip3.equals(ip2) || ip3.equals(ip4) || ip3.equals(ip5) || ip3.equals(ip6) || ip3.equals(ip7) || ip3.equals(ip8) || ip3.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip4.equals(ip1) || ip4.equals(ip2) || ip4.equals(ip3) || ip4.equals(ip5) || ip4.equals(ip6) || ip4.equals(ip7) || ip4.equals(ip8) || ip4.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip5.equals(ip1) || ip5.equals(ip2) || ip5.equals(ip3) || ip5.equals(ip4) || ip5.equals(ip6) || ip5.equals(ip7) || ip5.equals(ip8) || ip5.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip6.equals(ip1) || ip6.equals(ip2) || ip6.equals(ip3) || ip6.equals(ip4) || ip6.equals(ip5) || ip6.equals(ip7) || ip6.equals(ip8) || ip6.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip7.equals(ip1) || ip7.equals(ip2) || ip7.equals(ip3) || ip7.equals(ip4) || ip7.equals(ip5) || ip7.equals(ip6) || ip7.equals(ip8) || ip7.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip8.equals(ip1) || ip8.equals(ip2) || ip8.equals(ip3) || ip8.equals(ip4) || ip8.equals(ip5) || ip8.equals(ip6) || ip8.equals(ip7) || ip8.equals(ip9))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
					else if (ip9.equals(ip1) || ip9.equals(ip2) || ip9.equals(ip3) || ip9.equals(ip4) || ip9.equals(ip5) || ip9.equals(ip6) || ip9.equals(ip7) || ip9.equals(ip8))
					{
						player.sendMessage("Tournament: Register only 1 player per Computer");
						assist.sendMessage("Tournament: Register only 1 player per Computer");
						assist2.sendMessage("Tournament: Register only 1 player per Computer");
						assist3.sendMessage("Tournament: Register only 1 player per Computer");
						assist4.sendMessage("Tournament: Register only 1 player per Computer");
						assist5.sendMessage("Tournament: Register only 1 player per Computer");
						assist6.sendMessage("Tournament: Register only 1 player per Computer");
						assist7.sendMessage("Tournament: Register only 1 player per Computer");
						assist8.sendMessage("Tournament: Register only 1 player per Computer");
						return;
					}
				}
			}
			
			for (L2Effect effect : player.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					player.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist2.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist2.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist3.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist3.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist4.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist4.stopSkillEffects(effect.getSkill().getId());
			}
			
			for (L2Effect effect : assist5.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist5.stopSkillEffects(effect.getSkill().getId());
			}
			for (L2Effect effect : assist6.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist6.stopSkillEffects(effect.getSkill().getId());
			}
			for (L2Effect effect : assist7.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist7.stopSkillEffects(effect.getSkill().getId());
			}
			for (L2Effect effect : assist8.getAllEffects())
			{
				if (ArenaConfig.ARENA_DISABLE_SKILL_LIST_PERM.contains(effect.getSkill().getId()))
					assist8.stopSkillEffects(effect.getSkill().getId());
			}
			
			ClasseCheck(player);
			
			if (player.duelist_cont > ArenaConfig.duelist_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.duelist_COUNT_9X9 + " Duelist's or " + ArenaConfig.duelist_COUNT_9X9 + " Grand Khauatari's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.duelist_COUNT_9X9 + " Duelist's or " + ArenaConfig.duelist_COUNT_9X9 + " Grand Khauatari's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dreadnought_cont > ArenaConfig.dreadnought_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dreadnought_COUNT_9X9 + " Dread Nought's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dreadnought_COUNT_9X9 + " Dread Nought's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.tanker_cont > ArenaConfig.tanker_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.tanker_COUNT_9X9 + " Tanker's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.tanker_COUNT_9X9 + " Tanker's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dagger_cont > ArenaConfig.dagger_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dagger_COUNT_9X9 + " Dagger's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dagger_COUNT_9X9 + " Dagger's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.archer_cont > ArenaConfig.archer_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.archer_COUNT_9X9 + " Archer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.archer_COUNT_9X9 + " Archer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.bs_cont > ArenaConfig.bs_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.bs_COUNT_9X9 + " Bishop's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.bs_COUNT_9X9 + " Bishop's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.archmage_cont > ArenaConfig.archmage_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.archmage_COUNT_9X9 + " Archmage's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.archmage_COUNT_9X9 + " Archmage's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.soultaker_cont > ArenaConfig.soultaker_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.soultaker_COUNT_9X9 + " Soultaker's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.soultaker_COUNT_9X9 + " Soultaker's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.mysticMuse_cont > ArenaConfig.mysticMuse_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.mysticMuse_COUNT_9X9 + " Mystic Muse's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.mysticMuse_COUNT_9X9 + " Mystic Muse's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.stormScreamer_cont > ArenaConfig.stormScreamer_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.stormScreamer_COUNT_9X9 + " Storm Screamer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.stormScreamer_COUNT_9X9 + " Storm Screamer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.titan_cont > ArenaConfig.titan_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.titan_COUNT_9X9 + " Titan's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.titan_COUNT_9X9 + " Titan's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (player.dominator_cont > ArenaConfig.dominator_COUNT_9X9)
			{
				player.sendMessage("Tournament: Only " + ArenaConfig.dominator_COUNT_9X9 + " Dominator's or " + ArenaConfig.dominator_COUNT_9X9 + " Doomcryer's allowed per party.");
				player.sendPacket(new ExShowScreenMessage("Only " + ArenaConfig.dominator_COUNT_9X9 + " Dominator's or " + ArenaConfig.dominator_COUNT_9X9 + " Doomcryer's allowed per party.", 6 * 1000));
				clean(player);
				return;
			}
			else if (Arena9x9.getInstance().register(player, assist, assist2, assist3, assist4, assist5, assist6, assist7, assist8) && (player.getParty().getPartyMembers().get(1) != null && player.getParty().getPartyMembers().get(2) != null && player.getParty().getPartyMembers().get(3) != null && player.getParty().getPartyMembers().get(4) != null && player.getParty().getPartyMembers().get(5) != null && player.getParty().getPartyMembers().get(6) != null && player.getParty().getPartyMembers().get(7) != null && player.getParty().getPartyMembers().get(8) != null))
			{
				player.sendMessage("Tournament: Your participation has been approved.");
				assist.sendMessage("Tournament: Your participation has been approved.");
				assist2.sendMessage("Tournament: Your participation has been approved.");
				assist3.sendMessage("Tournament: Your participation has been approved.");
				assist4.sendMessage("Tournament: Your participation has been approved.");
				assist5.sendMessage("Tournament: Your participation has been approved.");
				assist6.sendMessage("Tournament: Your participation has been approved.");
				assist7.sendMessage("Tournament: Your participation has been approved.");
				assist8.sendMessage("Tournament: Your participation has been approved.");
				
				player.setArenaProtection(true);
				assist.setArenaProtection(true);
				assist2.setArenaProtection(true);
				assist3.setArenaProtection(true);
				assist4.setArenaProtection(true);
				assist5.setArenaProtection(true);
				assist6.setArenaProtection(true);
				assist7.setArenaProtection(true);
				assist8.setArenaProtection(true);
				
				player.setArena9x9(true);
				assist.setArena9x9(true);
				assist2.setArena9x9(true);
				assist3.setArena9x9(true);
				assist4.setArena9x9(true);
				assist5.setArena9x9(true);
				assist6.setArena9x9(true);
				assist7.setArena9x9(true);
				assist8.setArena9x9(true);
				clean(player);
				showChatWindow(player);
				
			}
			else
				player.sendMessage("Tournament: You or your member does not have the necessary requirements.");
		}
		else if (command.startsWith("remove"))
		{
			if (!player.isInParty())
			{
				player.sendMessage("Tournament: You dont have a party.");
				return;
			}
			else if (!player.getParty().isLeader(player))
			{
				player.sendMessage("Tournament: You are not the party leader!");
				return;
			}
			
			if (player.isArena3x3())
				Arena3x3.getInstance().remove(player);
			if (player.isArena5x5())
				Arena5x5.getInstance().remove(player);
			if (player.isArena9x9())
				Arena9x9.getInstance().remove(player);
		}
		else
			super.onBypassFeedback(player, command);
	}

	public void ClasseCheck(L2PcInstance activeChar)
	{
		L2Party plparty = activeChar.getParty();
		for (L2PcInstance player : plparty.getPartyMembers())
		{
			if (player != null)
			{
				if (player.getParty() != null)
				{
					if (player.getClassId() == ClassId.gladiator || player.getClassId() == ClassId.duelist)			
						activeChar.duelist_cont = activeChar.duelist_cont+1;		

					if (player.getClassId() == ClassId.warlord || player.getClassId() == ClassId.dreadnought)			
						activeChar.dreadnought_cont = activeChar.dreadnought_cont+1;		

					if (player.getClassId() == ClassId.paladin || player.getClassId() == ClassId.phoenixKnight || player.getClassId() == ClassId.darkAvenger || player.getClassId() == ClassId.hellKnight || player.getClassId() == ClassId.evaTemplar || player.getClassId() == ClassId.templeKnight || player.getClassId() == ClassId.shillienKnight || player.getClassId() == ClassId.shillienTemplar)		
						activeChar.tanker_cont = activeChar.tanker_cont+1;		

					if (player.getClassId() == ClassId.adventurer || player.getClassId() == ClassId.treasureHunter || player.getClassId() == ClassId.windRider || player.getClassId() == ClassId.plainsWalker || player.getClassId() == ClassId.ghostHunter || player.getClassId() == ClassId.abyssWalker)		
						activeChar.dagger_cont = activeChar.dagger_cont+1;		

					if (player.getClassId() == ClassId.hawkeye || player.getClassId() == ClassId.sagittarius|| player.getClassId() == ClassId.silverRanger || player.getClassId() == ClassId.moonlightSentinel|| player.getClassId() == ClassId.phantomRanger|| player.getClassId() == ClassId.ghostSentinel)		
						activeChar.archer_cont = activeChar.archer_cont+1;		

					if (player.getClassId() == ClassId.shillenElder || player.getClassId() == ClassId.shillienSaint || player.getClassId() == ClassId.bishop || player.getClassId() == ClassId.cardinal || player.getClassId() == ClassId.elder || player.getClassId() == ClassId.evaSaint)			
						activeChar.bs_cont = activeChar.bs_cont+1;		

					if (player.getClassId() == ClassId.archmage || player.getClassId() == ClassId.sorceror)			
						activeChar.archmage_cont = activeChar.archmage_cont+1;		

					if (player.getClassId() == ClassId.soultaker || player.getClassId() == ClassId.necromancer)			
						activeChar.soultaker_cont = activeChar.soultaker_cont+1;		

					if (player.getClassId() == ClassId.mysticMuse || player.getClassId() == ClassId.spellsinger)			
						activeChar.mysticMuse_cont = activeChar.mysticMuse_cont+1;		

					if (player.getClassId() == ClassId.stormScreamer || player.getClassId() == ClassId.spellhowler)			
						activeChar.stormScreamer_cont = activeChar.stormScreamer_cont+1;		

					if (player.getClassId() == ClassId.titan || player.getClassId() == ClassId.destroyer)			
						activeChar.titan_cont = activeChar.titan_cont+1;		

					if (player.getClassId() == ClassId.tyrant || player.getClassId() == ClassId.grandKhauatari)			
						activeChar.grandKhauatari_cont = activeChar.grandKhauatari_cont+1;		

					if (player.getClassId() == ClassId.orcShaman || player.getClassId() == ClassId.overlord)			
						activeChar.dominator_cont = activeChar.dominator_cont+1;		

					if (player.getClassId() == ClassId.doomcryer || player.getClassId() == ClassId.warcryer)			
						activeChar.doomcryer_cont = activeChar.doomcryer_cont+1;		

				}
			}
		}
	}
	
	public void clean(L2PcInstance player)
	{	
		player.duelist_cont = 0;
		player.dreadnought_cont = 0;
		player.tanker_cont = 0;
		player.dagger_cont = 0;
		player.archer_cont = 0;
		player.bs_cont = 0;
		player.archmage_cont = 0;
		player.soultaker_cont = 0;
		player.mysticMuse_cont = 0;
		player.stormScreamer_cont = 0;
		player.titan_cont = 0;
		player.grandKhauatari_cont = 0;
		player.dominator_cont = 0;
		player.doomcryer_cont = 0;
	}
}