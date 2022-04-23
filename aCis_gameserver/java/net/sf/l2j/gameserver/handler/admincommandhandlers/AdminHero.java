package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class AdminHero implements IAdminCommandHandler
{
    protected static final Logger _log = Logger.getLogger(AdminHero.class.getName());
    
    String INSERT_DATA = "REPLACE INTO characters_custom_data (obj_Id, char_name, hero, noble, hero_end_date) VALUES (?,?,?,?,?)";
    String DEL_DATA = "UPDATE characters_custom_data SET hero = 0 WHERE obj_Id=?";
    
    private static String[] ADMIN_COMMANDS = 
	{ 
	    "admin_hero" 
	};

    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_hero"))
        {
            String[] cmdParams = command.split(" ");
            long heroTime = 0L;
            if (cmdParams.length > 1)
            {
              try
              {
                 heroTime = Integer.parseInt(cmdParams[1]) * 24L * 60L * 60L * 1000L;
              }
              catch (NumberFormatException nfe)
              {
                 nfe.printStackTrace();
              }
            }
            
            L2Object target = activeChar.getTarget();
            if ((target instanceof L2PcInstance))
            {
                L2PcInstance targetPlayer = (L2PcInstance)target;
                boolean newHero = !targetPlayer.isHero();
                if (newHero)
                {
                   targetPlayer.setHero(true);
                   targetPlayer.sendMessage("You are now a hero.");
                   updateDatabase(targetPlayer, true, heroTime);
                   sendMessages(true, targetPlayer, activeChar, true, true);
                   targetPlayer.broadcastUserInfo();
                }
                else
                {
                   targetPlayer.setHero(false);
                   targetPlayer.sendMessage("You are no longer a hero.");
                   updateDatabase(targetPlayer, false, 0L);
                   sendMessages(false, targetPlayer, activeChar, true, true);
                   targetPlayer.broadcastUserInfo();
                }
                targetPlayer = null;
             }
             else
             {
                  activeChar.sendMessage("Impossible to set a non Player Target as hero.");
                  _log.info("GM: " + activeChar.getName() + " is trying to set a non Player Target as hero.");
                  return false;
             }
             target = null;
         }
         return true;
    }
  
    private void sendMessages(boolean fornewHero, L2PcInstance player, L2PcInstance gm, boolean announce, boolean notifyGmList)
    {
       if (fornewHero)
       {
           player.sendMessage(gm.getName() + " has granted Hero Status for you!");
           gm.sendMessage("You've granted Hero Status for " + player.getName());
           if (notifyGmList)
           {
             GmListTable.broadcastMessageToGMs("Warn: " + gm.getName() + " has set " + player.getName() + " as Hero!");
           }
       }
       else
       {
          player.sendMessage(gm.getName() + " has revoked Hero Status from you!");
          gm.sendMessage("You've revoked Hero Status from " + player.getName());
          if (notifyGmList)
          {
             GmListTable.broadcastMessageToGMs("Warn: " + gm.getName() + " has removed Hero Status of player" + player.getName());
          }
       }
    }
  
    private void updateDatabase(L2PcInstance player, boolean newHero, long heroTime)
    {
       Connection con = null;
       try
       {
           if (player == null)
               return;
           
           con = L2DatabaseFactory.getInstance().getConnection();
           PreparedStatement stmt = con.prepareStatement(newHero ? INSERT_DATA : DEL_DATA);
           if (newHero)
           {
              stmt.setInt(1, player.getObjectId());
              stmt.setString(2, player.getName());
              stmt.setInt(3, 1);
              stmt.setInt(4, 1);
              stmt.setLong(5, heroTime == 0L ? 0L : System.currentTimeMillis() + heroTime);
              stmt.execute();
              stmt.close();
           }
           else
           {
             stmt.setInt(1, player.getObjectId());
             stmt.execute();
             stmt.close();
           }
        } 
        catch (Exception e)
        {
            _log.log(Level.SEVERE, "Error: could not update database: ", e);
        }
        finally
        {
			try 
			{
				if (con != null)
					con.close();
			}
			catch (Exception e)
			{
				
			}
        }
    }

    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
}