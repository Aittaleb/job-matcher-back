package com.recherche.offre.rest;

import com.recherche.offre.dto.ApiErrorDto;
import com.recherche.offre.dto.RechercheOffreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Offres favorites", description = "Gestion des offres favorites par utilisateur")
public class OffreFavorieController {
    private final OffreService offreService;

    @GetMapping("/offres/favorites/user/{userId}")
    @Operation(summary = "Lister les offres favorites d'un utilisateur identifié par son identifiant technique", description = "Retourne les offres favorites d'un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des favoris",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RechercheOffreDto.class)))),
        @ApiResponse(responseCode = "400", description = "Identifiant utilisateur invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public List<RechercheOffreDto> getFavoriteOffers(@PathVariable final Long userId) {
        return offreService.fetchFavoriteOffers(userId);
    }

    @PostMapping(value = "/offres/favorites/{offerId}/user/{userId}")
    @Operation(summary = "Ajouter une offre favorite à un utilisateur", description = "Sauvegarde une offre dans les favoris utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Identifiant technique du favori",
            content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "400", description = "Requete invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public Long sauvegarderOffre(@Parameter(description = "identifiant francetravail de l'offre", example = "213BNDT") @PathVariable("offerId") final String offerId , @PathVariable("userId") Long userId) {
        return offreService.sauvegarderOffre(offerId, userId);
    }

    @DeleteMapping(value = "/offres/favorites/{id}/user/{userId}")
    @Operation(summary = "Supprimer une offre favorite", description = "Supprime une offre des favoris utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favori supprime"),
        @ApiResponse(responseCode = "400", description = "Requete invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public void supprimerOffre(@Parameter(description = "identifiant technique de l'offre", example = "1") @PathVariable("id") final Long idTechnique, @PathVariable("userId") final Long userId) {
        offreService.supprimerOffre(idTechnique, userId);
    }

}
