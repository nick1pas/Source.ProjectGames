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
import phantom.model.SupportSpell;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.util.Rnd;

public class SaggitariusAI extends CombatAI implements IConsumableSpender
{
	public SaggitariusAI(FakePlayer character)
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

		selfSupportBuffs();
		handleConsumable(_fakePlayer, getArrowId());
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FakeHelpers.getTestTargetClass(), FakeHelpers.getTestTargetRange());
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected double changeOfUsingSkill() 
	{
		return 0.15;
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	protected List<OffensiveSpell> getOffensiveSpells()
	{
		List<OffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OffensiveSpell(101, 1));
		_offensiveSpells.add(new OffensiveSpell(343, 1));
		return _offensiveSpells;
	}

	@Override
	protected List<HealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}

	@Override
	protected List<SupportSpell> getSelfSupportSpells()
	{
		List<SupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SupportSpell(99, 1));
		return _selfSupportSpells;
	}
}