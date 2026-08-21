package com.recherche.offre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OffreDto implements Serializable {
    private String description;
}
