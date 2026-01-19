package Controlador;

import java.util.ArrayList;

import modelo.*;

public class Controlador {

	Consultas gestor = new Consultas();
	
	public ArrayList<Users> obtenerProfesores() {
		 return gestor.obtenerProfesores();
	}

	public ArrayList<Users> obtenerAlumnos(int profesorId) {

		return gestor.obtenerAlumnos(profesorId);
	}
	
	public ArrayList<Horarios> obtenerHorarioProfe(int profesorId) {
		return gestor.obtenerHorarioProfe(profesorId);
	}

}
