package com.recherche.offre.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RomeCacheConfiguration {

    public static final String ROME_CACHE_NAME = "referentielCompetences";
    public static final String ROME_CACHE_MANAGER = "romeCacheManager";

    @Bean(name = ROME_CACHE_MANAGER)
    public CacheManager romeCacheManager(final OffreEmploiApiConfiguration offreEmploiApiConfiguration) {
        final long ttlHours = offreEmploiApiConfiguration.getRomeCacheTtlHours();

        final CaffeineCacheManager cacheManager = new CaffeineCacheManager(ROME_CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(Duration.ofHours(ttlHours)));
        return cacheManager;
    }
}

