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
package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.handler.itemhandlers.BeastSoulShot;
import net.sf.l2j.gameserver.handler.itemhandlers.BeastSpice;
import net.sf.l2j.gameserver.handler.itemhandlers.BeastSpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.BlessedSpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.Book;
import net.sf.l2j.gameserver.handler.itemhandlers.BreakingArrow;
import net.sf.l2j.gameserver.handler.itemhandlers.Calculator;
import net.sf.l2j.gameserver.handler.itemhandlers.Elixir;
import net.sf.l2j.gameserver.handler.itemhandlers.EnchantScrolls;
import net.sf.l2j.gameserver.handler.itemhandlers.FishShots;
import net.sf.l2j.gameserver.handler.itemhandlers.Harvester;
import net.sf.l2j.gameserver.handler.itemhandlers.ItemSkills;
import net.sf.l2j.gameserver.handler.itemhandlers.Keys;
import net.sf.l2j.gameserver.handler.itemhandlers.Maps;
import net.sf.l2j.gameserver.handler.itemhandlers.MercTicket;
import net.sf.l2j.gameserver.handler.itemhandlers.PaganKeys;
import net.sf.l2j.gameserver.handler.itemhandlers.PetFood;
import net.sf.l2j.gameserver.handler.itemhandlers.InfinityStone;
import net.sf.l2j.gameserver.handler.itemhandlers.Recipes;
import net.sf.l2j.gameserver.handler.itemhandlers.RollingDice;
import net.sf.l2j.gameserver.handler.itemhandlers.ScrollOfResurrection;
import net.sf.l2j.gameserver.handler.itemhandlers.Seed;
import net.sf.l2j.gameserver.handler.itemhandlers.SevenSignsRecord;
import net.sf.l2j.gameserver.handler.itemhandlers.SoulCrystals;
import net.sf.l2j.gameserver.handler.itemhandlers.SoulShots;
import net.sf.l2j.gameserver.handler.itemhandlers.SpecialXMas;
import net.sf.l2j.gameserver.handler.itemhandlers.SpiritShot;
import net.sf.l2j.gameserver.handler.itemhandlers.SummonItems;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.ClanLevel;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.ClanReputation;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillAegis;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillAgility;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillClarity;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillCyclonicResistance;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillEmpowerment;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillEssence;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillFortitude;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillFreedom;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillGuidance;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillImperium;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillLifeblood;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillLuck;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillMagicProtection;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillMagmaticResistance;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillMarch;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillMight;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillMorale;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillShieldBoost;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillSpirituality;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillVigilance;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillVitality;
import net.sf.l2j.gameserver.handler.itemhandlers.clan.skills.SkillWithstandAttack;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.hero.HeroCoin;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.AchievementsBook;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.AugmentSkills;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ClassItem;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.DeletePk;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.GradeDBox;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ItemClan;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ItemGender;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ItemName;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ItemNobles;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.ItemSkins;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.special.LuckBox;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.vip.VipCoin;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;

public class ItemHandler
{
	private final Map<Integer, IItemHandler> _datatable = new HashMap<>();
	
	public static ItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected ItemHandler()
	{
		registerItemHandler(new AchievementsBook());
		registerItemHandler(new AugmentSkills());
		registerItemHandler(new BeastSoulShot());
		registerItemHandler(new BeastSpice());
		registerItemHandler(new BeastSpiritShot());
		registerItemHandler(new BlessedSpiritShot());
		registerItemHandler(new Book());
		registerItemHandler(new BreakingArrow());
		registerItemHandler(new Calculator());
		registerItemHandler(new Elixir());
		registerItemHandler(new EnchantScrolls());
		registerItemHandler(new FishShots());
		registerItemHandler(new GradeDBox());
		registerItemHandler(new Harvester());
		registerItemHandler(new ItemGender());
		registerItemHandler(new ItemSkills());
		registerItemHandler(new ItemSkins());
		registerItemHandler(new ItemNobles());
		registerItemHandler(new ItemClan());
		registerItemHandler(new ItemName());
		registerItemHandler(new Keys());
		registerItemHandler(new Maps());
		registerItemHandler(new MercTicket());
		registerItemHandler(new PaganKeys());
		registerItemHandler(new PetFood());
		registerItemHandler(new Recipes());
		registerItemHandler(new RollingDice());
		registerItemHandler(new ScrollOfResurrection());
		registerItemHandler(new Seed());
		registerItemHandler(new SevenSignsRecord());
		registerItemHandler(new SoulShots());
		registerItemHandler(new SpecialXMas());
		registerItemHandler(new SoulCrystals());
		registerItemHandler(new SpiritShot());
		registerItemHandler(new SummonItems());
		registerItemHandler(new LuckBox());
		registerItemHandler(new ClassItem());
		registerItemHandler(new DeletePk());
		registerItemHandler(new InfinityStone());
		//Coins
		registerItemHandler(new HeroCoin());
		registerItemHandler(new VipCoin());
        //Clan Items
		registerItemHandler(new ClanLevel());
		registerItemHandler(new ClanReputation());
		registerItemHandler(new SkillAegis());
		registerItemHandler(new SkillAgility());
		registerItemHandler(new SkillClarity());
		registerItemHandler(new SkillCyclonicResistance());
		registerItemHandler(new SkillEmpowerment());
		registerItemHandler(new SkillEssence());
		registerItemHandler(new SkillFortitude());
		registerItemHandler(new SkillFreedom());
		registerItemHandler(new SkillGuidance());
		registerItemHandler(new SkillImperium());
		registerItemHandler(new SkillLifeblood());
		registerItemHandler(new SkillLuck());
		registerItemHandler(new SkillMagicProtection());
		registerItemHandler(new SkillMagmaticResistance());
		registerItemHandler(new SkillMarch());
		registerItemHandler(new SkillMight());
		registerItemHandler(new SkillMorale());
		registerItemHandler(new SkillShieldBoost());
		registerItemHandler(new SkillSpirituality());
		registerItemHandler(new SkillVigilance());
		registerItemHandler(new SkillVitality());
		registerItemHandler(new SkillWithstandAttack());
	}
	
	public void registerItemHandler(IItemHandler handler)
	{
		_datatable.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}
	
	public IItemHandler getItemHandler(EtcItem item)
	{
		if (item == null || item.getHandlerName() == null)
			return null;
		
		return _datatable.get(item.getHandlerName().hashCode());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final ItemHandler _instance = new ItemHandler();
	}
}