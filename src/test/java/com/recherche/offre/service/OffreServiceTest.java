package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
import com.recherche.offre.database.offre.SavedOfferEntity;
import com.recherche.offre.database.offre.SavedOfferRepository;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.dto.FranceTravailOffreDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.ResultatRechercheApiFranceTravailDto;
import com.recherche.offre.mappers.OffresMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffreServiceTest {

    @Mock
    private FranceTravailAuthService franceTravailAuthService;

    @Mock
    private FranceTravailOffresEmploiClient franceTravailClient;

    @Mock
    private SavedOfferRepository savedOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OffresMapper offresMapper;

    @InjectMocks
    private OffreService offreService;

    @Test
    void fetchOffers_retourneLaListeMappee() {
        final FranceTravailOffreDto source = new FranceTravailOffreDto();
        source.setId("FT-1");
        final RechercheOffreDto mapped = new RechercheOffreDto();
        mapped.setIdentifiantFt("FT-1");

        when(franceTravailClient.rechercherOffres()).thenReturn(new ResultatRechercheApiFranceTravailDto(List.of(source)));
        when(offresMapper.toOffreDtoList(List.of(source))).thenReturn(List.of(mapped));

        final var actual = offreService.fetchOffers();

        verify(franceTravailClient).rechercherOffres();
        verify(offresMapper).toOffreDtoList(List.of(source));
        verifyNoMoreInteractions(franceTravailClient, offresMapper, franceTravailAuthService, savedOfferRepository, userRepository);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(mapped, actual.get(0));
    }

    @Test
    void fetchOffers_retryApresUnauthorized() {
        final FeignException.Unauthorized unauthorized = mock(FeignException.Unauthorized.class);
        final FranceTravailOffreDto source = new FranceTravailOffreDto();
        source.setId("FT-2");
        final RechercheOffreDto mapped = new RechercheOffreDto();

        when(franceTravailClient.rechercherOffres())
                .thenThrow(unauthorized)
                .thenReturn(new ResultatRechercheApiFranceTravailDto(List.of(source)));
        when(offresMapper.toOffreDtoList(List.of(source))).thenReturn(List.of(mapped));

        final var actual = offreService.fetchOffers();

        verify(franceTravailClient, times(2)).rechercherOffres();
        verify(franceTravailAuthService).invalidateToken();
        verify(offresMapper).toOffreDtoList(List.of(source));
        verifyNoMoreInteractions(franceTravailClient, offresMapper, franceTravailAuthService, savedOfferRepository, userRepository);

        assertEquals(1, actual.size());
    }

    @Test
    void fetchOffersByKeyword_filtreParMotCle() {
        final FranceTravailOffreDto matchIntitule = new FranceTravailOffreDto();
        matchIntitule.setIntitule("Developpeur Java");
        matchIntitule.setDescription("Stack spring");

        final FranceTravailOffreDto noMatch = new FranceTravailOffreDto();
        noMatch.setIntitule("Comptable");
        noMatch.setDescription("Finance");

        final RechercheOffreDto mapped = new RechercheOffreDto();

        when(franceTravailClient.rechercherOffres())
                .thenReturn(new ResultatRechercheApiFranceTravailDto(List.of(matchIntitule, noMatch)));
        when(offresMapper.toOffreDtoList(List.of(matchIntitule))).thenReturn(List.of(mapped));

        final var actual = offreService.fetchOffersByKeyword("java");

        verify(franceTravailClient).rechercherOffres();
        verify(offresMapper).toOffreDtoList(List.of(matchIntitule));
        verifyNoMoreInteractions(franceTravailClient, offresMapper, franceTravailAuthService, savedOfferRepository, userRepository);

        assertEquals(1, actual.size());
        assertEquals(mapped, actual.get(0));
    }

    @Test
    void fetchOfferDetails_retryApresUnauthorized() {
        final FeignException.Unauthorized unauthorized = mock(FeignException.Unauthorized.class);
        final FranceTravailOffreDto offreFt = new FranceTravailOffreDto();
        final RechercheOffreDetailsDto details = new RechercheOffreDetailsDto();
        details.setDescription("desc");

        when(franceTravailClient.rechercherOffreParId("FT-12"))
                .thenThrow(unauthorized)
                .thenReturn(offreFt);
        when(offresMapper.toOffreDetailsDto(offreFt)).thenReturn(details);

        final var actual = offreService.fetchOfferDetails("FT-12");

        verify(franceTravailClient, times(2)).rechercherOffreParId("FT-12");
        verify(franceTravailAuthService).invalidateToken();
        verify(offresMapper).toOffreDetailsDto(offreFt);
        verifyNoMoreInteractions(franceTravailClient, offresMapper, franceTravailAuthService, savedOfferRepository, userRepository);

        assertEquals(details, actual);
    }

    @Test
    void fetchFavoriteOffers_retourneLesOffresAvecIdTechnique() {
        final SavedOfferEntity savedOffer = new SavedOfferEntity().setId(99L).setOfferId("FT-99");
        final FranceTravailOffreDto offreFt = new FranceTravailOffreDto();
        final RechercheOffreDetailsDto mapped = new RechercheOffreDetailsDto();
        mapped.setIdentifiantFt("FT-99");

        when(savedOfferRepository.findByUser_Id(1L)).thenReturn(List.of(savedOffer));
        when(franceTravailClient.rechercherOffreParId("FT-99")).thenReturn(offreFt);
        when(offresMapper.toOffreDetailsDto(offreFt)).thenReturn(mapped);

        final var actual = offreService.fetchFavoriteOffers(1L);

        verify(savedOfferRepository).findByUser_Id(1L);
        verify(franceTravailClient).rechercherOffreParId("FT-99");
        verify(offresMapper).toOffreDetailsDto(offreFt);
        verifyNoMoreInteractions(savedOfferRepository, franceTravailClient, offresMapper, franceTravailAuthService, userRepository);

        assertEquals(1, actual.size());
        assertEquals(99L, actual.get(0).getId());
        assertEquals("FT-99", actual.get(0).getIdentifiantFt());
    }

    @Test
    void sauvegarderOffre_retourneIdExistantSiDejaSauvegardee() {
        final SavedOfferEntity savedOffer = new SavedOfferEntity().setId(10L).setOfferId("FT-1");

        when(savedOfferRepository.findByUser_Id(1L)).thenReturn(List.of(savedOffer));

        final Long actual = offreService.sauvegarderOffre("FT-1", 1L);

        verify(savedOfferRepository).findByUser_Id(1L);
        verifyNoMoreInteractions(savedOfferRepository, userRepository, franceTravailClient, offresMapper, franceTravailAuthService);

        assertEquals(10L, actual);
    }

    @Test
    void sauvegarderOffre_creeUneOffreSiAbsente() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        final SavedOfferEntity persisted = new SavedOfferEntity().setId(33L).setOfferId("FT-33").setUser(user);

        when(savedOfferRepository.findByUser_Id(1L)).thenReturn(List.of());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(savedOfferRepository.save(any(SavedOfferEntity.class))).thenReturn(persisted);

        final Long actual = offreService.sauvegarderOffre("FT-33", 1L);

        verify(savedOfferRepository).findByUser_Id(1L);
        verify(userRepository).findById(1L);
        verify(savedOfferRepository).save(any(SavedOfferEntity.class));
        verifyNoMoreInteractions(savedOfferRepository, userRepository, franceTravailClient, offresMapper, franceTravailAuthService);

        assertEquals(33L, actual);
    }

    @Test
    void sauvegarderOffre_throwSiUtilisateurIntrouvable() {
        when(savedOfferRepository.findByUser_Id(anyLong())).thenReturn(List.of());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> offreService.sauvegarderOffre("FT-12", 1L));

        verify(savedOfferRepository).findByUser_Id(1L);
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(savedOfferRepository, userRepository, franceTravailClient, offresMapper, franceTravailAuthService);
    }

    @Test
    void supprimerOffre_supprimeParIdEtUtilisateur() {
        offreService.supprimerOffre(44L, 1L);

        verify(savedOfferRepository).deleteByIdAndUser_Id(44L, 1L);
        verifyNoMoreInteractions(savedOfferRepository, userRepository, franceTravailClient, offresMapper, franceTravailAuthService);
    }
}

