package l2p.gameserver.serverpackets;

public class ExMpccRoomInfo extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0x9B);
		// TODO ...
	}
}