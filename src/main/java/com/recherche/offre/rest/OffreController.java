package com.recherche.offre.rest;

import com.recherche.offre.dto.RechercheOffreDetailsDto;
import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OffreController {

    private final OffreService offreService;

    @GetMapping(value = "/offres")
    public List<RechercheOffreDto> rechercherOffres() {
        return offreService.fetchOffers();
    }

    @GetMapping(value = "/offres/{id}")
    public RechercheOffreDetailsDto rechercherOffresParIdentifiant(@PathVariable("id") String id) {
        return offreService.fetchOfferDetails(id);
    }

}
