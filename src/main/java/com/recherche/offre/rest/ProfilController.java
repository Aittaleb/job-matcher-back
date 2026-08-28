package com.recherche.offre.rest;

import com.recherche.offre.dto.ApiErrorDto;
import com.recherche.offre.dto.ProfilDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.recherche.offre.service.ProfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
@Tag(name = "Profil", description = "Operations de consultation et mise a jour du profil utilisateur")
public class ProfilController {

    private final ProfilService profilService;

    @GetMapping("/{userId}")
    @Operation(summary = "Recuperer un profil", description = "Retourne les informations de profil pour un utilisateur donne")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profil retourne",
            content = @Content(schema = @Schema(implementation = ProfilDto.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public ProfilDto getProfil(@PathVariable final Long userId) {
        return profilService.getInformationsProfil(userId);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Mettre a jour un profil", description = "Met a jour les informations utilisateur et ses competences")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profil mis a jour",
            content = @Content(schema = @Schema(implementation = ProfilDto.class))),
        @ApiResponse(responseCode = "400", description = "Donnees invalides",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class))),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public ProfilDto updateProfil(@PathVariable("userId") final Long userId, @RequestBody @Validated final ProfilDto profilDto) {
        return profilService.updateProfil(userId, profilDto);
    }
}
