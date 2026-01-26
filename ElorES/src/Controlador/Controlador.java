package Controlador;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Modelo.Centro;
import Modelo.Users;

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
    private static final String ESTADO = "estado";
    private static final String FECHA = "fecha";
    private static final String PROFESOR = "profesor";
    private static final String ALUMNO = "alumno";
    private static final String CENTRO = "centro";
    private static final String TITULO = "titulo";
    private static final String ASUNTO = "asunto";
    private static final String AULA = "aula";

    // ---------------------------
    //  CONEXIÓN ÚNICA
    // ---------------------------
    public Socket cliente;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Map<String, Integer> mapaProfesores = new HashMap<>();
    private Map<String, String> estadosReuniones = new HashMap<>();
    private ArrayList<Centro> centros = new ArrayList<Centro>();
    private ArrayList<Users> usuarios = new ArrayList<Users>();

    // ---------------------------
    //  REFERENCIAS A VISTAS
    // ---------------------------
    private Vista.Login login;
    private Vista.ConsultarAlumnos consultarAlumnos;
    private Vista.DetalleAlumno detalleAlumno;
    private Vista.MiPerfil miPerfil;
    private Vista.MiHorario miHorario;
    private Vista.OtrosHorarios otrosHorarios;
    private Vista.ConsultarReu consultarReu;
    private Vista.GestionPendientes pendientes;
    private Vista.CrearReu crearReuniones;
    private Vista.GestionReuniones gestionReuniones;

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
    //  LECTURA JSON GRANDE
    // ---------------------------
    private String readJson() throws IOException {
        int length = dis.readInt();
        byte[] data = new byte[length];
        dis.readFully(data);
        return new String(data, "UTF-8");
    }

    // ---------------------------
    //  PERFIL
    // ---------------------------
    public void cargarPerfil(int idProfe) {

        try {
            dos.writeUTF("1");
            dos.flush();

            String json = readJson();

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

            String json = readJson();

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

            String json = readJson();

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

            String json = readJson();

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

            String json = readJson();

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

            String json = readJson();

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
    public void cargarHorarioReuniones() {

        try {
            dos.writeUTF("3");
            dos.flush();

            String json = readJson();

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
                    consultarReu.getModelo().setValueAt(h.get(MODULOS), fila, col);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CargarReuniones() {
        try {
            dos.writeUTF("4");
            dos.flush();
            String json = readJson();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> h : lista) {

                if (h.get(ID) != null) {

                    int idReu = ((Double) h.get(ID)).intValue();
                    String fechaStr = h.get(FECHA).toString();
                    String estado = h.get(ESTADO).toString();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                    LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);

                    Locale localeES = Locale.forLanguageTag("es-ES");

                    int hora = fecha.getHour();
                    int fila = convertirHoraAFila(hora);

                    if (estado.equals(ESTADO)) {
                        Color color = new Color(255, 255, 208);
                        consultarReu.getTable().setBackground(color);
                    }

                    if (fila != -1) {

                        String dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, localeES).toLowerCase();

                        int col = switch (dia) {
                            case "lunes" -> 1;
                            case "martes" -> 2;
                            case "miércoles", "miercoles" -> 3;
                            case "jueves" -> 4;
                            case "viernes" -> 5;
                            default -> -1;
                        };

                        if (col != -1) {
                            String clave = "Reunion" + idReu;
                            String alumno = h.get("alumno").toString();

                            estadosReuniones.put(clave, estado.toLowerCase());
                            consultarReu.getModelo().setValueAt("Reunión " + idReu + " con " + alumno, fila, col);
                        }

                    }
                }
            }
            lista.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int convertirHoraAFila(int horaReal) {
        return switch (horaReal) {
            case 8 -> 1;
            case 9 -> 2;
            case 10 -> 3;
            case 11 -> 4;
            case 12 -> 5;
            case 13 -> 5;
            default -> -1;
        };
    }

    public void cargarPendientes() {
        try {
            dos.writeUTF("4");
            dos.flush();
            String json = readJson();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> h : lista) {
                String estado = h.get(ESTADO).toString();

                if (estado.equals("pendiente") || estado.equals("conflicto")) {
                    pendientes.getModelo().addRow(new Object[]{
                            h.get(ID),
                            h.get(PROFESOR),
                            h.get(ALUMNO),
                            h.get(CENTRO),
                            h.get(TITULO),
                            h.get(ASUNTO),
                            h.get(AULA)
                    });
                }
            }
            lista.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cambiarEstadoReunion(int idReu, String nuevoEstado) {
        try {
            dos.writeUTF("6");
            dos.writeUTF(String.valueOf(idReu));
            dos.writeUTF(nuevoEstado);
            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    //  CREAR REUNION
    // ---------------------------

    public void cargarCentros() {
        try {
            centros.clear();
            crearReuniones.getComboUbicacion().removeAllItems();

            dos.writeUTF("7");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();

            Centro[] lista = gson.fromJson(json, Centro[].class);

            crearReuniones.getComboUbicacion().addItem("");

            for (Centro c : lista) {
                centros.add(c);
                crearReuniones.getComboUbicacion().addItem(c.getNOM());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarAlumnos() {
        try {
            usuarios.clear();
            crearReuniones.getComboMiembros().removeAllItems();

            dos.writeUTF("8");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();

            Users[] lista = gson.fromJson(json, Users[].class);

            crearReuniones.getComboMiembros().addItem("");

            for (Users u : lista) {
                usuarios.add(u);
                crearReuniones.getComboMiembros().addItem(u.getNombre());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String consultarEstado(String fechaStr) {
        try {
            dos.writeUTF("4");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());

            for (Map<String, Object> h : lista) {

                if (fechaStr == h.get(FECHA)) {
                    return "conflicto";
                } else {
                    return "pendiente";
                }
            }
            lista.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void crearReunion() {

        ArrayList<String> nuevaReunion = new ArrayList<String>();
        ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();
        int idCentro = 0;
        int idAlumno = 0;

        if (crearReuniones.getTxtNombre().getText().isEmpty()) {
            crearReuniones.getLblError().setText("Debes poner un nombre a la reunion");
        } else if (crearReuniones.getTaTema().getText().isEmpty()) {
            crearReuniones.getLblError().setText("Debes poner un tema a la reunion");
        } else if (crearReuniones.getComboAula().getSelectedItem().toString().equals("")) {
            crearReuniones.getLblError().setText("Debes seleccionar un aula");
        } else if (crearReuniones.getComboUbicacion().getSelectedItem().toString().equals("")) {
            crearReuniones.getLblError().setText("Debes seleccionar una ubicación");
        } else if (crearReuniones.getComboMiembros().getSelectedItem().toString().equals("")) {
            crearReuniones.getLblError().setText("Debes seleccionar un alumno");
        } else if (crearReuniones.getDateChooser().getDate() == null) {
            crearReuniones.getLblError().setText("Debes seleccionar una fecha");
        } else {
            nuevaReunion.add(crearReuniones.getTxtNombre().getText().toString());
            nuevaReunion.add(crearReuniones.getTaTema().getText().toString());
            nuevaReunion.add(crearReuniones.getComboAula().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getComboUbicacion().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getComboMiembros().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getSpinnerHora().getValue().toString());
            nuevaReunion.add(crearReuniones.getDateChooser().getDate().toString());

            // Combinar fecha y hora
            Date dateChooser = crearReuniones.getDateChooser().getDate();
            Date spinnerHora = (Date) crearReuniones.getSpinnerHora().getValue();

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateChooser);

            Calendar calHora = Calendar.getInstance();
            calHora.setTime(spinnerHora);

            // Combinar hora y fecha
            cal.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, calHora.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, 0);

            // Timestamp final
            Timestamp timestamp = new Timestamp(cal.getTimeInMillis());

            // Formatear a string compatible con MySQL
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaStr = sdf.format(timestamp);

            for (Centro c : centros) {
                if (c.getNOM().equals(nuevaReunion.get(3))) {
                    idCentro = Integer.parseInt(c.getCCEN());
                }
            }

            for (Users u : usuarios) {
                if (u.getNombre().equals(nuevaReunion.get(4))) {
                    idAlumno = u.getId();
                }
            }

            String estado = consultarEstado(fechaStr);

            Map<String, Object> mapa = new java.util.HashMap<>();
            mapa.put("titulo", nuevaReunion.get(0));
            mapa.put("asunto", nuevaReunion.get(1));
            mapa.put("aula", nuevaReunion.get(2));
            mapa.put("idCentro", idCentro);
            mapa.put("idAlumno", idAlumno);
            mapa.put("fecha", fechaStr);
            mapa.put("estado", estado);
            listaEnviar.add(mapa);

            try {
                dos.writeUTF("9");
                dos.flush();
                Gson gson = new Gson();
                dos.writeUTF(gson.toJson(listaEnviar));
                dos.flush();

                nuevaReunion.clear();
                listaEnviar.clear();
                usuarios.clear();
                centros.clear();
                crearReuniones.getComboMiembros().removeAllItems();
                crearReuniones.getComboUbicacion().removeAllItems();

                crearReuniones.dispose();
                gestionReuniones.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void setConsultarReu(Vista.ConsultarReu consultarReu) {this.consultarReu = consultarReu;}
    public void setOtrosHorarios(Vista.OtrosHorarios otrosHorarios) { this.otrosHorarios = otrosHorarios; }
    public String getEstadoReunion(String clave) {return estadosReuniones.get(clave);}
    public void setGestionPendientes(Vista.GestionPendientes pendientes) {this.pendientes = pendientes;}
    public void setCrearReu(Vista.CrearReu crearReuniones) {this.crearReuniones = crearReuniones;}
    public void setGestionReuniones(Vista.GestionReuniones gestionReuniones) {this.gestionReuniones = gestionReuniones;}

}
