package com.recherche.offre.rest;

import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.service.ProfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
public class ProfilController {

    private final ProfilService profilService;

    @GetMapping("/{userId}")
    public ProfilDto getProfil(@PathVariable final Long userId) {
        return profilService.getInformationsProfil(userId);
    }

    @PutMapping("/{userId}")
    public String updateProfil(@PathVariable("userId") final Long userId, @RequestBody final ProfilDto profilDto) {
        return profilService.updateProfil(userId, profilDto);
    }
}
