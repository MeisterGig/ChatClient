package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import Common.Packet;
import Common.PacketType;

public class Client {
	private String name;
	private Socket socket;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private Thread receiveThread;
	
	private Encryption encryption;
	private HashMap<String, byte[]> publicKeys; 
	
	private Converter converter;
	
	public Client(String ip, int port, String name, Converter converter){
		try {
			encryption = new Encryption();
			publicKeys = new HashMap<String, byte[]>();
			
			this.name = name;
			this.converter = converter;
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
			p.from=name;
			p.message=encryption.getEncodedOwnPublicKey();
			os.writeObject(p);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String target, String message){
		if(publicKeys.containsKey(target)){
			Packet p = new Packet();
			p.packetType=PacketType.MESSAGE;
			p.target=target;
			p.message=encryption.encryptString(message, publicKeys.get(target));
			p.from=name;
			sendPacket(p);
		}
	}
	
	public void sendFile(String target, String path){
		if(publicKeys.containsKey(target)){
			try {
				File file = new File(path);
				FileInputStream fis = new FileInputStream(path);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				fis.close();
				
				Packet p = new Packet();
				p.packetType = PacketType.FILE;
				p.filename = file.getName();
				p.message =data;
				p.from = name;
				p.target = target;
				sendPacket(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void receiveFile(Packet p){
		try {
			File dirChat = new File("Chat");
			if(dirChat.exists()== false){
				dirChat.mkdir();
			}
			File dirHistory = new File("Chat/File");
			if(dirHistory.exists()== false){
			    dirHistory.mkdir();
			}
			
			String filename = p.filename;
			File f = new File("Chat/File/" + filename);
			if(!f.exists()){
				f.createNewFile();
			}else{
				f.delete();
				f.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(p.message);
			fos.close();
			
			converter.receiveFile(p.from,filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void logout(){
		Packet p = new Packet();
		p.packetType=PacketType.LOGOUT_BY_SERVER;
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
		if(packet.packetType==PacketType.LOGIN){
			publicKeys.put(packet.from, packet.message);
			converter.receiveClients(publicKeys.keySet().toArray(new String[publicKeys.size()]));
		}else if(packet.packetType==PacketType.LOGOUT){
			publicKeys.remove(packet.from);
			converter.receiveClients(publicKeys.keySet().toArray(new String[publicKeys.size()]));
		}else if(packet.packetType==PacketType.LOGOUT_BY_SERVER){
			close();
			converter.logoutByServer();
		}else if(packet.packetType==PacketType.MESSAGE){
			converter.receiveMessage(packet.from,encryption.decryptString(packet.message));
		}else if(packet.packetType==PacketType.FILE){
			receiveFile(packet);
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