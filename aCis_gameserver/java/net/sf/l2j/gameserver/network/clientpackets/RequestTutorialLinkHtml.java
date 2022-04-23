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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Level;

import net.sf.l2j.gameserver.handler.ITutorialHandler;
import net.sf.l2j.gameserver.handler.TutorialHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2ClassMasterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.serverpackets.TutorialCloseHtml;

public class RequestTutorialLinkHtml extends L2GameClientPacket
{
	String _bypass;

	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();

		if (player == null)
			return;

		if (_bypass.equalsIgnoreCase("close"))
		{
			player.sendPacket(TutorialCloseHtml.STATIC_PACKET);
			return;
		}

		if (_bypass.startsWith("-h"))
		{
			_bypass = _bypass.substring(2);
			
			if (_bypass.startsWith("_"))
				_bypass = _bypass.substring(1);
		}
		
		final ITutorialHandler handler = TutorialHandler.getInstance().getHandler(_bypass);

		if (handler != null)
		{
			String command = _bypass;
			String params = "";
			if (_bypass.indexOf("_") != -1)
			{
				command = _bypass.substring(0, _bypass.indexOf("_"));
				params = _bypass.substring(_bypass.indexOf("_")+1, _bypass.length());
			}
			handler.useLink(command, player, params);
		}
		else
		{
			_log.log(Level.WARNING, getClient() + " sent not handled RequestTutorialLinkHtml: [" + _bypass + "]");
		}

		L2ClassMasterInstance.onTutorialLink(player, _bypass);

		QuestState qs = player.getQuestState("Tutorial");
		if (qs != null)
			qs.getQuest().notifyEvent(_bypass, null, player);
	}
}