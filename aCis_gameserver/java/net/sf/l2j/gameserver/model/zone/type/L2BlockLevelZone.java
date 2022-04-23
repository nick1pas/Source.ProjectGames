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

import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

public class L2BlockLevelZone extends L2ZoneType
{
	private boolean _checkLevel;
	private int _level;
	
	public L2BlockLevelZone(final int id)
	{
		super(id);
		
		_checkLevel = false;
		_level = 1;
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("checkLevel"))
			_checkLevel = Boolean.parseBoolean(value);
		else if (name.equals("Level"))
			_level = Integer.parseInt(value);
		else
			super.setParameter(name, value);
	}
	@Override
	protected void onEnter(final L2Character character)
	{
		if (character instanceof L2PcInstance)
		{
			final L2PcInstance activeChar = (L2PcInstance) character;

			if (_checkLevel)
			{
				if (activeChar.getLevel() >= _level)
				{
					activeChar.teleToLocation(83597, 147888, -3405, 0);
					activeChar.sendMessage("Your level is not allowed on this Area.");
					return;
				}
			}			
		}
	}
	
	@Override
	protected void onExit(final L2Character character)
	{

	}
	
	@Override
	public void onDieInside(final L2Character character)
	{
	}
	
	@Override
	public void onReviveInside(final L2Character character)
	{
	}
}