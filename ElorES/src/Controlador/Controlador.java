package Controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Controlador {

    // ---------------------------
    //  CONEXIÓN ÚNICA
    // ---------------------------
    private Socket cliente;
    private DataInputStream dis;
    private DataOutputStream dos;

    // ---------------------------
    //  REFERENCIAS A VISTAS
    // ---------------------------
    private Vista.Login login;
    private Vista.ConsultarAlumnos consultarAlumnos;
    private Vista.DetalleAlumno detalleAlumno;
    private Vista.MiPerfil miPerfil;
    private Vista.MiHorario miHorario;
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
                int id = ((Double) u.get("id")).intValue();
                if (id == idProfe) {

                    miPerfil.getLblEmail().setText("Email: " + u.get("email"));
                    miPerfil.getLblNombre().setText("Nombre: " + u.get("nombre"));
                    miPerfil.getLblUsername().setText("Username: " + u.get("username"));
                    miPerfil.getLblApellidos().setText("Apellidos: " + u.get("apellidos"));
                    miPerfil.getLblDNI().setText("DNI: " + u.get("dni"));
                    miPerfil.getLblDireccion().setText("Dirección: " + u.get("direccion"));
                    miPerfil.getLblTelefono().setText("Teléfono: " + u.get("telefono1"));
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

            	int fila = ((Double) h.get("hora")).intValue() - 1;

                int col = switch (h.get("dia").toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };

                if (col != -1) {
                    miHorario.getModelo().setValueAt(h.get("modulos"), fila, col);
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
                        ((Double) a.get("id")).intValue(),
                        a.get("dni"),
                        a.get("nombre"),
                        a.get("apellidos")
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

                int id = ((Double) a.get("id")).intValue();

                if (id == idAlumno) {

                    detalleAlumno.getLblEmail().setText("Email: " + a.get("email"));
                    detalleAlumno.getLblNombre().setText("Nombre: " + a.get("nombre"));
                    detalleAlumno.getLblUsername().setText("Username: " + a.get("username"));
                    detalleAlumno.getLblApellidos().setText("Apellidos: " + a.get("apellidos"));
                    detalleAlumno.getLblDNI().setText("DNI: " + a.get("dni"));
                    detalleAlumno.getLblDireccion().setText("Dirección: " + a.get("direccion"));
                    detalleAlumno.getLblTelefono().setText("Teléfono: " + a.get("telefono1"));
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
                        r.get("profesor"),
                        r.get("alumno"),
                        r.get("centro"),
                        r.get("estado")
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  SETTERS DE VISTAS
    // ---------------------------
    public void setLogin(Vista.Login login) { this.login = login; }
    public void setMenuProfe(Vista.MenuProfe menuProfe) { }
    public void setConsultarAlumnos(Vista.ConsultarAlumnos consultarAlumnos) { this.consultarAlumnos = consultarAlumnos; }
    public void setDetalleAlumno(Vista.DetalleAlumno detalleAlumno) { this.detalleAlumno = detalleAlumno; }
    public void setMiPerfil(Vista.MiPerfil miPerfil) { this.miPerfil = miPerfil; }
    public void setMiHorario(Vista.MiHorario miHorario) { this.miHorario = miHorario; }
    public void setConsultarReu(Vista.ConsultarReu consultarReu) { }
    public void setOtrosHorarios(Vista.OtrosHorarios otrosHorarios) { }
}
