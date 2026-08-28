package com.recherche.offre.rest;

import com.recherche.offre.dto.CompetenceRomeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.recherche.offre.service.RomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Referentiel competences", description = "Acces au referentiel des competences ROME")
public class CompetenceController {

    private final RomeService romeService;

    @GetMapping(value = "/rome/competences")
    @Operation(summary = "Charger le referentiel ROME", description = "Retourne la liste des competences ROME en cache")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Referentiel retourne",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompetenceRomeDto.class))))
    })
    public List<CompetenceRomeDto> chargerRefRome() {
        return romeService.chargerCachedRome();
    }

}
