package com.recherche.offre.rest;

import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.service.MatchingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchingControllerTest {

    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private MatchingController matchingController;

    @Test
    void getMatchingOffers_delegateAuService() {
        final RapportCorrespondanceDto expected = new RapportCorrespondanceDto().setScore(75);
        when(matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(1L, "FT-7")).thenReturn(expected);

        final var actual = matchingController.getMatchingOffers(1L, "FT-7");

        verify(matchingService).calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(1L, "FT-7");
        verifyNoMoreInteractions(matchingService);
        assertEquals(expected, actual);
    }
}

