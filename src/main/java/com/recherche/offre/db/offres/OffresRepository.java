package com.recherche.offre.db.offres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffresRepository extends JpaRepository<OffresEntity, Long> {

    List<OffresEntity> findAllByDescriptionContains(String query);

}
