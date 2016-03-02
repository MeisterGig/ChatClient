package Main;

import Client.Client;

public class MainClient1 {
	public static void main(String[] args) {
		Client c = new Client("localhost", 1000, "MainClient1");
		c.start();
	}
}
