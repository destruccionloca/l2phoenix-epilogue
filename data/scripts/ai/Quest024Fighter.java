package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.quest.Quest;

/**
 * User: Death
 */
public class Quest024Fighter extends Fighter
{
	private final String myEvent;

	public Quest024Fighter(L2Character actor)
	{
		super(actor);
		myEvent = "playerInMobRange_" + actor.getNpcId();
	}

	@Override
	protected boolean thinkActive()
	{
		Quest q = QuestManager.getQuest(24);
		if(q != null)
			for(L2Player player : L2World.getAroundPlayers(getActor(), 900, 200))
				player.processQuestEvent(q.getName(), myEvent, null);

		return super.thinkActive();
	}
}