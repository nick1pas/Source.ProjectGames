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
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.entity.events.tournaments.properties.ArenaConfig;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.type.ActionType;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.type.L2InvasionZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.PetItemList;
import net.sf.l2j.gameserver.templates.skills.L2SkillType;

public final class UseItem extends L2GameClientPacket
{
	private int _objectId;
	private boolean _ctrlPressed;
		
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_ctrlPressed = readD() != 0;
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if (activeChar.isSubmitingPin())
		{
			activeChar.sendMessage("Unable to do any action while PIN is not submitted.");
			return;
		}
	
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMessageId.ITEMS_UNAVAILABLE_FOR_STORE_MANUFACTURE);
			return;
		}
		
		if (activeChar.getActiveTradeList() != null)
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_PICKUP_OR_USE_ITEM_WHILE_TRADING);
			return;
		}
		
		final ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null)
			return;
		
		if (item.getItem().getType2() == Item.TYPE2_QUEST)
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_USE_QUEST_ITEMS);
			return;
		}
		
		if (activeChar.isAlikeDead() || activeChar.isStunned() || activeChar.isSleeping() || activeChar.isParalyzed() || activeChar.isAfraid())
			return;
		
		if (Config.SUMMON_MOUNT_PROTECTION && activeChar.isInsideZone(ZoneId.PEACE) || activeChar.isInsideZone(ZoneId.SIEGE))
		{
			if (Config.LISTID_RESTRICT.contains(Integer.valueOf(item.getItemId())))
			{
				activeChar.sendMessage("You can not use this item here.");
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		if (activeChar.isInArenaEvent() || activeChar.isArenaProtection())
		{
			if (ArenaConfig.TOURNAMENT_LISTID_RESTRICT.contains(Integer.valueOf(item.getItemId())))
			{
				activeChar.sendMessage("You can not use this item during Tournament.");
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		if (!Config.KARMA_PLAYER_CAN_TELEPORT && activeChar.getKarma() > 0)
		{
			final SkillHolder[] sHolders = item.getItem().getSkills();
			if (sHolders != null)
			{
				for (SkillHolder sHolder : sHolders)
				{
					final L2Skill skill = sHolder.getSkill();
					if (skill != null && (skill.getSkillType() == L2SkillType.TELEPORT || skill.getSkillType() == L2SkillType.RECALL))
						return;
				}
			}
		}
		
		/*
		if (activeChar.isGM()) 
		{
			activeChar.sendPacket(new CreatureSay(0, Say2.PARTY, "Item Info", "ID: " + item.getItemId() + " | Name: " + item.getItemName()));
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		*/
		
		if (activeChar.isFishing() && item.getItem().getDefaultAction() != ActionType.fishingshot)
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_DO_WHILE_FISHING_3);
			return;
		}
		
		/*
		 * The player can't use pet items if no pet is currently summoned. If a pet is summoned and player uses the item directly, it will be used by the pet.
		 */
		if (item.isPetItem())
		{
			// If no pet, cancels the use
			if (!activeChar.hasPet())
			{
				activeChar.sendPacket(SystemMessageId.CANNOT_EQUIP_PET_ITEM);
				return;
			}
			
			final L2PetInstance pet = ((L2PetInstance) activeChar.getPet());
			
			if (!pet.canWear(item.getItem()))
			{
				activeChar.sendPacket(SystemMessageId.PET_CANNOT_USE_ITEM);
				return;
			}
			
			if (pet.isDead())
			{
				activeChar.sendPacket(SystemMessageId.CANNOT_GIVE_ITEMS_TO_DEAD_PET);
				return;
			}
			
			if (!pet.getInventory().validateCapacity(item))
			{
				activeChar.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS);
				return;
			}
			
			if (!pet.getInventory().validateWeight(item, 1))
			{
				activeChar.sendPacket(SystemMessageId.UNABLE_TO_PLACE_ITEM_YOUR_PET_IS_TOO_ENCUMBERED);
				return;
			}
			
			activeChar.transferItem("Transfer", _objectId, 1, pet.getInventory(), pet);
			
			// Equip it, removing first the previous item.
			if (item.isEquipped())
				pet.getInventory().unEquipItemInSlot(item.getLocationSlot());
			else
				pet.getInventory().equipPetItem(item);
			
			activeChar.sendPacket(new PetItemList(pet));
			pet.updateAndBroadcastStatus(1);
			return;
		}
		
		if (!activeChar.getInventory().canManipulateWithItemId(item.getItemId()))
			return;
		
		if (Config.DEBUG)
			_log.finest(activeChar.getName() + ": use item " + _objectId);
		
		if (!item.isEquipped())
		{
			if (!item.getItem().checkCondition(activeChar, activeChar, true))
				return;
		}

		if (activeChar.isInsideZone(ZoneId.INVASION) && L2InvasionZone.isRestrictedItem(item.getItemId()))
		{
			activeChar.sendMessage(item.getName() + " cannot be used inside Tournament Invasion Zone.");
			return;
		}

		if (item.isEquipable())
		{
			if (activeChar.isCastingNow() || activeChar.isCastingSimultaneouslyNow())
			{
				activeChar.sendPacket(SystemMessageId.CANNOT_USE_ITEM_WHILE_USING_MAGIC);
				return;
			}
			
			switch (item.getItem().getBodyPart())
			{
				case Item.SLOT_LR_HAND:
				case Item.SLOT_L_HAND:
				case Item.SLOT_R_HAND:
				{
					if (activeChar.isMounted())
					{
						activeChar.sendPacket(SystemMessageId.CANNOT_EQUIP_ITEM_DUE_TO_BAD_CONDITION);
						return;
					}
					
					// Don't allow weapon/shield equipment if a cursed weapon is equipped
					if (activeChar.isCursedWeaponEquipped())
						return;
					
					break;
				}
			}
			
			if (activeChar.isCursedWeaponEquipped() && item.getItemId() == 6408) // Don't allow to put formal wear
				return;
			
			if (activeChar.isAttackingNow())
			{
				ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
				{
					@Override
					public void run()
					{
	                    final ItemInstance itemToTest = activeChar.getInventory().getItemByObjectId(_objectId);
	                    if(itemToTest == null)
	                        return;
	                    
	                    activeChar.useEquippableItem(itemToTest, false);
					}
				}, activeChar.getAttackEndTime() - System.currentTimeMillis());
			}
			else
				activeChar.useEquippableItem(item, true);
		}
		else
		{
			if (activeChar.isCastingNow() && !(item.isPotion() || item.isElixir()))
				return;
			
			if (activeChar.getAttackType() == WeaponType.FISHINGROD && item.getItem().getItemType() == EtcItemType.LURE)
			{
				activeChar.getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, item);
				activeChar.broadcastUserInfo();
				
				sendPacket(new ItemList(activeChar, false));
				return;
			}
			
			final IItemHandler handler = ItemHandler.getInstance().getItemHandler(item.getEtcItem());
			if (handler != null)
				handler.useItem(activeChar, item, _ctrlPressed);
			
			for (Quest quest : item.getQuestEvents())
			{
				QuestState state = activeChar.getQuestState(quest.getName());
				if (state == null || !state.isStarted())
					continue;
				
				quest.notifyItemUse(item, activeChar, activeChar.getTarget());
			}
		}
	}
}