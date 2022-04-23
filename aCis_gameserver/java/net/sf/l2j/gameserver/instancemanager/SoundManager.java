package net.sf.l2j.gameserver.instancemanager;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.util.Rnd;


public class SoundManager //by xdem
{
	final static int MilisBetweenMultiKills = Config.QUAKE_SOUND_TIME;

	public static enum SoundFile
	{
		DUMMY("null"),//1 kill, not used
		DOUBLE_KILL("doublekill"),
		TRIPLE_KILL("triplekill"),
		QUADRA_KILL("quadrakill"),
		PENTA_KILL("pentakill"),
		LEGENDARY_KILL("legendary");

	    String FileName;

		SoundFile(String FileName)
		{
			this.FileName = FileName;
		}

		public String getFileName()
		{
			return FileName;
		}


		public static SoundFile getSoundFile(int i)
		{
			return SoundFile.values()[i];
		}
	}

	public static void playSound(Versus vs)
	{
		PlaySound sound;
		int level = vs.getKillLevel();
		level = level > 5 ? 5 : level;
		String s = SoundFile.getSoundFile(level).getFileName()+Rnd.get(1, 3);
		
		sound = new PlaySound(1, s, 0, 0, 0, 0, 0);
		ExShowScreenMessage msg = new ExShowScreenMessage(vs.getPlayer().getName() +" has scored a "+SoundFile.getSoundFile(level).toString().replace("_", " ")+"!", 4000);
		
		vs.getPlayer().sendPacket(msg);
		vs.getPlayer().sendPacket(sound);
		
		for (L2Character ch : vs.getPlayer().getKnownList().getKnownType(L2Character.class))
		{
			if (ch instanceof L2PcInstance)
			{
				if (Config.ALLOW_QUAKE_SOUND)
				{
					ch.sendPacket(msg);
				}
				ch.sendPacket(sound);
			}
		}
	}

	public static class Versus
	{
		private long _lastKillMs;
		private int _killLevel;
		final L2PcInstance _player;

		public Versus(L2PcInstance player)
		{
			_player = player;
			_lastKillMs = System.currentTimeMillis();
		}

		public void increaseKills()
		{
			if (_lastKillMs + MilisBetweenMultiKills > System.currentTimeMillis())
				_killLevel++;
			else
				_killLevel = 0;
			_lastKillMs = System.currentTimeMillis();
			if (_killLevel > 0)
				playSound(this);
		}

		public int getKillLevel()
		{
			return _killLevel;
		}

		public L2PcInstance getPlayer()
		{
			return _player;
		}
	}
}