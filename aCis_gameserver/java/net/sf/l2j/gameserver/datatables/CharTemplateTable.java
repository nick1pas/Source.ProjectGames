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
package net.sf.l2j.gameserver.datatables;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.actor.template.PcTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Unknown, Forsaiken
 */
public class CharTemplateTable
{
	private static final Logger _log = Logger.getLogger(CharTemplateTable.class.getName());
	
	private final Map<Integer, PcTemplate> _templates = new HashMap<>();
	
 	private static final String[] CHAR_CLASSES =
 	{
 			"Human Fighter",
 			"Warrior",
 			"Gladiator",
 			"Warlord",
 			"Human Knight",
 			"Paladin",
 			"Dark Avenger",
 			"Rogue",
 			"Treasure Hunter",
 			"Hawkeye",
 			"Human Mystic",
 			"Human Wizard",
 			"Sorceror",
 			"Necromancer",
 			"Warlock",
 			"Cleric",
 			"Bishop",
 			"Prophet",
 			"Elven Fighter",
 			"Elven Knight",
 			"Temple Knight",
 			"Swordsinger",
 			"Elven Scout",
 			"Plainswalker",
 			"Silver Ranger",
 			"Elven Mystic",
 			"Elven Wizard",
 			"Spellsinger",
 			"Elemental Summoner",
 			"Elven Oracle",
 			"Elven Elder",
 			"Dark Fighter",
 			"Palus Knight",
 			"Shillien Knight",
 			"Bladedancer",
 			"Assassin",
 			"Abyss Walker",
 			"Phantom Ranger",
 			"Dark Elven Mystic",
 			"Dark Elven Wizard",
 			"Spellhowler",
 			"Phantom Summoner",
 			"Shillien Oracle",
 			"Shillien Elder",
 			"Orc Fighter",
 			"Orc Raider",
 			"Destroyer",
 			"Orc Monk",
 			"Tyrant",
 			"Orc Mystic",
 			"Orc Shaman",
 			"Overlord",
 			"Warcryer",
 			"Dwarven Fighter",
 			"Dwarven Scavenger",
 			"Bounty Hunter",
 			"Dwarven Artisan",
 			"Warsmith",
 			"dummyEntry1",
 			"dummyEntry2",
 			"dummyEntry3",
 			"dummyEntry4",
 			"dummyEntry5",
 			"dummyEntry6",
 			"dummyEntry7",
 			"dummyEntry8",
 			"dummyEntry9",
 			"dummyEntry10",
 			"dummyEntry11",
 			"dummyEntry12",
 			"dummyEntry13",
 			"dummyEntry14",
 			"dummyEntry15",
 			"dummyEntry16",
 			"dummyEntry17",
 			"dummyEntry18",
 			"dummyEntry19",
 			"dummyEntry20",
 			"dummyEntry21",
 			"dummyEntry22",
 			"dummyEntry23",
 			"dummyEntry24",
 			"dummyEntry25",
 			"dummyEntry26",
 			"dummyEntry27",
 			"dummyEntry28",
 			"dummyEntry29",
 			"dummyEntry30",
 			"Duelist",
 			"DreadNought",
 			"Phoenix Knight",
 			"Hell Knight",
 			"Sagittarius",
 			"Adventurer",
 			"Archmage",
 			"Soultaker",
 			"Arcana Lord",
 			"Cardinal",
 			"Hierophant",
 			"Eva Templar",
 			"Sword Muse",
 			"Wind Rider",
 			"Moonlight Sentinel",
 			"Mystic Muse",
 			"Elemental Master",
 			"Eva's Saint",
 			"Shillien Templar",
 			"Spectral Dancer",
 			"Ghost Hunter",
 			"Ghost Sentinel",
 			"Storm Screamer",
 			"Spectral Master",
 			"Shillien Saint",
 			"Titan",
 			"Grand Khauatari",
 			"Dominator",
 			"Doomcryer",
 			"Fortune Seeker",
 			"Maestro"
 	};
 	
