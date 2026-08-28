package com.recherche.offre.mappers;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.dto.SkillDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SkillMapperTest {

    private final SkillMapper skillMapper = Mappers.getMapper(SkillMapper.class);

    @Test
    void toSkillEntity_mappeLesChamps() {
        final SkillDto skillDto = new SkillDto().setId(3L).setCode("java").setLibelle("Java");

        final SkillEntity actual = skillMapper.toSkillEntity(skillDto);

        assertNotNull(actual);
        assertEquals(3L, actual.getId());
        assertEquals("java", actual.getCode());
        assertEquals("Java", actual.getLibelle());
    }

    @Test
    void toSkillDto_mappeLesChamps() {
        final SkillEntity skillEntity = new SkillEntity().setId(7L).setCode("spring").setLibelle("Spring");

        final SkillDto actual = skillMapper.toSkillDto(skillEntity);

        assertNotNull(actual);
        assertEquals(7L, actual.getId());
        assertEquals("spring", actual.getCode());
        assertEquals("Spring", actual.getLibelle());
    }
}

