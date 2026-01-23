package com.example.ElorServ;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Controlador.Consultas;
import modelo.*;

@RestController
@RequestMapping("/reuniones")
public class ControladorReuniones {

	
	Consultas consultas = new Consultas();
	private ArrayList<Reuniones> reuniones = consultas.obtenerReuniones();
	private int siguienteId = reuniones.size(); 
	
	@GetMapping
	public ArrayList<Reuniones> getReuniones() {
		
		return reuniones;
	}
	
	@PostMapping
	public String crearReunion(@RequestBody Reuniones reunion) {
		siguienteId++;
		reunion.setIdReunion(siguienteId); 
		reuniones.add(reunion);
		return "reunion con ID " + reunion.getIdReunion()+ " creado!";
	}
	
	@PutMapping("/{id}")
	public String actualizarReunion(@PathVariable int id, @RequestBody Reuniones reunionActualizado) {
	    Reuniones reunion = null;

	    for (Reuniones u : reuniones) {
	        if (u.getIdReunion() == id) {
	            reunion = u;
	        }
	    }

	    if (reunion == null) {
	        return "reunion con ID " + id + " no existe";
	    }

	    reunion.setUsersByAlumnoId(reunionActualizado.getUsersByAlumnoId());
	    reunion.setUsersByProfesorId(reunionActualizado.getUsersByProfesorId());
	    reunion.setEstado(reunionActualizado.getEstado());
	    reunion.setEstadoEus(reunionActualizado.getEstadoEus());
	    reunion.setIdCentro(reunionActualizado.getIdCentro());
	    reunion.setTitulo(reunionActualizado.getTitulo());
	    reunion.setAsunto(reunionActualizado.getAsunto());
	    reunion.setAula(reunionActualizado.getAula());
	    reunion.setFecha(reunionActualizado.getFecha());
	    reunion.setCreatedAt(reunionActualizado.getCreatedAt());
	    reunion.setUpdatedAt(reunionActualizado.getUpdatedAt());
	    
	    return "reunion con ID " + id + " actualizado a " + reunion.getTitulo();
	}


	@DeleteMapping("/{id}")
	public String eliminarReunion(@PathVariable int id) {
	    Reuniones reunionAEliminar = null;

	    for (Reuniones u : reuniones) {
	        if (u.getIdReunion() == id) {
	            reunionAEliminar = u;
	        }
	    }

	    if (reunionAEliminar == null) {
	        return "reunion con ID " + id + " no existe";
	    }

	    reuniones.remove(reunionAEliminar);
	    return "reunion " + reunionAEliminar.getTitulo() + " eliminado!";
	}

	
}
