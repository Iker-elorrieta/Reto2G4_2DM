package Vista;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;


import com.example.ElorServ.Centro;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Controlador.Controlador;
import modelo.Horarios;

import modelo.Reuniones;
import modelo.Users;

public class HiloServidor extends Thread {

    private static final Object TITULO = "titulo";
    private static final Object ASUNTO = "asunto";
    private static final Object AULA = "aula";
    private static final Object IDCENTRO = "idCentro";
    private static final Object ALUMNO = "idAlumno";
    private static final Object FECHA = "fecha";
    private static final String ESTADO = "estado";
    
    private  Controlador controlador;
    private Socket cliente;
    ArrayList<Users> listaUsuarios = new ArrayList<Users>();
    ArrayList<Users> listaAlumnos = new ArrayList<Users>();
    ArrayList<Reuniones> listaReunionesProfe = new ArrayList<Reuniones>();
    ArrayList<Reuniones> listaCentros = new ArrayList<Reuniones>();
    ArrayList<Users> listaTodosAlumnos = new ArrayList<Users>();
    ArrayList<Horarios> listaHorarioProfe = new ArrayList<Horarios>();
    

    public HiloServidor(Socket cliente, String userEmail, String userContraseña) {
        this.cliente = cliente;
    }

    public HiloServidor(Socket cliente) {
        this.cliente = cliente;
    }

    public HiloServidor(Socket cliente, Controlador controlador) {
        this.cliente = cliente;
        this.controlador = controlador;
    }

    public HiloServidor() {}
    

   

    @Override
    public void run() {

        String idProfe = null;
        listaUsuarios = controlador.obtenerProfesores();
        Users usuario = new Users();
        
        try {

            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
            DataInputStream dis = new DataInputStream(cliente.getInputStream());


            boolean correcto = false;

            while (!correcto) {

                String correo = ois.readObject().toString();
                String contraseña = ois.readObject().toString();

                correcto = false;

                for (Users user : listaUsuarios) {

                    String emailCifrado = cifrarUsuario(user.getEmail());
                    String passCifrada = cifrarUsuario(user.getPassword());

                    if (emailCifrado.equals(correo) && passCifrada.equals(contraseña)) {
                        idProfe = user.getId().toString();
                        usuario = user;
                        correcto = true;
                    }
                }

                if (correcto) {
                    oos.writeObject(idProfe);
                    menu(idProfe, usuario, dos, dis, ois, oos, controlador, listaUsuarios);

                } else {
                    oos.writeObject("-1");
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void menu(String idProfe, Users usuario, DataOutputStream dos, DataInputStream dis, ObjectInputStream ois,  ObjectOutputStream oos, Controlador controlador, ArrayList<Users> listaUsuarios) {

        if (idProfe != null) {
            listaAlumnos = controlador.obtenerAlumnos(usuario);
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
                        // listaUsuarios
                        sendJson(oos, listaUsuarios);
                        break;
                    }

                    case 2: {
                        // listaAlumnos
                        sendJson(oos, listaAlumnos);
                        break;
                    }

                    case 3: {
                        listaHorarioProfe = controlador.obtenerHorarioProfe(idProfe);
                        sendJson(oos,listaHorarioProfe);
                        break;
                    }


                    case 4: {
                    	listaReunionesProfe = controlador.obtenerReunionesPorProfesor(idProfe);
                        sendJson(oos, listaReunionesProfe);
                        break;
                    }


                    case 5: {

                        Users otroProfe = (Users) ois.readObject();

                        ArrayList<Horarios> listaHorarioOtro =
                                controlador.obtenerHorarioProfe(otroProfe.getId().toString());

                        sendJson(oos, listaHorarioOtro);
                        break;
                    }

                    case 6: {
                        int id = Integer.parseInt(ois.readObject().toString());
                        String estado = ois.readObject().toString();
                                                
                        for (Reuniones r : listaReunionesProfe) {
                            if (id == r.getIdReunion()) {
                                r.setEstado(estado);
                                controlador.actualizarReunion(r, r.getEstado());
                            }
                        }
                        break;
                    }

                    case 7: {
                        ArrayList<Centro> centros = controlador.leerJson();

                        sendJson(oos, centros);
                        centros.clear();
                        break;
                    }

                    case 8: {
                        listaTodosAlumnos = controlador.obtenerTodosAlumnos();
                        sendJson(oos, listaTodosAlumnos);
                        break;
                    }

                    case 9: {

                        String json = ois.readObject().toString();

                        Gson gson = new Gson();

                        ArrayList<Map<String, Object>> lista =
                                gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

                        Reuniones reunion = new Reuniones();
                        Users alumno = new Users();
                        Users profesor = new Users();
                        for (Map<String, Object> a : lista) {
                            reunion.setTitulo(a.get(TITULO).toString());
                            reunion.setAsunto(a.get(ASUNTO).toString());
                            reunion.setAula(a.get(AULA).toString());
                            int idAlumno = ((Double) a.get(ALUMNO)).intValue();
                            alumno.setId(idAlumno);
                            reunion.setUsersByAlumnoId(alumno);
                            profesor.setId(Integer.parseInt(idProfe));
                            reunion.setUsersByProfesorId(profesor);
                            int idCentro = ((Double) a.get(IDCENTRO)).intValue();
                            reunion.setIdCentro(String.valueOf(idCentro));
                            reunion.setEstado(a.get(ESTADO).toString());
                            String fecha = a.get(FECHA).toString();
                            Timestamp fechats = Timestamp.valueOf(fecha);
                            reunion.setFecha(fechats);
                        }

                        controlador.crearReunion(reunion);
                        break;
                    }

                }

            } while (opcionInt != 0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendJson(ObjectOutputStream oos, Object obj) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        byte[] data = json.getBytes("UTF-8");

        oos.writeInt(data.length);
        oos.write(data);
        oos.flush();
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
