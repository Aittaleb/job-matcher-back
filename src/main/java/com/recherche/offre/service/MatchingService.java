package com.recherche.offre.service;

import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final ProfilService profilService;
    private final OffreService offreService;

    public RapportCorrespondanceDto calculMatchingSelonCompetences(final Long profilId, final String offreId) {
        var profil = profilService.getInformationsProfil(profilId);
        var offre = offreService.fetchOfferDetails(offreId);

        if (profil == null || offre == null) {
            return new RapportCorrespondanceDto()
                    .setScore(0)
                    .setCompetencesTrouvees(List.of())
                    .setCompetencesManquantes(List.of());
        }

        final List<SkillDto> profilCompetences = profil.getCompetences();
        final List<SkillDto> offreCompetences = offre.getCompetences();
        final List<SkillDto> competencesTrouvees = profilCompetences.stream()
                .filter(profilCompetence -> offreCompetences.stream()
                        .anyMatch(offreCompetence -> offreCompetence.getCode().equals(profilCompetence.getCode())))
                .toList();
        final List<SkillDto> competencesManquantes = offreCompetences.stream()
                .filter(offreCompetence -> profilCompetences.stream()
                        .noneMatch(profilCompetence -> offreCompetence.getCode().equals(profilCompetence.getCode())))
                .toList();

        final Integer score = competencesTrouvees.size() * 100 / (competencesTrouvees.size() + competencesManquantes.size());

        return new RapportCorrespondanceDto()
                .setScore(score)
                .setCompetencesTrouvees(competencesTrouvees)
                .setCompetencesManquantes(competencesManquantes);
    }

}
