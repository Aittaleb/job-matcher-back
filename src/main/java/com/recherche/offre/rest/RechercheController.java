package com.recherche.offre.rest;

import com.recherche.offre.dto.OffreDto;
import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RechercheController {

    private final OffreService offreService;
    @GetMapping(value = "/search")
    public List<OffreDto> rechercherParMotCle(@RequestParam("query") final String query) {
        return offreService.trouverListOffreParQuery(query);
    }

}
