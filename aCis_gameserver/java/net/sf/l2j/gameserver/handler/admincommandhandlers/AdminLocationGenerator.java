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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import net.sf.l2j.commons.io.LocationFileGenerator;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class AdminLocationGenerator implements IAdminCommandHandler
{
    private static final String ADMIN_COMMANDS[] = {"admin_newloc"};

    @Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.startsWith("admin_newloc"))
        {
            StringTokenizer st = new StringTokenizer(command);
            st.nextToken();
            LocationFileGenerator generator = null;
          
            int x = 0;
            int y = 0;
            int z = 0;
            int id = 0;

            try
            {
                if (st.countTokens() == 1)
                {
                    id = Integer.parseInt(st.nextToken());
                    x = activeChar.getX();
                    y = activeChar.getY();
                    z = activeChar.getZ();
                    generator = new LocationFileGenerator(id, x, y, z);
                }
                else if (st.countTokens() == 6)
                {
                    id = Integer.parseInt(st.nextToken());
                    x = Integer.parseInt(st.nextToken());
                    y = Integer.parseInt(st.nextToken());
                    z = Integer.parseInt(st.nextToken());
                    int price = Integer.parseInt(st.nextToken());
                    byte forNoble = Byte.parseByte(st.nextToken());                    
                    generator = new LocationFileGenerator(id, x, y, z, price, forNoble);
                }
                else
                {
                    activeChar.sendMessage("Automatic = //newloc ID CURRENT POSITION(price is 0 | forNoble is 0)");
                    activeChar.sendMessage("Manually = //newloc ID x y z price fornoble");
                }
                            
                if (generator != null)
                {
                    generator.insert();
                    activeChar.sendMessage("Location Generator insert in File.");
                }
            }
            catch(NumberFormatException e)
            {
                activeChar.sendMessage("Only numerical values");
            }
            catch(NoSuchElementException e)
            {
                activeChar.sendMessage("Invalid amount of elements");
            }
        }    
        return true;
    }

    @Override
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
}