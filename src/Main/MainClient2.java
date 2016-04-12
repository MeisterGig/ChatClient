package Main;

import java.util.Scanner;

import Client.Client;
import Client.ClientInterface;
import Common.Packet;

public class MainClient2 {
	static boolean running = true;

	public static void main(String[] args) {
		Client c = new Client("localhost", 1000, "MainClient2", new ClientInterface() {

			@Override
			public void receiveMessage(Packet p) {
				System.out.println(p.from + ": " + p.message);
			}

			@Override
			public void receiveBroadcast(Packet p) {
				System.out.println(p.from + ": " + p.message);
			}

			@Override
			public void logout() {
				System.out.println("Server closed connection!");
				running = false;
			}
		});
		
		boolean running = true;
		
		Scanner scanner = new Scanner(System.in);
		while(running){
			String[] message = scanner.next().split(";");
			if(message.length>=2){
				if(message[0].equals("*")){
					c.sendBroadcast(message[1]);
				}else if(message[0].equals("logout")){
					c.logout();
					running = false;
				}else{
					c.sendMessage(message[0], message[1]);
				}				
			}
		}
		scanner.close();		
	}
}
