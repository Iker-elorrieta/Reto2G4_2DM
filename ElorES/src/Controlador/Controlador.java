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

import Vista.MenuProfe;



public class Controlador {
	
	Socket cliente; DataInputStream dis; DataOutputStream dos;
	
	private Vista.Login login;
	private Vista.MiPerfil miPerfil;
	private Vista.MiHorario miHorario;
	private Vista.DetalleAlumno detalleAlumno;
	private Vista.ConsultarAlumnos consultarAlumnos;
	private Vista.ConsultarReu consultarReu;
	
	private static final String EMAIL = "email";
	private static final String NOMBRE = "nombre";
	private static final String USERNAME = "username";
	private static final String APELLIDOS = "apellidos";
	private static final String DNI = "dni";
	private static final String DIRECCION = "direccion";
	private static final String TELEFONO1 = "telefono1";
	private static final String ID = "id";
	private static final String Estado = "estado";
	private static final String Profesor = "profesor";
	private static final String Alumno = "alumno";
	private static final String Centro = "centro";
	
	public Controlador(Vista.Login login) {
	    this.login = login;
	}
	
	public Controlador(Vista.MiPerfil miPerfil) {
		this.miPerfil = miPerfil;
	}
	
	public Controlador(Vista.MiHorario miHorario) {
		this.miHorario = miHorario;
	}
	
	public Controlador(Vista.DetalleAlumno detalleAlumno) {
		this.detalleAlumno = detalleAlumno;
	}
	
	public Controlador(Vista.ConsultarAlumnos consultarAlumnos) {
		this.setConsultarAlumnos(consultarAlumnos);
	}
	
	public Controlador(Vista.ConsultarReu consultarReu) {
		this.setConsultarReu(consultarReu);
		
	}
	public Controlador() {
		
		
	}
	
  
	
	
    public int validarUsuario(Socket cliente, DataInputStream dis, DataOutputStream dos) {

    	int mensaje = 0;
    	try {           

    		if(this.login.getTfcorreo().getText().isEmpty() || this.login.getTfcontraseña().getPassword().toString().isEmpty()) {
    			 login.getLblError().setText("Debes completar todos los campos");
    			 mensaje = -1;
    		} else {
    			 // Cifrar correo y contraseña
                String correoHash = cifrarUsuario(this.login.getTfcorreo().getText());
            	String contraseña = new String(this.login.getTfcontraseña().getPassword());
                String passHash = cifrarUsuario(contraseña);

                // Enviar datos al servidor
                dos.writeUTF(correoHash);
                dos.writeUTF(passHash);

                // Leer respuesta del servidor
                String respuesta = dis.readUTF();

                if (respuesta.equals("-1")) {
                    mensaje = -1;
                    login.getLblError().setText("Correo o contraseña incorrectos");
                      
                } else {
                	mensaje = Integer.parseInt(respuesta);
                	MenuProfe menu = new MenuProfe(cliente, dis, dos, mensaje);
                    menu.setVisible(true);
                }
    		}
           

        } catch (IOException e) {
            e.printStackTrace();
            mensaje = -1;
            login.getLblError().setText("Error de conexión");
        }
    	
    	return mensaje;
    }
    
