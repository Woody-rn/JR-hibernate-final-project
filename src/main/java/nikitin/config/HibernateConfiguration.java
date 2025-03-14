package nikitin.config;

import nikitin.domain.City;
import nikitin.domain.Country;
import nikitin.domain.CountryLanguage;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

public class HibernateConfiguration {
    private static Configuration configuration;

    public static Configuration get() {
        if (Objects.isNull(configuration)) {
            configuration = new Configuration()
                    .addAnnotatedClass(City.class)
                    .addAnnotatedClass(Country.class)
                    .addAnnotatedClass(CountryLanguage.class);
        }
        return configuration;
    }

    private HibernateConfiguration() {}
}
