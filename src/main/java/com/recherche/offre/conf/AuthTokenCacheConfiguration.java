package com.recherche.offre.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AuthTokenCacheConfiguration {

    public static final String AUTH_TOKEN_CACHE = "franceTravailAuthToken";

    @Bean
    public CacheManager cacheManager(final OffreEmploiApiConfiguration offreEmploiApiConfiguration) {
        final long ttlSeconds = Math.max(1L, offreEmploiApiConfiguration.getAuthCacheTtlSeconds());

        final CaffeineCacheManager cacheManager = new CaffeineCacheManager(AUTH_TOKEN_CACHE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(Duration.ofSeconds(ttlSeconds)));
        return cacheManager;
    }
}

