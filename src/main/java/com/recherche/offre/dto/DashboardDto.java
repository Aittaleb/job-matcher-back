package com.recherche.offre.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DashboardDto {
    private Integer matchMoyen;
    private Integer nombreOffreAnalysees;
    private Integer nombreOffreFavories;
    private List<SkillDto> competencesADevelopper;
    private List<DashboardOffreDto> offresProposees;
}
