package l2p.gameserver.serverpackets;

public class ExShowLines extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0xA5);
		// TODO hdcc cx[ddd]
	}
}