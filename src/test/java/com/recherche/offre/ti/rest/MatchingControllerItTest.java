package com.recherche.offre.ti.rest;

import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MatchingControllerItTest extends ContexteParent {

    @Test
    @SneakyThrows
    void testGetMatchingOffers() {
        final String offreId = "212TTQH";
        stubFranceTravailOffreDetails(offreId);

        final ResponseEntity<String> response = testRestTemplate.getForEntity(
                url("/api/profil/1/offre/"+ offreId + "/matching"),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/matching-expected.json"), response.getBody(), JSONCompareMode.STRICT);

        verify(1, postRequestedFor(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire")));
        verify(1, getRequestedFor(urlPathMatching("/partenaire/offresdemploi/v2/offres/.*"))
                .withHeader("Authorization", equalTo("Bearer fake-access-token")));
    }
}
