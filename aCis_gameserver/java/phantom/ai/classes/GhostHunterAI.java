package phantom.ai.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phantom.FakePlayer;
import phantom.ai.CombatAI;
import phantom.helpers.FakeHelpers;
import phantom.model.HealingSpell;
import phantom.model.OffensiveSpell;
import phantom.model.SupportSpell;
import net.sf.l2j.gameserver.model.ShotType;

public class GhostHunterAI extends CombatAI
{
	public GhostHunterAI(FakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		handleShots();			
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
	public List<OffensiveSpell> getOffensiveSpells()
	{
		List<OffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OffensiveSpell(263, 1));
		_offensiveSpells.add(new OffensiveSpell(122, 1));
		_offensiveSpells.add(new OffensiveSpell(11, 1));
		_offensiveSpells.add(new OffensiveSpell(410, 1));
		_offensiveSpells.add(new OffensiveSpell(12, 1));
		_offensiveSpells.add(new OffensiveSpell(321, 1));
		_offensiveSpells.add(new OffensiveSpell(344, 1));
		_offensiveSpells.add(new OffensiveSpell(358, 1));		
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
		return Collections.emptyList();
	}
}