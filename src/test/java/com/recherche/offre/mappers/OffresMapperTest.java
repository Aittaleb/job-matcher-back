package com.recherche.offre.mappers;

import com.recherche.offre.dto.FranceTravailOffreDto;
import com.recherche.offre.dto.LieuTravailDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.SalaireDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OffresMapperTest {

    private final OffresMapper offresMapper = Mappers.getMapper(OffresMapper.class);

    @Test
    void shouldMapCommonFieldsForOfferDto() {
        FranceTravailOffreDto franceTravailOffre = buildOffer();

        RechercheOffreDto offre = offresMapper.toOffreDto(franceTravailOffre);

        assertCommonFields(offre);
    }

    @Test
    void shouldReuseCommonMappingForOfferDetailsDto() {
        FranceTravailOffreDto franceTravailOffre = buildOffer();

        RechercheOffreDetailsDto offre = offresMapper.toOffreDetailsDto(franceTravailOffre);

        assertCommonFields(offre);
        assertFieldValue(offre, "description", "Description détaillée");
    }

    private void assertCommonFields(RechercheOffreDto offre) {
        assertFieldValue(offre, "identifiantFt", "FT-123");
        assertFieldValue(offre, "intituleOffre", "Développeur Java");
        assertFieldValue(offre, "lieuTravail", "Paris");
        assertFieldValue(offre, "codePostal", "75001");
        assertFieldValue(offre, "salaire", "45K€");
    }

    private void assertFieldValue(Object target, String fieldName, String expectedValue) {
        assertDoesNotThrow(() -> {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            assertEquals(expectedValue, field.get(target));
        });
    }

    private Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> currentType = type;
        while (currentType != null) {
            try {
                return currentType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                currentType = currentType.getSuperclass();
            }
        }

        throw new NoSuchFieldException(fieldName);
    }

    private FranceTravailOffreDto buildOffer() {
        LieuTravailDto lieuTravail = new LieuTravailDto();
        lieuTravail.setLibelle("Paris");
        lieuTravail.setCodePostal("75001");

        SalaireDto salaire = new SalaireDto();
        salaire.setLibelle("45K€");

        FranceTravailOffreDto franceTravailOffre = new FranceTravailOffreDto();
        franceTravailOffre.setId("FT-123");
        franceTravailOffre.setIntitule("Développeur Java");
        franceTravailOffre.setDescription("Description détaillée");
        franceTravailOffre.setLieuTravail(lieuTravail);
        franceTravailOffre.setSalaire(salaire);
        return franceTravailOffre;
    }
}