    public static String cifrarUsuario(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] databytes = texto.getBytes();
            md.update(databytes);
            byte[] resumen = md.digest();
            return new String(resumen);
        } catch (NoSuchAlgorithmException e) {
            return "Error al cifrar";
        }
    }
    
    public void cargarDatosPerfil(DataInputStream dis, DataOutputStream dos, int id) {
	    try {
	        dos.writeUTF("1");
	        dos.flush();

	        String json = dis.readUTF();

	        Gson gson = new Gson();
	        ArrayList<Map<String, Object>> listaUsuarios = gson.fromJson(
	        	    json, new TypeToken<ArrayList<Map<String, Object>>>(){}.getType());

	        Map<String, Object> usuarioEncontrado = null;

	        for (Map<String, Object> usuario : listaUsuarios) {
	            Double usuarioId = Double.parseDouble(String.valueOf(usuario.get(ID)));
	            if (usuarioId == id) {
	                usuarioEncontrado = usuario;
	            }
	        }

	        if (usuarioEncontrado != null) {

	            // Asignar datos a los labels
	            miPerfil.getLblEmail().setText("Email: " + (String) usuarioEncontrado.get(EMAIL));;
				miPerfil.getLblNombre().setText("Nombre: " + (String) usuarioEncontrado.get(NOMBRE));
				miPerfil.getLblUsername().setText("Username: " + (String) usuarioEncontrado.get(USERNAME));
				miPerfil.getLblApellidos().setText("Apellidos: " + (String) usuarioEncontrado.get(APELLIDOS));
				miPerfil.getLblDNI().setText("DNI: " + (String) usuarioEncontrado.get(DNI));
				miPerfil.getLblDireccion().setText("Dirección: " + (String) usuarioEncontrado.get(DIRECCION));
				miPerfil.getLblTelefono().setText("Teléfono: " + (String) usuarioEncontrado.get(TELEFONO1));
				
	        } 
	    } catch (IOException e) {
	        e.printStackTrace();
		    miPerfil.getLblEmail().setText("No se encontró un usuario con id: " + id);

	    }
	   
    }
    
    public ArrayList<Map<String, Object>> cargarDatosHorario(DataInputStream dis, DataOutputStream dos) {
     
    	 ArrayList<Map<String, Object>> listaHorarios = null;
            try {
                dos.writeUTF("3");
                dos.flush();

                String json = dis.readUTF();

                Gson gson = new Gson();
                listaHorarios = gson.fromJson(
                        json,
                        new TypeToken<ArrayList<Map<String, Object>>>(){}.getType()
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        
        return listaHorarios;
    }
    
    
    public void rellenarTabla(ArrayList<Map<String, Object>> lista) {
    	
        for (Map<String, Object> horario : lista) {

            String hora = (String.valueOf(horario.get("hora"))); // 1–6
            String dia = (String) horario.get("dia");
            String asignatura = (String) horario.get("modulos");

            Double horaInt = Double.parseDouble(hora);
            int fila = horaInt.intValue() - 1;
            int columna = switch (dia.toLowerCase()) {
                case "lunes" -> 1;
                case "martes" -> 2;
                case "miercoles" -> 3;
                case "jueves" -> 4;
                case "viernes" -> 5;
                default -> -1;
            };

            if (columna != -1) {
                this.miHorario.getModelo().setValueAt(asignatura, fila, columna);
            }
        }
    }
    
    public DefaultTableModel cargarDatosAlumnos(DataInputStream dis, DataOutputStream dos, DefaultTableModel model) {
        try {
            dos.writeUTF("2");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> listaAlumnos = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Map<String, Object>>>() {}.getType()
            );

            model.setRowCount(0);

            for (Map<String, Object> alumno : listaAlumnos) {
            	int idAlumno = ((Double) alumno.get(ID)).intValue();
            	System.out.println("ID Alumno: " + idAlumno);
            	String dni = alumno.get(DNI).toString();
                String nombre = alumno.get(NOMBRE).toString();
                String apellidos = alumno.get(APELLIDOS).toString();

                model.addRow(new Object[]{idAlumno, dni, nombre, apellidos});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return model;
    }
    
    public void cargarDatosDetalleAlumno(DataInputStream dis, DataOutputStream dos, int idAlumno) {
    	try {
            // Pedir lista de alumnos
            dos.writeUTF("2");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> listaAlumnos = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Map<String, Object>>>() {}.getType()
            );


            Map<String, Object> alumnoEncontrado = null;

            for (Map<String, Object> alumno : listaAlumnos) {
                int idAlumno2 = ((Double) alumno.get("id")).intValue();
                if (idAlumno == idAlumno2) {
                    alumnoEncontrado = alumno;
                }
            }

            if (alumnoEncontrado != null) {
            	detalleAlumno.getLblEmail().setText("Email: " + (String) alumnoEncontrado.get(EMAIL));;
            	detalleAlumno.getLblNombre().setText("Nombre: " + (String) alumnoEncontrado.get(NOMBRE));
            	detalleAlumno.getLblUsername().setText("Username: " + (String) alumnoEncontrado.get(USERNAME));
            	detalleAlumno.getLblApellidos().setText("Apellidos: " + (String) alumnoEncontrado.get(APELLIDOS));
            	detalleAlumno.getLblDNI().setText("DNI: " + (String) alumnoEncontrado.get(DNI));
            	detalleAlumno.getLblDireccion().setText("Dirección: " + (String) alumnoEncontrado.get(DIRECCION));
            	detalleAlumno.getLblTelefono().setText("Teléfono: " + (String) alumnoEncontrado.get(TELEFONO1));
            	}

        } catch (IOException e) {
            e.printStackTrace();
        	detalleAlumno.getLblEmail().setText("Alumno no encontrado");
        }

    }

    
    public DefaultTableModel cargarDatosReuniones(DataInputStream dis, DataOutputStream dos, DefaultTableModel model) {
        try {
            dos.writeUTF("4");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> listaReuniones = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Map<String, Object>>>() {}.getType()
            );

            model.setRowCount(0);

            for (Map<String, Object> reunion : listaReuniones) {
            	String estado = reunion.get(Estado).toString();
            	String profesor = reunion.get(Profesor).toString();
                String alumno = reunion.get(Alumno).toString();
                String centro = reunion.get(Centro).toString();

                model.addRow(new Object[]{profesor, alumno, centro,estado});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return model;
    }
    
    public void cargarDatosDetalleReunion(DataInputStream dis, DataOutputStream dos, int idAlumno) {
    	try {
            // Pedir lista de alumnos
            dos.writeUTF("4");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> listaAlumnos = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Map<String, Object>>>() {}.getType()
            );

            
            Map<String, Object> alumnoEncontrado = null;

            for (Map<String, Object> alumno : listaAlumnos) {
                int idAlumno2 = ((Double) alumno.get("id")).intValue();
                if (idAlumno == idAlumno2) {
                    alumnoEncontrado = alumno;
                }
            }

            if (alumnoEncontrado != null) {
            	detalleAlumno.getLblEmail().setText("Email: " + (String) alumnoEncontrado.get(EMAIL));;
            	detalleAlumno.getLblNombre().setText("Nombre: " + (String) alumnoEncontrado.get(NOMBRE));
            	detalleAlumno.getLblUsername().setText("Username: " + (String) alumnoEncontrado.get(USERNAME));
            	detalleAlumno.getLblApellidos().setText("Apellidos: " + (String) alumnoEncontrado.get(APELLIDOS));
            	detalleAlumno.getLblDNI().setText("DNI: " + (String) alumnoEncontrado.get(DNI));
            	detalleAlumno.getLblDireccion().setText("Dirección: " + (String) alumnoEncontrado.get(DIRECCION));
            	detalleAlumno.getLblTelefono().setText("Teléfono: " + (String) alumnoEncontrado.get(TELEFONO1));
            	}

        } catch (IOException e) {
            e.printStackTrace();
        	detalleAlumno.getLblEmail().setText("Alumno no encontrado");
        }
    	
    }

	public Vista.ConsultarReu getConsultarReu() {
		return consultarReu;
	}

	public void setConsultarReu(Vista.ConsultarReu consultarReu) {
		this.consultarReu = consultarReu;
	}

	public Vista.ConsultarAlumnos getConsultarAlumnos() {
		return consultarAlumnos;
	}

	public void setConsultarAlumnos(Vista.ConsultarAlumnos consultarAlumnos) {
		this.consultarAlumnos = consultarAlumnos;
	}
	
	public void comprobarConexcion() {
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

	public Socket getCliente() { return cliente; }
	public DataInputStream getDis() { return dis; }
	public DataOutputStream getDos() { return dos; }


}
	

