package com.recherche.offre.database.userskill;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.user.UserEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_SKILL")
@Getter
@Setter
@NoArgsConstructor
public class UserSkillEntity {

    @EmbeddedId
    private UserSkillId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @MapsId("skillId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SKILL_ID", nullable = false)
    private SkillEntity skill;
}

