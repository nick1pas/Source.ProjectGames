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
package custom.PuzzleEvent;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

public class PuzzleEvent extends Quest
{
	private static final String qn = "PuzzleEvent";
	
	//NPC ID
	private static final int ID_NPC = 104;
	
	//Elf quebra cabeça
	private static final int PUZZLE_ELF_A = 9570;
	private static final int PUZZLE_ELF_B = 9571;
	private static final int PUZZLE_ELF_C = 9572;
	private static final int PUZZLE_ELF_D = 9573;
	private static final int PUZZLE_ELF_E = 9574;
	private static final int PUZZLE_ELF_F = 9575;
	private static final int PUZZLE_ELF_G = 9576;
	private static final int PUZZLE_ELF_H = 9577;
	
	//Lineage quebra cabeça
	private static final int PUZZLE_LINEAGE_A = 9578;
	private static final int PUZZLE_LINEAGE_B = 9579;
	private static final int PUZZLE_LINEAGE_C = 9580;
	private static final int PUZZLE_LINEAGE_D = 9581;
	private static final int PUZZLE_LINEAGE_E = 9582;
	private static final int PUZZLE_LINEAGE_F = 9583;
	private static final int PUZZLE_LINEAGE_G = 9584;
	private static final int PUZZLE_LINEAGE_H = 9585;
	
	public PuzzleEvent()
	{
		super(-1, qn, "custom");
		
		addStartNpc(ID_NPC);
		addTalkId(ID_NPC);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		return "puzzle-event.htm";
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		
		if (event.equalsIgnoreCase("puzzle_elf"))
		{
			if (st.hasQuestItems(PUZZLE_ELF_A) && st.hasQuestItems(PUZZLE_ELF_B) && st.hasQuestItems(PUZZLE_ELF_C) && st.hasQuestItems(PUZZLE_ELF_D) && st.hasQuestItems(PUZZLE_ELF_E) && st.hasQuestItems(PUZZLE_ELF_F) && st.hasQuestItems(PUZZLE_ELF_G) && st.hasQuestItems(PUZZLE_ELF_H))
			{
				//Pega as peças
				st.takeItems(PUZZLE_ELF_A, 1);
				st.takeItems(PUZZLE_ELF_B, 1);
				st.takeItems(PUZZLE_ELF_C, 1);
				st.takeItems(PUZZLE_ELF_D, 1);
				st.takeItems(PUZZLE_ELF_E, 1);
				st.takeItems(PUZZLE_ELF_F, 1);
				st.takeItems(PUZZLE_ELF_G, 1);
				st.takeItems(PUZZLE_ELF_H, 1);
				//Paga 5 event coin
				player.addItem("Event Coin", 9501, 5, player, true);
			}
			else
				htmltext = "no-puzzle.htm";
		}
		else if (event.equalsIgnoreCase("puzzle_lineage"))
		{
			if (st.hasQuestItems(PUZZLE_LINEAGE_A) && st.hasQuestItems(PUZZLE_LINEAGE_B) && st.hasQuestItems(PUZZLE_LINEAGE_C) && st.hasQuestItems(PUZZLE_LINEAGE_D) && st.hasQuestItems(PUZZLE_LINEAGE_E) && st.hasQuestItems(PUZZLE_LINEAGE_F) && st.hasQuestItems(PUZZLE_LINEAGE_G) && st.hasQuestItems(PUZZLE_LINEAGE_H))
			{
				//Pega as peças
				st.takeItems(PUZZLE_LINEAGE_A, 1);
				st.takeItems(PUZZLE_LINEAGE_B, 1);
				st.takeItems(PUZZLE_LINEAGE_C, 1);
				st.takeItems(PUZZLE_LINEAGE_D, 1);
				st.takeItems(PUZZLE_LINEAGE_E, 1);
				st.takeItems(PUZZLE_LINEAGE_F, 1);
				st.takeItems(PUZZLE_LINEAGE_G, 1);
				st.takeItems(PUZZLE_LINEAGE_H, 1);
				//Paga 5 event coin
				player.addItem("Event Coin", 9501, 5, player, true);
			}
			else
				htmltext = "no-puzzle.htm";
		}
		st.exitQuest(true);
		return htmltext;
	}

	public static void main(String[] args)
	{
		new PuzzleEvent();
	}
}
