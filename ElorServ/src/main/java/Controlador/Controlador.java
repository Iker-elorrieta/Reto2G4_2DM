package Controlador;

import java.util.ArrayList;

import com.example.ElorServ.Centro;
import com.example.ElorServ.LeerJson;

import modelo.*;

public class Controlador {

	LeerJson leerJson = new LeerJson();
	Consultas gestor = new Consultas();
	
	public ArrayList<Users> obtenerProfesores() {
		 return gestor.obtenerProfesores();
	}

	public ArrayList<Users> obtenerAlumnos(int profesorId) {
		
		return gestor.obtenerAlumnos(profesorId);
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		return gestor.obtenerTodosAlumnos();
	}
	
	public ArrayList<Horarios> obtenerHorarioProfe(int profesorId) {
		return gestor.obtenerHorarioProfe(profesorId);
	}
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(int profesorId) {
		return gestor.obtenerReunionesPorProfesor(profesorId);
	}
	
	public String obtenerNombreCentroPorId(String idCentro) {
	    ArrayList<Centro> centros = leerJson.getCentros();
	    for (Centro c : centros) {
	        if (c.getCCEN().equals(idCentro)) {
	            return c.getNOM(); 
	        }
	    }
	    return null;
	}
	
	
	public ArrayList<Horarios> obtenerHorarios() {
		return gestor.obtenerHorarios();
	}

	public void actualizarReunion(Integer idCentro, String Estado) {
		gestor.actualizarReunion(idCentro, Estado);
		
	}

	public ArrayList<Centro> leerJson() {
		ArrayList<Centro> centros = leerJson.getCentros();
		return centros;
	}

	public void crearReunion(Reuniones reunion) {
		
		gestor.crearReunion(reunion);
		
	}
}
