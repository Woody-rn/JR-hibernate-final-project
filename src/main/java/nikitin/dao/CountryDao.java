package nikitin.dao;

import nikitin.domain.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class CountryDao {
    private final SessionFactory sessionFactory;

    public CountryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Country> getAllCountries() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select c from Country c join fetch c.languages", Country.class).list();
    }
}
