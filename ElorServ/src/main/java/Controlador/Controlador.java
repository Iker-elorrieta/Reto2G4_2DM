package Controlador;

import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ElorServ.Centro;
import com.example.ElorServ.LeerJson;

import modelo.*;

@Component
public class Controlador {


    private final Consultas gestor;
    private final LeerJson leerJson;

    @Autowired
    public Controlador(Consultas gestor, LeerJson leerJson) {
        this.gestor = gestor;
        this.leerJson = leerJson;
    }
	public ArrayList<Users> obtenerProfesores() {
		 return gestor.obtenerProfesores();
	}

	public ArrayList<Users> obtenerAlumnos(Users profesor) {
		
		return gestor.obtenerAlumnos(profesor);
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		return gestor.obtenerTodosAlumnos();
	}
	
	public ArrayList<Horarios> obtenerHorarioProfe(String idProfe) {
		return gestor.obtenerHorarioProfe(idProfe);
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
