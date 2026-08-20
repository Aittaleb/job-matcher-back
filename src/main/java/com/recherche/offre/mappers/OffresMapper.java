package com.recherche.offre.mappers;

import com.recherche.offre.db.offres.OffresEntity;
import com.recherche.offre.dto.OffreDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OffresMapper {

    OffreDto toOffreDto(OffresEntity offresEntity);
    List<OffreDto> toOffreDtos(List<OffresEntity> offreEntities);
}
