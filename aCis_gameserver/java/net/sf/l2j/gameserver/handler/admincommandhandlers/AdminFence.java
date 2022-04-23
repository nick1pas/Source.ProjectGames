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

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.datatables.FenceTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.actor.instance.L2FenceInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminFence implements IAdminCommandHandler
{
    private static final String[] ADMIN_COMMANDS =
    {
        "admin_addfence",
        "admin_deletefence",
        "admin_listfence"
    };
   
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
    	if (command.startsWith("admin_addfence"))
    	{
    		StringTokenizer st = new StringTokenizer(command, " ");
    		try
    		{
    			st.nextToken();
    			int type = Integer.parseInt(st.nextToken());
    			int width = Integer.parseInt(st.nextToken());
    			int length = Integer.parseInt(st.nextToken());
    			//int height = 1;
    			
    			//if (st.hasMoreTokens())
    			//	height = Math.min(Integer.parseInt(st.nextToken()), 3);
    			
    			//for (int i = 0;i < height;i++)
    			for (int i = 0;i < 1;i++)
    			{
    				L2FenceInstance fence = new L2FenceInstance(IdFactory.getInstance().getNextId(), type, width, length, activeChar.getX(), activeChar.getY());
    				fence.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
    				FenceTable.addFence(fence);
    			}
    		}
    		catch (Exception e)
    		{
    			activeChar.sendMessage("Usage: //addfence <type> <width> <length> [<height>]");
    		}
    	}
    	else if (command.startsWith("admin_deletefence"))
    	{
    		StringTokenizer st = new StringTokenizer(command, " ");
    		st.nextToken();
    		try
    		{
    			L2Object fence = null;
    			if (activeChar.getTarget() instanceof L2FenceInstance)
    			{
    				fence = activeChar.getTarget();
    			}
    			else if (st.hasMoreTokens())
    			{
    				L2Object object = L2World.getInstance().findObject(Integer.parseInt(st.nextToken()));
    				if (object instanceof L2FenceInstance)
    					fence = object;
    			}

    			if (fence != null)
    			{
    				L2WorldRegion region = fence.getWorldRegion();
    				fence.decayMe();

    				if (region != null)
    					region.removeVisibleObject(fence);

    				fence.getKnownList().removeAllKnownObjects();
    				L2World.getInstance().removeObject(fence);
    				if(fence instanceof L2FenceInstance)
    					FenceTable.removeFence((L2FenceInstance)fence);
    			}
    			else
    				throw new RuntimeException();
    		}
    		catch (Exception e)
    		{
    			activeChar.sendMessage("No fence targeted with shift+click or //deletefence <fence_objectId>");
    		}
    	}
		else if(command.startsWith("admin_listfence"))
			listFences(activeChar);
        return true;
    }
   
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
	
    private static void listFences(L2PcInstance activeChar)
    {
    	StringBuilder tb = new StringBuilder();

    	tb.append("<html><body>Total Fences: " + FenceTable.getAllFences().size() + "<br><br>");
    	for (L2FenceInstance fence : FenceTable.getAllFences())
    		tb.append("<a action=\"bypass -h admin_deletefence " + fence.getObjectId() + " 1\">Fence: " + fence.getObjectId() + " [" + fence.getX() + " " + fence.getY() + " " + fence.getZ() + "]</a><br>");
    	tb.append("</body></html>");

    	NpcHtmlMessage html = new NpcHtmlMessage(0);
    	html.setHtml(tb.toString());
    	activeChar.sendPacket(html);
    }
}