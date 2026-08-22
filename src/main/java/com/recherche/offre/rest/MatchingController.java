package com.recherche.offre.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public class MatchingController {

     @GetMapping("offers/{id}/matching")
        public String getMatchingOffers(@PathVariable final Long id) {
            return "Correspondance de l'offre avec l'ID " + id;
        }
}
