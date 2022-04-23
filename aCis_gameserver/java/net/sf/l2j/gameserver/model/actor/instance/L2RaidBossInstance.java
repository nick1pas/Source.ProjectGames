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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossPointsManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager.StatusEnum;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.util.Rnd;

/**
 * This class manages all RaidBoss. In a group mob, there are one master called RaidBoss and several slaves called Minions.
 */
public class L2RaidBossInstance extends L2MonsterInstance
{
	private StatusEnum _raidStatus;
	protected ScheduledFuture<?> _maintenanceTask;
	
	/**
	 * Constructor of L2RaidBossInstance (use L2Character and L2NpcInstance constructor).
	 * <ul>
	 * <li>Call the L2Character constructor to set the _template of the L2RaidBossInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the L2RaidBossInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it</li>
	 * </ul>
	 * @param objectId Identifier of the object to initialized
	 * @param template L2NpcTemplate to apply to the NPC
	 */
	public L2RaidBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setIsRaid(true);
	}
	
	@Override
	public void onSpawn()
	{
		setIsNoRndWalk(true);
		super.onSpawn();
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
			return false;
		
		if (_maintenanceTask != null)
		{
			_maintenanceTask.cancel(false);
			_maintenanceTask = null;
		}
		
		if (killer != null)
		{
			final L2PcInstance player = killer.getActingPlayer();
			
			if (player != null)
			{
				if (Config.NOBLESS_FROM_BOSS)
				{
					if (getNpcId() == Config.BOSS_ID)
					{
						if (player.getParty() != null)
						{
							for (L2PcInstance member : player.getParty().getPartyMembers())
							{
								if (member.isNoble() == true)
								{
									member.sendMessage("Your party gained nobless status for defeating " + getName() + "!");
								}
								else if (member.isInsideRadius(getX(), getY(), getZ(), Config.RADIUS_TO_RAID, false, false))
								{
									member.setNoble(true, true);
									member.addItem("Quest", 7694, 1, member, true);
									member.broadcastPacket(new SocialAction(player, 16));
			                        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			                        html.setHtml("<html><body>Congratulations, you're now a noble!<br1>Open the Skills & Magic (ALT+K) to see your acquired abilities.</body></html>");
			                        member.sendPacket(html);
								}
								else
								{
									member.sendMessage("Your party killed " + getName() + "! But you were to far...");
								}
							}
						}
						else if (player.getParty() == null && !player.isNoble())
						{
							player.setNoble(true, true);
							player.addItem("Quest", 7694, 1, player, true);
							player.broadcastPacket(new SocialAction(player, 16));
	                        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	                        html.setHtml("<html><body>Congratulations, you're now a noble!<br1>Open the Skills & Magic (ALT+K) to see your acquired abilities.</body></html>");
	                        player.sendPacket(html);
						}
					}
				}
				
				if (Config.ANNOUNCE_RAID_BOSS_DEATH)
				{
					if (player.getClan() != null)
						Broadcast.gameAnnounceToOnlinePlayers("Raid Boss: " + getName() + " was killed by " + player.getName()+ " of the clan: " + player.getClan().getName());
					else
						Broadcast.gameAnnounceToOnlinePlayers("Raid Boss: " + getName() + " was killed by " + player.getName());
				}
			      
				broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.RAID_WAS_SUCCESSFUL));
				broadcastPacket(new PlaySound("systemmsg_e.1209"));

				if (player.isInParty())
				{
					for (L2PcInstance member : player.getParty().getPartyMembers())
					{
						RaidBossPointsManager.addPoints(member, getNpcId(), (getLevel() / 2) + Rnd.get(-5, 5));
						if (member.isNoble())
							Hero.getInstance().setRBkilled(member.getObjectId(), getNpcId());
					}
				}
				else
				{
					RaidBossPointsManager.addPoints(player, getNpcId(), (getLevel() / 2) + Rnd.get(-5, 5));
					if (player.isNoble())
						Hero.getInstance().setRBkilled(player.getObjectId(), getNpcId());
				}
			}
		}
		
		updatePvpFlagById();

		RaidBossSpawnManager.getInstance().updateStatus(this, true);
		return true;
	}
	
	@Override
	public void deleteMe()
	{
		if (_maintenanceTask != null)
		{
			_maintenanceTask.cancel(false);
			_maintenanceTask = null;
		}
		
		super.deleteMe();
	}
	
	/**
	 * Spawn minions.<br>
	 * Also if boss is too far from home location at the time of this check, teleport it to home.
	 */
	@Override
	protected void startMaintenanceTask()
	{
		super.startMaintenanceTask();
		
		_maintenanceTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				checkAndReturnToSpawn();
			}
		}, 60000, 30000);
	}
	
	protected void checkAndReturnToSpawn()
	{
		if (isDead() || isMovementDisabled())
			return;
		
		// Gordon does not have permanent spawn
		if (getNpcId() == 29095)
			return;
		
		final L2Spawn spawn = getSpawn();
		if (spawn == null)
			return;
		
		final int spawnX = spawn.getLocx();
		final int spawnY = spawn.getLocy();
		final int spawnZ = spawn.getLocz();
		
		//if (!isInCombat() && !isMovementDisabled())
		if (!isMovementDisabled())
		{
			if (!isInsideRadius(spawnX, spawnY, spawnZ, Math.max(Config.MAX_DRIFT_RANGE, 200), true, false))
				teleToLocation(spawnX, spawnY, spawnZ, 0);
		}
	}
	
	public void setRaidStatus(StatusEnum status)
	{
		_raidStatus = status;
	}
	
	public StatusEnum getRaidStatus()
	{
		return _raidStatus;
	}
	
	private void updatePvpFlagById()
	{
		if (Config.NPCS_FLAG_LIST.contains(getNpcId()) && Config.ALLOW_FLAG_ONKILL_BY_ID)
		{
			for (L2PcInstance playerInRadius : getKnownList().getKnownTypeInRadius(L2PcInstance.class, Config.NPCS_FLAG_RANGE)) 
			{
				final L2Party party = playerInRadius.getParty();
				if (party != null)
				{
					for (L2PcInstance member : party.getPartyMembers())
					{
						member.updatePvPStatus();
					}
				}
				else
					playerInRadius.updatePvPStatus();
			}
		}		
	}
}