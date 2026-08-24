package com.recherche.offre.service;

import com.recherche.offre.database.skill.SkillRepository;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.database.userskill.UserSkillEntity;
import com.recherche.offre.database.userskill.UserSkillRepository;
import com.recherche.offre.dto.SkillDto;
import com.recherche.offre.mappers.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillMapper skillMapper;

    public SkillDto ajouterSkill(final Long userId, final SkillDto skill) {
        // Vérifier si la compétence existe déjà dans la base de données
        var existingSkill = skillRepository.findByCode(skill.getCode())
                .orElseGet(() -> skillRepository.save(skillMapper.toSkillEntity(skill)));

        // Ajouter la compétence à l'utilisateur
        userSkillRepository.save(new UserSkillEntity()
                .setUser(userRepository.getReferenceById(userId))
                .setSkill(existingSkill));

        return skillMapper.toSkillDto(existingSkill);
    }


    public void supprimerSkill(final Long userId, final Long skillId) {
        skillRepository.findById(skillId).ifPresent(skill -> userSkillRepository.deleteByUser_IdAndSkill_Id(userId, skillId));
    }

}
