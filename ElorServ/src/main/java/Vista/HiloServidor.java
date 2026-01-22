package Vista;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;

import Controlador.Controlador;
import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;

public class HiloServidor extends Thread {

    private Socket cliente;
    private String userEmail;
    private String userContraseña;

    public HiloServidor(Socket cliente, String userEmail, String userContraseña) {
        this.cliente = cliente;
        this.userEmail = userEmail;
        this.userContraseña = userContraseña;
    }

    public HiloServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public HiloServidor() {}

    @Override
    public void run() {

        String idProfe = null;
        Controlador controlador = new Controlador();
        ArrayList<Users> listaUsuarios = controlador.obtenerProfesores();

        try {

            DataInputStream dis = new DataInputStream(cliente.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());

            boolean correcto = false;

            while (!correcto) {

                String correo = dis.readUTF();
                String contraseña = dis.readUTF();

                correcto = false;

                for (Users user : listaUsuarios) {

                    String emailCifrado = cifrarUsuario(user.getEmail());
                    String passCifrada = cifrarUsuario(user.getPassword());

                    if (emailCifrado.equals(correo) && passCifrada.equals(contraseña)) {
                        idProfe = user.getId().toString();
                        correcto = true;
                    }
                }

                if (correcto) {
                    dos.writeUTF(idProfe);
                } else {
                    dos.writeUTF("-1");
                }
            }

            menu(idProfe, dis, dos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu(String idProfe, DataInputStream dis, DataOutputStream dos) {

        Controlador controlador = new Controlador();

        ArrayList<Users> listaUsuarios = controlador.obtenerProfesores();
        ArrayList<Users> listaAlumnos = controlador.obtenerAlumnos(Integer.parseInt(idProfe));

        int opcionInt = 0;

        try {

            do {

                String opcion = dis.readUTF();
                opcionInt = Integer.parseInt(opcion);

                switch (opcionInt) {

                    case 0:
                        cliente.close();
                        break;

                    case 1: {
                        Gson gson = new Gson();
                        String json = gson.toJson(listaUsuarios);
                        dos.writeUTF(json);
                        dos.flush();
                        break;
                    }

                    case 2: {
                        Gson gson = new Gson();
                        String json = gson.toJson(listaAlumnos);
                        dos.writeUTF(json);
                        dos.flush();
                        break;
                    }

                    case 3: {
                        ArrayList<Horarios> listaHorarioProfe =
                                controlador.obtenerHorarioProfe(Integer.parseInt(idProfe));

                        ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();

                        for (Horarios h : listaHorarioProfe) {
                            Map<String, Object> mapa = new java.util.HashMap<>();
                            mapa.put("hora", h.getHora());
                            mapa.put("dia", h.getDia());
                            mapa.put("modulos",
                                    (h.getModulos() != null) ? h.getModulos().getNombre() : "");
                            listaEnviar.add(mapa);
                        }

                        Gson gson = new Gson();
                        String json = gson.toJson(listaEnviar);

                        dos.writeUTF(json);
                        dos.flush();
                        break;
                    }

                    case 4: {
                        ArrayList<Reuniones> listaReunionesProfe =
                                controlador.obtenerReunionesPorProfesor(Integer.parseInt(idProfe));

                        ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();

                        for (Reuniones r : listaReunionesProfe) {
                            Map<String, Object> mapa = new java.util.HashMap<>();
                            mapa.put("estado", r.getEstado());
                            mapa.put("profesor", r.getUsersByProfesorId().getNombre());
                            mapa.put("alumno", r.getUsersByAlumnoId().getNombre());
                            mapa.put("titulo", r.getTitulo());
                            mapa.put("asunto", r.getAsunto());
                            mapa.put("aula", r.getAula());
                            mapa.put("fecha", r.getFecha().toString());
                            mapa.put("centro",
                                    controlador.obtenerNombreCentroPorId(r.getIdCentro()));
                            listaEnviar.add(mapa);
                        }

                        Gson gson = new Gson();
                        String json = gson.toJson(listaEnviar);

                        dos.writeUTF(json);
                        dos.flush();
                        break;
                    }

                    case 5: {

                        int idOtroProfe = dis.readInt();  // ← AHORA SÍ LEEMOS EL INT

                        ArrayList<Horarios> listaHorarioOtro =
                                controlador.obtenerHorarioProfe(idOtroProfe);

                        ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();

                        for (Horarios h : listaHorarioOtro) {
                            Map<String, Object> mapa = new java.util.HashMap<>();
                            mapa.put("hora", h.getHora());
                            mapa.put("dia", h.getDia());
                            mapa.put("modulos",
                                    (h.getModulos() != null) ? h.getModulos().getNombre() : "");
                            listaEnviar.add(mapa);
                        }

                        Gson gson = new Gson();
                        String json = gson.toJson(listaEnviar);

                        dos.writeUTF(json);
                        dos.flush();
                        break;
                    }
                }

            } while (opcionInt != 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String cifrarUsuario(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(texto.getBytes());
            return new String(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "Error al cifrar";
        }
    }
}
