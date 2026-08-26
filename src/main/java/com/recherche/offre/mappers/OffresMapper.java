package com.recherche.offre.mappers;

import com.recherche.offre.dto.FranceTravailOffreDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OffresMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifiantFt", source = "franceTravailOffre.id")
    @Mapping(target = "intituleOffre", source = "franceTravailOffre.intitule")
    @Mapping(target = "lieuTravail", source = "franceTravailOffre.lieuTravail.libelle")
    @Mapping(target = "codePostal", source = "franceTravailOffre.lieuTravail.codePostal")
    @Mapping(target = "salaire", source = "franceTravailOffre.salaire.libelle")
    @Mapping(target = "competences", source = "franceTravailOffre.competences")
    RechercheOffreDto toOffreDto(FranceTravailOffreDto franceTravailOffre);

    @InheritConfiguration(name = "toOffreDto")
    @Mapping(target = "description", source = "franceTravailOffre.description")
    @Mapping(target = "typeContratLibelle", source = "franceTravailOffre.typeContrat")
    @Mapping(target = "dureeTravail", source = "franceTravailOffre.dureeTravailLibelle")
    RechercheOffreDetailsDto toOffreDetailsDto(FranceTravailOffreDto franceTravailOffre);

    List<RechercheOffreDto> toOffreDtoList(List<FranceTravailOffreDto> franceTravailOffreDtos);

}
