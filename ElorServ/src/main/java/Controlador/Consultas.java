package Controlador;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelo.*;

@Component
public class Consultas {
	


	public static final String PROFESOR = "'profesor'";
	public static final String ALUMNO = "'alumno'";

	@Autowired
    private  HibernateUtil hibernateUtil;

    
	
	// ===================== USUARIOS =====================

	public ArrayList<Users> obtenerUsuarios() {
		

		Session session = hibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Users";
		Query<Users> q = session.createQuery(hql, Users.class);
		
		return (ArrayList<Users>) q.list();
	}
	
	
	public ArrayList<Users> obtenerProfesores() {
		

		Session session = hibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Users where tipos.name = " + PROFESOR;
		Query<Users> q = session.createQuery(hql, Users.class);
		return (ArrayList<Users>) q.list();
	}


	// ===================== HORARIOS =====================
		public ArrayList<Horarios> obtenerHorariosProfesor() {
			
			Session session = hibernateUtil.getSessionFactory().openSession();
			
			String hql = "from Horarios where users.tipos.name = " + PROFESOR;
			Query<Horarios> q = session.createQuery(hql, Horarios.class);
			
			return (ArrayList<Horarios>) q.list();
		}
		
		public ArrayList<Horarios> obtenerHorariosAlumno() {
			
			Session session = hibernateUtil.getSessionFactory().openSession();
			
			String hql = "select h from Horarios h join h.modulos m join m.ciclos c join c.matriculacioneses mat join mat.users u where u.tipos.name = " +ALUMNO;
			Query<Horarios> q = session.createQuery(hql, Horarios.class);
			
			  
			return (ArrayList<Horarios>) q.list();
		}


	// ===================== REUNIONES =====================

	public ArrayList<Reuniones> obtenerReuniones() {
		

		Session session = hibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Reuniones";
		Query<Reuniones> q = session.createQuery(hql, Reuniones.class);		

		return (ArrayList<Reuniones>) q.list();
	}
	
	public ArrayList<Reuniones> obtenerReunionesPorProfesor(String idProfe) {


	    Session session = hibernateUtil.getSessionFactory().openSession();

	    //Cargar el objeto Users del profesor
	    Users profesor = session.get(Users.class, Integer.parseInt(idProfe));

	    // Consulta usando el objeto Users
	    String hql = """
	        select r
	        from Reuniones r
	        join fetch r.usersByAlumnoId
	        join fetch r.usersByProfesorId
	        where r.usersByProfesorId = :prof
	    """;

	    Query<Reuniones> q = session.createQuery(hql, Reuniones.class);
	    q.setParameter("prof", profesor);

	    return (ArrayList<Reuniones>) q.list();
	}


	// ===================== ALUMNOS =====================

	public ArrayList<Users> obtenerAlumnos(Users profesor) {
	    Session session = hibernateUtil.getSessionFactory().openSession();

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

	 
	    return (ArrayList<Users>) query.list();
	}
	
	public ArrayList<Users> obtenerTodosAlumnos() {
		
		List<Users> lista = new ArrayList<Users>();
		Session session = hibernateUtil.getSessionFactory().openSession();
		    
		try {
			String hql = "from Users u where u.tipos.name = " + ALUMNO;
			lista = session.createQuery(hql, Users.class).list();
			    
		} catch (Exception e) {
			e.printStackTrace();
		}

		   
		return (ArrayList<Users>) lista;
	}
	
	
	// ===================== HORARIO PROFE =====================

	public ArrayList<Horarios> obtenerHorarioProfe(String idProfe) {

	    Session session = hibernateUtil.getSessionFactory().openSession();

	    String hql = "from Horarios h join fetch modulos where h.users = " +idProfe;

	    Query<Horarios> query = session.createQuery(hql, Horarios.class);

	    return new ArrayList<>(query.list());
	}

	
	

	public ArrayList<Horarios> obtenerHorarios() {
		
		Session session = hibernateUtil.getSessionFactory().openSession();
		
		String hql = "from Horarios";
		Query<Horarios> query = session.createQuery(hql, Horarios.class);
		   
		return (ArrayList<Horarios>) query.list();
	}


	// ===================== UPDATE =====================

	public void actualizarReunion(Reuniones reunion, String estado) {

		Session session = hibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
	    
		String hql = "UPDATE Reuniones r SET r.estado = :estado WHERE r = :reunion";
	    
		MutationQuery query = session.createMutationQuery(hql);
		query.setParameter("estado", estado);
		query.setParameter("reunion", reunion);

		query.executeUpdate();
		tx.commit();
	}


	public void crearReunion(Reuniones reunion) {
		
		Session session = hibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.persist(reunion);
		tx.commit(); 

		   
	}

}
