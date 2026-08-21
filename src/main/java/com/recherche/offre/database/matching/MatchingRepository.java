package com.recherche.offre.database.matching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<MatchingEntity, Long> {

    List<MatchingEntity> findAllByUser_IdOrderByScoreDesc(Long userId);

    List<MatchingEntity> findAllByOffer_Id(Long offerId);
}

