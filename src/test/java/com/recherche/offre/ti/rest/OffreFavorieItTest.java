package com.recherche.offre.ti.rest;

import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;

import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreFavorieItTest extends ContexteParent {


    @Test
    @SneakyThrows
    void doitChargerLesOffresFavoris() {
        stubFranceTravailOffreDetails("213BNDT");
        final var response = testRestTemplate.getForEntity(
                url("/api/offres/favorites/user/" + 1),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/reponse-offre-favorite.json"), response.getBody(), JSONCompareMode.STRICT);
    }
}
