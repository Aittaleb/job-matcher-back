package com.recherche.offre.service;

import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.SkillDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private ProfilService profilService;

    @Mock
    private OffreService offreService;

    @InjectMocks
    private MatchingService matchingService;

    @Test
    void calculerRapportCorrespondanceParIdOffreEtIdUtilisateur_cas_vide() {
        when(profilService.getInformationsProfil(anyLong())).thenReturn(new ProfilDto());
        when(offreService.fetchOfferDetails(anyString())).thenReturn(new RechercheOffreDetailsDto());

        final var actual = matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(1L, "offreId");

        verify(profilService).getInformationsProfil(1L);
        verify(offreService).fetchOfferDetails("offreId");
        verifyNoMoreInteractions(profilService, offreService);

        assertNotNull(actual);
        assertEquals(0, actual.getScore());
        assertTrue(CollectionUtils.isEmpty(actual.getCompetencesTrouvees()));
        assertTrue(CollectionUtils.isEmpty(actual.getCompetencesManquantes()));
    }

    @Test
    void calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur() {
        final SkillDto skill1 = new SkillDto().setCode("skill1");
        final SkillDto skill2 = new SkillDto().setCode("skill2");
        final SkillDto skill3 = new SkillDto().setCode("skill3");
        final ProfilDto profil = new ProfilDto().setCompetences(List.of(skill1, skill2));
        final RechercheOffreDto offre = new RechercheOffreDto();
        offre.setCompetences(List.of(skill1, skill3));

        when(profilService.getInformationsProfil(anyLong())).thenReturn(profil);

        final var actual = matchingService.calculerRapportCorrespondancePourUneOffreDonneeEtIdUtilisateur(1L, offre);

        verify(profilService).getInformationsProfil(1L);
        verifyNoMoreInteractions(profilService);
        verifyNoInteractions(offreService);

        assertNotNull(actual);
        assertEquals(50, actual.getScore());
        assertEquals(1, actual.getCompetencesTrouvees().size());
        assertEquals(skill1, actual.getCompetencesTrouvees().get(0));
        assertEquals(1, actual.getCompetencesManquantes().size());
        assertEquals(skill3, actual.getCompetencesManquantes().get(0));
    }

    @Test
    void calculerRapportCorrespondanceParIdOffreEtIdUtilisateur_cas_competence_trouvee() {
        final SkillDto skill1 = new SkillDto().setCode("skill1");
        final SkillDto skill2 = new SkillDto().setCode("skill2");
        final SkillDto skill3 = new SkillDto().setCode("skill3");
        final ProfilDto profil = new ProfilDto().setCompetences(List.of(skill1, skill2));
        final RechercheOffreDetailsDto offre = new RechercheOffreDetailsDto();
        offre.setCompetences(List.of(skill1, skill3));

        when(profilService.getInformationsProfil(anyLong())).thenReturn(profil);
        when(offreService.fetchOfferDetails(anyString())).thenReturn(offre);

        final var actual = matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(1L, "offreId");

        verify(profilService).getInformationsProfil(1L);
        verify(offreService).fetchOfferDetails("offreId");
        verifyNoMoreInteractions(profilService, offreService);

        assertNotNull(actual);
        assertEquals(50, actual.getScore());
        assertEquals(1, actual.getCompetencesTrouvees().size());
        assertEquals(skill1, actual.getCompetencesTrouvees().get(0));
        assertEquals(1, actual.getCompetencesManquantes().size());
        assertEquals(skill3, actual.getCompetencesManquantes().get(0));
    }

    @Test
    void calculerRapportCorrespondanceParIdOffreEtIdUtilisateur_cas_aucune_competence_trouvee() {
        final SkillDto skill1 = new SkillDto().setCode("skill1");
        final SkillDto skill2 = new SkillDto().setCode("skill2");
        final SkillDto skill3 = new SkillDto().setCode("skill3");
        final SkillDto skill4 = new SkillDto().setCode("skill4");
        final ProfilDto profil = new ProfilDto().setCompetences(List.of(skill1, skill2));
        final RechercheOffreDetailsDto offre = new RechercheOffreDetailsDto();
        offre.setCompetences(List.of(skill3, skill4));

        when(profilService.getInformationsProfil(anyLong())).thenReturn(profil);
        when(offreService.fetchOfferDetails(anyString())).thenReturn(offre);

        final var actual = matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(1L, "offreId");

        verify(profilService).getInformationsProfil(1L);
        verify(offreService).fetchOfferDetails("offreId");
        verifyNoMoreInteractions(profilService, offreService);

        assertNotNull(actual);
        assertEquals(0, actual.getScore());
        assertTrue(CollectionUtils.isEmpty(actual.getCompetencesTrouvees()));
        assertEquals(2, actual.getCompetencesManquantes().size());
        assertEquals(List.of(skill3, skill4), actual.getCompetencesManquantes());
    }

}