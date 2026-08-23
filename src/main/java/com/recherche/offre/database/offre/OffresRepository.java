package com.recherche.offre.database.offre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffresRepository extends JpaRepository<OffreSauvegardeeEntity, Long> {

}
