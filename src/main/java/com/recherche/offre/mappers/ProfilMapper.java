package com.recherche.offre.mappers;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.dto.ProfilDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilMapper {

    @Mapping(target = "prenom", source = "utilisateur.prenom")
    @Mapping(target = "nom", source = "utilisateur.nom")
    @Mapping(target = "email", source = "utilisateur.email")
    @Mapping(target = "localisation", source = "utilisateur.localisation")
    @Mapping(target = "anneeExperience", source = "utilisateur.anneeExperience")
    @Mapping(target = "competences", source = "competences")
    ProfilDto toProfilDto(UserEntity utilisateur, List<SkillEntity> competences);

    @Mapping(target = "prenom", source = "profilDto.prenom")
    @Mapping(target = "nom", source = "profilDto.nom")
    @Mapping(target = "email", source = "profilDto.email")
    @Mapping(target = "localisation", source = "profilDto.localisation")
    @Mapping(target = "codePostal", source = "profilDto.codePostal")
    @Mapping(target = "anneeExperience", source = "profilDto.anneeExperience")
    @Mapping(target = "id", source = "id")
    UserEntity toUserEntity(ProfilDto profilDto, Long id);

}
