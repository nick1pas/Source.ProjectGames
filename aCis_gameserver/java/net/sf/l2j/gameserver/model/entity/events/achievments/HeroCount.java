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
package net.sf.l2j.gameserver.model.entity.events.achievments;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Condition;
import net.sf.l2j.gameserver.templates.StatsSet;
/**
 *
 *
 * @author Avaj
 */
public class HeroCount extends Condition
{
	public HeroCount(Object value)
	{
		super(value);
		setName("Hero Count");
	}

	@Override
	public boolean meetConditionRequirements(L2PcInstance player)
	{
		if (getValue() == null)
			return false;

		int val = Integer.parseInt(getValue().toString());
		for(int hero: Hero.getInstance().getHeroes().keySet())
		{
			if(hero==player.getObjectId())
			{
				StatsSet sts = Hero.getInstance().getHeroes().get(hero);
				if(sts.getString(Olympiad.CHAR_NAME).equals(player.getName()))
				{
					if(sts.getInteger(Hero.COUNT)>=val)
						return true;
				}
			}
		}
		return false;
	}
}