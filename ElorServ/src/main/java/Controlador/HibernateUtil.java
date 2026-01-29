package Controlador;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateUtil(Environment env) {
        this.sessionFactory = buildSessionFactory(env);
    }

    private SessionFactory buildSessionFactory(Environment env) {
        try {
            Configuration cfg = new Configuration();
            cfg.configure();
            Properties props = new Properties();
            props.put("hibernate.connection.url", env.getProperty("db.url"));
            props.put("hibernate.connection.username", env.getProperty("db.username"));
            props.put("hibernate.connection.password", env.getProperty("db.password"));
            cfg.setProperties(props);
            return cfg.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
