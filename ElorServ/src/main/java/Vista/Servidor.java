package Vista;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Controlador.Controlador;

@Component
public class Servidor extends Thread {

    private final Controlador controlador;

    @Autowired
    public Servidor(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket cliente = serverSocket.accept();
                HiloServidor hilo = new HiloServidor(cliente, controlador);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
