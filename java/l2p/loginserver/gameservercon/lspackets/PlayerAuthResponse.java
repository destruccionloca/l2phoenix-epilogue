package l2p.loginserver.gameservercon.lspackets;

import l2p.loginserver.L2LoginClient;

public class PlayerAuthResponse extends ServerBasePacket
{
	public PlayerAuthResponse(L2LoginClient client, boolean authedOnLs)
	{
		writeC(3);
		writeS(client.getAccount());
		writeC(authedOnLs ? 1 : 0);
		writeD(client.getSessionKey().playOkID1);
		writeD(client.getSessionKey().playOkID2);
		writeD(client.getSessionKey().loginOkID1);
		writeD(client.getSessionKey().loginOkID2);
		writeS(String.valueOf(client.getBonus())); //TODO переработать на использование account_fields
		writeS(client.account_fields.serialize());
		writeD(client.getBonusExpire());
	}

	/**
	 * Если читер попытался зайти без LS, то передаем просто его имя.
	 * @param name имя читера
	 */
	public PlayerAuthResponse(String name)
	{
		writeC(3);
		writeS(name);
		writeC(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeS(""); //TODO переработать на использование account_fields
		writeS("");
		writeD(0);
	}
}
