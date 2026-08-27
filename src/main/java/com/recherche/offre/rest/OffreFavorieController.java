package com.recherche.offre.rest;

import com.recherche.offre.dto.RechercheOffreDto;
import com.recherche.offre.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OffreFavorieController {
    private final OffreService offreService;

    @GetMapping("/offres/favorites/user/{userId}")
    public List<RechercheOffreDto> getFavoriteOffers(@PathVariable final Long userId) {
        return offreService.fetchFavoriteOffers(userId);
    }

    @PostMapping(value = "/offres/favorites/{offerId}/user/{userId}")
    public Long sauvegarderOffre(@PathVariable("offerId") final String offerId ,@PathVariable("userId") Long userId) {
        return offreService.sauvegarderOffre(offerId, userId);
    }

    @DeleteMapping(value = "/offres/favorites/{id}/user/{userId}")
    public void supprimerOffre(@PathVariable("id") final Long idTechnique, @PathVariable("userId") final Long userId) {
        offreService.supprimerOffre(idTechnique, userId);
    }

}
