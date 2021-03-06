package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;

public class RequestBR_MinigameInsertScore extends L2GameClientPacket
{
	private int score;

	@Override
	protected void readImpl() throws Exception
	{
		score = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		L2Player player = getClient().getActiveChar();
		System.out.println("RequestBR_MinigameInsertScore [" + score + "] from " + player);
	}
}