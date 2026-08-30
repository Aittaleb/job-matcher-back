package com.recherche.offre.ti.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OffreEmploiApiTestConfiguration {

    @Value("${offre-emploi.api.base-url}")
    private String baseUrl;

    @Value("${api.offredemploi.client_id}")
    private String clientId;

    @Value("${api.offredemploi.client_secret}")
    private String clientSecret;

    @Value("${offre-emploi.api.scope}")
    private String scope;

    @Value("${offre-emploi.api.auth-cache-ttl-seconds:240}")
    private long authCacheTtlSeconds;

    @Value("${offre-emploi.api.rome-cache-ttl-hours:24}")
    private long romeCacheTtlHours;

}
