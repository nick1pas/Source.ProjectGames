package net.sf.l2j.gameserver.model.entity.events.tournaments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.entity.events.tournaments.properties.ArenaConfig;
import net.sf.l2j.gameserver.model.entity.events.tournaments.properties.ArenaTask;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.util.Rnd;

public class Arena5x5 implements Runnable
{
	// list of participants
	public static List<Pair> registered;
	// number of Arenas
	int free = ArenaConfig.ARENA_EVENT_COUNT_5X5;
	// Arenas
	Arena[] arenas = new Arena[ArenaConfig.ARENA_EVENT_COUNT_5X5];
	// list of fights going on
	Map<Integer, String> fights = new HashMap<>(ArenaConfig.ARENA_EVENT_COUNT_5X5);

	public Arena5x5()
	{
		registered = new ArrayList<>();
		int[] coord;
		for (int i = 0; i < ArenaConfig.ARENA_EVENT_COUNT_5X5; i++)
		{
			coord = ArenaConfig.ARENA_EVENT_LOCS_5X5[i];
			arenas[i] = new Arena(i, coord[0], coord[1], coord[2]);
		}
	}

	public static Arena5x5 getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	public boolean register(L2PcInstance player, L2PcInstance assist, L2PcInstance assist2, L2PcInstance assist3, L2PcInstance assist4)
	{
		for (Pair p : registered)
		{
			if (p.getLeader() == player || p.getAssist() == player)
			{
				player.sendMessage("Tournament: You already registered!");
				return false;
			}
			else if (p.getLeader() == assist || p.getAssist() == assist)
			{
				player.sendMessage("Tournament: " + assist.getName() + " already registered!");
				return false;
			}
			else if (p.getLeader() == assist2 || p.getAssist2() == assist2)
			{
				player.sendMessage("Tournament: " + assist2.getName() + " already registered!");
				return false;
			}
			else if (p.getLeader() == assist3 || p.getAssist3() == assist3)
			{
				player.sendMessage("Tournament: " + assist3.getName() + " already registered!");
				return false;
			}
			else if (p.getLeader() == assist4 || p.getAssist4() == assist4)
			{
				player.sendMessage("Tournament: " + assist4.getName() + " already registered!");
				return false;
			}
		}
		return registered.add(new Pair(player, assist, assist2, assist3, assist4));
	}

	public boolean isRegistered(L2PcInstance player)
	{
		for (Pair p : registered)
		{
			if (p.getLeader() == player || p.getAssist() == player || p.getAssist2() == player || p.getAssist3() == player || p.getAssist4() == player)
			{
				return true;
			}
		}
		return false;
	}

	public Map<Integer, String> getFights()
	{
		return fights;
	}

