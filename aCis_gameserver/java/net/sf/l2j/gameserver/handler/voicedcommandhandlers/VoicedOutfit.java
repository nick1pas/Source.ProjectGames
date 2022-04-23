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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.data.DressMeData;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class VoicedOutfit implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{ 
		"dress",
		"dark",
		"light",
		"zaken",
		"muskeeter",
		"wizard"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("dress"))
		{
			if (activeChar.isVip())
				sendMainWindow(activeChar);
			else
				activeChar.sendMessage("You are not VIP.");
		}

		else if (command.equals("dark"))
		{
			if (activeChar.isVip())
			{
				if (activeChar.getDressMeData() == null)
				{
					DressMeData dmd = new DressMeData();
					activeChar.setDressMeData(dmd);
				}

				activeChar.setDressMeEnabled(true);

				activeChar.getDressMeData().setChestId(9951);
				activeChar.getDressMeData().setLegsId(9952);
				activeChar.getDressMeData().setGlovesId(9953);
				activeChar.getDressMeData().setBootsId(9954);
				activeChar.getDressMeData().setHairId(9950);
				activeChar.broadcastUserInfo();
			}
			else
				activeChar.sendMessage("You are not VIP.");
		}
		else if (command.equals("light"))
		{
			if (activeChar.isVip())
			{
				if (activeChar.getDressMeData() == null)
				{
					DressMeData dmd = new DressMeData();
					activeChar.setDressMeData(dmd);
				}

				activeChar.setDressMeEnabled(true);

				activeChar.getDressMeData().setChestId(9956);
				activeChar.getDressMeData().setLegsId(9957);
				activeChar.getDressMeData().setGlovesId(9958);
				activeChar.getDressMeData().setBootsId(9959);
				activeChar.getDressMeData().setHairId(9955);
				activeChar.broadcastUserInfo();
			}
			else
				activeChar.sendMessage("You are not VIP.");
		}
		else if (command.equals("zaken"))
		{
			if (activeChar.isVip())
			{
				if (activeChar.getDressMeData() == null)
				{
					DressMeData dmd = new DressMeData();
					activeChar.setDressMeData(dmd);
				}

				activeChar.setDressMeEnabled(true);

				activeChar.getDressMeData().setChestId(9961);
				activeChar.getDressMeData().setLegsId(9962);
				activeChar.getDressMeData().setGlovesId(9963);
				activeChar.getDressMeData().setBootsId(9964);
				activeChar.getDressMeData().setHairId(9960);
				activeChar.broadcastUserInfo();
			}
			else
				activeChar.sendMessage("You are not VIP.");
		}
		else if (command.equals("muskeeter"))
		{
			if (activeChar.isVip())
			{
				if (activeChar.getDressMeData() == null)
				{
					DressMeData dmd = new DressMeData();
					activeChar.setDressMeData(dmd);
				}

				activeChar.setDressMeEnabled(true);

				activeChar.getDressMeData().setChestId(9965);
				activeChar.getDressMeData().setHairId(9966);
				activeChar.broadcastUserInfo();
			}
			else
				activeChar.sendMessage("You are not VIP.");
		}
		else if (command.equals("wizard"))
		{
			if (activeChar.isVip())
			{
				if (activeChar.getDressMeData() == null)
				{
					DressMeData dmd = new DressMeData();
					activeChar.setDressMeData(dmd);
				}

				activeChar.setDressMeEnabled(true);

				activeChar.getDressMeData().setChestId(9967);
				activeChar.getDressMeData().setHairId(9968);
				activeChar.broadcastUserInfo();
			}
			else
				activeChar.sendMessage("You are not VIP.");
		}

		return true;
	}

	public static void sendMainWindow(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/outfite/Main.htm"); 
		activeChar.sendPacket(html);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}