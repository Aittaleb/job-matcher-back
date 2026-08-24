package com.recherche.offre.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RapportCorrespondanceDto {
    private Integer score;
    private List<SkillDto> competencesTrouvees;
    private List<SkillDto> competencesManquantes;
}
