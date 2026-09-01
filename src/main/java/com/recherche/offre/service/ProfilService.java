package com.recherche.offre.service;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.skill.SkillRepository;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.database.userskill.UserSkillEntity;
import com.recherche.offre.database.userskill.UserSkillRepository;
import com.recherche.offre.dto.CompetenceRomeDto;
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
    private final RomeService romeService;

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

        // 1) Charge le referentiel une seule fois pour valider toutes les competences.
        final Set<String> codesReferentiel = chargerCodesReferentiel();
        final List<UserSkillEntity> competencesActuelles = userSkillRepository.findAllByIdUserId(userEntity.getId());
        final Set<Long> competencesActuellesIds = extraireIdsCompetences(competencesActuelles);
        final Map<String, SkillDto> competencesSouhaiteesUniques = dedupliquerCompetencesSouhaitees(competences);

        // 2) Ajoute les competences manquantes et construit l'ensemble final desire.
        final Set<Long> competencesSouhaiteesIds = ajouterCompetencesManquantes(
            userEntity,
            competencesSouhaiteesUniques,
            codesReferentiel,
            competencesActuellesIds
        );

        // 3) Supprime les associations qui ne sont plus demandees.
        supprimerCompetencesObsoletes(competencesActuelles, competencesSouhaiteesIds);
    }

    private Set<Long> extraireIdsCompetences(final List<UserSkillEntity> competencesActuelles) {
        return competencesActuelles.stream()
            .map(userSkillEntity -> userSkillEntity.getSkill().getId())
            .collect(Collectors.toSet());
    }

    private Map<String, SkillDto> dedupliquerCompetencesSouhaitees(final List<SkillDto> competences) {
        final Map<String, SkillDto> competencesSouhaiteesUniques = new LinkedHashMap<>();
        for (final SkillDto competence : competences) {
            if (estCompetenceIgnorable(competence)) {
                continue;
            }
            competencesSouhaiteesUniques.putIfAbsent(construireCleCompetence(competence), competence);
        }
        return competencesSouhaiteesUniques;
    }

    private Set<Long> ajouterCompetencesManquantes(
        final UserEntity userEntity,
        final Map<String, SkillDto> competencesSouhaiteesUniques,
        final Set<String> codesReferentiel,
        final Set<Long> competencesActuellesIds
    ) {
        final Set<Long> competencesSouhaiteesIds = new HashSet<>();
        for (final SkillDto competence : competencesSouhaiteesUniques.values()) {
            final SkillEntity skillEntity = trouverOuCreerCompetence(competence, codesReferentiel);
            competencesSouhaiteesIds.add(skillEntity.getId());

            if (competencesActuellesIds.add(skillEntity.getId())) {
                userSkillRepository.save(new UserSkillEntity()
                    .setUser(userEntity)
                    .setSkill(skillEntity));
            }
        }
        return competencesSouhaiteesIds;
    }

    private void supprimerCompetencesObsoletes(final List<UserSkillEntity> competencesActuelles, final Set<Long> competencesSouhaiteesIds) {
        for (final UserSkillEntity userSkillEntity : competencesActuelles) {
            if (!competencesSouhaiteesIds.contains(userSkillEntity.getSkill().getId())) {
                userSkillRepository.delete(userSkillEntity);
            }
        }
    }

    private boolean estCompetenceIgnorable(final SkillDto competence) {
        return competence == null || (competence.getId() == null && (competence.getCode() == null || competence.getCode().isBlank()));
    }

    private String construireCleCompetence(final SkillDto competence) {
        return competence.getId() != null
            ? "ID:" + competence.getId()
            : "CODE:" + normaliserCode(competence.getCode());
    }

    private SkillEntity trouverOuCreerCompetence(final SkillDto competence, final Set<String> codesReferentiel) {
        if (competence.getId() != null) {
            return skillRepository.findById(competence.getId())
                .map(skillEntity -> {
                    verifierCodeReferentiel(skillEntity.getCode(), codesReferentiel);
                    return skillEntity;
                })
                .orElseGet(() -> trouverOuCreerCompetenceParCode(competence, codesReferentiel));
        }

        return trouverOuCreerCompetenceParCode(competence, codesReferentiel);
    }

    private SkillEntity trouverOuCreerCompetenceParCode(final SkillDto competence, final Set<String> codesReferentiel) {
        final String codeCompetence = competence.getCode();
        final String codeNormalise = verifierCodeReferentiel(codeCompetence, codesReferentiel);
        return skillRepository.findByCode(codeNormalise)
            .orElseGet(() -> skillRepository.save(skillMapper.toSkillEntity(competence)));
    }

    private Set<String> chargerCodesReferentiel() {
        return romeService.chargerCachedRome().stream()
            .map(CompetenceRomeDto::getCode)
            .filter(code -> code != null && !code.isBlank())
            .map(this::normaliserCode)
            .collect(Collectors.toSet());
    }

    private String verifierCodeReferentiel(final String codeCompetence, final Set<String> codesReferentiel) {
        if (codeCompetence == null || codeCompetence.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de competence est obligatoire");
        }

        final String codeNormalise = normaliserCode(codeCompetence);
        if (!codesReferentiel.contains(codeNormalise)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La competence '" + codeNormalise + "' n'est pas reconnue dans le referentiel ROME");
        }

        return codeNormalise;
    }

    private String normaliserCode(final String codeCompetence) {
        return codeCompetence.trim().toLowerCase();
    }

}
