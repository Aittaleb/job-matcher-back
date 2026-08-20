package com.recherche.offre.db.offres;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OFFRES")
@Data
public class OffresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SOFFRES001")
    private Long id;

    private String description;
}
