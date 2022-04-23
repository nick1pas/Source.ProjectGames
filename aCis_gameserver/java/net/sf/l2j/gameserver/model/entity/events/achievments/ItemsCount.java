package net.sf.l2j.gameserver.model.entity.events.achievments;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Condition;

/**
 * @author Matim
 * @version 1.0
 * <br><br>
 * Condition: ItemsCount
 * Check if player has proper ammount of item in inventory.
 */
public class ItemsCount extends Condition
{
	public ItemsCount(Object value)
	{
		super(value);
		setName("Items Count");
	}

	@Override
	public boolean meetConditionRequirements(L2PcInstance player)
	{
		if (getValue() == null)
			return false;

		String s = getValue().toString();
		StringTokenizer st = new StringTokenizer(s, ",");
		int id = 0;
		int ammount = 0;

		try
		{
			id = Integer.parseInt(st.nextToken());
			ammount = Integer.parseInt(st.nextToken());
			if (player.getInventory().getInventoryItemCount(id, 0) >= ammount)
				return true;
		}
		catch (NumberFormatException nfe)
		{
			nfe.printStackTrace();
		}
		return false;
	}
}