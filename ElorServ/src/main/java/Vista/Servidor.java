package Vista;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {

	static ServerSocket servidor;
	
	public static void main(String[] args) {

		try {
			 servidor = new ServerSocket(5000);
			
			
			while (true) {
				Socket cliente = servidor.accept();
				HiloServidor hilo = new HiloServidor(cliente);
				hilo.start();
			}
										
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			servidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	

}
