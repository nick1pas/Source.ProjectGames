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

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.data.DressMeData;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

public class ItemSkins implements IItemHandler
{
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance) playable;

		int itemId = item.getItemId();

		if (itemId == 30000) // Dark
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26103);
			activeChar.getDressMeData().setHairId(26108);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30001) // Light
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26102);
			activeChar.getDressMeData().setHairId(26107);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30002) // Pirate
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(9961);
			activeChar.getDressMeData().setLegsId(9962);
			activeChar.getDressMeData().setGlovesId(9963);
			activeChar.getDressMeData().setBootsId(9964);
			activeChar.getDressMeData().setHairId(9960);

			activeChar.broadcastUserInfo();
		}

		if (itemId == 30003) // Muskeeter
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(9965);
			activeChar.getDressMeData().setHairId(9966);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30004) // Wizard
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(9967);
			activeChar.getDressMeData().setHairId(9968);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30005) // Archer
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26100);
			activeChar.getDressMeData().setHairId(26105);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30006) // Ninja
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26101);
			activeChar.getDressMeData().setHairId(26106);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30007) // Beleth
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26104);
			activeChar.getDressMeData().setHairId(26109);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30008) // Knight
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26110);
			activeChar.getDressMeData().setHairId(26111);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30009) // Healer
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26112);
			activeChar.getDressMeData().setHairId(26113);

			activeChar.broadcastUserInfo();
		}
		
		if (itemId == 30010) // Lilith
		{
			if (activeChar.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				activeChar.setDressMeData(dmd);
			}
			activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1036, 1, 4000, 0));

			activeChar.setDressMeEnabled(true);

			activeChar.getDressMeData().setChestId(26114);
			activeChar.getDressMeData().setHairId(26115);

			activeChar.broadcastUserInfo();
		}
	}
}