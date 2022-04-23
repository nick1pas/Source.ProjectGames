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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEvent;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMEvent;
import net.sf.l2j.gameserver.model.entity.events.lastman.LMEvent;
import net.sf.l2j.gameserver.model.entity.events.teamvsteam.TvTEvent;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinParty;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * format cdd
 */
public final class RequestJoinParty extends L2GameClientPacket
{
	private String _name;
	private int _itemDistribution;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_itemDistribution = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance requestor = getClient().getActiveChar();
		if (requestor == null)
			return;
		
		final L2PcInstance target = L2World.getInstance().getPlayer(_name);
		if (target == null)
		{
			requestor.sendPacket(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
			return;
		}
		
		if (BlockList.isBlocked(target, requestor))
		{
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST).addPcName(target));
			return;
		}
		
		if (target.equals(requestor) || target.isCursedWeaponEquipped() || requestor.isCursedWeaponEquipped() || target.getAppearance().getInvisible()&& !target.isInsideZone(ZoneId.TOURNAMENT) && !target.isGM())
		{
			requestor.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		if (target.isInParty())
		{
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_ALREADY_IN_PARTY).addPcName(target));
			return;
		}
		
		if (CTFEvent.isPlayerParticipant(target.getObjectId())  || DMEvent.isPlayerParticipant(target.getObjectId()) || LMEvent.isPlayerParticipant(target.getObjectId()) || TvTEvent.isPlayerParticipant(target.getObjectId()))
		{
			requestor.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
        if (target.isPartyInRefuse())
        {
            requestor.sendMessage("The player you tried to invite is in refusal party mode.");
            return;
        }
			
		if (target.getClient().isDetached())
		{
			requestor.sendMessage("The player you tried to invite is in offline mode.");
			return;
		}
		
		if (target.isInJail() || requestor.isInJail())
		{
			requestor.sendMessage("The player you tried to invite is currently jailed.");
			return;
		}
		
		if (OlympiadManager.getInstance().isRegistered(target) || OlympiadManager.getInstance().isRegistered(requestor))
		{
			requestor.sendMessage("You can't party with this player right now.");
			return;
		}
		
		if (target.isInOlympiadMode() || requestor.isInOlympiadMode())
			return;
		
		if (!requestor.isInParty())
			createNewParty(target, requestor);
		else
		{
			/*
			if (target.getClassId() == ClassId.shillenElder || target.getClassId() == ClassId.shillienSaint || target.getClassId() == ClassId.bishop || target.getClassId() == ClassId.cardinal || target.getClassId() == ClassId.elder || target.getClassId() == ClassId.evaSaint)
			{
				checkParty(requestor);
				if (requestor.bs_cont <= Config.MAX_HEALER_ON_PARTY)
				{
					requestor.sendMessage("Max of " + Config.MAX_HEALER_ON_PARTY + " healer's are allowed per party!");
					cleanCheck(requestor);
					return;
				}
			}
			
			if (target.getClassId() == ClassId.overlord || target.getClassId() == ClassId.dominator)
			{
				checkParty(requestor);
				if (requestor.dominator_cont <= Config.MAX_COMBAT_HEALER_ON_PARTY)
				{
					requestor.sendMessage("Max of " + Config.MAX_COMBAT_HEALER_ON_PARTY + " combat healer's are allowed per party!");
					cleanCheck(requestor);
					return;
				}
			}
			*/
			
			if (!requestor.getParty().isInDimensionalRift())
				addTargetToParty(target, requestor);
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private static void addTargetToParty(L2PcInstance target, L2PcInstance requestor)
	{
		final L2Party party = requestor.getParty();
		if (party == null)
			return;
		
		if (!party.isLeader(requestor))
		{
			requestor.sendPacket(SystemMessageId.ONLY_LEADER_CAN_INVITE);
			return;
		}
		
		if (party.getMemberCount() >= 9)
		{
			requestor.sendPacket(SystemMessageId.PARTY_FULL);
			return;
		}
		
		if (party.getPendingInvitation() && !party.isInvitationRequestExpired())
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			return;
		}

		if (target.getClassId() == ClassId.shillenElder || target.getClassId() == ClassId.shillienSaint || target.getClassId() == ClassId.bishop || target.getClassId() == ClassId.cardinal || target.getClassId() == ClassId.elder || target.getClassId() == ClassId.evaSaint)
		{
			checkHealer(requestor);
			if (requestor.bs_cont > Config.MAX_HEALER_ON_PARTY || requestor.getClassId() == ClassId.cardinal && requestor.bs_cont > Config.MAX_HEALER_ON_PARTY -1 || requestor.getClassId() == ClassId.bishop && requestor.bs_cont > Config.MAX_HEALER_ON_PARTY -1 || requestor.getClassId() == ClassId.shillenElder && requestor.bs_cont > Config.MAX_HEALER_ON_PARTY -1 || requestor.getClassId() == ClassId.shillienSaint && requestor.bs_cont > Config.MAX_HEALER_ON_PARTY -1 || requestor.getClassId() == ClassId.elder && requestor.bs_cont > 1 || requestor.getClassId() == ClassId.evaSaint && requestor.bs_cont > Config.MAX_HEALER_ON_PARTY -1)
			{
				requestor.sendMessage("Max of " + Config.MAX_HEALER_ON_PARTY + " healer's are allowed per party!");
				return;
			}
		}

		if (!target.isProcessingRequest())
		{
			requestor.onTransactionRequest(target);
			
			// in case a leader change has happened, use party's mode
			target.sendPacket(new AskJoinParty(requestor.getName(), party.getLootDistribution()));
			party.setPendingInvitation(true);
			
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_INVITED_S1_TO_PARTY).addPcName(target));
			if (Config.DEBUG)
				_log.fine("Sent out a party invitation to " + target.getName());
		}
		else
		{
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addPcName(target));
			if (Config.DEBUG)
				_log.warning(requestor.getName() + " already received a party invitation");
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void createNewParty(L2PcInstance target, L2PcInstance requestor)
	{
		if (!target.isProcessingRequest())
		{
			requestor.setParty(new L2Party(requestor, _itemDistribution));
			
			requestor.onTransactionRequest(target);
			target.sendPacket(new AskJoinParty(requestor.getName(), _itemDistribution));
			requestor.getParty().setPendingInvitation(true);
			
			if (Config.DEBUG)
				_log.fine("Sent out a party invitation to " + target.getName());
			
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_INVITED_S1_TO_PARTY).addPcName(target));
		}
		else
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			
			if (Config.DEBUG)
				_log.warning(requestor.getName() + " already received a party invitation");
		}
	}
	
	public static void checkHealer(L2PcInstance activeChar)
	{
		L2Party playerParty = activeChar.getParty();
		
		for (L2PcInstance player : playerParty.getPartyMembers())
		{
			if (player != null)
			{
				if (player.getParty() != null)
				{
					if (player.getClassId() == ClassId.shillenElder || player.getClassId() == ClassId.shillienSaint || player.getClassId() == ClassId.bishop || player.getClassId() == ClassId.cardinal || player.getClassId() == ClassId.elder || player.getClassId() == ClassId.evaSaint)
						activeChar.bs_cont = activeChar.bs_cont + 1;
				}
			}
		}
	}
}