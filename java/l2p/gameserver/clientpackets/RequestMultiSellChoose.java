package l2p.gameserver.clientpackets;

import java.nio.BufferUnderflowException;
import java.util.logging.Logger;

import l2p.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.L2Multisell;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Multisell.MultiSellListContainer;
import l2p.gameserver.model.base.L2Augmentation;
import l2p.gameserver.model.base.MultiSellEntry;
import l2p.gameserver.model.base.MultiSellIngredient;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.serverpackets.ExPCCafePointInfo;
import l2p.gameserver.serverpackets.StatusUpdate;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.tables.ItemTable;
import l2p.gameserver.tables.PetDataTable;
import l2p.gameserver.templates.L2Item;
import l2p.util.GArray;
import l2p.util.Log;
import l2p.util.SafeMath;
import l2p.util.Util;

public class RequestMultiSellChoose extends L2GameClientPacket
{
	// format: cdddhdddddddddd
	private static Logger _log = Logger.getLogger(RequestMultiSellChoose.class.getName());
	private int _listId;
	private int _entryId;
	private long _amount;
	private int _enchant = 0;
	private byte _enchantAttr = L2Item.ATTRIBUTE_NONE;
	private int _enchantAttrVal = 0;
	private boolean _keepenchant = false;
	private boolean _notax = false;
	private MultiSellListContainer _list = null;
	private GArray<ItemData> _items = new GArray<ItemData>();

	private class ItemData
	{
		private final int _id;
		private final long _count;
		private final L2ItemInstance _item;

		public ItemData(int id, long count, L2ItemInstance item)
		{
			_id = id;
			_count = count;
			_item = item;
		}

		public int getId()
		{
			return _id;
		}

		public long getCount()
		{
			return _count;
		}

		public L2ItemInstance getItem()
		{
			return _item;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof ItemData))
				return false;

			ItemData i = (ItemData) obj;