	public boolean remove(L2PcInstance player)
	{
		for (Pair p : registered)
		{
			if (p.getLeader() == player || p.getAssist() == player || p.getAssist2() == player || p.getAssist3() == player || p.getAssist4() == player)
			{
				p.removeMessage();
				registered.remove(p);
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized void run()
	{
		boolean load = true;

		// while server is running
		while (load)
		{
			if (!ArenaTask.is_started())
				load = false;

			// if no have participants or arenas are busy wait 1 minute
			if (registered.size() < 2 || free == 0)
			{
				try
				{
					Thread.sleep(ArenaConfig.ARENA_CALL_INTERVAL * 1000);
				}
				catch (InterruptedException e)
				{
				}
				continue;
			}
			List<Pair> opponents = selectOpponents();
			if (opponents != null && opponents.size() == 2)
			{
				Thread T = new Thread(new EvtArenaTask(opponents));
				T.setDaemon(true);
				T.start();
			}
			// wait 1 minute for not stress server
			try
			{
				Thread.sleep(ArenaConfig.ARENA_CALL_INTERVAL * 1000);
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	private List<Pair> selectOpponents()
	{
		List<Pair> opponents = new ArrayList<>();
		Pair pairOne = null, pairTwo = null;
		int tries = 3;
		do
		{
			int first = 0, second = 0;
			if (getRegisteredCount() < 2)
				return opponents;

			if (pairOne == null)
			{
				first = Rnd.get(getRegisteredCount());
				pairOne = registered.get(first);
				if (pairOne.check())
				{
					opponents.add(0, pairOne);
					registered.remove(first);
				}
				else
				{
					pairOne = null;
					registered.remove(first);
					return null;
				}

			}
			if (pairTwo == null)
			{
				second = Rnd.get(getRegisteredCount());
				pairTwo = registered.get(second);
				if (pairTwo.check())
				{
					opponents.add(1, pairTwo);
					registered.remove(second);
				}
				else
				{
					pairTwo = null;
					registered.remove(second);
					return null;
				}

			}
		}
		while ((pairOne == null || pairTwo == null) && --tries > 0);
		return opponents;
	}

	public void clear()
	{
		registered.clear();
	}

	public int getRegisteredCount()
	{
		return registered.size();
	}

	private class Pair
	{
		private L2PcInstance leader, assist, assist2, assist3, assist4;

		public Pair(L2PcInstance leader, L2PcInstance assist, L2PcInstance assist2, L2PcInstance assist3, L2PcInstance assist4)
		{
			this.leader = leader;
			this.assist = assist;
			this.assist2 = assist2;
			this.assist3 = assist3;
			this.assist4 = assist4;
		}

		public L2PcInstance getAssist()
		{
			return assist;
		}

		public L2PcInstance getAssist2()
		{
			return assist2;
		}

		public L2PcInstance getAssist3()
		{
			return assist3;
		}

		public L2PcInstance getAssist4()
		{
			return assist4;
		}

		public L2PcInstance getLeader()
		{
			return leader;
		}

		public boolean check()
		{
			if ((leader == null || !leader.isOnline()))
			{
				if (assist != null || assist.isOnline())
					assist.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist2 != null || assist2.isOnline())
					assist2.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist3 != null || assist3.isOnline())
					assist3.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist4 != null || assist4.isOnline())
					assist4.sendMessage("Tournament: You participation in Event was Canceled.");

				return false;
			}
			
			else if (((assist == null || !assist.isOnline()) || (assist2 == null || !assist2.isOnline()) || (assist3 == null || !assist3.isOnline()) || (assist4 == null || !assist4.isOnline()) && (leader != null || leader.isOnline())))
			{
				leader.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist != null || assist.isOnline())
					assist.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist2 != null || assist2.isOnline())
					assist2.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist3 != null || assist3.isOnline())
					assist3.sendMessage("Tournament: You participation in Event was Canceled.");

				if (assist4 != null || assist4.isOnline())
					assist4.sendMessage("Tournament: You participation in Event was Canceled.");

				return false;
			}
			return true;
		}

		public boolean isDead()
		{
			if (ArenaConfig.ARENA_PROTECT)
			{
				if (leader != null && leader.isOnline() && leader.isArenaAttack() && !leader.isDead() && !leader.isInsideZone(ZoneId.ARENA_EVENT))
					leader.logout();
				if (assist != null && assist.isOnline() && assist.isArenaAttack() && !assist.isDead() && !assist.isInsideZone(ZoneId.ARENA_EVENT))
					assist.logout();
				if (assist2 != null && assist2.isOnline() && assist2.isArenaAttack() && !assist2.isDead() && !assist2.isInsideZone(ZoneId.ARENA_EVENT))
					assist2.logout();
				if (assist3 != null && assist3.isOnline() && assist3.isArenaAttack() && !assist3.isDead() && !assist3.isInsideZone(ZoneId.ARENA_EVENT))
					assist3.logout();
				if (assist4 != null && assist4.isOnline() && assist4.isArenaAttack() && !assist4.isDead() && !assist4.isInsideZone(ZoneId.ARENA_EVENT))
					assist4.logout();
			}

			if ((leader == null || leader.isDead() || !leader.isOnline() || !leader.isInsideZone(ZoneId.ARENA_EVENT) || !leader.isArenaAttack()) && (assist == null || assist.isDead() || !assist.isOnline() || !assist.isInsideZone(ZoneId.ARENA_EVENT) || !assist.isArenaAttack()) && (assist2 == null || assist2.isDead() || !assist2.isOnline() || !assist2.isInsideZone(ZoneId.ARENA_EVENT) || !assist2.isArenaAttack()) && (assist3 == null || assist3.isDead() || !assist3.isOnline() || !assist3.isInsideZone(ZoneId.ARENA_EVENT) || !assist3.isArenaAttack()) && (assist4 == null || assist4.isDead() || !assist4.isOnline() || !assist4.isInsideZone(ZoneId.ARENA_EVENT)))
				return false;

			return !(leader.isDead() && assist.isDead() && assist2.isDead() && assist3.isDead() && assist4.isDead());
		}

		public boolean isAlive()
		{
			if ((leader == null || leader.isDead() || !leader.isOnline() || !leader.isInsideZone(ZoneId.ARENA_EVENT) || !leader.isArenaAttack()) && (assist == null || assist.isDead() || !assist.isOnline() || !assist.isInsideZone(ZoneId.ARENA_EVENT) || !assist.isArenaAttack()) && (assist2 == null || assist2.isDead() || !assist2.isOnline() || !assist2.isInsideZone(ZoneId.ARENA_EVENT) || !assist2.isArenaAttack()) && (assist3 == null || assist3.isDead() || !assist3.isOnline() || !assist3.isInsideZone(ZoneId.ARENA_EVENT) || !assist3.isArenaAttack()) && (assist4 == null || assist4.isDead() || !assist4.isOnline() || !assist4.isInsideZone(ZoneId.ARENA_EVENT)))
				return false;

			return !(leader.isDead() && assist.isDead() && assist2.isDead() && assist3.isDead() && assist4.isDead());
		}

		public void teleportTo(int x, int y, int z)
		{
			if (leader != null && leader.isOnline())
			{
				leader.getAppearance().setInvisible();
				leader.setCurrentCp(leader.getMaxCp());
				leader.setCurrentHp(leader.getMaxHp());
				leader.setCurrentMp(leader.getMaxMp());

				/*
				if (leader.isInObserverMode())
				{
					leader.setLastCords(x, y, z);
					leader.leaveOlympiadObserverMode();
				}
				 */
				if (!leader.isInJail())
					leader.teleToLocation(x, y, z, 0);

				leader.broadcastUserInfo();

			}
			if (assist != null && assist.isOnline())
			{
				assist.getAppearance().setInvisible();
				assist.setCurrentCp(assist.getMaxCp());
				assist.setCurrentHp(assist.getMaxHp());
				assist.setCurrentMp(assist.getMaxMp());

				/*
				if (assist.isInObserverMode())
				{
					assist.setLastCords(x, y + 200, z);
					assist.leaveOlympiadObserverMode();
				}
				 */
				if (!assist.isInJail())
					assist.teleToLocation(x, y + 200, z, 0);

				assist.broadcastUserInfo();
			}
			if (assist2 != null && assist2.isOnline())
			{
				assist2.getAppearance().setInvisible();
				assist2.setCurrentCp(assist2.getMaxCp());
				assist2.setCurrentHp(assist2.getMaxHp());
				assist2.setCurrentMp(assist2.getMaxMp());

				/*
				if (assist2.isInObserverMode())
				{
					assist2.setLastCords(x, y + 150, z);
					assist2.leaveOlympiadObserverMode();
				}
				 */
				if (!assist2.isInJail())
					assist2.teleToLocation(x, y + 150, z, 0);

				assist2.broadcastUserInfo();
			}
			if (assist3 != null && assist3.isOnline())
			{
				assist3.getAppearance().setInvisible();
				assist3.setCurrentCp(assist3.getMaxCp());
				assist3.setCurrentHp(assist3.getMaxHp());
				assist3.setCurrentMp(assist3.getMaxMp());

				/*
				if (assist3.isInObserverMode())
				{
					assist3.setLastCords(x, y + 100, z);
					assist3.leaveOlympiadObserverMode();
				}
				 */
				if (!assist3.isInJail())
					assist3.teleToLocation(x, y + 100, z, 0);

				assist3.broadcastUserInfo();
			}
			if (assist4 != null && assist4.isOnline())
			{
				assist4.getAppearance().setInvisible();
				assist4.setCurrentCp(assist4.getMaxCp());
				assist4.setCurrentHp(assist4.getMaxHp());
				assist4.setCurrentMp(assist4.getMaxMp());

				/*
				if (assist4.isInObserverMode())
				{
					assist4.setLastCords(x, y + 50, z);
					assist4.leaveOlympiadObserverMode();
				}
				 */
				if (!assist4.isInJail())
					assist4.teleToLocation(x, y + 50, z, 0);

				assist4.broadcastUserInfo();
			}
		}

		public void EventTitle(String title, String color)
		{
			if (leader != null && leader.isOnline())
			{
				leader.setTitle(title);
				leader.getAppearance().setTitleColor(Integer.decode("0x" + color));
				leader.broadcastUserInfo();
				leader.broadcastTitleInfo();
			}

			if (assist != null && assist.isOnline())
			{
				assist.setTitle(title);
				assist.getAppearance().setTitleColor(Integer.decode("0x" + color));
				assist.broadcastUserInfo();
				assist.broadcastTitleInfo();
			}
			if (assist2 != null && assist2.isOnline())
			{
				assist2.setTitle(title);
				assist2.getAppearance().setTitleColor(Integer.decode("0x" + color));
				assist2.broadcastUserInfo();
				assist2.broadcastTitleInfo();
			}
			if (assist3 != null && assist3.isOnline())
			{
				assist3.setTitle(title);
				assist3.getAppearance().setTitleColor(Integer.decode("0x" + color));
				assist3.broadcastUserInfo();
				assist3.broadcastTitleInfo();
			}
			if (assist4 != null && assist4.isOnline())
			{
				assist4.setTitle(title);
				assist4.getAppearance().setTitleColor(Integer.decode("0x" + color));
				assist4.broadcastUserInfo();
				assist4.broadcastTitleInfo();
			}
		}

		public void saveTitle()
		{
			if (leader != null && leader.isOnline())
			{
				leader._originalTitleColorTournament = leader.getAppearance().getTitleColor();
				leader._originalTitleTournament = leader.getTitle();
			}

			if (assist != null && assist.isOnline())
			{
				assist._originalTitleColorTournament = assist.getAppearance().getTitleColor();
				assist._originalTitleTournament = assist.getTitle();
			}

			if (assist2 != null && assist2.isOnline())
			{
				assist2._originalTitleColorTournament = assist2.getAppearance().getTitleColor();
				assist2._originalTitleTournament = assist2.getTitle();
			}

			if (assist3 != null && assist3.isOnline())
			{
				assist3._originalTitleColorTournament = assist3.getAppearance().getTitleColor();
				assist3._originalTitleTournament = assist3.getTitle();
			}

			if (assist4 != null && assist4.isOnline())
			{
				assist4._originalTitleColorTournament = assist4.getAppearance().getTitleColor();
				assist4._originalTitleTournament = assist4.getTitle();
			}
		}

		public void backTitle()
		{
			if (leader != null && leader.isOnline())
			{
				leader.setTitle(leader._originalTitleTournament);
				leader.getAppearance().setTitleColor(leader._originalTitleColorTournament);
				leader.broadcastUserInfo();
				leader.broadcastTitleInfo();
			}

			if (assist != null && assist.isOnline())
			{
				assist.setTitle(assist._originalTitleTournament);
				assist.getAppearance().setTitleColor(assist._originalTitleColorTournament);
				assist.broadcastUserInfo();
				assist.broadcastTitleInfo();
			}

			if (assist2 != null && assist2.isOnline())
			{
				assist2.setTitle(assist2._originalTitleTournament);
				assist2.getAppearance().setTitleColor(assist2._originalTitleColorTournament);
				assist2.broadcastUserInfo();
				assist2.broadcastTitleInfo();
			}

			if (assist3 != null && assist3.isOnline())
			{
				assist3.setTitle(assist3._originalTitleTournament);
				assist3.getAppearance().setTitleColor(assist3._originalTitleColorTournament);
				assist3.broadcastUserInfo();
				assist3.broadcastTitleInfo();
			}

			if (assist4 != null && assist4.isOnline())
			{
				assist4.setTitle(assist4._originalTitleTournament);
				assist4.getAppearance().setTitleColor(assist4._originalTitleColorTournament);
				assist4.broadcastUserInfo();
				assist4.broadcastTitleInfo();
			}
		}

		public void rewards()
		{
			if (leader != null && leader.isOnline())
			{
				if (leader.isVip())
					leader.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, leader, true);
				else
					leader.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5, leader, true);
			}

