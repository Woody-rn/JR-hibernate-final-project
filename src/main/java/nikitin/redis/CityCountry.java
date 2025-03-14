package nikitin.redis;

import lombok.Getter;
import lombok.Setter;
import nikitin.domain.Continent;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class CityCountry {
    private Integer id;

    private String name;

    private String district;

    private Integer population;

    private String countryCode;

    private String alternativeCountryCode;

    private String countryName;

    private Continent continent;

    private String countryRegion;

    private BigDecimal countrySurfaceArea;

    private Integer countryPopulation;

    private Set<Language> languages;
}
