package com.recherche.offre.service;

import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final ProfilService profilService;
    private final OffreService offreService;

    public RapportCorrespondanceDto calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(final Long profilId, final String offreId) {
        var profil = profilService.getInformationsProfil(profilId);
        var offre = offreService.fetchOfferDetails(offreId);
        return getRapportCorrespondanceDto(offre, profil);
    }

    public RapportCorrespondanceDto calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(final Long profilId, final RechercheOffreDto offre) {
        var profil = profilService.getInformationsProfil(profilId);
        return getRapportCorrespondanceDto(offre, profil);
    }

    private RapportCorrespondanceDto getRapportCorrespondanceDto(final RechercheOffreDto offre, final ProfilDto profil) {
        if (profil == null || offre == null || CollectionUtils.isEmpty(profil.getCompetences()) || CollectionUtils.isEmpty(offre.getCompetences())) {
            return new RapportCorrespondanceDto()
                    .setScore(0)
                    .setCompetencesTrouvees(List.of())
                    .setCompetencesManquantes(List.of());
        }

        final List<SkillDto> profilCompetences = profil.getCompetences();
        final List<SkillDto> offreCompetences = offre.getCompetences();
        final List<SkillDto> competencesTrouvees = profilCompetences.stream()
                .filter(profilCompetence -> offreCompetences.stream()
                        .anyMatch(offreCompetence -> profilCompetence.getCode().equals(offreCompetence.getCode())))
                .toList();
        final List<SkillDto> competencesManquantes = offreCompetences.stream()
                .filter(offreCompetence -> profilCompetences.stream()
                        .noneMatch(profilCompetence -> profilCompetence.getCode().equals(offreCompetence.getCode())))
                .toList();

        final int score = competencesTrouvees.size() * 100 / (competencesTrouvees.size() + competencesManquantes.size());

        return new RapportCorrespondanceDto()
                .setScore(score)
                .setCompetencesTrouvees(competencesTrouvees)
                .setCompetencesManquantes(competencesManquantes);
    }

}
