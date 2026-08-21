package com.recherche.offre.database.userskill;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillId implements Serializable {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "SKILL_ID")
    private Long skillId;
}

