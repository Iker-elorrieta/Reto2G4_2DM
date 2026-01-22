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
	
	Socket cliente;
	
	String userEmail;
	
	String userContraseña;

	public HiloServidor(Socket cliente, String userEmail, String userContraseña) {
		this.cliente = cliente;
		this.userEmail = userEmail;
		this.userContraseña = userContraseña;
	}
	
	
	public HiloServidor() { 
	}

	public HiloServidor(Socket cliente) {
		this.cliente = cliente;
	}

	public Socket getCliente() {return cliente;}
	public void setCliente(Socket cliente) {this.cliente = cliente;}


	public String getUserEmail() {return userEmail;}
	public void setUserEmail(String userEmail) {this.userEmail = userEmail;}


	public String getUserContraseña() {return userContraseña;}
	public void setUserContraseña(String userContraseña) {this.userContraseña = userContraseña;}
	
	
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
	                    
	                    if (emailCifrado.equals(correo) &&
	                        passCifrada.equals(contraseña)) {
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
	            
		   	  menu(idProfe);

	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("El error aqui");
	        }
	 }
	 
	 public void menu(String idProfe) {
		 Controlador controlador = new Controlador();
	     ArrayList<Users> listaUsuarios = controlador.obtenerProfesores();
		 ArrayList<Users> listaAlumnos = controlador.obtenerAlumnos(Integer.parseInt(idProfe));

	   	   int opcionInt = 0;
	   	try {
	   		

			 do {
				 if(cliente != null) {
		   	        DataInputStream dis2  = new DataInputStream(cliente.getInputStream());
			        DataOutputStream dos2 = new DataOutputStream(cliente.getOutputStream());
			        
		   	    	String opcion = dis2.readUTF();
		            opcionInt = Integer.parseInt(opcion);
		            
		            //Caso 0: cliente cerrado
		            //Caso 1: Ver perfil
		            //Caso 2: Ver alumnos
		            //Caso 3: horario del profesor
		            //Caso 4: reuniones del profesor
		            //Caso 5: detalles alumnos
		            
		            switch(opcionInt) {
		             
		            case 0: 
		            	cliente.close();
		            	break;
		            case 1: 
		            	Gson gson = new Gson();
		            	String json = gson.toJson(listaUsuarios);
		            	 dos2.writeUTF(json);
		            	 dos2.flush();
		            	break;
		            
		            case 2:
		            	Gson gsonAlumnos = new Gson();
		            	String jsonAlum = gsonAlumnos.toJson(listaAlumnos);
		            	dos2.writeUTF(jsonAlum);
		            	dos2.flush();
		            	break;
		            	
		            case 3:
		                ArrayList<Horarios> listaHorarioProfe = controlador.obtenerHorarioProfe(Integer.parseInt(idProfe));

		                ArrayList<Map<String, Object>> listaEnviar = new ArrayList<>();

		                for (Horarios h : listaHorarioProfe) {
		                    Map<String, Object> mapa = new java.util.HashMap<>();
		                    mapa.put("hora", h.getHora());                  
		                    mapa.put("dia", h.getDia());                    
		                    String nombreModulo = (h.getModulos() != null) ? h.getModulos().getNombre() : "";
		                    mapa.put("modulos", nombreModulo);

		                    listaEnviar.add(mapa);
		                }

		                Gson gsonHorario = new Gson();
		                String jsonHorario = gsonHorario.toJson(listaEnviar);

		                dos2.writeUTF(jsonHorario);
		                dos2.flush();
		                break;
		                
		            case 4:
		                ArrayList<Reuniones> listaReunionesProfe = controlador.obtenerReunionesPorProfesor(Integer.parseInt(idProfe));

		                ArrayList<Map<String, Object>> listaEnviarReuniones = new ArrayList<>();

		                for (Reuniones r : listaReunionesProfe) {
		                    Map<String, Object> mapa = new java.util.HashMap<>();
		                    mapa.put("estado", r.getEstado());                  
		                    mapa.put("profesor", r.getUsersByProfesorId().getNombre());
		                    mapa.put("alumno", r.getUsersByAlumnoId().getNombre());
		                    mapa.put("titulo", r.getTitulo());
		                    mapa.put("asunto", r.getAsunto());
		                    mapa.put("aula", r.getAula());
		                    mapa.put("fecha", r.getFecha().toString());
		                    mapa.put("centro",controlador.obtenerNombreCentroPorId(r.getIdCentro()));

		                    listaEnviarReuniones.add(mapa);
		                }

		                Gson gsonReuniones= new Gson();
		                String jsonReuniones = gsonReuniones.toJson(listaEnviarReuniones);

		                dos2.writeUTF(jsonReuniones);
		                dos2.flush();
		                break;
		            case 5:
		            	Gson gsonAlumnos2 = new Gson();
		            	String jsonAlum2 = gsonAlumnos2.toJson(listaAlumnos);
		            	dos2.writeUTF(jsonAlum2);
		            	dos2.flush();
		            	break;
		            }
				 		}    
		   	     }while(opcionInt != 0);
	   		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	    
	 }

	public static String cifrarUsuario(String texto) {
		 try {
			 MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte databytes[] = texto.getBytes();
			 md.update(databytes);
			 byte resumen[] = md.digest();
			 String textoHash = new String(resumen);
			 return textoHash;
		 } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			 return "Error al cifrar";
		 }
		 
	}
}
