package com.recherche.offre.database.skill;

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
import lombok.experimental.Accessors;

@Entity
@Table(name = "SKILL")
@SequenceGenerator(name = "SSKILL001", sequenceName = "SSKILL001", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SSKILL001")
    private Long id;

    @Column(name = "LIBELLE", nullable = false, unique = true)
    private String libelle;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

}
