package nikitin.config;

import nikitin.domain.City;
import nikitin.domain.Country;
import org.hibernate.SessionFactory;

import java.util.Objects;

public class PreparedSessionFactory {
    private static SessionFactory sessionFactory;

    public static SessionFactory get() {
        if (Objects.isNull(sessionFactory)) {
            sessionFactory = HibernateConfiguration.get()
                    .addAnnotatedClass(City.class)
                    .addAnnotatedClass(Country.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    private PreparedSessionFactory() {
    }
}
