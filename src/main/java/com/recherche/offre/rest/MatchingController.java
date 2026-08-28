package com.recherche.offre.rest;

import com.recherche.offre.dto.ApiErrorDto;
import com.recherche.offre.dto.RapportCorrespondanceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.recherche.offre.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Operations de calcul de correspondance profil/offre")
public class MatchingController {

    private final MatchingService matchingService;

     @GetMapping("/profil/{profilId}/offre/{offreId}/matching")
      @Operation(summary = "Calculer le matching", description = "Calcule le score de correspondance pour un profil et une offre")
      @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Rapport de correspondance",
              content = @Content(schema = @Schema(implementation = RapportCorrespondanceDto.class))),
          @ApiResponse(responseCode = "400", description = "Parametres invalides",
              content = @Content(schema = @Schema(implementation = ApiErrorDto.class))),
          @ApiResponse(responseCode = "404", description = "Profil ou offre introuvable",
              content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
      })
     public RapportCorrespondanceDto getMatchingOffers(@Parameter(description = "identifiant technique de l'utilisateur", example = "1") @PathVariable final Long profilId, @Parameter(description = "identifiant francetravail de l'offre", example = "213BNDT") @PathVariable final String offreId) {
         return matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(profilId, offreId);
     }
}
