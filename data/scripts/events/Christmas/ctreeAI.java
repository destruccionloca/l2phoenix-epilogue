package events.Christmas;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SkillTable;

public class ctreeAI extends DefaultAI
{
	public ctreeAI(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		L2NpcInstance actor = getActor();
		if(actor == null)
			return true;

		int skillId = 2139;
		for(L2Player player : L2World.getAroundPlayers(actor, 200, 200))
			if(player != null && !player.isInZonePeace() && player.getEffectList().getEffectsBySkillId(skillId) == null)
				actor.doCast(SkillTable.getInstance().getInfo(skillId, 1), player, true);
		return false;
	}

	@Override
	protected boolean randomAnimation()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}