package com.recherche.offre.rest;

import com.recherche.offre.dto.RechercheOffreDetailsDto;
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
class OffreControllerTest {

    @Mock
    private OffreService offreService;

    @InjectMocks
    private OffreController offreController;

    @Test
    void rechercherOffresParMotCle_delegateAuService() {
        final List<RechercheOffreDto> expected = List.of(new RechercheOffreDto());
        when(offreService.fetchOffersByKeyword("java")).thenReturn(expected);

        final var actual = offreController.rechercherOffresParMotCle("java");

        verify(offreService).fetchOffersByKeyword("java");
        verifyNoMoreInteractions(offreService);
        assertEquals(expected, actual);
    }

    @Test
    void rechercherOffresParIdentifiant_delegateAuService() {
        final RechercheOffreDetailsDto expected = new RechercheOffreDetailsDto();
        expected.setDescription("desc");
        when(offreService.fetchOfferDetails("FT-2")).thenReturn(expected);

        final var actual = offreController.rechercherOffresParIdentifiant("FT-2");

        verify(offreService).fetchOfferDetails("FT-2");
        verifyNoMoreInteractions(offreService);
        assertEquals(expected, actual);
    }
}

