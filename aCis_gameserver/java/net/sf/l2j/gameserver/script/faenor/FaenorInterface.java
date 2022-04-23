/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.script.faenor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.datatables.AnnouncementTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.script.DateRange;
import net.sf.l2j.gameserver.script.EngineInterface;
import net.sf.l2j.gameserver.script.EventDroplist;

/**
 * @author Luis Arias
 */
public class FaenorInterface implements EngineInterface
{
	protected static final Logger _log = Logger.getLogger(FaenorInterface.class.getName());

	public static FaenorInterface getInstance()
	{
		return SingletonHolder._instance;
	}

	private FaenorInterface()
	{
		
	}

	public List<?> getAllPlayers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds a new Quest Drop to an NPC
	 * 
	 * @see com.l2jfrozen.gameserver.script.EngineInterface#addQuestDrop(int, int, int, int, int, String, String[])
	 */
	@Override
	public void addQuestDrop(int npcID, int itemID, int min, int max, int chance, String questID, String[] states)
	{
		NpcTemplate npc = NpcTable.getInstance().getTemplate(npcID);
		if(npc == null)
		{
			_log.info("FeanorInterface: Npc "+npcID+" is null..");
			return;
		}
		DropData drop = new DropData();
		drop.setItemId(itemID);
		drop.setMinDrop(min);
		drop.setMaxDrop(max);
		drop.setChance(chance);
		drop.setQuestID(questID);
		drop.addStates(states);
		addDrop(npc, drop, false);
	}

	/**
	 * Adds a new Drop to an NPC
	 * @param npcID 
	 * @param itemID 
	 * @param min 
	 * @param max 
	 * @param sweep 
	 * @param chance 
	 * @throws NullPointerException 
	 * 
	 * @see com.l2jfrozen.gameserver.script.EngineInterface#addQuestDrop(int, int, int, int, int, String, String[])
	 */
	public void addDrop(int npcID, int itemID, int min, int max, boolean sweep, int chance) throws NullPointerException
	{
		NpcTemplate npc = NpcTable.getInstance().getTemplate(npcID);
		if(npc == null)
		{
			_log.warning("Npc doesnt Exist");
			throw new NullPointerException();
		}
		DropData drop = new DropData();
		drop.setItemId(itemID);
		drop.setMinDrop(min);
		drop.setMaxDrop(max);
		drop.setChance(chance);

		addDrop(npc, drop, sweep);
	}
	
	/**
	 * Adds a new drop to an NPC. If the drop is sweep, it adds it to the NPC's Sweep category If the drop is non-sweep,
	 * it creates a new category for this drop.
	 * 
	 * @param npc
	 * @param drop
	 * @param sweep
	 */
	public void addDrop(NpcTemplate npc, DropData drop, boolean sweep)
	{
		if(sweep)
		{
			addDrop(npc, drop, -1);
		}
		else
		{
			int maxCategory = -1;

			if(npc.getDropData() != null)
			{
				for(DropCategory cat : npc.getDropData())
				{
					if(maxCategory < cat.getCategoryType())
					{
						maxCategory = cat.getCategoryType();
					}
				}
			}
			maxCategory++;
			npc.addDropData(drop, maxCategory);
		}

	}

	/**
	 * Adds a new drop to an NPC, in the specified category. If the category does not exist, it is created.
	 * 
	 * @param npc
	 * @param drop
	 * @param category 
	 */
	public void addDrop(NpcTemplate npc, DropData drop, int category)
	{
		npc.addDropData(drop, category);
	}

	/**
	 * @param npcID 
	 * @return Returns the _questDrops.
	 */
	public List<DropData> getQuestDrops(int npcID)
	{
		NpcTemplate npc = NpcTable.getInstance().getTemplate(npcID);
		if(npc == null)
			return null;
		List<DropData> questDrops = new ArrayList<DropData>();
		if(npc.getDropData() != null)
		{
			for(DropCategory cat : npc.getDropData())
			{
				for(DropData drop : cat.getAllDrops())
				{
					if(drop.getQuestID() != null)
					{
						questDrops.add(drop);
					}
				}
			}
		}
		return questDrops;
	}

	@Override
	public void addEventDrop(int[] items, int[] count, double chance, DateRange range)
	{
		EventDroplist.getInstance().addGlobalDrop(items, count, (int) (chance * DropData.MAX_CHANCE), range);
	}

	@Override
	public void onPlayerLogin(String[] message, DateRange validDateRange)
	{
		AnnouncementTable.getInstance().addEventAnnouncement(validDateRange, message);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final FaenorInterface _instance = new FaenorInterface();
	}
}
