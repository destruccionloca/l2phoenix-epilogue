package l2p.gameserver.serverpackets;

public class ExGetPremiumItemList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0x86);
		writeD(0); // count of QdQdS
	}
}