package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;
import l2p.gameserver.serverpackets.SendTradeDone;

public class RequestPrivateStoreQuitBuy extends L2GameClientPacket
{
	@Override
	public void readImpl()
	{}

	@Override
	public void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
			return;
		if(activeChar.getTradeList() != null)
		{
			activeChar.getTradeList().removeAll();
			activeChar.sendPacket(new SendTradeDone(0));
			activeChar.setTradeList(null);
			if(activeChar.isInTransaction())
				activeChar.getTransaction().cancel();
		}
		activeChar.setPrivateStoreType(L2Player.STORE_PRIVATE_NONE);
		activeChar.standUp();
		activeChar.broadcastUserInfo(true);
	}
}