			return _id == i._id && _count == i._count && _item == i._item;
		}
	}

	@Override
	public void readImpl()
	{
		try
		{
			_listId = readD();
			_entryId = readD();
			_amount = readQ();
		}
		catch(BufferUnderflowException e)
		{
			_log.warning(getClient().getLoginName() + " maybe packet cheater!");
		}
	}

	@Override
	public void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
			return;

		if(!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && activeChar.getKarma() > 0 && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}

		_list = activeChar.getMultisell();

		// На всякий случай...
		if(_list == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		// Проверяем, не подменили ли id
		if(activeChar.getMultisell().getListId() != _listId)
		{
			Util.handleIllegalPlayerAction(activeChar, "RequestMultiSellChoose[110]", "Tried to buy from multisell: " + _listId, 1);
			return;
		}

		if(_amount < 1)
		{
			activeChar.sendActionFailed();
			return;
		}

		_keepenchant = _list.isKeepEnchant();
		_notax = _list.isNoTax();

		for(MultiSellEntry entry : _list.getEntries())
			if(entry.getEntryId() == _entryId)
			{
				doExchange(activeChar, entry);
				break;
			}
	}

	private void doExchange(L2Player activeChar, MultiSellEntry entry)
	{
		PcInventory inv = activeChar.getInventory();

		int totalAdenaCost = 0;
		long tax;
		try
		{
			tax = SafeMath.safeMulLong(entry.getTax(), _amount);
		}
		catch(ArithmeticException e)
		{
			return;
		}
		L2NpcInstance merchant = activeChar.getLastNpc();
		Castle castle = merchant != null ? merchant.getCastle(activeChar) : null;

		GArray<MultiSellIngredient> productId = entry.getProduction();
		if(_keepenchant)
			for(MultiSellIngredient p : productId)
				_enchant = Math.max(_enchant, p.getItemEnchant());
		boolean logExchange = Config.LOG_MULTISELL_ID_LIST.contains(_listId);
		StringBuffer msgb = new StringBuffer();

		if(logExchange)
			msgb.append("<multisell id=").append(_listId).append(" player=\"").append(activeChar.getName()).append("\" oid=").append(activeChar.getObjectId()).append(">\n");

		synchronized (inv)
		{
			int slots = inv.slotsLeft();
			if(slots == 0)
			{
				activeChar.sendPacket(Msg.THE_WEIGHT_AND_VOLUME_LIMIT_OF_INVENTORY_MUST_NOT_BE_EXCEEDED);
				return;
			}

			int req = 0;
			long totalLoad = 0;
			for(MultiSellIngredient i : productId)
			{
				if(i.getItemId() <= 0)
					continue;
				totalLoad += ItemTable.getInstance().getTemplate(i.getItemId()).getWeight() * _amount;
				if(!ItemTable.getInstance().getTemplate(i.getItemId()).isStackable())
					req += _amount;
				else
					req++;
			}
			if(req > slots || !inv.validateWeight(totalLoad))
			{
				activeChar.sendPacket(Msg.THE_WEIGHT_AND_VOLUME_LIMIT_OF_INVENTORY_MUST_NOT_BE_EXCEEDED);
				return;
			}

			if(entry.getIngredients().size() == 0)
			{
				System.out.println("WARNING Ingredients list = 0 multisell id=:" + _listId + " player:" + activeChar.getName());
				activeChar.sendActionFailed();
				return;
			}

			L2Augmentation augmentation = null;

			// Перебор всех ингридиентов, проверка наличия и создание списка забираемого
			for(MultiSellIngredient ingridient : entry.getIngredients())
			{
				int ingridientItemId = ingridient.getItemId();
				long ingridientItemCount = ingridient.getItemCount();
				long total_amount;
				try
				{
					total_amount = SafeMath.safeMulLong(ingridientItemCount, _amount);
				}
				catch(ArithmeticException e)
				{
					activeChar.sendActionFailed();
					return;
				}

				if(ingridientItemId > 0 && !ItemTable.getInstance().getTemplate(ingridientItemId).isStackable())
					for(int i = 0; i < ingridientItemCount * _amount; i++)
					{
						L2ItemInstance[] list = inv.getAllItemsById(ingridientItemId);
						// Если энчант имеет значение - то ищем вещи с точно таким энчантом
						if(_keepenchant)
						{
							L2ItemInstance itemToTake = null;
							for(L2ItemInstance itm : list)
								if((itm.getEnchantLevel() == _enchant || itm.getItem().getType2() > 2) && !_items.contains(new ItemData(itm.getItemId(), itm.getCount(), itm)) && !itm.isShadowItem() && !itm.isTemporalItem() && (itm.getCustomFlags() & L2ItemInstance.FLAG_NO_TRADE) != L2ItemInstance.FLAG_NO_TRADE)
								{
									itemToTake = itm;
									if(itm.getAttributeElement() != L2Item.ATTRIBUTE_NONE)
									{
										_enchantAttr = itm.getAttributeElement();
										_enchantAttrVal = itm.getAttributeElementValue();
									}
									break;
								}

							if(itemToTake == null)
							{
								activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
								return;
							}

							if(!checkItem(itemToTake, activeChar))
							{
								activeChar.sendActionFailed();
								return;
							}

							if(itemToTake.getAugmentation() != null)
								augmentation = itemToTake.getAugmentation();
							_items.add(new ItemData(itemToTake.getItemId(), 1, itemToTake));
						}
						// Если энчант не обрабатывается берется вещь с наименьшим энчантом
						else
						{
							L2ItemInstance itemToTake = null;
							for(L2ItemInstance itm : list)
								if(!_items.contains(new ItemData(itm.getItemId(), itm.getCount(), itm)) && (itemToTake == null || itm.getEnchantLevel() < itemToTake.getEnchantLevel()) && !itm.isShadowItem() && !itm.isTemporalItem() && (itm.getCustomFlags() & L2ItemInstance.FLAG_NO_TRADE) != L2ItemInstance.FLAG_NO_TRADE && checkItem(itm, activeChar))
								{
									itemToTake = itm;
									if(itemToTake.getEnchantLevel() == 0)
										break;
								}

							if(itemToTake == null)
							{
								activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
								return;
							}
							if(itemToTake.getAugmentation() != null)
								augmentation = itemToTake.getAugmentation();
							_items.add(new ItemData(itemToTake.getItemId(), 1, itemToTake));
						}
					}
				else if(ingridientItemId == L2Item.ITEM_ID_CLAN_REPUTATION_SCORE)
				{
					if(activeChar.getClan() == null)
					{
						activeChar.sendPacket(Msg.YOU_ARE_NOT_A_CLAN_MEMBER);
						return;
					}

					if(activeChar.getClan().getReputationScore() < total_amount)
					{
						activeChar.sendPacket(Msg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
						return;
					}

					if(activeChar.getClan().getLeaderId() != activeChar.getObjectId())
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addString(activeChar.getName()));
						return;
					}
					_items.add(new ItemData(ingridientItemId, total_amount, null));
				}
				else if(ingridientItemId == L2Item.ITEM_ID_PC_BANG_POINTS)
				{
					if(activeChar.getPcBangPoints() < total_amount)
					{
						activeChar.sendPacket(Msg.YOU_ARE_SHORT_OF_ACCUMULATED_POINTS);
						return;
					}
					_items.add(new ItemData(ingridientItemId, total_amount, null));
				}
				else if(ingridientItemId == L2Item.ITEM_ID_FAME)
				{
					if(activeChar.getFame() < total_amount)
					{
						activeChar.sendPacket(Msg.NOT_ENOUGH_FAME_POINTS);
						return;
					}
					_items.add(new ItemData(ingridientItemId, total_amount, null));
				}
				else
				{
					if(ingridientItemId == 57)
						totalAdenaCost += ingridientItemCount * _amount;
					L2ItemInstance item = inv.getItemByItemId(ingridientItemId);

					if(item == null || item.getCount() < total_amount)
					{
						activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
						return;
					}

					_items.add(new ItemData(item.getItemId(), total_amount, item));
				}

				if(activeChar.getAdena() < totalAdenaCost)
				{
					activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
			}

			for(ItemData id : _items)
			{
				long count = id.getCount();
				if(count > 0)
				{
					L2ItemInstance item = id.getItem();

					if(item != null)
					{
						activeChar.sendPacket(SystemMessage.removeItems(item.getItemId(), count));
						if(logExchange)
							msgb.append("\t<destroy id=").append(item.getItemId()).append(" oid=").append(item.getObjectId()).append(" count=").append(count).append(">\n");

						if(item.isEquipped())
							inv.unEquipItemInSlot(item.getEquipSlot());
						inv.destroyItem(item, count, true);
					}
					else if(id.getId() == L2Item.ITEM_ID_CLAN_REPUTATION_SCORE)
					{
						activeChar.getClan().incReputation((int) -count, false, "MultiSell" + _listId);
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_THE_CLAN_REPUTATION_SCORE).addNumber(count));
					}
					else if(id.getId() == L2Item.ITEM_ID_PC_BANG_POINTS)
					{
						activeChar.setPcBangPoints(activeChar.getPcBangPoints() - (int) count);
						activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_USING_S1_POINT).addNumber(count), new ExPCCafePointInfo(activeChar));
					}
					else if(id.getId() == L2Item.ITEM_ID_FAME)
					{
						activeChar.setFame(activeChar.getFame() - (int) count, "MultiSell" + _listId);
						activeChar.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addNumber(count).addString("Fame"));
					}
				}
			}

			if(tax > 0 && !_notax)
				if(castle != null)
				{
					activeChar.sendMessage("Tax: " + tax);
					if(merchant != null && merchant.getReflection().getId() == 0)
					{
						castle.addToTreasury(tax, true, false);
						Log.add(castle.getName() + "|" + tax + "|Multisell", "treasury");
					}
				}

			for(MultiSellIngredient in : productId)
				if(in.getItemId() <= 0)
				{
					if(in.getItemId() == L2Item.ITEM_ID_CLAN_REPUTATION_SCORE)
					{
						activeChar.getClan().incReputation((int) (in.getItemCount() * _amount), false, "MultiSell" + _listId);
						activeChar.sendPacket(new SystemMessage(SystemMessage.YOUR_CLAN_HAS_ADDED_1S_POINTS_TO_ITS_CLAN_REPUTATION_SCORE).addNumber(in.getItemCount() * _amount));
					}
					else if(in.getItemId() == L2Item.ITEM_ID_PC_BANG_POINTS)
					{
						activeChar.setPcBangPoints(activeChar.getPcBangPoints() + (int) (in.getItemCount() * _amount));
						activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ACQUIRED_S1_PC_BANG_POINT).addNumber(in.getItemCount() * _amount), new ExPCCafePointInfo(activeChar));
					}
					else if(in.getItemId() == L2Item.ITEM_ID_FAME)
						activeChar.setFame(activeChar.getFame() + (int) (in.getItemCount() * _amount), "MultiSell" + _listId);
				}
				else if(ItemTable.getInstance().getTemplate(in.getItemId()).isStackable())
				{
					L2ItemInstance product = ItemTable.getInstance().createItem(in.getItemId());
					double total = in.getItemCount() * _amount;

					if(total < 0 || total > Long.MAX_VALUE)
					{
						activeChar.sendActionFailed();
						return;
					}

					product.setCount((long) total);
					activeChar.sendPacket(SystemMessage.obtainItems(product));
					if(logExchange)
						msgb.append("\t<add id=").append(product.getItemId()).append(" count=").append(product.getCount()).append(">\n");
					inv.addItem(product);
				}
				else
					for(int i = 0; i < _amount; i++)
					{
						L2ItemInstance product = inv.addItem(ItemTable.getInstance().createItem(in.getItemId()));
						if(_keepenchant)
						{
							product.setEnchantLevel(_enchant);
							if(_enchantAttr != L2Item.ATTRIBUTE_NONE)
								product.setAttributeElement(_enchantAttr, _enchantAttrVal, true);
						}
						if(in.getElementValue() > 0)
							product.setAttributeElement(in.getElement(), in.getElementValue(), true);
						if(augmentation != null && product.isEquipable() && product.canBeEnchanted() && !product.isRaidAccessory())
							product.setAugmentation(augmentation);
						if(logExchange)
							msgb.append("\t<add id=").append(product.getItemId()).append(" oid=").append(product.getObjectId()).append(" count=").append(product.getCount()).append(">\n");

						activeChar.sendPacket(SystemMessage.obtainItems(product));
					}
		}

		activeChar.sendStatusUpdate(false, StatusUpdate.CUR_LOAD);
		if(logExchange)
		{
			msgb.append("</multisell>\n");
			Log.add(msgb.toString(), "multisell", "");
			msgb = null;
		}

		if(_list == null || !_list.isShowAll()) // Если показывается только то, на что хватает материалов обновить окно у игрока
			L2Multisell.getInstance().SeparateAndSend(_listId, activeChar, castle == null ? 0 : castle.getTaxRate());
	}

	private boolean checkItem(L2ItemInstance temp, L2Player activeChar)
	{
		if(temp == null)
			return false;

		if(temp.isHeroWeapon())
			return false;

		if(temp.isShadowItem())
			return false;

		if(temp.isTemporalItem())
			return false;

		if(PetDataTable.isPetControlItem(temp) && activeChar.isMounted())
			return false;

		if(activeChar.getPet() != null && temp.getObjectId() == activeChar.getPet().getControlItemObjId())
			return false;

		if(temp.isEquipped())
			return false;

		if(temp.isWear())
			return false;

		if(activeChar.getEnchantScroll() == temp)
			return false;

		return true;
	}
}