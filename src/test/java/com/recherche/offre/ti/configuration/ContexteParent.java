package com.recherche.offre.ti.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.recherche.offre.conf.AuthTokenCacheConfiguration;
import com.recherche.offre.conf.RomeCacheConfiguration;
import org.flywaydb.core.Flyway;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.recherche.offre.utils.TestUtils.readFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContexteParent {

    protected static final String AUTH_TOKEN_PATH = "/connexion/oauth2/access_token";
    private static final String AUTH_TOKEN_RESPONSE_PATH = "/fixtures/auth-token-response.json";
    protected static final String FT_OFFRES_EMPLOI_PATH = "/partenaire/offresdemploi/v2/offres/search";
    protected static final String ROME_COMPETENCES_PATH = "/partenaire/rome-competences/v1/competences/competence";
    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(wireMockConfig().dynamicPort());

    @DynamicPropertySource
    static void registerProperties(final DynamicPropertyRegistry registry) {
        if (!WIRE_MOCK_SERVER.isRunning()) {
            WIRE_MOCK_SERVER.start();
        }
        registry.add("offre-emploi.api.base-url", WIRE_MOCK_SERVER::baseUrl);
        registry.add("api.france-travail.auth.base-url", WIRE_MOCK_SERVER::baseUrl);
    }

    @BeforeAll
    static void startWireMock() {
        if (!WIRE_MOCK_SERVER.isRunning()) {
            WIRE_MOCK_SERVER.start();
        }
        configureFor("localhost", WIRE_MOCK_SERVER.port());
    }

    @BeforeEach
    void resetTestState() {
        if (!WIRE_MOCK_SERVER.isRunning()) {
            WIRE_MOCK_SERVER.start();
        }
        resetDatabase();
        clearCaches();
        WIRE_MOCK_SERVER.resetAll();
        configureFor("localhost", WIRE_MOCK_SERVER.port());
        stubFranceTravailAuthToken();
        stubFranceTravailCompetences();
        stubFranceTravailOffres();
    }

    @Autowired
    protected Environment environment;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("testFlyway")
    private Flyway flyway;

    @Autowired
    @Qualifier(AuthTokenCacheConfiguration.AUTH_TOKEN_CACHE_MANAGER)
    private CacheManager authTokenCacheManager;

    @Autowired
    @Qualifier(RomeCacheConfiguration.ROME_CACHE_MANAGER)
    private CacheManager romeCacheManager;

    protected String url(final String path) {
        return "http://localhost:" + environment.getRequiredProperty("local.server.port") + path;
    }

    private void resetDatabase() {
        jdbcTemplate.execute("DROP ALL OBJECTS");
        flyway.migrate();
    }

    private void clearCaches() {
        clearCacheManager(authTokenCacheManager);
        clearCacheManager(romeCacheManager);
    }

    private void clearCacheManager(final CacheManager cacheManager) {
        cacheManager.getCacheNames().forEach(cacheName -> {
            final Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
    }

    protected static void stubFranceTravailAuthToken() {
        stubFor(post(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(readFile(AUTH_TOKEN_RESPONSE_PATH))
                        .withStatus(HttpStatus.OK.value())));
    }

    protected static void stubFranceTravailCompetences() {
        stubFor(get(urlEqualTo(ROME_COMPETENCES_PATH))
                .withHeader("Authorization", equalTo("Bearer fake-access-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(readFile("/fixtures/rome-competences-response.json"))));
    }

    protected static void stubFranceTravailOffres() {
        stubFor(get(urlEqualTo(FT_OFFRES_EMPLOI_PATH))
                .withHeader("Authorization", equalTo("Bearer fake-access-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(readFile("/fixtures/france-travail-offres.json"))));
    }

    protected static void stubFranceTravailOffreDetails(final String identifiantOffre) {
        stubFor(get(urlEqualTo("/partenaire/offresdemploi/v2/offres/" + identifiantOffre))
                .withHeader("Authorization", equalTo("Bearer fake-access-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(readFile("/fixtures/offre-details-response-"+identifiantOffre+".json"))));
    }

}
