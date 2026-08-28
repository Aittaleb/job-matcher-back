package com.recherche.offre.mappers;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.dto.ProfilDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProfilMapperTest {

    private final ProfilMapper profilMapper = Mappers.getMapper(ProfilMapper.class);

    @Test
    void toProfilDto_mappeLesChampsUtilisateurEtCompetences() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        user.setPrenom("Aya");
        user.setNom("Dupont");
        user.setEmail("aya@exemple.fr");
        user.setLocalisation("Lyon");
        user.setAnneeExperience(5);

        final SkillEntity skill = new SkillEntity().setId(11L).setCode("java").setLibelle("Java");

        final ProfilDto actual = profilMapper.toProfilDto(user, List.of(skill));

        assertNotNull(actual);
        assertEquals("Aya", actual.getPrenom());
        assertEquals("Dupont", actual.getNom());
        assertEquals("aya@exemple.fr", actual.getEmail());
        assertEquals("Lyon", actual.getLocalisation());
        assertEquals(5, actual.getAnneeExperience());
        assertNotNull(actual.getCompetences());
        assertEquals(1, actual.getCompetences().size());
        assertEquals("java", actual.getCompetences().get(0).getCode());
    }

    @Test
    void toUserEntity_mappeLesChampsProfilEtId() {
        final ProfilDto profil = new ProfilDto()
                .setPrenom("Aya")
                .setNom("Dupont")
                .setEmail("aya@exemple.fr")
                .setLocalisation("Lyon")
                .setCodePostal("69000")
                .setAnneeExperience(5);

        final UserEntity actual = profilMapper.toUserEntity(profil, 3L);

        assertNotNull(actual);
        assertEquals(3L, actual.getId());
        assertEquals("Aya", actual.getPrenom());
        assertEquals("Dupont", actual.getNom());
        assertEquals("aya@exemple.fr", actual.getEmail());
        assertEquals("Lyon", actual.getLocalisation());
        assertEquals("69000", actual.getCodePostal());
        assertEquals(5, actual.getAnneeExperience());
    }
}