			if (assist != null && assist.isOnline())
			{
				if (assist.isVip())
					assist.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist, true);
				else
					assist.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5, assist, true);
			}

			if (assist2 != null && assist2.isOnline())
			{
				if (assist2.isVip())
					assist2.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist2, true);
				else
					assist2.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5, assist2, true);
			}

			if (assist3 != null && assist3.isOnline())
			{
				if (assist3.isVip())
					assist3.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist3, true);
				else
					assist3.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5, assist3, true);
			}

			if (assist4 != null && assist4.isOnline())
			{
				if (assist4.isVip())
					assist4.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist4, true);
				else
					assist4.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_WIN_REWARD_COUNT_5X5, assist4, true);
			}

			sendPacket("Congratulations, your team won the event!", 5);
		}

		public void rewardsLost()
		{
			if (leader != null && leader.isOnline())
			{
				if (leader.isVip())
					leader.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, leader, true);
				else
					leader.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5, leader, true);
			}

			if (assist != null && assist.isOnline())
			{
				if (assist.isVip())
					assist.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist, true);
				else
					assist.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5, assist, true);
			}
			if (assist2 != null && assist2.isOnline())
			{
				if (assist2.isVip())
					assist2.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist2, true);
				else
					assist2.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5, assist2, true);
			}
			if (assist3 != null && assist3.isOnline())
			{
				if (assist3.isVip())
					assist3.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist3, true);
				else
					assist3.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5, assist3, true);
			}

			if (assist4 != null && assist4.isOnline())
			{
				if (assist4.isVip())
					assist4.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5 * Config.VIP_DROP_RATE, assist4, true);
				else
					assist4.addItem("Arena_Event", ArenaConfig.ARENA_REWARD_ID, ArenaConfig.ARENA_LOST_REWARD_COUNT_5X5, assist4, true);
			}

			sendPacket("your team lost the event! =(", 5);
		}

		public void setInTournamentEvent(boolean val)
		{
			if (leader != null && leader.isOnline())
				leader.setInArenaEvent(val);

			if (assist != null && assist.isOnline())
				assist.setInArenaEvent(val);

			if (assist2 != null && assist2.isOnline())
				assist2.setInArenaEvent(val);

			if (assist3 != null && assist3.isOnline())
				assist3.setInArenaEvent(val);

			if (assist4 != null && assist4.isOnline())
				assist4.setInArenaEvent(val);
		}

		public void removeMessage()
		{
			if (leader != null && leader.isOnline())
			{
				leader.sendMessage("Tournament: Your participation has been removed.");
				leader.setArenaProtection(false);
				leader.setArena5x5(false);
			}

			if (assist != null && assist.isOnline())
			{
				assist.sendMessage("Tournament: Your participation has been removed.");
				assist.setArenaProtection(false);
				assist.setArena5x5(false);
			}

			if (assist2 != null && assist2.isOnline())
			{
				assist2.sendMessage("Tournament: Your participation has been removed.");
				assist2.setArenaProtection(false);
				assist2.setArena5x5(false);
			}

			if (assist3 != null && assist3.isOnline())
			{
				assist3.sendMessage("Tournament: Your participation has been removed.");
				assist3.setArenaProtection(false);
				assist3.setArena5x5(false);
			}

			if (assist4 != null && assist4.isOnline())
			{
				assist4.sendMessage("Tournament: Your participation has been removed.");
				assist4.setArenaProtection(false);
				assist4.setArena5x5(false);
			}
		}

		public void setArenaProtection(boolean val)
		{
			if (leader != null && leader.isOnline())
			{
				leader.setArenaProtection(val);
				leader.setArena5x5(val);
			}

			if (assist != null && assist.isOnline())
			{
				assist.setArenaProtection(val);
				assist.setArena5x5(val);
			}
			if (assist2 != null && assist2.isOnline())
			{
				assist2.setArenaProtection(val);
				assist2.setArena5x5(val);
			}

			if (assist3 != null && assist3.isOnline())
			{
				assist3.setArenaProtection(val);
				assist3.setArena5x5(val);
			}

			if (assist4 != null && assist4.isOnline())
			{
				assist4.setArenaProtection(val);
				assist4.setArena5x5(val);
			}
		}

		public void revive()
		{
			if (leader != null && leader.isOnline() && leader.isDead())
				leader.doRevive();

			if (assist != null && assist.isOnline() && assist.isDead())
				assist.doRevive();

			if (assist2 != null && assist2.isOnline() && assist2.isDead())
				assist2.doRevive();

			if (assist3 != null && assist3.isOnline() && assist3.isDead())
				assist3.doRevive();

			if (assist4 != null && assist4.isOnline() && assist4.isDead())
				assist4.doRevive();
		}

		public void setImobilised(boolean val)
		{
			if (leader != null && leader.isOnline())
			{
				leader.setIsInvul(val);
				leader.setStopArena(val);
			}
			if (assist != null && assist.isOnline())
			{
				assist.setIsInvul(val);
				assist.setStopArena(val);
			}
			if (assist2 != null && assist2.isOnline())
			{
				assist2.setIsInvul(val);
				assist2.setStopArena(val);
			}
			if (assist3 != null && assist3.isOnline())
			{
				assist3.setIsInvul(val);
				assist3.setStopArena(val);
			}
			if (assist4 != null && assist4.isOnline())
			{
				assist4.setIsInvul(val);
				assist4.setStopArena(val);
			}
		}

		public void setArenaAttack(boolean val)
		{
			if (leader != null && leader.isOnline())
			{
				leader.setArenaAttack(val);
				leader.broadcastUserInfo();
			}

			if (assist != null && assist.isOnline())
			{
				assist.setArenaAttack(val);
				assist.broadcastUserInfo();
			}

			if (assist2 != null && assist2.isOnline())
			{
				assist2.setArenaAttack(val);
				assist2.broadcastUserInfo();
			}

			if (assist3 != null && assist3.isOnline())
			{
				assist3.setArenaAttack(val);
				assist3.broadcastUserInfo();
			}

			if (assist4 != null && assist4.isOnline())
			{
				assist4.setArenaAttack(val);
				assist4.broadcastUserInfo();
			}
		}

		public void removePet()
		{
			if (leader != null && leader.isOnline())
			{
				// Remove Summon's buffs
				if (leader.getPet() != null)
				{
					L2Summon summon = leader.getPet();
					if (summon != null)
						summon.unSummon(summon.getOwner());

					if (summon instanceof L2PetInstance)
						summon.unSummon(leader);
				}

				if (leader.getMountType() == 1 || leader.getMountType() == 2)
					leader.dismount();
			}

			if (assist != null && assist.isOnline())
			{
				// Remove Summon's buffs
				if (assist.getPet() != null)
				{
					L2Summon summon = assist.getPet();
					if (summon != null)
						summon.unSummon(summon.getOwner());

					if (summon instanceof L2PetInstance)
						summon.unSummon(assist);
				}

				if (assist.getMountType() == 1 || assist.getMountType() == 2)
					assist.dismount();

			}

			if (assist2 != null && assist2.isOnline())
			{
				// Remove Summon's buffs
				if (assist2.getPet() != null)
				{
					L2Summon summon = assist2.getPet();
					if (summon != null)
						summon.unSummon(summon.getOwner());

					if (summon instanceof L2PetInstance)
						summon.unSummon(assist2);

				}

				if (assist2.getMountType() == 1 || assist2.getMountType() == 2)
					assist2.dismount();

			}

			if (assist3 != null && assist3.isOnline())
			{
				// Remove Summon's buffs
				if (assist3.getPet() != null)
				{
					L2Summon summon = assist3.getPet();
					if (summon != null)
						summon.unSummon(summon.getOwner());

					if (summon instanceof L2PetInstance)
						summon.unSummon(assist3);
				}

				if (assist3.getMountType() == 1 || assist3.getMountType() == 2)
					assist3.dismount();

			}

			if (assist4 != null && assist4.isOnline())
			{
				// Remove Summon's buffs
				if (assist4.getPet() != null)
				{
					L2Summon summon = assist4.getPet();
					if (summon != null)
						summon.unSummon(summon.getOwner());

					if (summon instanceof L2PetInstance)
						summon.unSummon(assist4);

				}

				if (assist4.getMountType() == 1 || assist4.getMountType() == 2)
					assist4.dismount();

			}
		}

		public void removeSkills()
		{
			if (!(leader.getClassId() == ClassId.shillenElder || leader.getClassId() == ClassId.shillienSaint || leader.getClassId() == ClassId.bishop || leader.getClassId() == ClassId.cardinal || leader.getClassId() == ClassId.elder || leader.getClassId() == ClassId.evaSaint))
			{
				for (L2Effect effect : leader.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						leader.stopSkillEffects(effect.getSkill().getId());
				}
			}

			if (!(assist.getClassId() == ClassId.shillenElder || assist.getClassId() == ClassId.shillienSaint || assist.getClassId() == ClassId.bishop || assist.getClassId() == ClassId.cardinal || assist.getClassId() == ClassId.elder || assist.getClassId() == ClassId.evaSaint))
			{
				for (L2Effect effect : assist.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist.stopSkillEffects(effect.getSkill().getId());
				}
			}

			if (!(assist2.getClassId() == ClassId.shillenElder || assist2.getClassId() == ClassId.shillienSaint || assist2.getClassId() == ClassId.bishop || assist2.getClassId() == ClassId.cardinal || assist2.getClassId() == ClassId.elder || assist2.getClassId() == ClassId.evaSaint))
			{
				for (L2Effect effect : assist2.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist2.stopSkillEffects(effect.getSkill().getId());
				}
			}

			if (!(assist3.getClassId() == ClassId.shillenElder || assist3.getClassId() == ClassId.shillienSaint || assist3.getClassId() == ClassId.bishop || assist3.getClassId() == ClassId.cardinal || assist3.getClassId() == ClassId.elder || assist3.getClassId() == ClassId.evaSaint))
			{
				for (L2Effect effect : assist3.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist3.stopSkillEffects(effect.getSkill().getId());
				}
			}

			if (!(assist4.getClassId() == ClassId.shillenElder || assist4.getClassId() == ClassId.shillienSaint || assist4.getClassId() == ClassId.bishop || assist4.getClassId() == ClassId.cardinal || assist4.getClassId() == ClassId.elder || assist4.getClassId() == ClassId.evaSaint))
			{
				for (L2Effect effect : assist4.getAllEffects())
				{
					if (ArenaConfig.ARENA_STOP_SKILL_LIST.contains(effect.getSkill().getId()))
						assist4.stopSkillEffects(effect.getSkill().getId());
				}
			}
		}

		public void sendPacket(String message, int duration)
		{
			if (leader != null && leader.isOnline())
				leader.sendPacket(new ExShowScreenMessage(message, duration * 1000));

			if (assist != null && assist.isOnline())
				assist.sendPacket(new ExShowScreenMessage(message, duration * 1000));

			if (assist2 != null && assist2.isOnline())
				assist2.sendPacket(new ExShowScreenMessage(message, duration * 1000));

			if (assist3 != null && assist3.isOnline())
				assist3.sendPacket(new ExShowScreenMessage(message, duration * 1000));

			if (assist4 != null && assist4.isOnline())
				assist4.sendPacket(new ExShowScreenMessage(message, duration * 1000));

		}

		public void inicarContagem(int duration)
		{
			if (leader != null && leader.isOnline())
				ThreadPoolManager.getInstance().scheduleGeneral(new countdown(leader, duration), 0);

			if (assist != null && assist.isOnline())
				ThreadPoolManager.getInstance().scheduleGeneral(new countdown(assist, duration), 0);

			if (assist2 != null && assist2.isOnline())
				ThreadPoolManager.getInstance().scheduleGeneral(new countdown(assist2, duration), 0);

			if (assist3 != null && assist3.isOnline())
				ThreadPoolManager.getInstance().scheduleGeneral(new countdown(assist3, duration), 0);

			if (assist4 != null && assist4.isOnline())
				ThreadPoolManager.getInstance().scheduleGeneral(new countdown(assist4, duration), 0);
		}

		public void sendPacketinit(String message, int duration)
		{
			if (leader != null && leader.isOnline())
				leader.sendPacket(new ExShowScreenMessage(message, duration * 1000, ExShowScreenMessage.SMPOS.MIDDLE_LEFT, false));

			if (assist != null && assist.isOnline())
				assist.sendPacket(new ExShowScreenMessage(message, duration * 1000, ExShowScreenMessage.SMPOS.MIDDLE_LEFT, false));

			if (assist2 != null && assist2.isOnline())
				assist2.sendPacket(new ExShowScreenMessage(message, duration * 1000, ExShowScreenMessage.SMPOS.MIDDLE_LEFT, false));

			if (assist3 != null && assist3.isOnline())
				assist3.sendPacket(new ExShowScreenMessage(message, duration * 1000, ExShowScreenMessage.SMPOS.MIDDLE_LEFT, false));

			if (assist4 != null && assist4.isOnline())
				assist4.sendPacket(new ExShowScreenMessage(message, duration * 1000, ExShowScreenMessage.SMPOS.MIDDLE_LEFT, false));

		}
	}

	private class EvtArenaTask implements Runnable
	{
		private final Pair pairOne;
		private final Pair pairTwo;
		private final int pOneX, pOneY, pOneZ, pTwoX, pTwoY, pTwoZ;
		private Arena arena;

		public EvtArenaTask(List<Pair> opponents)
		{
			pairOne = opponents.get(0);
			pairTwo = opponents.get(1);
			L2PcInstance leader = pairOne.getLeader();
			pOneX = leader.getX();
			pOneY = leader.getY();
			pOneZ = leader.getZ();
			leader = pairTwo.getLeader();
			pTwoX = leader.getX();
			pTwoY = leader.getY();
			pTwoZ = leader.getZ();
		}

		@Override
		public void run()
		{
			free--;
			pairOne.saveTitle();
			pairTwo.saveTitle();
			portPairsToArena();
			pairOne.inicarContagem(ArenaConfig.ARENA_WAIT_INTERVAL_5X5);
			pairTwo.inicarContagem(ArenaConfig.ARENA_WAIT_INTERVAL_5X5);
			try
			{
				Thread.sleep(ArenaConfig.ARENA_WAIT_INTERVAL_5X5 * 1000);
			}
			catch (InterruptedException e1)
			{
			}
			pairOne.sendPacketinit("Started. Good Fight!", 3);
			pairTwo.sendPacketinit("Started. Good Fight!", 3);
			pairOne.EventTitle(ArenaConfig.MSG_TEAM1, ArenaConfig.TITLE_COLOR_TEAM1);
			pairTwo.EventTitle(ArenaConfig.MSG_TEAM2, ArenaConfig.TITLE_COLOR_TEAM2);
			pairOne.setImobilised(false);
			pairTwo.setImobilised(false);
			pairOne.setArenaAttack(true);
			pairTwo.setArenaAttack(true);

			while (check())
			{
				// check players status each seconds
				try
				{
					Thread.sleep(ArenaConfig.ARENA_CHECK_INTERVAL);
				}
				catch (InterruptedException e)
				{
					break;
				}
			}
			finishDuel();
			free++;
		}

		private void finishDuel()
		{
			fights.remove(arena.id);
			rewardWinner();
			pairOne.revive();
			pairTwo.revive();
			pairOne.teleportTo(pOneX, pOneY, pOneZ);
			pairTwo.teleportTo(pTwoX, pTwoY, pTwoZ);
			pairOne.backTitle();
			pairTwo.backTitle();
			pairOne.setInTournamentEvent(false);
			pairTwo.setInTournamentEvent(false);
			pairOne.setArenaProtection(false);
			pairTwo.setArenaProtection(false);
			pairOne.setArenaAttack(false);
			pairTwo.setArenaAttack(false);
			arena.setFree(true);
		}

		private void rewardWinner()
		{
			if (pairOne.isAlive() && !pairTwo.isAlive())
			{
				L2PcInstance leader1 = pairOne.getLeader();
				L2PcInstance leader2 = pairTwo.getLeader();

				if (leader1.getClan() != null && leader2.getClan() != null && ArenaConfig.TOURNAMENT_EVENT_ANNOUNCE)
					Broadcast.gameAnnounceToOnlinePlayers("(5X5):(" + leader1.getClan().getName() + " VS " + leader2.getClan().getName() + ") ~> " + leader1.getClan().getName() + " win!");

				pairOne.rewards();
				pairTwo.rewardsLost();
			}
			else if (pairTwo.isAlive() && !pairOne.isAlive())
			{
				L2PcInstance leader1 = pairTwo.getLeader();
				L2PcInstance leader2 = pairOne.getLeader();

				if (leader1.getClan() != null && leader2.getClan() != null && ArenaConfig.TOURNAMENT_EVENT_ANNOUNCE)
					Broadcast.gameAnnounceToOnlinePlayers("(5X5):(" + leader1.getClan().getName() + " VS " + leader2.getClan().getName() + ") ~> " + leader1.getClan().getName() + " win!");
				
				pairTwo.rewards();
				pairOne.rewardsLost();
			}
		}

		private boolean check()
		{
			return (pairOne.isDead() && pairTwo.isDead());
		}

		private void portPairsToArena()
		{
			for (Arena arena : arenas)
			{
				if (arena.isFree)
				{
					this.arena = arena;
					arena.setFree(false);
					pairOne.removePet();
					pairTwo.removePet();
					pairOne.teleportTo(arena.x - 850, arena.y, arena.z);
					pairTwo.teleportTo(arena.x + 850, arena.y, arena.z);
					pairOne.setImobilised(true);
					pairTwo.setImobilised(true);
					pairOne.setInTournamentEvent(true);
					pairTwo.setInTournamentEvent(true);
					pairOne.removeSkills();
					pairTwo.removeSkills();
					fights.put(this.arena.id, pairOne.getLeader().getName() + " vs " + pairTwo.getLeader().getName());
					break;
				}
			}
		}
	}

	private class Arena
	{
		protected int x, y, z;
		protected boolean isFree = true;
		int id;

		public Arena(int id, int x, int y, int z)
		{
			this.id = id;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void setFree(boolean val)
		{
			isFree = val;
		}
	}

	protected class countdown implements Runnable
	{
		private final L2PcInstance _player;
		private int _time;

		public countdown(L2PcInstance player, int time)
		{
			_time = time;
			_player = player;
		}

		@Override
		public void run()
		{
			if (_player.isOnline())
			{

				switch (_time)
				{
				case 300:
				case 240:
				case 180:
				case 120:
				case 57:
					if (_player.isOnline())
					{
						_player.sendPacket(new ExShowScreenMessage("The battle starts in 60 second(s)..", 4000));
						_player.sendMessage("60 second(s) to start the battle.");
					}
					break;
				case 45:
					if (_player.isOnline())
					{
						_player.sendPacket(new ExShowScreenMessage("" + _time + " ..", 3000));
						_player.sendMessage(_time + " second(s) to start the battle!");
					}
					break;
				case 27:
					if (_player.isOnline())
					{
						_player.sendPacket(new ExShowScreenMessage("The battle starts in 30 second(s)..", 4000));
						_player.sendMessage("30 second(s) to start the battle.");
					}
					break;
				case 20:
					if (_player.isOnline())
					{
						_player.sendPacket(new ExShowScreenMessage("" + _time + " ..", 3000));
						_player.sendMessage(_time + " second(s) to start the battle!");
					}
					break;
				case 15:
					if (_player.isOnline())
					{
						_player.sendPacket(new ExShowScreenMessage("" + _time + " ..", 3000));
						_player.sendMessage(_time + " second(s) to start the battle!");
					}
					break;
				case 10:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				case 5:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				case 4:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				case 3:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				case 2:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				case 1:
					if (_player.isOnline())
						_player.sendMessage(_time + " second(s) to start the battle!");
					break;
				}
				if (_time > 1)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new countdown(_player, _time - 1), 1000);
				}
			}
		}
	}

	private static class SingletonHolder
	{
		protected static final Arena5x5 INSTANCE = new Arena5x5();
	}
}