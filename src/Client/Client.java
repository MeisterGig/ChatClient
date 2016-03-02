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
	private boolean running = true;
	
	public Client(String ip, int port, String name){
		try {
			this.name = name;
			socket = new Socket(ip, port);
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(socket.getInputStream());
			new Thread(new Runnable() {				
				public void run() {
					while(running){
						try {
							receive((Packet)is.readObject());
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			Packet p = new Packet();
			p.packetType=PacketType.LOGIN;
			p.message=name;
			os.writeObject(p);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		Scanner scanner = new Scanner(System.in);
		while(running){
			String[] message = scanner.next().split(";");
			if(message.length>=2){
				Packet p;
				if(message[0].equals("*")){
					p = new Packet();
					p.packetType=PacketType.BROADCAST;
					p.message=message[1];
					p.from = name;
				}else if(message[0].equals("logout")){
					p = new Packet();
					p.packetType=PacketType.LOGOUT;
					running = false;
				}else{
					p = new Packet();
					p.packetType=PacketType.MESSAGE;
					p.target=message[0];
					p.message=message[1];
					p.from=name;
				}
				try {
					os.writeObject(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		close();
		scanner.close();
	}
	
	public void receive(Packet packet){
		if(packet.packetType==PacketType.LOGOUT){
			close();
		}else if(packet.packetType==PacketType.MESSAGE|| packet.packetType==PacketType.BROADCAST){
			System.out.println(packet.from + ":" + packet.message);
		}
	}
	
	public void close(){
		try {
			running = false;
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
