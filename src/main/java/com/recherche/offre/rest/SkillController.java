package com.recherche.offre.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SkillController {

    @GetMapping("/skills")
    public String getSkills() {
        return "liste des compétences";
    }

    @PostMapping("/profile/skills")
    public String addSkill() {
        return "compétence ajoutée";
    }

    @DeleteMapping("/profile/skills/{id}")
    public String deleteSkill(@PathVariable Long id) {
        return "compétence avec l'ID " + id + " supprimée";
    }

}
