package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Converter {
	private Frame frame;
	
	public Converter(String ip, String username, Frame fr){
		frame=fr;
	}

	public void sendFile(String to, String path) {
		//TODO
	}

	public void sendMessage(String to, String message) {
		addMessage(to, message);
		frame.refreshMessages(to, loadMessages(to));
		System.out.println(message);
		// TODO 
		
	}
	
	public void addMessage(String user, String message){
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
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(message);
    	    writer.newLine();
			
		} catch (IOException e) {
			System.err.println("Es konnte keine Chat-Datei erstellt werden!");
		}
		
		
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
				messages=messages+line+" <br>";
			}
			fileReader.close();
			
		    return messages;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
