package com.recherche.offre.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "france-travail-client", url = "${offre-emploi.api.base-url}", path = "/partenaire", configuration = com.recherche.offre.conf.FranceTravailOffresFeignConfig.class)
public interface FranceTravailOffresEmploiClient {

    @GetMapping("/offresdemploi/v2/offres/search")
    String rechercherOffres();

}