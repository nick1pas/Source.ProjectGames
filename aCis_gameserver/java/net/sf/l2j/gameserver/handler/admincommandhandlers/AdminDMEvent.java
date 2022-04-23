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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMConfig;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMEvent;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMEventTeleporter;
import net.sf.l2j.gameserver.model.entity.events.deathmatch.DMManager;

public class AdminDMEvent implements IAdminCommandHandler
{
    private static final String[] ADMIN_COMMANDS =
    {
        "admin_dm_add",
        "admin_dm_remove",
        "admin_dm_advance"
    };
   
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_dm_add"))
        {
            L2Object target = activeChar.getTarget();
           
            if (!(target instanceof L2PcInstance))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            add(activeChar, (L2PcInstance) target);
        }
        else if (command.startsWith("admin_dm_remove"))
        {
            L2Object target = activeChar.getTarget();
           
            if (!(target instanceof L2PcInstance))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            remove(activeChar, (L2PcInstance) target);
        }
        else if (command.startsWith( "admin_dm_advance" ))
        {
            DMManager.getInstance().skipDelay();
        }
       
        return true;
    }
   
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
   
    private void add(L2PcInstance activeChar, L2PcInstance playerInstance)
    {
        if (DMEvent.isPlayerParticipant(playerInstance.getObjectId()))
        {
            activeChar.sendMessage("Player already participated in the event!");
            return;
        }
       
        if (!DMEvent.addParticipant(playerInstance))
        {
            activeChar.sendMessage("Player instance could not be added, it seems to be null!");
            return;
        }
       
        if (DMEvent.isStarted())
        {
        	new DMEventTeleporter(playerInstance, true, false);
        }
    }
   
    private void remove(L2PcInstance activeChar, L2PcInstance playerInstance)
    {
        if (!DMEvent.removeParticipant(playerInstance))
        {
            activeChar.sendMessage("Player is not part of the event!");
            return;
        }
       
        new DMEventTeleporter(playerInstance, DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES, true, true);
    }
}