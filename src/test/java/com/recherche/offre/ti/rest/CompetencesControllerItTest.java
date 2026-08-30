package com.recherche.offre.ti.rest;

import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompetencesControllerItTest extends ContexteParent {

    private static final String ROME_COMPETENCES_PATH = "/partenaire/rome-competences/v1/competences/competence";

    @Test
    void contextLoads() {
        assertNotNull(testRestTemplate);
    }

    @Test
    @SneakyThrows
    void doitChargerLesCompetences() {
        final ResponseEntity<String> response = testRestTemplate.getForEntity(
                url("/api/rome/competences"),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/rome-competences-response.json"), response.getBody(), JSONCompareMode.STRICT);

        verify(1, postRequestedFor(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire")));
        verify(1, getRequestedFor(urlEqualTo(ROME_COMPETENCES_PATH))
                .withHeader("Authorization", equalTo("Bearer fake-access-token")));
    }
}
