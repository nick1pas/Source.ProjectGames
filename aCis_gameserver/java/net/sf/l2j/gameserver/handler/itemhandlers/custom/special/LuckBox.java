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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.util.Rnd;

public class LuckBox implements IItemHandler
{
	/*
    private final int CODEX_BOOK = 6622;
    private final int BLACK_LIFE_STONE = 8742;
    private final int SILVER_LIFE_STONE = 8752;
    private final int GOLD_LIFE_STONE = 8762;
    */

	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance)playable;

		switch (Rnd.get(8))
		{
		    case 0:
		    {
			     activeChar.addItem("Luck Box", Config.REWARD_LV_1, Config.REWARD_COUNT_1, activeChar, true);
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 1:
		    {
			     activeChar.addItem("Luck Box", Config.REWARD_LV_2, Config.REWARD_COUNT_2, activeChar, true);
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 2:
		    {
			     activeChar.addItem("Luck Box", Config.REWARD_LV_3, Config.REWARD_COUNT_3, activeChar, true);
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 3:
		    {
			     activeChar.addItem("Luck Box", Config.REWARD_LV_4, Config.REWARD_COUNT_4, activeChar, true);
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendMessage("Ohh noo! Your lucky box is empty.");   
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendMessage("Ohh noo! Your lucky box is empty.");   
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 6:
		    {
		    	 activeChar.sendMessage("Ohh noo! Your lucky box is empty.");   
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		    case 7:
		    {
		    	 activeChar.sendMessage("Ohh noo! Your lucky box is empty.");   
			     playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			     MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
			     activeChar.broadcastPacket(MSU);
			     break;
		    }
		}
	}
}