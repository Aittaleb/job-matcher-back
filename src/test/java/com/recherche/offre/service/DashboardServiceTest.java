package com.recherche.offre.service;

import com.recherche.offre.dto.DashboardDto;
import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.SkillDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private OffreService offreService;

    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getDashboard_calculeMoyenneTopCompetencesEtFavoris() {
        final SkillDto java = new SkillDto().setCode("java");
        final SkillDto spring = new SkillDto().setCode("spring");
        final SkillDto docker = new SkillDto().setCode("docker");

        final RechercheOffreDto offre1 = new RechercheOffreDto().setId(1L).setIntituleOffre("Offre 1").setCompetences(List.of(java));
        final RechercheOffreDto offre2 = new RechercheOffreDto().setId(2L).setIntituleOffre("Offre 2").setCompetences(List.of(spring));
        final RechercheOffreDto offre3 = new RechercheOffreDto().setId(3L).setIntituleOffre("Offre 3").setCompetences(List.of(docker));
        final RechercheOffreDto offre4 = new RechercheOffreDto().setId(4L).setIntituleOffre("Offre 4").setCompetences(List.of(java, spring));

        final RapportCorrespondanceDto rapport1 = new RapportCorrespondanceDto()
                .setScore(80)
                .setCompetencesManquantes(List.of(java, spring, java));
        final RapportCorrespondanceDto rapport2 = new RapportCorrespondanceDto()
                .setScore(60)
                .setCompetencesManquantes(List.of(spring, docker));
        final RapportCorrespondanceDto rapport3 = new RapportCorrespondanceDto()
                .setScore(90)
                .setCompetencesManquantes(List.of(docker));
        final RapportCorrespondanceDto rapport4 = new RapportCorrespondanceDto()
                .setScore(50)
                .setCompetencesManquantes(List.of(java));

        when(offreService.fetchOffers()).thenReturn(List.of(offre1, offre2, offre3, offre4));
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre1)).thenReturn(rapport1);
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre2)).thenReturn(rapport2);
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre3)).thenReturn(rapport3);
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre4)).thenReturn(rapport4);
        when(offreService.fetchFavoriteOffers(1L)).thenReturn(List.of(new RechercheOffreDto(), new RechercheOffreDto()));

        final DashboardDto actual = dashboardService.getDashboard(1L);

        verify(offreService).fetchOffers();
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre1);
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre2);
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre3);
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre4);
        verify(offreService).fetchFavoriteOffers(1L);
        verifyNoMoreInteractions(offreService, matchingService);

        assertNotNull(actual);
        assertEquals(70, actual.getMatchMoyen());
        assertEquals(4, actual.getNombreOffreAnalysees());
        assertEquals(2, actual.getNombreOffreFavories());
        assertEquals(3, actual.getCompetencesADevelopper().size());
        assertTrue(actual.getCompetencesADevelopper().contains(java));
        assertTrue(actual.getCompetencesADevelopper().contains(spring));
        assertTrue(actual.getCompetencesADevelopper().contains(docker));
        assertEquals(3, actual.getOffresProposees().size());
        assertEquals(3L, actual.getOffresProposees().get(0).getId());
        assertEquals(90, actual.getOffresProposees().get(0).getScoreMatching());
        assertEquals(1L, actual.getOffresProposees().get(1).getId());
        assertEquals(80, actual.getOffresProposees().get(1).getScoreMatching());
        assertEquals(2L, actual.getOffresProposees().get(2).getId());
        assertEquals(60, actual.getOffresProposees().get(2).getScoreMatching());
    }

    @Test
    void getDashboard_retourneUnTop3VideEtUneMoyenneAZeroQuandAucuneOffreNADeCompetences() {
        when(offreService.fetchOffers()).thenReturn(List.of(new RechercheOffreDto(), new RechercheOffreDto().setCompetences(List.of())));
        when(offreService.fetchFavoriteOffers(1L)).thenReturn(List.of());

        final DashboardDto actual = dashboardService.getDashboard(1L);

        verify(offreService).fetchOffers();
        verify(offreService).fetchFavoriteOffers(1L);
        verifyNoMoreInteractions(offreService, matchingService);

        assertNotNull(actual);
        assertEquals(0, actual.getMatchMoyen());
        assertEquals(0, actual.getNombreOffreAnalysees());
        assertEquals(0, actual.getNombreOffreFavories());
        assertTrue(actual.getCompetencesADevelopper().isEmpty());
        assertTrue(actual.getOffresProposees().isEmpty());
    }
}


