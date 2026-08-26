package com.recherche.offre.rest;

import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OffreFavorieController {
    private final OffreService offreService;

    @GetMapping("/offres/favorites")
    public String getFavoriteOffers() {
        return "Liste des offres favorites";
    }

    @PostMapping(value = "/favorites/{offerId}/user/{userId}")
    public Long sauvegarderOffre(@PathVariable("offerId") final String offerId ,@PathVariable("userId") Long userId) {
        return offreService.sauvegarderOffre(offerId, userId);
    }

    @DeleteMapping(value = "/favorites/{id}/user/{userId}")
    public void supprimerOffre(@PathVariable("id") final Long idTechnique, @PathVariable("userId") final Long userId) {
        offreService.supprimerOffre(idTechnique, userId);
    }

}
