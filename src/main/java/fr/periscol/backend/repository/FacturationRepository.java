package fr.periscol.backend.repository;

import fr.periscol.backend.domain.Facturation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Facturation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturationRepository extends JpaRepository<Facturation, Long> {}
