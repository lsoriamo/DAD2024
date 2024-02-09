package us.lsi.dad.sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//Se hereda de conexión para hacer uso de los sockets y demás
public class Server extends Conexion {

	public Server() throws IOException {
		super("servidor");
	}

	// Se usa el constructor para servidor de Conexion
	// Método para iniciar el servidor
	public void startServer() {
		try {
			// Esperando conexión
			System.out.println("Esperando...");

			// Accept comienza el socket y espera una conexión desde un cliente
			cs = ss.accept();

			// Se obtiene el flujo de salida del cliente para enviarle mensajes
			System.out.println("Cliente en línea");

			// Se le envía un mensaje al cliente usando su flujo de salida
			salidaCliente = new DataOutputStream(cs.getOutputStream());

			// Se obtiene el flujo entrante desde el cliente
			salidaCliente.writeUTF("Petición recibida y aceptada");

			BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			
			// Mientras haya mensajes desde el cliente
			while ((mensajeServidor = entrada.readLine()) != null) {
				// Se muestra por pantalla el mensaje recibido
				System.out.println(mensajeServidor);
			}

			System.out.println("Fin de la conexión");
			// Se finaliza la conexión con el cliente
			ss.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
