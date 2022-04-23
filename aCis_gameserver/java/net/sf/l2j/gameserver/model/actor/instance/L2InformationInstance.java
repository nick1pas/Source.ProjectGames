package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.util.Rnd;

public class L2InformationInstance extends L2NpcInstance
{
	public L2InformationInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onAction(L2PcInstance player)
	{
		if (this != player.getTarget()) 
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel() - getLevel()));
			player.sendPacket(new ValidateLocation(this));
		}
		else if (isInsideRadius(player, INTERACTION_DISTANCE, false, false)) 
		{
			SocialAction sa = new SocialAction(this, Rnd.get(8));
			broadcastPacket(sa);
			player.setCurrentFolkNPC(this);
			showMessageWindow(player);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else 
		{
			player.getAI().setIntention(CtrlIntention.INTERACT, this);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}

	private void showMessageWindow(L2PcInstance player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/Information.htm");
		
		html.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
		html.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
		html.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
		html.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
		html.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
		html.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
		html.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
		html.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ADENA));
		html.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
		html.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
		html.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
		html.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
		html.replace("%pet_rate_xp%", String.valueOf(Config.PET_XP_RATE));
		html.replace("%sineater_rate_xp%", String.valueOf(Config.SINEATER_XP_RATE));
		html.replace("%pet_food_rate%", String.valueOf(Config.PET_FOOD_RATE));
		html.replace("%name%", player.getName());
		player.sendPacket(html);
	}
}