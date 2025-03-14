package nikitin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "country", schema = "world")
public class Country {
    @Id
    private Integer id;

    @Column(length = 3, nullable = false)
    private String code;

    @Column(name = "code_2", length = 2, nullable = false)
    private String code2;

    @Column(length = 52, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Continent continent;

    @Column(length = 26, nullable = false)
    private String region;

    @Column(name = "surface_area", nullable = false)
    private BigDecimal surfaceArea;

    @Column(name = "indep_year")
    private Short indepYear;

    @Column(nullable = false)
    private Integer population;

    @Column(name = "life_expectancy", precision = 3, scale = 1)
    private BigDecimal lifeExpectancy;

    @Column(precision = 10, scale = 2)
    private BigDecimal gnp;

    @Column(name = "gnpo_id", precision = 10, scale = 2)
    private BigDecimal gnpId;

    @Column(name = "local_name", length = 45, nullable = false)
    private String localName;

    @Column(name = "government_form", length = 45, nullable = false)
    private String governmentForm;

    @Column(name = "head_of_state", length = 60)
    private String headOfState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital")
    private City capital;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Set<CountryLanguage> languages;
}
