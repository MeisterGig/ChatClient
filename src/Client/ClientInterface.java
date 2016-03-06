package Client;

import Common.Packet;

public interface ClientInterface {
	public void receiveMessage(Packet p);
	public void receiveBroadcast(Packet p);
	public void logout();
}
