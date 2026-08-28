package com.recherche.offre.rest;

import com.recherche.offre.dto.ApiErrorDto;
import com.recherche.offre.dto.RechercheOffreDetailsDto;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Offres", description = "Operations de recherche et detail d'offres")
public class OffreController {

    private final OffreService offreService;

    @GetMapping(value = "/offres")
    @Operation(summary = "Rechercher des offres", description = "Retourne les offres filtrées par mot-cle")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste d'offres",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RechercheOffreDto.class)))),
        @ApiResponse(responseCode = "400", description = "Parametre query invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public List<RechercheOffreDto> rechercherOffresParMotCle(@Parameter(description = "mot clé de la recherche", example = "conducteur") @RequestParam(value = "query") final String query) {
        return offreService.fetchOffersByKeyword(query);
    }

    @GetMapping(value = "/offres/{id}")
    @Operation(summary = "Consulter une offre", description = "Retourne les details d'une offre par identifiant France Travail")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Details de l'offre",
            content = @Content(schema = @Schema(implementation = RechercheOffreDetailsDto.class))),
        @ApiResponse(responseCode = "400", description = "Identifiant invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public RechercheOffreDetailsDto rechercherOffresParIdentifiant(@PathVariable("id") String id) {
        return offreService.fetchOfferDetails(id);
    }

}
