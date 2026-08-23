package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
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
}
