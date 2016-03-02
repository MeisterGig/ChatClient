package Main;

import Client.Client;

public class MainClient2 {
	public static void main(String[] args) {
		Client c = new Client("localhost", 1000, "MainClient2");
		c.start();
	}
}
