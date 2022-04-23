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

import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.zone.L2SpawnZone;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;

public class L2InvasionZone extends L2SpawnZone
{
	private static boolean _isNoRestart;
	private static boolean _isNoStore;
	private static boolean _isNoSummonFriend;
	private static List<Integer> _restrictedItems = new ArrayList<>();

	public L2InvasionZone(int id)
	{
		super(id);

		_isNoRestart = false;
		_isNoStore = false;
		_isNoSummonFriend = false;
	}

	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("isNoRestart"))
			_isNoRestart = Boolean.parseBoolean(value);
		else if (name.equals("isNoStore"))
			_isNoStore = Boolean.parseBoolean(value);
		else if (name.equals("isNoSummonFriend"))
			_isNoSummonFriend = Boolean.parseBoolean(value);
		else if (name.equals("restrictedItems"))
		{
			String[] property = value.split(",");
			for (String itemId : property)
				_restrictedItems.add(Integer.parseInt(itemId));
		}
		else
			super.setParameter(name, value);
	}

	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.INVASION, true);

		if (_isNoRestart)
			character.setInsideZone(ZoneId.NO_RESTART, true);

		if (_isNoStore)
			character.setInsideZone(ZoneId.NO_STORE, true);

		if (_isNoSummonFriend)
			character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);

		if (character instanceof L2PcInstance || character instanceof L2PetInstance)
		{
			checkItemRestriction(character);
		}
	}

	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.INVASION, false);

		if (_isNoRestart)
			character.setInsideZone(ZoneId.NO_RESTART, false);

		if (_isNoStore)
			character.setInsideZone(ZoneId.NO_STORE, false);

		if (_isNoSummonFriend)
			character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
	}

	@Override
	public void onDieInside(L2Character character)
	{

	}

	@Override
	public void onReviveInside(L2Character character)
	{

	}

	private static void checkItemRestriction(L2Character character)
	{
		for (ItemInstance item : character.getInventory().getPaperdollItems())
		{
			if (item == null || !isRestrictedItem(item.getItemId()))
				continue;

			character.getInventory().unEquipItemInSlot(item.getLocationSlot());
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(item);
			character.sendPacket(iu);
		}
	}
	
	public static boolean isRestrictedItem(int itemId)
	{
		return _restrictedItems.contains(itemId);
	}
}