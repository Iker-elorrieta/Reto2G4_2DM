package Principal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Vista.Login;

public class Principal {

	public static void main(String[] args) {

	
		try {
			Socket cliente = new Socket("localhost", 5000);
			DataInputStream dis = new DataInputStream(cliente.getInputStream());
			DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
			Login login = new Login(cliente,dis,dos);
			login.setVisible(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
