package phantom.ai.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phantom.FakePlayer;
import phantom.FakePlayerConfig;
import phantom.ai.CombatAI;
import phantom.ai.addon.IConsumableSpender;
import phantom.helpers.FakeHelpers;
import phantom.model.HealingSpell;
import phantom.model.OffensiveSpell;
import phantom.model.SpellUsageCondition;
import phantom.model.SupportSpell;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.util.Rnd;

public class DuelistAI extends CombatAI implements IConsumableSpender 
{
	public DuelistAI(FakePlayer character) 
	{
		super(character);
	}

	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		
		ThreadPoolManager.getInstance().scheduleAi(new Runnable()
		{
			@Override
			public void run()
			{
				_fakePlayer.despawnPlayer();
			}
			
		}, Rnd.get(FakePlayerConfig.DESPAWN_PVP_RANDOM_TIME_1 * 60 * 1000, FakePlayerConfig.DESPAWN_PVP_RANDOM_TIME_2 * 60 * 1000));

		handleShots();
		selfSupportBuffs();
		tryTargetRandomCreatureByTypeInRadius(FakeHelpers.getTestTargetClass(), FakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}

	@Override
	protected ShotType getShotType() 
	{
		return ShotType.SOULSHOT;
	}	
	
	@Override
	protected double changeOfUsingSkill() 
	{
		return 0.5;
	}

	@Override
	protected List<OffensiveSpell> getOffensiveSpells() 
	{
		List<OffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OffensiveSpell(345, 1));
		_offensiveSpells.add(new OffensiveSpell(261, 2));		
		_offensiveSpells.add(new OffensiveSpell(5, 3));		
		_offensiveSpells.add(new OffensiveSpell(6, 4));		
		_offensiveSpells.add(new OffensiveSpell(1, 5));
		return _offensiveSpells;
	}
	
	@Override
	protected List<SupportSpell> getSelfSupportSpells() 
	{
		List<SupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SupportSpell(139, 1));
		_selfSupportSpells.add(new SupportSpell(297, 2));
		_selfSupportSpells.add(new SupportSpell(440, SpellUsageCondition.MISSINGCP, 1000, 3));
		return _selfSupportSpells;
	}

	@Override
	protected List<HealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}
}