	public static CharTemplateTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected CharTemplateTable()
	{
		final File mainDir = new File("./data/xml/classes");
		if (!mainDir.isDirectory())
		{
			_log.log(Level.SEVERE, "CharTemplateTable: Main dir " + mainDir.getAbsolutePath() + " hasn't been found.");
			return;
		}
		
		for (final File file : mainDir.listFiles())
		{
			if (file.isFile() && file.getName().endsWith(".xml"))
				loadFileClass(file);
		}
		
		_log.log(Level.INFO, "CharTemplateTable: Loaded " + _templates.size() + " character templates.");
		_log.log(Level.INFO, "CharTemplateTable: Loaded " + SkillTreeTable.getInstance().getSkillTreesSize() + " classes skills trees.");
	}
	
	private void loadFileClass(final File f)
	{
		try
		{
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if ("class".equalsIgnoreCase(d.getNodeName()))
				{
					NamedNodeMap attrs = d.getAttributes();
					StatsSet set = new StatsSet();
					
					final int classId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
					final int parentId = Integer.parseInt(attrs.getNamedItem("parentId").getNodeValue());
					String items = null;
					
					set.set("classId", classId);
					
					for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
					{
						if ("set".equalsIgnoreCase(cd.getNodeName()))
						{
							attrs = cd.getAttributes();
							String name = attrs.getNamedItem("name").getNodeValue().trim();
							String value = attrs.getNamedItem("val").getNodeValue().trim();
							set.set(name, value);
						}
						else if ("skillTrees".equalsIgnoreCase(cd.getNodeName()))
						{
							List<L2SkillLearn> skills = new ArrayList<>();
							for (Node cb = cd.getFirstChild(); cb != null; cb = cb.getNextSibling())
							{
								L2SkillLearn skillLearn = null;
								if ("skill".equalsIgnoreCase(cb.getNodeName()))
								{
									attrs = cb.getAttributes();
									final int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
									final int lvl = Integer.parseInt(attrs.getNamedItem("lvl").getNodeValue());
									final int minLvl = Integer.parseInt(attrs.getNamedItem("minLvl").getNodeValue());
									final int cost = Integer.parseInt(attrs.getNamedItem("sp").getNodeValue());
									skillLearn = new L2SkillLearn(id, lvl, minLvl, cost, 0, 0);
									skills.add(skillLearn);
								}
							}
							SkillTreeTable.getInstance().addSkillsToSkillTrees(skills, classId, parentId);
						}
						else if ("items".equalsIgnoreCase(cd.getNodeName()))
						{
							attrs = cd.getAttributes();
							items = attrs.getNamedItem("val").getNodeValue().trim();
						}
					}
					PcTemplate pcT = new PcTemplate(set);
					
					// Add items listed in "items" if class possess a filled "items" string.
					if (items != null)
					{
						String[] itemsSplit = items.split(";");
						for (String element : itemsSplit)
							pcT.addItem(Integer.parseInt(element));
					}
					
					_templates.put(pcT.getClassId().getId(), pcT);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "CharTemplateTable: Error loading from file: " + f.getName(), e);
		}
	}
	
	public PcTemplate getTemplate(ClassId classId)
	{
		return _templates.get(classId.getId());
	}
	
	public PcTemplate getTemplate(int classId)
	{
		return _templates.get(classId);
	}
	
	public final String getClassNameById(int classId)
	{
		PcTemplate pcTemplate = _templates.get(classId);
		if (pcTemplate == null)
			throw new IllegalArgumentException("No template for classId: " + classId);
		
		return pcTemplate.getClassName();
	}
	
	public static final int getClassIdByName(String className)
	{
		int currId = 1;

		for(String name : CHAR_CLASSES)
		{
			if(name.equalsIgnoreCase(className))
			{
				break;
			}

			currId++;
		}

		return currId;
	}
	
	private static class SingletonHolder
	{
		protected static final CharTemplateTable _instance = new CharTemplateTable();
	}
}