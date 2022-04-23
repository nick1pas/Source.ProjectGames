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
package net.sf.l2j.gameserver.model.zone.type;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class L2ChangePvpZone extends L2SpawnZone
{
	private boolean _isActive = false;
	private String _name;
	private String _annouceName;
	private Location _loc;

	private boolean _checkClasses;
	private static List<String> _classes = new ArrayList<>();
	private boolean _getFlag;
	
	public L2ChangePvpZone(int id)
	{
		super(id);

		_checkClasses = false;
		_getFlag = false;
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("checkClasses"))
			_checkClasses = Boolean.parseBoolean(value);
		else if (name.equals("Classes"))
		{
			String[] propertySplit = value.split(",");
			for (String i : propertySplit)
				 _classes.add(i);
		}
		else if (name.equals("annouceName"))
			_annouceName = value;
		else if (name.equals("getFlag"))
			_getFlag = Boolean.parseBoolean(value);
		else
			super.setParameter(name, value);
	}
	
	public void active(boolean give)
	{
		if (give)
		{
			for (L2Character character : getCharactersInside())
			{
				character.setInsideZone(ZoneId.CHANGE_PVP, true);

				if (character instanceof L2PcInstance)
				{
					final L2PcInstance activeChar = (L2PcInstance) character;

					if (_getFlag)
					{
						PvpFlagTaskManager.getInstance().add(activeChar, Config.PVP_NORMAL_TIME);

						// Set pvp flag
						if (activeChar.getPvpFlag() == 0)
							activeChar.updatePvPFlag(1);
					}

					if (_checkClasses)
					{
						if (_classes != null && _classes.contains(""+ activeChar.getClassId().getId()))
						{
							activeChar.teleToLocation(83597, 147888, -3405, 0);
							activeChar.sendMessage("Your class is not allowed in this zone.");
							return;
						}
					}
				}
			}
		}
		_isActive = give;
	}
	
	public boolean isActive()
	{
		return _isActive;
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		if (isActive())
		{
			character.setInsideZone(ZoneId.CHANGE_PVP, true);
			
			if (character instanceof L2PcInstance)
			{
				final L2PcInstance activeChar = (L2PcInstance) character;
				
				if (_getFlag)
				{
					PvpFlagTaskManager.getInstance().add(activeChar, Config.PVP_NORMAL_TIME);

					// Set pvp flag
					if (activeChar.getPvpFlag() == 0)
						activeChar.updatePvPFlag(1);
				}
				
				if (_checkClasses)
				{
					if (_classes != null && _classes.contains(""+ activeChar.getClassId().getId()))
					{
						activeChar.teleToLocation(83597, 147888, -3405, 0);
						activeChar.sendMessage("Your class is not allowed in this zone.");
						return;
					}
				}
			}
		}
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		if (character instanceof L2PcInstance)
			character.setInsideZone(ZoneId.CHANGE_PVP, false);
	}
	
	@Override
	public void onDieInside(L2Character character)
	{

	}
	
	@Override
	public void onReviveInside(L2Character character)
	{

	}
	
	public void addLoc(Location loc)
	{
		_loc = loc;
	}
	
	public Location getLoc()
	{
		return _loc;
	}

	public void addName(String name)
	{
		_name = name.replaceAll("_", " ");
	}

	public String getName()
	{
		return _name;
	}

	public String getAnnouce()
	{
		return _annouceName;
	}
}