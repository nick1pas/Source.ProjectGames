/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

/**
 * Give/remove aio status
 * and changes name and title color if enabled
 * 
 * Uses:
 * setaio [<player_name>] [<time_duration in days>]
 *
 * If <player_name> is not specified, the current target player is used.
 *
 * @author KhayrusS && SweeTs
 *
 */
public class AdminAio implements IAdminCommandHandler
{
	private static String[] _adminCommands = 
	{ 
		"admin_setaio", 
		"admin_removeaio" 
	};
	
	private final static Logger _log = Logger.getLogger(AdminAio.class.getName());

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		
		if (command.startsWith("admin_setaio"))
		{
			if (target != null && target instanceof L2PcInstance)
				player = (L2PcInstance)target;
			else
				player = activeChar;
			
			try
			{
				st.nextToken();
				String time = st.nextToken();
				
				if (st.hasMoreTokens())
				{
					String playername = time;
					time = st.nextToken();
					player = L2World.getInstance().getPlayer(playername);
					doAio(activeChar, player, playername, time);
				}
				else
				{
					String playername = player.getName();
					doAio(activeChar, player, playername, time);
				}
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Usage: //setaio <char_name> [time](in days)");
			}
		}
		else if(command.startsWith("admin_removeaio"))
		{
			if (target != null && target instanceof L2PcInstance)
				player = (L2PcInstance)target;
			else
				player = activeChar;
			
			try
			{
				st.nextToken();
				
				if (st.hasMoreTokens())
				{
					String playername = st.nextToken();
					player = L2World.getInstance().getPlayer(playername);
					removeAio(activeChar, player, playername);
				}
				else
				{
					String playername = player.getName();
					removeAio(activeChar, player, playername);
				}
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Usage: //removeaio <char_name>");
			}
		}
		return true;
	}

	public void doAio(L2PcInstance activeChar, L2PcInstance player, String playername, String time)
	{
		int days = Integer.parseInt(time);
		
		if (player == null)
		{
			activeChar.sendMessage("Character not found.");
			return;
		}
		if (player.isAio())
		{
			activeChar.sendMessage("Player " + playername + " is already an AIO.");
			return;
		}
		if (days > 0)
		{
			player.getStat().addExp(player.getStat().getExpForLevel(81));
			player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 100, 0));
			player.setAio(true);
			player.setEndTime("aio", days);
			player.sendPacket(new CreatureSay(0,Say2.HERO_VOICE,"System","Dear player, you are now an AIO, congratulations."));
			
			try (Connection con = L2DatabaseFactory.getInstance().getConnection())
			{
				PreparedStatement statement = con.prepareStatement("UPDATE characters SET aio=1, aio_end=? WHERE obj_id=?");
				statement.setLong(1, player.getAioEndTime());
				statement.setInt(2, player.getObjectId());
				statement.execute();
				statement.close();

				if(Config.ALLOW_AIO_NCOLOR && player.isAio())
					player.getAppearance().setNameColor(Config.AIO_NCOLOR);

				if(Config.ALLOW_AIO_TCOLOR && player.isAio())
					player.getAppearance().setTitleColor(Config.AIO_TCOLOR);

				player.removeSkills();
				player.rewardAioSkills();
				
				if(Config.ALLOW_AIO_ITEM && player.isAio())
				{
					player.getInventory().addItem("", Config.AIO_ITEMID, 1, player, null);
					player.getInventory().equipItem(player.getInventory().getItemByItemId(Config.AIO_ITEMID));
					
				}
				
				player.broadcastUserInfo();
				player.sendSkillList();
				
				GmListTable.broadcastMessageToGMs("GM "+ activeChar.getName()+ " set an AIO status for player "+ playername + " for " + time + " day(s)");
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING,"Something went wrong, check log folder for details", e);
			}
		}
	}

	public void removeAio(L2PcInstance activeChar, L2PcInstance player, String playername)
	{
		if (!player.isAio())
		{
			activeChar.sendMessage("Player " + playername + " is not an AIO.");
			return;
		}
		
		player.setAio(false);
		player.setAioEndTime(0);

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET Aio=0, Aio_end=0 WHERE obj_id=?");
			statement.setInt(1, player.getObjectId());
			statement.execute();
			statement.close();

			player.removeSkills();
			player.removeExpAndSp(6299994999L, 366666666);
			
			if (Config.ALLOW_AIO_ITEM && activeChar.isAio() == false)
			{
				player.getInventory().destroyItemByItemId("", Config.AIO_ITEMID, 1, player, null);
				player.getWarehouse().destroyItemByItemId("", Config.AIO_ITEMID, 1, player, null);
			}
			
			player.getAppearance().setNameColor(0xFFFFFF);
			player.getAppearance().setTitleColor(0xFFFFFF);
			player.broadcastUserInfo();
			player.sendSkillList();
			
			GmListTable.broadcastMessageToGMs("GM "+activeChar.getName()+" removed an AIO status of player " + playername);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING,"Something went wrong, check log folder for details", e);
		}
	}

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}