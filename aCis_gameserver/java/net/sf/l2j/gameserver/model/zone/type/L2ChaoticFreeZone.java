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
 * this program. If not, see <[url="http://www.gnu.org/licenses/>."]http://www.gnu.org/licenses/>.[/url]
 */
package net.sf.l2j.gameserver.model.zone.type;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class L2ChaoticFreeZone extends L2SpawnZone
{
	private boolean _checkClasses;
	private static List<String> _classes = new ArrayList<>();
	
	public L2ChaoticFreeZone(int id)
	{
		super(id);

		_checkClasses = false;
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
		else
		{
			super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.FLAG_AREA, true);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
		character.setInsideZone(ZoneId.NO_RESTART, true);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance activeChar = (L2PcInstance) character;

			if (_checkClasses)
			{
				if (_classes != null && _classes.contains(""+ activeChar.getClassId().getId()))
				{
					activeChar.teleToLocation(83597, 147888, -3405, 0);
					activeChar.sendMessage("Your class is not allowed in the chaotic zone.");
					return;
				}
			}
							
			activeChar.updatePvPStatus();
		}
	}

	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.FLAG_AREA, false);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		character.setInsideZone(ZoneId.NO_RESTART, false);
		
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance activeChar = (L2PcInstance) character;
			
			PvpFlagTaskManager.getInstance().add(activeChar, Config.PVP_NORMAL_TIME);
			
			// Set pvp flag
			if (activeChar.getPvpFlag() == 0)
				activeChar.updatePvPFlag(1);
		}
	}

	@Override
	public void onDieInside(L2Character character)
	{

	}

	@Override
	public void onReviveInside(L2Character character)
	{
		
	}
}