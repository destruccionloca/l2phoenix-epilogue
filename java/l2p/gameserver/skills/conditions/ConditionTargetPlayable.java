package l2p.gameserver.skills.conditions;

import l2p.gameserver.model.L2Playable;
import l2p.gameserver.skills.Env;

public class ConditionTargetPlayable extends Condition
{
	private final boolean _flag;

	public ConditionTargetPlayable(boolean flag)
	{
		_flag = flag;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.target instanceof L2Playable == _flag;
	}
}
