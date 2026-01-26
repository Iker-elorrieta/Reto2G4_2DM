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
	ArrayList<Horarios> listaHorarios = new ArrayList<>();
	ArrayList<Reuniones> listaReuniones = new ArrayList<>();
	ArrayList<Users> listaAlumnos = new ArrayList<>();

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
		
		listaHorarios.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Horarios where users.tipos.name = " + PROFESOR;
		Query<Horarios> q = session.createQuery(hql, Horarios.class);
		List<Horarios> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Horarios horario = filas.get(i);
			listaHorarios.add(horario);
		}

		   
		return listaHorarios;
	}
	
	public ArrayList<Horarios> obtenerHorariosAlumno() {
		
		listaHorarios.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Horarios where users.tipos.name = " + ALUMNO;
		Query<Horarios> q = session.createQuery(hql, Horarios.class);
		List<Horarios> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Horarios horario = filas.get(i);
			listaHorarios.add(horario);
		}

		   
		return listaHorarios;
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
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(int profesorId) {
		
		listaReuniones.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Reuniones where usersByProfesorId.id = " + profesorId;
		Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
		List<Reuniones> filas = q.list();
		
		for (int i = 0; i < filas.size(); i++) {
			Reuniones reunion = filas.get(i);
			listaReuniones.add(reunion);
		}

		   
		return listaReuniones;
	}


	// ===================== ALUMNOS =====================

	public ArrayList<Users> obtenerAlumnos(int profesorId) {
		
		listaAlumnos.clear();  

		Session session = HibernateUtil.getSessionFactory().openSession();
	    
		try {
			String hql = "select distinct r.usersByAlumnoId "
					   + "from Users profesor "
					   + "join profesor.reunionesesForProfesorId r "
					   + "where profesor.id = :idProfesor";
			
			Query<Users> query = session.createQuery(hql, Users.class);
			query.setParameter("idProfesor", profesorId); 
			listaAlumnos.addAll(query.list());
		    
		} catch (Exception e) {
			e.printStackTrace();
		}

		   
		return listaAlumnos;
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		
		listaAlumnos.clear();  
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		    
		try {
			String hql = "from Users u where u.tipos.name = " + ALUMNO;
			Query<Users> query = session.createQuery(hql, Users.class);
			listaAlumnos.addAll(query.list());
			    
		} catch (Exception e) {
			e.printStackTrace();
		}

		   
		return listaAlumnos;
	}
	
	
	// ===================== HORARIO PROFE =====================

	public ArrayList<Horarios> obtenerHorarioProfe(int profesorId) {
		
		ArrayList<Horarios> listaHorarios = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			String hql = "from Horarios h where h.users.id = :idProfesor";
			Query<Horarios> query = session.createQuery(hql, Horarios.class);
			query.setParameter("idProfesor", profesorId); 
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

	public void actualizarReunion(Integer idReunion, String estado) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
	    
		String hql = "UPDATE Reuniones r SET r.estado = :estado WHERE r.idReunion = :id";
	    
		MutationQuery query = session.createMutationQuery(hql);
		query.setParameter("estado", estado);
		query.setParameter("id", idReunion);

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
