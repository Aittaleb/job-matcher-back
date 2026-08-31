package com.recherche.offre.ti.rest;

import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreControllerItTest extends ContexteParent {

    @Test
    @SneakyThrows
    void doitChargerLesOffresParMotCle() {
        final String motCle = "serveur";

        final var response = testRestTemplate.getForEntity(
                url("/api/offres?query="+ motCle),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/offres-expected.json"), response.getBody(), JSONCompareMode.STRICT);

        verify(1, postRequestedFor(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire")));
        verify(1, getRequestedFor(urlPathEqualTo(FT_OFFRES_EMPLOI_PATH))
                .withHeader("Authorization", equalTo("Bearer fake-access-token")));
    }

    @Test
    @SneakyThrows
    void doitChargerLeDetailDeLOffreParSonIdentifiant() {
        final String idOffre = "212TTQH";
        stubFranceTravailOffreDetails(idOffre);

        final var response = testRestTemplate.getForEntity(
                url("/api/offres/"+ idOffre),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/offres-details-expected.json"), response.getBody(), JSONCompareMode.STRICT);

        verify(1, postRequestedFor(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire")));
        verify(1, getRequestedFor(urlPathMatching("/partenaire/offresdemploi/v2/offres/.*"))
                .withHeader("Authorization", equalTo("Bearer fake-access-token")));
    }

}
