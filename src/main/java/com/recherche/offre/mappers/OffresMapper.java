package com.recherche.offre.mappers;

import com.recherche.offre.database.offre.OffreSauvegardeeEntity;
import com.recherche.offre.dto.FranceTravailOffreDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OffresMapper {

    FranceTravailOffreDto toOffreDto(OffreSauvegardeeEntity offresEntity);
    List<FranceTravailOffreDto> toOffreDtos(List<OffreSauvegardeeEntity> offreEntities);
}
