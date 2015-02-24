package quests._138_TempleChampionPart2;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import quests._137_TempleChampionPart1._137_TempleChampionPart1;

public class _138_TempleChampionPart2 extends Quest implements ScriptFile
{
	// NPCs
	private static final int SYLVAIN = 30070;
	private static final int PUPINA = 30118;
	private static final int ANGUS = 30474;
	private static final int SLA = 30666;

	// ITEMs
	private static final int MANIFESTO = 10341;
	private static final int RELIC = 10342;
	private static final int ANGUS_REC = 10343;
	private static final int PUPINA_REC = 10344;

	// Monsters
	private final static int Wyrm = 20176;
	private final static int GuardianBasilisk = 20550;
	private final static int RoadScavenger = 20551;
	private final static int FetteredSoul = 20552;

	public CheckStatus LAST_CHECK_STATUS = CheckStatus.GRACIA_FINAL;

	public void onLoad()
	{}

	public void onReload()
	{}

	public void onShutdown()
	{}

	public _138_TempleChampionPart2()
	{
		super(false);

		// Нет стартового NPC, чтобы квест не появлялся в списке раньше времени
		addFirstTalkId(SYLVAIN);
		addTalkId(SYLVAIN, PUPINA, ANGUS, SLA);
		addKillId(Wyrm, GuardianBasilisk, RoadScavenger, FetteredSoul);
		addQuestItem(MANIFESTO, RELIC, ANGUS_REC, PUPINA_REC);
	}

	@Override
	public String onFirstTalk(L2NpcInstance npc, L2Player player)
	{
		QuestState qs = player.getQuestState(_137_TempleChampionPart1.class);
		if(qs != null && qs.isCompleted() && player.getQuestState(getClass()) == null)
			newQuestState(player, STARTED);
		return "";
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("sylvain_q0138_04.htm"))
		{
			st.set("cond", "1");
			st.playSound(SOUND_ACCEPT);
			st.giveItems(MANIFESTO, 1);
		}
		else if(event.equalsIgnoreCase("sylvain_q0138_09.htm"))
		{
			st.addExpAndSp(187062, 11307);
			st.giveItems(ADENA_ID, 84593);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("sylvain_q0138_06.htm"))
		{
			st.set("cond", "2");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("pupina_q0138_08.htm"))
		{
			st.set("cond", "3");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("pupina_q0138_11.htm"))
		{
			st.set("cond", "6");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
			st.giveItems(PUPINA_REC, 1);
		}
		else if(event.equalsIgnoreCase("grandmaster_angus_q0138_03.htm"))
		{
			st.set("cond", "4");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("preacher_sla_q0138_03.htm"))
		{
			st.set("talk", "1");
			st.takeItems(PUPINA_REC, -1);
		}
		else if(event.equalsIgnoreCase("preacher_sla_q0138_05.htm"))
		{
			st.set("talk", "2");
			st.takeItems(MANIFESTO, -1);
		}
		else if(event.equalsIgnoreCase("preacher_sla_q0138_12.htm"))
		{
			st.set("cond", "7");
			st.playSound(SOUND_MIDDLE);
			st.unset("talk");
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");

		if(npcId == SYLVAIN)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 36)
					htmltext = "sylvain_q0138_01.htm";
				else
					htmltext = "sylvain_q0138_03.htm";
			}
			else if(cond == 1)
				htmltext = "sylvain_q0138_04.htm";
			else if(cond >= 2 && cond <= 6)
				htmltext = "sylvain_q0138_06.htm";
			else if(cond == 7)
				htmltext = "sylvain_q0138_08.htm";
		}
		else if(npcId == PUPINA)
		{
			if(cond == 2)
				htmltext = "pupina_q0138_02.htm";
			else if(cond == 3 || cond == 4)
				htmltext = "pupina_q0138_09.htm";
			else if(cond == 5)
			{
				htmltext = "pupina_q0138_10.htm";
				st.takeItems(ANGUS_REC, -1);
			}
			else if(cond == 6)
				htmltext = "pupina_q0138_13.htm";
		}
		else if(npcId == ANGUS)
		{
			if(cond == 3)
				htmltext = "grandmaster_angus_q0138_02.htm";
			else if(cond == 4)
			{
				if(st.getQuestItemsCount(RELIC) >= 10)
				{
					htmltext = "grandmaster_angus_q0138_05.htm";
					st.takeItems(RELIC, -1);
					st.giveItems(ANGUS_REC, 1);
					st.set("cond", "5");
					st.playSound(SOUND_MIDDLE);
				}
				else
					htmltext = "grandmaster_angus_q0138_04.htm";
			}
			else if(cond == 5)
				htmltext = "grandmaster_angus_q0138_06.htm";
		}
		else if(npcId == SLA)
			if(cond == 6)
			{
				if(st.getInt("talk") == 0)
					htmltext = "preacher_sla_q0138_02.htm";
				else if(st.getInt("talk") == 1)
					htmltext = "preacher_sla_q0138_03.htm";
				else if(st.getInt("talk") == 2)
					htmltext = "preacher_sla_q0138_05.htm";
			}
			else if(cond == 7)
				htmltext = "preacher_sla_q0138_13.htm";

		return htmltext;
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		if(st.getState() != STARTED)
			return null;
		if(st.getInt("cond") == 4)
			if(st.getQuestItemsCount(RELIC) < 10)
			{
				st.giveItems(RELIC, 1);
				if(st.getQuestItemsCount(RELIC) >= 10)
					st.playSound(SOUND_MIDDLE);
				else
					st.playSound(SOUND_ITEMGET);
			}
		return null;
	}
}