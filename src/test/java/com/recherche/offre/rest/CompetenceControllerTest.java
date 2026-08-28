package com.recherche.offre.rest;

import com.recherche.offre.dto.CompetenceRomeDto;
import com.recherche.offre.service.RomeService;
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
class CompetenceControllerTest {

    @Mock
    private RomeService romeService;

    @InjectMocks
    private CompetenceController competenceController;

    @Test
    void chargerRefRome_delegateAuService() {
        final List<CompetenceRomeDto> expected = List.of(new CompetenceRomeDto());
        when(romeService.chargerCachedRome()).thenReturn(expected);

        final var actual = competenceController.chargerRefRome();

        verify(romeService).chargerCachedRome();
        verifyNoMoreInteractions(romeService);
        assertEquals(expected, actual);
    }
}

