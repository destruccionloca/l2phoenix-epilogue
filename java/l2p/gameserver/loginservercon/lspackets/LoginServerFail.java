package l2p.gameserver.loginservercon.lspackets;

import java.util.logging.Logger;

import l2p.gameserver.loginservercon.AttLS;

public class LoginServerFail extends LoginServerBasePacket
{
	private static Logger log = Logger.getLogger(LoginServerFail.class.getName());

	private static final String[] reasons = { "None", "Reason ip banned", "Reason ip reserved", "Reason wrong hexid",
			"Reason id reserved", "Reason no free ID", "Not authed", "Reason alreday logged in" };
	private int _reason;

	public LoginServerFail(byte[] decrypt, AttLS loginServer)
	{
		super(decrypt, loginServer);
	}

	public String getReason()
	{
		return reasons[_reason];
	}

	@Override
	public void read()
	{
		_reason = readC();

		log.info("Damn! Registration Failed: " + getReason());
		getLoginServer().getCon().restart();
	}
}