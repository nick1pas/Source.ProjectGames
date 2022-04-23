package net.sf.l2j.gameserver.handler.itemhandlers.custom.hero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

public class HeroCoin implements IItemHandler
{
	protected static final Logger _log = Logger.getLogger(HeroCoin.class.getName());
	
	String INSERT_DATA = "REPLACE INTO characters_custom_data (obj_Id, char_name, hero, noble, hero_end_date) VALUES (?,?,?,?,?)";

	//Hero Coin Level - 1
	private final int HERO_ID1 = Config.HERO_COIN_ID1;
	private final int HERO_DAYS1 = Config.HERO_DAYS_ID1;
	
	//Hero Coin Level - 2
	private final int HERO_ID2 = Config.HERO_COIN_ID2;
	private final int HERO_DAYS2 = Config.HERO_DAYS_ID2;
	
	//Hero Coin Level - 3
	private final int HERO_ID3 = Config.HERO_COIN_ID3;
	private final int HERO_DAYS3 = Config.HERO_DAYS_ID3;
	
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;

		L2PcInstance activeChar = (L2PcInstance)playable;

		int itemId = item.getItemId();

		if (itemId == HERO_ID1)
		{
			if(activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
			}

			if(activeChar.isHero())
			{
				activeChar.sendMessage("You are already a hero!");
			}
			else
			{
				activeChar.broadcastPacket(new SocialAction(activeChar, 16));
				activeChar.setHero(true);
				updateDatabase(activeChar, HERO_DAYS1 * 24L * 60L * 60L * 1000L);
				activeChar.sendMessage("You are now a hero, you are granted With hero status, skills, aura.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			}
		}
		
		if (itemId == HERO_ID2)
		{
			if(activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
			}

			if(activeChar.isHero())
			{
				activeChar.sendMessage("You are already a hero!");
			}
			else
			{
				activeChar.broadcastPacket(new SocialAction(activeChar, 16));
				activeChar.setHero(true);
				updateDatabase(activeChar, HERO_DAYS2 * 24L * 60L * 60L * 1000L);
				activeChar.sendMessage("You are now a hero, you are granted With hero status, skills, aura.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			}
		}
		
		if (itemId == HERO_ID3)
		{
			if(activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This item cannot be used on Olympiad Games.");
			}

			if(activeChar.isHero())
			{
				activeChar.sendMessage("You are already a hero!");
			}
			else
			{
				activeChar.broadcastPacket(new SocialAction(activeChar, 16));
				activeChar.setHero(true);
				updateDatabase(activeChar, HERO_DAYS3 * 24L * 60L * 60L * 1000L);
				activeChar.sendMessage("You are now a hero, you are granted With hero status, skills, aura.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			}
		}
	}
	
	private void updateDatabase(L2PcInstance player, long heroTime)
	{
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
			if (player == null)
				return;
			
			PreparedStatement stmt = con.prepareStatement(INSERT_DATA);

			stmt.setInt(1, player.getObjectId());
			stmt.setString(2, player.getName());
			stmt.setInt(3, 1);
			stmt.setInt(4, player.isNoble() ? 1 : 0);
			stmt.setLong(5, heroTime == 0 ? 0 : System.currentTimeMillis() + heroTime);
			stmt.execute();
			stmt.close();
			con.close();
		}
		catch(Exception e)
		{
			_log.log(Level.SEVERE, "Hero Coin Error: could not update database: ", e);
			e.printStackTrace();
		}
	}
}