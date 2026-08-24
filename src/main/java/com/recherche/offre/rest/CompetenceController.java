package com.recherche.offre.rest;

import com.recherche.offre.dto.CompetenceRomeDto;
import com.recherche.offre.service.RomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CompetenceController {

    private final RomeService romeService;

    @GetMapping(value = "/rome/competences")
    public List<CompetenceRomeDto> chargerRefRome() {
        return romeService.chargerCachedRome();
    }

}
