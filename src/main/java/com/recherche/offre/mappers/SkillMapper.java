package com.recherche.offre.mappers;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.dto.SkillDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillEntity toSkillEntity(SkillDto skillDto);

    @InheritInverseConfiguration
    SkillDto toSkillDto(SkillEntity skillEntity);
}
