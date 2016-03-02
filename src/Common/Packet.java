package Common;

import java.io.Serializable;

public class Packet implements Serializable{
	private static final long serialVersionUID = 9078917536788485396L;
	public PacketType packetType;
	public String message;
	public String target;
	public String from;
}
