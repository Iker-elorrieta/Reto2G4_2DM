package Controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Controlador {

    // ---------------------------
    //  CONSTANTES JSON
    // ---------------------------
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NOMBRE = "nombre";
    private static final String USERNAME = "username";
    private static final String APELLIDOS = "apellidos";
    private static final String DNI = "dni";
    private static final String DIRECCION = "direccion";
    private static final String TELEFONO = "telefono1";
    private static final String HORA = "hora";
    private static final String DIA = "dia";
    private static final String MODULOS = "modulos";
    private static final String PROFESOR = "profesor";
    private static final String ALUMNO = "alumno";
    private static final String CENTRO = "centro";
    private static final String ESTADO = "estado";

    // ---------------------------
    //  CONEXIÓN ÚNICA
    // ---------------------------
    public Socket cliente;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Map<String, Integer> mapaProfesores = new HashMap<>();

    // ---------------------------
    //  REFERENCIAS A VISTAS
    // ---------------------------
    private Vista.Login login;
    private Vista.ConsultarAlumnos consultarAlumnos;
    private Vista.DetalleAlumno detalleAlumno;
    private Vista.MiPerfil miPerfil;
    private Vista.MiHorario miHorario;
    private Vista.OtrosHorarios otrosHorarios;

    // ---------------------------
    //  CONSTRUCTOR
    // ---------------------------
    public Controlador() {}

    // ---------------------------
    //  INICIALIZAR CONEXIÓN
    // ---------------------------
    public void conectar() {
        try {
            if (cliente == null) {
                cliente = new Socket("localhost", 5000);
                dis = new DataInputStream(cliente.getInputStream());
                dos = new DataOutputStream(cliente.getOutputStream());
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  LOGIN
    // ---------------------------
    public int validarUsuario() {

        try {
        	
        	if (cliente == null || cliente.isClosed()) { 
        		conectar(); 
        		}
        	
            String correo = login.getTfcorreo().getText();
            String pass = new String(login.getTfcontraseña().getPassword());

            if (correo.isEmpty() || pass.isEmpty()) {
                login.getLblError().setText("Debes completar todos los campos");
                return -1;
            }

            String correoHash = cifrar(correo);
            String passHash = cifrar(pass);

            dos.writeUTF(correoHash);
            dos.writeUTF(passHash);

            String respuesta = dis.readUTF();

            if (respuesta.equals("-1")) {
                login.getLblError().setText("Correo o contraseña incorrectos");
                return -1;
            }

            return Integer.parseInt(respuesta);

        } catch (IOException e) {
            e.printStackTrace();
            login.getLblError().setText("Error de conexión");
            return -1;
        }
    }

    // ---------------------------
    //  CIFRADO
    // ---------------------------
    private String cifrar(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return new String(md.digest(texto.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return texto;
        }
    }

    // ---------------------------
    //  PERFIL
    // ---------------------------
    public void cargarPerfil(int idProfe) {

        try {
            dos.writeUTF("1");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> u : lista) {
                int id = ((Double) u.get(ID)).intValue();
                if (id == idProfe) {

                    miPerfil.getLblEmail().setText("Email: " + u.get(EMAIL));
                    miPerfil.getLblNombre().setText("Nombre: " + u.get(NOMBRE));
                    miPerfil.getLblUsername().setText("Username: " + u.get(USERNAME));
                    miPerfil.getLblApellidos().setText("Apellidos: " + u.get(APELLIDOS));
                    miPerfil.getLblDNI().setText("DNI: " + u.get(DNI));
                    miPerfil.getLblDireccion().setText("Dirección: " + u.get(DIRECCION));
                    miPerfil.getLblTelefono().setText("Teléfono: " + u.get(TELEFONO));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  HORARIO
    // ---------------------------
    public void cargarHorario(int idProfe) {

        try {
            dos.writeUTF("3");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> h : lista) {

                int fila = ((Double) h.get(HORA)).intValue() - 1;

                int col = switch (h.get(DIA).toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };

                if (col != -1) {
                    miHorario.getModelo().setValueAt(h.get(MODULOS), fila, col);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  OTROS HORARIOS
    // ---------------------------
    public void otrosHorarios() {
        try {
            dos.writeUTF("1");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            mapaProfesores.clear();
            otrosHorarios.getCbProfesores().removeAllItems();

            for (Map<String, Object> h : lista) {

                String nombre = h.get(NOMBRE).toString();
                int id = ((Double) h.get(ID)).intValue();

                mapaProfesores.put(nombre, id);
                otrosHorarios.getCbProfesores().addItem(nombre);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHorarioDeProfesor(String nombreProfe) {

        int idProfe = mapaProfesores.get(nombreProfe);
        DefaultTableModel model = otrosHorarios.getModelo();

        try {
            dos.writeUTF("5"); 
            dos.writeInt(idProfe);
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            // limpiar tabla
            for (int i = 0; i < 6; i++) {
                for (int j = 1; j < 6; j++) {
                    model.setValueAt("", i, j);
                }
            }

            for (Map<String, Object> h : lista) {

                int fila = ((Double) h.get(HORA)).intValue() - 1;

                int col = switch (h.get(DIA).toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };

                if (col != -1) {
                    model.setValueAt(h.get(MODULOS), fila, col);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ---------------------------
    //  ALUMNOS
    // ---------------------------
    public void cargarAlumnos(int idProfe) {

        try {
            dos.writeUTF("2");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            DefaultTableModel model = consultarAlumnos.getModel();
            model.setRowCount(0);

            for (Map<String, Object> a : lista) {

                model.addRow(new Object[]{
                        ((Double) a.get(ID)).intValue(),
                        a.get(DNI),
                        a.get(NOMBRE),
                        a.get(APELLIDOS)
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  DETALLE ALUMNO
    // ---------------------------
    public void cargarDetalleAlumno(int idAlumno) {

        try {
            dos.writeUTF("2");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> a : lista) {

                int id = ((Double) a.get(ID)).intValue();

                if (id == idAlumno) {

                    detalleAlumno.getLblEmail().setText("Email: " + a.get(EMAIL));
                    detalleAlumno.getLblNombre().setText("Nombre: " + a.get(NOMBRE));
                    detalleAlumno.getLblUsername().setText("Username: " + a.get(USERNAME));
                    detalleAlumno.getLblApellidos().setText("Apellidos: " + a.get(APELLIDOS));
                    detalleAlumno.getLblDNI().setText("DNI: " + a.get(DNI));
                    detalleAlumno.getLblDireccion().setText("Dirección: " + a.get(DIRECCION));
                    detalleAlumno.getLblTelefono().setText("Teléfono: " + a.get(TELEFONO));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  REUNIONES
    // ---------------------------
    public void cargarReuniones(DefaultTableModel model) {

        try {
            dos.writeUTF("4");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            model.setRowCount(0);

            for (Map<String, Object> r : lista) {

                model.addRow(new Object[]{
                        r.get(PROFESOR),
                        r.get(ALUMNO),
                        r.get(CENTRO),
                        r.get(ESTADO)
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void cerrarConexion() {
        try {
            dos.writeUTF("0");
            dos.flush();
            cliente.close();
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ---------------------------
    //  SETTERS DE VISTAS
    // ---------------------------
    public void setLogin(Vista.Login login) { this.login = login; }
    public void setMenuProfe(Vista.MenuProfe menuProfe) {}
    public void setConsultarAlumnos(Vista.ConsultarAlumnos consultarAlumnos) { this.consultarAlumnos = consultarAlumnos; }
    public void setDetalleAlumno(Vista.DetalleAlumno detalleAlumno) { this.detalleAlumno = detalleAlumno; }
    public void setMiPerfil(Vista.MiPerfil miPerfil) { this.miPerfil = miPerfil; }
    public void setMiHorario(Vista.MiHorario miHorario) { this.miHorario = miHorario; }
    public void setConsultarReu(Vista.ConsultarReu consultarReu) {}
    public void setOtrosHorarios(Vista.OtrosHorarios otrosHorarios) { this.otrosHorarios = otrosHorarios; }
}
