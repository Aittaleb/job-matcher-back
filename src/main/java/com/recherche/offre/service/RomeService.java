package com.recherche.offre.service;

import com.recherche.offre.client.FranceTravailOffresEmploiClient;
import com.recherche.offre.conf.RomeCacheConfiguration;
import com.recherche.offre.dto.CompetenceRomeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RomeService {

    private final FranceTravailOffresEmploiClient franceTravailApiClient;

    @Cacheable(
        cacheNames = RomeCacheConfiguration.ROME_CACHE_NAME,
        cacheManager = RomeCacheConfiguration.ROME_CACHE_MANAGER
    )
    public List<CompetenceRomeDto> chargerCachedRome() {
        return franceTravailApiClient.chargerReferentielCompetences();
    }
}
