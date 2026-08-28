package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
import com.recherche.offre.dto.CompetenceRomeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RomeServiceTest {

    @Mock
    private FranceTravailOffresEmploiClient franceTravailApiClient;

    @InjectMocks
    private RomeService romeService;

    @Test
    void chargerCachedRome_retourneLesCompetences() {
        final CompetenceRomeDto competence = new CompetenceRomeDto();
        competence.setCode("M1805");
        final List<CompetenceRomeDto> expected = List.of(competence);

        when(franceTravailApiClient.chargerReferentielCompetences()).thenReturn(expected);

        final var actual = romeService.chargerCachedRome();

        verify(franceTravailApiClient).chargerReferentielCompetences();
        verifyNoMoreInteractions(franceTravailApiClient);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}

