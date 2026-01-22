package Controlador;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.*;
public class Consultas {
	
	ArrayList<Users> listaUsuarios = new ArrayList<>();
	ArrayList<Horarios> listaHorarios = new ArrayList<>();
	ArrayList<Reuniones> listaReuniones = new ArrayList<>();
	ArrayList<Users> listaAlumnos = new ArrayList<>();

	
	public static final String PROFESOR = "'profesor'";
	public static final String ALUMNO = "'alumno'";

	
	public ArrayList<Users> obtenerUsuarios() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Users";
	        Query<Users> q = session.createQuery(hql, Users.class);
	        List<Users> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	            Users user = (Users) filas.get(i);
	            listaUsuarios.add(user);
	        };
	     return listaUsuarios;
	}
	
	
	public ArrayList<Users> obtenerProfesores() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Users where tipos.name = " +PROFESOR;
	        Query<Users> q = session.createQuery(hql, Users.class);
	        List<Users> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	            Users user = (Users) filas.get(i);
	            listaUsuarios.add(user);
	        };
	     return listaUsuarios;
	}


	public ArrayList<Horarios> obtenerHorariosProfesor() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Horarios where users.tipos.name = " +PROFESOR;
	        Query<Horarios> q = session.createQuery(hql, Horarios.class);
	        List<Horarios> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	            Horarios horario = (Horarios) filas.get(i);
	            listaHorarios.add(horario);
	        };
	     return listaHorarios;
	}
	
	public ArrayList<Horarios> obtenerHorariosAlumno() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Horarios where users.tipos.name = " +ALUMNO;
	        Query<Horarios> q = session.createQuery(hql, Horarios.class);
	        List<Horarios> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	            Horarios horario = (Horarios) filas.get(i);
	            listaHorarios.add(horario);
	        };
	     return listaHorarios;
	}


	public ArrayList<Reuniones> obtenerReuniones() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Reuniones";
	        Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
	        List<Reuniones> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	        	Reuniones reunion = (Reuniones) filas.get(i);
	            listaReuniones.add(reunion);
	        };
	     return listaReuniones;
	}
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(int profesorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		 String hql = "from Reuniones where usersByProfesorId.id = " + profesorId;
	        Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
	        List<Reuniones> filas = q.list();
	        for (int i = 0; i < filas.size(); i++) {
	        	Reuniones reunion = (Reuniones) filas.get(i);
	            listaReuniones.add(reunion);
	        };
	     return listaReuniones;
	}

	public ArrayList<Users> obtenerAlumnos(int profesorId) {
	    ArrayList<Users> listaAlumnos = new ArrayList<>();
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    
	    try {

		    String hql = "select distinct r.usersByAlumnoId " + "from Users profesor " + "join profesor.reunionesesForProfesorId r " + "where profesor.id = :idProfesor";
		    Query<Users> query = session.createQuery(hql, Users.class);
		    query.setParameter("idProfesor", profesorId); 
		    listaAlumnos.addAll(query.list());
		    
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return listaAlumnos;
	}
	
	
	public ArrayList<Horarios> obtenerHorarioProfe(int profesorId) {
		ArrayList<Horarios> listaHorarios = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			 String hql = "from Horarios h where h.users.id = :idProfesor";
			    Query<Horarios> query = session.createQuery(hql, Horarios.class);
			    query.setParameter("idProfesor", profesorId); 
			    listaHorarios.addAll(query.list());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listaHorarios;
	}


	public ArrayList<Horarios> obtenerHorarios() {
		ArrayList<Horarios> listaHorarios = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			 String hql = "from Horarios";
			    Query<Horarios> query = session.createQuery(hql, Horarios.class);
			    listaHorarios.addAll(query.list());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listaHorarios;
	}



}
