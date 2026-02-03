package Controlador;


import java.awt.Image;
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

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import modelo.*;

public class Controlador {

    // ---------------------------
    //  CONSTANTES JSON
    // ---------------------------
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
               
                // IMPORTANTE: El orden de creación de los streams importa
                // ObjectOutputStream DEBE crearse antes que ObjectInputStream
                // para evitar deadlock en el handshake inicial
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
        // si el socket no existe o si está cerrado se vuelve a abrir la conexión con el servidor
            if (cliente == null || cliente.isClosed()) {
                conectar();
            }

            String correo = login.getTfcorreo().getText();
            String pass = new String(login.getTfcontraseña().getPassword()); // La contraseña se convierte a String porque getPassword() devuelve un char[]

            if (correo.isEmpty() || pass.isEmpty()) {
                login.getLblError().setText("Debes completar todos los campos");
                return -1;
            }
           
            // Se envían dos objetos al servidor correo cifrado y contraseña cifrada
            // El servidor los recibe, los compara con los datos cifrados de la base de datos
            String correoHash = cifrar(correo);
            String passHash = cifrar(pass);

            // Envío al servidor para validación
            oos.writeObject(correoHash);
            oos.writeObject(passHash);

            String respuesta = ois.readObject().toString();

            // El servidor devuelve -1 si las credenciales son incorrectas
            // o el ID del usuario si son correctas
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
        // Se obtiene el algoritmo SHA-256, MessageDigest se encarga de crear el hash
            MessageDigest md = MessageDigest.getInstance("SHA-256");
           
            /*texto.getBytes() → convierte el texto a bytes
digest() → genera el hash
new String(...) → lo convierte a texto*/
            return new String(md.digest(texto.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return texto;
        }
    }

    // ---------------------------
    //  LECTURA JSON GRANDE
    // ---------------------------
    private String readJson() throws IOException {
        // 1. Leer cuanost bytes tiene el JSON
        int length = ois.readInt();
       
        // 2. Crear array del tamaño exacto
        byte[] data = new byte[length];
       
        // 3. Leer todos los bytes (readFully garantiza lectura completa)
        ois.readFully(data);
       
        // 4. Convertir a String UTF-8
        return new String(data, "UTF-8");
    }
   


