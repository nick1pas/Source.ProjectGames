package phantom.ai;

import java.util.List;
import java.util.stream.Collectors;

import phantom.FakePlayer;
import net.sf.l2j.gameserver.GeoData;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable.TeleportWhereType;
import net.sf.l2j.gameserver.instancemanager.custom.PvPZoneManager;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.type.L2ChangePvpZone;
import net.sf.l2j.gameserver.network.serverpackets.MoveToLocation;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.util.Point3D;
import net.sf.l2j.util.Rnd;

public abstract class FakePlayerAI
{
	protected final FakePlayer _fakePlayer;		
	protected volatile boolean _clientMoving;
	protected volatile boolean _clientAutoAttacking;
	private long _moveToPawnTimeout;
	protected int _clientMovingToPawnOffset;	
	protected boolean _isBusyThinking = false;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 10;
	
	public FakePlayerAI(FakePlayer character)
	{
		_fakePlayer = character;
		setup();
	}
	
	public void setup() 
	{
		_fakePlayer.setIsRunning(true);
	}
	
	protected void handleDeath() 
	{
		if (_fakePlayer.isDead() && !_fakePlayer.isInsideZone(ZoneId.CHANGE_PVP))
		{
			if (iterationsOnDeath >= toVillageIterationsOnDeath) 
			{
				toVillageOnDeath();
				setBusyThinking(true);
			}
			iterationsOnDeath++;
			return;
		}
		
		if (_fakePlayer.isDead() && _fakePlayer.isInsideZone(ZoneId.CHANGE_PVP))
		{
			if (iterationsOnDeath >= toVillageIterationsOnDeath) 
			{
				toPvpZoneOnDeath();
				setBusyThinking(true);
			}
			iterationsOnDeath++;
			return;
		}
		
		iterationsOnDeath = 0;		
	}
	
	public void setBusyThinking(boolean thinking)
	{
		_isBusyThinking = thinking;
	}
	
	public boolean isBusyThinking()
	{
		return _isBusyThinking;
	}
	
