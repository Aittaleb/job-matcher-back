package com.recherche.offre.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RechercheOffreDetailsDto extends RechercheOffreDto implements Serializable {

    private String description;
    private String typeContratLibelle;
    private String natureContrat;
    private String experienceLibelle;
    private String dureeTravail;

}
