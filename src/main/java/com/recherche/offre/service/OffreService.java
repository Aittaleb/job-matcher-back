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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    public List<RechercheOffreDto> fetchFavoriteOffers(final Long userId) {
        return savedOfferRepository.findByUser_Id(userId)
                .stream()
                .map(savedOffer -> fetchOfferDetails(savedOffer.getOfferId()).setId(savedOffer.getId()))
                .toList();
    }

    public Long sauvegarderOffre(final String offerId, final Long userId) {
        final List<SavedOfferEntity> alreadySavedOfferList = savedOfferRepository.findByUser_Id(userId)
                .stream()
                .filter(savedOffer -> savedOffer.getOfferId().equals(offerId))
                .toList();
        if (!CollectionUtils.isEmpty(alreadySavedOfferList)) {
            return alreadySavedOfferList.get(0).getId();
        }
        return savedOfferRepository.save(
                new SavedOfferEntity()
                        .setOfferId(offerId)
                        .setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + userId)))
        ).getId();
    }

    @Transactional
    public void supprimerOffre(final Long idTechnique, final Long userId) {
        savedOfferRepository.deleteByIdAndUser_Id(idTechnique, userId);
    }
}
