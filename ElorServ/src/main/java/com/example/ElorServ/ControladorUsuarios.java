package com.example.ElorServ;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;

import Controlador.Consultas;
import Controlador.HibernateUtil;
import jakarta.annotation.PostConstruct;
import modelo.Users;


@RestController
@RequestMapping("/users")
public class ControladorUsuarios {
	

	private Consultas consultas; 
	private ArrayList<Users> usuarios;
	private int siguienteId = 17; 
	
	private final HibernateUtil hibernateUtil;

	public ControladorUsuarios(HibernateUtil hibernateUtil, Consultas consultas) {
		this.hibernateUtil = hibernateUtil;
		this.consultas = consultas;
	}
	

    @PostConstruct
    public void init() {
        this.usuarios = consultas.obtenerUsuarios();
    }
	
	@GetMapping
	public ArrayList<Users> getUsuarios() {
		
		return usuarios;
	}
	
	@PostMapping
	public String crearUsuario(@RequestBody Users usuario) {
		siguienteId++;
		usuario.setId(siguienteId); 
		usuarios.add(usuario);
		return "Usuario " + usuario.getNombre()+ " creado!";
	}
	
	@PutMapping("/{id}")
	public String actualizarUsuario(@PathVariable int id, @RequestBody Users usuarioActualizado) {
	    Users usuario = null;

	    for (Users u : usuarios) {
	        if (u.getId() == id) {
	            usuario = u;
	        }
	    }

	    if (usuario == null) {
	        return "Usuario con ID " + id + " no existe";
	    }

	    usuario.setTipos(usuarioActualizado.getTipos());
	    usuario.setEmail(usuarioActualizado.getEmail());
	    usuario.setUsername(usuarioActualizado.getUsername());
	    usuario.setPassword(usuarioActualizado.getPassword());
	    usuario.setNombre(usuarioActualizado.getNombre());
	    usuario.setApellidos(usuarioActualizado.getApellidos());
	    usuario.setDni(usuarioActualizado.getDni());
	    usuario.setDireccion(usuarioActualizado.getDireccion());
	    usuario.setTelefono1(usuarioActualizado.getTelefono1());
	    usuario.setTelefono2(usuarioActualizado.getTelefono2());

	    return "Usuario con ID " + id + " actualizado a " + usuario.getNombre();
	}


	@DeleteMapping("/{id}")
	public String eliminarUsuario(@PathVariable int id) {
	    Users usuarioAEliminar = null;

	    for (Users u : usuarios) {
	        if (u.getId() == id) {
	            usuarioAEliminar = u;
	        }
	    }

	    if (usuarioAEliminar == null) {
	        return "Usuario con ID " + id + " no existe";
	    }

	    usuarios.remove(usuarioAEliminar);
	    return "Usuario " + usuarioAEliminar.getNombre() + " eliminado!";
	}

	public HibernateUtil getHibernateUtil() {
		return hibernateUtil;
	}

	
	


}
