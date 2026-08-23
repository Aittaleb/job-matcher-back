package com.recherche.offre.rest;

import com.recherche.offre.dto.SkillDto;
import com.recherche.offre.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/profile/{userId}/skills")
    public SkillDto addSkill(@PathVariable final Long userId, @RequestBody SkillDto skill) {
        return skillService.ajouterSkill(userId, skill);
    }

    @DeleteMapping("/profile/{userId}/skills/{id}")
    public void deleteSkill(@PathVariable("userId") Long userId, @PathVariable("id") final Long skillId) {
        skillService.supprimerSkill(userId, skillId);
    }

}
