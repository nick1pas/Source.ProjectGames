package net.sf.l2j.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.util.Rnd;

public class L2VipShopInstance extends L2NpcInstance
{
	public L2VipShopInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onAction(L2PcInstance player)
	{
		if (this != player.getTarget()) 
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel() - getLevel()));
			player.sendPacket(new ValidateLocation(this));
		}
		else if (isInsideRadius(player, INTERACTION_DISTANCE, false, false)) 
		{
			SocialAction sa = new SocialAction(this, Rnd.get(8));
			broadcastPacket(sa);
			player.setCurrentFolkNPC(this);
			showMessageWindow(player);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else 
		{
			player.getAI().setIntention(CtrlIntention.INTERACT, this);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}

	private void showMessageWindow(L2PcInstance player)
	{
		String filename = "data/html/mods/vipshop/start.htm";

		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
		filename = null;
		html = null;
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("add_vip"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();

			String priceId = null, priceCount = null, time = null;
			int vipPriceId = 0, vipPriceCount = 0, vipTime = 0;

			if (st.hasMoreTokens())
			{
				priceId = st.nextToken();
				priceCount = st.nextToken();
				time = st.nextToken();

				try
				{
					vipPriceId = Integer.parseInt(priceId);
					vipPriceCount = Integer.parseInt(priceCount);
					vipTime = Integer.parseInt(time);
				}
				catch(NumberFormatException e) 
				{

				}
			}
			else
			{
				_log.warning("Could not update VIP status of player " + player.getName());
				return;
			}

			makeVipCharacter(player, vipPriceId, vipPriceCount, vipTime);
		}
		else if (command.startsWith("remove_vip"))
			removeVip(player);

		showMessageWindow(player);
	}

	public void makeVipCharacter(L2PcInstance player, int itemId, int itemCount, int vipTime)
	{
		ItemInstance itemInstance = player.getInventory().getItemByItemId(itemId);

		if (itemInstance == null || !itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < (itemCount))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			return;
		}
		else if (itemInstance.isStackable())
		{
			if (!player.destroyItemByItemId("Vip", itemId, itemCount, player.getTarget(), true))
			{
				player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
				return;
			}
		}
		else
			for (int i = 0; i < (itemCount); i++)
				player.destroyItemByItemId("Vip", itemId, 1, player.getTarget(), true);

		doVip(player, vipTime);
	}

	public void doVip(L2PcInstance player, int days)
	{
		if(player == null)
			return;

		if (player.isVip())
		{
			player.sendMessage("You are already an VIP.");
			return;
		}

		int daysLeft = player.getVipEndTime() <= 0 ? 0 : (int) ((player.getVipEndTime() - System.currentTimeMillis()) / 86400000);
		player.setVip(true);
		player.setEndTime("vip", days + daysLeft);

		player.getStat().addExp(player.getStat().getExpForLevel(81));
		player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 100, 0));

		if(Config.ALLOW_VIP_NCOLOR && player.isVip())
			player.getAppearance().setNameColor(Config.VIP_NCOLOR);

		if(Config.ALLOW_VIP_TCOLOR && player.isVip())
			player.getAppearance().setTitleColor(Config.VIP_TCOLOR);

		player.rewardVipSkills();

		if(Config.ALLOW_VIP_ITEM && player.isVip())
		{
			player.getInventory().addItem("", Config.VIP_ITEMID, 1, player, null);
			player.getInventory().equipItem(player.getInventory().getItemByItemId(Config.VIP_ITEMID));
		}

		player.broadcastUserInfo();
		player.sendSkillList();

		player.sendMessage("You are now an Vip, Congratulations!");
	}

	public void removeVip(L2PcInstance player)
	{
		if(!player.isVip())
		{
			player.sendMessage("You are not an Vip.");
			return;
		}

		player.setVip(false);
		player.setVipEndTime(0);

		if (Config.ALLOW_VIP_ITEM && player.isVip() == false)
		{
			player.getInventory().destroyItemByItemId("", Config.VIP_ITEMID, 1, player, null);
			player.getWarehouse().destroyItemByItemId("", Config.VIP_ITEMID, 1, player, null);
		}

		player.getAppearance().setNameColor(0xFFFFFF);
		player.getAppearance().setTitleColor(0xFFFFFF);
		player.broadcastUserInfo();
		player.sendSkillList();

		player.sendMessage("Now You are not an Vip...");
	}
}