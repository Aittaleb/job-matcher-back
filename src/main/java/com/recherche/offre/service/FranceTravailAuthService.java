package com.recherche.offre.service;

import com.recherche.offre.client.AuthClient;
import com.recherche.offre.conf.AuthTokenCacheConfiguration;
import com.recherche.offre.conf.OffreEmploiApiConfiguration;
import com.recherche.offre.dto.CredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class FranceTravailAuthService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String REALM = "/partenaire";
    private final AuthClient authClient;
    private final OffreEmploiApiConfiguration offreEmploiApiConfiguration;

    @Cacheable(cacheNames = AuthTokenCacheConfiguration.AUTH_TOKEN_CACHE, key = "'bearer'")
    public String getBearerToken() {
        return BEARER_PREFIX + requestCredentials().getAccessToken();
    }

    @CacheEvict(cacheNames = AuthTokenCacheConfiguration.AUTH_TOKEN_CACHE, key = "'bearer'", beforeInvocation = true)
    public void invalidateToken() {
        // Methode intentionnellement vide: l'annotation gere l'invalidation du cache.
    }

    private CredentialsDto requestCredentials() {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", offreEmploiApiConfiguration.getClientId());
        formData.add("client_secret", offreEmploiApiConfiguration.getClientSecret());
        formData.add("scope", offreEmploiApiConfiguration.getScope());
        return authClient.getCredentials(REALM, formData);
    }
}

