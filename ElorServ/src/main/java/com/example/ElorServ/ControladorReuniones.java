package com.example.ElorServ;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

import Controlador.Consultas;
import Controlador.HibernateUtil;
import modelo.*;

@RestController
@RequestMapping("/reuniones")
public class ControladorReuniones {

    private final Consultas consultas;

    private ArrayList<Reuniones> reuniones;
    private int siguienteId;
    
    private final HibernateUtil hibernateUtil;

    public ControladorReuniones(HibernateUtil hibernateUtil, Consultas consultas) {
        this.hibernateUtil = hibernateUtil;
        this.consultas = consultas;
    }

    @PostConstruct
    public void init() {
        this.reuniones = consultas.obtenerReuniones();
        this.siguienteId = reuniones.size();
    }

    @GetMapping
    public ArrayList<Reuniones> getReuniones() {
    	 var session = hibernateUtil.getSessionFactory().openSession();
         session.close();
        return reuniones;
    }

    @PostMapping
    public String crearReunion(@RequestBody Reuniones reunion) {
        siguienteId++;
        reunion.setIdReunion(siguienteId);
        reuniones.add(reunion);
        return "reunion con ID " + reunion.getIdReunion() + " creado!";
    }

    @PutMapping("/{id}")
    public String actualizarReunion(@PathVariable int id, @RequestBody Reuniones reunionActualizado) {
        Reuniones reunion = reuniones.stream()
                .filter(r -> r.getIdReunion() == id)
                .findFirst()
                .orElse(null);

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
        Reuniones reunion = reuniones.stream()
                .filter(r -> r.getIdReunion() == id)
                .findFirst()
                .orElse(null);

        if (reunion == null) {
            return "reunion con ID " + id + " no existe";
        }

        reuniones.remove(reunion);
        return "reunion " + reunion.getTitulo() + " eliminado!";
    }
}
