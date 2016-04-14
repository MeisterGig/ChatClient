package Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Converter {
	
	public Converter(String ip, String username, Frame frame){
		
	}

	public void sendFile(String to, String path) {
		//TODO
	}

	public void sendMessage(String to, String message) {
		System.out.println(message);
		// TODO 
		
	}

	public String loadMessages(String user) {
		
		File file = new File("Chat/history/"+user+".chat"); // Alle Werte werden in eine log Datei gespeichert.
		try {
			File dirHistory = new File("Chat/history");
			if(dirHistory.exists()== false){
				dirHistory.mkdir();
			}
			if (file.exists() == false) {
				file.createNewFile();
				System.err.println("Log-Datei erstellt");
			}
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
		} catch (IOException e) {
			System.err.println("Es konnte keine Chat-Datei erstellt werden!");
		}
		
		return null;
	}
	
}
