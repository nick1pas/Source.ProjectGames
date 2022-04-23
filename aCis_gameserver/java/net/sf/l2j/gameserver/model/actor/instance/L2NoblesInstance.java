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

import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

public class L2NoblesInstance extends L2NpcInstance
{
	private static final int SILVER_SHARD = 9502;
	private static final int GOLDEN_SHARD = 9503;
	private static final int SACRED_FEATHER = 9504;
	private static final int NOBLESSE_TIARA = 7694;
	
	public L2NoblesInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("becomeNoblesse"))
		{
			if (player.isNoble())
			{
				player.sendMessage("You already got Noblesse status.");
				return;
			}
			else if (player.getPvpKills() <= 100)
			{
				player.sendMessage("Your Pvp's should be greater than or equal to 100.");
				return;
			}
			else if (player.getInventory().getInventoryItemCount(SILVER_SHARD, 0) >= 200 && player.getInventory().getInventoryItemCount(GOLDEN_SHARD, 0) >= 100 && player.getInventory().getInventoryItemCount(SACRED_FEATHER, 0) >= 1)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				player.sendMessage("Congratulations, you are now a Noblesse!");
				
				player.destroyItemByItemId("Consume", SILVER_SHARD, 200, player, true);
				player.destroyItemByItemId("Consume", GOLDEN_SHARD, 100, player, true);
				player.destroyItemByItemId("Consume", SACRED_FEATHER, 1, player, true);
				player.addItem("Loot", NOBLESSE_TIARA, 1, player, true);
				player.setNoble(true, true);
				player.broadcastPacket(new SocialAction(player, 16));
				player.sendPacket(html);
			}
			else
			{				
				player.sendMessage("You dont have required item's!");
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
		html.setFile("data/html/mods/nobless/noblesse.htm");
		html.replace("%objectId%", String.valueOf(player.getTargetId()));
		player.sendPacket(html);
    }
}