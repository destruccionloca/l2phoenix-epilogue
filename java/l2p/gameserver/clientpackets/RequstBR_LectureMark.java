package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;

public class RequstBR_LectureMark extends L2GameClientPacket
{
	private int mark;

	@Override
	protected void readImpl() throws Exception
	{
		mark = readC();
	}

	@Override
	protected void runImpl() throws Exception
	{
		L2Player player = getClient().getActiveChar();
		System.out.println("RequstBR_LectureMark [" + mark + "] from " + player);
	}
}