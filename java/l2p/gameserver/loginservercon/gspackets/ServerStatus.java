package l2p.gameserver.loginservercon.gspackets;

import l2p.gameserver.loginservercon.Attribute;
import l2p.util.GArray;

public class ServerStatus extends GameServerBasePacket
{

	public ServerStatus(GArray<Attribute> attributes)
	{
		writeC(0x06);
		writeD(attributes.size());
		for(Attribute temp : attributes)
		{
			writeD(temp.id);
			writeD(temp.value);
		}

		attributes.clear();
	}
}