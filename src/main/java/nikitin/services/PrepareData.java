package nikitin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import nikitin.dao.CityDao;
import nikitin.dao.CountryDao;
import nikitin.domain.City;
import nikitin.domain.Country;
import nikitin.domain.CountryLanguage;
import nikitin.redis.CityCountry;
import nikitin.redis.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PrepareData {
    private final SessionFactory sessionFactory;

    private final CityDao cityDao;
    private final CountryDao countryDao;
    private final ObjectMapper mapper;

    private final RedisClient redisClient;


    public PrepareData(SessionFactory sessionFactory, CityDao cityDao, CountryDao countryDao, ObjectMapper mapper, RedisClient redisClient) {
        this.sessionFactory = sessionFactory;
        this.cityDao = cityDao;
        this.countryDao = countryDao;
        this.mapper = mapper;
        this.redisClient = redisClient;
    }

    public void begin() {
        List<City> allCities = fetchData();
        List<CityCountry> preparedData = transformData(allCities);
        pushToRedis(preparedData);
    }

    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {

            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            List<Country> countries = countryDao.getAllCountries();

            int totalCount = cityDao.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDao.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;

        }
    }

    private List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
            CityCountry res = new CityCountry();
            res.setId(city.getId());
            res.setName(city.getName());
            res.setPopulation(city.getPopulation());
            res.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            res.setAlternativeCountryCode(country.getCode2());
            res.setContinent(country.getContinent());
            res.setCountryCode(country.getCode());
            res.setCountryName(country.getName());
            res.setCountryPopulation(country.getPopulation());
            res.setCountryRegion(country.getRegion());
            res.setCountrySurfaceArea(country.getSurfaceArea());
            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(cl -> {
                Language language = new Language();
                language.setLanguage(cl.getLanguage());
                language.setIsOfficial(cl.getIsOfficial());
                language.setPercentage(cl.getPercentage());
                return language;
            }).collect(Collectors.toSet());
            res.setLanguages(languages);

            return res;
        }).collect(Collectors.toList());
    }
}
