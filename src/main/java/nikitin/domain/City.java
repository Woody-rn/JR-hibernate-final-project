package nikitin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "city", schema = "world")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 35, nullable = false)
    private String name;

    @Getter
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(length = 20, nullable = false)
    private String district;

    @Column(nullable = false)
    private Integer population;
}
