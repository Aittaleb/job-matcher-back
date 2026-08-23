package com.recherche.offre.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LieuTravailDto implements Serializable {
    private String codePostal;
    private String commune;
    private String libelle;
}
