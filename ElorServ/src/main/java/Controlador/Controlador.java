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

	public ArrayList<Users> obtenerAlumnos(Users profesor) {
		
		return gestor.obtenerAlumnos(profesor);
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		return gestor.obtenerTodosAlumnos();
	}
	
	public ArrayList<Horarios> obtenerHorarioProfe(Users profe) {
		return gestor.obtenerHorarioProfe(profe);
	}
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(String idProfe) {
		return gestor.obtenerReunionesPorProfesor(idProfe);
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

	public void actualizarReunion(Reuniones reunion, String Estado) {
		gestor.actualizarReunion(reunion, Estado);
		
	}

	public ArrayList<Centro> leerJson() {
		ArrayList<Centro> centros = leerJson.getCentros();
		return centros;
	}

	public void crearReunion(Reuniones reunion) {
		
		gestor.crearReunion(reunion);
		
	}
}
