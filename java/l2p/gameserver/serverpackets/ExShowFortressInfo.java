package l2p.gameserver.serverpackets;

import l2p.gameserver.instancemanager.FortressManager;
import l2p.gameserver.model.entity.residence.Fortress;
import l2p.gameserver.model.entity.siege.SiegeClanType;
import l2p.gameserver.tables.ClanTable;
import l2p.util.GArray;

public class ExShowFortressInfo extends L2GameServerPacket
{
	private GArray<FortressInfo> _infos = new GArray<FortressInfo>();

	public ExShowFortressInfo()
	{
		int id, status, siege;
		String owner;

		for(Fortress fortress : FortressManager.getInstance().getFortresses().values())
		{
			id = fortress.getId();
			owner = ClanTable.getInstance().getClanName(fortress.getOwnerId());
			// Если число атакующих больше нуля, значит скоро начнется осада
			status = fortress.getSiege().getAttackerClans().isEmpty() ? 0 : 1; //status? 0 - At Peace, 1 - In War
			siege = fortress.getOwnerId() == 0 ? 0 : (int) (System.currentTimeMillis() / 1000 - fortress.getOwnDate());
			_infos.add(new FortressInfo(owner, id, status, siege)); //time held в секундах
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0x15);
		writeD(_infos.size());
		for(FortressInfo _info : _infos)
		{
			writeD(_info._id);
			writeS(_info._owner);
			writeD(_info._status);
			writeD(_info._siege);
		}
		_infos.clear();
	}

	static class FortressInfo
	{
		public int _id, _status, _siege;
		public String _owner;

		public FortressInfo(String owner, int id, int status, int siege)
		{
			_owner = owner;
			_id = id;
			_status = status;
			_siege = siege;
		}
	}
}