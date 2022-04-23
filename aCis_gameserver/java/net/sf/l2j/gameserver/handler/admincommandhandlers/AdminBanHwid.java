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

import hwid.HwidConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.taskmanager.CloseHwidTask;

public class AdminBanHwid implements IAdminCommandHandler
{
	
	private static String[] _adminCommands =
	{
		"admin_ban_hwid"
	};
	protected static final Logger _log = Logger.getLogger(AdminBanHwid.class.getName());
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
	    if (!HwidConfig.ALLOW_GUARD_SYSTEM)
		{
			activeChar.sendMessage("Hwid system is Disabled.");
			return false;				
		}
	    
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String player = "";
		L2PcInstance targetPlayer = null;
		// One parameter, player name
		if (st.hasMoreTokens())
		{
			player = st.nextToken();
			targetPlayer = L2World.getInstance().getPlayer(player);
		}
		else
		{
			// If there is no name, select target
			if (activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
				targetPlayer = (L2PcInstance) activeChar.getTarget();
		}

		// Can't ban yourself
		if (targetPlayer != null && targetPlayer.equals(activeChar))
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
			return false;
		}
   	
		if (command.startsWith("admin_ban_hwid"))
		{
			String hwid = targetPlayer.getHWid();
			if (hwid != null)
			{
				updateDatabase(targetPlayer);					
				targetPlayer.setAccountAccesslevel(-100);					
				for (L2PcInstance p : L2World.getInstance().getL2Players())
				{
					String hwidz = p.getHWid();

					if (p.isOnline())
					{
						if (hwidz.equals(hwid))
						{
							p.setHwidBlock(true);
							BanHwid(p);					
						}
					}
				}
				activeChar.sendMessage(new StringBuilder().append("HWID : ").append(hwid).append(" Banned").toString());
			}			
		}		
	
		return false;
	}
	
	public static void BanHwid(L2PcInstance player)
	{
		//String name = "Indisponivel";
		String hwidban = player.getHWid();

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT char_name FROM banned_hwid WHERE hwid=?");
			statement.setString(1, hwidban);
			ResultSet rset = statement.executeQuery();

			while (rset.next())
			{
				//name = 
				rset.getString("char_name");

				//String htmFile = "data/html/mods/Banned_Hwid.htm";  	
				//String htmContent = HtmCache.getInstance().getHtm(htmFile);
				//if (htmContent != null)
				//{
				//	NpcHtmlMessage Html = new NpcHtmlMessage(1);
				//	Html.setHtml(htmContent);
				//	Html.replace("%name%", name); 
				//	player.sendPacket(Html);
				//}
			}

			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "Ban_Hwid: " + e.getMessage(), e);
		}

		player.sendPacket(SystemMessageId.HWID_HAS_BEEN_BANNED);
		player.startAbnormalEffect(0x0800);
		player.startAbnormalEffect(AbnormalEffect.ROOT);
		player.setIsImmobilized(true);
		player.setIsParalyzed(true);
		player.setIsInvul(true);
		player.broadcastPacket(new StopMove(player));
		ThreadPoolManager.getInstance().scheduleGeneral(new CloseHwidTask(player), 10 * 1000);
	}

	public static void updateDatabase(L2PcInstance player)
	{
		// prevents any NPE.
		if (player == null)
			return;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stmt = con.prepareStatement(INSERT_DATA);

			stmt.setString(1, player.getName());
			stmt.setString(2, player.getHWid());
			stmt.execute();
			stmt.close();
			stmt = null;
		}
		catch(Exception e)
		{
			e.printStackTrace(); 			
		}
	}

	// Updates That Will be Executed by MySQL
	// ----------------------------------------
	static String INSERT_DATA = "REPLACE INTO banned_hwid (char_name, hwid) VALUES (?,?)";

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}