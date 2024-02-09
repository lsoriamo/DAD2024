package us.lsi.dad.sockets;

import java.io.IOException;

public class MainServer {

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
