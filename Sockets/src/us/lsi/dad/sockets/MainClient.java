package us.lsi.dad.sockets;

import java.io.IOException;

public class MainClient {

	public static void main(String[] args) {
		try {   
			Client client = new Client();
			client.startClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
