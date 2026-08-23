package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
import com.recherche.offre.database.offre.SavedOfferEntity;
import com.recherche.offre.database.offre.SavedOfferRepository;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.mappers.OffresMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OffreService {
    private final FranceTravailAuthService franceTravailAuthService;
    private final FranceTravailOffresEmploiClient franceTravailClient;
    private final SavedOfferRepository savedOfferRepository;
    private final UserRepository userRepository;
    private final OffresMapper offresMapper;

    public List<RechercheOffreDto> fetchOffers() {
        try {
            return offresMapper.toOffreDtoList(franceTravailClient.rechercherOffres().resultats());
        } catch (final FeignException.Unauthorized exception) {
            franceTravailAuthService.invalidateToken();
            return offresMapper.toOffreDtoList(franceTravailClient.rechercherOffres().resultats());
        }
    }

    public RechercheOffreDetailsDto fetchOfferDetails(final String id) {
        try {
            return offresMapper.toOffreDetailsDto(franceTravailClient.rechercherOffreParId(id));
        } catch (final FeignException.Unauthorized exception) {
            franceTravailAuthService.invalidateToken();
            return offresMapper.toOffreDetailsDto(franceTravailClient.rechercherOffreParId(id));
        }
    }

    public Long sauvegarderOffre(final String offerId, final Long userId) {
        return savedOfferRepository.save(
                new SavedOfferEntity()
                        .setOfferId(offerId)
                        .setUser(userRepository.findById(userId).orElse(null))
        ).getId();
    }

    public void supprimerOffre(final Long idTechnique, final Long userId) {
        savedOfferRepository.deleteByIdAndUser_Id(idTechnique, userId);
    }
}
