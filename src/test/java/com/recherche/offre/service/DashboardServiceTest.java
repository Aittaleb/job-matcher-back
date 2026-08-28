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

        final RechercheOffreDto offre1 = new RechercheOffreDto().setCompetences(List.of(java));
        final RechercheOffreDto offre2 = new RechercheOffreDto().setCompetences(List.of(spring));

        final RapportCorrespondanceDto rapport1 = new RapportCorrespondanceDto()
                .setScore(80)
                .setCompetencesManquantes(List.of(java, spring, java));
        final RapportCorrespondanceDto rapport2 = new RapportCorrespondanceDto()
                .setScore(60)
                .setCompetencesManquantes(List.of(spring, docker));

        when(offreService.fetchOffers()).thenReturn(List.of(offre1, offre2));
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre1)).thenReturn(rapport1);
        when(matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre2)).thenReturn(rapport2);
        when(offreService.fetchFavoriteOffers(1L)).thenReturn(List.of(new RechercheOffreDto(), new RechercheOffreDto()));

        final DashboardDto actual = dashboardService.getDashboard(1L);

        verify(offreService).fetchOffers();
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre1);
        verify(matchingService).calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre2);
        verify(offreService).fetchFavoriteOffers(1L);
        verifyNoMoreInteractions(offreService, matchingService);

        assertNotNull(actual);
        assertEquals(70, actual.getMatchMoyen());
        assertEquals(2, actual.getNombreOffreAnalysees());
        assertEquals(2, actual.getNombreOffreFavories());
        assertEquals(3, actual.getCompetencesADevelopper().size());
        assertTrue(actual.getCompetencesADevelopper().contains(java));
        assertTrue(actual.getCompetencesADevelopper().contains(spring));
        assertTrue(actual.getCompetencesADevelopper().contains(docker));
    }
}


