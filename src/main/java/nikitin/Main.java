package nikitin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import nikitin.config.PreparedSessionFactory;
import nikitin.config.RedisClientCreator;
import nikitin.dao.CityDao;
import nikitin.dao.CountryDao;
import nikitin.domain.City;
import nikitin.domain.CountryLanguage;
import nikitin.redis.CityCountry;
import nikitin.services.PrepareData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Main {
    private final SessionFactory sessionFactory;

    private final CityDao cityDao;
    private final CountryDao countryDao;
    private final ObjectMapper mapper;

    private final RedisClient redisClient;

    private final PrepareData prepareData;

    public Main() {
        this.sessionFactory = PreparedSessionFactory.get();
        this.redisClient = RedisClientCreator.get();
        this.cityDao = new CityDao(sessionFactory);
        this.countryDao = new CountryDao(sessionFactory);
        this.mapper = new ObjectMapper();
        this.prepareData = new PrepareData(sessionFactory, cityDao, countryDao, mapper, redisClient);
    }

    public static void main(String[] args) {
        /*URLClassLoader classLoader = (URLClassLoader) Main.class.getClassLoader();
        StringBuilder classpath = new StringBuilder();
        for (URL url : classLoader.getURLs()) {
            classpath.append(url.getPath()).append(File.pathSeparator);
        }
        System.setProperty("java.class.path", classpath.toString());*/

        Main main = new Main();
        main.prepareData.begin();

        //закроем текущую сессию, чтоб точно делать запрос к БД, а не вытянуть данные из кэша
        main.sessionFactory.getCurrentSession().close();

        //выбираем случайных 10 id городов
        //так как мы не делали обработку невалидных ситуаций, используй существующие в БД id
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        main.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        main.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        main.shutdown();
    }


    private void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDao.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    private void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void shutdown() {
        if (Objects.nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (Objects.nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}