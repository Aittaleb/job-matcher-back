package com.recherche.offre.database.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@SequenceGenerator(name = "SUSER001", sequenceName = "SUSER001", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUSER001")
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "YEARS_EXPERIENCE", nullable = false)
    private Integer yearsExperience;
}
