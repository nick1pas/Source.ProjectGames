package phantom;

import java.util.logging.Level;

import phantom.ai.FakePlayerAI;
import phantom.helpers.FakeHelpers;
import net.sf.l2j.gameserver.GeoData;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Party.MessageType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.template.PcTemplate;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSiegeFlag;
import net.sf.l2j.gameserver.templates.skills.L2SkillType;
import net.sf.l2j.util.Point3D;
import net.sf.l2j.util.Rnd;

public class FakePlayer extends L2PcInstance
{
	private FakePlayerAI _fakeAi;	
	private boolean _underControl = false;
	
	public boolean isUnderControl() 
	{
		return _underControl;
	}
	
	public void setUnderControl(boolean underControl) 
	{
		_underControl = underControl;
	}
	
	protected FakePlayer(int objectId)
	{
		super(objectId);
	}
	
	public FakePlayer(int objectId, PcTemplate template, String accountName, PcAppearance app)
	{
		super(objectId, template, accountName, app);
	}
		
	public FakePlayerAI getFakeAi()
	{
		return _fakeAi;
	}
	
	public void setFakeAi(FakePlayerAI _fakeAi)
	{
		this._fakeAi = _fakeAi;
	}
	
	public void assignDefaultAI() 
	{
		try 
		{
			setFakeAi(FakeHelpers.getAIbyClassId(getClassId()).getConstructor(FakePlayer.class).newInstance(this));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean checkUseMagicConditions(L2Skill skill, boolean forceUse, boolean dontMove)
	{
		if (skill == null)
			return false;
		
		if (isDead() || isOutOfControl())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (isSkillDisabled(skill))
			return false;
		
		L2SkillType sklType = skill.getSkillType();
		
		if (isFishing() && (sklType != L2SkillType.PUMPING && sklType != L2SkillType.REELING && sklType != L2SkillType.FISHING))
			return false;
		
		if (inObserverMode())
		{
			abortCast();
			return false;
		}
		
		if (isSitting())
		{
			if (skill.isToggle())
			{
				L2Effect effect = getFirstEffect(skill.getId());
				if (effect != null)
				{
					effect.exit();
					return false;
				}
			}			
			return false;
		}
		
		if (skill.isToggle())
		{
			L2Effect effect = getFirstEffect(skill.getId());
			
			if (effect != null)
			{
				if (skill.getId() != 60)
					effect.exit();
				
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		if (isFakeDeath())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		L2Object target = null;
		SkillTargetType sklTargetType = skill.getTargetType();
		Point3D worldPosition = getCurrentSkillWorldPosition();
		
		if (sklTargetType == SkillTargetType.TARGET_GROUND && worldPosition == null)
		{
			_log.info("WorldPosition is null for skill: " + skill.getName() + ", player: " + getName() + ".");
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		switch (sklTargetType)
		{
			// Target the player if skill type is AURA, PARTY, CLAN or SELF
			case TARGET_AURA:
			case TARGET_FRONT_AURA:
			case TARGET_BEHIND_AURA:
			case TARGET_AURA_UNDEAD:
			case TARGET_PARTY:
			case TARGET_ALLY:
			case TARGET_CLAN:
			case TARGET_GROUND:
			case TARGET_SELF:
			case TARGET_CORPSE_ALLY:
			case TARGET_AREA_SUMMON:
				target = this;
				break;
			case TARGET_PET:
			case TARGET_SUMMON:
				target = getPet();
				break;
			default:
				target = getTarget();
				break;
		}
		
		// Check the validity of the target
		if (target == null)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (target instanceof L2DoorInstance)
		{
			if (!((L2DoorInstance) target).isAutoAttackable(this) // Siege doors only hittable during siege
				|| (((L2DoorInstance) target).isUnlockable() && skill.getSkillType() != L2SkillType.UNLOCK)) // unlockable doors
			{
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		// Are the target and the player in the same duel?
		if (isInDuel())
		{
			if (target instanceof L2Playable)
			{
				// Get Player
				L2PcInstance cha = target.getActingPlayer();
				if (cha.getDuelId() != getDuelId())
				{
					sendPacket(SystemMessageId.INCORRECT_TARGET);
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
			}
		}

		// Check if all casting conditions are completed
		if (!skill.checkCondition(this, target, false))
		{
			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		// ************************************* Check Skill Type *******************************************
		
		// Check if this is offensive magic skill
		if (skill.isOffensive())
		{
			if (isInsidePeaceZone(this, target))
			{
				// If Creature or target is in a peace zone, send a system message TARGET_IN_PEACEZONE ActionFailed
				sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			if (isInOlympiadMode() && !isOlympiadStart())
			{
				// if Player is in Olympia and the match isn't already start, send ActionFailed
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			// Check if the target is attackable
			if (!target.isAttackable() && !getAccessLevel().allowPeaceAttack())
			{
				// If target is not attackable, send ActionFailed
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			// Check if a Forced ATTACK is in progress on non-attackable target
			if (!target.isAutoAttackable(this) && !forceUse)
			{
				switch (sklTargetType)
				{
					case TARGET_AURA:
					case TARGET_FRONT_AURA:
					case TARGET_BEHIND_AURA:
					case TARGET_AURA_UNDEAD:
					case TARGET_CLAN:
					case TARGET_ALLY:
					case TARGET_PARTY:
					case TARGET_SELF:
					case TARGET_GROUND:
					case TARGET_CORPSE_ALLY:
					case TARGET_AREA_SUMMON:
						break;
					default: // Send ActionFailed to the Player
						sendPacket(ActionFailed.STATIC_PACKET);
						return false;
				}
			}
			
			// Check if the target is in the skill cast range
			if (dontMove)
			{
				// Calculate the distance between the Player and the target
				if (sklTargetType == SkillTargetType.TARGET_GROUND)
				{
					if (!isInsideRadius(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), (int) (skill.getCastRange() + getTemplate().getCollisionRadius()), false, false))
					{
						// Send a System Message to the caster
						sendPacket(SystemMessageId.TARGET_TOO_FAR);
						
						// Send ActionFailed to the Player
						sendPacket(ActionFailed.STATIC_PACKET);
						return false;
					}
				}
				else if (skill.getCastRange() > 0 && !isInsideRadius(target, (int) (skill.getCastRange() + getTemplate().getCollisionRadius()), false, false))
				{
					// Send a System Message to the caster
					sendPacket(SystemMessageId.TARGET_TOO_FAR);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
			}
		}
		
		// Check if the skill is defensive
		if (!skill.isOffensive() && target instanceof L2MonsterInstance && !forceUse)
		{
			// check if the target is a monster and if force attack is set.. if not then we don't want to cast.
			switch (sklTargetType)
			{
				case TARGET_PET:
				case TARGET_SUMMON:
				case TARGET_AURA:
				case TARGET_FRONT_AURA:
				case TARGET_BEHIND_AURA:
				case TARGET_AURA_UNDEAD:
				case TARGET_CLAN:
				case TARGET_SELF:
				case TARGET_CORPSE_ALLY:
				case TARGET_PARTY:
				case TARGET_ALLY:
				case TARGET_CORPSE_MOB:
				case TARGET_AREA_CORPSE_MOB:
				case TARGET_GROUND:
					break;
				default:
				{
					switch (sklType)
					{
						case BEAST_FEED:
						case DELUXE_KEY_UNLOCK:
						case UNLOCK:
							break;
						default:
							sendPacket(ActionFailed.STATIC_PACKET);
							return false;
					}
					break;
				}
			}
		}
		
		// Check if the skill is Spoil type and if the target isn't already spoiled
		if (sklType == L2SkillType.SPOIL)
		{
			if (!(target instanceof L2MonsterInstance))
			{
				// Send a System Message to the Player
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				
				// Send ActionFailed to the Player
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}

		// Check if the skill is Drain Soul (Soul Crystals) and if the target is a MOB
		if (sklType == L2SkillType.DRAIN_SOUL)
		{
			if (!(target instanceof L2MonsterInstance))
			{
				// Send a System Message to the Player
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				
				// Send ActionFailed to the Player
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		// Check if this is a Pvp skill and target isn't a non-flagged/non-karma player
		switch (sklTargetType)
		{
			case TARGET_PARTY:
			case TARGET_ALLY: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
			case TARGET_CLAN: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
			case TARGET_AURA:
			case TARGET_FRONT_AURA:
			case TARGET_BEHIND_AURA:
			case TARGET_AURA_UNDEAD:
			case TARGET_GROUND:
			case TARGET_SELF:
			case TARGET_CORPSE_ALLY:
			case TARGET_AREA_SUMMON:
				break;
			default:
				if (!checkPvpSkill(target, skill) && !getAccessLevel().allowPeaceAttack())
				{
					// Send a System Message to the Player
					sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
		}
		
		if ((sklTargetType == SkillTargetType.TARGET_HOLY && !checkIfOkToCastSealOfRule(CastleManager.getInstance().getCastle(this), false, skill, target)) || (sklType == L2SkillType.SIEGEFLAG && !L2SkillSiegeFlag.checkIfOkToPlaceFlag(this, false)) || (sklType == L2SkillType.STRSIEGEASSAULT && !checkIfOkToUseStriderSiegeAssault(skill)) || (sklType == L2SkillType.SUMMON_FRIEND && !(checkSummonerStatus(this) && checkSummonTargetStatus(target, this))))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			abortCast();
			return false;
		}
		
		// finally, after passing all conditions
		return true;
	}
	
	public void forceAutoAttack(L2Character creature)
	{
		if (this.getTarget() == null)
			return;
		
		if (isInsidePeaceZone(this, this.getTarget()))
			return;
		
		if (isInOlympiadMode() && getTarget() != null && getTarget() instanceof L2Playable)
		{
			L2PcInstance target = getTarget().getActingPlayer();
			if (target == null || (target.isInOlympiadMode() && (!isOlympiadStart() || getOlympiadGameId() != target.getOlympiadGameId())))
				return;
		}
		
		if (getTarget() != null && !getTarget().isAttackable() && !getAccessLevel().allowPeaceAttack())
			return;
		
		if (isConfused())
			return;
		
		// GeoData Los Check or dz > 1000
		if (!GeoData.getInstance().canSeeTarget(this, this.getTarget()))
			return;
		
		// Notify AI with ATTACK
		getAI().setIntention(CtrlIntention.ATTACK, this.getTarget());
	}
	
	public synchronized void despawnPlayer()
	{
		try
		{
			// Put the online status to false
			setOnlineStatus(false, true);
			
			// abort cast & attack and remove the target. Cancels movement aswell.
			abortAttack();
			abortCast();
			stopMove(null);
			setTarget(null);

			if (isFlying())
				removeSkill(FrequentSkill.WYVERN_BREATH.getSkill().getId(), false);
			
			// Stop all scheduled tasks
			stopAllTimers();
	
			// Stop signets & toggles effects.
			for (L2Effect effect : getAllEffects())
			{
				if (effect.getSkill().isToggle())
				{
					effect.exit();
					continue;
				}
				
				switch (effect.getEffectType())
				{
					case SIGNET_GROUND:
					case SIGNET_EFFECT:
						effect.exit();
						break;
				default:
					break;
				}
			}
			
			// Remove the Player from the world
			decayMe();
			
			// If a party is in progress, leave it
			if (getParty() != null)
				getParty().removePartyMember(this, MessageType.Disconnected);
			
			// If the Player has Pet, unsummon it
			if (getPet() != null)
				getPet().unSummon(this);
			
			// Handle removal from olympiad game
			if (OlympiadManager.getInstance().isRegistered(this) || getOlympiadGameId() != -1)
				OlympiadManager.getInstance().removeDisconnectedCompetitor(this);
			
			// set the status for pledge member list to OFFLINE
			if (getClan() != null)
			{
				L2ClanMember clanMember = getClan().getClanMember(getObjectId());
				if (clanMember != null)
					clanMember.setPlayerInstance(null);
			}
			
			// deals with sudden exit in the middle of transaction
			if (getActiveRequester() != null)
			{
				setActiveRequester(null);
				cancelActiveTrade();
			}
			
			// Oust player from boat
			if (getVehicle() != null)
				getVehicle().oustPlayer(this);
			
			// Update inventory and remove them from the world
			getInventory().deleteMe();
			
			// Update warehouse and remove them from the world
			clearWarehouse();
			
			// Update freight and remove them from the world
			clearFreight();
			clearDepositedFreight();
			
			if (isCursedWeaponEquipped())
				CursedWeaponsManager.getInstance().getCursedWeapon(getCursedWeaponEquippedId()).setPlayer(null);
			
			if (getClanId() > 0)
				getClan().broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(this), this);
			
			L2World.getInstance().removePlayer(this); // force remove in case of crash during teleport
			
			// friends & blocklist update
			notifyFriends(false);
			getBlockList().playerLogout();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception on deleteMe()" + e.getMessage(), e);
		}
	}

	public void heal() 
	{
		setCurrentCp(getMaxCp());
		setCurrentHp(getMaxHp());
		setCurrentMp(getMaxMp());
	}

	public void randomWalk()
	{
		int x = getX();
		int y = getY();
		int z = getZ();
		switch (Rnd.get(1, 7))
		{
			case 1:
				x = getLastX();
				y = getLastY();
				z = getLastZ();
				break;
			case 2:
				x += 200;
				y -= 60;
				break;
			case 3:
				x += 400;
				y -= 60;
				break;
			case 4:
				x = getLastX();
				y = getLastY();
				z = getLastZ();
				break;
			case 5:
				x -= 200;
				y += 60;
				break;
			case 6:
				x -= 400;
				y += 60;
				break;
			case 7:
				x = getLastX();
				y = getLastY();
				z = getLastZ();
				break;
		}
		
		setRunning();
		getAI().setIntention(CtrlIntention.MOVE_TO, new L2CharPosition(x, y, z, 0));
	}
}