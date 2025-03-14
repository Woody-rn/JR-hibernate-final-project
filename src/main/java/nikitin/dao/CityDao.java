package nikitin.dao;

import nikitin.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CityDao {
    private final SessionFactory sessionFactory;

    public CityDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<City> getItems(int offset, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query<City> cites = session.createQuery("from City", City.class);
        cites.setFirstResult(offset);
        cites.setMaxResults(limit);
        return cites.list();
    }

    public City getById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Query<City> query = session.createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }

    public int getTotalCount() {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }
}
