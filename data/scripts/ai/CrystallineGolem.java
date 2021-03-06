package ai;

import java.util.HashMap;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * @author Diamond
 */
public class CrystallineGolem extends Fighter
{
	private static final int CORAL_GARDEN_SECRETGATE = 24220026; // Tears Door

	private static final int Crystal_Fragment = 9693;

	private L2ItemInstance itemToConsume = null;
	private Location lastPoint = null;

	private static String[] says = new String[] { "Ням, ням!!!", "Дай!!!", "Хочу!!!", "Моe!!!", "Еще!!!", "Еда!!!" };

	private static String[] says2 = new String[] { "Отдай!!!", "Верни!!!", "Жадные вы, уйду я от вас...",
			"Куда оно подевалось?", "Наверное показалось...", "Оо", "ZzZzzz" };

	private static HashMap<Long, Info> instanceInfo = new HashMap<Long, Info>();

	private static class Info
	{
		boolean stage1 = false;
		boolean stage2 = false;
	}

	public CrystallineGolem(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		L2NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;

		if(_def_think)
		{
			doTask();
			return true;
		}

		if(itemToConsume != null)
			if(itemToConsume.isVisible())
			{
				itemToConsume.pickupMe(actor);
				itemToConsume = null;
			}
			else
			{
				itemToConsume = null;
				Functions.npcSay(actor, says2[Rnd.get(says.length)]);
				actor.setWalking();
				addTaskMove(lastPoint, true);
				lastPoint = null;
				return true;
			}

		Info info = instanceInfo.get(actor.getReflection().getId());
		if(info == null)
		{
			info = new Info();
			instanceInfo.put(actor.getReflection().getId(), info);
		}

		boolean opened = info.stage1 && info.stage2;

		if(!info.stage1)
		{
			int dx = actor.getX() - 142999;
			int dy = actor.getY() - 151671;
			if(dx * dx + dy * dy < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage1 = true;
			}
		}

		if(!info.stage2)
		{
			int dx = actor.getX() - 139494;
			int dy = actor.getY() - 151668;
			if(dx * dx + dy * dy < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage2 = true;
			}
		}

		if(!opened && info.stage1 && info.stage2)
			actor.getReflection().openDoor(CORAL_GARDEN_SECRETGATE);

		if(Rnd.chance(10))
			for(L2Object obj : L2World.getAroundObjects(actor, 300, 200))
				if(obj.isItem())
				{
					L2ItemInstance item = (L2ItemInstance) obj;
					if(item.getItemId() == Crystal_Fragment)
					{
						if(Rnd.chance(50))
							Functions.npcSay(actor, says[Rnd.get(says.length)]);
						itemToConsume = item;
						lastPoint = actor.getLoc();
						actor.setRunning();
						addTaskMove(item.getLoc(), false);
						return true;
					}
				}

		if(randomAnimation())
			return true;

		return false;
	}

	@Override
	protected void onEvtAttacked(L2Character attacker, int damage)
	{}

	@Override
	protected void onEvtAggression(L2Character target, int aggro)
	{}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}