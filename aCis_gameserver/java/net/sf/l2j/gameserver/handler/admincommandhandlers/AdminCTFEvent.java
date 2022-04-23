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
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFConfig;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEvent;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFEventTeleporter;
import net.sf.l2j.gameserver.model.entity.events.capturetheflag.CTFManager;

public class AdminCTFEvent implements IAdminCommandHandler
{
    private static final String[] ADMIN_COMMANDS =
    {
        "admin_ctf_add",
        "admin_ctf_remove",
        "admin_ctf_advance"
    };
   
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_ctf_add"))
        {
            L2Object target = activeChar.getTarget();
           
            if (!(target instanceof L2PcInstance))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            add(activeChar, (L2PcInstance) target);
        }
        else if (command.startsWith("admin_ctf_remove"))
        {
            L2Object target = activeChar.getTarget();
           
            if (!(target instanceof L2PcInstance))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            remove(activeChar, (L2PcInstance) target);
        }
        else if (command.startsWith( "admin_ctf_advance" ))
        {
            CTFManager.getInstance().skipDelay();
        }
       
        return true;
    }
   
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
   
    private void add(L2PcInstance activeChar, L2PcInstance playerInstance)
    {
        if (CTFEvent.isPlayerParticipant(playerInstance.getObjectId()))
        {
            activeChar.sendMessage("Player already participated in the event!");
            return;
        }
       
        if (!CTFEvent.addParticipant(playerInstance))
        {
            activeChar.sendMessage("Player instance could not be added, it seems to be null!");
            return;
        }
       
        if (CTFEvent.isStarted())
        {
            new CTFEventTeleporter(playerInstance, CTFEvent.getParticipantTeamCoordinates(playerInstance.getObjectId()), true, false);
        }
    }
   
    private void remove(L2PcInstance activeChar, L2PcInstance playerInstance)
    {
        if (!CTFEvent.removeParticipant(playerInstance.getObjectId()))
        {
            activeChar.sendMessage("Player is not part of the event!");
            return;
        }
       
        new CTFEventTeleporter(playerInstance, CTFConfig.CTF_EVENT_PARTICIPATION_NPC_COORDINATES, true, true);
    }
}