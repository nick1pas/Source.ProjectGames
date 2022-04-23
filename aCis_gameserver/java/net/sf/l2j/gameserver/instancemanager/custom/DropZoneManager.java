/*
 * Copyright (C) 2014-2018 L2jAdmins
 * 
 * This file is part of L2jAdmins.
 * 
 * L2jAdmins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2jAdmins is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.instancemanager.custom;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.holder.RewardHolder;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;

/**
 * @author DooFy
 */
public class DropZoneManager
{
	protected static final Logger _log = Logger.getLogger(DropZoneManager.class.getName());

	protected DropZoneManager()
	{
	}

	public static final DropZoneManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static boolean _canReward = false;
	private static HashMap<String, Integer> _playerHwids = new HashMap<>();

	// Give Reward
	public final static void addReward(L2Character killer, L2MonsterInstance monster)
	{
		if (killer instanceof L2Playable)
		{
			L2PcInstance player = killer.getActingPlayer();

			if (player.isInParty())
			{
				List<L2PcInstance> party = player.getParty().getPartyMembers();

				for (L2PcInstance member : party)
				{
					String pHwid = member.getHWid();

					if (!_playerHwids.containsKey(pHwid))
					{
						_playerHwids.put(pHwid, 1);
						_canReward = true;
					}
					else
					{
						int count = _playerHwids.get(pHwid);

						if (count < 1)
						{
							_playerHwids.remove(pHwid);
							_playerHwids.put(pHwid, count + 1);
							_canReward = true;
						}
						else
						{
							member.sendMessage("Already 1 member of your PC have been rewarded, so this character won't be rewarded.");
							_canReward = false;
						}
					}
					if (_canReward)
					{
						if (member.isInsideRadius(monster.getX(), monster.getY(), monster.getZ(), 1000, false, false))
							RandomReward(member);
						else
							member.sendMessage("You are too far from your party to be rewarded.");
					}
				}
				_playerHwids.clear();   
			}
			else
			{
				RandomReward(player);
			}
		}
	}

	public static void RandomReward(L2PcInstance player)
	{
		for (RewardHolder reward : Config.PARTY_ZONE_REWARDS)
		{
			if (Rnd.get(100) <= reward.getRewardChance())
			{
				if (player.isVip())
				{
					player.getInventory().addItem("Cube Reward", reward.getRewardId(), reward.getRewardCount() * Config.VIP_DROP_RATE, player, null);

					if (reward.getRewardCount() > 1)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(reward.getRewardId()).addItemNumber(reward.getRewardCount()));
					else
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(reward.getRewardId()));
				}
				else
				{
					player.getInventory().addItem("Cube Reward", reward.getRewardId(), reward.getRewardCount(), player, null);

					if (reward.getRewardCount() > 1)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(reward.getRewardId()).addItemNumber(reward.getRewardCount()));
					else
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(reward.getRewardId()));
				}
			}
		}
	}

	private static class SingletonHolder
	{
		protected static final DropZoneManager _instance = new DropZoneManager();
	}
}