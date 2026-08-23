package com.recherche.offre.database.userskill;

import com.recherche.offre.database.skill.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkillEntity, UserSkillId> {

    @Query("select us.skill from UserSkillEntity us where us.user.id = :userId")
    List<SkillEntity> findSkillsByUserId(@Param("userId") Long userId);

    void deleteByUser_IdAndSkill_Id(Long userId, Long skillId);

    List<UserSkillEntity> findAllByIdUserId(Long userId);
}

