package com.example.ElorServ;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Controlador.Consultas;
import modelo.*;


@RestController
public class ControladorHorarios {

	Consultas consultas = new Consultas();
	private ArrayList<Horarios> profesores = consultas.obtenerHorariosProfesor();
	private ArrayList<Horarios> alumnos = consultas.obtenerHorariosAlumno();

	@GetMapping("/horarios/profesores")
	public ArrayList<Horarios> getProfesores() {
		
		return profesores;
	}
	
	@GetMapping("/horarios/alumnos")
	public ArrayList<Horarios> getAlumnos() {
		
		return alumnos;
	}

}
