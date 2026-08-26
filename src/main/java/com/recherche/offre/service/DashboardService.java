package com.recherche.offre.service;

import com.recherche.offre.dto.DashboardDto;
import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OffreService offreService;
    private final MatchingService matchingService;

    public DashboardDto getDashboard(Long userId) {
        final List<RechercheOffreDto> offres = offreService.fetchOffers();
        final List<Integer> listeScores = new ArrayList<>();
        final List<SkillDto> listeCompetencesManquantes = new ArrayList<>();
        offres.stream().filter(rechercheOffreDto -> !CollectionUtils.isEmpty(rechercheOffreDto.getCompetences())).forEach(offre -> {
            final RapportCorrespondanceDto rapport = matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(userId, offre);
            listeScores.add(rapport.getScore());
            listeCompetencesManquantes.addAll(rapport.getCompetencesManquantes());
        });

        // calcul du score moyen
        final Integer scoreMoyen = listeScores.stream().mapToInt(Integer::intValue).sum() / listeScores.size();
        final Integer nombreOffresAnalysees = listeScores.size();

        // top 3 des compétences les plus réccurentes à développer
        final List<SkillDto> topCompetences = listeCompetencesManquantes.stream()
                .collect(java.util.stream.Collectors.groupingBy(SkillDto::getCode, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(e -> listeCompetencesManquantes.stream().filter(s -> s.getCode().equals(e.getKey())).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return new DashboardDto()
                .setMatchMoyen(scoreMoyen)
                .setNombreOffreAnalysees(nombreOffresAnalysees)
                .setNombreOffreFavories(offreService.fetchFavoriteOffers(userId).size())
                .setCompetencesADevelopper(topCompetences);
    }
}
