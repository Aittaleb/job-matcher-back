package com.recherche.offre.rest;

import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.service.OffreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffreFavorieControllerTest {

    @Mock
    private OffreService offreService;

    @InjectMocks
    private OffreFavorieController offreFavorieController;

    @Test
    void getFavoriteOffers_delegateAuService() {
        final List<RechercheOffreDto> expected = List.of(new RechercheOffreDto());
        when(offreService.fetchFavoriteOffers(1L)).thenReturn(expected);

        final var actual = offreFavorieController.getFavoriteOffers(1L);

        verify(offreService).fetchFavoriteOffers(1L);
        verifyNoMoreInteractions(offreService);
        assertEquals(expected, actual);
    }

    @Test
    void sauvegarderOffre_delegateAuService() {
        when(offreService.sauvegarderOffre("FT-1", 1L)).thenReturn(55L);

        final Long actual = offreFavorieController.sauvegarderOffre("FT-1", 1L);

        verify(offreService).sauvegarderOffre("FT-1", 1L);
        verifyNoMoreInteractions(offreService);
        assertEquals(55L, actual);
    }

    @Test
    void supprimerOffre_delegateAuService() {
        offreFavorieController.supprimerOffre(9L, 1L);

        verify(offreService).supprimerOffre(9L, 1L);
        verifyNoMoreInteractions(offreService);
    }
}

