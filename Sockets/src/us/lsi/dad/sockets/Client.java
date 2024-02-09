package us.lsi.dad.sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client extends Conexion {
	public Client() throws IOException {
		super("cliente");
	}

	// Se usa el constructor para cliente de Conexion public

	// Método para iniciar el cliente
	void startClient() {
		try {
			// Flujo de datos hacia el servidor
			// Se enviarán dos mensajes
			salidaServidor = new DataOutputStream(cs.getOutputStream());

			// Se escribe en el servidor usando su flujo de datos
			for (int i = 0; i < 10; i++) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String text = reader.readLine();
				salidaServidor.writeUTF("El cliente dice: " + text + "\n");
			}
			// Fin de la conexión
			cs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
