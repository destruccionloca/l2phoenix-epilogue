package l2p.gameserver.skills.conditions;

import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.skills.Env;

public class ConditionTargetPlayerRace extends Condition
{
	private final Race _race;

	public ConditionTargetPlayerRace(String race)
	{
		_race = Race.valueOf(race.toLowerCase());
	}

	@Override
	protected boolean testImpl(Env env)
	{
		L2Character target = env.target;
		return target != null && target.isPlayer() && _race == ((L2Player) target).getRace();
	}
}