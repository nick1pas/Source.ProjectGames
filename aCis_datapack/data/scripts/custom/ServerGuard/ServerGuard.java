package custom.ServerGuard;

import java.util.Collection;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

/**
 * Serve Guard
 * @author Emerson Gouveia
 * @version 0.0.0.3+ aCis
 */
public class ServerGuard extends Quest
{
	// Enable / Disable service guard
	public static final boolean SERVER_GUARD = true;
	// Start time
	private static final int START_TIME = 60000;
	// End Time
	private static final int RESTART_TIME = 120000;
	
	// Id iten restricion?
	private static final int ITEM_ID_AND_MAX_COUNT[][] =
	{
		{
			9500,
			30000000
		},
		{
			9501,
			2500
		},
		{
			9507,
			15000
		},
		{
			9508,
			25000
		},
		{
			9509,
			12000
		},
	    {
			9556,
			12000
		},
		{
			9552,
			500
		},
		{
			9511,
			600
		},
		{
			9513,
			5
		},
		{
			9555,
			500
		}
	};
	
	//Ban Acoount: 
	public static final boolean BANNED_OR_REMOVE = true;
	
	// Send Message
	public static final boolean MESSAGE = true;
	
	public ServerGuard()
	{
		super(-1, ServerGuard.class.getSimpleName(), "Server Guard");
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new GuardServer(), START_TIME, RESTART_TIME);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("Scanner"))
		{
			System.out.println("Guard Server: Star search.");
			Collection<L2PcInstance> onlinePlayers = L2World.getInstance().getAllPlayers().values();
			for (L2PcInstance players : onlinePlayers)
			{
				ItemInstance item;
				for (int[] element : ITEM_ID_AND_MAX_COUNT)
				{
					item = players.getInventory().getItemByItemId(element[0]);
					if ((item != null) && (item.getCount() >= element[1]))
					{
						if (BANNED_OR_REMOVE)
						{
							if (MESSAGE)
							{
								System.out.println("Guard Server: Banned accaunt [" + players.getAccountName() + "] player name:[" + players.getName() + "] _ Item:[" + item.getName() + "[ Count:[" + item.getCount() + "]");
							}
							players.setAccountAccesslevel(-100);
							players.logout();
						}
						else
						{
							if (MESSAGE)
							{
								System.out.println("Guard Server: Remove item accaunt [" + players.getAccountName() + "]  player name:[" + players.getName() + "] _ Item:[" + item.getName() + "[ Count:[" + item.getCount() + "]");
							}
							players.destroyItemByItemId("GuardServer", element[0], item.getCount(), players, true);
						}
					}
				}
			}
			System.out.println("Guard Server: Finish search. RESTART_TIME: " + ((RESTART_TIME / 1000) / 60) + " minut.");
		}
		return null;
	}
	
	public class GuardServer implements Runnable
	{
		@Override
		public void run()
		{
			QuestManager.getInstance().getQuest("ServerGuard").notifyEvent("Scanner", null, null);
		}
	}
	
	public static void main(String[] arg)
	{
		if (SERVER_GUARD)
		{
			new ServerGuard();
			System.out.println("Guard Server: ON");
		}
		else
		{
			System.out.println("Guard Server: OFF");
		}
	}
}