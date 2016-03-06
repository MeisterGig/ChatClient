package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Common.Packet;
import Common.PacketType;

public class Client {
	private String name;
	private Socket socket;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private Thread receiveThread;
	
	private ClientInterface clientInterface;
	
	public Client(String ip, int port, String name, ClientInterface clientInterface){
		try {
			this.name = name;
			this.clientInterface = clientInterface;
			socket = new Socket(ip, port);
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(socket.getInputStream());
			receiveThread = new Thread(new Runnable() {				
				public void run() {
					while(true){
						try {
							receive((Packet)is.readObject());
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			receiveThread.start();			
			Packet p = new Packet();
			p.packetType=PacketType.LOGIN;
			p.message=name;
			os.writeObject(p);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String target, String message){
		Packet p = new Packet();
		p.packetType=PacketType.MESSAGE;
		p.target=target;
		p.message=message;
		p.from=name;
		sendPacket(p);
	}
	
	public void sendBroadcast(String message){
		Packet p = new Packet();
		p.packetType=PacketType.BROADCAST;
		p.message=message;
		p.from = name;
		sendPacket(p);
	}
	
	public void logout(){
		Packet p = new Packet();
		p.packetType=PacketType.LOGOUT;
		receiveThread.stop();
		sendPacket(p);
		close();
	}
	
	private void sendPacket(Packet p){
		try {
			os.writeObject(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receive(Packet packet){
		if(packet.packetType==PacketType.LOGOUT){
			close();
			clientInterface.logout();
		}else if(packet.packetType==PacketType.MESSAGE){
			clientInterface.receiveMessage(packet);
		}else if(packet.packetType==PacketType.BROADCAST){
			clientInterface.receiveBroadcast(packet);
		}
	}
	
	public void close(){
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
