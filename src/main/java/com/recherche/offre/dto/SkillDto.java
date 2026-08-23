package com.recherche.offre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SkillDto implements Serializable {

    private Long id;
    private String name;
}
