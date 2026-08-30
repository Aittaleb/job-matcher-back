package com.recherche.offre.ti.rest;

import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.dto.SkillDto;
import com.recherche.offre.ti.configuration.ContexteParent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.recherche.offre.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProfilControllerItTest extends ContexteParent {

    @Autowired
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    void doitGetLeProfil() {
        final var response = testRestTemplate.getForEntity(
                url("/api/profil/" + 1),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/profil-reponse-expected.json"), response.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    @SneakyThrows
    void doitModifierLeProfil() {
        testRestTemplate.put(
                url("/api/profil/" + 1),
                new ProfilDto()
                        .setPrenom("NouveauPrenom")
                        .setNom("NouveauNom")
                        .setEmail("abdel@gmail.com")
                        .setLocalisation("NouveauLieu")
                        .setCodePostal("75000")
                        .setAnneeExperience(5)
                        .setCompetences(
                                List.of(
                                        new SkillDto()
                                                .setCode("519057")
                                                .setLibelle("NouvelleCompetence"),
                                        new SkillDto()
                                                .setCode("519073")
                                                .setLibelle("NouvelleCompetence2")
                                )
                        )
        );

        final var response = testRestTemplate.getForEntity(
                url("/api/profil/" + 1),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/profil-maj.json"), response.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    @SneakyThrows
    void doitModifierLeProfil_cas_erreur_validation_competence() {
        testRestTemplate.put(
                url("/api/profil/" + 1),
                new ProfilDto()
                        .setPrenom("NouveauPrenom")
                        .setNom("NouveauNom")
                        .setEmail("abdel@gmail.com")
                        .setLocalisation("NouveauLieu")
                        .setCodePostal("75000")
                        .setAnneeExperience(5)
                        .setCompetences(
                                List.of(
                                        new SkillDto()
                                                .setCode("competence_non_reconnue")
                                                .setLibelle("NouvelleCompetence")
                                )
                        )
        );
        // FIXME: On devrait vérifier le code de retour de la requête PUT pour s'assurer qu'elle échoue
        // On s'attend à ce que la mise à jour échoue en raison d'une compétence non reconnue
        final var response = testRestTemplate.getForEntity(
                url("/api/profil/" + 1),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JSONAssert.assertEquals(readFile("/fixtures/profil-inchange.json"), response.getBody(), JSONCompareMode.STRICT);
    }

}
