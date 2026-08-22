package com.recherche.offre.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profil")
public class ProfilController {

    @GetMapping("/")
    public String getProfil() {
        return "Profil information";
    }

    @PutMapping("/")
    public String updateProfil() {
        return "Profil updated";
    }
}
