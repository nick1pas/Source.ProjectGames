package net.sf.l2j.gameserver.handler.tutorialhandlers;

import net.sf.l2j.gameserver.handler.ITutorialHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.events.StartupSystem;

public class StartupHandler implements ITutorialHandler
{
	private static final String[] LINK_COMMANDS =
	{
		"start"
	};

	@Override
	public boolean useLink(String _command, L2PcInstance activeChar, String params)
	{
		if (_command.startsWith("start"))
		{
			StartupSystem.handleCommands(activeChar, params);
		}
		return true;
	}

	@Override
	public String[] getLinkList()
	{
		return LINK_COMMANDS;
	}
}