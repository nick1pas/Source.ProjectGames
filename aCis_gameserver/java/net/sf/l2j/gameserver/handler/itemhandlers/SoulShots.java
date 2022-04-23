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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.util.Rnd;

public class SoulShots implements IItemHandler
{
	private static final int MANA_POT_CD = 2, HEALING_POT_CD = 11, CP_POT_CD = 2;

	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;
		
		final L2PcInstance activeChar = (L2PcInstance) playable;
		final ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		final Weapon weaponItem = activeChar.getActiveWeaponItem();
		final int itemId = item.getItemId();
		
	    if (itemId == 9510 || itemId == 9511 || itemId == 9512)
		{
			switch (itemId)
			{
				case 9510: // Mana Potion
				{
					if (activeChar.isAutoPot(9510))
					{
						activeChar.sendPacket(new ExAutoSoulShot(9510, 0));
						activeChar.sendMessage("Deactivated auto mana potions.");
						activeChar.setAutoPot(9510, null, false);
					}
					else
					{
						if (activeChar.getInventory().getItemByItemId(9510) != null)
						{
							if (activeChar.isInOlympiadMode())
							{
								activeChar.sendMessage("This item cannot be used on Olympiad Games.");
								return;
							}
							if (activeChar.getInventory().getItemByItemId(9510).getCount() > 1)
							{
								activeChar.sendPacket(new ExAutoSoulShot(9510, 1));
								activeChar.sendMessage("Activated auto mana potions.");
								activeChar.setAutoPot(9510, ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoPot(9510, activeChar), 1000, MANA_POT_CD * 1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2279, 2, 0, 100);
								activeChar.broadcastPacket(msu);
											
								ItemSkills is = new ItemSkills();
								is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9510), true);
							}
						}
					}
					break;
				}
				case 9511: // Greater Healing Potion
				{
					if (activeChar.isAutoPot(9511))
					{
						activeChar.sendPacket(new ExAutoSoulShot(9511, 0));
						activeChar.sendMessage("Deactivated auto healing potions.");
						activeChar.setAutoPot(9511, null, false);
					}
					else
					{
						if (activeChar.getInventory().getItemByItemId(9511) != null)
						{
							if (activeChar.isInOlympiadMode())
							{
								activeChar.sendMessage("This item cannot be used on Olympiad Games.");
								return;
							}
							if (activeChar.getInventory().getItemByItemId(9511).getCount() > 1)
							{
								activeChar.sendPacket(new ExAutoSoulShot(9511, 1));
								activeChar.sendMessage("Activated auto healing potions.");
								activeChar.setAutoPot(9511, ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoPot(9511, activeChar), 1000, HEALING_POT_CD * 1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2037, 1, 0, 100);
								activeChar.broadcastPacket(msu);
			
								ItemSkills is = new ItemSkills();
								is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9511), true);
							}
						}
					}	
					break;
				}
				case 9512: // Greater CP Potion
				{
					if (activeChar.isAutoPot(9512))
					{
						activeChar.sendPacket(new ExAutoSoulShot(9512, 0));
						activeChar.sendMessage("Deactivated auto cp potions.");
						activeChar.setAutoPot(9512, null, false);
					}
					else
					{
						if (activeChar.getInventory().getItemByItemId(9512) != null)
						{
							if (activeChar.isInOlympiadMode())
							{
								activeChar.sendMessage("This item cannot be used on Olympiad Games.");
								return;
							}
							if (activeChar.getInventory().getItemByItemId(9512).getCount() > 1)
							{
								activeChar.sendPacket(new ExAutoSoulShot(9512, 1));
								activeChar.sendMessage("Activated auto cp potions.");
								activeChar.setAutoPot(9512, ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoPot(9512, activeChar), 1000, CP_POT_CD * 1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2166, 2, 0, 100);
								activeChar.broadcastPacket(msu);
											
								ItemSkills is = new ItemSkills();
								is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9512), true);
							}
						}
					}
					break;
				}
			}
			return;
		}
					
		// Check if soulshot can be used
		if (weaponInst == null || weaponItem.getSoulShotCount() == 0)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
				activeChar.sendPacket(SystemMessageId.CANNOT_USE_SOULSHOTS);
			return;
		}
		
		if (weaponItem.getCrystalType() != item.getItem().getCrystalType())
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
				activeChar.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
			
			return;
		}
		
		// Check if Soulshot are already active.
		if (activeChar.isChargedShot(ShotType.SOULSHOT))
			return;
		
		// Consume Soulshots if player has enough of them.
		int ssCount = weaponItem.getSoulShotCount();
		if (weaponItem.getReducedSoulShot() > 0 && Rnd.get(100) < weaponItem.getReducedSoulShotChance())
			ssCount = weaponItem.getReducedSoulShot();
		
		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), ssCount, null, false))
		{
			if (!activeChar.disableAutoShot(itemId))
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);
			
			return;
		}
		
		final SkillHolder[] skills = item.getItem().getSkills();
		
		weaponInst.setChargedShot(ShotType.SOULSHOT, true);
		activeChar.sendPacket(SystemMessageId.ENABLED_SOULSHOT);
		Broadcast.toSelfAndKnownPlayersInRadiusSq(activeChar, new MagicSkillUse(activeChar, activeChar, skills[0].getSkillId(), 1, 0, 0), 360000);
	}
		
	private class AutoPot implements Runnable
	{
		private int id;
		private L2PcInstance activeChar;
			
		public AutoPot(int id, L2PcInstance activeChar)
		{
			this.id = id;
			this.activeChar = activeChar;
		}
			
		@Override
		public void run()
		{
			if (activeChar.getInventory().getItemByItemId(id) == null)
			{
				activeChar.sendPacket(new ExAutoSoulShot(id, 0));
				activeChar.setAutoPot(id, null, false);
				return;
			}
				
			switch (id)
			{
				case 9510:
				{
					if (activeChar.getCurrentMp() < 0.70*activeChar.getMaxMp())
					{
						MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2279, 2, 0, 100);
						activeChar.broadcastPacket(msu);
							
						ItemSkills is = new ItemSkills();
						is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9510), true);
					}
						
					break;
				}
				case 9511:
				{
					if (activeChar.getCurrentHp() < 0.95*activeChar.getMaxHp())
					{
						MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2037, 1, 0, 100);
						activeChar.broadcastPacket(msu);
							
						ItemSkills is = new ItemSkills();
						is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9511), true);
					}
						
					break;
				}
				case 9512:
				{
					if (activeChar.getCurrentCp() < 0.95*activeChar.getMaxCp())
					{
						MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2166, 2, 0, 100);
						activeChar.broadcastPacket(msu);
							
						ItemSkills is = new ItemSkills();
						is.useItem(activeChar, activeChar.getInventory().getItemByItemId(9512), true);
					}
						
					break;
				}
			}
				
			if (activeChar.getInventory().getItemByItemId(id) == null)
			{
				activeChar.sendPacket(new ExAutoSoulShot(id, 0));
				activeChar.setAutoPot(id, null, false);
			}
		}
	}
}