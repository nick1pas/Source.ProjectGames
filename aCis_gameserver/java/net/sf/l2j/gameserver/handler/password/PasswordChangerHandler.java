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
package net.sf.l2j.gameserver.handler.password;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.l2j.Base64;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.handler.ICustomByPassHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class PasswordChangerHandler implements IVoicedCommandHandler, ICustomByPassHandler
{
	private static String[] VOICED_COMMANDS =
	{
		"password"
	};
	
	private static final String[] _BYPASSCMD = 
	{
		"change_pass"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target) 
	{
		if (command.equalsIgnoreCase("password"))
			showMenu(activeChar);
		
		return true;
	}

	@Override
	public void handleCommand(String command, final L2PcInstance player, String parameters)
	{
		if (parameters.split(" ").length < 3)
		{
			showMenu(player);
			return;
		}

		String curPass = parameters.split(" ")[0];
		String newPass1 = parameters.split(" ")[1];
		String newPass2 = parameters.split(" ")[2];

		changePassword(curPass, newPass1, newPass2, player);
	}

	private boolean changePassword(String currPass, String newPass, String repeatNewPass, L2PcInstance activeChar)
	{
		if (newPass.length() < 5)
		{
			activeChar.sendMessage("The new password is too short!");
			showMenu(activeChar);
			return false;
		}
		if (newPass.length() > 20)
		{
			activeChar.sendMessage("The new password is too long!");
			showMenu(activeChar);
			return false;
		}
		if (!newPass.equals(repeatNewPass))
		{
			activeChar.sendMessage("Repeated password doesn't match the new password.");
			showMenu(activeChar);
			return false;
		}

		Connection con = null;
		String password = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] raw = currPass.getBytes("UTF-8");
			raw = md.digest(raw);
			String currPassEncoded = Base64.encodeBytes(raw);

			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT password FROM accounts WHERE login=?");
			statement.setString(1, activeChar.getAccountName());
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				password = rset.getString("password");
			}
			rset.close();
			statement.close();
			byte[] password2 =
							null;
			if (currPassEncoded.equals(password)) 
			{
				password2 = newPass.getBytes("UTF-8");
				password2 = md.digest(password2);

				PreparedStatement statement2 = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?");
				statement2.setString(1, Base64.encodeBytes(password2));
				statement2.setString(2, activeChar.getAccountName());
				statement2.executeUpdate();
				statement2.close();

				activeChar.sendMessage("Your password has been changed succesfully!");
				activeChar.sendMessage("You will be disconnected for security reasons.");
				waitSecs(5);

				activeChar.deleteMe();
				activeChar.getClient().closeNow();
			} 
			else 
			{
				activeChar.sendMessage("The current password you've inserted is incorrect! Please try again!");
				return password2 != null;
			}
		} 
		catch (Exception e)
		{
			//_log.warning("could not update the password of account: " + activeChar.getAccountName());
		} 
		finally
		{
			try
			{
				if (con != null)
					con.close();
			} 
			catch (SQLException e) 
			{
				//_log.warning("Failed to close database connection!");
			}

		}
		return true;
	}

	private void showMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		String text = HtmCache.getInstance().getHtm("data/html/mods/menu/Change_Password.htm");
		htm.setHtml(text);
		activeChar.sendPacket(htm);
	}

	@Override
	public String[] getByPassCommands() 
	{
		return _BYPASSCMD;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
	
	public static void waitSecs(int i)
	{
		try
		{
			Thread.sleep(i * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
