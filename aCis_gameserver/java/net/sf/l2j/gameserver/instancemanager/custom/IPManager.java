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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.util.Util;

public class IPManager
{
    private static final Logger _log = Logger.getLogger(IPManager.class.getName());

    public IPManager()
    {
        _log.log(Level.INFO, "IPManager - Loaded.");
    }

    private boolean multiboxKickTask(L2PcInstance activeChar, Integer numberBox, Collection<L2PcInstance> world)
    {
        Map<String, List<L2PcInstance>> ipMap = new HashMap<String, List<L2PcInstance>>();
        for (L2PcInstance player : world)
        {
            if (player.getClient() == null || player.getClient().isDetached())
                continue;
            else
            {
                String ip = activeChar.getClient().getConnection().getInetAddress().getHostAddress();
                String playerIp = player.getClient().getConnection().getInetAddress().getHostAddress();
                
                if (ip.equals(playerIp))
                {
                    if (ipMap.get(ip) == null)
                        ipMap.put(ip, new ArrayList<L2PcInstance>());
                    
                    ipMap.get(ip).add(player);
                    
                    if (ipMap.get(ip).size() >= numberBox)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean validBox(L2PcInstance activeChar, Integer numberBox, Collection<L2PcInstance> world, Boolean forcedLogOut)
    {
        if (multiboxKickTask(activeChar, numberBox, world))
        {
            if (forcedLogOut)
            {
                L2GameClient client = activeChar.getClient();
                _log.warning("Multibox Protection: " + client.getConnection().getInetAddress().getHostAddress() + " was trying to use over " + numberBox + " clients!");
                Util.handleIllegalPlayerAction(activeChar, "Multibox Protection: " + client.getConnection().getInetAddress().getHostAddress() + " was trying to use over " + numberBox + " clients!", Config.MULTIBOX_PROTECTION_PUNISH);
                activeChar.sendMessage("You break the limit of " + Config.MULTIBOX_PROTECTION_CLIENTS_PER_PC + " box per IP.");
            }
            return true;
        }
        return false;
    }
    
    private static class SingletonHolder
    {
        protected static final IPManager _instance = new IPManager();
    }

    public static final IPManager getInstance()
    {
        return SingletonHolder._instance;
    }
}