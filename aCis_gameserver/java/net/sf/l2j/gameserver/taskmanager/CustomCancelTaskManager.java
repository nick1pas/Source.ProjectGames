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
package net.sf.l2j.gameserver.taskmanager;

import java.util.HashMap;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;

public class CustomCancelTaskManager implements Runnable
{
	private L2PcInstance _player = null;
	private HashMap<L2Skill, int[]> _buffs = null;

	public CustomCancelTaskManager(L2PcInstance _player, HashMap<L2Skill, int[]> _buffs)
	{
		this._player = _player;
		this._buffs = _buffs;
	}

	@Override
	public void run()
	{
		if (_player == null || !_player.isOnline())
			return;

		for (L2Skill s : _buffs.keySet())
		{
			if (s == null)
				continue;

			Env env = new Env();
			env._character = _player;
			env._target = _player;
			env._skill = s;
			L2Effect ef;
			for (EffectTemplate et : s.getEffectTemplates())
			{
				ef = et.getEffect(env);
				if (ef != null)
				{
					ef.setCount(_buffs.get(s)[0]);
					ef.setFirstTime(_buffs.get(s)[1]);
					ef.scheduleEffect();
				}
			}
		}
	}
}