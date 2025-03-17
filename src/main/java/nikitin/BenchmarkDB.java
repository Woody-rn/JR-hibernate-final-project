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
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 2, time = 2)
public class BenchmarkDB {
    private CityDao cityDao;
    private ObjectMapper mapper;
    private RedisClient redisClient;
    private SessionFactory sessionFactory;
    private List<Integer> ids;

    @Setup(Level.Trial)
    public void setup() {
        ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);
        mapper = new ObjectMapper();
        redisClient = RedisClientCreator.get();
        sessionFactory = PreparedSessionFactory.get();
        cityDao = new CityDao(sessionFactory);
        CountryDao countryDao = new CountryDao(sessionFactory);
        PrepareData prepareData = new PrepareData(sessionFactory, cityDao, countryDao, mapper, redisClient);
        prepareData.begin();
        sessionFactory.getCurrentSession().close();
    }

    @Benchmark
    public void testMysqlData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDao.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    @Benchmark
    public void testRedisData() {
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

    @TearDown(Level.Trial)
    public void tearDown() {
        if (Objects.nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (Objects.nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}
