package com.recherche.offre.ti.rest;

import com.recherche.offre.database.offre.SavedOfferRepository;
import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.*;

public class OffreFavorieItTest extends ContexteParent {

    @Autowired
    private SavedOfferRepository offreRepository;

    @Test
    @SneakyThrows
    void doitChargerLesOffresFavoris() {
        stubFranceTravailOffreDetails("212TTQH");
        final var response = testRestTemplate.getForEntity(
                url("/api/offres/favorites/user/" + 1),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/reponse-offre-favorite.json"), response.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    @SneakyThrows
    void doitSauvegarderOffre() {
        stubFranceTravailOffreDetails("212TTQH");
        stubFranceTravailOffreDetails("213CMRK");
        testRestTemplate.postForEntity(
                url("/api/offres/favorites/213CMRK/user/1"), "", String.class
        );

        final var response = testRestTemplate.getForEntity(
                url("/api/offres/favorites/user/" + 1),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/reponse-offres-favorites-apres-maj.json"), response.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    @SneakyThrows
    void doitSupprimerOffre() {
        testRestTemplate.delete(
                url("/api/offres/favorites/1/user/1"),
                String.class
        );

        final var actual = offreRepository.findByUser_Id(1L);
        assertTrue(CollectionUtils.isEmpty(actual));
    }
}
