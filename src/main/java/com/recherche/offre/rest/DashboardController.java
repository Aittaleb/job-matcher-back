package com.recherche.offre.rest;

import com.recherche.offre.dto.ApiErrorDto;
import com.recherche.offre.dto.DashboardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.recherche.offre.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@RestController
@Tag(name = "Dashboard", description = "Indicateurs de matching et competences a developper")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Recuperer le dashboard", description = "Retourne les indicateurs du dashboard pour un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard retourne",
            content = @Content(schema = @Schema(implementation = DashboardDto.class))),
        @ApiResponse(responseCode = "400", description = "Identifiant utilisateur invalide",
            content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    public DashboardDto getDashboard(@Parameter(name = "userId", description = "identifiant technique de l'utilisateur", example = "1", required = true) @PathVariable final Long userId) {
        return dashboardService.getDashboard(userId);
    }

}
