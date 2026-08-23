package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OffreService {
    private final FranceTravailAuthService franceTravailAuthService;
    private final FranceTravailOffresEmploiClient franceTravailClient;


    public String fetchOffers() {
        try {
            return franceTravailClient.rechercherOffres();
        } catch (final FeignException.Unauthorized exception) {
            franceTravailAuthService.invalidateToken();
            return franceTravailClient.rechercherOffres();
        }
    }
}
