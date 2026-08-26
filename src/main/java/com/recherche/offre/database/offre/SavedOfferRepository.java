package com.recherche.offre.database.offre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedOfferRepository extends JpaRepository<SavedOfferEntity, Long> {

    void deleteByIdAndUser_Id(Long idTechnique, Long userId);

    List<SavedOfferEntity> findByUser_Id(Long userId);
}

