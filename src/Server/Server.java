package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import Common.Packet;
import Common.PacketType;

public class Server {
	private HashMap<String, Client> clients;
	private int port;
	private boolean listening = true;
	
	public Server(int port){
		clients = new HashMap<String, Client>();
		this.port = port;
	}
	
	public void start(){
		try {
			ServerSocket server = new ServerSocket(port);
			while(listening){
				Socket socket = server.accept();
				ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
				String name;
				name = ((Packet)is.readObject()).message;
				if(!clients.containsKey(name)){
					Client client = new Client(this, name, is, os, socket);
					clients.put(name, client);
				}else{
					Packet p = new Packet();
					p.packetType = PacketType.LOGOUT;
					os.writeObject(p);
					os.close();
					is.close();
					socket.close();
				}
			}
			server.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void sendMessage(String name,Packet message){
		Client c = clients.get(name);
		if(c!=null)c.send(message);
	}
	
	public void sendToAll(Packet message){
		clients.forEach((k, v)->v.send(message));
	}
}
