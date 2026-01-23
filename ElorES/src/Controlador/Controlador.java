package Controlador;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
    private static final String ESTADO = "estado";
    private static final String FECHA = "fecha";
	private static final Object PROFESOR = "profesor";
	private static final Object ALUMNO = "alumno";
	private static final Object CENTRO = "centro";
	private static final Object TITULO = "titulo";
	private static final Object ASUNTO = "asunto";
	private static final Object AULA = "aula";

    // ---------------------------
    //  CONEXIÓN ÚNICA
    // ---------------------------
    public Socket cliente;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Map<String, Integer> mapaProfesores = new HashMap<>();
    private Map<String, String> estadosReuniones = new HashMap<>();


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
    public void cargarHorarioReuniones() {

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
	    	String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> lista =
                    gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());
            

            for (Map<String, Object> h : lista) {

                if (h.get(ID)!= null) {

                    int idReu = ((Double) h.get(ID)).intValue();
                    String fechaStr = h.get(FECHA).toString();
                    String estado = h.get(ESTADO).toString();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                    LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);

                    Locale localeES = Locale.forLanguageTag("es-ES");

                    int hora = fecha.getHour();
                    int fila = convertirHoraAFila(hora);
                    
                    if(estado.equals(ESTADO)) {
                    	Color color = new Color(255,255,208);
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
                            consultarReu.getModelo().setValueAt("Reunión " + idReu +" con " + alumno , fila, col);
                        }

                    }
                }
            }

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
            case 13 -> 6;
            default -> -1; 
        };
    }

    
    public void cargarPendientes() {
    	try {
			dos.writeUTF("4");
			dos.flush();
	    	String json = dis.readUTF();

	        Gson gson = new Gson();
	        ArrayList<Map<String, Object>> lista =
	                gson.fromJson(json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());
	        

	        for (Map<String, Object> h : lista) {
	        	String estado = h.get("estado").toString();
	        	
	        	if(estado.equals("pendiente")) {
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

            String respuesta = dis.readUTF();
            System.out.println("Servidor: " + respuesta);

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
    public void setConsultarReu(Vista.ConsultarReu consultarReu) {this.consultarReu = consultarReu;}
    public void setOtrosHorarios(Vista.OtrosHorarios otrosHorarios) { this.otrosHorarios = otrosHorarios; }
    public String getEstadoReunion(String clave) {return estadosReuniones.get(clave);}
    public void setGestionPendientes(Vista.GestionPendientes pendientes) {this.pendientes = pendientes;}

}
