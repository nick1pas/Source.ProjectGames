package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SellList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class L2MurdocInstance extends L2NpcInstance
{
	public L2MurdocInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
    private final int reputation = 150000;
    private final byte level = 8;
    
    // id skills
    private final int[] clanSkills =
    {
        370,
        371,
        372,
        373,
        374,
        375,
        376,
        377,
        378,
        379,
        380,
        381,
        382,
        383,
        384,
        385,
        386,
        387,
        388,
        389,
        390,
        391
    };

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("castleBlacksmith"))
		{
			final L2Clan clan = player.getClan();
			
			for (Siege siege : SiegeManager.getSieges())
			{
				if (siege.isInProgress())
				{
					if (siege.getAttackerClan(clan) != null || siege.getDefenderClan(clan) != null)
					{
						showChatWindow(player);
						player.sendMessage("Your clan is in siege, castle trader is not available.");
						return;
					}
				}
			}
			if (clan == null || !clan.hasCastle())
			{				
				showChatWindow(player);
				player.sendMessage("You are not a member of a guild with castle.");
				return;
			}
			else
				showBlacksmithWindow(player);
		}
		else if (command.startsWith("clanFull"))
		{
	        if (player.isClanLeader())
	        {
	        	if (player.getClan().getLevel() == 8)
	        	{
	        		player.sendMessage("Your clan is already maximum level!");
	        		return;
	        	}
	        	else if (player.getInventory().getInventoryItemCount(9500, 0) >= 50000)
	        	{       
	        		player.destroyItemByItemId("Consume", 9500, 50000, player, true);
	        		player.getClan().changeLevel(level);
	        		player.getClan().addReputationScore(reputation);

	        		for (int s : clanSkills)
	        		{
	        			L2Skill clanSkill = SkillTable.getInstance().getInfo(s, SkillTable.getInstance().getMaxLevel(s));
	        			player.getClan().addNewSkill(clanSkill);
	        		}

	        		player.sendSkillList();
	        		player.getClan().updateClanInDB();            
	        		player.sendMessage("Your clan Level/Skills/Reputation has been updated!");      
	        		player.broadcastUserInfo();

	        		showChatWindow(player);
	        	}
	        	else
	        	{				
	        		player.sendMessage("You dont have required item's!");
	        		return;
	        	}
	        }
	        else
	        {
	            player.sendMessage("You are not the clan leader.");  
	            showChatWindow(player);
	        }
		}
		else if (command.startsWith("rideWyvern"))
		{
			// If player is mounted on a strider
			if (player.isMounted() && (player.getMountNpcId() == 12526 || player.getMountNpcId() == 12527 || player.getMountNpcId() == 12528))
			{
				// Check for strider level
				if (player.getMountLevel() < 55)
				{
					player.sendMessage("To transform a strider into a wyvern, your strider must be at least level 55.");
					showBlacksmithWindow(player);
					return;
				}

				// Check for items consumption
				else if (player.destroyItemByItemId("Wyvern", 9501, 20, player, true))
				{
					player.dismount();
					if (player.mount(12621, 0, true))
					{
						player.sendMessage("You have ridden the wyvern! Please remember to keep it well-fed.");
						return;
					}
				}
				else
				{
					player.sendMessage("To transform a strider into a wyvern, you must have 20 Event Token's.");
					showBlacksmithWindow(player);
					return;
				}
			}
			else
			{
				player.sendMessage("You may only ride a wyvern while you're riding a strider.");
				showBlacksmithWindow(player);
				return;
			}
			showBlacksmithWindow(player);
		}
		//Castle Buffs
	    else if (command.equalsIgnoreCase("castle_fire")) 
	    {
			if (player.getInventory().getInventoryItemCount(9501, 0) >= 50)
			{
				player.destroyItemByItemId("Event", 9501, 1, player, true);
				player.broadcastPacket(new MagicSkillUse(this, player, 1426, 1, 10, 0));

				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(5420, 1));
				SkillTable.getInstance().getInfo(5420,1).getEffects(this, player);
				showBlacksmithWindow(player);
			}
			else
			{
				player.sendMessage("You do not have 50 Event Token's to buff.");
				showBlacksmithWindow(player);
				return;
			}
	    }
	    else if (command.equalsIgnoreCase("castle_water")) 
	    {
			if (player.getInventory().getInventoryItemCount(9501, 0) >= 50)
			{
				player.destroyItemByItemId("Event", 9501, 1, player, true);
				player.broadcastPacket(new MagicSkillUse(this, player, 1426, 1, 10, 0));

				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(5421, 1));
				SkillTable.getInstance().getInfo(5421,1).getEffects(this, player);
				showBlacksmithWindow(player);
			}
			else
			{
				player.sendMessage("You do not have 50 Event Token's to buff.");
				showBlacksmithWindow(player);
				return;
			}
	    }
	    else if (command.equalsIgnoreCase("castle_thunder")) 
	    {
			if (player.getInventory().getInventoryItemCount(9501, 0) >= 50)
			{
				player.destroyItemByItemId("Event", 9501, 1, player, true);
				player.broadcastPacket(new MagicSkillUse(this, player, 1426, 1, 10, 0));

				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(5422, 1));
				SkillTable.getInstance().getInfo(5422,1).getEffects(this, player);
				showBlacksmithWindow(player);
			}
			else
			{
				player.sendMessage("You do not have 50 Event Token's to buff.");
				showBlacksmithWindow(player);
				return;
			}
	    }
		else if (command.equalsIgnoreCase("Sell"))
		{
			player.sendPacket(new SellList(player));
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/mods/blacksmith/blacksmith.htm");
		html.replace("%objectId%", String.valueOf(player.getTargetId()));
		player.sendPacket(html);
    }
	
	private void showBlacksmithWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/mods/blacksmith/blacksmith-1.htm");
		html.replace("%objectId%", String.valueOf(player.getTargetId()));
		player.sendPacket(html);
    }
}