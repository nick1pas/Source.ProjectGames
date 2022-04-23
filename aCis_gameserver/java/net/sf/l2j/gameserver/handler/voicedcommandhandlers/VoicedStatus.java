package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.GMViewCharacterInfo;
import net.sf.l2j.gameserver.network.serverpackets.GMViewHennaInfo;

public class VoicedStatus implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{
		"status",
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("status"))
		{
			if (activeChar.getTarget() == null)
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}
			if (!(activeChar.getTarget() instanceof L2PcInstance))
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}

			L2Character targetCharacter = (L2Character) activeChar.getTarget();
			L2PcInstance targetPlayer = targetCharacter.getActingPlayer();

			activeChar.sendPacket(new GMViewCharacterInfo(targetPlayer));
			activeChar.sendPacket(new GMViewHennaInfo(targetPlayer));
			return true;
		}
		/*
		else if (command.startsWith("inventory"))
		{
			if (activeChar.getTarget() == null)
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}
			if (!(activeChar.getTarget() instanceof L2PcInstance))
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}

			L2Character targetCharacter = (L2Character) activeChar.getTarget();
			L2PcInstance targetPlayer = targetCharacter.getActingPlayer();

			activeChar.sendPacket(new GMViewItemList(targetPlayer));
			return true;
		}
		else if (command.startsWith("skills"))
		{
			if (activeChar.getTarget() == null)
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}
			if (!(activeChar.getTarget() instanceof L2PcInstance))
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}

			L2Character targetCharacter = (L2Character) activeChar.getTarget();
			L2PcInstance targetPlayer = targetCharacter.getActingPlayer();

			activeChar.sendPacket(new GMViewSkillInfo(targetPlayer));
			return true;
		}
		*/
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}