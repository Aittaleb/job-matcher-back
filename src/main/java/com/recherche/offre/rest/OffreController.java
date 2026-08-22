package com.recherche.offre.rest;

import com.recherche.offre.conf.OffreEmploiApiConfiguration;
import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OffreController {

    private final OffreService offreService;

    @GetMapping(value = "/offres")
    public String rechercherParMotCle() {
        return offreService.fetchOffers();
    }

}
