package com.recherche.offre.client;

import com.recherche.offre.dto.CompetenceRomeDto;
import com.recherche.offre.dto.FranceTravailOffreDto;
import com.recherche.offre.dto.ResultatRechercheApiFranceTravailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "france-travail-client", url = "${offre-emploi.api.base-url}", path = "/partenaire", configuration = com.recherche.offre.conf.FranceTravailOffresFeignConfig.class)
public interface FranceTravailOffresEmploiClient {

    @GetMapping("/offresdemploi/v2/offres/search")
    ResultatRechercheApiFranceTravailDto rechercherOffres();

    @GetMapping("/offresdemploi/v2/offres/{id}")
    FranceTravailOffreDto rechercherOffreParId(@PathVariable("id") String id);

    @GetMapping("/rome-competences/v1/competences/competence")
    List<CompetenceRomeDto> chargerReferentielCompetences();

}