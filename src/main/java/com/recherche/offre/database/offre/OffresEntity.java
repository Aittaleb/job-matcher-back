package com.recherche.offre.database.offre;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OFFRES")
@SequenceGenerator(name = "SOFFRES001", sequenceName = "SOFFRES001", allocationSize = 50)
@Data
public class OffresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOFFRES001")
    private Long id;

    private String description;
}
