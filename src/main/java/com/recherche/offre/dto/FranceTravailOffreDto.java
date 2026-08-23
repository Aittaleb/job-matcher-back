package com.recherche.offre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FranceTravailOffreDto implements Serializable {

    private String id;
    private String intitule;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateActualisation;
    private LieuTravailDto lieuTravail;
    private String romeCode;
    private String romeLibelle;
    private String typeContrat;
    private List<CompetenceDto> competences;
    private SalaireDto salaire;

}