    // ---------------------------
    //  PERFIL
    // ---------------------------
    public void cargarPerfil(int idProfe) {

        try {
            // "1" es el código de operación que el servidor entiende como "obtener usuarios"
            dos.writeUTF("1");
            dos.flush();

            String json = readJson();

            Gson gson = new Gson();
           
            // TypeToken es necesario porque Java borra los tipos genéricos en tiempo de ejecución
            // Esto permite a Gson saber que es ArrayList<Users> específicamente
            ArrayList<Users> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Users>>() {}.getType());

            // Buscar el profesor específico en la lista recibida
            for (Users u : lista) {
                int id = u.getId().intValue();
                if (id == idProfe) {

                    miPerfil.getLblEmail().setText("Email: " + u.getEmail());
                    miPerfil.getLblNombre().setText("Nombre: " + u.getNombre());
                    miPerfil.getLblUsername().setText("Username: " + u.getUsername());
                    miPerfil.getLblApellidos().setText("Apellidos: " + u.getApellidos());
                    miPerfil.getLblDNI().setText("DNI: " + u.getDni());
                    miPerfil.getLblDireccion().setText("Dirección: " + u.getDireccion());
                    miPerfil.getLblTelefono().setText("Teléfono: " + u.getTelefono1());
                    
                    // ------------------------------
                    // CARGAR FOTO DEL PERFIL
                    // ------------------------------
                    String foto = u.getArgazkiaUrl(); // nombre del archivo de la foto
                    if (foto != null && !foto.isEmpty()) {
                        ImageIcon icon = new ImageIcon("fotos/usuarios/" + foto);
                        Image img = icon.getImage().getScaledInstance(
                                miPerfil.getLblFotoPerfil().getWidth(),
                                miPerfil.getLblFotoPerfil().getHeight(),
                                Image.SCALE_SMOOTH
                        );
                        miPerfil.getLblFotoPerfil().setIcon(new ImageIcon(img));
                    }
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
            // "3" = código para obtener horarios
            dos.writeUTF("3");
            dos.flush();

            String informacion = "";
            String json = readJson();

            Gson gson = new Gson();
            ArrayList<Horarios> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Horarios>>() {}.getType());

            for (Horarios h : lista) {        

                // Convertir hora (1-6) a índice de fila de la tabla (0-5)
                int fila = h.getHora() -1;
               

                // Switch expression (Java 14+): mapea día de la semana a columna
                // Retorna directamente el valor sin necesidad de break
                int col = switch (h.getDia().toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };
               
                // HTML en JTable permite múltiples líneas y formato
                if(h.getAula() != null) {
                informacion = "<html>" + h.getModulos().getNombre() + "<br>" + h.getAula() + "</html>";
                } else {
                informacion = "<html>" +  h.getModulos().getNombre() + "<br>Sin aula asignada</html>";
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
            ArrayList<Users> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Users>>() {}.getType());

            // Limpiar datos previos
            mapaProfesores.clear();
            otrosHorarios.getCbProfesores().removeAllItems();

            for (Users h : lista) {

                // Crear copia limpia del objeto para evitar referencias mutables
                Users user = new Users();

                // Validación defensiva: verificar null antes de asignar
                // Usar .toString() en lugar de casting directo por seguridad
                if (h.getId() != null) {
                    user.setId(h.getId().intValue());
                }
                if (h.getNombre() != null) {
                    user.setNombre(h.getNombre().toString());
                }
                if (h.getApellidos() != null) {
                    user.setApellidos(h.getApellidos().toString());
                }
                if (h.getEmail() != null) {
                    user.setEmail(h.getEmail().toString());
                }
                if (h.getUsername() != null) {
                    user.setUsername(h.getUsername().toString());
                }
                if (h.getDni() != null) {
                    user.setDni(h.getDni().toString());
                }
                if (h.getTelefono1() != null) {
                    user.setTelefono1(h.getTelefono1().toString());
                }
                if (h.getTelefono2() != null) {
                    user.setTelefono2(h.getTelefono2().toString());
                }
                if (h.getDireccion() != null) {
                    user.setDireccion(h.getDireccion().toString());
                }

                // Usar HashMap para búsqueda O(1) por nombre completo
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
            // "5" = código para obtener horario de un profesor específico
            dos.writeUTF("5");
            dos.flush();
            oos.writeObject(profesor);
            oos.flush();

            String informacion;
            String json = readJson();
           
            Gson gson = new Gson();
            ArrayList<Horarios> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Horarios>>() {}.getType());

            // Limpiar tabla antes de llenarla con nuevo horario
            // 6 filas (horas) x 5 columnas (días laborables)
            for (int i = 0; i < 6; i++) {
                for (int j = 1; j < 6; j++) {
                    model.setValueAt("", i, j);
                }
            }

            for (Horarios h : lista) {        

                int fila = h.getHora() -1;
               

                int col = switch (h.getDia().toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };
                if(h.getAula() != null) {
                informacion = "<html>" + h.getModulos().getNombre() + "<br>" + h.getAula() + "</html>";
                } else {
                informacion = "<html>" +  h.getModulos().getNombre() + "<br>Sin aula asignada</html>";
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
            ArrayList<Users> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Users>>() {}.getType());

            DefaultTableModel model = consultarAlumnos.getModel();
            model.setRowCount(0);

            for (Users a : lista) {

                model.addRow(new Object[]{
                        a.getId().intValue(),
                        a.getDni(),
                        a.getNombre(),
                        a.getApellidos()
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
            ArrayList<Users> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Users>>() {}.getType());

            for (Users a : lista) {

                int id = a.getId().intValue();

                if (id == idAlumno) {

                    detalleAlumno.getLblEmail().setText("Email: " + a.getEmail());
                    detalleAlumno.getLblNombre().setText("Nombre: " + a.getNombre());
                    detalleAlumno.getLblUsername().setText("Username: " + a.getUsername());
                    detalleAlumno.getLblApellidos().setText("Apellidos: " + a.getApellidos());
                    detalleAlumno.getLblDNI().setText("DNI: " + a.getDni());
                    detalleAlumno.getLblDireccion().setText("Dirección: " + a.getDireccion());
                    detalleAlumno.getLblTelefono().setText("Teléfono: " + a.getTelefono1());
                    

                    // ------------------------------
                    // CARGAR FOTO DEL ALUMNO
                    // ------------------------------
                    String foto = a.getArgazkiaUrl(); // nombre del archivo de la foto
                    if (foto != null && !foto.isEmpty()) {
                        // Creamos un ImageIcon desde la carpeta local "fotos/usuarios/"
                        ImageIcon icon = new ImageIcon("fotos/usuarios/" + foto);

                        // Escalar la imagen al tamaño del JLabel
                        Image img = icon.getImage().getScaledInstance(
                                detalleAlumno.getLblFotoAlumno().getWidth(),
                                detalleAlumno.getLblFotoAlumno().getHeight(),
                                Image.SCALE_SMOOTH
                        );

                        detalleAlumno.getLblFotoAlumno().setIcon(new ImageIcon(img));
                    }

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
            ArrayList<Horarios> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Horarios>>() {}.getType());

            for (Horarios h : lista) {        

                int fila = h.getHora() -1;
               

                int col = switch (h.getDia().toString().toLowerCase()) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miercoles" -> 3;
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };
                if(h.getAula() != null) {
                informacion = "<html>" + h.getModulos().getNombre() + "<br>" + h.getAula() + "</html>";
                } else {
                informacion = "<html>" +  h.getModulos().getNombre() + "<br>Sin aula asignada</html>";
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
            // "4" = código para obtener reuniones
            dos.writeUTF("4");
            dos.flush();

            String json = readJson();

            Gson gson = new Gson();
            ArrayList<Reuniones> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Reuniones>>() {}.getType());

            for (Reuniones h : lista) {

                // -----------------------------
                // 1. ID DE LA REUNIÓN
                // -----------------------------
                int idReu = h.getIdReunion().intValue();

                // -----------------------------
                // 2. FECHA - PARSING COMPLEJO
                // -----------------------------
                String fechaStr = h.getFecha().toString();

                // Eliminar espacio no separable Unicode (U+202F) que puede causar problemas
                fechaStr = fechaStr.replace("\u202F", " ");
                

                // Definir dos formatos posibles de fecha:
                // SQL: "2024-01-15 14:30:00"
                DateTimeFormatter formatoSQL =
                     DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S]");

                // Inglés: "Jan 15, 2024, 2:30:00 PM"
                DateTimeFormatter formatoIngles =
                     DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);

                LocalDateTime fecha = null;

                // Intentar parsear con formato SQL primero, luego inglés
                // Esto maneja diferentes formatos que puede devolver el servidor
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
                if (fila == -1) continue; // Hora fuera del rango del horario escolar

                // -----------------------------
                // 3. DÍA DE LA SEMANA
                // -----------------------------
                Locale localeES = Locale.forLanguageTag("es-ES");
               
                // Obtener nombre del día en español (FULL = nombre completo)
                String dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, localeES).toLowerCase();

                int col = switch (dia) {
                    case "lunes" -> 1;
                    case "martes" -> 2;
                    case "miércoles", "miercoles" -> 3; // Ambas ortografías por compatibilidad
                    case "jueves" -> 4;
                    case "viernes" -> 5;
                    default -> -1;
                };
                if (col == -1) continue; // Día no laborable (fin de semana)

                // -----------------------------
                // 4. ESTADO
                // -----------------------------
                String estado = h.getEstado().toString();

                // -----------------------------
                // 5. ALUMNO
                // -----------------------------
                Users alumnoObj = h.getUsersByAlumnoId();
 
                // Operador ternario para evitar NullPointerException
                String alumno = alumnoObj != null && alumnoObj.getNombre() != null
                        ? alumnoObj.getNombre().toString()
                        : "Alumno";

                // -----------------------------
                // 6. GUARDAR ESTADO PARA COLORES
                // -----------------------------
                // Este mapa se usa luego para colorear las celdas según el estado
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



    /**
     * Convierte hora real (8:00 - 13:00) a índice de fila en la tabla (1-5)
     * El horario escolar típico va de 8:00 a 14:00
     */
    private int convertirHoraAFila(int horaReal) {
        return switch (horaReal) {
            case 8 -> 1;
            case 9 -> 2;
            case 10 -> 3;
            case 11 -> 4;
            case 12 -> 5;
            case 13 -> 5; // 13:00 va en la misma fila que 12:00 (última hora)
            default -> -1; // Hora fuera del horario escolar
        };
    }

    public void cargarPendientes() {
        try {
            dos.writeUTF("4");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();

            ArrayList<Reuniones> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Reuniones>>() {}.getType());

            for (Reuniones h : lista) {

                String estado = h.getEstado().toString();
               
                // Solo mostrar reuniones que requieren acción (pendientes o con conflicto)
                if (estado.equals(PENDIENTE) || estado.equals(CONFLICTO)) {

                    // ID
                    Object id = h.getIdReunion();

                    // PROFESOR
                    // Doble conversión JSON necesaria por cómo Gson maneja objetos anidados
                    Users profObj =
                            gson.fromJson(gson.toJson(h.getUsersByProfesorId()),
                                    new TypeToken<Users>(){}.getType());
                    String profesor = profObj.getNombre().toString();

                    // ALUMNO
                    Users alumObj =
                            gson.fromJson(gson.toJson(h.getUsersByAlumnoId()),
                                    new TypeToken<Users>(){}.getType());
                    String alumno = alumObj.getNombre();

                    // CENTRO
                    String centro = h.getIdCentro().toString();

                    // Añadir fila directamente
                    pendientes.getModelo().addRow(new Object[]{
                            id,
                            profesor,
                            alumno,
                            centro,
                            h.getTitulo(),
                            h.getAsunto(),
                            h.getAula()
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
            // "6" = código para actualizar estado de reunión
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

            // "7" = código para obtener centros educativos
            dos.writeUTF("7");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();

            Centro[] lista = gson.fromJson(json, Centro[].class);

            // Añadir opción vacía al inicio para forzar selección explícita
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

            // "8" = código para obtener alumnos
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

    /**
     * Consulta si ya existe una reunión en la fecha/hora especificada
     * Retorna "conflicto" si hay solapamiento, "pendiente" si está libre
     */
    public String consultarEstado(String fechaStr) {
        try {
            dos.writeUTF("4");
            dos.flush();

            String json = readJson();
            Gson gson = new Gson();
            ArrayList<Reuniones> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Reuniones>>() {}.getType());

            // Verificar si la fecha coincide con alguna reunión existente
            for (Reuniones h : lista) {

                if (fechaStr.equals(h.getFecha().toString())) {
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
        ArrayList<Reuniones> listaEnviar = new ArrayList<Reuniones>();
        String idCentro = "";
        int idAlumno = 0;

        // Validación exhaustiva de todos los campos antes de crear la reunión
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
            // Recopilar todos los datos del formulario
            nuevaReunion.add(crearReuniones.getTxtNombre().getText().toString());
            nuevaReunion.add(crearReuniones.getTaTema().getText().toString());
            nuevaReunion.add(crearReuniones.getComboAula().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getComboUbicacion().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getComboMiembros().getSelectedItem().toString());
            nuevaReunion.add(crearReuniones.getSpinnerHora().getValue().toString());
            nuevaReunion.add(crearReuniones.getDateChooser().getDate().toString());

            // -----------------------------
            // COMBINAR FECHA Y HORA
            // -----------------------------
            // DateChooser da solo la fecha (sin hora)
            Date dateChooser = crearReuniones.getDateChooser().getDate();
           
            // Spinner da solo la hora (sin fecha)
            Date spinnerHora = (Date) crearReuniones.getSpinnerHora().getValue();

            // Usar Calendar para combinar ambos
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateChooser);

            Calendar calHora = Calendar.getInstance();
            calHora.setTime(spinnerHora);

            // Sobrescribir la hora del Calendar de fecha con la hora del spinner
            cal.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, calHora.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, 0); // Resetear milisegundos para precisión

            // Timestamp final para MySQL
            Timestamp timestamp = new Timestamp(cal.getTimeInMillis());

            // Formatear a string compatible con MySQL (YYYY-MM-DD HH:MM:SS)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaStr = sdf.format(timestamp);

            // Convertir nombre de centro seleccionado a su ID
            for (Centro c : centros) {
                if (c.getNOM().equals(nuevaReunion.get(3))) {
                    idCentro = c.getCCEN();
                }
            }

            // Convertir nombre de alumno seleccionado a su ID
            for (Users u : usuarios) {
                if (u.getNombre().equals(nuevaReunion.get(4))) {
                    idAlumno = u.getId();
                }
            }

            // Verificar si hay conflicto de horario
            String estado = consultarEstado(fechaStr);
            Users user = new Users();

            // Crear objeto Reuniones con todos los datos
            Reuniones reunion = new Reuniones();
            reunion.setTitulo(nuevaReunion.get(0));
            reunion.setAsunto(nuevaReunion.get(1));
            reunion.setAula(nuevaReunion.get(2));
            reunion.setIdCentro(idCentro);
            user.setId(idAlumno);
            reunion.setUsersByAlumnoId(user);
            reunion.setFecha(timestamp);
            reunion.setEstado(estado);

            listaEnviar.add(reunion);

            try {
                // "9" = código para crear nueva reunión
                dos.writeUTF("9");
                dos.flush();
                Gson gson = new Gson();
               
                // Enviar reunión serializada en JSON
                oos.writeObject(gson.toJson(listaEnviar));
                oos.flush();

                // Limpiar todos los datos temporales
                nuevaReunion.clear();
                listaEnviar.clear();
                usuarios.clear();
                centros.clear();
                crearReuniones.getComboMiembros().removeAllItems();
                crearReuniones.getComboUbicacion().removeAllItems();

                // Cerrar ventana de creación y mostrar gestión
                crearReuniones.dispose();
                gestionReuniones.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void cerrarConexion() {
        try {
            // "0" = código para cerrar conexión del lado del servidor
            dos.writeUTF("0");
            dos.flush();
           
            // Cerrar todos los streams en orden inverso a su creación
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
