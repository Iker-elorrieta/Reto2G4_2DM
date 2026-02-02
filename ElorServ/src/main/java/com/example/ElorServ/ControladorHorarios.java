package com.example.ElorServ;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Controlador.Consultas;
import Controlador.HibernateUtil;
import modelo.*;


@RestController
public class ControladorHorarios {

	@Autowired
    private Consultas consultas;
	@Autowired
    private HibernateUtil hibernateUtil;

    

    @GetMapping("/horarios/profesores")
    public ArrayList<Horarios> getProfesores() {
        return consultas.obtenerHorariosProfesor();
    }

    @GetMapping("/horarios/alumnos")
    public ArrayList<Horarios> getAlumnos() {
    	 var session = hibernateUtil.getSessionFactory().openSession();
         session.close();
        return consultas.obtenerHorariosAlumno();
    }
}

