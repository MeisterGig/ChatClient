package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Converter {
	private Frame frame;
	private Client client;
	
	public Converter(String ip, String username, Frame fr){
		frame=fr;
		client=new Client(ip,24498,username,this);
	}

	public void sendFile(String to, String path) {
		client.sendFile(to, path);
	}

	public void sendMessage(String to, String message) {
		addMessage(to, "<div color=\"blue\" align=\"right\">"+message+" </div> <br>");
		frame.refreshMessages(to, loadMessages(to));
		client.sendMessage(to, message);
		
	}
	
	public void receiveClients(String clients[]){
		frame.receiveClients(clients);
	}
	

	public void receiveFile(String from, String path) {
		addMessage(from, "<div color=\"blue\" align=\"right\">Datei bekommen unter: "+path+" </div> <br>");
		frame.refreshMessages(from, loadMessages(from));
	}
	
	public void receiveMessage(String from, String message){
		addMessage(from, "<div color=\"blue\" align=\"right\">"+message+" </div> <br>");
		frame.refreshMessages(from, loadMessages(from));
	}
	
	public void logoutByServer() {
		System.exit(1);
		
	}
	
	public void addMessage(String user, String message){
		String mes=loadMessages(user);
		File file = new File("Chat/history/"+user+".chat"); // Alle Werte werden in eine log Datei gespeichert.
		
		try {
			File dirChat = new File("Chat");
			if(dirChat.exists()== false){
				dirChat.mkdir();
			}
			File dirHistory = new File("Chat/history");
			if(dirHistory.exists()== false){
				dirHistory.mkdir();
			}
			if (file.exists() == false) {
				file.createNewFile();
				System.err.println("Datei erstellt!");
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			writer.write(mes);
			writer.write(message);
    	    writer.newLine();
    	    writer.flush();
    	    writer.close();
			
		} catch (IOException e) {
			System.err.println("Es konnte keine Chat-Datei erstellt werden!");
		}
		
		
	}

	public String loadMessages(String user) {
		
		File file = new File("Chat/history/"+user+".chat"); // Alle Werte werden in eine log Datei gespeichert.
		try {
			File dirChat = new File("Chat");
			if(dirChat.exists()== false){
				dirChat.mkdir();
			}
			File dirHistory = new File("Chat/history");
			if(dirHistory.exists()== false){
				dirHistory.mkdir();
			}
			if (file.exists() == false) {
				file.createNewFile();
				System.err.println("Datei erstellt!");
			}
		} catch (IOException e) {
			System.err.println("Es konnte keine Chat-Datei erstellt werden!");
		}
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line ="";
			String messages="";
			while((line = bufferedReader.readLine()) != null){
				stringBuffer.append(line);
				messages=messages+line;
			}
			fileReader.close();
			
		    return messages;
			
		} catch (IOException e) {
			System.err.println("Es konnte keine Chat-Datei geladen werden!");
		}
		
		return null;
	}
	
}
