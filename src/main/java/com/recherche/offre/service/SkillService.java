package com.recherche.offre.service;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.skill.SkillRepository;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.database.userskill.UserSkillEntity;
import com.recherche.offre.database.userskill.UserSkillRepository;
import com.recherche.offre.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;

    public SkillDto ajouterSkill(final Long userId, final SkillDto skill) {
        // Vérifier si la compétence existe déjà dans la base de données
        var existingSkill = skillRepository.findByNameIgnoreCase(skill.getName())
                .orElseGet(() -> skillRepository.save(new SkillEntity().setName(skill.getName())));

        // Ajouter la compétence à l'utilisateur
        userSkillRepository.save(new UserSkillEntity()
                .setUser(userRepository.getReferenceById(userId))
                .setSkill(existingSkill));

        return new SkillDto(existingSkill.getId(), existingSkill.getName());
    }


    public void supprimerSkill(final Long userId, final Long skillId) {
        skillRepository.findById(skillId).ifPresent(skill -> userSkillRepository.deleteByUser_IdAndSkill_Id(userId, skillId));
    }

}
