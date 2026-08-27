package com.recherche.offre.service;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.skill.SkillRepository;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.database.userskill.UserSkillEntity;
import com.recherche.offre.database.userskill.UserSkillRepository;
import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.dto.SkillDto;
import com.recherche.offre.mappers.ProfilMapper;
import com.recherche.offre.mappers.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfilService {

    private final ProfilMapper profilMapper;
    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public ProfilDto getInformationsProfil(final Long userId) {
        final UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        final List<SkillEntity> competences = userSkillRepository.findSkillsByUserId(userId);
        return profilMapper.toProfilDto(userEntity, competences);
    }

    @Transactional
    public ProfilDto updateProfil(final Long userId, final ProfilDto profilDto) {
        final UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        userEntity.setPrenom(profilDto.getPrenom());
        userEntity.setNom(profilDto.getNom());
        userEntity.setEmail(profilDto.getEmail());
        userEntity.setLocalisation(profilDto.getLocalisation());
        userEntity.setCodePostal(profilDto.getCodePostal());
        userEntity.setAnneeExperience(profilDto.getAnneeExperience());
        userRepository.save(userEntity);

        synchroniserCompetences(userEntity, profilDto.getCompetences());

        return getInformationsProfil(userId);
    }

    private void synchroniserCompetences(final UserEntity userEntity, final List<SkillDto> competences) {
        if (CollectionUtils.isEmpty(competences)) {
            return;
        }

        final List<UserSkillEntity> competencesActuelles = userSkillRepository.findAllByIdUserId(userEntity.getId());
        final Set<Long> competencesActuellesIds = competencesActuelles.stream()
            .map(userSkillEntity -> userSkillEntity.getSkill().getId())
            .collect(Collectors.toSet());
        final Map<String, SkillDto> competencesSouhaiteesParNom = new LinkedHashMap<>();

        for (final SkillDto competence : competences) {
            if (competence == null || (competence.getId() == null && (competence.getCode() == null || competence.getCode().isBlank()))) {
                continue;
            }

            final String cle = competence.getId() != null
                ? "ID:" + competence.getId()
                : "NAME:" + competence.getCode().trim().toLowerCase();
            competencesSouhaiteesParNom.putIfAbsent(cle, competence);
        }

        final Set<Long> competencesSouhaitees = new HashSet<>();

        for (final SkillDto competence : competencesSouhaiteesParNom.values()) {
            final SkillEntity skillEntity = trouverOuCreerCompetence(competence);
            competencesSouhaitees.add(skillEntity.getId());

            if (competencesActuellesIds.add(skillEntity.getId())) {
                userSkillRepository.save(new UserSkillEntity()
                    .setUser(userEntity)
                    .setSkill(skillEntity));
            }
        }

        for (final UserSkillEntity userSkillEntity : competencesActuelles) {
            if (!competencesSouhaitees.contains(userSkillEntity.getSkill().getId())) {
                userSkillRepository.delete(userSkillEntity);
            }
        }
    }

    private SkillEntity trouverOuCreerCompetence(final SkillDto competence) {
        if (competence.getId() != null) {
            return skillRepository.findById(competence.getId())
                .orElseGet(() -> trouverOuCreerCompetenceParCode(competence));
        }

        return trouverOuCreerCompetenceParCode(competence);
    }

    private SkillEntity trouverOuCreerCompetenceParCode(final SkillDto competence) {
        final String codeCompetence = competence.getCode();
        if (codeCompetence == null || codeCompetence.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de compétence est obligatoire");
        }

        final String codeNormalise = codeCompetence.trim();
        return skillRepository.findByCode(codeNormalise)
            .orElseGet(() -> skillRepository.save(skillMapper.toSkillEntity(competence)));
    }

}
