/*

 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Founation, either version 3 of the License, or (at your option) any later
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
package net.sf.l2j.commons.io;

import java.io.FileWriter;
import java.io.IOException;
import net.sf.l2j.gameserver.model.L2TeleportLocation;

public class LocationFileGenerator
{
    private static final String FILE_PATH = "./data/xml/locationsGenerator.txt";
    private L2TeleportLocation loc;

    public LocationFileGenerator(int id, int x, int y, int z)
    {
        loc = new L2TeleportLocation();
        loc.setTeleId(id);
        loc.setLocX(x);
        loc.setLocY(y);
        loc.setLocZ(z);
    }

    public LocationFileGenerator(int id, int x, int y, int z, int price, byte forNoble)
    {
        this(id, x, y, z);
        loc.setPrice(price);
        loc.setIsForNoble(forNoble == 1);
    }
    
    public void insert()
    {
        try (FileWriter writer = new FileWriter(FILE_PATH, true))
        {    
        	int forNoble = loc.getIsForNoble() == true ? 1 : 0;        
            String modelXML = "<teleport id=\""+loc.getTeleId()+"\" loc_x=\""+loc.getLocX()+"\" loc_y=\""+loc.getLocY()+"\" loc_z=\""+loc.getLocZ()+"\" price=\""+loc.getPrice()+"\" fornoble=\""+forNoble+"\"/>\r\n";
            writer.write(modelXML);      
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}