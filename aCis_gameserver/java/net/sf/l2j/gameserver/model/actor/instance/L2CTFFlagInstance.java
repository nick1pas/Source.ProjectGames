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

import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFConfig;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEvent;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class L2CTFFlagInstance extends L2NpcInstance
{
	private static final String flagsPath = "data/html/mods/events/ctf/flags/";

	public L2CTFFlagInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void showChatWindow(L2PcInstance playerInstance, int val)
	{
		if (playerInstance == null)
			return;
		
		if (CTFEvent.isStarting() || CTFEvent.isStarted())
		{
			final String flag = getTitle();
			final String team = CTFEvent.getParticipantTeam(playerInstance.getObjectId()).getName();
			final String enemyteam = CTFEvent.getParticipantEnemyTeam(playerInstance.getObjectId()).getName();

			// player talking to friendly flag
			if (flag == team)
			{
				// team flag is missing
				if (CTFEvent.getEnemyCarrier(playerInstance) != null)
				{
					final String htmContent = HtmCache.getInstance().getHtm(flagsPath + "flag_friendly_missing.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
					
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%enemyteam%", enemyteam);
					npcHtmlMessage.replace("%team%", team);
					npcHtmlMessage.replace("%player%", playerInstance.getName());
					playerInstance.sendPacket(npcHtmlMessage);
				}
				// player has returned with enemy flag
				else if (playerInstance == CTFEvent.getTeamCarrier(playerInstance))
				{
					if (CTFConfig.CTF_EVENT_CAPTURE_SKILL > 0)
					{
						playerInstance.broadcastPacket(new MagicSkillUse(playerInstance, CTFConfig.CTF_EVENT_CAPTURE_SKILL, 1, 1, 1));
					}
					CTFEvent.removeFlagCarrier(playerInstance);
					CTFEvent.getParticipantTeam(playerInstance.getObjectId()).increasePoints();
					CTFEvent.broadcastScreenMessage(playerInstance.getName() + " scored for the " + team + " team!", 7);
				}
				// go get the flag
				else
				{
					final String htmContent = HtmCache.getInstance().getHtm(flagsPath + "flag_friendly.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
					
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%enemyteam%", enemyteam);
					npcHtmlMessage.replace("%team%", team);
					npcHtmlMessage.replace("%player%", playerInstance.getName());
					playerInstance.sendPacket(npcHtmlMessage);
				}
			}
			else
			// player talking to enemy flag
			{
				// player has flag
				if (CTFEvent.playerIsCarrier(playerInstance))
				{
					final String htmContent = HtmCache.getInstance().getHtm(flagsPath + "flag_enemy.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
					
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%enemyteam%", enemyteam);
					npcHtmlMessage.replace("%team%", team);
					npcHtmlMessage.replace("%player%", playerInstance.getName());
					playerInstance.sendPacket(npcHtmlMessage);
				}
				// enemy flag is missing
				else if (CTFEvent.getTeamCarrier(playerInstance) != null)
				{
					final String htmContent = HtmCache.getInstance().getHtm(flagsPath + "flag_enemy_missing.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
					
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%enemyteam%", enemyteam);
					npcHtmlMessage.replace("%player%", CTFEvent.getTeamCarrier(playerInstance).getName());
					playerInstance.sendPacket(npcHtmlMessage);
				}
				// take flag
				else
				{
					if (CTFConfig.CTF_EVENT_CAPTURE_SKILL > 0)
					{
						playerInstance.broadcastPacket(new MagicSkillUse(playerInstance, CTFConfig.CTF_EVENT_CAPTURE_SKILL, 1, 1, 1));
					}
					CTFEvent.setCarrierUnequippedWeapons(playerInstance, playerInstance.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND), playerInstance.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND));
					playerInstance.getInventory().equipItem(ItemTable.getInstance().createItem("ctf", CTFEvent.getEnemyTeamFlagId(playerInstance), 1, playerInstance, null));
					playerInstance.getInventory().blockAllItems();
					playerInstance.broadcastUserInfo();
					CTFEvent.setTeamCarrier(playerInstance);
					CTFEvent.broadcastScreenMessage(playerInstance.getName() + " has taken the " + enemyteam + " flag team!", 5);
				}
			}
		}
		
		playerInstance.sendPacket(ActionFailed.STATIC_PACKET);
	}
}