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
package net.sf.l2j.gameserver.datatables.custom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SkipTable
{
	private static final Logger _log = Logger.getLogger(SkipTable.class.getName());
	
	private static final List<Integer> _skip = new ArrayList<>();
	
	public static SkipTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public SkipTable()
	{
		load();
	}
	
	private static void load()
	{
		try
		{
			File f = new File("./data/xml/skipping_items.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("item"))
				{
					int itemId = Integer.parseInt(d.getAttributes().getNamedItem("id").getNodeValue());
					_skip.add(itemId);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("SkipTable: Error parsing SkipItems.xml " + e);
		}
		
		_log.info("SkipTable: Loaded " + _skip.size() + " skipping item(s).");
	}
	
	public void reload()
	{
		_skip.clear();
		load();
	}
	
	public static boolean isSkipped(int itemId)
	{
		return _skip.contains(itemId);
	}
	
	private static class SingletonHolder
	{
		protected static final SkipTable _instance = new SkipTable();
	}
}