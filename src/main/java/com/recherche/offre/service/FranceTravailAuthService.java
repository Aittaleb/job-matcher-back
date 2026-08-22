package com.recherche.offre.service;

import com.recherche.offre.client.AuthClient;
import com.recherche.offre.conf.OffreEmploiApiConfiguration;
import com.recherche.offre.dto.CredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FranceTravailAuthService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String REALM = "/partenaire";
    private static final long EXPIRATION_SAFETY_WINDOW_SECONDS = 60L;

    private final AuthClient authClient;
    private final OffreEmploiApiConfiguration offreEmploiApiConfiguration;

    private volatile CachedCredentials cachedCredentials;

    public CredentialsDto getCredentials() {
        final CachedCredentials localCache = cachedCredentials;
        if (isCacheValid(localCache)) {
            return localCache.credentials();
        }

        synchronized (this) {
            final CachedCredentials synchronizedCache = cachedCredentials;
            if (isCacheValid(synchronizedCache)) {
                return synchronizedCache.credentials();
            }

            final CredentialsDto refreshedCredentials = requestCredentials();
            cachedCredentials = new CachedCredentials(refreshedCredentials, computeExpirationInstant(refreshedCredentials));
            return refreshedCredentials;
        }
    }

    public String getBearerToken() {
        return BEARER_PREFIX + getCredentials().getAccessToken();
    }

    public synchronized void invalidateToken() {
        cachedCredentials = null;
    }

    private CredentialsDto requestCredentials() {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", offreEmploiApiConfiguration.getClientId());
        formData.add("client_secret", offreEmploiApiConfiguration.getClientSecret());
        formData.add("scope", offreEmploiApiConfiguration.getScope());
        return authClient.getCredentials(REALM, formData);
    }

    private boolean isCacheValid(final CachedCredentials cache) {
        return cache != null
                && cache.credentials() != null
                && cache.credentials().getAccessToken() != null
                && Instant.now().isBefore(cache.expiresAt());
    }

    private Instant computeExpirationInstant(final CredentialsDto credentials) {
        if (credentials.getExpiresIn() == null || credentials.getExpiresIn() <= 0) {
            return Instant.now();
        }

        final long effectiveDuration = Math.max(1L, credentials.getExpiresIn() - EXPIRATION_SAFETY_WINDOW_SECONDS);
        return Instant.now().plusSeconds(effectiveDuration);
    }

    private record CachedCredentials(CredentialsDto credentials, Instant expiresAt) {
    }
}

