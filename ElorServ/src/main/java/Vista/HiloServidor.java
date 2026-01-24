package Vista;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.ElorServ.Centro;
import com.google.gson.Gson;

import Controlador.Controlador;
import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;

public class HiloServidor extends Thread {

    private Socket cliente;
    ArrayList<Users> listaUsuarios = new ArrayList<Users>();
    ArrayList<Users> listaAlumnos = new ArrayList<Users>();
    ArrayList<Reuniones> listaReunionesProfe = new ArrayList<Reuniones>();
    ArrayList<Reuniones> listaCentros = new ArrayList<Reuniones>();

    public HiloServidor(Socket cliente, String userEmail, String userContraseña) {
        this.cliente = cliente;
    }

    public HiloServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public HiloServidor() {}

    @Override
    public void run() {

        String idProfe = null;
        Controlador controlador = new Controlador();
        listaUsuarios = controlador.obtenerProfesores();

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
                    menu(idProfe, dis, dos, controlador, listaUsuarios);

                } else {
                    dos.writeUTF("-1");
                }
                
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu(String idProfe, DataInputStream dis, DataOutputStream dos, Controlador controlador, ArrayList<Users> listaUsuarios) {

         if (idProfe != null) {
     		listaAlumnos = controlador.obtenerAlumnos(Integer.parseInt(idProfe));
         }

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
                         listaReunionesProfe =
                                controlador.obtenerReunionesPorProfesor(Integer.parseInt(idProfe));

                        ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();

                        for (Reuniones r : listaReunionesProfe) {
                            Map<String, Object> mapa = new java.util.HashMap<>();
                            mapa.put("id", r.getIdReunion());
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

                        int idOtroProfe = dis.readInt(); 

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
                    case 6: {
                    	int id = Integer.parseInt(dis.readUTF());
                    	String estado = dis.readUTF();
                    	for(Reuniones r : listaReunionesProfe) {
                    		if(id ==  r.getIdReunion()) {
                    			r.setEstado(estado); 
                    			System.out.println(estado);
                    			controlador.actualizarReunion(r.getIdReunion(), r.getEstado());
                    		}
                    	}
                    	break;
                    }
                    case 7: {
                        ArrayList<Centro> centros = controlador.leerJson(); 

                        Set<String> centrosUnicos = new HashSet<>();

                        for (Centro c : centros) {
                            if (c.getNOM() != null) {
                                centrosUnicos.add(c.getNOM());
                            }
                        }

                        Gson gson = new Gson();
                        dos.writeUTF(gson.toJson(centrosUnicos));
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
