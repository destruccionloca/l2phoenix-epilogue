package l2p.gameserver.skills.effects;

import l2p.gameserver.model.L2Effect;
import l2p.gameserver.serverpackets.FinishRotating;
import l2p.gameserver.serverpackets.StartRotating;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.skills.Env;

public class EffectTurner extends L2Effect
{
	public EffectTurner(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		getEffected().broadcastPacket(new StartRotating(getEffected(), getEffected().getHeading(), 1, 65535));
		getEffected().broadcastPacket(new FinishRotating(getEffected(), getEffector().getHeading(), 65535));
		getEffected().setHeading(getEffector().getHeading());
		getEffected().sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(_displayId, _displayLevel));
		getEffected().startStunning();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		getEffected().stopStunning();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}