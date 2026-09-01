package com.recherche.offre.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
public class DashboardOffreDto extends RechercheOffreDto implements Serializable {

    private Integer scoreMatching;

}

