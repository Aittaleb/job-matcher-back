package com.recherche.offre.dto;

import lombok.Data;

import java.util.List;

@Data
public class RechercheOffreDto {

    private Long id;
    private String identifiantFt;
    private String intituleOffre;
    private String lieuTravail;
    private String codePostal;
    private String salaire;
    private List<SkillDto> competences;

}
