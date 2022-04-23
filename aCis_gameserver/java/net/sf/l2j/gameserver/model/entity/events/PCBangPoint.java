package net.sf.l2j.gameserver.model.entity.events;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;

/**
 * @author Anumis
 */
public class PCBangPoint implements Runnable
{
	Logger _log = Logger.getLogger(PCBangPoint.class.getName());
	private static PCBangPoint _instance;

	public static PCBangPoint getInstance()
	{
		if (_instance == null)
		{
			_instance = new PCBangPoint();
		}
		return _instance;
	}

	private PCBangPoint()
	{
		_log.info("PcBang point event started.");
	}

	@Override
	public void run()
	{
		int score = 0;
		for (L2PcInstance activeChar: L2World.getInstance().getAllPlayers().values())
		{
			if (activeChar.getLevel() > Config.PCB_MIN_LEVEL)
			{
				if (activeChar.isVip())
				{
					score = Rnd.get(Config.PCB_POINT_MIN, Config.PCB_POINT_MAX) * Config.VIP_DROP_RATE;

					if (Rnd.get(100) <= Config.PCB_CHANCE_DUAL_POINT)
					{
						score *= 2;
						activeChar.addPcBangScore(score);
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT_DOUBLE).addNumber(score));
						activeChar.updatePcBangWnd(score, true, true);
					}
					else
					{
						activeChar.addPcBangScore(score);
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT).addNumber(score));
						activeChar.updatePcBangWnd(score, true, false);
					}
				}
				else
				{
					score = Rnd.get(Config.PCB_POINT_MIN, Config.PCB_POINT_MAX);

					if (Rnd.get(100) <= Config.PCB_CHANCE_DUAL_POINT)
					{
						score *= 2;
						activeChar.addPcBangScore(score);
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT_DOUBLE).addNumber(score));
						activeChar.updatePcBangWnd(score, true, true);
					}
					else
					{
						activeChar.addPcBangScore(score);
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT).addNumber(score));
						activeChar.updatePcBangWnd(score, true, false);
					}
				}
			}
			activeChar = null;
		}
	}
}