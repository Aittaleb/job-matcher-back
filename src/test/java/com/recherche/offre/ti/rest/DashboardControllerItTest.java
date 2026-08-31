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

public class DashboardControllerItTest extends ContexteParent {


    @Test
    @SneakyThrows
    void doitChargerLeDashboard() {
        stubFranceTravailOffreDetails("212TTQH");
        final ResponseEntity<String> response = testRestTemplate.getForEntity(
                url("/api/dashboard/user/1"),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/dashboard-expected.json"), response.getBody(), JSONCompareMode.STRICT);

        verify(1, postRequestedFor(urlPathEqualTo(AUTH_TOKEN_PATH))
                .withQueryParam("realm", equalTo("/partenaire")));
        verify(1, getRequestedFor(urlPathEqualTo(FT_OFFRES_EMPLOI_PATH))
                .withHeader("Authorization", equalTo("Bearer fake-access-token")));
    }

}
