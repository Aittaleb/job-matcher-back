package com.recherche.offre.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfilDtoValidationTest {

    @Test
    void shouldFailWhenMandatoryFieldsAreInvalid() {
        final Validator validator;
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        final ProfilDto profilDto = new ProfilDto()
                .setPrenom(" ")
                .setNom("")
                .setEmail("not-an-email")
                .setLocalisation(" ")
                .setCodePostal("75")
                .setAnneeExperience(-1);

        final var violations = validator.validate(profilDto);

        assertTrue(violations.size() >= 6);
    }

    @Test
    void shouldPassWhenDtoIsValid() {
        final Validator validator;
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        final ProfilDto profilDto = new ProfilDto()
                .setPrenom("Aya")
                .setNom("Dupont")
                .setEmail("aya@exemple.fr")
                .setLocalisation("Lyon")
                .setCodePostal("69000")
                .setAnneeExperience(5);

        final var violations = validator.validate(profilDto);

        assertEquals(0, violations.size());
    }
}

