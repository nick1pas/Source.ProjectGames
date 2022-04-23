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

public class L2AioShopInstance extends L2NpcInstance
{
	public L2AioShopInstance(int objectId, NpcTemplate template)
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
		String filename = "data/html/mods/aioshop/start.htm";

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
		if (command.startsWith("add_aio"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();

			String priceId = null, priceCount = null, time = null;
			int aioPriceId = 0, aioPriceCount = 0, aioTime = 0;

			if (st.hasMoreTokens())
			{
				priceId = st.nextToken();
				priceCount = st.nextToken();
				time = st.nextToken();

				try
				{
					aioPriceId = Integer.parseInt(priceId);
					aioPriceCount = Integer.parseInt(priceCount);
					aioTime = Integer.parseInt(time);
				}
				catch(NumberFormatException e) 
				{

				}
			}
			else
			{
				_log.warning("Could not update aio status of player " + player.getName());
				return;
			}

			makeAioCharacter(player, aioPriceId, aioPriceCount, aioTime);
		}
		else if (command.startsWith("remove_aio"))
			removeAio(player);

		showMessageWindow(player);
	}

	public void makeAioCharacter(L2PcInstance player, int itemId, int itemCount, int aioTime)
	{
		ItemInstance itemInstance = player.getInventory().getItemByItemId(itemId);

		if (itemInstance == null || !itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < (itemCount))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			return;
		}
		else if (itemInstance.isStackable())
		{
			if (!player.destroyItemByItemId("Aio", itemId, itemCount, player.getTarget(), true))
			{
				player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
				return;
			}
		}
		else
			for (int i = 0; i < (itemCount); i++)
				player.destroyItemByItemId("Aio", itemId, 1, player.getTarget(), true);

		doAio(player, aioTime);
	}

	public void doAio(L2PcInstance player, int days)
	{
		if(player == null)
			return;

		if (player.isAio())
		{
			player.sendMessage("You are already an AIO.");
			return;
		}

		int daysLeft = player.getAioEndTime() <= 0 ? 0 : (int) ((player.getAioEndTime() - System.currentTimeMillis()) / 86400000);
		player.setAio(true);
		player.setEndTime("aio", days + daysLeft);

		player.getStat().addExp(player.getStat().getExpForLevel(81));
		player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 100, 0));

		if(Config.ALLOW_AIO_NCOLOR && player.isAio())
			player.getAppearance().setNameColor(Config.AIO_NCOLOR);

		if(Config.ALLOW_AIO_TCOLOR && player.isAio())
			player.getAppearance().setTitleColor(Config.AIO_TCOLOR);

		player.removeSkills();
		player.rewardAioSkills();

		/* Give Aio Dual */
		if(Config.ALLOW_AIO_ITEM && player.isAio())
		{
			player.getInventory().addItem("", Config.AIO_ITEMID, 1, player, null);
			player.getInventory().equipItem(player.getInventory().getItemByItemId(Config.AIO_ITEMID));
		}

		player.broadcastUserInfo();
		player.sendSkillList();

		player.sendMessage("You are now an Aio, Congratulations!");
	}

	public void removeAio(L2PcInstance player)
	{
		if(!player.isAio())
		{
			player.sendMessage("You are not an AIO.");
			return;
		}

		player.setAio(false);
		player.setAioEndTime(0);

		player.removeSkills();
		player.removeExpAndSp(6299994999L, 366666666);

		if (Config.ALLOW_AIO_ITEM && player.isAio() == false)
		{
			player.getInventory().destroyItemByItemId("", Config.AIO_ITEMID, 1, player, null);
			player.getWarehouse().destroyItemByItemId("", Config.AIO_ITEMID, 1, player, null);
		}

		player.getAppearance().setNameColor(0xFFFFFF);
		player.getAppearance().setTitleColor(0xFFFFFF);
		player.broadcastUserInfo();
		player.sendSkillList();

		player.sendMessage("Now You are not an Aio...");
	}
}