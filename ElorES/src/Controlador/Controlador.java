package Controlador;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import modelo.Centro;
import modelo.Users;

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
    private static final String AULA = "aula";
	private static final Object IDREUNION = "idReunion";
	private static final Object USERSBYALUMNOID = "usersByAlumnoId";
	private static final Object IDCENTRO = "idCentro";
	private static final Object USERSBYPROFESORID = "usersByProfesorId";
	private static final Object PENDIENTE = "pendiente";
	private static final Object CONFLICTO = "conflicto";

    // ---------------------------
    //  CONEXIÓN ÚNICA
    // ---------------------------
    public Socket cliente;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private DataOutputStream dos;

    private Map<String, Users> mapaProfesores = new HashMap<>();
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

    
    private static int SOCKET_PORT = Integer.parseInt(System.getenv().getOrDefault("SOCKET_PORT", "5000"));

	private static String SOCKET_HOST = System.getenv().getOrDefault("SOCKET_HOST", "localhost");
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
                cliente = new Socket(SOCKET_HOST, SOCKET_PORT);
                oos = new ObjectOutputStream(cliente.getOutputStream());
                ois = new ObjectInputStream(cliente.getInputStream());
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

            oos.writeObject(correoHash);
            oos.writeObject(passHash);

            String respuesta = ois.readObject().toString();

            if (respuesta.equals("-1")) {
                login.getLblError().setText("Correo o contraseña incorrectos");
                return -1;
            }

            return Integer.parseInt(respuesta);

        } catch (IOException e) {
            e.printStackTrace();
            login.getLblError().setText("Error de conexión");
            return -1;
        } catch (ClassNotFoundException e) {
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
        int length = ois.readInt();
        byte[] data = new byte[length];
        ois.readFully(data);
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

            String informacion = "";
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
                
                if(h.get(AULA) != null) {
                	informacion = "<html>" + h.get(MODULOS) + "<br>" + h.get(AULA) + "</html>";
                } else {
                	informacion = "<html>" + h.get(MODULOS) + "<br>Sin aula asignada</html>";
                }

                if (col != -1) {
                    miHorario.getModelo().setValueAt(informacion, fila, col);
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

                Users user = new Users();

                if (h.get("id") != null) {
                    user.setId(((Double) h.get("id")).intValue()); 
                }
                if (h.get("nombre") != null) {
                    user.setNombre(h.get("nombre").toString());
                }
                if (h.get("apellidos") != null) {
                    user.setApellidos(h.get("apellidos").toString());
                }
                if (h.get("email") != null) {
                    user.setEmail(h.get("email").toString());
                }
                if (h.get("username") != null) {
                    user.setUsername(h.get("username").toString());
                }
                if (h.get("dni") != null) {
                    user.setDni(h.get("dni").toString());
                }
                if (h.get("telefono1") != null) {
                    user.setTelefono1(h.get("telefono1").toString());
                }
                if (h.get("telefono2") != null) {
                    user.setTelefono2(h.get("telefono2").toString());
                }
                if (h.get("direccion") != null) {
                    user.setDireccion(h.get("direccion").toString());
                }

                String nombreVisible = user.getNombre() + " " + user.getApellidos();
                mapaProfesores.put(nombreVisible, user); 
                otrosHorarios.getCbProfesores().addItem(nombreVisible);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cargarHorarioDeProfesor(Users profesor) {

        DefaultTableModel model = otrosHorarios.getModelo();

        try {
            dos.writeUTF("5");
            dos.flush();
            oos.writeObject(profesor);
            oos.flush();

            String informacion;
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

                if(h.get(AULA) != null) {
                	informacion = "<html>" + h.get(MODULOS) + "<br>" + h.get(AULA) + "</html>";
                } else {
                	informacion = "<html>" + h.get(MODULOS) + "<br>Sin aula asignada</html>";
                }

                if (col != -1) {
                    otrosHorarios.getModelo().setValueAt(informacion, fila, col);
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
            String informacion; 
            
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
                
                if(h.get(AULA) != null) {
                	informacion = "<html>" + h.get(MODULOS) + "<br>" + h.get(AULA) + "</html>";
                } else {
                	informacion = "<html>" + h.get(MODULOS) + "<br>Sin aula asignada</html>";
                }

                if (col != -1) {
                    consultarReu.getModelo().setValueAt(informacion, fila, col);
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

                // -----------------------------
                // 1. ID DE LA REUNIÓN
                // -----------------------------
                int idReu = ((Double) h.get(IDREUNION)).intValue();

                // -----------------------------
                // 2. FECHA
                // -----------------------------
                String fechaStr = h.get(FECHA).toString();

                fechaStr = fechaStr.replace("\u202F", " ");

                DateTimeFormatter formatoSQL =
                     DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S]");

                DateTimeFormatter formatoIngles =
                     DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);

                LocalDateTime fecha = null;

                try {
                	fecha = LocalDateTime.parse(fechaStr, formatoSQL);
                	} catch (Exception e1) {
                		try {
                			fecha = LocalDateTime.parse(fechaStr, formatoIngles);
                			} catch (Exception e2) {
                				e2.printStackTrace();
                				}
                		}


                int hora = fecha.getHour();
                int fila = convertirHoraAFila(hora);
                if (fila == -1) continue;

                // -----------------------------
                // 3. DÍA DE LA SEMANA
                // -----------------------------
                Locale localeES = Locale.forLanguageTag("es-ES");
                String dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, localeES).toLowerCase();

                int col = switch (dia) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miércoles", "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };
                if (col == -1) continue;

                // -----------------------------
                // 4. ESTADO
                // -----------------------------
                String estado = h.get(ESTADO).toString();

                // -----------------------------
                // 5. ALUMNO 
                // -----------------------------
                Map<String, Object> alumnoObj =
                	    gson.fromJson(gson.toJson(h.get(USERSBYALUMNOID)),
                	                  new TypeToken<Map<String, Object>>(){}.getType());

                String alumno = alumnoObj != null && alumnoObj.get(NOMBRE) != null
                        ? alumnoObj.get("nombre").toString()
                        : "Alumno";

                // -----------------------------
                // 6. GUARDAR ESTADO PARA COLORES
                // -----------------------------
                String clave = "Reunion" + idReu;
                estadosReuniones.put(clave, estado.toLowerCase());

                // -----------------------------
                // 7. PINTAR EN LA TABLA
                // -----------------------------
                consultarReu.getModelo().setValueAt(
                        "Reunión " + idReu + " con " + alumno,
                        fila,
                        col
                );
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
                if (estado.equals(PENDIENTE) || estado.equals(CONFLICTO)) {

                    // ID
                    Object id = h.get(IDREUNION);

                    // PROFESOR
                    Map<String, Object> profObj =
                            gson.fromJson(gson.toJson(h.get(USERSBYPROFESORID)),
                                    new TypeToken<Map<String, Object>>(){}.getType());
                    String profesor = profObj.get("nombre").toString();

                    // ALUMNO
                    Map<String, Object> alumObj =
                            gson.fromJson(gson.toJson(h.get(USERSBYALUMNOID)),
                                    new TypeToken<Map<String, Object>>(){}.getType());
                    String alumno = alumObj.get("nombre").toString();

                    // CENTRO
                    String centro = h.get(IDCENTRO).toString();

                    // Añadir fila directamente
                    pendientes.getModelo().addRow(new Object[]{
                            id,
                            profesor,
                            alumno,
                            centro,
                            h.get("titulo"),
                            h.get("asunto"),
                            h.get("aula")
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
            dos.flush();
            oos.writeObject(String.valueOf(idReu));
            oos.writeObject(nuevoEstado);
            oos.flush();

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
                oos.writeObject(gson.toJson(listaEnviar));
                oos.flush();

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
            ois.close();
            oos.close();
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
    public Map<String, Users> getMapaProfesores() {return mapaProfesores;}

}
