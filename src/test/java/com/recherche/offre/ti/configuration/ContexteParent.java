package com.recherche.offre.ti.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.recherche.offre.utils.TestUtils.readFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ContexteParent.TestRestTemplateConfiguration.class)
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
    void resetWireMock() {
        WIRE_MOCK_SERVER.resetAll();
        configureFor("localhost", WIRE_MOCK_SERVER.port());
        stubFranceTravailAuthToken();
        stubFranceTravailCompetences();
        stubFranceTravailOffres();
    }

    @AfterAll
    static void stopWireMock() {
        WIRE_MOCK_SERVER.stop();
    }

    @Autowired
    protected Environment environment;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected String url(final String path) {
        return "http://localhost:" + environment.getRequiredProperty("local.server.port") + path;
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

    protected static void stubForGetWS(final String responsePathFile, final String url) {
        stubFor(get(urlEqualTo(url)).willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(readFile(responsePathFile))
                .withStatus(HttpStatus.OK.value())));
    }

    protected static void stubForGetWSWithQueryParams(final String responsePathFile, final String url, final Map<String, StringValuePattern> queryParams) {
        stubFor(get(urlEqualTo(url))
                .withQueryParams(queryParams)
                .willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(readFile(responsePathFile))
                .withStatus(HttpStatus.OK.value())));
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class TestRestTemplateConfiguration {

        @Bean
        TestRestTemplate testRestTemplate() {
            return new TestRestTemplate();
        }
    }

}
