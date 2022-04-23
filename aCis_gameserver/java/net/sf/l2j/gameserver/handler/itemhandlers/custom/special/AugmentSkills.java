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
package net.sf.l2j.gameserver.handler.itemhandlers.custom.special;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class AugmentSkills implements IItemHandler
{
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance) playable;
		
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("This item cannot be used on Olympiad Games.");
			return;
		}

		int itemId = item.getItemId();
		
		if (itemId == 9800) // Item Skill: Might Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3132, 10), false, false);
		
		if (itemId == 9801) // Item Skill: Shield Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3135, 10), false, false);
		
		if (itemId == 9802) // Item Skill: Duel Might Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3134, 10), false, false);	
		
		if (itemId == 9803) // Item Skill: Empower Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3133, 10), false, false);
		
		if (itemId == 9804) // Item Skill: Magic Barrier Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3136, 10), false, false);	
		
		if (itemId == 9805) // Item Skill: Wild Magic Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3142, 10), false, false);	
		
		if (itemId == 9806) // Item Skill: Blessed Body Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3124, 10), false, false);
		
		if (itemId == 9807) // Item Skill: Heal Empower Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3138, 10), false, false);	
		
		if (itemId == 9808) // Item Skill: Agility Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3139, 10), false, false);	
		
		if (itemId == 9809) // Item Skill: Guidance Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3140, 10), false, false);
		
		if (itemId == 9810) // Item Skill: Focus Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3141, 10), false, false);
		
		if (itemId == 9811) // Item Skill: Refresh Lv.10
			activeChar.useMagic(SkillTable.getInstance().getInfo(3199, 3), false, false);
	}
}