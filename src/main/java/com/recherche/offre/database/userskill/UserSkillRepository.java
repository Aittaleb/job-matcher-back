package com.recherche.offre.database.userskill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkillEntity, UserSkillId> {

    List<UserSkillEntity> findAllByUser_Id(Long userId);

    List<UserSkillEntity> findAllBySkill_Id(Long skillId);
}

