package com.recherche.offre.rest;

import com.recherche.offre.dto.RapportCorrespondanceDto;
import com.recherche.offre.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

     @GetMapping("/profil/{profilId}/offre/{offreId}/matching")
     public RapportCorrespondanceDto getMatchingOffers(@PathVariable final Long profilId, @PathVariable final String offreId) {
         return matchingService.calculerRapportCorrespondanceParIdOffreEtIdUtilisateur(profilId, offreId);
     }
}
