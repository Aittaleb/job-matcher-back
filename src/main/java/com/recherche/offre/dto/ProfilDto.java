package com.recherche.offre.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ProfilDto {

    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String localisation;
    private String codePostal;
    private Integer anneeExperience;
    private List<SkillDto> competences;

}
