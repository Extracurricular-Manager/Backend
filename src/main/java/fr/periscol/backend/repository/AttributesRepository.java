package fr.periscol.backend.repository;

import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.domain.tarification.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Attributes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributesRepository extends JpaRepository<Attributes, Long> {}
