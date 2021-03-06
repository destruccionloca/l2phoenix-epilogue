package l2p.gameserver.serverpackets;

import javolution.util.FastMap;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.items.L2ItemInstance;

/**
 * ddQhdhhhhhdhhhhhhhh - Gracia Final
 */
public class ExRpItemLink extends L2GameServerPacket
{
	private static final FastMap<Integer, ItemInfo> _cache = new FastMap<Integer, ItemInfo>().setShared(true);
	private static final long cache_time = 10000;
	private ItemInfo _item = null;

	public static void addItem(int id)
	{
		if(_cache.containsKey(id))
			_cache.get(id).updateCache(L2ObjectsStorage.getItemByObjId(id), true);
		else
			_cache.put(id, new ItemInfo(id));
	}

	public ExRpItemLink(int id)
	{
		if((_item = _cache.get(id)) == null)
		{
			//FIXME
			//System.out.println("ExRpItemLink cache is null for item: " + id);
		}
		else
			_item.updateCache(L2ObjectsStorage.getItemByObjId(id), false);
	}

	@Override
	protected final void writeImpl()
	{
		if(_item == null || _item.getObjectId() == 0)
			return;
		writeC(EXTENDED_PACKET);
		writeH(0x6c);

		writeD(_item.getObjectId());
		writeD(_item.getItemId());
		writeQ(_item.getCount());
		writeH(_item.getType2());
		writeD(_item.getBodyPart());
		writeH(_item.getEnchantLevel());
		writeH(0); // unknown
		writeH(0); // unknown
		writeD(_item.getAugmentationId());
		writeD(_item.getShadowLifeTime());

		writeH(_item.getAttackElement()[0]);
		writeH(_item.getAttackElement()[1]);
		writeH(_item.getDefenceFire());
		writeH(_item.getDefenceWater());
		writeH(_item.getDefenceWind());
		writeH(_item.getDefenceEarth());
		writeH(_item.getDefenceHoly());
		writeH(_item.getDefenceUnholy());
		if(getClient().getRevision() >= 152)
		{
			writeH(0);
			writeH(0);
			writeH(0);
		}
	}

	private static class ItemInfo
	{
		private int objectId, itemId;
		private long long_count, nextUpdate;
		private short type2;
		private int bodyPart;
		private short enchantLevel;
		private int augmentationId;
		private int shadowLifeTime;
		private int[] attackElement;
		private int defenceFire;
		private int defenceWater;
		private int defenceWind;
		private int defenceEarth;
		private int defenceHoly;
		private int defenceUnholy;

		private ItemInfo(int id)
		{
			L2ItemInstance item = L2ObjectsStorage.getItemByObjId(id);
			if(item == null)
			{
				objectId = 0;
				return;
			}
			objectId = item.getObjectId();
			itemId = item.getItemId();
			type2 = (short) item.getItem().getType2ForPackets();
			bodyPart = item.getItem().getBodyPart();
			updateCache(item, true);
		}

		public synchronized void updateCache(L2ItemInstance item, boolean force)
		{
			if(item == null || item.getItemId() != itemId || !force && nextUpdate > System.currentTimeMillis())
				return;
			nextUpdate = System.currentTimeMillis() + cache_time;

			long_count = item.getCount();
			enchantLevel = (short) item.getEnchantLevel();
			augmentationId = item.getAugmentationId();
			shadowLifeTime = item.isShadowItem() ? item.getLifeTimeRemaining() : -1;
			attackElement = item.getAttackElement();
			defenceFire = item.getDefenceFire();
			defenceWater = item.getDefenceWater();
			defenceWind = item.getDefenceWind();
			defenceEarth = item.getDefenceEarth();
			defenceHoly = item.getDefenceHoly();
			defenceUnholy = item.getDefenceUnholy();
		}

		public int getObjectId()
		{
			return objectId;
		}

		public int getItemId()
		{
			return itemId;
		}

		public long getCount()
		{
			return long_count;
		}

		public short getType2()
		{
			return type2;
		}

		public int getBodyPart()
		{
			return bodyPart;
		}

		public short getEnchantLevel()
		{
			return enchantLevel;
		}

		public int getAugmentationId()
		{
			return augmentationId;
		}

		public int getShadowLifeTime()
		{
			return shadowLifeTime;
		}

		public int[] getAttackElement()
		{
			return attackElement;
		}

		public int getDefenceFire()
		{
			return defenceFire;
		}

		public int getDefenceWater()
		{
			return defenceWater;
		}

		public int getDefenceWind()
		{
			return defenceWind;
		}

		public int getDefenceEarth()
		{
			return defenceEarth;
		}

		public int getDefenceHoly()
		{
			return defenceHoly;
		}

		public int getDefenceUnholy()
		{
			return defenceUnholy;
		}
	}
}