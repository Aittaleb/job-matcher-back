package com.recherche.offre.service;

import com.recherche.offre.database.offre.OffresRepository;
import com.recherche.offre.dto.OffreDto;
import com.recherche.offre.mappers.OffresMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffresRepository repository;
    private final OffresMapper mapper;

    public List<OffreDto> trouverListOffreParQuery(final String query) {
        return mapper.toOffreDtos(repository.findAllByDescriptionContains(query));
    }
}
