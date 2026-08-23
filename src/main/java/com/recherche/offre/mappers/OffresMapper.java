package com.recherche.offre.mappers;

import com.recherche.offre.dto.FranceTravailOffreDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.dto.ResultatRechercheApiFranceTravailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OffresMapper {

    @Mapping(target = "identifiantOffre", source = "franceTravailOffre.id")
    @Mapping(target = "intituleOffre", source = "franceTravailOffre.intitule")
    @Mapping(target = "lieuTravail", source = "franceTravailOffre.lieuTravail.libelle")
    @Mapping(target = "codePostal", source = "franceTravailOffre.lieuTravail.codePostal")
    @Mapping(target = "salaire", source = "franceTravailOffre.salaire.libelle")
    RechercheOffreDto toOffreDto(FranceTravailOffreDto franceTravailOffre);

    @Mapping(target = "identifiantOffre", source = "franceTravailOffre.id")
    @Mapping(target = "intituleOffre", source = "franceTravailOffre.intitule")
    @Mapping(target = "lieuTravail", source = "franceTravailOffre.lieuTravail.libelle")
    @Mapping(target = "codePostal", source = "franceTravailOffre.lieuTravail.codePostal")
    @Mapping(target = "salaire", source = "franceTravailOffre.salaire.libelle")
    @Mapping(target = "description", source = "franceTravailOffre.description")
    RechercheOffreDetailsDto toOffreDetailsDto(FranceTravailOffreDto franceTravailOffre);

    List<RechercheOffreDto> toOffreDtoList(List<FranceTravailOffreDto> franceTravailOffreDtos);

}
