package com.recherche.offre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ProfilDto {

    private Long id;

    @NotBlank(message = "Le prenom est obligatoire")
    @Size(max = 100, message = "Le prenom ne doit pas depasser 100 caracteres")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas depasser 100 caracteres")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Size(max = 255, message = "L'email ne doit pas depasser 255 caracteres")
    private String email;

    @NotBlank(message = "La localisation est obligatoire")
    @Size(max = 150, message = "La localisation ne doit pas depasser 150 caracteres")
    private String localisation;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    private String codePostal;

    @NotNull(message = "L'annee d'experience est obligatoire")
    @Min(value = 0, message = "L'annee d'experience doit etre positive")
    @Max(value = 60, message = "L'annee d'experience est invalide")
    private Integer anneeExperience;

    private List<SkillDto> competences;

}
