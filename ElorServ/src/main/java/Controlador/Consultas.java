package Controlador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import modelo.*;

public class Consultas {
	
	ArrayList<Users> listaUsuarios = new ArrayList<>();
	ArrayList<Horarios> listaHorariosProfe = new ArrayList<>();
	ArrayList<Horarios> listaHorariosAlumno = new ArrayList<>();
	ArrayList<Reuniones> listaReuniones = new ArrayList<>();

	public static final String PROFESOR = "'profesor'";
	public static final String ALUMNO = "'alumno'";

	
	// ===================== USUARIOS =====================

	public ArrayList<Users> obtenerUsuarios() {
		
		listaUsuarios.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Users";
		Query<Users> q = session.createQuery(hql, Users.class);
		List<Users> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Users user = filas.get(i);
			listaUsuarios.add(user);
		}

		return listaUsuarios;
	}
	
	
	public ArrayList<Users> obtenerProfesores() {
		
		listaUsuarios.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Users where tipos.name = " + PROFESOR;
		Query<Users> q = session.createQuery(hql, Users.class);
		List<Users> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Users user = filas.get(i);
			listaUsuarios.add(user);
		}

		   
		return listaUsuarios;
	}


	// ===================== HORARIOS =====================
		public ArrayList<Horarios> obtenerHorariosProfesor() {
			
			listaHorariosProfe.clear(); 
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			String hql = "from Horarios where users.tipos.name = " + PROFESOR;
			Query<Horarios> q = session.createQuery(hql, Horarios.class);
			List<Horarios> filas = q.list();
			
			for (int i = 0; i < filas.size(); i++) {
				Horarios horario = filas.get(i);
				listaHorariosProfe.add(horario);
			}
			  
			return listaHorariosProfe;
		}
		
		public ArrayList<Horarios> obtenerHorariosAlumno() {
			
			listaHorariosAlumno.clear(); 
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			String hql = "select h from Horarios h join h.modulos m join m.ciclos c join c.matriculacioneses mat join mat.users u where u.tipos.name = " +ALUMNO;
			Query<Horarios> q = session.createQuery(hql, Horarios.class);
			List<Horarios> filas = q.list();
			
			for (int i = 0; i < filas.size(); i++) {
				Horarios horario = filas.get(i);
				listaHorariosAlumno.add(horario);
			}
			  
			return listaHorariosAlumno;
		}


	// ===================== REUNIONES =====================

	public ArrayList<Reuniones> obtenerReuniones() {
		
		listaReuniones.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Reuniones";
		Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
		List<Reuniones> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Reuniones reunion = filas.get(i);
			listaReuniones.add(reunion);
		}

		   
		return listaReuniones;
	}
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(String idProfe) {
		
		listaReuniones.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Reuniones where usersByProfesorId = " + idProfe;
		Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
		List<Reuniones> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Reuniones reunion = filas.get(i);
			listaReuniones.add(reunion);
		}

		   
		return listaReuniones;
	}


	// ===================== ALUMNOS =====================

	public ArrayList<Users> obtenerAlumnos(Users profesor) {
	    ArrayList<Users> listaAlumnos = new ArrayList<>();
	    Session session = HibernateUtil.getSessionFactory().openSession();

	    try {
	    	  String hql =
	    	            "select distinct m.users " +
	    	            "from Users p " +
	    	            "join p.horarioses h " +
	    	            "join h.modulos mo " +
	    	            "join mo.ciclos c " +
	    	            "join c.matriculacioneses m " +
	    	            "where p = :prof";
	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("prof", profesor);  

	        listaAlumnos.addAll(query.list());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return listaAlumnos;
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		
		List<Users> lista = new ArrayList<Users>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		    
		try {
			String hql = "from Users u where u.tipos.name = " + ALUMNO;
			lista = session.createQuery(hql, Users.class).list();
			    
		} catch (Exception e) {
			e.printStackTrace();
		}

		   
		return (ArrayList<Users>) lista;
	}
	
	
	// ===================== HORARIO PROFE =====================

	public ArrayList<Horarios> obtenerHorarioProfe(Users profe) {
	    ArrayList<Horarios> listaHorarios = new ArrayList<>();
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    
	    try {
	        Users profeGestionado = session.get(Users.class, profe.getId());

	        String hql = "from Horarios h where h.users = :profe";
	        Query<Horarios> query = session.createQuery(hql, Horarios.class);
	        query.setParameter("profe", profeGestionado); 
	        listaHorarios.addAll(query.list());
	        
	    } catch (Exception e) {
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		   
		return listaHorarios;
	}


	// ===================== UPDATE =====================

	public void actualizarReunion(Reuniones reunion, String estado) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
	    
		String hql = "UPDATE Reuniones r SET r.estado = :estado WHERE r = :reunion";
	    
		MutationQuery query = session.createMutationQuery(hql);
		query.setParameter("estado", estado);
		query.setParameter("reunion", reunion);

		query.executeUpdate();
		tx.commit();
	}


	public void crearReunion(Reuniones reunion) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.persist(reunion);
		tx.commit(); 

		   
	}

}
