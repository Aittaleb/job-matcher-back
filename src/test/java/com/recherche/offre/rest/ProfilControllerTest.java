package com.recherche.offre.rest;

import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.service.ProfilService;
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
class ProfilControllerTest {

    @Mock
    private ProfilService profilService;

    @InjectMocks
    private ProfilController profilController;

    @Test
    void getProfil_delegateAuService() {
        final ProfilDto expected = new ProfilDto().setPrenom("Aya");
        when(profilService.getInformationsProfil(1L)).thenReturn(expected);

        final var actual = profilController.getProfil(1L);

        verify(profilService).getInformationsProfil(1L);
        verifyNoMoreInteractions(profilService);
        assertEquals(expected, actual);
    }

    @Test
    void updateProfil_delegateAuService() {
        final ProfilDto input = new ProfilDto().setPrenom("Aya");
        final ProfilDto expected = new ProfilDto().setPrenom("Aya").setNom("Dupont");
        when(profilService.updateProfil(1L, input)).thenReturn(expected);

        final var actual = profilController.updateProfil(1L, input);

        verify(profilService).updateProfil(1L, input);
        verifyNoMoreInteractions(profilService);
        assertEquals(expected, actual);
    }
}

