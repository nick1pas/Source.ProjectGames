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

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;

public class InfinityStone implements IItemHandler
{
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance)playable;
		
		if (activeChar.isAllSkillsDisabled())
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("This item cannot be used on Olympiad Games.");
		}

		//weapons infinity
		if (activeChar.getInventory().getInventoryItemCount(6611, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6612, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6613, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6614, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6615, 1) >= 1 || activeChar.getInventory().getInventoryItemCount(6616, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6617, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6618, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6619, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6620, -1) >= 1 || activeChar.getInventory().getInventoryItemCount(6621, -1) >= 1)
		{
			activeChar.sendMessage("Your weapon infinity has been destructed.");
			
			//weapons inventory 
			activeChar.getInventory().destroyItemByItemId("Destroy", 6611, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6612, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6613, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6614, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6615, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6616, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6617, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6618, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6619, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6620, 1, activeChar, null);
			activeChar.getInventory().destroyItemByItemId("Destroy", 6621, 1, activeChar, null);
			
			//weapons warehouse
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6611, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6612, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6613, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6614, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6615, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6616, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6617, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6618, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6619, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6620, 1, activeChar, null);
			activeChar.getWarehouse().destroyItemByItemId("Destroy", 6621, 1, activeChar, null);
			
			//prism stone
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			activeChar.getInventory().updateDatabase();
			activeChar.sendPacket(new ItemList(activeChar, true));
		}
		else
		{
			activeChar.sendMessage("You do not have a infinity weapon.");
		}
	}
}