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
package net.sf.l2j.gameserver.instancemanager.custom;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.util.Rnd;

public class ZergManager
{
    private static final Logger _log = Logger.getLogger(ZergManager.class.getName());

    public ZergManager()
    {
        _log.log(Level.INFO, "Anti-ZergManager - Loaded.");
    }

    private boolean checkClanAreaKickTask(L2PcInstance activeChar, Integer numberBox)
    {
        Map<String, List<L2PcInstance>> zergMap = new HashMap<String, List<L2PcInstance>>();
        
        L2Clan clan = activeChar.getClan();
        
        if (clan != null)
        {
        	for (L2PcInstance player : clan.getOnlineMembers())
        	{
        		if (!player.isInsideZone(ZoneId.NO_ZERG) || player.getClan() == null)
        			continue;
        		
        		else
        		{
        			String zerg1 = activeChar.getClan().getName();
        			String zerg2 = player.getClan().getName();

        			if (zerg1.equals(zerg2))
        			{
        				if (zergMap.get(zerg1) == null)
        					zergMap.put(zerg1, new ArrayList<L2PcInstance>());

        				zergMap.get(zerg1).add(player);

        				if (zergMap.get(zerg1).size() > numberBox)
        					return true;
        			}
        		}
        	}
        }
        return false;
    }
    
    private boolean checkAllyAreaKickTask(L2PcInstance activeChar, Integer numberBox, Collection<L2PcInstance> world)
    {
    	Map<String, List<L2PcInstance>> zergMap = new HashMap<String, List<L2PcInstance>>();

    	for (L2PcInstance player : world)
    	{
			if (!player.isInsideZone(ZoneId.NO_ZERG) || player.getAllyId() == 0)
				continue;

    		else
    		{
    			String zerg1 = activeChar.getClan().getAllyName();
    			String zerg2 = player.getClan().getAllyName();

    			if (zerg1.equals(zerg2))
    			{
    				if (zergMap.get(zerg1) == null)
    					zergMap.put(zerg1, new ArrayList<L2PcInstance>());

    				zergMap.get(zerg1).add(player);

    				if (zergMap.get(zerg1).size() > numberBox)
    					return true;
    			}
    		}
    	}
    	return false;
    }

    public boolean checkClanArea(L2PcInstance activeChar, Integer numberBox, Boolean forcedTeleport)
    {
        if (checkClanAreaKickTask(activeChar, numberBox))
        {
            if (forcedTeleport)
            {
            	activeChar.sendPacket(new ExShowScreenMessage("Allowed only " + numberBox + " clans members on this area!", 6 * 1000));
            	RandomTeleport(activeChar);
            }
            return true;
        }
        return false;
    }
    
    public boolean checkAllyArea(L2PcInstance activeChar, Integer numberBox, Collection<L2PcInstance> world, Boolean forcedTeleport)
    {
        if (checkAllyAreaKickTask(activeChar, numberBox, world))
        {
            if (forcedTeleport)
            {
            	activeChar.sendPacket(new ExShowScreenMessage("Allowed only " + numberBox + " ally members on this area!", 6 * 1000));
            	RandomTeleport(activeChar);
            }
            return true;
        }
        return false;
    }

    //Giran Coord's
	public void RandomTeleport(L2PcInstance activeChar)
	{
		switch (Rnd.get(5))
		{
		    case 0:
		    {
		    	int x = 82533 + Rnd.get(100);
		    	int y = 149122 + Rnd.get(100);
		    	activeChar.teleToLocation(x, y, -3474, 0);
		    	break;
		    }
		    case 1:
		    {
		    	int x = 82571 + Rnd.get(100);
		    	int y = 148060 + Rnd.get(100);
		    	activeChar.teleToLocation(x, y, -3467, 0);
		    	break;
		    }
		    case 2:
		    {
		    	int x = 81376 + Rnd.get(100);
		    	int y = 148042 + Rnd.get(100);
		    	activeChar.teleToLocation(x, y, -3474, 0);
		    	break;
		    }
		    case 3:
		    {
		    	int x = 81359 + Rnd.get(100);
		    	int y = 149218 + Rnd.get(100);
		    	activeChar.teleToLocation(x, y, -3474, 0);
		    	break;
		    }
		    case 4:
		    {
		    	int x = 82862 + Rnd.get(100);
		    	int y = 148606 + Rnd.get(100);
		    	activeChar.teleToLocation(x, y, -3474, 0);
		    	break;
		    }
	    }
	}
	
    private static class SingletonHolder
    {
        protected static final ZergManager _instance = new ZergManager();
    }

    public static final ZergManager getInstance()
    {
        return SingletonHolder._instance;
    }
}