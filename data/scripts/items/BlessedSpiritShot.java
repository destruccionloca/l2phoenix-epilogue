package items;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.IItemHandler;
import l2p.gameserver.handler.ItemHandler;
import l2p.gameserver.model.L2Playable;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.ExAutoSoulShot;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.tables.ItemTable;
import l2p.gameserver.templates.L2Item;
import l2p.gameserver.templates.L2Weapon;

public class BlessedSpiritShot implements IItemHandler, ScriptFile
{
	// all the items ids that this handler knowns
	private static final int[] _itemIds = { 3947, 3948, 3949, 3950, 3951, 3952, 22072, 22073, 22074, 22075, 22076 };
	private static final short[] _skillIds = { 2061, 2160, 2161, 2162, 2163, 2164 };

	public void useItem(L2Playable playable, L2ItemInstance item, Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return;
		L2Player player = (L2Player) playable;

		L2ItemInstance weaponInst = player.getActiveWeaponInstance();
		L2Weapon weaponItem = player.getActiveWeaponItem();
		int SoulshotId = item.getItemId();
		boolean isAutoSoulShot = false;
		L2Item itemTemplate = ItemTable.getInstance().getTemplate(item.getItemId());

		if(player.getAutoSoulShot().contains(SoulshotId))
			isAutoSoulShot = true;

		if(weaponInst == null)
		{
			if(!isAutoSoulShot)
				player.sendPacket(Msg.CANNOT_USE_SPIRITSHOTS);
			return;
		}

		if(weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			// already charged by blessed spirit shot
			// btw we cant charge only when bsps is charged
			return;

		int spiritshotId = item.getItemId();
		int grade = weaponItem.getCrystalType().externalOrdinal;
		int blessedsoulSpiritConsumption = weaponItem.getSpiritShotCount();
		long count = item.getCount();

		if(blessedsoulSpiritConsumption == 0)
		{
			// Can't use Spiritshots
			if(isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addString(itemTemplate.getName()));
				return;
			}
			player.sendPacket(Msg.CANNOT_USE_SPIRITSHOTS);
			return;
		}

		if(grade == 0 && spiritshotId != 3947 // NG
				|| grade == 1 && spiritshotId != 3948 && spiritshotId != 22072 // D
				|| grade == 2 && spiritshotId != 3949 && spiritshotId != 22073 // C
				|| grade == 3 && spiritshotId != 3950 && spiritshotId != 22074 // B
				|| grade == 4 && spiritshotId != 3951 && spiritshotId != 22075 // A
				|| grade == 5 && spiritshotId != 3952 && spiritshotId != 22076 // S
		)
		{
			if(isAutoSoulShot)
				return;
			player.sendPacket(Msg.SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return;
		}

		if(count < blessedsoulSpiritConsumption)
		{
			if(isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addString(itemTemplate.getName()));
				return;
			}
			player.sendPacket(Msg.NOT_ENOUGH_SPIRITSHOTS);
			return;
		}

		weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT);
		player.getInventory().destroyItem(item, blessedsoulSpiritConsumption, false);
		player.sendPacket(Msg.POWER_OF_MANA_ENABLED);
		player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade], 1, 0, 0));
	}

	public final int[] getItemIds()
	{
		return _itemIds;
	}

	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	public void onReload()
	{}

	public void onShutdown()
	{}
}