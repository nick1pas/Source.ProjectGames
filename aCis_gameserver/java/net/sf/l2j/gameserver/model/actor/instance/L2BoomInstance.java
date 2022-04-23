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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.Rnd;

public class L2BoomInstance extends L2NpcInstance
{	
	public L2BoomInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	//Mensagens
	private static final String[] BOX_MSG =
	{
		"I'm sorry $s1... hehehe!",
		"Fail... huehue!",
		"3..2..1... BoOoOoOoMm",
		"I never get tired of doing this!",
		"BoOoOoMm!"
	};
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("Blue_Box"))
		{
			if (player.getInventory().getInventoryItemCount(Config.EVENT_KEY, 0) >= Config.EVENT_KEY_AMOUNT_1)
			{
				player.destroyItemByItemId("Consume", Config.EVENT_KEY, Config.EVENT_KEY_AMOUNT_1, player, true);
				BlueBox(player, this);
			}
			else
			{
				player.sendMessage("You do not have enough keys.");
				return;
			}
		}
		else if (command.startsWith("Red_Box"))
		{
			if (player.getInventory().getInventoryItemCount(Config.EVENT_KEY, 0) >= Config.EVENT_KEY_AMOUNT_2)
			{
				player.destroyItemByItemId("Consume", Config.EVENT_KEY, Config.EVENT_KEY_AMOUNT_2, player, true);
				RedBox(player, this);
			}
			else
			{
				player.sendMessage("You do not have enough keys.");
				return;
			}
		}
		else if (command.startsWith("Green_Box"))
		{
			if (player.getInventory().getInventoryItemCount(Config.EVENT_KEY, 0) >= Config.EVENT_KEY_AMOUNT_3)
			{
				player.destroyItemByItemId("Consume", Config.EVENT_KEY, Config.EVENT_KEY_AMOUNT_3, player, true);
				GreenBox(player, this);
			}
			else
			{
				player.sendMessage("You do not have enough keys.");
				return;
			}
		}
		else if (command.startsWith("Yellow_Box"))
		{
			if (player.getInventory().getInventoryItemCount(Config.EVENT_KEY, 0) >= Config.EVENT_KEY_AMOUNT_4)
			{
				player.destroyItemByItemId("Consume", Config.EVENT_KEY, Config.EVENT_KEY_AMOUNT_4, player, true);
				YellowBox(player, this);
			}
			else
			{
				player.sendMessage("You do not have enough keys.");
				return;
			}
		}
		else if (command.startsWith("Black_Box"))
		{
			if (player.getInventory().getInventoryItemCount(Config.EVENT_KEY, 0) >= Config.EVENT_KEY_AMOUNT_5)
			{
				player.destroyItemByItemId("Consume", Config.EVENT_KEY, Config.EVENT_KEY_AMOUNT_5, player, true);
				BlackBox(player, this);
			}
			else
			{
				player.sendMessage("You do not have enough keys.");
				return;
			}
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	public void showChatWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/mods/treasure/Main.htm");
		html.replace("%objectId%", String.valueOf(player.getTargetId()));
		player.sendPacket(html);
	}

	//Lv.1 Reward's
	public void BlueBox(L2PcInstance activeChar, L2Npc npc)
	{
		switch (Rnd.get(6))
		{
		    case 0:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 1:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 2:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 3:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", "Nice, You WON it..."));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 2024, 1, 1, 0));
				 
				 for (int[] reward : Config.LVL_1_REWARD) 
				 {
					 if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					 {
						 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
					 } 
					 else 
					 {
						 for (int i = 0; i < reward[1]; i++) 
						 {
							 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
						 }
					 }
				 }
			     break;
		    }
	    }
	}

	//Lv.2 Reward's
	public void RedBox(L2PcInstance activeChar, L2Npc npc)
	{
		switch (Rnd.get(6))
		{
		    case 0:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 1:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 2:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 3:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", "Nice, You WON it..."));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 2024, 1, 1, 0));
				 for (int[] reward : Config.LVL_2_REWARD) 
				 {
					 if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					 {
						 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
					 } 
					 else 
					 {
						 for (int i = 0; i < reward[1]; i++) 
						 {
							 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
						 }
					 }
				 }
			     break;
		    }
	    }
	}
	
	//Lv.3 Reward's
	public void GreenBox(L2PcInstance activeChar, L2Npc npc)
	{
		switch (Rnd.get(6))
		{
		    case 0:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 1:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 2:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 3:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", "Nice, You WON it..."));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 2024, 1, 1, 0));
				 for (int[] reward : Config.LVL_3_REWARD) 
				 {
					 if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					 {
						 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
					 } 
					 else 
					 {
						 for (int i = 0; i < reward[1]; i++) 
						 {
							 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
						 }
					 }
				 }
			     break;
		    }
	    }
	}
	
	//Lv.4 Reward's
	public void YellowBox(L2PcInstance activeChar, L2Npc npc)
	{
		switch (Rnd.get(6))
		{
		    case 0:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 1:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 2:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 3:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", "Nice, You WON it..."));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 2024, 1, 1, 0));
				 for (int[] reward : Config.LVL_4_REWARD) 
				 {
					 if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					 {
						 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
					 } 
					 else 
					 {
						 for (int i = 0; i < reward[1]; i++) 
						 {
							 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
						 }
					 }
				 }
			     break;
		    }
	    }
	}
	
	//Lv.5 Reward's
	public void BlackBox(L2PcInstance activeChar, L2Npc npc)
	{
		switch (Rnd.get(6))
		{
		    case 0:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 1:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 2:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 3:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 4:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", BOX_MSG[Rnd.get(5)].replace("$s1", activeChar.getName())));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 347, 1, 1, 0));
				 kill(activeChar);
			     break;
		    }
		    case 5:
		    {
		    	 activeChar.sendPacket(new CreatureSay(npc.getObjectId(), 0, "Chest", "Nice, You WON it..."));
				 npc.broadcastPacket(new MagicSkillUse(this, npc, 2024, 1, 1, 0));
				 for (int[] reward : Config.LVL_5_REWARD) 
				 {
					 if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					 {
						 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
					 } 
					 else 
					 {
						 for (int i = 0; i < reward[1]; i++) 
						 {
							 activeChar.addItem("Event Reward", reward[0], reward[1], activeChar, true);
						 }
					 }
				 }
			     break;
		    }
	    }
	}
	
	//Do not kill GM's
	private static void kill(L2PcInstance activeChar)
	{
		activeChar.stopAllEffects();
		activeChar.reduceCurrentHp(activeChar.getMaxHp() + activeChar.getMaxCp() + 1, activeChar, null);
	}
}