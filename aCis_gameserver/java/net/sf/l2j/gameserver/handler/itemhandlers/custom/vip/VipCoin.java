package net.sf.l2j.gameserver.handler.itemhandlers.custom.vip;

import java.util.Calendar;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;

public class VipCoin implements IItemHandler
{
	//Hero Coin Level - 1
	private final int VIP_ID1 = Config.VIP_COIN_ID1;
	private final int VIP_DAYS1 = Config.VIP_DAYS_ID1;
	
	//Hero Coin Level - 2
	private final int VIP_ID2 = Config.VIP_COIN_ID2;
	private final int VIP_DAYS2 = Config.VIP_DAYS_ID2;
	
	//Hero Coin Level - 3
	private final int VIP_ID3 = Config.VIP_COIN_ID3;
	private final int VIP_DAYS3 = Config.VIP_DAYS_ID3;
	
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance)playable;

		int itemId = item.getItemId();

		if (itemId == VIP_ID1)
		{
			if (activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
				return;
			}
			if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				if (activeChar.isVip())
				{
					long daysleft = (activeChar.getVipEndTime() - Calendar.getInstance().getTimeInMillis()) / 86400000L;
					activeChar.setEndTime("vip", (int)(daysleft + VIP_DAYS1));
					activeChar.sendMessage("Congratulations, You just received another " + VIP_DAYS1 + " day of VIP.");
				}
				else
				{
					activeChar.setVip(true);
					activeChar.setEndTime("vip", VIP_DAYS1);
					activeChar.sendMessage("Congrats, you just became VIP per " + VIP_DAYS1 + " day.");
				}

				if (Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
					activeChar.getAppearance().setNameColor(Config.VIP_NCOLOR);

				if (Config.ALLOW_VIP_TCOLOR && activeChar.isVip()) 
					activeChar.getAppearance().setTitleColor(Config.VIP_TCOLOR);

				activeChar.broadcastUserInfo();
				activeChar.sendPacket(new EtcStatusUpdate(activeChar));
			}
		}
		
		if (itemId == VIP_ID2)
		{
			if (activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
				return;
			}
			if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				if (activeChar.isVip())
				{
					long daysleft = (activeChar.getVipEndTime() - Calendar.getInstance().getTimeInMillis()) / 86400000L;
					activeChar.setEndTime("vip", (int)(daysleft + VIP_DAYS2));
					activeChar.sendMessage("Congratulations, You just received another " + VIP_DAYS2 + " day of VIP.");
				}
				else
				{
					activeChar.setVip(true);
					activeChar.setEndTime("vip", VIP_DAYS2);
					activeChar.sendMessage("Congrats, you just became VIP per " + VIP_DAYS2 + " day.");
				}

				if (Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
					activeChar.getAppearance().setNameColor(Config.VIP_NCOLOR);

				if (Config.ALLOW_VIP_TCOLOR && activeChar.isVip()) 
					activeChar.getAppearance().setTitleColor(Config.VIP_TCOLOR);

				activeChar.broadcastUserInfo();
				activeChar.sendPacket(new EtcStatusUpdate(activeChar));
			}
		}
		
		if (itemId == VIP_ID3)
		{
			if (activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
				return;
			}
			if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				if (activeChar.isVip())
				{
					long daysleft = (activeChar.getVipEndTime() - Calendar.getInstance().getTimeInMillis()) / 86400000L;
					activeChar.setEndTime("vip", (int)(daysleft + VIP_DAYS3));
					activeChar.sendMessage("Congratulations, You just received another " + VIP_DAYS3 + " day of VIP.");
				}
				else
				{
					activeChar.setVip(true);
					activeChar.setEndTime("vip", VIP_DAYS3);
					activeChar.sendMessage("Congrats, you just became VIP per " + VIP_DAYS3 + " day.");
				}

				if (Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
					activeChar.getAppearance().setNameColor(Config.VIP_NCOLOR);

				if (Config.ALLOW_VIP_TCOLOR && activeChar.isVip()) 
					activeChar.getAppearance().setTitleColor(Config.VIP_TCOLOR);

				activeChar.broadcastUserInfo();
				activeChar.sendPacket(new EtcStatusUpdate(activeChar));
			}
		}
	}
}
