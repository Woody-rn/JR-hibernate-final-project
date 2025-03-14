package nikitin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "country_language", schema = "world")
public class CountryLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(length = 30, nullable = false)
    private String language;

    @Column(name = "is_official", nullable = false)
    private Boolean isOfficial;

    @Column(precision = 4, scale = 1, nullable = false)
    private BigDecimal percentage;


}
