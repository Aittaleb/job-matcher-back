package com.recherche.offre.service;

import com.recherche.offre.database.skill.SkillEntity;
import com.recherche.offre.database.skill.SkillRepository;
import com.recherche.offre.database.user.UserEntity;
import com.recherche.offre.database.user.UserRepository;
import com.recherche.offre.database.userskill.UserSkillRepository;
import com.recherche.offre.dto.CompetenceRomeDto;
import com.recherche.offre.dto.ProfilDto;
import com.recherche.offre.dto.SkillDto;
import com.recherche.offre.mappers.ProfilMapper;
import com.recherche.offre.mappers.SkillMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilServiceTest {

    @Mock
    private ProfilMapper profilMapper;

    @Mock
    private UserSkillRepository userSkillRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private RomeService romeService;

    @InjectMocks
    private ProfilService profilService;

    @Test
    void getInformationsProfil_retourneLeProfilMappe() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        final SkillEntity skill = new SkillEntity().setId(10L).setCode("java");
        final ProfilDto expected = new ProfilDto().setPrenom("Aya");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSkillRepository.findSkillsByUserId(1L)).thenReturn(List.of(skill));
        when(profilMapper.toProfilDto(user, List.of(skill))).thenReturn(expected);

        final var actual = profilService.getInformationsProfil(1L);

        verify(userRepository).findById(1L);
        verify(userSkillRepository).findSkillsByUserId(1L);
        verify(profilMapper).toProfilDto(user, List.of(skill));
        verifyNoMoreInteractions(userRepository, userSkillRepository, profilMapper, skillRepository, skillMapper, romeService);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getInformationsProfil_throwSiUtilisateurIntrouvable() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        final ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profilService.getInformationsProfil(1L));

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, userSkillRepository, profilMapper, skillRepository, skillMapper, romeService);

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateProfil_metAJourInfosEtRechargeLeProfil() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        final ProfilDto input = new ProfilDto()
                .setPrenom("Aya")
                .setNom("Dupont")
                .setEmail("aya@exemple.fr")
                .setLocalisation("Lyon")
                .setCodePostal("69000")
                .setAnneeExperience(4)
                .setCompetences(Collections.emptyList());
        final ProfilDto expected = new ProfilDto().setPrenom("Aya").setNom("Dupont");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userSkillRepository.findSkillsByUserId(1L)).thenReturn(Collections.emptyList());
        when(profilMapper.toProfilDto(any(UserEntity.class), any())).thenReturn(expected);

        final var actual = profilService.updateProfil(1L, input);

        verify(userRepository, times(2)).findById(1L);
        verify(userRepository).save(user);
        verify(userSkillRepository).findSkillsByUserId(1L);
        verify(profilMapper).toProfilDto(any(UserEntity.class), any());
        verifyNoMoreInteractions(userRepository, userSkillRepository, profilMapper, skillRepository, skillMapper, romeService);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals("Aya", user.getPrenom());
        assertEquals("Dupont", user.getNom());
        assertEquals("aya@exemple.fr", user.getEmail());
        assertEquals("Lyon", user.getLocalisation());
        assertEquals("69000", user.getCodePostal());
        assertEquals(4, user.getAnneeExperience());
    }

    @Test
    void updateProfil_throwSiCodeCompetenceInvalide() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        final ProfilDto input = new ProfilDto().setCompetences(List.of(new SkillDto().setId(999L).setCode("   ")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSkillRepository.findAllByIdUserId(1L)).thenReturn(Collections.emptyList());
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());
        when(romeService.chargerCachedRome()).thenReturn(List.of(buildRomeCompetence("java")));

        final ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profilService.updateProfil(1L, input));

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(romeService).chargerCachedRome();
        verify(userSkillRepository).findAllByIdUserId(1L);
        verify(skillRepository).findById(999L);
        verifyNoMoreInteractions(userRepository, userSkillRepository, profilMapper, skillRepository, skillMapper, romeService);

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void updateProfil_throwSiCompetenceHorsReferentielRome() {
        final UserEntity user = new UserEntity();
        user.setId(1L);
        final ProfilDto input = new ProfilDto().setCompetences(List.of(new SkillDto().setCode("unknown-skill")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSkillRepository.findAllByIdUserId(1L)).thenReturn(Collections.emptyList());
        when(romeService.chargerCachedRome()).thenReturn(List.of(buildRomeCompetence("java"), buildRomeCompetence("spring")));

        final ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profilService.updateProfil(1L, input));

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(romeService).chargerCachedRome();
        verify(userSkillRepository).findAllByIdUserId(1L);
        verifyNoMoreInteractions(userRepository, userSkillRepository, profilMapper, skillRepository, skillMapper, romeService);

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("La competence 'unknown-skill' n'est pas reconnue dans le referentiel ROME", ex.getReason());
    }

    private CompetenceRomeDto buildRomeCompetence(final String code) {
        final CompetenceRomeDto competenceRomeDto = new CompetenceRomeDto();
        competenceRomeDto.setCode(code);
        return competenceRomeDto;
    }
}


