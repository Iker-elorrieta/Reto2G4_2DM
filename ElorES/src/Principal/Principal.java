package Principal;

import Controlador.Controlador;
import Vista.Login;

public class Principal {

    public static void main(String[] args) {

        // Crear el controlador central
        Controlador controlador = new Controlador();

        // Abrir la conexión con el servidor
        controlador.conectar();

        // Abrir la ventana de login
        Login login = new Login(controlador);
        login.setVisible(true);
    }
}