	protected void teleportToLocation(int x, int y, int z, int randomOffset) 
	{
		_fakePlayer.stopMove(null);
		_fakePlayer.abortAttack();
		_fakePlayer.abortCast();		
		_fakePlayer.setIsTeleporting(true);
		_fakePlayer.setTarget(null);		
		_fakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);		
		if (randomOffset > 0)
		{
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}		
		z += 5;
		_fakePlayer.broadcastPacket(new TeleportToLocation(_fakePlayer, x, y, z));
		_fakePlayer.decayMe();		
		_fakePlayer.setXYZ(x, y, z);
		_fakePlayer.onTeleported();		
		_fakePlayer.revalidateZone(true);
	}
	

	protected void tryTargetRandomCreatureByTypeInRadius(Class<? extends L2Character> creatureClass, int radius)
	{
		if (_fakePlayer.getTarget() == null) 
		{
			List<L2Character> targets = _fakePlayer.getKnownList().getKnownTypeInRadius(creatureClass, radius).stream().filter(x-> checkTarget(x) && GeoData.getInstance().canSeeTarget(_fakePlayer, x)).collect(Collectors.toList());
			if(!targets.isEmpty()) 
			{
				L2Character target = targets.get(Rnd.get(0, targets.size() -1 ));
				_fakePlayer.setTarget(target);				
			}
		}
		else 
		{
			if (((L2Character)_fakePlayer.getTarget()).isDead())
			_fakePlayer.setTarget(null);
		}	
	}

	protected boolean checkTarget(L2Character target)
	{
		if (target == null)
		{
			return false;
		}
		if (target.isDead() || target.isGM())
		{
			return false;
		}
		if (target.isInsideZone(ZoneId.PEACE) || target.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
		{
			return false;
		}
		if (target instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) target;
            if ((_fakePlayer.getClanId() > 0 && player.getClanId() > 0 && _fakePlayer.getClanId() == player.getClanId()) || (_fakePlayer.getAllyId() > 0 && player.getAllyId() > 0 && _fakePlayer.getAllyId() == player.getAllyId()))
            {
                return false;
            }
			if (player.getClan() != null)
			{
				return true;
			}
			if (player.getKarma() > 0 || player.getPvpFlag() > 0)
			{
				return true;
			}
			if (player.isInsideZone(ZoneId.PVP) && player.getActiveWeaponInstance() != null || player.isInsideZone(ZoneId.SIEGE) || player.isInsideZone(ZoneId.CHANGE_PVP))
			{
				return true;
			}
			if (player.inObserverMode())
			{
				return false;
			}
		}
		else if (target instanceof L2SummonInstance)
		{
			L2Summon summon = (L2Summon) target;
			if (summon.getKarma() > 0 || summon.getPvpFlag() > 0)
			{
				return true;
			}
			if (summon.isInsideZone(ZoneId.PVP) || summon.isInsideZone(ZoneId.SIEGE) || summon.isInsideZone(ZoneId.CHANGE_PVP))
			{
				return true;
			}
		}
		/*
		else if (target instanceof L2MonsterInstance)
		{
			L2MonsterInstance monster = (L2MonsterInstance) target;
			if (_fakePlayer.isInsideRadius(monster, 3200, false, false))
			{
				return true;
			}
		}
		*/
		return false;
	}
	
	public void castSpell(L2Skill skill)
	{
		if (!_fakePlayer.isCastingNow()) 
		{		
			if (skill.getTargetType() == SkillTargetType.TARGET_GROUND)
			{
				if (maybeMoveToPosition((_fakePlayer).getCurrentSkillWorldPosition(), skill.getCastRange()))
				{
					_fakePlayer.setIsCastingNow(false);
					return;
				}
			}
			else
			{
				if (checkTargetLost(_fakePlayer.getTarget()))
				{
					if (skill.isOffensive() && _fakePlayer.getTarget() != null)
						_fakePlayer.setTarget(null);
					
					_fakePlayer.setIsCastingNow(false);
					return;
				}
				
				if (_fakePlayer.getTarget() != null)
				{
					if (maybeMoveToPawn(_fakePlayer.getTarget(), skill.getCastRange()))
						return;
				}
				
				if (_fakePlayer.isSkillDisabled(skill)) 
					return;					
			}
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fakePlayer.getAI().setIntention(CtrlIntention.CAST, skill, _fakePlayer.getTarget());
		}
		_fakePlayer.forceAutoAttack((L2Character) _fakePlayer.getTarget());
	}
	
	protected void castSelfSpell(L2Skill skill) 
	{
		if (!_fakePlayer.isCastingNow() && !_fakePlayer.isSkillDisabled(skill))
		{		
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fakePlayer.doCast(skill);
		}
	}
	
	protected void toVillageOnDeath() 
	{
		Location location = MapRegionTable.getInstance().getTeleToLocation(_fakePlayer, TeleportWhereType.Town);
		
		if (_fakePlayer.isDead())
			_fakePlayer.doRevive();
		
		_fakePlayer.getFakeAi().teleportToLocation(location.getX(), location.getY(), location.getZ(), 20);
	}
	
	protected void toPvpZoneOnDeath() 
	{
		if (_fakePlayer.isDead())
			_fakePlayer.doRevive();
		
		_fakePlayer.teleToLocation(((L2ChangePvpZone) PvPZoneManager.getInstance().getZone()).getSpawnLoc(), 20);
	}
	
	protected void clientStopMoving(L2CharPosition loc)
	{
		if (_fakePlayer.isMoving())
			_fakePlayer.stopMove(loc);
		
		_clientMovingToPawnOffset = 0;
		
		if (_clientMoving || loc != null)
		{
			_clientMoving = false;
			
			_fakePlayer.broadcastPacket(new StopMove(_fakePlayer));
			
			if (loc != null)
				_fakePlayer.broadcastPacket(new StopRotation(_fakePlayer.getObjectId(), loc.heading, 0));
		}
	}
	
	protected boolean checkTargetLost(L2Object target)
	{
		if (target instanceof L2PcInstance)
		{
			final L2PcInstance victim = (L2PcInstance) target;
			if (victim.isFakeDeath())
			{
				victim.stopFakeDeath(true);
				return false;
			}
		}
		
		if (target == null)
		{
			_fakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);
			return true;
		}
		return false;
	}
	
	protected boolean maybeMoveToPosition(Point3D worldPosition, int offset)
	{
		if (worldPosition == null)
			return false;
		
		if (offset < 0)
			return false;
			
		if (!_fakePlayer.isInsideRadius(worldPosition.getX(), worldPosition.getY(), (int) (offset + _fakePlayer.getTemplate().getCollisionRadius()), false))
		{
			if (_fakePlayer.isMovementDisabled())
				return true;
			
			int x = _fakePlayer.getX();
			int y = _fakePlayer.getY();
			
			double dx = worldPosition.getX() - x;
			double dy = worldPosition.getY() - y;
			
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			double sin = dy / dist;
			double cos = dx / dist;
			
			dist -= offset - 5;
			
			x += (int) (dist * cos);
			y += (int) (dist * sin);
			
			moveTo(x, y, worldPosition.getZ());
			return true;
		}

		return false;
	}	
	
	protected void moveToPawn(L2Object pawn, int offset)
	{
		if (!_fakePlayer.isMovementDisabled())
		{
			if (offset < 10)
				offset = 10;
			
			boolean sendPacket = true;
			if (_clientMoving && (_fakePlayer.getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout)
						return;
					
					sendPacket = false;
				}
				else if (_fakePlayer.isOnGeodataPath())
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout + 1000)
						return;
				}
			}
			
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			_fakePlayer.setTarget(pawn);
			_moveToPawnTimeout = System.currentTimeMillis() + 1000;
			
			if (pawn == null)
				return;
			
			_fakePlayer.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			
			if (!_fakePlayer.isMoving())
			{
				return;
			}
			
			if (pawn instanceof L2Character)
			{
				if (_fakePlayer.isOnGeodataPath())
				{
					_fakePlayer.broadcastPacket(new MoveToLocation(_fakePlayer));
					_clientMovingToPawnOffset = 0;
				}
				else if (sendPacket)
					_fakePlayer.broadcastPacket(new MoveToPawn(_fakePlayer, pawn, offset));
			}
			else
				_fakePlayer.broadcastPacket(new MoveToLocation(_fakePlayer));
		}
	}
	
	public void moveTo(int x, int y, int z)
	{
		if (!_fakePlayer.isMovementDisabled())
		{
			_clientMoving = true;
			_clientMovingToPawnOffset = 0;
			_fakePlayer.moveToLocation(x, y, z, 0);
			
			_fakePlayer.broadcastPacket(new MoveToLocation(_fakePlayer));
			
		}
	}
	
	protected boolean maybeMoveToPawn(L2Object target, int offset) 
	{
		if (target == null || offset < 0)
			return false;
		
		offset += _fakePlayer.getTemplate().getCollisionRadius();
		if (target instanceof L2Character)
			offset += ((L2Character) target).getTemplate().getCollisionRadius();
		
		if (!_fakePlayer.isInsideRadius(target, offset, false, false))
		{			
			if (_fakePlayer.isMovementDisabled())
			{
				if (_fakePlayer.getAI().getIntention() == CtrlIntention.ATTACK)
					_fakePlayer.getAI().setIntention(CtrlIntention.IDLE);				
				return true;
			}
			
			if (target instanceof L2Character && !(target instanceof L2DoorInstance))
			{
				if (((L2Character) target).isMoving())
					offset -= 30;
				
				if (offset < 5)
					offset = 5;
			}
			
			moveToPawn(target, offset);
			return true;
		}
		
		if(!GeoData.getInstance().canSeeTarget(_fakePlayer, _fakePlayer.getTarget()))
		{
			_fakePlayer.setIsCastingNow(false);
			moveToPawn(target, 50);			
			return true;
		}
		return false;
	}	
	
	public abstract void thinkAndAct(); 
}