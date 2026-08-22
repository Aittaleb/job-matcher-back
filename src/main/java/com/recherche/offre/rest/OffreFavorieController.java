package com.recherche.offre.rest;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public class OffreFavorieController {

    @GetMapping("/offres/favorites")
    public String getFavoriteOffers() {
        return "Liste des offres favorites";
    }

    @PostMapping("/offres/favorites")
    public String creerOffreFavorie() {
        return "Offre sauvegardée en tant que favorite";
    }

    @DeleteMapping("/offres/favorites/{id}")
    public String supprimerOffreFavorie(@PathVariable final Long id) {
        return "Offre avec l'ID " + id + " supprimée des favorites";
    }

}
